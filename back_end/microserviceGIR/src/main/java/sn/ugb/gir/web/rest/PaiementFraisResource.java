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
import sn.ugb.gir.repository.PaiementFraisRepository;
import sn.ugb.gir.service.PaiementFraisService;
import sn.ugb.gir.service.dto.PaiementFraisDTO;
import sn.ugb.gir.web.rest.errors.BadRequestAlertException;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link sn.ugb.gir.domain.PaiementFrais}.
 */
@RestController
@RequestMapping("/api/paiement-frais")
public class PaiementFraisResource {

    private final Logger log = LoggerFactory.getLogger(PaiementFraisResource.class);

    private static final String ENTITY_NAME = "microserviceGirPaiementFrais";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final PaiementFraisService paiementFraisService;

    private final PaiementFraisRepository paiementFraisRepository;

    public PaiementFraisResource(PaiementFraisService paiementFraisService, PaiementFraisRepository paiementFraisRepository) {
        this.paiementFraisService = paiementFraisService;
        this.paiementFraisRepository = paiementFraisRepository;
    }

    /**
     * {@code POST  /paiement-frais} : Create a new paiementFrais.
     *
     * @param paiementFraisDTO the paiementFraisDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new paiementFraisDTO, or with status {@code 400 (Bad Request)} if the paiementFrais has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<PaiementFraisDTO> createPaiementFrais(@Valid @RequestBody PaiementFraisDTO paiementFraisDTO)
        throws URISyntaxException {
        log.debug("REST request to save PaiementFrais : {}", paiementFraisDTO);
        if (paiementFraisDTO.getId() != null) {
            throw new BadRequestAlertException("A new paiementFrais cannot already have an ID", ENTITY_NAME, "idexists");
        }
        PaiementFraisDTO result = paiementFraisService.save(paiementFraisDTO);
        return ResponseEntity
            .created(new URI("/api/paiement-frais/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /paiement-frais/:id} : Updates an existing paiementFrais.
     *
     * @param id the id of the paiementFraisDTO to save.
     * @param paiementFraisDTO the paiementFraisDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated paiementFraisDTO,
     * or with status {@code 400 (Bad Request)} if the paiementFraisDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the paiementFraisDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<PaiementFraisDTO> updatePaiementFrais(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody PaiementFraisDTO paiementFraisDTO
    ) throws URISyntaxException {
        log.debug("REST request to update PaiementFrais : {}, {}", id, paiementFraisDTO);
        if (paiementFraisDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, paiementFraisDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!paiementFraisRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        PaiementFraisDTO result = paiementFraisService.update(paiementFraisDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, paiementFraisDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /paiement-frais/:id} : Partial updates given fields of an existing paiementFrais, field will ignore if it is null
     *
     * @param id the id of the paiementFraisDTO to save.
     * @param paiementFraisDTO the paiementFraisDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated paiementFraisDTO,
     * or with status {@code 400 (Bad Request)} if the paiementFraisDTO is not valid,
     * or with status {@code 404 (Not Found)} if the paiementFraisDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the paiementFraisDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<PaiementFraisDTO> partialUpdatePaiementFrais(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody PaiementFraisDTO paiementFraisDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update PaiementFrais partially : {}, {}", id, paiementFraisDTO);
        if (paiementFraisDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, paiementFraisDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!paiementFraisRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<PaiementFraisDTO> result = paiementFraisService.partialUpdate(paiementFraisDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, paiementFraisDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /paiement-frais} : get all the paiementFrais.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of paiementFrais in body.
     */
    @GetMapping("")
    public ResponseEntity<List<PaiementFraisDTO>> getAllPaiementFrais(@org.springdoc.core.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get a page of PaiementFrais");
        Page<PaiementFraisDTO> page = paiementFraisService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /paiement-frais/:id} : get the "id" paiementFrais.
     *
     * @param id the id of the paiementFraisDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the paiementFraisDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<PaiementFraisDTO> getPaiementFrais(@PathVariable("id") Long id) {
        log.debug("REST request to get PaiementFrais : {}", id);
        Optional<PaiementFraisDTO> paiementFraisDTO = paiementFraisService.findOne(id);
        return ResponseUtil.wrapOrNotFound(paiementFraisDTO);
    }

    /**
     * {@code DELETE  /paiement-frais/:id} : delete the "id" paiementFrais.
     *
     * @param id the id of the paiementFraisDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePaiementFrais(@PathVariable("id") Long id) {
        log.debug("REST request to delete PaiementFrais : {}", id);
        paiementFraisService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
