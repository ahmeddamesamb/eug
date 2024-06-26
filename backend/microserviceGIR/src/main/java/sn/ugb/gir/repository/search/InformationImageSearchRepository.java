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
import sn.ugb.gir.domain.InformationImage;
import sn.ugb.gir.repository.InformationImageRepository;

/**
 * Spring Data Elasticsearch repository for the {@link InformationImage} entity.
 */
public interface InformationImageSearchRepository
    extends ElasticsearchRepository<InformationImage, Long>, InformationImageSearchRepositoryInternal {}

interface InformationImageSearchRepositoryInternal {
    Page<InformationImage> search(String query, Pageable pageable);

    Page<InformationImage> search(Query query);

    @Async
    void index(InformationImage entity);

    @Async
    void deleteFromIndexById(Long id);
}

class InformationImageSearchRepositoryInternalImpl implements InformationImageSearchRepositoryInternal {

    private final ElasticsearchTemplate elasticsearchTemplate;
    private final InformationImageRepository repository;

    InformationImageSearchRepositoryInternalImpl(ElasticsearchTemplate elasticsearchTemplate, InformationImageRepository repository) {
        this.elasticsearchTemplate = elasticsearchTemplate;
        this.repository = repository;
    }

    @Override
    public Page<InformationImage> search(String query, Pageable pageable) {
        NativeQuery nativeQuery = new NativeQuery(QueryStringQuery.of(qs -> qs.query(query))._toQuery());
        return search(nativeQuery.setPageable(pageable));
    }

    @Override
    public Page<InformationImage> search(Query query) {
        SearchHits<InformationImage> searchHits = elasticsearchTemplate.search(query, InformationImage.class);
        List<InformationImage> hits = searchHits.map(SearchHit::getContent).stream().toList();
        return new PageImpl<>(hits, query.getPageable(), searchHits.getTotalHits());
    }

    @Override
    public void index(InformationImage entity) {
        repository.findById(entity.getId()).ifPresent(elasticsearchTemplate::save);
    }

    @Override
    public void deleteFromIndexById(Long id) {
        elasticsearchTemplate.delete(String.valueOf(id), InformationImage.class);
    }
}
