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
import sn.ugb.gateway.repository.UserProfileBlocFonctionnelRepository;
import sn.ugb.gateway.service.UserProfileBlocFonctionnelService;
import sn.ugb.gateway.service.dto.ServiceUserDTO;
import sn.ugb.gateway.service.dto.UserProfileBlocFonctionnelDTO;
import sn.ugb.gateway.web.rest.errors.BadRequestAlertException;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.reactive.ResponseUtil;

/**
 * REST controller for managing {@link sn.ugb.gateway.domain.UserProfileBlocFonctionnel}.
 */
@RestController
@RequestMapping("/api/user-profile-bloc-fonctionnels")
public class UserProfileBlocFonctionnelResource {

    private final Logger log = LoggerFactory.getLogger(UserProfileBlocFonctionnelResource.class);

    private static final String ENTITY_NAME = "userProfileBlocFonctionnel";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final UserProfileBlocFonctionnelService userProfileBlocFonctionnelService;

    private final UserProfileBlocFonctionnelRepository userProfileBlocFonctionnelRepository;

    public UserProfileBlocFonctionnelResource(
        UserProfileBlocFonctionnelService userProfileBlocFonctionnelService,
        UserProfileBlocFonctionnelRepository userProfileBlocFonctionnelRepository
    ) {
        this.userProfileBlocFonctionnelService = userProfileBlocFonctionnelService;
        this.userProfileBlocFonctionnelRepository = userProfileBlocFonctionnelRepository;
    }

