package com.project.userservice.filter;

import com.project.userservice.constants.ApplicationConstants;
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
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;
import lombok.extern.slf4j.Slf4j;

import javax.crypto.SecretKey;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.project.userservice.constants.SecurityConstants.*;

@Slf4j
public class JWTTokenValidatorFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String jwt = null;
        try{
            jwt = request.getHeader(AUTH_HEADER).split(" ")[1].trim();
        } catch (Exception e){
            log.info("Exception occured during fetching the JWT!");
        }
        if (null != jwt){
            try {
                SecretKey key = Keys.hmacShaKeyFor(JWT_KEY.getBytes(StandardCharsets.UTF_8));

                Claims claims = Jwts.parserBuilder()
                        .setSigningKey(key)
                        .build()
                        .parseClaimsJws(jwt)
                        .getBody();

                String username = String.valueOf(claims.get(CLAIM_USERNAME));
                List<SimpleGrantedAuthority> authorities = extractAuthorities(claims);

                Authentication auth
                        = new UsernamePasswordAuthenticationToken(username, null, authorities);

                SecurityContextHolder.getContext().setAuthentication(auth);
            }catch (Exception e){
                throw new BadCredentialsException("Invalid Token received!");
            }
        }
        filterChain.doFilter(request, response);
    }

    private static List<SimpleGrantedAuthority> extractAuthorities(Claims claims) {
        List<SimpleGrantedAuthority> authorities = new ArrayList<>();
        String[] roleArr = String.valueOf(claims.get(CLAIM_ROLE)).split(",");
        Arrays.asList(roleArr).forEach(str -> authorities.add(new SimpleGrantedAuthority(str)));
        return authorities;
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        return ApplicationConstants.PERMITTED_ENDPOINTS.contains(request.getServletPath());
    }
}
