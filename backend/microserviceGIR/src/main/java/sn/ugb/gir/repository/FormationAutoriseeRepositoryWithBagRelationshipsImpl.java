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
import sn.ugb.gir.domain.FormationAutorisee;

/**
 * Utility repository to load bag relationships based on https://vladmihalcea.com/hibernate-multiplebagfetchexception/
 */
public class FormationAutoriseeRepositoryWithBagRelationshipsImpl implements FormationAutoriseeRepositoryWithBagRelationships {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Optional<FormationAutorisee> fetchBagRelationships(Optional<FormationAutorisee> formationAutorisee) {
        return formationAutorisee.map(this::fetchFormations);
    }

    @Override
    public Page<FormationAutorisee> fetchBagRelationships(Page<FormationAutorisee> formationAutorisees) {
        return new PageImpl<>(
            fetchBagRelationships(formationAutorisees.getContent()),
            formationAutorisees.getPageable(),
            formationAutorisees.getTotalElements()
        );
    }

    @Override
    public List<FormationAutorisee> fetchBagRelationships(List<FormationAutorisee> formationAutorisees) {
        return Optional.of(formationAutorisees).map(this::fetchFormations).orElse(Collections.emptyList());
    }

    FormationAutorisee fetchFormations(FormationAutorisee result) {
        return entityManager
            .createQuery(
                "select formationAutorisee from FormationAutorisee formationAutorisee left join fetch formationAutorisee.formations where formationAutorisee.id = :id",
                FormationAutorisee.class
            )
            .setParameter("id", result.getId())
            .getSingleResult();
    }

    List<FormationAutorisee> fetchFormations(List<FormationAutorisee> formationAutorisees) {
        HashMap<Object, Integer> order = new HashMap<>();
        IntStream.range(0, formationAutorisees.size()).forEach(index -> order.put(formationAutorisees.get(index).getId(), index));
        List<FormationAutorisee> result = entityManager
            .createQuery(
                "select formationAutorisee from FormationAutorisee formationAutorisee left join fetch formationAutorisee.formations where formationAutorisee in :formationAutorisees",
                FormationAutorisee.class
            )
            .setParameter("formationAutorisees", formationAutorisees)
            .getResultList();
        Collections.sort(result, (o1, o2) -> Integer.compare(order.get(o1.getId()), order.get(o2.getId())));
        return result;
    }
}
