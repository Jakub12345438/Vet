package przychodnia.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import przychodnia.models.Holiday;

import java.sql.Date;
import java.util.List;

@Repository
public interface HolidayRepository extends JpaRepository<Holiday, Long> {

    @Query("SELECT FUNCTION('DATE',h.startDate) FROM Holiday h WHERE h.vet.userID = :vetId")
    List<Date> findBusyHolidayDatesForVet(@Param("vetId") Long vetId);

    @Query("SELECT h FROM Holiday h WHERE h.vet.userID = :vetId")
    List<Holiday> findHolidaysForVet(@Param("vetId") Long vetId);

}

