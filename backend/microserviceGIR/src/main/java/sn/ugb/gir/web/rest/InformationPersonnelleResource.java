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
import sn.ugb.gir.repository.InformationPersonnelleRepository;
import sn.ugb.gir.service.InformationPersonnelleService;
import sn.ugb.gir.service.dto.InformationPersonnelleDTO;
import sn.ugb.gir.web.rest.errors.BadRequestAlertException;
import sn.ugb.gir.web.rest.errors.ElasticsearchExceptionMapper;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link sn.ugb.gir.domain.InformationPersonnelle}.
 */
@RestController
@RequestMapping("/api/information-personnelles")
public class InformationPersonnelleResource {

    private final Logger log = LoggerFactory.getLogger(InformationPersonnelleResource.class);

    private static final String ENTITY_NAME = "microserviceGirInformationPersonnelle";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final InformationPersonnelleService informationPersonnelleService;

    private final InformationPersonnelleRepository informationPersonnelleRepository;

    public InformationPersonnelleResource(
        InformationPersonnelleService informationPersonnelleService,
        InformationPersonnelleRepository informationPersonnelleRepository
    ) {
        this.informationPersonnelleService = informationPersonnelleService;
        this.informationPersonnelleRepository = informationPersonnelleRepository;
    }

    /**
     * {@code POST  /information-personnelles} : Create a new informationPersonnelle.
     *
     * @param informationPersonnelleDTO the informationPersonnelleDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new informationPersonnelleDTO, or with status {@code 400 (Bad Request)} if the informationPersonnelle has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<InformationPersonnelleDTO> createInformationPersonnelle(
        @Valid @RequestBody InformationPersonnelleDTO informationPersonnelleDTO
    ) throws URISyntaxException {
        log.debug("REST request to save InformationPersonnelle : {}", informationPersonnelleDTO);
        if (informationPersonnelleDTO.getId() != null) {
            throw new BadRequestAlertException("A new informationPersonnelle cannot already have an ID", ENTITY_NAME, "idexists");
        }
        InformationPersonnelleDTO result = informationPersonnelleService.save(informationPersonnelleDTO);
        return ResponseEntity
            .created(new URI("/api/information-personnelles/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /information-personnelles/:id} : Updates an existing informationPersonnelle.
     *
     * @param id the id of the informationPersonnelleDTO to save.
     * @param informationPersonnelleDTO the informationPersonnelleDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated informationPersonnelleDTO,
     * or with status {@code 400 (Bad Request)} if the informationPersonnelleDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the informationPersonnelleDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<InformationPersonnelleDTO> updateInformationPersonnelle(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody InformationPersonnelleDTO informationPersonnelleDTO
    ) throws URISyntaxException {
        log.debug("REST request to update InformationPersonnelle : {}, {}", id, informationPersonnelleDTO);
        if (informationPersonnelleDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, informationPersonnelleDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!informationPersonnelleRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        InformationPersonnelleDTO result = informationPersonnelleService.update(informationPersonnelleDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, informationPersonnelleDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /information-personnelles/:id} : Partial updates given fields of an existing informationPersonnelle, field will ignore if it is null
     *
     * @param id the id of the informationPersonnelleDTO to save.
     * @param informationPersonnelleDTO the informationPersonnelleDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated informationPersonnelleDTO,
     * or with status {@code 400 (Bad Request)} if the informationPersonnelleDTO is not valid,
     * or with status {@code 404 (Not Found)} if the informationPersonnelleDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the informationPersonnelleDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<InformationPersonnelleDTO> partialUpdateInformationPersonnelle(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody InformationPersonnelleDTO informationPersonnelleDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update InformationPersonnelle partially : {}, {}", id, informationPersonnelleDTO);
        if (informationPersonnelleDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, informationPersonnelleDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!informationPersonnelleRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<InformationPersonnelleDTO> result = informationPersonnelleService.partialUpdate(informationPersonnelleDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, informationPersonnelleDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /information-personnelles} : get all the informationPersonnelles.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of informationPersonnelles in body.
     */
    @GetMapping("")
    public ResponseEntity<List<InformationPersonnelleDTO>> getAllInformationPersonnelles(
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get a page of InformationPersonnelles");
        Page<InformationPersonnelleDTO> page = informationPersonnelleService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /information-personnelles/:id} : get the "id" informationPersonnelle.
     *
     * @param id the id of the informationPersonnelleDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the informationPersonnelleDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<InformationPersonnelleDTO> getInformationPersonnelle(@PathVariable("id") Long id) {
        log.debug("REST request to get InformationPersonnelle : {}", id);
        Optional<InformationPersonnelleDTO> informationPersonnelleDTO = informationPersonnelleService.findOne(id);
        return ResponseUtil.wrapOrNotFound(informationPersonnelleDTO);
    }

    /**
     * {@code GET  /information-personnelles/:codeEtudiant} : get the "codeEtudiant" informationPersonnelle.
     *
     * @param codeEtudiant the codeEtudiant of the informationPersonnelleDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the informationPersonnelleDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("etudiant/{codeEtudiant}")
    public ResponseEntity<InformationPersonnelleDTO> getInformationPersonnelle(@PathVariable("codeEtudiant") String codeEtudiant) {
        log.debug("REST request to get InformationPersonnelle : {}", codeEtudiant);
        Optional<InformationPersonnelleDTO> informationPersonnelleDTO = informationPersonnelleService.findOneByCodeEtudiant(codeEtudiant);
        return ResponseUtil.wrapOrNotFound(informationPersonnelleDTO);
    }

    /**
     * {@code DELETE  /information-personnelles/:id} : delete the "id" informationPersonnelle.
     *
     * @param id the id of the informationPersonnelleDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteInformationPersonnelle(@PathVariable("id") Long id) {
        log.debug("REST request to delete InformationPersonnelle : {}", id);
        informationPersonnelleService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code SEARCH  /information-personnelles/_search?query=:query} : search for the informationPersonnelle corresponding
     * to the query.
     *
     * @param query the query of the informationPersonnelle search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search")
    public ResponseEntity<List<InformationPersonnelleDTO>> searchInformationPersonnelles(
        @RequestParam("query") String query,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to search for a page of InformationPersonnelles for query {}", query);
        try {
            Page<InformationPersonnelleDTO> page = informationPersonnelleService.search(query, pageable);
            HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
            return ResponseEntity.ok().headers(headers).body(page.getContent());
        } catch (RuntimeException e) {
            throw ElasticsearchExceptionMapper.mapException(e);
        }
    }
}
