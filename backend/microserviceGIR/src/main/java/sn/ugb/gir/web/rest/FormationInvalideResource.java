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
import sn.ugb.gir.repository.FormationInvalideRepository;
import sn.ugb.gir.service.FormationInvalideService;
import sn.ugb.gir.service.dto.FormationInvalideDTO;
import sn.ugb.gir.web.rest.errors.BadRequestAlertException;
import sn.ugb.gir.web.rest.errors.ElasticsearchExceptionMapper;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link sn.ugb.gir.domain.FormationInvalide}.
 */
@RestController
@RequestMapping("/api/formation-invalides")
public class FormationInvalideResource {

    private final Logger log = LoggerFactory.getLogger(FormationInvalideResource.class);

    private static final String ENTITY_NAME = "microserviceGirFormationInvalide";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final FormationInvalideService formationInvalideService;

    private final FormationInvalideRepository formationInvalideRepository;

    public FormationInvalideResource(
        FormationInvalideService formationInvalideService,
        FormationInvalideRepository formationInvalideRepository
    ) {
        this.formationInvalideService = formationInvalideService;
        this.formationInvalideRepository = formationInvalideRepository;
    }

    /**
     * {@code POST  /formation-invalides} : Create a new formationInvalide.
     *
     * @param formationInvalideDTO the formationInvalideDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new formationInvalideDTO, or with status {@code 400 (Bad Request)} if the formationInvalide has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<FormationInvalideDTO> createFormationInvalide(@RequestBody FormationInvalideDTO formationInvalideDTO)
        throws URISyntaxException {
        log.debug("REST request to save FormationInvalide : {}", formationInvalideDTO);
        if (formationInvalideDTO.getId() != null) {
            throw new BadRequestAlertException("A new formationInvalide cannot already have an ID", ENTITY_NAME, "idexists");
        }
        FormationInvalideDTO result = formationInvalideService.save(formationInvalideDTO);
        return ResponseEntity
            .created(new URI("/api/formation-invalides/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /formation-invalides/:id} : Updates an existing formationInvalide.
     *
     * @param id the id of the formationInvalideDTO to save.
     * @param formationInvalideDTO the formationInvalideDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated formationInvalideDTO,
     * or with status {@code 400 (Bad Request)} if the formationInvalideDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the formationInvalideDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<FormationInvalideDTO> updateFormationInvalide(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody FormationInvalideDTO formationInvalideDTO
    ) throws URISyntaxException {
        log.debug("REST request to update FormationInvalide : {}, {}", id, formationInvalideDTO);
        if (formationInvalideDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, formationInvalideDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!formationInvalideRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        FormationInvalideDTO result = formationInvalideService.update(formationInvalideDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, formationInvalideDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /formation-invalides/:id} : Partial updates given fields of an existing formationInvalide, field will ignore if it is null
     *
     * @param id the id of the formationInvalideDTO to save.
     * @param formationInvalideDTO the formationInvalideDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated formationInvalideDTO,
     * or with status {@code 400 (Bad Request)} if the formationInvalideDTO is not valid,
     * or with status {@code 404 (Not Found)} if the formationInvalideDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the formationInvalideDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<FormationInvalideDTO> partialUpdateFormationInvalide(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody FormationInvalideDTO formationInvalideDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update FormationInvalide partially : {}, {}", id, formationInvalideDTO);
        if (formationInvalideDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, formationInvalideDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!formationInvalideRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<FormationInvalideDTO> result = formationInvalideService.partialUpdate(formationInvalideDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, formationInvalideDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /formation-invalides} : get all the formationInvalides.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of formationInvalides in body.
     */
    @GetMapping("")
    public ResponseEntity<List<FormationInvalideDTO>> getAllFormationInvalides(
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get a page of FormationInvalides");
        Page<FormationInvalideDTO> page = formationInvalideService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /formation-invalides/:id} : get the "id" formationInvalide.
     *
     * @param id the id of the formationInvalideDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the formationInvalideDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<FormationInvalideDTO> getFormationInvalide(@PathVariable("id") Long id) {
        log.debug("REST request to get FormationInvalide : {}", id);
        Optional<FormationInvalideDTO> formationInvalideDTO = formationInvalideService.findOne(id);
        return ResponseUtil.wrapOrNotFound(formationInvalideDTO);
    }

    /**
     * {@code DELETE  /formation-invalides/:id} : delete the "id" formationInvalide.
     *
     * @param id the id of the formationInvalideDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFormationInvalide(@PathVariable("id") Long id) {
        log.debug("REST request to delete FormationInvalide : {}", id);
        formationInvalideService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code SEARCH  /formation-invalides/_search?query=:query} : search for the formationInvalide corresponding
     * to the query.
     *
     * @param query the query of the formationInvalide search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search")
    public ResponseEntity<List<FormationInvalideDTO>> searchFormationInvalides(
        @RequestParam("query") String query,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to search for a page of FormationInvalides for query {}", query);
        try {
            Page<FormationInvalideDTO> page = formationInvalideService.search(query, pageable);
            HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
            return ResponseEntity.ok().headers(headers).body(page.getContent());
        } catch (RuntimeException e) {
            throw ElasticsearchExceptionMapper.mapException(e);
        }
    }

    @PostMapping("/invalider-formation")
    public ResponseEntity<FormationInvalideDTO> invaliderFormation(@RequestParam Long formationId, @RequestParam Long anneeAcademiqueId) {
        FormationInvalideDTO result = formationInvalideService.invaliderFormation(formationId, anneeAcademiqueId);
        return ResponseEntity.ok().body(result);
    }
}
