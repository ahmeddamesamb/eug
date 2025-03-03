package sn.ugb.ged.web.rest;

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
import sn.ugb.ged.repository.DocumentDelivreRepository;
import sn.ugb.ged.service.DocumentDelivreService;
import sn.ugb.ged.service.dto.DocumentDelivreDTO;
import sn.ugb.ged.web.rest.errors.BadRequestAlertException;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link sn.ugb.ged.domain.DocumentDelivre}.
 */
@RestController
@RequestMapping("/api/document-delivres")
public class DocumentDelivreResource {

    private final Logger log = LoggerFactory.getLogger(DocumentDelivreResource.class);

    private static final String ENTITY_NAME = "microserviceGedDocumentDelivre";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final DocumentDelivreService documentDelivreService;

    private final DocumentDelivreRepository documentDelivreRepository;

    public DocumentDelivreResource(DocumentDelivreService documentDelivreService, DocumentDelivreRepository documentDelivreRepository) {
        this.documentDelivreService = documentDelivreService;
        this.documentDelivreRepository = documentDelivreRepository;
    }

    /**
     * {@code POST  /document-delivres} : Create a new documentDelivre.
     *
     * @param documentDelivreDTO the documentDelivreDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new documentDelivreDTO, or with status {@code 400 (Bad Request)} if the documentDelivre has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<DocumentDelivreDTO> createDocumentDelivre(@RequestBody DocumentDelivreDTO documentDelivreDTO)
        throws URISyntaxException {
        log.debug("REST request to save DocumentDelivre : {}", documentDelivreDTO);
        if (documentDelivreDTO.getId() != null) {
            throw new BadRequestAlertException("A new documentDelivre cannot already have an ID", ENTITY_NAME, "idexists");
        }
        DocumentDelivreDTO result = documentDelivreService.save(documentDelivreDTO);
        return ResponseEntity
            .created(new URI("/api/document-delivres/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /document-delivres/:id} : Updates an existing documentDelivre.
     *
     * @param id the id of the documentDelivreDTO to save.
     * @param documentDelivreDTO the documentDelivreDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated documentDelivreDTO,
     * or with status {@code 400 (Bad Request)} if the documentDelivreDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the documentDelivreDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<DocumentDelivreDTO> updateDocumentDelivre(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody DocumentDelivreDTO documentDelivreDTO
    ) throws URISyntaxException {
        log.debug("REST request to update DocumentDelivre : {}, {}", id, documentDelivreDTO);
        if (documentDelivreDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, documentDelivreDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!documentDelivreRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        DocumentDelivreDTO result = documentDelivreService.update(documentDelivreDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, documentDelivreDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /document-delivres/:id} : Partial updates given fields of an existing documentDelivre, field will ignore if it is null
     *
     * @param id the id of the documentDelivreDTO to save.
     * @param documentDelivreDTO the documentDelivreDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated documentDelivreDTO,
     * or with status {@code 400 (Bad Request)} if the documentDelivreDTO is not valid,
     * or with status {@code 404 (Not Found)} if the documentDelivreDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the documentDelivreDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<DocumentDelivreDTO> partialUpdateDocumentDelivre(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody DocumentDelivreDTO documentDelivreDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update DocumentDelivre partially : {}, {}", id, documentDelivreDTO);
        if (documentDelivreDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, documentDelivreDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!documentDelivreRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<DocumentDelivreDTO> result = documentDelivreService.partialUpdate(documentDelivreDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, documentDelivreDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /document-delivres} : get all the documentDelivres.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of documentDelivres in body.
     */
    @GetMapping("")
    public ResponseEntity<List<DocumentDelivreDTO>> getAllDocumentDelivres(
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get a page of DocumentDelivres");
        Page<DocumentDelivreDTO> page = documentDelivreService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /document-delivres/:id} : get the "id" documentDelivre.
     *
     * @param id the id of the documentDelivreDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the documentDelivreDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<DocumentDelivreDTO> getDocumentDelivre(@PathVariable("id") Long id) {
        log.debug("REST request to get DocumentDelivre : {}", id);
        Optional<DocumentDelivreDTO> documentDelivreDTO = documentDelivreService.findOne(id);
        return ResponseUtil.wrapOrNotFound(documentDelivreDTO);
    }

    /**
     * {@code DELETE  /document-delivres/:id} : delete the "id" documentDelivre.
     *
     * @param id the id of the documentDelivreDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDocumentDelivre(@PathVariable("id") Long id) {
        log.debug("REST request to delete DocumentDelivre : {}", id);
        documentDelivreService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
