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
import sn.ugb.gir.repository.TypeFraisRepository;
import sn.ugb.gir.service.TypeFraisService;
import sn.ugb.gir.service.dto.TypeFraisDTO;
import sn.ugb.gir.web.rest.errors.BadRequestAlertException;
import sn.ugb.gir.web.rest.errors.ElasticsearchExceptionMapper;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link sn.ugb.gir.domain.TypeFrais}.
 */
@RestController
@RequestMapping("/api/type-frais")
public class TypeFraisResource {

    private final Logger log = LoggerFactory.getLogger(TypeFraisResource.class);

    private static final String ENTITY_NAME = "microserviceGirTypeFrais";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final TypeFraisService typeFraisService;

    private final TypeFraisRepository typeFraisRepository;

    public TypeFraisResource(TypeFraisService typeFraisService, TypeFraisRepository typeFraisRepository) {
        this.typeFraisService = typeFraisService;
        this.typeFraisRepository = typeFraisRepository;
    }

    /**
     * {@code POST  /type-frais} : Create a new typeFrais.
     *
     * @param typeFraisDTO the typeFraisDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new typeFraisDTO, or with status {@code 400 (Bad Request)} if the typeFrais has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<TypeFraisDTO> createTypeFrais(@Valid @RequestBody TypeFraisDTO typeFraisDTO) throws URISyntaxException {
        log.debug("REST request to save TypeFrais : {}", typeFraisDTO);
        if (typeFraisDTO.getId() != null) {
            throw new BadRequestAlertException("A new typeFrais cannot already have an ID", ENTITY_NAME, "idexists");
        }
        TypeFraisDTO result = typeFraisService.save(typeFraisDTO);
        return ResponseEntity
            .created(new URI("/api/type-frais/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /type-frais/:id} : Updates an existing typeFrais.
     *
     * @param id the id of the typeFraisDTO to save.
     * @param typeFraisDTO the typeFraisDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated typeFraisDTO,
     * or with status {@code 400 (Bad Request)} if the typeFraisDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the typeFraisDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<TypeFraisDTO> updateTypeFrais(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody TypeFraisDTO typeFraisDTO
    ) throws URISyntaxException {
        log.debug("REST request to update TypeFrais : {}, {}", id, typeFraisDTO);
        if (typeFraisDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, typeFraisDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!typeFraisRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        TypeFraisDTO result = typeFraisService.update(typeFraisDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, typeFraisDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /type-frais/:id} : Partial updates given fields of an existing typeFrais, field will ignore if it is null
     *
     * @param id the id of the typeFraisDTO to save.
     * @param typeFraisDTO the typeFraisDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated typeFraisDTO,
     * or with status {@code 400 (Bad Request)} if the typeFraisDTO is not valid,
     * or with status {@code 404 (Not Found)} if the typeFraisDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the typeFraisDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<TypeFraisDTO> partialUpdateTypeFrais(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody TypeFraisDTO typeFraisDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update TypeFrais partially : {}, {}", id, typeFraisDTO);
        if (typeFraisDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, typeFraisDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!typeFraisRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<TypeFraisDTO> result = typeFraisService.partialUpdate(typeFraisDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, typeFraisDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /type-frais} : get all the typeFrais.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of typeFrais in body.
     */
    @GetMapping("")
    public ResponseEntity<List<TypeFraisDTO>> getAllTypeFrais(@org.springdoc.core.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get a page of TypeFrais");
        Page<TypeFraisDTO> page = typeFraisService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /type-frais/:id} : get the "id" typeFrais.
     *
     * @param id the id of the typeFraisDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the typeFraisDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<TypeFraisDTO> getTypeFrais(@PathVariable("id") Long id) {
        log.debug("REST request to get TypeFrais : {}", id);
        Optional<TypeFraisDTO> typeFraisDTO = typeFraisService.findOne(id);
        return ResponseUtil.wrapOrNotFound(typeFraisDTO);
    }

    /**
     * {@code DELETE  /type-frais/:id} : delete the "id" typeFrais.
     *
     * @param id the id of the typeFraisDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTypeFrais(@PathVariable("id") Long id) {
        log.debug("REST request to delete TypeFrais : {}", id);
        typeFraisService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code SEARCH  /type-frais/_search?query=:query} : search for the typeFrais corresponding
     * to the query.
     *
     * @param query the query of the typeFrais search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search")
    public ResponseEntity<List<TypeFraisDTO>> searchTypeFrais(
        @RequestParam("query") String query,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to search for a page of TypeFrais for query {}", query);
        try {
            Page<TypeFraisDTO> page = typeFraisService.search(query, pageable);
            HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
            return ResponseEntity.ok().headers(headers).body(page.getContent());
        } catch (RuntimeException e) {
            throw ElasticsearchExceptionMapper.mapException(e);
        }
    }
}
