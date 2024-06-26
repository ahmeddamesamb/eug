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
import sn.ugb.gateway.domain.BlocFonctionnel;
import sn.ugb.gateway.repository.BlocFonctionnelRepository;
import sn.ugb.gateway.repository.EntityManager;
import sn.ugb.gateway.repository.search.BlocFonctionnelSearchRepository;
import sn.ugb.gateway.service.dto.BlocFonctionnelDTO;
import sn.ugb.gateway.service.mapper.BlocFonctionnelMapper;

/**
 * Integration tests for the {@link BlocFonctionnelResource} REST controller.
 */
@IntegrationTest
@AutoConfigureWebTestClient(timeout = IntegrationTest.DEFAULT_ENTITY_TIMEOUT)
@WithMockUser
class BlocFonctionnelResourceIT {

    private static final String DEFAULT_LIBELLE_BLOC = "AAAAAAAAAA";
    private static final String UPDATED_LIBELLE_BLOC = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_DATE_AJOUT_BLOC = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE_AJOUT_BLOC = LocalDate.now(ZoneId.systemDefault());

    private static final Boolean DEFAULT_ACTIF_YN = false;
    private static final Boolean UPDATED_ACTIF_YN = true;

    private static final String ENTITY_API_URL = "/api/bloc-fonctionnels";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/bloc-fonctionnels/_search";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private BlocFonctionnelRepository blocFonctionnelRepository;

    @Autowired
    private BlocFonctionnelMapper blocFonctionnelMapper;

    @Autowired
    private BlocFonctionnelSearchRepository blocFonctionnelSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private WebTestClient webTestClient;

