package sn.ugb.gateway.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.reactive.server.SecurityMockServerConfigurers.csrf;

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
import sn.ugb.gateway.domain.Ressource;
import sn.ugb.gateway.repository.EntityManager;
import sn.ugb.gateway.repository.RessourceRepository;
import sn.ugb.gateway.repository.search.RessourceSearchRepository;
import sn.ugb.gateway.service.dto.RessourceDTO;
import sn.ugb.gateway.service.mapper.RessourceMapper;

/**
 * Integration tests for the {@link RessourceResource} REST controller.
 */
@IntegrationTest
@AutoConfigureWebTestClient(timeout = IntegrationTest.DEFAULT_ENTITY_TIMEOUT)
@WithMockUser
class RessourceResourceIT {

    private static final String DEFAULT_LIBELLE = "AAAAAAAAAA";
    private static final String UPDATED_LIBELLE = "BBBBBBBBBB";

    private static final Boolean DEFAULT_ACTIF_YN = false;
    private static final Boolean UPDATED_ACTIF_YN = true;

    private static final String ENTITY_API_URL = "/api/ressources";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/ressources/_search";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private RessourceRepository ressourceRepository;

    @Autowired
    private RessourceMapper ressourceMapper;

    @Autowired
    private RessourceSearchRepository ressourceSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private WebTestClient webTestClient;

