package sn.ugb.gir.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import sn.ugb.gir.domain.Pays;

/**
 * Utility repository to load bag relationships based on https://vladmihalcea.com/hibernate-multiplebagfetchexception/
 */
public class PaysRepositoryWithBagRelationshipsImpl implements PaysRepositoryWithBagRelationships {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Optional<Pays> fetchBagRelationships(Optional<Pays> pays) {
        return pays.map(this::fetchZones);
    }

    @Override
    public Page<Pays> fetchBagRelationships(Page<Pays> pays) {
        return new PageImpl<>(fetchBagRelationships(pays.getContent()), pays.getPageable(), pays.getTotalElements());
    }

    @Override
    public List<Pays> fetchBagRelationships(List<Pays> pays) {
        return Optional.of(pays).map(this::fetchZones).orElse(Collections.emptyList());
    }

    Pays fetchZones(Pays result) {
        return entityManager
            .createQuery("select pays from Pays pays left join fetch pays.zones where pays.id = :id", Pays.class)
            .setParameter("id", result.getId())
            .getSingleResult();
    }

    List<Pays> fetchZones(List<Pays> pays) {
        HashMap<Object, Integer> order = new HashMap<>();
        IntStream.range(0, pays.size()).forEach(index -> order.put(pays.get(index).getId(), index));
        List<Pays> result = entityManager
            .createQuery("select pays from Pays pays left join fetch pays.zones where pays in :pays", Pays.class)
            .setParameter("pays", pays)
            .getResultList();
        Collections.sort(result, (o1, o2) -> Integer.compare(order.get(o1.getId()), order.get(o2.getId())));
        return result;
    }
}
