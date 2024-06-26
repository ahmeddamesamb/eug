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
import sn.ugb.gir.domain.Ufr;
import sn.ugb.gir.repository.UfrRepository;

/**
 * Spring Data Elasticsearch repository for the {@link Ufr} entity.
 */
public interface UfrSearchRepository extends ElasticsearchRepository<Ufr, Long>, UfrSearchRepositoryInternal {}

interface UfrSearchRepositoryInternal {
    Page<Ufr> search(String query, Pageable pageable);

    Page<Ufr> search(Query query);

    @Async
    void index(Ufr entity);

    @Async
    void deleteFromIndexById(Long id);
}

class UfrSearchRepositoryInternalImpl implements UfrSearchRepositoryInternal {

    private final ElasticsearchTemplate elasticsearchTemplate;
    private final UfrRepository repository;

    UfrSearchRepositoryInternalImpl(ElasticsearchTemplate elasticsearchTemplate, UfrRepository repository) {
        this.elasticsearchTemplate = elasticsearchTemplate;
        this.repository = repository;
    }

    @Override
    public Page<Ufr> search(String query, Pageable pageable) {
        NativeQuery nativeQuery = new NativeQuery(QueryStringQuery.of(qs -> qs.query(query))._toQuery());
        return search(nativeQuery.setPageable(pageable));
    }

    @Override
    public Page<Ufr> search(Query query) {
        SearchHits<Ufr> searchHits = elasticsearchTemplate.search(query, Ufr.class);
        List<Ufr> hits = searchHits.map(SearchHit::getContent).stream().toList();
        return new PageImpl<>(hits, query.getPageable(), searchHits.getTotalHits());
    }

    @Override
    public void index(Ufr entity) {
        repository.findById(entity.getId()).ifPresent(elasticsearchTemplate::save);
    }

    @Override
    public void deleteFromIndexById(Long id) {
        elasticsearchTemplate.delete(String.valueOf(id), Ufr.class);
    }
}
