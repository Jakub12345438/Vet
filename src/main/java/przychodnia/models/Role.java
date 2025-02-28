package przychodnia.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Schema(description = "Represents a role entity.")
public class Role
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "The unique ID of the role.")
    @Column(name = "roleID") // Kolumna odpowiadająca roleID w bazie danych
    private Integer roleID;

    @Column(nullable=false, unique=true)
    @Schema(description = "The name of the role.")
    private String roleName;

    @ToString.Exclude
    @ManyToMany(mappedBy="roles")
    @Schema(description = "The users to which the role is assigned.")
    private List<UserModel> userModels = new ArrayList<>();

    public Role(Integer roleID, String roleName) {
        this.roleID = roleID;
        this.roleName = roleName;
    }


}

