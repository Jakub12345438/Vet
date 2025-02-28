package przychodnia.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import przychodnia.models.Animal;
import przychodnia.models.UserModel;
import przychodnia.models.Gender;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class VisitDto {
    private Long visitID;
    private String clientName;
    private String clientSurname;
    private String animalName;
    private Animal animal;
    private Gender gender;
    private UserModel vet;
    private LocalDateTime dateTime;
    private String description;
    private String clientPhoneNumber;
    private String vetPhoneNumber;

}
