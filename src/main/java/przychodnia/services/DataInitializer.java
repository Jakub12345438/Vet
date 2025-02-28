package przychodnia.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import przychodnia.models.Role;
import przychodnia.models.UserModel;
import przychodnia.repositories.RoleRepository;
import przychodnia.repositories.UserRepository;

import java.util.Arrays;

@Component
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    @Autowired
    public DataInitializer(UserRepository userRepository, RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
    }

    @Override
    public void run(String... args) {

        Role userRole = new Role(1, "ROLE_USER");
        Role adminRole = new Role(2, "ROLE_ADMIN");
        Role doctorRole = new Role(3,"ROLE_DOCTOR");

        roleRepository.saveAll(Arrays.asList(userRole,adminRole,doctorRole));


        UserModel adminUser = new UserModel(1L, "Admin", "User", "admin@example.com", "123456789", new BCryptPasswordEncoder().encode("Admin12345!"),  Arrays.asList(userRole,adminRole));
        userRepository.save(adminUser);


    }
}
