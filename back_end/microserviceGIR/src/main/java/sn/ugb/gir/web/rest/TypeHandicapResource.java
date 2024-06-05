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
import sn.ugb.gir.repository.TypeHandicapRepository;
import sn.ugb.gir.service.TypeHandicapService;
import sn.ugb.gir.service.dto.TypeHandicapDTO;
import sn.ugb.gir.web.rest.errors.BadRequestAlertException;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link sn.ugb.gir.domain.TypeHandicap}.
 */
@RestController
@RequestMapping("/api/type-handicaps")
public class TypeHandicapResource {

    private final Logger log = LoggerFactory.getLogger(TypeHandicapResource.class);

    private static final String ENTITY_NAME = "microserviceGirTypeHandicap";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final TypeHandicapService typeHandicapService;

    private final TypeHandicapRepository typeHandicapRepository;

    public TypeHandicapResource(TypeHandicapService typeHandicapService, TypeHandicapRepository typeHandicapRepository) {
        this.typeHandicapService = typeHandicapService;
        this.typeHandicapRepository = typeHandicapRepository;
    }

    /**
     * {@code POST  /type-handicaps} : Create a new typeHandicap.
     *
     * @param typeHandicapDTO the typeHandicapDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new typeHandicapDTO, or with status {@code 400 (Bad Request)} if the typeHandicap has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<TypeHandicapDTO> createTypeHandicap(@Valid @RequestBody TypeHandicapDTO typeHandicapDTO)
        throws URISyntaxException {
        log.debug("REST request to save TypeHandicap : {}", typeHandicapDTO);
        if (typeHandicapDTO.getId() != null) {
            throw new BadRequestAlertException("A new typeHandicap cannot already have an ID", ENTITY_NAME, "idexists");
        }

        if (typeHandicapDTO.getLibelleTypeHandicap() == null) {
            throw new BadRequestAlertException("A new typeHandicap cannot already have an LibelleTypeHandicap", ENTITY_NAME, "LibelleTypeHandicap exists");
        }

        if (typeHandicapDTO.getLibelleTypeHandicap().isBlank()) {
            throw new BadRequestAlertException("A new typeHandicap cannot already have an LibelleTypeHandicap", ENTITY_NAME, "LibelleTypeHandicap is empty");
        }

        TypeHandicapDTO result = typeHandicapService.save(typeHandicapDTO);
        return ResponseEntity
            .created(new URI("/api/type-handicaps/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /type-handicaps/:id} : Updates an existing typeHandicap.
     *
     * @param id the id of the typeHandicapDTO to save.
     * @param typeHandicapDTO the typeHandicapDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated typeHandicapDTO,
     * or with status {@code 400 (Bad Request)} if the typeHandicapDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the typeHandicapDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<TypeHandicapDTO> updateTypeHandicap(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody TypeHandicapDTO typeHandicapDTO
    ) throws URISyntaxException {
        log.debug("REST request to update TypeHandicap : {}, {}", id, typeHandicapDTO);
        if (typeHandicapDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, typeHandicapDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!typeHandicapRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }
        if (typeHandicapDTO.getLibelleTypeHandicap() == null) {
            throw new BadRequestAlertException("A new typeHandicap cannot already have an LibelleTypeHandicap", ENTITY_NAME, "LibelleTypeHandicap exists");
        }

        if (typeHandicapDTO.getLibelleTypeHandicap().isBlank()) {
            throw new BadRequestAlertException("A new typeHandicap cannot already have an LibelleTypeHandicap", ENTITY_NAME, "LibelleTypeHandicap is empty");
        }

        TypeHandicapDTO result = typeHandicapService.update(typeHandicapDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, typeHandicapDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /type-handicaps/:id} : Partial updates given fields of an existing typeHandicap, field will ignore if it is null
     *
     * @param id the id of the typeHandicapDTO to save.
     * @param typeHandicapDTO the typeHandicapDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated typeHandicapDTO,
     * or with status {@code 400 (Bad Request)} if the typeHandicapDTO is not valid,
     * or with status {@code 404 (Not Found)} if the typeHandicapDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the typeHandicapDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<TypeHandicapDTO> partialUpdateTypeHandicap(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody TypeHandicapDTO typeHandicapDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update TypeHandicap partially : {}, {}", id, typeHandicapDTO);
        if (typeHandicapDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, typeHandicapDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!typeHandicapRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<TypeHandicapDTO> result = typeHandicapService.partialUpdate(typeHandicapDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, typeHandicapDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /type-handicaps} : get all the typeHandicaps.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of typeHandicaps in body.
     */
    @GetMapping("")
    public ResponseEntity<List<TypeHandicapDTO>> getAllTypeHandicaps(@org.springdoc.core.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get a page of TypeHandicaps");
        Page<TypeHandicapDTO> page = typeHandicapService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /type-handicaps/:id} : get the "id" typeHandicap.
     *
     * @param id the id of the typeHandicapDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the typeHandicapDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<TypeHandicapDTO> getTypeHandicap(@PathVariable("id") Long id) {
        log.debug("REST request to get TypeHandicap : {}", id);
        Optional<TypeHandicapDTO> typeHandicapDTO = typeHandicapService.findOne(id);
        return ResponseUtil.wrapOrNotFound(typeHandicapDTO);
    }

    /**
     * {@code DELETE  /type-handicaps/:id} : delete the "id" typeHandicap.
     *
     * @param id the id of the typeHandicapDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTypeHandicap(@PathVariable("id") Long id) {
        log.debug("REST request to delete TypeHandicap : {}", id);
        typeHandicapService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
