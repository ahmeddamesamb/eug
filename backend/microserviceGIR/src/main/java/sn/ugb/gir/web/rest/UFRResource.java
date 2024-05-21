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
import sn.ugb.gir.repository.UFRRepository;
import sn.ugb.gir.service.UFRService;
import sn.ugb.gir.service.dto.UFRDTO;
import sn.ugb.gir.web.rest.errors.BadRequestAlertException;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link sn.ugb.gir.domain.UFR}.
 */
@RestController
@RequestMapping("/api/ufrs")
public class UFRResource {

    private final Logger log = LoggerFactory.getLogger(UFRResource.class);

    private static final String ENTITY_NAME = "microserviceGirufr";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final UFRService uFRService;

    private final UFRRepository uFRRepository;

    public UFRResource(UFRService uFRService, UFRRepository uFRRepository) {
        this.uFRService = uFRService;
        this.uFRRepository = uFRRepository;
    }

    /**
     * {@code POST  /ufrs} : Create a new uFR.
     *
     * @param uFRDTO the uFRDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new uFRDTO, or with status {@code 400 (Bad Request)} if the uFR has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<UFRDTO> createUFR(@RequestBody UFRDTO uFRDTO) throws URISyntaxException {
        log.debug("REST request to save UFR : {}", uFRDTO);
        if (uFRDTO.getId() != null) {
            throw new BadRequestAlertException("A new uFR cannot already have an ID", ENTITY_NAME, "idexists");
        }
        UFRDTO result = uFRService.save(uFRDTO);
        return ResponseEntity
            .created(new URI("/api/ufrs/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /ufrs/:id} : Updates an existing uFR.
     *
     * @param id the id of the uFRDTO to save.
     * @param uFRDTO the uFRDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated uFRDTO,
     * or with status {@code 400 (Bad Request)} if the uFRDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the uFRDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<UFRDTO> updateUFR(@PathVariable(value = "id", required = false) final Long id, @RequestBody UFRDTO uFRDTO)
        throws URISyntaxException {
        log.debug("REST request to update UFR : {}, {}", id, uFRDTO);
        if (uFRDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, uFRDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!uFRRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        UFRDTO result = uFRService.update(uFRDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, uFRDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /ufrs/:id} : Partial updates given fields of an existing uFR, field will ignore if it is null
     *
     * @param id the id of the uFRDTO to save.
     * @param uFRDTO the uFRDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated uFRDTO,
     * or with status {@code 400 (Bad Request)} if the uFRDTO is not valid,
     * or with status {@code 404 (Not Found)} if the uFRDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the uFRDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<UFRDTO> partialUpdateUFR(@PathVariable(value = "id", required = false) final Long id, @RequestBody UFRDTO uFRDTO)
        throws URISyntaxException {
        log.debug("REST request to partial update UFR partially : {}, {}", id, uFRDTO);
        if (uFRDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, uFRDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!uFRRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<UFRDTO> result = uFRService.partialUpdate(uFRDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, uFRDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /ufrs} : get all the uFRS.
     *
     * @param pageable the pagination information.
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of uFRS in body.
     */
    @GetMapping("")
    public ResponseEntity<List<UFRDTO>> getAllUFRS(
        @org.springdoc.core.annotations.ParameterObject Pageable pageable,
        @RequestParam(name = "eagerload", required = false, defaultValue = "true") boolean eagerload
    ) {
        log.debug("REST request to get a page of UFRS");
        Page<UFRDTO> page;
        if (eagerload) {
            page = uFRService.findAllWithEagerRelationships(pageable);
        } else {
            page = uFRService.findAll(pageable);
        }
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /ufrs/:id} : get the "id" uFR.
     *
     * @param id the id of the uFRDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the uFRDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<UFRDTO> getUFR(@PathVariable("id") Long id) {
        log.debug("REST request to get UFR : {}", id);
        Optional<UFRDTO> uFRDTO = uFRService.findOne(id);
        return ResponseUtil.wrapOrNotFound(uFRDTO);
    }

    /**
     * {@code DELETE  /ufrs/:id} : delete the "id" uFR.
     *
     * @param id the id of the uFRDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUFR(@PathVariable("id") Long id) {
        log.debug("REST request to delete UFR : {}", id);
        uFRService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
