package sn.ugb.aua.repository.search;

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
import sn.ugb.aua.domain.Laboratoire;
import sn.ugb.aua.repository.LaboratoireRepository;

/**
 * Spring Data Elasticsearch repository for the {@link Laboratoire} entity.
 */
public interface LaboratoireSearchRepository extends ElasticsearchRepository<Laboratoire, Long>, LaboratoireSearchRepositoryInternal {}

interface LaboratoireSearchRepositoryInternal {
    Page<Laboratoire> search(String query, Pageable pageable);

    Page<Laboratoire> search(Query query);

    @Async
    void index(Laboratoire entity);

    @Async
    void deleteFromIndexById(Long id);
}

class LaboratoireSearchRepositoryInternalImpl implements LaboratoireSearchRepositoryInternal {

    private final ElasticsearchTemplate elasticsearchTemplate;
    private final LaboratoireRepository repository;

    LaboratoireSearchRepositoryInternalImpl(ElasticsearchTemplate elasticsearchTemplate, LaboratoireRepository repository) {
        this.elasticsearchTemplate = elasticsearchTemplate;
        this.repository = repository;
    }

    @Override
    public Page<Laboratoire> search(String query, Pageable pageable) {
        NativeQuery nativeQuery = new NativeQuery(QueryStringQuery.of(qs -> qs.query(query))._toQuery());
        return search(nativeQuery.setPageable(pageable));
    }

    @Override
    public Page<Laboratoire> search(Query query) {
        SearchHits<Laboratoire> searchHits = elasticsearchTemplate.search(query, Laboratoire.class);
        List<Laboratoire> hits = searchHits.map(SearchHit::getContent).stream().toList();
        return new PageImpl<>(hits, query.getPageable(), searchHits.getTotalHits());
    }

    @Override
    public void index(Laboratoire entity) {
        repository.findById(entity.getId()).ifPresent(elasticsearchTemplate::save);
    }

    @Override
    public void deleteFromIndexById(Long id) {
        elasticsearchTemplate.delete(String.valueOf(id), Laboratoire.class);
    }
}
