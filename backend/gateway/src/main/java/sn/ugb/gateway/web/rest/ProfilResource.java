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
import sn.ugb.gateway.repository.ProfilRepository;
import sn.ugb.gateway.service.ProfilService;
import sn.ugb.gateway.service.dto.ProfilDTO;
import sn.ugb.gateway.web.rest.errors.BadRequestAlertException;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.reactive.ResponseUtil;

/**
 * REST controller for managing {@link sn.ugb.gateway.domain.Profil}.
 */
@RestController
@RequestMapping("/api/profils")
public class ProfilResource {

    private final Logger log = LoggerFactory.getLogger(ProfilResource.class);

    private static final String ENTITY_NAME = "profil";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ProfilService profilService;

    private final ProfilRepository profilRepository;

    public ProfilResource(ProfilService profilService, ProfilRepository profilRepository) {
        this.profilService = profilService;
        this.profilRepository = profilRepository;
    }

    /**
     * {@code POST  /profils} : Create a new profil.
     *
     * @param profilDTO the profilDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new profilDTO, or with status {@code 400 (Bad Request)} if the profil has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public Mono<ResponseEntity<ProfilDTO>> createProfil(@Valid @RequestBody ProfilDTO profilDTO) throws URISyntaxException {
        log.debug("REST request to save Profil : {}", profilDTO);
        if (profilDTO.getId() != null) {
            throw new BadRequestAlertException("A new profil cannot already have an ID", ENTITY_NAME, "idexists");
        }
        return profilService
            .save(profilDTO)
            .map(result -> {
                try {
                    return ResponseEntity
                        .created(new URI("/api/profils/" + result.getId()))
                        .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
                        .body(result);
                } catch (URISyntaxException e) {
                    throw new RuntimeException(e);
                }
            });
    }

    /**
     * {@code PUT  /profils/:id} : Updates an existing profil.
     *
     * @param id the id of the profilDTO to save.
     * @param profilDTO the profilDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated profilDTO,
     * or with status {@code 400 (Bad Request)} if the profilDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the profilDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public Mono<ResponseEntity<ProfilDTO>> updateProfil(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody ProfilDTO profilDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Profil : {}, {}", id, profilDTO);
        if (profilDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, profilDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return profilRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                return profilService
                    .update(profilDTO)
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
     * {@code PATCH  /profils/:id} : Partial updates given fields of an existing profil, field will ignore if it is null
     *
     * @param id the id of the profilDTO to save.
     * @param profilDTO the profilDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated profilDTO,
     * or with status {@code 400 (Bad Request)} if the profilDTO is not valid,
     * or with status {@code 404 (Not Found)} if the profilDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the profilDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public Mono<ResponseEntity<ProfilDTO>> partialUpdateProfil(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody ProfilDTO profilDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Profil partially : {}, {}", id, profilDTO);
        if (profilDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, profilDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return profilRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                Mono<ProfilDTO> result = profilService.partialUpdate(profilDTO);

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
     * {@code GET  /profils} : get all the profils.
     *
     * @param pageable the pagination information.
     * @param request a {@link ServerHttpRequest} request.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of profils in body.
     */
    @GetMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<List<ProfilDTO>>> getAllProfils(
        @org.springdoc.core.annotations.ParameterObject Pageable pageable,
        ServerHttpRequest request
    ) {
        log.debug("REST request to get a page of Profils");
        return profilService
            .countAll()
            .zipWith(profilService.findAll(pageable).collectList())
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
     * {@code GET  /profils/:id} : get the "id" profil.
     *
     * @param id the id of the profilDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the profilDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public Mono<ResponseEntity<ProfilDTO>> getProfil(@PathVariable("id") Long id) {
        log.debug("REST request to get Profil : {}", id);
        Mono<ProfilDTO> profilDTO = profilService.findOne(id);
        return ResponseUtil.wrapOrNotFound(profilDTO);
    }

    /**
     * {@code DELETE  /profils/:id} : delete the "id" profil.
     *
     * @param id the id of the profilDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Void>> deleteProfil(@PathVariable("id") Long id) {
        log.debug("REST request to delete Profil : {}", id);
        return profilService
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
     * {@code SEARCH  /profils/_search?query=:query} : search for the profil corresponding
     * to the query.
     *
     * @param query the query of the profil search.
     * @param pageable the pagination information.
     * @param request a {@link ServerHttpRequest} request.
     * @return the result of the search.
     */
    @GetMapping("/_search")
    public Mono<ResponseEntity<Flux<ProfilDTO>>> searchProfils(
        @RequestParam("query") String query,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable,
        ServerHttpRequest request
    ) {
        log.debug("REST request to search for a page of Profils for query {}", query);
        return profilService
            .searchCount()
            .map(total -> new PageImpl<>(new ArrayList<>(), pageable, total))
            .map(page ->
                PaginationUtil.generatePaginationHttpHeaders(
                    ForwardedHeaderUtils.adaptFromForwardedHeaders(request.getURI(), request.getHeaders()),
                    page
                )
            )
            .map(headers -> ResponseEntity.ok().headers(headers).body(profilService.search(query, pageable)));
    }
}
