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
import sn.ugb.gir.domain.Ministere;
import sn.ugb.gir.repository.MinistereRepository;

/**
 * Spring Data Elasticsearch repository for the {@link Ministere} entity.
 */
public interface MinistereSearchRepository extends ElasticsearchRepository<Ministere, Long>, MinistereSearchRepositoryInternal {}

interface MinistereSearchRepositoryInternal {
    Page<Ministere> search(String query, Pageable pageable);

    Page<Ministere> search(Query query);

    @Async
    void index(Ministere entity);

    @Async
    void deleteFromIndexById(Long id);
}

class MinistereSearchRepositoryInternalImpl implements MinistereSearchRepositoryInternal {

    private final ElasticsearchTemplate elasticsearchTemplate;
    private final MinistereRepository repository;

    MinistereSearchRepositoryInternalImpl(ElasticsearchTemplate elasticsearchTemplate, MinistereRepository repository) {
        this.elasticsearchTemplate = elasticsearchTemplate;
        this.repository = repository;
    }

    @Override
    public Page<Ministere> search(String query, Pageable pageable) {
        NativeQuery nativeQuery = new NativeQuery(QueryStringQuery.of(qs -> qs.query(query))._toQuery());
        return search(nativeQuery.setPageable(pageable));
    }

    @Override
    public Page<Ministere> search(Query query) {
        SearchHits<Ministere> searchHits = elasticsearchTemplate.search(query, Ministere.class);
        List<Ministere> hits = searchHits.map(SearchHit::getContent).stream().toList();
        return new PageImpl<>(hits, query.getPageable(), searchHits.getTotalHits());
    }

    @Override
    public void index(Ministere entity) {
        repository.findById(entity.getId()).ifPresent(elasticsearchTemplate::save);
    }

    @Override
    public void deleteFromIndexById(Long id) {
        elasticsearchTemplate.delete(String.valueOf(id), Ministere.class);
    }
}
