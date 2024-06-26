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
import sn.ugb.gir.domain.Campagne;
import sn.ugb.gir.repository.CampagneRepository;

/**
 * Spring Data Elasticsearch repository for the {@link Campagne} entity.
 */
public interface CampagneSearchRepository extends ElasticsearchRepository<Campagne, Long>, CampagneSearchRepositoryInternal {}

interface CampagneSearchRepositoryInternal {
    Page<Campagne> search(String query, Pageable pageable);

    Page<Campagne> search(Query query);

    @Async
    void index(Campagne entity);

    @Async
    void deleteFromIndexById(Long id);
}

class CampagneSearchRepositoryInternalImpl implements CampagneSearchRepositoryInternal {

    private final ElasticsearchTemplate elasticsearchTemplate;
    private final CampagneRepository repository;

    CampagneSearchRepositoryInternalImpl(ElasticsearchTemplate elasticsearchTemplate, CampagneRepository repository) {
        this.elasticsearchTemplate = elasticsearchTemplate;
        this.repository = repository;
    }

    @Override
    public Page<Campagne> search(String query, Pageable pageable) {
        NativeQuery nativeQuery = new NativeQuery(QueryStringQuery.of(qs -> qs.query(query))._toQuery());
        return search(nativeQuery.setPageable(pageable));
    }

    @Override
    public Page<Campagne> search(Query query) {
        SearchHits<Campagne> searchHits = elasticsearchTemplate.search(query, Campagne.class);
        List<Campagne> hits = searchHits.map(SearchHit::getContent).stream().toList();
        return new PageImpl<>(hits, query.getPageable(), searchHits.getTotalHits());
    }

    @Override
    public void index(Campagne entity) {
        repository.findById(entity.getId()).ifPresent(elasticsearchTemplate::save);
    }

    @Override
    public void deleteFromIndexById(Long id) {
        elasticsearchTemplate.delete(String.valueOf(id), Campagne.class);
    }
}
