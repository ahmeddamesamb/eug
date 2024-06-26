package sn.ugb.gir.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import sn.ugb.gir.domain.Frais;

public interface FraisRepositoryWithBagRelationships {
    Optional<Frais> fetchBagRelationships(Optional<Frais> frais);

    List<Frais> fetchBagRelationships(List<Frais> frais);

    Page<Frais> fetchBagRelationships(Page<Frais> frais);
}
