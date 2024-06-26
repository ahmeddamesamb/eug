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
import sn.ugb.gateway.domain.UserProfile;
import sn.ugb.gateway.repository.EntityManager;
import sn.ugb.gateway.repository.UserProfileRepository;
import sn.ugb.gateway.repository.search.UserProfileSearchRepository;
import sn.ugb.gateway.service.dto.UserProfileDTO;
import sn.ugb.gateway.service.mapper.UserProfileMapper;

/**
 * Integration tests for the {@link UserProfileResource} REST controller.
 */
@IntegrationTest
@AutoConfigureWebTestClient(timeout = IntegrationTest.DEFAULT_ENTITY_TIMEOUT)
@WithMockUser
class UserProfileResourceIT {

    private static final LocalDate DEFAULT_DATE_DEBUT = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE_DEBUT = LocalDate.now(ZoneId.systemDefault());

    private static final LocalDate DEFAULT_DATE_FIN = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE_FIN = LocalDate.now(ZoneId.systemDefault());

    private static final Boolean DEFAULT_EN_COURS_YN = false;
    private static final Boolean UPDATED_EN_COURS_YN = true;

    private static final String ENTITY_API_URL = "/api/user-profiles";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/user-profiles/_search";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private UserProfileRepository userProfileRepository;

    @Autowired
    private UserProfileMapper userProfileMapper;

    @Autowired
    private UserProfileSearchRepository userProfileSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private WebTestClient webTestClient;

