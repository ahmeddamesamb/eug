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
import sn.ugb.gir.repository.FormationPriveeRepository;
import sn.ugb.gir.service.FormationPriveeService;
import sn.ugb.gir.service.dto.FormationPriveeDTO;
import sn.ugb.gir.web.rest.errors.BadRequestAlertException;
import sn.ugb.gir.web.rest.errors.ElasticsearchExceptionMapper;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link sn.ugb.gir.domain.FormationPrivee}.
 */
@RestController
@RequestMapping("/api/formation-privees")
public class FormationPriveeResource {

    private final Logger log = LoggerFactory.getLogger(FormationPriveeResource.class);

    private static final String ENTITY_NAME = "microserviceGirFormationPrivee";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final FormationPriveeService formationPriveeService;

    private final FormationPriveeRepository formationPriveeRepository;

    public FormationPriveeResource(FormationPriveeService formationPriveeService, FormationPriveeRepository formationPriveeRepository) {
        this.formationPriveeService = formationPriveeService;
        this.formationPriveeRepository = formationPriveeRepository;
    }

    /**
     * {@code POST  /formation-privees} : Create a new formationPrivee.
     *
     * @param formationPriveeDTO the formationPriveeDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new formationPriveeDTO, or with status {@code 400 (Bad Request)} if the formationPrivee has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<FormationPriveeDTO> createFormationPrivee(@Valid @RequestBody FormationPriveeDTO formationPriveeDTO)
        throws URISyntaxException {
        log.debug("REST request to save FormationPrivee : {}", formationPriveeDTO);
        if (formationPriveeDTO.getId() != null) {
            throw new BadRequestAlertException("A new formationPrivee cannot already have an ID", ENTITY_NAME, "idexists");
        }
        FormationPriveeDTO result = formationPriveeService.save(formationPriveeDTO);
        return ResponseEntity
            .created(new URI("/api/formation-privees/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /formation-privees/:id} : Updates an existing formationPrivee.
     *
     * @param id the id of the formationPriveeDTO to save.
     * @param formationPriveeDTO the formationPriveeDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated formationPriveeDTO,
     * or with status {@code 400 (Bad Request)} if the formationPriveeDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the formationPriveeDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<FormationPriveeDTO> updateFormationPrivee(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody FormationPriveeDTO formationPriveeDTO
    ) throws URISyntaxException {
        log.debug("REST request to update FormationPrivee : {}, {}", id, formationPriveeDTO);
        if (formationPriveeDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, formationPriveeDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!formationPriveeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        FormationPriveeDTO result = formationPriveeService.update(formationPriveeDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, formationPriveeDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /formation-privees/:id} : Partial updates given fields of an existing formationPrivee, field will ignore if it is null
     *
     * @param id the id of the formationPriveeDTO to save.
     * @param formationPriveeDTO the formationPriveeDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated formationPriveeDTO,
     * or with status {@code 400 (Bad Request)} if the formationPriveeDTO is not valid,
     * or with status {@code 404 (Not Found)} if the formationPriveeDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the formationPriveeDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<FormationPriveeDTO> partialUpdateFormationPrivee(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody FormationPriveeDTO formationPriveeDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update FormationPrivee partially : {}, {}", id, formationPriveeDTO);
        if (formationPriveeDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, formationPriveeDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!formationPriveeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<FormationPriveeDTO> result = formationPriveeService.partialUpdate(formationPriveeDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, formationPriveeDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /formation-privees} : get all the formationPrivees.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of formationPrivees in body.
     */
    @GetMapping("")
    public ResponseEntity<List<FormationPriveeDTO>> getAllFormationPrivees(
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get a page of FormationPrivees");
        Page<FormationPriveeDTO> page = formationPriveeService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /formation-privees/:id} : get the "id" formationPrivee.
     *
     * @param id the id of the formationPriveeDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the formationPriveeDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<FormationPriveeDTO> getFormationPrivee(@PathVariable("id") Long id) {
        log.debug("REST request to get FormationPrivee : {}", id);
        Optional<FormationPriveeDTO> formationPriveeDTO = formationPriveeService.findOne(id);
        return ResponseUtil.wrapOrNotFound(formationPriveeDTO);
    }

    /**
     * {@code DELETE  /formation-privees/:id} : delete the "id" formationPrivee.
     *
     * @param id the id of the formationPriveeDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFormationPrivee(@PathVariable("id") Long id) {
        log.debug("REST request to delete FormationPrivee : {}", id);
        formationPriveeService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code SEARCH  /formation-privees/_search?query=:query} : search for the formationPrivee corresponding
     * to the query.
     *
     * @param query the query of the formationPrivee search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search")
    public ResponseEntity<List<FormationPriveeDTO>> searchFormationPrivees(
        @RequestParam("query") String query,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to search for a page of FormationPrivees for query {}", query);
        try {
            Page<FormationPriveeDTO> page = formationPriveeService.search(query, pageable);
            HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
            return ResponseEntity.ok().headers(headers).body(page.getContent());
        } catch (RuntimeException e) {
            throw ElasticsearchExceptionMapper.mapException(e);
        }
    }
}
