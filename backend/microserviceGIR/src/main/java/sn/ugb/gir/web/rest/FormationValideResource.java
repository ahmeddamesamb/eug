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
import sn.ugb.gir.repository.FormationValideRepository;
import sn.ugb.gir.service.FormationValideService;
import sn.ugb.gir.service.dto.FormationValideDTO;
import sn.ugb.gir.web.rest.errors.BadRequestAlertException;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link sn.ugb.gir.domain.FormationValide}.
 */
@RestController
@RequestMapping("/api/formation-valides")
public class FormationValideResource {

    private final Logger log = LoggerFactory.getLogger(FormationValideResource.class);

    private static final String ENTITY_NAME = "microserviceGirFormationValide";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final FormationValideService formationValideService;

    private final FormationValideRepository formationValideRepository;

    public FormationValideResource(FormationValideService formationValideService, FormationValideRepository formationValideRepository) {
        this.formationValideService = formationValideService;
        this.formationValideRepository = formationValideRepository;
    }

    /**
     * {@code POST  /formation-valides} : Create a new formationValide.
     *
     * @param formationValideDTO the formationValideDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new formationValideDTO, or with status {@code 400 (Bad Request)} if the formationValide has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<FormationValideDTO> createFormationValide(@RequestBody FormationValideDTO formationValideDTO)
        throws URISyntaxException {
        log.debug("REST request to save FormationValide : {}", formationValideDTO);
        if (formationValideDTO.getId() != null) {
            throw new BadRequestAlertException("A new formationValide cannot already have an ID", ENTITY_NAME, "idexists");
        }
        FormationValideDTO result = formationValideService.save(formationValideDTO);
        return ResponseEntity
            .created(new URI("/api/formation-valides/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /formation-valides/:id} : Updates an existing formationValide.
     *
     * @param id the id of the formationValideDTO to save.
     * @param formationValideDTO the formationValideDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated formationValideDTO,
     * or with status {@code 400 (Bad Request)} if the formationValideDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the formationValideDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<FormationValideDTO> updateFormationValide(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody FormationValideDTO formationValideDTO
    ) throws URISyntaxException {
        log.debug("REST request to update FormationValide : {}, {}", id, formationValideDTO);
        if (formationValideDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, formationValideDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!formationValideRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        FormationValideDTO result = formationValideService.update(formationValideDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, formationValideDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /formation-valides/:id} : Partial updates given fields of an existing formationValide, field will ignore if it is null
     *
     * @param id the id of the formationValideDTO to save.
     * @param formationValideDTO the formationValideDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated formationValideDTO,
     * or with status {@code 400 (Bad Request)} if the formationValideDTO is not valid,
     * or with status {@code 404 (Not Found)} if the formationValideDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the formationValideDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<FormationValideDTO> partialUpdateFormationValide(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody FormationValideDTO formationValideDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update FormationValide partially : {}, {}", id, formationValideDTO);
        if (formationValideDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, formationValideDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!formationValideRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<FormationValideDTO> result = formationValideService.partialUpdate(formationValideDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, formationValideDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /formation-valides} : get all the formationValides.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of formationValides in body.
     */
    @GetMapping("")
    public ResponseEntity<List<FormationValideDTO>> getAllFormationValides(
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get a page of FormationValides");
        Page<FormationValideDTO> page = formationValideService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /formation-valides/:id} : get the "id" formationValide.
     *
     * @param id the id of the formationValideDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the formationValideDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<FormationValideDTO> getFormationValide(@PathVariable("id") Long id) {
        log.debug("REST request to get FormationValide : {}", id);
        Optional<FormationValideDTO> formationValideDTO = formationValideService.findOne(id);
        return ResponseUtil.wrapOrNotFound(formationValideDTO);
    }

    /**
     * {@code DELETE  /formation-valides/:id} : delete the "id" formationValide.
     *
     * @param id the id of the formationValideDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFormationValide(@PathVariable("id") Long id) {
        log.debug("REST request to delete FormationValide : {}", id);
        formationValideService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
