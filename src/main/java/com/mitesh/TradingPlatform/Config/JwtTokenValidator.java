package com.mitesh.TradingPlatform.Config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.crypto.SecretKey;
import java.io.IOException;
import java.util.List;

public class JwtTokenValidator extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String jwt = request.getHeader(JwtConstant.JWT_HEADER);

        if (jwt != null && jwt.startsWith("Bearer ")) {
            jwt = jwt.substring(7); // Remove "Bearer " prefix

            try {
                SecretKey key = Keys.hmacShaKeyFor(JwtConstant.SECRET_KEY.getBytes());

                Claims claims = Jwts.parser()
                        .setSigningKey(key)
                        .build()
                        .parseClaimsJws(jwt)
                        .getBody();

                // You can also extract username/roles etc. here from claims
                // String username = claims.getSubject();

                // Optionally, set the authentication in SecurityContextHolder
                // if using Spring Security context
                String email=String.valueOf(claims.get("email"));
                String authorities= String.valueOf(claims.get("authorities"));
                List<GrantedAuthority> authoritiesList= AuthorityUtils.commaSeparatedStringToAuthorityList(authorities);
                Authentication auth=new UsernamePasswordAuthenticationToken(
                        email,
                        null,
                        authoritiesList
                );

                SecurityContextHolder.getContext().setAuthentication(auth);
            } catch (Exception e) {
                throw new RuntimeException("Invalid Token..!");
            }
        }
        filterChain.doFilter(request,response);
    }
}

