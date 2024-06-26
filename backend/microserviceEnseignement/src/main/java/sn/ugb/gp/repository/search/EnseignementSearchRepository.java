package sn.ugb.gp.repository.search;

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
import sn.ugb.gp.domain.Enseignement;
import sn.ugb.gp.repository.EnseignementRepository;

/**
 * Spring Data Elasticsearch repository for the {@link Enseignement} entity.
 */
public interface EnseignementSearchRepository extends ElasticsearchRepository<Enseignement, Long>, EnseignementSearchRepositoryInternal {}

interface EnseignementSearchRepositoryInternal {
    Page<Enseignement> search(String query, Pageable pageable);

    Page<Enseignement> search(Query query);

    @Async
    void index(Enseignement entity);

    @Async
    void deleteFromIndexById(Long id);
}

class EnseignementSearchRepositoryInternalImpl implements EnseignementSearchRepositoryInternal {

    private final ElasticsearchTemplate elasticsearchTemplate;
    private final EnseignementRepository repository;

    EnseignementSearchRepositoryInternalImpl(ElasticsearchTemplate elasticsearchTemplate, EnseignementRepository repository) {
        this.elasticsearchTemplate = elasticsearchTemplate;
        this.repository = repository;
    }

    @Override
    public Page<Enseignement> search(String query, Pageable pageable) {
        NativeQuery nativeQuery = new NativeQuery(QueryStringQuery.of(qs -> qs.query(query))._toQuery());
        return search(nativeQuery.setPageable(pageable));
    }

    @Override
    public Page<Enseignement> search(Query query) {
        SearchHits<Enseignement> searchHits = elasticsearchTemplate.search(query, Enseignement.class);
        List<Enseignement> hits = searchHits.map(SearchHit::getContent).stream().toList();
        return new PageImpl<>(hits, query.getPageable(), searchHits.getTotalHits());
    }

    @Override
    public void index(Enseignement entity) {
        repository.findById(entity.getId()).ifPresent(elasticsearchTemplate::save);
    }

    @Override
    public void deleteFromIndexById(Long id) {
        elasticsearchTemplate.delete(String.valueOf(id), Enseignement.class);
    }
}