    private BlocFonctionnel blocFonctionnel;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static BlocFonctionnel createEntity(EntityManager em) {
        BlocFonctionnel blocFonctionnel = new BlocFonctionnel()
            .libelleBloc(DEFAULT_LIBELLE_BLOC)
            .dateAjoutBloc(DEFAULT_DATE_AJOUT_BLOC)
            .actifYN(DEFAULT_ACTIF_YN);
        return blocFonctionnel;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static BlocFonctionnel createUpdatedEntity(EntityManager em) {
        BlocFonctionnel blocFonctionnel = new BlocFonctionnel()
            .libelleBloc(UPDATED_LIBELLE_BLOC)
            .dateAjoutBloc(UPDATED_DATE_AJOUT_BLOC)
            .actifYN(UPDATED_ACTIF_YN);
        return blocFonctionnel;
    }

    public static void deleteEntities(EntityManager em) {
        try {
            em.deleteAll(BlocFonctionnel.class).block();
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
        blocFonctionnelSearchRepository.deleteAll().block();
        assertThat(blocFonctionnelSearchRepository.count().block()).isEqualTo(0);
    }

    @BeforeEach
    public void setupCsrf() {
        webTestClient = webTestClient.mutateWith(csrf());
    }

    @BeforeEach
    public void initTest() {
        deleteEntities(em);
        blocFonctionnel = createEntity(em);
    }

    @Test
    void createBlocFonctionnel() throws Exception {
        int databaseSizeBeforeCreate = blocFonctionnelRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(blocFonctionnelSearchRepository.findAll().collectList().block());
        // Create the BlocFonctionnel
        BlocFonctionnelDTO blocFonctionnelDTO = blocFonctionnelMapper.toDto(blocFonctionnel);
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(blocFonctionnelDTO))
            .exchange()
            .expectStatus()
            .isCreated();

        // Validate the BlocFonctionnel in the database
        List<BlocFonctionnel> blocFonctionnelList = blocFonctionnelRepository.findAll().collectList().block();
        assertThat(blocFonctionnelList).hasSize(databaseSizeBeforeCreate + 1);
        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(blocFonctionnelSearchRepository.findAll().collectList().block());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore + 1);
            });
        BlocFonctionnel testBlocFonctionnel = blocFonctionnelList.get(blocFonctionnelList.size() - 1);
        assertThat(testBlocFonctionnel.getLibelleBloc()).isEqualTo(DEFAULT_LIBELLE_BLOC);
        assertThat(testBlocFonctionnel.getDateAjoutBloc()).isEqualTo(DEFAULT_DATE_AJOUT_BLOC);
        assertThat(testBlocFonctionnel.getActifYN()).isEqualTo(DEFAULT_ACTIF_YN);
    }

    @Test
    void createBlocFonctionnelWithExistingId() throws Exception {
        // Create the BlocFonctionnel with an existing ID
        blocFonctionnel.setId(1L);
        BlocFonctionnelDTO blocFonctionnelDTO = blocFonctionnelMapper.toDto(blocFonctionnel);

        int databaseSizeBeforeCreate = blocFonctionnelRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(blocFonctionnelSearchRepository.findAll().collectList().block());

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(blocFonctionnelDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the BlocFonctionnel in the database
        List<BlocFonctionnel> blocFonctionnelList = blocFonctionnelRepository.findAll().collectList().block();
        assertThat(blocFonctionnelList).hasSize(databaseSizeBeforeCreate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(blocFonctionnelSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void checkLibelleBlocIsRequired() throws Exception {
        int databaseSizeBeforeTest = blocFonctionnelRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(blocFonctionnelSearchRepository.findAll().collectList().block());
        // set the field null
        blocFonctionnel.setLibelleBloc(null);

        // Create the BlocFonctionnel, which fails.
        BlocFonctionnelDTO blocFonctionnelDTO = blocFonctionnelMapper.toDto(blocFonctionnel);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(blocFonctionnelDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<BlocFonctionnel> blocFonctionnelList = blocFonctionnelRepository.findAll().collectList().block();
        assertThat(blocFonctionnelList).hasSize(databaseSizeBeforeTest);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(blocFonctionnelSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void checkDateAjoutBlocIsRequired() throws Exception {
        int databaseSizeBeforeTest = blocFonctionnelRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(blocFonctionnelSearchRepository.findAll().collectList().block());
        // set the field null
        blocFonctionnel.setDateAjoutBloc(null);

        // Create the BlocFonctionnel, which fails.
        BlocFonctionnelDTO blocFonctionnelDTO = blocFonctionnelMapper.toDto(blocFonctionnel);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(blocFonctionnelDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<BlocFonctionnel> blocFonctionnelList = blocFonctionnelRepository.findAll().collectList().block();
        assertThat(blocFonctionnelList).hasSize(databaseSizeBeforeTest);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(blocFonctionnelSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void checkActifYNIsRequired() throws Exception {
        int databaseSizeBeforeTest = blocFonctionnelRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(blocFonctionnelSearchRepository.findAll().collectList().block());
        // set the field null
        blocFonctionnel.setActifYN(null);

        // Create the BlocFonctionnel, which fails.
        BlocFonctionnelDTO blocFonctionnelDTO = blocFonctionnelMapper.toDto(blocFonctionnel);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(blocFonctionnelDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<BlocFonctionnel> blocFonctionnelList = blocFonctionnelRepository.findAll().collectList().block();
        assertThat(blocFonctionnelList).hasSize(databaseSizeBeforeTest);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(blocFonctionnelSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void getAllBlocFonctionnels() {
        // Initialize the database
        blocFonctionnelRepository.save(blocFonctionnel).block();

        // Get all the blocFonctionnelList
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
            .value(hasItem(blocFonctionnel.getId().intValue()))
            .jsonPath("$.[*].libelleBloc")
            .value(hasItem(DEFAULT_LIBELLE_BLOC))
            .jsonPath("$.[*].dateAjoutBloc")
            .value(hasItem(DEFAULT_DATE_AJOUT_BLOC.toString()))
            .jsonPath("$.[*].actifYN")
            .value(hasItem(DEFAULT_ACTIF_YN.booleanValue()));
    }

    @Test
    void getBlocFonctionnel() {
        // Initialize the database
        blocFonctionnelRepository.save(blocFonctionnel).block();

        // Get the blocFonctionnel
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, blocFonctionnel.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(blocFonctionnel.getId().intValue()))
            .jsonPath("$.libelleBloc")
            .value(is(DEFAULT_LIBELLE_BLOC))
            .jsonPath("$.dateAjoutBloc")
            .value(is(DEFAULT_DATE_AJOUT_BLOC.toString()))
            .jsonPath("$.actifYN")
            .value(is(DEFAULT_ACTIF_YN.booleanValue()));
    }

    @Test
    void getNonExistingBlocFonctionnel() {
        // Get the blocFonctionnel
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_PROBLEM_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putExistingBlocFonctionnel() throws Exception {
        // Initialize the database
        blocFonctionnelRepository.save(blocFonctionnel).block();

        int databaseSizeBeforeUpdate = blocFonctionnelRepository.findAll().collectList().block().size();
        blocFonctionnelSearchRepository.save(blocFonctionnel).block();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(blocFonctionnelSearchRepository.findAll().collectList().block());

        // Update the blocFonctionnel
        BlocFonctionnel updatedBlocFonctionnel = blocFonctionnelRepository.findById(blocFonctionnel.getId()).block();
        updatedBlocFonctionnel.libelleBloc(UPDATED_LIBELLE_BLOC).dateAjoutBloc(UPDATED_DATE_AJOUT_BLOC).actifYN(UPDATED_ACTIF_YN);
        BlocFonctionnelDTO blocFonctionnelDTO = blocFonctionnelMapper.toDto(updatedBlocFonctionnel);

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, blocFonctionnelDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(blocFonctionnelDTO))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the BlocFonctionnel in the database
        List<BlocFonctionnel> blocFonctionnelList = blocFonctionnelRepository.findAll().collectList().block();
        assertThat(blocFonctionnelList).hasSize(databaseSizeBeforeUpdate);
        BlocFonctionnel testBlocFonctionnel = blocFonctionnelList.get(blocFonctionnelList.size() - 1);
        assertThat(testBlocFonctionnel.getLibelleBloc()).isEqualTo(UPDATED_LIBELLE_BLOC);
        assertThat(testBlocFonctionnel.getDateAjoutBloc()).isEqualTo(UPDATED_DATE_AJOUT_BLOC);
        assertThat(testBlocFonctionnel.getActifYN()).isEqualTo(UPDATED_ACTIF_YN);
        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(blocFonctionnelSearchRepository.findAll().collectList().block());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
                List<BlocFonctionnel> blocFonctionnelSearchList = IterableUtils.toList(
                    blocFonctionnelSearchRepository.findAll().collectList().block()
                );
                BlocFonctionnel testBlocFonctionnelSearch = blocFonctionnelSearchList.get(searchDatabaseSizeAfter - 1);
                assertThat(testBlocFonctionnelSearch.getLibelleBloc()).isEqualTo(UPDATED_LIBELLE_BLOC);
                assertThat(testBlocFonctionnelSearch.getDateAjoutBloc()).isEqualTo(UPDATED_DATE_AJOUT_BLOC);
                assertThat(testBlocFonctionnelSearch.getActifYN()).isEqualTo(UPDATED_ACTIF_YN);
            });
    }

    @Test
    void putNonExistingBlocFonctionnel() throws Exception {
        int databaseSizeBeforeUpdate = blocFonctionnelRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(blocFonctionnelSearchRepository.findAll().collectList().block());
        blocFonctionnel.setId(longCount.incrementAndGet());

        // Create the BlocFonctionnel
        BlocFonctionnelDTO blocFonctionnelDTO = blocFonctionnelMapper.toDto(blocFonctionnel);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, blocFonctionnelDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(blocFonctionnelDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the BlocFonctionnel in the database
        List<BlocFonctionnel> blocFonctionnelList = blocFonctionnelRepository.findAll().collectList().block();
        assertThat(blocFonctionnelList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(blocFonctionnelSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void putWithIdMismatchBlocFonctionnel() throws Exception {
        int databaseSizeBeforeUpdate = blocFonctionnelRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(blocFonctionnelSearchRepository.findAll().collectList().block());
        blocFonctionnel.setId(longCount.incrementAndGet());

        // Create the BlocFonctionnel
        BlocFonctionnelDTO blocFonctionnelDTO = blocFonctionnelMapper.toDto(blocFonctionnel);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, longCount.incrementAndGet())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(blocFonctionnelDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the BlocFonctionnel in the database
        List<BlocFonctionnel> blocFonctionnelList = blocFonctionnelRepository.findAll().collectList().block();
        assertThat(blocFonctionnelList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(blocFonctionnelSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void putWithMissingIdPathParamBlocFonctionnel() throws Exception {
        int databaseSizeBeforeUpdate = blocFonctionnelRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(blocFonctionnelSearchRepository.findAll().collectList().block());
        blocFonctionnel.setId(longCount.incrementAndGet());

        // Create the BlocFonctionnel
        BlocFonctionnelDTO blocFonctionnelDTO = blocFonctionnelMapper.toDto(blocFonctionnel);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(blocFonctionnelDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the BlocFonctionnel in the database
        List<BlocFonctionnel> blocFonctionnelList = blocFonctionnelRepository.findAll().collectList().block();
        assertThat(blocFonctionnelList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(blocFonctionnelSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void partialUpdateBlocFonctionnelWithPatch() throws Exception {
        // Initialize the database
        blocFonctionnelRepository.save(blocFonctionnel).block();

        int databaseSizeBeforeUpdate = blocFonctionnelRepository.findAll().collectList().block().size();

        // Update the blocFonctionnel using partial update
        BlocFonctionnel partialUpdatedBlocFonctionnel = new BlocFonctionnel();
        partialUpdatedBlocFonctionnel.setId(blocFonctionnel.getId());

        partialUpdatedBlocFonctionnel.actifYN(UPDATED_ACTIF_YN);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedBlocFonctionnel.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedBlocFonctionnel))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the BlocFonctionnel in the database
        List<BlocFonctionnel> blocFonctionnelList = blocFonctionnelRepository.findAll().collectList().block();
        assertThat(blocFonctionnelList).hasSize(databaseSizeBeforeUpdate);
        BlocFonctionnel testBlocFonctionnel = blocFonctionnelList.get(blocFonctionnelList.size() - 1);
        assertThat(testBlocFonctionnel.getLibelleBloc()).isEqualTo(DEFAULT_LIBELLE_BLOC);
        assertThat(testBlocFonctionnel.getDateAjoutBloc()).isEqualTo(DEFAULT_DATE_AJOUT_BLOC);
        assertThat(testBlocFonctionnel.getActifYN()).isEqualTo(UPDATED_ACTIF_YN);
    }

    @Test
    void fullUpdateBlocFonctionnelWithPatch() throws Exception {
        // Initialize the database
        blocFonctionnelRepository.save(blocFonctionnel).block();

        int databaseSizeBeforeUpdate = blocFonctionnelRepository.findAll().collectList().block().size();

        // Update the blocFonctionnel using partial update
        BlocFonctionnel partialUpdatedBlocFonctionnel = new BlocFonctionnel();
        partialUpdatedBlocFonctionnel.setId(blocFonctionnel.getId());

        partialUpdatedBlocFonctionnel.libelleBloc(UPDATED_LIBELLE_BLOC).dateAjoutBloc(UPDATED_DATE_AJOUT_BLOC).actifYN(UPDATED_ACTIF_YN);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedBlocFonctionnel.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedBlocFonctionnel))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the BlocFonctionnel in the database
        List<BlocFonctionnel> blocFonctionnelList = blocFonctionnelRepository.findAll().collectList().block();
        assertThat(blocFonctionnelList).hasSize(databaseSizeBeforeUpdate);
        BlocFonctionnel testBlocFonctionnel = blocFonctionnelList.get(blocFonctionnelList.size() - 1);
        assertThat(testBlocFonctionnel.getLibelleBloc()).isEqualTo(UPDATED_LIBELLE_BLOC);
        assertThat(testBlocFonctionnel.getDateAjoutBloc()).isEqualTo(UPDATED_DATE_AJOUT_BLOC);
        assertThat(testBlocFonctionnel.getActifYN()).isEqualTo(UPDATED_ACTIF_YN);
    }

    @Test
    void patchNonExistingBlocFonctionnel() throws Exception {
        int databaseSizeBeforeUpdate = blocFonctionnelRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(blocFonctionnelSearchRepository.findAll().collectList().block());
        blocFonctionnel.setId(longCount.incrementAndGet());

        // Create the BlocFonctionnel
        BlocFonctionnelDTO blocFonctionnelDTO = blocFonctionnelMapper.toDto(blocFonctionnel);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, blocFonctionnelDTO.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(blocFonctionnelDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the BlocFonctionnel in the database
        List<BlocFonctionnel> blocFonctionnelList = blocFonctionnelRepository.findAll().collectList().block();
        assertThat(blocFonctionnelList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(blocFonctionnelSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void patchWithIdMismatchBlocFonctionnel() throws Exception {
        int databaseSizeBeforeUpdate = blocFonctionnelRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(blocFonctionnelSearchRepository.findAll().collectList().block());
        blocFonctionnel.setId(longCount.incrementAndGet());

        // Create the BlocFonctionnel
        BlocFonctionnelDTO blocFonctionnelDTO = blocFonctionnelMapper.toDto(blocFonctionnel);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, longCount.incrementAndGet())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(blocFonctionnelDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the BlocFonctionnel in the database
        List<BlocFonctionnel> blocFonctionnelList = blocFonctionnelRepository.findAll().collectList().block();
        assertThat(blocFonctionnelList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(blocFonctionnelSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void patchWithMissingIdPathParamBlocFonctionnel() throws Exception {
        int databaseSizeBeforeUpdate = blocFonctionnelRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(blocFonctionnelSearchRepository.findAll().collectList().block());
        blocFonctionnel.setId(longCount.incrementAndGet());

        // Create the BlocFonctionnel
        BlocFonctionnelDTO blocFonctionnelDTO = blocFonctionnelMapper.toDto(blocFonctionnel);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(blocFonctionnelDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the BlocFonctionnel in the database
        List<BlocFonctionnel> blocFonctionnelList = blocFonctionnelRepository.findAll().collectList().block();
        assertThat(blocFonctionnelList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(blocFonctionnelSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void deleteBlocFonctionnel() {
        // Initialize the database
        blocFonctionnelRepository.save(blocFonctionnel).block();
        blocFonctionnelRepository.save(blocFonctionnel).block();
        blocFonctionnelSearchRepository.save(blocFonctionnel).block();

        int databaseSizeBeforeDelete = blocFonctionnelRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(blocFonctionnelSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeBefore).isEqualTo(databaseSizeBeforeDelete);

        // Delete the blocFonctionnel
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, blocFonctionnel.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        List<BlocFonctionnel> blocFonctionnelList = blocFonctionnelRepository.findAll().collectList().block();
        assertThat(blocFonctionnelList).hasSize(databaseSizeBeforeDelete - 1);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(blocFonctionnelSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore - 1);
    }

    @Test
    void searchBlocFonctionnel() {
        // Initialize the database
        blocFonctionnel = blocFonctionnelRepository.save(blocFonctionnel).block();
        blocFonctionnelSearchRepository.save(blocFonctionnel).block();

        // Search the blocFonctionnel
        webTestClient
            .get()
            .uri(ENTITY_SEARCH_API_URL + "?query=id:" + blocFonctionnel.getId())
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.[*].id")
            .value(hasItem(blocFonctionnel.getId().intValue()))
            .jsonPath("$.[*].libelleBloc")
            .value(hasItem(DEFAULT_LIBELLE_BLOC))
            .jsonPath("$.[*].dateAjoutBloc")
            .value(hasItem(DEFAULT_DATE_AJOUT_BLOC.toString()))
            .jsonPath("$.[*].actifYN")
            .value(hasItem(DEFAULT_ACTIF_YN.booleanValue()));
    }
}
