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
import sn.ugb.gir.domain.Specialite;
import sn.ugb.gir.repository.SpecialiteRepository;
import sn.ugb.gir.repository.search.SpecialiteSearchRepository;
import sn.ugb.gir.service.dto.SpecialiteDTO;
import sn.ugb.gir.service.mapper.SpecialiteMapper;

/**
 * Integration tests for the {@link SpecialiteResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class SpecialiteResourceIT {

    private static final String DEFAULT_NOM_SPECIALITES = "AAAAAAAAAA";
    private static final String UPDATED_NOM_SPECIALITES = "BBBBBBBBBB";

    private static final String DEFAULT_SIGLE_SPECIALITES = "AAAAAAAAAA";
    private static final String UPDATED_SIGLE_SPECIALITES = "BBBBBBBBBB";

    private static final Boolean DEFAULT_SPECIALITE_PARTICULIER_YN = false;
    private static final Boolean UPDATED_SPECIALITE_PARTICULIER_YN = true;

    private static final Boolean DEFAULT_SPECIALITES_PAYANTE_YN = false;
    private static final Boolean UPDATED_SPECIALITES_PAYANTE_YN = true;

    private static final Boolean DEFAULT_ACTIF_YN = false;
    private static final Boolean UPDATED_ACTIF_YN = true;

    private static final String ENTITY_API_URL = "/api/specialites";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/specialites/_search";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private SpecialiteRepository specialiteRepository;

    @Autowired
    private SpecialiteMapper specialiteMapper;

    @Autowired
    private SpecialiteSearchRepository specialiteSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restSpecialiteMockMvc;

    private Specialite specialite;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Specialite createEntity(EntityManager em) {
        Specialite specialite = new Specialite()
            .nomSpecialites(DEFAULT_NOM_SPECIALITES)
            .sigleSpecialites(DEFAULT_SIGLE_SPECIALITES)
            .specialiteParticulierYN(DEFAULT_SPECIALITE_PARTICULIER_YN)
            .specialitesPayanteYN(DEFAULT_SPECIALITES_PAYANTE_YN)
            .actifYN(DEFAULT_ACTIF_YN);
        return specialite;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Specialite createUpdatedEntity(EntityManager em) {
        Specialite specialite = new Specialite()
            .nomSpecialites(UPDATED_NOM_SPECIALITES)
            .sigleSpecialites(UPDATED_SIGLE_SPECIALITES)
            .specialiteParticulierYN(UPDATED_SPECIALITE_PARTICULIER_YN)
            .specialitesPayanteYN(UPDATED_SPECIALITES_PAYANTE_YN)
            .actifYN(UPDATED_ACTIF_YN);
        return specialite;
    }

    @AfterEach
    public void cleanupElasticSearchRepository() {
        specialiteSearchRepository.deleteAll();
        assertThat(specialiteSearchRepository.count()).isEqualTo(0);
    }

    @BeforeEach
    public void initTest() {
        specialite = createEntity(em);
    }

    @Test
    @Transactional
    void createSpecialite() throws Exception {
        int databaseSizeBeforeCreate = specialiteRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(specialiteSearchRepository.findAll());
        // Create the Specialite
        SpecialiteDTO specialiteDTO = specialiteMapper.toDto(specialite);
        restSpecialiteMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(specialiteDTO))
            )
            .andExpect(status().isCreated());

        // Validate the Specialite in the database
        List<Specialite> specialiteList = specialiteRepository.findAll();
        assertThat(specialiteList).hasSize(databaseSizeBeforeCreate + 1);
        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(specialiteSearchRepository.findAll());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore + 1);
            });
        Specialite testSpecialite = specialiteList.get(specialiteList.size() - 1);
        assertThat(testSpecialite.getNomSpecialites()).isEqualTo(DEFAULT_NOM_SPECIALITES);
        assertThat(testSpecialite.getSigleSpecialites()).isEqualTo(DEFAULT_SIGLE_SPECIALITES);
        assertThat(testSpecialite.getSpecialiteParticulierYN()).isEqualTo(DEFAULT_SPECIALITE_PARTICULIER_YN);
        assertThat(testSpecialite.getSpecialitesPayanteYN()).isEqualTo(DEFAULT_SPECIALITES_PAYANTE_YN);
        assertThat(testSpecialite.getActifYN()).isEqualTo(DEFAULT_ACTIF_YN);
    }

    @Test
    @Transactional
    void createSpecialiteWithExistingId() throws Exception {
        // Create the Specialite with an existing ID
        specialite.setId(1L);
        SpecialiteDTO specialiteDTO = specialiteMapper.toDto(specialite);

        int databaseSizeBeforeCreate = specialiteRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(specialiteSearchRepository.findAll());

        // An entity with an existing ID cannot be created, so this API call must fail
        restSpecialiteMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(specialiteDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Specialite in the database
        List<Specialite> specialiteList = specialiteRepository.findAll();
        assertThat(specialiteList).hasSize(databaseSizeBeforeCreate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(specialiteSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void checkNomSpecialitesIsRequired() throws Exception {
        int databaseSizeBeforeTest = specialiteRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(specialiteSearchRepository.findAll());
        // set the field null
        specialite.setNomSpecialites(null);

        // Create the Specialite, which fails.
        SpecialiteDTO specialiteDTO = specialiteMapper.toDto(specialite);

        restSpecialiteMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(specialiteDTO))
            )
            .andExpect(status().isBadRequest());

        List<Specialite> specialiteList = specialiteRepository.findAll();
        assertThat(specialiteList).hasSize(databaseSizeBeforeTest);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(specialiteSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void checkSigleSpecialitesIsRequired() throws Exception {
        int databaseSizeBeforeTest = specialiteRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(specialiteSearchRepository.findAll());
        // set the field null
        specialite.setSigleSpecialites(null);

        // Create the Specialite, which fails.
        SpecialiteDTO specialiteDTO = specialiteMapper.toDto(specialite);

        restSpecialiteMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(specialiteDTO))
            )
            .andExpect(status().isBadRequest());

        List<Specialite> specialiteList = specialiteRepository.findAll();
        assertThat(specialiteList).hasSize(databaseSizeBeforeTest);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(specialiteSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void checkSpecialitesPayanteYNIsRequired() throws Exception {
        int databaseSizeBeforeTest = specialiteRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(specialiteSearchRepository.findAll());
        // set the field null
        specialite.setSpecialitesPayanteYN(null);

        // Create the Specialite, which fails.
        SpecialiteDTO specialiteDTO = specialiteMapper.toDto(specialite);

        restSpecialiteMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(specialiteDTO))
            )
            .andExpect(status().isBadRequest());

        List<Specialite> specialiteList = specialiteRepository.findAll();
        assertThat(specialiteList).hasSize(databaseSizeBeforeTest);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(specialiteSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void getAllSpecialites() throws Exception {
        // Initialize the database
        specialiteRepository.saveAndFlush(specialite);

        // Get all the specialiteList
        restSpecialiteMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(specialite.getId().intValue())))
            .andExpect(jsonPath("$.[*].nomSpecialites").value(hasItem(DEFAULT_NOM_SPECIALITES)))
            .andExpect(jsonPath("$.[*].sigleSpecialites").value(hasItem(DEFAULT_SIGLE_SPECIALITES)))
            .andExpect(jsonPath("$.[*].specialiteParticulierYN").value(hasItem(DEFAULT_SPECIALITE_PARTICULIER_YN.booleanValue())))
            .andExpect(jsonPath("$.[*].specialitesPayanteYN").value(hasItem(DEFAULT_SPECIALITES_PAYANTE_YN.booleanValue())))
            .andExpect(jsonPath("$.[*].actifYN").value(hasItem(DEFAULT_ACTIF_YN.booleanValue())));
    }

    @Test
    @Transactional
    void getSpecialite() throws Exception {
        // Initialize the database
        specialiteRepository.saveAndFlush(specialite);

        // Get the specialite
        restSpecialiteMockMvc
            .perform(get(ENTITY_API_URL_ID, specialite.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(specialite.getId().intValue()))
            .andExpect(jsonPath("$.nomSpecialites").value(DEFAULT_NOM_SPECIALITES))
            .andExpect(jsonPath("$.sigleSpecialites").value(DEFAULT_SIGLE_SPECIALITES))
            .andExpect(jsonPath("$.specialiteParticulierYN").value(DEFAULT_SPECIALITE_PARTICULIER_YN.booleanValue()))
            .andExpect(jsonPath("$.specialitesPayanteYN").value(DEFAULT_SPECIALITES_PAYANTE_YN.booleanValue()))
            .andExpect(jsonPath("$.actifYN").value(DEFAULT_ACTIF_YN.booleanValue()));
    }

    @Test
    @Transactional
    void getNonExistingSpecialite() throws Exception {
        // Get the specialite
        restSpecialiteMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingSpecialite() throws Exception {
        // Initialize the database
        specialiteRepository.saveAndFlush(specialite);

        int databaseSizeBeforeUpdate = specialiteRepository.findAll().size();
        specialiteSearchRepository.save(specialite);
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(specialiteSearchRepository.findAll());

        // Update the specialite
        Specialite updatedSpecialite = specialiteRepository.findById(specialite.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedSpecialite are not directly saved in db
        em.detach(updatedSpecialite);
        updatedSpecialite
            .nomSpecialites(UPDATED_NOM_SPECIALITES)
            .sigleSpecialites(UPDATED_SIGLE_SPECIALITES)
            .specialiteParticulierYN(UPDATED_SPECIALITE_PARTICULIER_YN)
            .specialitesPayanteYN(UPDATED_SPECIALITES_PAYANTE_YN)
            .actifYN(UPDATED_ACTIF_YN);
        SpecialiteDTO specialiteDTO = specialiteMapper.toDto(updatedSpecialite);

        restSpecialiteMockMvc
            .perform(
                put(ENTITY_API_URL_ID, specialiteDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(specialiteDTO))
            )
            .andExpect(status().isOk());

        // Validate the Specialite in the database
        List<Specialite> specialiteList = specialiteRepository.findAll();
        assertThat(specialiteList).hasSize(databaseSizeBeforeUpdate);
        Specialite testSpecialite = specialiteList.get(specialiteList.size() - 1);
        assertThat(testSpecialite.getNomSpecialites()).isEqualTo(UPDATED_NOM_SPECIALITES);
        assertThat(testSpecialite.getSigleSpecialites()).isEqualTo(UPDATED_SIGLE_SPECIALITES);
        assertThat(testSpecialite.getSpecialiteParticulierYN()).isEqualTo(UPDATED_SPECIALITE_PARTICULIER_YN);
        assertThat(testSpecialite.getSpecialitesPayanteYN()).isEqualTo(UPDATED_SPECIALITES_PAYANTE_YN);
        assertThat(testSpecialite.getActifYN()).isEqualTo(UPDATED_ACTIF_YN);
        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(specialiteSearchRepository.findAll());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
                List<Specialite> specialiteSearchList = IterableUtils.toList(specialiteSearchRepository.findAll());
                Specialite testSpecialiteSearch = specialiteSearchList.get(searchDatabaseSizeAfter - 1);
                assertThat(testSpecialiteSearch.getNomSpecialites()).isEqualTo(UPDATED_NOM_SPECIALITES);
                assertThat(testSpecialiteSearch.getSigleSpecialites()).isEqualTo(UPDATED_SIGLE_SPECIALITES);
                assertThat(testSpecialiteSearch.getSpecialiteParticulierYN()).isEqualTo(UPDATED_SPECIALITE_PARTICULIER_YN);
                assertThat(testSpecialiteSearch.getSpecialitesPayanteYN()).isEqualTo(UPDATED_SPECIALITES_PAYANTE_YN);
                assertThat(testSpecialiteSearch.getActifYN()).isEqualTo(UPDATED_ACTIF_YN);
            });
    }

    @Test
    @Transactional
    void putNonExistingSpecialite() throws Exception {
        int databaseSizeBeforeUpdate = specialiteRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(specialiteSearchRepository.findAll());
        specialite.setId(longCount.incrementAndGet());

        // Create the Specialite
        SpecialiteDTO specialiteDTO = specialiteMapper.toDto(specialite);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSpecialiteMockMvc
            .perform(
                put(ENTITY_API_URL_ID, specialiteDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(specialiteDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Specialite in the database
        List<Specialite> specialiteList = specialiteRepository.findAll();
        assertThat(specialiteList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(specialiteSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void putWithIdMismatchSpecialite() throws Exception {
        int databaseSizeBeforeUpdate = specialiteRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(specialiteSearchRepository.findAll());
        specialite.setId(longCount.incrementAndGet());

        // Create the Specialite
        SpecialiteDTO specialiteDTO = specialiteMapper.toDto(specialite);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSpecialiteMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(specialiteDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Specialite in the database
        List<Specialite> specialiteList = specialiteRepository.findAll();
        assertThat(specialiteList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(specialiteSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamSpecialite() throws Exception {
        int databaseSizeBeforeUpdate = specialiteRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(specialiteSearchRepository.findAll());
        specialite.setId(longCount.incrementAndGet());

        // Create the Specialite
        SpecialiteDTO specialiteDTO = specialiteMapper.toDto(specialite);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSpecialiteMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(specialiteDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Specialite in the database
        List<Specialite> specialiteList = specialiteRepository.findAll();
        assertThat(specialiteList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(specialiteSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void partialUpdateSpecialiteWithPatch() throws Exception {
        // Initialize the database
        specialiteRepository.saveAndFlush(specialite);

        int databaseSizeBeforeUpdate = specialiteRepository.findAll().size();

        // Update the specialite using partial update
        Specialite partialUpdatedSpecialite = new Specialite();
        partialUpdatedSpecialite.setId(specialite.getId());

        partialUpdatedSpecialite
            .nomSpecialites(UPDATED_NOM_SPECIALITES)
            .specialitesPayanteYN(UPDATED_SPECIALITES_PAYANTE_YN)
            .actifYN(UPDATED_ACTIF_YN);

        restSpecialiteMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSpecialite.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedSpecialite))
            )
            .andExpect(status().isOk());

        // Validate the Specialite in the database
        List<Specialite> specialiteList = specialiteRepository.findAll();
        assertThat(specialiteList).hasSize(databaseSizeBeforeUpdate);
        Specialite testSpecialite = specialiteList.get(specialiteList.size() - 1);
        assertThat(testSpecialite.getNomSpecialites()).isEqualTo(UPDATED_NOM_SPECIALITES);
        assertThat(testSpecialite.getSigleSpecialites()).isEqualTo(DEFAULT_SIGLE_SPECIALITES);
        assertThat(testSpecialite.getSpecialiteParticulierYN()).isEqualTo(DEFAULT_SPECIALITE_PARTICULIER_YN);
        assertThat(testSpecialite.getSpecialitesPayanteYN()).isEqualTo(UPDATED_SPECIALITES_PAYANTE_YN);
        assertThat(testSpecialite.getActifYN()).isEqualTo(UPDATED_ACTIF_YN);
    }

    @Test
    @Transactional
    void fullUpdateSpecialiteWithPatch() throws Exception {
        // Initialize the database
        specialiteRepository.saveAndFlush(specialite);

        int databaseSizeBeforeUpdate = specialiteRepository.findAll().size();

        // Update the specialite using partial update
        Specialite partialUpdatedSpecialite = new Specialite();
        partialUpdatedSpecialite.setId(specialite.getId());

        partialUpdatedSpecialite
            .nomSpecialites(UPDATED_NOM_SPECIALITES)
            .sigleSpecialites(UPDATED_SIGLE_SPECIALITES)
            .specialiteParticulierYN(UPDATED_SPECIALITE_PARTICULIER_YN)
            .specialitesPayanteYN(UPDATED_SPECIALITES_PAYANTE_YN)
            .actifYN(UPDATED_ACTIF_YN);

        restSpecialiteMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSpecialite.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedSpecialite))
            )
            .andExpect(status().isOk());

        // Validate the Specialite in the database
        List<Specialite> specialiteList = specialiteRepository.findAll();
        assertThat(specialiteList).hasSize(databaseSizeBeforeUpdate);
        Specialite testSpecialite = specialiteList.get(specialiteList.size() - 1);
        assertThat(testSpecialite.getNomSpecialites()).isEqualTo(UPDATED_NOM_SPECIALITES);
        assertThat(testSpecialite.getSigleSpecialites()).isEqualTo(UPDATED_SIGLE_SPECIALITES);
        assertThat(testSpecialite.getSpecialiteParticulierYN()).isEqualTo(UPDATED_SPECIALITE_PARTICULIER_YN);
        assertThat(testSpecialite.getSpecialitesPayanteYN()).isEqualTo(UPDATED_SPECIALITES_PAYANTE_YN);
        assertThat(testSpecialite.getActifYN()).isEqualTo(UPDATED_ACTIF_YN);
    }

    @Test
    @Transactional
    void patchNonExistingSpecialite() throws Exception {
        int databaseSizeBeforeUpdate = specialiteRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(specialiteSearchRepository.findAll());
        specialite.setId(longCount.incrementAndGet());

        // Create the Specialite
        SpecialiteDTO specialiteDTO = specialiteMapper.toDto(specialite);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSpecialiteMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, specialiteDTO.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(specialiteDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Specialite in the database
        List<Specialite> specialiteList = specialiteRepository.findAll();
        assertThat(specialiteList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(specialiteSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void patchWithIdMismatchSpecialite() throws Exception {
        int databaseSizeBeforeUpdate = specialiteRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(specialiteSearchRepository.findAll());
        specialite.setId(longCount.incrementAndGet());

        // Create the Specialite
        SpecialiteDTO specialiteDTO = specialiteMapper.toDto(specialite);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSpecialiteMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(specialiteDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Specialite in the database
        List<Specialite> specialiteList = specialiteRepository.findAll();
        assertThat(specialiteList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(specialiteSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamSpecialite() throws Exception {
        int databaseSizeBeforeUpdate = specialiteRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(specialiteSearchRepository.findAll());
        specialite.setId(longCount.incrementAndGet());

        // Create the Specialite
        SpecialiteDTO specialiteDTO = specialiteMapper.toDto(specialite);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSpecialiteMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(specialiteDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Specialite in the database
        List<Specialite> specialiteList = specialiteRepository.findAll();
        assertThat(specialiteList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(specialiteSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void deleteSpecialite() throws Exception {
        // Initialize the database
        specialiteRepository.saveAndFlush(specialite);
        specialiteRepository.save(specialite);
        specialiteSearchRepository.save(specialite);

        int databaseSizeBeforeDelete = specialiteRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(specialiteSearchRepository.findAll());
        assertThat(searchDatabaseSizeBefore).isEqualTo(databaseSizeBeforeDelete);

        // Delete the specialite
        restSpecialiteMockMvc
            .perform(delete(ENTITY_API_URL_ID, specialite.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Specialite> specialiteList = specialiteRepository.findAll();
        assertThat(specialiteList).hasSize(databaseSizeBeforeDelete - 1);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(specialiteSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore - 1);
    }

    @Test
    @Transactional
    void searchSpecialite() throws Exception {
        // Initialize the database
        specialite = specialiteRepository.saveAndFlush(specialite);
        specialiteSearchRepository.save(specialite);

        // Search the specialite
        restSpecialiteMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + specialite.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(specialite.getId().intValue())))
            .andExpect(jsonPath("$.[*].nomSpecialites").value(hasItem(DEFAULT_NOM_SPECIALITES)))
            .andExpect(jsonPath("$.[*].sigleSpecialites").value(hasItem(DEFAULT_SIGLE_SPECIALITES)))
            .andExpect(jsonPath("$.[*].specialiteParticulierYN").value(hasItem(DEFAULT_SPECIALITE_PARTICULIER_YN.booleanValue())))
            .andExpect(jsonPath("$.[*].specialitesPayanteYN").value(hasItem(DEFAULT_SPECIALITES_PAYANTE_YN.booleanValue())))
            .andExpect(jsonPath("$.[*].actifYN").value(hasItem(DEFAULT_ACTIF_YN.booleanValue())));
    }
}
