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
import sn.ugb.gir.domain.FormationInvalide;
import sn.ugb.gir.repository.FormationInvalideRepository;

/**
 * Spring Data Elasticsearch repository for the {@link FormationInvalide} entity.
 */
public interface FormationInvalideSearchRepository
    extends ElasticsearchRepository<FormationInvalide, Long>, FormationInvalideSearchRepositoryInternal {}

interface FormationInvalideSearchRepositoryInternal {
    Page<FormationInvalide> search(String query, Pageable pageable);

    Page<FormationInvalide> search(Query query);

    @Async
    void index(FormationInvalide entity);

    @Async
    void deleteFromIndexById(Long id);
}

class FormationInvalideSearchRepositoryInternalImpl implements FormationInvalideSearchRepositoryInternal {

    private final ElasticsearchTemplate elasticsearchTemplate;
    private final FormationInvalideRepository repository;

    FormationInvalideSearchRepositoryInternalImpl(ElasticsearchTemplate elasticsearchTemplate, FormationInvalideRepository repository) {
        this.elasticsearchTemplate = elasticsearchTemplate;
        this.repository = repository;
    }

    @Override
    public Page<FormationInvalide> search(String query, Pageable pageable) {
        NativeQuery nativeQuery = new NativeQuery(QueryStringQuery.of(qs -> qs.query(query))._toQuery());
        return search(nativeQuery.setPageable(pageable));
    }

    @Override
    public Page<FormationInvalide> search(Query query) {
        SearchHits<FormationInvalide> searchHits = elasticsearchTemplate.search(query, FormationInvalide.class);
        List<FormationInvalide> hits = searchHits.map(SearchHit::getContent).stream().toList();
        return new PageImpl<>(hits, query.getPageable(), searchHits.getTotalHits());
    }

    @Override
    public void index(FormationInvalide entity) {
        repository.findById(entity.getId()).ifPresent(elasticsearchTemplate::save);
    }

    @Override
    public void deleteFromIndexById(Long id) {
        elasticsearchTemplate.delete(String.valueOf(id), FormationInvalide.class);
    }
}
