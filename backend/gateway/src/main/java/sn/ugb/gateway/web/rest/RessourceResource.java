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
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.web.util.ForwardedHeaderUtils;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import sn.ugb.gateway.repository.RessourceRepository;
import sn.ugb.gateway.service.RessourceService;
import sn.ugb.gateway.service.dto.RessourceDTO;
import sn.ugb.gateway.web.rest.errors.BadRequestAlertException;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.reactive.ResponseUtil;

/**
 * REST controller for managing {@link sn.ugb.gateway.domain.Ressource}.
 */
@RestController
@RequestMapping("/api/ressources")
public class RessourceResource {

    private final Logger log = LoggerFactory.getLogger(RessourceResource.class);

    private static final String ENTITY_NAME = "ressource";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final RessourceService ressourceService;

    private final RessourceRepository ressourceRepository;

    public RessourceResource(RessourceService ressourceService, RessourceRepository ressourceRepository) {
        this.ressourceService = ressourceService;
        this.ressourceRepository = ressourceRepository;
    }

    /**
     * {@code POST  /ressources} : Create a new ressource.
     *
     * @param ressourceDTO the ressourceDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new ressourceDTO, or with status {@code 400 (Bad Request)} if the ressource has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public Mono<ResponseEntity<RessourceDTO>> createRessource(@Valid @RequestBody RessourceDTO ressourceDTO) throws URISyntaxException {
        log.debug("REST request to save Ressource : {}", ressourceDTO);
        if (ressourceDTO.getId() != null) {
            throw new BadRequestAlertException("A new ressource cannot already have an ID", ENTITY_NAME, "idexists");
        }
        return ressourceService
            .save(ressourceDTO)
            .map(result -> {
                try {
                    return ResponseEntity
                        .created(new URI("/api/ressources/" + result.getId()))
                        .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
                        .body(result);
                } catch (URISyntaxException e) {
                    throw new RuntimeException(e);
                }
            });
    }

    /**
     * {@code PUT  /ressources/:id} : Updates an existing ressource.
     *
     * @param id the id of the ressourceDTO to save.
     * @param ressourceDTO the ressourceDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated ressourceDTO,
     * or with status {@code 400 (Bad Request)} if the ressourceDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the ressourceDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public Mono<ResponseEntity<RessourceDTO>> updateRessource(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody RessourceDTO ressourceDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Ressource : {}, {}", id, ressourceDTO);
        if (ressourceDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, ressourceDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return ressourceRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                return ressourceService
                    .update(ressourceDTO)
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
     * {@code PATCH  /ressources/:id} : Partial updates given fields of an existing ressource, field will ignore if it is null
     *
     * @param id the id of the ressourceDTO to save.
     * @param ressourceDTO the ressourceDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated ressourceDTO,
     * or with status {@code 400 (Bad Request)} if the ressourceDTO is not valid,
     * or with status {@code 404 (Not Found)} if the ressourceDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the ressourceDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public Mono<ResponseEntity<RessourceDTO>> partialUpdateRessource(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody RessourceDTO ressourceDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Ressource partially : {}, {}", id, ressourceDTO);
        if (ressourceDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, ressourceDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return ressourceRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                Mono<RessourceDTO> result = ressourceService.partialUpdate(ressourceDTO);

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
     * {@code GET  /ressources} : get all the ressources.
     *
     * @param pageable the pagination information.
     * @param request a {@link ServerHttpRequest} request.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of ressources in body.
     */
    @GetMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<List<RessourceDTO>>> getAllRessources(
        @org.springdoc.core.annotations.ParameterObject Pageable pageable,
        ServerHttpRequest request
    ) {
        log.debug("REST request to get a page of Ressources");
        return ressourceService
            .countAll()
            .zipWith(ressourceService.findAll(pageable).collectList())
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
     * {@code GET  /ressources/:id} : get the "id" ressource.
     *
     * @param id the id of the ressourceDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the ressourceDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public Mono<ResponseEntity<RessourceDTO>> getRessource(@PathVariable("id") Long id) {
        log.debug("REST request to get Ressource : {}", id);
        Mono<RessourceDTO> ressourceDTO = ressourceService.findOne(id);
        return ResponseUtil.wrapOrNotFound(ressourceDTO);
    }

    /**
     * {@code DELETE  /ressources/:id} : delete the "id" ressource.
     *
     * @param id the id of the ressourceDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Void>> deleteRessource(@PathVariable("id") Long id) {
        log.debug("REST request to delete Ressource : {}", id);
        return ressourceService
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
     * {@code SEARCH  /ressources/_search?query=:query} : search for the ressource corresponding
     * to the query.
     *
     * @param query the query of the ressource search.
     * @param pageable the pagination information.
     * @param request a {@link ServerHttpRequest} request.
     * @return the result of the search.
     */
    @GetMapping("/_search")
    public Mono<ResponseEntity<Flux<RessourceDTO>>> searchRessources(
        @RequestParam("query") String query,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable,
        ServerHttpRequest request
    ) {
        log.debug("REST request to search for a page of Ressources for query {}", query);
        return ressourceService
            .searchCount()
            .map(total -> new PageImpl<>(new ArrayList<>(), pageable, total))
            .map(page ->
                PaginationUtil.generatePaginationHttpHeaders(
                    ForwardedHeaderUtils.adaptFromForwardedHeaders(request.getURI(), request.getHeaders()),
                    page
                )
            )
            .map(headers -> ResponseEntity.ok().headers(headers).body(ressourceService.search(query, pageable)));
    }

@GetMapping(value = "/blocfonctionnels/{blocfonctionnelId}", produces = MediaType.APPLICATION_JSON_VALUE)
public Mono<ResponseEntity<List<RessourceDTO>>> getAllRessourceByBlocfonctionnelId(
    @PathVariable Long blocfonctionnelId,
    @org.springdoc.core.annotations.ParameterObject Pageable pageable,
    ServerWebExchange exchange
) {
    return ressourceService
               .countByBlocfonctionnelId(blocfonctionnelId)
               .zipWith(ressourceService.findAllRessourceByBlocfonctionnel(blocfonctionnelId, pageable).collectList())
               .map(countWithEntities ->
                        ResponseEntity
                            .ok()
                            .headers(
                                PaginationUtil.generatePaginationHttpHeaders(
                                    UriComponentsBuilder.fromHttpRequest(exchange.getRequest()),
                                    new PageImpl<>(countWithEntities.getT2(), pageable, countWithEntities.getT1())
                                )
                            )
                            .body(countWithEntities.getT2())
               );
}

@GetMapping(value = "/services/{serviceId}", produces = MediaType.APPLICATION_JSON_VALUE)
public Mono<ResponseEntity<List<RessourceDTO>>> getAllRessourceByServiceId(
    @PathVariable Long serviceId,
    @org.springdoc.core.annotations.ParameterObject Pageable pageable,
    ServerWebExchange exchange
) {
    return ressourceService
               .countByServiceId(serviceId)
               .zipWith(ressourceService.findAllRessourceByService(serviceId, pageable).collectList())
               .map(countWithEntities ->
                        ResponseEntity
                            .ok()
                            .headers(
                                PaginationUtil.generatePaginationHttpHeaders(
                                    UriComponentsBuilder.fromHttpRequest(exchange.getRequest()),
                                    new PageImpl<>(countWithEntities.getT2(), pageable, countWithEntities.getT1())
                                )
                            )
                            .body(countWithEntities.getT2())
               );
}

    @PutMapping("/actifYN/{id}")
    public Mono<ResponseEntity<RessourceDTO>> setActifYNRessource(@PathVariable Long id, @RequestBody Boolean actifYN) {
        return ressourceService.setActifYNRessource(id, actifYN)
                   .map(updatedRessource -> ResponseEntity.ok().body(updatedRessource));
    }
}
