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
import sn.ugb.gir.domain.DisciplineSportiveEtudiant;
import sn.ugb.gir.repository.DisciplineSportiveEtudiantRepository;

/**
 * Spring Data Elasticsearch repository for the {@link DisciplineSportiveEtudiant} entity.
 */
public interface DisciplineSportiveEtudiantSearchRepository
    extends ElasticsearchRepository<DisciplineSportiveEtudiant, Long>, DisciplineSportiveEtudiantSearchRepositoryInternal {}

interface DisciplineSportiveEtudiantSearchRepositoryInternal {
    Page<DisciplineSportiveEtudiant> search(String query, Pageable pageable);

    Page<DisciplineSportiveEtudiant> search(Query query);

    @Async
    void index(DisciplineSportiveEtudiant entity);

    @Async
    void deleteFromIndexById(Long id);
}

class DisciplineSportiveEtudiantSearchRepositoryInternalImpl implements DisciplineSportiveEtudiantSearchRepositoryInternal {

    private final ElasticsearchTemplate elasticsearchTemplate;
    private final DisciplineSportiveEtudiantRepository repository;

    DisciplineSportiveEtudiantSearchRepositoryInternalImpl(
        ElasticsearchTemplate elasticsearchTemplate,
        DisciplineSportiveEtudiantRepository repository
    ) {
        this.elasticsearchTemplate = elasticsearchTemplate;
        this.repository = repository;
    }

    @Override
    public Page<DisciplineSportiveEtudiant> search(String query, Pageable pageable) {
        NativeQuery nativeQuery = new NativeQuery(QueryStringQuery.of(qs -> qs.query(query))._toQuery());
        return search(nativeQuery.setPageable(pageable));
    }

    @Override
    public Page<DisciplineSportiveEtudiant> search(Query query) {
        SearchHits<DisciplineSportiveEtudiant> searchHits = elasticsearchTemplate.search(query, DisciplineSportiveEtudiant.class);
        List<DisciplineSportiveEtudiant> hits = searchHits.map(SearchHit::getContent).stream().toList();
        return new PageImpl<>(hits, query.getPageable(), searchHits.getTotalHits());
    }

    @Override
    public void index(DisciplineSportiveEtudiant entity) {
        repository.findById(entity.getId()).ifPresent(elasticsearchTemplate::save);
    }

    @Override
    public void deleteFromIndexById(Long id) {
        elasticsearchTemplate.delete(String.valueOf(id), DisciplineSportiveEtudiant.class);
    }
}
