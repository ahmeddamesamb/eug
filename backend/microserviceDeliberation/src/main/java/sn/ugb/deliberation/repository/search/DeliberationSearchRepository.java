package sn.ugb.deliberation.repository.search;

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
import sn.ugb.deliberation.domain.Deliberation;
import sn.ugb.deliberation.repository.DeliberationRepository;

/**
 * Spring Data Elasticsearch repository for the {@link Deliberation} entity.
 */
public interface DeliberationSearchRepository extends ElasticsearchRepository<Deliberation, Long>, DeliberationSearchRepositoryInternal {}

interface DeliberationSearchRepositoryInternal {
    Page<Deliberation> search(String query, Pageable pageable);

    Page<Deliberation> search(Query query);

    @Async
    void index(Deliberation entity);

    @Async
    void deleteFromIndexById(Long id);
}

class DeliberationSearchRepositoryInternalImpl implements DeliberationSearchRepositoryInternal {

    private final ElasticsearchTemplate elasticsearchTemplate;
    private final DeliberationRepository repository;

    DeliberationSearchRepositoryInternalImpl(ElasticsearchTemplate elasticsearchTemplate, DeliberationRepository repository) {
        this.elasticsearchTemplate = elasticsearchTemplate;
        this.repository = repository;
    }

    @Override
    public Page<Deliberation> search(String query, Pageable pageable) {
        NativeQuery nativeQuery = new NativeQuery(QueryStringQuery.of(qs -> qs.query(query))._toQuery());
        return search(nativeQuery.setPageable(pageable));
    }

    @Override
    public Page<Deliberation> search(Query query) {
        SearchHits<Deliberation> searchHits = elasticsearchTemplate.search(query, Deliberation.class);
        List<Deliberation> hits = searchHits.map(SearchHit::getContent).stream().toList();
        return new PageImpl<>(hits, query.getPageable(), searchHits.getTotalHits());
    }

    @Override
    public void index(Deliberation entity) {
        repository.findById(entity.getId()).ifPresent(elasticsearchTemplate::save);
    }

    @Override
    public void deleteFromIndexById(Long id) {
        elasticsearchTemplate.delete(String.valueOf(id), Deliberation.class);
    }
}