    private Ressource ressource;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Ressource createEntity(EntityManager em) {
        Ressource ressource = new Ressource().libelle(DEFAULT_LIBELLE).actifYN(DEFAULT_ACTIF_YN);
        return ressource;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Ressource createUpdatedEntity(EntityManager em) {
        Ressource ressource = new Ressource().libelle(UPDATED_LIBELLE).actifYN(UPDATED_ACTIF_YN);
        return ressource;
    }

    public static void deleteEntities(EntityManager em) {
        try {
            em.deleteAll(Ressource.class).block();
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
        ressourceSearchRepository.deleteAll().block();
        assertThat(ressourceSearchRepository.count().block()).isEqualTo(0);
    }

    @BeforeEach
    public void setupCsrf() {
        webTestClient = webTestClient.mutateWith(csrf());
    }

    @BeforeEach
    public void initTest() {
        deleteEntities(em);
        ressource = createEntity(em);
    }

    @Test
    void createRessource() throws Exception {
        int databaseSizeBeforeCreate = ressourceRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(ressourceSearchRepository.findAll().collectList().block());
        // Create the Ressource
        RessourceDTO ressourceDTO = ressourceMapper.toDto(ressource);
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(ressourceDTO))
            .exchange()
            .expectStatus()
            .isCreated();

        // Validate the Ressource in the database
        List<Ressource> ressourceList = ressourceRepository.findAll().collectList().block();
        assertThat(ressourceList).hasSize(databaseSizeBeforeCreate + 1);
        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(ressourceSearchRepository.findAll().collectList().block());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore + 1);
            });
        Ressource testRessource = ressourceList.get(ressourceList.size() - 1);
        assertThat(testRessource.getLibelle()).isEqualTo(DEFAULT_LIBELLE);
        assertThat(testRessource.getActifYN()).isEqualTo(DEFAULT_ACTIF_YN);
    }

    @Test
    void createRessourceWithExistingId() throws Exception {
        // Create the Ressource with an existing ID
        ressource.setId(1L);
        RessourceDTO ressourceDTO = ressourceMapper.toDto(ressource);

        int databaseSizeBeforeCreate = ressourceRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(ressourceSearchRepository.findAll().collectList().block());

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(ressourceDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Ressource in the database
        List<Ressource> ressourceList = ressourceRepository.findAll().collectList().block();
        assertThat(ressourceList).hasSize(databaseSizeBeforeCreate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(ressourceSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void checkLibelleIsRequired() throws Exception {
        int databaseSizeBeforeTest = ressourceRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(ressourceSearchRepository.findAll().collectList().block());
        // set the field null
        ressource.setLibelle(null);

        // Create the Ressource, which fails.
        RessourceDTO ressourceDTO = ressourceMapper.toDto(ressource);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(ressourceDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<Ressource> ressourceList = ressourceRepository.findAll().collectList().block();
        assertThat(ressourceList).hasSize(databaseSizeBeforeTest);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(ressourceSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void checkActifYNIsRequired() throws Exception {
        int databaseSizeBeforeTest = ressourceRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(ressourceSearchRepository.findAll().collectList().block());
        // set the field null
        ressource.setActifYN(null);

        // Create the Ressource, which fails.
        RessourceDTO ressourceDTO = ressourceMapper.toDto(ressource);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(ressourceDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<Ressource> ressourceList = ressourceRepository.findAll().collectList().block();
        assertThat(ressourceList).hasSize(databaseSizeBeforeTest);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(ressourceSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void getAllRessources() {
        // Initialize the database
        ressourceRepository.save(ressource).block();

        // Get all the ressourceList
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
            .value(hasItem(ressource.getId().intValue()))
            .jsonPath("$.[*].libelle")
            .value(hasItem(DEFAULT_LIBELLE))
            .jsonPath("$.[*].actifYN")
            .value(hasItem(DEFAULT_ACTIF_YN.booleanValue()));
    }

    @Test
    void getRessource() {
        // Initialize the database
        ressourceRepository.save(ressource).block();

        // Get the ressource
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, ressource.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(ressource.getId().intValue()))
            .jsonPath("$.libelle")
            .value(is(DEFAULT_LIBELLE))
            .jsonPath("$.actifYN")
            .value(is(DEFAULT_ACTIF_YN.booleanValue()));
    }

    @Test
    void getNonExistingRessource() {
        // Get the ressource
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_PROBLEM_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putExistingRessource() throws Exception {
        // Initialize the database
        ressourceRepository.save(ressource).block();

        int databaseSizeBeforeUpdate = ressourceRepository.findAll().collectList().block().size();
        ressourceSearchRepository.save(ressource).block();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(ressourceSearchRepository.findAll().collectList().block());

        // Update the ressource
        Ressource updatedRessource = ressourceRepository.findById(ressource.getId()).block();
        updatedRessource.libelle(UPDATED_LIBELLE).actifYN(UPDATED_ACTIF_YN);
        RessourceDTO ressourceDTO = ressourceMapper.toDto(updatedRessource);

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, ressourceDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(ressourceDTO))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Ressource in the database
        List<Ressource> ressourceList = ressourceRepository.findAll().collectList().block();
        assertThat(ressourceList).hasSize(databaseSizeBeforeUpdate);
        Ressource testRessource = ressourceList.get(ressourceList.size() - 1);
        assertThat(testRessource.getLibelle()).isEqualTo(UPDATED_LIBELLE);
        assertThat(testRessource.getActifYN()).isEqualTo(UPDATED_ACTIF_YN);
        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(ressourceSearchRepository.findAll().collectList().block());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
                List<Ressource> ressourceSearchList = IterableUtils.toList(ressourceSearchRepository.findAll().collectList().block());
                Ressource testRessourceSearch = ressourceSearchList.get(searchDatabaseSizeAfter - 1);
                assertThat(testRessourceSearch.getLibelle()).isEqualTo(UPDATED_LIBELLE);
                assertThat(testRessourceSearch.getActifYN()).isEqualTo(UPDATED_ACTIF_YN);
            });
    }

    @Test
    void putNonExistingRessource() throws Exception {
        int databaseSizeBeforeUpdate = ressourceRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(ressourceSearchRepository.findAll().collectList().block());
        ressource.setId(longCount.incrementAndGet());

        // Create the Ressource
        RessourceDTO ressourceDTO = ressourceMapper.toDto(ressource);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, ressourceDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(ressourceDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Ressource in the database
        List<Ressource> ressourceList = ressourceRepository.findAll().collectList().block();
        assertThat(ressourceList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(ressourceSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void putWithIdMismatchRessource() throws Exception {
        int databaseSizeBeforeUpdate = ressourceRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(ressourceSearchRepository.findAll().collectList().block());
        ressource.setId(longCount.incrementAndGet());

        // Create the Ressource
        RessourceDTO ressourceDTO = ressourceMapper.toDto(ressource);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, longCount.incrementAndGet())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(ressourceDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Ressource in the database
        List<Ressource> ressourceList = ressourceRepository.findAll().collectList().block();
        assertThat(ressourceList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(ressourceSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void putWithMissingIdPathParamRessource() throws Exception {
        int databaseSizeBeforeUpdate = ressourceRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(ressourceSearchRepository.findAll().collectList().block());
        ressource.setId(longCount.incrementAndGet());

        // Create the Ressource
        RessourceDTO ressourceDTO = ressourceMapper.toDto(ressource);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(ressourceDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the Ressource in the database
        List<Ressource> ressourceList = ressourceRepository.findAll().collectList().block();
        assertThat(ressourceList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(ressourceSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void partialUpdateRessourceWithPatch() throws Exception {
        // Initialize the database
        ressourceRepository.save(ressource).block();

        int databaseSizeBeforeUpdate = ressourceRepository.findAll().collectList().block().size();

        // Update the ressource using partial update
        Ressource partialUpdatedRessource = new Ressource();
        partialUpdatedRessource.setId(ressource.getId());

        partialUpdatedRessource.libelle(UPDATED_LIBELLE);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedRessource.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedRessource))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Ressource in the database
        List<Ressource> ressourceList = ressourceRepository.findAll().collectList().block();
        assertThat(ressourceList).hasSize(databaseSizeBeforeUpdate);
        Ressource testRessource = ressourceList.get(ressourceList.size() - 1);
        assertThat(testRessource.getLibelle()).isEqualTo(UPDATED_LIBELLE);
        assertThat(testRessource.getActifYN()).isEqualTo(DEFAULT_ACTIF_YN);
    }

    @Test
    void fullUpdateRessourceWithPatch() throws Exception {
        // Initialize the database
        ressourceRepository.save(ressource).block();

        int databaseSizeBeforeUpdate = ressourceRepository.findAll().collectList().block().size();

        // Update the ressource using partial update
        Ressource partialUpdatedRessource = new Ressource();
        partialUpdatedRessource.setId(ressource.getId());

        partialUpdatedRessource.libelle(UPDATED_LIBELLE).actifYN(UPDATED_ACTIF_YN);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedRessource.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedRessource))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Ressource in the database
        List<Ressource> ressourceList = ressourceRepository.findAll().collectList().block();
        assertThat(ressourceList).hasSize(databaseSizeBeforeUpdate);
        Ressource testRessource = ressourceList.get(ressourceList.size() - 1);
        assertThat(testRessource.getLibelle()).isEqualTo(UPDATED_LIBELLE);
        assertThat(testRessource.getActifYN()).isEqualTo(UPDATED_ACTIF_YN);
    }

    @Test
    void patchNonExistingRessource() throws Exception {
        int databaseSizeBeforeUpdate = ressourceRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(ressourceSearchRepository.findAll().collectList().block());
        ressource.setId(longCount.incrementAndGet());

        // Create the Ressource
        RessourceDTO ressourceDTO = ressourceMapper.toDto(ressource);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, ressourceDTO.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(ressourceDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Ressource in the database
        List<Ressource> ressourceList = ressourceRepository.findAll().collectList().block();
        assertThat(ressourceList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(ressourceSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void patchWithIdMismatchRessource() throws Exception {
        int databaseSizeBeforeUpdate = ressourceRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(ressourceSearchRepository.findAll().collectList().block());
        ressource.setId(longCount.incrementAndGet());

        // Create the Ressource
        RessourceDTO ressourceDTO = ressourceMapper.toDto(ressource);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, longCount.incrementAndGet())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(ressourceDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Ressource in the database
        List<Ressource> ressourceList = ressourceRepository.findAll().collectList().block();
        assertThat(ressourceList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(ressourceSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void patchWithMissingIdPathParamRessource() throws Exception {
        int databaseSizeBeforeUpdate = ressourceRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(ressourceSearchRepository.findAll().collectList().block());
        ressource.setId(longCount.incrementAndGet());

        // Create the Ressource
        RessourceDTO ressourceDTO = ressourceMapper.toDto(ressource);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(ressourceDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the Ressource in the database
        List<Ressource> ressourceList = ressourceRepository.findAll().collectList().block();
        assertThat(ressourceList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(ressourceSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void deleteRessource() {
        // Initialize the database
        ressourceRepository.save(ressource).block();
        ressourceRepository.save(ressource).block();
        ressourceSearchRepository.save(ressource).block();

        int databaseSizeBeforeDelete = ressourceRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(ressourceSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeBefore).isEqualTo(databaseSizeBeforeDelete);

        // Delete the ressource
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, ressource.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        List<Ressource> ressourceList = ressourceRepository.findAll().collectList().block();
        assertThat(ressourceList).hasSize(databaseSizeBeforeDelete - 1);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(ressourceSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore - 1);
    }

    @Test
    void searchRessource() {
        // Initialize the database
        ressource = ressourceRepository.save(ressource).block();
        ressourceSearchRepository.save(ressource).block();

        // Search the ressource
        webTestClient
            .get()
            .uri(ENTITY_SEARCH_API_URL + "?query=id:" + ressource.getId())
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.[*].id")
            .value(hasItem(ressource.getId().intValue()))
            .jsonPath("$.[*].libelle")
            .value(hasItem(DEFAULT_LIBELLE))
            .jsonPath("$.[*].actifYN")
            .value(hasItem(DEFAULT_ACTIF_YN.booleanValue()));
    }
}
