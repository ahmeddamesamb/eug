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
import sn.ugb.gir.domain.Frais;
import sn.ugb.gir.repository.FraisRepository;

/**
 * Spring Data Elasticsearch repository for the {@link Frais} entity.
 */
public interface FraisSearchRepository extends ElasticsearchRepository<Frais, Long>, FraisSearchRepositoryInternal {}

interface FraisSearchRepositoryInternal {
    Page<Frais> search(String query, Pageable pageable);

    Page<Frais> search(Query query);

    @Async
    void index(Frais entity);

    @Async
    void deleteFromIndexById(Long id);
}

class FraisSearchRepositoryInternalImpl implements FraisSearchRepositoryInternal {

    private final ElasticsearchTemplate elasticsearchTemplate;
    private final FraisRepository repository;

    FraisSearchRepositoryInternalImpl(ElasticsearchTemplate elasticsearchTemplate, FraisRepository repository) {
        this.elasticsearchTemplate = elasticsearchTemplate;
        this.repository = repository;
    }

    @Override
    public Page<Frais> search(String query, Pageable pageable) {
        NativeQuery nativeQuery = new NativeQuery(QueryStringQuery.of(qs -> qs.query(query))._toQuery());
        return search(nativeQuery.setPageable(pageable));
    }

    @Override
    public Page<Frais> search(Query query) {
        SearchHits<Frais> searchHits = elasticsearchTemplate.search(query, Frais.class);
        List<Frais> hits = searchHits.map(SearchHit::getContent).stream().toList();
        return new PageImpl<>(hits, query.getPageable(), searchHits.getTotalHits());
    }

    @Override
    public void index(Frais entity) {
        repository.findOneWithEagerRelationships(entity.getId()).ifPresent(elasticsearchTemplate::save);
    }

    @Override
    public void deleteFromIndexById(Long id) {
        elasticsearchTemplate.delete(String.valueOf(id), Frais.class);
    }
}
