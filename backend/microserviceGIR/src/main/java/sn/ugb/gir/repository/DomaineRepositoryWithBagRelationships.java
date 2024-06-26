package sn.ugb.gir.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import sn.ugb.gir.domain.Domaine;

public interface DomaineRepositoryWithBagRelationships {
    Optional<Domaine> fetchBagRelationships(Optional<Domaine> domaine);

    List<Domaine> fetchBagRelationships(List<Domaine> domaines);

    Page<Domaine> fetchBagRelationships(Page<Domaine> domaines);
}
