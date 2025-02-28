package przychodnia.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import przychodnia.models.Visit;

import java.sql.Date;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;


@Repository
public interface VisitRepository extends JpaRepository<Visit, Long> {

    @Query("SELECT v FROM Visit v WHERE v.userModel.userID = :userId")
    List<Visit> findByUserId(@Param("userId") Long userId);

    @Query("SELECT v FROM Visit v WHERE v.vet.userID = :vetId")
    List<Visit> findByVetId(@Param("vetId") Long vetId);

    @Query("SELECT v FROM Visit v WHERE v.userModel.email = :email")
    List<Visit> findVisitsByEmail(@Param("email") String email); // Find visits by e-mail

    @Query("SELECT v FROM Visit v WHERE v.userModel.userID = :userId AND YEAR(v.dateTime) = :year")
    List<Visit> findVisitsForUserAndYear(@Param("userId") Long userId, @Param("year") int year);

    @Query("SELECT v.dateTime FROM Visit v WHERE v.vet.userID = :vetId AND FUNCTION('DATE', v.dateTime) = :date")
    List<LocalDateTime> findBusyTimesForVetAndDate(@Param("vetId") Long vetId, @Param("date") LocalDate date);

    @Query("SELECT FUNCTION('DATE',v.dateTime) FROM Visit v WHERE v.vet.userID = :vetId")
    List<Date> findBusyDatesForVet(@Param("vetId") Long vetId);

}




