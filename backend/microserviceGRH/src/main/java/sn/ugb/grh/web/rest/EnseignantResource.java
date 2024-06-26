package sn.ugb.grh.web.rest;

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
import sn.ugb.grh.repository.EnseignantRepository;
import sn.ugb.grh.service.EnseignantService;
import sn.ugb.grh.service.dto.EnseignantDTO;
import sn.ugb.grh.web.rest.errors.BadRequestAlertException;
import sn.ugb.grh.web.rest.errors.ElasticsearchExceptionMapper;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link sn.ugb.grh.domain.Enseignant}.
 */
@RestController
@RequestMapping("/api/enseignants")
public class EnseignantResource {

    private final Logger log = LoggerFactory.getLogger(EnseignantResource.class);

    private static final String ENTITY_NAME = "microserviceGrhEnseignant";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final EnseignantService enseignantService;

    private final EnseignantRepository enseignantRepository;

    public EnseignantResource(EnseignantService enseignantService, EnseignantRepository enseignantRepository) {
        this.enseignantService = enseignantService;
        this.enseignantRepository = enseignantRepository;
    }

    /**
     * {@code POST  /enseignants} : Create a new enseignant.
     *
     * @param enseignantDTO the enseignantDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new enseignantDTO, or with status {@code 400 (Bad Request)} if the enseignant has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<EnseignantDTO> createEnseignant(@Valid @RequestBody EnseignantDTO enseignantDTO) throws URISyntaxException {
        log.debug("REST request to save Enseignant : {}", enseignantDTO);
        if (enseignantDTO.getId() != null) {
            throw new BadRequestAlertException("A new enseignant cannot already have an ID", ENTITY_NAME, "idexists");
        }
        EnseignantDTO result = enseignantService.save(enseignantDTO);
        return ResponseEntity
            .created(new URI("/api/enseignants/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /enseignants/:id} : Updates an existing enseignant.
     *
     * @param id the id of the enseignantDTO to save.
     * @param enseignantDTO the enseignantDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated enseignantDTO,
     * or with status {@code 400 (Bad Request)} if the enseignantDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the enseignantDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<EnseignantDTO> updateEnseignant(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody EnseignantDTO enseignantDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Enseignant : {}, {}", id, enseignantDTO);
        if (enseignantDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, enseignantDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!enseignantRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        EnseignantDTO result = enseignantService.update(enseignantDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, enseignantDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /enseignants/:id} : Partial updates given fields of an existing enseignant, field will ignore if it is null
     *
     * @param id the id of the enseignantDTO to save.
     * @param enseignantDTO the enseignantDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated enseignantDTO,
     * or with status {@code 400 (Bad Request)} if the enseignantDTO is not valid,
     * or with status {@code 404 (Not Found)} if the enseignantDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the enseignantDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<EnseignantDTO> partialUpdateEnseignant(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody EnseignantDTO enseignantDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Enseignant partially : {}, {}", id, enseignantDTO);
        if (enseignantDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, enseignantDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!enseignantRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<EnseignantDTO> result = enseignantService.partialUpdate(enseignantDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, enseignantDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /enseignants} : get all the enseignants.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of enseignants in body.
     */
    @GetMapping("")
    public ResponseEntity<List<EnseignantDTO>> getAllEnseignants(@org.springdoc.core.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get a page of Enseignants");
        Page<EnseignantDTO> page = enseignantService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /enseignants/:id} : get the "id" enseignant.
     *
     * @param id the id of the enseignantDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the enseignantDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<EnseignantDTO> getEnseignant(@PathVariable("id") Long id) {
        log.debug("REST request to get Enseignant : {}", id);
        Optional<EnseignantDTO> enseignantDTO = enseignantService.findOne(id);
        return ResponseUtil.wrapOrNotFound(enseignantDTO);
    }

    /**
     * {@code DELETE  /enseignants/:id} : delete the "id" enseignant.
     *
     * @param id the id of the enseignantDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEnseignant(@PathVariable("id") Long id) {
        log.debug("REST request to delete Enseignant : {}", id);
        enseignantService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code SEARCH  /enseignants/_search?query=:query} : search for the enseignant corresponding
     * to the query.
     *
     * @param query the query of the enseignant search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search")
    public ResponseEntity<List<EnseignantDTO>> searchEnseignants(
        @RequestParam("query") String query,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to search for a page of Enseignants for query {}", query);
        try {
            Page<EnseignantDTO> page = enseignantService.search(query, pageable);
            HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
            return ResponseEntity.ok().headers(headers).body(page.getContent());
        } catch (RuntimeException e) {
            throw ElasticsearchExceptionMapper.mapException(e);
        }
    }
}
