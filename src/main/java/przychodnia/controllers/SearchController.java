package przychodnia.controllers;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import przychodnia.dtos.SearchForm;
import przychodnia.models.UserModel;
import przychodnia.models.Visit;
import przychodnia.services.FilterService;
import przychodnia.services.UserService;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Controller
@AllArgsConstructor
public class SearchController {

    private final FilterService filterService;

    private final UserService userService;

    @GetMapping("/search")
    public String showSearchForm(Model model)
    {
        List<UserModel> doctors = userService.findWithRole("ROLE_DOCTOR");
        model.addAttribute("doctors",doctors);
        model.addAttribute("searchForm", new SearchForm());
        model.addAttribute("foundVisits", new ArrayList<Visit>());
        model.addAttribute("searchPerformed",false);
        return "AdminVisitSearchView";
    }

    @PostMapping("/search")
    public String searchVisits(@ModelAttribute("searchForm") SearchForm searchForm, Model model) {
        List<Visit> allVisits = filterService.findSelectedVisits(searchForm.getVet(),  searchForm.getLocalDateTime());

        // Formatowanie daty i godziny
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");
        List<String> formattedDateTimes = new ArrayList<>();
        for (Visit visit : allVisits) {
            String formattedDateTime = visit.getDateTime().format(formatter);
            formattedDateTimes.add(formattedDateTime);
        }

        model.addAttribute("allVisits", allVisits);
        model.addAttribute("formattedDateTimes", formattedDateTimes);
        model.addAttribute("searchPerformed", true);
        return "AdminVisitSearchView";
    }


}