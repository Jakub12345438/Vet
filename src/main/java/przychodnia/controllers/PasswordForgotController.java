package przychodnia.controllers;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import przychodnia.dtos.MailDto;
import przychodnia.dtos.PasswordForgotDto;
import przychodnia.models.PasswordResetToken;
import przychodnia.models.UserModel;
import przychodnia.repositories.PasswordResetTokenRepository;
import przychodnia.services.EmailService;
import przychodnia.services.UserService;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Controller
@AllArgsConstructor
public class PasswordForgotController {

    private UserService userService;

    private PasswordResetTokenRepository tokenRepository;
    private EmailService emailService;

    @ModelAttribute("forgotPasswordForm")
    public PasswordForgotDto forgotPasswordDto() {
        return new PasswordForgotDto();
    }

    @GetMapping("/forgot-password")
    public String showForgotPasswordPage() {
        return "ForgotPasswordView";
    }

    @PostMapping("/forgot-password")
    public String sendForgotPasswordForm(@ModelAttribute("forgotPasswordForm") @Valid PasswordForgotDto form,
                                            BindingResult result,
                                            HttpServletRequest request) {

        if (result.hasErrors()) {
            return "ForgotPasswordView";
        }

        UserModel user = userService.findUserByEmail(form.getEmail());
        if (user == null) {
            result.rejectValue("email", "null", "Nie udało się znaleźć konta dla tego adresu e-mail.");
            return "ForgotPasswordView";
        }

        PasswordResetToken token = new PasswordResetToken();
        token.setToken(UUID.randomUUID().toString());
        token.setUser(user);
        token.setExpiryDate(35);
        tokenRepository.save(token);

        MailDto mail = new MailDto();
        mail.setFrom("mail@example.com");
        mail.setTo(user.getEmail());
        mail.setSubject("Prośba o zmianę hasła.");

        Map<String, Object> model = new HashMap<>();
        model.put("token", token);
        model.put("user", user);
        String url = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort();
        model.put("resetUrl", url + "/reset-password?token=" + token.getToken());
        mail.setModel(model);
        emailService.sendEmail(mail);

        return "redirect:/forgot-password?success";

    }
}
