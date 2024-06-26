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
import sn.ugb.gir.domain.TypeBourse;
import sn.ugb.gir.repository.TypeBourseRepository;

/**
 * Spring Data Elasticsearch repository for the {@link TypeBourse} entity.
 */
public interface TypeBourseSearchRepository extends ElasticsearchRepository<TypeBourse, Long>, TypeBourseSearchRepositoryInternal {}

interface TypeBourseSearchRepositoryInternal {
    Page<TypeBourse> search(String query, Pageable pageable);

    Page<TypeBourse> search(Query query);

    @Async
    void index(TypeBourse entity);

    @Async
    void deleteFromIndexById(Long id);
}

class TypeBourseSearchRepositoryInternalImpl implements TypeBourseSearchRepositoryInternal {

    private final ElasticsearchTemplate elasticsearchTemplate;
    private final TypeBourseRepository repository;

    TypeBourseSearchRepositoryInternalImpl(ElasticsearchTemplate elasticsearchTemplate, TypeBourseRepository repository) {
        this.elasticsearchTemplate = elasticsearchTemplate;
        this.repository = repository;
    }

    @Override
    public Page<TypeBourse> search(String query, Pageable pageable) {
        NativeQuery nativeQuery = new NativeQuery(QueryStringQuery.of(qs -> qs.query(query))._toQuery());
        return search(nativeQuery.setPageable(pageable));
    }

    @Override
    public Page<TypeBourse> search(Query query) {
        SearchHits<TypeBourse> searchHits = elasticsearchTemplate.search(query, TypeBourse.class);
        List<TypeBourse> hits = searchHits.map(SearchHit::getContent).stream().toList();
        return new PageImpl<>(hits, query.getPageable(), searchHits.getTotalHits());
    }

    @Override
    public void index(TypeBourse entity) {
        repository.findById(entity.getId()).ifPresent(elasticsearchTemplate::save);
    }

    @Override
    public void deleteFromIndexById(Long id) {
        elasticsearchTemplate.delete(String.valueOf(id), TypeBourse.class);
    }
}
