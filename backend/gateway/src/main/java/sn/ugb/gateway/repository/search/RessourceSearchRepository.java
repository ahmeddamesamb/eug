package sn.ugb.gateway.repository.search;

import co.elastic.clients.elasticsearch._types.query_dsl.QueryStringQuery;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.client.elc.NativeQuery;
import org.springframework.data.elasticsearch.client.elc.ReactiveElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.query.Query;
import org.springframework.data.elasticsearch.repository.ReactiveElasticsearchRepository;
import reactor.core.publisher.Flux;
import sn.ugb.gateway.domain.Ressource;

/**
 * Spring Data Elasticsearch repository for the {@link Ressource} entity.
 */
public interface RessourceSearchRepository extends ReactiveElasticsearchRepository<Ressource, Long>, RessourceSearchRepositoryInternal {}

interface RessourceSearchRepositoryInternal {
    Flux<Ressource> search(String query, Pageable pageable);

    Flux<Ressource> search(Query query);
}

class RessourceSearchRepositoryInternalImpl implements RessourceSearchRepositoryInternal {

    private final ReactiveElasticsearchTemplate reactiveElasticsearchTemplate;

    RessourceSearchRepositoryInternalImpl(ReactiveElasticsearchTemplate reactiveElasticsearchTemplate) {
        this.reactiveElasticsearchTemplate = reactiveElasticsearchTemplate;
    }

    @Override
    public Flux<Ressource> search(String query, Pageable pageable) {
        NativeQuery nativeQuery = new NativeQuery(QueryStringQuery.of(qs -> qs.query(query))._toQuery());
        nativeQuery.setPageable(pageable);
        return search(nativeQuery);
    }

    @Override
    public Flux<Ressource> search(Query query) {
        return reactiveElasticsearchTemplate.search(query, Ressource.class).map(SearchHit::getContent);
    }
}
