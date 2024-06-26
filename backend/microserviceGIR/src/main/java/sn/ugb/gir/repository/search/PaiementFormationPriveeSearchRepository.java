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
import sn.ugb.gir.domain.PaiementFormationPrivee;
import sn.ugb.gir.repository.PaiementFormationPriveeRepository;

/**
 * Spring Data Elasticsearch repository for the {@link PaiementFormationPrivee} entity.
 */
public interface PaiementFormationPriveeSearchRepository
    extends ElasticsearchRepository<PaiementFormationPrivee, Long>, PaiementFormationPriveeSearchRepositoryInternal {}

interface PaiementFormationPriveeSearchRepositoryInternal {
    Page<PaiementFormationPrivee> search(String query, Pageable pageable);

    Page<PaiementFormationPrivee> search(Query query);

    @Async
    void index(PaiementFormationPrivee entity);

    @Async
    void deleteFromIndexById(Long id);
}

class PaiementFormationPriveeSearchRepositoryInternalImpl implements PaiementFormationPriveeSearchRepositoryInternal {

    private final ElasticsearchTemplate elasticsearchTemplate;
    private final PaiementFormationPriveeRepository repository;

    PaiementFormationPriveeSearchRepositoryInternalImpl(
        ElasticsearchTemplate elasticsearchTemplate,
        PaiementFormationPriveeRepository repository
    ) {
        this.elasticsearchTemplate = elasticsearchTemplate;
        this.repository = repository;
    }

    @Override
    public Page<PaiementFormationPrivee> search(String query, Pageable pageable) {
        NativeQuery nativeQuery = new NativeQuery(QueryStringQuery.of(qs -> qs.query(query))._toQuery());
        return search(nativeQuery.setPageable(pageable));
    }

    @Override
    public Page<PaiementFormationPrivee> search(Query query) {
        SearchHits<PaiementFormationPrivee> searchHits = elasticsearchTemplate.search(query, PaiementFormationPrivee.class);
        List<PaiementFormationPrivee> hits = searchHits.map(SearchHit::getContent).stream().toList();
        return new PageImpl<>(hits, query.getPageable(), searchHits.getTotalHits());
    }

    @Override
    public void index(PaiementFormationPrivee entity) {
        repository.findById(entity.getId()).ifPresent(elasticsearchTemplate::save);
    }

    @Override
    public void deleteFromIndexById(Long id) {
        elasticsearchTemplate.delete(String.valueOf(id), PaiementFormationPrivee.class);
    }
}
