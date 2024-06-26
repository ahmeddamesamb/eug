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
import sn.ugb.gir.domain.InscriptionAdministrative;
import sn.ugb.gir.repository.InscriptionAdministrativeRepository;
import sn.ugb.gir.repository.search.InscriptionAdministrativeSearchRepository;
import sn.ugb.gir.service.dto.InscriptionAdministrativeDTO;
import sn.ugb.gir.service.mapper.InscriptionAdministrativeMapper;

/**
 * Integration tests for the {@link InscriptionAdministrativeResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class InscriptionAdministrativeResourceIT {

    private static final Boolean DEFAULT_NOUVEAU_INSCRIT_YN = false;
    private static final Boolean UPDATED_NOUVEAU_INSCRIT_YN = true;

    private static final Boolean DEFAULT_REPRISE_YN = false;
    private static final Boolean UPDATED_REPRISE_YN = true;

    private static final Boolean DEFAULT_AUTORISE_YN = false;
    private static final Boolean UPDATED_AUTORISE_YN = true;

    private static final Integer DEFAULT_ORDRE_INSCRIPTION = 1;
    private static final Integer UPDATED_ORDRE_INSCRIPTION = 2;

    private static final String ENTITY_API_URL = "/api/inscription-administratives";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/inscription-administratives/_search";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private InscriptionAdministrativeRepository inscriptionAdministrativeRepository;

    @Autowired
    private InscriptionAdministrativeMapper inscriptionAdministrativeMapper;

    @Autowired
    private InscriptionAdministrativeSearchRepository inscriptionAdministrativeSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restInscriptionAdministrativeMockMvc;

    private InscriptionAdministrative inscriptionAdministrative;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static InscriptionAdministrative createEntity(EntityManager em) {
        InscriptionAdministrative inscriptionAdministrative = new InscriptionAdministrative()
            .nouveauInscritYN(DEFAULT_NOUVEAU_INSCRIT_YN)
            .repriseYN(DEFAULT_REPRISE_YN)
            .autoriseYN(DEFAULT_AUTORISE_YN)
            .ordreInscription(DEFAULT_ORDRE_INSCRIPTION);
        return inscriptionAdministrative;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static InscriptionAdministrative createUpdatedEntity(EntityManager em) {
        InscriptionAdministrative inscriptionAdministrative = new InscriptionAdministrative()
            .nouveauInscritYN(UPDATED_NOUVEAU_INSCRIT_YN)
            .repriseYN(UPDATED_REPRISE_YN)
            .autoriseYN(UPDATED_AUTORISE_YN)
            .ordreInscription(UPDATED_ORDRE_INSCRIPTION);
        return inscriptionAdministrative;
    }

    @AfterEach
    public void cleanupElasticSearchRepository() {
        inscriptionAdministrativeSearchRepository.deleteAll();
        assertThat(inscriptionAdministrativeSearchRepository.count()).isEqualTo(0);
    }

    @BeforeEach
    public void initTest() {
        inscriptionAdministrative = createEntity(em);
    }

    @Test
    @Transactional
    void createInscriptionAdministrative() throws Exception {
        int databaseSizeBeforeCreate = inscriptionAdministrativeRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(inscriptionAdministrativeSearchRepository.findAll());
        // Create the InscriptionAdministrative
        InscriptionAdministrativeDTO inscriptionAdministrativeDTO = inscriptionAdministrativeMapper.toDto(inscriptionAdministrative);
        restInscriptionAdministrativeMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(inscriptionAdministrativeDTO))
            )
            .andExpect(status().isCreated());

        // Validate the InscriptionAdministrative in the database
        List<InscriptionAdministrative> inscriptionAdministrativeList = inscriptionAdministrativeRepository.findAll();
        assertThat(inscriptionAdministrativeList).hasSize(databaseSizeBeforeCreate + 1);
        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(inscriptionAdministrativeSearchRepository.findAll());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore + 1);
            });
        InscriptionAdministrative testInscriptionAdministrative = inscriptionAdministrativeList.get(
            inscriptionAdministrativeList.size() - 1
        );
        assertThat(testInscriptionAdministrative.getNouveauInscritYN()).isEqualTo(DEFAULT_NOUVEAU_INSCRIT_YN);
        assertThat(testInscriptionAdministrative.getRepriseYN()).isEqualTo(DEFAULT_REPRISE_YN);
        assertThat(testInscriptionAdministrative.getAutoriseYN()).isEqualTo(DEFAULT_AUTORISE_YN);
        assertThat(testInscriptionAdministrative.getOrdreInscription()).isEqualTo(DEFAULT_ORDRE_INSCRIPTION);
    }

    @Test
    @Transactional
    void createInscriptionAdministrativeWithExistingId() throws Exception {
        // Create the InscriptionAdministrative with an existing ID
        inscriptionAdministrative.setId(1L);
        InscriptionAdministrativeDTO inscriptionAdministrativeDTO = inscriptionAdministrativeMapper.toDto(inscriptionAdministrative);

        int databaseSizeBeforeCreate = inscriptionAdministrativeRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(inscriptionAdministrativeSearchRepository.findAll());

        // An entity with an existing ID cannot be created, so this API call must fail
        restInscriptionAdministrativeMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(inscriptionAdministrativeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the InscriptionAdministrative in the database
        List<InscriptionAdministrative> inscriptionAdministrativeList = inscriptionAdministrativeRepository.findAll();
        assertThat(inscriptionAdministrativeList).hasSize(databaseSizeBeforeCreate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(inscriptionAdministrativeSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void getAllInscriptionAdministratives() throws Exception {
        // Initialize the database
        inscriptionAdministrativeRepository.saveAndFlush(inscriptionAdministrative);

        // Get all the inscriptionAdministrativeList
        restInscriptionAdministrativeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(inscriptionAdministrative.getId().intValue())))
            .andExpect(jsonPath("$.[*].nouveauInscritYN").value(hasItem(DEFAULT_NOUVEAU_INSCRIT_YN.booleanValue())))
            .andExpect(jsonPath("$.[*].repriseYN").value(hasItem(DEFAULT_REPRISE_YN.booleanValue())))
            .andExpect(jsonPath("$.[*].autoriseYN").value(hasItem(DEFAULT_AUTORISE_YN.booleanValue())))
            .andExpect(jsonPath("$.[*].ordreInscription").value(hasItem(DEFAULT_ORDRE_INSCRIPTION)));
    }

    @Test
    @Transactional
    void getInscriptionAdministrative() throws Exception {
        // Initialize the database
        inscriptionAdministrativeRepository.saveAndFlush(inscriptionAdministrative);

        // Get the inscriptionAdministrative
        restInscriptionAdministrativeMockMvc
            .perform(get(ENTITY_API_URL_ID, inscriptionAdministrative.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(inscriptionAdministrative.getId().intValue()))
            .andExpect(jsonPath("$.nouveauInscritYN").value(DEFAULT_NOUVEAU_INSCRIT_YN.booleanValue()))
            .andExpect(jsonPath("$.repriseYN").value(DEFAULT_REPRISE_YN.booleanValue()))
            .andExpect(jsonPath("$.autoriseYN").value(DEFAULT_AUTORISE_YN.booleanValue()))
            .andExpect(jsonPath("$.ordreInscription").value(DEFAULT_ORDRE_INSCRIPTION));
    }

    @Test
    @Transactional
    void getNonExistingInscriptionAdministrative() throws Exception {
        // Get the inscriptionAdministrative
        restInscriptionAdministrativeMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingInscriptionAdministrative() throws Exception {
        // Initialize the database
        inscriptionAdministrativeRepository.saveAndFlush(inscriptionAdministrative);

        int databaseSizeBeforeUpdate = inscriptionAdministrativeRepository.findAll().size();
        inscriptionAdministrativeSearchRepository.save(inscriptionAdministrative);
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(inscriptionAdministrativeSearchRepository.findAll());

        // Update the inscriptionAdministrative
        InscriptionAdministrative updatedInscriptionAdministrative = inscriptionAdministrativeRepository
            .findById(inscriptionAdministrative.getId())
            .orElseThrow();
        // Disconnect from session so that the updates on updatedInscriptionAdministrative are not directly saved in db
        em.detach(updatedInscriptionAdministrative);
        updatedInscriptionAdministrative
            .nouveauInscritYN(UPDATED_NOUVEAU_INSCRIT_YN)
            .repriseYN(UPDATED_REPRISE_YN)
            .autoriseYN(UPDATED_AUTORISE_YN)
            .ordreInscription(UPDATED_ORDRE_INSCRIPTION);
        InscriptionAdministrativeDTO inscriptionAdministrativeDTO = inscriptionAdministrativeMapper.toDto(updatedInscriptionAdministrative);

        restInscriptionAdministrativeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, inscriptionAdministrativeDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(inscriptionAdministrativeDTO))
            )
            .andExpect(status().isOk());

        // Validate the InscriptionAdministrative in the database
        List<InscriptionAdministrative> inscriptionAdministrativeList = inscriptionAdministrativeRepository.findAll();
        assertThat(inscriptionAdministrativeList).hasSize(databaseSizeBeforeUpdate);
        InscriptionAdministrative testInscriptionAdministrative = inscriptionAdministrativeList.get(
            inscriptionAdministrativeList.size() - 1
        );
        assertThat(testInscriptionAdministrative.getNouveauInscritYN()).isEqualTo(UPDATED_NOUVEAU_INSCRIT_YN);
        assertThat(testInscriptionAdministrative.getRepriseYN()).isEqualTo(UPDATED_REPRISE_YN);
        assertThat(testInscriptionAdministrative.getAutoriseYN()).isEqualTo(UPDATED_AUTORISE_YN);
        assertThat(testInscriptionAdministrative.getOrdreInscription()).isEqualTo(UPDATED_ORDRE_INSCRIPTION);
        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(inscriptionAdministrativeSearchRepository.findAll());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
                List<InscriptionAdministrative> inscriptionAdministrativeSearchList = IterableUtils.toList(
                    inscriptionAdministrativeSearchRepository.findAll()
                );
                InscriptionAdministrative testInscriptionAdministrativeSearch = inscriptionAdministrativeSearchList.get(
                    searchDatabaseSizeAfter - 1
                );
                assertThat(testInscriptionAdministrativeSearch.getNouveauInscritYN()).isEqualTo(UPDATED_NOUVEAU_INSCRIT_YN);
                assertThat(testInscriptionAdministrativeSearch.getRepriseYN()).isEqualTo(UPDATED_REPRISE_YN);
                assertThat(testInscriptionAdministrativeSearch.getAutoriseYN()).isEqualTo(UPDATED_AUTORISE_YN);
                assertThat(testInscriptionAdministrativeSearch.getOrdreInscription()).isEqualTo(UPDATED_ORDRE_INSCRIPTION);
            });
    }

    @Test
    @Transactional
    void putNonExistingInscriptionAdministrative() throws Exception {
        int databaseSizeBeforeUpdate = inscriptionAdministrativeRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(inscriptionAdministrativeSearchRepository.findAll());
        inscriptionAdministrative.setId(longCount.incrementAndGet());

        // Create the InscriptionAdministrative
        InscriptionAdministrativeDTO inscriptionAdministrativeDTO = inscriptionAdministrativeMapper.toDto(inscriptionAdministrative);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restInscriptionAdministrativeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, inscriptionAdministrativeDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(inscriptionAdministrativeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the InscriptionAdministrative in the database
        List<InscriptionAdministrative> inscriptionAdministrativeList = inscriptionAdministrativeRepository.findAll();
        assertThat(inscriptionAdministrativeList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(inscriptionAdministrativeSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void putWithIdMismatchInscriptionAdministrative() throws Exception {
        int databaseSizeBeforeUpdate = inscriptionAdministrativeRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(inscriptionAdministrativeSearchRepository.findAll());
        inscriptionAdministrative.setId(longCount.incrementAndGet());

        // Create the InscriptionAdministrative
        InscriptionAdministrativeDTO inscriptionAdministrativeDTO = inscriptionAdministrativeMapper.toDto(inscriptionAdministrative);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restInscriptionAdministrativeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(inscriptionAdministrativeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the InscriptionAdministrative in the database
        List<InscriptionAdministrative> inscriptionAdministrativeList = inscriptionAdministrativeRepository.findAll();
        assertThat(inscriptionAdministrativeList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(inscriptionAdministrativeSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamInscriptionAdministrative() throws Exception {
        int databaseSizeBeforeUpdate = inscriptionAdministrativeRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(inscriptionAdministrativeSearchRepository.findAll());
        inscriptionAdministrative.setId(longCount.incrementAndGet());

        // Create the InscriptionAdministrative
        InscriptionAdministrativeDTO inscriptionAdministrativeDTO = inscriptionAdministrativeMapper.toDto(inscriptionAdministrative);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restInscriptionAdministrativeMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(inscriptionAdministrativeDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the InscriptionAdministrative in the database
        List<InscriptionAdministrative> inscriptionAdministrativeList = inscriptionAdministrativeRepository.findAll();
        assertThat(inscriptionAdministrativeList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(inscriptionAdministrativeSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void partialUpdateInscriptionAdministrativeWithPatch() throws Exception {
        // Initialize the database
        inscriptionAdministrativeRepository.saveAndFlush(inscriptionAdministrative);

        int databaseSizeBeforeUpdate = inscriptionAdministrativeRepository.findAll().size();

        // Update the inscriptionAdministrative using partial update
        InscriptionAdministrative partialUpdatedInscriptionAdministrative = new InscriptionAdministrative();
        partialUpdatedInscriptionAdministrative.setId(inscriptionAdministrative.getId());

        partialUpdatedInscriptionAdministrative.nouveauInscritYN(UPDATED_NOUVEAU_INSCRIT_YN).autoriseYN(UPDATED_AUTORISE_YN);

        restInscriptionAdministrativeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedInscriptionAdministrative.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedInscriptionAdministrative))
            )
            .andExpect(status().isOk());

        // Validate the InscriptionAdministrative in the database
        List<InscriptionAdministrative> inscriptionAdministrativeList = inscriptionAdministrativeRepository.findAll();
        assertThat(inscriptionAdministrativeList).hasSize(databaseSizeBeforeUpdate);
        InscriptionAdministrative testInscriptionAdministrative = inscriptionAdministrativeList.get(
            inscriptionAdministrativeList.size() - 1
        );
        assertThat(testInscriptionAdministrative.getNouveauInscritYN()).isEqualTo(UPDATED_NOUVEAU_INSCRIT_YN);
        assertThat(testInscriptionAdministrative.getRepriseYN()).isEqualTo(DEFAULT_REPRISE_YN);
        assertThat(testInscriptionAdministrative.getAutoriseYN()).isEqualTo(UPDATED_AUTORISE_YN);
        assertThat(testInscriptionAdministrative.getOrdreInscription()).isEqualTo(DEFAULT_ORDRE_INSCRIPTION);
    }

    @Test
    @Transactional
    void fullUpdateInscriptionAdministrativeWithPatch() throws Exception {
        // Initialize the database
        inscriptionAdministrativeRepository.saveAndFlush(inscriptionAdministrative);

        int databaseSizeBeforeUpdate = inscriptionAdministrativeRepository.findAll().size();

        // Update the inscriptionAdministrative using partial update
        InscriptionAdministrative partialUpdatedInscriptionAdministrative = new InscriptionAdministrative();
        partialUpdatedInscriptionAdministrative.setId(inscriptionAdministrative.getId());

        partialUpdatedInscriptionAdministrative
            .nouveauInscritYN(UPDATED_NOUVEAU_INSCRIT_YN)
            .repriseYN(UPDATED_REPRISE_YN)
            .autoriseYN(UPDATED_AUTORISE_YN)
            .ordreInscription(UPDATED_ORDRE_INSCRIPTION);

        restInscriptionAdministrativeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedInscriptionAdministrative.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedInscriptionAdministrative))
            )
            .andExpect(status().isOk());

        // Validate the InscriptionAdministrative in the database
        List<InscriptionAdministrative> inscriptionAdministrativeList = inscriptionAdministrativeRepository.findAll();
        assertThat(inscriptionAdministrativeList).hasSize(databaseSizeBeforeUpdate);
        InscriptionAdministrative testInscriptionAdministrative = inscriptionAdministrativeList.get(
            inscriptionAdministrativeList.size() - 1
        );
        assertThat(testInscriptionAdministrative.getNouveauInscritYN()).isEqualTo(UPDATED_NOUVEAU_INSCRIT_YN);
        assertThat(testInscriptionAdministrative.getRepriseYN()).isEqualTo(UPDATED_REPRISE_YN);
        assertThat(testInscriptionAdministrative.getAutoriseYN()).isEqualTo(UPDATED_AUTORISE_YN);
        assertThat(testInscriptionAdministrative.getOrdreInscription()).isEqualTo(UPDATED_ORDRE_INSCRIPTION);
    }

    @Test
    @Transactional
    void patchNonExistingInscriptionAdministrative() throws Exception {
        int databaseSizeBeforeUpdate = inscriptionAdministrativeRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(inscriptionAdministrativeSearchRepository.findAll());
        inscriptionAdministrative.setId(longCount.incrementAndGet());

        // Create the InscriptionAdministrative
        InscriptionAdministrativeDTO inscriptionAdministrativeDTO = inscriptionAdministrativeMapper.toDto(inscriptionAdministrative);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restInscriptionAdministrativeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, inscriptionAdministrativeDTO.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(inscriptionAdministrativeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the InscriptionAdministrative in the database
        List<InscriptionAdministrative> inscriptionAdministrativeList = inscriptionAdministrativeRepository.findAll();
        assertThat(inscriptionAdministrativeList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(inscriptionAdministrativeSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void patchWithIdMismatchInscriptionAdministrative() throws Exception {
        int databaseSizeBeforeUpdate = inscriptionAdministrativeRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(inscriptionAdministrativeSearchRepository.findAll());
        inscriptionAdministrative.setId(longCount.incrementAndGet());

        // Create the InscriptionAdministrative
        InscriptionAdministrativeDTO inscriptionAdministrativeDTO = inscriptionAdministrativeMapper.toDto(inscriptionAdministrative);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restInscriptionAdministrativeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(inscriptionAdministrativeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the InscriptionAdministrative in the database
        List<InscriptionAdministrative> inscriptionAdministrativeList = inscriptionAdministrativeRepository.findAll();
        assertThat(inscriptionAdministrativeList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(inscriptionAdministrativeSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamInscriptionAdministrative() throws Exception {
        int databaseSizeBeforeUpdate = inscriptionAdministrativeRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(inscriptionAdministrativeSearchRepository.findAll());
        inscriptionAdministrative.setId(longCount.incrementAndGet());

        // Create the InscriptionAdministrative
        InscriptionAdministrativeDTO inscriptionAdministrativeDTO = inscriptionAdministrativeMapper.toDto(inscriptionAdministrative);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restInscriptionAdministrativeMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(inscriptionAdministrativeDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the InscriptionAdministrative in the database
        List<InscriptionAdministrative> inscriptionAdministrativeList = inscriptionAdministrativeRepository.findAll();
        assertThat(inscriptionAdministrativeList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(inscriptionAdministrativeSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void deleteInscriptionAdministrative() throws Exception {
        // Initialize the database
        inscriptionAdministrativeRepository.saveAndFlush(inscriptionAdministrative);
        inscriptionAdministrativeRepository.save(inscriptionAdministrative);
        inscriptionAdministrativeSearchRepository.save(inscriptionAdministrative);

        int databaseSizeBeforeDelete = inscriptionAdministrativeRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(inscriptionAdministrativeSearchRepository.findAll());
        assertThat(searchDatabaseSizeBefore).isEqualTo(databaseSizeBeforeDelete);

        // Delete the inscriptionAdministrative
        restInscriptionAdministrativeMockMvc
            .perform(delete(ENTITY_API_URL_ID, inscriptionAdministrative.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<InscriptionAdministrative> inscriptionAdministrativeList = inscriptionAdministrativeRepository.findAll();
        assertThat(inscriptionAdministrativeList).hasSize(databaseSizeBeforeDelete - 1);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(inscriptionAdministrativeSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore - 1);
    }

    @Test
    @Transactional
    void searchInscriptionAdministrative() throws Exception {
        // Initialize the database
        inscriptionAdministrative = inscriptionAdministrativeRepository.saveAndFlush(inscriptionAdministrative);
        inscriptionAdministrativeSearchRepository.save(inscriptionAdministrative);

        // Search the inscriptionAdministrative
        restInscriptionAdministrativeMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + inscriptionAdministrative.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(inscriptionAdministrative.getId().intValue())))
            .andExpect(jsonPath("$.[*].nouveauInscritYN").value(hasItem(DEFAULT_NOUVEAU_INSCRIT_YN.booleanValue())))
            .andExpect(jsonPath("$.[*].repriseYN").value(hasItem(DEFAULT_REPRISE_YN.booleanValue())))
            .andExpect(jsonPath("$.[*].autoriseYN").value(hasItem(DEFAULT_AUTORISE_YN.booleanValue())))
            .andExpect(jsonPath("$.[*].ordreInscription").value(hasItem(DEFAULT_ORDRE_INSCRIPTION)));
    }
}
