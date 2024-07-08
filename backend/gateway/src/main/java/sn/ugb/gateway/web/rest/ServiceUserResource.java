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
import sn.ugb.gateway.repository.ServiceUserRepository;
import sn.ugb.gateway.service.ServiceUserService;
import sn.ugb.gateway.service.dto.ServiceUserDTO;
import sn.ugb.gateway.web.rest.errors.BadRequestAlertException;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.reactive.ResponseUtil;

/**
 * REST controller for managing {@link sn.ugb.gateway.domain.ServiceUser}.
 */
@RestController
@RequestMapping("/api/service-users")
public class ServiceUserResource {

    private final Logger log = LoggerFactory.getLogger(ServiceUserResource.class);

    private static final String ENTITY_NAME = "serviceUser";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ServiceUserService serviceUserService;

    private final ServiceUserRepository serviceUserRepository;

    public ServiceUserResource(ServiceUserService serviceUserService, ServiceUserRepository serviceUserRepository) {
        this.serviceUserService = serviceUserService;
        this.serviceUserRepository = serviceUserRepository;
    }

    /**
     * {@code POST  /service-users} : Create a new serviceUser.
     *
     * @param serviceUserDTO the serviceUserDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new serviceUserDTO, or with status {@code 400 (Bad Request)} if the serviceUser has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public Mono<ResponseEntity<ServiceUserDTO>> createServiceUser(@Valid @RequestBody ServiceUserDTO serviceUserDTO)
        throws URISyntaxException {
        log.debug("REST request to save ServiceUser : {}", serviceUserDTO);
        if (serviceUserDTO.getId() != null) {
            throw new BadRequestAlertException("A new serviceUser cannot already have an ID", ENTITY_NAME, "idexists");
        }
        return serviceUserService
            .save(serviceUserDTO)
            .map(result -> {
                try {
                    return ResponseEntity
                        .created(new URI("/api/service-users/" + result.getId()))
                        .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
                        .body(result);
                } catch (URISyntaxException e) {
                    throw new RuntimeException(e);
                }
            });
    }

    /**
     * {@code PUT  /service-users/:id} : Updates an existing serviceUser.
     *
     * @param id the id of the serviceUserDTO to save.
     * @param serviceUserDTO the serviceUserDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated serviceUserDTO,
     * or with status {@code 400 (Bad Request)} if the serviceUserDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the serviceUserDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public Mono<ResponseEntity<ServiceUserDTO>> updateServiceUser(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody ServiceUserDTO serviceUserDTO
    ) throws URISyntaxException {
        log.debug("REST request to update ServiceUser : {}, {}", id, serviceUserDTO);
        if (serviceUserDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, serviceUserDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return serviceUserRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                return serviceUserService
                    .update(serviceUserDTO)
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
     * {@code PATCH  /service-users/:id} : Partial updates given fields of an existing serviceUser, field will ignore if it is null
     *
     * @param id the id of the serviceUserDTO to save.
     * @param serviceUserDTO the serviceUserDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated serviceUserDTO,
     * or with status {@code 400 (Bad Request)} if the serviceUserDTO is not valid,
     * or with status {@code 404 (Not Found)} if the serviceUserDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the serviceUserDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public Mono<ResponseEntity<ServiceUserDTO>> partialUpdateServiceUser(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody ServiceUserDTO serviceUserDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update ServiceUser partially : {}, {}", id, serviceUserDTO);
        if (serviceUserDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, serviceUserDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return serviceUserRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                Mono<ServiceUserDTO> result = serviceUserService.partialUpdate(serviceUserDTO);

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
     * {@code GET  /service-users} : get all the serviceUsers.
     *
     * @param pageable the pagination information.
     * @param request a {@link ServerHttpRequest} request.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of serviceUsers in body.
     */
    @GetMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<List<ServiceUserDTO>>> getAllServiceUsers(
        @org.springdoc.core.annotations.ParameterObject Pageable pageable,
        ServerHttpRequest request
    ) {
        log.debug("REST request to get a page of ServiceUsers");
        return serviceUserService
            .countAll()
            .zipWith(serviceUserService.findAll(pageable).collectList())
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
     * {@code GET  /service-users/:id} : get the "id" serviceUser.
     *
     * @param id the id of the serviceUserDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the serviceUserDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public Mono<ResponseEntity<ServiceUserDTO>> getServiceUser(@PathVariable("id") Long id) {
        log.debug("REST request to get ServiceUser : {}", id);
        Mono<ServiceUserDTO> serviceUserDTO = serviceUserService.findOne(id);
        return ResponseUtil.wrapOrNotFound(serviceUserDTO);
    }

    /**
     * {@code POST  /service-users/:id/actifYN} : set the "actifYN" status for the serviceUser.
     *
     * @param id the id of the serviceUserDTO to update.
     * @param actifYN the new actifYN status.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated serviceUserDTO, or with status {@code 404 (Not Found)}.
     */
    @PostMapping("/{id}/actifYN")
    public Mono<ResponseEntity<ServiceUserDTO>> setActifYNServiceUser(@PathVariable Long id, @RequestParam Boolean actifYN) {
        log.debug("REST request to set actifYN for ServiceUser : {} to {}", id, actifYN);
        Mono<ServiceUserDTO> serviceUserDTO = serviceUserService.setServiceUserActifYN(id, actifYN);
        return ResponseUtil.wrapOrNotFound(serviceUserDTO);
    }


    /**
     * {@code DELETE  /service-users/:id} : delete the "id" serviceUser.
     *
     * @param id the id of the serviceUserDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Void>> deleteServiceUser(@PathVariable("id") Long id) {
        log.debug("REST request to delete ServiceUser : {}", id);
        return serviceUserService
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
     * {@code SEARCH  /service-users/_search?query=:query} : search for the serviceUser corresponding
     * to the query.
     *
     * @param query the query of the serviceUser search.
     * @param pageable the pagination information.
     * @param request a {@link ServerHttpRequest} request.
     * @return the result of the search.
     */
    @GetMapping("/_search")
    public Mono<ResponseEntity<Flux<ServiceUserDTO>>> searchServiceUsers(
        @RequestParam("query") String query,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable,
        ServerHttpRequest request
    ) {
        log.debug("REST request to search for a page of ServiceUsers for query {}", query);
        return serviceUserService
            .searchCount()
            .map(total -> new PageImpl<>(new ArrayList<>(), pageable, total))
            .map(page ->
                PaginationUtil.generatePaginationHttpHeaders(
                    ForwardedHeaderUtils.adaptFromForwardedHeaders(request.getURI(), request.getHeaders()),
                    page
                )
            )
            .map(headers -> ResponseEntity.ok().headers(headers).body(serviceUserService.search(query, pageable)));
    }
}
