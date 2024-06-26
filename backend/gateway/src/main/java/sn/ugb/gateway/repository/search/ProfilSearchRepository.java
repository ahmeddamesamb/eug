package sn.ugb.gateway.repository.search;

import co.elastic.clients.elasticsearch._types.query_dsl.QueryStringQuery;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.client.elc.NativeQuery;
import org.springframework.data.elasticsearch.client.elc.ReactiveElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.query.Query;
import org.springframework.data.elasticsearch.repository.ReactiveElasticsearchRepository;
import reactor.core.publisher.Flux;
import sn.ugb.gateway.domain.Profil;

/**
 * Spring Data Elasticsearch repository for the {@link Profil} entity.
 */
public interface ProfilSearchRepository extends ReactiveElasticsearchRepository<Profil, Long>, ProfilSearchRepositoryInternal {}

interface ProfilSearchRepositoryInternal {
    Flux<Profil> search(String query, Pageable pageable);

    Flux<Profil> search(Query query);
}

class ProfilSearchRepositoryInternalImpl implements ProfilSearchRepositoryInternal {

    private final ReactiveElasticsearchTemplate reactiveElasticsearchTemplate;

    ProfilSearchRepositoryInternalImpl(ReactiveElasticsearchTemplate reactiveElasticsearchTemplate) {
        this.reactiveElasticsearchTemplate = reactiveElasticsearchTemplate;
    }

    @Override
    public Flux<Profil> search(String query, Pageable pageable) {
        NativeQuery nativeQuery = new NativeQuery(QueryStringQuery.of(qs -> qs.query(query))._toQuery());
        nativeQuery.setPageable(pageable);
        return search(nativeQuery);
    }

    @Override
    public Flux<Profil> search(Query query) {
        return reactiveElasticsearchTemplate.search(query, Profil.class).map(SearchHit::getContent);
    }
}
