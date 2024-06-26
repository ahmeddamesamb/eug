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
import sn.ugb.gateway.domain.Profil;
import sn.ugb.gateway.repository.EntityManager;
import sn.ugb.gateway.repository.ProfilRepository;
import sn.ugb.gateway.repository.search.ProfilSearchRepository;
import sn.ugb.gateway.service.dto.ProfilDTO;
import sn.ugb.gateway.service.mapper.ProfilMapper;

/**
 * Integration tests for the {@link ProfilResource} REST controller.
 */
@IntegrationTest
@AutoConfigureWebTestClient(timeout = IntegrationTest.DEFAULT_ENTITY_TIMEOUT)
@WithMockUser
class ProfilResourceIT {

    private static final String DEFAULT_LIBELLE = "AAAAAAAAAA";
    private static final String UPDATED_LIBELLE = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_DATE_AJOUT = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE_AJOUT = LocalDate.now(ZoneId.systemDefault());

    private static final Boolean DEFAULT_ACTIF_YN = false;
    private static final Boolean UPDATED_ACTIF_YN = true;

    private static final String ENTITY_API_URL = "/api/profils";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/profils/_search";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ProfilRepository profilRepository;

    @Autowired
    private ProfilMapper profilMapper;

    @Autowired
    private ProfilSearchRepository profilSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private WebTestClient webTestClient;

