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
import sn.ugb.gir.domain.Specialite;
import sn.ugb.gir.repository.SpecialiteRepository;

/**
 * Spring Data Elasticsearch repository for the {@link Specialite} entity.
 */
public interface SpecialiteSearchRepository extends ElasticsearchRepository<Specialite, Long>, SpecialiteSearchRepositoryInternal {}

interface SpecialiteSearchRepositoryInternal {
    Page<Specialite> search(String query, Pageable pageable);

    Page<Specialite> search(Query query);

    @Async
    void index(Specialite entity);

    @Async
    void deleteFromIndexById(Long id);
}

class SpecialiteSearchRepositoryInternalImpl implements SpecialiteSearchRepositoryInternal {

    private final ElasticsearchTemplate elasticsearchTemplate;
    private final SpecialiteRepository repository;

    SpecialiteSearchRepositoryInternalImpl(ElasticsearchTemplate elasticsearchTemplate, SpecialiteRepository repository) {
        this.elasticsearchTemplate = elasticsearchTemplate;
        this.repository = repository;
    }

    @Override
    public Page<Specialite> search(String query, Pageable pageable) {
        NativeQuery nativeQuery = new NativeQuery(QueryStringQuery.of(qs -> qs.query(query))._toQuery());
        return search(nativeQuery.setPageable(pageable));
    }

    @Override
    public Page<Specialite> search(Query query) {
        SearchHits<Specialite> searchHits = elasticsearchTemplate.search(query, Specialite.class);
        List<Specialite> hits = searchHits.map(SearchHit::getContent).stream().toList();
        return new PageImpl<>(hits, query.getPageable(), searchHits.getTotalHits());
    }

    @Override
    public void index(Specialite entity) {
        repository.findById(entity.getId()).ifPresent(elasticsearchTemplate::save);
    }

    @Override
    public void deleteFromIndexById(Long id) {
        elasticsearchTemplate.delete(String.valueOf(id), Specialite.class);
    }
}
