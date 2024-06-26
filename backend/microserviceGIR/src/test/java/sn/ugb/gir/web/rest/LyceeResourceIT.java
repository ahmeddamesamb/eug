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
import sn.ugb.gir.domain.Lycee;
import sn.ugb.gir.repository.LyceeRepository;
import sn.ugb.gir.repository.search.LyceeSearchRepository;
import sn.ugb.gir.service.dto.LyceeDTO;
import sn.ugb.gir.service.mapper.LyceeMapper;

/**
 * Integration tests for the {@link LyceeResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class LyceeResourceIT {

    private static final String DEFAULT_NOM_LYCEE = "AAAAAAAAAA";
    private static final String UPDATED_NOM_LYCEE = "BBBBBBBBBB";

    private static final String DEFAULT_CODE_LYCEE = "AAAAAAAAAA";
    private static final String UPDATED_CODE_LYCEE = "BBBBBBBBBB";

    private static final String DEFAULT_VILLE_LYCEE = "AAAAAAAAAA";
    private static final String UPDATED_VILLE_LYCEE = "BBBBBBBBBB";

    private static final Integer DEFAULT_ACADEMIE_LYCEE = 1;
    private static final Integer UPDATED_ACADEMIE_LYCEE = 2;

    private static final String DEFAULT_CENTRE_EXAMEN = "AAAAAAAAAA";
    private static final String UPDATED_CENTRE_EXAMEN = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/lycees";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/lycees/_search";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private LyceeRepository lyceeRepository;

    @Autowired
    private LyceeMapper lyceeMapper;

    @Autowired
    private LyceeSearchRepository lyceeSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restLyceeMockMvc;

    private Lycee lycee;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Lycee createEntity(EntityManager em) {
        Lycee lycee = new Lycee()
            .nomLycee(DEFAULT_NOM_LYCEE)
            .codeLycee(DEFAULT_CODE_LYCEE)
            .villeLycee(DEFAULT_VILLE_LYCEE)
            .academieLycee(DEFAULT_ACADEMIE_LYCEE)
            .centreExamen(DEFAULT_CENTRE_EXAMEN);
        return lycee;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Lycee createUpdatedEntity(EntityManager em) {
        Lycee lycee = new Lycee()
            .nomLycee(UPDATED_NOM_LYCEE)
            .codeLycee(UPDATED_CODE_LYCEE)
            .villeLycee(UPDATED_VILLE_LYCEE)
            .academieLycee(UPDATED_ACADEMIE_LYCEE)
            .centreExamen(UPDATED_CENTRE_EXAMEN);
        return lycee;
    }

    @AfterEach
    public void cleanupElasticSearchRepository() {
        lyceeSearchRepository.deleteAll();
        assertThat(lyceeSearchRepository.count()).isEqualTo(0);
    }

    @BeforeEach
    public void initTest() {
        lycee = createEntity(em);
    }

    @Test
    @Transactional
    void createLycee() throws Exception {
        int databaseSizeBeforeCreate = lyceeRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(lyceeSearchRepository.findAll());
        // Create the Lycee
        LyceeDTO lyceeDTO = lyceeMapper.toDto(lycee);
        restLyceeMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(lyceeDTO))
            )
            .andExpect(status().isCreated());

        // Validate the Lycee in the database
        List<Lycee> lyceeList = lyceeRepository.findAll();
        assertThat(lyceeList).hasSize(databaseSizeBeforeCreate + 1);
        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(lyceeSearchRepository.findAll());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore + 1);
            });
        Lycee testLycee = lyceeList.get(lyceeList.size() - 1);
        assertThat(testLycee.getNomLycee()).isEqualTo(DEFAULT_NOM_LYCEE);
        assertThat(testLycee.getCodeLycee()).isEqualTo(DEFAULT_CODE_LYCEE);
        assertThat(testLycee.getVilleLycee()).isEqualTo(DEFAULT_VILLE_LYCEE);
        assertThat(testLycee.getAcademieLycee()).isEqualTo(DEFAULT_ACADEMIE_LYCEE);
        assertThat(testLycee.getCentreExamen()).isEqualTo(DEFAULT_CENTRE_EXAMEN);
    }

    @Test
    @Transactional
    void createLyceeWithExistingId() throws Exception {
        // Create the Lycee with an existing ID
        lycee.setId(1L);
        LyceeDTO lyceeDTO = lyceeMapper.toDto(lycee);

        int databaseSizeBeforeCreate = lyceeRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(lyceeSearchRepository.findAll());

        // An entity with an existing ID cannot be created, so this API call must fail
        restLyceeMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(lyceeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Lycee in the database
        List<Lycee> lyceeList = lyceeRepository.findAll();
        assertThat(lyceeList).hasSize(databaseSizeBeforeCreate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(lyceeSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void checkNomLyceeIsRequired() throws Exception {
        int databaseSizeBeforeTest = lyceeRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(lyceeSearchRepository.findAll());
        // set the field null
        lycee.setNomLycee(null);

        // Create the Lycee, which fails.
        LyceeDTO lyceeDTO = lyceeMapper.toDto(lycee);

        restLyceeMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(lyceeDTO))
            )
            .andExpect(status().isBadRequest());

        List<Lycee> lyceeList = lyceeRepository.findAll();
        assertThat(lyceeList).hasSize(databaseSizeBeforeTest);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(lyceeSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void getAllLycees() throws Exception {
        // Initialize the database
        lyceeRepository.saveAndFlush(lycee);

        // Get all the lyceeList
        restLyceeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(lycee.getId().intValue())))
            .andExpect(jsonPath("$.[*].nomLycee").value(hasItem(DEFAULT_NOM_LYCEE)))
            .andExpect(jsonPath("$.[*].codeLycee").value(hasItem(DEFAULT_CODE_LYCEE)))
            .andExpect(jsonPath("$.[*].villeLycee").value(hasItem(DEFAULT_VILLE_LYCEE)))
            .andExpect(jsonPath("$.[*].academieLycee").value(hasItem(DEFAULT_ACADEMIE_LYCEE)))
            .andExpect(jsonPath("$.[*].centreExamen").value(hasItem(DEFAULT_CENTRE_EXAMEN)));
    }

    @Test
    @Transactional
    void getLycee() throws Exception {
        // Initialize the database
        lyceeRepository.saveAndFlush(lycee);

        // Get the lycee
        restLyceeMockMvc
            .perform(get(ENTITY_API_URL_ID, lycee.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(lycee.getId().intValue()))
            .andExpect(jsonPath("$.nomLycee").value(DEFAULT_NOM_LYCEE))
            .andExpect(jsonPath("$.codeLycee").value(DEFAULT_CODE_LYCEE))
            .andExpect(jsonPath("$.villeLycee").value(DEFAULT_VILLE_LYCEE))
            .andExpect(jsonPath("$.academieLycee").value(DEFAULT_ACADEMIE_LYCEE))
            .andExpect(jsonPath("$.centreExamen").value(DEFAULT_CENTRE_EXAMEN));
    }

    @Test
    @Transactional
    void getNonExistingLycee() throws Exception {
        // Get the lycee
        restLyceeMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingLycee() throws Exception {
        // Initialize the database
        lyceeRepository.saveAndFlush(lycee);

        int databaseSizeBeforeUpdate = lyceeRepository.findAll().size();
        lyceeSearchRepository.save(lycee);
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(lyceeSearchRepository.findAll());

        // Update the lycee
        Lycee updatedLycee = lyceeRepository.findById(lycee.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedLycee are not directly saved in db
        em.detach(updatedLycee);
        updatedLycee
            .nomLycee(UPDATED_NOM_LYCEE)
            .codeLycee(UPDATED_CODE_LYCEE)
            .villeLycee(UPDATED_VILLE_LYCEE)
            .academieLycee(UPDATED_ACADEMIE_LYCEE)
            .centreExamen(UPDATED_CENTRE_EXAMEN);
        LyceeDTO lyceeDTO = lyceeMapper.toDto(updatedLycee);

        restLyceeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, lyceeDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(lyceeDTO))
            )
            .andExpect(status().isOk());

        // Validate the Lycee in the database
        List<Lycee> lyceeList = lyceeRepository.findAll();
        assertThat(lyceeList).hasSize(databaseSizeBeforeUpdate);
        Lycee testLycee = lyceeList.get(lyceeList.size() - 1);
        assertThat(testLycee.getNomLycee()).isEqualTo(UPDATED_NOM_LYCEE);
        assertThat(testLycee.getCodeLycee()).isEqualTo(UPDATED_CODE_LYCEE);
        assertThat(testLycee.getVilleLycee()).isEqualTo(UPDATED_VILLE_LYCEE);
        assertThat(testLycee.getAcademieLycee()).isEqualTo(UPDATED_ACADEMIE_LYCEE);
        assertThat(testLycee.getCentreExamen()).isEqualTo(UPDATED_CENTRE_EXAMEN);
        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(lyceeSearchRepository.findAll());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
                List<Lycee> lyceeSearchList = IterableUtils.toList(lyceeSearchRepository.findAll());
                Lycee testLyceeSearch = lyceeSearchList.get(searchDatabaseSizeAfter - 1);
                assertThat(testLyceeSearch.getNomLycee()).isEqualTo(UPDATED_NOM_LYCEE);
                assertThat(testLyceeSearch.getCodeLycee()).isEqualTo(UPDATED_CODE_LYCEE);
                assertThat(testLyceeSearch.getVilleLycee()).isEqualTo(UPDATED_VILLE_LYCEE);
                assertThat(testLyceeSearch.getAcademieLycee()).isEqualTo(UPDATED_ACADEMIE_LYCEE);
                assertThat(testLyceeSearch.getCentreExamen()).isEqualTo(UPDATED_CENTRE_EXAMEN);
            });
    }

    @Test
    @Transactional
    void putNonExistingLycee() throws Exception {
        int databaseSizeBeforeUpdate = lyceeRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(lyceeSearchRepository.findAll());
        lycee.setId(longCount.incrementAndGet());

        // Create the Lycee
        LyceeDTO lyceeDTO = lyceeMapper.toDto(lycee);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restLyceeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, lyceeDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(lyceeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Lycee in the database
        List<Lycee> lyceeList = lyceeRepository.findAll();
        assertThat(lyceeList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(lyceeSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void putWithIdMismatchLycee() throws Exception {
        int databaseSizeBeforeUpdate = lyceeRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(lyceeSearchRepository.findAll());
        lycee.setId(longCount.incrementAndGet());

        // Create the Lycee
        LyceeDTO lyceeDTO = lyceeMapper.toDto(lycee);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLyceeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(lyceeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Lycee in the database
        List<Lycee> lyceeList = lyceeRepository.findAll();
        assertThat(lyceeList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(lyceeSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamLycee() throws Exception {
        int databaseSizeBeforeUpdate = lyceeRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(lyceeSearchRepository.findAll());
        lycee.setId(longCount.incrementAndGet());

        // Create the Lycee
        LyceeDTO lyceeDTO = lyceeMapper.toDto(lycee);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLyceeMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(lyceeDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Lycee in the database
        List<Lycee> lyceeList = lyceeRepository.findAll();
        assertThat(lyceeList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(lyceeSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void partialUpdateLyceeWithPatch() throws Exception {
        // Initialize the database
        lyceeRepository.saveAndFlush(lycee);

        int databaseSizeBeforeUpdate = lyceeRepository.findAll().size();

        // Update the lycee using partial update
        Lycee partialUpdatedLycee = new Lycee();
        partialUpdatedLycee.setId(lycee.getId());

        partialUpdatedLycee.nomLycee(UPDATED_NOM_LYCEE).centreExamen(UPDATED_CENTRE_EXAMEN);

        restLyceeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedLycee.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedLycee))
            )
            .andExpect(status().isOk());

        // Validate the Lycee in the database
        List<Lycee> lyceeList = lyceeRepository.findAll();
        assertThat(lyceeList).hasSize(databaseSizeBeforeUpdate);
        Lycee testLycee = lyceeList.get(lyceeList.size() - 1);
        assertThat(testLycee.getNomLycee()).isEqualTo(UPDATED_NOM_LYCEE);
        assertThat(testLycee.getCodeLycee()).isEqualTo(DEFAULT_CODE_LYCEE);
        assertThat(testLycee.getVilleLycee()).isEqualTo(DEFAULT_VILLE_LYCEE);
        assertThat(testLycee.getAcademieLycee()).isEqualTo(DEFAULT_ACADEMIE_LYCEE);
        assertThat(testLycee.getCentreExamen()).isEqualTo(UPDATED_CENTRE_EXAMEN);
    }

    @Test
    @Transactional
    void fullUpdateLyceeWithPatch() throws Exception {
        // Initialize the database
        lyceeRepository.saveAndFlush(lycee);

        int databaseSizeBeforeUpdate = lyceeRepository.findAll().size();

        // Update the lycee using partial update
        Lycee partialUpdatedLycee = new Lycee();
        partialUpdatedLycee.setId(lycee.getId());

        partialUpdatedLycee
            .nomLycee(UPDATED_NOM_LYCEE)
            .codeLycee(UPDATED_CODE_LYCEE)
            .villeLycee(UPDATED_VILLE_LYCEE)
            .academieLycee(UPDATED_ACADEMIE_LYCEE)
            .centreExamen(UPDATED_CENTRE_EXAMEN);

        restLyceeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedLycee.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedLycee))
            )
            .andExpect(status().isOk());

        // Validate the Lycee in the database
        List<Lycee> lyceeList = lyceeRepository.findAll();
        assertThat(lyceeList).hasSize(databaseSizeBeforeUpdate);
        Lycee testLycee = lyceeList.get(lyceeList.size() - 1);
        assertThat(testLycee.getNomLycee()).isEqualTo(UPDATED_NOM_LYCEE);
        assertThat(testLycee.getCodeLycee()).isEqualTo(UPDATED_CODE_LYCEE);
        assertThat(testLycee.getVilleLycee()).isEqualTo(UPDATED_VILLE_LYCEE);
        assertThat(testLycee.getAcademieLycee()).isEqualTo(UPDATED_ACADEMIE_LYCEE);
        assertThat(testLycee.getCentreExamen()).isEqualTo(UPDATED_CENTRE_EXAMEN);
    }

    @Test
    @Transactional
    void patchNonExistingLycee() throws Exception {
        int databaseSizeBeforeUpdate = lyceeRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(lyceeSearchRepository.findAll());
        lycee.setId(longCount.incrementAndGet());

        // Create the Lycee
        LyceeDTO lyceeDTO = lyceeMapper.toDto(lycee);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restLyceeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, lyceeDTO.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(lyceeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Lycee in the database
        List<Lycee> lyceeList = lyceeRepository.findAll();
        assertThat(lyceeList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(lyceeSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void patchWithIdMismatchLycee() throws Exception {
        int databaseSizeBeforeUpdate = lyceeRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(lyceeSearchRepository.findAll());
        lycee.setId(longCount.incrementAndGet());

        // Create the Lycee
        LyceeDTO lyceeDTO = lyceeMapper.toDto(lycee);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLyceeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(lyceeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Lycee in the database
        List<Lycee> lyceeList = lyceeRepository.findAll();
        assertThat(lyceeList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(lyceeSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamLycee() throws Exception {
        int databaseSizeBeforeUpdate = lyceeRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(lyceeSearchRepository.findAll());
        lycee.setId(longCount.incrementAndGet());

        // Create the Lycee
        LyceeDTO lyceeDTO = lyceeMapper.toDto(lycee);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLyceeMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(lyceeDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Lycee in the database
        List<Lycee> lyceeList = lyceeRepository.findAll();
        assertThat(lyceeList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(lyceeSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void deleteLycee() throws Exception {
        // Initialize the database
        lyceeRepository.saveAndFlush(lycee);
        lyceeRepository.save(lycee);
        lyceeSearchRepository.save(lycee);

        int databaseSizeBeforeDelete = lyceeRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(lyceeSearchRepository.findAll());
        assertThat(searchDatabaseSizeBefore).isEqualTo(databaseSizeBeforeDelete);

        // Delete the lycee
        restLyceeMockMvc
            .perform(delete(ENTITY_API_URL_ID, lycee.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Lycee> lyceeList = lyceeRepository.findAll();
        assertThat(lyceeList).hasSize(databaseSizeBeforeDelete - 1);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(lyceeSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore - 1);
    }

    @Test
    @Transactional
    void searchLycee() throws Exception {
        // Initialize the database
        lycee = lyceeRepository.saveAndFlush(lycee);
        lyceeSearchRepository.save(lycee);

        // Search the lycee
        restLyceeMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + lycee.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(lycee.getId().intValue())))
            .andExpect(jsonPath("$.[*].nomLycee").value(hasItem(DEFAULT_NOM_LYCEE)))
            .andExpect(jsonPath("$.[*].codeLycee").value(hasItem(DEFAULT_CODE_LYCEE)))
            .andExpect(jsonPath("$.[*].villeLycee").value(hasItem(DEFAULT_VILLE_LYCEE)))
            .andExpect(jsonPath("$.[*].academieLycee").value(hasItem(DEFAULT_ACADEMIE_LYCEE)))
            .andExpect(jsonPath("$.[*].centreExamen").value(hasItem(DEFAULT_CENTRE_EXAMEN)));
    }
}
