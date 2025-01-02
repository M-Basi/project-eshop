package gr.eshop.marios.EshopApp.security;


import gr.eshop.marios.EshopApp.authentication.JwtAuthenticationFilter;
import gr.eshop.marios.EshopApp.core.enums.Role;
import io.swagger.v3.oas.models.info.Info;

import io.swagger.v3.oas.models.OpenAPI;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfiguration {

    private final UserDetailsService userDetailsService;
    private final JwtAuthenticationFilter jwtAuthFilter;

    /**
     * Configures the security filter chain for HTTP requests.
     *
     * @param http the HttpSecurity object to configure
     * @return the configured SecurityFilterChain
     * @throws Exception if an error occurs during configuration
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .cors(httpSecurityCorsConfigurer -> httpSecurityCorsConfigurer.configurationSource(corsConfigurationSource()))
                .csrf(AbstractHttpConfigurer::disable)
                .exceptionHandling(exceptions -> exceptions
                        .authenticationEntryPoint(myCustomAuthenticationEntryPoint())
                        .accessDeniedHandler(myCustomAccessDeniedHandler())
                )
                .authorizeHttpRequests(req -> req
//                        .anyRequest().permitAll()
                        .requestMatchers("/uploads/**").permitAll()
                        .requestMatchers("/swagger-ui/**",
                                "/v3/api-docs/**",
                                "/swagger-resources/**",
                                "/webjars/**",
                                "/swagger-ui.html").permitAll()
                        .requestMatchers("/api/brands").permitAll()
                        .requestMatchers("/api/categories").permitAll()
                        .requestMatchers("/api/regions").permitAll()
                        .requestMatchers("/api/auth/authenticate").permitAll()
                        .requestMatchers("/api/auth/refreshToken").permitAll()
                        .requestMatchers("/favicon.ico").permitAll()
                        .requestMatchers("/api/users/register").permitAll()
                        .requestMatchers("/api/users/checkEmail").permitAll()
                        .requestMatchers("/api/users/all").hasAnyAuthority(Role.ADMIN_USER.name())
                        .requestMatchers("/api/users/allPaginated").hasAnyAuthority(Role.ADMIN_USER.name())
                        .requestMatchers("/api/users/user/**").hasAnyAuthority(Role.CUSTOMER_USER.name(), Role.ADMIN_USER.name())
                        .requestMatchers("/api/customers/all").hasAnyAuthority(Role.ADMIN_USER.name())
                        .requestMatchers("/api/customers/allPaginated").hasAnyAuthority(Role.ADMIN_USER.name())
                        .requestMatchers("/api/customers/customer/**").hasAnyAuthority(Role.CUSTOMER_USER.name(), Role.ADMIN_USER.name())
                        .requestMatchers("/api/customerInfo/**").hasAnyAuthority(Role.CUSTOMER_USER.name())
                        .requestMatchers("/api/paymentInfo/**").hasAnyAuthority(Role.CUSTOMER_USER.name())
                        .requestMatchers("/api/orders/**").hasAnyAuthority(Role.CUSTOMER_USER.name())
                        .requestMatchers("/api/product/**").hasAnyAuthority(Role.ADMIN_USER.name())
                        .requestMatchers("/api/products/**").permitAll()
                        .requestMatchers("/**").permitAll()
                )
                .sessionManagement((session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)))
                .authenticationProvider(authenticationProvider())
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    /**
     * Configures CORS for the application.
     *
     * @return the CorsConfigurationSource containing the configured CORS settings
     */
    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration corsConfiguration = new CorsConfiguration();
        corsConfiguration.setAllowedOrigins(List.of(
                "http://localhost:4200",
                "http://localhost:5173",
                "http://localhost:8080"));
        corsConfiguration.setAllowedMethods(List.of("*"));
        corsConfiguration.setAllowedHeaders(List.of("*"));
        corsConfiguration.setAllowCredentials(true);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", corsConfiguration);
        source.registerCorsConfiguration("/swagger-ui", corsConfiguration);
        source.registerCorsConfiguration("/v3/api-docs", corsConfiguration);
        source.registerCorsConfiguration("/swagger-ui/**", corsConfiguration);
        source.registerCorsConfiguration("/v3/api-docs/**", corsConfiguration);
        return source;

    }

    /**
     * Configures the authentication provider with user details service and password encoder.
     *
     * @return the configured AuthenticationProvider
     */
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    /**
     * Configures the password encoder for the application.
     *
     * @return the configured PasswordEncoder
     */
    @Bean
    public PasswordEncoder passwordEncoder() {

        return new BCryptPasswordEncoder(11);
    }


    /**
     * Configures the authentication manager for the application.
     *
     * @param config the AuthenticationConfiguration
     * @return the configured AuthenticationManager
     * @throws Exception if an error occurs during configuration
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config)
            throws Exception{
        return config.getAuthenticationManager();
    }

    /**
     * Provides a custom authentication entry point for handling unauthorized access.
     *
     * @return the custom AuthenticationEntryPoint
     */
    @Bean
    public AuthenticationEntryPoint myCustomAuthenticationEntryPoint() {
        return new CustomAuthenticationEntryPoint();
    }

    /**
     * Provides a custom access denied handler for handling forbidden access.
     *
     * @return the custom AccessDeniedHandler
     */
    @Bean
    public AccessDeniedHandler myCustomAccessDeniedHandler() {
        return new CustomAccessDeniedHandler();
    }


}
