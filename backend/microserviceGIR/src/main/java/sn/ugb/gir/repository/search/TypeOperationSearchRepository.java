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
import sn.ugb.gir.domain.TypeOperation;
import sn.ugb.gir.repository.TypeOperationRepository;

/**
 * Spring Data Elasticsearch repository for the {@link TypeOperation} entity.
 */
public interface TypeOperationSearchRepository
    extends ElasticsearchRepository<TypeOperation, Long>, TypeOperationSearchRepositoryInternal {}

interface TypeOperationSearchRepositoryInternal {
    Page<TypeOperation> search(String query, Pageable pageable);

    Page<TypeOperation> search(Query query);

    @Async
    void index(TypeOperation entity);

    @Async
    void deleteFromIndexById(Long id);
}

class TypeOperationSearchRepositoryInternalImpl implements TypeOperationSearchRepositoryInternal {

    private final ElasticsearchTemplate elasticsearchTemplate;
    private final TypeOperationRepository repository;

    TypeOperationSearchRepositoryInternalImpl(ElasticsearchTemplate elasticsearchTemplate, TypeOperationRepository repository) {
        this.elasticsearchTemplate = elasticsearchTemplate;
        this.repository = repository;
    }

    @Override
    public Page<TypeOperation> search(String query, Pageable pageable) {
        NativeQuery nativeQuery = new NativeQuery(QueryStringQuery.of(qs -> qs.query(query))._toQuery());
        return search(nativeQuery.setPageable(pageable));
    }

    @Override
    public Page<TypeOperation> search(Query query) {
        SearchHits<TypeOperation> searchHits = elasticsearchTemplate.search(query, TypeOperation.class);
        List<TypeOperation> hits = searchHits.map(SearchHit::getContent).stream().toList();
        return new PageImpl<>(hits, query.getPageable(), searchHits.getTotalHits());
    }

    @Override
    public void index(TypeOperation entity) {
        repository.findById(entity.getId()).ifPresent(elasticsearchTemplate::save);
    }

    @Override
    public void deleteFromIndexById(Long id) {
        elasticsearchTemplate.delete(String.valueOf(id), TypeOperation.class);
    }
}
