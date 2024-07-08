package sn.ugb.gateway.web.rest;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.net.URI;
import java.net.URISyntaxException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.util.ForwardedHeaderUtils;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import sn.ugb.gateway.repository.HistoriqueConnexionRepository;
import sn.ugb.gateway.service.HistoriqueConnexionService;
import sn.ugb.gateway.service.dto.HistoriqueConnexionDTO;
import sn.ugb.gateway.web.rest.errors.BadRequestAlertException;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.reactive.ResponseUtil;

/**
 * REST controller for managing {@link sn.ugb.gateway.domain.HistoriqueConnexion}.
 */
@RestController
@RequestMapping("/api/historique-connexions")
public class HistoriqueConnexionResource {

    private final Logger log = LoggerFactory.getLogger(HistoriqueConnexionResource.class);

    private static final String ENTITY_NAME = "historiqueConnexion";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final HistoriqueConnexionService historiqueConnexionService;

    private final HistoriqueConnexionRepository historiqueConnexionRepository;

    public HistoriqueConnexionResource(
        HistoriqueConnexionService historiqueConnexionService,
        HistoriqueConnexionRepository historiqueConnexionRepository
    ) {
        this.historiqueConnexionService = historiqueConnexionService;
        this.historiqueConnexionRepository = historiqueConnexionRepository;
    }

