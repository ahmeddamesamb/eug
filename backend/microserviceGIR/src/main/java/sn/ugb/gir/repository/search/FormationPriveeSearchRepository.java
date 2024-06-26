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
import sn.ugb.gir.domain.FormationPrivee;
import sn.ugb.gir.repository.FormationPriveeRepository;

/**
 * Spring Data Elasticsearch repository for the {@link FormationPrivee} entity.
 */
public interface FormationPriveeSearchRepository
    extends ElasticsearchRepository<FormationPrivee, Long>, FormationPriveeSearchRepositoryInternal {}

interface FormationPriveeSearchRepositoryInternal {
    Page<FormationPrivee> search(String query, Pageable pageable);

    Page<FormationPrivee> search(Query query);

    @Async
    void index(FormationPrivee entity);

    @Async
    void deleteFromIndexById(Long id);
}

class FormationPriveeSearchRepositoryInternalImpl implements FormationPriveeSearchRepositoryInternal {

    private final ElasticsearchTemplate elasticsearchTemplate;
    private final FormationPriveeRepository repository;

    FormationPriveeSearchRepositoryInternalImpl(ElasticsearchTemplate elasticsearchTemplate, FormationPriveeRepository repository) {
        this.elasticsearchTemplate = elasticsearchTemplate;
        this.repository = repository;
    }

    @Override
    public Page<FormationPrivee> search(String query, Pageable pageable) {
        NativeQuery nativeQuery = new NativeQuery(QueryStringQuery.of(qs -> qs.query(query))._toQuery());
        return search(nativeQuery.setPageable(pageable));
    }

    @Override
    public Page<FormationPrivee> search(Query query) {
        SearchHits<FormationPrivee> searchHits = elasticsearchTemplate.search(query, FormationPrivee.class);
        List<FormationPrivee> hits = searchHits.map(SearchHit::getContent).stream().toList();
        return new PageImpl<>(hits, query.getPageable(), searchHits.getTotalHits());
    }

    @Override
    public void index(FormationPrivee entity) {
        repository.findById(entity.getId()).ifPresent(elasticsearchTemplate::save);
    }

    @Override
    public void deleteFromIndexById(Long id) {
        elasticsearchTemplate.delete(String.valueOf(id), FormationPrivee.class);
    }
}
