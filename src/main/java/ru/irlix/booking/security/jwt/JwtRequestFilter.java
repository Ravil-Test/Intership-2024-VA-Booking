package ru.irlix.booking.security.jwt;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.SignatureException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.GenericFilterBean;

import java.io.IOException;
import java.util.Enumeration;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtRequestFilter extends GenericFilterBean {

    private final JwtTokenUtils jwtTokenUtils;

    private void setAuthentication(String login, String jwt) {
        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(
                login,
                null,
                jwtTokenUtils.getRoles(jwt).stream().map(SimpleGrantedAuthority::new).toList());
        SecurityContextHolder.getContext().setAuthentication(token);
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        Enumeration<String> headers = httpRequest.getHeaders("Authorization");
        String authHeader = null;

        if (headers != null && headers.hasMoreElements()) {
            authHeader = headers.nextElement();
        }

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String jwt = authHeader.substring(7);
            try {
                String username = jwtTokenUtils.getUsername(jwt);
                if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                    setAuthentication(username, jwt);
                }
            } catch (ExpiredJwtException e) {
                log.warn("Время жизни токена вышло");

                httpResponse.setCharacterEncoding("UTF-8");
                httpResponse.setContentType("application/json; charset=UTF-8");
                httpResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                String jsonResponse = "{\"error\": \"Время жизни токена вышло\"}";
                httpResponse.getWriter().write(jsonResponse);
                return;
            } catch (SignatureException e) {
                log.error("Подпись неправильная");

                httpResponse.setCharacterEncoding("UTF-8");
                httpResponse.setContentType("application/json; charset=UTF-8");
                httpResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                String jsonResponse = "{\"error\": \"Подпись неправильная\"}";
                httpResponse.getWriter().write(jsonResponse);
                return;
            }
        }

        chain.doFilter(httpRequest, httpResponse);
    }

}