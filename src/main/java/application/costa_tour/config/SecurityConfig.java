package application.costa_tour.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(req ->
                    req
                        .requestMatchers(
                                "/user/auth",
                                "/plan/all",
                                "/plan/{id}",
                                "/files/**",
                                "/location/**"
                        ).permitAll()
                        .anyRequest().authenticated()
                    )
                .formLogin(Customizer.withDefaults())
                .build();
    }

}
