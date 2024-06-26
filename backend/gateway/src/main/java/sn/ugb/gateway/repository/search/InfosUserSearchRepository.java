package sn.ugb.gateway.repository.search;

import co.elastic.clients.elasticsearch._types.query_dsl.QueryStringQuery;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.client.elc.NativeQuery;
import org.springframework.data.elasticsearch.client.elc.ReactiveElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.query.Query;
import org.springframework.data.elasticsearch.repository.ReactiveElasticsearchRepository;
import reactor.core.publisher.Flux;
import sn.ugb.gateway.domain.InfosUser;

/**
 * Spring Data Elasticsearch repository for the {@link InfosUser} entity.
 */
public interface InfosUserSearchRepository extends ReactiveElasticsearchRepository<InfosUser, Long>, InfosUserSearchRepositoryInternal {}

interface InfosUserSearchRepositoryInternal {
    Flux<InfosUser> search(String query, Pageable pageable);

    Flux<InfosUser> search(Query query);
}

class InfosUserSearchRepositoryInternalImpl implements InfosUserSearchRepositoryInternal {

    private final ReactiveElasticsearchTemplate reactiveElasticsearchTemplate;

    InfosUserSearchRepositoryInternalImpl(ReactiveElasticsearchTemplate reactiveElasticsearchTemplate) {
        this.reactiveElasticsearchTemplate = reactiveElasticsearchTemplate;
    }

    @Override
    public Flux<InfosUser> search(String query, Pageable pageable) {
        NativeQuery nativeQuery = new NativeQuery(QueryStringQuery.of(qs -> qs.query(query))._toQuery());
        nativeQuery.setPageable(pageable);
        return search(nativeQuery);
    }

    @Override
    public Flux<InfosUser> search(Query query) {
        return reactiveElasticsearchTemplate.search(query, InfosUser.class).map(SearchHit::getContent);
    }
}
