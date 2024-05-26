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
import sn.ugb.gir.repository.UniversiteRepository;
import sn.ugb.gir.service.UniversiteService;
import sn.ugb.gir.service.dto.UniversiteDTO;
import sn.ugb.gir.web.rest.errors.BadRequestAlertException;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link sn.ugb.gir.domain.Universite}.
 */
@RestController
@RequestMapping("/api/universites")
public class UniversiteResource {

    private final Logger log = LoggerFactory.getLogger(UniversiteResource.class);

    private static final String ENTITY_NAME = "microserviceGirUniversite";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final UniversiteService universiteService;

    private final UniversiteRepository universiteRepository;

    public UniversiteResource(UniversiteService universiteService, UniversiteRepository universiteRepository) {
        this.universiteService = universiteService;
        this.universiteRepository = universiteRepository;
    }

    /**
     * {@code POST  /universites} : Create a new universite.
     *
     * @param universiteDTO the universiteDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new universiteDTO, or with status {@code 400 (Bad Request)} if the universite has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<UniversiteDTO> createUniversite(@Valid @RequestBody UniversiteDTO universiteDTO) throws URISyntaxException {
        log.debug("REST request to save Universite : {}", universiteDTO);
        if (universiteDTO.getId() != null) {
            throw new BadRequestAlertException("A new universite cannot already have an ID", ENTITY_NAME, "idexists");
        }
        if ( universiteRepository.existsUniversiteByNomUniversite( universiteDTO.getNomUniversite() ) ) {
            throw new BadRequestAlertException("Cet université exist deja !!!", ENTITY_NAME, "nomuniversiteexists");
        }
        UniversiteDTO result = universiteService.save(universiteDTO);
        return ResponseEntity
            .created(new URI("/api/universites/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /universites/:id} : Updates an existing universite.
     *
     * @param id the id of the universiteDTO to save.
     * @param universiteDTO the universiteDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated universiteDTO,
     * or with status {@code 400 (Bad Request)} if the universiteDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the universiteDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<UniversiteDTO> updateUniversite(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody UniversiteDTO universiteDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Universite : {}, {}", id, universiteDTO);
        if (universiteDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, universiteDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!universiteRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        UniversiteDTO result = universiteService.update(universiteDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, universiteDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /universites/:id} : Partial updates given fields of an existing universite, field will ignore if it is null
     *
     * @param id the id of the universiteDTO to save.
     * @param universiteDTO the universiteDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated universiteDTO,
     * or with status {@code 400 (Bad Request)} if the universiteDTO is not valid,
     * or with status {@code 404 (Not Found)} if the universiteDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the universiteDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<UniversiteDTO> partialUpdateUniversite(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody UniversiteDTO universiteDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Universite partially : {}, {}", id, universiteDTO);
        if (universiteDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, universiteDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!universiteRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<UniversiteDTO> result = universiteService.partialUpdate(universiteDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, universiteDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /universites} : get all the universites.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of universites in body.
     */
    @GetMapping("")
    public ResponseEntity<List<UniversiteDTO>> getAllUniversites(@org.springdoc.core.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get a page of Universites");
        Page<UniversiteDTO> page = universiteService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /universites} : get all the universites having ministere id.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of universites in body.
     */
    @GetMapping("/ministeres/{id}")
    public ResponseEntity<List<UniversiteDTO>> findByMinistereId(@org.springdoc.core.annotations.ParameterObject Pageable pageable, @PathVariable("id") Long id) {
        log.debug("REST request to get a page of Universites having an id of a ministere");
        Page<UniversiteDTO> page = universiteService.findAllByMinistereId(pageable,id);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /universites/:id} : get the "id" universite.
     *
     * @param id the id of the universiteDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the universiteDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<UniversiteDTO> getUniversite(@PathVariable("id") Long id) {
        log.debug("REST request to get Universite : {}", id);
        Optional<UniversiteDTO> universiteDTO = universiteService.findOne(id);
        return ResponseUtil.wrapOrNotFound(universiteDTO);
    }

    /**
     * {@code DELETE  /universites/:id} : delete the "id" universite.
     *
     * @param id the id of the universiteDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUniversite(@PathVariable("id") Long id) {
        log.debug("REST request to delete Universite : {}", id);
        universiteService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
