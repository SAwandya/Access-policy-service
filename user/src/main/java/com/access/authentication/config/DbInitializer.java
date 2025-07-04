package com.access.authentication.config;

import com.access.authentication.model.ERole;
import com.access.authentication.model.Role;
import com.access.authentication.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Component
public class DbInitializer implements CommandLineRunner {

    @Autowired
    private RoleRepository roleRepository;

    @Override
    public void run(String... args) throws Exception {
        // Initialize roles if they don't exist
        Arrays.stream(ERole.values()).forEach(role -> {
            if (!roleRepository.findByName(role).isPresent()) {
                roleRepository.save(new Role(role));
            }
        });
    }
}
