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
import sn.ugb.gir.domain.Pays;
import sn.ugb.gir.repository.PaysRepository;

/**
 * Spring Data Elasticsearch repository for the {@link Pays} entity.
 */
public interface PaysSearchRepository extends ElasticsearchRepository<Pays, Long>, PaysSearchRepositoryInternal {}

interface PaysSearchRepositoryInternal {
    Page<Pays> search(String query, Pageable pageable);

    Page<Pays> search(Query query);

    @Async
    void index(Pays entity);

    @Async
    void deleteFromIndexById(Long id);
}

class PaysSearchRepositoryInternalImpl implements PaysSearchRepositoryInternal {

    private final ElasticsearchTemplate elasticsearchTemplate;
    private final PaysRepository repository;

    PaysSearchRepositoryInternalImpl(ElasticsearchTemplate elasticsearchTemplate, PaysRepository repository) {
        this.elasticsearchTemplate = elasticsearchTemplate;
        this.repository = repository;
    }

    @Override
    public Page<Pays> search(String query, Pageable pageable) {
        NativeQuery nativeQuery = new NativeQuery(QueryStringQuery.of(qs -> qs.query(query))._toQuery());
        return search(nativeQuery.setPageable(pageable));
    }

    @Override
    public Page<Pays> search(Query query) {
        SearchHits<Pays> searchHits = elasticsearchTemplate.search(query, Pays.class);
        List<Pays> hits = searchHits.map(SearchHit::getContent).stream().toList();
        return new PageImpl<>(hits, query.getPageable(), searchHits.getTotalHits());
    }

    @Override
    public void index(Pays entity) {
        repository.findOneWithEagerRelationships(entity.getId()).ifPresent(elasticsearchTemplate::save);
    }

    @Override
    public void deleteFromIndexById(Long id) {
        elasticsearchTemplate.delete(String.valueOf(id), Pays.class);
    }
}
