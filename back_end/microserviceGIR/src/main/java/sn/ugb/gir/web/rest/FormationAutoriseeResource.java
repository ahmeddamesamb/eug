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
import sn.ugb.gir.repository.FormationAutoriseeRepository;
import sn.ugb.gir.service.FormationAutoriseeService;
import sn.ugb.gir.service.dto.FormationAutoriseeDTO;
import sn.ugb.gir.web.rest.errors.BadRequestAlertException;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link sn.ugb.gir.domain.FormationAutorisee}.
 */
@RestController
@RequestMapping("/api/formation-autorisees")
public class FormationAutoriseeResource {

    private final Logger log = LoggerFactory.getLogger(FormationAutoriseeResource.class);

    private static final String ENTITY_NAME = "microserviceGirFormationAutorisee";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final FormationAutoriseeService formationAutoriseeService;

    private final FormationAutoriseeRepository formationAutoriseeRepository;

    public FormationAutoriseeResource(
        FormationAutoriseeService formationAutoriseeService,
        FormationAutoriseeRepository formationAutoriseeRepository
    ) {
        this.formationAutoriseeService = formationAutoriseeService;
        this.formationAutoriseeRepository = formationAutoriseeRepository;
    }

    /**
     * {@code POST  /formation-autorisees} : Create a new formationAutorisee.
     *
     * @param formationAutoriseeDTO the formationAutoriseeDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new formationAutoriseeDTO, or with status {@code 400 (Bad Request)} if the formationAutorisee has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<FormationAutoriseeDTO> createFormationAutorisee(@RequestBody FormationAutoriseeDTO formationAutoriseeDTO)
        throws URISyntaxException {
        log.debug("REST request to save FormationAutorisee : {}", formationAutoriseeDTO);
        if (formationAutoriseeDTO.getId() != null) {
            throw new BadRequestAlertException("A new formationAutorisee cannot already have an ID", ENTITY_NAME, "idexists");
        }
        FormationAutoriseeDTO result = formationAutoriseeService.save(formationAutoriseeDTO);
        return ResponseEntity
            .created(new URI("/api/formation-autorisees/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /formation-autorisees/:id} : Updates an existing formationAutorisee.
     *
     * @param id the id of the formationAutoriseeDTO to save.
     * @param formationAutoriseeDTO the formationAutoriseeDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated formationAutoriseeDTO,
     * or with status {@code 400 (Bad Request)} if the formationAutoriseeDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the formationAutoriseeDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<FormationAutoriseeDTO> updateFormationAutorisee(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody FormationAutoriseeDTO formationAutoriseeDTO
    ) throws URISyntaxException {
        log.debug("REST request to update FormationAutorisee : {}, {}", id, formationAutoriseeDTO);
        if (formationAutoriseeDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, formationAutoriseeDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!formationAutoriseeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        FormationAutoriseeDTO result = formationAutoriseeService.update(formationAutoriseeDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, formationAutoriseeDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /formation-autorisees/:id} : Partial updates given fields of an existing formationAutorisee, field will ignore if it is null
     *
     * @param id the id of the formationAutoriseeDTO to save.
     * @param formationAutoriseeDTO the formationAutoriseeDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated formationAutoriseeDTO,
     * or with status {@code 400 (Bad Request)} if the formationAutoriseeDTO is not valid,
     * or with status {@code 404 (Not Found)} if the formationAutoriseeDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the formationAutoriseeDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<FormationAutoriseeDTO> partialUpdateFormationAutorisee(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody FormationAutoriseeDTO formationAutoriseeDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update FormationAutorisee partially : {}, {}", id, formationAutoriseeDTO);
        if (formationAutoriseeDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, formationAutoriseeDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!formationAutoriseeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<FormationAutoriseeDTO> result = formationAutoriseeService.partialUpdate(formationAutoriseeDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, formationAutoriseeDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /formation-autorisees} : get all the formationAutorisees.
     *
     * @param pageable the pagination information.
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of formationAutorisees in body.
     */
    @GetMapping("")
    public ResponseEntity<List<FormationAutoriseeDTO>> getAllFormationAutorisees(
        @org.springdoc.core.annotations.ParameterObject Pageable pageable,
        @RequestParam(name = "eagerload", required = false, defaultValue = "true") boolean eagerload
    ) {
        log.debug("REST request to get a page of FormationAutorisees");
        Page<FormationAutoriseeDTO> page;
        if (eagerload) {
            page = formationAutoriseeService.findAllWithEagerRelationships(pageable);
        } else {
            page = formationAutoriseeService.findAll(pageable);
        }
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /formation-autorisees/:id} : get the "id" formationAutorisee.
     *
     * @param id the id of the formationAutoriseeDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the formationAutoriseeDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<FormationAutoriseeDTO> getFormationAutorisee(@PathVariable("id") Long id) {
        log.debug("REST request to get FormationAutorisee : {}", id);
        Optional<FormationAutoriseeDTO> formationAutoriseeDTO = formationAutoriseeService.findOne(id);
        return ResponseUtil.wrapOrNotFound(formationAutoriseeDTO);
    }

    /**
     * {@code DELETE  /formation-autorisees/:id} : delete the "id" formationAutorisee.
     *
     * @param id the id of the formationAutoriseeDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFormationAutorisee(@PathVariable("id") Long id) {
        log.debug("REST request to delete FormationAutorisee : {}", id);
        formationAutoriseeService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
