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
import sn.ugb.gir.domain.AnneeAcademique;
import sn.ugb.gir.repository.AnneeAcademiqueRepository;

/**
 * Spring Data Elasticsearch repository for the {@link AnneeAcademique} entity.
 */
public interface AnneeAcademiqueSearchRepository
    extends ElasticsearchRepository<AnneeAcademique, Long>, AnneeAcademiqueSearchRepositoryInternal {}

interface AnneeAcademiqueSearchRepositoryInternal {
    Page<AnneeAcademique> search(String query, Pageable pageable);

    Page<AnneeAcademique> search(Query query);

    @Async
    void index(AnneeAcademique entity);

    @Async
    void deleteFromIndexById(Long id);
}

class AnneeAcademiqueSearchRepositoryInternalImpl implements AnneeAcademiqueSearchRepositoryInternal {

    private final ElasticsearchTemplate elasticsearchTemplate;
    private final AnneeAcademiqueRepository repository;

    AnneeAcademiqueSearchRepositoryInternalImpl(ElasticsearchTemplate elasticsearchTemplate, AnneeAcademiqueRepository repository) {
        this.elasticsearchTemplate = elasticsearchTemplate;
        this.repository = repository;
    }

    @Override
    public Page<AnneeAcademique> search(String query, Pageable pageable) {
        NativeQuery nativeQuery = new NativeQuery(QueryStringQuery.of(qs -> qs.query(query))._toQuery());
        return search(nativeQuery.setPageable(pageable));
    }

    @Override
    public Page<AnneeAcademique> search(Query query) {
        SearchHits<AnneeAcademique> searchHits = elasticsearchTemplate.search(query, AnneeAcademique.class);
        List<AnneeAcademique> hits = searchHits.map(SearchHit::getContent).stream().toList();
        return new PageImpl<>(hits, query.getPageable(), searchHits.getTotalHits());
    }

    @Override
    public void index(AnneeAcademique entity) {
        repository.findById(entity.getId()).ifPresent(elasticsearchTemplate::save);
    }

    @Override
    public void deleteFromIndexById(Long id) {
        elasticsearchTemplate.delete(String.valueOf(id), AnneeAcademique.class);
    }
}
