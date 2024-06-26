package sn.ugb.gir.web.rest;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import sn.ugb.gir.repository.DisciplineSportiveRepository;
import sn.ugb.gir.service.DisciplineSportiveService;
import sn.ugb.gir.service.dto.DisciplineSportiveDTO;
import sn.ugb.gir.web.rest.errors.BadRequestAlertException;
import sn.ugb.gir.web.rest.errors.ElasticsearchExceptionMapper;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link sn.ugb.gir.domain.DisciplineSportive}.
 */
@RestController
@RequestMapping("/api/discipline-sportives")
public class DisciplineSportiveResource {

    private final Logger log = LoggerFactory.getLogger(DisciplineSportiveResource.class);

    private static final String ENTITY_NAME = "microserviceGirDisciplineSportive";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final DisciplineSportiveService disciplineSportiveService;

    private final DisciplineSportiveRepository disciplineSportiveRepository;

    public DisciplineSportiveResource(
        DisciplineSportiveService disciplineSportiveService,
        DisciplineSportiveRepository disciplineSportiveRepository
    ) {
        this.disciplineSportiveService = disciplineSportiveService;
        this.disciplineSportiveRepository = disciplineSportiveRepository;
    }

    /**
     * {@code POST  /discipline-sportives} : Create a new disciplineSportive.
     *
     * @param disciplineSportiveDTO the disciplineSportiveDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new disciplineSportiveDTO, or with status {@code 400 (Bad Request)} if the disciplineSportive has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<DisciplineSportiveDTO> createDisciplineSportive(@Valid @RequestBody DisciplineSportiveDTO disciplineSportiveDTO)
        throws URISyntaxException {
        log.debug("REST request to save DisciplineSportive : {}", disciplineSportiveDTO);
        if (disciplineSportiveDTO.getId() != null) {
            throw new BadRequestAlertException("A new disciplineSportive cannot already have an ID", ENTITY_NAME, "idexists");
        }
        DisciplineSportiveDTO result = disciplineSportiveService.save(disciplineSportiveDTO);
        return ResponseEntity
            .created(new URI("/api/discipline-sportives/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /discipline-sportives/:id} : Updates an existing disciplineSportive.
     *
     * @param id the id of the disciplineSportiveDTO to save.
     * @param disciplineSportiveDTO the disciplineSportiveDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated disciplineSportiveDTO,
     * or with status {@code 400 (Bad Request)} if the disciplineSportiveDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the disciplineSportiveDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<DisciplineSportiveDTO> updateDisciplineSportive(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody DisciplineSportiveDTO disciplineSportiveDTO
    ) throws URISyntaxException {
        log.debug("REST request to update DisciplineSportive : {}, {}", id, disciplineSportiveDTO);
        if (disciplineSportiveDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, disciplineSportiveDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!disciplineSportiveRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        DisciplineSportiveDTO result = disciplineSportiveService.update(disciplineSportiveDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, disciplineSportiveDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /discipline-sportives/:id} : Partial updates given fields of an existing disciplineSportive, field will ignore if it is null
     *
     * @param id the id of the disciplineSportiveDTO to save.
     * @param disciplineSportiveDTO the disciplineSportiveDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated disciplineSportiveDTO,
     * or with status {@code 400 (Bad Request)} if the disciplineSportiveDTO is not valid,
     * or with status {@code 404 (Not Found)} if the disciplineSportiveDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the disciplineSportiveDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<DisciplineSportiveDTO> partialUpdateDisciplineSportive(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody DisciplineSportiveDTO disciplineSportiveDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update DisciplineSportive partially : {}, {}", id, disciplineSportiveDTO);
        if (disciplineSportiveDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, disciplineSportiveDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!disciplineSportiveRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<DisciplineSportiveDTO> result = disciplineSportiveService.partialUpdate(disciplineSportiveDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, disciplineSportiveDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /discipline-sportives} : get all the disciplineSportives.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of disciplineSportives in body.
     */
    @GetMapping("")
    public ResponseEntity<List<DisciplineSportiveDTO>> getAllDisciplineSportives(
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get a page of DisciplineSportives");
        Page<DisciplineSportiveDTO> page = disciplineSportiveService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /discipline-sportives/:id} : get the "id" disciplineSportive.
     *
     * @param id the id of the disciplineSportiveDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the disciplineSportiveDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<DisciplineSportiveDTO> getDisciplineSportive(@PathVariable("id") Long id) {
        log.debug("REST request to get DisciplineSportive : {}", id);
        Optional<DisciplineSportiveDTO> disciplineSportiveDTO = disciplineSportiveService.findOne(id);
        return ResponseUtil.wrapOrNotFound(disciplineSportiveDTO);
    }

    /**
     * {@code DELETE  /discipline-sportives/:id} : delete the "id" disciplineSportive.
     *
     * @param id the id of the disciplineSportiveDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDisciplineSportive(@PathVariable("id") Long id) {
        log.debug("REST request to delete DisciplineSportive : {}", id);
        disciplineSportiveService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code SEARCH  /discipline-sportives/_search?query=:query} : search for the disciplineSportive corresponding
     * to the query.
     *
     * @param query the query of the disciplineSportive search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search")
    public ResponseEntity<List<DisciplineSportiveDTO>> searchDisciplineSportives(
        @RequestParam("query") String query,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to search for a page of DisciplineSportives for query {}", query);
        try {
            Page<DisciplineSportiveDTO> page = disciplineSportiveService.search(query, pageable);
            HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
            return ResponseEntity.ok().headers(headers).body(page.getContent());
        } catch (RuntimeException e) {
            throw ElasticsearchExceptionMapper.mapException(e);
        }
    }
}
