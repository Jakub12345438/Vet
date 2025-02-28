package przychodnia.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import przychodnia.models.Holiday;
import przychodnia.repositories.HolidayRepository;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;

@Service
public class HolidayService {

    private final HolidayRepository holidayRepository;


    @Autowired
    public HolidayService(HolidayRepository holidayRepository) {
        this.holidayRepository = holidayRepository;
    }

    public void addHoliday(Holiday holiday) {

        holiday.setStartDate(holiday.getStartDate());

        // Przypisanie lekarza do urlopu
        holiday.setVet(holiday.getVet());

        // Zapis urlopu do bazy danych
        holidayRepository.save(holiday);
    }

    public List<Holiday> findAllHolidays()
    {
        return holidayRepository.findAll();
    }

    public void deleteHolidayById(long holidayID) {
        holidayRepository.deleteById(holidayID);
    }

    public List<LocalDate> findBusyHolidayDatesForVet(Long vetId) {
        // Pobranie listy urlopów z repozytorium dla danego weterynarza
        List<Date> dates = holidayRepository.findBusyHolidayDatesForVet(vetId);
        return dates.stream()
                .map(Date::toLocalDate).toList();

    }

    public List<Holiday> findHolidaysForVet(Long vetId)
    {
        return holidayRepository.findHolidaysForVet(vetId);
    }

}

