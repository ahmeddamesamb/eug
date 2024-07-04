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
import sn.ugb.gateway.repository.InfoUserRessourceRepository;
import sn.ugb.gateway.service.InfoUserRessourceService;
import sn.ugb.gateway.service.dto.InfoUserRessourceDTO;
import sn.ugb.gateway.web.rest.errors.BadRequestAlertException;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.reactive.ResponseUtil;

/**
 * REST controller for managing {@link sn.ugb.gateway.domain.InfoUserRessource}.
 */
@RestController
@RequestMapping("/api/info-user-ressources")
public class InfoUserRessourceResource {

    private final Logger log = LoggerFactory.getLogger(InfoUserRessourceResource.class);

    private static final String ENTITY_NAME = "infoUserRessource";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final InfoUserRessourceService infoUserRessourceService;

    private final InfoUserRessourceRepository infoUserRessourceRepository;

    public InfoUserRessourceResource(
        InfoUserRessourceService infoUserRessourceService,
        InfoUserRessourceRepository infoUserRessourceRepository
    ) {
        this.infoUserRessourceService = infoUserRessourceService;
        this.infoUserRessourceRepository = infoUserRessourceRepository;
    }

    /**
     * {@code POST  /info-user-ressources} : Create a new infoUserRessource.
     *
     * @param infoUserRessourceDTO the infoUserRessourceDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new infoUserRessourceDTO, or with status {@code 400 (Bad Request)} if the infoUserRessource has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public Mono<ResponseEntity<InfoUserRessourceDTO>> createInfoUserRessource(
        @Valid @RequestBody InfoUserRessourceDTO infoUserRessourceDTO
    ) throws URISyntaxException {
        log.debug("REST request to save InfoUserRessource : {}", infoUserRessourceDTO);
        if (infoUserRessourceDTO.getId() != null) {
            throw new BadRequestAlertException("A new infoUserRessource cannot already have an ID", ENTITY_NAME, "idexists");
        }
        return infoUserRessourceService
            .save(infoUserRessourceDTO)
            .map(result -> {
                try {
                    return ResponseEntity
                        .created(new URI("/api/info-user-ressources/" + result.getId()))
                        .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
                        .body(result);
                } catch (URISyntaxException e) {
                    throw new RuntimeException(e);
                }
            });
    }

    /**
     * {@code PUT  /info-user-ressources/:id} : Updates an existing infoUserRessource.
     *
     * @param id the id of the infoUserRessourceDTO to save.
     * @param infoUserRessourceDTO the infoUserRessourceDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated infoUserRessourceDTO,
     * or with status {@code 400 (Bad Request)} if the infoUserRessourceDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the infoUserRessourceDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public Mono<ResponseEntity<InfoUserRessourceDTO>> updateInfoUserRessource(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody InfoUserRessourceDTO infoUserRessourceDTO
    ) throws URISyntaxException {
        log.debug("REST request to update InfoUserRessource : {}, {}", id, infoUserRessourceDTO);
        if (infoUserRessourceDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, infoUserRessourceDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return infoUserRessourceRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                return infoUserRessourceService
                    .update(infoUserRessourceDTO)
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
     * {@code PATCH  /info-user-ressources/:id} : Partial updates given fields of an existing infoUserRessource, field will ignore if it is null
     *
     * @param id the id of the infoUserRessourceDTO to save.
     * @param infoUserRessourceDTO the infoUserRessourceDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated infoUserRessourceDTO,
     * or with status {@code 400 (Bad Request)} if the infoUserRessourceDTO is not valid,
     * or with status {@code 404 (Not Found)} if the infoUserRessourceDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the infoUserRessourceDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public Mono<ResponseEntity<InfoUserRessourceDTO>> partialUpdateInfoUserRessource(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody InfoUserRessourceDTO infoUserRessourceDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update InfoUserRessource partially : {}, {}", id, infoUserRessourceDTO);
        if (infoUserRessourceDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, infoUserRessourceDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return infoUserRessourceRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                Mono<InfoUserRessourceDTO> result = infoUserRessourceService.partialUpdate(infoUserRessourceDTO);

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
     * {@code GET  /info-user-ressources} : get all the infoUserRessources.
     *
     * @param pageable the pagination information.
     * @param request a {@link ServerHttpRequest} request.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of infoUserRessources in body.
     */
    @GetMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<List<InfoUserRessourceDTO>>> getAllInfoUserRessources(
        @org.springdoc.core.annotations.ParameterObject Pageable pageable,
        ServerHttpRequest request
    ) {
        log.debug("REST request to get a page of InfoUserRessources");
        return infoUserRessourceService
            .countAll()
            .zipWith(infoUserRessourceService.findAll(pageable).collectList())
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
     * {@code GET  /info-user-ressources/:id} : get the "id" infoUserRessource.
     *
     * @param id the id of the infoUserRessourceDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the infoUserRessourceDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public Mono<ResponseEntity<InfoUserRessourceDTO>> getInfoUserRessource(@PathVariable("id") Long id) {
        log.debug("REST request to get InfoUserRessource : {}", id);
        Mono<InfoUserRessourceDTO> infoUserRessourceDTO = infoUserRessourceService.findOne(id);
        return ResponseUtil.wrapOrNotFound(infoUserRessourceDTO);
    }

    /**
     * {@code DELETE  /info-user-ressources/:id} : delete the "id" infoUserRessource.
     *
     * @param id the id of the infoUserRessourceDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Void>> deleteInfoUserRessource(@PathVariable("id") Long id) {
        log.debug("REST request to delete InfoUserRessource : {}", id);
        return infoUserRessourceService
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
     * {@code SEARCH  /info-user-ressources/_search?query=:query} : search for the infoUserRessource corresponding
     * to the query.
     *
     * @param query the query of the infoUserRessource search.
     * @param pageable the pagination information.
     * @param request a {@link ServerHttpRequest} request.
     * @return the result of the search.
     */
    @GetMapping("/_search")
    public Mono<ResponseEntity<Flux<InfoUserRessourceDTO>>> searchInfoUserRessources(
        @RequestParam("query") String query,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable,
        ServerHttpRequest request
    ) {
        log.debug("REST request to search for a page of InfoUserRessources for query {}", query);
        return infoUserRessourceService
            .searchCount()
            .map(total -> new PageImpl<>(new ArrayList<>(), pageable, total))
            .map(page ->
                PaginationUtil.generatePaginationHttpHeaders(
                    ForwardedHeaderUtils.adaptFromForwardedHeaders(request.getURI(), request.getHeaders()),
                    page
                )
            )
            .map(headers -> ResponseEntity.ok().headers(headers).body(infoUserRessourceService.search(query, pageable)));
    }

    @GetMapping("/by-infos-user/{infosUserId}")
    public Flux<InfoUserRessourceDTO> getAllInfosUserRessourceByInfosUserId(@PathVariable Long infosUserId) {
        log.debug("REST request to get all InfoUserRessource by InfosUserId : {}", infosUserId);
        return infoUserRessourceService.findAllByInfosUserId(infosUserId);
    }

    @GetMapping("/by-ressource/{ressourceId}")
    public Flux<InfoUserRessourceDTO> getAllInfosUserRessourceByRessourceId(@PathVariable Long ressourceId) {
        log.debug("REST request to get all InfoUserRessource by RessourceId : {}", ressourceId);
        return infoUserRessourceService.findAllByRessourceId(ressourceId);
    }

    @GetMapping("/by-actif/{actifYN}")
    public Flux<InfoUserRessourceDTO> getAllInfosUserRessourceByActifYN(@PathVariable Boolean actifYN) {
        log.debug("REST request to get all InfoUserRessource by ActifYN : {}", actifYN);
        return infoUserRessourceService.findAllByActifYN(actifYN);
    }

    @PutMapping("/archive/{id}")
    public Mono<ResponseEntity<Void>> archiveInfoUserRessource(@PathVariable Long id, @RequestBody Boolean enCours) {
        return infoUserRessourceService.archive(id, enCours)
            .then(Mono.just(ResponseEntity.noContent().build()));
    }
}
