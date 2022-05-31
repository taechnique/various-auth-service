package io.teach.infrastructure.config;

import io.teach.infrastructure.interceptor.AuthCheckInterceptor;
import io.teach.infrastructure.service.DefaultDynamicServiceProvider;
import io.teach.infrastructure.service.StrategyService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import static org.springframework.security.config.Customizer.withDefaults;

@EnableWebMvc
@Configuration
public class SecurityConfiguration implements WebMvcConfigurer {



    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry
                .addInterceptor(new AuthCheckInterceptor(new DefaultDynamicServiceProvider()));
    }

    @Bean
    public SecurityFilterChain filterChain(final HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .formLogin().disable()
                .authorizeRequests(auth ->
                        auth
                                .antMatchers(
                                        "/api/v1/user/**",
                                        "/api/v1/member/**",
                                        "/api/v1/infra/email/verify/send")
                                .permitAll()
                                .anyRequest().authenticated())
                .httpBasic(withDefaults());

        return http.build();
    }

    @Bean
    public WebSecurityCustomizer securityCustomizer() {
        return (web) -> web.ignoring().antMatchers("/api/v1/auth");
    }
}
