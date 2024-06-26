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
import sn.ugb.gateway.domain.UserProfileBlocFonctionnel;
import sn.ugb.gateway.repository.EntityManager;
import sn.ugb.gateway.repository.UserProfileBlocFonctionnelRepository;
import sn.ugb.gateway.repository.search.UserProfileBlocFonctionnelSearchRepository;
import sn.ugb.gateway.service.dto.UserProfileBlocFonctionnelDTO;
import sn.ugb.gateway.service.mapper.UserProfileBlocFonctionnelMapper;

/**
 * Integration tests for the {@link UserProfileBlocFonctionnelResource} REST controller.
 */
@IntegrationTest
@AutoConfigureWebTestClient(timeout = IntegrationTest.DEFAULT_ENTITY_TIMEOUT)
@WithMockUser
class UserProfileBlocFonctionnelResourceIT {

    private static final LocalDate DEFAULT_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final Boolean DEFAULT_EN_COURS_YN = false;
    private static final Boolean UPDATED_EN_COURS_YN = true;

    private static final String ENTITY_API_URL = "/api/user-profile-bloc-fonctionnels";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/user-profile-bloc-fonctionnels/_search";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private UserProfileBlocFonctionnelRepository userProfileBlocFonctionnelRepository;

    @Autowired
    private UserProfileBlocFonctionnelMapper userProfileBlocFonctionnelMapper;

    @Autowired
    private UserProfileBlocFonctionnelSearchRepository userProfileBlocFonctionnelSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private WebTestClient webTestClient;

