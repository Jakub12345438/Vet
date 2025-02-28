package przychodnia.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import przychodnia.models.UserModel;

import java.time.LocalDateTime;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class SearchForm {
    private UserModel vet;
    private LocalDateTime localDateTime;
}
