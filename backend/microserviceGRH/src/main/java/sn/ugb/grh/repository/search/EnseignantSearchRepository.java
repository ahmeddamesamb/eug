package sn.ugb.grh.repository.search;

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
import sn.ugb.grh.domain.Enseignant;
import sn.ugb.grh.repository.EnseignantRepository;

/**
 * Spring Data Elasticsearch repository for the {@link Enseignant} entity.
 */
public interface EnseignantSearchRepository extends ElasticsearchRepository<Enseignant, Long>, EnseignantSearchRepositoryInternal {}

interface EnseignantSearchRepositoryInternal {
    Page<Enseignant> search(String query, Pageable pageable);

    Page<Enseignant> search(Query query);

    @Async
    void index(Enseignant entity);

    @Async
    void deleteFromIndexById(Long id);
}

class EnseignantSearchRepositoryInternalImpl implements EnseignantSearchRepositoryInternal {

    private final ElasticsearchTemplate elasticsearchTemplate;
    private final EnseignantRepository repository;

    EnseignantSearchRepositoryInternalImpl(ElasticsearchTemplate elasticsearchTemplate, EnseignantRepository repository) {
        this.elasticsearchTemplate = elasticsearchTemplate;
        this.repository = repository;
    }

    @Override
    public Page<Enseignant> search(String query, Pageable pageable) {
        NativeQuery nativeQuery = new NativeQuery(QueryStringQuery.of(qs -> qs.query(query))._toQuery());
        return search(nativeQuery.setPageable(pageable));
    }

    @Override
    public Page<Enseignant> search(Query query) {
        SearchHits<Enseignant> searchHits = elasticsearchTemplate.search(query, Enseignant.class);
        List<Enseignant> hits = searchHits.map(SearchHit::getContent).stream().toList();
        return new PageImpl<>(hits, query.getPageable(), searchHits.getTotalHits());
    }

    @Override
    public void index(Enseignant entity) {
        repository.findById(entity.getId()).ifPresent(elasticsearchTemplate::save);
    }

    @Override
    public void deleteFromIndexById(Long id) {
        elasticsearchTemplate.delete(String.valueOf(id), Enseignant.class);
    }
}
