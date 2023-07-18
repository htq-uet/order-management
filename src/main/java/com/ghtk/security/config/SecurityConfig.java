package com.ghtk.security.config;

import com.ghtk.security.filter.JwtAuthFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutHandler;

import static com.ghtk.model.Permission.*;
import static com.ghtk.model.Role.*;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    @Autowired
    private final JwtAuthFilter jwtAuthFilter;

    @Autowired
    private final AuthenticationProvider authenticationProvider;

    @Autowired
    private final LogoutHandler logoutHandler;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(requests -> requests
                        .requestMatchers("/auth/**").permitAll()
                )
                .authorizeHttpRequests(requests -> requests
                        .requestMatchers("/shop/**").hasRole(SHOP.name())
                        .requestMatchers(HttpMethod.GET, "/shop/**")
                            .hasAuthority(SHOP_READ.name())
                        .requestMatchers(HttpMethod.POST, "/shop/**")
                            .hasAuthority(SHOP_CREATE.name())
                        .requestMatchers(HttpMethod.PUT, "/shop/**")
                            .hasAuthority(SHOP_UPDATE.name())
                        .requestMatchers(HttpMethod.DELETE, "/shop/**")
                            .hasAuthority(SHOP_DELETE.name())

                        .requestMatchers(("/staff/**")).hasAnyRole(STAFF.name(), SHOP.name())
                        .requestMatchers(HttpMethod.GET, "/staff/**")
                            .hasAnyAuthority(STAFF_READ.name(), SHOP_READ.name())
                        .requestMatchers(HttpMethod.POST, "/staff/**")
                            .hasAnyAuthority(STAFF_CREATE.name(), SHOP_CREATE.name())
                        .requestMatchers(HttpMethod.PUT, "/staff/**")
                            .hasAnyAuthority(STAFF_UPDATE.name(), SHOP_UPDATE.name())
                        .requestMatchers(HttpMethod.DELETE, "/staff/**")
                            .hasAnyAuthority(STAFF_DELETE.name(), SHOP_DELETE.name())

                )
                .authorizeHttpRequests(requests -> requests
                        .anyRequest().authenticated()
                )
                .sessionManagement(sessionManagement -> sessionManagement
                        .sessionFixation().changeSessionId()
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .authenticationProvider(authenticationProvider)
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .addLogoutHandler(logoutHandler)
                        .logoutSuccessHandler(
                                (request, response, authentication) ->
                                SecurityContextHolder.clearContext()

                        )
                );
        return http.build();
    }

}
