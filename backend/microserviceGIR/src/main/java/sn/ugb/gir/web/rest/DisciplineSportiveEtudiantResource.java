package sn.ugb.gir.web.rest;

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
import sn.ugb.gir.repository.DisciplineSportiveEtudiantRepository;
import sn.ugb.gir.service.DisciplineSportiveEtudiantService;
import sn.ugb.gir.service.dto.DisciplineSportiveEtudiantDTO;
import sn.ugb.gir.web.rest.errors.BadRequestAlertException;
import sn.ugb.gir.web.rest.errors.ElasticsearchExceptionMapper;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link sn.ugb.gir.domain.DisciplineSportiveEtudiant}.
 */
@RestController
@RequestMapping("/api/discipline-sportive-etudiants")
public class DisciplineSportiveEtudiantResource {

    private final Logger log = LoggerFactory.getLogger(DisciplineSportiveEtudiantResource.class);

    private static final String ENTITY_NAME = "microserviceGirDisciplineSportiveEtudiant";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final DisciplineSportiveEtudiantService disciplineSportiveEtudiantService;

    private final DisciplineSportiveEtudiantRepository disciplineSportiveEtudiantRepository;

    public DisciplineSportiveEtudiantResource(
        DisciplineSportiveEtudiantService disciplineSportiveEtudiantService,
        DisciplineSportiveEtudiantRepository disciplineSportiveEtudiantRepository
    ) {
        this.disciplineSportiveEtudiantService = disciplineSportiveEtudiantService;
        this.disciplineSportiveEtudiantRepository = disciplineSportiveEtudiantRepository;
    }

    /**
     * {@code POST  /discipline-sportive-etudiants} : Create a new disciplineSportiveEtudiant.
     *
     * @param disciplineSportiveEtudiantDTO the disciplineSportiveEtudiantDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new disciplineSportiveEtudiantDTO, or with status {@code 400 (Bad Request)} if the disciplineSportiveEtudiant has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<DisciplineSportiveEtudiantDTO> createDisciplineSportiveEtudiant(
        @RequestBody DisciplineSportiveEtudiantDTO disciplineSportiveEtudiantDTO
    ) throws URISyntaxException {
        log.debug("REST request to save DisciplineSportiveEtudiant : {}", disciplineSportiveEtudiantDTO);
        if (disciplineSportiveEtudiantDTO.getId() != null) {
            throw new BadRequestAlertException("A new disciplineSportiveEtudiant cannot already have an ID", ENTITY_NAME, "idexists");
        }
        DisciplineSportiveEtudiantDTO result = disciplineSportiveEtudiantService.save(disciplineSportiveEtudiantDTO);
        return ResponseEntity
            .created(new URI("/api/discipline-sportive-etudiants/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /discipline-sportive-etudiants/:id} : Updates an existing disciplineSportiveEtudiant.
     *
     * @param id the id of the disciplineSportiveEtudiantDTO to save.
     * @param disciplineSportiveEtudiantDTO the disciplineSportiveEtudiantDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated disciplineSportiveEtudiantDTO,
     * or with status {@code 400 (Bad Request)} if the disciplineSportiveEtudiantDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the disciplineSportiveEtudiantDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<DisciplineSportiveEtudiantDTO> updateDisciplineSportiveEtudiant(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody DisciplineSportiveEtudiantDTO disciplineSportiveEtudiantDTO
    ) throws URISyntaxException {
        log.debug("REST request to update DisciplineSportiveEtudiant : {}, {}", id, disciplineSportiveEtudiantDTO);

        validateDataUpdate(disciplineSportiveEtudiantDTO,id);
        DisciplineSportiveEtudiantDTO result = disciplineSportiveEtudiantService.update(disciplineSportiveEtudiantDTO);
        return ResponseEntity
            .ok()
            .headers(
                HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, disciplineSportiveEtudiantDTO.getId().toString())
            )
            .body(result);
    }

    /**
     * {@code PATCH  /discipline-sportive-etudiants/:id} : Partial updates given fields of an existing disciplineSportiveEtudiant, field will ignore if it is null
     *
     * @param id the id of the disciplineSportiveEtudiantDTO to save.
     * @param disciplineSportiveEtudiantDTO the disciplineSportiveEtudiantDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated disciplineSportiveEtudiantDTO,
     * or with status {@code 400 (Bad Request)} if the disciplineSportiveEtudiantDTO is not valid,
     * or with status {@code 404 (Not Found)} if the disciplineSportiveEtudiantDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the disciplineSportiveEtudiantDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<DisciplineSportiveEtudiantDTO> partialUpdateDisciplineSportiveEtudiant(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody DisciplineSportiveEtudiantDTO disciplineSportiveEtudiantDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update DisciplineSportiveEtudiant partially : {}, {}", id, disciplineSportiveEtudiantDTO);

        validateDataUpdate(disciplineSportiveEtudiantDTO,id);
        Optional<DisciplineSportiveEtudiantDTO> result = disciplineSportiveEtudiantService.partialUpdate(disciplineSportiveEtudiantDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, disciplineSportiveEtudiantDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /discipline-sportive-etudiants} : get all the disciplineSportiveEtudiants.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of disciplineSportiveEtudiants in body.
     */
    @GetMapping("")
    public ResponseEntity<List<DisciplineSportiveEtudiantDTO>> getAllDisciplineSportiveEtudiants(
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get a page of DisciplineSportiveEtudiants");
        Page<DisciplineSportiveEtudiantDTO> page = disciplineSportiveEtudiantService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /discipline-sportive-etudiants/:id} : get the "id" disciplineSportiveEtudiant.
     *
     * @param id the id of the disciplineSportiveEtudiantDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the disciplineSportiveEtudiantDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<DisciplineSportiveEtudiantDTO> getDisciplineSportiveEtudiant(@PathVariable("id") Long id) {
        log.debug("REST request to get DisciplineSportiveEtudiant : {}", id);
        Optional<DisciplineSportiveEtudiantDTO> disciplineSportiveEtudiantDTO = disciplineSportiveEtudiantService.findOne(id);
        return ResponseUtil.wrapOrNotFound(disciplineSportiveEtudiantDTO);
    }

    /**
     * {@code DELETE  /discipline-sportive-etudiants/:id} : delete the "id" disciplineSportiveEtudiant.
     *
     * @param id the id of the disciplineSportiveEtudiantDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDisciplineSportiveEtudiant(@PathVariable("id") Long id) {
        log.debug("REST request to delete DisciplineSportiveEtudiant : {}", id);
        disciplineSportiveEtudiantService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code SEARCH  /discipline-sportive-etudiants/_search?query=:query} : search for the disciplineSportiveEtudiant corresponding
     * to the query.
     *
     * @param query the query of the disciplineSportiveEtudiant search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search")
    public ResponseEntity<List<DisciplineSportiveEtudiantDTO>> searchDisciplineSportiveEtudiants(
        @RequestParam("query") String query,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to search for a page of DisciplineSportiveEtudiants for query {}", query);
        try {
            Page<DisciplineSportiveEtudiantDTO> page = disciplineSportiveEtudiantService.search(query, pageable);
            HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
            return ResponseEntity.ok().headers(headers).body(page.getContent());
        } catch (RuntimeException e) {
            throw ElasticsearchExceptionMapper.mapException(e);
        }
    }

    private void validateDataUpdate (DisciplineSportiveEtudiantDTO disciplineSportiveEtudiantDTO, Long id){
        if (disciplineSportiveEtudiantDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, disciplineSportiveEtudiantDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!disciplineSportiveEtudiantRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }
    }
}
