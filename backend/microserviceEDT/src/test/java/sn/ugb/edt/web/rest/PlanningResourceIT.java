package sn.ugb.edt.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import jakarta.persistence.EntityManager;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
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
import sn.ugb.edt.IntegrationTest;
import sn.ugb.edt.domain.Planning;
import sn.ugb.edt.repository.PlanningRepository;
import sn.ugb.edt.repository.search.PlanningSearchRepository;
import sn.ugb.edt.service.dto.PlanningDTO;
import sn.ugb.edt.service.mapper.PlanningMapper;

/**
 * Integration tests for the {@link PlanningResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class PlanningResourceIT {

    private static final Instant DEFAULT_DATE_DEBUT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_DATE_DEBUT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_DATE_FIN = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_DATE_FIN = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/plannings";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/plannings/_search";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private PlanningRepository planningRepository;

    @Autowired
    private PlanningMapper planningMapper;

    @Autowired
    private PlanningSearchRepository planningSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restPlanningMockMvc;

    private Planning planning;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Planning createEntity(EntityManager em) {
        Planning planning = new Planning().dateDebut(DEFAULT_DATE_DEBUT).dateFin(DEFAULT_DATE_FIN);
        return planning;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Planning createUpdatedEntity(EntityManager em) {
        Planning planning = new Planning().dateDebut(UPDATED_DATE_DEBUT).dateFin(UPDATED_DATE_FIN);
        return planning;
    }

    @AfterEach
    public void cleanupElasticSearchRepository() {
        planningSearchRepository.deleteAll();
        assertThat(planningSearchRepository.count()).isEqualTo(0);
    }

    @BeforeEach
    public void initTest() {
        planning = createEntity(em);
    }

    @Test
    @Transactional
    void createPlanning() throws Exception {
        int databaseSizeBeforeCreate = planningRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(planningSearchRepository.findAll());
        // Create the Planning
        PlanningDTO planningDTO = planningMapper.toDto(planning);
        restPlanningMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(planningDTO))
            )
            .andExpect(status().isCreated());

        // Validate the Planning in the database
        List<Planning> planningList = planningRepository.findAll();
        assertThat(planningList).hasSize(databaseSizeBeforeCreate + 1);
        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(planningSearchRepository.findAll());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore + 1);
            });
        Planning testPlanning = planningList.get(planningList.size() - 1);
        assertThat(testPlanning.getDateDebut()).isEqualTo(DEFAULT_DATE_DEBUT);
        assertThat(testPlanning.getDateFin()).isEqualTo(DEFAULT_DATE_FIN);
    }

    @Test
    @Transactional
    void createPlanningWithExistingId() throws Exception {
        // Create the Planning with an existing ID
        planning.setId(1L);
        PlanningDTO planningDTO = planningMapper.toDto(planning);

        int databaseSizeBeforeCreate = planningRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(planningSearchRepository.findAll());

        // An entity with an existing ID cannot be created, so this API call must fail
        restPlanningMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(planningDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Planning in the database
        List<Planning> planningList = planningRepository.findAll();
        assertThat(planningList).hasSize(databaseSizeBeforeCreate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(planningSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void getAllPlannings() throws Exception {
        // Initialize the database
        planningRepository.saveAndFlush(planning);

        // Get all the planningList
        restPlanningMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(planning.getId().intValue())))
            .andExpect(jsonPath("$.[*].dateDebut").value(hasItem(DEFAULT_DATE_DEBUT.toString())))
            .andExpect(jsonPath("$.[*].dateFin").value(hasItem(DEFAULT_DATE_FIN.toString())));
    }

    @Test
    @Transactional
    void getPlanning() throws Exception {
        // Initialize the database
        planningRepository.saveAndFlush(planning);

        // Get the planning
        restPlanningMockMvc
            .perform(get(ENTITY_API_URL_ID, planning.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(planning.getId().intValue()))
            .andExpect(jsonPath("$.dateDebut").value(DEFAULT_DATE_DEBUT.toString()))
            .andExpect(jsonPath("$.dateFin").value(DEFAULT_DATE_FIN.toString()));
    }

    @Test
    @Transactional
    void getNonExistingPlanning() throws Exception {
        // Get the planning
        restPlanningMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingPlanning() throws Exception {
        // Initialize the database
        planningRepository.saveAndFlush(planning);

        int databaseSizeBeforeUpdate = planningRepository.findAll().size();
        planningSearchRepository.save(planning);
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(planningSearchRepository.findAll());

        // Update the planning
        Planning updatedPlanning = planningRepository.findById(planning.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedPlanning are not directly saved in db
        em.detach(updatedPlanning);
        updatedPlanning.dateDebut(UPDATED_DATE_DEBUT).dateFin(UPDATED_DATE_FIN);
        PlanningDTO planningDTO = planningMapper.toDto(updatedPlanning);

        restPlanningMockMvc
            .perform(
                put(ENTITY_API_URL_ID, planningDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(planningDTO))
            )
            .andExpect(status().isOk());

        // Validate the Planning in the database
        List<Planning> planningList = planningRepository.findAll();
        assertThat(planningList).hasSize(databaseSizeBeforeUpdate);
        Planning testPlanning = planningList.get(planningList.size() - 1);
        assertThat(testPlanning.getDateDebut()).isEqualTo(UPDATED_DATE_DEBUT);
        assertThat(testPlanning.getDateFin()).isEqualTo(UPDATED_DATE_FIN);
        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(planningSearchRepository.findAll());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
                List<Planning> planningSearchList = IterableUtils.toList(planningSearchRepository.findAll());
                Planning testPlanningSearch = planningSearchList.get(searchDatabaseSizeAfter - 1);
                assertThat(testPlanningSearch.getDateDebut()).isEqualTo(UPDATED_DATE_DEBUT);
                assertThat(testPlanningSearch.getDateFin()).isEqualTo(UPDATED_DATE_FIN);
            });
    }

    @Test
    @Transactional
    void putNonExistingPlanning() throws Exception {
        int databaseSizeBeforeUpdate = planningRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(planningSearchRepository.findAll());
        planning.setId(longCount.incrementAndGet());

        // Create the Planning
        PlanningDTO planningDTO = planningMapper.toDto(planning);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPlanningMockMvc
            .perform(
                put(ENTITY_API_URL_ID, planningDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(planningDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Planning in the database
        List<Planning> planningList = planningRepository.findAll();
        assertThat(planningList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(planningSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void putWithIdMismatchPlanning() throws Exception {
        int databaseSizeBeforeUpdate = planningRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(planningSearchRepository.findAll());
        planning.setId(longCount.incrementAndGet());

        // Create the Planning
        PlanningDTO planningDTO = planningMapper.toDto(planning);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPlanningMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(planningDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Planning in the database
        List<Planning> planningList = planningRepository.findAll();
        assertThat(planningList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(planningSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamPlanning() throws Exception {
        int databaseSizeBeforeUpdate = planningRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(planningSearchRepository.findAll());
        planning.setId(longCount.incrementAndGet());

        // Create the Planning
        PlanningDTO planningDTO = planningMapper.toDto(planning);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPlanningMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(planningDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Planning in the database
        List<Planning> planningList = planningRepository.findAll();
        assertThat(planningList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(planningSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void partialUpdatePlanningWithPatch() throws Exception {
        // Initialize the database
        planningRepository.saveAndFlush(planning);

        int databaseSizeBeforeUpdate = planningRepository.findAll().size();

        // Update the planning using partial update
        Planning partialUpdatedPlanning = new Planning();
        partialUpdatedPlanning.setId(planning.getId());

        partialUpdatedPlanning.dateDebut(UPDATED_DATE_DEBUT);

        restPlanningMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPlanning.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPlanning))
            )
            .andExpect(status().isOk());

        // Validate the Planning in the database
        List<Planning> planningList = planningRepository.findAll();
        assertThat(planningList).hasSize(databaseSizeBeforeUpdate);
        Planning testPlanning = planningList.get(planningList.size() - 1);
        assertThat(testPlanning.getDateDebut()).isEqualTo(UPDATED_DATE_DEBUT);
        assertThat(testPlanning.getDateFin()).isEqualTo(DEFAULT_DATE_FIN);
    }

    @Test
    @Transactional
    void fullUpdatePlanningWithPatch() throws Exception {
        // Initialize the database
        planningRepository.saveAndFlush(planning);

        int databaseSizeBeforeUpdate = planningRepository.findAll().size();

        // Update the planning using partial update
        Planning partialUpdatedPlanning = new Planning();
        partialUpdatedPlanning.setId(planning.getId());

        partialUpdatedPlanning.dateDebut(UPDATED_DATE_DEBUT).dateFin(UPDATED_DATE_FIN);

        restPlanningMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPlanning.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPlanning))
            )
            .andExpect(status().isOk());

        // Validate the Planning in the database
        List<Planning> planningList = planningRepository.findAll();
        assertThat(planningList).hasSize(databaseSizeBeforeUpdate);
        Planning testPlanning = planningList.get(planningList.size() - 1);
        assertThat(testPlanning.getDateDebut()).isEqualTo(UPDATED_DATE_DEBUT);
        assertThat(testPlanning.getDateFin()).isEqualTo(UPDATED_DATE_FIN);
    }

    @Test
    @Transactional
    void patchNonExistingPlanning() throws Exception {
        int databaseSizeBeforeUpdate = planningRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(planningSearchRepository.findAll());
        planning.setId(longCount.incrementAndGet());

        // Create the Planning
        PlanningDTO planningDTO = planningMapper.toDto(planning);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPlanningMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, planningDTO.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(planningDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Planning in the database
        List<Planning> planningList = planningRepository.findAll();
        assertThat(planningList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(planningSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void patchWithIdMismatchPlanning() throws Exception {
        int databaseSizeBeforeUpdate = planningRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(planningSearchRepository.findAll());
        planning.setId(longCount.incrementAndGet());

        // Create the Planning
        PlanningDTO planningDTO = planningMapper.toDto(planning);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPlanningMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(planningDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Planning in the database
        List<Planning> planningList = planningRepository.findAll();
        assertThat(planningList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(planningSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamPlanning() throws Exception {
        int databaseSizeBeforeUpdate = planningRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(planningSearchRepository.findAll());
        planning.setId(longCount.incrementAndGet());

        // Create the Planning
        PlanningDTO planningDTO = planningMapper.toDto(planning);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPlanningMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(planningDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Planning in the database
        List<Planning> planningList = planningRepository.findAll();
        assertThat(planningList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(planningSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void deletePlanning() throws Exception {
        // Initialize the database
        planningRepository.saveAndFlush(planning);
        planningRepository.save(planning);
        planningSearchRepository.save(planning);

        int databaseSizeBeforeDelete = planningRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(planningSearchRepository.findAll());
        assertThat(searchDatabaseSizeBefore).isEqualTo(databaseSizeBeforeDelete);

        // Delete the planning
        restPlanningMockMvc
            .perform(delete(ENTITY_API_URL_ID, planning.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Planning> planningList = planningRepository.findAll();
        assertThat(planningList).hasSize(databaseSizeBeforeDelete - 1);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(planningSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore - 1);
    }

    @Test
    @Transactional
    void searchPlanning() throws Exception {
        // Initialize the database
        planning = planningRepository.saveAndFlush(planning);
        planningSearchRepository.save(planning);

        // Search the planning
        restPlanningMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + planning.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(planning.getId().intValue())))
            .andExpect(jsonPath("$.[*].dateDebut").value(hasItem(DEFAULT_DATE_DEBUT.toString())))
            .andExpect(jsonPath("$.[*].dateFin").value(hasItem(DEFAULT_DATE_FIN.toString())));
    }
}
