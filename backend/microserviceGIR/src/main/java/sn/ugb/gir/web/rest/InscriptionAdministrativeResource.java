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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import sn.ugb.gir.repository.InscriptionAdministrativeRepository;
import sn.ugb.gir.service.InscriptionAdministrativeService;
import sn.ugb.gir.service.dto.InscriptionAdministrativeDTO;
import sn.ugb.gir.web.rest.errors.BadRequestAlertException;
import sn.ugb.gir.web.rest.errors.ElasticsearchExceptionMapper;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link sn.ugb.gir.domain.InscriptionAdministrative}.
 */
@RestController
@RequestMapping("/api/inscription-administratives")
public class InscriptionAdministrativeResource {

    private final Logger log = LoggerFactory.getLogger(InscriptionAdministrativeResource.class);

    private static final String ENTITY_NAME = "microserviceGirInscriptionAdministrative";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final InscriptionAdministrativeService inscriptionAdministrativeService;

    private final InscriptionAdministrativeRepository inscriptionAdministrativeRepository;

    public InscriptionAdministrativeResource(
        InscriptionAdministrativeService inscriptionAdministrativeService,
        InscriptionAdministrativeRepository inscriptionAdministrativeRepository
    ) {
        this.inscriptionAdministrativeService = inscriptionAdministrativeService;
        this.inscriptionAdministrativeRepository = inscriptionAdministrativeRepository;
    }

    /**
     * {@code POST  /inscription-administratives} : Create a new inscriptionAdministrative.
     *
     * @param inscriptionAdministrativeDTO the inscriptionAdministrativeDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new inscriptionAdministrativeDTO, or with status {@code 400 (Bad Request)} if the inscriptionAdministrative has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<InscriptionAdministrativeDTO> createInscriptionAdministrative(
        @RequestBody InscriptionAdministrativeDTO inscriptionAdministrativeDTO
    ) throws URISyntaxException {
        log.debug("REST request to save InscriptionAdministrative : {}", inscriptionAdministrativeDTO);
        if (inscriptionAdministrativeDTO.getId() != null) {
            throw new BadRequestAlertException("A new inscriptionAdministrative cannot already have an ID", ENTITY_NAME, "idexists");
        }
        InscriptionAdministrativeDTO result = inscriptionAdministrativeService.save(inscriptionAdministrativeDTO);
        return ResponseEntity
            .created(new URI("/api/inscription-administratives/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /inscription-administratives/:id} : Updates an existing inscriptionAdministrative.
     *
     * @param id the id of the inscriptionAdministrativeDTO to save.
     * @param inscriptionAdministrativeDTO the inscriptionAdministrativeDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated inscriptionAdministrativeDTO,
     * or with status {@code 400 (Bad Request)} if the inscriptionAdministrativeDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the inscriptionAdministrativeDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<InscriptionAdministrativeDTO> updateInscriptionAdministrative(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody InscriptionAdministrativeDTO inscriptionAdministrativeDTO
    ) throws URISyntaxException {
        log.debug("REST request to update InscriptionAdministrative : {}, {}", id, inscriptionAdministrativeDTO);
        if (inscriptionAdministrativeDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, inscriptionAdministrativeDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!inscriptionAdministrativeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        InscriptionAdministrativeDTO result = inscriptionAdministrativeService.update(inscriptionAdministrativeDTO);
        return ResponseEntity
            .ok()
            .headers(
                HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, inscriptionAdministrativeDTO.getId().toString())
            )
            .body(result);
    }

    /**
     * {@code PATCH  /inscription-administratives/:id} : Partial updates given fields of an existing inscriptionAdministrative, field will ignore if it is null
     *
     * @param id the id of the inscriptionAdministrativeDTO to save.
     * @param inscriptionAdministrativeDTO the inscriptionAdministrativeDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated inscriptionAdministrativeDTO,
     * or with status {@code 400 (Bad Request)} if the inscriptionAdministrativeDTO is not valid,
     * or with status {@code 404 (Not Found)} if the inscriptionAdministrativeDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the inscriptionAdministrativeDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<InscriptionAdministrativeDTO> partialUpdateInscriptionAdministrative(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody InscriptionAdministrativeDTO inscriptionAdministrativeDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update InscriptionAdministrative partially : {}, {}", id, inscriptionAdministrativeDTO);
        if (inscriptionAdministrativeDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, inscriptionAdministrativeDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!inscriptionAdministrativeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<InscriptionAdministrativeDTO> result = inscriptionAdministrativeService.partialUpdate(inscriptionAdministrativeDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, inscriptionAdministrativeDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /inscription-administratives} : get all the inscriptionAdministratives.
     *
     * @param pageable the pagination information.
     * @param filter the filter of the request.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of inscriptionAdministratives in body.
     */
    @GetMapping("")
    public ResponseEntity<List<InscriptionAdministrativeDTO>> getAllInscriptionAdministratives(
        @org.springdoc.core.annotations.ParameterObject Pageable pageable,
        @RequestParam(name = "filter", required = false) String filter
    ) {
        if ("processusdinscriptionadministrative-is-null".equals(filter)) {
            log.debug("REST request to get all InscriptionAdministratives where processusDinscriptionAdministrative is null");
            return new ResponseEntity<>(
                inscriptionAdministrativeService.findAllWhereProcessusDinscriptionAdministrativeIsNull(),
                HttpStatus.OK
            );
        }
        log.debug("REST request to get a page of InscriptionAdministratives");
        Page<InscriptionAdministrativeDTO> page = inscriptionAdministrativeService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /inscription-administratives/:id} : get the "id" inscriptionAdministrative.
     *
     * @param id the id of the inscriptionAdministrativeDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the inscriptionAdministrativeDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<InscriptionAdministrativeDTO> getInscriptionAdministrative(@PathVariable("id") Long id) {
        log.debug("REST request to get InscriptionAdministrative : {}", id);
        Optional<InscriptionAdministrativeDTO> inscriptionAdministrativeDTO = inscriptionAdministrativeService.findOne(id);
        return ResponseUtil.wrapOrNotFound(inscriptionAdministrativeDTO);
    }

    /**
     * {@code DELETE  /inscription-administratives/:id} : delete the "id" inscriptionAdministrative.
     *
     * @param id the id of the inscriptionAdministrativeDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteInscriptionAdministrative(@PathVariable("id") Long id) {
        log.debug("REST request to delete InscriptionAdministrative : {}", id);
        inscriptionAdministrativeService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code SEARCH  /inscription-administratives/_search?query=:query} : search for the inscriptionAdministrative corresponding
     * to the query.
     *
     * @param query the query of the inscriptionAdministrative search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search")
    public ResponseEntity<List<InscriptionAdministrativeDTO>> searchInscriptionAdministratives(
        @RequestParam("query") String query,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to search for a page of InscriptionAdministratives for query {}", query);
        try {
            Page<InscriptionAdministrativeDTO> page = inscriptionAdministrativeService.search(query, pageable);
            HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
            return ResponseEntity.ok().headers(headers).body(page.getContent());
        } catch (RuntimeException e) {
            throw ElasticsearchExceptionMapper.mapException(e);
        }
    }
}
