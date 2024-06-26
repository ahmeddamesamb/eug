package sn.ugb.gateway.repository.search;

import co.elastic.clients.elasticsearch._types.query_dsl.QueryStringQuery;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.client.elc.NativeQuery;
import org.springframework.data.elasticsearch.client.elc.ReactiveElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.query.Query;
import org.springframework.data.elasticsearch.repository.ReactiveElasticsearchRepository;
import reactor.core.publisher.Flux;
import sn.ugb.gateway.domain.HistoriqueConnexion;

/**
 * Spring Data Elasticsearch repository for the {@link HistoriqueConnexion} entity.
 */
public interface HistoriqueConnexionSearchRepository
    extends ReactiveElasticsearchRepository<HistoriqueConnexion, Long>, HistoriqueConnexionSearchRepositoryInternal {}

interface HistoriqueConnexionSearchRepositoryInternal {
    Flux<HistoriqueConnexion> search(String query, Pageable pageable);

    Flux<HistoriqueConnexion> search(Query query);
}

class HistoriqueConnexionSearchRepositoryInternalImpl implements HistoriqueConnexionSearchRepositoryInternal {

    private final ReactiveElasticsearchTemplate reactiveElasticsearchTemplate;

    HistoriqueConnexionSearchRepositoryInternalImpl(ReactiveElasticsearchTemplate reactiveElasticsearchTemplate) {
        this.reactiveElasticsearchTemplate = reactiveElasticsearchTemplate;
    }

    @Override
    public Flux<HistoriqueConnexion> search(String query, Pageable pageable) {
        NativeQuery nativeQuery = new NativeQuery(QueryStringQuery.of(qs -> qs.query(query))._toQuery());
        nativeQuery.setPageable(pageable);
        return search(nativeQuery);
    }

    @Override
    public Flux<HistoriqueConnexion> search(Query query) {
        return reactiveElasticsearchTemplate.search(query, HistoriqueConnexion.class).map(SearchHit::getContent);
    }
}
