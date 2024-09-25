package application.costa_tour.jwt;

import application.costa_tour.exception.InvalidCredentialsException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
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

    public String getToken(UserDetails user) {
        return getToken(new HashMap<>(), user);
    }

    private String getToken(Map<String,Object> extraClaims, UserDetails user) {
        return Jwts.builder()
                .setClaims(extraClaims)
                .setSubject(user.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000*60*24))
                .signWith(getKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    private Key getKey() {
        byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(keyBytes);
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
        return Jwts.parser()
                .setSigningKey(getKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
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
