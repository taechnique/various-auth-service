package io.teach.infrastructure.config;

import io.taech.print.impl.Printer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ServiceBeanConfig {


    @Bean
    public Printer out() {
        return new Printer();
    }
}
