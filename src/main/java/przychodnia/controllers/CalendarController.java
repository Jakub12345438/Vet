package przychodnia.controllers;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import przychodnia.models.Holiday;
import przychodnia.models.Visit;
import przychodnia.services.HolidayService;
import przychodnia.services.VisitService;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Objects;

@Controller
@AllArgsConstructor
public class CalendarController {

    private VisitService visitService;

    private HolidayService holidayService;

    @GetMapping("/visits-vet")
    public String getVetVisits(Model model) {
        Long userId = visitService.getUserIdFromAuthentication(); // Pobierz identyfikator użytkownika zalogowanego

        List<Visit> vetVisits = visitService.findVisitsByDoctorId(userId);
        model.addAttribute("vetVisits", vetVisits);
        return "vet-visits"; // Widok szczegółów wizyty
    }

    @GetMapping("/visit/details/{id}")
    public String getVisitDetails(@PathVariable Long id, Model model) {
        Visit visit = visitService.findVisitById(id);
        Long userID = visitService.getUserIdFromAuthentication(); // Pobierz identyfikator użytkownika zalogowanego
        if(Objects.equals(visit.getVet().getUserID(), userID)) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");
            String formattedDateTime = visit.getDateTime().format(formatter);
            model.addAttribute("visit", visit);
            model.addAttribute("visitId", id);
            model.addAttribute("formattedDateTime", formattedDateTime);
            return "VisitDetailsUser"; // Widok z detalami wizyty
        }
        else
        {
            throw new IllegalStateException("Użytkownik nie ma uprawnień.");
        }
    }

    @GetMapping("/vet-holidays")
    public String getVetHolidays(Model model)
    {
        Long userId = visitService.getUserIdFromAuthentication(); // Pobierz identyfikator użytkownika zalogowanego
        List<Holiday> vetHolidays = holidayService.findHolidaysForVet(userId);
        model.addAttribute("vetHolidays", vetHolidays);
        return "VetHolidays";
    }

}

