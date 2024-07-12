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
import sn.ugb.gir.repository.ProcessusInscriptionAdministrativeRepository;
import sn.ugb.gir.service.ProcessusInscriptionAdministrativeService;
import sn.ugb.gir.service.dto.EtudiantDTO;
import sn.ugb.gir.service.dto.InformationPersonnelleDTO;
import sn.ugb.gir.service.dto.ProcessusInscriptionAdministrativeDTO;
import sn.ugb.gir.web.rest.errors.BadRequestAlertException;
import sn.ugb.gir.web.rest.errors.ElasticsearchExceptionMapper;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link sn.ugb.gir.domain.ProcessusInscriptionAdministrative}.
 */
@RestController
@RequestMapping("/api/processus-inscription-administratives")
public class ProcessusInscriptionAdministrativeResource {

    private final Logger log = LoggerFactory.getLogger(ProcessusInscriptionAdministrativeResource.class);

    private static final String ENTITY_NAME = "microserviceGirProcessusInscriptionAdministrative";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ProcessusInscriptionAdministrativeService processusInscriptionAdministrativeService;

    private final ProcessusInscriptionAdministrativeRepository processusInscriptionAdministrativeRepository;

    public ProcessusInscriptionAdministrativeResource(
        ProcessusInscriptionAdministrativeService processusInscriptionAdministrativeService,
        ProcessusInscriptionAdministrativeRepository processusInscriptionAdministrativeRepository
    ) {
        this.processusInscriptionAdministrativeService = processusInscriptionAdministrativeService;
        this.processusInscriptionAdministrativeRepository = processusInscriptionAdministrativeRepository;
    }

    /**
     * {@code POST  /processus-inscription-administratives} : Create a new processusInscriptionAdministrative.
     *
     * @param processusInscriptionAdministrativeDTO the processusInscriptionAdministrativeDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new processusInscriptionAdministrativeDTO, or with status {@code 400 (Bad Request)} if the processusInscriptionAdministrative has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<ProcessusInscriptionAdministrativeDTO> createProcessusInscriptionAdministrative(
        @RequestBody ProcessusInscriptionAdministrativeDTO processusInscriptionAdministrativeDTO
    ) throws URISyntaxException {
        log.debug("REST request to save ProcessusInscriptionAdministrative : {}", processusInscriptionAdministrativeDTO);
        if (processusInscriptionAdministrativeDTO.getId() != null) {
            throw new BadRequestAlertException(
                "A new processusInscriptionAdministrative cannot already have an ID",
                ENTITY_NAME,
                "idexists"
            );
        }
        ProcessusInscriptionAdministrativeDTO result = processusInscriptionAdministrativeService.save(
            processusInscriptionAdministrativeDTO
        );
        return ResponseEntity
            .created(new URI("/api/processus-inscription-administratives/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /processus-inscription-administratives/:id} : Updates an existing processusInscriptionAdministrative.
     *
     * @param id the id of the processusInscriptionAdministrativeDTO to save.
     * @param processusInscriptionAdministrativeDTO the processusInscriptionAdministrativeDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated processusInscriptionAdministrativeDTO,
     * or with status {@code 400 (Bad Request)} if the processusInscriptionAdministrativeDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the processusInscriptionAdministrativeDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<ProcessusInscriptionAdministrativeDTO> updateProcessusInscriptionAdministrative(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody ProcessusInscriptionAdministrativeDTO processusInscriptionAdministrativeDTO
    ) throws URISyntaxException {
        log.debug("REST request to update ProcessusInscriptionAdministrative : {}, {}", id, processusInscriptionAdministrativeDTO);
        if (processusInscriptionAdministrativeDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, processusInscriptionAdministrativeDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!processusInscriptionAdministrativeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        ProcessusInscriptionAdministrativeDTO result = processusInscriptionAdministrativeService.update(
            processusInscriptionAdministrativeDTO
        );
        return ResponseEntity
            .ok()
            .headers(
                HeaderUtil.createEntityUpdateAlert(
                    applicationName,
                    true,
                    ENTITY_NAME,
                    processusInscriptionAdministrativeDTO.getId().toString()
                )
            )
            .body(result);
    }

    /**
     * {@code PATCH  /processus-inscription-administratives/:id} : Partial updates given fields of an existing processusInscriptionAdministrative, field will ignore if it is null
     *
     * @param id the id of the processusInscriptionAdministrativeDTO to save.
     * @param processusInscriptionAdministrativeDTO the processusInscriptionAdministrativeDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated processusInscriptionAdministrativeDTO,
     * or with status {@code 400 (Bad Request)} if the processusInscriptionAdministrativeDTO is not valid,
     * or with status {@code 404 (Not Found)} if the processusInscriptionAdministrativeDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the processusInscriptionAdministrativeDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<ProcessusInscriptionAdministrativeDTO> partialUpdateProcessusInscriptionAdministrative(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody ProcessusInscriptionAdministrativeDTO processusInscriptionAdministrativeDTO
    ) throws URISyntaxException {
        log.debug(
            "REST request to partial update ProcessusInscriptionAdministrative partially : {}, {}",
            id,
            processusInscriptionAdministrativeDTO
        );
        if (processusInscriptionAdministrativeDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, processusInscriptionAdministrativeDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!processusInscriptionAdministrativeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ProcessusInscriptionAdministrativeDTO> result = processusInscriptionAdministrativeService.partialUpdate(
            processusInscriptionAdministrativeDTO
        );

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, processusInscriptionAdministrativeDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /processus-inscription-administratives} : get all the processusInscriptionAdministratives.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of processusInscriptionAdministratives in body.
     */
    @GetMapping("")
    public ResponseEntity<List<ProcessusInscriptionAdministrativeDTO>> getAllProcessusInscriptionAdministratives(
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get a page of ProcessusInscriptionAdministratives");
        Page<ProcessusInscriptionAdministrativeDTO> page = processusInscriptionAdministrativeService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /processus-inscription-administratives/:id} : get the "id" processusInscriptionAdministrative.
     *
     * @param id the id of the processusInscriptionAdministrativeDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the processusInscriptionAdministrativeDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<ProcessusInscriptionAdministrativeDTO> getProcessusInscriptionAdministrative(@PathVariable("id") Long id) {
        log.debug("REST request to get ProcessusInscriptionAdministrative : {}", id);
        Optional<ProcessusInscriptionAdministrativeDTO> processusInscriptionAdministrativeDTO =
            processusInscriptionAdministrativeService.findOne(id);
        return ResponseUtil.wrapOrNotFound(processusInscriptionAdministrativeDTO);
    }