    private UserProfile userProfile;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static UserProfile createEntity(EntityManager em) {
        UserProfile userProfile = new UserProfile().dateDebut(DEFAULT_DATE_DEBUT).dateFin(DEFAULT_DATE_FIN).enCoursYN(DEFAULT_EN_COURS_YN);
        return userProfile;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static UserProfile createUpdatedEntity(EntityManager em) {
        UserProfile userProfile = new UserProfile().dateDebut(UPDATED_DATE_DEBUT).dateFin(UPDATED_DATE_FIN).enCoursYN(UPDATED_EN_COURS_YN);
        return userProfile;
    }

    public static void deleteEntities(EntityManager em) {
        try {
            em.deleteAll(UserProfile.class).block();
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
        userProfileSearchRepository.deleteAll().block();
        assertThat(userProfileSearchRepository.count().block()).isEqualTo(0);
    }

    @BeforeEach
    public void setupCsrf() {
        webTestClient = webTestClient.mutateWith(csrf());
    }

    @BeforeEach
    public void initTest() {
        deleteEntities(em);
        userProfile = createEntity(em);
    }

    @Test
    void createUserProfile() throws Exception {
        int databaseSizeBeforeCreate = userProfileRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(userProfileSearchRepository.findAll().collectList().block());
        // Create the UserProfile
        UserProfileDTO userProfileDTO = userProfileMapper.toDto(userProfile);
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(userProfileDTO))
            .exchange()
            .expectStatus()
            .isCreated();

        // Validate the UserProfile in the database
        List<UserProfile> userProfileList = userProfileRepository.findAll().collectList().block();
        assertThat(userProfileList).hasSize(databaseSizeBeforeCreate + 1);
        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(userProfileSearchRepository.findAll().collectList().block());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore + 1);
            });
        UserProfile testUserProfile = userProfileList.get(userProfileList.size() - 1);
        assertThat(testUserProfile.getDateDebut()).isEqualTo(DEFAULT_DATE_DEBUT);
        assertThat(testUserProfile.getDateFin()).isEqualTo(DEFAULT_DATE_FIN);
        assertThat(testUserProfile.getEnCoursYN()).isEqualTo(DEFAULT_EN_COURS_YN);
    }

    @Test
    void createUserProfileWithExistingId() throws Exception {
        // Create the UserProfile with an existing ID
        userProfile.setId(1L);
        UserProfileDTO userProfileDTO = userProfileMapper.toDto(userProfile);

        int databaseSizeBeforeCreate = userProfileRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(userProfileSearchRepository.findAll().collectList().block());

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(userProfileDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the UserProfile in the database
        List<UserProfile> userProfileList = userProfileRepository.findAll().collectList().block();
        assertThat(userProfileList).hasSize(databaseSizeBeforeCreate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(userProfileSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void checkDateDebutIsRequired() throws Exception {
        int databaseSizeBeforeTest = userProfileRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(userProfileSearchRepository.findAll().collectList().block());
        // set the field null
        userProfile.setDateDebut(null);

        // Create the UserProfile, which fails.
        UserProfileDTO userProfileDTO = userProfileMapper.toDto(userProfile);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(userProfileDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<UserProfile> userProfileList = userProfileRepository.findAll().collectList().block();
        assertThat(userProfileList).hasSize(databaseSizeBeforeTest);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(userProfileSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void checkDateFinIsRequired() throws Exception {
        int databaseSizeBeforeTest = userProfileRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(userProfileSearchRepository.findAll().collectList().block());
        // set the field null
        userProfile.setDateFin(null);

        // Create the UserProfile, which fails.
        UserProfileDTO userProfileDTO = userProfileMapper.toDto(userProfile);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(userProfileDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<UserProfile> userProfileList = userProfileRepository.findAll().collectList().block();
        assertThat(userProfileList).hasSize(databaseSizeBeforeTest);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(userProfileSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void checkEnCoursYNIsRequired() throws Exception {
        int databaseSizeBeforeTest = userProfileRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(userProfileSearchRepository.findAll().collectList().block());
        // set the field null
        userProfile.setEnCoursYN(null);

        // Create the UserProfile, which fails.
        UserProfileDTO userProfileDTO = userProfileMapper.toDto(userProfile);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(userProfileDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<UserProfile> userProfileList = userProfileRepository.findAll().collectList().block();
        assertThat(userProfileList).hasSize(databaseSizeBeforeTest);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(userProfileSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void getAllUserProfiles() {
        // Initialize the database
        userProfileRepository.save(userProfile).block();

        // Get all the userProfileList
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
            .value(hasItem(userProfile.getId().intValue()))
            .jsonPath("$.[*].dateDebut")
            .value(hasItem(DEFAULT_DATE_DEBUT.toString()))
            .jsonPath("$.[*].dateFin")
            .value(hasItem(DEFAULT_DATE_FIN.toString()))
            .jsonPath("$.[*].enCoursYN")
            .value(hasItem(DEFAULT_EN_COURS_YN.booleanValue()));
    }

    @Test
    void getUserProfile() {
        // Initialize the database
        userProfileRepository.save(userProfile).block();

        // Get the userProfile
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, userProfile.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(userProfile.getId().intValue()))
            .jsonPath("$.dateDebut")
            .value(is(DEFAULT_DATE_DEBUT.toString()))
            .jsonPath("$.dateFin")
            .value(is(DEFAULT_DATE_FIN.toString()))
            .jsonPath("$.enCoursYN")
            .value(is(DEFAULT_EN_COURS_YN.booleanValue()));
    }

    @Test
    void getNonExistingUserProfile() {
        // Get the userProfile
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_PROBLEM_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putExistingUserProfile() throws Exception {
        // Initialize the database
        userProfileRepository.save(userProfile).block();

        int databaseSizeBeforeUpdate = userProfileRepository.findAll().collectList().block().size();
        userProfileSearchRepository.save(userProfile).block();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(userProfileSearchRepository.findAll().collectList().block());

        // Update the userProfile
        UserProfile updatedUserProfile = userProfileRepository.findById(userProfile.getId()).block();
        updatedUserProfile.dateDebut(UPDATED_DATE_DEBUT).dateFin(UPDATED_DATE_FIN).enCoursYN(UPDATED_EN_COURS_YN);
        UserProfileDTO userProfileDTO = userProfileMapper.toDto(updatedUserProfile);

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, userProfileDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(userProfileDTO))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the UserProfile in the database
        List<UserProfile> userProfileList = userProfileRepository.findAll().collectList().block();
        assertThat(userProfileList).hasSize(databaseSizeBeforeUpdate);
        UserProfile testUserProfile = userProfileList.get(userProfileList.size() - 1);
        assertThat(testUserProfile.getDateDebut()).isEqualTo(UPDATED_DATE_DEBUT);
        assertThat(testUserProfile.getDateFin()).isEqualTo(UPDATED_DATE_FIN);
        assertThat(testUserProfile.getEnCoursYN()).isEqualTo(UPDATED_EN_COURS_YN);
        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(userProfileSearchRepository.findAll().collectList().block());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
                List<UserProfile> userProfileSearchList = IterableUtils.toList(userProfileSearchRepository.findAll().collectList().block());
                UserProfile testUserProfileSearch = userProfileSearchList.get(searchDatabaseSizeAfter - 1);
                assertThat(testUserProfileSearch.getDateDebut()).isEqualTo(UPDATED_DATE_DEBUT);
                assertThat(testUserProfileSearch.getDateFin()).isEqualTo(UPDATED_DATE_FIN);
                assertThat(testUserProfileSearch.getEnCoursYN()).isEqualTo(UPDATED_EN_COURS_YN);
            });
    }

    @Test
    void putNonExistingUserProfile() throws Exception {
        int databaseSizeBeforeUpdate = userProfileRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(userProfileSearchRepository.findAll().collectList().block());
        userProfile.setId(longCount.incrementAndGet());

        // Create the UserProfile
        UserProfileDTO userProfileDTO = userProfileMapper.toDto(userProfile);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, userProfileDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(userProfileDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the UserProfile in the database
        List<UserProfile> userProfileList = userProfileRepository.findAll().collectList().block();
        assertThat(userProfileList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(userProfileSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void putWithIdMismatchUserProfile() throws Exception {
        int databaseSizeBeforeUpdate = userProfileRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(userProfileSearchRepository.findAll().collectList().block());
        userProfile.setId(longCount.incrementAndGet());

        // Create the UserProfile
        UserProfileDTO userProfileDTO = userProfileMapper.toDto(userProfile);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, longCount.incrementAndGet())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(userProfileDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the UserProfile in the database
        List<UserProfile> userProfileList = userProfileRepository.findAll().collectList().block();
        assertThat(userProfileList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(userProfileSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void putWithMissingIdPathParamUserProfile() throws Exception {
        int databaseSizeBeforeUpdate = userProfileRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(userProfileSearchRepository.findAll().collectList().block());
        userProfile.setId(longCount.incrementAndGet());

        // Create the UserProfile
        UserProfileDTO userProfileDTO = userProfileMapper.toDto(userProfile);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(userProfileDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the UserProfile in the database
        List<UserProfile> userProfileList = userProfileRepository.findAll().collectList().block();
        assertThat(userProfileList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(userProfileSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void partialUpdateUserProfileWithPatch() throws Exception {
        // Initialize the database
        userProfileRepository.save(userProfile).block();

        int databaseSizeBeforeUpdate = userProfileRepository.findAll().collectList().block().size();

        // Update the userProfile using partial update
        UserProfile partialUpdatedUserProfile = new UserProfile();
        partialUpdatedUserProfile.setId(userProfile.getId());

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedUserProfile.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedUserProfile))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the UserProfile in the database
        List<UserProfile> userProfileList = userProfileRepository.findAll().collectList().block();
        assertThat(userProfileList).hasSize(databaseSizeBeforeUpdate);
        UserProfile testUserProfile = userProfileList.get(userProfileList.size() - 1);
        assertThat(testUserProfile.getDateDebut()).isEqualTo(DEFAULT_DATE_DEBUT);
        assertThat(testUserProfile.getDateFin()).isEqualTo(DEFAULT_DATE_FIN);
        assertThat(testUserProfile.getEnCoursYN()).isEqualTo(DEFAULT_EN_COURS_YN);
    }

    @Test
    void fullUpdateUserProfileWithPatch() throws Exception {
        // Initialize the database
        userProfileRepository.save(userProfile).block();

        int databaseSizeBeforeUpdate = userProfileRepository.findAll().collectList().block().size();

        // Update the userProfile using partial update
        UserProfile partialUpdatedUserProfile = new UserProfile();
        partialUpdatedUserProfile.setId(userProfile.getId());

        partialUpdatedUserProfile.dateDebut(UPDATED_DATE_DEBUT).dateFin(UPDATED_DATE_FIN).enCoursYN(UPDATED_EN_COURS_YN);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedUserProfile.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedUserProfile))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the UserProfile in the database
        List<UserProfile> userProfileList = userProfileRepository.findAll().collectList().block();
        assertThat(userProfileList).hasSize(databaseSizeBeforeUpdate);
        UserProfile testUserProfile = userProfileList.get(userProfileList.size() - 1);
        assertThat(testUserProfile.getDateDebut()).isEqualTo(UPDATED_DATE_DEBUT);
        assertThat(testUserProfile.getDateFin()).isEqualTo(UPDATED_DATE_FIN);
        assertThat(testUserProfile.getEnCoursYN()).isEqualTo(UPDATED_EN_COURS_YN);
    }

    @Test
    void patchNonExistingUserProfile() throws Exception {
        int databaseSizeBeforeUpdate = userProfileRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(userProfileSearchRepository.findAll().collectList().block());
        userProfile.setId(longCount.incrementAndGet());

        // Create the UserProfile
        UserProfileDTO userProfileDTO = userProfileMapper.toDto(userProfile);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, userProfileDTO.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(userProfileDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the UserProfile in the database
        List<UserProfile> userProfileList = userProfileRepository.findAll().collectList().block();
        assertThat(userProfileList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(userProfileSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void patchWithIdMismatchUserProfile() throws Exception {
        int databaseSizeBeforeUpdate = userProfileRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(userProfileSearchRepository.findAll().collectList().block());
        userProfile.setId(longCount.incrementAndGet());

        // Create the UserProfile
        UserProfileDTO userProfileDTO = userProfileMapper.toDto(userProfile);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, longCount.incrementAndGet())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(userProfileDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the UserProfile in the database
        List<UserProfile> userProfileList = userProfileRepository.findAll().collectList().block();
        assertThat(userProfileList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(userProfileSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void patchWithMissingIdPathParamUserProfile() throws Exception {
        int databaseSizeBeforeUpdate = userProfileRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(userProfileSearchRepository.findAll().collectList().block());
        userProfile.setId(longCount.incrementAndGet());

        // Create the UserProfile
        UserProfileDTO userProfileDTO = userProfileMapper.toDto(userProfile);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(userProfileDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the UserProfile in the database
        List<UserProfile> userProfileList = userProfileRepository.findAll().collectList().block();
        assertThat(userProfileList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(userProfileSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void deleteUserProfile() {
        // Initialize the database
        userProfileRepository.save(userProfile).block();
        userProfileRepository.save(userProfile).block();
        userProfileSearchRepository.save(userProfile).block();

        int databaseSizeBeforeDelete = userProfileRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(userProfileSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeBefore).isEqualTo(databaseSizeBeforeDelete);

        // Delete the userProfile
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, userProfile.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        List<UserProfile> userProfileList = userProfileRepository.findAll().collectList().block();
        assertThat(userProfileList).hasSize(databaseSizeBeforeDelete - 1);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(userProfileSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore - 1);
    }

    @Test
    void searchUserProfile() {
        // Initialize the database
        userProfile = userProfileRepository.save(userProfile).block();
        userProfileSearchRepository.save(userProfile).block();

        // Search the userProfile
        webTestClient
            .get()
            .uri(ENTITY_SEARCH_API_URL + "?query=id:" + userProfile.getId())
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.[*].id")
            .value(hasItem(userProfile.getId().intValue()))
            .jsonPath("$.[*].dateDebut")
            .value(hasItem(DEFAULT_DATE_DEBUT.toString()))
            .jsonPath("$.[*].dateFin")
            .value(hasItem(DEFAULT_DATE_FIN.toString()))
            .jsonPath("$.[*].enCoursYN")
            .value(hasItem(DEFAULT_EN_COURS_YN.booleanValue()));
    }
}
