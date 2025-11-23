package br.com.futurehub.futurehubgs.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableMethodSecurity
public class SecurityConfig {

    /**
     * Usuários de autenticação (camada de segurança, não é a entidade de domínio Usuario).
     *
     * Logins padronizados da aplicação FutureHub:
     *
     * - admin / senha: 123456  -> ROLE_ADMIN, ROLE_USER
     * - user  / senha: 1234    -> ROLE_USER
     *
     * Em produção, trocar para:
     *  - senhas criptografadas (BCrypt)
     *  - usuários vindos do banco (UserDetailsService customizado)
     */
    @Bean
    public UserDetailsService userDetailsService() {
        UserDetails admin = User.withUsername("admin")
                .password("{noop}123456") // {noop} = sem encoder (apenas para DEV / demo)
                .roles("ADMIN", "USER")
                .build();

        UserDetails user = User.withUsername("user")
                .password("{noop}1234")
                .roles("USER")
                .build();

        return new InMemoryUserDetailsManager(admin, user);
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http
                // CSRF desabilitado em DEV (facilita testes com POST/PUT/DELETE via Swagger / tools)
                .csrf(csrf -> csrf.disable())

                .authorizeHttpRequests(auth -> auth
                        // Pré-flight CORS (OPTIONS) sempre liberado
                        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()

                        // Swagger/OpenAPI totalmente públicos (para facilitar testes)
                        .requestMatchers(
                                "/swagger-ui.html",
                                "/swagger-ui/**",
                                "/v3/api-docs/**"
                        ).permitAll()

                        // Actuator (apenas health/info públicos)
                        .requestMatchers(HttpMethod.GET,
                                "/actuator/health",
                                "/actuator/info"
                        ).permitAll()

                        // Endpoint simples de teste de i18n
                        .requestMatchers(HttpMethod.GET, "/hello").permitAll()

                        // 👉 Novo: cadastro de usuário é público (não precisa estar logado)
                        .requestMatchers(HttpMethod.POST, "/api/usuarios").permitAll()

                        // Endpoints GET públicos da API (mural e ranking visíveis sem login)
                        .requestMatchers(HttpMethod.GET,
                                "/api/ideias/**",
                                "/api/missoes/**",
                                "/api/rankings/**"
                        ).permitAll()

                        // Qualquer outra requisição exige autenticação (admin ou user)
                        .anyRequest().authenticated()
                )

                // Basic Auth simples (popup do navegador / auth no Swagger)
                .httpBasic(Customizer.withDefaults());

        return http.build();
    }
}



