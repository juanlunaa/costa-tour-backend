package application.costa_tour.jwt;

import application.costa_tour.exception.InvalidCredentialsException;
import application.costa_tour.exception.UnauthorizedException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import io.jsonwebtoken.Jwts;

@Service
public class JwtService {

    @Value("${secret-key}")
    private String SECRET_KEY;

    private static final long EXPIRATION_TIME_MS = 1000 * 60 * 60;

    public String getToken(UserDetails user) {
        return getToken(Map.of("role", user.getAuthorities()), user);
    }

    private String getToken(Map<String,Object> extraClaims, UserDetails user) {
        return Jwts.builder()
                .setClaims(extraClaims)
                .setSubject(user.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME_MS))
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY.getBytes())
                .compact();
    }

    private Key getKey() {
        byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    //  Metodo para extraer el token de la request
    public String getTokenFromReq(HttpServletRequest req) {
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

//  Retorna el email que esta en el payload del token
    public String getEmailFromToken (String token) {
        return getClaim(token, claims -> claims.getSubject());
    }

//  Valida si el token corresponde al usuario y si aun no ha expirado
    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String username = getEmailFromToken(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

//  Retorna las Claims de un token
    private Claims getAllClaims (String token) {
        try {
            return Jwts.parser()
                    .setSigningKey(SECRET_KEY.getBytes())
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (ExpiredJwtException e) {
            throw new UnauthorizedException("Token has expired");
        }
    }

//  Metodo generico que retorna una Claim en especifica del token
    public <T> T getClaim (String token, Function<Claims, T> claimsResolver) {
        final Claims claims = getAllClaims(token);
        return claimsResolver.apply(claims);
    }

//  Retorna la fecha de expiracion que se le asigno al token
    private Date getExpiration (String token) {
        return getClaim(token, claims -> claims.getExpiration());
    }

//  Valida si el token ya expiro
    private boolean isTokenExpired (String token) {
        return getExpiration(token).before(new Date());
    }
}
