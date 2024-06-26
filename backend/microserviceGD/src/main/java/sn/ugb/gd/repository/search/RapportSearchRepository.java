package sn.ugb.gd.repository.search;

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
import sn.ugb.gd.domain.Rapport;
import sn.ugb.gd.repository.RapportRepository;

/**
 * Spring Data Elasticsearch repository for the {@link Rapport} entity.
 */
public interface RapportSearchRepository extends ElasticsearchRepository<Rapport, Long>, RapportSearchRepositoryInternal {}

interface RapportSearchRepositoryInternal {
    Page<Rapport> search(String query, Pageable pageable);

    Page<Rapport> search(Query query);

    @Async
    void index(Rapport entity);

    @Async
    void deleteFromIndexById(Long id);
}

class RapportSearchRepositoryInternalImpl implements RapportSearchRepositoryInternal {

    private final ElasticsearchTemplate elasticsearchTemplate;
    private final RapportRepository repository;

    RapportSearchRepositoryInternalImpl(ElasticsearchTemplate elasticsearchTemplate, RapportRepository repository) {
        this.elasticsearchTemplate = elasticsearchTemplate;
        this.repository = repository;
    }

    @Override
    public Page<Rapport> search(String query, Pageable pageable) {
        NativeQuery nativeQuery = new NativeQuery(QueryStringQuery.of(qs -> qs.query(query))._toQuery());
        return search(nativeQuery.setPageable(pageable));
    }

    @Override
    public Page<Rapport> search(Query query) {
        SearchHits<Rapport> searchHits = elasticsearchTemplate.search(query, Rapport.class);
        List<Rapport> hits = searchHits.map(SearchHit::getContent).stream().toList();
        return new PageImpl<>(hits, query.getPageable(), searchHits.getTotalHits());
    }

    @Override
    public void index(Rapport entity) {
        repository.findById(entity.getId()).ifPresent(elasticsearchTemplate::save);
    }

    @Override
    public void deleteFromIndexById(Long id) {
        elasticsearchTemplate.delete(String.valueOf(id), Rapport.class);
    }
}
