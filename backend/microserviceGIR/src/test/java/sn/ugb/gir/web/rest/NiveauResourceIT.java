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
import sn.ugb.gir.domain.Niveau;
import sn.ugb.gir.repository.NiveauRepository;
import sn.ugb.gir.repository.search.NiveauSearchRepository;
import sn.ugb.gir.service.dto.NiveauDTO;
import sn.ugb.gir.service.mapper.NiveauMapper;

/**
 * Integration tests for the {@link NiveauResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class NiveauResourceIT {

    private static final String DEFAULT_LIBELLE_NIVEAU = "AAAAAAAAAA";
    private static final String UPDATED_LIBELLE_NIVEAU = "BBBBBBBBBB";

    private static final String DEFAULT_CODE_NIVEAU = "AAAAAAAAAA";
    private static final String UPDATED_CODE_NIVEAU = "BBBBBBBBBB";

    private static final String DEFAULT_ANNEE_ETUDE = "AAAAAAAAAA";
    private static final String UPDATED_ANNEE_ETUDE = "BBBBBBBBBB";

    private static final Boolean DEFAULT_ACTIF_YN = false;
    private static final Boolean UPDATED_ACTIF_YN = true;

    private static final String ENTITY_API_URL = "/api/niveaus";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/niveaus/_search";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private NiveauRepository niveauRepository;

    @Autowired
    private NiveauMapper niveauMapper;

    @Autowired
    private NiveauSearchRepository niveauSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restNiveauMockMvc;

    private Niveau niveau;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Niveau createEntity(EntityManager em) {
        Niveau niveau = new Niveau()
            .libelleNiveau(DEFAULT_LIBELLE_NIVEAU)
            .codeNiveau(DEFAULT_CODE_NIVEAU)
            .anneeEtude(DEFAULT_ANNEE_ETUDE)
            .actifYN(DEFAULT_ACTIF_YN);
        return niveau;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Niveau createUpdatedEntity(EntityManager em) {
        Niveau niveau = new Niveau()
            .libelleNiveau(UPDATED_LIBELLE_NIVEAU)
            .codeNiveau(UPDATED_CODE_NIVEAU)
            .anneeEtude(UPDATED_ANNEE_ETUDE)
            .actifYN(UPDATED_ACTIF_YN);
        return niveau;
    }

    @AfterEach
    public void cleanupElasticSearchRepository() {
        niveauSearchRepository.deleteAll();
        assertThat(niveauSearchRepository.count()).isEqualTo(0);
    }

    @BeforeEach
    public void initTest() {
        niveau = createEntity(em);
    }

    @Test
    @Transactional
    void createNiveau() throws Exception {
        int databaseSizeBeforeCreate = niveauRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(niveauSearchRepository.findAll());
        // Create the Niveau
        NiveauDTO niveauDTO = niveauMapper.toDto(niveau);
        restNiveauMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(niveauDTO))
            )
            .andExpect(status().isCreated());

        // Validate the Niveau in the database
        List<Niveau> niveauList = niveauRepository.findAll();
        assertThat(niveauList).hasSize(databaseSizeBeforeCreate + 1);
        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(niveauSearchRepository.findAll());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore + 1);
            });
        Niveau testNiveau = niveauList.get(niveauList.size() - 1);
        assertThat(testNiveau.getLibelleNiveau()).isEqualTo(DEFAULT_LIBELLE_NIVEAU);
        assertThat(testNiveau.getCodeNiveau()).isEqualTo(DEFAULT_CODE_NIVEAU);
        assertThat(testNiveau.getAnneeEtude()).isEqualTo(DEFAULT_ANNEE_ETUDE);
        assertThat(testNiveau.getActifYN()).isEqualTo(DEFAULT_ACTIF_YN);
    }

    @Test
    @Transactional
    void createNiveauWithExistingId() throws Exception {
        // Create the Niveau with an existing ID
        niveau.setId(1L);
        NiveauDTO niveauDTO = niveauMapper.toDto(niveau);

        int databaseSizeBeforeCreate = niveauRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(niveauSearchRepository.findAll());

        // An entity with an existing ID cannot be created, so this API call must fail
        restNiveauMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(niveauDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Niveau in the database
        List<Niveau> niveauList = niveauRepository.findAll();
        assertThat(niveauList).hasSize(databaseSizeBeforeCreate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(niveauSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void checkLibelleNiveauIsRequired() throws Exception {
        int databaseSizeBeforeTest = niveauRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(niveauSearchRepository.findAll());
        // set the field null
        niveau.setLibelleNiveau(null);

        // Create the Niveau, which fails.
        NiveauDTO niveauDTO = niveauMapper.toDto(niveau);

        restNiveauMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(niveauDTO))
            )
            .andExpect(status().isBadRequest());

        List<Niveau> niveauList = niveauRepository.findAll();
        assertThat(niveauList).hasSize(databaseSizeBeforeTest);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(niveauSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void checkCodeNiveauIsRequired() throws Exception {
        int databaseSizeBeforeTest = niveauRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(niveauSearchRepository.findAll());
        // set the field null
        niveau.setCodeNiveau(null);

        // Create the Niveau, which fails.
        NiveauDTO niveauDTO = niveauMapper.toDto(niveau);

        restNiveauMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(niveauDTO))
            )
            .andExpect(status().isBadRequest());

        List<Niveau> niveauList = niveauRepository.findAll();
        assertThat(niveauList).hasSize(databaseSizeBeforeTest);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(niveauSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void getAllNiveaus() throws Exception {
        // Initialize the database
        niveauRepository.saveAndFlush(niveau);

        // Get all the niveauList
        restNiveauMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(niveau.getId().intValue())))
            .andExpect(jsonPath("$.[*].libelleNiveau").value(hasItem(DEFAULT_LIBELLE_NIVEAU)))
            .andExpect(jsonPath("$.[*].codeNiveau").value(hasItem(DEFAULT_CODE_NIVEAU)))
            .andExpect(jsonPath("$.[*].anneeEtude").value(hasItem(DEFAULT_ANNEE_ETUDE)))
            .andExpect(jsonPath("$.[*].actifYN").value(hasItem(DEFAULT_ACTIF_YN.booleanValue())));
    }

    @Test
    @Transactional
    void getNiveau() throws Exception {
        // Initialize the database
        niveauRepository.saveAndFlush(niveau);

        // Get the niveau
        restNiveauMockMvc
            .perform(get(ENTITY_API_URL_ID, niveau.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(niveau.getId().intValue()))
            .andExpect(jsonPath("$.libelleNiveau").value(DEFAULT_LIBELLE_NIVEAU))
            .andExpect(jsonPath("$.codeNiveau").value(DEFAULT_CODE_NIVEAU))
            .andExpect(jsonPath("$.anneeEtude").value(DEFAULT_ANNEE_ETUDE))
            .andExpect(jsonPath("$.actifYN").value(DEFAULT_ACTIF_YN.booleanValue()));
    }

    @Test
    @Transactional
    void getNonExistingNiveau() throws Exception {
        // Get the niveau
        restNiveauMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingNiveau() throws Exception {
        // Initialize the database
        niveauRepository.saveAndFlush(niveau);

        int databaseSizeBeforeUpdate = niveauRepository.findAll().size();
        niveauSearchRepository.save(niveau);
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(niveauSearchRepository.findAll());

        // Update the niveau
        Niveau updatedNiveau = niveauRepository.findById(niveau.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedNiveau are not directly saved in db
        em.detach(updatedNiveau);
        updatedNiveau
            .libelleNiveau(UPDATED_LIBELLE_NIVEAU)
            .codeNiveau(UPDATED_CODE_NIVEAU)
            .anneeEtude(UPDATED_ANNEE_ETUDE)
            .actifYN(UPDATED_ACTIF_YN);
        NiveauDTO niveauDTO = niveauMapper.toDto(updatedNiveau);

        restNiveauMockMvc
            .perform(
                put(ENTITY_API_URL_ID, niveauDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(niveauDTO))
            )
            .andExpect(status().isOk());

        // Validate the Niveau in the database
        List<Niveau> niveauList = niveauRepository.findAll();
        assertThat(niveauList).hasSize(databaseSizeBeforeUpdate);
        Niveau testNiveau = niveauList.get(niveauList.size() - 1);
        assertThat(testNiveau.getLibelleNiveau()).isEqualTo(UPDATED_LIBELLE_NIVEAU);
        assertThat(testNiveau.getCodeNiveau()).isEqualTo(UPDATED_CODE_NIVEAU);
        assertThat(testNiveau.getAnneeEtude()).isEqualTo(UPDATED_ANNEE_ETUDE);
        assertThat(testNiveau.getActifYN()).isEqualTo(UPDATED_ACTIF_YN);
        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(niveauSearchRepository.findAll());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
                List<Niveau> niveauSearchList = IterableUtils.toList(niveauSearchRepository.findAll());
                Niveau testNiveauSearch = niveauSearchList.get(searchDatabaseSizeAfter - 1);
                assertThat(testNiveauSearch.getLibelleNiveau()).isEqualTo(UPDATED_LIBELLE_NIVEAU);
                assertThat(testNiveauSearch.getCodeNiveau()).isEqualTo(UPDATED_CODE_NIVEAU);
                assertThat(testNiveauSearch.getAnneeEtude()).isEqualTo(UPDATED_ANNEE_ETUDE);
                assertThat(testNiveauSearch.getActifYN()).isEqualTo(UPDATED_ACTIF_YN);
            });
    }

    @Test
    @Transactional
    void putNonExistingNiveau() throws Exception {
        int databaseSizeBeforeUpdate = niveauRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(niveauSearchRepository.findAll());
        niveau.setId(longCount.incrementAndGet());

        // Create the Niveau
        NiveauDTO niveauDTO = niveauMapper.toDto(niveau);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restNiveauMockMvc
            .perform(
                put(ENTITY_API_URL_ID, niveauDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(niveauDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Niveau in the database
        List<Niveau> niveauList = niveauRepository.findAll();
        assertThat(niveauList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(niveauSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void putWithIdMismatchNiveau() throws Exception {
        int databaseSizeBeforeUpdate = niveauRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(niveauSearchRepository.findAll());
        niveau.setId(longCount.incrementAndGet());

        // Create the Niveau
        NiveauDTO niveauDTO = niveauMapper.toDto(niveau);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restNiveauMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(niveauDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Niveau in the database
        List<Niveau> niveauList = niveauRepository.findAll();
        assertThat(niveauList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(niveauSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamNiveau() throws Exception {
        int databaseSizeBeforeUpdate = niveauRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(niveauSearchRepository.findAll());
        niveau.setId(longCount.incrementAndGet());

        // Create the Niveau
        NiveauDTO niveauDTO = niveauMapper.toDto(niveau);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restNiveauMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(niveauDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Niveau in the database
        List<Niveau> niveauList = niveauRepository.findAll();
        assertThat(niveauList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(niveauSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void partialUpdateNiveauWithPatch() throws Exception {
        // Initialize the database
        niveauRepository.saveAndFlush(niveau);

        int databaseSizeBeforeUpdate = niveauRepository.findAll().size();

        // Update the niveau using partial update
        Niveau partialUpdatedNiveau = new Niveau();
        partialUpdatedNiveau.setId(niveau.getId());

        partialUpdatedNiveau.codeNiveau(UPDATED_CODE_NIVEAU).anneeEtude(UPDATED_ANNEE_ETUDE);

        restNiveauMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedNiveau.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedNiveau))
            )
            .andExpect(status().isOk());

        // Validate the Niveau in the database
        List<Niveau> niveauList = niveauRepository.findAll();
        assertThat(niveauList).hasSize(databaseSizeBeforeUpdate);
        Niveau testNiveau = niveauList.get(niveauList.size() - 1);
        assertThat(testNiveau.getLibelleNiveau()).isEqualTo(DEFAULT_LIBELLE_NIVEAU);
        assertThat(testNiveau.getCodeNiveau()).isEqualTo(UPDATED_CODE_NIVEAU);
        assertThat(testNiveau.getAnneeEtude()).isEqualTo(UPDATED_ANNEE_ETUDE);
        assertThat(testNiveau.getActifYN()).isEqualTo(DEFAULT_ACTIF_YN);
    }

    @Test
    @Transactional
    void fullUpdateNiveauWithPatch() throws Exception {
        // Initialize the database
        niveauRepository.saveAndFlush(niveau);

        int databaseSizeBeforeUpdate = niveauRepository.findAll().size();

        // Update the niveau using partial update
        Niveau partialUpdatedNiveau = new Niveau();
        partialUpdatedNiveau.setId(niveau.getId());

        partialUpdatedNiveau
            .libelleNiveau(UPDATED_LIBELLE_NIVEAU)
            .codeNiveau(UPDATED_CODE_NIVEAU)
            .anneeEtude(UPDATED_ANNEE_ETUDE)
            .actifYN(UPDATED_ACTIF_YN);

        restNiveauMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedNiveau.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedNiveau))
            )
            .andExpect(status().isOk());

        // Validate the Niveau in the database
        List<Niveau> niveauList = niveauRepository.findAll();
        assertThat(niveauList).hasSize(databaseSizeBeforeUpdate);
        Niveau testNiveau = niveauList.get(niveauList.size() - 1);
        assertThat(testNiveau.getLibelleNiveau()).isEqualTo(UPDATED_LIBELLE_NIVEAU);
        assertThat(testNiveau.getCodeNiveau()).isEqualTo(UPDATED_CODE_NIVEAU);
        assertThat(testNiveau.getAnneeEtude()).isEqualTo(UPDATED_ANNEE_ETUDE);
        assertThat(testNiveau.getActifYN()).isEqualTo(UPDATED_ACTIF_YN);
    }

    @Test
    @Transactional
    void patchNonExistingNiveau() throws Exception {
        int databaseSizeBeforeUpdate = niveauRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(niveauSearchRepository.findAll());
        niveau.setId(longCount.incrementAndGet());

        // Create the Niveau
        NiveauDTO niveauDTO = niveauMapper.toDto(niveau);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restNiveauMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, niveauDTO.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(niveauDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Niveau in the database
        List<Niveau> niveauList = niveauRepository.findAll();
        assertThat(niveauList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(niveauSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void patchWithIdMismatchNiveau() throws Exception {
        int databaseSizeBeforeUpdate = niveauRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(niveauSearchRepository.findAll());
        niveau.setId(longCount.incrementAndGet());

        // Create the Niveau
        NiveauDTO niveauDTO = niveauMapper.toDto(niveau);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restNiveauMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(niveauDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Niveau in the database
        List<Niveau> niveauList = niveauRepository.findAll();
        assertThat(niveauList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(niveauSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamNiveau() throws Exception {
        int databaseSizeBeforeUpdate = niveauRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(niveauSearchRepository.findAll());
        niveau.setId(longCount.incrementAndGet());

        // Create the Niveau
        NiveauDTO niveauDTO = niveauMapper.toDto(niveau);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restNiveauMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(niveauDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Niveau in the database
        List<Niveau> niveauList = niveauRepository.findAll();
        assertThat(niveauList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(niveauSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void deleteNiveau() throws Exception {
        // Initialize the database
        niveauRepository.saveAndFlush(niveau);
        niveauRepository.save(niveau);
        niveauSearchRepository.save(niveau);

        int databaseSizeBeforeDelete = niveauRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(niveauSearchRepository.findAll());
        assertThat(searchDatabaseSizeBefore).isEqualTo(databaseSizeBeforeDelete);

        // Delete the niveau
        restNiveauMockMvc
            .perform(delete(ENTITY_API_URL_ID, niveau.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Niveau> niveauList = niveauRepository.findAll();
        assertThat(niveauList).hasSize(databaseSizeBeforeDelete - 1);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(niveauSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore - 1);
    }

    @Test
    @Transactional
    void searchNiveau() throws Exception {
        // Initialize the database
        niveau = niveauRepository.saveAndFlush(niveau);
        niveauSearchRepository.save(niveau);

        // Search the niveau
        restNiveauMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + niveau.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(niveau.getId().intValue())))
            .andExpect(jsonPath("$.[*].libelleNiveau").value(hasItem(DEFAULT_LIBELLE_NIVEAU)))
            .andExpect(jsonPath("$.[*].codeNiveau").value(hasItem(DEFAULT_CODE_NIVEAU)))
            .andExpect(jsonPath("$.[*].anneeEtude").value(hasItem(DEFAULT_ANNEE_ETUDE)))
            .andExpect(jsonPath("$.[*].actifYN").value(hasItem(DEFAULT_ACTIF_YN.booleanValue())));
    }
}
