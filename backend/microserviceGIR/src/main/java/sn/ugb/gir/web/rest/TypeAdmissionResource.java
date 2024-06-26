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
import sn.ugb.gir.repository.TypeAdmissionRepository;
import sn.ugb.gir.service.TypeAdmissionService;
import sn.ugb.gir.service.dto.TypeAdmissionDTO;
import sn.ugb.gir.web.rest.errors.BadRequestAlertException;
import sn.ugb.gir.web.rest.errors.ElasticsearchExceptionMapper;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link sn.ugb.gir.domain.TypeAdmission}.
 */
@RestController
@RequestMapping("/api/type-admissions")
public class TypeAdmissionResource {

    private final Logger log = LoggerFactory.getLogger(TypeAdmissionResource.class);

    private static final String ENTITY_NAME = "microserviceGirTypeAdmission";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final TypeAdmissionService typeAdmissionService;

    private final TypeAdmissionRepository typeAdmissionRepository;

    public TypeAdmissionResource(TypeAdmissionService typeAdmissionService, TypeAdmissionRepository typeAdmissionRepository) {
        this.typeAdmissionService = typeAdmissionService;
        this.typeAdmissionRepository = typeAdmissionRepository;
    }

    /**
     * {@code POST  /type-admissions} : Create a new typeAdmission.
     *
     * @param typeAdmissionDTO the typeAdmissionDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new typeAdmissionDTO, or with status {@code 400 (Bad Request)} if the typeAdmission has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<TypeAdmissionDTO> createTypeAdmission(@Valid @RequestBody TypeAdmissionDTO typeAdmissionDTO)
        throws URISyntaxException {
        log.debug("REST request to save TypeAdmission : {}", typeAdmissionDTO);
        if (typeAdmissionDTO.getId() != null) {
            throw new BadRequestAlertException("A new typeAdmission cannot already have an ID", ENTITY_NAME, "idexists");
        }
        TypeAdmissionDTO result = typeAdmissionService.save(typeAdmissionDTO);
        return ResponseEntity
            .created(new URI("/api/type-admissions/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /type-admissions/:id} : Updates an existing typeAdmission.
     *
     * @param id the id of the typeAdmissionDTO to save.
     * @param typeAdmissionDTO the typeAdmissionDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated typeAdmissionDTO,
     * or with status {@code 400 (Bad Request)} if the typeAdmissionDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the typeAdmissionDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<TypeAdmissionDTO> updateTypeAdmission(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody TypeAdmissionDTO typeAdmissionDTO
    ) throws URISyntaxException {
        log.debug("REST request to update TypeAdmission : {}, {}", id, typeAdmissionDTO);
        if (typeAdmissionDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, typeAdmissionDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!typeAdmissionRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        TypeAdmissionDTO result = typeAdmissionService.update(typeAdmissionDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, typeAdmissionDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /type-admissions/:id} : Partial updates given fields of an existing typeAdmission, field will ignore if it is null
     *
     * @param id the id of the typeAdmissionDTO to save.
     * @param typeAdmissionDTO the typeAdmissionDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated typeAdmissionDTO,
     * or with status {@code 400 (Bad Request)} if the typeAdmissionDTO is not valid,
     * or with status {@code 404 (Not Found)} if the typeAdmissionDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the typeAdmissionDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<TypeAdmissionDTO> partialUpdateTypeAdmission(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody TypeAdmissionDTO typeAdmissionDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update TypeAdmission partially : {}, {}", id, typeAdmissionDTO);
        if (typeAdmissionDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, typeAdmissionDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!typeAdmissionRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<TypeAdmissionDTO> result = typeAdmissionService.partialUpdate(typeAdmissionDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, typeAdmissionDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /type-admissions} : get all the typeAdmissions.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of typeAdmissions in body.
     */
    @GetMapping("")
    public ResponseEntity<List<TypeAdmissionDTO>> getAllTypeAdmissions(@org.springdoc.core.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get a page of TypeAdmissions");
        Page<TypeAdmissionDTO> page = typeAdmissionService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /type-admissions/:id} : get the "id" typeAdmission.
     *
     * @param id the id of the typeAdmissionDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the typeAdmissionDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<TypeAdmissionDTO> getTypeAdmission(@PathVariable("id") Long id) {
        log.debug("REST request to get TypeAdmission : {}", id);
        Optional<TypeAdmissionDTO> typeAdmissionDTO = typeAdmissionService.findOne(id);
        return ResponseUtil.wrapOrNotFound(typeAdmissionDTO);
    }

    /**
     * {@code DELETE  /type-admissions/:id} : delete the "id" typeAdmission.
     *
     * @param id the id of the typeAdmissionDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTypeAdmission(@PathVariable("id") Long id) {
        log.debug("REST request to delete TypeAdmission : {}", id);
        typeAdmissionService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code SEARCH  /type-admissions/_search?query=:query} : search for the typeAdmission corresponding
     * to the query.
     *
     * @param query the query of the typeAdmission search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search")
    public ResponseEntity<List<TypeAdmissionDTO>> searchTypeAdmissions(
        @RequestParam("query") String query,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to search for a page of TypeAdmissions for query {}", query);
        try {
            Page<TypeAdmissionDTO> page = typeAdmissionService.search(query, pageable);
            HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
            return ResponseEntity.ok().headers(headers).body(page.getContent());
        } catch (RuntimeException e) {
            throw ElasticsearchExceptionMapper.mapException(e);
        }
    }
}
