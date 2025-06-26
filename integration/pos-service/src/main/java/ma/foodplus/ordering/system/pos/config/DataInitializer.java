package ma.foodplus.ordering.system.pos.config;

import ma.foodplus.ordering.system.pos.domain.User;
import ma.foodplus.ordering.system.pos.enums.UserRole;
import ma.foodplus.ordering.system.pos.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public DataInitializer(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {
        // Seed admin user if not exists
        String adminUsername = "admin";
        if (!userRepository.existsByUsername(adminUsername)) {
            User admin = new User();
            admin.setFirstName("System");
            admin.setLastName("Administrator");
            admin.setEmail("admin@foodplus.com");
            admin.setUsername(adminUsername);
            admin.setPassword(passwordEncoder.encode("admin123")); // Change in production!
            admin.setRole(UserRole.ADMIN);
            admin.setActive(true);
            userRepository.save(admin);
        }
        // Optionally seed other roles/users here if needed
    }
} 