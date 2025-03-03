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
import sn.ugb.ged.domain.DocumentDelivre;
import sn.ugb.ged.repository.DocumentDelivreRepository;

/**
 * Spring Data Elasticsearch repository for the {@link DocumentDelivre} entity.
 */
public interface DocumentDelivreSearchRepository
    extends ElasticsearchRepository<DocumentDelivre, Long>, DocumentDelivreSearchRepositoryInternal {}

interface DocumentDelivreSearchRepositoryInternal {
    Page<DocumentDelivre> search(String query, Pageable pageable);

    Page<DocumentDelivre> search(Query query);

    @Async
    void index(DocumentDelivre entity);

    @Async
    void deleteFromIndexById(Long id);
}

class DocumentDelivreSearchRepositoryInternalImpl implements DocumentDelivreSearchRepositoryInternal {

    private final ElasticsearchTemplate elasticsearchTemplate;
    private final DocumentDelivreRepository repository;

    DocumentDelivreSearchRepositoryInternalImpl(ElasticsearchTemplate elasticsearchTemplate, DocumentDelivreRepository repository) {
        this.elasticsearchTemplate = elasticsearchTemplate;
        this.repository = repository;
    }

    @Override
    public Page<DocumentDelivre> search(String query, Pageable pageable) {
        NativeQuery nativeQuery = new NativeQuery(QueryStringQuery.of(qs -> qs.query(query))._toQuery());
        return search(nativeQuery.setPageable(pageable));
    }

    @Override
    public Page<DocumentDelivre> search(Query query) {
        SearchHits<DocumentDelivre> searchHits = elasticsearchTemplate.search(query, DocumentDelivre.class);
        List<DocumentDelivre> hits = searchHits.map(SearchHit::getContent).stream().toList();
        return new PageImpl<>(hits, query.getPageable(), searchHits.getTotalHits());
    }

    @Override
    public void index(DocumentDelivre entity) {
        repository.findById(entity.getId()).ifPresent(elasticsearchTemplate::save);
    }

    @Override
    public void deleteFromIndexById(Long id) {
        elasticsearchTemplate.delete(String.valueOf(id), DocumentDelivre.class);
    }
}
