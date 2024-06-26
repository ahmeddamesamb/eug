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
import sn.ugb.gir.domain.TypeFrais;
import sn.ugb.gir.repository.TypeFraisRepository;

/**
 * Spring Data Elasticsearch repository for the {@link TypeFrais} entity.
 */
public interface TypeFraisSearchRepository extends ElasticsearchRepository<TypeFrais, Long>, TypeFraisSearchRepositoryInternal {}

interface TypeFraisSearchRepositoryInternal {
    Page<TypeFrais> search(String query, Pageable pageable);

    Page<TypeFrais> search(Query query);

    @Async
    void index(TypeFrais entity);

    @Async
    void deleteFromIndexById(Long id);
}

class TypeFraisSearchRepositoryInternalImpl implements TypeFraisSearchRepositoryInternal {

    private final ElasticsearchTemplate elasticsearchTemplate;
    private final TypeFraisRepository repository;

    TypeFraisSearchRepositoryInternalImpl(ElasticsearchTemplate elasticsearchTemplate, TypeFraisRepository repository) {
        this.elasticsearchTemplate = elasticsearchTemplate;
        this.repository = repository;
    }

    @Override
    public Page<TypeFrais> search(String query, Pageable pageable) {
        NativeQuery nativeQuery = new NativeQuery(QueryStringQuery.of(qs -> qs.query(query))._toQuery());
        return search(nativeQuery.setPageable(pageable));
    }

    @Override
    public Page<TypeFrais> search(Query query) {
        SearchHits<TypeFrais> searchHits = elasticsearchTemplate.search(query, TypeFrais.class);
        List<TypeFrais> hits = searchHits.map(SearchHit::getContent).stream().toList();
        return new PageImpl<>(hits, query.getPageable(), searchHits.getTotalHits());
    }

    @Override
    public void index(TypeFrais entity) {
        repository.findById(entity.getId()).ifPresent(elasticsearchTemplate::save);
    }

    @Override
    public void deleteFromIndexById(Long id) {
        elasticsearchTemplate.delete(String.valueOf(id), TypeFrais.class);
    }
}
