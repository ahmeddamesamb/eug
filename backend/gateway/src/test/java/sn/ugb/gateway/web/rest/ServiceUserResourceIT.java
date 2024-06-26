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
import sn.ugb.gateway.domain.ServiceUser;
import sn.ugb.gateway.repository.EntityManager;
import sn.ugb.gateway.repository.ServiceUserRepository;
import sn.ugb.gateway.repository.search.ServiceUserSearchRepository;
import sn.ugb.gateway.service.dto.ServiceUserDTO;
import sn.ugb.gateway.service.mapper.ServiceUserMapper;

/**
 * Integration tests for the {@link ServiceUserResource} REST controller.
 */
@IntegrationTest
@AutoConfigureWebTestClient(timeout = IntegrationTest.DEFAULT_ENTITY_TIMEOUT)
@WithMockUser
class ServiceUserResourceIT {

    private static final String DEFAULT_NOM = "AAAAAAAAAA";
    private static final String UPDATED_NOM = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_DATE_AJOUT = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE_AJOUT = LocalDate.now(ZoneId.systemDefault());

    private static final Boolean DEFAULT_ACTIF_YN = false;
    private static final Boolean UPDATED_ACTIF_YN = true;

    private static final String ENTITY_API_URL = "/api/service-users";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/service-users/_search";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ServiceUserRepository serviceUserRepository;

    @Autowired
    private ServiceUserMapper serviceUserMapper;

    @Autowired
    private ServiceUserSearchRepository serviceUserSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private WebTestClient webTestClient;

