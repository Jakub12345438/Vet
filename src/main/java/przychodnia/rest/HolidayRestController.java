package przychodnia.rest;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class HolidayRestController {

    private static final String NAGER_API_URL = "https://date.nager.at/api/v3/publicholidays/{year}/{countryCode}";

    @GetMapping("/api/holidays/{year}")
    public ResponseEntity<?> getHolidays(@PathVariable Integer year) {
        if (year < java.time.Year.now().getValue()) {
            return ResponseEntity.badRequest().body("Podany rok musi być obecny lub późniejszy.");
        }

        // Przygotowanie URL do Nager.Date API
        String url = NAGER_API_URL
                .replace("{year}", String.valueOf(year))
                .replace("{countryCode}", "PL"); // Używamy kodu kraju PL dla Polski

        try {
            RestTemplate restTemplate = new RestTemplate();
            ResponseEntity<List> response = restTemplate.getForEntity(url, List.class);

            if (response.getStatusCode().is2xxSuccessful()) {
                List<Map<String, Object>> holidays = response.getBody();
                if (holidays != null) {
                    List<String> holidayDates = new ArrayList<>();

                    // Iteracja przez listę świąt
                    for (Map<String, Object> holiday : holidays) {
                        String date = (String) holiday.get("date");
                        holidayDates.add(date); // Dodajemy daty w formacie ISO
                    }

                    return ResponseEntity.ok(holidayDates);
                } else {
                    return ResponseEntity.status(500).body("Struktura odpowiedzi API jest nieprawidłowa.");
                }
            }  else {
                return ResponseEntity.status(response.getStatusCode()).body("Nie udało się otrzymać obiektów świąt z API.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Wystąpił błąd podczas pozyskiwania danych o świętach: " + e.getMessage());
        }
    }

    @GetMapping("/api/holidays-with-names/{year}")
    public ResponseEntity<?> getHolidaysWithNames(@PathVariable Integer year) {
        if (year < java.time.Year.now().getValue()) {
            return ResponseEntity.badRequest().body("Podany rok musi być obecny lub późniejszy.");
        }

        // Przygotowanie URL do Nager.Date API
        String url = NAGER_API_URL
                .replace("{year}", String.valueOf(year))
                .replace("{countryCode}", "PL"); // Używamy kodu kraju PL dla Polski

        try {
            RestTemplate restTemplate = new RestTemplate();
            ResponseEntity<List> response = restTemplate.getForEntity(url, List.class);

            if (response.getStatusCode().is2xxSuccessful()) {
                List<Map<String, Object>> holidays = response.getBody();
                if (holidays != null) {
                    List<Map<String, String>> holidayDetails = new ArrayList<>();

                    // Iteracja przez listę świąt
                    for (Map<String, Object> holiday : holidays) {
                        String date = (String) holiday.get("date");
                        String localName = (String) holiday.get("localName"); // Pobieranie lokalnej nazwy

                        Map<String, String> holidayDetail = new HashMap<>();
                        holidayDetail.put("date", date);
                        holidayDetail.put("name", localName);

                        holidayDetails.add(holidayDetail);
                    }

                    return ResponseEntity.ok(holidayDetails);
                } else {
                    return ResponseEntity.status(500).body("Struktura odpowiedzi API jest nieprawidłowa.");
                }
            } else {
                return ResponseEntity.status(response.getStatusCode()).body("Nie udało się otrzymać obiektów świąt z API.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Wystąpił błąd podczas pozyskiwania danych o świętach: " + e.getMessage());
        }
    }

}
