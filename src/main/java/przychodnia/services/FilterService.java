package przychodnia.services;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import przychodnia.models.UserModel;
import przychodnia.models.Visit;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class FilterService {

    private final VisitService visitService;

    public boolean isVisitExist(Visit visit, UserModel vet, LocalDateTime localDateTime)
    {
        return visit.getVet().equals(vet) && visit.getDateTime().isEqual(localDateTime);
    }

    public List<Visit> findSelectedVisits(UserModel vet, LocalDateTime localDateTime)
    {
        List<Visit> allVisits = visitService.findAllVisits();

        return allVisits.stream()
                .filter(visit -> isVisitExist(visit, vet, localDateTime))
                .collect(Collectors.toList());
    }

}

