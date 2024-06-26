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
import sn.ugb.gir.domain.TypeFormation;
import sn.ugb.gir.repository.TypeFormationRepository;

/**
 * Spring Data Elasticsearch repository for the {@link TypeFormation} entity.
 */
public interface TypeFormationSearchRepository
    extends ElasticsearchRepository<TypeFormation, Long>, TypeFormationSearchRepositoryInternal {}

interface TypeFormationSearchRepositoryInternal {
    Page<TypeFormation> search(String query, Pageable pageable);

    Page<TypeFormation> search(Query query);

    @Async
    void index(TypeFormation entity);

    @Async
    void deleteFromIndexById(Long id);
}

class TypeFormationSearchRepositoryInternalImpl implements TypeFormationSearchRepositoryInternal {

    private final ElasticsearchTemplate elasticsearchTemplate;
    private final TypeFormationRepository repository;

    TypeFormationSearchRepositoryInternalImpl(ElasticsearchTemplate elasticsearchTemplate, TypeFormationRepository repository) {
        this.elasticsearchTemplate = elasticsearchTemplate;
        this.repository = repository;
    }

    @Override
    public Page<TypeFormation> search(String query, Pageable pageable) {
        NativeQuery nativeQuery = new NativeQuery(QueryStringQuery.of(qs -> qs.query(query))._toQuery());
        return search(nativeQuery.setPageable(pageable));
    }

    @Override
    public Page<TypeFormation> search(Query query) {
        SearchHits<TypeFormation> searchHits = elasticsearchTemplate.search(query, TypeFormation.class);
        List<TypeFormation> hits = searchHits.map(SearchHit::getContent).stream().toList();
        return new PageImpl<>(hits, query.getPageable(), searchHits.getTotalHits());
    }

    @Override
    public void index(TypeFormation entity) {
        repository.findById(entity.getId()).ifPresent(elasticsearchTemplate::save);
    }

    @Override
    public void deleteFromIndexById(Long id) {
        elasticsearchTemplate.delete(String.valueOf(id), TypeFormation.class);
    }
}
