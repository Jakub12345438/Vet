package przychodnia.controllers;

import lombok.AllArgsConstructor;
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
public class VetVisitsController {

    private final VisitService visitService;

    private final UserService userService;

    @GetMapping("/vet")
    public String showVisits(Model model) {
        Long userId = visitService.getUserIdFromAuthentication(); // Pobierz identyfikator użytkownika zalogowanego

        List<Visit> vetVisits = visitService.findVisitsByDoctorId(userId);
        // Formatowanie daty i czasu za pomocą DateTimeFormatter
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");
        List<String> formattedDateTimes = new ArrayList<>();
        for (Visit visit : vetVisits) {
            String formattedDateTime = visit.getDateTime().format(formatter);
            formattedDateTimes.add(formattedDateTime);
        }
        model.addAttribute("vetVisits", vetVisits);
        model.addAttribute("formattedDateTimes",formattedDateTimes);
        return "VetVisitsView";
    }

    @GetMapping("/edit-visit-vet/{visitID}")
    public String showEditForm(@PathVariable long visitID, Model model) {
        Visit visitToEdit = visitService.findVisitById(visitID);
        Long userID = visitService.getUserIdFromAuthentication(); // Pobierz identyfikator użytkownika zalogowanego
        // Pobranie listy weterynarzy z bazy danych
        List<UserModel> vets = userService.findWithRole("ROLE_DOCTOR");
        if(Objects.equals(visitToEdit.getVet().getUserID(), userID)) {
            model.addAttribute("editedVisit", visitToEdit);
            model.addAttribute("visitId", visitID);
            model.addAttribute("vets", vets);
            return "VetEditVisit";
        }
        else
        {
            throw new IllegalStateException("Użytkownik nie ma uprawnień.");
        }
    }

    @PostMapping("/edit-visit-vet/{visitID}")
    public String editVisit(@PathVariable long visitID, @ModelAttribute("editedVisit") Visit editedVisit) {
        visitService.editVisit(visitID, editedVisit);
        return "redirect:/vet";

    }


    @PostMapping("/delete-visit-vet/{visitID}")
    public String deleteVisit(@PathVariable long visitID) {
        visitService.deleteVisitById(visitID);
        return "redirect:/vet"; // Przekierowanie na stronę z listą zamówień
    }
}

