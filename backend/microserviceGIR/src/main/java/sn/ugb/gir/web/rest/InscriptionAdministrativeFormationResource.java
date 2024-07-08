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
import sn.ugb.gir.repository.InscriptionAdministrativeFormationRepository;
import sn.ugb.gir.service.InscriptionAdministrativeFormationService;
import sn.ugb.gir.service.dto.InformationsDerniersInscriptionsDTO;
import sn.ugb.gir.service.dto.InscriptionAdministrativeFormationDTO;
import sn.ugb.gir.web.rest.errors.BadRequestAlertException;
import sn.ugb.gir.web.rest.errors.ElasticsearchExceptionMapper;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link sn.ugb.gir.domain.InscriptionAdministrativeFormation}.
 */
@RestController
@RequestMapping("/api/inscription-administrative-formations")
public class InscriptionAdministrativeFormationResource {

    private final Logger log = LoggerFactory.getLogger(InscriptionAdministrativeFormationResource.class);

    private static final String ENTITY_NAME = "microserviceGirInscriptionAdministrativeFormation";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final InscriptionAdministrativeFormationService inscriptionAdministrativeFormationService;

    private final InscriptionAdministrativeFormationRepository inscriptionAdministrativeFormationRepository;

    public InscriptionAdministrativeFormationResource(
        InscriptionAdministrativeFormationService inscriptionAdministrativeFormationService,
        InscriptionAdministrativeFormationRepository inscriptionAdministrativeFormationRepository
    ) {
        this.inscriptionAdministrativeFormationService = inscriptionAdministrativeFormationService;
        this.inscriptionAdministrativeFormationRepository = inscriptionAdministrativeFormationRepository;
    }

    /**
     * {@code POST  /inscription-administrative-formations} : Create a new inscriptionAdministrativeFormation.
     *
     * @param inscriptionAdministrativeFormationDTO the inscriptionAdministrativeFormationDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new inscriptionAdministrativeFormationDTO, or with status {@code 400 (Bad Request)} if the inscriptionAdministrativeFormation has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<InscriptionAdministrativeFormationDTO> createInscriptionAdministrativeFormation(
        @Valid @RequestBody InscriptionAdministrativeFormationDTO inscriptionAdministrativeFormationDTO
    ) throws URISyntaxException {
        log.debug("REST request to save InscriptionAdministrativeFormation : {}", inscriptionAdministrativeFormationDTO);
        if (inscriptionAdministrativeFormationDTO.getId() != null) {
            throw new BadRequestAlertException(
                "A new inscriptionAdministrativeFormation cannot already have an ID",
                ENTITY_NAME,
                "idexists"
            );
        }
        InscriptionAdministrativeFormationDTO result = inscriptionAdministrativeFormationService.save(
            inscriptionAdministrativeFormationDTO
        );
        return ResponseEntity
            .created(new URI("/api/inscription-administrative-formations/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /inscription-administrative-formations/:id} : Updates an existing inscriptionAdministrativeFormation.
     *
     * @param id the id of the inscriptionAdministrativeFormationDTO to save.
     * @param inscriptionAdministrativeFormationDTO the inscriptionAdministrativeFormationDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated inscriptionAdministrativeFormationDTO,
     * or with status {@code 400 (Bad Request)} if the inscriptionAdministrativeFormationDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the inscriptionAdministrativeFormationDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<InscriptionAdministrativeFormationDTO> updateInscriptionAdministrativeFormation(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody InscriptionAdministrativeFormationDTO inscriptionAdministrativeFormationDTO
    ) throws URISyntaxException {
        log.debug("REST request to update InscriptionAdministrativeFormation : {}, {}", id, inscriptionAdministrativeFormationDTO);

        validateDataUpdate(inscriptionAdministrativeFormationDTO, id);
        InscriptionAdministrativeFormationDTO result = inscriptionAdministrativeFormationService.update(
            inscriptionAdministrativeFormationDTO
        );
        return ResponseEntity
            .ok()
            .headers(
                HeaderUtil.createEntityUpdateAlert(
                    applicationName,
                    true,
                    ENTITY_NAME,
                    inscriptionAdministrativeFormationDTO.getId().toString()
                )
            )
            .body(result);
    }

    /**
     * {@code PATCH  /inscription-administrative-formations/:id} : Partial updates given fields of an existing inscriptionAdministrativeFormation, field will ignore if it is null
     *
     * @param id the id of the inscriptionAdministrativeFormationDTO to save.
     * @param inscriptionAdministrativeFormationDTO the inscriptionAdministrativeFormationDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated inscriptionAdministrativeFormationDTO,
     * or with status {@code 400 (Bad Request)} if the inscriptionAdministrativeFormationDTO is not valid,
     * or with status {@code 404 (Not Found)} if the inscriptionAdministrativeFormationDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the inscriptionAdministrativeFormationDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<InscriptionAdministrativeFormationDTO> partialUpdateInscriptionAdministrativeFormation(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody InscriptionAdministrativeFormationDTO inscriptionAdministrativeFormationDTO
    ) throws URISyntaxException {
        log.debug(
            "REST request to partial update InscriptionAdministrativeFormation partially : {}, {}",
            id,
            inscriptionAdministrativeFormationDTO
        );

        validateDataUpdate(inscriptionAdministrativeFormationDTO, id);
        Optional<InscriptionAdministrativeFormationDTO> result = inscriptionAdministrativeFormationService.partialUpdate(
            inscriptionAdministrativeFormationDTO
        );

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, inscriptionAdministrativeFormationDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /inscription-administrative-formations} : get all the inscriptionAdministrativeFormations.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of inscriptionAdministrativeFormations in body.
     */
    @GetMapping("")
    public ResponseEntity<List<InscriptionAdministrativeFormationDTO>> getAllInscriptionAdministrativeFormations(
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get a page of InscriptionAdministrativeFormations");
        Page<InscriptionAdministrativeFormationDTO> page = inscriptionAdministrativeFormationService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /inscription-administrative-formations/:id} : get the "id" inscriptionAdministrativeFormation.
     *
     * @param id the id of the inscriptionAdministrativeFormationDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the inscriptionAdministrativeFormationDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<InscriptionAdministrativeFormationDTO> getInscriptionAdministrativeFormation(@PathVariable("id") Long id) {
        log.debug("REST request to get InscriptionAdministrativeFormation : {}", id);
        Optional<InscriptionAdministrativeFormationDTO> inscriptionAdministrativeFormationDTO =
            inscriptionAdministrativeFormationService.findOne(id);
        return ResponseUtil.wrapOrNotFound(inscriptionAdministrativeFormationDTO);
    }

