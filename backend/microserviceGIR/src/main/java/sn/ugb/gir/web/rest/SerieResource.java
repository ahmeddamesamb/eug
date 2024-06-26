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
import sn.ugb.gir.repository.SerieRepository;
import sn.ugb.gir.service.SerieService;
import sn.ugb.gir.service.dto.SerieDTO;
import sn.ugb.gir.web.rest.errors.BadRequestAlertException;
import sn.ugb.gir.web.rest.errors.ElasticsearchExceptionMapper;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link sn.ugb.gir.domain.Serie}.
 */
@RestController
@RequestMapping("/api/series")
public class SerieResource {

    private final Logger log = LoggerFactory.getLogger(SerieResource.class);

    private static final String ENTITY_NAME = "microserviceGirSerie";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final SerieService serieService;

    private final SerieRepository serieRepository;

    public SerieResource(SerieService serieService, SerieRepository serieRepository) {
        this.serieService = serieService;
        this.serieRepository = serieRepository;
    }

    /**
     * {@code POST  /series} : Create a new serie.
     *
     * @param serieDTO the serieDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new serieDTO, or with status {@code 400 (Bad Request)} if the serie has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<SerieDTO> createSerie(@Valid @RequestBody SerieDTO serieDTO) throws URISyntaxException {
        log.debug("REST request to save Serie : {}", serieDTO);
        if (serieDTO.getId() != null) {
            throw new BadRequestAlertException("A new serie cannot already have an ID", ENTITY_NAME, "idexists");
        }
        SerieDTO result = serieService.save(serieDTO);
        return ResponseEntity
            .created(new URI("/api/series/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /series/:id} : Updates an existing serie.
     *
     * @param id the id of the serieDTO to save.
     * @param serieDTO the serieDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated serieDTO,
     * or with status {@code 400 (Bad Request)} if the serieDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the serieDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<SerieDTO> updateSerie(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody SerieDTO serieDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Serie : {}, {}", id, serieDTO);
        if (serieDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, serieDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!serieRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        SerieDTO result = serieService.update(serieDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, serieDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /series/:id} : Partial updates given fields of an existing serie, field will ignore if it is null
     *
     * @param id the id of the serieDTO to save.
     * @param serieDTO the serieDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated serieDTO,
     * or with status {@code 400 (Bad Request)} if the serieDTO is not valid,
     * or with status {@code 404 (Not Found)} if the serieDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the serieDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<SerieDTO> partialUpdateSerie(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody SerieDTO serieDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Serie partially : {}, {}", id, serieDTO);
        if (serieDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, serieDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!serieRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<SerieDTO> result = serieService.partialUpdate(serieDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, serieDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /series} : get all the series.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of series in body.
     */
    @GetMapping("")
    public ResponseEntity<List<SerieDTO>> getAllSeries(@org.springdoc.core.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get a page of Series");
        Page<SerieDTO> page = serieService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /series/:id} : get the "id" serie.
     *
     * @param id the id of the serieDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the serieDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<SerieDTO> getSerie(@PathVariable("id") Long id) {
        log.debug("REST request to get Serie : {}", id);
        Optional<SerieDTO> serieDTO = serieService.findOne(id);
        return ResponseUtil.wrapOrNotFound(serieDTO);
    }

    /**
     * {@code DELETE  /series/:id} : delete the "id" serie.
     *
     * @param id the id of the serieDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSerie(@PathVariable("id") Long id) {
        log.debug("REST request to delete Serie : {}", id);
        serieService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code SEARCH  /series/_search?query=:query} : search for the serie corresponding
     * to the query.
     *
     * @param query the query of the serie search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search")
    public ResponseEntity<List<SerieDTO>> searchSeries(
        @RequestParam("query") String query,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to search for a page of Series for query {}", query);
        try {
            Page<SerieDTO> page = serieService.search(query, pageable);
            HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
            return ResponseEntity.ok().headers(headers).body(page.getContent());
        } catch (RuntimeException e) {
            throw ElasticsearchExceptionMapper.mapException(e);
        }
    }
}
