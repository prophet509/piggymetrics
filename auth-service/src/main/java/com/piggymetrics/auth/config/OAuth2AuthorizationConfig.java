package com.piggymetrics.auth.config;

import java.util.UUID;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.oauth2.server.authorization.client.InMemoryRegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.settings.AuthorizationServerSettings;
import org.springframework.security.oauth2.server.authorization.settings.ClientSettings;

@Configuration
public class OAuth2AuthorizationConfig {

    @Bean
    RegisteredClientRepository registeredClientRepository(Environment env, PasswordEncoder passwordEncoder) {
        RegisteredClient accountService = serviceClient("account-service", env.getProperty("ACCOUNT_SERVICE_PASSWORD", "password"), passwordEncoder);
        RegisteredClient statisticsService = serviceClient("statistics-service", env.getProperty("STATISTICS_SERVICE_PASSWORD", "password"), passwordEncoder);
        RegisteredClient notificationService = serviceClient("notification-service", env.getProperty("NOTIFICATION_SERVICE_PASSWORD", "password"), passwordEncoder);

        RegisteredClient browser = RegisteredClient.withId(UUID.randomUUID().toString())
                .clientId("browser")
                .clientAuthenticationMethod(ClientAuthenticationMethod.NONE)
                .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
                .authorizationGrantType(AuthorizationGrantType.REFRESH_TOKEN)
                .redirectUri("http://127.0.0.1:8080/login/oauth2/code/browser")
                .scope("ui")
                .clientSettings(ClientSettings.builder().requireAuthorizationConsent(false).requireProofKey(true).build())
                .build();

        return new InMemoryRegisteredClientRepository(browser, accountService, statisticsService, notificationService);
    }

    @Bean
    AuthorizationServerSettings authorizationServerSettings() {
        return AuthorizationServerSettings.builder().issuer("http://auth-service:5000/uaa").build();
    }

    private RegisteredClient serviceClient(String clientId, String rawSecret, PasswordEncoder passwordEncoder) {
        return RegisteredClient.withId(UUID.randomUUID().toString())
                .clientId(clientId)
                .clientSecret(passwordEncoder.encode(rawSecret))
                .clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_BASIC)
                .authorizationGrantType(AuthorizationGrantType.CLIENT_CREDENTIALS)
                .scope("server")
                .clientSettings(ClientSettings.builder().requireAuthorizationConsent(false).build())
                .build();
    }
}
