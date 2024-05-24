package sn.ugb.gd.web.rest;

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
import sn.ugb.gd.repository.TypeRapportRepository;
import sn.ugb.gd.service.TypeRapportService;
import sn.ugb.gd.service.dto.TypeRapportDTO;
import sn.ugb.gd.web.rest.errors.BadRequestAlertException;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link sn.ugb.gd.domain.TypeRapport}.
 */
@RestController
@RequestMapping("/api/type-rapports")
public class TypeRapportResource {

    private final Logger log = LoggerFactory.getLogger(TypeRapportResource.class);

    private static final String ENTITY_NAME = "microserviceGdTypeRapport";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final TypeRapportService typeRapportService;

    private final TypeRapportRepository typeRapportRepository;

    public TypeRapportResource(TypeRapportService typeRapportService, TypeRapportRepository typeRapportRepository) {
        this.typeRapportService = typeRapportService;
        this.typeRapportRepository = typeRapportRepository;
    }

    /**
     * {@code POST  /type-rapports} : Create a new typeRapport.
     *
     * @param typeRapportDTO the typeRapportDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new typeRapportDTO, or with status {@code 400 (Bad Request)} if the typeRapport has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<TypeRapportDTO> createTypeRapport(@RequestBody TypeRapportDTO typeRapportDTO) throws URISyntaxException {
        log.debug("REST request to save TypeRapport : {}", typeRapportDTO);
        if (typeRapportDTO.getId() != null) {
            throw new BadRequestAlertException("A new typeRapport cannot already have an ID", ENTITY_NAME, "idexists");
        }
        TypeRapportDTO result = typeRapportService.save(typeRapportDTO);
        return ResponseEntity
            .created(new URI("/api/type-rapports/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /type-rapports/:id} : Updates an existing typeRapport.
     *
     * @param id the id of the typeRapportDTO to save.
     * @param typeRapportDTO the typeRapportDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated typeRapportDTO,
     * or with status {@code 400 (Bad Request)} if the typeRapportDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the typeRapportDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<TypeRapportDTO> updateTypeRapport(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody TypeRapportDTO typeRapportDTO
    ) throws URISyntaxException {
        log.debug("REST request to update TypeRapport : {}, {}", id, typeRapportDTO);
        if (typeRapportDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, typeRapportDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!typeRapportRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        TypeRapportDTO result = typeRapportService.update(typeRapportDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, typeRapportDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /type-rapports/:id} : Partial updates given fields of an existing typeRapport, field will ignore if it is null
     *
     * @param id the id of the typeRapportDTO to save.
     * @param typeRapportDTO the typeRapportDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated typeRapportDTO,
     * or with status {@code 400 (Bad Request)} if the typeRapportDTO is not valid,
     * or with status {@code 404 (Not Found)} if the typeRapportDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the typeRapportDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<TypeRapportDTO> partialUpdateTypeRapport(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody TypeRapportDTO typeRapportDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update TypeRapport partially : {}, {}", id, typeRapportDTO);
        if (typeRapportDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, typeRapportDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!typeRapportRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<TypeRapportDTO> result = typeRapportService.partialUpdate(typeRapportDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, typeRapportDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /type-rapports} : get all the typeRapports.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of typeRapports in body.
     */
    @GetMapping("")
    public ResponseEntity<List<TypeRapportDTO>> getAllTypeRapports(@org.springdoc.core.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get a page of TypeRapports");
        Page<TypeRapportDTO> page = typeRapportService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /type-rapports/:id} : get the "id" typeRapport.
     *
     * @param id the id of the typeRapportDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the typeRapportDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<TypeRapportDTO> getTypeRapport(@PathVariable("id") Long id) {
        log.debug("REST request to get TypeRapport : {}", id);
        Optional<TypeRapportDTO> typeRapportDTO = typeRapportService.findOne(id);
        return ResponseUtil.wrapOrNotFound(typeRapportDTO);
    }

    /**
     * {@code DELETE  /type-rapports/:id} : delete the "id" typeRapport.
     *
     * @param id the id of the typeRapportDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTypeRapport(@PathVariable("id") Long id) {
        log.debug("REST request to delete TypeRapport : {}", id);
        typeRapportService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
