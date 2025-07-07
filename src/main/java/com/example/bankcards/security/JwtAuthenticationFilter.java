package com.example.bankcards.security;

import com.example.bankcards.model.Role;
import com.example.bankcards.model.RoleType;
import com.example.bankcards.service.security.JwtTokenService;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingRequestWrapper;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import static com.example.bankcards.service.security.JwtTokenService.ID_CLAIM;
import static com.example.bankcards.service.security.JwtTokenService.ROLES_CLAIM;

@Component
@Slf4j
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtTokenService jwtTokenService;
    private final String BEARER_PREFIX = "Bearer ";

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        ContentCachingRequestWrapper requestWrapper = new ContentCachingRequestWrapper(request);
        Authentication authentication = convert(requestWrapper);
        if (authentication != null) {
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }
        filterChain.doFilter(request, response);

    }

    public Authentication convert(HttpServletRequest request){
        String token = extractBearerToken(request);
        return (token != null && jwtTokenService.validate(token)) ?
                toAuthentication(token) : null;
    }

    private String extractBearerToken(HttpServletRequest request){
        String authorizationHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        return (authorizationHeader != null && authorizationHeader.startsWith(BEARER_PREFIX)) ?
                authorizationHeader.substring(BEARER_PREFIX.length()) : null;
    }

    public Authentication toAuthentication(String token) {
        Claims claims = jwtTokenService.parseTokenClaims(token);
        String phoneNumber = claims.getSubject();
        String id = claims.get(ID_CLAIM, String.class);
        List<String> roleTypeList = claims.get(ROLES_CLAIM,List.class);
        List<Role> roles = roleTypeList.stream()
                .map(roleType -> Role.builder().roleType(RoleType.valueOf(roleType))
                        .build()).toList();
        jwtTokenService.validateClaims(phoneNumber, id, roles);
        AppUserPrincipal principal = new AppUserPrincipal(UUID.fromString(id),phoneNumber,roles.stream().map(Role::getRoleType).toList());
        return new UsernamePasswordAuthenticationToken(
                principal,
                null,
                roles.stream().map(Role::grantedAuthority).toList());
    }

}
