package com.jpmc.midascore.component;

import com.jpmc.midascore.entity.User;
import com.jpmc.midascore.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class AppUserPopulator implements CommandLineRunner {

    private final UserRepository userRepository;

    public AppUserPopulator(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public void run(String... args) {
        if (userRepository.count() == 0) {
            userRepository.save(new User("Waldorf", 5000.0f));
            userRepository.save(new User("Statler", 3000.0f));
        }
    }
}
