package przychodnia.dtos;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PasswordForgotDto {

    @NotEmpty(message = "Pole e-mail nie może być puste.")
    private String email;

}

