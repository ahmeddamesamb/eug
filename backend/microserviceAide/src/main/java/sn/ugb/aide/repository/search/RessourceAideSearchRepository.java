package sn.ugb.aide.repository.search;

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
import sn.ugb.aide.domain.RessourceAide;
import sn.ugb.aide.repository.RessourceAideRepository;

/**
 * Spring Data Elasticsearch repository for the {@link RessourceAide} entity.
 */
public interface RessourceAideSearchRepository
    extends ElasticsearchRepository<RessourceAide, Long>, RessourceAideSearchRepositoryInternal {}

interface RessourceAideSearchRepositoryInternal {
    Page<RessourceAide> search(String query, Pageable pageable);

    Page<RessourceAide> search(Query query);

    @Async
    void index(RessourceAide entity);

    @Async
    void deleteFromIndexById(Long id);
}

class RessourceAideSearchRepositoryInternalImpl implements RessourceAideSearchRepositoryInternal {

    private final ElasticsearchTemplate elasticsearchTemplate;
    private final RessourceAideRepository repository;

    RessourceAideSearchRepositoryInternalImpl(ElasticsearchTemplate elasticsearchTemplate, RessourceAideRepository repository) {
        this.elasticsearchTemplate = elasticsearchTemplate;
        this.repository = repository;
    }

    @Override
    public Page<RessourceAide> search(String query, Pageable pageable) {
        NativeQuery nativeQuery = new NativeQuery(QueryStringQuery.of(qs -> qs.query(query))._toQuery());
        return search(nativeQuery.setPageable(pageable));
    }

    @Override
    public Page<RessourceAide> search(Query query) {
        SearchHits<RessourceAide> searchHits = elasticsearchTemplate.search(query, RessourceAide.class);
        List<RessourceAide> hits = searchHits.map(SearchHit::getContent).stream().toList();
        return new PageImpl<>(hits, query.getPageable(), searchHits.getTotalHits());
    }

    @Override
    public void index(RessourceAide entity) {
        repository.findById(entity.getId()).ifPresent(elasticsearchTemplate::save);
    }

    @Override
    public void deleteFromIndexById(Long id) {
        elasticsearchTemplate.delete(String.valueOf(id), RessourceAide.class);
    }
}
