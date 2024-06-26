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
import sn.ugb.gir.domain.Formation;
import sn.ugb.gir.repository.FormationRepository;
import sn.ugb.gir.repository.search.FormationSearchRepository;
import sn.ugb.gir.service.dto.FormationDTO;
import sn.ugb.gir.service.mapper.FormationMapper;

/**
 * Integration tests for the {@link FormationResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class FormationResourceIT {

    private static final Boolean DEFAULT_CLASSE_DIPLOMANTE_YN = false;
    private static final Boolean UPDATED_CLASSE_DIPLOMANTE_YN = true;

    private static final String DEFAULT_LIBELLE_DIPLOME = "AAAAAAAAAA";
    private static final String UPDATED_LIBELLE_DIPLOME = "BBBBBBBBBB";

    private static final String DEFAULT_CODE_FORMATION = "AAAAAAAAAA";
    private static final String UPDATED_CODE_FORMATION = "BBBBBBBBBB";

    private static final Integer DEFAULT_NBRE_CREDITS_MIN = 1;
    private static final Integer UPDATED_NBRE_CREDITS_MIN = 2;

    private static final Boolean DEFAULT_EST_PARCOURS_YN = false;
    private static final Boolean UPDATED_EST_PARCOURS_YN = true;

    private static final Boolean DEFAULT_LMD_YN = false;
    private static final Boolean UPDATED_LMD_YN = true;

    private static final Boolean DEFAULT_ACTIF_YN = false;
    private static final Boolean UPDATED_ACTIF_YN = true;

    private static final String ENTITY_API_URL = "/api/formations";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/formations/_search";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private FormationRepository formationRepository;

    @Autowired
    private FormationMapper formationMapper;

    @Autowired
    private FormationSearchRepository formationSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restFormationMockMvc;

    private Formation formation;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Formation createEntity(EntityManager em) {
        Formation formation = new Formation()
            .classeDiplomanteYN(DEFAULT_CLASSE_DIPLOMANTE_YN)
            .libelleDiplome(DEFAULT_LIBELLE_DIPLOME)
            .codeFormation(DEFAULT_CODE_FORMATION)
            .nbreCreditsMin(DEFAULT_NBRE_CREDITS_MIN)
            .estParcoursYN(DEFAULT_EST_PARCOURS_YN)
            .lmdYN(DEFAULT_LMD_YN)
            .actifYN(DEFAULT_ACTIF_YN);
        return formation;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Formation createUpdatedEntity(EntityManager em) {
        Formation formation = new Formation()
            .classeDiplomanteYN(UPDATED_CLASSE_DIPLOMANTE_YN)
            .libelleDiplome(UPDATED_LIBELLE_DIPLOME)
            .codeFormation(UPDATED_CODE_FORMATION)
            .nbreCreditsMin(UPDATED_NBRE_CREDITS_MIN)
            .estParcoursYN(UPDATED_EST_PARCOURS_YN)
            .lmdYN(UPDATED_LMD_YN)
            .actifYN(UPDATED_ACTIF_YN);
        return formation;
    }

    @AfterEach
    public void cleanupElasticSearchRepository() {
        formationSearchRepository.deleteAll();
        assertThat(formationSearchRepository.count()).isEqualTo(0);
    }

    @BeforeEach
    public void initTest() {
        formation = createEntity(em);
    }

    @Test
    @Transactional
    void createFormation() throws Exception {
        int databaseSizeBeforeCreate = formationRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(formationSearchRepository.findAll());
        // Create the Formation
        FormationDTO formationDTO = formationMapper.toDto(formation);
        restFormationMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(formationDTO))
            )
            .andExpect(status().isCreated());

        // Validate the Formation in the database
        List<Formation> formationList = formationRepository.findAll();
        assertThat(formationList).hasSize(databaseSizeBeforeCreate + 1);
        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(formationSearchRepository.findAll());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore + 1);
            });
        Formation testFormation = formationList.get(formationList.size() - 1);
        assertThat(testFormation.getClasseDiplomanteYN()).isEqualTo(DEFAULT_CLASSE_DIPLOMANTE_YN);
        assertThat(testFormation.getLibelleDiplome()).isEqualTo(DEFAULT_LIBELLE_DIPLOME);
        assertThat(testFormation.getCodeFormation()).isEqualTo(DEFAULT_CODE_FORMATION);
        assertThat(testFormation.getNbreCreditsMin()).isEqualTo(DEFAULT_NBRE_CREDITS_MIN);
        assertThat(testFormation.getEstParcoursYN()).isEqualTo(DEFAULT_EST_PARCOURS_YN);
        assertThat(testFormation.getLmdYN()).isEqualTo(DEFAULT_LMD_YN);
        assertThat(testFormation.getActifYN()).isEqualTo(DEFAULT_ACTIF_YN);
    }

    @Test
    @Transactional
    void createFormationWithExistingId() throws Exception {
        // Create the Formation with an existing ID
        formation.setId(1L);
        FormationDTO formationDTO = formationMapper.toDto(formation);

        int databaseSizeBeforeCreate = formationRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(formationSearchRepository.findAll());

        // An entity with an existing ID cannot be created, so this API call must fail
        restFormationMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(formationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Formation in the database
        List<Formation> formationList = formationRepository.findAll();
        assertThat(formationList).hasSize(databaseSizeBeforeCreate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(formationSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void getAllFormations() throws Exception {
        // Initialize the database
        formationRepository.saveAndFlush(formation);

        // Get all the formationList
        restFormationMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(formation.getId().intValue())))
            .andExpect(jsonPath("$.[*].classeDiplomanteYN").value(hasItem(DEFAULT_CLASSE_DIPLOMANTE_YN.booleanValue())))
            .andExpect(jsonPath("$.[*].libelleDiplome").value(hasItem(DEFAULT_LIBELLE_DIPLOME)))
            .andExpect(jsonPath("$.[*].codeFormation").value(hasItem(DEFAULT_CODE_FORMATION)))
            .andExpect(jsonPath("$.[*].nbreCreditsMin").value(hasItem(DEFAULT_NBRE_CREDITS_MIN)))
            .andExpect(jsonPath("$.[*].estParcoursYN").value(hasItem(DEFAULT_EST_PARCOURS_YN.booleanValue())))
            .andExpect(jsonPath("$.[*].lmdYN").value(hasItem(DEFAULT_LMD_YN.booleanValue())))
            .andExpect(jsonPath("$.[*].actifYN").value(hasItem(DEFAULT_ACTIF_YN.booleanValue())));
    }

    @Test
    @Transactional
    void getFormation() throws Exception {
        // Initialize the database
        formationRepository.saveAndFlush(formation);

        // Get the formation
        restFormationMockMvc
            .perform(get(ENTITY_API_URL_ID, formation.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(formation.getId().intValue()))
            .andExpect(jsonPath("$.classeDiplomanteYN").value(DEFAULT_CLASSE_DIPLOMANTE_YN.booleanValue()))
            .andExpect(jsonPath("$.libelleDiplome").value(DEFAULT_LIBELLE_DIPLOME))
            .andExpect(jsonPath("$.codeFormation").value(DEFAULT_CODE_FORMATION))
            .andExpect(jsonPath("$.nbreCreditsMin").value(DEFAULT_NBRE_CREDITS_MIN))
            .andExpect(jsonPath("$.estParcoursYN").value(DEFAULT_EST_PARCOURS_YN.booleanValue()))
            .andExpect(jsonPath("$.lmdYN").value(DEFAULT_LMD_YN.booleanValue()))
            .andExpect(jsonPath("$.actifYN").value(DEFAULT_ACTIF_YN.booleanValue()));
    }

    @Test
    @Transactional
    void getNonExistingFormation() throws Exception {
        // Get the formation
        restFormationMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingFormation() throws Exception {
        // Initialize the database
        formationRepository.saveAndFlush(formation);

        int databaseSizeBeforeUpdate = formationRepository.findAll().size();
        formationSearchRepository.save(formation);
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(formationSearchRepository.findAll());

        // Update the formation
        Formation updatedFormation = formationRepository.findById(formation.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedFormation are not directly saved in db
        em.detach(updatedFormation);
        updatedFormation
            .classeDiplomanteYN(UPDATED_CLASSE_DIPLOMANTE_YN)
            .libelleDiplome(UPDATED_LIBELLE_DIPLOME)
            .codeFormation(UPDATED_CODE_FORMATION)
            .nbreCreditsMin(UPDATED_NBRE_CREDITS_MIN)
            .estParcoursYN(UPDATED_EST_PARCOURS_YN)
            .lmdYN(UPDATED_LMD_YN)
            .actifYN(UPDATED_ACTIF_YN);
        FormationDTO formationDTO = formationMapper.toDto(updatedFormation);

        restFormationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, formationDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(formationDTO))
            )
            .andExpect(status().isOk());

        // Validate the Formation in the database
        List<Formation> formationList = formationRepository.findAll();
        assertThat(formationList).hasSize(databaseSizeBeforeUpdate);
        Formation testFormation = formationList.get(formationList.size() - 1);
        assertThat(testFormation.getClasseDiplomanteYN()).isEqualTo(UPDATED_CLASSE_DIPLOMANTE_YN);
        assertThat(testFormation.getLibelleDiplome()).isEqualTo(UPDATED_LIBELLE_DIPLOME);
        assertThat(testFormation.getCodeFormation()).isEqualTo(UPDATED_CODE_FORMATION);
        assertThat(testFormation.getNbreCreditsMin()).isEqualTo(UPDATED_NBRE_CREDITS_MIN);
        assertThat(testFormation.getEstParcoursYN()).isEqualTo(UPDATED_EST_PARCOURS_YN);
        assertThat(testFormation.getLmdYN()).isEqualTo(UPDATED_LMD_YN);
        assertThat(testFormation.getActifYN()).isEqualTo(UPDATED_ACTIF_YN);
        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(formationSearchRepository.findAll());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
                List<Formation> formationSearchList = IterableUtils.toList(formationSearchRepository.findAll());
                Formation testFormationSearch = formationSearchList.get(searchDatabaseSizeAfter - 1);
                assertThat(testFormationSearch.getClasseDiplomanteYN()).isEqualTo(UPDATED_CLASSE_DIPLOMANTE_YN);
                assertThat(testFormationSearch.getLibelleDiplome()).isEqualTo(UPDATED_LIBELLE_DIPLOME);
                assertThat(testFormationSearch.getCodeFormation()).isEqualTo(UPDATED_CODE_FORMATION);
                assertThat(testFormationSearch.getNbreCreditsMin()).isEqualTo(UPDATED_NBRE_CREDITS_MIN);
                assertThat(testFormationSearch.getEstParcoursYN()).isEqualTo(UPDATED_EST_PARCOURS_YN);
                assertThat(testFormationSearch.getLmdYN()).isEqualTo(UPDATED_LMD_YN);
                assertThat(testFormationSearch.getActifYN()).isEqualTo(UPDATED_ACTIF_YN);
            });
    }

    @Test
    @Transactional
    void putNonExistingFormation() throws Exception {
        int databaseSizeBeforeUpdate = formationRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(formationSearchRepository.findAll());
        formation.setId(longCount.incrementAndGet());

        // Create the Formation
        FormationDTO formationDTO = formationMapper.toDto(formation);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFormationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, formationDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(formationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Formation in the database
        List<Formation> formationList = formationRepository.findAll();
        assertThat(formationList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(formationSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void putWithIdMismatchFormation() throws Exception {
        int databaseSizeBeforeUpdate = formationRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(formationSearchRepository.findAll());
        formation.setId(longCount.incrementAndGet());

        // Create the Formation
        FormationDTO formationDTO = formationMapper.toDto(formation);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFormationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(formationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Formation in the database
        List<Formation> formationList = formationRepository.findAll();
        assertThat(formationList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(formationSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamFormation() throws Exception {
        int databaseSizeBeforeUpdate = formationRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(formationSearchRepository.findAll());
        formation.setId(longCount.incrementAndGet());

        // Create the Formation
        FormationDTO formationDTO = formationMapper.toDto(formation);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFormationMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(formationDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Formation in the database
        List<Formation> formationList = formationRepository.findAll();
        assertThat(formationList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(formationSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void partialUpdateFormationWithPatch() throws Exception {
        // Initialize the database
        formationRepository.saveAndFlush(formation);

        int databaseSizeBeforeUpdate = formationRepository.findAll().size();

        // Update the formation using partial update
        Formation partialUpdatedFormation = new Formation();
        partialUpdatedFormation.setId(formation.getId());

        partialUpdatedFormation
            .classeDiplomanteYN(UPDATED_CLASSE_DIPLOMANTE_YN)
            .libelleDiplome(UPDATED_LIBELLE_DIPLOME)
            .estParcoursYN(UPDATED_EST_PARCOURS_YN)
            .actifYN(UPDATED_ACTIF_YN);

        restFormationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedFormation.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedFormation))
            )
            .andExpect(status().isOk());

        // Validate the Formation in the database
        List<Formation> formationList = formationRepository.findAll();
        assertThat(formationList).hasSize(databaseSizeBeforeUpdate);
        Formation testFormation = formationList.get(formationList.size() - 1);
        assertThat(testFormation.getClasseDiplomanteYN()).isEqualTo(UPDATED_CLASSE_DIPLOMANTE_YN);
        assertThat(testFormation.getLibelleDiplome()).isEqualTo(UPDATED_LIBELLE_DIPLOME);
        assertThat(testFormation.getCodeFormation()).isEqualTo(DEFAULT_CODE_FORMATION);
        assertThat(testFormation.getNbreCreditsMin()).isEqualTo(DEFAULT_NBRE_CREDITS_MIN);
        assertThat(testFormation.getEstParcoursYN()).isEqualTo(UPDATED_EST_PARCOURS_YN);
        assertThat(testFormation.getLmdYN()).isEqualTo(DEFAULT_LMD_YN);
        assertThat(testFormation.getActifYN()).isEqualTo(UPDATED_ACTIF_YN);
    }

    @Test
    @Transactional
    void fullUpdateFormationWithPatch() throws Exception {
        // Initialize the database
        formationRepository.saveAndFlush(formation);

        int databaseSizeBeforeUpdate = formationRepository.findAll().size();

        // Update the formation using partial update
        Formation partialUpdatedFormation = new Formation();
        partialUpdatedFormation.setId(formation.getId());

        partialUpdatedFormation
            .classeDiplomanteYN(UPDATED_CLASSE_DIPLOMANTE_YN)
            .libelleDiplome(UPDATED_LIBELLE_DIPLOME)
            .codeFormation(UPDATED_CODE_FORMATION)
            .nbreCreditsMin(UPDATED_NBRE_CREDITS_MIN)
            .estParcoursYN(UPDATED_EST_PARCOURS_YN)
            .lmdYN(UPDATED_LMD_YN)
            .actifYN(UPDATED_ACTIF_YN);

        restFormationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedFormation.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedFormation))
            )
            .andExpect(status().isOk());

        // Validate the Formation in the database
        List<Formation> formationList = formationRepository.findAll();
        assertThat(formationList).hasSize(databaseSizeBeforeUpdate);
        Formation testFormation = formationList.get(formationList.size() - 1);
        assertThat(testFormation.getClasseDiplomanteYN()).isEqualTo(UPDATED_CLASSE_DIPLOMANTE_YN);
        assertThat(testFormation.getLibelleDiplome()).isEqualTo(UPDATED_LIBELLE_DIPLOME);
        assertThat(testFormation.getCodeFormation()).isEqualTo(UPDATED_CODE_FORMATION);
        assertThat(testFormation.getNbreCreditsMin()).isEqualTo(UPDATED_NBRE_CREDITS_MIN);
        assertThat(testFormation.getEstParcoursYN()).isEqualTo(UPDATED_EST_PARCOURS_YN);
        assertThat(testFormation.getLmdYN()).isEqualTo(UPDATED_LMD_YN);
        assertThat(testFormation.getActifYN()).isEqualTo(UPDATED_ACTIF_YN);
    }

    @Test
    @Transactional
    void patchNonExistingFormation() throws Exception {
        int databaseSizeBeforeUpdate = formationRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(formationSearchRepository.findAll());
        formation.setId(longCount.incrementAndGet());

        // Create the Formation
        FormationDTO formationDTO = formationMapper.toDto(formation);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFormationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, formationDTO.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(formationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Formation in the database
        List<Formation> formationList = formationRepository.findAll();
        assertThat(formationList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(formationSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void patchWithIdMismatchFormation() throws Exception {
        int databaseSizeBeforeUpdate = formationRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(formationSearchRepository.findAll());
        formation.setId(longCount.incrementAndGet());

        // Create the Formation
        FormationDTO formationDTO = formationMapper.toDto(formation);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFormationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(formationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Formation in the database
        List<Formation> formationList = formationRepository.findAll();
        assertThat(formationList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(formationSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamFormation() throws Exception {
        int databaseSizeBeforeUpdate = formationRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(formationSearchRepository.findAll());
        formation.setId(longCount.incrementAndGet());

        // Create the Formation
        FormationDTO formationDTO = formationMapper.toDto(formation);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFormationMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(formationDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Formation in the database
        List<Formation> formationList = formationRepository.findAll();
        assertThat(formationList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(formationSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void deleteFormation() throws Exception {
        // Initialize the database
        formationRepository.saveAndFlush(formation);
        formationRepository.save(formation);
        formationSearchRepository.save(formation);

        int databaseSizeBeforeDelete = formationRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(formationSearchRepository.findAll());
        assertThat(searchDatabaseSizeBefore).isEqualTo(databaseSizeBeforeDelete);

        // Delete the formation
        restFormationMockMvc
            .perform(delete(ENTITY_API_URL_ID, formation.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Formation> formationList = formationRepository.findAll();
        assertThat(formationList).hasSize(databaseSizeBeforeDelete - 1);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(formationSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore - 1);
    }

    @Test
    @Transactional
    void searchFormation() throws Exception {
        // Initialize the database
        formation = formationRepository.saveAndFlush(formation);
        formationSearchRepository.save(formation);

        // Search the formation
        restFormationMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + formation.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(formation.getId().intValue())))
            .andExpect(jsonPath("$.[*].classeDiplomanteYN").value(hasItem(DEFAULT_CLASSE_DIPLOMANTE_YN.booleanValue())))
            .andExpect(jsonPath("$.[*].libelleDiplome").value(hasItem(DEFAULT_LIBELLE_DIPLOME)))
            .andExpect(jsonPath("$.[*].codeFormation").value(hasItem(DEFAULT_CODE_FORMATION)))
            .andExpect(jsonPath("$.[*].nbreCreditsMin").value(hasItem(DEFAULT_NBRE_CREDITS_MIN)))
            .andExpect(jsonPath("$.[*].estParcoursYN").value(hasItem(DEFAULT_EST_PARCOURS_YN.booleanValue())))
            .andExpect(jsonPath("$.[*].lmdYN").value(hasItem(DEFAULT_LMD_YN.booleanValue())))
            .andExpect(jsonPath("$.[*].actifYN").value(hasItem(DEFAULT_ACTIF_YN.booleanValue())));
    }
}
