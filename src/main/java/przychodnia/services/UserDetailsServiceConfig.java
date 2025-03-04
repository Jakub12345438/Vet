package przychodnia.services;

import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import przychodnia.models.UserModel;
import przychodnia.repositories.UserRepository;
import org.springframework.security.core.userdetails.User;


@Service
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
