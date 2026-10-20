package com.example.user_service.config;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.example.user_service.service.CustomUserDetailsService;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component 
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    @Autowired 
    private JwtService jwtService;
    @Autowired 
    private CustomUserDetailsService userDetailsService;

    @Override 
    public void doFilterInternal(HttpServletRequest request,HttpServletResponse response,FilterChain filterChain) throws ServletException,IOException{
        final String authHeaders=request.getHeader("Authorization");

        if(authHeaders==null || !authHeaders.startsWith("Bearer ")){
            filterChain.doFilter(request, response);
            return ;
        }

        String token=authHeaders.substring(7);

        String user_name=jwtService.getClaims(token).getSubject();

        if(user_name!=null && SecurityContextHolder.getContext().getAuthentication()==null){
            UserDetails userDetails=userDetailsService.loadUserByUsername(user_name);
            if(jwtService.isTokenValid(userDetails, token)){
                UsernamePasswordAuthenticationToken authenticationToken=new UsernamePasswordAuthenticationToken(
                    user_name, userDetails.getAuthorities()
                );
                
                authenticationToken.setDetails(
                    new WebAuthenticationDetailsSource()
                    .buildDetails(request)
                );

                SecurityContextHolder.getContext()
                .setAuthentication(authenticationToken);

            }
        }
        filterChain.doFilter(request, response);

    }
}
