package sn.ugb.aua.web.rest;

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
import sn.ugb.aua.repository.LaboratoireRepository;
import sn.ugb.aua.service.LaboratoireService;
import sn.ugb.aua.service.dto.LaboratoireDTO;
import sn.ugb.aua.web.rest.errors.BadRequestAlertException;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link sn.ugb.aua.domain.Laboratoire}.
 */
@RestController
@RequestMapping("/api/laboratoires")
public class LaboratoireResource {

    private final Logger log = LoggerFactory.getLogger(LaboratoireResource.class);

    private static final String ENTITY_NAME = "microserviceAuaLaboratoire";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final LaboratoireService laboratoireService;

    private final LaboratoireRepository laboratoireRepository;

    public LaboratoireResource(LaboratoireService laboratoireService, LaboratoireRepository laboratoireRepository) {
        this.laboratoireService = laboratoireService;
        this.laboratoireRepository = laboratoireRepository;
    }

    /**
     * {@code POST  /laboratoires} : Create a new laboratoire.
     *
     * @param laboratoireDTO the laboratoireDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new laboratoireDTO, or with status {@code 400 (Bad Request)} if the laboratoire has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<LaboratoireDTO> createLaboratoire(@Valid @RequestBody LaboratoireDTO laboratoireDTO) throws URISyntaxException {
        log.debug("REST request to save Laboratoire : {}", laboratoireDTO);
        if (laboratoireDTO.getId() != null) {
            throw new BadRequestAlertException("A new laboratoire cannot already have an ID", ENTITY_NAME, "idexists");
        }
        LaboratoireDTO result = laboratoireService.save(laboratoireDTO);
        return ResponseEntity
            .created(new URI("/api/laboratoires/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /laboratoires/:id} : Updates an existing laboratoire.
     *
     * @param id the id of the laboratoireDTO to save.
     * @param laboratoireDTO the laboratoireDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated laboratoireDTO,
     * or with status {@code 400 (Bad Request)} if the laboratoireDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the laboratoireDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<LaboratoireDTO> updateLaboratoire(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody LaboratoireDTO laboratoireDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Laboratoire : {}, {}", id, laboratoireDTO);
        if (laboratoireDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, laboratoireDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!laboratoireRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        LaboratoireDTO result = laboratoireService.update(laboratoireDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, laboratoireDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /laboratoires/:id} : Partial updates given fields of an existing laboratoire, field will ignore if it is null
     *
     * @param id the id of the laboratoireDTO to save.
     * @param laboratoireDTO the laboratoireDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated laboratoireDTO,
     * or with status {@code 400 (Bad Request)} if the laboratoireDTO is not valid,
     * or with status {@code 404 (Not Found)} if the laboratoireDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the laboratoireDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<LaboratoireDTO> partialUpdateLaboratoire(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody LaboratoireDTO laboratoireDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Laboratoire partially : {}, {}", id, laboratoireDTO);
        if (laboratoireDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, laboratoireDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!laboratoireRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<LaboratoireDTO> result = laboratoireService.partialUpdate(laboratoireDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, laboratoireDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /laboratoires} : get all the laboratoires.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of laboratoires in body.
     */
    @GetMapping("")
    public ResponseEntity<List<LaboratoireDTO>> getAllLaboratoires(@org.springdoc.core.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get a page of Laboratoires");
        Page<LaboratoireDTO> page = laboratoireService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /laboratoires/:id} : get the "id" laboratoire.
     *
     * @param id the id of the laboratoireDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the laboratoireDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<LaboratoireDTO> getLaboratoire(@PathVariable("id") Long id) {
        log.debug("REST request to get Laboratoire : {}", id);
        Optional<LaboratoireDTO> laboratoireDTO = laboratoireService.findOne(id);
        return ResponseUtil.wrapOrNotFound(laboratoireDTO);
    }

    /**
     * {@code DELETE  /laboratoires/:id} : delete the "id" laboratoire.
     *
     * @param id the id of the laboratoireDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteLaboratoire(@PathVariable("id") Long id) {
        log.debug("REST request to delete Laboratoire : {}", id);
        laboratoireService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
