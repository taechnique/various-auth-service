package io.teach.infrastructure.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import io.taech.print.impl.Printer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ServiceBeanConfig {

    @Bean
    public Printer printer() {
        return new Printer();
    }


}
