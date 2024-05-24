package sn.ugb.gir.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import sn.ugb.gir.domain.FormationAutorisee;

public interface FormationAutoriseeRepositoryWithBagRelationships {
    Optional<FormationAutorisee> fetchBagRelationships(Optional<FormationAutorisee> formationAutorisee);

    List<FormationAutorisee> fetchBagRelationships(List<FormationAutorisee> formationAutorisees);

    Page<FormationAutorisee> fetchBagRelationships(Page<FormationAutorisee> formationAutorisees);
}
