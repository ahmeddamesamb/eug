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
import sn.ugb.gir.domain.Operateur;
import sn.ugb.gir.repository.OperateurRepository;

/**
 * Spring Data Elasticsearch repository for the {@link Operateur} entity.
 */
public interface OperateurSearchRepository extends ElasticsearchRepository<Operateur, Long>, OperateurSearchRepositoryInternal {}

interface OperateurSearchRepositoryInternal {
    Page<Operateur> search(String query, Pageable pageable);

    Page<Operateur> search(Query query);

    @Async
    void index(Operateur entity);

    @Async
    void deleteFromIndexById(Long id);
}

class OperateurSearchRepositoryInternalImpl implements OperateurSearchRepositoryInternal {

    private final ElasticsearchTemplate elasticsearchTemplate;
    private final OperateurRepository repository;

    OperateurSearchRepositoryInternalImpl(ElasticsearchTemplate elasticsearchTemplate, OperateurRepository repository) {
        this.elasticsearchTemplate = elasticsearchTemplate;
        this.repository = repository;
    }

    @Override
    public Page<Operateur> search(String query, Pageable pageable) {
        NativeQuery nativeQuery = new NativeQuery(QueryStringQuery.of(qs -> qs.query(query))._toQuery());
        return search(nativeQuery.setPageable(pageable));
    }

    @Override
    public Page<Operateur> search(Query query) {
        SearchHits<Operateur> searchHits = elasticsearchTemplate.search(query, Operateur.class);
        List<Operateur> hits = searchHits.map(SearchHit::getContent).stream().toList();
        return new PageImpl<>(hits, query.getPageable(), searchHits.getTotalHits());
    }

    @Override
    public void index(Operateur entity) {
        repository.findById(entity.getId()).ifPresent(elasticsearchTemplate::save);
    }

    @Override
    public void deleteFromIndexById(Long id) {
        elasticsearchTemplate.delete(String.valueOf(id), Operateur.class);
    }
}