    private UserProfileBlocFonctionnel userProfileBlocFonctionnel;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static UserProfileBlocFonctionnel createEntity(EntityManager em) {
        UserProfileBlocFonctionnel userProfileBlocFonctionnel = new UserProfileBlocFonctionnel()
            .date(DEFAULT_DATE)
            .enCoursYN(DEFAULT_EN_COURS_YN);
        return userProfileBlocFonctionnel;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static UserProfileBlocFonctionnel createUpdatedEntity(EntityManager em) {
        UserProfileBlocFonctionnel userProfileBlocFonctionnel = new UserProfileBlocFonctionnel()
            .date(UPDATED_DATE)
            .enCoursYN(UPDATED_EN_COURS_YN);
        return userProfileBlocFonctionnel;
    }

    public static void deleteEntities(EntityManager em) {
        try {
            em.deleteAll(UserProfileBlocFonctionnel.class).block();
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
        userProfileBlocFonctionnelSearchRepository.deleteAll().block();
        assertThat(userProfileBlocFonctionnelSearchRepository.count().block()).isEqualTo(0);
    }

    @BeforeEach
    public void setupCsrf() {
        webTestClient = webTestClient.mutateWith(csrf());
    }

    @BeforeEach
    public void initTest() {
        deleteEntities(em);
        userProfileBlocFonctionnel = createEntity(em);
    }

    @Test
    void createUserProfileBlocFonctionnel() throws Exception {
        int databaseSizeBeforeCreate = userProfileBlocFonctionnelRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(userProfileBlocFonctionnelSearchRepository.findAll().collectList().block());
        // Create the UserProfileBlocFonctionnel
        UserProfileBlocFonctionnelDTO userProfileBlocFonctionnelDTO = userProfileBlocFonctionnelMapper.toDto(userProfileBlocFonctionnel);
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(userProfileBlocFonctionnelDTO))
            .exchange()
            .expectStatus()
            .isCreated();

        // Validate the UserProfileBlocFonctionnel in the database
        List<UserProfileBlocFonctionnel> userProfileBlocFonctionnelList = userProfileBlocFonctionnelRepository
            .findAll()
            .collectList()
            .block();
        assertThat(userProfileBlocFonctionnelList).hasSize(databaseSizeBeforeCreate + 1);
        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(
                    userProfileBlocFonctionnelSearchRepository.findAll().collectList().block()
                );
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore + 1);
            });
        UserProfileBlocFonctionnel testUserProfileBlocFonctionnel = userProfileBlocFonctionnelList.get(
            userProfileBlocFonctionnelList.size() - 1
        );
        assertThat(testUserProfileBlocFonctionnel.getDate()).isEqualTo(DEFAULT_DATE);
        assertThat(testUserProfileBlocFonctionnel.getEnCoursYN()).isEqualTo(DEFAULT_EN_COURS_YN);
    }

    @Test
    void createUserProfileBlocFonctionnelWithExistingId() throws Exception {
        // Create the UserProfileBlocFonctionnel with an existing ID
        userProfileBlocFonctionnel.setId(1L);
        UserProfileBlocFonctionnelDTO userProfileBlocFonctionnelDTO = userProfileBlocFonctionnelMapper.toDto(userProfileBlocFonctionnel);

        int databaseSizeBeforeCreate = userProfileBlocFonctionnelRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(userProfileBlocFonctionnelSearchRepository.findAll().collectList().block());

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(userProfileBlocFonctionnelDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the UserProfileBlocFonctionnel in the database
        List<UserProfileBlocFonctionnel> userProfileBlocFonctionnelList = userProfileBlocFonctionnelRepository
            .findAll()
            .collectList()
            .block();
        assertThat(userProfileBlocFonctionnelList).hasSize(databaseSizeBeforeCreate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(userProfileBlocFonctionnelSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void checkDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = userProfileBlocFonctionnelRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(userProfileBlocFonctionnelSearchRepository.findAll().collectList().block());
        // set the field null
        userProfileBlocFonctionnel.setDate(null);

        // Create the UserProfileBlocFonctionnel, which fails.
        UserProfileBlocFonctionnelDTO userProfileBlocFonctionnelDTO = userProfileBlocFonctionnelMapper.toDto(userProfileBlocFonctionnel);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(userProfileBlocFonctionnelDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<UserProfileBlocFonctionnel> userProfileBlocFonctionnelList = userProfileBlocFonctionnelRepository
            .findAll()
            .collectList()
            .block();
        assertThat(userProfileBlocFonctionnelList).hasSize(databaseSizeBeforeTest);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(userProfileBlocFonctionnelSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void checkEnCoursYNIsRequired() throws Exception {
        int databaseSizeBeforeTest = userProfileBlocFonctionnelRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(userProfileBlocFonctionnelSearchRepository.findAll().collectList().block());
        // set the field null
        userProfileBlocFonctionnel.setEnCoursYN(null);

        // Create the UserProfileBlocFonctionnel, which fails.
        UserProfileBlocFonctionnelDTO userProfileBlocFonctionnelDTO = userProfileBlocFonctionnelMapper.toDto(userProfileBlocFonctionnel);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(userProfileBlocFonctionnelDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<UserProfileBlocFonctionnel> userProfileBlocFonctionnelList = userProfileBlocFonctionnelRepository
            .findAll()
            .collectList()
            .block();
        assertThat(userProfileBlocFonctionnelList).hasSize(databaseSizeBeforeTest);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(userProfileBlocFonctionnelSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void getAllUserProfileBlocFonctionnels() {
        // Initialize the database
        userProfileBlocFonctionnelRepository.save(userProfileBlocFonctionnel).block();

        // Get all the userProfileBlocFonctionnelList
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
            .value(hasItem(userProfileBlocFonctionnel.getId().intValue()))
            .jsonPath("$.[*].date")
            .value(hasItem(DEFAULT_DATE.toString()))
            .jsonPath("$.[*].enCoursYN")
            .value(hasItem(DEFAULT_EN_COURS_YN.booleanValue()));
    }

    @Test
    void getUserProfileBlocFonctionnel() {
        // Initialize the database
        userProfileBlocFonctionnelRepository.save(userProfileBlocFonctionnel).block();

        // Get the userProfileBlocFonctionnel
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, userProfileBlocFonctionnel.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(userProfileBlocFonctionnel.getId().intValue()))
            .jsonPath("$.date")
            .value(is(DEFAULT_DATE.toString()))
            .jsonPath("$.enCoursYN")
            .value(is(DEFAULT_EN_COURS_YN.booleanValue()));
    }

    @Test
    void getNonExistingUserProfileBlocFonctionnel() {
        // Get the userProfileBlocFonctionnel
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_PROBLEM_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putExistingUserProfileBlocFonctionnel() throws Exception {
        // Initialize the database
        userProfileBlocFonctionnelRepository.save(userProfileBlocFonctionnel).block();

        int databaseSizeBeforeUpdate = userProfileBlocFonctionnelRepository.findAll().collectList().block().size();
        userProfileBlocFonctionnelSearchRepository.save(userProfileBlocFonctionnel).block();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(userProfileBlocFonctionnelSearchRepository.findAll().collectList().block());

        // Update the userProfileBlocFonctionnel
        UserProfileBlocFonctionnel updatedUserProfileBlocFonctionnel = userProfileBlocFonctionnelRepository
            .findById(userProfileBlocFonctionnel.getId())
            .block();
        updatedUserProfileBlocFonctionnel.date(UPDATED_DATE).enCoursYN(UPDATED_EN_COURS_YN);
        UserProfileBlocFonctionnelDTO userProfileBlocFonctionnelDTO = userProfileBlocFonctionnelMapper.toDto(
            updatedUserProfileBlocFonctionnel
        );

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, userProfileBlocFonctionnelDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(userProfileBlocFonctionnelDTO))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the UserProfileBlocFonctionnel in the database
        List<UserProfileBlocFonctionnel> userProfileBlocFonctionnelList = userProfileBlocFonctionnelRepository
            .findAll()
            .collectList()
            .block();
        assertThat(userProfileBlocFonctionnelList).hasSize(databaseSizeBeforeUpdate);
        UserProfileBlocFonctionnel testUserProfileBlocFonctionnel = userProfileBlocFonctionnelList.get(
            userProfileBlocFonctionnelList.size() - 1
        );
        assertThat(testUserProfileBlocFonctionnel.getDate()).isEqualTo(UPDATED_DATE);
        assertThat(testUserProfileBlocFonctionnel.getEnCoursYN()).isEqualTo(UPDATED_EN_COURS_YN);
        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(
                    userProfileBlocFonctionnelSearchRepository.findAll().collectList().block()
                );
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
                List<UserProfileBlocFonctionnel> userProfileBlocFonctionnelSearchList = IterableUtils.toList(
                    userProfileBlocFonctionnelSearchRepository.findAll().collectList().block()
                );
                UserProfileBlocFonctionnel testUserProfileBlocFonctionnelSearch = userProfileBlocFonctionnelSearchList.get(
                    searchDatabaseSizeAfter - 1
                );
                assertThat(testUserProfileBlocFonctionnelSearch.getDate()).isEqualTo(UPDATED_DATE);
                assertThat(testUserProfileBlocFonctionnelSearch.getEnCoursYN()).isEqualTo(UPDATED_EN_COURS_YN);
            });
    }

    @Test
    void putNonExistingUserProfileBlocFonctionnel() throws Exception {
        int databaseSizeBeforeUpdate = userProfileBlocFonctionnelRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(userProfileBlocFonctionnelSearchRepository.findAll().collectList().block());
        userProfileBlocFonctionnel.setId(longCount.incrementAndGet());

        // Create the UserProfileBlocFonctionnel
        UserProfileBlocFonctionnelDTO userProfileBlocFonctionnelDTO = userProfileBlocFonctionnelMapper.toDto(userProfileBlocFonctionnel);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, userProfileBlocFonctionnelDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(userProfileBlocFonctionnelDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the UserProfileBlocFonctionnel in the database
        List<UserProfileBlocFonctionnel> userProfileBlocFonctionnelList = userProfileBlocFonctionnelRepository
            .findAll()
            .collectList()
            .block();
        assertThat(userProfileBlocFonctionnelList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(userProfileBlocFonctionnelSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void putWithIdMismatchUserProfileBlocFonctionnel() throws Exception {
        int databaseSizeBeforeUpdate = userProfileBlocFonctionnelRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(userProfileBlocFonctionnelSearchRepository.findAll().collectList().block());
        userProfileBlocFonctionnel.setId(longCount.incrementAndGet());

        // Create the UserProfileBlocFonctionnel
        UserProfileBlocFonctionnelDTO userProfileBlocFonctionnelDTO = userProfileBlocFonctionnelMapper.toDto(userProfileBlocFonctionnel);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, longCount.incrementAndGet())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(userProfileBlocFonctionnelDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the UserProfileBlocFonctionnel in the database
        List<UserProfileBlocFonctionnel> userProfileBlocFonctionnelList = userProfileBlocFonctionnelRepository
            .findAll()
            .collectList()
            .block();
        assertThat(userProfileBlocFonctionnelList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(userProfileBlocFonctionnelSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void putWithMissingIdPathParamUserProfileBlocFonctionnel() throws Exception {
        int databaseSizeBeforeUpdate = userProfileBlocFonctionnelRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(userProfileBlocFonctionnelSearchRepository.findAll().collectList().block());
        userProfileBlocFonctionnel.setId(longCount.incrementAndGet());

        // Create the UserProfileBlocFonctionnel
        UserProfileBlocFonctionnelDTO userProfileBlocFonctionnelDTO = userProfileBlocFonctionnelMapper.toDto(userProfileBlocFonctionnel);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(userProfileBlocFonctionnelDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the UserProfileBlocFonctionnel in the database
        List<UserProfileBlocFonctionnel> userProfileBlocFonctionnelList = userProfileBlocFonctionnelRepository
            .findAll()
            .collectList()
            .block();
        assertThat(userProfileBlocFonctionnelList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(userProfileBlocFonctionnelSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void partialUpdateUserProfileBlocFonctionnelWithPatch() throws Exception {
        // Initialize the database
        userProfileBlocFonctionnelRepository.save(userProfileBlocFonctionnel).block();

        int databaseSizeBeforeUpdate = userProfileBlocFonctionnelRepository.findAll().collectList().block().size();

        // Update the userProfileBlocFonctionnel using partial update
        UserProfileBlocFonctionnel partialUpdatedUserProfileBlocFonctionnel = new UserProfileBlocFonctionnel();
        partialUpdatedUserProfileBlocFonctionnel.setId(userProfileBlocFonctionnel.getId());

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedUserProfileBlocFonctionnel.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedUserProfileBlocFonctionnel))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the UserProfileBlocFonctionnel in the database
        List<UserProfileBlocFonctionnel> userProfileBlocFonctionnelList = userProfileBlocFonctionnelRepository
            .findAll()
            .collectList()
            .block();
        assertThat(userProfileBlocFonctionnelList).hasSize(databaseSizeBeforeUpdate);
        UserProfileBlocFonctionnel testUserProfileBlocFonctionnel = userProfileBlocFonctionnelList.get(
            userProfileBlocFonctionnelList.size() - 1
        );
        assertThat(testUserProfileBlocFonctionnel.getDate()).isEqualTo(DEFAULT_DATE);
        assertThat(testUserProfileBlocFonctionnel.getEnCoursYN()).isEqualTo(DEFAULT_EN_COURS_YN);
    }

    @Test
    void fullUpdateUserProfileBlocFonctionnelWithPatch() throws Exception {
        // Initialize the database
        userProfileBlocFonctionnelRepository.save(userProfileBlocFonctionnel).block();

        int databaseSizeBeforeUpdate = userProfileBlocFonctionnelRepository.findAll().collectList().block().size();

        // Update the userProfileBlocFonctionnel using partial update
        UserProfileBlocFonctionnel partialUpdatedUserProfileBlocFonctionnel = new UserProfileBlocFonctionnel();
        partialUpdatedUserProfileBlocFonctionnel.setId(userProfileBlocFonctionnel.getId());

        partialUpdatedUserProfileBlocFonctionnel.date(UPDATED_DATE).enCoursYN(UPDATED_EN_COURS_YN);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedUserProfileBlocFonctionnel.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedUserProfileBlocFonctionnel))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the UserProfileBlocFonctionnel in the database
        List<UserProfileBlocFonctionnel> userProfileBlocFonctionnelList = userProfileBlocFonctionnelRepository
            .findAll()
            .collectList()
            .block();
        assertThat(userProfileBlocFonctionnelList).hasSize(databaseSizeBeforeUpdate);
        UserProfileBlocFonctionnel testUserProfileBlocFonctionnel = userProfileBlocFonctionnelList.get(
            userProfileBlocFonctionnelList.size() - 1
        );
        assertThat(testUserProfileBlocFonctionnel.getDate()).isEqualTo(UPDATED_DATE);
        assertThat(testUserProfileBlocFonctionnel.getEnCoursYN()).isEqualTo(UPDATED_EN_COURS_YN);
    }

    @Test
    void patchNonExistingUserProfileBlocFonctionnel() throws Exception {
        int databaseSizeBeforeUpdate = userProfileBlocFonctionnelRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(userProfileBlocFonctionnelSearchRepository.findAll().collectList().block());
        userProfileBlocFonctionnel.setId(longCount.incrementAndGet());

        // Create the UserProfileBlocFonctionnel
        UserProfileBlocFonctionnelDTO userProfileBlocFonctionnelDTO = userProfileBlocFonctionnelMapper.toDto(userProfileBlocFonctionnel);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, userProfileBlocFonctionnelDTO.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(userProfileBlocFonctionnelDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the UserProfileBlocFonctionnel in the database
        List<UserProfileBlocFonctionnel> userProfileBlocFonctionnelList = userProfileBlocFonctionnelRepository
            .findAll()
            .collectList()
            .block();
        assertThat(userProfileBlocFonctionnelList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(userProfileBlocFonctionnelSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void patchWithIdMismatchUserProfileBlocFonctionnel() throws Exception {
        int databaseSizeBeforeUpdate = userProfileBlocFonctionnelRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(userProfileBlocFonctionnelSearchRepository.findAll().collectList().block());
        userProfileBlocFonctionnel.setId(longCount.incrementAndGet());

        // Create the UserProfileBlocFonctionnel
        UserProfileBlocFonctionnelDTO userProfileBlocFonctionnelDTO = userProfileBlocFonctionnelMapper.toDto(userProfileBlocFonctionnel);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, longCount.incrementAndGet())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(userProfileBlocFonctionnelDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the UserProfileBlocFonctionnel in the database
        List<UserProfileBlocFonctionnel> userProfileBlocFonctionnelList = userProfileBlocFonctionnelRepository
            .findAll()
            .collectList()
            .block();
        assertThat(userProfileBlocFonctionnelList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(userProfileBlocFonctionnelSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void patchWithMissingIdPathParamUserProfileBlocFonctionnel() throws Exception {
        int databaseSizeBeforeUpdate = userProfileBlocFonctionnelRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(userProfileBlocFonctionnelSearchRepository.findAll().collectList().block());
        userProfileBlocFonctionnel.setId(longCount.incrementAndGet());

        // Create the UserProfileBlocFonctionnel
        UserProfileBlocFonctionnelDTO userProfileBlocFonctionnelDTO = userProfileBlocFonctionnelMapper.toDto(userProfileBlocFonctionnel);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(userProfileBlocFonctionnelDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the UserProfileBlocFonctionnel in the database
        List<UserProfileBlocFonctionnel> userProfileBlocFonctionnelList = userProfileBlocFonctionnelRepository
            .findAll()
            .collectList()
            .block();
        assertThat(userProfileBlocFonctionnelList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(userProfileBlocFonctionnelSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void deleteUserProfileBlocFonctionnel() {
        // Initialize the database
        userProfileBlocFonctionnelRepository.save(userProfileBlocFonctionnel).block();
        userProfileBlocFonctionnelRepository.save(userProfileBlocFonctionnel).block();
        userProfileBlocFonctionnelSearchRepository.save(userProfileBlocFonctionnel).block();

        int databaseSizeBeforeDelete = userProfileBlocFonctionnelRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(userProfileBlocFonctionnelSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeBefore).isEqualTo(databaseSizeBeforeDelete);

        // Delete the userProfileBlocFonctionnel
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, userProfileBlocFonctionnel.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        List<UserProfileBlocFonctionnel> userProfileBlocFonctionnelList = userProfileBlocFonctionnelRepository
            .findAll()
            .collectList()
            .block();
        assertThat(userProfileBlocFonctionnelList).hasSize(databaseSizeBeforeDelete - 1);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(userProfileBlocFonctionnelSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore - 1);
    }

    @Test
    void searchUserProfileBlocFonctionnel() {
        // Initialize the database
        userProfileBlocFonctionnel = userProfileBlocFonctionnelRepository.save(userProfileBlocFonctionnel).block();
        userProfileBlocFonctionnelSearchRepository.save(userProfileBlocFonctionnel).block();

        // Search the userProfileBlocFonctionnel
        webTestClient
            .get()
            .uri(ENTITY_SEARCH_API_URL + "?query=id:" + userProfileBlocFonctionnel.getId())
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.[*].id")
            .value(hasItem(userProfileBlocFonctionnel.getId().intValue()))
            .jsonPath("$.[*].date")
            .value(hasItem(DEFAULT_DATE.toString()))
            .jsonPath("$.[*].enCoursYN")
            .value(hasItem(DEFAULT_EN_COURS_YN.booleanValue()));
    }
}
