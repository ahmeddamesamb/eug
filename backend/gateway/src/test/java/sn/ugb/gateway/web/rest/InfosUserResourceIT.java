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
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import sn.ugb.gateway.IntegrationTest;
import sn.ugb.gateway.domain.InfosUser;
import sn.ugb.gateway.repository.EntityManager;
import sn.ugb.gateway.repository.InfosUserRepository;
import sn.ugb.gateway.repository.search.InfosUserSearchRepository;
import sn.ugb.gateway.service.InfosUserService;
import sn.ugb.gateway.service.dto.InfosUserDTO;
import sn.ugb.gateway.service.mapper.InfosUserMapper;

/**
 * Integration tests for the {@link InfosUserResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureWebTestClient(timeout = IntegrationTest.DEFAULT_ENTITY_TIMEOUT)
@WithMockUser
class InfosUserResourceIT {

    private static final LocalDate DEFAULT_DATE_AJOUT = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE_AJOUT = LocalDate.now(ZoneId.systemDefault());

    private static final Boolean DEFAULT_ACTIF_YN = false;
    private static final Boolean UPDATED_ACTIF_YN = true;

    private static final String ENTITY_API_URL = "/api/infos-users";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/infos-users/_search";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private InfosUserRepository infosUserRepository;

    @Mock
    private InfosUserRepository infosUserRepositoryMock;

    @Autowired
    private InfosUserMapper infosUserMapper;

    @Mock
    private InfosUserService infosUserServiceMock;

    @Autowired
    private InfosUserSearchRepository infosUserSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private WebTestClient webTestClient;

    private InfosUser infosUser;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static InfosUser createEntity(EntityManager em) {
        InfosUser infosUser = new InfosUser().dateAjout(DEFAULT_DATE_AJOUT).actifYN(DEFAULT_ACTIF_YN);
        return infosUser;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static InfosUser createUpdatedEntity(EntityManager em) {
        InfosUser infosUser = new InfosUser().dateAjout(UPDATED_DATE_AJOUT).actifYN(UPDATED_ACTIF_YN);
        return infosUser;
    }

    public static void deleteEntities(EntityManager em) {
        try {
            em.deleteAll(InfosUser.class).block();
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
        infosUserSearchRepository.deleteAll().block();
        assertThat(infosUserSearchRepository.count().block()).isEqualTo(0);
    }

    @BeforeEach
    public void setupCsrf() {
        webTestClient = webTestClient.mutateWith(csrf());
    }

    @BeforeEach
    public void initTest() {
        deleteEntities(em);
        infosUser = createEntity(em);
    }

    @Test
    void createInfosUser() throws Exception {
        int databaseSizeBeforeCreate = infosUserRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(infosUserSearchRepository.findAll().collectList().block());
        // Create the InfosUser
        InfosUserDTO infosUserDTO = infosUserMapper.toDto(infosUser);
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(infosUserDTO))
            .exchange()
            .expectStatus()
            .isCreated();

        // Validate the InfosUser in the database
        List<InfosUser> infosUserList = infosUserRepository.findAll().collectList().block();
        assertThat(infosUserList).hasSize(databaseSizeBeforeCreate + 1);
        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(infosUserSearchRepository.findAll().collectList().block());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore + 1);
            });
        InfosUser testInfosUser = infosUserList.get(infosUserList.size() - 1);
        assertThat(testInfosUser.getDateAjout()).isEqualTo(DEFAULT_DATE_AJOUT);
        assertThat(testInfosUser.getActifYN()).isEqualTo(DEFAULT_ACTIF_YN);
    }

    @Test
    void createInfosUserWithExistingId() throws Exception {
        // Create the InfosUser with an existing ID
        infosUser.setId(1L);
        InfosUserDTO infosUserDTO = infosUserMapper.toDto(infosUser);

        int databaseSizeBeforeCreate = infosUserRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(infosUserSearchRepository.findAll().collectList().block());

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(infosUserDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the InfosUser in the database
        List<InfosUser> infosUserList = infosUserRepository.findAll().collectList().block();
        assertThat(infosUserList).hasSize(databaseSizeBeforeCreate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(infosUserSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void checkDateAjoutIsRequired() throws Exception {
        int databaseSizeBeforeTest = infosUserRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(infosUserSearchRepository.findAll().collectList().block());
        // set the field null
        infosUser.setDateAjout(null);

        // Create the InfosUser, which fails.
        InfosUserDTO infosUserDTO = infosUserMapper.toDto(infosUser);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(infosUserDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<InfosUser> infosUserList = infosUserRepository.findAll().collectList().block();
        assertThat(infosUserList).hasSize(databaseSizeBeforeTest);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(infosUserSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void checkActifYNIsRequired() throws Exception {
        int databaseSizeBeforeTest = infosUserRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(infosUserSearchRepository.findAll().collectList().block());
        // set the field null
        infosUser.setActifYN(null);

        // Create the InfosUser, which fails.
        InfosUserDTO infosUserDTO = infosUserMapper.toDto(infosUser);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(infosUserDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<InfosUser> infosUserList = infosUserRepository.findAll().collectList().block();
        assertThat(infosUserList).hasSize(databaseSizeBeforeTest);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(infosUserSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void getAllInfosUsers() {
        // Initialize the database
        infosUserRepository.save(infosUser).block();

        // Get all the infosUserList
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
            .value(hasItem(infosUser.getId().intValue()))
            .jsonPath("$.[*].dateAjout")
            .value(hasItem(DEFAULT_DATE_AJOUT.toString()))
            .jsonPath("$.[*].actifYN")
            .value(hasItem(DEFAULT_ACTIF_YN.booleanValue()));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllInfosUsersWithEagerRelationshipsIsEnabled() {
        when(infosUserServiceMock.findAllWithEagerRelationships(any())).thenReturn(Flux.empty());

        webTestClient.get().uri(ENTITY_API_URL + "?eagerload=true").exchange().expectStatus().isOk();

        verify(infosUserServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllInfosUsersWithEagerRelationshipsIsNotEnabled() {
        when(infosUserServiceMock.findAllWithEagerRelationships(any())).thenReturn(Flux.empty());

        webTestClient.get().uri(ENTITY_API_URL + "?eagerload=false").exchange().expectStatus().isOk();
        verify(infosUserRepositoryMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    void getInfosUser() {
        // Initialize the database
        infosUserRepository.save(infosUser).block();

        // Get the infosUser
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, infosUser.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(infosUser.getId().intValue()))
            .jsonPath("$.dateAjout")
            .value(is(DEFAULT_DATE_AJOUT.toString()))
            .jsonPath("$.actifYN")
            .value(is(DEFAULT_ACTIF_YN.booleanValue()));
    }

    @Test
    void getNonExistingInfosUser() {
        // Get the infosUser
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_PROBLEM_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putExistingInfosUser() throws Exception {
        // Initialize the database
        infosUserRepository.save(infosUser).block();

        int databaseSizeBeforeUpdate = infosUserRepository.findAll().collectList().block().size();
        infosUserSearchRepository.save(infosUser).block();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(infosUserSearchRepository.findAll().collectList().block());

        // Update the infosUser
        InfosUser updatedInfosUser = infosUserRepository.findById(infosUser.getId()).block();
        updatedInfosUser.dateAjout(UPDATED_DATE_AJOUT).actifYN(UPDATED_ACTIF_YN);
        InfosUserDTO infosUserDTO = infosUserMapper.toDto(updatedInfosUser);

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, infosUserDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(infosUserDTO))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the InfosUser in the database
        List<InfosUser> infosUserList = infosUserRepository.findAll().collectList().block();
        assertThat(infosUserList).hasSize(databaseSizeBeforeUpdate);
        InfosUser testInfosUser = infosUserList.get(infosUserList.size() - 1);
        assertThat(testInfosUser.getDateAjout()).isEqualTo(UPDATED_DATE_AJOUT);
        assertThat(testInfosUser.getActifYN()).isEqualTo(UPDATED_ACTIF_YN);
        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(infosUserSearchRepository.findAll().collectList().block());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
                List<InfosUser> infosUserSearchList = IterableUtils.toList(infosUserSearchRepository.findAll().collectList().block());
                InfosUser testInfosUserSearch = infosUserSearchList.get(searchDatabaseSizeAfter - 1);
                assertThat(testInfosUserSearch.getDateAjout()).isEqualTo(UPDATED_DATE_AJOUT);
                assertThat(testInfosUserSearch.getActifYN()).isEqualTo(UPDATED_ACTIF_YN);
            });
    }

    @Test
    void putNonExistingInfosUser() throws Exception {
        int databaseSizeBeforeUpdate = infosUserRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(infosUserSearchRepository.findAll().collectList().block());
        infosUser.setId(longCount.incrementAndGet());

        // Create the InfosUser
        InfosUserDTO infosUserDTO = infosUserMapper.toDto(infosUser);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, infosUserDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(infosUserDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the InfosUser in the database
        List<InfosUser> infosUserList = infosUserRepository.findAll().collectList().block();
        assertThat(infosUserList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(infosUserSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void putWithIdMismatchInfosUser() throws Exception {
        int databaseSizeBeforeUpdate = infosUserRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(infosUserSearchRepository.findAll().collectList().block());
        infosUser.setId(longCount.incrementAndGet());

        // Create the InfosUser
        InfosUserDTO infosUserDTO = infosUserMapper.toDto(infosUser);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, longCount.incrementAndGet())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(infosUserDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the InfosUser in the database
        List<InfosUser> infosUserList = infosUserRepository.findAll().collectList().block();
        assertThat(infosUserList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(infosUserSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void putWithMissingIdPathParamInfosUser() throws Exception {
        int databaseSizeBeforeUpdate = infosUserRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(infosUserSearchRepository.findAll().collectList().block());
        infosUser.setId(longCount.incrementAndGet());

        // Create the InfosUser
        InfosUserDTO infosUserDTO = infosUserMapper.toDto(infosUser);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(infosUserDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the InfosUser in the database
        List<InfosUser> infosUserList = infosUserRepository.findAll().collectList().block();
        assertThat(infosUserList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(infosUserSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void partialUpdateInfosUserWithPatch() throws Exception {
        // Initialize the database
        infosUserRepository.save(infosUser).block();

        int databaseSizeBeforeUpdate = infosUserRepository.findAll().collectList().block().size();

        // Update the infosUser using partial update
        InfosUser partialUpdatedInfosUser = new InfosUser();
        partialUpdatedInfosUser.setId(infosUser.getId());

        partialUpdatedInfosUser.dateAjout(UPDATED_DATE_AJOUT);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedInfosUser.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedInfosUser))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the InfosUser in the database
        List<InfosUser> infosUserList = infosUserRepository.findAll().collectList().block();
        assertThat(infosUserList).hasSize(databaseSizeBeforeUpdate);
        InfosUser testInfosUser = infosUserList.get(infosUserList.size() - 1);
        assertThat(testInfosUser.getDateAjout()).isEqualTo(UPDATED_DATE_AJOUT);
        assertThat(testInfosUser.getActifYN()).isEqualTo(DEFAULT_ACTIF_YN);
    }

    @Test
    void fullUpdateInfosUserWithPatch() throws Exception {
        // Initialize the database
        infosUserRepository.save(infosUser).block();

        int databaseSizeBeforeUpdate = infosUserRepository.findAll().collectList().block().size();

        // Update the infosUser using partial update
        InfosUser partialUpdatedInfosUser = new InfosUser();
        partialUpdatedInfosUser.setId(infosUser.getId());

        partialUpdatedInfosUser.dateAjout(UPDATED_DATE_AJOUT).actifYN(UPDATED_ACTIF_YN);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedInfosUser.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedInfosUser))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the InfosUser in the database
        List<InfosUser> infosUserList = infosUserRepository.findAll().collectList().block();
        assertThat(infosUserList).hasSize(databaseSizeBeforeUpdate);
        InfosUser testInfosUser = infosUserList.get(infosUserList.size() - 1);
        assertThat(testInfosUser.getDateAjout()).isEqualTo(UPDATED_DATE_AJOUT);
        assertThat(testInfosUser.getActifYN()).isEqualTo(UPDATED_ACTIF_YN);
    }

    @Test
    void patchNonExistingInfosUser() throws Exception {
        int databaseSizeBeforeUpdate = infosUserRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(infosUserSearchRepository.findAll().collectList().block());
        infosUser.setId(longCount.incrementAndGet());

        // Create the InfosUser
        InfosUserDTO infosUserDTO = infosUserMapper.toDto(infosUser);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, infosUserDTO.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(infosUserDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the InfosUser in the database
        List<InfosUser> infosUserList = infosUserRepository.findAll().collectList().block();
        assertThat(infosUserList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(infosUserSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void patchWithIdMismatchInfosUser() throws Exception {
        int databaseSizeBeforeUpdate = infosUserRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(infosUserSearchRepository.findAll().collectList().block());
        infosUser.setId(longCount.incrementAndGet());

        // Create the InfosUser
        InfosUserDTO infosUserDTO = infosUserMapper.toDto(infosUser);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, longCount.incrementAndGet())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(infosUserDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the InfosUser in the database
        List<InfosUser> infosUserList = infosUserRepository.findAll().collectList().block();
        assertThat(infosUserList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(infosUserSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void patchWithMissingIdPathParamInfosUser() throws Exception {
        int databaseSizeBeforeUpdate = infosUserRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(infosUserSearchRepository.findAll().collectList().block());
        infosUser.setId(longCount.incrementAndGet());

        // Create the InfosUser
        InfosUserDTO infosUserDTO = infosUserMapper.toDto(infosUser);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(infosUserDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the InfosUser in the database
        List<InfosUser> infosUserList = infosUserRepository.findAll().collectList().block();
        assertThat(infosUserList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(infosUserSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void deleteInfosUser() {
        // Initialize the database
        infosUserRepository.save(infosUser).block();
        infosUserRepository.save(infosUser).block();
        infosUserSearchRepository.save(infosUser).block();

        int databaseSizeBeforeDelete = infosUserRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(infosUserSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeBefore).isEqualTo(databaseSizeBeforeDelete);

        // Delete the infosUser
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, infosUser.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        List<InfosUser> infosUserList = infosUserRepository.findAll().collectList().block();
        assertThat(infosUserList).hasSize(databaseSizeBeforeDelete - 1);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(infosUserSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore - 1);
    }

    @Test
    void searchInfosUser() {
        // Initialize the database
        infosUser = infosUserRepository.save(infosUser).block();
        infosUserSearchRepository.save(infosUser).block();

        // Search the infosUser
        webTestClient
            .get()
            .uri(ENTITY_SEARCH_API_URL + "?query=id:" + infosUser.getId())
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.[*].id")
            .value(hasItem(infosUser.getId().intValue()))
            .jsonPath("$.[*].dateAjout")
            .value(hasItem(DEFAULT_DATE_AJOUT.toString()))
            .jsonPath("$.[*].actifYN")
            .value(hasItem(DEFAULT_ACTIF_YN.booleanValue()));
    }
}
