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
import sn.ugb.gir.domain.TypeHandicap;
import sn.ugb.gir.repository.TypeHandicapRepository;

/**
 * Spring Data Elasticsearch repository for the {@link TypeHandicap} entity.
 */
public interface TypeHandicapSearchRepository extends ElasticsearchRepository<TypeHandicap, Long>, TypeHandicapSearchRepositoryInternal {}

interface TypeHandicapSearchRepositoryInternal {
    Page<TypeHandicap> search(String query, Pageable pageable);

    Page<TypeHandicap> search(Query query);

    @Async
    void index(TypeHandicap entity);

    @Async
    void deleteFromIndexById(Long id);
}

class TypeHandicapSearchRepositoryInternalImpl implements TypeHandicapSearchRepositoryInternal {

    private final ElasticsearchTemplate elasticsearchTemplate;
    private final TypeHandicapRepository repository;

    TypeHandicapSearchRepositoryInternalImpl(ElasticsearchTemplate elasticsearchTemplate, TypeHandicapRepository repository) {
        this.elasticsearchTemplate = elasticsearchTemplate;
        this.repository = repository;
    }

    @Override
    public Page<TypeHandicap> search(String query, Pageable pageable) {
        NativeQuery nativeQuery = new NativeQuery(QueryStringQuery.of(qs -> qs.query(query))._toQuery());
        return search(nativeQuery.setPageable(pageable));
    }

    @Override
    public Page<TypeHandicap> search(Query query) {
        SearchHits<TypeHandicap> searchHits = elasticsearchTemplate.search(query, TypeHandicap.class);
        List<TypeHandicap> hits = searchHits.map(SearchHit::getContent).stream().toList();
        return new PageImpl<>(hits, query.getPageable(), searchHits.getTotalHits());
    }

    @Override
    public void index(TypeHandicap entity) {
        repository.findById(entity.getId()).ifPresent(elasticsearchTemplate::save);
    }

    @Override
    public void deleteFromIndexById(Long id) {
        elasticsearchTemplate.delete(String.valueOf(id), TypeHandicap.class);
    }
}
