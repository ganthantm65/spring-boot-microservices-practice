package com.example.user_service.config;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import com.example.user_service.service.CustomUserDetailsService;

@Configuration
public class SecurityConfig {
    @Autowired 
    private final CorsConfigurationSource configurationSource;
    @Autowired 
    private final CustomUserDetailsService userDetailsService;
    @Autowired 
    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    public SecurityConfig(
            CustomUserDetailsService userDetailsService,
            JwtAuthenticationFilter jwtAuthenticationFilter, CorsConfigurationSource configurationSource) {
        this.userDetailsService = userDetailsService;
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
        this.configurationSource = configurationSource;
    }

    
    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetailsService);
        provider.setPasswordEncoder(passwordEncoder());
        return provider;
    }

    @Bean 
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean 
    public AuthenticationManager authenticationManage(){
        return new ProviderManager(authenticationProvider());
    }

    @Bean 
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception{
        http
            .cors(cors->cors.configurationSource(configurationSource))
            .csrf(csrf->csrf.disable())
            .sessionManagement(session->session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests(auth->auth
                .requestMatchers(
                    "/auth/admin/login",
                    "/auth/admin/register",
                    "/auth/user/login",
                    "/auth/user/register"
                ).permitAll()
                .requestMatchers(HttpMethod.OPTIONS,"/**").permitAll()
                .anyRequest().authenticated()
            )
            .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
        
        return http.build();
            
    }

    @Bean 
    public CorsConfigurationSource configurationSource(){
        CorsConfiguration corsCon=new CorsConfiguration();

        corsCon.addAllowedOrigin("http://localhost:5173");
        corsCon.addAllowedHeader("*");
        corsCon.addAllowedMethod("GET");
        corsCon.addAllowedMethod("POST");
        corsCon.addAllowedMethod("PUT");
        corsCon.addAllowedMethod("DELETE");
        corsCon.addAllowedMethod("OPTIONS");
        corsCon.setAllowCredentials(true);
        corsCon.setExposedHeaders(List.of("Authorization"));

        UrlBasedCorsConfigurationSource corsConfigurationSource=new UrlBasedCorsConfigurationSource();

        corsConfigurationSource.registerCorsConfiguration("/**",corsCon);

        return corsConfigurationSource;
    }
}