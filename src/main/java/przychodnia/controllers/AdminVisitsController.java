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

@Controller
@AllArgsConstructor
public class AdminVisitsController {

    private final VisitService visitService;

    private final UserService userService;

    @GetMapping("/admin")
    public String showVisits(Model model) {
        List<Visit> visits = visitService.findAllVisits();
        // Formatowanie daty i czasu za pomocą DateTimeFormatter
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");
        List<String> formattedDateTimes = new ArrayList<>();
        for (Visit visit : visits) {
            String formattedDateTime = visit.getDateTime().format(formatter);
            formattedDateTimes.add(formattedDateTime);
        }
        model.addAttribute("listVisits", visits);
        model.addAttribute("formattedDateTimes",formattedDateTimes);
        return "AdminVisitsView";
    }

    @GetMapping("/edit-visit/{visitID}")
    public String showEditForm(@PathVariable long visitID, Model model) {
        Visit visitToEdit = visitService.findVisitById(visitID);
        // Pobranie listy weterynarzy z bazy danych
        List<UserModel> vets = userService.findWithRole("ROLE_DOCTOR");
        model.addAttribute("editedVisit", visitToEdit);
        model.addAttribute("visitId", visitID);
        model.addAttribute("vets", vets);
        return "AdminEditVisit";
    }

    @PostMapping("/edit-visit/{visitID}")
    public String editVisit(@PathVariable long visitID, @ModelAttribute("editedVisit") Visit editedVisit) {
        visitService.editVisit(visitID, editedVisit);
        return "redirect:/admin";

    }


    @PostMapping("/delete-visit/{visitID}")
    public String deleteVisit(@PathVariable long visitID) {
        visitService.deleteVisitById(visitID);
        return "redirect:/admin"; // Przekierowanie na stronę z listą zamówień
    }
}
