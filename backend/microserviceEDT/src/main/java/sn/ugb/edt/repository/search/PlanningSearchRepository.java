package sn.ugb.edt.repository.search;

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
import sn.ugb.edt.domain.Planning;
import sn.ugb.edt.repository.PlanningRepository;

/**
 * Spring Data Elasticsearch repository for the {@link Planning} entity.
 */
public interface PlanningSearchRepository extends ElasticsearchRepository<Planning, Long>, PlanningSearchRepositoryInternal {}

interface PlanningSearchRepositoryInternal {
    Page<Planning> search(String query, Pageable pageable);

    Page<Planning> search(Query query);

    @Async
    void index(Planning entity);

    @Async
    void deleteFromIndexById(Long id);
}

class PlanningSearchRepositoryInternalImpl implements PlanningSearchRepositoryInternal {

    private final ElasticsearchTemplate elasticsearchTemplate;
    private final PlanningRepository repository;

    PlanningSearchRepositoryInternalImpl(ElasticsearchTemplate elasticsearchTemplate, PlanningRepository repository) {
        this.elasticsearchTemplate = elasticsearchTemplate;
        this.repository = repository;
    }

    @Override
    public Page<Planning> search(String query, Pageable pageable) {
        NativeQuery nativeQuery = new NativeQuery(QueryStringQuery.of(qs -> qs.query(query))._toQuery());
        return search(nativeQuery.setPageable(pageable));
    }

    @Override
    public Page<Planning> search(Query query) {
        SearchHits<Planning> searchHits = elasticsearchTemplate.search(query, Planning.class);
        List<Planning> hits = searchHits.map(SearchHit::getContent).stream().toList();
        return new PageImpl<>(hits, query.getPageable(), searchHits.getTotalHits());
    }

    @Override
    public void index(Planning entity) {
        repository.findById(entity.getId()).ifPresent(elasticsearchTemplate::save);
    }

    @Override
    public void deleteFromIndexById(Long id) {
        elasticsearchTemplate.delete(String.valueOf(id), Planning.class);
    }
}
