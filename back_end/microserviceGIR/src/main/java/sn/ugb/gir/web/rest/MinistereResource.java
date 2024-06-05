package sn.ugb.gir.web.rest;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.net.URI;
import java.net.URISyntaxException;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import sn.ugb.gir.repository.MinistereRepository;
import sn.ugb.gir.service.MinistereService;
import sn.ugb.gir.service.dto.MinistereDTO;
import sn.ugb.gir.web.rest.errors.BadRequestAlertException;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link sn.ugb.gir.domain.Ministere}.
 */
@RestController
@RequestMapping("/api/ministeres")
public class MinistereResource {

    private final Logger log = LoggerFactory.getLogger(MinistereResource.class);

    private static final String ENTITY_NAME = "microserviceGirMinistere";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final MinistereService ministereService;

    private final MinistereRepository ministereRepository;

    public MinistereResource(MinistereService ministereService, MinistereRepository ministereRepository) {
        this.ministereService = ministereService;
        this.ministereRepository = ministereRepository;
    }

    /**
     * {@code POST  /ministeres} : Create a new ministere.
     *
     * @param ministereDTO the ministereDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new ministereDTO, or with status {@code 400 (Bad Request)} if the ministere has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<MinistereDTO> createMinistere(@Valid @RequestBody MinistereDTO ministereDTO) throws URISyntaxException {
        MinistereDTO result = ministereService.save(ministereDTO);
        return ResponseEntity
            .created(new URI("/api/ministeres/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /ministeres/:id} : Updates an existing ministere.
     *
     * @param id the id of the ministereDTO to save.
     * @param ministereDTO the ministereDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated ministereDTO,
     * or with status {@code 400 (Bad Request)} if the ministereDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the ministereDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<MinistereDTO> updateMinistere(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody MinistereDTO ministereDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Ministere : {}, {}", id, ministereDTO);
        if (ministereDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, ministereDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!ministereRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }
        MinistereDTO result = ministereService.update(ministereDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, ministereDTO.getId().toString()))
            .body(result);
    }


    /**
     * {@code PATCH  /ministeres/:id} : Partial updates given fields of an existing ministere, field will ignore if it is null
     *
     * @param id the id of the ministereDTO to save.
     * @param ministereDTO the ministereDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated ministereDTO,
     * or with status {@code 400 (Bad Request)} if the ministereDTO is not valid,
     * or with status {@code 404 (Not Found)} if the ministereDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the ministereDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<MinistereDTO> partialUpdateMinistere(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody MinistereDTO ministereDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Ministere partially : {}, {}", id, ministereDTO);
        if (ministereDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, ministereDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!ministereRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<MinistereDTO> result = ministereService.partialUpdate(ministereDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, ministereDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /ministeres} : get all the ministeres.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of ministeres in body.
     */
    @GetMapping("")
    public ResponseEntity<List<MinistereDTO>> getAllMinisteres(@org.springdoc.core.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get a page of Ministeres");
        Page<MinistereDTO> page = ministereService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /ministeres/:id} : get the "id" ministere.
     *
     * @param id the id of the ministereDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the ministereDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<MinistereDTO> getMinistere(@PathVariable("id") Long id) {
        log.debug("REST request to get Ministere : {}", id);
        Optional<MinistereDTO> ministereDTO = ministereService.findOne(id);
        return ResponseUtil.wrapOrNotFound(ministereDTO);
    }

    /**
     * {@code DELETE  /ministeres/:id} : delete the "id" ministere.
     *
     * @param id the id of the ministereDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMinistere(@PathVariable("id") Long id) {
        log.debug("REST request to delete Ministere : {}", id);
        ministereService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    @GetMapping("/current")
    public ResponseEntity<MinistereDTO> getInfosCurrentMinistere() {
        log.debug("REST request to get current Ministere");
        Optional<MinistereDTO> ministereDTO = ministereService.findCurrent();
        return ResponseUtil.wrapOrNotFound(ministereDTO);
    }

    @GetMapping("/periode")
    public ResponseEntity<List<MinistereDTO>> getInfosMinistreByPeriode(
        @RequestParam(value = "startDate", required = true) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
        @RequestParam(value = "endDate", required = true) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get Ministere by period: {} - {}", startDate, endDate);
        Page<MinistereDTO> page = ministereService.findByPeriode(startDate, endDate, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }
}
