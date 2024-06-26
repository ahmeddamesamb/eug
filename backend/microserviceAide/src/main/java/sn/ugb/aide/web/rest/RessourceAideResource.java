package sn.ugb.aide.web.rest;

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
import sn.ugb.aide.repository.RessourceAideRepository;
import sn.ugb.aide.service.RessourceAideService;
import sn.ugb.aide.service.dto.RessourceAideDTO;
import sn.ugb.aide.web.rest.errors.BadRequestAlertException;
import sn.ugb.aide.web.rest.errors.ElasticsearchExceptionMapper;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link sn.ugb.aide.domain.RessourceAide}.
 */
@RestController
@RequestMapping("/api/ressource-aides")
public class RessourceAideResource {

    private final Logger log = LoggerFactory.getLogger(RessourceAideResource.class);

    private static final String ENTITY_NAME = "microserviceAideRessourceAide";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final RessourceAideService ressourceAideService;

    private final RessourceAideRepository ressourceAideRepository;

    public RessourceAideResource(RessourceAideService ressourceAideService, RessourceAideRepository ressourceAideRepository) {
        this.ressourceAideService = ressourceAideService;
        this.ressourceAideRepository = ressourceAideRepository;
    }

    /**
     * {@code POST  /ressource-aides} : Create a new ressourceAide.
     *
     * @param ressourceAideDTO the ressourceAideDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new ressourceAideDTO, or with status {@code 400 (Bad Request)} if the ressourceAide has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<RessourceAideDTO> createRessourceAide(@Valid @RequestBody RessourceAideDTO ressourceAideDTO)
        throws URISyntaxException {
        log.debug("REST request to save RessourceAide : {}", ressourceAideDTO);
        if (ressourceAideDTO.getId() != null) {
            throw new BadRequestAlertException("A new ressourceAide cannot already have an ID", ENTITY_NAME, "idexists");
        }
        RessourceAideDTO result = ressourceAideService.save(ressourceAideDTO);
        return ResponseEntity
            .created(new URI("/api/ressource-aides/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /ressource-aides/:id} : Updates an existing ressourceAide.
     *
     * @param id the id of the ressourceAideDTO to save.
     * @param ressourceAideDTO the ressourceAideDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated ressourceAideDTO,
     * or with status {@code 400 (Bad Request)} if the ressourceAideDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the ressourceAideDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<RessourceAideDTO> updateRessourceAide(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody RessourceAideDTO ressourceAideDTO
    ) throws URISyntaxException {
        log.debug("REST request to update RessourceAide : {}, {}", id, ressourceAideDTO);
        if (ressourceAideDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, ressourceAideDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!ressourceAideRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        RessourceAideDTO result = ressourceAideService.update(ressourceAideDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, ressourceAideDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /ressource-aides/:id} : Partial updates given fields of an existing ressourceAide, field will ignore if it is null
     *
     * @param id the id of the ressourceAideDTO to save.
     * @param ressourceAideDTO the ressourceAideDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated ressourceAideDTO,
     * or with status {@code 400 (Bad Request)} if the ressourceAideDTO is not valid,
     * or with status {@code 404 (Not Found)} if the ressourceAideDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the ressourceAideDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<RessourceAideDTO> partialUpdateRessourceAide(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody RessourceAideDTO ressourceAideDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update RessourceAide partially : {}, {}", id, ressourceAideDTO);
        if (ressourceAideDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, ressourceAideDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!ressourceAideRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<RessourceAideDTO> result = ressourceAideService.partialUpdate(ressourceAideDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, ressourceAideDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /ressource-aides} : get all the ressourceAides.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of ressourceAides in body.
     */
    @GetMapping("")
    public ResponseEntity<List<RessourceAideDTO>> getAllRessourceAides(@org.springdoc.core.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get a page of RessourceAides");
        Page<RessourceAideDTO> page = ressourceAideService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /ressource-aides/:id} : get the "id" ressourceAide.
     *
     * @param id the id of the ressourceAideDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the ressourceAideDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<RessourceAideDTO> getRessourceAide(@PathVariable("id") Long id) {
        log.debug("REST request to get RessourceAide : {}", id);
        Optional<RessourceAideDTO> ressourceAideDTO = ressourceAideService.findOne(id);
        return ResponseUtil.wrapOrNotFound(ressourceAideDTO);
    }

    /**
     * {@code DELETE  /ressource-aides/:id} : delete the "id" ressourceAide.
     *
     * @param id the id of the ressourceAideDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRessourceAide(@PathVariable("id") Long id) {
        log.debug("REST request to delete RessourceAide : {}", id);
        ressourceAideService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code SEARCH  /ressource-aides/_search?query=:query} : search for the ressourceAide corresponding
     * to the query.
     *
     * @param query the query of the ressourceAide search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search")
    public ResponseEntity<List<RessourceAideDTO>> searchRessourceAides(
        @RequestParam("query") String query,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to search for a page of RessourceAides for query {}", query);
        try {
            Page<RessourceAideDTO> page = ressourceAideService.search(query, pageable);
            HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
            return ResponseEntity.ok().headers(headers).body(page.getContent());
        } catch (RuntimeException e) {
            throw ElasticsearchExceptionMapper.mapException(e);
        }
    }
}
