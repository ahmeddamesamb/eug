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
import sn.ugb.gir.domain.TypeAdmission;
import sn.ugb.gir.repository.TypeAdmissionRepository;

/**
 * Spring Data Elasticsearch repository for the {@link TypeAdmission} entity.
 */
public interface TypeAdmissionSearchRepository
    extends ElasticsearchRepository<TypeAdmission, Long>, TypeAdmissionSearchRepositoryInternal {}

interface TypeAdmissionSearchRepositoryInternal {
    Page<TypeAdmission> search(String query, Pageable pageable);

    Page<TypeAdmission> search(Query query);

    @Async
    void index(TypeAdmission entity);

    @Async
    void deleteFromIndexById(Long id);
}

class TypeAdmissionSearchRepositoryInternalImpl implements TypeAdmissionSearchRepositoryInternal {

    private final ElasticsearchTemplate elasticsearchTemplate;
    private final TypeAdmissionRepository repository;

    TypeAdmissionSearchRepositoryInternalImpl(ElasticsearchTemplate elasticsearchTemplate, TypeAdmissionRepository repository) {
        this.elasticsearchTemplate = elasticsearchTemplate;
        this.repository = repository;
    }

    @Override
    public Page<TypeAdmission> search(String query, Pageable pageable) {
        NativeQuery nativeQuery = new NativeQuery(QueryStringQuery.of(qs -> qs.query(query))._toQuery());
        return search(nativeQuery.setPageable(pageable));
    }

    @Override
    public Page<TypeAdmission> search(Query query) {
        SearchHits<TypeAdmission> searchHits = elasticsearchTemplate.search(query, TypeAdmission.class);
        List<TypeAdmission> hits = searchHits.map(SearchHit::getContent).stream().toList();
        return new PageImpl<>(hits, query.getPageable(), searchHits.getTotalHits());
    }

    @Override
    public void index(TypeAdmission entity) {
        repository.findById(entity.getId()).ifPresent(elasticsearchTemplate::save);
    }

    @Override
    public void deleteFromIndexById(Long id) {
        elasticsearchTemplate.delete(String.valueOf(id), TypeAdmission.class);
    }
}
