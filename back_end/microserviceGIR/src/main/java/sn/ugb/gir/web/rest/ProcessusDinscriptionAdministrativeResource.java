package sn.ugb.gir.web.rest;

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
import sn.ugb.gir.repository.ProcessusDinscriptionAdministrativeRepository;
import sn.ugb.gir.service.ProcessusDinscriptionAdministrativeService;
import sn.ugb.gir.service.dto.ProcessusDinscriptionAdministrativeDTO;
import sn.ugb.gir.web.rest.errors.BadRequestAlertException;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link sn.ugb.gir.domain.ProcessusDinscriptionAdministrative}.
 */
@RestController
@RequestMapping("/api/processus-dinscription-administratives")
public class ProcessusDinscriptionAdministrativeResource {

    private final Logger log = LoggerFactory.getLogger(ProcessusDinscriptionAdministrativeResource.class);

    private static final String ENTITY_NAME = "microserviceGirProcessusDinscriptionAdministrative";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ProcessusDinscriptionAdministrativeService processusDinscriptionAdministrativeService;

    private final ProcessusDinscriptionAdministrativeRepository processusDinscriptionAdministrativeRepository;

    public ProcessusDinscriptionAdministrativeResource(
        ProcessusDinscriptionAdministrativeService processusDinscriptionAdministrativeService,
        ProcessusDinscriptionAdministrativeRepository processusDinscriptionAdministrativeRepository
    ) {
        this.processusDinscriptionAdministrativeService = processusDinscriptionAdministrativeService;
        this.processusDinscriptionAdministrativeRepository = processusDinscriptionAdministrativeRepository;
    }

    /**
     * {@code POST  /processus-dinscription-administratives} : Create a new processusDinscriptionAdministrative.
     *
     * @param processusDinscriptionAdministrativeDTO the processusDinscriptionAdministrativeDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new processusDinscriptionAdministrativeDTO, or with status {@code 400 (Bad Request)} if the processusDinscriptionAdministrative has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<ProcessusDinscriptionAdministrativeDTO> createProcessusDinscriptionAdministrative(
        @RequestBody ProcessusDinscriptionAdministrativeDTO processusDinscriptionAdministrativeDTO
    ) throws URISyntaxException {
        log.debug("REST request to save ProcessusDinscriptionAdministrative : {}", processusDinscriptionAdministrativeDTO);
        if (processusDinscriptionAdministrativeDTO.getId() != null) {
            throw new BadRequestAlertException(
                "A new processusDinscriptionAdministrative cannot already have an ID",
                ENTITY_NAME,
                "idexists"
            );
        }
        ProcessusDinscriptionAdministrativeDTO result = processusDinscriptionAdministrativeService.save(
            processusDinscriptionAdministrativeDTO
        );
        return ResponseEntity
            .created(new URI("/api/processus-dinscription-administratives/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /processus-dinscription-administratives/:id} : Updates an existing processusDinscriptionAdministrative.
     *
     * @param id the id of the processusDinscriptionAdministrativeDTO to save.
     * @param processusDinscriptionAdministrativeDTO the processusDinscriptionAdministrativeDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated processusDinscriptionAdministrativeDTO,
     * or with status {@code 400 (Bad Request)} if the processusDinscriptionAdministrativeDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the processusDinscriptionAdministrativeDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<ProcessusDinscriptionAdministrativeDTO> updateProcessusDinscriptionAdministrative(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody ProcessusDinscriptionAdministrativeDTO processusDinscriptionAdministrativeDTO
    ) throws URISyntaxException {
        log.debug("REST request to update ProcessusDinscriptionAdministrative : {}, {}", id, processusDinscriptionAdministrativeDTO);
        if (processusDinscriptionAdministrativeDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, processusDinscriptionAdministrativeDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!processusDinscriptionAdministrativeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        ProcessusDinscriptionAdministrativeDTO result = processusDinscriptionAdministrativeService.update(
            processusDinscriptionAdministrativeDTO
        );
        return ResponseEntity
            .ok()
            .headers(
                HeaderUtil.createEntityUpdateAlert(
                    applicationName,
                    true,
                    ENTITY_NAME,
                    processusDinscriptionAdministrativeDTO.getId().toString()
                )
            )
            .body(result);
    }

    /**
     * {@code PATCH  /processus-dinscription-administratives/:id} : Partial updates given fields of an existing processusDinscriptionAdministrative, field will ignore if it is null
     *
     * @param id the id of the processusDinscriptionAdministrativeDTO to save.
     * @param processusDinscriptionAdministrativeDTO the processusDinscriptionAdministrativeDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated processusDinscriptionAdministrativeDTO,
     * or with status {@code 400 (Bad Request)} if the processusDinscriptionAdministrativeDTO is not valid,
     * or with status {@code 404 (Not Found)} if the processusDinscriptionAdministrativeDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the processusDinscriptionAdministrativeDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<ProcessusDinscriptionAdministrativeDTO> partialUpdateProcessusDinscriptionAdministrative(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody ProcessusDinscriptionAdministrativeDTO processusDinscriptionAdministrativeDTO
    ) throws URISyntaxException {
        log.debug(
            "REST request to partial update ProcessusDinscriptionAdministrative partially : {}, {}",
            id,
            processusDinscriptionAdministrativeDTO
        );
        if (processusDinscriptionAdministrativeDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, processusDinscriptionAdministrativeDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!processusDinscriptionAdministrativeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ProcessusDinscriptionAdministrativeDTO> result = processusDinscriptionAdministrativeService.partialUpdate(
            processusDinscriptionAdministrativeDTO
        );

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(
                applicationName,
                true,
                ENTITY_NAME,
                processusDinscriptionAdministrativeDTO.getId().toString()
            )
        );
    }

    /**
     * {@code GET  /processus-dinscription-administratives} : get all the processusDinscriptionAdministratives.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of processusDinscriptionAdministratives in body.
     */
    @GetMapping("")
    public ResponseEntity<List<ProcessusDinscriptionAdministrativeDTO>> getAllProcessusDinscriptionAdministratives(
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get a page of ProcessusDinscriptionAdministratives");
        Page<ProcessusDinscriptionAdministrativeDTO> page = processusDinscriptionAdministrativeService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /processus-dinscription-administratives/:id} : get the "id" processusDinscriptionAdministrative.
     *
     * @param id the id of the processusDinscriptionAdministrativeDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the processusDinscriptionAdministrativeDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<ProcessusDinscriptionAdministrativeDTO> getProcessusDinscriptionAdministrative(@PathVariable("id") Long id) {
        log.debug("REST request to get ProcessusDinscriptionAdministrative : {}", id);
        Optional<ProcessusDinscriptionAdministrativeDTO> processusDinscriptionAdministrativeDTO =
            processusDinscriptionAdministrativeService.findOne(id);
        return ResponseUtil.wrapOrNotFound(processusDinscriptionAdministrativeDTO);
    }

    /**
     * {@code DELETE  /processus-dinscription-administratives/:id} : delete the "id" processusDinscriptionAdministrative.
     *
     * @param id the id of the processusDinscriptionAdministrativeDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProcessusDinscriptionAdministrative(@PathVariable("id") Long id) {
        log.debug("REST request to delete ProcessusDinscriptionAdministrative : {}", id);
        processusDinscriptionAdministrativeService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
