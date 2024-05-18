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
import sn.ugb.gir.repository.CampagneRepository;
import sn.ugb.gir.service.CampagneService;
import sn.ugb.gir.service.dto.CampagneDTO;
import sn.ugb.gir.web.rest.errors.BadRequestAlertException;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link sn.ugb.gir.domain.Campagne}.
 */
@RestController
@RequestMapping("/api/campagnes")
public class CampagneResource {

    private final Logger log = LoggerFactory.getLogger(CampagneResource.class);

    private static final String ENTITY_NAME = "microserviceGirCampagne";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CampagneService campagneService;

    private final CampagneRepository campagneRepository;

    public CampagneResource(CampagneService campagneService, CampagneRepository campagneRepository) {
        this.campagneService = campagneService;
        this.campagneRepository = campagneRepository;
    }

    /**
     * {@code POST  /campagnes} : Create a new campagne.
     *
     * @param campagneDTO the campagneDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new campagneDTO, or with status {@code 400 (Bad Request)} if the campagne has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<CampagneDTO> createCampagne(@RequestBody CampagneDTO campagneDTO) throws URISyntaxException {
        log.debug("REST request to save Campagne : {}", campagneDTO);
        if (campagneDTO.getId() != null) {
            throw new BadRequestAlertException("A new campagne cannot already have an ID", ENTITY_NAME, "idexists");
        }
        CampagneDTO result = campagneService.save(campagneDTO);
        return ResponseEntity
            .created(new URI("/api/campagnes/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /campagnes/:id} : Updates an existing campagne.
     *
     * @param id the id of the campagneDTO to save.
     * @param campagneDTO the campagneDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated campagneDTO,
     * or with status {@code 400 (Bad Request)} if the campagneDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the campagneDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<CampagneDTO> updateCampagne(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody CampagneDTO campagneDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Campagne : {}, {}", id, campagneDTO);
        if (campagneDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, campagneDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!campagneRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        CampagneDTO result = campagneService.update(campagneDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, campagneDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /campagnes/:id} : Partial updates given fields of an existing campagne, field will ignore if it is null
     *
     * @param id the id of the campagneDTO to save.
     * @param campagneDTO the campagneDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated campagneDTO,
     * or with status {@code 400 (Bad Request)} if the campagneDTO is not valid,
     * or with status {@code 404 (Not Found)} if the campagneDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the campagneDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<CampagneDTO> partialUpdateCampagne(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody CampagneDTO campagneDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Campagne partially : {}, {}", id, campagneDTO);
        if (campagneDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, campagneDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!campagneRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<CampagneDTO> result = campagneService.partialUpdate(campagneDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, campagneDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /campagnes} : get all the campagnes.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of campagnes in body.
     */
    @GetMapping("")
    public ResponseEntity<List<CampagneDTO>> getAllCampagnes(@org.springdoc.core.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get a page of Campagnes");
        Page<CampagneDTO> page = campagneService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /campagnes/:id} : get the "id" campagne.
     *
     * @param id the id of the campagneDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the campagneDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<CampagneDTO> getCampagne(@PathVariable("id") Long id) {
        log.debug("REST request to get Campagne : {}", id);
        Optional<CampagneDTO> campagneDTO = campagneService.findOne(id);
        return ResponseUtil.wrapOrNotFound(campagneDTO);
    }

    /**
     * {@code DELETE  /campagnes/:id} : delete the "id" campagne.
     *
     * @param id the id of the campagneDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCampagne(@PathVariable("id") Long id) {
        log.debug("REST request to delete Campagne : {}", id);
        campagneService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
