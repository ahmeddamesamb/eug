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
import sn.ugb.gir.domain.ProcessusInscriptionAdministrative;
import sn.ugb.gir.repository.ProcessusInscriptionAdministrativeRepository;

/**
 * Spring Data Elasticsearch repository for the {@link ProcessusInscriptionAdministrative} entity.
 */
public interface ProcessusInscriptionAdministrativeSearchRepository
    extends ElasticsearchRepository<ProcessusInscriptionAdministrative, Long>, ProcessusInscriptionAdministrativeSearchRepositoryInternal {}

interface ProcessusInscriptionAdministrativeSearchRepositoryInternal {
    Page<ProcessusInscriptionAdministrative> search(String query, Pageable pageable);

    Page<ProcessusInscriptionAdministrative> search(Query query);

    @Async
    void index(ProcessusInscriptionAdministrative entity);

    @Async
    void deleteFromIndexById(Long id);
}

class ProcessusInscriptionAdministrativeSearchRepositoryInternalImpl implements ProcessusInscriptionAdministrativeSearchRepositoryInternal {

    private final ElasticsearchTemplate elasticsearchTemplate;
    private final ProcessusInscriptionAdministrativeRepository repository;

    ProcessusInscriptionAdministrativeSearchRepositoryInternalImpl(
        ElasticsearchTemplate elasticsearchTemplate,
        ProcessusInscriptionAdministrativeRepository repository
    ) {
        this.elasticsearchTemplate = elasticsearchTemplate;
        this.repository = repository;
    }

    @Override
    public Page<ProcessusInscriptionAdministrative> search(String query, Pageable pageable) {
        NativeQuery nativeQuery = new NativeQuery(QueryStringQuery.of(qs -> qs.query(query))._toQuery());
        return search(nativeQuery.setPageable(pageable));
    }

    @Override
    public Page<ProcessusInscriptionAdministrative> search(Query query) {
        SearchHits<ProcessusInscriptionAdministrative> searchHits = elasticsearchTemplate.search(
            query,
            ProcessusInscriptionAdministrative.class
        );
        List<ProcessusInscriptionAdministrative> hits = searchHits.map(SearchHit::getContent).stream().toList();
        return new PageImpl<>(hits, query.getPageable(), searchHits.getTotalHits());
    }

    @Override
    public void index(ProcessusInscriptionAdministrative entity) {
        repository.findById(entity.getId()).ifPresent(elasticsearchTemplate::save);
    }

    @Override
    public void deleteFromIndexById(Long id) {
        elasticsearchTemplate.delete(String.valueOf(id), ProcessusInscriptionAdministrative.class);
    }
}
