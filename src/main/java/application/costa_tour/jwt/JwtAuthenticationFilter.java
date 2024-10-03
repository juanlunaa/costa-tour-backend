package application.costa_tour.jwt;

import application.costa_tour.exception.UnauthorizedException;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.UnavailableException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

import java.io.IOException;

// OncePerRequestFilter es una clase abstracta para crear filtros perzonalizados
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private JwtService jwtService;

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    @Qualifier("handlerExceptionResolver")
    private HandlerExceptionResolver resolver;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
//      Se obtiene el endpoint al que se esta haciendo la solicitud
        String path = request.getRequestURI();

        final String token = jwtService.getTokenFromReq(request);
        String email;
//      Si no hay token se continua la cadena de filtros directamente
        if (token == null) {
            filterChain.doFilter(request, response);
            return;
        }
        email = jwtService.getEmailFromToken(token);

//      Si hay un email en el payload del token y el contexto del SecurityContextHolder esta vacio
//      entonces el usuario se va a buscar en la bd
        if (email != null && SecurityContextHolder.getContext().getAuthentication()==null) {
            UserDetails userDetails = userDetailsService.loadUserByUsername(email);

//          Validamos si el token es valido para ese usuario
            if (jwtService.isTokenValid(token, userDetails)) {
//              Si lo es añadimos el usuario al contexto del SecurityContextHolder para que cuando se
//              quiera volver a autenticar no haya necesidad de buscar en la bd
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        userDetails,
                        null,
                        userDetails.getAuthorities());

                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                SecurityContextHolder.getContext().setAuthentication(authToken);
            } else {
                try {
                    throw new UnavailableException("Token is invalid");
                } catch (UnauthorizedException e) {
                    resolver.resolveException(request, response, null, e);
                }
            }
        }
        filterChain.doFilter(request, response);
    }
}
