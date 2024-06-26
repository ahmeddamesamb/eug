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
import sn.ugb.gir.domain.Cycle;
import sn.ugb.gir.repository.CycleRepository;

/**
 * Spring Data Elasticsearch repository for the {@link Cycle} entity.
 */
public interface CycleSearchRepository extends ElasticsearchRepository<Cycle, Long>, CycleSearchRepositoryInternal {}

interface CycleSearchRepositoryInternal {
    Page<Cycle> search(String query, Pageable pageable);

    Page<Cycle> search(Query query);

    @Async
    void index(Cycle entity);

    @Async
    void deleteFromIndexById(Long id);
}

class CycleSearchRepositoryInternalImpl implements CycleSearchRepositoryInternal {

    private final ElasticsearchTemplate elasticsearchTemplate;
    private final CycleRepository repository;

    CycleSearchRepositoryInternalImpl(ElasticsearchTemplate elasticsearchTemplate, CycleRepository repository) {
        this.elasticsearchTemplate = elasticsearchTemplate;
        this.repository = repository;
    }

    @Override
    public Page<Cycle> search(String query, Pageable pageable) {
        NativeQuery nativeQuery = new NativeQuery(QueryStringQuery.of(qs -> qs.query(query))._toQuery());
        return search(nativeQuery.setPageable(pageable));
    }

    @Override
    public Page<Cycle> search(Query query) {
        SearchHits<Cycle> searchHits = elasticsearchTemplate.search(query, Cycle.class);
        List<Cycle> hits = searchHits.map(SearchHit::getContent).stream().toList();
        return new PageImpl<>(hits, query.getPageable(), searchHits.getTotalHits());
    }

    @Override
    public void index(Cycle entity) {
        repository.findById(entity.getId()).ifPresent(elasticsearchTemplate::save);
    }

    @Override
    public void deleteFromIndexById(Long id) {
        elasticsearchTemplate.delete(String.valueOf(id), Cycle.class);
    }
}
