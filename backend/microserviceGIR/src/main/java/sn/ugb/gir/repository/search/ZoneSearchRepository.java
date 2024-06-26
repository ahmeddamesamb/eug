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
import sn.ugb.gir.domain.Zone;
import sn.ugb.gir.repository.ZoneRepository;

/**
 * Spring Data Elasticsearch repository for the {@link Zone} entity.
 */
public interface ZoneSearchRepository extends ElasticsearchRepository<Zone, Long>, ZoneSearchRepositoryInternal {}

interface ZoneSearchRepositoryInternal {
    Page<Zone> search(String query, Pageable pageable);

    Page<Zone> search(Query query);

    @Async
    void index(Zone entity);

    @Async
    void deleteFromIndexById(Long id);
}

class ZoneSearchRepositoryInternalImpl implements ZoneSearchRepositoryInternal {

    private final ElasticsearchTemplate elasticsearchTemplate;
    private final ZoneRepository repository;

    ZoneSearchRepositoryInternalImpl(ElasticsearchTemplate elasticsearchTemplate, ZoneRepository repository) {
        this.elasticsearchTemplate = elasticsearchTemplate;
        this.repository = repository;
    }

    @Override
    public Page<Zone> search(String query, Pageable pageable) {
        NativeQuery nativeQuery = new NativeQuery(QueryStringQuery.of(qs -> qs.query(query))._toQuery());
        return search(nativeQuery.setPageable(pageable));
    }

    @Override
    public Page<Zone> search(Query query) {
        SearchHits<Zone> searchHits = elasticsearchTemplate.search(query, Zone.class);
        List<Zone> hits = searchHits.map(SearchHit::getContent).stream().toList();
        return new PageImpl<>(hits, query.getPageable(), searchHits.getTotalHits());
    }

    @Override
    public void index(Zone entity) {
        repository.findById(entity.getId()).ifPresent(elasticsearchTemplate::save);
    }

    @Override
    public void deleteFromIndexById(Long id) {
        elasticsearchTemplate.delete(String.valueOf(id), Zone.class);
    }
}
