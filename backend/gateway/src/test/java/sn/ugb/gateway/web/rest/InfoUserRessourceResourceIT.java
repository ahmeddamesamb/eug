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
import sn.ugb.gateway.domain.InfoUserRessource;
import sn.ugb.gateway.repository.EntityManager;
import sn.ugb.gateway.repository.InfoUserRessourceRepository;
import sn.ugb.gateway.repository.search.InfoUserRessourceSearchRepository;
import sn.ugb.gateway.service.dto.InfoUserRessourceDTO;
import sn.ugb.gateway.service.mapper.InfoUserRessourceMapper;

/**
 * Integration tests for the {@link InfoUserRessourceResource} REST controller.
 */
@IntegrationTest
@AutoConfigureWebTestClient(timeout = IntegrationTest.DEFAULT_ENTITY_TIMEOUT)
@WithMockUser
class InfoUserRessourceResourceIT {

    private static final LocalDate DEFAULT_DATE_AJOUT = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE_AJOUT = LocalDate.now(ZoneId.systemDefault());

    private static final Boolean DEFAULT_EN_COURS_YN = false;
    private static final Boolean UPDATED_EN_COURS_YN = true;

    private static final String ENTITY_API_URL = "/api/info-user-ressources";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/info-user-ressources/_search";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private InfoUserRessourceRepository infoUserRessourceRepository;

    @Autowired
    private InfoUserRessourceMapper infoUserRessourceMapper;

    @Autowired
    private InfoUserRessourceSearchRepository infoUserRessourceSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private WebTestClient webTestClient;

