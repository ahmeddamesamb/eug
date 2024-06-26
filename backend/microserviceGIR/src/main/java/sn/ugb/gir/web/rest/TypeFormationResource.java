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
import sn.ugb.gir.repository.TypeFormationRepository;
import sn.ugb.gir.service.TypeFormationService;
import sn.ugb.gir.service.dto.TypeFormationDTO;
import sn.ugb.gir.web.rest.errors.BadRequestAlertException;
import sn.ugb.gir.web.rest.errors.ElasticsearchExceptionMapper;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link sn.ugb.gir.domain.TypeFormation}.
 */
@RestController
@RequestMapping("/api/type-formations")
public class TypeFormationResource {

    private final Logger log = LoggerFactory.getLogger(TypeFormationResource.class);

    private static final String ENTITY_NAME = "microserviceGirTypeFormation";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final TypeFormationService typeFormationService;

    private final TypeFormationRepository typeFormationRepository;

    public TypeFormationResource(TypeFormationService typeFormationService, TypeFormationRepository typeFormationRepository) {
        this.typeFormationService = typeFormationService;
        this.typeFormationRepository = typeFormationRepository;
    }

    /**
     * {@code POST  /type-formations} : Create a new typeFormation.
     *
     * @param typeFormationDTO the typeFormationDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new typeFormationDTO, or with status {@code 400 (Bad Request)} if the typeFormation has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<TypeFormationDTO> createTypeFormation(@Valid @RequestBody TypeFormationDTO typeFormationDTO)
        throws URISyntaxException {
        log.debug("REST request to save TypeFormation : {}", typeFormationDTO);
        if (typeFormationDTO.getId() != null) {
            throw new BadRequestAlertException("A new typeFormation cannot already have an ID", ENTITY_NAME, "idexists");
        }
        TypeFormationDTO result = typeFormationService.save(typeFormationDTO);
        return ResponseEntity
            .created(new URI("/api/type-formations/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /type-formations/:id} : Updates an existing typeFormation.
     *
     * @param id the id of the typeFormationDTO to save.
     * @param typeFormationDTO the typeFormationDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated typeFormationDTO,
     * or with status {@code 400 (Bad Request)} if the typeFormationDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the typeFormationDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<TypeFormationDTO> updateTypeFormation(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody TypeFormationDTO typeFormationDTO
    ) throws URISyntaxException {
        log.debug("REST request to update TypeFormation : {}, {}", id, typeFormationDTO);
        if (typeFormationDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, typeFormationDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!typeFormationRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        TypeFormationDTO result = typeFormationService.update(typeFormationDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, typeFormationDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /type-formations/:id} : Partial updates given fields of an existing typeFormation, field will ignore if it is null
     *
     * @param id the id of the typeFormationDTO to save.
     * @param typeFormationDTO the typeFormationDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated typeFormationDTO,
     * or with status {@code 400 (Bad Request)} if the typeFormationDTO is not valid,
     * or with status {@code 404 (Not Found)} if the typeFormationDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the typeFormationDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<TypeFormationDTO> partialUpdateTypeFormation(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody TypeFormationDTO typeFormationDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update TypeFormation partially : {}, {}", id, typeFormationDTO);
        if (typeFormationDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, typeFormationDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!typeFormationRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<TypeFormationDTO> result = typeFormationService.partialUpdate(typeFormationDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, typeFormationDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /type-formations} : get all the typeFormations.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of typeFormations in body.
     */
    @GetMapping("")
    public ResponseEntity<List<TypeFormationDTO>> getAllTypeFormations(@org.springdoc.core.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get a page of TypeFormations");
        Page<TypeFormationDTO> page = typeFormationService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /type-formations/:id} : get the "id" typeFormation.
     *
     * @param id the id of the typeFormationDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the typeFormationDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<TypeFormationDTO> getTypeFormation(@PathVariable("id") Long id) {
        log.debug("REST request to get TypeFormation : {}", id);
        Optional<TypeFormationDTO> typeFormationDTO = typeFormationService.findOne(id);
        return ResponseUtil.wrapOrNotFound(typeFormationDTO);
    }

    /**
     * {@code DELETE  /type-formations/:id} : delete the "id" typeFormation.
     *
     * @param id the id of the typeFormationDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTypeFormation(@PathVariable("id") Long id) {
        log.debug("REST request to delete TypeFormation : {}", id);
        typeFormationService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code SEARCH  /type-formations/_search?query=:query} : search for the typeFormation corresponding
     * to the query.
     *
     * @param query the query of the typeFormation search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search")
    public ResponseEntity<List<TypeFormationDTO>> searchTypeFormations(
        @RequestParam("query") String query,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to search for a page of TypeFormations for query {}", query);
        try {
            Page<TypeFormationDTO> page = typeFormationService.search(query, pageable);
            HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
            return ResponseEntity.ok().headers(headers).body(page.getContent());
        } catch (RuntimeException e) {
            throw ElasticsearchExceptionMapper.mapException(e);
        }
    }
}
