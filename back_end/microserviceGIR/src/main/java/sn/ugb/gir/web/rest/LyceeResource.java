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
import sn.ugb.gir.repository.LyceeRepository;
import sn.ugb.gir.service.LyceeService;
import sn.ugb.gir.service.dto.LyceeDTO;
import sn.ugb.gir.web.rest.errors.BadRequestAlertException;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link sn.ugb.gir.domain.Lycee}.
 */
@RestController
@RequestMapping("/api/lycees")
public class LyceeResource {

    private final Logger log = LoggerFactory.getLogger(LyceeResource.class);

    private static final String ENTITY_NAME = "microserviceGirLycee";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final LyceeService lyceeService;

    private final LyceeRepository lyceeRepository;

    public LyceeResource(LyceeService lyceeService, LyceeRepository lyceeRepository) {
        this.lyceeService = lyceeService;
        this.lyceeRepository = lyceeRepository;
    }

    /**
     * {@code POST  /lycees} : Create a new lycee.
     *
     * @param lyceeDTO the lyceeDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new lyceeDTO, or with status {@code 400 (Bad Request)} if the lycee has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<LyceeDTO> createLycee(@Valid @RequestBody LyceeDTO lyceeDTO) throws URISyntaxException {
        log.debug("REST request to save Lycee : {}", lyceeDTO);

        LyceeDTO result = lyceeService.save(lyceeDTO);
        return ResponseEntity
            .created(new URI("/api/lycees/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }



    /**
     * {@code PUT  /lycees/:id} : Updates an existing lycee.
     *
     * @param id the id of the lyceeDTO to save.
     * @param lyceeDTO the lyceeDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated lyceeDTO,
     * or with status {@code 400 (Bad Request)} if the lyceeDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the lyceeDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<LyceeDTO> updateLycee(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody LyceeDTO lyceeDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Lycee : {}, {}", id, lyceeDTO);

        LyceeDTO result = lyceeService.update(lyceeDTO,id);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, lyceeDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /lycees/:id} : Partial updates given fields of an existing lycee, field will ignore if it is null
     *
     * @param id the id of the lyceeDTO to save.
     * @param lyceeDTO the lyceeDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated lyceeDTO,
     * or with status {@code 400 (Bad Request)} if the lyceeDTO is not valid,
     * or with status {@code 404 (Not Found)} if the lyceeDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the lyceeDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<LyceeDTO> partialUpdateLycee(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody LyceeDTO lyceeDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Lycee partially : {}, {}", id, lyceeDTO);

        Optional<LyceeDTO> result = lyceeService.partialUpdate(lyceeDTO,id);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, lyceeDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /lycees} : get all the lycees.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of lycees in body.
     */
    @GetMapping("")
    public ResponseEntity<List<LyceeDTO>> getAllLycees(@org.springdoc.core.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get a page of Lycees");
        Page<LyceeDTO> page = lyceeService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /lycees/:id} : get the "id" lycee.
     *
     * @param id the id of the lyceeDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the lyceeDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<LyceeDTO> getLycee(@PathVariable("id") Long id) {
        log.debug("REST request to get Lycee : {}", id);
        Optional<LyceeDTO> lyceeDTO = lyceeService.findOne(id);
        return ResponseUtil.wrapOrNotFound(lyceeDTO);
    }

    /**
     * {@code DELETE  /lycees/:id} : delete the "id" lycee.
     *
     * @param id the id of the lyceeDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteLycee(@PathVariable("id") Long id) {
        log.debug("REST request to delete Lycee : {}", id);
        lyceeService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code GET  /lycees} : get all the lycees.
     *
     * @param pageable the pagination information.
     *
     * @param id the id of region
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of lycees in body.
     */
    @GetMapping("/Regions/{id}")
    public ResponseEntity<List<LyceeDTO>> getAllLyceesByRegion(@org.springdoc.core.annotations.ParameterObject Pageable pageable, @PathVariable("id") Long id) {
        log.debug("REST request to get a page of Lycees having an id of region");
        Page<LyceeDTO> page = lyceeService.findAllByRegionId(pageable,id);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }
}
