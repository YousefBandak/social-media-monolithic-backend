package object_orienters.techspot.security;

import object_orienters.techspot.security.jwt.AuthEntryPointJwt;
import object_orienters.techspot.security.jwt.AuthTokenFilter;
import object_orienters.techspot.security.service.ImpleUserDetailsService;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class WebSecurityConfig implements WebMvcConfigurer {
    @Autowired
    ImpleUserDetailsService userDetailsService;

    @Autowired
    private AuthEntryPointJwt unauthorizedHandler;

    @Bean
    public AuthTokenFilter authenticationJwtTokenFilter() {
        return new AuthTokenFilter();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();

        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());

        return authProvider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf(c -> c.disable())
                .cors(c -> c.disable()) // TODO
                .sessionManagement(s -> s.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .exceptionHandling(exception -> exception.authenticationEntryPoint(unauthorizedHandler))
                .authorizeHttpRequests(authorize -> authorize
                        // .requestMatchers("/login", "/auth/login", "auth/signup", "auth/refreshtoken",
                        // "auth/usernameExists/**")
                        .requestMatchers("/**").permitAll()
                        .requestMatchers("/swagger-ui.html").permitAll()
                        .requestMatchers("/swagger-ui/**").permitAll()
                        .requestMatchers("/api-docs/**").permitAll()

                // .anyRequest().authenticated()
                )
                .oauth2Login(withDefaults()).logout(l -> l.logoutUrl("auth/logout")
                        .logoutSuccessUrl("auth/login").permitAll().deleteCookies("JSESSIONID")
                        .invalidateHttpSession(true))
                .formLogin(form -> {
                    form.permitAll();
                    form.failureHandler((request, response, exception) -> {
                        logger.error("Failed to login", exception);

                        exception.printStackTrace();
                        response.sendRedirect("/auth/signup");
                    });
                    form.successForwardUrl("/auth/home");
                })
                .authenticationProvider(authenticationProvider())
                .logout(logout -> logout.logoutSuccessUrl("/auth/login"))
                .addFilterBefore(authenticationJwtTokenFilter(), UsernamePasswordAuthenticationFilter.class)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        return http.build();
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        // Setting up CORS for /profiles/ endpoint
        registry.addMapping("/feed/**") // Applies CORS to all subpaths under /profiles/
                .allowedOrigins("http://localhost:3000") // Only allow requests from localhost:3000
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS") // Allowed HTTP methods from the origin
                .allowedHeaders("*") // Allow all headers
                .allowCredentials(true); // Allow credentials like cookies, authorization headers, etc.
    }

    Logger logger = org.slf4j.LoggerFactory.getLogger(WebSecurityConfig.class);
}
