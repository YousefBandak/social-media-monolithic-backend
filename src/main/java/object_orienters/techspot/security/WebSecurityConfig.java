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
                .cors(withDefaults())

                .sessionManagement(s -> s.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .exceptionHandling(exception -> exception.authenticationEntryPoint(unauthorizedHandler))
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("/auth/**").permitAll()
                        .requestMatchers("/chat/**").permitAll()
                        .requestMatchers("/messages/**").permitAll()
                        .requestMatchers("/**").permitAll()
                        .requestMatchers("/media_uploads/**").permitAll()
                        .requestMatchers("/swagger-ui.html").permitAll()
                        .requestMatchers("/swagger-ui/**").permitAll()
                        .requestMatchers("/api-docs/**").permitAll()
                        .requestMatchers("/oauth2/callback/google").permitAll()
                        .requestMatchers("/login/oauth2/code/github").permitAll()
                        .anyRequest().authenticated()
                )
//                .oauth2Login(withDefaults())
                .logout(l -> l.logoutUrl("/auth/logout")
                        // .logoutSuccessUrl("/auth/login")
                        .permitAll()
                        .deleteCookies("JSESSIONID")
                        .invalidateHttpSession(true))

                .oauth2Login(
                        oauth2 -> oauth2
                                .loginPage("http://localhost:3000/login")
                                .authorizationEndpoint(authorization -> authorization
                                        .baseUri("/oauth2/authorize")
                                )
                                .redirectionEndpoint(redirection -> redirection
                                        .baseUri("http://localhost:8080/oauth2/callback/google")
                                )
                                .redirectionEndpoint(redirection -> redirection
                                        .baseUri("http://localhost:8080/login/oauth2/code/github")
                                )

                )
                .authenticationProvider(authenticationProvider())
                .addFilterBefore(authenticationJwtTokenFilter(), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    // @Override
    // public void addCorsMappings(CorsRegistry registry) {
    // registry.addMapping("/**")
    // .allowedOrigins("")
    // .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
    // .allowedHeaders("*")
    // .allowCredentials(true);
    // }

    Logger logger = org.slf4j.LoggerFactory.getLogger(WebSecurityConfig.class);
}
