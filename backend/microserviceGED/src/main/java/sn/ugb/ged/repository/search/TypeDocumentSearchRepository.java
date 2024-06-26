package sn.ugb.ged.repository.search;

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
import sn.ugb.ged.domain.TypeDocument;
import sn.ugb.ged.repository.TypeDocumentRepository;

/**
 * Spring Data Elasticsearch repository for the {@link TypeDocument} entity.
 */
public interface TypeDocumentSearchRepository extends ElasticsearchRepository<TypeDocument, Long>, TypeDocumentSearchRepositoryInternal {}

interface TypeDocumentSearchRepositoryInternal {
    Page<TypeDocument> search(String query, Pageable pageable);

    Page<TypeDocument> search(Query query);

    @Async
    void index(TypeDocument entity);

    @Async
    void deleteFromIndexById(Long id);
}

class TypeDocumentSearchRepositoryInternalImpl implements TypeDocumentSearchRepositoryInternal {

    private final ElasticsearchTemplate elasticsearchTemplate;
    private final TypeDocumentRepository repository;

    TypeDocumentSearchRepositoryInternalImpl(ElasticsearchTemplate elasticsearchTemplate, TypeDocumentRepository repository) {
        this.elasticsearchTemplate = elasticsearchTemplate;
        this.repository = repository;
    }

    @Override
    public Page<TypeDocument> search(String query, Pageable pageable) {
        NativeQuery nativeQuery = new NativeQuery(QueryStringQuery.of(qs -> qs.query(query))._toQuery());
        return search(nativeQuery.setPageable(pageable));
    }

    @Override
    public Page<TypeDocument> search(Query query) {
        SearchHits<TypeDocument> searchHits = elasticsearchTemplate.search(query, TypeDocument.class);
        List<TypeDocument> hits = searchHits.map(SearchHit::getContent).stream().toList();
        return new PageImpl<>(hits, query.getPageable(), searchHits.getTotalHits());
    }

    @Override
    public void index(TypeDocument entity) {
        repository.findById(entity.getId()).ifPresent(elasticsearchTemplate::save);
    }

    @Override
    public void deleteFromIndexById(Long id) {
        elasticsearchTemplate.delete(String.valueOf(id), TypeDocument.class);
    }
}
