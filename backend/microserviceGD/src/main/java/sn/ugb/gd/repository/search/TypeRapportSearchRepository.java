package sn.ugb.gd.repository.search;

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
import sn.ugb.gd.domain.TypeRapport;
import sn.ugb.gd.repository.TypeRapportRepository;

/**
 * Spring Data Elasticsearch repository for the {@link TypeRapport} entity.
 */
public interface TypeRapportSearchRepository extends ElasticsearchRepository<TypeRapport, Long>, TypeRapportSearchRepositoryInternal {}

interface TypeRapportSearchRepositoryInternal {
    Page<TypeRapport> search(String query, Pageable pageable);

    Page<TypeRapport> search(Query query);

    @Async
    void index(TypeRapport entity);

    @Async
    void deleteFromIndexById(Long id);
}

class TypeRapportSearchRepositoryInternalImpl implements TypeRapportSearchRepositoryInternal {

    private final ElasticsearchTemplate elasticsearchTemplate;
    private final TypeRapportRepository repository;

    TypeRapportSearchRepositoryInternalImpl(ElasticsearchTemplate elasticsearchTemplate, TypeRapportRepository repository) {
        this.elasticsearchTemplate = elasticsearchTemplate;
        this.repository = repository;
    }

    @Override
    public Page<TypeRapport> search(String query, Pageable pageable) {
        NativeQuery nativeQuery = new NativeQuery(QueryStringQuery.of(qs -> qs.query(query))._toQuery());
        return search(nativeQuery.setPageable(pageable));
    }

    @Override
    public Page<TypeRapport> search(Query query) {
        SearchHits<TypeRapport> searchHits = elasticsearchTemplate.search(query, TypeRapport.class);
        List<TypeRapport> hits = searchHits.map(SearchHit::getContent).stream().toList();
        return new PageImpl<>(hits, query.getPageable(), searchHits.getTotalHits());
    }

    @Override
    public void index(TypeRapport entity) {
        repository.findById(entity.getId()).ifPresent(elasticsearchTemplate::save);
    }

    @Override
    public void deleteFromIndexById(Long id) {
        elasticsearchTemplate.delete(String.valueOf(id), TypeRapport.class);
    }
}
