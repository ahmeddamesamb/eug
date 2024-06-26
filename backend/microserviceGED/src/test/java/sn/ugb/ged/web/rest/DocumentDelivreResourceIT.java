package sn.ugb.ged.web.rest;

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
import sn.ugb.ged.IntegrationTest;
import sn.ugb.ged.domain.DocumentDelivre;
import sn.ugb.ged.repository.DocumentDelivreRepository;
import sn.ugb.ged.repository.search.DocumentDelivreSearchRepository;
import sn.ugb.ged.service.dto.DocumentDelivreDTO;
import sn.ugb.ged.service.mapper.DocumentDelivreMapper;

/**
 * Integration tests for the {@link DocumentDelivreResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class DocumentDelivreResourceIT {

    private static final String DEFAULT_LIBELLE_DOC = "AAAAAAAAAA";
    private static final String UPDATED_LIBELLE_DOC = "BBBBBBBBBB";

    private static final Instant DEFAULT_ANNEE_DOC = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_ANNEE_DOC = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_DATE_ENREGISTREMENT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_DATE_ENREGISTREMENT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/document-delivres";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/document-delivres/_search";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private DocumentDelivreRepository documentDelivreRepository;

    @Autowired
    private DocumentDelivreMapper documentDelivreMapper;

    @Autowired
    private DocumentDelivreSearchRepository documentDelivreSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restDocumentDelivreMockMvc;

    private DocumentDelivre documentDelivre;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static DocumentDelivre createEntity(EntityManager em) {
        DocumentDelivre documentDelivre = new DocumentDelivre()
            .libelleDoc(DEFAULT_LIBELLE_DOC)
            .anneeDoc(DEFAULT_ANNEE_DOC)
            .dateEnregistrement(DEFAULT_DATE_ENREGISTREMENT);
        return documentDelivre;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static DocumentDelivre createUpdatedEntity(EntityManager em) {
        DocumentDelivre documentDelivre = new DocumentDelivre()
            .libelleDoc(UPDATED_LIBELLE_DOC)
            .anneeDoc(UPDATED_ANNEE_DOC)
            .dateEnregistrement(UPDATED_DATE_ENREGISTREMENT);
        return documentDelivre;
    }

    @AfterEach
    public void cleanupElasticSearchRepository() {
        documentDelivreSearchRepository.deleteAll();
        assertThat(documentDelivreSearchRepository.count()).isEqualTo(0);
    }

    @BeforeEach
    public void initTest() {
        documentDelivre = createEntity(em);
    }

    @Test
    @Transactional
    void createDocumentDelivre() throws Exception {
        int databaseSizeBeforeCreate = documentDelivreRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(documentDelivreSearchRepository.findAll());
        // Create the DocumentDelivre
        DocumentDelivreDTO documentDelivreDTO = documentDelivreMapper.toDto(documentDelivre);
        restDocumentDelivreMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(documentDelivreDTO))
            )
            .andExpect(status().isCreated());

        // Validate the DocumentDelivre in the database
        List<DocumentDelivre> documentDelivreList = documentDelivreRepository.findAll();
        assertThat(documentDelivreList).hasSize(databaseSizeBeforeCreate + 1);
        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(documentDelivreSearchRepository.findAll());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore + 1);
            });
        DocumentDelivre testDocumentDelivre = documentDelivreList.get(documentDelivreList.size() - 1);
        assertThat(testDocumentDelivre.getLibelleDoc()).isEqualTo(DEFAULT_LIBELLE_DOC);
        assertThat(testDocumentDelivre.getAnneeDoc()).isEqualTo(DEFAULT_ANNEE_DOC);
        assertThat(testDocumentDelivre.getDateEnregistrement()).isEqualTo(DEFAULT_DATE_ENREGISTREMENT);
    }

    @Test
    @Transactional
    void createDocumentDelivreWithExistingId() throws Exception {
        // Create the DocumentDelivre with an existing ID
        documentDelivre.setId(1L);
        DocumentDelivreDTO documentDelivreDTO = documentDelivreMapper.toDto(documentDelivre);

        int databaseSizeBeforeCreate = documentDelivreRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(documentDelivreSearchRepository.findAll());

        // An entity with an existing ID cannot be created, so this API call must fail
        restDocumentDelivreMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(documentDelivreDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the DocumentDelivre in the database
        List<DocumentDelivre> documentDelivreList = documentDelivreRepository.findAll();
        assertThat(documentDelivreList).hasSize(databaseSizeBeforeCreate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(documentDelivreSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void getAllDocumentDelivres() throws Exception {
        // Initialize the database
        documentDelivreRepository.saveAndFlush(documentDelivre);

        // Get all the documentDelivreList
        restDocumentDelivreMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(documentDelivre.getId().intValue())))
            .andExpect(jsonPath("$.[*].libelleDoc").value(hasItem(DEFAULT_LIBELLE_DOC)))
            .andExpect(jsonPath("$.[*].anneeDoc").value(hasItem(DEFAULT_ANNEE_DOC.toString())))
            .andExpect(jsonPath("$.[*].dateEnregistrement").value(hasItem(DEFAULT_DATE_ENREGISTREMENT.toString())));
    }

    @Test
    @Transactional
    void getDocumentDelivre() throws Exception {
        // Initialize the database
        documentDelivreRepository.saveAndFlush(documentDelivre);

        // Get the documentDelivre
        restDocumentDelivreMockMvc
            .perform(get(ENTITY_API_URL_ID, documentDelivre.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(documentDelivre.getId().intValue()))
            .andExpect(jsonPath("$.libelleDoc").value(DEFAULT_LIBELLE_DOC))
            .andExpect(jsonPath("$.anneeDoc").value(DEFAULT_ANNEE_DOC.toString()))
            .andExpect(jsonPath("$.dateEnregistrement").value(DEFAULT_DATE_ENREGISTREMENT.toString()));
    }

    @Test
    @Transactional
    void getNonExistingDocumentDelivre() throws Exception {
        // Get the documentDelivre
        restDocumentDelivreMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingDocumentDelivre() throws Exception {
        // Initialize the database
        documentDelivreRepository.saveAndFlush(documentDelivre);

        int databaseSizeBeforeUpdate = documentDelivreRepository.findAll().size();
        documentDelivreSearchRepository.save(documentDelivre);
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(documentDelivreSearchRepository.findAll());

        // Update the documentDelivre
        DocumentDelivre updatedDocumentDelivre = documentDelivreRepository.findById(documentDelivre.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedDocumentDelivre are not directly saved in db
        em.detach(updatedDocumentDelivre);
        updatedDocumentDelivre.libelleDoc(UPDATED_LIBELLE_DOC).anneeDoc(UPDATED_ANNEE_DOC).dateEnregistrement(UPDATED_DATE_ENREGISTREMENT);
        DocumentDelivreDTO documentDelivreDTO = documentDelivreMapper.toDto(updatedDocumentDelivre);

        restDocumentDelivreMockMvc
            .perform(
                put(ENTITY_API_URL_ID, documentDelivreDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(documentDelivreDTO))
            )
            .andExpect(status().isOk());

        // Validate the DocumentDelivre in the database
        List<DocumentDelivre> documentDelivreList = documentDelivreRepository.findAll();
        assertThat(documentDelivreList).hasSize(databaseSizeBeforeUpdate);
        DocumentDelivre testDocumentDelivre = documentDelivreList.get(documentDelivreList.size() - 1);
        assertThat(testDocumentDelivre.getLibelleDoc()).isEqualTo(UPDATED_LIBELLE_DOC);
        assertThat(testDocumentDelivre.getAnneeDoc()).isEqualTo(UPDATED_ANNEE_DOC);
        assertThat(testDocumentDelivre.getDateEnregistrement()).isEqualTo(UPDATED_DATE_ENREGISTREMENT);
        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(documentDelivreSearchRepository.findAll());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
                List<DocumentDelivre> documentDelivreSearchList = IterableUtils.toList(documentDelivreSearchRepository.findAll());
                DocumentDelivre testDocumentDelivreSearch = documentDelivreSearchList.get(searchDatabaseSizeAfter - 1);
                assertThat(testDocumentDelivreSearch.getLibelleDoc()).isEqualTo(UPDATED_LIBELLE_DOC);
                assertThat(testDocumentDelivreSearch.getAnneeDoc()).isEqualTo(UPDATED_ANNEE_DOC);
                assertThat(testDocumentDelivreSearch.getDateEnregistrement()).isEqualTo(UPDATED_DATE_ENREGISTREMENT);
            });
    }

    @Test
    @Transactional
    void putNonExistingDocumentDelivre() throws Exception {
        int databaseSizeBeforeUpdate = documentDelivreRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(documentDelivreSearchRepository.findAll());
        documentDelivre.setId(longCount.incrementAndGet());

        // Create the DocumentDelivre
        DocumentDelivreDTO documentDelivreDTO = documentDelivreMapper.toDto(documentDelivre);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDocumentDelivreMockMvc
            .perform(
                put(ENTITY_API_URL_ID, documentDelivreDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(documentDelivreDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the DocumentDelivre in the database
        List<DocumentDelivre> documentDelivreList = documentDelivreRepository.findAll();
        assertThat(documentDelivreList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(documentDelivreSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void putWithIdMismatchDocumentDelivre() throws Exception {
        int databaseSizeBeforeUpdate = documentDelivreRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(documentDelivreSearchRepository.findAll());
        documentDelivre.setId(longCount.incrementAndGet());

        // Create the DocumentDelivre
        DocumentDelivreDTO documentDelivreDTO = documentDelivreMapper.toDto(documentDelivre);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDocumentDelivreMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(documentDelivreDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the DocumentDelivre in the database
        List<DocumentDelivre> documentDelivreList = documentDelivreRepository.findAll();
        assertThat(documentDelivreList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(documentDelivreSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamDocumentDelivre() throws Exception {
        int databaseSizeBeforeUpdate = documentDelivreRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(documentDelivreSearchRepository.findAll());
        documentDelivre.setId(longCount.incrementAndGet());

        // Create the DocumentDelivre
        DocumentDelivreDTO documentDelivreDTO = documentDelivreMapper.toDto(documentDelivre);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDocumentDelivreMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(documentDelivreDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the DocumentDelivre in the database
        List<DocumentDelivre> documentDelivreList = documentDelivreRepository.findAll();
        assertThat(documentDelivreList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(documentDelivreSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void partialUpdateDocumentDelivreWithPatch() throws Exception {
        // Initialize the database
        documentDelivreRepository.saveAndFlush(documentDelivre);

        int databaseSizeBeforeUpdate = documentDelivreRepository.findAll().size();

        // Update the documentDelivre using partial update
        DocumentDelivre partialUpdatedDocumentDelivre = new DocumentDelivre();
        partialUpdatedDocumentDelivre.setId(documentDelivre.getId());

        partialUpdatedDocumentDelivre.anneeDoc(UPDATED_ANNEE_DOC);

        restDocumentDelivreMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDocumentDelivre.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedDocumentDelivre))
            )
            .andExpect(status().isOk());

        // Validate the DocumentDelivre in the database
        List<DocumentDelivre> documentDelivreList = documentDelivreRepository.findAll();
        assertThat(documentDelivreList).hasSize(databaseSizeBeforeUpdate);
        DocumentDelivre testDocumentDelivre = documentDelivreList.get(documentDelivreList.size() - 1);
        assertThat(testDocumentDelivre.getLibelleDoc()).isEqualTo(DEFAULT_LIBELLE_DOC);
        assertThat(testDocumentDelivre.getAnneeDoc()).isEqualTo(UPDATED_ANNEE_DOC);
        assertThat(testDocumentDelivre.getDateEnregistrement()).isEqualTo(DEFAULT_DATE_ENREGISTREMENT);
    }

    @Test
    @Transactional
    void fullUpdateDocumentDelivreWithPatch() throws Exception {
        // Initialize the database
        documentDelivreRepository.saveAndFlush(documentDelivre);

        int databaseSizeBeforeUpdate = documentDelivreRepository.findAll().size();

        // Update the documentDelivre using partial update
        DocumentDelivre partialUpdatedDocumentDelivre = new DocumentDelivre();
        partialUpdatedDocumentDelivre.setId(documentDelivre.getId());

        partialUpdatedDocumentDelivre
            .libelleDoc(UPDATED_LIBELLE_DOC)
            .anneeDoc(UPDATED_ANNEE_DOC)
            .dateEnregistrement(UPDATED_DATE_ENREGISTREMENT);

        restDocumentDelivreMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDocumentDelivre.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedDocumentDelivre))
            )
            .andExpect(status().isOk());

        // Validate the DocumentDelivre in the database
        List<DocumentDelivre> documentDelivreList = documentDelivreRepository.findAll();
        assertThat(documentDelivreList).hasSize(databaseSizeBeforeUpdate);
        DocumentDelivre testDocumentDelivre = documentDelivreList.get(documentDelivreList.size() - 1);
        assertThat(testDocumentDelivre.getLibelleDoc()).isEqualTo(UPDATED_LIBELLE_DOC);
        assertThat(testDocumentDelivre.getAnneeDoc()).isEqualTo(UPDATED_ANNEE_DOC);
        assertThat(testDocumentDelivre.getDateEnregistrement()).isEqualTo(UPDATED_DATE_ENREGISTREMENT);
    }

    @Test
    @Transactional
    void patchNonExistingDocumentDelivre() throws Exception {
        int databaseSizeBeforeUpdate = documentDelivreRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(documentDelivreSearchRepository.findAll());
        documentDelivre.setId(longCount.incrementAndGet());

        // Create the DocumentDelivre
        DocumentDelivreDTO documentDelivreDTO = documentDelivreMapper.toDto(documentDelivre);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDocumentDelivreMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, documentDelivreDTO.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(documentDelivreDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the DocumentDelivre in the database
        List<DocumentDelivre> documentDelivreList = documentDelivreRepository.findAll();
        assertThat(documentDelivreList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(documentDelivreSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void patchWithIdMismatchDocumentDelivre() throws Exception {
        int databaseSizeBeforeUpdate = documentDelivreRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(documentDelivreSearchRepository.findAll());
        documentDelivre.setId(longCount.incrementAndGet());

        // Create the DocumentDelivre
        DocumentDelivreDTO documentDelivreDTO = documentDelivreMapper.toDto(documentDelivre);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDocumentDelivreMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(documentDelivreDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the DocumentDelivre in the database
        List<DocumentDelivre> documentDelivreList = documentDelivreRepository.findAll();
        assertThat(documentDelivreList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(documentDelivreSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamDocumentDelivre() throws Exception {
        int databaseSizeBeforeUpdate = documentDelivreRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(documentDelivreSearchRepository.findAll());
        documentDelivre.setId(longCount.incrementAndGet());

        // Create the DocumentDelivre
        DocumentDelivreDTO documentDelivreDTO = documentDelivreMapper.toDto(documentDelivre);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDocumentDelivreMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(documentDelivreDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the DocumentDelivre in the database
        List<DocumentDelivre> documentDelivreList = documentDelivreRepository.findAll();
        assertThat(documentDelivreList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(documentDelivreSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void deleteDocumentDelivre() throws Exception {
        // Initialize the database
        documentDelivreRepository.saveAndFlush(documentDelivre);
        documentDelivreRepository.save(documentDelivre);
        documentDelivreSearchRepository.save(documentDelivre);

        int databaseSizeBeforeDelete = documentDelivreRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(documentDelivreSearchRepository.findAll());
        assertThat(searchDatabaseSizeBefore).isEqualTo(databaseSizeBeforeDelete);

        // Delete the documentDelivre
        restDocumentDelivreMockMvc
            .perform(delete(ENTITY_API_URL_ID, documentDelivre.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<DocumentDelivre> documentDelivreList = documentDelivreRepository.findAll();
        assertThat(documentDelivreList).hasSize(databaseSizeBeforeDelete - 1);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(documentDelivreSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore - 1);
    }

    @Test
    @Transactional
    void searchDocumentDelivre() throws Exception {
        // Initialize the database
        documentDelivre = documentDelivreRepository.saveAndFlush(documentDelivre);
        documentDelivreSearchRepository.save(documentDelivre);

        // Search the documentDelivre
        restDocumentDelivreMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + documentDelivre.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(documentDelivre.getId().intValue())))
            .andExpect(jsonPath("$.[*].libelleDoc").value(hasItem(DEFAULT_LIBELLE_DOC)))
            .andExpect(jsonPath("$.[*].anneeDoc").value(hasItem(DEFAULT_ANNEE_DOC.toString())))
            .andExpect(jsonPath("$.[*].dateEnregistrement").value(hasItem(DEFAULT_DATE_ENREGISTREMENT.toString())));
    }
}