    /**
     * {@code POST  /historique-connexions} : Create a new historiqueConnexion.
     *
     * @param historiqueConnexionDTO the historiqueConnexionDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new historiqueConnexionDTO, or with status {@code 400 (Bad Request)} if the historiqueConnexion has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public Mono<ResponseEntity<HistoriqueConnexionDTO>> createHistoriqueConnexion(
        @Valid @RequestBody HistoriqueConnexionDTO historiqueConnexionDTO
    ) throws URISyntaxException {
        log.debug("REST request to save HistoriqueConnexion : {}", historiqueConnexionDTO);
        if (historiqueConnexionDTO.getId() != null) {
            throw new BadRequestAlertException("A new historiqueConnexion cannot already have an ID", ENTITY_NAME, "idexists");
        }
        return historiqueConnexionService
            .save(historiqueConnexionDTO)
            .map(result -> {
                try {
                    return ResponseEntity
                        .created(new URI("/api/historique-connexions/" + result.getId()))
                        .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
                        .body(result);
                } catch (URISyntaxException e) {
                    throw new RuntimeException(e);
                }
            });
    }

    /**
     * {@code PUT  /historique-connexions/:id} : Updates an existing historiqueConnexion.
     *
     * @param id the id of the historiqueConnexionDTO to save.
     * @param historiqueConnexionDTO the historiqueConnexionDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated historiqueConnexionDTO,
     * or with status {@code 400 (Bad Request)} if the historiqueConnexionDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the historiqueConnexionDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public Mono<ResponseEntity<HistoriqueConnexionDTO>> updateHistoriqueConnexion(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody HistoriqueConnexionDTO historiqueConnexionDTO
    ) throws URISyntaxException {
        log.debug("REST request to update HistoriqueConnexion : {}, {}", id, historiqueConnexionDTO);
        if (historiqueConnexionDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, historiqueConnexionDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return historiqueConnexionRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                return historiqueConnexionService
                    .update(historiqueConnexionDTO)
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
     * {@code PATCH  /historique-connexions/:id} : Partial updates given fields of an existing historiqueConnexion, field will ignore if it is null
     *
     * @param id the id of the historiqueConnexionDTO to save.
     * @param historiqueConnexionDTO the historiqueConnexionDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated historiqueConnexionDTO,
     * or with status {@code 400 (Bad Request)} if the historiqueConnexionDTO is not valid,
     * or with status {@code 404 (Not Found)} if the historiqueConnexionDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the historiqueConnexionDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public Mono<ResponseEntity<HistoriqueConnexionDTO>> partialUpdateHistoriqueConnexion(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody HistoriqueConnexionDTO historiqueConnexionDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update HistoriqueConnexion partially : {}, {}", id, historiqueConnexionDTO);
        if (historiqueConnexionDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, historiqueConnexionDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return historiqueConnexionRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                Mono<HistoriqueConnexionDTO> result = historiqueConnexionService.partialUpdate(historiqueConnexionDTO);

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
     * {@code GET  /historique-connexions} : get all the historiqueConnexions.
     *
     * @param pageable the pagination information.
     * @param request a {@link ServerHttpRequest} request.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of historiqueConnexions in body.
     */
    @GetMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<List<HistoriqueConnexionDTO>>> getAllHistoriqueConnexions(
        @org.springdoc.core.annotations.ParameterObject Pageable pageable,
        ServerHttpRequest request
    ) {
        log.debug("REST request to get a page of HistoriqueConnexions");
        return historiqueConnexionService
            .countAll()
            .zipWith(historiqueConnexionService.findAll(pageable).collectList())
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
     * {@code GET  /historique-connexions/:id} : get the "id" historiqueConnexion.
     *
     * @param id the id of the historiqueConnexionDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the historiqueConnexionDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public Mono<ResponseEntity<HistoriqueConnexionDTO>> getHistoriqueConnexion(@PathVariable("id") Long id) {
        log.debug("REST request to get HistoriqueConnexion : {}", id);
        Mono<HistoriqueConnexionDTO> historiqueConnexionDTO = historiqueConnexionService.findOne(id);
        return ResponseUtil.wrapOrNotFound(historiqueConnexionDTO);
    }

    /**
     * {@code DELETE  /historique-connexions/:id} : delete the "id" historiqueConnexion.
     *
     * @param id the id of the historiqueConnexionDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Void>> deleteHistoriqueConnexion(@PathVariable("id") Long id) {
        log.debug("REST request to delete HistoriqueConnexion : {}", id);
        return historiqueConnexionService
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
     * {@code SEARCH  /historique-connexions/_search?query=:query} : search for the historiqueConnexion corresponding
     * to the query.
     *
     * @param query the query of the historiqueConnexion search.
     * @param pageable the pagination information.
     * @param request a {@link ServerHttpRequest} request.
     * @return the result of the search.
     */
    @GetMapping("/_search")
    public Mono<ResponseEntity<Flux<HistoriqueConnexionDTO>>> searchHistoriqueConnexions(
        @RequestParam("query") String query,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable,
        ServerHttpRequest request
    ) {
        log.debug("REST request to search for a page of HistoriqueConnexions for query {}", query);
        return historiqueConnexionService
            .searchCount()
            .map(total -> new PageImpl<>(new ArrayList<>(), pageable, total))
            .map(page ->
                PaginationUtil.generatePaginationHttpHeaders(
                    ForwardedHeaderUtils.adaptFromForwardedHeaders(request.getURI(), request.getHeaders()),
                    page
                )
            )
            .map(headers -> ResponseEntity.ok().headers(headers).body(historiqueConnexionService.search(query, pageable)));
    }

    /**
     * GET  /historique-connexions/en-cours-yn/:enCoursYN : get all the historiqueConnexions by enCoursYN.
     *
     * @param enCoursYN the enCoursYN of the HistoriqueConnexionDTO to retrieve.
     * @param pageable the pagination information.
     * @param request a {@link ServerHttpRequest} request.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of historiqueConnexions in body.
     */
    @GetMapping("/en-cours-yn/{enCoursYN}")
    public Mono<ResponseEntity<List<HistoriqueConnexionDTO>>> getAllHistoriqueConnexionByEncoursYN(
        @PathVariable Boolean enCoursYN,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable,
        ServerHttpRequest request
    ) {
        log.debug("REST request to get a page of HistoriqueConnexions by enCoursYN: {}", enCoursYN);
        return historiqueConnexionService
            .countAllByEnCoursYN(enCoursYN)
            .zipWith(historiqueConnexionService.getAllHistoriqueConnexionByEncoursYN(enCoursYN, pageable).collectList())
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
     * {@code POST  /archive/:id} : Archive the historiqueConnexion.
     *
     * @param id the id of the historiqueConnexionDTO to archive.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the archived historiqueConnexionDTO,
     * or with status {@code 404 (Not Found)} if the historiqueConnexionDTO couldn't be archived.
     */
    @PostMapping("/archive/{id}")
    public Mono<ResponseEntity<HistoriqueConnexionDTO>> archiveHistoriqueConnexion(@PathVariable Long id) {
        log.debug("REST request to archive HistoriqueConnexion : {}", id);
        return historiqueConnexionService.archiveHistoriqueConnexion(id)
            .map(result ->
                ResponseEntity.ok()
                    .headers(HeaderUtil.createAlert(applicationName, "historiqueConnexion archived", id.toString()))
                    .body(result)
            )
            .onErrorResume(e -> Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND)));
    }

