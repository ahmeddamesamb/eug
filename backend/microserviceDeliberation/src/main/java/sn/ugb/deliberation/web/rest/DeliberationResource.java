package sn.ugb.deliberation.web.rest;

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
import sn.ugb.deliberation.repository.DeliberationRepository;
import sn.ugb.deliberation.service.DeliberationService;
import sn.ugb.deliberation.service.dto.DeliberationDTO;
import sn.ugb.deliberation.web.rest.errors.BadRequestAlertException;
import sn.ugb.deliberation.web.rest.errors.ElasticsearchExceptionMapper;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link sn.ugb.deliberation.domain.Deliberation}.
 */
@RestController
@RequestMapping("/api/deliberations")
public class DeliberationResource {

    private final Logger log = LoggerFactory.getLogger(DeliberationResource.class);

    private static final String ENTITY_NAME = "microserviceDeliberationDeliberation";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final DeliberationService deliberationService;

    private final DeliberationRepository deliberationRepository;

    public DeliberationResource(DeliberationService deliberationService, DeliberationRepository deliberationRepository) {
        this.deliberationService = deliberationService;
        this.deliberationRepository = deliberationRepository;
    }

    /**
     * {@code POST  /deliberations} : Create a new deliberation.
     *
     * @param deliberationDTO the deliberationDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new deliberationDTO, or with status {@code 400 (Bad Request)} if the deliberation has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<DeliberationDTO> createDeliberation(@RequestBody DeliberationDTO deliberationDTO) throws URISyntaxException {
        log.debug("REST request to save Deliberation : {}", deliberationDTO);
        if (deliberationDTO.getId() != null) {
            throw new BadRequestAlertException("A new deliberation cannot already have an ID", ENTITY_NAME, "idexists");
        }
        DeliberationDTO result = deliberationService.save(deliberationDTO);
        return ResponseEntity
            .created(new URI("/api/deliberations/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /deliberations/:id} : Updates an existing deliberation.
     *
     * @param id the id of the deliberationDTO to save.
     * @param deliberationDTO the deliberationDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated deliberationDTO,
     * or with status {@code 400 (Bad Request)} if the deliberationDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the deliberationDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<DeliberationDTO> updateDeliberation(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody DeliberationDTO deliberationDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Deliberation : {}, {}", id, deliberationDTO);
        if (deliberationDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, deliberationDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!deliberationRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        DeliberationDTO result = deliberationService.update(deliberationDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, deliberationDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /deliberations/:id} : Partial updates given fields of an existing deliberation, field will ignore if it is null
     *
     * @param id the id of the deliberationDTO to save.
     * @param deliberationDTO the deliberationDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated deliberationDTO,
     * or with status {@code 400 (Bad Request)} if the deliberationDTO is not valid,
     * or with status {@code 404 (Not Found)} if the deliberationDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the deliberationDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<DeliberationDTO> partialUpdateDeliberation(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody DeliberationDTO deliberationDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Deliberation partially : {}, {}", id, deliberationDTO);
        if (deliberationDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, deliberationDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!deliberationRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<DeliberationDTO> result = deliberationService.partialUpdate(deliberationDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, deliberationDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /deliberations} : get all the deliberations.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of deliberations in body.
     */
    @GetMapping("")
    public ResponseEntity<List<DeliberationDTO>> getAllDeliberations(@org.springdoc.core.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get a page of Deliberations");
        Page<DeliberationDTO> page = deliberationService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /deliberations/:id} : get the "id" deliberation.
     *
     * @param id the id of the deliberationDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the deliberationDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<DeliberationDTO> getDeliberation(@PathVariable("id") Long id) {
        log.debug("REST request to get Deliberation : {}", id);
        Optional<DeliberationDTO> deliberationDTO = deliberationService.findOne(id);
        return ResponseUtil.wrapOrNotFound(deliberationDTO);
    }

    /**
     * {@code DELETE  /deliberations/:id} : delete the "id" deliberation.
     *
     * @param id the id of the deliberationDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDeliberation(@PathVariable("id") Long id) {
        log.debug("REST request to delete Deliberation : {}", id);
        deliberationService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code SEARCH  /deliberations/_search?query=:query} : search for the deliberation corresponding
     * to the query.
     *
     * @param query the query of the deliberation search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search")
    public ResponseEntity<List<DeliberationDTO>> searchDeliberations(
        @RequestParam("query") String query,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to search for a page of Deliberations for query {}", query);
        try {
            Page<DeliberationDTO> page = deliberationService.search(query, pageable);
            HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
            return ResponseEntity.ok().headers(headers).body(page.getContent());
        } catch (RuntimeException e) {
            throw ElasticsearchExceptionMapper.mapException(e);
        }
    }
}
