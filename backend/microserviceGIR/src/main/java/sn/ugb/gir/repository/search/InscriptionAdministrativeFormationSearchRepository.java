package sn.ugb.gir.repository.search;

import co.elastic.clients.elasticsearch._types.query_dsl.QueryStringQuery;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.client.elc.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.client.elc.NativeQuery;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.query.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.scheduling.annotation.Async;
import sn.ugb.gir.domain.InscriptionAdministrativeFormation;
import sn.ugb.gir.repository.InscriptionAdministrativeFormationRepository;

/**
 * Spring Data Elasticsearch repository for the {@link InscriptionAdministrativeFormation} entity.
 */
public interface InscriptionAdministrativeFormationSearchRepository
    extends ElasticsearchRepository<InscriptionAdministrativeFormation, Long>, InscriptionAdministrativeFormationSearchRepositoryInternal {}

interface InscriptionAdministrativeFormationSearchRepositoryInternal {
    Page<InscriptionAdministrativeFormation> search(String query, Pageable pageable);

    Page<InscriptionAdministrativeFormation> search(Query query);

    @Async
    void index(InscriptionAdministrativeFormation entity);

    @Async
    void deleteFromIndexById(Long id);
}

class InscriptionAdministrativeFormationSearchRepositoryInternalImpl implements InscriptionAdministrativeFormationSearchRepositoryInternal {

    private final ElasticsearchTemplate elasticsearchTemplate;
    private final InscriptionAdministrativeFormationRepository repository;

    InscriptionAdministrativeFormationSearchRepositoryInternalImpl(
        ElasticsearchTemplate elasticsearchTemplate,
        InscriptionAdministrativeFormationRepository repository
    ) {
        this.elasticsearchTemplate = elasticsearchTemplate;
        this.repository = repository;
    }

    @Override
    public Page<InscriptionAdministrativeFormation> search(String query, Pageable pageable) {
        NativeQuery nativeQuery = new NativeQuery(QueryStringQuery.of(qs -> qs.query(query))._toQuery());
        return search(nativeQuery.setPageable(pageable));
    }

    @Override
    public Page<InscriptionAdministrativeFormation> search(Query query) {
        SearchHits<InscriptionAdministrativeFormation> searchHits = elasticsearchTemplate.search(
            query,
            InscriptionAdministrativeFormation.class
        );
        List<InscriptionAdministrativeFormation> hits = searchHits.map(SearchHit::getContent).stream().toList();
        return new PageImpl<>(hits, query.getPageable(), searchHits.getTotalHits());
    }

    @Override
    public void index(InscriptionAdministrativeFormation entity) {
        repository.findById(entity.getId()).ifPresent(elasticsearchTemplate::save);
    }

    @Override
    public void deleteFromIndexById(Long id) {
        elasticsearchTemplate.delete(String.valueOf(id), InscriptionAdministrativeFormation.class);
    }
}
