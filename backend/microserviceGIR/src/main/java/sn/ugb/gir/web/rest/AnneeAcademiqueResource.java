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
import sn.ugb.gir.repository.AnneeAcademiqueRepository;
import sn.ugb.gir.service.AnneeAcademiqueService;
import sn.ugb.gir.service.dto.AnneeAcademiqueDTO;
import sn.ugb.gir.web.rest.errors.BadRequestAlertException;
import sn.ugb.gir.web.rest.errors.ElasticsearchExceptionMapper;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link sn.ugb.gir.domain.AnneeAcademique}.
 */
@RestController
@RequestMapping("/api/annee-academiques")
public class AnneeAcademiqueResource {

    private final Logger log = LoggerFactory.getLogger(AnneeAcademiqueResource.class);

    private static final String ENTITY_NAME = "microserviceGirAnneeAcademique";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final AnneeAcademiqueService anneeAcademiqueService;

    private final AnneeAcademiqueRepository anneeAcademiqueRepository;

    public AnneeAcademiqueResource(AnneeAcademiqueService anneeAcademiqueService, AnneeAcademiqueRepository anneeAcademiqueRepository) {
        this.anneeAcademiqueService = anneeAcademiqueService;
        this.anneeAcademiqueRepository = anneeAcademiqueRepository;
    }

    /**
     * {@code POST  /annee-academiques} : Create a new anneeAcademique.
     *
     * @param anneeAcademiqueDTO the anneeAcademiqueDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new anneeAcademiqueDTO, or with status {@code 400 (Bad Request)} if the anneeAcademique has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<AnneeAcademiqueDTO> createAnneeAcademique(@Valid @RequestBody AnneeAcademiqueDTO anneeAcademiqueDTO)
        throws URISyntaxException {
        log.debug("REST request to save AnneeAcademique : {}", anneeAcademiqueDTO);
        if (anneeAcademiqueDTO.getId() != null) {
            throw new BadRequestAlertException("A new anneeAcademique cannot already have an ID", ENTITY_NAME, "idexists");
        }
        AnneeAcademiqueDTO result = anneeAcademiqueService.save(anneeAcademiqueDTO);
        return ResponseEntity
            .created(new URI("/api/annee-academiques/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /annee-academiques/:id} : Updates an existing anneeAcademique.
     *
     * @param id the id of the anneeAcademiqueDTO to save.
     * @param anneeAcademiqueDTO the anneeAcademiqueDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated anneeAcademiqueDTO,
     * or with status {@code 400 (Bad Request)} if the anneeAcademiqueDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the anneeAcademiqueDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<AnneeAcademiqueDTO> updateAnneeAcademique(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody AnneeAcademiqueDTO anneeAcademiqueDTO
    ) throws URISyntaxException {
        log.debug("REST request to update AnneeAcademique : {}, {}", id, anneeAcademiqueDTO);
        if (anneeAcademiqueDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, anneeAcademiqueDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!anneeAcademiqueRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        AnneeAcademiqueDTO result = anneeAcademiqueService.update(anneeAcademiqueDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, anneeAcademiqueDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /annee-academiques/:id} : Partial updates given fields of an existing anneeAcademique, field will ignore if it is null
     *
     * @param id the id of the anneeAcademiqueDTO to save.
     * @param anneeAcademiqueDTO the anneeAcademiqueDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated anneeAcademiqueDTO,
     * or with status {@code 400 (Bad Request)} if the anneeAcademiqueDTO is not valid,
     * or with status {@code 404 (Not Found)} if the anneeAcademiqueDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the anneeAcademiqueDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<AnneeAcademiqueDTO> partialUpdateAnneeAcademique(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody AnneeAcademiqueDTO anneeAcademiqueDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update AnneeAcademique partially : {}, {}", id, anneeAcademiqueDTO);
        if (anneeAcademiqueDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, anneeAcademiqueDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!anneeAcademiqueRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<AnneeAcademiqueDTO> result = anneeAcademiqueService.partialUpdate(anneeAcademiqueDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, anneeAcademiqueDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /annee-academiques} : get all the anneeAcademiques.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of anneeAcademiques in body.
     */
    @GetMapping("")
    public ResponseEntity<List<AnneeAcademiqueDTO>> getAllAnneeAcademiques(
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get a page of AnneeAcademiques");
        Page<AnneeAcademiqueDTO> page = anneeAcademiqueService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /annee-academiques/:id} : get the "id" anneeAcademique.
     *
     * @param id the id of the anneeAcademiqueDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the anneeAcademiqueDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<AnneeAcademiqueDTO> getAnneeAcademique(@PathVariable("id") Long id) {
        log.debug("REST request to get AnneeAcademique : {}", id);
        Optional<AnneeAcademiqueDTO> anneeAcademiqueDTO = anneeAcademiqueService.findOne(id);
        return ResponseUtil.wrapOrNotFound(anneeAcademiqueDTO);
    }

    /**
     * {@code DELETE  /annee-academiques/:id} : delete the "id" anneeAcademique.
     *
     * @param id the id of the anneeAcademiqueDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAnneeAcademique(@PathVariable("id") Long id) {
        log.debug("REST request to delete AnneeAcademique : {}", id);
        anneeAcademiqueService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code SEARCH  /annee-academiques/_search?query=:query} : search for the anneeAcademique corresponding
     * to the query.
     *
     * @param query the query of the anneeAcademique search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search")
    public ResponseEntity<List<AnneeAcademiqueDTO>> searchAnneeAcademiques(
        @RequestParam("query") String query,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to search for a page of AnneeAcademiques for query {}", query);
        try {
            Page<AnneeAcademiqueDTO> page = anneeAcademiqueService.search(query, pageable);
            HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
            return ResponseEntity.ok().headers(headers).body(page.getContent());
        } catch (RuntimeException e) {
            throw ElasticsearchExceptionMapper.mapException(e);
        }
    }

    /**
     * {@code GET  /annee-academiques/current} : Get the "current" anneeAcademique.
     *
     * @return the ResponseEntity with status 200 (OK) and the current anneeAcademiqueDTO in body.
     */
    @GetMapping("/current")
    public ResponseEntity<AnneeAcademiqueDTO> getInfosCurrentAnneeAcademique() {
        log.debug("REST request to get AnneeAcademique ");
        Optional<AnneeAcademiqueDTO> anneeAcademiqueDTO = anneeAcademiqueService.getInfosCurrentAnneeAcademique();
        return ResponseUtil.wrapOrNotFound(anneeAcademiqueDTO);
    }
}
