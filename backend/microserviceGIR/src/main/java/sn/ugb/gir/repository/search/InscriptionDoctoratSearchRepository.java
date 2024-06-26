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
import sn.ugb.gir.domain.InscriptionDoctorat;
import sn.ugb.gir.repository.InscriptionDoctoratRepository;

/**
 * Spring Data Elasticsearch repository for the {@link InscriptionDoctorat} entity.
 */
public interface InscriptionDoctoratSearchRepository
    extends ElasticsearchRepository<InscriptionDoctorat, Long>, InscriptionDoctoratSearchRepositoryInternal {}

interface InscriptionDoctoratSearchRepositoryInternal {
    Page<InscriptionDoctorat> search(String query, Pageable pageable);

    Page<InscriptionDoctorat> search(Query query);

    @Async
    void index(InscriptionDoctorat entity);

    @Async
    void deleteFromIndexById(Long id);
}

class InscriptionDoctoratSearchRepositoryInternalImpl implements InscriptionDoctoratSearchRepositoryInternal {

    private final ElasticsearchTemplate elasticsearchTemplate;
    private final InscriptionDoctoratRepository repository;

    InscriptionDoctoratSearchRepositoryInternalImpl(ElasticsearchTemplate elasticsearchTemplate, InscriptionDoctoratRepository repository) {
        this.elasticsearchTemplate = elasticsearchTemplate;
        this.repository = repository;
    }

    @Override
    public Page<InscriptionDoctorat> search(String query, Pageable pageable) {
        NativeQuery nativeQuery = new NativeQuery(QueryStringQuery.of(qs -> qs.query(query))._toQuery());
        return search(nativeQuery.setPageable(pageable));
    }

    @Override
    public Page<InscriptionDoctorat> search(Query query) {
        SearchHits<InscriptionDoctorat> searchHits = elasticsearchTemplate.search(query, InscriptionDoctorat.class);
        List<InscriptionDoctorat> hits = searchHits.map(SearchHit::getContent).stream().toList();
        return new PageImpl<>(hits, query.getPageable(), searchHits.getTotalHits());
    }

    @Override
    public void index(InscriptionDoctorat entity) {
        repository.findById(entity.getId()).ifPresent(elasticsearchTemplate::save);
    }

    @Override
    public void deleteFromIndexById(Long id) {
        elasticsearchTemplate.delete(String.valueOf(id), InscriptionDoctorat.class);
    }
}
