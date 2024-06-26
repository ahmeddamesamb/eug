package sn.ugb.gateway.web.rest;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.util.ForwardedHeaderUtils;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import sn.ugb.gateway.repository.BlocFonctionnelRepository;
import sn.ugb.gateway.service.BlocFonctionnelService;
import sn.ugb.gateway.service.dto.BlocFonctionnelDTO;
import sn.ugb.gateway.web.rest.errors.BadRequestAlertException;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.reactive.ResponseUtil;

/**
 * REST controller for managing {@link sn.ugb.gateway.domain.BlocFonctionnel}.
 */
@RestController
@RequestMapping("/api/bloc-fonctionnels")
public class BlocFonctionnelResource {

    private final Logger log = LoggerFactory.getLogger(BlocFonctionnelResource.class);

    private static final String ENTITY_NAME = "blocFonctionnel";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final BlocFonctionnelService blocFonctionnelService;

    private final BlocFonctionnelRepository blocFonctionnelRepository;

    public BlocFonctionnelResource(BlocFonctionnelService blocFonctionnelService, BlocFonctionnelRepository blocFonctionnelRepository) {
        this.blocFonctionnelService = blocFonctionnelService;
        this.blocFonctionnelRepository = blocFonctionnelRepository;
    }

    /**
     * {@code POST  /bloc-fonctionnels} : Create a new blocFonctionnel.
     *
     * @param blocFonctionnelDTO the blocFonctionnelDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new blocFonctionnelDTO, or with status {@code 400 (Bad Request)} if the blocFonctionnel has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public Mono<ResponseEntity<BlocFonctionnelDTO>> createBlocFonctionnel(@Valid @RequestBody BlocFonctionnelDTO blocFonctionnelDTO)
        throws URISyntaxException {
        log.debug("REST request to save BlocFonctionnel : {}", blocFonctionnelDTO);
        if (blocFonctionnelDTO.getId() != null) {
            throw new BadRequestAlertException("A new blocFonctionnel cannot already have an ID", ENTITY_NAME, "idexists");
        }
        return blocFonctionnelService
            .save(blocFonctionnelDTO)
            .map(result -> {
                try {
                    return ResponseEntity
                        .created(new URI("/api/bloc-fonctionnels/" + result.getId()))
                        .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
                        .body(result);
                } catch (URISyntaxException e) {
                    throw new RuntimeException(e);
                }
            });
    }

    /**
     * {@code PUT  /bloc-fonctionnels/:id} : Updates an existing blocFonctionnel.
     *
     * @param id the id of the blocFonctionnelDTO to save.
     * @param blocFonctionnelDTO the blocFonctionnelDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated blocFonctionnelDTO,
     * or with status {@code 400 (Bad Request)} if the blocFonctionnelDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the blocFonctionnelDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public Mono<ResponseEntity<BlocFonctionnelDTO>> updateBlocFonctionnel(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody BlocFonctionnelDTO blocFonctionnelDTO
    ) throws URISyntaxException {
        log.debug("REST request to update BlocFonctionnel : {}, {}", id, blocFonctionnelDTO);
        if (blocFonctionnelDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, blocFonctionnelDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return blocFonctionnelRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                return blocFonctionnelService
                    .update(blocFonctionnelDTO)
                    .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND)))
                    .map(result ->
                        ResponseEntity
                            .ok()
                            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
                            .body(result)
                    );
            });
    }

    /**
     * {@code PATCH  /bloc-fonctionnels/:id} : Partial updates given fields of an existing blocFonctionnel, field will ignore if it is null
     *
     * @param id the id of the blocFonctionnelDTO to save.
     * @param blocFonctionnelDTO the blocFonctionnelDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated blocFonctionnelDTO,
     * or with status {@code 400 (Bad Request)} if the blocFonctionnelDTO is not valid,
     * or with status {@code 404 (Not Found)} if the blocFonctionnelDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the blocFonctionnelDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public Mono<ResponseEntity<BlocFonctionnelDTO>> partialUpdateBlocFonctionnel(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody BlocFonctionnelDTO blocFonctionnelDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update BlocFonctionnel partially : {}, {}", id, blocFonctionnelDTO);
        if (blocFonctionnelDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, blocFonctionnelDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return blocFonctionnelRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                Mono<BlocFonctionnelDTO> result = blocFonctionnelService.partialUpdate(blocFonctionnelDTO);

                return result
                    .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND)))
                    .map(res ->
                        ResponseEntity
                            .ok()
                            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, res.getId().toString()))
                            .body(res)
                    );
            });
    }

    /**
     * {@code GET  /bloc-fonctionnels} : get all the blocFonctionnels.
     *
     * @param pageable the pagination information.
     * @param request a {@link ServerHttpRequest} request.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of blocFonctionnels in body.
     */
    @GetMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<List<BlocFonctionnelDTO>>> getAllBlocFonctionnels(
        @org.springdoc.core.annotations.ParameterObject Pageable pageable,
        ServerHttpRequest request
    ) {
        log.debug("REST request to get a page of BlocFonctionnels");
        return blocFonctionnelService
            .countAll()
            .zipWith(blocFonctionnelService.findAll(pageable).collectList())
            .map(countWithEntities ->
                ResponseEntity
                    .ok()
                    .headers(
                        PaginationUtil.generatePaginationHttpHeaders(
                            ForwardedHeaderUtils.adaptFromForwardedHeaders(request.getURI(), request.getHeaders()),
                            new PageImpl<>(countWithEntities.getT2(), pageable, countWithEntities.getT1())
                        )
                    )
                    .body(countWithEntities.getT2())
            );
    }