    /**
     * {@code DELETE  /processus-inscription-administratives/:id} : delete the "id" processusInscriptionAdministrative.
     *
     * @param id the id of the processusInscriptionAdministrativeDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProcessusInscriptionAdministrative(@PathVariable("id") Long id) {
        log.debug("REST request to delete ProcessusInscriptionAdministrative : {}", id);
        processusInscriptionAdministrativeService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code SEARCH  /processus-inscription-administratives/_search?query=:query} : search for the processusInscriptionAdministrative corresponding
     * to the query.
     *
     * @param query the query of the processusInscriptionAdministrative search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search")
    public ResponseEntity<List<ProcessusInscriptionAdministrativeDTO>> searchProcessusInscriptionAdministratives(
        @RequestParam("query") String query,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to search for a page of ProcessusInscriptionAdministratives for query {}", query);
        try {
            Page<ProcessusInscriptionAdministrativeDTO> page = processusInscriptionAdministrativeService.search(query, pageable);
            HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
            return ResponseEntity.ok().headers(headers).body(page.getContent());
        } catch (RuntimeException e) {
            throw ElasticsearchExceptionMapper.mapException(e);
        }
    }
    @PostMapping("/generate-institutional-email/{codeEtu}")
    public ResponseEntity<String> generateEmail(@PathVariable String codeEtu) {
        String email = processusInscriptionAdministrativeService.generateInstitutionalEmail(codeEtu);
        return ResponseEntity.ok(email);
    }

    @PostMapping("/generateCodeBU/{codeEtu}")
    public ResponseEntity<String> generateCodeEtudiant(@PathVariable String codeEtu) {
        String codeBU = processusInscriptionAdministrativeService.generateCodeBareBU(codeEtu);
        return ResponseEntity.ok(codeBU);
    }

}
