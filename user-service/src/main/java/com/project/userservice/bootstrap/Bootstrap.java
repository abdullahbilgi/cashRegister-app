package com.project.userservice.bootstrap;

import com.project.userservice.entity.Role;
import com.project.userservice.entity.User;
import com.project.userservice.repository.RoleRepository;
import com.project.userservice.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

@Component
@Profile("bootstrap")
@RequiredArgsConstructor
@Transactional
@Slf4j
public class Bootstrap implements CommandLineRunner {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;


    @Override
    public void run(String... args) throws Exception {

        if (roleRepository.count() < 1){

            Role adminRole = Role.builder()
                    .name("ADMIN")
                    .description("This is a Admin Role")
                    .build();

            Role cashierRole = Role.builder()
                    .name("CASHIER")
                    .description("This is a Cashier Role")
                    .build();

            Role managerRole = Role.builder()
                    .name("MANAGER")
                    .description("This is a Manager Role")
                    .build();

            Role automationRole = Role.builder()
                    .name("AUTOMATION")
                    .description("This is a Automation Role")
                    .build();

            roleRepository.save(adminRole);
            roleRepository.save(managerRole);
            roleRepository.save(cashierRole);
            roleRepository.save(automationRole);

            log.info("Roles were saved successfully!");

            String password = passwordEncoder.encode("password");

            User testAdmin = User.builder()
                    .name("admin")
                    .surname("Test")
                    .username("admintest")
                    .password(password)
                    .roles(Set.of(adminRole))
                    .build();

            User testManager = User.builder()
                    .name("manager")
                    .surname("Test")
                    .username("managertest")
                    .password(password)
                    .roles(Set.of(managerRole))
                    .build();

            User testCashier = User.builder()
                    .name("cashier")
                    .surname("Test")
                    .username("cashiertest")
                    .password(password)
                    .roles(Set.of(cashierRole))
                    .build();

            User testAutomation = User.builder()
                    .name("automation")
                    .surname("automation")
                    .username("automationtest")
                    .password(password)
                    .roles(Set.of(automationRole))
                    .build();

            userRepository.save(testAdmin);
            userRepository.save(testManager);
            userRepository.save(testCashier);
            userRepository.save(testAutomation);

            log.info("Users were saved successfully!");


        }

    }
}
