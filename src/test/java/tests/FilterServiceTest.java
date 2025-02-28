package tests;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import przychodnia.models.UserModel;
import przychodnia.models.Visit;
import przychodnia.services.FilterService;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(MockitoExtension.class)
public class FilterServiceTest {

    private FilterService filterService;

    @BeforeEach
    public void setUp() {
        // Tworzymy instancję FilterService
        filterService = new FilterService(null); // Przekazujemy null, bo nie testujemy tutaj VisitService
    }

    @Test
    public void testIsVisitExist_True() {
        // Przygotowanie danych
        UserModel vet = new UserModel(1L, "Artur", "Nowak");
        LocalDateTime visitTime = LocalDateTime.now();

        Visit visit = new Visit();
        visit.setVet(vet);
        visit.setDateTime(visitTime);

        // Testowanie poprawnego dopasowania
        boolean result = filterService.isVisitExist(visit, vet, visitTime);

        // Oczekujemy, że zwróci true, ponieważ doktor i czas są takie same
        assertTrue(result);
    }

    @Test
    public void testIsVisitExist_False_DifferentDoctor() {
        // Przygotowanie danych
        UserModel vet1 = new UserModel(1L, "Artur", "Nowak");
        UserModel vet2 = new UserModel(2L, "Andrzej", "Lewandowski");
        LocalDateTime visitTime = LocalDateTime.now();

        Visit visit = new Visit();
        visit.setVet(vet1);
        visit.setDateTime(visitTime);

        // Testowanie niepoprawnego dopasowania z różnymi doktorami
        boolean result = filterService.isVisitExist(visit, vet2, visitTime);

        // Oczekujemy, że zwróci false, ponieważ doktorzy są różni
        assertFalse(result);
    }

    @Test
    public void testIsVisitExist_False_DifferentTime() {
        // Przygotowanie danych
        UserModel vet = new UserModel(1L, "Artur", "Nowak");
        LocalDateTime visitTime1 = LocalDateTime.now();
        LocalDateTime visitTime2 = visitTime1.plusHours(1); // Inny czas

        Visit visit = new Visit();
        visit.setVet(vet);
        visit.setDateTime(visitTime1);

        // Testowanie niepoprawnego dopasowania z różnymi czasami
        boolean result = filterService.isVisitExist(visit, vet, visitTime2);

        // Oczekujemy, że zwróci false, ponieważ czasy są różne
        assertFalse(result);
    }

    @Test
    public void testIsVisitExist_False_DifferentDoctorAndTime() {
        // Przygotowanie danych
        UserModel vet1 = new UserModel(1L, "Artur", "Nowak");
        UserModel vet2 = new UserModel(2L, "Andrzej", "Lewandowski"); // Inny doktor
        LocalDateTime visitTime1 = LocalDateTime.now();
        LocalDateTime visitTime2 = visitTime1.plusHours(1); // Inny czas

        Visit visit = new Visit();
        visit.setVet(vet1);
        visit.setDateTime(visitTime1);

        // Testowanie niepoprawnego dopasowania z różnymi doktorami i czasami
        boolean result = filterService.isVisitExist(visit, vet2, visitTime2);

        // Oczekujemy, że zwróci false, ponieważ doktor i czas są różne
        assertFalse(result);
    }

}
