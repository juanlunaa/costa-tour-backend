package application.costa_tour.jwt;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

// OncePerRequestFilter es una clase abstracta para crear filtros perzonalizados
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private JwtService jwtService;

    @Autowired
    private UserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        final String token = getTokenFromReq(request);
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
            }
        }

        filterChain.doFilter(request, response);
    }

//  Metodo para extraer el token de la request
    private String getTokenFromReq(HttpServletRequest req) {
//      Se extrae los datos de autorizacion que vienen en el header de la request
//      donde se espera que este el token
//        final String authHeader = req.getHeader(HttpHeaders.AUTHORIZATION);

//      Segun el estandar de autenticacion el token se envie en el encabezado
//      HTTP Authorization debe ir con el prefijo Bearer
//        if (StringUtils.hasText(authHeader) && authHeader.startsWith("Bearer ")) {
//          Retornamos el authHeader desde el index 7 para que solamente vaya el token
//          sin el prefijo "Bearer "
//            return authHeader.substring(7);
//        }
//      Si no hay token en el header
//        return null;
        String jwtToken = null;
//      Como el token va a estar almacenado en las cookies, debemos sacarlo de ahi
        Cookie[] cookies = req.getCookies();

        if (cookies == null) return null;

        for (Cookie cookie : cookies) {
            if (cookie.getName().equals("token")) {
                jwtToken = cookie.getValue();
            }
        }

        return jwtToken;
    }
}
