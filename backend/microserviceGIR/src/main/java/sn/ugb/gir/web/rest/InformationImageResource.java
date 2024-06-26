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
import sn.ugb.gir.repository.InformationImageRepository;
import sn.ugb.gir.service.InformationImageService;
import sn.ugb.gir.service.dto.InformationImageDTO;
import sn.ugb.gir.web.rest.errors.BadRequestAlertException;
import sn.ugb.gir.web.rest.errors.ElasticsearchExceptionMapper;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link sn.ugb.gir.domain.InformationImage}.
 */
@RestController
@RequestMapping("/api/information-images")
public class InformationImageResource {

    private final Logger log = LoggerFactory.getLogger(InformationImageResource.class);

    private static final String ENTITY_NAME = "microserviceGirInformationImage";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final InformationImageService informationImageService;

    private final InformationImageRepository informationImageRepository;

    public InformationImageResource(
        InformationImageService informationImageService,
        InformationImageRepository informationImageRepository
    ) {
        this.informationImageService = informationImageService;
        this.informationImageRepository = informationImageRepository;
    }

    /**
     * {@code POST  /information-images} : Create a new informationImage.
     *
     * @param informationImageDTO the informationImageDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new informationImageDTO, or with status {@code 400 (Bad Request)} if the informationImage has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<InformationImageDTO> createInformationImage(@Valid @RequestBody InformationImageDTO informationImageDTO)
        throws URISyntaxException {
        log.debug("REST request to save InformationImage : {}", informationImageDTO);
        if (informationImageDTO.getId() != null) {
            throw new BadRequestAlertException("A new informationImage cannot already have an ID", ENTITY_NAME, "idexists");
        }
        InformationImageDTO result = informationImageService.save(informationImageDTO);
        return ResponseEntity
            .created(new URI("/api/information-images/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /information-images/:id} : Updates an existing informationImage.
     *
     * @param id the id of the informationImageDTO to save.
     * @param informationImageDTO the informationImageDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated informationImageDTO,
     * or with status {@code 400 (Bad Request)} if the informationImageDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the informationImageDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<InformationImageDTO> updateInformationImage(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody InformationImageDTO informationImageDTO
    ) throws URISyntaxException {
        log.debug("REST request to update InformationImage : {}, {}", id, informationImageDTO);
        if (informationImageDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, informationImageDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!informationImageRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        InformationImageDTO result = informationImageService.update(informationImageDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, informationImageDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /information-images/:id} : Partial updates given fields of an existing informationImage, field will ignore if it is null
     *
     * @param id the id of the informationImageDTO to save.
     * @param informationImageDTO the informationImageDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated informationImageDTO,
     * or with status {@code 400 (Bad Request)} if the informationImageDTO is not valid,
     * or with status {@code 404 (Not Found)} if the informationImageDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the informationImageDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<InformationImageDTO> partialUpdateInformationImage(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody InformationImageDTO informationImageDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update InformationImage partially : {}, {}", id, informationImageDTO);
        if (informationImageDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, informationImageDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!informationImageRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<InformationImageDTO> result = informationImageService.partialUpdate(informationImageDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, informationImageDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /information-images} : get all the informationImages.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of informationImages in body.
     */
    @GetMapping("")
    public ResponseEntity<List<InformationImageDTO>> getAllInformationImages(
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get a page of InformationImages");
        Page<InformationImageDTO> page = informationImageService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /information-images/:id} : get the "id" informationImage.
     *
     * @param id the id of the informationImageDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the informationImageDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<InformationImageDTO> getInformationImage(@PathVariable("id") Long id) {
        log.debug("REST request to get InformationImage : {}", id);
        Optional<InformationImageDTO> informationImageDTO = informationImageService.findOne(id);
        return ResponseUtil.wrapOrNotFound(informationImageDTO);
    }

    /**
     * {@code DELETE  /information-images/:id} : delete the "id" informationImage.
     *
     * @param id the id of the informationImageDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteInformationImage(@PathVariable("id") Long id) {
        log.debug("REST request to delete InformationImage : {}", id);
        informationImageService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code SEARCH  /information-images/_search?query=:query} : search for the informationImage corresponding
     * to the query.
     *
     * @param query the query of the informationImage search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search")
    public ResponseEntity<List<InformationImageDTO>> searchInformationImages(
        @RequestParam("query") String query,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to search for a page of InformationImages for query {}", query);
        try {
            Page<InformationImageDTO> page = informationImageService.search(query, pageable);
            HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
            return ResponseEntity.ok().headers(headers).body(page.getContent());
        } catch (RuntimeException e) {
            throw ElasticsearchExceptionMapper.mapException(e);
        }
    }
}
