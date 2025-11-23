package cl.bakery.Usuarios.security;

import java.io.IOException;
import java.util.Collections;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class ApiKeyFilter extends OncePerRequestFilter {

    private static final Logger logger = LoggerFactory.getLogger(ApiKeyFilter.class);
    private static final String HEADER_NAME = "X-API-KEY";

    private final String validApiKey;

    public ApiKeyFilter(@Value("${app.api.key}") String validApiKey) {
        this.validApiKey = validApiKey != null ? validApiKey.trim() : null;
        logger.info("ApiKeyFilter inicializado. API Key cargada? {}", this.validApiKey != null);
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain)
            throws ServletException, IOException {

        String path = request.getRequestURI();
        logger.debug("Request URI: {}", path);

        // permitir Swagger sin API Key
        if (path.contains("/swagger-ui") ||
                path.contains("/v3/api-docs") ||
                path.contains("/doc")) {

            filterChain.doFilter(request, response);
            return;
        }

        // permitir preflight CORS sin API Key
        if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
            filterChain.doFilter(request, response);
            return;
        }

        // leer encabezado
        String apiKey = request.getHeader("X-API-KEY");
        if (apiKey == null) {
            apiKey = request.getHeader("x-api-key");
        }
        
        logger.debug("API Key recibida: {}", apiKey);

        // validar API Key
        if (apiKey == null || validApiKey == null || !apiKey.trim().equals(validApiKey)) {
            logger.warn("API KEY inválida o ausente. Recibida='{}'", apiKey);
            response.setStatus(HttpStatus.FORBIDDEN.value());
            response.getWriter().write("API KEY INVALIDA O AUSENTE");
            return;
        }

        // MARCAR COMO AUTENTICADO
        UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken("apikey-user", null,
                Collections.emptyList());

        SecurityContextHolder.getContext().setAuthentication(auth);


        filterChain.doFilter(request, response);
    }
}
