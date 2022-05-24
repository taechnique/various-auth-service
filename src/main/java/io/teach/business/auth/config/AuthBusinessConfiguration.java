package io.teach.business.auth.config;

import io.teach.business.auth.controller.AuthorizationController;
import io.teach.business.auth.service.AuthServiceFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AuthBusinessConfiguration {

    @Bean
    public AuthorizationController authorizationController() {
        System.out.println("컨트롤러 주입");
        return new AuthorizationController();
    }

}
