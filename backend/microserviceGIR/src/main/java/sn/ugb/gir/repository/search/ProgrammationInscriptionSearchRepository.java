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
import sn.ugb.gir.domain.ProgrammationInscription;
import sn.ugb.gir.repository.ProgrammationInscriptionRepository;

/**
 * Spring Data Elasticsearch repository for the {@link ProgrammationInscription} entity.
 */
public interface ProgrammationInscriptionSearchRepository
    extends ElasticsearchRepository<ProgrammationInscription, Long>, ProgrammationInscriptionSearchRepositoryInternal {}

interface ProgrammationInscriptionSearchRepositoryInternal {
    Page<ProgrammationInscription> search(String query, Pageable pageable);

    Page<ProgrammationInscription> search(Query query);

    @Async
    void index(ProgrammationInscription entity);

    @Async
    void deleteFromIndexById(Long id);
}

class ProgrammationInscriptionSearchRepositoryInternalImpl implements ProgrammationInscriptionSearchRepositoryInternal {

    private final ElasticsearchTemplate elasticsearchTemplate;
    private final ProgrammationInscriptionRepository repository;

    ProgrammationInscriptionSearchRepositoryInternalImpl(
        ElasticsearchTemplate elasticsearchTemplate,
        ProgrammationInscriptionRepository repository
    ) {
        this.elasticsearchTemplate = elasticsearchTemplate;
        this.repository = repository;
    }

    @Override
    public Page<ProgrammationInscription> search(String query, Pageable pageable) {
        NativeQuery nativeQuery = new NativeQuery(QueryStringQuery.of(qs -> qs.query(query))._toQuery());
        return search(nativeQuery.setPageable(pageable));
    }

    @Override
    public Page<ProgrammationInscription> search(Query query) {
        SearchHits<ProgrammationInscription> searchHits = elasticsearchTemplate.search(query, ProgrammationInscription.class);
        List<ProgrammationInscription> hits = searchHits.map(SearchHit::getContent).stream().toList();
        return new PageImpl<>(hits, query.getPageable(), searchHits.getTotalHits());
    }

    @Override
    public void index(ProgrammationInscription entity) {
        repository.findById(entity.getId()).ifPresent(elasticsearchTemplate::save);
    }

    @Override
    public void deleteFromIndexById(Long id) {
        elasticsearchTemplate.delete(String.valueOf(id), ProgrammationInscription.class);
    }
}
