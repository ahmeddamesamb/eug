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
import sn.ugb.gir.repository.PaiementFormationPriveeRepository;
import sn.ugb.gir.service.PaiementFormationPriveeService;
import sn.ugb.gir.service.dto.PaiementFormationPriveeDTO;
import sn.ugb.gir.web.rest.errors.BadRequestAlertException;
import sn.ugb.gir.web.rest.errors.ElasticsearchExceptionMapper;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link sn.ugb.gir.domain.PaiementFormationPrivee}.
 */
@RestController
@RequestMapping("/api/paiement-formation-privees")
public class PaiementFormationPriveeResource {

    private final Logger log = LoggerFactory.getLogger(PaiementFormationPriveeResource.class);

    private static final String ENTITY_NAME = "microserviceGirPaiementFormationPrivee";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final PaiementFormationPriveeService paiementFormationPriveeService;

    private final PaiementFormationPriveeRepository paiementFormationPriveeRepository;

    public PaiementFormationPriveeResource(
        PaiementFormationPriveeService paiementFormationPriveeService,
        PaiementFormationPriveeRepository paiementFormationPriveeRepository
    ) {
        this.paiementFormationPriveeService = paiementFormationPriveeService;
        this.paiementFormationPriveeRepository = paiementFormationPriveeRepository;
    }

    /**
     * {@code POST  /paiement-formation-privees} : Create a new paiementFormationPrivee.
     *
     * @param paiementFormationPriveeDTO the paiementFormationPriveeDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new paiementFormationPriveeDTO, or with status {@code 400 (Bad Request)} if the paiementFormationPrivee has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<PaiementFormationPriveeDTO> createPaiementFormationPrivee(
        @RequestBody PaiementFormationPriveeDTO paiementFormationPriveeDTO
    ) throws URISyntaxException {
        log.debug("REST request to save PaiementFormationPrivee : {}", paiementFormationPriveeDTO);
        if (paiementFormationPriveeDTO.getId() != null) {
            throw new BadRequestAlertException("A new paiementFormationPrivee cannot already have an ID", ENTITY_NAME, "idexists");
        }
        PaiementFormationPriveeDTO result = paiementFormationPriveeService.save(paiementFormationPriveeDTO);
        return ResponseEntity
            .created(new URI("/api/paiement-formation-privees/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /paiement-formation-privees/:id} : Updates an existing paiementFormationPrivee.
     *
     * @param id the id of the paiementFormationPriveeDTO to save.
     * @param paiementFormationPriveeDTO the paiementFormationPriveeDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated paiementFormationPriveeDTO,
     * or with status {@code 400 (Bad Request)} if the paiementFormationPriveeDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the paiementFormationPriveeDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<PaiementFormationPriveeDTO> updatePaiementFormationPrivee(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody PaiementFormationPriveeDTO paiementFormationPriveeDTO
    ) throws URISyntaxException {
        log.debug("REST request to update PaiementFormationPrivee : {}, {}", id, paiementFormationPriveeDTO);
        if (paiementFormationPriveeDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, paiementFormationPriveeDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!paiementFormationPriveeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        PaiementFormationPriveeDTO result = paiementFormationPriveeService.update(paiementFormationPriveeDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, paiementFormationPriveeDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /paiement-formation-privees/:id} : Partial updates given fields of an existing paiementFormationPrivee, field will ignore if it is null
     *
     * @param id the id of the paiementFormationPriveeDTO to save.
     * @param paiementFormationPriveeDTO the paiementFormationPriveeDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated paiementFormationPriveeDTO,
     * or with status {@code 400 (Bad Request)} if the paiementFormationPriveeDTO is not valid,
     * or with status {@code 404 (Not Found)} if the paiementFormationPriveeDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the paiementFormationPriveeDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<PaiementFormationPriveeDTO> partialUpdatePaiementFormationPrivee(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody PaiementFormationPriveeDTO paiementFormationPriveeDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update PaiementFormationPrivee partially : {}, {}", id, paiementFormationPriveeDTO);
        if (paiementFormationPriveeDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, paiementFormationPriveeDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!paiementFormationPriveeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<PaiementFormationPriveeDTO> result = paiementFormationPriveeService.partialUpdate(paiementFormationPriveeDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, paiementFormationPriveeDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /paiement-formation-privees} : get all the paiementFormationPrivees.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of paiementFormationPrivees in body.
     */
    @GetMapping("")
    public ResponseEntity<List<PaiementFormationPriveeDTO>> getAllPaiementFormationPrivees(
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get a page of PaiementFormationPrivees");
        Page<PaiementFormationPriveeDTO> page = paiementFormationPriveeService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /paiement-formation-privees/:id} : get the "id" paiementFormationPrivee.
     *
     * @param id the id of the paiementFormationPriveeDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the paiementFormationPriveeDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<PaiementFormationPriveeDTO> getPaiementFormationPrivee(@PathVariable("id") Long id) {
        log.debug("REST request to get PaiementFormationPrivee : {}", id);
        Optional<PaiementFormationPriveeDTO> paiementFormationPriveeDTO = paiementFormationPriveeService.findOne(id);
        return ResponseUtil.wrapOrNotFound(paiementFormationPriveeDTO);
    }

    /**
     * {@code DELETE  /paiement-formation-privees/:id} : delete the "id" paiementFormationPrivee.
     *
     * @param id the id of the paiementFormationPriveeDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePaiementFormationPrivee(@PathVariable("id") Long id) {
        log.debug("REST request to delete PaiementFormationPrivee : {}", id);
        paiementFormationPriveeService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code SEARCH  /paiement-formation-privees/_search?query=:query} : search for the paiementFormationPrivee corresponding
     * to the query.
     *
     * @param query the query of the paiementFormationPrivee search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search")
    public ResponseEntity<List<PaiementFormationPriveeDTO>> searchPaiementFormationPrivees(
        @RequestParam("query") String query,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to search for a page of PaiementFormationPrivees for query {}", query);
        try {
            Page<PaiementFormationPriveeDTO> page = paiementFormationPriveeService.search(query, pageable);
            HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
            return ResponseEntity.ok().headers(headers).body(page.getContent());
        } catch (RuntimeException e) {
            throw ElasticsearchExceptionMapper.mapException(e);
        }
    }
}
