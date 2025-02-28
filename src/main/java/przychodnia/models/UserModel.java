package przychodnia.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Schema(description = "Represents an user entity.")
public class UserModel
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "The unique ID of the user.")
    @Column(name = "userID") // Kolumna odpowiadająca userID w bazie danych
    private Long userID;

    @Column(nullable=false)
    @Schema(description = "The name of the user.")
    private String name;

    @Column(nullable=false)
    @Schema(description = "The surname of the user.")
    private String surname;

    @Column(nullable=false, unique=true)
    @Schema(description = "The e-mail of the user.")
    private String email;

    @Column(nullable=false)
    @Schema(description = "The phone number of the user.")
    private String phoneNumber;
    @Column(nullable=false)
    @Schema(description = "The password of the user.")
    private String password;

    @Column
    private String resetToken;

    @Column
    private LocalDateTime resetTokenExpirationDate;

    @ToString.Exclude
    @ManyToMany(fetch = FetchType.EAGER, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(
            joinColumns={@JoinColumn(referencedColumnName="userID")},
            inverseJoinColumns={@JoinColumn(referencedColumnName="roleID")})
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    @JsonIgnore
    @Schema(description = "The roles which are assigned to the user.")
    private List<Role> roles = new ArrayList<>();

    @ToString.Exclude
    @OneToMany(mappedBy = "userModel", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    @JsonIgnore
    @Schema(description = "The visits which are assigned to the user or vet.")
    private List<Visit> visits = new ArrayList<>();

    // Relacja z Holiday
    @ToString.Exclude
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    @JsonIgnore
    @OneToMany(mappedBy = "vet", cascade = CascadeType.ALL)
    private List<Holiday> holidays; // Lista dni wolnych weterynarza

    public UserModel(Long userID, String name, String surname, String email, String phoneNumber, String password, List<Role> roles) {
        this.userID = userID;
        this.name = name;
        this.surname = surname;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.password = password;
        this.roles = roles;
    }

    /**
     * Maps user roles to GrantedAuthority for Spring Security.
     */
    public Collection<? extends GrantedAuthority> mapUserRolesToAuthorities(Collection<Role> roles) {
        if (roles == null || roles.isEmpty()) {
            return List.of(); // Zwraca pustą listę zamiast null
        }
        return roles.stream()
                .map(role -> new SimpleGrantedAuthority(role.getRoleName()))
                .collect(Collectors.toList());
    }

    public String getFullName() {
        return this.name + " " + this.surname;
    }

    public boolean hasRole(String roleName) {
        return roles.stream().anyMatch(role -> role.getRoleName().equalsIgnoreCase(roleName));
    }

    public UserModel(Long userID, String name, String surname) {
        this.userID = userID;
        this.name = name;
        this.surname = surname;
    }

    public List<String> getRoleNames(List<Role> roles)
    {
        List<String> roleNames = new ArrayList<>();
        for(Role role: roles)
        {
            String roleName = role.getRoleName();
            roleNames.add(roleName);
        }
        return roleNames;
    }


}
