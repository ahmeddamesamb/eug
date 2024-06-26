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
import sn.ugb.gir.domain.DisciplineSportive;
import sn.ugb.gir.repository.DisciplineSportiveRepository;

/**
 * Spring Data Elasticsearch repository for the {@link DisciplineSportive} entity.
 */
public interface DisciplineSportiveSearchRepository
    extends ElasticsearchRepository<DisciplineSportive, Long>, DisciplineSportiveSearchRepositoryInternal {}

interface DisciplineSportiveSearchRepositoryInternal {
    Page<DisciplineSportive> search(String query, Pageable pageable);

    Page<DisciplineSportive> search(Query query);

    @Async
    void index(DisciplineSportive entity);

    @Async
    void deleteFromIndexById(Long id);
}

class DisciplineSportiveSearchRepositoryInternalImpl implements DisciplineSportiveSearchRepositoryInternal {

    private final ElasticsearchTemplate elasticsearchTemplate;
    private final DisciplineSportiveRepository repository;

    DisciplineSportiveSearchRepositoryInternalImpl(ElasticsearchTemplate elasticsearchTemplate, DisciplineSportiveRepository repository) {
        this.elasticsearchTemplate = elasticsearchTemplate;
        this.repository = repository;
    }

    @Override
    public Page<DisciplineSportive> search(String query, Pageable pageable) {
        NativeQuery nativeQuery = new NativeQuery(QueryStringQuery.of(qs -> qs.query(query))._toQuery());
        return search(nativeQuery.setPageable(pageable));
    }

    @Override
    public Page<DisciplineSportive> search(Query query) {
        SearchHits<DisciplineSportive> searchHits = elasticsearchTemplate.search(query, DisciplineSportive.class);
        List<DisciplineSportive> hits = searchHits.map(SearchHit::getContent).stream().toList();
        return new PageImpl<>(hits, query.getPageable(), searchHits.getTotalHits());
    }

    @Override
    public void index(DisciplineSportive entity) {
        repository.findById(entity.getId()).ifPresent(elasticsearchTemplate::save);
    }

    @Override
    public void deleteFromIndexById(Long id) {
        elasticsearchTemplate.delete(String.valueOf(id), DisciplineSportive.class);
    }
}
