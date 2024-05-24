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
import sn.ugb.gir.repository.TypeSelectionRepository;
import sn.ugb.gir.service.TypeSelectionService;
import sn.ugb.gir.service.dto.TypeSelectionDTO;
import sn.ugb.gir.web.rest.errors.BadRequestAlertException;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link sn.ugb.gir.domain.TypeSelection}.
 */
@RestController
@RequestMapping("/api/type-selections")
public class TypeSelectionResource {

    private final Logger log = LoggerFactory.getLogger(TypeSelectionResource.class);

    private static final String ENTITY_NAME = "microserviceGirTypeSelection";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final TypeSelectionService typeSelectionService;

    private final TypeSelectionRepository typeSelectionRepository;

    public TypeSelectionResource(TypeSelectionService typeSelectionService, TypeSelectionRepository typeSelectionRepository) {
        this.typeSelectionService = typeSelectionService;
        this.typeSelectionRepository = typeSelectionRepository;
    }

    /**
     * {@code POST  /type-selections} : Create a new typeSelection.
     *
     * @param typeSelectionDTO the typeSelectionDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new typeSelectionDTO, or with status {@code 400 (Bad Request)} if the typeSelection has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<TypeSelectionDTO> createTypeSelection(@Valid @RequestBody TypeSelectionDTO typeSelectionDTO)
        throws URISyntaxException {
        log.debug("REST request to save TypeSelection : {}", typeSelectionDTO);
        if (typeSelectionDTO.getId() != null) {
            throw new BadRequestAlertException("A new typeSelection cannot already have an ID", ENTITY_NAME, "idexists");
        }
        TypeSelectionDTO result = typeSelectionService.save(typeSelectionDTO);
        return ResponseEntity
            .created(new URI("/api/type-selections/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /type-selections/:id} : Updates an existing typeSelection.
     *
     * @param id the id of the typeSelectionDTO to save.
     * @param typeSelectionDTO the typeSelectionDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated typeSelectionDTO,
     * or with status {@code 400 (Bad Request)} if the typeSelectionDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the typeSelectionDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<TypeSelectionDTO> updateTypeSelection(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody TypeSelectionDTO typeSelectionDTO
    ) throws URISyntaxException {
        log.debug("REST request to update TypeSelection : {}, {}", id, typeSelectionDTO);
        if (typeSelectionDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, typeSelectionDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!typeSelectionRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        TypeSelectionDTO result = typeSelectionService.update(typeSelectionDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, typeSelectionDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /type-selections/:id} : Partial updates given fields of an existing typeSelection, field will ignore if it is null
     *
     * @param id the id of the typeSelectionDTO to save.
     * @param typeSelectionDTO the typeSelectionDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated typeSelectionDTO,
     * or with status {@code 400 (Bad Request)} if the typeSelectionDTO is not valid,
     * or with status {@code 404 (Not Found)} if the typeSelectionDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the typeSelectionDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<TypeSelectionDTO> partialUpdateTypeSelection(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody TypeSelectionDTO typeSelectionDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update TypeSelection partially : {}, {}", id, typeSelectionDTO);
        if (typeSelectionDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, typeSelectionDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!typeSelectionRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<TypeSelectionDTO> result = typeSelectionService.partialUpdate(typeSelectionDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, typeSelectionDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /type-selections} : get all the typeSelections.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of typeSelections in body.
     */
    @GetMapping("")
    public ResponseEntity<List<TypeSelectionDTO>> getAllTypeSelections(@org.springdoc.core.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get a page of TypeSelections");
        Page<TypeSelectionDTO> page = typeSelectionService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /type-selections/:id} : get the "id" typeSelection.
     *
     * @param id the id of the typeSelectionDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the typeSelectionDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<TypeSelectionDTO> getTypeSelection(@PathVariable("id") Long id) {
        log.debug("REST request to get TypeSelection : {}", id);
        Optional<TypeSelectionDTO> typeSelectionDTO = typeSelectionService.findOne(id);
        return ResponseUtil.wrapOrNotFound(typeSelectionDTO);
    }

    /**
     * {@code DELETE  /type-selections/:id} : delete the "id" typeSelection.
     *
     * @param id the id of the typeSelectionDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTypeSelection(@PathVariable("id") Long id) {
        log.debug("REST request to delete TypeSelection : {}", id);
        typeSelectionService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
