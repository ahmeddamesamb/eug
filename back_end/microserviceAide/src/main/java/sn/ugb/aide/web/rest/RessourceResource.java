package sn.ugb.aide.web.rest;

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
import sn.ugb.aide.repository.RessourceRepository;
import sn.ugb.aide.service.RessourceService;
import sn.ugb.aide.service.dto.RessourceDTO;
import sn.ugb.aide.web.rest.errors.BadRequestAlertException;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link sn.ugb.aide.domain.Ressource}.
 */
@RestController
@RequestMapping("/api/ressources")
public class RessourceResource {

    private final Logger log = LoggerFactory.getLogger(RessourceResource.class);

    private static final String ENTITY_NAME = "microserviceAideRessource";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final RessourceService ressourceService;

    private final RessourceRepository ressourceRepository;

    public RessourceResource(RessourceService ressourceService, RessourceRepository ressourceRepository) {
        this.ressourceService = ressourceService;
        this.ressourceRepository = ressourceRepository;
    }

    /**
     * {@code POST  /ressources} : Create a new ressource.
     *
     * @param ressourceDTO the ressourceDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new ressourceDTO, or with status {@code 400 (Bad Request)} if the ressource has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<RessourceDTO> createRessource(@RequestBody RessourceDTO ressourceDTO) throws URISyntaxException {
        log.debug("REST request to save Ressource : {}", ressourceDTO);
        if (ressourceDTO.getId() != null) {
            throw new BadRequestAlertException("A new ressource cannot already have an ID", ENTITY_NAME, "idexists");
        }
        RessourceDTO result = ressourceService.save(ressourceDTO);
        return ResponseEntity
            .created(new URI("/api/ressources/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /ressources/:id} : Updates an existing ressource.
     *
     * @param id the id of the ressourceDTO to save.
     * @param ressourceDTO the ressourceDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated ressourceDTO,
     * or with status {@code 400 (Bad Request)} if the ressourceDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the ressourceDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<RessourceDTO> updateRessource(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody RessourceDTO ressourceDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Ressource : {}, {}", id, ressourceDTO);
        if (ressourceDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, ressourceDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!ressourceRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        RessourceDTO result = ressourceService.update(ressourceDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, ressourceDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /ressources/:id} : Partial updates given fields of an existing ressource, field will ignore if it is null
     *
     * @param id the id of the ressourceDTO to save.
     * @param ressourceDTO the ressourceDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated ressourceDTO,
     * or with status {@code 400 (Bad Request)} if the ressourceDTO is not valid,
     * or with status {@code 404 (Not Found)} if the ressourceDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the ressourceDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<RessourceDTO> partialUpdateRessource(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody RessourceDTO ressourceDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Ressource partially : {}, {}", id, ressourceDTO);
        if (ressourceDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, ressourceDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!ressourceRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<RessourceDTO> result = ressourceService.partialUpdate(ressourceDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, ressourceDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /ressources} : get all the ressources.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of ressources in body.
     */
    @GetMapping("")
    public ResponseEntity<List<RessourceDTO>> getAllRessources(@org.springdoc.core.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get a page of Ressources");
        Page<RessourceDTO> page = ressourceService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /ressources/:id} : get the "id" ressource.
     *
     * @param id the id of the ressourceDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the ressourceDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<RessourceDTO> getRessource(@PathVariable("id") Long id) {
        log.debug("REST request to get Ressource : {}", id);
        Optional<RessourceDTO> ressourceDTO = ressourceService.findOne(id);
        return ResponseUtil.wrapOrNotFound(ressourceDTO);
    }

    /**
     * {@code DELETE  /ressources/:id} : delete the "id" ressource.
     *
     * @param id the id of the ressourceDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRessource(@PathVariable("id") Long id) {
        log.debug("REST request to delete Ressource : {}", id);
        ressourceService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
