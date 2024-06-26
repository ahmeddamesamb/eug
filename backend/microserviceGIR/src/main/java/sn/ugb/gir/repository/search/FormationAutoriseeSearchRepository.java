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
import sn.ugb.gir.domain.FormationAutorisee;
import sn.ugb.gir.repository.FormationAutoriseeRepository;

/**
 * Spring Data Elasticsearch repository for the {@link FormationAutorisee} entity.
 */
public interface FormationAutoriseeSearchRepository
    extends ElasticsearchRepository<FormationAutorisee, Long>, FormationAutoriseeSearchRepositoryInternal {}

interface FormationAutoriseeSearchRepositoryInternal {
    Page<FormationAutorisee> search(String query, Pageable pageable);

    Page<FormationAutorisee> search(Query query);

    @Async
    void index(FormationAutorisee entity);

    @Async
    void deleteFromIndexById(Long id);
}

class FormationAutoriseeSearchRepositoryInternalImpl implements FormationAutoriseeSearchRepositoryInternal {

    private final ElasticsearchTemplate elasticsearchTemplate;
    private final FormationAutoriseeRepository repository;

    FormationAutoriseeSearchRepositoryInternalImpl(ElasticsearchTemplate elasticsearchTemplate, FormationAutoriseeRepository repository) {
        this.elasticsearchTemplate = elasticsearchTemplate;
        this.repository = repository;
    }

    @Override
    public Page<FormationAutorisee> search(String query, Pageable pageable) {
        NativeQuery nativeQuery = new NativeQuery(QueryStringQuery.of(qs -> qs.query(query))._toQuery());
        return search(nativeQuery.setPageable(pageable));
    }

    @Override
    public Page<FormationAutorisee> search(Query query) {
        SearchHits<FormationAutorisee> searchHits = elasticsearchTemplate.search(query, FormationAutorisee.class);
        List<FormationAutorisee> hits = searchHits.map(SearchHit::getContent).stream().toList();
        return new PageImpl<>(hits, query.getPageable(), searchHits.getTotalHits());
    }

    @Override
    public void index(FormationAutorisee entity) {
        repository.findOneWithEagerRelationships(entity.getId()).ifPresent(elasticsearchTemplate::save);
    }

    @Override
    public void deleteFromIndexById(Long id) {
        elasticsearchTemplate.delete(String.valueOf(id), FormationAutorisee.class);
    }
}
