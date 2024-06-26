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
import sn.ugb.gir.domain.Cycle;
import sn.ugb.gir.repository.CycleRepository;
import sn.ugb.gir.repository.search.CycleSearchRepository;
import sn.ugb.gir.service.dto.CycleDTO;
import sn.ugb.gir.service.mapper.CycleMapper;

/**
 * Integration tests for the {@link CycleResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class CycleResourceIT {

    private static final String DEFAULT_LIBELLE_CYCLE = "AAAAAAAAAA";
    private static final String UPDATED_LIBELLE_CYCLE = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/cycles";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/cycles/_search";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private CycleRepository cycleRepository;

    @Autowired
    private CycleMapper cycleMapper;

    @Autowired
    private CycleSearchRepository cycleSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restCycleMockMvc;

    private Cycle cycle;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Cycle createEntity(EntityManager em) {
        Cycle cycle = new Cycle().libelleCycle(DEFAULT_LIBELLE_CYCLE);
        return cycle;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Cycle createUpdatedEntity(EntityManager em) {
        Cycle cycle = new Cycle().libelleCycle(UPDATED_LIBELLE_CYCLE);
        return cycle;
    }

    @AfterEach
    public void cleanupElasticSearchRepository() {
        cycleSearchRepository.deleteAll();
        assertThat(cycleSearchRepository.count()).isEqualTo(0);
    }

    @BeforeEach
    public void initTest() {
        cycle = createEntity(em);
    }

    @Test
    @Transactional
    void createCycle() throws Exception {
        int databaseSizeBeforeCreate = cycleRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(cycleSearchRepository.findAll());
        // Create the Cycle
        CycleDTO cycleDTO = cycleMapper.toDto(cycle);
        restCycleMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(cycleDTO))
            )
            .andExpect(status().isCreated());

        // Validate the Cycle in the database
        List<Cycle> cycleList = cycleRepository.findAll();
        assertThat(cycleList).hasSize(databaseSizeBeforeCreate + 1);
        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(cycleSearchRepository.findAll());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore + 1);
            });
        Cycle testCycle = cycleList.get(cycleList.size() - 1);
        assertThat(testCycle.getLibelleCycle()).isEqualTo(DEFAULT_LIBELLE_CYCLE);
    }

    @Test
    @Transactional
    void createCycleWithExistingId() throws Exception {
        // Create the Cycle with an existing ID
        cycle.setId(1L);
        CycleDTO cycleDTO = cycleMapper.toDto(cycle);

        int databaseSizeBeforeCreate = cycleRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(cycleSearchRepository.findAll());

        // An entity with an existing ID cannot be created, so this API call must fail
        restCycleMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(cycleDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Cycle in the database
        List<Cycle> cycleList = cycleRepository.findAll();
        assertThat(cycleList).hasSize(databaseSizeBeforeCreate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(cycleSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void checkLibelleCycleIsRequired() throws Exception {
        int databaseSizeBeforeTest = cycleRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(cycleSearchRepository.findAll());
        // set the field null
        cycle.setLibelleCycle(null);

        // Create the Cycle, which fails.
        CycleDTO cycleDTO = cycleMapper.toDto(cycle);

        restCycleMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(cycleDTO))
            )
            .andExpect(status().isBadRequest());

        List<Cycle> cycleList = cycleRepository.findAll();
        assertThat(cycleList).hasSize(databaseSizeBeforeTest);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(cycleSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void getAllCycles() throws Exception {
        // Initialize the database
        cycleRepository.saveAndFlush(cycle);

        // Get all the cycleList
        restCycleMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(cycle.getId().intValue())))
            .andExpect(jsonPath("$.[*].libelleCycle").value(hasItem(DEFAULT_LIBELLE_CYCLE)));
    }

    @Test
    @Transactional
    void getCycle() throws Exception {
        // Initialize the database
        cycleRepository.saveAndFlush(cycle);

        // Get the cycle
        restCycleMockMvc
            .perform(get(ENTITY_API_URL_ID, cycle.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(cycle.getId().intValue()))
            .andExpect(jsonPath("$.libelleCycle").value(DEFAULT_LIBELLE_CYCLE));
    }

    @Test
    @Transactional
    void getNonExistingCycle() throws Exception {
        // Get the cycle
        restCycleMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingCycle() throws Exception {
        // Initialize the database
        cycleRepository.saveAndFlush(cycle);

        int databaseSizeBeforeUpdate = cycleRepository.findAll().size();
        cycleSearchRepository.save(cycle);
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(cycleSearchRepository.findAll());

        // Update the cycle
        Cycle updatedCycle = cycleRepository.findById(cycle.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedCycle are not directly saved in db
        em.detach(updatedCycle);
        updatedCycle.libelleCycle(UPDATED_LIBELLE_CYCLE);
        CycleDTO cycleDTO = cycleMapper.toDto(updatedCycle);

        restCycleMockMvc
            .perform(
                put(ENTITY_API_URL_ID, cycleDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(cycleDTO))
            )
            .andExpect(status().isOk());

        // Validate the Cycle in the database
        List<Cycle> cycleList = cycleRepository.findAll();
        assertThat(cycleList).hasSize(databaseSizeBeforeUpdate);
        Cycle testCycle = cycleList.get(cycleList.size() - 1);
        assertThat(testCycle.getLibelleCycle()).isEqualTo(UPDATED_LIBELLE_CYCLE);
        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(cycleSearchRepository.findAll());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
                List<Cycle> cycleSearchList = IterableUtils.toList(cycleSearchRepository.findAll());
                Cycle testCycleSearch = cycleSearchList.get(searchDatabaseSizeAfter - 1);
                assertThat(testCycleSearch.getLibelleCycle()).isEqualTo(UPDATED_LIBELLE_CYCLE);
            });
    }

    @Test
    @Transactional
    void putNonExistingCycle() throws Exception {
        int databaseSizeBeforeUpdate = cycleRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(cycleSearchRepository.findAll());
        cycle.setId(longCount.incrementAndGet());

        // Create the Cycle
        CycleDTO cycleDTO = cycleMapper.toDto(cycle);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCycleMockMvc
            .perform(
                put(ENTITY_API_URL_ID, cycleDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(cycleDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Cycle in the database
        List<Cycle> cycleList = cycleRepository.findAll();
        assertThat(cycleList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(cycleSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void putWithIdMismatchCycle() throws Exception {
        int databaseSizeBeforeUpdate = cycleRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(cycleSearchRepository.findAll());
        cycle.setId(longCount.incrementAndGet());

        // Create the Cycle
        CycleDTO cycleDTO = cycleMapper.toDto(cycle);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCycleMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(cycleDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Cycle in the database
        List<Cycle> cycleList = cycleRepository.findAll();
        assertThat(cycleList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(cycleSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamCycle() throws Exception {
        int databaseSizeBeforeUpdate = cycleRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(cycleSearchRepository.findAll());
        cycle.setId(longCount.incrementAndGet());

        // Create the Cycle
        CycleDTO cycleDTO = cycleMapper.toDto(cycle);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCycleMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(cycleDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Cycle in the database
        List<Cycle> cycleList = cycleRepository.findAll();
        assertThat(cycleList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(cycleSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void partialUpdateCycleWithPatch() throws Exception {
        // Initialize the database
        cycleRepository.saveAndFlush(cycle);

        int databaseSizeBeforeUpdate = cycleRepository.findAll().size();

        // Update the cycle using partial update
        Cycle partialUpdatedCycle = new Cycle();
        partialUpdatedCycle.setId(cycle.getId());

        restCycleMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCycle.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCycle))
            )
            .andExpect(status().isOk());

        // Validate the Cycle in the database
        List<Cycle> cycleList = cycleRepository.findAll();
        assertThat(cycleList).hasSize(databaseSizeBeforeUpdate);
        Cycle testCycle = cycleList.get(cycleList.size() - 1);
        assertThat(testCycle.getLibelleCycle()).isEqualTo(DEFAULT_LIBELLE_CYCLE);
    }

    @Test
    @Transactional
    void fullUpdateCycleWithPatch() throws Exception {
        // Initialize the database
        cycleRepository.saveAndFlush(cycle);

        int databaseSizeBeforeUpdate = cycleRepository.findAll().size();

        // Update the cycle using partial update
        Cycle partialUpdatedCycle = new Cycle();
        partialUpdatedCycle.setId(cycle.getId());

        partialUpdatedCycle.libelleCycle(UPDATED_LIBELLE_CYCLE);

        restCycleMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCycle.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCycle))
            )
            .andExpect(status().isOk());

        // Validate the Cycle in the database
        List<Cycle> cycleList = cycleRepository.findAll();
        assertThat(cycleList).hasSize(databaseSizeBeforeUpdate);
        Cycle testCycle = cycleList.get(cycleList.size() - 1);
        assertThat(testCycle.getLibelleCycle()).isEqualTo(UPDATED_LIBELLE_CYCLE);
    }

    @Test
    @Transactional
    void patchNonExistingCycle() throws Exception {
        int databaseSizeBeforeUpdate = cycleRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(cycleSearchRepository.findAll());
        cycle.setId(longCount.incrementAndGet());

        // Create the Cycle
        CycleDTO cycleDTO = cycleMapper.toDto(cycle);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCycleMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, cycleDTO.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(cycleDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Cycle in the database
        List<Cycle> cycleList = cycleRepository.findAll();
        assertThat(cycleList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(cycleSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void patchWithIdMismatchCycle() throws Exception {
        int databaseSizeBeforeUpdate = cycleRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(cycleSearchRepository.findAll());
        cycle.setId(longCount.incrementAndGet());

        // Create the Cycle
        CycleDTO cycleDTO = cycleMapper.toDto(cycle);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCycleMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(cycleDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Cycle in the database
        List<Cycle> cycleList = cycleRepository.findAll();
        assertThat(cycleList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(cycleSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamCycle() throws Exception {
        int databaseSizeBeforeUpdate = cycleRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(cycleSearchRepository.findAll());
        cycle.setId(longCount.incrementAndGet());

        // Create the Cycle
        CycleDTO cycleDTO = cycleMapper.toDto(cycle);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCycleMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(cycleDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Cycle in the database
        List<Cycle> cycleList = cycleRepository.findAll();
        assertThat(cycleList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(cycleSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void deleteCycle() throws Exception {
        // Initialize the database
        cycleRepository.saveAndFlush(cycle);
        cycleRepository.save(cycle);
        cycleSearchRepository.save(cycle);

        int databaseSizeBeforeDelete = cycleRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(cycleSearchRepository.findAll());
        assertThat(searchDatabaseSizeBefore).isEqualTo(databaseSizeBeforeDelete);

        // Delete the cycle
        restCycleMockMvc
            .perform(delete(ENTITY_API_URL_ID, cycle.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Cycle> cycleList = cycleRepository.findAll();
        assertThat(cycleList).hasSize(databaseSizeBeforeDelete - 1);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(cycleSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore - 1);
    }

    @Test
    @Transactional
    void searchCycle() throws Exception {
        // Initialize the database
        cycle = cycleRepository.saveAndFlush(cycle);
        cycleSearchRepository.save(cycle);

        // Search the cycle
        restCycleMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + cycle.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(cycle.getId().intValue())))
            .andExpect(jsonPath("$.[*].libelleCycle").value(hasItem(DEFAULT_LIBELLE_CYCLE)));
    }
}
