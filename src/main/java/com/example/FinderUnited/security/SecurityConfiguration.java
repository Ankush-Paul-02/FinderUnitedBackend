package com.example.FinderUnited.security;

import com.example.FinderUnited.business.service.exceptions.AuthenticationEntryPointException;
import com.example.FinderUnited.business.service.exceptions.UnauthorizedException;
import com.example.FinderUnited.data.enums.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfiguration {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final AuthenticationProvider authenticationProvider;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(
                        request -> request
                                .requestMatchers(
                                        "/auth/**",
                                        "/test/**"
                                )
                                .permitAll()
                                .requestMatchers("/success/**").hasAnyAuthority(Role.ADMIN.name(), Role.USER.name())
                                .requestMatchers("/admin/**").hasAnyAuthority(Role.ADMIN.name())
                                .requestMatchers("/user/**").hasAnyAuthority(Role.USER.name())
                                .anyRequest()
                                .authenticated()
                )
                .exceptionHandling(
                        ex -> ex
                                .authenticationEntryPoint(
                                        (request, response, authException) -> {
                                            throw new AuthenticationEntryPointException(authException.getMessage());
                                        }
                                )
                                .accessDeniedHandler(
                                        (request, response, accessDeniedException) -> {
                                            throw new UnauthorizedException(accessDeniedException.getMessage());
                                        }
                                )
                )
                .sessionManagement(manager -> manager.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authenticationProvider(authenticationProvider)
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return httpSecurity.build();
    }
}
