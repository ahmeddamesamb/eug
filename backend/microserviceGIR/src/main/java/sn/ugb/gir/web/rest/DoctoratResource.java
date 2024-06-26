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
import sn.ugb.gir.repository.DoctoratRepository;
import sn.ugb.gir.service.DoctoratService;
import sn.ugb.gir.service.dto.DoctoratDTO;
import sn.ugb.gir.web.rest.errors.BadRequestAlertException;
import sn.ugb.gir.web.rest.errors.ElasticsearchExceptionMapper;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link sn.ugb.gir.domain.Doctorat}.
 */
@RestController
@RequestMapping("/api/doctorats")
public class DoctoratResource {

    private final Logger log = LoggerFactory.getLogger(DoctoratResource.class);

    private static final String ENTITY_NAME = "microserviceGirDoctorat";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final DoctoratService doctoratService;

    private final DoctoratRepository doctoratRepository;

    public DoctoratResource(DoctoratService doctoratService, DoctoratRepository doctoratRepository) {
        this.doctoratService = doctoratService;
        this.doctoratRepository = doctoratRepository;
    }

    /**
     * {@code POST  /doctorats} : Create a new doctorat.
     *
     * @param doctoratDTO the doctoratDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new doctoratDTO, or with status {@code 400 (Bad Request)} if the doctorat has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<DoctoratDTO> createDoctorat(@Valid @RequestBody DoctoratDTO doctoratDTO) throws URISyntaxException {
        log.debug("REST request to save Doctorat : {}", doctoratDTO);
        if (doctoratDTO.getId() != null) {
            throw new BadRequestAlertException("A new doctorat cannot already have an ID", ENTITY_NAME, "idexists");
        }
        DoctoratDTO result = doctoratService.save(doctoratDTO);
        return ResponseEntity
            .created(new URI("/api/doctorats/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /doctorats/:id} : Updates an existing doctorat.
     *
     * @param id the id of the doctoratDTO to save.
     * @param doctoratDTO the doctoratDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated doctoratDTO,
     * or with status {@code 400 (Bad Request)} if the doctoratDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the doctoratDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<DoctoratDTO> updateDoctorat(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody DoctoratDTO doctoratDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Doctorat : {}, {}", id, doctoratDTO);
        if (doctoratDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, doctoratDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!doctoratRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        DoctoratDTO result = doctoratService.update(doctoratDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, doctoratDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /doctorats/:id} : Partial updates given fields of an existing doctorat, field will ignore if it is null
     *
     * @param id the id of the doctoratDTO to save.
     * @param doctoratDTO the doctoratDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated doctoratDTO,
     * or with status {@code 400 (Bad Request)} if the doctoratDTO is not valid,
     * or with status {@code 404 (Not Found)} if the doctoratDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the doctoratDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<DoctoratDTO> partialUpdateDoctorat(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody DoctoratDTO doctoratDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Doctorat partially : {}, {}", id, doctoratDTO);
        if (doctoratDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, doctoratDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!doctoratRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<DoctoratDTO> result = doctoratService.partialUpdate(doctoratDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, doctoratDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /doctorats} : get all the doctorats.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of doctorats in body.
     */
    @GetMapping("")
    public ResponseEntity<List<DoctoratDTO>> getAllDoctorats(@org.springdoc.core.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get a page of Doctorats");
        Page<DoctoratDTO> page = doctoratService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /doctorats/:id} : get the "id" doctorat.
     *
     * @param id the id of the doctoratDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the doctoratDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<DoctoratDTO> getDoctorat(@PathVariable("id") Long id) {
        log.debug("REST request to get Doctorat : {}", id);
        Optional<DoctoratDTO> doctoratDTO = doctoratService.findOne(id);
        return ResponseUtil.wrapOrNotFound(doctoratDTO);
    }

    /**
     * {@code DELETE  /doctorats/:id} : delete the "id" doctorat.
     *
     * @param id the id of the doctoratDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDoctorat(@PathVariable("id") Long id) {
        log.debug("REST request to delete Doctorat : {}", id);
        doctoratService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code SEARCH  /doctorats/_search?query=:query} : search for the doctorat corresponding
     * to the query.
     *
     * @param query the query of the doctorat search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search")
    public ResponseEntity<List<DoctoratDTO>> searchDoctorats(
        @RequestParam("query") String query,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to search for a page of Doctorats for query {}", query);
        try {
            Page<DoctoratDTO> page = doctoratService.search(query, pageable);
            HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
            return ResponseEntity.ok().headers(headers).body(page.getContent());
        } catch (RuntimeException e) {
            throw ElasticsearchExceptionMapper.mapException(e);
        }
    }
}
