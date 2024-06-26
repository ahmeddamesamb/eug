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
import sn.ugb.gir.domain.InscriptionDoctorat;
import sn.ugb.gir.repository.InscriptionDoctoratRepository;
import sn.ugb.gir.repository.search.InscriptionDoctoratSearchRepository;
import sn.ugb.gir.service.dto.InscriptionDoctoratDTO;
import sn.ugb.gir.service.mapper.InscriptionDoctoratMapper;

/**
 * Integration tests for the {@link InscriptionDoctoratResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class InscriptionDoctoratResourceIT {

    private static final String DEFAULT_SOURCE_FINANCEMENT = "AAAAAAAAAA";
    private static final String UPDATED_SOURCE_FINANCEMENT = "BBBBBBBBBB";

    private static final String DEFAULT_CO_ENCADREUR_ID = "AAAAAAAAAA";
    private static final String UPDATED_CO_ENCADREUR_ID = "BBBBBBBBBB";

    private static final Integer DEFAULT_NOMBRE_INSCRIPTION = 1;
    private static final Integer UPDATED_NOMBRE_INSCRIPTION = 2;

    private static final String ENTITY_API_URL = "/api/inscription-doctorats";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/inscription-doctorats/_search";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private InscriptionDoctoratRepository inscriptionDoctoratRepository;

    @Autowired
    private InscriptionDoctoratMapper inscriptionDoctoratMapper;

    @Autowired
    private InscriptionDoctoratSearchRepository inscriptionDoctoratSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restInscriptionDoctoratMockMvc;

    private InscriptionDoctorat inscriptionDoctorat;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static InscriptionDoctorat createEntity(EntityManager em) {
        InscriptionDoctorat inscriptionDoctorat = new InscriptionDoctorat()
            .sourceFinancement(DEFAULT_SOURCE_FINANCEMENT)
            .coEncadreurId(DEFAULT_CO_ENCADREUR_ID)
            .nombreInscription(DEFAULT_NOMBRE_INSCRIPTION);
        return inscriptionDoctorat;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static InscriptionDoctorat createUpdatedEntity(EntityManager em) {
        InscriptionDoctorat inscriptionDoctorat = new InscriptionDoctorat()
            .sourceFinancement(UPDATED_SOURCE_FINANCEMENT)
            .coEncadreurId(UPDATED_CO_ENCADREUR_ID)
            .nombreInscription(UPDATED_NOMBRE_INSCRIPTION);
        return inscriptionDoctorat;
    }

    @AfterEach
    public void cleanupElasticSearchRepository() {
        inscriptionDoctoratSearchRepository.deleteAll();
        assertThat(inscriptionDoctoratSearchRepository.count()).isEqualTo(0);
    }

    @BeforeEach
    public void initTest() {
        inscriptionDoctorat = createEntity(em);
    }

    @Test
    @Transactional
    void createInscriptionDoctorat() throws Exception {
        int databaseSizeBeforeCreate = inscriptionDoctoratRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(inscriptionDoctoratSearchRepository.findAll());
        // Create the InscriptionDoctorat
        InscriptionDoctoratDTO inscriptionDoctoratDTO = inscriptionDoctoratMapper.toDto(inscriptionDoctorat);
        restInscriptionDoctoratMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(inscriptionDoctoratDTO))
            )
            .andExpect(status().isCreated());

        // Validate the InscriptionDoctorat in the database
        List<InscriptionDoctorat> inscriptionDoctoratList = inscriptionDoctoratRepository.findAll();
        assertThat(inscriptionDoctoratList).hasSize(databaseSizeBeforeCreate + 1);
        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(inscriptionDoctoratSearchRepository.findAll());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore + 1);
            });
        InscriptionDoctorat testInscriptionDoctorat = inscriptionDoctoratList.get(inscriptionDoctoratList.size() - 1);
        assertThat(testInscriptionDoctorat.getSourceFinancement()).isEqualTo(DEFAULT_SOURCE_FINANCEMENT);
        assertThat(testInscriptionDoctorat.getCoEncadreurId()).isEqualTo(DEFAULT_CO_ENCADREUR_ID);
        assertThat(testInscriptionDoctorat.getNombreInscription()).isEqualTo(DEFAULT_NOMBRE_INSCRIPTION);
    }

    @Test
    @Transactional
    void createInscriptionDoctoratWithExistingId() throws Exception {
        // Create the InscriptionDoctorat with an existing ID
        inscriptionDoctorat.setId(1L);
        InscriptionDoctoratDTO inscriptionDoctoratDTO = inscriptionDoctoratMapper.toDto(inscriptionDoctorat);

        int databaseSizeBeforeCreate = inscriptionDoctoratRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(inscriptionDoctoratSearchRepository.findAll());

        // An entity with an existing ID cannot be created, so this API call must fail
        restInscriptionDoctoratMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(inscriptionDoctoratDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the InscriptionDoctorat in the database
        List<InscriptionDoctorat> inscriptionDoctoratList = inscriptionDoctoratRepository.findAll();
        assertThat(inscriptionDoctoratList).hasSize(databaseSizeBeforeCreate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(inscriptionDoctoratSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void getAllInscriptionDoctorats() throws Exception {
        // Initialize the database
        inscriptionDoctoratRepository.saveAndFlush(inscriptionDoctorat);

        // Get all the inscriptionDoctoratList
        restInscriptionDoctoratMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(inscriptionDoctorat.getId().intValue())))
            .andExpect(jsonPath("$.[*].sourceFinancement").value(hasItem(DEFAULT_SOURCE_FINANCEMENT)))
            .andExpect(jsonPath("$.[*].coEncadreurId").value(hasItem(DEFAULT_CO_ENCADREUR_ID)))
            .andExpect(jsonPath("$.[*].nombreInscription").value(hasItem(DEFAULT_NOMBRE_INSCRIPTION)));
    }

    @Test
    @Transactional
    void getInscriptionDoctorat() throws Exception {
        // Initialize the database
        inscriptionDoctoratRepository.saveAndFlush(inscriptionDoctorat);

        // Get the inscriptionDoctorat
        restInscriptionDoctoratMockMvc
            .perform(get(ENTITY_API_URL_ID, inscriptionDoctorat.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(inscriptionDoctorat.getId().intValue()))
            .andExpect(jsonPath("$.sourceFinancement").value(DEFAULT_SOURCE_FINANCEMENT))
            .andExpect(jsonPath("$.coEncadreurId").value(DEFAULT_CO_ENCADREUR_ID))
            .andExpect(jsonPath("$.nombreInscription").value(DEFAULT_NOMBRE_INSCRIPTION));
    }

    @Test
    @Transactional
    void getNonExistingInscriptionDoctorat() throws Exception {
        // Get the inscriptionDoctorat
        restInscriptionDoctoratMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingInscriptionDoctorat() throws Exception {
        // Initialize the database
        inscriptionDoctoratRepository.saveAndFlush(inscriptionDoctorat);

        int databaseSizeBeforeUpdate = inscriptionDoctoratRepository.findAll().size();
        inscriptionDoctoratSearchRepository.save(inscriptionDoctorat);
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(inscriptionDoctoratSearchRepository.findAll());

        // Update the inscriptionDoctorat
        InscriptionDoctorat updatedInscriptionDoctorat = inscriptionDoctoratRepository.findById(inscriptionDoctorat.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedInscriptionDoctorat are not directly saved in db
        em.detach(updatedInscriptionDoctorat);
        updatedInscriptionDoctorat
            .sourceFinancement(UPDATED_SOURCE_FINANCEMENT)
            .coEncadreurId(UPDATED_CO_ENCADREUR_ID)
            .nombreInscription(UPDATED_NOMBRE_INSCRIPTION);
        InscriptionDoctoratDTO inscriptionDoctoratDTO = inscriptionDoctoratMapper.toDto(updatedInscriptionDoctorat);

        restInscriptionDoctoratMockMvc
            .perform(
                put(ENTITY_API_URL_ID, inscriptionDoctoratDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(inscriptionDoctoratDTO))
            )
            .andExpect(status().isOk());

        // Validate the InscriptionDoctorat in the database
        List<InscriptionDoctorat> inscriptionDoctoratList = inscriptionDoctoratRepository.findAll();
        assertThat(inscriptionDoctoratList).hasSize(databaseSizeBeforeUpdate);
        InscriptionDoctorat testInscriptionDoctorat = inscriptionDoctoratList.get(inscriptionDoctoratList.size() - 1);
        assertThat(testInscriptionDoctorat.getSourceFinancement()).isEqualTo(UPDATED_SOURCE_FINANCEMENT);
        assertThat(testInscriptionDoctorat.getCoEncadreurId()).isEqualTo(UPDATED_CO_ENCADREUR_ID);
        assertThat(testInscriptionDoctorat.getNombreInscription()).isEqualTo(UPDATED_NOMBRE_INSCRIPTION);
        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(inscriptionDoctoratSearchRepository.findAll());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
                List<InscriptionDoctorat> inscriptionDoctoratSearchList = IterableUtils.toList(
                    inscriptionDoctoratSearchRepository.findAll()
                );
                InscriptionDoctorat testInscriptionDoctoratSearch = inscriptionDoctoratSearchList.get(searchDatabaseSizeAfter - 1);
                assertThat(testInscriptionDoctoratSearch.getSourceFinancement()).isEqualTo(UPDATED_SOURCE_FINANCEMENT);
                assertThat(testInscriptionDoctoratSearch.getCoEncadreurId()).isEqualTo(UPDATED_CO_ENCADREUR_ID);
                assertThat(testInscriptionDoctoratSearch.getNombreInscription()).isEqualTo(UPDATED_NOMBRE_INSCRIPTION);
            });
    }

    @Test
    @Transactional
    void putNonExistingInscriptionDoctorat() throws Exception {
        int databaseSizeBeforeUpdate = inscriptionDoctoratRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(inscriptionDoctoratSearchRepository.findAll());
        inscriptionDoctorat.setId(longCount.incrementAndGet());

        // Create the InscriptionDoctorat
        InscriptionDoctoratDTO inscriptionDoctoratDTO = inscriptionDoctoratMapper.toDto(inscriptionDoctorat);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restInscriptionDoctoratMockMvc
            .perform(
                put(ENTITY_API_URL_ID, inscriptionDoctoratDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(inscriptionDoctoratDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the InscriptionDoctorat in the database
        List<InscriptionDoctorat> inscriptionDoctoratList = inscriptionDoctoratRepository.findAll();
        assertThat(inscriptionDoctoratList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(inscriptionDoctoratSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void putWithIdMismatchInscriptionDoctorat() throws Exception {
        int databaseSizeBeforeUpdate = inscriptionDoctoratRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(inscriptionDoctoratSearchRepository.findAll());
        inscriptionDoctorat.setId(longCount.incrementAndGet());

        // Create the InscriptionDoctorat
        InscriptionDoctoratDTO inscriptionDoctoratDTO = inscriptionDoctoratMapper.toDto(inscriptionDoctorat);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restInscriptionDoctoratMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(inscriptionDoctoratDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the InscriptionDoctorat in the database
        List<InscriptionDoctorat> inscriptionDoctoratList = inscriptionDoctoratRepository.findAll();
        assertThat(inscriptionDoctoratList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(inscriptionDoctoratSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamInscriptionDoctorat() throws Exception {
        int databaseSizeBeforeUpdate = inscriptionDoctoratRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(inscriptionDoctoratSearchRepository.findAll());
        inscriptionDoctorat.setId(longCount.incrementAndGet());

        // Create the InscriptionDoctorat
        InscriptionDoctoratDTO inscriptionDoctoratDTO = inscriptionDoctoratMapper.toDto(inscriptionDoctorat);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restInscriptionDoctoratMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(inscriptionDoctoratDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the InscriptionDoctorat in the database
        List<InscriptionDoctorat> inscriptionDoctoratList = inscriptionDoctoratRepository.findAll();
        assertThat(inscriptionDoctoratList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(inscriptionDoctoratSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void partialUpdateInscriptionDoctoratWithPatch() throws Exception {
        // Initialize the database
        inscriptionDoctoratRepository.saveAndFlush(inscriptionDoctorat);

        int databaseSizeBeforeUpdate = inscriptionDoctoratRepository.findAll().size();

        // Update the inscriptionDoctorat using partial update
        InscriptionDoctorat partialUpdatedInscriptionDoctorat = new InscriptionDoctorat();
        partialUpdatedInscriptionDoctorat.setId(inscriptionDoctorat.getId());

        partialUpdatedInscriptionDoctorat.sourceFinancement(UPDATED_SOURCE_FINANCEMENT);

        restInscriptionDoctoratMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedInscriptionDoctorat.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedInscriptionDoctorat))
            )
            .andExpect(status().isOk());

        // Validate the InscriptionDoctorat in the database
        List<InscriptionDoctorat> inscriptionDoctoratList = inscriptionDoctoratRepository.findAll();
        assertThat(inscriptionDoctoratList).hasSize(databaseSizeBeforeUpdate);
        InscriptionDoctorat testInscriptionDoctorat = inscriptionDoctoratList.get(inscriptionDoctoratList.size() - 1);
        assertThat(testInscriptionDoctorat.getSourceFinancement()).isEqualTo(UPDATED_SOURCE_FINANCEMENT);
        assertThat(testInscriptionDoctorat.getCoEncadreurId()).isEqualTo(DEFAULT_CO_ENCADREUR_ID);
        assertThat(testInscriptionDoctorat.getNombreInscription()).isEqualTo(DEFAULT_NOMBRE_INSCRIPTION);
    }

    @Test
    @Transactional
    void fullUpdateInscriptionDoctoratWithPatch() throws Exception {
        // Initialize the database
        inscriptionDoctoratRepository.saveAndFlush(inscriptionDoctorat);

        int databaseSizeBeforeUpdate = inscriptionDoctoratRepository.findAll().size();

        // Update the inscriptionDoctorat using partial update
        InscriptionDoctorat partialUpdatedInscriptionDoctorat = new InscriptionDoctorat();
        partialUpdatedInscriptionDoctorat.setId(inscriptionDoctorat.getId());

        partialUpdatedInscriptionDoctorat
            .sourceFinancement(UPDATED_SOURCE_FINANCEMENT)
            .coEncadreurId(UPDATED_CO_ENCADREUR_ID)
            .nombreInscription(UPDATED_NOMBRE_INSCRIPTION);

        restInscriptionDoctoratMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedInscriptionDoctorat.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedInscriptionDoctorat))
            )
            .andExpect(status().isOk());

        // Validate the InscriptionDoctorat in the database
        List<InscriptionDoctorat> inscriptionDoctoratList = inscriptionDoctoratRepository.findAll();
        assertThat(inscriptionDoctoratList).hasSize(databaseSizeBeforeUpdate);
        InscriptionDoctorat testInscriptionDoctorat = inscriptionDoctoratList.get(inscriptionDoctoratList.size() - 1);
        assertThat(testInscriptionDoctorat.getSourceFinancement()).isEqualTo(UPDATED_SOURCE_FINANCEMENT);
        assertThat(testInscriptionDoctorat.getCoEncadreurId()).isEqualTo(UPDATED_CO_ENCADREUR_ID);
        assertThat(testInscriptionDoctorat.getNombreInscription()).isEqualTo(UPDATED_NOMBRE_INSCRIPTION);
    }

    @Test
    @Transactional
    void patchNonExistingInscriptionDoctorat() throws Exception {
        int databaseSizeBeforeUpdate = inscriptionDoctoratRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(inscriptionDoctoratSearchRepository.findAll());
        inscriptionDoctorat.setId(longCount.incrementAndGet());

        // Create the InscriptionDoctorat
        InscriptionDoctoratDTO inscriptionDoctoratDTO = inscriptionDoctoratMapper.toDto(inscriptionDoctorat);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restInscriptionDoctoratMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, inscriptionDoctoratDTO.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(inscriptionDoctoratDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the InscriptionDoctorat in the database
        List<InscriptionDoctorat> inscriptionDoctoratList = inscriptionDoctoratRepository.findAll();
        assertThat(inscriptionDoctoratList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(inscriptionDoctoratSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void patchWithIdMismatchInscriptionDoctorat() throws Exception {
        int databaseSizeBeforeUpdate = inscriptionDoctoratRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(inscriptionDoctoratSearchRepository.findAll());
        inscriptionDoctorat.setId(longCount.incrementAndGet());

        // Create the InscriptionDoctorat
        InscriptionDoctoratDTO inscriptionDoctoratDTO = inscriptionDoctoratMapper.toDto(inscriptionDoctorat);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restInscriptionDoctoratMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(inscriptionDoctoratDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the InscriptionDoctorat in the database
        List<InscriptionDoctorat> inscriptionDoctoratList = inscriptionDoctoratRepository.findAll();
        assertThat(inscriptionDoctoratList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(inscriptionDoctoratSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamInscriptionDoctorat() throws Exception {
        int databaseSizeBeforeUpdate = inscriptionDoctoratRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(inscriptionDoctoratSearchRepository.findAll());
        inscriptionDoctorat.setId(longCount.incrementAndGet());

        // Create the InscriptionDoctorat
        InscriptionDoctoratDTO inscriptionDoctoratDTO = inscriptionDoctoratMapper.toDto(inscriptionDoctorat);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restInscriptionDoctoratMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(inscriptionDoctoratDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the InscriptionDoctorat in the database
        List<InscriptionDoctorat> inscriptionDoctoratList = inscriptionDoctoratRepository.findAll();
        assertThat(inscriptionDoctoratList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(inscriptionDoctoratSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void deleteInscriptionDoctorat() throws Exception {
        // Initialize the database
        inscriptionDoctoratRepository.saveAndFlush(inscriptionDoctorat);
        inscriptionDoctoratRepository.save(inscriptionDoctorat);
        inscriptionDoctoratSearchRepository.save(inscriptionDoctorat);

        int databaseSizeBeforeDelete = inscriptionDoctoratRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(inscriptionDoctoratSearchRepository.findAll());
        assertThat(searchDatabaseSizeBefore).isEqualTo(databaseSizeBeforeDelete);

        // Delete the inscriptionDoctorat
        restInscriptionDoctoratMockMvc
            .perform(delete(ENTITY_API_URL_ID, inscriptionDoctorat.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<InscriptionDoctorat> inscriptionDoctoratList = inscriptionDoctoratRepository.findAll();
        assertThat(inscriptionDoctoratList).hasSize(databaseSizeBeforeDelete - 1);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(inscriptionDoctoratSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore - 1);
    }

    @Test
    @Transactional
    void searchInscriptionDoctorat() throws Exception {
        // Initialize the database
        inscriptionDoctorat = inscriptionDoctoratRepository.saveAndFlush(inscriptionDoctorat);
        inscriptionDoctoratSearchRepository.save(inscriptionDoctorat);

        // Search the inscriptionDoctorat
        restInscriptionDoctoratMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + inscriptionDoctorat.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(inscriptionDoctorat.getId().intValue())))
            .andExpect(jsonPath("$.[*].sourceFinancement").value(hasItem(DEFAULT_SOURCE_FINANCEMENT)))
            .andExpect(jsonPath("$.[*].coEncadreurId").value(hasItem(DEFAULT_CO_ENCADREUR_ID)))
            .andExpect(jsonPath("$.[*].nombreInscription").value(hasItem(DEFAULT_NOMBRE_INSCRIPTION)));
    }
}