    /**
     * {@code POST  /user-profile-bloc-fonctionnels} : Create a new userProfileBlocFonctionnel.
     *
     * @param userProfileBlocFonctionnelDTO the userProfileBlocFonctionnelDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new userProfileBlocFonctionnelDTO, or with status {@code 400 (Bad Request)} if the userProfileBlocFonctionnel has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public Mono<ResponseEntity<UserProfileBlocFonctionnelDTO>> createUserProfileBlocFonctionnel(
        @Valid @RequestBody UserProfileBlocFonctionnelDTO userProfileBlocFonctionnelDTO
    ) throws URISyntaxException {
        log.debug("REST request to save UserProfileBlocFonctionnel : {}", userProfileBlocFonctionnelDTO);
        if (userProfileBlocFonctionnelDTO.getId() != null) {
            throw new BadRequestAlertException("A new userProfileBlocFonctionnel cannot already have an ID", ENTITY_NAME, "idexists");
        }
        return userProfileBlocFonctionnelService
            .save(userProfileBlocFonctionnelDTO)
            .map(result -> {
                try {
                    return ResponseEntity
                        .created(new URI("/api/user-profile-bloc-fonctionnels/" + result.getId()))
                        .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
                        .body(result);
                } catch (URISyntaxException e) {
                    throw new RuntimeException(e);
                }
            });
    }

    /**
     * {@code PUT  /user-profile-bloc-fonctionnels/:id} : Updates an existing userProfileBlocFonctionnel.
     *
     * @param id the id of the userProfileBlocFonctionnelDTO to save.
     * @param userProfileBlocFonctionnelDTO the userProfileBlocFonctionnelDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated userProfileBlocFonctionnelDTO,
     * or with status {@code 400 (Bad Request)} if the userProfileBlocFonctionnelDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the userProfileBlocFonctionnelDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public Mono<ResponseEntity<UserProfileBlocFonctionnelDTO>> updateUserProfileBlocFonctionnel(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody UserProfileBlocFonctionnelDTO userProfileBlocFonctionnelDTO
    ) throws URISyntaxException {
        log.debug("REST request to update UserProfileBlocFonctionnel : {}, {}", id, userProfileBlocFonctionnelDTO);
        if (userProfileBlocFonctionnelDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, userProfileBlocFonctionnelDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return userProfileBlocFonctionnelRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                return userProfileBlocFonctionnelService
                    .update(userProfileBlocFonctionnelDTO)
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
     * {@code PATCH  /user-profile-bloc-fonctionnels/:id} : Partial updates given fields of an existing userProfileBlocFonctionnel, field will ignore if it is null
     *
     * @param id the id of the userProfileBlocFonctionnelDTO to save.
     * @param userProfileBlocFonctionnelDTO the userProfileBlocFonctionnelDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated userProfileBlocFonctionnelDTO,
     * or with status {@code 400 (Bad Request)} if the userProfileBlocFonctionnelDTO is not valid,
     * or with status {@code 404 (Not Found)} if the userProfileBlocFonctionnelDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the userProfileBlocFonctionnelDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public Mono<ResponseEntity<UserProfileBlocFonctionnelDTO>> partialUpdateUserProfileBlocFonctionnel(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody UserProfileBlocFonctionnelDTO userProfileBlocFonctionnelDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update UserProfileBlocFonctionnel partially : {}, {}", id, userProfileBlocFonctionnelDTO);
        if (userProfileBlocFonctionnelDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, userProfileBlocFonctionnelDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return userProfileBlocFonctionnelRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                Mono<UserProfileBlocFonctionnelDTO> result = userProfileBlocFonctionnelService.partialUpdate(userProfileBlocFonctionnelDTO);

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
     * {@code GET  /user-profile-bloc-fonctionnels} : get all the userProfileBlocFonctionnels.
     *
     * @param pageable the pagination information.
     * @param request a {@link ServerHttpRequest} request.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of userProfileBlocFonctionnels in body.
     */
    @GetMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<List<UserProfileBlocFonctionnelDTO>>> getAllUserProfileBlocFonctionnels(
        @org.springdoc.core.annotations.ParameterObject Pageable pageable,
        ServerHttpRequest request
    ) {
        log.debug("REST request to get a page of UserProfileBlocFonctionnels");
        return userProfileBlocFonctionnelService
            .countAll()
            .zipWith(userProfileBlocFonctionnelService.findAll(pageable).collectList())
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
     * {@code GET  /user-profile-bloc-fonctionnels/:id} : get the "id" userProfileBlocFonctionnel.
     *
     * @param id the id of the userProfileBlocFonctionnelDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the userProfileBlocFonctionnelDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public Mono<ResponseEntity<UserProfileBlocFonctionnelDTO>> getUserProfileBlocFonctionnel(@PathVariable("id") Long id) {
        log.debug("REST request to get UserProfileBlocFonctionnel : {}", id);
        Mono<UserProfileBlocFonctionnelDTO> userProfileBlocFonctionnelDTO = userProfileBlocFonctionnelService.findOne(id);
        return ResponseUtil.wrapOrNotFound(userProfileBlocFonctionnelDTO);
    }

    /**
     * {@code DELETE  /user-profile-bloc-fonctionnels/:id} : delete the "id" userProfileBlocFonctionnel.
     *
     * @param id the id of the userProfileBlocFonctionnelDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Void>> deleteUserProfileBlocFonctionnel(@PathVariable("id") Long id) {
        log.debug("REST request to delete UserProfileBlocFonctionnel : {}", id);
        return userProfileBlocFonctionnelService
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
     * {@code SEARCH  /user-profile-bloc-fonctionnels/_search?query=:query} : search for the userProfileBlocFonctionnel corresponding
     * to the query.
     *
     * @param query the query of the userProfileBlocFonctionnel search.
     * @param pageable the pagination information.
     * @param request a {@link ServerHttpRequest} request.
     * @return the result of the search.
     */
    @GetMapping("/_search")
    public Mono<ResponseEntity<Flux<UserProfileBlocFonctionnelDTO>>> searchUserProfileBlocFonctionnels(
        @RequestParam("query") String query,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable,
        ServerHttpRequest request
    ) {
        log.debug("REST request to search for a page of UserProfileBlocFonctionnels for query {}", query);
        return userProfileBlocFonctionnelService
            .searchCount()
            .map(total -> new PageImpl<>(new ArrayList<>(), pageable, total))
            .map(page ->
                PaginationUtil.generatePaginationHttpHeaders(
                    ForwardedHeaderUtils.adaptFromForwardedHeaders(request.getURI(), request.getHeaders()),
                    page
                )
            )
            .map(headers -> ResponseEntity.ok().headers(headers).body(userProfileBlocFonctionnelService.search(query, pageable)));
    }

    /**
     * {@code GET  /user-profile-bloc-fonctionnels/user-profiles/:userProfileId} : get all the userProfileBlocFonctionnels by userProfileId.
     *
     * @param userProfileId the userProfileId of the UserProfileBlocFonctionnelDTO to retrieve.
     * @param pageable the pagination information.
     * @param request a {@link ServerHttpRequest} request.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of userProfileBlocFonctionnels in body.
     */
    @GetMapping(value = "/user-profiles/{userProfileId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<List<UserProfileBlocFonctionnelDTO>>> getAllUserProfilBlocFonctionnelsByUserProfilId(
        @PathVariable Long userProfileId,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable,
        ServerHttpRequest request
    ) {
        log.debug("REST request to get a page of UserProfileBlocFonctionnels by userProfileId : {}", userProfileId);
        return userProfileBlocFonctionnelService
            .countByUserProfileId(userProfileId)
            .zipWith(userProfileBlocFonctionnelService.getAllUserProfilBlocFonctionnelByUserProfilId(userProfileId, pageable).collectList())
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
     * {@code GET  /user-profile-bloc-fonctionnels/bloc-fonctionnels/:blocFonctionnelId} : get all the userProfileBlocFonctionnels by blocFonctionnelId.
     *
     * @param blocFonctionnelId the blocFonctionnelId of the UserProfileBlocFonctionnelDTO to retrieve.
     * @param pageable the pagination information.
     * @param request a {@link ServerHttpRequest} request.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of userProfileBlocFonctionnels in body.
     */
    @GetMapping(value = "/bloc-fonctionnels/{blocFonctionnelId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<List<UserProfileBlocFonctionnelDTO>>> getAllUserProfileBlocFonctionnelsByBlocFonctionnelId(
        @PathVariable Long blocFonctionnelId,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable,
        ServerHttpRequest request
    ) {
        log.debug("REST request to get a page of UserProfileBlocFonctionnels by blocFonctionnelId : {}", blocFonctionnelId);
        return userProfileBlocFonctionnelService
            .countByBlocFonctionnelId(blocFonctionnelId)
            .zipWith(userProfileBlocFonctionnelService.findAllByBlocFonctionnelId(blocFonctionnelId, pageable).collectList())
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
     * {@code GET  /user-profile-bloc-fonctionnels/en-cours-yn/:enCoursYN} : get all the userProfileBlocFonctionnels by enCoursYN.
     *
     * @param enCoursYN the enCoursYN of the UserProfileBlocFonctionnelDTO to retrieve.
     * @param pageable the pagination information.
     * @param request a {@link ServerHttpRequest} request.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of userProfileBlocFonctionnels in body.
     */
    @GetMapping(value = "/en-cours-yn/{enCoursYN}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<List<UserProfileBlocFonctionnelDTO>>> getAllUserProfileBlocFonctionnelsByEnCoursYN(
        @PathVariable Boolean enCoursYN,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable,
        ServerHttpRequest request
    ) {
        log.debug("REST request to get a page of UserProfileBlocFonctionnels by enCoursYN : {}", enCoursYN);
        return userProfileBlocFonctionnelService
            .countByEnCoursYN(enCoursYN)
            .zipWith(userProfileBlocFonctionnelService.findAllByEnCoursYN(enCoursYN, pageable).collectList())
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
     * {@code GET  /user-profile-bloc-fonctionnels/service-users/user-profiles/:userProfileId} : get all services by userProfileId.
     *
     * @param userProfileId the userProfileId of the UserProfileBlocFonctionnelDTO to retrieve.
     * @param pageable the pagination information.
     * @param request a {@link ServerHttpRequest} request.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of services in body.
     */
    @GetMapping(value = "/service-users/user-profiles/{userProfileId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<List<ServiceUserDTO>>> getAllServicesByUserProfileId(
        @PathVariable Long userProfileId,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable,
        ServerHttpRequest request
    ) {
        log.debug("REST request to get a page of Services by userProfileId : {}", userProfileId);
        return userProfileBlocFonctionnelService
            .countByUserProfileId(userProfileId)
            .zipWith(userProfileBlocFonctionnelService.findAllServiceByUserProfileId(userProfileId, pageable).collectList())
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
}
