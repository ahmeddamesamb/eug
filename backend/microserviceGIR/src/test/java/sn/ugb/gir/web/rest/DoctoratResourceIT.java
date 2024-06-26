package sn.ugb.gir.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import jakarta.persistence.EntityManager;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import sn.ugb.gir.IntegrationTest;
import sn.ugb.gir.domain.Doctorat;
import sn.ugb.gir.repository.DoctoratRepository;
import sn.ugb.gir.repository.search.DoctoratSearchRepository;
import sn.ugb.gir.service.dto.DoctoratDTO;
import sn.ugb.gir.service.mapper.DoctoratMapper;

/**
 * Integration tests for the {@link DoctoratResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class DoctoratResourceIT {

    private static final String DEFAULT_SUJET = "AAAAAAAAAA";
    private static final String UPDATED_SUJET = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_ANNEE_INSCRIPTION_DOCTORAT = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_ANNEE_INSCRIPTION_DOCTORAT = LocalDate.now(ZoneId.systemDefault());

    private static final Long DEFAULT_ENCADREUR_ID = 1L;
    private static final Long UPDATED_ENCADREUR_ID = 2L;

    private static final Long DEFAULT_LABORATOIR_ID = 1L;
    private static final Long UPDATED_LABORATOIR_ID = 2L;

    private static final String ENTITY_API_URL = "/api/doctorats";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/doctorats/_search";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private DoctoratRepository doctoratRepository;

    @Autowired
    private DoctoratMapper doctoratMapper;

    @Autowired
    private DoctoratSearchRepository doctoratSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restDoctoratMockMvc;

    private Doctorat doctorat;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Doctorat createEntity(EntityManager em) {
        Doctorat doctorat = new Doctorat()
            .sujet(DEFAULT_SUJET)
            .anneeInscriptionDoctorat(DEFAULT_ANNEE_INSCRIPTION_DOCTORAT)
            .encadreurId(DEFAULT_ENCADREUR_ID)
            .laboratoirId(DEFAULT_LABORATOIR_ID);
        return doctorat;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Doctorat createUpdatedEntity(EntityManager em) {
        Doctorat doctorat = new Doctorat()
            .sujet(UPDATED_SUJET)
            .anneeInscriptionDoctorat(UPDATED_ANNEE_INSCRIPTION_DOCTORAT)
            .encadreurId(UPDATED_ENCADREUR_ID)
            .laboratoirId(UPDATED_LABORATOIR_ID);
        return doctorat;
    }

    @AfterEach
    public void cleanupElasticSearchRepository() {
        doctoratSearchRepository.deleteAll();
        assertThat(doctoratSearchRepository.count()).isEqualTo(0);
    }

    @BeforeEach
    public void initTest() {
        doctorat = createEntity(em);
    }

    @Test
    @Transactional
    void createDoctorat() throws Exception {
        int databaseSizeBeforeCreate = doctoratRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(doctoratSearchRepository.findAll());
        // Create the Doctorat
        DoctoratDTO doctoratDTO = doctoratMapper.toDto(doctorat);
        restDoctoratMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(doctoratDTO))
            )
            .andExpect(status().isCreated());

        // Validate the Doctorat in the database
        List<Doctorat> doctoratList = doctoratRepository.findAll();
        assertThat(doctoratList).hasSize(databaseSizeBeforeCreate + 1);
        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(doctoratSearchRepository.findAll());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore + 1);
            });
        Doctorat testDoctorat = doctoratList.get(doctoratList.size() - 1);
        assertThat(testDoctorat.getSujet()).isEqualTo(DEFAULT_SUJET);
        assertThat(testDoctorat.getAnneeInscriptionDoctorat()).isEqualTo(DEFAULT_ANNEE_INSCRIPTION_DOCTORAT);
        assertThat(testDoctorat.getEncadreurId()).isEqualTo(DEFAULT_ENCADREUR_ID);
        assertThat(testDoctorat.getLaboratoirId()).isEqualTo(DEFAULT_LABORATOIR_ID);
    }

    @Test
    @Transactional
    void createDoctoratWithExistingId() throws Exception {
        // Create the Doctorat with an existing ID
        doctorat.setId(1L);
        DoctoratDTO doctoratDTO = doctoratMapper.toDto(doctorat);

        int databaseSizeBeforeCreate = doctoratRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(doctoratSearchRepository.findAll());

        // An entity with an existing ID cannot be created, so this API call must fail
        restDoctoratMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(doctoratDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Doctorat in the database
        List<Doctorat> doctoratList = doctoratRepository.findAll();
        assertThat(doctoratList).hasSize(databaseSizeBeforeCreate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(doctoratSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void checkSujetIsRequired() throws Exception {
        int databaseSizeBeforeTest = doctoratRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(doctoratSearchRepository.findAll());
        // set the field null
        doctorat.setSujet(null);

        // Create the Doctorat, which fails.
        DoctoratDTO doctoratDTO = doctoratMapper.toDto(doctorat);

        restDoctoratMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(doctoratDTO))
            )
            .andExpect(status().isBadRequest());

        List<Doctorat> doctoratList = doctoratRepository.findAll();
        assertThat(doctoratList).hasSize(databaseSizeBeforeTest);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(doctoratSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void getAllDoctorats() throws Exception {
        // Initialize the database
        doctoratRepository.saveAndFlush(doctorat);

        // Get all the doctoratList
        restDoctoratMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(doctorat.getId().intValue())))
            .andExpect(jsonPath("$.[*].sujet").value(hasItem(DEFAULT_SUJET)))
            .andExpect(jsonPath("$.[*].anneeInscriptionDoctorat").value(hasItem(DEFAULT_ANNEE_INSCRIPTION_DOCTORAT.toString())))
            .andExpect(jsonPath("$.[*].encadreurId").value(hasItem(DEFAULT_ENCADREUR_ID.intValue())))
            .andExpect(jsonPath("$.[*].laboratoirId").value(hasItem(DEFAULT_LABORATOIR_ID.intValue())));
    }

    @Test
    @Transactional
    void getDoctorat() throws Exception {
        // Initialize the database
        doctoratRepository.saveAndFlush(doctorat);

        // Get the doctorat
        restDoctoratMockMvc
            .perform(get(ENTITY_API_URL_ID, doctorat.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(doctorat.getId().intValue()))
            .andExpect(jsonPath("$.sujet").value(DEFAULT_SUJET))
            .andExpect(jsonPath("$.anneeInscriptionDoctorat").value(DEFAULT_ANNEE_INSCRIPTION_DOCTORAT.toString()))
            .andExpect(jsonPath("$.encadreurId").value(DEFAULT_ENCADREUR_ID.intValue()))
            .andExpect(jsonPath("$.laboratoirId").value(DEFAULT_LABORATOIR_ID.intValue()));
    }

    @Test
    @Transactional
    void getNonExistingDoctorat() throws Exception {
        // Get the doctorat
        restDoctoratMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingDoctorat() throws Exception {
        // Initialize the database
        doctoratRepository.saveAndFlush(doctorat);

        int databaseSizeBeforeUpdate = doctoratRepository.findAll().size();
        doctoratSearchRepository.save(doctorat);
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(doctoratSearchRepository.findAll());

        // Update the doctorat
        Doctorat updatedDoctorat = doctoratRepository.findById(doctorat.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedDoctorat are not directly saved in db
        em.detach(updatedDoctorat);
        updatedDoctorat
            .sujet(UPDATED_SUJET)
            .anneeInscriptionDoctorat(UPDATED_ANNEE_INSCRIPTION_DOCTORAT)
            .encadreurId(UPDATED_ENCADREUR_ID)
            .laboratoirId(UPDATED_LABORATOIR_ID);
        DoctoratDTO doctoratDTO = doctoratMapper.toDto(updatedDoctorat);

        restDoctoratMockMvc
            .perform(
                put(ENTITY_API_URL_ID, doctoratDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(doctoratDTO))
            )
            .andExpect(status().isOk());

        // Validate the Doctorat in the database
        List<Doctorat> doctoratList = doctoratRepository.findAll();
        assertThat(doctoratList).hasSize(databaseSizeBeforeUpdate);
        Doctorat testDoctorat = doctoratList.get(doctoratList.size() - 1);
        assertThat(testDoctorat.getSujet()).isEqualTo(UPDATED_SUJET);
        assertThat(testDoctorat.getAnneeInscriptionDoctorat()).isEqualTo(UPDATED_ANNEE_INSCRIPTION_DOCTORAT);
        assertThat(testDoctorat.getEncadreurId()).isEqualTo(UPDATED_ENCADREUR_ID);
        assertThat(testDoctorat.getLaboratoirId()).isEqualTo(UPDATED_LABORATOIR_ID);
        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(doctoratSearchRepository.findAll());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
                List<Doctorat> doctoratSearchList = IterableUtils.toList(doctoratSearchRepository.findAll());
                Doctorat testDoctoratSearch = doctoratSearchList.get(searchDatabaseSizeAfter - 1);
                assertThat(testDoctoratSearch.getSujet()).isEqualTo(UPDATED_SUJET);
                assertThat(testDoctoratSearch.getAnneeInscriptionDoctorat()).isEqualTo(UPDATED_ANNEE_INSCRIPTION_DOCTORAT);
                assertThat(testDoctoratSearch.getEncadreurId()).isEqualTo(UPDATED_ENCADREUR_ID);
                assertThat(testDoctoratSearch.getLaboratoirId()).isEqualTo(UPDATED_LABORATOIR_ID);
            });
    }

    @Test
    @Transactional
    void putNonExistingDoctorat() throws Exception {
        int databaseSizeBeforeUpdate = doctoratRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(doctoratSearchRepository.findAll());
        doctorat.setId(longCount.incrementAndGet());

        // Create the Doctorat
        DoctoratDTO doctoratDTO = doctoratMapper.toDto(doctorat);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDoctoratMockMvc
            .perform(
                put(ENTITY_API_URL_ID, doctoratDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(doctoratDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Doctorat in the database
        List<Doctorat> doctoratList = doctoratRepository.findAll();
        assertThat(doctoratList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(doctoratSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void putWithIdMismatchDoctorat() throws Exception {
        int databaseSizeBeforeUpdate = doctoratRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(doctoratSearchRepository.findAll());
        doctorat.setId(longCount.incrementAndGet());

        // Create the Doctorat
        DoctoratDTO doctoratDTO = doctoratMapper.toDto(doctorat);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDoctoratMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(doctoratDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Doctorat in the database
        List<Doctorat> doctoratList = doctoratRepository.findAll();
        assertThat(doctoratList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(doctoratSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamDoctorat() throws Exception {
        int databaseSizeBeforeUpdate = doctoratRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(doctoratSearchRepository.findAll());
        doctorat.setId(longCount.incrementAndGet());

        // Create the Doctorat
        DoctoratDTO doctoratDTO = doctoratMapper.toDto(doctorat);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDoctoratMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(doctoratDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Doctorat in the database
        List<Doctorat> doctoratList = doctoratRepository.findAll();
        assertThat(doctoratList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(doctoratSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void partialUpdateDoctoratWithPatch() throws Exception {
        // Initialize the database
        doctoratRepository.saveAndFlush(doctorat);

        int databaseSizeBeforeUpdate = doctoratRepository.findAll().size();

        // Update the doctorat using partial update
        Doctorat partialUpdatedDoctorat = new Doctorat();
        partialUpdatedDoctorat.setId(doctorat.getId());

        partialUpdatedDoctorat
            .anneeInscriptionDoctorat(UPDATED_ANNEE_INSCRIPTION_DOCTORAT)
            .encadreurId(UPDATED_ENCADREUR_ID)
            .laboratoirId(UPDATED_LABORATOIR_ID);

        restDoctoratMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDoctorat.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedDoctorat))
            )
            .andExpect(status().isOk());

        // Validate the Doctorat in the database
        List<Doctorat> doctoratList = doctoratRepository.findAll();
        assertThat(doctoratList).hasSize(databaseSizeBeforeUpdate);
        Doctorat testDoctorat = doctoratList.get(doctoratList.size() - 1);
        assertThat(testDoctorat.getSujet()).isEqualTo(DEFAULT_SUJET);
        assertThat(testDoctorat.getAnneeInscriptionDoctorat()).isEqualTo(UPDATED_ANNEE_INSCRIPTION_DOCTORAT);
        assertThat(testDoctorat.getEncadreurId()).isEqualTo(UPDATED_ENCADREUR_ID);
        assertThat(testDoctorat.getLaboratoirId()).isEqualTo(UPDATED_LABORATOIR_ID);
    }

    @Test
    @Transactional
    void fullUpdateDoctoratWithPatch() throws Exception {
        // Initialize the database
        doctoratRepository.saveAndFlush(doctorat);

        int databaseSizeBeforeUpdate = doctoratRepository.findAll().size();

        // Update the doctorat using partial update
        Doctorat partialUpdatedDoctorat = new Doctorat();
        partialUpdatedDoctorat.setId(doctorat.getId());

        partialUpdatedDoctorat
            .sujet(UPDATED_SUJET)
            .anneeInscriptionDoctorat(UPDATED_ANNEE_INSCRIPTION_DOCTORAT)
            .encadreurId(UPDATED_ENCADREUR_ID)
            .laboratoirId(UPDATED_LABORATOIR_ID);

        restDoctoratMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDoctorat.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedDoctorat))
            )
            .andExpect(status().isOk());

        // Validate the Doctorat in the database
        List<Doctorat> doctoratList = doctoratRepository.findAll();
        assertThat(doctoratList).hasSize(databaseSizeBeforeUpdate);
        Doctorat testDoctorat = doctoratList.get(doctoratList.size() - 1);
        assertThat(testDoctorat.getSujet()).isEqualTo(UPDATED_SUJET);
        assertThat(testDoctorat.getAnneeInscriptionDoctorat()).isEqualTo(UPDATED_ANNEE_INSCRIPTION_DOCTORAT);
        assertThat(testDoctorat.getEncadreurId()).isEqualTo(UPDATED_ENCADREUR_ID);
        assertThat(testDoctorat.getLaboratoirId()).isEqualTo(UPDATED_LABORATOIR_ID);
    }

    @Test
    @Transactional
    void patchNonExistingDoctorat() throws Exception {
        int databaseSizeBeforeUpdate = doctoratRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(doctoratSearchRepository.findAll());
        doctorat.setId(longCount.incrementAndGet());

        // Create the Doctorat
        DoctoratDTO doctoratDTO = doctoratMapper.toDto(doctorat);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDoctoratMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, doctoratDTO.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(doctoratDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Doctorat in the database
        List<Doctorat> doctoratList = doctoratRepository.findAll();
        assertThat(doctoratList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(doctoratSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void patchWithIdMismatchDoctorat() throws Exception {
        int databaseSizeBeforeUpdate = doctoratRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(doctoratSearchRepository.findAll());
        doctorat.setId(longCount.incrementAndGet());

        // Create the Doctorat
        DoctoratDTO doctoratDTO = doctoratMapper.toDto(doctorat);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDoctoratMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(doctoratDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Doctorat in the database
        List<Doctorat> doctoratList = doctoratRepository.findAll();
        assertThat(doctoratList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(doctoratSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamDoctorat() throws Exception {
        int databaseSizeBeforeUpdate = doctoratRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(doctoratSearchRepository.findAll());
        doctorat.setId(longCount.incrementAndGet());

        // Create the Doctorat
        DoctoratDTO doctoratDTO = doctoratMapper.toDto(doctorat);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDoctoratMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(doctoratDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Doctorat in the database
        List<Doctorat> doctoratList = doctoratRepository.findAll();
        assertThat(doctoratList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(doctoratSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void deleteDoctorat() throws Exception {
        // Initialize the database
        doctoratRepository.saveAndFlush(doctorat);
        doctoratRepository.save(doctorat);
        doctoratSearchRepository.save(doctorat);

        int databaseSizeBeforeDelete = doctoratRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(doctoratSearchRepository.findAll());
        assertThat(searchDatabaseSizeBefore).isEqualTo(databaseSizeBeforeDelete);

        // Delete the doctorat
        restDoctoratMockMvc
            .perform(delete(ENTITY_API_URL_ID, doctorat.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Doctorat> doctoratList = doctoratRepository.findAll();
        assertThat(doctoratList).hasSize(databaseSizeBeforeDelete - 1);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(doctoratSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore - 1);
    }

    @Test
    @Transactional
    void searchDoctorat() throws Exception {
        // Initialize the database
        doctorat = doctoratRepository.saveAndFlush(doctorat);
        doctoratSearchRepository.save(doctorat);

        // Search the doctorat
        restDoctoratMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + doctorat.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(doctorat.getId().intValue())))
            .andExpect(jsonPath("$.[*].sujet").value(hasItem(DEFAULT_SUJET)))
            .andExpect(jsonPath("$.[*].anneeInscriptionDoctorat").value(hasItem(DEFAULT_ANNEE_INSCRIPTION_DOCTORAT.toString())))
            .andExpect(jsonPath("$.[*].encadreurId").value(hasItem(DEFAULT_ENCADREUR_ID.intValue())))
            .andExpect(jsonPath("$.[*].laboratoirId").value(hasItem(DEFAULT_LABORATOIR_ID.intValue())));
    }
}
