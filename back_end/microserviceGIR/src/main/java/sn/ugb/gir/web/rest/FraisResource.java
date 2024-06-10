package sn.ugb.gir.web.rest;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.net.URI;
import java.net.URISyntaxException;
import java.time.LocalDate;
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
import sn.ugb.gir.domain.enumeration.Cycle;
import sn.ugb.gir.repository.FraisRepository;
import sn.ugb.gir.service.FraisService;
import sn.ugb.gir.service.dto.FraisDTO;
import sn.ugb.gir.web.rest.errors.BadRequestAlertException;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

import static org.hibernate.id.IdentifierGenerator.ENTITY_NAME;

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
            throw new BadRequestAlertException("Un nouveau frais ne doit pas avoir un ID", ENTITY_NAME, "idexists");
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
        FraisDTO result = fraisService.update(fraisDTO, id);
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
        Optional<FraisDTO> result = fraisService.partialUpdate(fraisDTO, id);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, fraisDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /frais} : get all the frais.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of frais in body.
     */
    @GetMapping("")
    public ResponseEntity<List<FraisDTO>> getAllFrais(@org.springdoc.core.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get a page of Frais");
        Page<FraisDTO> page = fraisService.findAll(pageable);
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
     * {@code GET  /frais} : get all the frais.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of frais in body.
     */
    @GetMapping("/cycle/{cycle}")
    public ResponseEntity<List<FraisDTO>> getFraisByCycle(@org.springdoc.core.annotations.ParameterObject Pageable pageable, @PathVariable("cycle") Cycle cycle) {
        log.debug("REST request to get a page of Frais for a given cycle");
        Page<FraisDTO> page = fraisService.findAllFraisByCycle(pageable,cycle);
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
        if (fraisDTO.getEstEnApplicationYN() == 1) {
            fraisDTO.setDateFin(null);
        }
    }
}
