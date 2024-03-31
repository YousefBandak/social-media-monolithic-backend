package object_orienters.techspot.security.jwt;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

@Component
public class AuthEntryPointJwt implements AuthenticationEntryPoint {

    private static final Logger logger = LoggerFactory.getLogger(AuthEntryPointJwt.class);

//    @Override
//    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException)
//            throws IOException, ServletException {
//
//        logger.error("Unauthorized error: {}", authException.getMessage());
//
//        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
//        if (isAuthenticationError(authException)) {
//            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
//
//            final Map<String, Object> body = new HashMap<>();
//            body.put("status", HttpServletResponse.SC_UNAUTHORIZED);
//            body.put("error", "Unauthorized");
//            body.put("message", authException.getMessage());
//            body.put("cause", authException.getCause());
//            body.put("path", request.getServletPath());
//
//            final ObjectMapper mapper = new ObjectMapper();
//            mapper.writeValue(response.getOutputStream(), body);
//        } else {
//
//            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
//
//            final Map<String, Object> body = new HashMap<>();
//            body.put("status", HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
//            body.put("error", "Internal Server Error");
//            body.put("message", authException.getMessage());
//            body.put("path", request.getServletPath());
//
//            final ObjectMapper mapper = new ObjectMapper();
//            mapper.writeValue(response.getOutputStream(), body);
//
//        }
//    }
//
//    private boolean isAuthenticationError(AuthenticationException authException) {
//        // Check if the exception is related to authentication
//        // You can customize this logic based on the type of authentication exceptions you want to handle
//
//        return authException.getMessage().contains("Unauthorized") || authException.getMessage().contains("Bad credentials");
//    }


    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException)
            throws IOException, ServletException {
        if (isAuthenticationError(authException)) {
            logger.error("Unauthorized error: {}", authException.getMessage());

            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

            final Map<String, Object> body = new HashMap<>();
            body.put("status", HttpServletResponse.SC_UNAUTHORIZED);
            body.put("error", "Unauthorized");
            body.put("message", authException.getMessage());
            body.put("cause", authException.getCause());
            body.put("path", request.getServletPath());

            final ObjectMapper mapper = new ObjectMapper();
            mapper.writeValue(response.getOutputStream(), body);
        } else {
            // Pass the exception to the default handling mechanism
            throw authException;
        }
    }

    private boolean isAuthenticationError(AuthenticationException authException) {
        // Check if the exception is related to authentication
        // You can customize this logic based on the type of authentication exceptions you want to handle
        return authException instanceof AuthenticationException;
    }


}
