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
import sn.ugb.gir.domain.Departement;
import sn.ugb.gir.repository.DepartementRepository;

/**
 * Spring Data Elasticsearch repository for the {@link Departement} entity.
 */
public interface DepartementSearchRepository extends ElasticsearchRepository<Departement, Long>, DepartementSearchRepositoryInternal {}

interface DepartementSearchRepositoryInternal {
    Page<Departement> search(String query, Pageable pageable);

    Page<Departement> search(Query query);

    @Async
    void index(Departement entity);

    @Async
    void deleteFromIndexById(Long id);
}

class DepartementSearchRepositoryInternalImpl implements DepartementSearchRepositoryInternal {

    private final ElasticsearchTemplate elasticsearchTemplate;
    private final DepartementRepository repository;

    DepartementSearchRepositoryInternalImpl(ElasticsearchTemplate elasticsearchTemplate, DepartementRepository repository) {
        this.elasticsearchTemplate = elasticsearchTemplate;
        this.repository = repository;
    }

    @Override
    public Page<Departement> search(String query, Pageable pageable) {
        NativeQuery nativeQuery = new NativeQuery(QueryStringQuery.of(qs -> qs.query(query))._toQuery());
        return search(nativeQuery.setPageable(pageable));
    }

    @Override
    public Page<Departement> search(Query query) {
        SearchHits<Departement> searchHits = elasticsearchTemplate.search(query, Departement.class);
        List<Departement> hits = searchHits.map(SearchHit::getContent).stream().toList();
        return new PageImpl<>(hits, query.getPageable(), searchHits.getTotalHits());
    }

    @Override
    public void index(Departement entity) {
        repository.findById(entity.getId()).ifPresent(elasticsearchTemplate::save);
    }

    @Override
    public void deleteFromIndexById(Long id) {
        elasticsearchTemplate.delete(String.valueOf(id), Departement.class);
    }
}
