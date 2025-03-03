package sn.ugb.gir.web.rest;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import sn.ugb.gir.repository.RegionRepository;
import sn.ugb.gir.service.RegionService;
import sn.ugb.gir.service.dto.RegionDTO;
import sn.ugb.gir.web.rest.errors.BadRequestAlertException;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * REST controller for managing {@link sn.ugb.gir.domain.Region}.
 */
@RestController
@RequestMapping("/api/regions")
public class RegionResource {

private static final String ENTITY_NAME = "microserviceGirRegion";
private final Logger log = LoggerFactory.getLogger(RegionResource.class);
private final RegionService regionService;
private final RegionRepository regionRepository;
@Value("${jhipster.clientApp.name}")
private String applicationName;

public RegionResource(RegionService regionService, RegionRepository regionRepository) {
    this.regionService = regionService;
    this.regionRepository = regionRepository;
}

/**
 * {@code POST  /regions} : Create a new region.
 *
 * @param regionDTO the regionDTO to create.
 * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new regionDTO, or with status {@code 400 (Bad Request)} if the region has already an ID.
 * @throws URISyntaxException if the Location URI syntax is incorrect.
 */
@PostMapping("")
public ResponseEntity<RegionDTO> createRegion(@Valid @RequestBody RegionDTO regionDTO) throws URISyntaxException {
    log.debug("REST request to save Region : {}", regionDTO);
    if (regionDTO.getId() != null) {
        throw new BadRequestAlertException("A new region cannot already have an ID", ENTITY_NAME, "idexists");
    }
    RegionDTO result = regionService.save(regionDTO);
    return ResponseEntity
               .created(new URI("/api/regions/" + result.getId()))
               .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
               .body(result);
}

/**
 * {@code PUT  /regions/:id} : Updates an existing region.
 *
 * @param id        the id of the regionDTO to save.
 * @param regionDTO the regionDTO to update.
 * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated regionDTO,
 * or with status {@code 400 (Bad Request)} if the regionDTO is not valid,
 * or with status {@code 500 (Internal Server Error)} if the regionDTO couldn't be updated.
 * @throws URISyntaxException if the Location URI syntax is incorrect.
 */
@PutMapping("/{id}")
public ResponseEntity<RegionDTO> updateRegion(
    @PathVariable(value = "id", required = false) final Long id,
    @Valid @RequestBody RegionDTO regionDTO
) throws URISyntaxException {
    log.debug("REST request to update Region : {}, {}", id, regionDTO);
    if (regionDTO.getId() == null) {
        throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
    }
    if (!Objects.equals(id, regionDTO.getId())) {
        throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
    }

    if (!regionRepository.existsById(id)) {
        throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
    }

    RegionDTO result = regionService.update(regionDTO);
    return ResponseEntity
               .ok()
               .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, regionDTO.getId().toString()))
               .body(result);
}

/**
 * {@code PATCH  /regions/:id} : Partial updates given fields of an existing region, field will ignore if it is null
 *
 * @param id        the id of the regionDTO to save.
 * @param regionDTO the regionDTO to update.
 * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated regionDTO,
 * or with status {@code 400 (Bad Request)} if the regionDTO is not valid,
 * or with status {@code 404 (Not Found)} if the regionDTO is not found,
 * or with status {@code 500 (Internal Server Error)} if the regionDTO couldn't be updated.
 * @throws URISyntaxException if the Location URI syntax is incorrect.
 */
@PatchMapping(value = "/{id}", consumes = {"application/json", "application/merge-patch+json"})
public ResponseEntity<RegionDTO> partialUpdateRegion(
    @PathVariable(value = "id", required = false) final Long id,
    @NotNull @RequestBody RegionDTO regionDTO
) throws URISyntaxException {
    log.debug("REST request to partial update Region partially : {}, {}", id, regionDTO);
    if (regionDTO.getId() == null) {
        throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
    }
    if (!Objects.equals(id, regionDTO.getId())) {
        throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
    }

    if (!regionRepository.existsById(id)) {
        throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
    }

    Optional<RegionDTO> result = regionService.partialUpdate(regionDTO);

    return ResponseUtil.wrapOrNotFound(
        result,
        HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, regionDTO.getId().toString())
    );
}

/**
 * {@code GET  /regions} : get all the regions.
 *
 * @param pageable the pagination information.
 * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of regions in body.
 */
@GetMapping("")
public ResponseEntity<List<RegionDTO>> getAllRegions(@org.springdoc.core.annotations.ParameterObject Pageable pageable) {
    log.debug("REST request to get a page of Regions");
    Page<RegionDTO> page = regionService.findAll(pageable);
    HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
    return ResponseEntity.ok().headers(headers).body(page.getContent());
}

/**
 * {@code GET  /regions/:id} : get the "id" region.
 *
 * @param id the id of the regionDTO to retrieve.
 * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the regionDTO, or with status {@code 404 (Not Found)}.
 */
@GetMapping("/{id}")
public ResponseEntity<RegionDTO> getRegion(@PathVariable("id") Long id) {
    log.debug("REST request to get Region : {}", id);
    Optional<RegionDTO> regionDTO = regionService.findOne(id);
    return ResponseUtil.wrapOrNotFound(regionDTO);
}

/**
 * {@code DELETE  /regions/:id} : delete the "id" region.
 *
 * @param id the id of the regionDTO to delete.
 * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
 */
@DeleteMapping("/{id}")
public ResponseEntity<Void> deleteRegion(@PathVariable("id") Long id) {
    log.debug("REST request to delete Region : {}", id);
    regionService.delete(id);
    return ResponseEntity
               .noContent()
               .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
               .build();
}

@GetMapping("/pays/{paysId}")
public ResponseEntity<List<RegionDTO>> getAllRegionByPays(@PathVariable Long paysId, @org.springdoc.core.annotations.ParameterObject Pageable pageable) {
    log.debug("REST request to get a page of Regions by Pays ID : {}", paysId);
    Page<RegionDTO> page = regionService.findAllRegionByPays(paysId, pageable);
    HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
    return ResponseEntity.ok().headers(headers).body(page.getContent());
}


}
