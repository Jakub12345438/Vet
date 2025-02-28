package przychodnia.controllers;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import przychodnia.dtos.UserDto;
import przychodnia.services.UserService;

@Controller
@AllArgsConstructor
public class AuthController {

    private final UserService userService;

    @GetMapping("/register")
    public String showRegistrationForm(Model model){

        UserDto user = new UserDto();
        model.addAttribute("user", user);
        return "RegistrationView";
    }

    @PostMapping("/register/save")
    public String registration(@Valid @ModelAttribute("user") UserDto userDto,
                               BindingResult result,
                               Model model) {
        // Sprawdź, czy użytkownik już istnieje
        if (userService.findUserByEmail(userDto.getEmail()) != null) {
            result.rejectValue("email", "", "Istnieje już konto o takich danych logowania.");
        }

        // Sprawdź, czy występują błędy walidacji
        if (result.hasErrors()) {
            model.addAttribute("user", userDto);
            return "RegistrationView"; // Zwróć widok rejestracji w przypadku błędów
        }

        // Dodaj nowego użytkownika
        userService.addRegularUser(userDto);
        return "redirect:/register?success"; // Przekierowanie po udanej rejestracji
    }

    @GetMapping("/login")
    public String login() {
        return "LoginView"; // Zwraca widok logowania
    }


}
