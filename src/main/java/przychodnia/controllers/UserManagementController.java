package przychodnia.controllers;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import przychodnia.models.UserModel;
import przychodnia.services.UserService;

import java.util.List;

@Controller
@AllArgsConstructor
public class UserManagementController {

    private final UserService userService;

    @GetMapping("/users")
    public String getUsers(Model model){
        List<UserModel> users = userService.findAllUsers();
        model.addAttribute("users", users);
        return "AdminUsersView";
    }

    @PostMapping("/add-doctor-role/{userId}")
    public String addDoctorRole(@PathVariable Long userId) {
        userService.addDoctorRole(userId);
        return "redirect:/users";
    }

    @PostMapping("/remove-doctor-role/{userId}")
    public String removeDoctorRole(@PathVariable Long userId) {
        userService.removeDoctorRole(userId);
        return "redirect:/users";
    }

    @PostMapping("/remove-user/{userId}")
    public String removeUser(@PathVariable Long userId)
    {
        userService.deleteUserById(userId);
        return "redirect:/users";
    }

}
