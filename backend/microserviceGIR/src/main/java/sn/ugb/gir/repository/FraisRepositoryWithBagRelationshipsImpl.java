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
import sn.ugb.gir.domain.Frais;

/**
 * Utility repository to load bag relationships based on https://vladmihalcea.com/hibernate-multiplebagfetchexception/
 */
public class FraisRepositoryWithBagRelationshipsImpl implements FraisRepositoryWithBagRelationships {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Optional<Frais> fetchBagRelationships(Optional<Frais> frais) {
        return frais.map(this::fetchUniversites);
    }

    @Override
    public Page<Frais> fetchBagRelationships(Page<Frais> frais) {
        return new PageImpl<>(fetchBagRelationships(frais.getContent()), frais.getPageable(), frais.getTotalElements());
    }

    @Override
    public List<Frais> fetchBagRelationships(List<Frais> frais) {
        return Optional.of(frais).map(this::fetchUniversites).orElse(Collections.emptyList());
    }

    Frais fetchUniversites(Frais result) {
        return entityManager
            .createQuery("select frais from Frais frais left join fetch frais.universites where frais.id = :id", Frais.class)
            .setParameter("id", result.getId())
            .getSingleResult();
    }

    List<Frais> fetchUniversites(List<Frais> frais) {
        HashMap<Object, Integer> order = new HashMap<>();
        IntStream.range(0, frais.size()).forEach(index -> order.put(frais.get(index).getId(), index));
        List<Frais> result = entityManager
            .createQuery("select frais from Frais frais left join fetch frais.universites where frais in :frais", Frais.class)
            .setParameter("frais", frais)
            .getResultList();
        Collections.sort(result, (o1, o2) -> Integer.compare(order.get(o1.getId()), order.get(o2.getId())));
        return result;
    }
}