    /**
     * GET  /historique-connexions/timeslots : get all the historiqueConnexions within the specified time slots.
     *
     * @param dateDebut the start date and time of the time slot.
     * @param dateFin the end date and time of the time slot.
     * @param pageable the pagination information.
     * @param request a {@link ServerHttpRequest} request.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of historiqueConnexions in body.
     */
    @Operation(summary = "Get all historiqueConnexions by time slots",
        description = "Get a list of historiqueConnexions filtered by the specified time slots.")
    @GetMapping("/timeslots")
    public Mono<ResponseEntity<List<HistoriqueConnexionDTO>>> getAllHistoriqueConnexionByTimeSlots(
        @Parameter(
            description = "The start date time of the HistoriqueConnexion to retrieve.",
            required = true,
            schema = @Schema(type = "string", format = "date", example = "2023-07-04")
        )
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDate dateDebut,

        @Parameter(
            description = "The end date time of the HistoriqueConnexion to retrieve.",
            required = true,
            schema = @Schema(type = "string", format = "date", example = "2024-07-05")
        )
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDate dateFin,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable,
        ServerHttpRequest request
    ) {
        log.debug("REST request to get a page of HistoriqueConnexions by time slots: {} to {}", dateDebut, dateFin);
        return historiqueConnexionService
            .countAllByTimeSlots(dateDebut, dateFin)
            .zipWith(historiqueConnexionService.getAllHistoriqueConnexionByTimeSlots(dateDebut, dateFin, pageable).collectList())
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
     * GET  /historique-connexions/infos-users/timeslots/{infosUserId} : get all the historiqueConnexions by infosUserId within the specified time slots.
     *
     * @param infosUserId the ID of the infosUser to retrieve historiqueConnexions for.
     * @param dateDebut the start date and time of the time slot.
     * @param dateFin the end date and time of the time slot.
     * @param pageable the pagination information.
     * @param request a {@link ServerHttpRequest} request.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of historiqueConnexions in body.
     */
    @Operation(summary = "Get all historiqueConnexions by time slots",
        description = "Get a list of historiqueConnexions filtered by the specified time slots.")
    @GetMapping("/infos-users/timeslots/{infosUserId}")
    public Mono<ResponseEntity<List<HistoriqueConnexionDTO>>> getAllHistoriqueConnexionByInfosUserIdAndTimeSlots(
        @PathVariable Long infosUserId,
        @Parameter(
            description = "The start date time of the HistoriqueConnexion to retrieve.",
            required = true,
            schema = @Schema(type = "string", format = "date", example = "2023-07-04")
        )
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDate dateDebut,

        @Parameter(
            description = "The end date time of the HistoriqueConnexion to retrieve.",
            required = true,
            schema = @Schema(type = "string", format = "date", example = "2024-07-05")
        )
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDate dateFin,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable,
        ServerHttpRequest request
    ) {
        log.debug("REST request to get a page of HistoriqueConnexions by infosUserId: {} and time slots: {} to {}", infosUserId, dateDebut, dateFin);
        return historiqueConnexionService
            .countAllByInfosUserIdAndTimeSlots(infosUserId, dateDebut, dateFin)
            .zipWith(historiqueConnexionService.getAllHistoriqueConnexionByInfosUserIdAndTimeSlots(infosUserId, dateDebut, dateFin, pageable).collectList())
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
