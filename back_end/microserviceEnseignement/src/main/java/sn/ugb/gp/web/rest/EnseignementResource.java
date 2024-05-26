package sn.ugb.gp.web.rest;

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
import sn.ugb.gp.repository.EnseignementRepository;
import sn.ugb.gp.service.EnseignementService;
import sn.ugb.gp.service.dto.EnseignementDTO;
import sn.ugb.gp.web.rest.errors.BadRequestAlertException;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link sn.ugb.gp.domain.Enseignement}.
 */
@RestController
@RequestMapping("/api/enseignements")
public class EnseignementResource {

    private final Logger log = LoggerFactory.getLogger(EnseignementResource.class);

    private static final String ENTITY_NAME = "microserviceEnseignementEnseignement";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final EnseignementService enseignementService;

    private final EnseignementRepository enseignementRepository;

    public EnseignementResource(EnseignementService enseignementService, EnseignementRepository enseignementRepository) {
        this.enseignementService = enseignementService;
        this.enseignementRepository = enseignementRepository;
    }

    /**
     * {@code POST  /enseignements} : Create a new enseignement.
     *
     * @param enseignementDTO the enseignementDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new enseignementDTO, or with status {@code 400 (Bad Request)} if the enseignement has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<EnseignementDTO> createEnseignement(@RequestBody EnseignementDTO enseignementDTO) throws URISyntaxException {
        log.debug("REST request to save Enseignement : {}", enseignementDTO);
        if (enseignementDTO.getId() != null) {
            throw new BadRequestAlertException("A new enseignement cannot already have an ID", ENTITY_NAME, "idexists");
        }
        EnseignementDTO result = enseignementService.save(enseignementDTO);
        return ResponseEntity
            .created(new URI("/api/enseignements/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /enseignements/:id} : Updates an existing enseignement.
     *
     * @param id the id of the enseignementDTO to save.
     * @param enseignementDTO the enseignementDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated enseignementDTO,
     * or with status {@code 400 (Bad Request)} if the enseignementDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the enseignementDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<EnseignementDTO> updateEnseignement(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody EnseignementDTO enseignementDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Enseignement : {}, {}", id, enseignementDTO);
        if (enseignementDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, enseignementDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!enseignementRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        EnseignementDTO result = enseignementService.update(enseignementDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, enseignementDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /enseignements/:id} : Partial updates given fields of an existing enseignement, field will ignore if it is null
     *
     * @param id the id of the enseignementDTO to save.
     * @param enseignementDTO the enseignementDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated enseignementDTO,
     * or with status {@code 400 (Bad Request)} if the enseignementDTO is not valid,
     * or with status {@code 404 (Not Found)} if the enseignementDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the enseignementDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<EnseignementDTO> partialUpdateEnseignement(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody EnseignementDTO enseignementDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Enseignement partially : {}, {}", id, enseignementDTO);
        if (enseignementDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, enseignementDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!enseignementRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<EnseignementDTO> result = enseignementService.partialUpdate(enseignementDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, enseignementDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /enseignements} : get all the enseignements.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of enseignements in body.
     */
    @GetMapping("")
    public ResponseEntity<List<EnseignementDTO>> getAllEnseignements(@org.springdoc.core.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get a page of Enseignements");
        Page<EnseignementDTO> page = enseignementService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /enseignements/:id} : get the "id" enseignement.
     *
     * @param id the id of the enseignementDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the enseignementDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<EnseignementDTO> getEnseignement(@PathVariable("id") Long id) {
        log.debug("REST request to get Enseignement : {}", id);
        Optional<EnseignementDTO> enseignementDTO = enseignementService.findOne(id);
        return ResponseUtil.wrapOrNotFound(enseignementDTO);
    }

    /**
     * {@code DELETE  /enseignements/:id} : delete the "id" enseignement.
     *
     * @param id the id of the enseignementDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEnseignement(@PathVariable("id") Long id) {
        log.debug("REST request to delete Enseignement : {}", id);
        enseignementService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
