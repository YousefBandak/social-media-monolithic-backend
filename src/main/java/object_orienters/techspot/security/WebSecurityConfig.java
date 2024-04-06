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
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableMethodSecurity
public class WebSecurityConfig { // extends WebSecurityConfigurerAdapter {
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
        // return http
        // .csrf(csrf -> csrf.disable())
        // .exceptionHandling(exception ->
        // exception.authenticationEntryPoint(unauthorizedHandler))
        // .sessionManagement(session ->
        // session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
        // // Enable OAuth2 login with default settings
        // .formLogin(formLogin -> {
        // formLogin.loginPage("/customLogin") // Set custom login page URL
        // .failureUrl("/customFailure"); // Set custom failure URL
        // })
        // .oauth2Login(withDefaults())// Enable form-based login with default settings
        // .authorizeHttpRequests(auth -> {
        // auth.requestMatchers("/customLogin").permitAll();
        // auth.requestMatchers("/customFailure").permitAll();
        // auth.requestMatchers("/auth/login").permitAll();
        // auth.requestMatchers("/auth/signup").permitAll();
        // auth.requestMatchers("/favicon.ico").permitAll();
        // auth.requestMatchers("/auth/**").permitAll();
        // //auth.requestMatchers("/login/**").permitAll();
        // //auth.requestMatchers("/").permitAll();
        // // auth.anyRequest().authenticated();
        // })
        //
        //
        // .authenticationProvider(authenticationProvider()) // Register custom
        // authentication provider
        // .addFilterBefore(authenticationJwtTokenFilter(),
        // UsernamePasswordAuthenticationFilter.class) // Add JWT token filter
        // .build();

        ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////

        // http
        // .csrf(csrf -> csrf.disable()) // If it's a REST API, you're not using CSRF
        // protection.
        // .authorizeHttpRequests(authorize -> authorize
        // .requestMatchers("/login", "/auth/**").permitAll() // Allow public access to
        // these endpoints
        // .anyRequest().authenticated()) // All other requests must be authenticated.
        // .oauth2Login(withDefaults())
        // .formLogin(form -> {
        // form.permitAll();
        // //form.failureUrl("/auth/signup");
        // form.failureHandler((request, response, exception) -> {
        // logger.error("Failed to login", exception);
        // response.sendRedirect("/auth/signup");
        // });
        // form.successForwardUrl("/auth/a");
        // logger.info("Form login enabled");
        // logger.info(form.isCustomLoginPage()+"");
        //
        // })
        // ;
        // // Other configurations...
        // return http.build();

        //////////////////////////////////////////////////////////////////////////////////////////////////////////
        // code with both
        http
                .csrf(c -> c.disable())
                // .csrf(c -> c
                //         .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse()))
                .sessionManagement(s-> s.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .exceptionHandling(exception -> exception.authenticationEntryPoint(unauthorizedHandler))
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("/login", "/auth/login", "auth/signup", "auth/refreshtoken",
                                "auth/usernameExists/**")
                        .permitAll()
                        .anyRequest().authenticated())
                .oauth2Login(withDefaults()).logout(l -> l.logoutUrl("auth/logout")
                        .logoutSuccessUrl("auth/login").permitAll().deleteCookies("JSESSIONID").invalidateHttpSession(true))
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

    Logger logger = org.slf4j.LoggerFactory.getLogger(WebSecurityConfig.class);
}
