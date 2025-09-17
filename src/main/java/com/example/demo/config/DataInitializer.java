package com.example.demo.config;

import com.example.demo.model.AppUser;
import com.example.demo.repository.AppUserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class DataInitializer {

    @Bean
    CommandLineRunner seedUsers(AppUserRepository repository, PasswordEncoder passwordEncoder) {
        return args -> {
            if (repository.count() == 0) {
                AppUser admin = new AppUser("admin", passwordEncoder.encode("admin"), "ROLE_ADMIN");
                AppUser user = new AppUser("user", passwordEncoder.encode("password"), "ROLE_USER");
                repository.save(admin);
                repository.save(user);
            }
        };
    }
}
