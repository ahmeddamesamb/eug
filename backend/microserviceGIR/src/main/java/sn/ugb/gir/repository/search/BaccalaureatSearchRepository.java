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
import sn.ugb.gir.domain.Baccalaureat;
import sn.ugb.gir.repository.BaccalaureatRepository;

/**
 * Spring Data Elasticsearch repository for the {@link Baccalaureat} entity.
 */
public interface BaccalaureatSearchRepository extends ElasticsearchRepository<Baccalaureat, Long>, BaccalaureatSearchRepositoryInternal {}

interface BaccalaureatSearchRepositoryInternal {
    Page<Baccalaureat> search(String query, Pageable pageable);

    Page<Baccalaureat> search(Query query);

    @Async
    void index(Baccalaureat entity);

    @Async
    void deleteFromIndexById(Long id);
}

class BaccalaureatSearchRepositoryInternalImpl implements BaccalaureatSearchRepositoryInternal {

    private final ElasticsearchTemplate elasticsearchTemplate;
    private final BaccalaureatRepository repository;

    BaccalaureatSearchRepositoryInternalImpl(ElasticsearchTemplate elasticsearchTemplate, BaccalaureatRepository repository) {
        this.elasticsearchTemplate = elasticsearchTemplate;
        this.repository = repository;
    }

    @Override
    public Page<Baccalaureat> search(String query, Pageable pageable) {
        NativeQuery nativeQuery = new NativeQuery(QueryStringQuery.of(qs -> qs.query(query))._toQuery());
        return search(nativeQuery.setPageable(pageable));
    }

    @Override
    public Page<Baccalaureat> search(Query query) {
        SearchHits<Baccalaureat> searchHits = elasticsearchTemplate.search(query, Baccalaureat.class);
        List<Baccalaureat> hits = searchHits.map(SearchHit::getContent).stream().toList();
        return new PageImpl<>(hits, query.getPageable(), searchHits.getTotalHits());
    }

    @Override
    public void index(Baccalaureat entity) {
        repository.findById(entity.getId()).ifPresent(elasticsearchTemplate::save);
    }

    @Override
    public void deleteFromIndexById(Long id) {
        elasticsearchTemplate.delete(String.valueOf(id), Baccalaureat.class);
    }
}