    /**
     * {@code GET  /bloc-fonctionnels/:id} : get the "id" blocFonctionnel.
     *
     * @param id the id of the blocFonctionnelDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the blocFonctionnelDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public Mono<ResponseEntity<BlocFonctionnelDTO>> getBlocFonctionnel(@PathVariable("id") Long id) {
        log.debug("REST request to get BlocFonctionnel : {}", id);
        Mono<BlocFonctionnelDTO> blocFonctionnelDTO = blocFonctionnelService.findOne(id);
        return ResponseUtil.wrapOrNotFound(blocFonctionnelDTO);
    }

    /**
     * {@code DELETE  /bloc-fonctionnels/:id} : delete the "id" blocFonctionnel.
     *
     * @param id the id of the blocFonctionnelDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Void>> deleteBlocFonctionnel(@PathVariable("id") Long id) {
        log.debug("REST request to delete BlocFonctionnel : {}", id);
        return blocFonctionnelService
            .delete(id)
            .then(
                Mono.just(
                    ResponseEntity
                        .noContent()
                        .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
                        .build()
                )
            );
    }

    /**
     * {@code SEARCH  /bloc-fonctionnels/_search?query=:query} : search for the blocFonctionnel corresponding
     * to the query.
     *
     * @param query the query of the blocFonctionnel search.
     * @param pageable the pagination information.
     * @param request a {@link ServerHttpRequest} request.
     * @return the result of the search.
     */
    @GetMapping("/_search")
    public Mono<ResponseEntity<Flux<BlocFonctionnelDTO>>> searchBlocFonctionnels(
        @RequestParam("query") String query,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable,
        ServerHttpRequest request
    ) {
        log.debug("REST request to search for a page of BlocFonctionnels for query {}", query);
        return blocFonctionnelService
            .searchCount()
            .map(total -> new PageImpl<>(new ArrayList<>(), pageable, total))
            .map(page ->
                PaginationUtil.generatePaginationHttpHeaders(
                    ForwardedHeaderUtils.adaptFromForwardedHeaders(request.getURI(), request.getHeaders()),
                    page
                )
            )
            .map(headers -> ResponseEntity.ok().headers(headers).body(blocFonctionnelService.search(query, pageable)));
    }
}
