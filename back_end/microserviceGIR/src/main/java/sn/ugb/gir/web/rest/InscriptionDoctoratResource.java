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
import sn.ugb.gir.repository.InscriptionDoctoratRepository;
import sn.ugb.gir.service.InscriptionDoctoratService;
import sn.ugb.gir.service.dto.InscriptionDoctoratDTO;
import sn.ugb.gir.web.rest.errors.BadRequestAlertException;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link sn.ugb.gir.domain.InscriptionDoctorat}.
 */
@RestController
@RequestMapping("/api/inscription-doctorats")
public class InscriptionDoctoratResource {

    private final Logger log = LoggerFactory.getLogger(InscriptionDoctoratResource.class);

    private static final String ENTITY_NAME = "microserviceGirInscriptionDoctorat";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final InscriptionDoctoratService inscriptionDoctoratService;

    private final InscriptionDoctoratRepository inscriptionDoctoratRepository;

    public InscriptionDoctoratResource(
        InscriptionDoctoratService inscriptionDoctoratService,
        InscriptionDoctoratRepository inscriptionDoctoratRepository
    ) {
        this.inscriptionDoctoratService = inscriptionDoctoratService;
        this.inscriptionDoctoratRepository = inscriptionDoctoratRepository;
    }

    /**
     * {@code POST  /inscription-doctorats} : Create a new inscriptionDoctorat.
     *
     * @param inscriptionDoctoratDTO the inscriptionDoctoratDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new inscriptionDoctoratDTO, or with status {@code 400 (Bad Request)} if the inscriptionDoctorat has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<InscriptionDoctoratDTO> createInscriptionDoctorat(@RequestBody InscriptionDoctoratDTO inscriptionDoctoratDTO)
        throws URISyntaxException {
        log.debug("REST request to save InscriptionDoctorat : {}", inscriptionDoctoratDTO);
        if (inscriptionDoctoratDTO.getId() != null) {
            throw new BadRequestAlertException("A new inscriptionDoctorat cannot already have an ID", ENTITY_NAME, "idexists");
        }
        InscriptionDoctoratDTO result = inscriptionDoctoratService.save(inscriptionDoctoratDTO);
        return ResponseEntity
            .created(new URI("/api/inscription-doctorats/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /inscription-doctorats/:id} : Updates an existing inscriptionDoctorat.
     *
     * @param id the id of the inscriptionDoctoratDTO to save.
     * @param inscriptionDoctoratDTO the inscriptionDoctoratDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated inscriptionDoctoratDTO,
     * or with status {@code 400 (Bad Request)} if the inscriptionDoctoratDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the inscriptionDoctoratDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<InscriptionDoctoratDTO> updateInscriptionDoctorat(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody InscriptionDoctoratDTO inscriptionDoctoratDTO
    ) throws URISyntaxException {
        log.debug("REST request to update InscriptionDoctorat : {}, {}", id, inscriptionDoctoratDTO);
        if (inscriptionDoctoratDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, inscriptionDoctoratDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!inscriptionDoctoratRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        InscriptionDoctoratDTO result = inscriptionDoctoratService.update(inscriptionDoctoratDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, inscriptionDoctoratDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /inscription-doctorats/:id} : Partial updates given fields of an existing inscriptionDoctorat, field will ignore if it is null
     *
     * @param id the id of the inscriptionDoctoratDTO to save.
     * @param inscriptionDoctoratDTO the inscriptionDoctoratDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated inscriptionDoctoratDTO,
     * or with status {@code 400 (Bad Request)} if the inscriptionDoctoratDTO is not valid,
     * or with status {@code 404 (Not Found)} if the inscriptionDoctoratDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the inscriptionDoctoratDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<InscriptionDoctoratDTO> partialUpdateInscriptionDoctorat(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody InscriptionDoctoratDTO inscriptionDoctoratDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update InscriptionDoctorat partially : {}, {}", id, inscriptionDoctoratDTO);
        if (inscriptionDoctoratDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, inscriptionDoctoratDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!inscriptionDoctoratRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<InscriptionDoctoratDTO> result = inscriptionDoctoratService.partialUpdate(inscriptionDoctoratDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, inscriptionDoctoratDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /inscription-doctorats} : get all the inscriptionDoctorats.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of inscriptionDoctorats in body.
     */
    @GetMapping("")
    public ResponseEntity<List<InscriptionDoctoratDTO>> getAllInscriptionDoctorats(
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get a page of InscriptionDoctorats");
        Page<InscriptionDoctoratDTO> page = inscriptionDoctoratService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /inscription-doctorats/:id} : get the "id" inscriptionDoctorat.
     *
     * @param id the id of the inscriptionDoctoratDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the inscriptionDoctoratDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<InscriptionDoctoratDTO> getInscriptionDoctorat(@PathVariable("id") Long id) {
        log.debug("REST request to get InscriptionDoctorat : {}", id);
        Optional<InscriptionDoctoratDTO> inscriptionDoctoratDTO = inscriptionDoctoratService.findOne(id);
        return ResponseUtil.wrapOrNotFound(inscriptionDoctoratDTO);
    }

    /**
     * {@code DELETE  /inscription-doctorats/:id} : delete the "id" inscriptionDoctorat.
     *
     * @param id the id of the inscriptionDoctoratDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteInscriptionDoctorat(@PathVariable("id") Long id) {
        log.debug("REST request to delete InscriptionDoctorat : {}", id);
        inscriptionDoctoratService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
