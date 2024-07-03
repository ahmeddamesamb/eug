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
import sn.ugb.gir.domain.InformationPersonnelle;
import sn.ugb.gir.repository.InformationPersonnelleRepository;

/**
 * Spring Data Elasticsearch repository for the {@link InformationPersonnelle} entity.
 */
public interface InformationPersonnelleSearchRepository
    extends ElasticsearchRepository<InformationPersonnelle, Long>, InformationPersonnelleSearchRepositoryInternal {}

interface InformationPersonnelleSearchRepositoryInternal {
    Page<InformationPersonnelle> search(String query, Pageable pageable);

    Page<InformationPersonnelle> search(Query query);

    @Async
    void index(InformationPersonnelle entity);

    @Async
    void deleteFromIndexById(Long id);


}

class InformationPersonnelleSearchRepositoryInternalImpl implements InformationPersonnelleSearchRepositoryInternal {

    private final ElasticsearchTemplate elasticsearchTemplate;
    private final InformationPersonnelleRepository repository;

    InformationPersonnelleSearchRepositoryInternalImpl(
        ElasticsearchTemplate elasticsearchTemplate,
        InformationPersonnelleRepository repository
    ) {
        this.elasticsearchTemplate = elasticsearchTemplate;
        this.repository = repository;
    }

    @Override
    public Page<InformationPersonnelle> search(String query, Pageable pageable) {
        NativeQuery nativeQuery = new NativeQuery(QueryStringQuery.of(qs -> qs.query(query))._toQuery());
        return search(nativeQuery.setPageable(pageable));
    }

    @Override
    public Page<InformationPersonnelle> search(Query query) {
        SearchHits<InformationPersonnelle> searchHits = elasticsearchTemplate.search(query, InformationPersonnelle.class);
        List<InformationPersonnelle> hits = searchHits.map(SearchHit::getContent).stream().toList();
        return new PageImpl<>(hits, query.getPageable(), searchHits.getTotalHits());
    }

    @Override
    public void index(InformationPersonnelle entity) {
        repository.findById(entity.getId()).ifPresent(elasticsearchTemplate::save);
    }

    @Override
    public void deleteFromIndexById(Long id) {
        elasticsearchTemplate.delete(String.valueOf(id), InformationPersonnelle.class);
    }
}
