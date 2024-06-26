package sn.ugb.gir.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import sn.ugb.gir.domain.Pays;

public interface PaysRepositoryWithBagRelationships {
    Optional<Pays> fetchBagRelationships(Optional<Pays> pays);

    List<Pays> fetchBagRelationships(List<Pays> pays);

    Page<Pays> fetchBagRelationships(Page<Pays> pays);
}
