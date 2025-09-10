package org.example.civic_govt.security;

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
                                .requestMatchers("/api/auth/authenticate/**").permitAll() // Sign-up and sign-in are public
                                .requestMatchers(HttpMethod.POST, "/api/auth/issues/create/**").hasAuthority("CITIZEN") // Citizens report issues
                                .requestMatchers(HttpMethod.GET, "/api/auth/issues/fetch-all").authenticated() // Anyone logged in can see all issues
                                .requestMatchers("/api/auth/admin/**").hasAuthority("ADMIN") // Only admins can access the admin portal
                                .requestMatchers("/api/auth/issues/*/assign/*").hasAuthority("ADMIN") // Only officials can assign issues
                                .requestMatchers("/api/auth/issues/*/status/*").hasAuthority("OFFICIAL") // Only officials can update issue status
                                .requestMatchers("/api/auth/issues/*/comments/*").hasAuthority("CITIZEN") // Corrected pattern for comments
                                .requestMatchers("/api/auth/issues/*/votes/*").hasAuthority("CITIZEN") // Corrected pattern for votes
                                .requestMatchers("/api/auth/users/**").hasAnyAuthority("CITIZEN", "OFFICIAL", "ADMIN") // Corrected and broadened access
                                .anyRequest().authenticated()
                );
        http.authenticationProvider(authenticationProvider());
        http.addFilterBefore(authenticationJwtTokenFilter(), UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }
}