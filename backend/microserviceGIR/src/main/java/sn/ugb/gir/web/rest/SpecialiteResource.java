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
import sn.ugb.gir.repository.SpecialiteRepository;
import sn.ugb.gir.service.SpecialiteService;
import sn.ugb.gir.service.dto.SpecialiteDTO;
import sn.ugb.gir.web.rest.errors.BadRequestAlertException;
import sn.ugb.gir.web.rest.errors.ElasticsearchExceptionMapper;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link sn.ugb.gir.domain.Specialite}.
 */
@RestController
@RequestMapping("/api/specialites")
public class SpecialiteResource {

    private final Logger log = LoggerFactory.getLogger(SpecialiteResource.class);

    private static final String ENTITY_NAME = "microserviceGirSpecialite";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final SpecialiteService specialiteService;

    private final SpecialiteRepository specialiteRepository;

    public SpecialiteResource(SpecialiteService specialiteService, SpecialiteRepository specialiteRepository) {
        this.specialiteService = specialiteService;
        this.specialiteRepository = specialiteRepository;
    }

    /**
     * {@code POST  /specialites} : Create a new specialite.
     *
     * @param specialiteDTO the specialiteDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new specialiteDTO, or with status {@code 400 (Bad Request)} if the specialite has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<SpecialiteDTO> createSpecialite(@Valid @RequestBody SpecialiteDTO specialiteDTO) throws URISyntaxException {
        log.debug("REST request to save Specialite : {}", specialiteDTO);
        if (specialiteDTO.getId() != null) {
            throw new BadRequestAlertException("A new specialite cannot already have an ID", ENTITY_NAME, "idexists");
        }
        SpecialiteDTO result = specialiteService.save(specialiteDTO);
        return ResponseEntity
            .created(new URI("/api/specialites/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /specialites/:id} : Updates an existing specialite.
     *
     * @param id the id of the specialiteDTO to save.
     * @param specialiteDTO the specialiteDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated specialiteDTO,
     * or with status {@code 400 (Bad Request)} if the specialiteDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the specialiteDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<SpecialiteDTO> updateSpecialite(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody SpecialiteDTO specialiteDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Specialite : {}, {}", id, specialiteDTO);
        if (specialiteDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, specialiteDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!specialiteRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        SpecialiteDTO result = specialiteService.update(specialiteDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, specialiteDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /specialites/:id} : Partial updates given fields of an existing specialite, field will ignore if it is null
     *
     * @param id the id of the specialiteDTO to save.
     * @param specialiteDTO the specialiteDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated specialiteDTO,
     * or with status {@code 400 (Bad Request)} if the specialiteDTO is not valid,
     * or with status {@code 404 (Not Found)} if the specialiteDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the specialiteDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<SpecialiteDTO> partialUpdateSpecialite(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody SpecialiteDTO specialiteDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Specialite partially : {}, {}", id, specialiteDTO);
        if (specialiteDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, specialiteDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!specialiteRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<SpecialiteDTO> result = specialiteService.partialUpdate(specialiteDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, specialiteDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /specialites} : get all the specialites.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of specialites in body.
     */
    @GetMapping("")
    public ResponseEntity<List<SpecialiteDTO>> getAllSpecialites(@org.springdoc.core.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get a page of Specialites");
        Page<SpecialiteDTO> page = specialiteService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /specialites/:id} : get the "id" specialite.
     *
     * @param id the id of the specialiteDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the specialiteDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<SpecialiteDTO> getSpecialite(@PathVariable("id") Long id) {
        log.debug("REST request to get Specialite : {}", id);
        Optional<SpecialiteDTO> specialiteDTO = specialiteService.findOne(id);
        return ResponseUtil.wrapOrNotFound(specialiteDTO);
    }

    /**
     * {@code DELETE  /specialites/:id} : delete the "id" specialite.
     *
     * @param id the id of the specialiteDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSpecialite(@PathVariable("id") Long id) {
        log.debug("REST request to delete Specialite : {}", id);
        specialiteService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code SEARCH  /specialites/_search?query=:query} : search for the specialite corresponding
     * to the query.
     *
     * @param query the query of the specialite search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search")
    public ResponseEntity<List<SpecialiteDTO>> searchSpecialites(
        @RequestParam("query") String query,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to search for a page of Specialites for query {}", query);
        try {
            Page<SpecialiteDTO> page = specialiteService.search(query, pageable);
            HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
            return ResponseEntity.ok().headers(headers).body(page.getContent());
        } catch (RuntimeException e) {
            throw ElasticsearchExceptionMapper.mapException(e);
        }
    }

    @GetMapping("/mentions/{mentionId}")
    public ResponseEntity<List<SpecialiteDTO>> getAllSpecialiteByMention(
        @PathVariable Long mentionId,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get a page of Specialites by Mention ID : {}", mentionId);
        Page<SpecialiteDTO> page = specialiteService.getAllSpecialiteByMention(mentionId, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    @GetMapping("/domaines/{domaineId}")
    public ResponseEntity<List<SpecialiteDTO>> getAllSpecialiteByDomaine(
        @PathVariable Long domaineId,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get a page of Specialites by Domaine" +
            " ID : {}", domaineId);
        Page<SpecialiteDTO> page = specialiteService.getAllSpecialiteByDomaine(domaineId, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    @GetMapping("/ufrs/{ufrId}")
    public ResponseEntity<List<SpecialiteDTO>> getAllSpecialiteByUFR(
        @PathVariable Long ufrId,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get a page of Specialites by UFR ID : {}", ufrId);
        Page<SpecialiteDTO> page = specialiteService.getAllSpecialiteByUfr(ufrId, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    @GetMapping("/universites/{universiteId}")
    public ResponseEntity<List<SpecialiteDTO>> getAllSpecialiteByUniversite(
        @PathVariable Long universiteId,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get a page of Specialites by Universite ID : {}", universiteId);
        Page<SpecialiteDTO> page = specialiteService.getAllSpecialiteByUniversite(universiteId, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    @GetMapping("/ministeres/{ministereId}")
    public ResponseEntity<List<SpecialiteDTO>> getAllSpecialiteByMinistere(
        @PathVariable Long ministereId,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get a page of Specialites by Ministere ID : {}", ministereId);
        Page<SpecialiteDTO> page = specialiteService.getAllSpecialiteByMinistere(ministereId, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }
}
