package com.project.cravehub.config;

import com.project.cravehub.dto.UserRegistrationDto;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {

    @Bean
    public UserRegistrationDto userRegistrationDto() {
        return new UserRegistrationDto();
    }
}
