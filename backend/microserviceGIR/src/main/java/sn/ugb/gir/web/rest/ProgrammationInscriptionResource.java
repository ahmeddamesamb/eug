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
import sn.ugb.gir.repository.ProgrammationInscriptionRepository;
import sn.ugb.gir.service.ProgrammationInscriptionService;
import sn.ugb.gir.service.dto.ProgrammationInscriptionDTO;
import sn.ugb.gir.web.rest.errors.BadRequestAlertException;
import sn.ugb.gir.web.rest.errors.ElasticsearchExceptionMapper;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link sn.ugb.gir.domain.ProgrammationInscription}.
 */
@RestController
@RequestMapping("/api/programmation-inscriptions")
public class ProgrammationInscriptionResource {

    private final Logger log = LoggerFactory.getLogger(ProgrammationInscriptionResource.class);

    private static final String ENTITY_NAME = "microserviceGirProgrammationInscription";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ProgrammationInscriptionService programmationInscriptionService;

    private final ProgrammationInscriptionRepository programmationInscriptionRepository;

    public ProgrammationInscriptionResource(
        ProgrammationInscriptionService programmationInscriptionService,
        ProgrammationInscriptionRepository programmationInscriptionRepository
    ) {
        this.programmationInscriptionService = programmationInscriptionService;
        this.programmationInscriptionRepository = programmationInscriptionRepository;
    }

    /**
     * {@code POST  /programmation-inscriptions} : Create a new programmationInscription.
     *
     * @param programmationInscriptionDTO the programmationInscriptionDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new programmationInscriptionDTO, or with status {@code 400 (Bad Request)} if the programmationInscription has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<ProgrammationInscriptionDTO> createProgrammationInscription(
        @RequestBody ProgrammationInscriptionDTO programmationInscriptionDTO
    ) throws URISyntaxException {
        log.debug("REST request to save ProgrammationInscription : {}", programmationInscriptionDTO);
        if (programmationInscriptionDTO.getId() != null) {
            throw new BadRequestAlertException("A new programmationInscription cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ProgrammationInscriptionDTO result = programmationInscriptionService.save(programmationInscriptionDTO);
        return ResponseEntity
            .created(new URI("/api/programmation-inscriptions/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /programmation-inscriptions/:id} : Updates an existing programmationInscription.
     *
     * @param id the id of the programmationInscriptionDTO to save.
     * @param programmationInscriptionDTO the programmationInscriptionDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated programmationInscriptionDTO,
     * or with status {@code 400 (Bad Request)} if the programmationInscriptionDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the programmationInscriptionDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<ProgrammationInscriptionDTO> updateProgrammationInscription(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody ProgrammationInscriptionDTO programmationInscriptionDTO
    ) throws URISyntaxException {
        log.debug("REST request to update ProgrammationInscription : {}, {}", id, programmationInscriptionDTO);
        if (programmationInscriptionDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, programmationInscriptionDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!programmationInscriptionRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        ProgrammationInscriptionDTO result = programmationInscriptionService.update(programmationInscriptionDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, programmationInscriptionDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /programmation-inscriptions/:id} : Partial updates given fields of an existing programmationInscription, field will ignore if it is null
     *
     * @param id the id of the programmationInscriptionDTO to save.
     * @param programmationInscriptionDTO the programmationInscriptionDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated programmationInscriptionDTO,
     * or with status {@code 400 (Bad Request)} if the programmationInscriptionDTO is not valid,
     * or with status {@code 404 (Not Found)} if the programmationInscriptionDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the programmationInscriptionDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<ProgrammationInscriptionDTO> partialUpdateProgrammationInscription(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody ProgrammationInscriptionDTO programmationInscriptionDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update ProgrammationInscription partially : {}, {}", id, programmationInscriptionDTO);
        if (programmationInscriptionDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, programmationInscriptionDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!programmationInscriptionRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ProgrammationInscriptionDTO> result = programmationInscriptionService.partialUpdate(programmationInscriptionDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, programmationInscriptionDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /programmation-inscriptions} : get all the programmationInscriptions.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of programmationInscriptions in body.
     */
    @GetMapping("")
    public ResponseEntity<List<ProgrammationInscriptionDTO>> getAllProgrammationInscriptions(
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get a page of ProgrammationInscriptions");
        Page<ProgrammationInscriptionDTO> page = programmationInscriptionService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /programmation-inscriptions/:id} : get the "id" programmationInscription.
     *
     * @param id the id of the programmationInscriptionDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the programmationInscriptionDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<ProgrammationInscriptionDTO> getProgrammationInscription(@PathVariable("id") Long id) {
        log.debug("REST request to get ProgrammationInscription : {}", id);
        Optional<ProgrammationInscriptionDTO> programmationInscriptionDTO = programmationInscriptionService.findOne(id);
        return ResponseUtil.wrapOrNotFound(programmationInscriptionDTO);
    }

    /**
     * {@code DELETE  /programmation-inscriptions/:id} : delete the "id" programmationInscription.
     *
     * @param id the id of the programmationInscriptionDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProgrammationInscription(@PathVariable("id") Long id) {
        log.debug("REST request to delete ProgrammationInscription : {}", id);
        programmationInscriptionService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code SEARCH  /programmation-inscriptions/_search?query=:query} : search for the programmationInscription corresponding
     * to the query.
     *
     * @param query the query of the programmationInscription search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search")
    public ResponseEntity<List<ProgrammationInscriptionDTO>> searchProgrammationInscriptions(
        @RequestParam("query") String query,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to search for a page of ProgrammationInscriptions for query {}", query);
        try {
            Page<ProgrammationInscriptionDTO> page = programmationInscriptionService.search(query, pageable);
            HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
            return ResponseEntity.ok().headers(headers).body(page.getContent());
        } catch (RuntimeException e) {
            throw ElasticsearchExceptionMapper.mapException(e);
        }
    }
}
