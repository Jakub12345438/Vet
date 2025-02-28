package przychodnia.configuration;

import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import przychodnia.models.UserModel;
import przychodnia.repositories.UserRepository;
import org.springframework.security.core.userdetails.User;


@Component
@AllArgsConstructor
public class UserDetailsServiceConfig implements UserDetailsService {

    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        UserModel user = userRepository.findByEmail(email);

        if (user != null) {
            return new User(user.getEmail(),
                    user.getPassword(),
                    user.mapUserRolesToAuthorities(user.getRoles()));
        } else {
            throw new UsernameNotFoundException("Nieprawidłowe dane logowania.");
        }

    }

}
