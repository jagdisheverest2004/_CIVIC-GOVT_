package org.example.civic_govt.security;

import org.example.civic_govt.model.User;
import org.example.civic_govt.security.jwt.AuthEntryPointJwt;
import org.example.civic_govt.security.jwt.AuthTokenFilter;
import org.example.civic_govt.security.services.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {

    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    @Autowired
    private AuthEntryPointJwt unauthorizedHandler;

    @Bean
    public AuthTokenFilter authenticationJwtTokenFilter() {
        return new AuthTokenFilter();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(userDetailsService);
        authenticationProvider.setPasswordEncoder(passwordEncoder());
        return authenticationProvider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfig) throws Exception {
        return authenticationConfig.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.cors(withDefaults())
                .csrf(AbstractHttpConfigurer::disable)
                .exceptionHandling(exception -> exception.authenticationEntryPoint(unauthorizedHandler))
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(authorizeRequests ->
                        authorizeRequests
                                .requestMatchers("/api/auth/authenticate/signup").permitAll()
                                .requestMatchers("/api/auth/authenticate/signin").permitAll()
                                .requestMatchers("/api/auth/authenticate/signout").authenticated()
                                .requestMatchers(HttpMethod.POST, "/api/auth/issues/create-issue").hasAuthority("CITIZEN")
                                .requestMatchers(HttpMethod.GET, "/api/auth/issues/fetch-all").authenticated()
                                .requestMatchers(HttpMethod.GET, "/api/auth/issues/departments/*/issues").authenticated()
                                .requestMatchers("/api/auth/admin/**").hasAuthority("ADMIN")
                                .requestMatchers("/api/auth/issues/*/assign/*").hasAuthority("ZONE_HEAD")
                                .requestMatchers("/api/auth/issues/*/status/*").hasAuthority("SUBORDINATE")
                                .requestMatchers("/api/auth/issues/*/comments/*").hasAuthority("CITIZEN")
                                .requestMatchers("/api/auth/issues/*/votes/*").hasAuthority("CITIZEN")
                                .requestMatchers("/api/auth/users/**").authenticated()
                                .requestMatchers("/api/auth/account/**").hasAnyAuthority("ADMIN","ZONE_HEAD","DEPT_HEAD","DISTRICT_HEAD")
                                .anyRequest().authenticated()
                );
        http.authenticationProvider(authenticationProvider());
        http.addFilterBefore(authenticationJwtTokenFilter(), UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }
}