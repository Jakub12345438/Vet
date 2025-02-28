package przychodnia.services;

import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import przychodnia.dtos.UserDto;
import przychodnia.models.Holiday;
import przychodnia.models.Role;
import przychodnia.models.UserModel;
import przychodnia.models.Visit;
import przychodnia.repositories.HolidayRepository;
import przychodnia.repositories.RoleRepository;
import przychodnia.repositories.UserRepository;
import przychodnia.repositories.VisitRepository;

import java.util.List;

@Service
@AllArgsConstructor
public class UserService  {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    private final VisitService visitService;

    private final HolidayService holidayService;

    private final VisitRepository visitRepository;

    private final HolidayRepository holidayRepository;

    public void addRegularUser(UserDto userDto) {
        UserModel userModel = new UserModel();

        userModel.setName(userDto.getName());
        userModel.setSurname(userDto.getSurname());
        userModel.setEmail(userDto.getEmail());
        userModel.setPhoneNumber(userDto.getPhoneNumber());
        userModel.setPassword(passwordEncoder.encode(userDto.getPassword()));

        Role role = roleRepository.findByRoleName("ROLE_USER");

        userModel.setRoles(List.of(role));
        userRepository.save(userModel);
    }

    public UserModel findUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public List<UserModel> findAllUsers() {
        return userRepository.findAll();
    }

    public void addDoctorRole(Long userId) {
        UserModel user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("Użytkownik nie istnieje"));
        Role doctorRole = roleRepository.findByRoleName("ROLE_DOCTOR");

        if (!user.getRoles().contains(doctorRole)) {
            user.getRoles().add(doctorRole);
            userRepository.save(user);
            //Usuń wizyty przypisane do użytkownika
            List<Visit> visits = visitService.findVisitsByUserId(userId);
            visitRepository.deleteAll(visits);
        }

    }

    public void removeDoctorRole(Long userId) {
        UserModel user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("Użytkownik nie istnieje"));
        Role doctorRole = roleRepository.findByRoleName("ROLE_DOCTOR");

        if (user.getRoles().contains(doctorRole)) {
            user.getRoles().remove(doctorRole);
            userRepository.save(user);
            // Usuń wizyty i urlopy przypisane do tego weterynarza
            List<Visit> visits = visitService.findVisitsByDoctorId(userId);
            List<Holiday> holidays = holidayService.findHolidaysForVet(userId);
            visitRepository.deleteAll(visits);
            holidayRepository.deleteAll(holidays);
        }
    }

    public void deleteUserById(Long userId)
    {
        UserModel user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("Użytkownik nie istnieje"));
        userRepository.deleteById(userId);
        List<Visit> visits = visitService.findVisitsByUserId(userId);
        visitRepository.deleteAll(visits);
    }

    public List<UserModel> findWithRole(String roleName) {
        return userRepository.findByRoleName(roleName);
    }

    public void updatePassword(String password, Long userId) {
        userRepository.updatePassword(password, userId);
    }


}
