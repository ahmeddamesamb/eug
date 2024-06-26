package sn.ugb.gateway.repository.search;

import co.elastic.clients.elasticsearch._types.query_dsl.QueryStringQuery;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.client.elc.NativeQuery;
import org.springframework.data.elasticsearch.client.elc.ReactiveElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.query.Query;
import org.springframework.data.elasticsearch.repository.ReactiveElasticsearchRepository;
import reactor.core.publisher.Flux;
import sn.ugb.gateway.domain.BlocFonctionnel;

/**
 * Spring Data Elasticsearch repository for the {@link BlocFonctionnel} entity.
 */
public interface BlocFonctionnelSearchRepository
    extends ReactiveElasticsearchRepository<BlocFonctionnel, Long>, BlocFonctionnelSearchRepositoryInternal {}

interface BlocFonctionnelSearchRepositoryInternal {
    Flux<BlocFonctionnel> search(String query, Pageable pageable);

    Flux<BlocFonctionnel> search(Query query);
}

class BlocFonctionnelSearchRepositoryInternalImpl implements BlocFonctionnelSearchRepositoryInternal {

    private final ReactiveElasticsearchTemplate reactiveElasticsearchTemplate;

    BlocFonctionnelSearchRepositoryInternalImpl(ReactiveElasticsearchTemplate reactiveElasticsearchTemplate) {
        this.reactiveElasticsearchTemplate = reactiveElasticsearchTemplate;
    }

    @Override
    public Flux<BlocFonctionnel> search(String query, Pageable pageable) {
        NativeQuery nativeQuery = new NativeQuery(QueryStringQuery.of(qs -> qs.query(query))._toQuery());
        nativeQuery.setPageable(pageable);
        return search(nativeQuery);
    }

    @Override
    public Flux<BlocFonctionnel> search(Query query) {
        return reactiveElasticsearchTemplate.search(query, BlocFonctionnel.class).map(SearchHit::getContent);
    }
}
