package przychodnia.dtos;

import jakarta.validation.constraints.NotEmpty;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDto
{
    private Long userID;
    @NotEmpty(message = "Pole imię nie może być puste.")
    private String name;
    @NotEmpty(message = "Pole nazwisko nie może być puste.")
    private String surname;
    @NotEmpty(message = "Pole e-mail nie może być puste.")
    private String email;
    @NotEmpty(message = "Pole numer telefonu nie może być puste.")
    private String phoneNumber;
    @NotEmpty(message = "Pole hasło nie może być puste.")
    private String password;

}
