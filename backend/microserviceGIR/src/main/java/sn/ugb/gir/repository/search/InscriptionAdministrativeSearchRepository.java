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
import sn.ugb.gir.domain.InscriptionAdministrative;
import sn.ugb.gir.repository.InscriptionAdministrativeRepository;

/**
 * Spring Data Elasticsearch repository for the {@link InscriptionAdministrative} entity.
 */
public interface InscriptionAdministrativeSearchRepository
    extends ElasticsearchRepository<InscriptionAdministrative, Long>, InscriptionAdministrativeSearchRepositoryInternal {}

interface InscriptionAdministrativeSearchRepositoryInternal {
    Page<InscriptionAdministrative> search(String query, Pageable pageable);

    Page<InscriptionAdministrative> search(Query query);

    @Async
    void index(InscriptionAdministrative entity);

    @Async
    void deleteFromIndexById(Long id);
}

class InscriptionAdministrativeSearchRepositoryInternalImpl implements InscriptionAdministrativeSearchRepositoryInternal {

    private final ElasticsearchTemplate elasticsearchTemplate;
    private final InscriptionAdministrativeRepository repository;

    InscriptionAdministrativeSearchRepositoryInternalImpl(
        ElasticsearchTemplate elasticsearchTemplate,
        InscriptionAdministrativeRepository repository
    ) {
        this.elasticsearchTemplate = elasticsearchTemplate;
        this.repository = repository;
    }

    @Override
    public Page<InscriptionAdministrative> search(String query, Pageable pageable) {
        NativeQuery nativeQuery = new NativeQuery(QueryStringQuery.of(qs -> qs.query(query))._toQuery());
        return search(nativeQuery.setPageable(pageable));
    }

    @Override
    public Page<InscriptionAdministrative> search(Query query) {
        SearchHits<InscriptionAdministrative> searchHits = elasticsearchTemplate.search(query, InscriptionAdministrative.class);
        List<InscriptionAdministrative> hits = searchHits.map(SearchHit::getContent).stream().toList();
        return new PageImpl<>(hits, query.getPageable(), searchHits.getTotalHits());
    }

    @Override
    public void index(InscriptionAdministrative entity) {
        repository.findById(entity.getId()).ifPresent(elasticsearchTemplate::save);
    }

    @Override
    public void deleteFromIndexById(Long id) {
        elasticsearchTemplate.delete(String.valueOf(id), InscriptionAdministrative.class);
    }
}