    private Profil profil;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Profil createEntity(EntityManager em) {
        Profil profil = new Profil().libelle(DEFAULT_LIBELLE).dateAjout(DEFAULT_DATE_AJOUT).actifYN(DEFAULT_ACTIF_YN);
        return profil;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Profil createUpdatedEntity(EntityManager em) {
        Profil profil = new Profil().libelle(UPDATED_LIBELLE).dateAjout(UPDATED_DATE_AJOUT).actifYN(UPDATED_ACTIF_YN);
        return profil;
    }

    public static void deleteEntities(EntityManager em) {
        try {
            em.deleteAll(Profil.class).block();
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
        profilSearchRepository.deleteAll().block();
        assertThat(profilSearchRepository.count().block()).isEqualTo(0);
    }

    @BeforeEach
    public void setupCsrf() {
        webTestClient = webTestClient.mutateWith(csrf());
    }

    @BeforeEach
    public void initTest() {
        deleteEntities(em);
        profil = createEntity(em);
    }

    @Test
    void createProfil() throws Exception {
        int databaseSizeBeforeCreate = profilRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(profilSearchRepository.findAll().collectList().block());
        // Create the Profil
        ProfilDTO profilDTO = profilMapper.toDto(profil);
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(profilDTO))
            .exchange()
            .expectStatus()
            .isCreated();

        // Validate the Profil in the database
        List<Profil> profilList = profilRepository.findAll().collectList().block();
        assertThat(profilList).hasSize(databaseSizeBeforeCreate + 1);
        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(profilSearchRepository.findAll().collectList().block());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore + 1);
            });
        Profil testProfil = profilList.get(profilList.size() - 1);
        assertThat(testProfil.getLibelle()).isEqualTo(DEFAULT_LIBELLE);
        assertThat(testProfil.getDateAjout()).isEqualTo(DEFAULT_DATE_AJOUT);
        assertThat(testProfil.getActifYN()).isEqualTo(DEFAULT_ACTIF_YN);
    }

    @Test
    void createProfilWithExistingId() throws Exception {
        // Create the Profil with an existing ID
        profil.setId(1L);
        ProfilDTO profilDTO = profilMapper.toDto(profil);

        int databaseSizeBeforeCreate = profilRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(profilSearchRepository.findAll().collectList().block());

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(profilDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Profil in the database
        List<Profil> profilList = profilRepository.findAll().collectList().block();
        assertThat(profilList).hasSize(databaseSizeBeforeCreate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(profilSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void checkLibelleIsRequired() throws Exception {
        int databaseSizeBeforeTest = profilRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(profilSearchRepository.findAll().collectList().block());
        // set the field null
        profil.setLibelle(null);

        // Create the Profil, which fails.
        ProfilDTO profilDTO = profilMapper.toDto(profil);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(profilDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<Profil> profilList = profilRepository.findAll().collectList().block();
        assertThat(profilList).hasSize(databaseSizeBeforeTest);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(profilSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void checkDateAjoutIsRequired() throws Exception {
        int databaseSizeBeforeTest = profilRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(profilSearchRepository.findAll().collectList().block());
        // set the field null
        profil.setDateAjout(null);

        // Create the Profil, which fails.
        ProfilDTO profilDTO = profilMapper.toDto(profil);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(profilDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<Profil> profilList = profilRepository.findAll().collectList().block();
        assertThat(profilList).hasSize(databaseSizeBeforeTest);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(profilSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void checkActifYNIsRequired() throws Exception {
        int databaseSizeBeforeTest = profilRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(profilSearchRepository.findAll().collectList().block());
        // set the field null
        profil.setActifYN(null);

        // Create the Profil, which fails.
        ProfilDTO profilDTO = profilMapper.toDto(profil);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(profilDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<Profil> profilList = profilRepository.findAll().collectList().block();
        assertThat(profilList).hasSize(databaseSizeBeforeTest);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(profilSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void getAllProfils() {
        // Initialize the database
        profilRepository.save(profil).block();

        // Get all the profilList
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
            .value(hasItem(profil.getId().intValue()))
            .jsonPath("$.[*].libelle")
            .value(hasItem(DEFAULT_LIBELLE))
            .jsonPath("$.[*].dateAjout")
            .value(hasItem(DEFAULT_DATE_AJOUT.toString()))
            .jsonPath("$.[*].actifYN")
            .value(hasItem(DEFAULT_ACTIF_YN.booleanValue()));
    }

    @Test
    void getProfil() {
        // Initialize the database
        profilRepository.save(profil).block();

        // Get the profil
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, profil.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(profil.getId().intValue()))
            .jsonPath("$.libelle")
            .value(is(DEFAULT_LIBELLE))
            .jsonPath("$.dateAjout")
            .value(is(DEFAULT_DATE_AJOUT.toString()))
            .jsonPath("$.actifYN")
            .value(is(DEFAULT_ACTIF_YN.booleanValue()));
    }

    @Test
    void getNonExistingProfil() {
        // Get the profil
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_PROBLEM_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putExistingProfil() throws Exception {
        // Initialize the database
        profilRepository.save(profil).block();

        int databaseSizeBeforeUpdate = profilRepository.findAll().collectList().block().size();
        profilSearchRepository.save(profil).block();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(profilSearchRepository.findAll().collectList().block());

        // Update the profil
        Profil updatedProfil = profilRepository.findById(profil.getId()).block();
        updatedProfil.libelle(UPDATED_LIBELLE).dateAjout(UPDATED_DATE_AJOUT).actifYN(UPDATED_ACTIF_YN);
        ProfilDTO profilDTO = profilMapper.toDto(updatedProfil);

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, profilDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(profilDTO))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Profil in the database
        List<Profil> profilList = profilRepository.findAll().collectList().block();
        assertThat(profilList).hasSize(databaseSizeBeforeUpdate);
        Profil testProfil = profilList.get(profilList.size() - 1);
        assertThat(testProfil.getLibelle()).isEqualTo(UPDATED_LIBELLE);
        assertThat(testProfil.getDateAjout()).isEqualTo(UPDATED_DATE_AJOUT);
        assertThat(testProfil.getActifYN()).isEqualTo(UPDATED_ACTIF_YN);
        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(profilSearchRepository.findAll().collectList().block());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
                List<Profil> profilSearchList = IterableUtils.toList(profilSearchRepository.findAll().collectList().block());
                Profil testProfilSearch = profilSearchList.get(searchDatabaseSizeAfter - 1);
                assertThat(testProfilSearch.getLibelle()).isEqualTo(UPDATED_LIBELLE);
                assertThat(testProfilSearch.getDateAjout()).isEqualTo(UPDATED_DATE_AJOUT);
                assertThat(testProfilSearch.getActifYN()).isEqualTo(UPDATED_ACTIF_YN);
            });
    }

    @Test
    void putNonExistingProfil() throws Exception {
        int databaseSizeBeforeUpdate = profilRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(profilSearchRepository.findAll().collectList().block());
        profil.setId(longCount.incrementAndGet());

        // Create the Profil
        ProfilDTO profilDTO = profilMapper.toDto(profil);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, profilDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(profilDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Profil in the database
        List<Profil> profilList = profilRepository.findAll().collectList().block();
        assertThat(profilList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(profilSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void putWithIdMismatchProfil() throws Exception {
        int databaseSizeBeforeUpdate = profilRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(profilSearchRepository.findAll().collectList().block());
        profil.setId(longCount.incrementAndGet());

        // Create the Profil
        ProfilDTO profilDTO = profilMapper.toDto(profil);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, longCount.incrementAndGet())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(profilDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Profil in the database
        List<Profil> profilList = profilRepository.findAll().collectList().block();
        assertThat(profilList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(profilSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void putWithMissingIdPathParamProfil() throws Exception {
        int databaseSizeBeforeUpdate = profilRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(profilSearchRepository.findAll().collectList().block());
        profil.setId(longCount.incrementAndGet());

        // Create the Profil
        ProfilDTO profilDTO = profilMapper.toDto(profil);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(profilDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the Profil in the database
        List<Profil> profilList = profilRepository.findAll().collectList().block();
        assertThat(profilList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(profilSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void partialUpdateProfilWithPatch() throws Exception {
        // Initialize the database
        profilRepository.save(profil).block();

        int databaseSizeBeforeUpdate = profilRepository.findAll().collectList().block().size();

        // Update the profil using partial update
        Profil partialUpdatedProfil = new Profil();
        partialUpdatedProfil.setId(profil.getId());

        partialUpdatedProfil.dateAjout(UPDATED_DATE_AJOUT).actifYN(UPDATED_ACTIF_YN);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedProfil.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedProfil))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Profil in the database
        List<Profil> profilList = profilRepository.findAll().collectList().block();
        assertThat(profilList).hasSize(databaseSizeBeforeUpdate);
        Profil testProfil = profilList.get(profilList.size() - 1);
        assertThat(testProfil.getLibelle()).isEqualTo(DEFAULT_LIBELLE);
        assertThat(testProfil.getDateAjout()).isEqualTo(UPDATED_DATE_AJOUT);
        assertThat(testProfil.getActifYN()).isEqualTo(UPDATED_ACTIF_YN);
    }

    @Test
    void fullUpdateProfilWithPatch() throws Exception {
        // Initialize the database
        profilRepository.save(profil).block();

        int databaseSizeBeforeUpdate = profilRepository.findAll().collectList().block().size();

        // Update the profil using partial update
        Profil partialUpdatedProfil = new Profil();
        partialUpdatedProfil.setId(profil.getId());

        partialUpdatedProfil.libelle(UPDATED_LIBELLE).dateAjout(UPDATED_DATE_AJOUT).actifYN(UPDATED_ACTIF_YN);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedProfil.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedProfil))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Profil in the database
        List<Profil> profilList = profilRepository.findAll().collectList().block();
        assertThat(profilList).hasSize(databaseSizeBeforeUpdate);
        Profil testProfil = profilList.get(profilList.size() - 1);
        assertThat(testProfil.getLibelle()).isEqualTo(UPDATED_LIBELLE);
        assertThat(testProfil.getDateAjout()).isEqualTo(UPDATED_DATE_AJOUT);
        assertThat(testProfil.getActifYN()).isEqualTo(UPDATED_ACTIF_YN);
    }

    @Test
    void patchNonExistingProfil() throws Exception {
        int databaseSizeBeforeUpdate = profilRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(profilSearchRepository.findAll().collectList().block());
        profil.setId(longCount.incrementAndGet());

        // Create the Profil
        ProfilDTO profilDTO = profilMapper.toDto(profil);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, profilDTO.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(profilDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Profil in the database
        List<Profil> profilList = profilRepository.findAll().collectList().block();
        assertThat(profilList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(profilSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void patchWithIdMismatchProfil() throws Exception {
        int databaseSizeBeforeUpdate = profilRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(profilSearchRepository.findAll().collectList().block());
        profil.setId(longCount.incrementAndGet());

        // Create the Profil
        ProfilDTO profilDTO = profilMapper.toDto(profil);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, longCount.incrementAndGet())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(profilDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Profil in the database
        List<Profil> profilList = profilRepository.findAll().collectList().block();
        assertThat(profilList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(profilSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void patchWithMissingIdPathParamProfil() throws Exception {
        int databaseSizeBeforeUpdate = profilRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(profilSearchRepository.findAll().collectList().block());
        profil.setId(longCount.incrementAndGet());

        // Create the Profil
        ProfilDTO profilDTO = profilMapper.toDto(profil);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(profilDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the Profil in the database
        List<Profil> profilList = profilRepository.findAll().collectList().block();
        assertThat(profilList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(profilSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void deleteProfil() {
        // Initialize the database
        profilRepository.save(profil).block();
        profilRepository.save(profil).block();
        profilSearchRepository.save(profil).block();

        int databaseSizeBeforeDelete = profilRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(profilSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeBefore).isEqualTo(databaseSizeBeforeDelete);

        // Delete the profil
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, profil.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        List<Profil> profilList = profilRepository.findAll().collectList().block();
        assertThat(profilList).hasSize(databaseSizeBeforeDelete - 1);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(profilSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore - 1);
    }

    @Test
    void searchProfil() {
        // Initialize the database
        profil = profilRepository.save(profil).block();
        profilSearchRepository.save(profil).block();

        // Search the profil
        webTestClient
            .get()
            .uri(ENTITY_SEARCH_API_URL + "?query=id:" + profil.getId())
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.[*].id")
            .value(hasItem(profil.getId().intValue()))
            .jsonPath("$.[*].libelle")
            .value(hasItem(DEFAULT_LIBELLE))
            .jsonPath("$.[*].dateAjout")
            .value(hasItem(DEFAULT_DATE_AJOUT.toString()))
            .jsonPath("$.[*].actifYN")
            .value(hasItem(DEFAULT_ACTIF_YN.booleanValue()));
    }
}
