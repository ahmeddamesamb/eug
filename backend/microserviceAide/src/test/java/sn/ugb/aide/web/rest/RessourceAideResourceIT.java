package sn.ugb.aide.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import jakarta.persistence.EntityManager;
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
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import sn.ugb.aide.IntegrationTest;
import sn.ugb.aide.domain.RessourceAide;
import sn.ugb.aide.repository.RessourceAideRepository;
import sn.ugb.aide.repository.search.RessourceAideSearchRepository;
import sn.ugb.aide.service.dto.RessourceAideDTO;
import sn.ugb.aide.service.mapper.RessourceAideMapper;

/**
 * Integration tests for the {@link RessourceAideResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class RessourceAideResourceIT {

    private static final String DEFAULT_NOM = "AAAAAAAAAA";
    private static final String UPDATED_NOM = "BBBBBBBBBB";

    private static final String DEFAULT_LIBELLE = "AAAAAAAAAA";
    private static final String UPDATED_LIBELLE = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/ressource-aides";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/ressource-aides/_search";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private RessourceAideRepository ressourceAideRepository;

    @Autowired
    private RessourceAideMapper ressourceAideMapper;

    @Autowired
    private RessourceAideSearchRepository ressourceAideSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restRessourceAideMockMvc;

    private RessourceAide ressourceAide;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static RessourceAide createEntity(EntityManager em) {
        RessourceAide ressourceAide = new RessourceAide().nom(DEFAULT_NOM).libelle(DEFAULT_LIBELLE);
        return ressourceAide;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static RessourceAide createUpdatedEntity(EntityManager em) {
        RessourceAide ressourceAide = new RessourceAide().nom(UPDATED_NOM).libelle(UPDATED_LIBELLE);
        return ressourceAide;
    }

    @AfterEach
    public void cleanupElasticSearchRepository() {
        ressourceAideSearchRepository.deleteAll();
        assertThat(ressourceAideSearchRepository.count()).isEqualTo(0);
    }

    @BeforeEach
    public void initTest() {
        ressourceAide = createEntity(em);
    }

    @Test
    @Transactional
    void createRessourceAide() throws Exception {
        int databaseSizeBeforeCreate = ressourceAideRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(ressourceAideSearchRepository.findAll());
        // Create the RessourceAide
        RessourceAideDTO ressourceAideDTO = ressourceAideMapper.toDto(ressourceAide);
        restRessourceAideMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(ressourceAideDTO))
            )
            .andExpect(status().isCreated());

        // Validate the RessourceAide in the database
        List<RessourceAide> ressourceAideList = ressourceAideRepository.findAll();
        assertThat(ressourceAideList).hasSize(databaseSizeBeforeCreate + 1);
        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(ressourceAideSearchRepository.findAll());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore + 1);
            });
        RessourceAide testRessourceAide = ressourceAideList.get(ressourceAideList.size() - 1);
        assertThat(testRessourceAide.getNom()).isEqualTo(DEFAULT_NOM);
        assertThat(testRessourceAide.getLibelle()).isEqualTo(DEFAULT_LIBELLE);
    }

    @Test
    @Transactional
    void createRessourceAideWithExistingId() throws Exception {
        // Create the RessourceAide with an existing ID
        ressourceAide.setId(1L);
        RessourceAideDTO ressourceAideDTO = ressourceAideMapper.toDto(ressourceAide);

        int databaseSizeBeforeCreate = ressourceAideRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(ressourceAideSearchRepository.findAll());

        // An entity with an existing ID cannot be created, so this API call must fail
        restRessourceAideMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(ressourceAideDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the RessourceAide in the database
        List<RessourceAide> ressourceAideList = ressourceAideRepository.findAll();
        assertThat(ressourceAideList).hasSize(databaseSizeBeforeCreate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(ressourceAideSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void checkNomIsRequired() throws Exception {
        int databaseSizeBeforeTest = ressourceAideRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(ressourceAideSearchRepository.findAll());
        // set the field null
        ressourceAide.setNom(null);

        // Create the RessourceAide, which fails.
        RessourceAideDTO ressourceAideDTO = ressourceAideMapper.toDto(ressourceAide);

        restRessourceAideMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(ressourceAideDTO))
            )
            .andExpect(status().isBadRequest());

        List<RessourceAide> ressourceAideList = ressourceAideRepository.findAll();
        assertThat(ressourceAideList).hasSize(databaseSizeBeforeTest);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(ressourceAideSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void getAllRessourceAides() throws Exception {
        // Initialize the database
        ressourceAideRepository.saveAndFlush(ressourceAide);

        // Get all the ressourceAideList
        restRessourceAideMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(ressourceAide.getId().intValue())))
            .andExpect(jsonPath("$.[*].nom").value(hasItem(DEFAULT_NOM)))
            .andExpect(jsonPath("$.[*].libelle").value(hasItem(DEFAULT_LIBELLE)));
    }

    @Test
    @Transactional
    void getRessourceAide() throws Exception {
        // Initialize the database
        ressourceAideRepository.saveAndFlush(ressourceAide);

        // Get the ressourceAide
        restRessourceAideMockMvc
            .perform(get(ENTITY_API_URL_ID, ressourceAide.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(ressourceAide.getId().intValue()))
            .andExpect(jsonPath("$.nom").value(DEFAULT_NOM))
            .andExpect(jsonPath("$.libelle").value(DEFAULT_LIBELLE));
    }

    @Test
    @Transactional
    void getNonExistingRessourceAide() throws Exception {
        // Get the ressourceAide
        restRessourceAideMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingRessourceAide() throws Exception {
        // Initialize the database
        ressourceAideRepository.saveAndFlush(ressourceAide);

        int databaseSizeBeforeUpdate = ressourceAideRepository.findAll().size();
        ressourceAideSearchRepository.save(ressourceAide);
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(ressourceAideSearchRepository.findAll());

        // Update the ressourceAide
        RessourceAide updatedRessourceAide = ressourceAideRepository.findById(ressourceAide.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedRessourceAide are not directly saved in db
        em.detach(updatedRessourceAide);
        updatedRessourceAide.nom(UPDATED_NOM).libelle(UPDATED_LIBELLE);
        RessourceAideDTO ressourceAideDTO = ressourceAideMapper.toDto(updatedRessourceAide);

        restRessourceAideMockMvc
            .perform(
                put(ENTITY_API_URL_ID, ressourceAideDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(ressourceAideDTO))
            )
            .andExpect(status().isOk());

        // Validate the RessourceAide in the database
        List<RessourceAide> ressourceAideList = ressourceAideRepository.findAll();
        assertThat(ressourceAideList).hasSize(databaseSizeBeforeUpdate);
        RessourceAide testRessourceAide = ressourceAideList.get(ressourceAideList.size() - 1);
        assertThat(testRessourceAide.getNom()).isEqualTo(UPDATED_NOM);
        assertThat(testRessourceAide.getLibelle()).isEqualTo(UPDATED_LIBELLE);
        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(ressourceAideSearchRepository.findAll());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
                List<RessourceAide> ressourceAideSearchList = IterableUtils.toList(ressourceAideSearchRepository.findAll());
                RessourceAide testRessourceAideSearch = ressourceAideSearchList.get(searchDatabaseSizeAfter - 1);
                assertThat(testRessourceAideSearch.getNom()).isEqualTo(UPDATED_NOM);
                assertThat(testRessourceAideSearch.getLibelle()).isEqualTo(UPDATED_LIBELLE);
            });
    }

    @Test
    @Transactional
    void putNonExistingRessourceAide() throws Exception {
        int databaseSizeBeforeUpdate = ressourceAideRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(ressourceAideSearchRepository.findAll());
        ressourceAide.setId(longCount.incrementAndGet());

        // Create the RessourceAide
        RessourceAideDTO ressourceAideDTO = ressourceAideMapper.toDto(ressourceAide);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restRessourceAideMockMvc
            .perform(
                put(ENTITY_API_URL_ID, ressourceAideDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(ressourceAideDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the RessourceAide in the database
        List<RessourceAide> ressourceAideList = ressourceAideRepository.findAll();
        assertThat(ressourceAideList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(ressourceAideSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void putWithIdMismatchRessourceAide() throws Exception {
        int databaseSizeBeforeUpdate = ressourceAideRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(ressourceAideSearchRepository.findAll());
        ressourceAide.setId(longCount.incrementAndGet());

        // Create the RessourceAide
        RessourceAideDTO ressourceAideDTO = ressourceAideMapper.toDto(ressourceAide);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRessourceAideMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(ressourceAideDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the RessourceAide in the database
        List<RessourceAide> ressourceAideList = ressourceAideRepository.findAll();
        assertThat(ressourceAideList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(ressourceAideSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamRessourceAide() throws Exception {
        int databaseSizeBeforeUpdate = ressourceAideRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(ressourceAideSearchRepository.findAll());
        ressourceAide.setId(longCount.incrementAndGet());

        // Create the RessourceAide
        RessourceAideDTO ressourceAideDTO = ressourceAideMapper.toDto(ressourceAide);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRessourceAideMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(ressourceAideDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the RessourceAide in the database
        List<RessourceAide> ressourceAideList = ressourceAideRepository.findAll();
        assertThat(ressourceAideList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(ressourceAideSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void partialUpdateRessourceAideWithPatch() throws Exception {
        // Initialize the database
        ressourceAideRepository.saveAndFlush(ressourceAide);

        int databaseSizeBeforeUpdate = ressourceAideRepository.findAll().size();

        // Update the ressourceAide using partial update
        RessourceAide partialUpdatedRessourceAide = new RessourceAide();
        partialUpdatedRessourceAide.setId(ressourceAide.getId());

        partialUpdatedRessourceAide.nom(UPDATED_NOM);

        restRessourceAideMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedRessourceAide.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedRessourceAide))
            )
            .andExpect(status().isOk());

        // Validate the RessourceAide in the database
        List<RessourceAide> ressourceAideList = ressourceAideRepository.findAll();
        assertThat(ressourceAideList).hasSize(databaseSizeBeforeUpdate);
        RessourceAide testRessourceAide = ressourceAideList.get(ressourceAideList.size() - 1);
        assertThat(testRessourceAide.getNom()).isEqualTo(UPDATED_NOM);
        assertThat(testRessourceAide.getLibelle()).isEqualTo(DEFAULT_LIBELLE);
    }

    @Test
    @Transactional
    void fullUpdateRessourceAideWithPatch() throws Exception {
        // Initialize the database
        ressourceAideRepository.saveAndFlush(ressourceAide);

        int databaseSizeBeforeUpdate = ressourceAideRepository.findAll().size();

        // Update the ressourceAide using partial update
        RessourceAide partialUpdatedRessourceAide = new RessourceAide();
        partialUpdatedRessourceAide.setId(ressourceAide.getId());

        partialUpdatedRessourceAide.nom(UPDATED_NOM).libelle(UPDATED_LIBELLE);

        restRessourceAideMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedRessourceAide.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedRessourceAide))
            )
            .andExpect(status().isOk());

        // Validate the RessourceAide in the database
        List<RessourceAide> ressourceAideList = ressourceAideRepository.findAll();
        assertThat(ressourceAideList).hasSize(databaseSizeBeforeUpdate);
        RessourceAide testRessourceAide = ressourceAideList.get(ressourceAideList.size() - 1);
        assertThat(testRessourceAide.getNom()).isEqualTo(UPDATED_NOM);
        assertThat(testRessourceAide.getLibelle()).isEqualTo(UPDATED_LIBELLE);
    }

    @Test
    @Transactional
    void patchNonExistingRessourceAide() throws Exception {
        int databaseSizeBeforeUpdate = ressourceAideRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(ressourceAideSearchRepository.findAll());
        ressourceAide.setId(longCount.incrementAndGet());

        // Create the RessourceAide
        RessourceAideDTO ressourceAideDTO = ressourceAideMapper.toDto(ressourceAide);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restRessourceAideMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, ressourceAideDTO.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(ressourceAideDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the RessourceAide in the database
        List<RessourceAide> ressourceAideList = ressourceAideRepository.findAll();
        assertThat(ressourceAideList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(ressourceAideSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void patchWithIdMismatchRessourceAide() throws Exception {
        int databaseSizeBeforeUpdate = ressourceAideRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(ressourceAideSearchRepository.findAll());
        ressourceAide.setId(longCount.incrementAndGet());

        // Create the RessourceAide
        RessourceAideDTO ressourceAideDTO = ressourceAideMapper.toDto(ressourceAide);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRessourceAideMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(ressourceAideDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the RessourceAide in the database
        List<RessourceAide> ressourceAideList = ressourceAideRepository.findAll();
        assertThat(ressourceAideList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(ressourceAideSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamRessourceAide() throws Exception {
        int databaseSizeBeforeUpdate = ressourceAideRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(ressourceAideSearchRepository.findAll());
        ressourceAide.setId(longCount.incrementAndGet());

        // Create the RessourceAide
        RessourceAideDTO ressourceAideDTO = ressourceAideMapper.toDto(ressourceAide);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRessourceAideMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(ressourceAideDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the RessourceAide in the database
        List<RessourceAide> ressourceAideList = ressourceAideRepository.findAll();
        assertThat(ressourceAideList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(ressourceAideSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void deleteRessourceAide() throws Exception {
        // Initialize the database
        ressourceAideRepository.saveAndFlush(ressourceAide);
        ressourceAideRepository.save(ressourceAide);
        ressourceAideSearchRepository.save(ressourceAide);

        int databaseSizeBeforeDelete = ressourceAideRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(ressourceAideSearchRepository.findAll());
        assertThat(searchDatabaseSizeBefore).isEqualTo(databaseSizeBeforeDelete);

        // Delete the ressourceAide
        restRessourceAideMockMvc
            .perform(delete(ENTITY_API_URL_ID, ressourceAide.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<RessourceAide> ressourceAideList = ressourceAideRepository.findAll();
        assertThat(ressourceAideList).hasSize(databaseSizeBeforeDelete - 1);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(ressourceAideSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore - 1);
    }

    @Test
    @Transactional
    void searchRessourceAide() throws Exception {
        // Initialize the database
        ressourceAide = ressourceAideRepository.saveAndFlush(ressourceAide);
        ressourceAideSearchRepository.save(ressourceAide);

        // Search the ressourceAide
        restRessourceAideMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + ressourceAide.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(ressourceAide.getId().intValue())))
            .andExpect(jsonPath("$.[*].nom").value(hasItem(DEFAULT_NOM)))
            .andExpect(jsonPath("$.[*].libelle").value(hasItem(DEFAULT_LIBELLE)));
    }
}
