package ru.ssau.todo.filters;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.jspecify.annotations.NullMarked;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;
import ru.ssau.todo.ExceptionHandler.InvalidTokenException;
import ru.ssau.todo.service.CustomGrantedAuthority;
import ru.ssau.todo.service.CustomUserDetails;
import ru.ssau.todo.service.CustomUserDetailsService;
import ru.ssau.todo.service.TokenService;
import tools.jackson.core.type.TypeReference;
import tools.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.lang.reflect.Type;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.Date;
import java.util.Map;
import java.util.Objects;

@Component
public class JwtFilter extends OncePerRequestFilter {
    public static final String BEARER_PREFIX = "Bearer ";
    public static final String HEADER_NAME = "Authorization";
    private final TokenService tokenService;
    private final CustomUserDetailsService customUserDetailsService;

    public JwtFilter(TokenService tokenService, CustomUserDetailsService customUserDetailsService) {
        this.tokenService = tokenService;
        this.customUserDetailsService = customUserDetailsService;
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        String path = request.getRequestURI();
        return path.startsWith("/auth/login") || path.startsWith("/auth/refresh") || path.startsWith("/users/register");
    }


    @Override
    @NullMarked
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String authHeader = request.getHeader(HEADER_NAME);
        if (!authHeader.startsWith(BEARER_PREFIX)) {
            filterChain.doFilter(request, response);
        }
        String jwt = authHeader.substring(BEARER_PREFIX.length());
        String[] token = jwt.split("\\.", 2);
        String payloadPart = token[0];
        String signature = token[1];
        String encodingSignature;
        try {
            encodingSignature = tokenService.encodingSignature(payloadPart);
        } catch (NoSuchAlgorithmException | InvalidKeyException e) {
            throw new RuntimeException(e);
        }
        if (!signature.equals(encodingSignature)){
            throw new InvalidTokenException("Invalid token");
        }
        byte[] decoded =
                Base64.getUrlDecoder().decode(payloadPart);
        ObjectMapper mapper = new ObjectMapper();
        Map<String, Object> payload = mapper.readValue(decoded, new TypeReference<Map<String, Object>>() {});
        long exp = Long.parseLong(payload.get("exp").toString());
        if(exp < System.currentTimeMillis()){
            throw new InvalidTokenException("The token's validity period has finally disappeared");
        }
        long userId = Long.parseLong(payload.get("userId").toString());
        CustomUserDetails userDetails = customUserDetailsService.loadUserByUserId(userId);
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(userDetails,null, userDetails.getAuthorities());
        context.setAuthentication(authToken);
        SecurityContextHolder.setContext(context);
        filterChain.doFilter(request, response);
    }
}
