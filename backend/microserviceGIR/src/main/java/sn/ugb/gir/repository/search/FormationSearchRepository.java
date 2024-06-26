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
import sn.ugb.gir.domain.Formation;
import sn.ugb.gir.repository.FormationRepository;

/**
 * Spring Data Elasticsearch repository for the {@link Formation} entity.
 */
public interface FormationSearchRepository extends ElasticsearchRepository<Formation, Long>, FormationSearchRepositoryInternal {}

interface FormationSearchRepositoryInternal {
    Page<Formation> search(String query, Pageable pageable);

    Page<Formation> search(Query query);

    @Async
    void index(Formation entity);

    @Async
    void deleteFromIndexById(Long id);
}

class FormationSearchRepositoryInternalImpl implements FormationSearchRepositoryInternal {

    private final ElasticsearchTemplate elasticsearchTemplate;
    private final FormationRepository repository;

    FormationSearchRepositoryInternalImpl(ElasticsearchTemplate elasticsearchTemplate, FormationRepository repository) {
        this.elasticsearchTemplate = elasticsearchTemplate;
        this.repository = repository;
    }

    @Override
    public Page<Formation> search(String query, Pageable pageable) {
        NativeQuery nativeQuery = new NativeQuery(QueryStringQuery.of(qs -> qs.query(query))._toQuery());
        return search(nativeQuery.setPageable(pageable));
    }

    @Override
    public Page<Formation> search(Query query) {
        SearchHits<Formation> searchHits = elasticsearchTemplate.search(query, Formation.class);
        List<Formation> hits = searchHits.map(SearchHit::getContent).stream().toList();
        return new PageImpl<>(hits, query.getPageable(), searchHits.getTotalHits());
    }

    @Override
    public void index(Formation entity) {
        repository.findById(entity.getId()).ifPresent(elasticsearchTemplate::save);
    }

    @Override
    public void deleteFromIndexById(Long id) {
        elasticsearchTemplate.delete(String.valueOf(id), Formation.class);
    }
}