    /**
     * {@code DELETE  /inscription-administrative-formations/:id} : delete the "id" inscriptionAdministrativeFormation.
     *
     * @param id the id of the inscriptionAdministrativeFormationDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteInscriptionAdministrativeFormation(@PathVariable("id") Long id) {
        log.debug("REST request to delete InscriptionAdministrativeFormation : {}", id);
        inscriptionAdministrativeFormationService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code SEARCH  /inscription-administrative-formations/_search?query=:query} : search for the inscriptionAdministrativeFormation corresponding
     * to the query.
     *
     * @param query the query of the inscriptionAdministrativeFormation search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search")
    public ResponseEntity<List<InscriptionAdministrativeFormationDTO>> searchInscriptionAdministrativeFormations(
        @RequestParam("query") String query,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to search for a page of InscriptionAdministrativeFormations for query {}", query);
        try {
            Page<InscriptionAdministrativeFormationDTO> page = inscriptionAdministrativeFormationService.search(query, pageable);
            HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
            return ResponseEntity.ok().headers(headers).body(page.getContent());
        } catch (RuntimeException e) {
            throw ElasticsearchExceptionMapper.mapException(e);
        }
    }

    /**
     * {@code GET  /inscription-administrative-formations} : get all the inscriptionAdministrativeFormations by the last anneeacademique.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of inscriptionAdministrativeFormations in body.
     */
    @GetMapping("/derniersInscrits")
    public ResponseEntity<List<InformationsDerniersInscriptionsDTO>> getAllDerniersInscriptionAdministrativeFormations(
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get a page of InscriptionAdministrativeFormations by the last anneeacademique.");
        Page<InformationsDerniersInscriptionsDTO> page = inscriptionAdministrativeFormationService.findAllByDernierInscription(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }


    public void validateDataUpdate(InscriptionAdministrativeFormationDTO inscriptionAdministrativeFormationDTO, Long id){
        if (inscriptionAdministrativeFormationDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, inscriptionAdministrativeFormationDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!inscriptionAdministrativeFormationRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }
    }
}
