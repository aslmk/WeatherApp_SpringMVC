package com.aslmk.config;

import com.aslmk.filter.AuthorizedUsersFilter;
import com.aslmk.filter.UnauthorizedUsersFilter;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.client.RestTemplate;

@Configuration
public class AppConfig {

    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder builder) {
        return builder.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public FilterRegistrationBean<UnauthorizedUsersFilter> unauthorizedUsersFilter() {
        FilterRegistrationBean<UnauthorizedUsersFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(new UnauthorizedUsersFilter());
        registrationBean.addUrlPatterns("/locations", "/location/*");
        return registrationBean;
    }

    @Bean
    public FilterRegistrationBean<AuthorizedUsersFilter> authorizedUsersFilter() {
        FilterRegistrationBean<AuthorizedUsersFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(new AuthorizedUsersFilter());
        registrationBean.addUrlPatterns("/auth/login/*", "/auth/register/*");
        return registrationBean;
    }

}
