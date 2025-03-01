package przychodnia.controllers;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import przychodnia.models.Holiday;
import przychodnia.models.UserModel;
import przychodnia.services.HolidayService;
import przychodnia.services.UserService;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Controller
@AllArgsConstructor
public class HolidayController {

    private final UserService userService;
    private final HolidayService holidayService;

    @GetMapping("/holidays")
    public String showAddHolidayForm(Model model) {
        // Lista weterynarzy
        List<UserModel> vets = userService.findWithRole("ROLE_DOCTOR");
        model.addAttribute("vets", vets);

        // Obiekt Holiday dla formularza
        model.addAttribute("holiday", new Holiday());
        return "AdminVetHolidaysManagement";
    }

    @PostMapping("/holidays/add")
    public String addHoliday(@ModelAttribute("holiday") Holiday holiday) {
            holidayService.addHoliday(holiday);
            return "redirect:/holidays";
    }

    @GetMapping("/holidays-vets")
    public String showHolidays(Model model) {
        List<Holiday> holidays = holidayService.findAllHolidays();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        List<String> formattedStartDates = new ArrayList<>();
        for (Holiday holiday : holidays) {
            String formattedStartDate = holiday.getStartDate().format(formatter);
            formattedStartDates.add(formattedStartDate);
        }
        model.addAttribute("holidays", holidays);
        model.addAttribute("formattedStartDates",formattedStartDates);
        return "AdminVetHolidays";
    }

    @PostMapping("/delete-holiday/{holidayID}")
    public String deleteHoliday(@PathVariable long holidayID) {
        holidayService.deleteHolidayById(holidayID);
        return "redirect:/holidays-vets";
    }


}

