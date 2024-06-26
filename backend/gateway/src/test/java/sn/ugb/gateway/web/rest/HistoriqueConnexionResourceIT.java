package sn.ugb.gateway.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.reactive.server.SecurityMockServerConfigurers.csrf;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;
import org.apache.commons.collections4.IterableUtils;
import org.assertj.core.util.IterableUtil;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.reactive.server.WebTestClient;
import sn.ugb.gateway.IntegrationTest;
import sn.ugb.gateway.domain.HistoriqueConnexion;
import sn.ugb.gateway.repository.EntityManager;
import sn.ugb.gateway.repository.HistoriqueConnexionRepository;
import sn.ugb.gateway.repository.search.HistoriqueConnexionSearchRepository;
import sn.ugb.gateway.service.dto.HistoriqueConnexionDTO;
import sn.ugb.gateway.service.mapper.HistoriqueConnexionMapper;

/**
 * Integration tests for the {@link HistoriqueConnexionResource} REST controller.
 */
@IntegrationTest
@AutoConfigureWebTestClient(timeout = IntegrationTest.DEFAULT_ENTITY_TIMEOUT)
@WithMockUser
class HistoriqueConnexionResourceIT {

    private static final LocalDate DEFAULT_DATE_DEBUT_CONNEXION = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE_DEBUT_CONNEXION = LocalDate.now(ZoneId.systemDefault());

    private static final LocalDate DEFAULT_DATE_FIN_CONNEXION = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE_FIN_CONNEXION = LocalDate.now(ZoneId.systemDefault());

    private static final String DEFAULT_ADRESSE_IP = "AAAAAAAAAA";
    private static final String UPDATED_ADRESSE_IP = "BBBBBBBBBB";

    private static final Boolean DEFAULT_ACTIF_YN = false;
    private static final Boolean UPDATED_ACTIF_YN = true;

    private static final String ENTITY_API_URL = "/api/historique-connexions";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/historique-connexions/_search";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private HistoriqueConnexionRepository historiqueConnexionRepository;

    @Autowired
    private HistoriqueConnexionMapper historiqueConnexionMapper;

    @Autowired
    private HistoriqueConnexionSearchRepository historiqueConnexionSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private WebTestClient webTestClient;

