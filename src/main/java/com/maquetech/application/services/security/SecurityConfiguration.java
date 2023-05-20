package com.maquetech.application.services.security;

import com.maquetech.application.services.user.UserService;
import com.maquetech.application.views.user.UserLoginView;
import com.vaadin.flow.spring.security.VaadinWebSecurity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.provisioning.UserDetailsManager;

@EnableWebSecurity
@Configuration
public class SecurityConfiguration extends VaadinWebSecurity {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        super.configure(http);
        setLoginView(http, UserLoginView.class);
    }

    @Bean
    public UserDetailsManager userDetailsService(@Autowired UserService service) {
        return new InMemoryUserDetailsManager(service.getAll());
    }
}

