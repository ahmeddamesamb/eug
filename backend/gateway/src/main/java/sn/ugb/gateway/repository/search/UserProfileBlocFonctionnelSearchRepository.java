package sn.ugb.gateway.repository.search;

import co.elastic.clients.elasticsearch._types.query_dsl.QueryStringQuery;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.client.elc.NativeQuery;
import org.springframework.data.elasticsearch.client.elc.ReactiveElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.query.Query;
import org.springframework.data.elasticsearch.repository.ReactiveElasticsearchRepository;
import reactor.core.publisher.Flux;
import sn.ugb.gateway.domain.UserProfileBlocFonctionnel;

/**
 * Spring Data Elasticsearch repository for the {@link UserProfileBlocFonctionnel} entity.
 */
public interface UserProfileBlocFonctionnelSearchRepository
    extends ReactiveElasticsearchRepository<UserProfileBlocFonctionnel, Long>, UserProfileBlocFonctionnelSearchRepositoryInternal {}

interface UserProfileBlocFonctionnelSearchRepositoryInternal {
    Flux<UserProfileBlocFonctionnel> search(String query, Pageable pageable);

    Flux<UserProfileBlocFonctionnel> search(Query query);
}

class UserProfileBlocFonctionnelSearchRepositoryInternalImpl implements UserProfileBlocFonctionnelSearchRepositoryInternal {

    private final ReactiveElasticsearchTemplate reactiveElasticsearchTemplate;

    UserProfileBlocFonctionnelSearchRepositoryInternalImpl(ReactiveElasticsearchTemplate reactiveElasticsearchTemplate) {
        this.reactiveElasticsearchTemplate = reactiveElasticsearchTemplate;
    }

    @Override
    public Flux<UserProfileBlocFonctionnel> search(String query, Pageable pageable) {
        NativeQuery nativeQuery = new NativeQuery(QueryStringQuery.of(qs -> qs.query(query))._toQuery());
        nativeQuery.setPageable(pageable);
        return search(nativeQuery);
    }

    @Override
    public Flux<UserProfileBlocFonctionnel> search(Query query) {
        return reactiveElasticsearchTemplate.search(query, UserProfileBlocFonctionnel.class).map(SearchHit::getContent);
    }
}
