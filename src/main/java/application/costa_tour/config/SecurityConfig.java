package application.costa_tour.config;

import application.costa_tour.jwt.JwtAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

//  Se inyecta el filtro personalizado de JWT para añadirlo a la cadena de filtros
    @Autowired
    private JwtAuthenticationFilter jwtAuthFilter;

//  Se inyecta el AuthenticationProvider configurado en ApplicationConfig
    @Autowired
    private AuthenticationProvider authProvider;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(req ->
                    req
                        .requestMatchers(
                                "/user/auth",
                                "/user/validate-email",
                                "/plan/all",
                                "/plan/{id}",
                                "/interest/**",
                                "/files/**",
                                "/location/**",
                                "/turist/create",
                                "/turist/validate-dni"
                        ).permitAll()
                        .requestMatchers("/user/**").authenticated()
                        .requestMatchers("/plan/**").authenticated()
                        .anyRequest().authenticated()
                    )
//              No se crearan ni se usaran sesiones HTTP para almacenar información sobre el usuario autenticado
                .sessionManagement(sessionManager ->
                        sessionManager.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
//              Setteo del AuthenticationProvider
                .authenticationProvider(authProvider)
//              Se añade el filtro de JWT a la cadena de filtros
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }

}
