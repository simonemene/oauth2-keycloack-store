package com.store.security.store_security.security;

import com.store.security.store_security.exceptionhandle.CustomAccessDeniedHandler;
import com.store.security.store_security.filter.JwtValidatorFilter;
import com.store.security.store_security.properties.StoreProperties;
import com.store.security.store_security.provider.UserProviderDetailsManager;
import com.store.security.store_security.service.UserSecurityDetailService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.password.CompromisedPasswordChecker;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.logout.HttpStatusReturningLogoutSuccessHandler;
import org.springframework.security.web.authentication.password.HaveIBeenPwnedRestApiPasswordChecker;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.security.web.session.HttpSessionEventPublisher;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;

import java.util.List;

@Slf4j
@Configuration
@AllArgsConstructor
@Profile("dev")
public class ConfigSecurityDev {

    private final StoreProperties storeProperties;

    /**
     * Bean responsible for customizable Spring Security configurations
     * and for defining the security filter chain.
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {


        http.csrf(AbstractHttpConfigurer::disable);
        http.sessionManagement(session->session.sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED));
        http.authorizeHttpRequests(auth ->
                        auth.requestMatchers("/api/article/addArticle",
                                        "/api/article/addArticle/**",
                                        "/api/article/deleteArticle/{id}",
                                        "/api/article/decrementArticle",
                                        "/api/v1/articles/**",
                                        "/api/v1/articles").hasRole("ADMIN")
                                .requestMatchers("/api/user/{username}",
                                        "/api/user/{id}",
                                        "/api/v1/stock",
                                        "/api/v1/stock/**","/api/auth/user").hasAnyRole("USER","ADMIN")
                                .requestMatchers(
                                        "/api/user").hasAnyRole("ADMIN","TRACK")
                                .requestMatchers(
                                        "/api/v1/track/{idOrder}","/api/orders","/api/orders/{username}").hasAnyRole("USER","ADMIN","TRACK")
                                .requestMatchers("/api/auth/user").authenticated()
                                .requestMatchers("/api/auth/registration","api/auth/login",
                                       "/v3/api-docs",
                                       "/h2-console/**",
                                       "/v3/api-docs/**",
                                       "/swagger-ui/**",
                                       "/swagger-ui.html",
                                       "/swagger-ui/index.html",
                                        "/api/auth/logout"
                                       ).permitAll());
        //set custom filter
        http.addFilterBefore(new JwtValidatorFilter(storeProperties),BasicAuthenticationFilter.class);
        http.cors(cors->cors.configurationSource(
                new CorsConfigurationSource() {
                    @Override
                    public CorsConfiguration getCorsConfiguration(
                            HttpServletRequest request) {
                        CorsConfiguration cors = new CorsConfiguration();
                        cors.addAllowedOrigin(storeProperties.securityAllowedOrigin());
                        cors.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE","PATCH"));
                        cors.setAllowCredentials(true);
                        cors.setAllowedHeaders(List.of("*"));
                        cors.setExposedHeaders(List.of("Authorization"));
                        cors.setMaxAge(3600L);
                        return cors;
                    }
                }
        ));

        http.logout(logout->
                logout.deleteCookies("JSESSIONID","CSRF-TOKEN")
                        .logoutUrl("/api/auth/logout")
                        .invalidateHttpSession(true).permitAll()
                        .logoutSuccessHandler(new HttpStatusReturningLogoutSuccessHandler(HttpStatus.OK))
        );

        //authentication
        http.formLogin(AbstractHttpConfigurer::disable);
        http.httpBasic(AbstractHttpConfigurer::disable);
        http.exceptionHandling(exception->exception.accessDeniedHandler(new CustomAccessDeniedHandler()));
        return http.build();

    }

    /**
     * Bean responsible for verifying whether a password has been exposed
     * in a known data breach.
     */
    @Bean
    public CompromisedPasswordChecker compromisedPasswordChecker() {
        return new HaveIBeenPwnedRestApiPasswordChecker();
    }

    /**
     * Bean for handling password hashing operations.
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    @Bean
    public static HttpSessionEventPublisher httpSessionEventPublisher() {
        return new HttpSessionEventPublisher();
    }

    @Bean
    public AuthenticationManager authenticationManager(
            UserSecurityDetailService userDetailsService,PasswordEncoder passwordEncoder)
    {
        UserProviderDetailsManager userProviderDetailsManager = new UserProviderDetailsManager(userDetailsService,passwordEncoder);
        return new ProviderManager(userProviderDetailsManager);
    }

}