    private HistoriqueConnexion historiqueConnexion;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static HistoriqueConnexion createEntity(EntityManager em) {
        HistoriqueConnexion historiqueConnexion = new HistoriqueConnexion()
            .dateDebutConnexion(DEFAULT_DATE_DEBUT_CONNEXION)
            .dateFinConnexion(DEFAULT_DATE_FIN_CONNEXION)
            .adresseIp(DEFAULT_ADRESSE_IP)
            .actifYN(DEFAULT_ACTIF_YN);
        return historiqueConnexion;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static HistoriqueConnexion createUpdatedEntity(EntityManager em) {
        HistoriqueConnexion historiqueConnexion = new HistoriqueConnexion()
            .dateDebutConnexion(UPDATED_DATE_DEBUT_CONNEXION)
            .dateFinConnexion(UPDATED_DATE_FIN_CONNEXION)
            .adresseIp(UPDATED_ADRESSE_IP)
            .actifYN(UPDATED_ACTIF_YN);
        return historiqueConnexion;
    }

    public static void deleteEntities(EntityManager em) {
        try {
            em.deleteAll(HistoriqueConnexion.class).block();
        } catch (Exception e) {
            // It can fail, if other entities are still referring this - it will be removed later.
        }
    }

    @AfterEach
    public void cleanup() {
        deleteEntities(em);
    }

    @AfterEach
    public void cleanupElasticSearchRepository() {
        historiqueConnexionSearchRepository.deleteAll().block();
        assertThat(historiqueConnexionSearchRepository.count().block()).isEqualTo(0);
    }

    @BeforeEach
    public void setupCsrf() {
        webTestClient = webTestClient.mutateWith(csrf());
    }

    @BeforeEach
    public void initTest() {
        deleteEntities(em);
        historiqueConnexion = createEntity(em);
    }

    @Test
    void createHistoriqueConnexion() throws Exception {
        int databaseSizeBeforeCreate = historiqueConnexionRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(historiqueConnexionSearchRepository.findAll().collectList().block());
        // Create the HistoriqueConnexion
        HistoriqueConnexionDTO historiqueConnexionDTO = historiqueConnexionMapper.toDto(historiqueConnexion);
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(historiqueConnexionDTO))
            .exchange()
            .expectStatus()
            .isCreated();

        // Validate the HistoriqueConnexion in the database
        List<HistoriqueConnexion> historiqueConnexionList = historiqueConnexionRepository.findAll().collectList().block();
        assertThat(historiqueConnexionList).hasSize(databaseSizeBeforeCreate + 1);
        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(historiqueConnexionSearchRepository.findAll().collectList().block());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore + 1);
            });
        HistoriqueConnexion testHistoriqueConnexion = historiqueConnexionList.get(historiqueConnexionList.size() - 1);
        assertThat(testHistoriqueConnexion.getDateDebutConnexion()).isEqualTo(DEFAULT_DATE_DEBUT_CONNEXION);
        assertThat(testHistoriqueConnexion.getDateFinConnexion()).isEqualTo(DEFAULT_DATE_FIN_CONNEXION);
        assertThat(testHistoriqueConnexion.getAdresseIp()).isEqualTo(DEFAULT_ADRESSE_IP);
        assertThat(testHistoriqueConnexion.getActifYN()).isEqualTo(DEFAULT_ACTIF_YN);
    }

    @Test
    void createHistoriqueConnexionWithExistingId() throws Exception {
        // Create the HistoriqueConnexion with an existing ID
        historiqueConnexion.setId(1L);
        HistoriqueConnexionDTO historiqueConnexionDTO = historiqueConnexionMapper.toDto(historiqueConnexion);

        int databaseSizeBeforeCreate = historiqueConnexionRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(historiqueConnexionSearchRepository.findAll().collectList().block());

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(historiqueConnexionDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the HistoriqueConnexion in the database
        List<HistoriqueConnexion> historiqueConnexionList = historiqueConnexionRepository.findAll().collectList().block();
        assertThat(historiqueConnexionList).hasSize(databaseSizeBeforeCreate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(historiqueConnexionSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void checkDateDebutConnexionIsRequired() throws Exception {
        int databaseSizeBeforeTest = historiqueConnexionRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(historiqueConnexionSearchRepository.findAll().collectList().block());
        // set the field null
        historiqueConnexion.setDateDebutConnexion(null);

        // Create the HistoriqueConnexion, which fails.
        HistoriqueConnexionDTO historiqueConnexionDTO = historiqueConnexionMapper.toDto(historiqueConnexion);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(historiqueConnexionDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<HistoriqueConnexion> historiqueConnexionList = historiqueConnexionRepository.findAll().collectList().block();
        assertThat(historiqueConnexionList).hasSize(databaseSizeBeforeTest);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(historiqueConnexionSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void checkDateFinConnexionIsRequired() throws Exception {
        int databaseSizeBeforeTest = historiqueConnexionRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(historiqueConnexionSearchRepository.findAll().collectList().block());
        // set the field null
        historiqueConnexion.setDateFinConnexion(null);

        // Create the HistoriqueConnexion, which fails.
        HistoriqueConnexionDTO historiqueConnexionDTO = historiqueConnexionMapper.toDto(historiqueConnexion);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(historiqueConnexionDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<HistoriqueConnexion> historiqueConnexionList = historiqueConnexionRepository.findAll().collectList().block();
        assertThat(historiqueConnexionList).hasSize(databaseSizeBeforeTest);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(historiqueConnexionSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void checkActifYNIsRequired() throws Exception {
        int databaseSizeBeforeTest = historiqueConnexionRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(historiqueConnexionSearchRepository.findAll().collectList().block());
        // set the field null
        historiqueConnexion.setActifYN(null);

        // Create the HistoriqueConnexion, which fails.
        HistoriqueConnexionDTO historiqueConnexionDTO = historiqueConnexionMapper.toDto(historiqueConnexion);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(historiqueConnexionDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<HistoriqueConnexion> historiqueConnexionList = historiqueConnexionRepository.findAll().collectList().block();
        assertThat(historiqueConnexionList).hasSize(databaseSizeBeforeTest);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(historiqueConnexionSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void getAllHistoriqueConnexions() {
        // Initialize the database
        historiqueConnexionRepository.save(historiqueConnexion).block();

        // Get all the historiqueConnexionList
        webTestClient
            .get()
            .uri(ENTITY_API_URL + "?sort=id,desc")
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.[*].id")
            .value(hasItem(historiqueConnexion.getId().intValue()))
            .jsonPath("$.[*].dateDebutConnexion")
            .value(hasItem(DEFAULT_DATE_DEBUT_CONNEXION.toString()))
            .jsonPath("$.[*].dateFinConnexion")
            .value(hasItem(DEFAULT_DATE_FIN_CONNEXION.toString()))
            .jsonPath("$.[*].adresseIp")
            .value(hasItem(DEFAULT_ADRESSE_IP))
            .jsonPath("$.[*].actifYN")
            .value(hasItem(DEFAULT_ACTIF_YN.booleanValue()));
    }

    @Test
    void getHistoriqueConnexion() {
        // Initialize the database
        historiqueConnexionRepository.save(historiqueConnexion).block();

        // Get the historiqueConnexion
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, historiqueConnexion.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(historiqueConnexion.getId().intValue()))
            .jsonPath("$.dateDebutConnexion")
            .value(is(DEFAULT_DATE_DEBUT_CONNEXION.toString()))
            .jsonPath("$.dateFinConnexion")
            .value(is(DEFAULT_DATE_FIN_CONNEXION.toString()))
            .jsonPath("$.adresseIp")
            .value(is(DEFAULT_ADRESSE_IP))
            .jsonPath("$.actifYN")
            .value(is(DEFAULT_ACTIF_YN.booleanValue()));
    }

    @Test
    void getNonExistingHistoriqueConnexion() {
        // Get the historiqueConnexion
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_PROBLEM_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putExistingHistoriqueConnexion() throws Exception {
        // Initialize the database
        historiqueConnexionRepository.save(historiqueConnexion).block();

        int databaseSizeBeforeUpdate = historiqueConnexionRepository.findAll().collectList().block().size();
        historiqueConnexionSearchRepository.save(historiqueConnexion).block();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(historiqueConnexionSearchRepository.findAll().collectList().block());

        // Update the historiqueConnexion
        HistoriqueConnexion updatedHistoriqueConnexion = historiqueConnexionRepository.findById(historiqueConnexion.getId()).block();
        updatedHistoriqueConnexion
            .dateDebutConnexion(UPDATED_DATE_DEBUT_CONNEXION)
            .dateFinConnexion(UPDATED_DATE_FIN_CONNEXION)
            .adresseIp(UPDATED_ADRESSE_IP)
            .actifYN(UPDATED_ACTIF_YN);
        HistoriqueConnexionDTO historiqueConnexionDTO = historiqueConnexionMapper.toDto(updatedHistoriqueConnexion);

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, historiqueConnexionDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(historiqueConnexionDTO))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the HistoriqueConnexion in the database
        List<HistoriqueConnexion> historiqueConnexionList = historiqueConnexionRepository.findAll().collectList().block();
        assertThat(historiqueConnexionList).hasSize(databaseSizeBeforeUpdate);
        HistoriqueConnexion testHistoriqueConnexion = historiqueConnexionList.get(historiqueConnexionList.size() - 1);
        assertThat(testHistoriqueConnexion.getDateDebutConnexion()).isEqualTo(UPDATED_DATE_DEBUT_CONNEXION);
        assertThat(testHistoriqueConnexion.getDateFinConnexion()).isEqualTo(UPDATED_DATE_FIN_CONNEXION);
        assertThat(testHistoriqueConnexion.getAdresseIp()).isEqualTo(UPDATED_ADRESSE_IP);
        assertThat(testHistoriqueConnexion.getActifYN()).isEqualTo(UPDATED_ACTIF_YN);
        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(historiqueConnexionSearchRepository.findAll().collectList().block());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
                List<HistoriqueConnexion> historiqueConnexionSearchList = IterableUtils.toList(
                    historiqueConnexionSearchRepository.findAll().collectList().block()
                );
                HistoriqueConnexion testHistoriqueConnexionSearch = historiqueConnexionSearchList.get(searchDatabaseSizeAfter - 1);
                assertThat(testHistoriqueConnexionSearch.getDateDebutConnexion()).isEqualTo(UPDATED_DATE_DEBUT_CONNEXION);
                assertThat(testHistoriqueConnexionSearch.getDateFinConnexion()).isEqualTo(UPDATED_DATE_FIN_CONNEXION);
                assertThat(testHistoriqueConnexionSearch.getAdresseIp()).isEqualTo(UPDATED_ADRESSE_IP);
                assertThat(testHistoriqueConnexionSearch.getActifYN()).isEqualTo(UPDATED_ACTIF_YN);
            });
    }

    @Test
    void putNonExistingHistoriqueConnexion() throws Exception {
        int databaseSizeBeforeUpdate = historiqueConnexionRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(historiqueConnexionSearchRepository.findAll().collectList().block());
        historiqueConnexion.setId(longCount.incrementAndGet());

        // Create the HistoriqueConnexion
        HistoriqueConnexionDTO historiqueConnexionDTO = historiqueConnexionMapper.toDto(historiqueConnexion);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, historiqueConnexionDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(historiqueConnexionDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the HistoriqueConnexion in the database
        List<HistoriqueConnexion> historiqueConnexionList = historiqueConnexionRepository.findAll().collectList().block();
        assertThat(historiqueConnexionList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(historiqueConnexionSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void putWithIdMismatchHistoriqueConnexion() throws Exception {
        int databaseSizeBeforeUpdate = historiqueConnexionRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(historiqueConnexionSearchRepository.findAll().collectList().block());
        historiqueConnexion.setId(longCount.incrementAndGet());

        // Create the HistoriqueConnexion
        HistoriqueConnexionDTO historiqueConnexionDTO = historiqueConnexionMapper.toDto(historiqueConnexion);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, longCount.incrementAndGet())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(historiqueConnexionDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the HistoriqueConnexion in the database
        List<HistoriqueConnexion> historiqueConnexionList = historiqueConnexionRepository.findAll().collectList().block();
        assertThat(historiqueConnexionList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(historiqueConnexionSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void putWithMissingIdPathParamHistoriqueConnexion() throws Exception {
        int databaseSizeBeforeUpdate = historiqueConnexionRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(historiqueConnexionSearchRepository.findAll().collectList().block());
        historiqueConnexion.setId(longCount.incrementAndGet());

        // Create the HistoriqueConnexion
        HistoriqueConnexionDTO historiqueConnexionDTO = historiqueConnexionMapper.toDto(historiqueConnexion);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(historiqueConnexionDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the HistoriqueConnexion in the database
        List<HistoriqueConnexion> historiqueConnexionList = historiqueConnexionRepository.findAll().collectList().block();
        assertThat(historiqueConnexionList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(historiqueConnexionSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void partialUpdateHistoriqueConnexionWithPatch() throws Exception {
        // Initialize the database
        historiqueConnexionRepository.save(historiqueConnexion).block();

        int databaseSizeBeforeUpdate = historiqueConnexionRepository.findAll().collectList().block().size();

        // Update the historiqueConnexion using partial update
        HistoriqueConnexion partialUpdatedHistoriqueConnexion = new HistoriqueConnexion();
        partialUpdatedHistoriqueConnexion.setId(historiqueConnexion.getId());

        partialUpdatedHistoriqueConnexion.adresseIp(UPDATED_ADRESSE_IP).actifYN(UPDATED_ACTIF_YN);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedHistoriqueConnexion.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedHistoriqueConnexion))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the HistoriqueConnexion in the database
        List<HistoriqueConnexion> historiqueConnexionList = historiqueConnexionRepository.findAll().collectList().block();
        assertThat(historiqueConnexionList).hasSize(databaseSizeBeforeUpdate);
        HistoriqueConnexion testHistoriqueConnexion = historiqueConnexionList.get(historiqueConnexionList.size() - 1);
        assertThat(testHistoriqueConnexion.getDateDebutConnexion()).isEqualTo(DEFAULT_DATE_DEBUT_CONNEXION);
        assertThat(testHistoriqueConnexion.getDateFinConnexion()).isEqualTo(DEFAULT_DATE_FIN_CONNEXION);
        assertThat(testHistoriqueConnexion.getAdresseIp()).isEqualTo(UPDATED_ADRESSE_IP);
        assertThat(testHistoriqueConnexion.getActifYN()).isEqualTo(UPDATED_ACTIF_YN);
    }

    @Test
    void fullUpdateHistoriqueConnexionWithPatch() throws Exception {
        // Initialize the database
        historiqueConnexionRepository.save(historiqueConnexion).block();

        int databaseSizeBeforeUpdate = historiqueConnexionRepository.findAll().collectList().block().size();

        // Update the historiqueConnexion using partial update
        HistoriqueConnexion partialUpdatedHistoriqueConnexion = new HistoriqueConnexion();
        partialUpdatedHistoriqueConnexion.setId(historiqueConnexion.getId());

        partialUpdatedHistoriqueConnexion
            .dateDebutConnexion(UPDATED_DATE_DEBUT_CONNEXION)
            .dateFinConnexion(UPDATED_DATE_FIN_CONNEXION)
            .adresseIp(UPDATED_ADRESSE_IP)
            .actifYN(UPDATED_ACTIF_YN);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedHistoriqueConnexion.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedHistoriqueConnexion))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the HistoriqueConnexion in the database
        List<HistoriqueConnexion> historiqueConnexionList = historiqueConnexionRepository.findAll().collectList().block();
        assertThat(historiqueConnexionList).hasSize(databaseSizeBeforeUpdate);
        HistoriqueConnexion testHistoriqueConnexion = historiqueConnexionList.get(historiqueConnexionList.size() - 1);
        assertThat(testHistoriqueConnexion.getDateDebutConnexion()).isEqualTo(UPDATED_DATE_DEBUT_CONNEXION);
        assertThat(testHistoriqueConnexion.getDateFinConnexion()).isEqualTo(UPDATED_DATE_FIN_CONNEXION);
        assertThat(testHistoriqueConnexion.getAdresseIp()).isEqualTo(UPDATED_ADRESSE_IP);
        assertThat(testHistoriqueConnexion.getActifYN()).isEqualTo(UPDATED_ACTIF_YN);
    }

    @Test
    void patchNonExistingHistoriqueConnexion() throws Exception {
        int databaseSizeBeforeUpdate = historiqueConnexionRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(historiqueConnexionSearchRepository.findAll().collectList().block());
        historiqueConnexion.setId(longCount.incrementAndGet());

        // Create the HistoriqueConnexion
        HistoriqueConnexionDTO historiqueConnexionDTO = historiqueConnexionMapper.toDto(historiqueConnexion);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, historiqueConnexionDTO.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(historiqueConnexionDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the HistoriqueConnexion in the database
        List<HistoriqueConnexion> historiqueConnexionList = historiqueConnexionRepository.findAll().collectList().block();
        assertThat(historiqueConnexionList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(historiqueConnexionSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void patchWithIdMismatchHistoriqueConnexion() throws Exception {
        int databaseSizeBeforeUpdate = historiqueConnexionRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(historiqueConnexionSearchRepository.findAll().collectList().block());
        historiqueConnexion.setId(longCount.incrementAndGet());

        // Create the HistoriqueConnexion
        HistoriqueConnexionDTO historiqueConnexionDTO = historiqueConnexionMapper.toDto(historiqueConnexion);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, longCount.incrementAndGet())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(historiqueConnexionDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the HistoriqueConnexion in the database
        List<HistoriqueConnexion> historiqueConnexionList = historiqueConnexionRepository.findAll().collectList().block();
        assertThat(historiqueConnexionList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(historiqueConnexionSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void patchWithMissingIdPathParamHistoriqueConnexion() throws Exception {
        int databaseSizeBeforeUpdate = historiqueConnexionRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(historiqueConnexionSearchRepository.findAll().collectList().block());
        historiqueConnexion.setId(longCount.incrementAndGet());

        // Create the HistoriqueConnexion
        HistoriqueConnexionDTO historiqueConnexionDTO = historiqueConnexionMapper.toDto(historiqueConnexion);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(historiqueConnexionDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the HistoriqueConnexion in the database
        List<HistoriqueConnexion> historiqueConnexionList = historiqueConnexionRepository.findAll().collectList().block();
        assertThat(historiqueConnexionList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(historiqueConnexionSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void deleteHistoriqueConnexion() {
        // Initialize the database
        historiqueConnexionRepository.save(historiqueConnexion).block();
        historiqueConnexionRepository.save(historiqueConnexion).block();
        historiqueConnexionSearchRepository.save(historiqueConnexion).block();

        int databaseSizeBeforeDelete = historiqueConnexionRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(historiqueConnexionSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeBefore).isEqualTo(databaseSizeBeforeDelete);

        // Delete the historiqueConnexion
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, historiqueConnexion.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        List<HistoriqueConnexion> historiqueConnexionList = historiqueConnexionRepository.findAll().collectList().block();
        assertThat(historiqueConnexionList).hasSize(databaseSizeBeforeDelete - 1);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(historiqueConnexionSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore - 1);
    }

    @Test
    void searchHistoriqueConnexion() {
        // Initialize the database
        historiqueConnexion = historiqueConnexionRepository.save(historiqueConnexion).block();
        historiqueConnexionSearchRepository.save(historiqueConnexion).block();

        // Search the historiqueConnexion
        webTestClient
            .get()
            .uri(ENTITY_SEARCH_API_URL + "?query=id:" + historiqueConnexion.getId())
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.[*].id")
            .value(hasItem(historiqueConnexion.getId().intValue()))
            .jsonPath("$.[*].dateDebutConnexion")
            .value(hasItem(DEFAULT_DATE_DEBUT_CONNEXION.toString()))
            .jsonPath("$.[*].dateFinConnexion")
            .value(hasItem(DEFAULT_DATE_FIN_CONNEXION.toString()))
            .jsonPath("$.[*].adresseIp")
            .value(hasItem(DEFAULT_ADRESSE_IP))
            .jsonPath("$.[*].actifYN")
            .value(hasItem(DEFAULT_ACTIF_YN.booleanValue()));
    }
}
