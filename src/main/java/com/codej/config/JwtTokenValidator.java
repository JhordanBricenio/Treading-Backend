package com.codej.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.crypto.SecretKey;
import java.io.IOException;
import java.util.List;

public class JwtTokenValidator extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String jwt= request.getHeader(JwtConstant.JWT_TOKEN_HEADER);
        if(jwt!=null){
            jwt= jwt.substring(7);
            try{
                SecretKey key= Keys.hmacShaKeyFor(JwtConstant.SECRET_KEY.getBytes());
                Claims claims= Jwts.parser().setSigningKey(key).parseClaimsJws(jwt).getBody();
                String email= claims.get("email", String.class);
                String authorities= claims.get("authorities", String.class);

                List<GrantedAuthority> authorityList = List.of(authorities.split(",")).stream().map(s -> (GrantedAuthority) () -> s).toList();

                Authentication authentication = new UsernamePasswordAuthenticationToken(email, null, authorityList);
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }catch (Exception e){
                throw  new BadCredentialsException("Invalid token");
            }

        }
        filterChain.doFilter(request, response);
    }
}
