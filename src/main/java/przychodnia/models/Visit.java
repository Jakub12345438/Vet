package przychodnia.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Represents a visit entity.")
public class Visit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "The unique ID of the visit.")
    private Long visitID;

    @Column
    @Schema(description = "The name of the client.")
    private String clientName;

    @Column
    @Schema(description = "The surname of the client.")
    private String clientSurname;

    @Column
    @Schema(description = "The name of the animal.")
    private String animalName;

    @Column
    @Schema(description = "The type of the animal.")
    private Animal animal;

    @Column
    @Schema(description = "The gender of the patient.")
    private Gender gender;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "vet_id", nullable = false) // Pole reprezentujące weterynarza
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    @JsonIgnore
    @Schema(description = "The vet assigned to the visit.")
    private UserModel vet;

    @Column(nullable = false)
    @Schema(description = "The date and the time of the visit.")
    @DateTimeFormat(pattern = "dd.MM.yyyy HH:mm")
    private LocalDateTime dateTime;

    @Column
    @Schema(description = "The description of the visit.")
    private String description;

    @Column
    @Schema(description = "The phone number of the client.")
    private String clientPhoneNumber;

    @Column
    @Schema(description = "The phone number of the vet.")
    private String vetPhoneNumber;

    @ToString.Exclude
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    @JsonIgnore
    @Schema(description = "The userModel which is assigned to the visit.")
    private UserModel userModel; // Pole odniesienia do użytkownika

    public Visit(Long visitID, UserModel vet, LocalDateTime dateTime) {
        this.visitID = visitID;
        this.vet = vet;
        this.dateTime = dateTime;
    }

    public Visit(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }
}
