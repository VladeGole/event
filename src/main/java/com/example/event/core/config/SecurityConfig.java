package com.example.event.core.config;

import com.example.event.model.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class SecurityConfig {

    private final JwtAuthentacationFilter jwtFilter;
    private final AuthenticationProvider authenticationProvider;

    public SecurityConfig(JwtAuthentacationFilter jwtFilter, AuthenticationProvider authenticationProvider) {
        this.jwtFilter = jwtFilter;
        this.authenticationProvider = authenticationProvider;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity.csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth-> auth.requestMatchers(HttpMethod.GET,"/api/v1/**")
                        .permitAll()
                        .requestMatchers("/api/v1/auth/**")
                        .permitAll()
                        .requestMatchers(
                                        "/v3/api-docs/**",
                                        "/swagger-ui/**",
                                        "/swagger-ui.html"
                        ).permitAll()
                        .requestMatchers(HttpMethod.POST,"/api/v1/countries/**").hasAuthority(Role.ADMINISTRATOR.name())
                        .requestMatchers(HttpMethod.PATCH,"/api/v1/countries/**").hasAuthority(Role.ADMINISTRATOR.name())
                        .requestMatchers(HttpMethod.DELETE,"/api/v1/countries/**").hasAuthority(Role.ADMINISTRATOR.name())
                        .requestMatchers(HttpMethod.POST,"/api/v1/participants/**").hasAuthority(Role.PARTICIPANT.name())
                        .requestMatchers(HttpMethod.PATCH,"/api/v1/participants/**").hasAuthority(Role.PARTICIPANT.name())
                        .requestMatchers(HttpMethod.DELETE,"/api/v1/participants/**").hasAuthority(Role.PARTICIPANT.name())
                        .requestMatchers(HttpMethod.POST,"/api/v1/events/**").hasAuthority(Role.ADMIN.name())
                        .requestMatchers(HttpMethod.PATCH,"/api/v1/events/**").hasAuthority(Role.ADMIN.name())
                        .requestMatchers(HttpMethod.DELETE,"/api/v1/events/**").hasAuthority(Role.ADMIN.name())
                        .anyRequest()
                        .authenticated()
                )
                .sessionManagement(session-> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authenticationProvider(authenticationProvider)
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
                .build();

    }
}
