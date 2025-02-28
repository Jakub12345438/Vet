package przychodnia.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Holiday {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long holidayID;

    @Column(nullable = false)
    @DateTimeFormat(pattern = "dd.MM.yyyy")
    private LocalDate startDate;

    // Relacja z weterynarzem
    @ManyToOne
    @JoinColumn(name = "vet_id", nullable = false) // Klucz obcy w tabeli Holiday
    private UserModel vet;

}
