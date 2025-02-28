package przychodnia.rest;

import lombok.AllArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import przychodnia.models.Visit;
import przychodnia.services.HolidayService;
import przychodnia.services.VisitService;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@AllArgsConstructor
public class UserVisitsRestController {

    private final VisitService visitService;

    private final HolidayService holidayService;

    @GetMapping("/api/user-visits/{userId}/{year}")
    public ResponseEntity<List<LocalDate>> getUserVisits(@PathVariable Long userId, @PathVariable int year) {
        List<Visit> visits = visitService.getUserVisitsForYear(userId, year);
        List<LocalDate> visitDates = visits.stream()
                .map(visit -> visit.getDateTime().toLocalDate()) // Konwersja na format ISO
                .collect(Collectors.toList());
        return ResponseEntity.ok(visitDates);
    }

   @GetMapping("/api/vets/busy-times/{vetId}/{date}")
    public ResponseEntity<List<String>> getVetBusyTimes(@PathVariable Long vetId, @PathVariable @DateTimeFormat(pattern = "dd-MM-YYYY") LocalDate date) {
        List<LocalTime> busyTimes = visitService.getBusyTimesForVetAndDate(vetId, date);
       // Konwertuj LocalTime na format HH:mm
       List<String> formattedBusyTimes = busyTimes.stream()
               .map(time -> time.format(DateTimeFormatter.ofPattern("HH:mm")))
               .collect(Collectors.toList());

       return ResponseEntity.ok(formattedBusyTimes);
    }

    @GetMapping("/api/vets/busy-dates/{vetId}")
    public ResponseEntity<List<String>> getVetBusyDates(@PathVariable Long vetId) {
        List<LocalDate> busyDates = visitService.getBusyDatesForVet(vetId);
        // Konwertuj LocalTime na format HH:mm
        List<String> formattedBusyDates = busyDates.stream()
                .map(date -> date.format(DateTimeFormatter.ofPattern("dd.MM.yyyy")))
                .collect(Collectors.toList());

        return ResponseEntity.ok(formattedBusyDates);
    }

    @GetMapping("/api/vets/holidays/{vetId}")
    public ResponseEntity<List<String>> getVetHolidays(@PathVariable Long vetId) {
        List<LocalDate> holidays = holidayService.findBusyHolidayDatesForVet(vetId);
        // Konwertuj LocalDate na format "dd.MM.yyyy"
        List<String> formattedHolidays = holidays.stream()
                .map(date -> date.format(DateTimeFormatter.ofPattern("dd.MM.yyyy")))
                .collect(Collectors.toList());

        return ResponseEntity.ok(formattedHolidays);
    }

}