    private ServiceUser serviceUser;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ServiceUser createEntity(EntityManager em) {
        ServiceUser serviceUser = new ServiceUser().nom(DEFAULT_NOM).dateAjout(DEFAULT_DATE_AJOUT).actifYN(DEFAULT_ACTIF_YN);
        return serviceUser;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ServiceUser createUpdatedEntity(EntityManager em) {
        ServiceUser serviceUser = new ServiceUser().nom(UPDATED_NOM).dateAjout(UPDATED_DATE_AJOUT).actifYN(UPDATED_ACTIF_YN);
        return serviceUser;
    }

    public static void deleteEntities(EntityManager em) {
        try {
            em.deleteAll(ServiceUser.class).block();
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
        serviceUserSearchRepository.deleteAll().block();
        assertThat(serviceUserSearchRepository.count().block()).isEqualTo(0);
    }

    @BeforeEach
    public void setupCsrf() {
        webTestClient = webTestClient.mutateWith(csrf());
    }

    @BeforeEach
    public void initTest() {
        deleteEntities(em);
        serviceUser = createEntity(em);
    }

    @Test
    void createServiceUser() throws Exception {
        int databaseSizeBeforeCreate = serviceUserRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(serviceUserSearchRepository.findAll().collectList().block());
        // Create the ServiceUser
        ServiceUserDTO serviceUserDTO = serviceUserMapper.toDto(serviceUser);
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(serviceUserDTO))
            .exchange()
            .expectStatus()
            .isCreated();

        // Validate the ServiceUser in the database
        List<ServiceUser> serviceUserList = serviceUserRepository.findAll().collectList().block();
        assertThat(serviceUserList).hasSize(databaseSizeBeforeCreate + 1);
        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(serviceUserSearchRepository.findAll().collectList().block());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore + 1);
            });
        ServiceUser testServiceUser = serviceUserList.get(serviceUserList.size() - 1);
        assertThat(testServiceUser.getNom()).isEqualTo(DEFAULT_NOM);
        assertThat(testServiceUser.getDateAjout()).isEqualTo(DEFAULT_DATE_AJOUT);
        assertThat(testServiceUser.getActifYN()).isEqualTo(DEFAULT_ACTIF_YN);
    }

    @Test
    void createServiceUserWithExistingId() throws Exception {
        // Create the ServiceUser with an existing ID
        serviceUser.setId(1L);
        ServiceUserDTO serviceUserDTO = serviceUserMapper.toDto(serviceUser);

        int databaseSizeBeforeCreate = serviceUserRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(serviceUserSearchRepository.findAll().collectList().block());

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(serviceUserDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the ServiceUser in the database
        List<ServiceUser> serviceUserList = serviceUserRepository.findAll().collectList().block();
        assertThat(serviceUserList).hasSize(databaseSizeBeforeCreate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(serviceUserSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void checkNomIsRequired() throws Exception {
        int databaseSizeBeforeTest = serviceUserRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(serviceUserSearchRepository.findAll().collectList().block());
        // set the field null
        serviceUser.setNom(null);

        // Create the ServiceUser, which fails.
        ServiceUserDTO serviceUserDTO = serviceUserMapper.toDto(serviceUser);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(serviceUserDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<ServiceUser> serviceUserList = serviceUserRepository.findAll().collectList().block();
        assertThat(serviceUserList).hasSize(databaseSizeBeforeTest);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(serviceUserSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void checkDateAjoutIsRequired() throws Exception {
        int databaseSizeBeforeTest = serviceUserRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(serviceUserSearchRepository.findAll().collectList().block());
        // set the field null
        serviceUser.setDateAjout(null);

        // Create the ServiceUser, which fails.
        ServiceUserDTO serviceUserDTO = serviceUserMapper.toDto(serviceUser);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(serviceUserDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<ServiceUser> serviceUserList = serviceUserRepository.findAll().collectList().block();
        assertThat(serviceUserList).hasSize(databaseSizeBeforeTest);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(serviceUserSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void checkActifYNIsRequired() throws Exception {
        int databaseSizeBeforeTest = serviceUserRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(serviceUserSearchRepository.findAll().collectList().block());
        // set the field null
        serviceUser.setActifYN(null);

        // Create the ServiceUser, which fails.
        ServiceUserDTO serviceUserDTO = serviceUserMapper.toDto(serviceUser);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(serviceUserDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<ServiceUser> serviceUserList = serviceUserRepository.findAll().collectList().block();
        assertThat(serviceUserList).hasSize(databaseSizeBeforeTest);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(serviceUserSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void getAllServiceUsers() {
        // Initialize the database
        serviceUserRepository.save(serviceUser).block();

        // Get all the serviceUserList
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
            .value(hasItem(serviceUser.getId().intValue()))
            .jsonPath("$.[*].nom")
            .value(hasItem(DEFAULT_NOM))
            .jsonPath("$.[*].dateAjout")
            .value(hasItem(DEFAULT_DATE_AJOUT.toString()))
            .jsonPath("$.[*].actifYN")
            .value(hasItem(DEFAULT_ACTIF_YN.booleanValue()));
    }

    @Test
    void getServiceUser() {
        // Initialize the database
        serviceUserRepository.save(serviceUser).block();

        // Get the serviceUser
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, serviceUser.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(serviceUser.getId().intValue()))
            .jsonPath("$.nom")
            .value(is(DEFAULT_NOM))
            .jsonPath("$.dateAjout")
            .value(is(DEFAULT_DATE_AJOUT.toString()))
            .jsonPath("$.actifYN")
            .value(is(DEFAULT_ACTIF_YN.booleanValue()));
    }

    @Test
    void getNonExistingServiceUser() {
        // Get the serviceUser
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_PROBLEM_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putExistingServiceUser() throws Exception {
        // Initialize the database
        serviceUserRepository.save(serviceUser).block();

        int databaseSizeBeforeUpdate = serviceUserRepository.findAll().collectList().block().size();
        serviceUserSearchRepository.save(serviceUser).block();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(serviceUserSearchRepository.findAll().collectList().block());

        // Update the serviceUser
        ServiceUser updatedServiceUser = serviceUserRepository.findById(serviceUser.getId()).block();
        updatedServiceUser.nom(UPDATED_NOM).dateAjout(UPDATED_DATE_AJOUT).actifYN(UPDATED_ACTIF_YN);
        ServiceUserDTO serviceUserDTO = serviceUserMapper.toDto(updatedServiceUser);

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, serviceUserDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(serviceUserDTO))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the ServiceUser in the database
        List<ServiceUser> serviceUserList = serviceUserRepository.findAll().collectList().block();
        assertThat(serviceUserList).hasSize(databaseSizeBeforeUpdate);
        ServiceUser testServiceUser = serviceUserList.get(serviceUserList.size() - 1);
        assertThat(testServiceUser.getNom()).isEqualTo(UPDATED_NOM);
        assertThat(testServiceUser.getDateAjout()).isEqualTo(UPDATED_DATE_AJOUT);
        assertThat(testServiceUser.getActifYN()).isEqualTo(UPDATED_ACTIF_YN);
        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(serviceUserSearchRepository.findAll().collectList().block());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
                List<ServiceUser> serviceUserSearchList = IterableUtils.toList(serviceUserSearchRepository.findAll().collectList().block());
                ServiceUser testServiceUserSearch = serviceUserSearchList.get(searchDatabaseSizeAfter - 1);
                assertThat(testServiceUserSearch.getNom()).isEqualTo(UPDATED_NOM);
                assertThat(testServiceUserSearch.getDateAjout()).isEqualTo(UPDATED_DATE_AJOUT);
                assertThat(testServiceUserSearch.getActifYN()).isEqualTo(UPDATED_ACTIF_YN);
            });
    }

    @Test
    void putNonExistingServiceUser() throws Exception {
        int databaseSizeBeforeUpdate = serviceUserRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(serviceUserSearchRepository.findAll().collectList().block());
        serviceUser.setId(longCount.incrementAndGet());

        // Create the ServiceUser
        ServiceUserDTO serviceUserDTO = serviceUserMapper.toDto(serviceUser);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, serviceUserDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(serviceUserDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the ServiceUser in the database
        List<ServiceUser> serviceUserList = serviceUserRepository.findAll().collectList().block();
        assertThat(serviceUserList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(serviceUserSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void putWithIdMismatchServiceUser() throws Exception {
        int databaseSizeBeforeUpdate = serviceUserRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(serviceUserSearchRepository.findAll().collectList().block());
        serviceUser.setId(longCount.incrementAndGet());

        // Create the ServiceUser
        ServiceUserDTO serviceUserDTO = serviceUserMapper.toDto(serviceUser);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, longCount.incrementAndGet())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(serviceUserDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the ServiceUser in the database
        List<ServiceUser> serviceUserList = serviceUserRepository.findAll().collectList().block();
        assertThat(serviceUserList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(serviceUserSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void putWithMissingIdPathParamServiceUser() throws Exception {
        int databaseSizeBeforeUpdate = serviceUserRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(serviceUserSearchRepository.findAll().collectList().block());
        serviceUser.setId(longCount.incrementAndGet());

        // Create the ServiceUser
        ServiceUserDTO serviceUserDTO = serviceUserMapper.toDto(serviceUser);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(serviceUserDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the ServiceUser in the database
        List<ServiceUser> serviceUserList = serviceUserRepository.findAll().collectList().block();
        assertThat(serviceUserList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(serviceUserSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void partialUpdateServiceUserWithPatch() throws Exception {
        // Initialize the database
        serviceUserRepository.save(serviceUser).block();

        int databaseSizeBeforeUpdate = serviceUserRepository.findAll().collectList().block().size();

        // Update the serviceUser using partial update
        ServiceUser partialUpdatedServiceUser = new ServiceUser();
        partialUpdatedServiceUser.setId(serviceUser.getId());

        partialUpdatedServiceUser.nom(UPDATED_NOM);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedServiceUser.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedServiceUser))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the ServiceUser in the database
        List<ServiceUser> serviceUserList = serviceUserRepository.findAll().collectList().block();
        assertThat(serviceUserList).hasSize(databaseSizeBeforeUpdate);
        ServiceUser testServiceUser = serviceUserList.get(serviceUserList.size() - 1);
        assertThat(testServiceUser.getNom()).isEqualTo(UPDATED_NOM);
        assertThat(testServiceUser.getDateAjout()).isEqualTo(DEFAULT_DATE_AJOUT);
        assertThat(testServiceUser.getActifYN()).isEqualTo(DEFAULT_ACTIF_YN);
    }

    @Test
    void fullUpdateServiceUserWithPatch() throws Exception {
        // Initialize the database
        serviceUserRepository.save(serviceUser).block();

        int databaseSizeBeforeUpdate = serviceUserRepository.findAll().collectList().block().size();

        // Update the serviceUser using partial update
        ServiceUser partialUpdatedServiceUser = new ServiceUser();
        partialUpdatedServiceUser.setId(serviceUser.getId());

        partialUpdatedServiceUser.nom(UPDATED_NOM).dateAjout(UPDATED_DATE_AJOUT).actifYN(UPDATED_ACTIF_YN);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedServiceUser.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedServiceUser))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the ServiceUser in the database
        List<ServiceUser> serviceUserList = serviceUserRepository.findAll().collectList().block();
        assertThat(serviceUserList).hasSize(databaseSizeBeforeUpdate);
        ServiceUser testServiceUser = serviceUserList.get(serviceUserList.size() - 1);
        assertThat(testServiceUser.getNom()).isEqualTo(UPDATED_NOM);
        assertThat(testServiceUser.getDateAjout()).isEqualTo(UPDATED_DATE_AJOUT);
        assertThat(testServiceUser.getActifYN()).isEqualTo(UPDATED_ACTIF_YN);
    }

    @Test
    void patchNonExistingServiceUser() throws Exception {
        int databaseSizeBeforeUpdate = serviceUserRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(serviceUserSearchRepository.findAll().collectList().block());
        serviceUser.setId(longCount.incrementAndGet());

        // Create the ServiceUser
        ServiceUserDTO serviceUserDTO = serviceUserMapper.toDto(serviceUser);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, serviceUserDTO.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(serviceUserDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the ServiceUser in the database
        List<ServiceUser> serviceUserList = serviceUserRepository.findAll().collectList().block();
        assertThat(serviceUserList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(serviceUserSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void patchWithIdMismatchServiceUser() throws Exception {
        int databaseSizeBeforeUpdate = serviceUserRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(serviceUserSearchRepository.findAll().collectList().block());
        serviceUser.setId(longCount.incrementAndGet());

        // Create the ServiceUser
        ServiceUserDTO serviceUserDTO = serviceUserMapper.toDto(serviceUser);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, longCount.incrementAndGet())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(serviceUserDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the ServiceUser in the database
        List<ServiceUser> serviceUserList = serviceUserRepository.findAll().collectList().block();
        assertThat(serviceUserList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(serviceUserSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void patchWithMissingIdPathParamServiceUser() throws Exception {
        int databaseSizeBeforeUpdate = serviceUserRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(serviceUserSearchRepository.findAll().collectList().block());
        serviceUser.setId(longCount.incrementAndGet());

        // Create the ServiceUser
        ServiceUserDTO serviceUserDTO = serviceUserMapper.toDto(serviceUser);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(serviceUserDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the ServiceUser in the database
        List<ServiceUser> serviceUserList = serviceUserRepository.findAll().collectList().block();
        assertThat(serviceUserList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(serviceUserSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void deleteServiceUser() {
        // Initialize the database
        serviceUserRepository.save(serviceUser).block();
        serviceUserRepository.save(serviceUser).block();
        serviceUserSearchRepository.save(serviceUser).block();

        int databaseSizeBeforeDelete = serviceUserRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(serviceUserSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeBefore).isEqualTo(databaseSizeBeforeDelete);

        // Delete the serviceUser
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, serviceUser.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        List<ServiceUser> serviceUserList = serviceUserRepository.findAll().collectList().block();
        assertThat(serviceUserList).hasSize(databaseSizeBeforeDelete - 1);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(serviceUserSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore - 1);
    }

    @Test
    void searchServiceUser() {
        // Initialize the database
        serviceUser = serviceUserRepository.save(serviceUser).block();
        serviceUserSearchRepository.save(serviceUser).block();

        // Search the serviceUser
        webTestClient
            .get()
            .uri(ENTITY_SEARCH_API_URL + "?query=id:" + serviceUser.getId())
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.[*].id")
            .value(hasItem(serviceUser.getId().intValue()))
            .jsonPath("$.[*].nom")
            .value(hasItem(DEFAULT_NOM))
            .jsonPath("$.[*].dateAjout")
            .value(hasItem(DEFAULT_DATE_AJOUT.toString()))
            .jsonPath("$.[*].actifYN")
            .value(hasItem(DEFAULT_ACTIF_YN.booleanValue()));
    }
}
