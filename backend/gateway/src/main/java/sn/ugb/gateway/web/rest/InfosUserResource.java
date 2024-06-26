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
import sn.ugb.gateway.repository.InfosUserRepository;
import sn.ugb.gateway.service.InfosUserService;
import sn.ugb.gateway.service.dto.InfosUserDTO;
import sn.ugb.gateway.web.rest.errors.BadRequestAlertException;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.reactive.ResponseUtil;

/**
 * REST controller for managing {@link sn.ugb.gateway.domain.InfosUser}.
 */
@RestController
@RequestMapping("/api/infos-users")
public class InfosUserResource {

    private final Logger log = LoggerFactory.getLogger(InfosUserResource.class);

    private static final String ENTITY_NAME = "infosUser";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final InfosUserService infosUserService;

    private final InfosUserRepository infosUserRepository;

    public InfosUserResource(InfosUserService infosUserService, InfosUserRepository infosUserRepository) {
        this.infosUserService = infosUserService;
        this.infosUserRepository = infosUserRepository;
    }

    /**
     * {@code POST  /infos-users} : Create a new infosUser.
     *
     * @param infosUserDTO the infosUserDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new infosUserDTO, or with status {@code 400 (Bad Request)} if the infosUser has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public Mono<ResponseEntity<InfosUserDTO>> createInfosUser(@Valid @RequestBody InfosUserDTO infosUserDTO) throws URISyntaxException {
        log.debug("REST request to save InfosUser : {}", infosUserDTO);
        if (infosUserDTO.getId() != null) {
            throw new BadRequestAlertException("A new infosUser cannot already have an ID", ENTITY_NAME, "idexists");
        }
        return infosUserService
            .save(infosUserDTO)
            .map(result -> {
                try {
                    return ResponseEntity
                        .created(new URI("/api/infos-users/" + result.getId()))
                        .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
                        .body(result);
                } catch (URISyntaxException e) {
                    throw new RuntimeException(e);
                }
            });
    }

    /**
     * {@code PUT  /infos-users/:id} : Updates an existing infosUser.
     *
     * @param id the id of the infosUserDTO to save.
     * @param infosUserDTO the infosUserDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated infosUserDTO,
     * or with status {@code 400 (Bad Request)} if the infosUserDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the infosUserDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public Mono<ResponseEntity<InfosUserDTO>> updateInfosUser(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody InfosUserDTO infosUserDTO
    ) throws URISyntaxException {
        log.debug("REST request to update InfosUser : {}, {}", id, infosUserDTO);
        if (infosUserDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, infosUserDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return infosUserRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                return infosUserService
                    .update(infosUserDTO)
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
     * {@code PATCH  /infos-users/:id} : Partial updates given fields of an existing infosUser, field will ignore if it is null
     *
     * @param id the id of the infosUserDTO to save.
     * @param infosUserDTO the infosUserDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated infosUserDTO,
     * or with status {@code 400 (Bad Request)} if the infosUserDTO is not valid,
     * or with status {@code 404 (Not Found)} if the infosUserDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the infosUserDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public Mono<ResponseEntity<InfosUserDTO>> partialUpdateInfosUser(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody InfosUserDTO infosUserDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update InfosUser partially : {}, {}", id, infosUserDTO);
        if (infosUserDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, infosUserDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return infosUserRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                Mono<InfosUserDTO> result = infosUserService.partialUpdate(infosUserDTO);

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
     * {@code GET  /infos-users} : get all the infosUsers.
     *
     * @param pageable the pagination information.
     * @param request a {@link ServerHttpRequest} request.
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of infosUsers in body.
     */
    @GetMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<List<InfosUserDTO>>> getAllInfosUsers(
        @org.springdoc.core.annotations.ParameterObject Pageable pageable,
        ServerHttpRequest request,
        @RequestParam(name = "eagerload", required = false, defaultValue = "true") boolean eagerload
    ) {
        log.debug("REST request to get a page of InfosUsers");
        return infosUserService
            .countAll()
            .zipWith(infosUserService.findAll(pageable).collectList())
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
     * {@code GET  /infos-users/:id} : get the "id" infosUser.
     *
     * @param id the id of the infosUserDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the infosUserDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public Mono<ResponseEntity<InfosUserDTO>> getInfosUser(@PathVariable("id") Long id) {
        log.debug("REST request to get InfosUser : {}", id);
        Mono<InfosUserDTO> infosUserDTO = infosUserService.findOne(id);
        return ResponseUtil.wrapOrNotFound(infosUserDTO);
    }

    /**
     * {@code DELETE  /infos-users/:id} : delete the "id" infosUser.
     *
     * @param id the id of the infosUserDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Void>> deleteInfosUser(@PathVariable("id") Long id) {
        log.debug("REST request to delete InfosUser : {}", id);
        return infosUserService
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
     * {@code SEARCH  /infos-users/_search?query=:query} : search for the infosUser corresponding
     * to the query.
     *
     * @param query the query of the infosUser search.
     * @param pageable the pagination information.
     * @param request a {@link ServerHttpRequest} request.
     * @return the result of the search.
     */
    @GetMapping("/_search")
    public Mono<ResponseEntity<Flux<InfosUserDTO>>> searchInfosUsers(
        @RequestParam("query") String query,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable,
        ServerHttpRequest request
    ) {
        log.debug("REST request to search for a page of InfosUsers for query {}", query);
        return infosUserService
            .searchCount()
            .map(total -> new PageImpl<>(new ArrayList<>(), pageable, total))
            .map(page ->
                PaginationUtil.generatePaginationHttpHeaders(
                    ForwardedHeaderUtils.adaptFromForwardedHeaders(request.getURI(), request.getHeaders()),
                    page
                )
            )
            .map(headers -> ResponseEntity.ok().headers(headers).body(infosUserService.search(query, pageable)));
    }
}
