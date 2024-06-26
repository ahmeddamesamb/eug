package sn.ugb.aclc.repository.search;

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
import sn.ugb.aclc.domain.Candidat;
import sn.ugb.aclc.repository.CandidatRepository;

/**
 * Spring Data Elasticsearch repository for the {@link Candidat} entity.
 */
public interface CandidatSearchRepository extends ElasticsearchRepository<Candidat, Long>, CandidatSearchRepositoryInternal {}

interface CandidatSearchRepositoryInternal {
    Page<Candidat> search(String query, Pageable pageable);

    Page<Candidat> search(Query query);

    @Async
    void index(Candidat entity);

    @Async
    void deleteFromIndexById(Long id);
}

class CandidatSearchRepositoryInternalImpl implements CandidatSearchRepositoryInternal {

    private final ElasticsearchTemplate elasticsearchTemplate;
    private final CandidatRepository repository;

    CandidatSearchRepositoryInternalImpl(ElasticsearchTemplate elasticsearchTemplate, CandidatRepository repository) {
        this.elasticsearchTemplate = elasticsearchTemplate;
        this.repository = repository;
    }

    @Override
    public Page<Candidat> search(String query, Pageable pageable) {
        NativeQuery nativeQuery = new NativeQuery(QueryStringQuery.of(qs -> qs.query(query))._toQuery());
        return search(nativeQuery.setPageable(pageable));
    }

    @Override
    public Page<Candidat> search(Query query) {
        SearchHits<Candidat> searchHits = elasticsearchTemplate.search(query, Candidat.class);
        List<Candidat> hits = searchHits.map(SearchHit::getContent).stream().toList();
        return new PageImpl<>(hits, query.getPageable(), searchHits.getTotalHits());
    }

    @Override
    public void index(Candidat entity) {
        repository.findById(entity.getId()).ifPresent(elasticsearchTemplate::save);
    }

    @Override
    public void deleteFromIndexById(Long id) {
        elasticsearchTemplate.delete(String.valueOf(id), Candidat.class);
    }
}
