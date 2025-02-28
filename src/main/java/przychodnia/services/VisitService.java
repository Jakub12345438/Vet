package przychodnia.services;

import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import przychodnia.models.UserModel;
import przychodnia.models.Visit;
import przychodnia.repositories.UserRepository;
import przychodnia.repositories.VisitRepository;

import java.sql.Date;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class VisitService {

    private final VisitRepository visitRepository;

    private final UserRepository userRepository;

    public void addVisit(Visit visitDto) {
        // Pobierz zalogowanego użytkownika
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userEmail = authentication.getName();
        UserModel userModel = userRepository.findByEmail(userEmail);

        // Tworzenie nowego obiektu Visit
        Visit visit = new Visit();
        visit.setClientName(visitDto.getClientName());
        visit.setClientSurname(visitDto.getClientSurname());
        visit.setAnimalName(visitDto.getAnimalName());
        visit.setAnimal(visitDto.getAnimal());
        visit.setGender(visitDto.getGender());
        visit.setVet(visitDto.getVet());
        visit.setDateTime(visitDto.getDateTime());
        visit.setDescription(visitDto.getDescription());
        visit.setClientPhoneNumber(visitDto.getClientPhoneNumber());
        visit.setVetPhoneNumber(visitDto.getVetPhoneNumber());

        // Przypisz obiekt UserModel do pola userModel w Visit
        visit.setUserModel(userModel);

        // Zapisz nową wizytę
        visitRepository.save(visit);
    }

    public List<Visit> findAllVisits() {
        return visitRepository.findAll();
    }

    public void deleteVisitById(long visitID) {
        visitRepository.deleteById(visitID);
    }

    public Visit findVisitById(long visitID) {
        Optional<Visit> visitOptional = visitRepository.findById(visitID);
        return visitOptional.orElseThrow(() -> new EntityNotFoundException("Wizyta o podanym ID nie istnieje: " + visitID));
    }

    public void editVisit(long visitID, Visit editedVisit) {
        Visit existingVisit = findVisitById(visitID);

        // Edytuj odpowiednie pola
        existingVisit.setClientName(editedVisit.getClientName());
        existingVisit.setClientSurname(editedVisit.getClientSurname());
        existingVisit.setAnimalName(editedVisit.getAnimalName());
        existingVisit.setAnimal(editedVisit.getAnimal());
        existingVisit.setGender(editedVisit.getGender());
        existingVisit.setVet(editedVisit.getVet());
        existingVisit.setDateTime(editedVisit.getDateTime());
        existingVisit.setDescription(editedVisit.getDescription());
        existingVisit.setClientPhoneNumber(editedVisit.getClientPhoneNumber());
        existingVisit.setVetPhoneNumber(editedVisit.getVetPhoneNumber());
        // Zapisz zmiany
        visitRepository.save(existingVisit);
    }


    public Long getUserIdFromAuthentication() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userEmail= authentication.getName();
        UserModel userModel = userRepository.findByEmail(userEmail);
        if (userModel != null) {
            return userModel.getUserID();
        } else {
            throw new IllegalStateException("Użytkownik nie jest zalogowany.");
        }
    }

    public List<Visit> findVisitsByUserId(Long userId) {
        return visitRepository.findByUserId(userId);
    }

    public List<Visit> findVisitsByDoctorId(Long userId) {

        return visitRepository.findByVetId(userId);
    }

    public List<Visit> getUserVisitsForYear(Long userId, int year) {

        // Pobierz wizyty użytkownika w podanym zakresie czasu
        return visitRepository.findVisitsForUserAndYear(userId, year);
    }

    public List<LocalTime> getBusyTimesForVetAndDate(Long vetId, LocalDate date) {
        List<LocalDateTime> dateTimes = visitRepository.findBusyTimesForVetAndDate(vetId, date);
        return dateTimes.stream().map(LocalDateTime::toLocalTime).collect(Collectors.toList());
    }

    public List<LocalDate> getBusyDatesForVet(Long vetId)
    {
        List<Date> dates = visitRepository.findBusyDatesForVet(vetId);
        return dates.stream()
                .map(Date::toLocalDate).toList();
    }

}