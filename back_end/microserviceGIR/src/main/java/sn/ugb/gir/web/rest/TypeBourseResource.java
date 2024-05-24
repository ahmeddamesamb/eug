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
import sn.ugb.gir.repository.TypeBourseRepository;
import sn.ugb.gir.service.TypeBourseService;
import sn.ugb.gir.service.dto.TypeBourseDTO;
import sn.ugb.gir.web.rest.errors.BadRequestAlertException;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link sn.ugb.gir.domain.TypeBourse}.
 */
@RestController
@RequestMapping("/api/type-bourses")
public class TypeBourseResource {

    private final Logger log = LoggerFactory.getLogger(TypeBourseResource.class);

    private static final String ENTITY_NAME = "microserviceGirTypeBourse";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final TypeBourseService typeBourseService;

    private final TypeBourseRepository typeBourseRepository;

    public TypeBourseResource(TypeBourseService typeBourseService, TypeBourseRepository typeBourseRepository) {
        this.typeBourseService = typeBourseService;
        this.typeBourseRepository = typeBourseRepository;
    }

    /**
     * {@code POST  /type-bourses} : Create a new typeBourse.
     *
     * @param typeBourseDTO the typeBourseDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new typeBourseDTO, or with status {@code 400 (Bad Request)} if the typeBourse has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<TypeBourseDTO> createTypeBourse(@Valid @RequestBody TypeBourseDTO typeBourseDTO) throws URISyntaxException {
        log.debug("REST request to save TypeBourse : {}", typeBourseDTO);
        if (typeBourseDTO.getId() != null) {
            throw new BadRequestAlertException("A new typeBourse cannot already have an ID", ENTITY_NAME, "idexists");
        }
        TypeBourseDTO result = typeBourseService.save(typeBourseDTO);
        return ResponseEntity
            .created(new URI("/api/type-bourses/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /type-bourses/:id} : Updates an existing typeBourse.
     *
     * @param id the id of the typeBourseDTO to save.
     * @param typeBourseDTO the typeBourseDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated typeBourseDTO,
     * or with status {@code 400 (Bad Request)} if the typeBourseDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the typeBourseDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<TypeBourseDTO> updateTypeBourse(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody TypeBourseDTO typeBourseDTO
    ) throws URISyntaxException {
        log.debug("REST request to update TypeBourse : {}, {}", id, typeBourseDTO);
        if (typeBourseDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, typeBourseDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!typeBourseRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        TypeBourseDTO result = typeBourseService.update(typeBourseDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, typeBourseDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /type-bourses/:id} : Partial updates given fields of an existing typeBourse, field will ignore if it is null
     *
     * @param id the id of the typeBourseDTO to save.
     * @param typeBourseDTO the typeBourseDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated typeBourseDTO,
     * or with status {@code 400 (Bad Request)} if the typeBourseDTO is not valid,
     * or with status {@code 404 (Not Found)} if the typeBourseDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the typeBourseDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<TypeBourseDTO> partialUpdateTypeBourse(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody TypeBourseDTO typeBourseDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update TypeBourse partially : {}, {}", id, typeBourseDTO);
        if (typeBourseDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, typeBourseDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!typeBourseRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<TypeBourseDTO> result = typeBourseService.partialUpdate(typeBourseDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, typeBourseDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /type-bourses} : get all the typeBourses.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of typeBourses in body.
     */
    @GetMapping("")
    public ResponseEntity<List<TypeBourseDTO>> getAllTypeBourses(@org.springdoc.core.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get a page of TypeBourses");
        Page<TypeBourseDTO> page = typeBourseService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /type-bourses/:id} : get the "id" typeBourse.
     *
     * @param id the id of the typeBourseDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the typeBourseDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<TypeBourseDTO> getTypeBourse(@PathVariable("id") Long id) {
        log.debug("REST request to get TypeBourse : {}", id);
        Optional<TypeBourseDTO> typeBourseDTO = typeBourseService.findOne(id);
        return ResponseUtil.wrapOrNotFound(typeBourseDTO);
    }

    /**
     * {@code DELETE  /type-bourses/:id} : delete the "id" typeBourse.
     *
     * @param id the id of the typeBourseDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTypeBourse(@PathVariable("id") Long id) {
        log.debug("REST request to delete TypeBourse : {}", id);
        typeBourseService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
