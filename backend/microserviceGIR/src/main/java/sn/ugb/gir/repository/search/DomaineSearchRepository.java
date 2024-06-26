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
import sn.ugb.gir.domain.Domaine;
import sn.ugb.gir.repository.DomaineRepository;

/**
 * Spring Data Elasticsearch repository for the {@link Domaine} entity.
 */
public interface DomaineSearchRepository extends ElasticsearchRepository<Domaine, Long>, DomaineSearchRepositoryInternal {}

interface DomaineSearchRepositoryInternal {
    Page<Domaine> search(String query, Pageable pageable);

    Page<Domaine> search(Query query);

    @Async
    void index(Domaine entity);

    @Async
    void deleteFromIndexById(Long id);
}

class DomaineSearchRepositoryInternalImpl implements DomaineSearchRepositoryInternal {

    private final ElasticsearchTemplate elasticsearchTemplate;
    private final DomaineRepository repository;

    DomaineSearchRepositoryInternalImpl(ElasticsearchTemplate elasticsearchTemplate, DomaineRepository repository) {
        this.elasticsearchTemplate = elasticsearchTemplate;
        this.repository = repository;
    }

    @Override
    public Page<Domaine> search(String query, Pageable pageable) {
        NativeQuery nativeQuery = new NativeQuery(QueryStringQuery.of(qs -> qs.query(query))._toQuery());
        return search(nativeQuery.setPageable(pageable));
    }

    @Override
    public Page<Domaine> search(Query query) {
        SearchHits<Domaine> searchHits = elasticsearchTemplate.search(query, Domaine.class);
        List<Domaine> hits = searchHits.map(SearchHit::getContent).stream().toList();
        return new PageImpl<>(hits, query.getPageable(), searchHits.getTotalHits());
    }

    @Override
    public void index(Domaine entity) {
        repository.findOneWithEagerRelationships(entity.getId()).ifPresent(elasticsearchTemplate::save);
    }

    @Override
    public void deleteFromIndexById(Long id) {
        elasticsearchTemplate.delete(String.valueOf(id), Domaine.class);
    }
}
