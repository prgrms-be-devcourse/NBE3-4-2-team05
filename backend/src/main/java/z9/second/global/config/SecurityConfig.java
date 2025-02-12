package z9.second.global.config;

import static org.springframework.http.HttpMethod.GET;
import static org.springframework.http.HttpMethod.PATCH;
import static org.springframework.http.HttpMethod.POST;
import static z9.second.global.security.constant.HeaderConstant.CONTENT_TYPE;
import static z9.second.global.security.constant.JWTConstant.ACCESS_TOKEN_HEADER;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import z9.second.global.security.entrypoint.CustomAccessDeniedEntryPoint;
import z9.second.global.security.entrypoint.CustomAuthenticationEntryPoint;
import z9.second.global.security.filter.AuthenticationFilter;
import z9.second.global.security.filter.ReissueFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final AuthenticationFilter authenticationFilter;
    private final ReissueFilter reissueFilter;
    private final CustomAuthenticationEntryPoint customAuthenticationEntryPoint;
    private final CustomAccessDeniedEntryPoint customAccessDeniedEntryPoint;

    @Bean
    protected PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration)
            throws Exception {
        return configuration.getAuthenticationManager();
    }

    @Bean
    protected CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowCredentials(true);
        configuration.setAllowedOrigins(List.of("http://localhost:3000"));
        configuration.setAllowedMethods(List.of("*"));
        configuration.setAllowedHeaders(List.of(ACCESS_TOKEN_HEADER, CONTENT_TYPE));
        configuration.setExposedHeaders(List.of(ACCESS_TOKEN_HEADER));
        configuration.setMaxAge(3600L);

        return request -> configuration;
    }

    @Bean
    protected SecurityFilterChain filterChain(HttpSecurity http)
            throws Exception {

        http
                .csrf(AbstractHttpConfigurer::disable)
                .formLogin(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable);

        http.sessionManagement((session) ->
                session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        http.cors((cors) ->
                cors.configurationSource(corsConfigurationSource()));

        http.authorizeHttpRequests((auth) -> auth
                .requestMatchers(GET, "/api/v1/sample/only-user").authenticated()
                .requestMatchers(GET, "/api/v1/sample/only-admin").hasRole("ADMIN")

                //authentication Domain
                .requestMatchers(POST, "/api/v1/logout").authenticated()
                .requestMatchers(PATCH, "/api/v1/resign").authenticated()

                //user Domain
                .requestMatchers(GET, "/api/v1/users").authenticated()
                .requestMatchers(PATCH, "/api/v1/users/profile").authenticated()
                .requestMatchers(GET, "/api/v1/users/schedules").authenticated()
                .requestMatchers(GET, "/api/v1/users/classes").authenticated()

                //class Domain
                // All classes in the Domain package require authentication
                .requestMatchers("/api/v1/classes/**").authenticated()

                //schedules Domain
                // All schedules in the Domain package require authentication
                .requestMatchers("/api/v1/schedules/**").authenticated()

                //search Domain
                .requestMatchers(GET, "/api/v1/search/favorite").authenticated()

                //checkin Domain
                // All checkIn in the Domain package require authentication
                .requestMatchers("/api/v1/checkin/**").authenticated()

                //나머지 다 permitAll
                .anyRequest().permitAll());

        http.exceptionHandling((exception) -> exception
                .authenticationEntryPoint(customAuthenticationEntryPoint)
                .accessDeniedHandler(customAccessDeniedEntryPoint));

        http.addFilterBefore(authenticationFilter, UsernamePasswordAuthenticationFilter.class);
        http.addFilterBefore(reissueFilter, AuthenticationFilter.class);

        return http.build();
    }
}
