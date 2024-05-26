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
import sn.ugb.gir.domain.Domaine;

/**
 * Utility repository to load bag relationships based on https://vladmihalcea.com/hibernate-multiplebagfetchexception/
 */
public class DomaineRepositoryWithBagRelationshipsImpl implements DomaineRepositoryWithBagRelationships {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Optional<Domaine> fetchBagRelationships(Optional<Domaine> domaine) {
        return domaine.map(this::fetchUfrs);
    }

    @Override
    public Page<Domaine> fetchBagRelationships(Page<Domaine> domaines) {
        return new PageImpl<>(fetchBagRelationships(domaines.getContent()), domaines.getPageable(), domaines.getTotalElements());
    }

    @Override
    public List<Domaine> fetchBagRelationships(List<Domaine> domaines) {
        return Optional.of(domaines).map(this::fetchUfrs).orElse(Collections.emptyList());
    }

    Domaine fetchUfrs(Domaine result) {
        return entityManager
            .createQuery("select domaine from Domaine domaine left join fetch domaine.ufrs where domaine.id = :id", Domaine.class)
            .setParameter("id", result.getId())
            .getSingleResult();
    }

    List<Domaine> fetchUfrs(List<Domaine> domaines) {
        HashMap<Object, Integer> order = new HashMap<>();
        IntStream.range(0, domaines.size()).forEach(index -> order.put(domaines.get(index).getId(), index));
        List<Domaine> result = entityManager
            .createQuery("select domaine from Domaine domaine left join fetch domaine.ufrs where domaine in :domaines", Domaine.class)
            .setParameter("domaines", domaines)
            .getResultList();
        Collections.sort(result, (o1, o2) -> Integer.compare(order.get(o1.getId()), order.get(o2.getId())));
        return result;
    }
}
