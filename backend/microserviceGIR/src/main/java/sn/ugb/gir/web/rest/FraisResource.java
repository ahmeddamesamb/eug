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
import sn.ugb.gir.repository.FraisRepository;
import sn.ugb.gir.service.FraisService;
import sn.ugb.gir.service.dto.CycleDTO;
import sn.ugb.gir.service.dto.FraisDTO;
import sn.ugb.gir.web.rest.errors.BadRequestAlertException;
import sn.ugb.gir.web.rest.errors.ElasticsearchExceptionMapper;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link sn.ugb.gir.domain.Frais}.
 */
@RestController
@RequestMapping("/api/frais")
public class FraisResource {

    private final Logger log = LoggerFactory.getLogger(FraisResource.class);

    private static final String ENTITY_NAME = "microserviceGirFrais";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final FraisService fraisService;

    private final FraisRepository fraisRepository;

    public FraisResource(FraisService fraisService, FraisRepository fraisRepository) {
        this.fraisService = fraisService;
        this.fraisRepository = fraisRepository;
    }

    /**
     * {@code POST  /frais} : Create a new frais.
     *
     * @param fraisDTO the fraisDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new fraisDTO, or with status {@code 400 (Bad Request)} if the frais has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<FraisDTO> createFrais(@Valid @RequestBody FraisDTO fraisDTO) throws URISyntaxException {
        log.debug("REST request to save Frais : {}", fraisDTO);
        if (fraisDTO.getId() != null) {
            throw new BadRequestAlertException("A new frais cannot already have an ID", ENTITY_NAME, "idexists");
        }
        FraisDTO result = fraisService.save(fraisDTO);
        return ResponseEntity
            .created(new URI("/api/frais/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /frais/:id} : Updates an existing frais.
     *
     * @param id the id of the fraisDTO to save.
     * @param fraisDTO the fraisDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated fraisDTO,
     * or with status {@code 400 (Bad Request)} if the fraisDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the fraisDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<FraisDTO> updateFrais(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody FraisDTO fraisDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Frais : {}, {}", id, fraisDTO);

        validateDataUpdate(fraisDTO,id);
        FraisDTO result = fraisService.update(fraisDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, fraisDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /frais/:id} : Partial updates given fields of an existing frais, field will ignore if it is null
     *
     * @param id the id of the fraisDTO to save.
     * @param fraisDTO the fraisDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated fraisDTO,
     * or with status {@code 400 (Bad Request)} if the fraisDTO is not valid,
     * or with status {@code 404 (Not Found)} if the fraisDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the fraisDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<FraisDTO> partialUpdateFrais(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody FraisDTO fraisDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Frais partially : {}, {}", id, fraisDTO);

        validateDataUpdate(fraisDTO,id);
        Optional<FraisDTO> result = fraisService.partialUpdate(fraisDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, fraisDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /frais} : get all the frais.
     *
     * @param pageable the pagination information.
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of frais in body.
     */
    @GetMapping("")
    public ResponseEntity<List<FraisDTO>> getAllFrais(
        @org.springdoc.core.annotations.ParameterObject Pageable pageable,
        @RequestParam(name = "eagerload", required = false, defaultValue = "true") boolean eagerload
    ) {
        log.debug("REST request to get a page of Frais");
        Page<FraisDTO> page;
        if (eagerload) {
            page = fraisService.findAllWithEagerRelationships(pageable);
        } else {
            page = fraisService.findAll(pageable);
        }
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }


    /**
     * {@code GET  /frais/:id} : get the "id" frais.
     *
     * @param id the id of the fraisDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the fraisDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<FraisDTO> getFrais(@PathVariable("id") Long id) {
        log.debug("REST request to get Frais : {}", id);
        Optional<FraisDTO> fraisDTO = fraisService.findOne(id);
        return ResponseUtil.wrapOrNotFound(fraisDTO);
    }

    /**
     * {@code DELETE  /frais/:id} : delete the "id" frais.
     *
     * @param id the id of the fraisDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFrais(@PathVariable("id") Long id) {
        log.debug("REST request to delete Frais : {}", id);
        fraisService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code SEARCH  /frais/_search?query=:query} : search for the frais corresponding
     * to the query.
     *
     * @param query the query of the frais search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search")
    public ResponseEntity<List<FraisDTO>> searchFrais(
        @RequestParam("query") String query,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to search for a page of Frais for query {}", query);
        try {
            Page<FraisDTO> page = fraisService.search(query, pageable);
            HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
            return ResponseEntity.ok().headers(headers).body(page.getContent());
        } catch (RuntimeException e) {
            throw ElasticsearchExceptionMapper.mapException(e);
        }
    }

    /**
     * {@code GET  /frais} : get all the frais.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of frais in body.
     */
    @GetMapping("/cycleID/{cycleID}")
    public ResponseEntity<List<FraisDTO>> getFraisByCycleId(@org.springdoc.core.annotations.ParameterObject Pageable pageable, @PathVariable("cycleID") Long cycleID) {
        log.debug("REST request to get a page of Frais for a given cycle");
        Page<FraisDTO> page = fraisService.findAllFraisByCycleId(pageable,cycleID);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /frais} : get all the frais.
     *
     * @param pageable the pagination information.
     * @param universiteId an id of universite
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of frais in body.
     */
    @GetMapping("/universiteId/{universiteId}")
    public ResponseEntity<List<FraisDTO>> getAllFraisByUniversite(
        @org.springdoc.core.annotations.ParameterObject Pageable pageable, @PathVariable("universiteId") Long universiteId
    ) {
        log.debug("REST request to get a page of Frais for an universite");
        Page<FraisDTO> page;
        page = fraisService.findAllFraisByUniversiteId(pageable,universiteId);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /frais} : get all the frais.
     *
     * @param pageable the pagination information.
     * @param ministereId an id of ministere
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of frais in body.
     */
    @GetMapping("/ministereId/{ministereId}")
    public ResponseEntity<List<FraisDTO>> getAllFraisByMinistere(
        @org.springdoc.core.annotations.ParameterObject Pageable pageable, @PathVariable("ministereId") Long ministereId
    ) {
        log.debug("REST request to get a page of Frais for an ministere");
        Page<FraisDTO> page;
        page = fraisService.findAllFraisByMinistereId(pageable,ministereId);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    private void validateDataUpdate(FraisDTO fraisDTO,Long id){
        if (fraisDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, fraisDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!fraisRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }
        if (fraisDTO.getEstEnApplicationYN()) {
            fraisDTO.setDateFin(null);
        }
    }

}
