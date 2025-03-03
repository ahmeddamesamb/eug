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
import sn.ugb.gir.domain.Universite;
import sn.ugb.gir.repository.UniversiteRepository;

/**
 * Spring Data Elasticsearch repository for the {@link Universite} entity.
 */
public interface UniversiteSearchRepository extends ElasticsearchRepository<Universite, Long>, UniversiteSearchRepositoryInternal {}

interface UniversiteSearchRepositoryInternal {
    Page<Universite> search(String query, Pageable pageable);

    Page<Universite> search(Query query);

    @Async
    void index(Universite entity);

    @Async
    void deleteFromIndexById(Long id);
}

class UniversiteSearchRepositoryInternalImpl implements UniversiteSearchRepositoryInternal {

    private final ElasticsearchTemplate elasticsearchTemplate;
    private final UniversiteRepository repository;

    UniversiteSearchRepositoryInternalImpl(ElasticsearchTemplate elasticsearchTemplate, UniversiteRepository repository) {
        this.elasticsearchTemplate = elasticsearchTemplate;
        this.repository = repository;
    }

    @Override
    public Page<Universite> search(String query, Pageable pageable) {
        NativeQuery nativeQuery = new NativeQuery(QueryStringQuery.of(qs -> qs.query(query))._toQuery());
        return search(nativeQuery.setPageable(pageable));
    }

    @Override
    public Page<Universite> search(Query query) {
        SearchHits<Universite> searchHits = elasticsearchTemplate.search(query, Universite.class);
        List<Universite> hits = searchHits.map(SearchHit::getContent).stream().toList();
        return new PageImpl<>(hits, query.getPageable(), searchHits.getTotalHits());
    }

    @Override
    public void index(Universite entity) {
        repository.findById(entity.getId()).ifPresent(elasticsearchTemplate::save);
    }

    @Override
    public void deleteFromIndexById(Long id) {
        elasticsearchTemplate.delete(String.valueOf(id), Universite.class);
    }
}
