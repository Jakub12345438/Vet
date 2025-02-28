package przychodnia.controllers;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import przychodnia.dtos.PasswordResetDto;
import przychodnia.models.PasswordResetToken;
import przychodnia.models.UserModel;
import przychodnia.repositories.PasswordResetTokenRepository;
import przychodnia.services.UserService;


@Controller
@AllArgsConstructor
public class PasswordResetController {

    private UserService userService;
    private PasswordResetTokenRepository tokenRepository;
    private BCryptPasswordEncoder passwordEncoder;

    @ModelAttribute("resetPasswordForm")
    public PasswordResetDto resetPasswordDto() {
        return new PasswordResetDto();
    }

    @GetMapping("/reset-password")
    public String showResetPasswordPage(@RequestParam(required = false) String token,
                                           Model model) {

        PasswordResetToken resetToken = tokenRepository.findByToken(token);
        if (resetToken == null) {
            model.addAttribute("error", "Nie znaleziono tokenu resetowania hasła.");
        } else if (resetToken.isExpired()) {
            model.addAttribute("error", "Okres ważności tokenu minął, proszę wysłać nową prośbę o zresetowanie hasła.");
        } else {
            model.addAttribute("token", resetToken.getToken());
        }

        return "ResetPasswordView";
    }

    @PostMapping("/reset-password")
    @Transactional
    public String sendResetPasswordForm(@ModelAttribute("resetPasswordForm") @Valid PasswordResetDto form,
                                      BindingResult result,
                                      RedirectAttributes redirectAttributes) {

        if (result.hasErrors()) {
            redirectAttributes.addFlashAttribute(BindingResult.class.getName() + ".resetPasswordForm", result);
            redirectAttributes.addFlashAttribute("resetPasswordForm", form);
            return "redirect:/reset-password?token=" + form.getToken();
        }

        PasswordResetToken token = tokenRepository.findByToken(form.getToken());
        UserModel user = token.getUser();
        String updatedPassword = passwordEncoder.encode(form.getPassword());
        userService.updatePassword(updatedPassword, user.getUserID());
        tokenRepository.delete(token);

        return "redirect:/login?resetSuccess";
    }

}