    private InfoUserRessource infoUserRessource;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static InfoUserRessource createEntity(EntityManager em) {
        InfoUserRessource infoUserRessource = new InfoUserRessource().dateAjout(DEFAULT_DATE_AJOUT).enCoursYN(DEFAULT_EN_COURS_YN);
        return infoUserRessource;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static InfoUserRessource createUpdatedEntity(EntityManager em) {
        InfoUserRessource infoUserRessource = new InfoUserRessource().dateAjout(UPDATED_DATE_AJOUT).enCoursYN(UPDATED_EN_COURS_YN);
        return infoUserRessource;
    }

    public static void deleteEntities(EntityManager em) {
        try {
            em.deleteAll(InfoUserRessource.class).block();
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
        infoUserRessourceSearchRepository.deleteAll().block();
        assertThat(infoUserRessourceSearchRepository.count().block()).isEqualTo(0);
    }

    @BeforeEach
    public void setupCsrf() {
        webTestClient = webTestClient.mutateWith(csrf());
    }

    @BeforeEach
    public void initTest() {
        deleteEntities(em);
        infoUserRessource = createEntity(em);
    }

    @Test
    void createInfoUserRessource() throws Exception {
        int databaseSizeBeforeCreate = infoUserRessourceRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(infoUserRessourceSearchRepository.findAll().collectList().block());
        // Create the InfoUserRessource
        InfoUserRessourceDTO infoUserRessourceDTO = infoUserRessourceMapper.toDto(infoUserRessource);
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(infoUserRessourceDTO))
            .exchange()
            .expectStatus()
            .isCreated();

        // Validate the InfoUserRessource in the database
        List<InfoUserRessource> infoUserRessourceList = infoUserRessourceRepository.findAll().collectList().block();
        assertThat(infoUserRessourceList).hasSize(databaseSizeBeforeCreate + 1);
        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(infoUserRessourceSearchRepository.findAll().collectList().block());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore + 1);
            });
        InfoUserRessource testInfoUserRessource = infoUserRessourceList.get(infoUserRessourceList.size() - 1);
        assertThat(testInfoUserRessource.getDateAjout()).isEqualTo(DEFAULT_DATE_AJOUT);
        assertThat(testInfoUserRessource.getEnCoursYN()).isEqualTo(DEFAULT_EN_COURS_YN);
    }

    @Test
    void createInfoUserRessourceWithExistingId() throws Exception {
        // Create the InfoUserRessource with an existing ID
        infoUserRessource.setId(1L);
        InfoUserRessourceDTO infoUserRessourceDTO = infoUserRessourceMapper.toDto(infoUserRessource);

        int databaseSizeBeforeCreate = infoUserRessourceRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(infoUserRessourceSearchRepository.findAll().collectList().block());

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(infoUserRessourceDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the InfoUserRessource in the database
        List<InfoUserRessource> infoUserRessourceList = infoUserRessourceRepository.findAll().collectList().block();
        assertThat(infoUserRessourceList).hasSize(databaseSizeBeforeCreate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(infoUserRessourceSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void checkDateAjoutIsRequired() throws Exception {
        int databaseSizeBeforeTest = infoUserRessourceRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(infoUserRessourceSearchRepository.findAll().collectList().block());
        // set the field null
        infoUserRessource.setDateAjout(null);

        // Create the InfoUserRessource, which fails.
        InfoUserRessourceDTO infoUserRessourceDTO = infoUserRessourceMapper.toDto(infoUserRessource);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(infoUserRessourceDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<InfoUserRessource> infoUserRessourceList = infoUserRessourceRepository.findAll().collectList().block();
        assertThat(infoUserRessourceList).hasSize(databaseSizeBeforeTest);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(infoUserRessourceSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void checkEnCoursYNIsRequired() throws Exception {
        int databaseSizeBeforeTest = infoUserRessourceRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(infoUserRessourceSearchRepository.findAll().collectList().block());
        // set the field null
        infoUserRessource.setEnCoursYN(null);

        // Create the InfoUserRessource, which fails.
        InfoUserRessourceDTO infoUserRessourceDTO = infoUserRessourceMapper.toDto(infoUserRessource);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(infoUserRessourceDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<InfoUserRessource> infoUserRessourceList = infoUserRessourceRepository.findAll().collectList().block();
        assertThat(infoUserRessourceList).hasSize(databaseSizeBeforeTest);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(infoUserRessourceSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void getAllInfoUserRessources() {
        // Initialize the database
        infoUserRessourceRepository.save(infoUserRessource).block();

        // Get all the infoUserRessourceList
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
            .value(hasItem(infoUserRessource.getId().intValue()))
            .jsonPath("$.[*].dateAjout")
            .value(hasItem(DEFAULT_DATE_AJOUT.toString()))
            .jsonPath("$.[*].enCoursYN")
            .value(hasItem(DEFAULT_EN_COURS_YN.booleanValue()));
    }

    @Test
    void getInfoUserRessource() {
        // Initialize the database
        infoUserRessourceRepository.save(infoUserRessource).block();

        // Get the infoUserRessource
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, infoUserRessource.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(infoUserRessource.getId().intValue()))
            .jsonPath("$.dateAjout")
            .value(is(DEFAULT_DATE_AJOUT.toString()))
            .jsonPath("$.enCoursYN")
            .value(is(DEFAULT_EN_COURS_YN.booleanValue()));
    }

    @Test
    void getNonExistingInfoUserRessource() {
        // Get the infoUserRessource
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_PROBLEM_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putExistingInfoUserRessource() throws Exception {
        // Initialize the database
        infoUserRessourceRepository.save(infoUserRessource).block();

        int databaseSizeBeforeUpdate = infoUserRessourceRepository.findAll().collectList().block().size();
        infoUserRessourceSearchRepository.save(infoUserRessource).block();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(infoUserRessourceSearchRepository.findAll().collectList().block());

        // Update the infoUserRessource
        InfoUserRessource updatedInfoUserRessource = infoUserRessourceRepository.findById(infoUserRessource.getId()).block();
        updatedInfoUserRessource.dateAjout(UPDATED_DATE_AJOUT).enCoursYN(UPDATED_EN_COURS_YN);
        InfoUserRessourceDTO infoUserRessourceDTO = infoUserRessourceMapper.toDto(updatedInfoUserRessource);

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, infoUserRessourceDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(infoUserRessourceDTO))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the InfoUserRessource in the database
        List<InfoUserRessource> infoUserRessourceList = infoUserRessourceRepository.findAll().collectList().block();
        assertThat(infoUserRessourceList).hasSize(databaseSizeBeforeUpdate);
        InfoUserRessource testInfoUserRessource = infoUserRessourceList.get(infoUserRessourceList.size() - 1);
        assertThat(testInfoUserRessource.getDateAjout()).isEqualTo(UPDATED_DATE_AJOUT);
        assertThat(testInfoUserRessource.getEnCoursYN()).isEqualTo(UPDATED_EN_COURS_YN);
        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(infoUserRessourceSearchRepository.findAll().collectList().block());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
                List<InfoUserRessource> infoUserRessourceSearchList = IterableUtils.toList(
                    infoUserRessourceSearchRepository.findAll().collectList().block()
                );
                InfoUserRessource testInfoUserRessourceSearch = infoUserRessourceSearchList.get(searchDatabaseSizeAfter - 1);
                assertThat(testInfoUserRessourceSearch.getDateAjout()).isEqualTo(UPDATED_DATE_AJOUT);
                assertThat(testInfoUserRessourceSearch.getEnCoursYN()).isEqualTo(UPDATED_EN_COURS_YN);
            });
    }

    @Test
    void putNonExistingInfoUserRessource() throws Exception {
        int databaseSizeBeforeUpdate = infoUserRessourceRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(infoUserRessourceSearchRepository.findAll().collectList().block());
        infoUserRessource.setId(longCount.incrementAndGet());

        // Create the InfoUserRessource
        InfoUserRessourceDTO infoUserRessourceDTO = infoUserRessourceMapper.toDto(infoUserRessource);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, infoUserRessourceDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(infoUserRessourceDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the InfoUserRessource in the database
        List<InfoUserRessource> infoUserRessourceList = infoUserRessourceRepository.findAll().collectList().block();
        assertThat(infoUserRessourceList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(infoUserRessourceSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void putWithIdMismatchInfoUserRessource() throws Exception {
        int databaseSizeBeforeUpdate = infoUserRessourceRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(infoUserRessourceSearchRepository.findAll().collectList().block());
        infoUserRessource.setId(longCount.incrementAndGet());

        // Create the InfoUserRessource
        InfoUserRessourceDTO infoUserRessourceDTO = infoUserRessourceMapper.toDto(infoUserRessource);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, longCount.incrementAndGet())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(infoUserRessourceDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the InfoUserRessource in the database
        List<InfoUserRessource> infoUserRessourceList = infoUserRessourceRepository.findAll().collectList().block();
        assertThat(infoUserRessourceList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(infoUserRessourceSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void putWithMissingIdPathParamInfoUserRessource() throws Exception {
        int databaseSizeBeforeUpdate = infoUserRessourceRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(infoUserRessourceSearchRepository.findAll().collectList().block());
        infoUserRessource.setId(longCount.incrementAndGet());

        // Create the InfoUserRessource
        InfoUserRessourceDTO infoUserRessourceDTO = infoUserRessourceMapper.toDto(infoUserRessource);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(infoUserRessourceDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the InfoUserRessource in the database
        List<InfoUserRessource> infoUserRessourceList = infoUserRessourceRepository.findAll().collectList().block();
        assertThat(infoUserRessourceList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(infoUserRessourceSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void partialUpdateInfoUserRessourceWithPatch() throws Exception {
        // Initialize the database
        infoUserRessourceRepository.save(infoUserRessource).block();

        int databaseSizeBeforeUpdate = infoUserRessourceRepository.findAll().collectList().block().size();

        // Update the infoUserRessource using partial update
        InfoUserRessource partialUpdatedInfoUserRessource = new InfoUserRessource();
        partialUpdatedInfoUserRessource.setId(infoUserRessource.getId());

        partialUpdatedInfoUserRessource.dateAjout(UPDATED_DATE_AJOUT);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedInfoUserRessource.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedInfoUserRessource))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the InfoUserRessource in the database
        List<InfoUserRessource> infoUserRessourceList = infoUserRessourceRepository.findAll().collectList().block();
        assertThat(infoUserRessourceList).hasSize(databaseSizeBeforeUpdate);
        InfoUserRessource testInfoUserRessource = infoUserRessourceList.get(infoUserRessourceList.size() - 1);
        assertThat(testInfoUserRessource.getDateAjout()).isEqualTo(UPDATED_DATE_AJOUT);
        assertThat(testInfoUserRessource.getEnCoursYN()).isEqualTo(DEFAULT_EN_COURS_YN);
    }

    @Test
    void fullUpdateInfoUserRessourceWithPatch() throws Exception {
        // Initialize the database
        infoUserRessourceRepository.save(infoUserRessource).block();

        int databaseSizeBeforeUpdate = infoUserRessourceRepository.findAll().collectList().block().size();

        // Update the infoUserRessource using partial update
        InfoUserRessource partialUpdatedInfoUserRessource = new InfoUserRessource();
        partialUpdatedInfoUserRessource.setId(infoUserRessource.getId());

        partialUpdatedInfoUserRessource.dateAjout(UPDATED_DATE_AJOUT).enCoursYN(UPDATED_EN_COURS_YN);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedInfoUserRessource.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedInfoUserRessource))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the InfoUserRessource in the database
        List<InfoUserRessource> infoUserRessourceList = infoUserRessourceRepository.findAll().collectList().block();
        assertThat(infoUserRessourceList).hasSize(databaseSizeBeforeUpdate);
        InfoUserRessource testInfoUserRessource = infoUserRessourceList.get(infoUserRessourceList.size() - 1);
        assertThat(testInfoUserRessource.getDateAjout()).isEqualTo(UPDATED_DATE_AJOUT);
        assertThat(testInfoUserRessource.getEnCoursYN()).isEqualTo(UPDATED_EN_COURS_YN);
    }

    @Test
    void patchNonExistingInfoUserRessource() throws Exception {
        int databaseSizeBeforeUpdate = infoUserRessourceRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(infoUserRessourceSearchRepository.findAll().collectList().block());
        infoUserRessource.setId(longCount.incrementAndGet());

        // Create the InfoUserRessource
        InfoUserRessourceDTO infoUserRessourceDTO = infoUserRessourceMapper.toDto(infoUserRessource);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, infoUserRessourceDTO.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(infoUserRessourceDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the InfoUserRessource in the database
        List<InfoUserRessource> infoUserRessourceList = infoUserRessourceRepository.findAll().collectList().block();
        assertThat(infoUserRessourceList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(infoUserRessourceSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void patchWithIdMismatchInfoUserRessource() throws Exception {
        int databaseSizeBeforeUpdate = infoUserRessourceRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(infoUserRessourceSearchRepository.findAll().collectList().block());
        infoUserRessource.setId(longCount.incrementAndGet());

        // Create the InfoUserRessource
        InfoUserRessourceDTO infoUserRessourceDTO = infoUserRessourceMapper.toDto(infoUserRessource);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, longCount.incrementAndGet())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(infoUserRessourceDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the InfoUserRessource in the database
        List<InfoUserRessource> infoUserRessourceList = infoUserRessourceRepository.findAll().collectList().block();
        assertThat(infoUserRessourceList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(infoUserRessourceSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void patchWithMissingIdPathParamInfoUserRessource() throws Exception {
        int databaseSizeBeforeUpdate = infoUserRessourceRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(infoUserRessourceSearchRepository.findAll().collectList().block());
        infoUserRessource.setId(longCount.incrementAndGet());

        // Create the InfoUserRessource
        InfoUserRessourceDTO infoUserRessourceDTO = infoUserRessourceMapper.toDto(infoUserRessource);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(infoUserRessourceDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the InfoUserRessource in the database
        List<InfoUserRessource> infoUserRessourceList = infoUserRessourceRepository.findAll().collectList().block();
        assertThat(infoUserRessourceList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(infoUserRessourceSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void deleteInfoUserRessource() {
        // Initialize the database
        infoUserRessourceRepository.save(infoUserRessource).block();
        infoUserRessourceRepository.save(infoUserRessource).block();
        infoUserRessourceSearchRepository.save(infoUserRessource).block();

        int databaseSizeBeforeDelete = infoUserRessourceRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(infoUserRessourceSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeBefore).isEqualTo(databaseSizeBeforeDelete);

        // Delete the infoUserRessource
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, infoUserRessource.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        List<InfoUserRessource> infoUserRessourceList = infoUserRessourceRepository.findAll().collectList().block();
        assertThat(infoUserRessourceList).hasSize(databaseSizeBeforeDelete - 1);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(infoUserRessourceSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore - 1);
    }

    @Test
    void searchInfoUserRessource() {
        // Initialize the database
        infoUserRessource = infoUserRessourceRepository.save(infoUserRessource).block();
        infoUserRessourceSearchRepository.save(infoUserRessource).block();

        // Search the infoUserRessource
        webTestClient
            .get()
            .uri(ENTITY_SEARCH_API_URL + "?query=id:" + infoUserRessource.getId())
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.[*].id")
            .value(hasItem(infoUserRessource.getId().intValue()))
            .jsonPath("$.[*].dateAjout")
            .value(hasItem(DEFAULT_DATE_AJOUT.toString()))
            .jsonPath("$.[*].enCoursYN")
            .value(hasItem(DEFAULT_EN_COURS_YN.booleanValue()));
    }
}
