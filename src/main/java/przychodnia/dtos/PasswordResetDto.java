package przychodnia.dtos;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PasswordResetDto {

    @NotEmpty(message = "Pole nie może być puste.")
    private String password;

    @NotEmpty(message = "Pole nie może być puste.")
    private String confirmPassword;

    @NotEmpty(message = "Pole nie może być puste.")
    private String token;

}

