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
import sn.ugb.gir.repository.UfrRepository;
import sn.ugb.gir.service.UfrService;
import sn.ugb.gir.service.dto.UfrDTO;
import sn.ugb.gir.web.rest.errors.BadRequestAlertException;
import sn.ugb.gir.web.rest.errors.ElasticsearchExceptionMapper;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link sn.ugb.gir.domain.Ufr}.
 */
@RestController
@RequestMapping("/api/ufrs")
public class UfrResource {

    private final Logger log = LoggerFactory.getLogger(UfrResource.class);

    private static final String ENTITY_NAME = "microserviceGirUfr";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final UfrService ufrService;

    private final UfrRepository ufrRepository;

    public UfrResource(UfrService ufrService, UfrRepository ufrRepository) {
        this.ufrService = ufrService;
        this.ufrRepository = ufrRepository;
    }

    /**
     * {@code POST  /ufrs} : Create a new ufr.
     *
     * @param ufrDTO the ufrDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new ufrDTO, or with status {@code 400 (Bad Request)} if the ufr has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<UfrDTO> createUfr(@Valid @RequestBody UfrDTO ufrDTO) throws URISyntaxException {
        log.debug("REST request to save Ufr : {}", ufrDTO);
        if (ufrDTO.getId() != null) {
            throw new BadRequestAlertException("A new ufr cannot already have an ID", ENTITY_NAME, "idexists");
        }
        UfrDTO result = ufrService.save(ufrDTO);
        return ResponseEntity
            .created(new URI("/api/ufrs/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /ufrs/:id} : Updates an existing ufr.
     *
     * @param id the id of the ufrDTO to save.
     * @param ufrDTO the ufrDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated ufrDTO,
     * or with status {@code 400 (Bad Request)} if the ufrDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the ufrDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<UfrDTO> updateUfr(@PathVariable(value = "id", required = false) final Long id, @Valid @RequestBody UfrDTO ufrDTO)
        throws URISyntaxException {
        log.debug("REST request to update Ufr : {}, {}", id, ufrDTO);
        if (ufrDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, ufrDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!ufrRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        UfrDTO result = ufrService.update(ufrDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, ufrDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /ufrs/:id} : Partial updates given fields of an existing ufr, field will ignore if it is null
     *
     * @param id the id of the ufrDTO to save.
     * @param ufrDTO the ufrDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated ufrDTO,
     * or with status {@code 400 (Bad Request)} if the ufrDTO is not valid,
     * or with status {@code 404 (Not Found)} if the ufrDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the ufrDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<UfrDTO> partialUpdateUfr(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody UfrDTO ufrDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Ufr partially : {}, {}", id, ufrDTO);
        if (ufrDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, ufrDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!ufrRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<UfrDTO> result = ufrService.partialUpdate(ufrDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, ufrDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /ufrs} : get all the ufrs.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of ufrs in body.
     */
    @GetMapping("")
    public ResponseEntity<List<UfrDTO>> getAllUfrs(@org.springdoc.core.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get a page of Ufrs");
        Page<UfrDTO> page = ufrService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /ufrs/:id} : get the "id" ufr.
     *
     * @param id the id of the ufrDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the ufrDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<UfrDTO> getUfr(@PathVariable("id") Long id) {
        log.debug("REST request to get Ufr : {}", id);
        Optional<UfrDTO> ufrDTO = ufrService.findOne(id);
        return ResponseUtil.wrapOrNotFound(ufrDTO);
    }

    /**
     * {@code DELETE  /ufrs/:id} : delete the "id" ufr.
     *
     * @param id the id of the ufrDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUfr(@PathVariable("id") Long id) {
        log.debug("REST request to delete Ufr : {}", id);
        ufrService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code SEARCH  /ufrs/_search?query=:query} : search for the ufr corresponding
     * to the query.
     *
     * @param query the query of the ufr search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search")
    public ResponseEntity<List<UfrDTO>> searchUfrs(
        @RequestParam("query") String query,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to search for a page of Ufrs for query {}", query);
        try {
            Page<UfrDTO> page = ufrService.search(query, pageable);
            HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
            return ResponseEntity.ok().headers(headers).body(page.getContent());
        } catch (RuntimeException e) {
            throw ElasticsearchExceptionMapper.mapException(e);
        }
    }

    @GetMapping("/universites/{universiteId}")
    public ResponseEntity<List<UfrDTO>> getAllUfrByUniversite(
        @PathVariable Long universiteId,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get a page of UFRs by Universite ID : {}", universiteId);
        Page<UfrDTO> page = ufrService.getAllUfrByUniversite(universiteId, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    @GetMapping("/ministeres/{ministereId}")
    public ResponseEntity<List<UfrDTO>> getAllUfrByMinistere(
        @PathVariable Long ministereId,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get a page of UFRs by Ministere ID : {}", ministereId);
        Page<UfrDTO> page = ufrService.getAllUfrByMinistere(ministereId, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }
}
