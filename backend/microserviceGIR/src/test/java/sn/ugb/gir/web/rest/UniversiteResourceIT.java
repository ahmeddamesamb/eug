package sn.ugb.gir.web.rest;

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
import sn.ugb.gir.IntegrationTest;
import sn.ugb.gir.domain.Universite;
import sn.ugb.gir.repository.UniversiteRepository;
import sn.ugb.gir.repository.search.UniversiteSearchRepository;
import sn.ugb.gir.service.dto.UniversiteDTO;
import sn.ugb.gir.service.mapper.UniversiteMapper;

/**
 * Integration tests for the {@link UniversiteResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class UniversiteResourceIT {

    private static final String DEFAULT_NOM_UNIVERSITE = "AAAAAAAAAA";
    private static final String UPDATED_NOM_UNIVERSITE = "BBBBBBBBBB";

    private static final String DEFAULT_SIGLE_UNIVERSITE = "AAAAAAAAAA";
    private static final String UPDATED_SIGLE_UNIVERSITE = "BBBBBBBBBB";

    private static final Boolean DEFAULT_ACTIF_YN = false;
    private static final Boolean UPDATED_ACTIF_YN = true;

    private static final String ENTITY_API_URL = "/api/universites";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/universites/_search";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private UniversiteRepository universiteRepository;

    @Autowired
    private UniversiteMapper universiteMapper;

    @Autowired
    private UniversiteSearchRepository universiteSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restUniversiteMockMvc;

    private Universite universite;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Universite createEntity(EntityManager em) {
        Universite universite = new Universite()
            .nomUniversite(DEFAULT_NOM_UNIVERSITE)
            .sigleUniversite(DEFAULT_SIGLE_UNIVERSITE)
            .actifYN(DEFAULT_ACTIF_YN);
        return universite;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Universite createUpdatedEntity(EntityManager em) {
        Universite universite = new Universite()
            .nomUniversite(UPDATED_NOM_UNIVERSITE)
            .sigleUniversite(UPDATED_SIGLE_UNIVERSITE)
            .actifYN(UPDATED_ACTIF_YN);
        return universite;
    }

    @AfterEach
    public void cleanupElasticSearchRepository() {
        universiteSearchRepository.deleteAll();
        assertThat(universiteSearchRepository.count()).isEqualTo(0);
    }

    @BeforeEach
    public void initTest() {
        universite = createEntity(em);
    }

    @Test
    @Transactional
    void createUniversite() throws Exception {
        int databaseSizeBeforeCreate = universiteRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(universiteSearchRepository.findAll());
        // Create the Universite
        UniversiteDTO universiteDTO = universiteMapper.toDto(universite);
        restUniversiteMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(universiteDTO))
            )
            .andExpect(status().isCreated());

        // Validate the Universite in the database
        List<Universite> universiteList = universiteRepository.findAll();
        assertThat(universiteList).hasSize(databaseSizeBeforeCreate + 1);
        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(universiteSearchRepository.findAll());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore + 1);
            });
        Universite testUniversite = universiteList.get(universiteList.size() - 1);
        assertThat(testUniversite.getNomUniversite()).isEqualTo(DEFAULT_NOM_UNIVERSITE);
        assertThat(testUniversite.getSigleUniversite()).isEqualTo(DEFAULT_SIGLE_UNIVERSITE);
        assertThat(testUniversite.getActifYN()).isEqualTo(DEFAULT_ACTIF_YN);
    }

    @Test
    @Transactional
    void createUniversiteWithExistingId() throws Exception {
        // Create the Universite with an existing ID
        universite.setId(1L);
        UniversiteDTO universiteDTO = universiteMapper.toDto(universite);

        int databaseSizeBeforeCreate = universiteRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(universiteSearchRepository.findAll());

        // An entity with an existing ID cannot be created, so this API call must fail
        restUniversiteMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(universiteDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Universite in the database
        List<Universite> universiteList = universiteRepository.findAll();
        assertThat(universiteList).hasSize(databaseSizeBeforeCreate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(universiteSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void checkNomUniversiteIsRequired() throws Exception {
        int databaseSizeBeforeTest = universiteRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(universiteSearchRepository.findAll());
        // set the field null
        universite.setNomUniversite(null);

        // Create the Universite, which fails.
        UniversiteDTO universiteDTO = universiteMapper.toDto(universite);

        restUniversiteMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(universiteDTO))
            )
            .andExpect(status().isBadRequest());

        List<Universite> universiteList = universiteRepository.findAll();
        assertThat(universiteList).hasSize(databaseSizeBeforeTest);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(universiteSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void checkSigleUniversiteIsRequired() throws Exception {
        int databaseSizeBeforeTest = universiteRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(universiteSearchRepository.findAll());
        // set the field null
        universite.setSigleUniversite(null);

        // Create the Universite, which fails.
        UniversiteDTO universiteDTO = universiteMapper.toDto(universite);

        restUniversiteMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(universiteDTO))
            )
            .andExpect(status().isBadRequest());

        List<Universite> universiteList = universiteRepository.findAll();
        assertThat(universiteList).hasSize(databaseSizeBeforeTest);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(universiteSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void checkActifYNIsRequired() throws Exception {
        int databaseSizeBeforeTest = universiteRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(universiteSearchRepository.findAll());
        // set the field null
        universite.setActifYN(null);

        // Create the Universite, which fails.
        UniversiteDTO universiteDTO = universiteMapper.toDto(universite);

        restUniversiteMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(universiteDTO))
            )
            .andExpect(status().isBadRequest());

        List<Universite> universiteList = universiteRepository.findAll();
        assertThat(universiteList).hasSize(databaseSizeBeforeTest);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(universiteSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void getAllUniversites() throws Exception {
        // Initialize the database
        universiteRepository.saveAndFlush(universite);

        // Get all the universiteList
        restUniversiteMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(universite.getId().intValue())))
            .andExpect(jsonPath("$.[*].nomUniversite").value(hasItem(DEFAULT_NOM_UNIVERSITE)))
            .andExpect(jsonPath("$.[*].sigleUniversite").value(hasItem(DEFAULT_SIGLE_UNIVERSITE)))
            .andExpect(jsonPath("$.[*].actifYN").value(hasItem(DEFAULT_ACTIF_YN.booleanValue())));
    }

    @Test
    @Transactional
    void getUniversite() throws Exception {
        // Initialize the database
        universiteRepository.saveAndFlush(universite);

        // Get the universite
        restUniversiteMockMvc
            .perform(get(ENTITY_API_URL_ID, universite.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(universite.getId().intValue()))
            .andExpect(jsonPath("$.nomUniversite").value(DEFAULT_NOM_UNIVERSITE))
            .andExpect(jsonPath("$.sigleUniversite").value(DEFAULT_SIGLE_UNIVERSITE))
            .andExpect(jsonPath("$.actifYN").value(DEFAULT_ACTIF_YN.booleanValue()));
    }

    @Test
    @Transactional
    void getNonExistingUniversite() throws Exception {
        // Get the universite
        restUniversiteMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingUniversite() throws Exception {
        // Initialize the database
        universiteRepository.saveAndFlush(universite);

        int databaseSizeBeforeUpdate = universiteRepository.findAll().size();
        universiteSearchRepository.save(universite);
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(universiteSearchRepository.findAll());

        // Update the universite
        Universite updatedUniversite = universiteRepository.findById(universite.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedUniversite are not directly saved in db
        em.detach(updatedUniversite);
        updatedUniversite.nomUniversite(UPDATED_NOM_UNIVERSITE).sigleUniversite(UPDATED_SIGLE_UNIVERSITE).actifYN(UPDATED_ACTIF_YN);
        UniversiteDTO universiteDTO = universiteMapper.toDto(updatedUniversite);

        restUniversiteMockMvc
            .perform(
                put(ENTITY_API_URL_ID, universiteDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(universiteDTO))
            )
            .andExpect(status().isOk());

        // Validate the Universite in the database
        List<Universite> universiteList = universiteRepository.findAll();
        assertThat(universiteList).hasSize(databaseSizeBeforeUpdate);
        Universite testUniversite = universiteList.get(universiteList.size() - 1);
        assertThat(testUniversite.getNomUniversite()).isEqualTo(UPDATED_NOM_UNIVERSITE);
        assertThat(testUniversite.getSigleUniversite()).isEqualTo(UPDATED_SIGLE_UNIVERSITE);
        assertThat(testUniversite.getActifYN()).isEqualTo(UPDATED_ACTIF_YN);
        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(universiteSearchRepository.findAll());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
                List<Universite> universiteSearchList = IterableUtils.toList(universiteSearchRepository.findAll());
                Universite testUniversiteSearch = universiteSearchList.get(searchDatabaseSizeAfter - 1);
                assertThat(testUniversiteSearch.getNomUniversite()).isEqualTo(UPDATED_NOM_UNIVERSITE);
                assertThat(testUniversiteSearch.getSigleUniversite()).isEqualTo(UPDATED_SIGLE_UNIVERSITE);
                assertThat(testUniversiteSearch.getActifYN()).isEqualTo(UPDATED_ACTIF_YN);
            });
    }

    @Test
    @Transactional
    void putNonExistingUniversite() throws Exception {
        int databaseSizeBeforeUpdate = universiteRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(universiteSearchRepository.findAll());
        universite.setId(longCount.incrementAndGet());

        // Create the Universite
        UniversiteDTO universiteDTO = universiteMapper.toDto(universite);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restUniversiteMockMvc
            .perform(
                put(ENTITY_API_URL_ID, universiteDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(universiteDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Universite in the database
        List<Universite> universiteList = universiteRepository.findAll();
        assertThat(universiteList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(universiteSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void putWithIdMismatchUniversite() throws Exception {
        int databaseSizeBeforeUpdate = universiteRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(universiteSearchRepository.findAll());
        universite.setId(longCount.incrementAndGet());

        // Create the Universite
        UniversiteDTO universiteDTO = universiteMapper.toDto(universite);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUniversiteMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(universiteDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Universite in the database
        List<Universite> universiteList = universiteRepository.findAll();
        assertThat(universiteList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(universiteSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamUniversite() throws Exception {
        int databaseSizeBeforeUpdate = universiteRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(universiteSearchRepository.findAll());
        universite.setId(longCount.incrementAndGet());

        // Create the Universite
        UniversiteDTO universiteDTO = universiteMapper.toDto(universite);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUniversiteMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(universiteDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Universite in the database
        List<Universite> universiteList = universiteRepository.findAll();
        assertThat(universiteList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(universiteSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void partialUpdateUniversiteWithPatch() throws Exception {
        // Initialize the database
        universiteRepository.saveAndFlush(universite);

        int databaseSizeBeforeUpdate = universiteRepository.findAll().size();

        // Update the universite using partial update
        Universite partialUpdatedUniversite = new Universite();
        partialUpdatedUniversite.setId(universite.getId());

        partialUpdatedUniversite.nomUniversite(UPDATED_NOM_UNIVERSITE).sigleUniversite(UPDATED_SIGLE_UNIVERSITE).actifYN(UPDATED_ACTIF_YN);

        restUniversiteMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedUniversite.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedUniversite))
            )
            .andExpect(status().isOk());

        // Validate the Universite in the database
        List<Universite> universiteList = universiteRepository.findAll();
        assertThat(universiteList).hasSize(databaseSizeBeforeUpdate);
        Universite testUniversite = universiteList.get(universiteList.size() - 1);
        assertThat(testUniversite.getNomUniversite()).isEqualTo(UPDATED_NOM_UNIVERSITE);
        assertThat(testUniversite.getSigleUniversite()).isEqualTo(UPDATED_SIGLE_UNIVERSITE);
        assertThat(testUniversite.getActifYN()).isEqualTo(UPDATED_ACTIF_YN);
    }

    @Test
    @Transactional
    void fullUpdateUniversiteWithPatch() throws Exception {
        // Initialize the database
        universiteRepository.saveAndFlush(universite);

        int databaseSizeBeforeUpdate = universiteRepository.findAll().size();

        // Update the universite using partial update
        Universite partialUpdatedUniversite = new Universite();
        partialUpdatedUniversite.setId(universite.getId());

        partialUpdatedUniversite.nomUniversite(UPDATED_NOM_UNIVERSITE).sigleUniversite(UPDATED_SIGLE_UNIVERSITE).actifYN(UPDATED_ACTIF_YN);

        restUniversiteMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedUniversite.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedUniversite))
            )
            .andExpect(status().isOk());

        // Validate the Universite in the database
        List<Universite> universiteList = universiteRepository.findAll();
        assertThat(universiteList).hasSize(databaseSizeBeforeUpdate);
        Universite testUniversite = universiteList.get(universiteList.size() - 1);
        assertThat(testUniversite.getNomUniversite()).isEqualTo(UPDATED_NOM_UNIVERSITE);
        assertThat(testUniversite.getSigleUniversite()).isEqualTo(UPDATED_SIGLE_UNIVERSITE);
        assertThat(testUniversite.getActifYN()).isEqualTo(UPDATED_ACTIF_YN);
    }

    @Test
    @Transactional
    void patchNonExistingUniversite() throws Exception {
        int databaseSizeBeforeUpdate = universiteRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(universiteSearchRepository.findAll());
        universite.setId(longCount.incrementAndGet());

        // Create the Universite
        UniversiteDTO universiteDTO = universiteMapper.toDto(universite);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restUniversiteMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, universiteDTO.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(universiteDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Universite in the database
        List<Universite> universiteList = universiteRepository.findAll();
        assertThat(universiteList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(universiteSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void patchWithIdMismatchUniversite() throws Exception {
        int databaseSizeBeforeUpdate = universiteRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(universiteSearchRepository.findAll());
        universite.setId(longCount.incrementAndGet());

        // Create the Universite
        UniversiteDTO universiteDTO = universiteMapper.toDto(universite);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUniversiteMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(universiteDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Universite in the database
        List<Universite> universiteList = universiteRepository.findAll();
        assertThat(universiteList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(universiteSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamUniversite() throws Exception {
        int databaseSizeBeforeUpdate = universiteRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(universiteSearchRepository.findAll());
        universite.setId(longCount.incrementAndGet());

        // Create the Universite
        UniversiteDTO universiteDTO = universiteMapper.toDto(universite);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUniversiteMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(universiteDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Universite in the database
        List<Universite> universiteList = universiteRepository.findAll();
        assertThat(universiteList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(universiteSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void deleteUniversite() throws Exception {
        // Initialize the database
        universiteRepository.saveAndFlush(universite);
        universiteRepository.save(universite);
        universiteSearchRepository.save(universite);

        int databaseSizeBeforeDelete = universiteRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(universiteSearchRepository.findAll());
        assertThat(searchDatabaseSizeBefore).isEqualTo(databaseSizeBeforeDelete);

        // Delete the universite
        restUniversiteMockMvc
            .perform(delete(ENTITY_API_URL_ID, universite.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Universite> universiteList = universiteRepository.findAll();
        assertThat(universiteList).hasSize(databaseSizeBeforeDelete - 1);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(universiteSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore - 1);
    }

    @Test
    @Transactional
    void searchUniversite() throws Exception {
        // Initialize the database
        universite = universiteRepository.saveAndFlush(universite);
        universiteSearchRepository.save(universite);

        // Search the universite
        restUniversiteMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + universite.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(universite.getId().intValue())))
            .andExpect(jsonPath("$.[*].nomUniversite").value(hasItem(DEFAULT_NOM_UNIVERSITE)))
            .andExpect(jsonPath("$.[*].sigleUniversite").value(hasItem(DEFAULT_SIGLE_UNIVERSITE)))
            .andExpect(jsonPath("$.[*].actifYN").value(hasItem(DEFAULT_ACTIF_YN.booleanValue())));
    }
}
