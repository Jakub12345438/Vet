package przychodnia.controllers;

import lombok.AllArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import przychodnia.models.UserModel;
import przychodnia.models.Visit;
import przychodnia.services.UserService;
import przychodnia.services.VisitService;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Controller
@AllArgsConstructor
public class UserVisitsController {
    private VisitService visitService;

    private UserService userService;

    @GetMapping("/visit")
    public String showPatientForm(Model model) {

        // Pobranie informacji o użytkowniku
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Long loggedUserId = visitService.getUserIdFromAuthentication();
        //Sprawdzenie ról użytkownika
        boolean isDoctor = authentication.getAuthorities().stream()
                .anyMatch(authority -> authority.getAuthority().equals("ROLE_DOCTOR"));
        boolean isAdmin = authentication.getAuthorities().stream()
                .anyMatch(authority -> authority.getAuthority().equals("ROLE_ADMIN"));

        model.addAttribute("isDoctor", isDoctor);
        model.addAttribute("isAdmin", isAdmin);

        // Jeśli użytkownik ma rolę "ROLE_DOCTOR", rzucamy wyjątek
        if (isDoctor) {
            throw new AccessDeniedException("Nie masz uprawnień do tej strony.");
        }

        // Jeśli użytkownik ma rolę "ROLE_DOCTOR", rzucamy wyjątek
        if (isAdmin) {
            throw new AccessDeniedException("Nie masz uprawnień do tej strony.");
        }

        // Pobranie listy weterynarzy z bazy danych
        List<UserModel> vets = userService.findWithRole("ROLE_DOCTOR");

        model.addAttribute("loggedUserId", loggedUserId);
        // Przekazanie listy weterynarzy i pustego formularza
        model.addAttribute("vets", vets);
        model.addAttribute("newPatient", new Visit());
        return "UserVisitForm";
    }

    @PostMapping("/visit")
    public String registerPatient(@ModelAttribute("newPatient") Visit visit) {
        visitService.addVisit(visit);
        return "redirect:/visit";
    }

    @GetMapping("/user-visits")
    public String getUserVisits(Model model) {

        // Pobranie informacji o użytkowniku
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        // Sprawdzenie ról użytkownika
        boolean isDoctor = authentication.getAuthorities().stream()
                .anyMatch(authority -> authority.getAuthority().equals("ROLE_DOCTOR"));

        boolean isAdmin = authentication.getAuthorities().stream()
                .anyMatch(authority -> authority.getAuthority().equals("ROLE_ADMIN"));

        model.addAttribute("isDoctor", isDoctor);
        model.addAttribute("isAdmin", isAdmin);

        // Jeśli użytkownik ma rolę "ROLE_DOCTOR", rzucamy wyjątek
        if (isDoctor) {
            throw new AccessDeniedException("Nie masz uprawnień do tej strony.");
        }

        // Jeśli użytkownik ma rolę "ROLE_ADMIN", rzucamy wyjątek
        if (isAdmin) {
            throw new AccessDeniedException("Nie masz uprawnień do tej strony.");
        }

        Long userId = visitService.getUserIdFromAuthentication(); // Pobierz identyfikator użytkownika zalogowanego

        List<Visit> userVisits = visitService.findVisitsByUserId(userId);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");
        List<String> formattedDateTimes = new ArrayList<>();
        for (Visit visit : userVisits) {
            String formattedDateTime = visit.getDateTime().format(formatter);
            formattedDateTimes.add(formattedDateTime);
        }

        model.addAttribute("userVisits", userVisits);
        model.addAttribute("formattedDateTimes", formattedDateTimes);

        return "UserVisitsView";
    }

    @GetMapping("/edit/{visitID}")
    public String showEditForm(@PathVariable Long visitID, Model model) {

        // Pobierz wizytę do edycji
        Visit visitToEdit = visitService.findVisitById(visitID);

        Long userID = visitService.getUserIdFromAuthentication(); // Pobierz identyfikator użytkownika zalogowanego

        // Pobranie listy weterynarzy z bazy danych
        List<UserModel> vets = userService.findWithRole("ROLE_DOCTOR");

        if(Objects.equals(visitToEdit.getUserModel().getUserID(), userID)) {

            model.addAttribute("editedVisit", visitToEdit);
            model.addAttribute("visitId", visitID);
            model.addAttribute("vets", vets);

            return "UserEditVisit";
        }
        else
        {
            throw new IllegalStateException("Użytkownik nie ma uprawnień.");
        }
    }

    @PostMapping("/edit/{visitID}")
    public String editVisit(@PathVariable long visitID, @ModelAttribute("editedVisit") Visit editedVisit) {
        visitService.editVisit(visitID, editedVisit);
        return "redirect:/user-visits";

    }


    @PostMapping("/delete/{visitID}")
    public String deleteVisit(@PathVariable long visitID) {
        visitService.deleteVisitById(visitID);
        return "redirect:/user-visits"; // Przekierowanie na stronę z listą zamówień
    }

}


