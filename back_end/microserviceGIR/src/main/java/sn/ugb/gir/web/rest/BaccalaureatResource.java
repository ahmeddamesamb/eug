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
import sn.ugb.gir.repository.BaccalaureatRepository;
import sn.ugb.gir.service.BaccalaureatService;
import sn.ugb.gir.service.dto.BaccalaureatDTO;
import sn.ugb.gir.web.rest.errors.BadRequestAlertException;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link sn.ugb.gir.domain.Baccalaureat}.
 */
@RestController
@RequestMapping("/api/baccalaureats")
public class BaccalaureatResource {

    private final Logger log = LoggerFactory.getLogger(BaccalaureatResource.class);

    private static final String ENTITY_NAME = "microserviceGirBaccalaureat";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final BaccalaureatService baccalaureatService;

    private final BaccalaureatRepository baccalaureatRepository;

    public BaccalaureatResource(BaccalaureatService baccalaureatService, BaccalaureatRepository baccalaureatRepository) {
        this.baccalaureatService = baccalaureatService;
        this.baccalaureatRepository = baccalaureatRepository;
    }

    /**
     * {@code POST  /baccalaureats} : Create a new baccalaureat.
     *
     * @param baccalaureatDTO the baccalaureatDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new baccalaureatDTO, or with status {@code 400 (Bad Request)} if the baccalaureat has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<BaccalaureatDTO> createBaccalaureat(@RequestBody BaccalaureatDTO baccalaureatDTO) throws URISyntaxException {
        log.debug("REST request to save Baccalaureat : {}", baccalaureatDTO);
        if (baccalaureatDTO.getId() != null) {
            throw new BadRequestAlertException("A new baccalaureat cannot already have an ID", ENTITY_NAME, "idexists");
        }
        BaccalaureatDTO result = baccalaureatService.save(baccalaureatDTO);
        return ResponseEntity
            .created(new URI("/api/baccalaureats/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /baccalaureats/:id} : Updates an existing baccalaureat.
     *
     * @param id the id of the baccalaureatDTO to save.
     * @param baccalaureatDTO the baccalaureatDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated baccalaureatDTO,
     * or with status {@code 400 (Bad Request)} if the baccalaureatDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the baccalaureatDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<BaccalaureatDTO> updateBaccalaureat(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody BaccalaureatDTO baccalaureatDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Baccalaureat : {}, {}", id, baccalaureatDTO);
        if (baccalaureatDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, baccalaureatDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!baccalaureatRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        BaccalaureatDTO result = baccalaureatService.update(baccalaureatDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, baccalaureatDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /baccalaureats/:id} : Partial updates given fields of an existing baccalaureat, field will ignore if it is null
     *
     * @param id the id of the baccalaureatDTO to save.
     * @param baccalaureatDTO the baccalaureatDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated baccalaureatDTO,
     * or with status {@code 400 (Bad Request)} if the baccalaureatDTO is not valid,
     * or with status {@code 404 (Not Found)} if the baccalaureatDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the baccalaureatDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<BaccalaureatDTO> partialUpdateBaccalaureat(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody BaccalaureatDTO baccalaureatDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Baccalaureat partially : {}, {}", id, baccalaureatDTO);
        if (baccalaureatDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, baccalaureatDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!baccalaureatRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<BaccalaureatDTO> result = baccalaureatService.partialUpdate(baccalaureatDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, baccalaureatDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /baccalaureats} : get all the baccalaureats.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of baccalaureats in body.
     */
    @GetMapping("")
    public ResponseEntity<List<BaccalaureatDTO>> getAllBaccalaureats(@org.springdoc.core.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get a page of Baccalaureats");
        Page<BaccalaureatDTO> page = baccalaureatService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /baccalaureats/:id} : get the "id" baccalaureat.
     *
     * @param id the id of the baccalaureatDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the baccalaureatDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<BaccalaureatDTO> getBaccalaureat(@PathVariable("id") Long id) {
        log.debug("REST request to get Baccalaureat : {}", id);
        Optional<BaccalaureatDTO> baccalaureatDTO = baccalaureatService.findOne(id);
        return ResponseUtil.wrapOrNotFound(baccalaureatDTO);
    }

    /**
     * {@code DELETE  /baccalaureats/:id} : delete the "id" baccalaureat.
     *
     * @param id the id of the baccalaureatDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBaccalaureat(@PathVariable("id") Long id) {
        log.debug("REST request to delete Baccalaureat : {}", id);
        baccalaureatService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
