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
import sn.ugb.gir.repository.OperateurRepository;
import sn.ugb.gir.service.OperateurService;
import sn.ugb.gir.service.dto.OperateurDTO;
import sn.ugb.gir.web.rest.errors.BadRequestAlertException;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

import static org.hibernate.id.IdentifierGenerator.ENTITY_NAME;

/**
 * REST controller for managing {@link sn.ugb.gir.domain.Operateur}.
 */
@RestController
@RequestMapping("/api/operateurs")
public class OperateurResource {

    private final Logger log = LoggerFactory.getLogger(OperateurResource.class);

    private static final String ENTITY_NAME = "microserviceGirOperateur";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final OperateurService operateurService;

    private final OperateurRepository operateurRepository;

    public OperateurResource(OperateurService operateurService, OperateurRepository operateurRepository) {
        this.operateurService = operateurService;
        this.operateurRepository = operateurRepository;
    }

    /**
     * {@code POST  /operateurs} : Create a new operateur.
     *
     * @param operateurDTO the operateurDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new operateurDTO, or with status {@code 400 (Bad Request)} if the operateur has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<OperateurDTO> createOperateur(@Valid @RequestBody OperateurDTO operateurDTO) throws URISyntaxException {
        log.debug("REST request to save Operateur : {}", operateurDTO);


        OperateurDTO result = operateurService.save(operateurDTO);
        return ResponseEntity
            .created(new URI("/api/operateurs/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /operateurs/:id} : Updates an existing operateur.
     *
     * @param id the id of the operateurDTO to save.
     * @param operateurDTO the operateurDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated operateurDTO,
     * or with status {@code 400 (Bad Request)} if the operateurDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the operateurDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<OperateurDTO> updateOperateur(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody OperateurDTO operateurDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Operateur : {}, {}", id, operateurDTO);

        validateDataUpdate(operateurDTO,id);
        OperateurDTO result = operateurService.update(operateurDTO,id);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, operateurDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /operateurs/:id} : Partial updates given fields of an existing operateur, field will ignore if it is null
     *
     * @param id the id of the operateurDTO to save.
     * @param operateurDTO the operateurDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated operateurDTO,
     * or with status {@code 400 (Bad Request)} if the operateurDTO is not valid,
     * or with status {@code 404 (Not Found)} if the operateurDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the operateurDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<OperateurDTO> partialUpdateOperateur(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody OperateurDTO operateurDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Operateur partially : {}, {}", id, operateurDTO);

        validateDataUpdate(operateurDTO,id);
        Optional<OperateurDTO> result = operateurService.partialUpdate(operateurDTO,id);
        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, operateurDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /operateurs} : get all the operateurs.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of operateurs in body.
     */
    @GetMapping("")
    public ResponseEntity<List<OperateurDTO>> getAllOperateurs(@org.springdoc.core.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get a page of Operateurs");
        Page<OperateurDTO> page = operateurService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /operateurs/:id} : get the "id" operateur.
     *
     * @param id the id of the operateurDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the operateurDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<OperateurDTO> getOperateur(@PathVariable("id") Long id) {
        log.debug("REST request to get Operateur : {}", id);
        Optional<OperateurDTO> operateurDTO = operateurService.findOne(id);
        return ResponseUtil.wrapOrNotFound(operateurDTO);
    }

    /**
     * {@code DELETE  /operateurs/:id} : delete the "id" operateur.
     *
     * @param id the id of the operateurDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOperateur(@PathVariable("id") Long id) {
        log.debug("REST request to delete Operateur : {}", id);
        operateurService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
    public void validateDataUpdate(OperateurDTO operateurDTO, Long id){
        if (operateurDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, operateurDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }
        if (!operateurRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }
    }
}
