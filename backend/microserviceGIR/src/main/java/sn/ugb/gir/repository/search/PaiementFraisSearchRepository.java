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
import sn.ugb.gir.domain.PaiementFrais;
import sn.ugb.gir.repository.PaiementFraisRepository;

/**
 * Spring Data Elasticsearch repository for the {@link PaiementFrais} entity.
 */
public interface PaiementFraisSearchRepository
    extends ElasticsearchRepository<PaiementFrais, Long>, PaiementFraisSearchRepositoryInternal {}

interface PaiementFraisSearchRepositoryInternal {
    Page<PaiementFrais> search(String query, Pageable pageable);

    Page<PaiementFrais> search(Query query);

    @Async
    void index(PaiementFrais entity);

    @Async
    void deleteFromIndexById(Long id);
}

class PaiementFraisSearchRepositoryInternalImpl implements PaiementFraisSearchRepositoryInternal {

    private final ElasticsearchTemplate elasticsearchTemplate;
    private final PaiementFraisRepository repository;

    PaiementFraisSearchRepositoryInternalImpl(ElasticsearchTemplate elasticsearchTemplate, PaiementFraisRepository repository) {
        this.elasticsearchTemplate = elasticsearchTemplate;
        this.repository = repository;
    }

    @Override
    public Page<PaiementFrais> search(String query, Pageable pageable) {
        NativeQuery nativeQuery = new NativeQuery(QueryStringQuery.of(qs -> qs.query(query))._toQuery());
        return search(nativeQuery.setPageable(pageable));
    }

    @Override
    public Page<PaiementFrais> search(Query query) {
        SearchHits<PaiementFrais> searchHits = elasticsearchTemplate.search(query, PaiementFrais.class);
        List<PaiementFrais> hits = searchHits.map(SearchHit::getContent).stream().toList();
        return new PageImpl<>(hits, query.getPageable(), searchHits.getTotalHits());
    }

    @Override
    public void index(PaiementFrais entity) {
        repository.findById(entity.getId()).ifPresent(elasticsearchTemplate::save);
    }

    @Override
    public void deleteFromIndexById(Long id) {
        elasticsearchTemplate.delete(String.valueOf(id), PaiementFrais.class);
    }
}
