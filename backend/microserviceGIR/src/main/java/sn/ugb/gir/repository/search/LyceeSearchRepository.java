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
import sn.ugb.gir.domain.Lycee;
import sn.ugb.gir.repository.LyceeRepository;

/**
 * Spring Data Elasticsearch repository for the {@link Lycee} entity.
 */
public interface LyceeSearchRepository extends ElasticsearchRepository<Lycee, Long>, LyceeSearchRepositoryInternal {}

interface LyceeSearchRepositoryInternal {
    Page<Lycee> search(String query, Pageable pageable);

    Page<Lycee> search(Query query);

    @Async
    void index(Lycee entity);

    @Async
    void deleteFromIndexById(Long id);
}

class LyceeSearchRepositoryInternalImpl implements LyceeSearchRepositoryInternal {

    private final ElasticsearchTemplate elasticsearchTemplate;
    private final LyceeRepository repository;

    LyceeSearchRepositoryInternalImpl(ElasticsearchTemplate elasticsearchTemplate, LyceeRepository repository) {
        this.elasticsearchTemplate = elasticsearchTemplate;
        this.repository = repository;
    }

    @Override
    public Page<Lycee> search(String query, Pageable pageable) {
        NativeQuery nativeQuery = new NativeQuery(QueryStringQuery.of(qs -> qs.query(query))._toQuery());
        return search(nativeQuery.setPageable(pageable));
    }

    @Override
    public Page<Lycee> search(Query query) {
        SearchHits<Lycee> searchHits = elasticsearchTemplate.search(query, Lycee.class);
        List<Lycee> hits = searchHits.map(SearchHit::getContent).stream().toList();
        return new PageImpl<>(hits, query.getPageable(), searchHits.getTotalHits());
    }

    @Override
    public void index(Lycee entity) {
        repository.findById(entity.getId()).ifPresent(elasticsearchTemplate::save);
    }

    @Override
    public void deleteFromIndexById(Long id) {
        elasticsearchTemplate.delete(String.valueOf(id), Lycee.class);
    }
}
