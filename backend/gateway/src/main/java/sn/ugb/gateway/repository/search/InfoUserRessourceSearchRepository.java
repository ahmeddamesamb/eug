package sn.ugb.gateway.repository.search;

import co.elastic.clients.elasticsearch._types.query_dsl.QueryStringQuery;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.client.elc.NativeQuery;
import org.springframework.data.elasticsearch.client.elc.ReactiveElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.query.Query;
import org.springframework.data.elasticsearch.repository.ReactiveElasticsearchRepository;
import reactor.core.publisher.Flux;
import sn.ugb.gateway.domain.InfoUserRessource;

/**
 * Spring Data Elasticsearch repository for the {@link InfoUserRessource} entity.
 */
public interface InfoUserRessourceSearchRepository
    extends ReactiveElasticsearchRepository<InfoUserRessource, Long>, InfoUserRessourceSearchRepositoryInternal {}

interface InfoUserRessourceSearchRepositoryInternal {
    Flux<InfoUserRessource> search(String query, Pageable pageable);

    Flux<InfoUserRessource> search(Query query);
}

class InfoUserRessourceSearchRepositoryInternalImpl implements InfoUserRessourceSearchRepositoryInternal {

    private final ReactiveElasticsearchTemplate reactiveElasticsearchTemplate;

    InfoUserRessourceSearchRepositoryInternalImpl(ReactiveElasticsearchTemplate reactiveElasticsearchTemplate) {
        this.reactiveElasticsearchTemplate = reactiveElasticsearchTemplate;
    }

    @Override
    public Flux<InfoUserRessource> search(String query, Pageable pageable) {
        NativeQuery nativeQuery = new NativeQuery(QueryStringQuery.of(qs -> qs.query(query))._toQuery());
        nativeQuery.setPageable(pageable);
        return search(nativeQuery);
    }

    @Override
    public Flux<InfoUserRessource> search(Query query) {
        return reactiveElasticsearchTemplate.search(query, InfoUserRessource.class).map(SearchHit::getContent);
    }
}
