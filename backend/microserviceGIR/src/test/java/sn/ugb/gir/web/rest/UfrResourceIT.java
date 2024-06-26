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
import sn.ugb.gir.domain.Ufr;
import sn.ugb.gir.repository.UfrRepository;
import sn.ugb.gir.repository.search.UfrSearchRepository;
import sn.ugb.gir.service.dto.UfrDTO;
import sn.ugb.gir.service.mapper.UfrMapper;

/**
 * Integration tests for the {@link UfrResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class UfrResourceIT {

    private static final String DEFAULT_LIBELLE_UFR = "AAAAAAAAAA";
    private static final String UPDATED_LIBELLE_UFR = "BBBBBBBBBB";

    private static final String DEFAULT_SIGLE_UFR = "AAAAAAAAAA";
    private static final String UPDATED_SIGLE_UFR = "BBBBBBBBBB";

    private static final String DEFAULT_PREFIXE = "AAAAAAAAAA";
    private static final String UPDATED_PREFIXE = "BBBBBBBBBB";

    private static final Boolean DEFAULT_ACTIF_YN = false;
    private static final Boolean UPDATED_ACTIF_YN = true;

    private static final String ENTITY_API_URL = "/api/ufrs";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/ufrs/_search";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private UfrRepository ufrRepository;

    @Autowired
    private UfrMapper ufrMapper;

    @Autowired
    private UfrSearchRepository ufrSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restUfrMockMvc;

    private Ufr ufr;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Ufr createEntity(EntityManager em) {
        Ufr ufr = new Ufr().libelleUfr(DEFAULT_LIBELLE_UFR).sigleUfr(DEFAULT_SIGLE_UFR).prefixe(DEFAULT_PREFIXE).actifYN(DEFAULT_ACTIF_YN);
        return ufr;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Ufr createUpdatedEntity(EntityManager em) {
        Ufr ufr = new Ufr().libelleUfr(UPDATED_LIBELLE_UFR).sigleUfr(UPDATED_SIGLE_UFR).prefixe(UPDATED_PREFIXE).actifYN(UPDATED_ACTIF_YN);
        return ufr;
    }

    @AfterEach
    public void cleanupElasticSearchRepository() {
        ufrSearchRepository.deleteAll();
        assertThat(ufrSearchRepository.count()).isEqualTo(0);
    }

    @BeforeEach
    public void initTest() {
        ufr = createEntity(em);
    }

    @Test
    @Transactional
    void createUfr() throws Exception {
        int databaseSizeBeforeCreate = ufrRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(ufrSearchRepository.findAll());
        // Create the Ufr
        UfrDTO ufrDTO = ufrMapper.toDto(ufr);
        restUfrMockMvc
            .perform(
                post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(ufrDTO))
            )
            .andExpect(status().isCreated());

        // Validate the Ufr in the database
        List<Ufr> ufrList = ufrRepository.findAll();
        assertThat(ufrList).hasSize(databaseSizeBeforeCreate + 1);
        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(ufrSearchRepository.findAll());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore + 1);
            });
        Ufr testUfr = ufrList.get(ufrList.size() - 1);
        assertThat(testUfr.getLibelleUfr()).isEqualTo(DEFAULT_LIBELLE_UFR);
        assertThat(testUfr.getSigleUfr()).isEqualTo(DEFAULT_SIGLE_UFR);
        assertThat(testUfr.getPrefixe()).isEqualTo(DEFAULT_PREFIXE);
        assertThat(testUfr.getActifYN()).isEqualTo(DEFAULT_ACTIF_YN);
    }

    @Test
    @Transactional
    void createUfrWithExistingId() throws Exception {
        // Create the Ufr with an existing ID
        ufr.setId(1L);
        UfrDTO ufrDTO = ufrMapper.toDto(ufr);

        int databaseSizeBeforeCreate = ufrRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(ufrSearchRepository.findAll());

        // An entity with an existing ID cannot be created, so this API call must fail
        restUfrMockMvc
            .perform(
                post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(ufrDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Ufr in the database
        List<Ufr> ufrList = ufrRepository.findAll();
        assertThat(ufrList).hasSize(databaseSizeBeforeCreate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(ufrSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void checkLibelleUfrIsRequired() throws Exception {
        int databaseSizeBeforeTest = ufrRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(ufrSearchRepository.findAll());
        // set the field null
        ufr.setLibelleUfr(null);

        // Create the Ufr, which fails.
        UfrDTO ufrDTO = ufrMapper.toDto(ufr);

        restUfrMockMvc
            .perform(
                post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(ufrDTO))
            )
            .andExpect(status().isBadRequest());

        List<Ufr> ufrList = ufrRepository.findAll();
        assertThat(ufrList).hasSize(databaseSizeBeforeTest);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(ufrSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void checkSigleUfrIsRequired() throws Exception {
        int databaseSizeBeforeTest = ufrRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(ufrSearchRepository.findAll());
        // set the field null
        ufr.setSigleUfr(null);

        // Create the Ufr, which fails.
        UfrDTO ufrDTO = ufrMapper.toDto(ufr);

        restUfrMockMvc
            .perform(
                post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(ufrDTO))
            )
            .andExpect(status().isBadRequest());

        List<Ufr> ufrList = ufrRepository.findAll();
        assertThat(ufrList).hasSize(databaseSizeBeforeTest);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(ufrSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void getAllUfrs() throws Exception {
        // Initialize the database
        ufrRepository.saveAndFlush(ufr);

        // Get all the ufrList
        restUfrMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(ufr.getId().intValue())))
            .andExpect(jsonPath("$.[*].libelleUfr").value(hasItem(DEFAULT_LIBELLE_UFR)))
            .andExpect(jsonPath("$.[*].sigleUfr").value(hasItem(DEFAULT_SIGLE_UFR)))
            .andExpect(jsonPath("$.[*].prefixe").value(hasItem(DEFAULT_PREFIXE)))
            .andExpect(jsonPath("$.[*].actifYN").value(hasItem(DEFAULT_ACTIF_YN.booleanValue())));
    }

    @Test
    @Transactional
    void getUfr() throws Exception {
        // Initialize the database
        ufrRepository.saveAndFlush(ufr);

        // Get the ufr
        restUfrMockMvc
            .perform(get(ENTITY_API_URL_ID, ufr.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(ufr.getId().intValue()))
            .andExpect(jsonPath("$.libelleUfr").value(DEFAULT_LIBELLE_UFR))
            .andExpect(jsonPath("$.sigleUfr").value(DEFAULT_SIGLE_UFR))
            .andExpect(jsonPath("$.prefixe").value(DEFAULT_PREFIXE))
            .andExpect(jsonPath("$.actifYN").value(DEFAULT_ACTIF_YN.booleanValue()));
    }

    @Test
    @Transactional
    void getNonExistingUfr() throws Exception {
        // Get the ufr
        restUfrMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingUfr() throws Exception {
        // Initialize the database
        ufrRepository.saveAndFlush(ufr);

        int databaseSizeBeforeUpdate = ufrRepository.findAll().size();
        ufrSearchRepository.save(ufr);
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(ufrSearchRepository.findAll());

        // Update the ufr
        Ufr updatedUfr = ufrRepository.findById(ufr.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedUfr are not directly saved in db
        em.detach(updatedUfr);
        updatedUfr.libelleUfr(UPDATED_LIBELLE_UFR).sigleUfr(UPDATED_SIGLE_UFR).prefixe(UPDATED_PREFIXE).actifYN(UPDATED_ACTIF_YN);
        UfrDTO ufrDTO = ufrMapper.toDto(updatedUfr);

        restUfrMockMvc
            .perform(
                put(ENTITY_API_URL_ID, ufrDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(ufrDTO))
            )
            .andExpect(status().isOk());

        // Validate the Ufr in the database
        List<Ufr> ufrList = ufrRepository.findAll();
        assertThat(ufrList).hasSize(databaseSizeBeforeUpdate);
        Ufr testUfr = ufrList.get(ufrList.size() - 1);
        assertThat(testUfr.getLibelleUfr()).isEqualTo(UPDATED_LIBELLE_UFR);
        assertThat(testUfr.getSigleUfr()).isEqualTo(UPDATED_SIGLE_UFR);
        assertThat(testUfr.getPrefixe()).isEqualTo(UPDATED_PREFIXE);
        assertThat(testUfr.getActifYN()).isEqualTo(UPDATED_ACTIF_YN);
        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(ufrSearchRepository.findAll());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
                List<Ufr> ufrSearchList = IterableUtils.toList(ufrSearchRepository.findAll());
                Ufr testUfrSearch = ufrSearchList.get(searchDatabaseSizeAfter - 1);
                assertThat(testUfrSearch.getLibelleUfr()).isEqualTo(UPDATED_LIBELLE_UFR);
                assertThat(testUfrSearch.getSigleUfr()).isEqualTo(UPDATED_SIGLE_UFR);
                assertThat(testUfrSearch.getPrefixe()).isEqualTo(UPDATED_PREFIXE);
                assertThat(testUfrSearch.getActifYN()).isEqualTo(UPDATED_ACTIF_YN);
            });
    }

    @Test
    @Transactional
    void putNonExistingUfr() throws Exception {
        int databaseSizeBeforeUpdate = ufrRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(ufrSearchRepository.findAll());
        ufr.setId(longCount.incrementAndGet());

        // Create the Ufr
        UfrDTO ufrDTO = ufrMapper.toDto(ufr);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restUfrMockMvc
            .perform(
                put(ENTITY_API_URL_ID, ufrDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(ufrDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Ufr in the database
        List<Ufr> ufrList = ufrRepository.findAll();
        assertThat(ufrList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(ufrSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void putWithIdMismatchUfr() throws Exception {
        int databaseSizeBeforeUpdate = ufrRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(ufrSearchRepository.findAll());
        ufr.setId(longCount.incrementAndGet());

        // Create the Ufr
        UfrDTO ufrDTO = ufrMapper.toDto(ufr);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUfrMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(ufrDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Ufr in the database
        List<Ufr> ufrList = ufrRepository.findAll();
        assertThat(ufrList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(ufrSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamUfr() throws Exception {
        int databaseSizeBeforeUpdate = ufrRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(ufrSearchRepository.findAll());
        ufr.setId(longCount.incrementAndGet());

        // Create the Ufr
        UfrDTO ufrDTO = ufrMapper.toDto(ufr);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUfrMockMvc
            .perform(
                put(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(ufrDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Ufr in the database
        List<Ufr> ufrList = ufrRepository.findAll();
        assertThat(ufrList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(ufrSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void partialUpdateUfrWithPatch() throws Exception {
        // Initialize the database
        ufrRepository.saveAndFlush(ufr);

        int databaseSizeBeforeUpdate = ufrRepository.findAll().size();

        // Update the ufr using partial update
        Ufr partialUpdatedUfr = new Ufr();
        partialUpdatedUfr.setId(ufr.getId());

        partialUpdatedUfr.prefixe(UPDATED_PREFIXE).actifYN(UPDATED_ACTIF_YN);

        restUfrMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedUfr.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedUfr))
            )
            .andExpect(status().isOk());

        // Validate the Ufr in the database
        List<Ufr> ufrList = ufrRepository.findAll();
        assertThat(ufrList).hasSize(databaseSizeBeforeUpdate);
        Ufr testUfr = ufrList.get(ufrList.size() - 1);
        assertThat(testUfr.getLibelleUfr()).isEqualTo(DEFAULT_LIBELLE_UFR);
        assertThat(testUfr.getSigleUfr()).isEqualTo(DEFAULT_SIGLE_UFR);
        assertThat(testUfr.getPrefixe()).isEqualTo(UPDATED_PREFIXE);
        assertThat(testUfr.getActifYN()).isEqualTo(UPDATED_ACTIF_YN);
    }

    @Test
    @Transactional
    void fullUpdateUfrWithPatch() throws Exception {
        // Initialize the database
        ufrRepository.saveAndFlush(ufr);

        int databaseSizeBeforeUpdate = ufrRepository.findAll().size();

        // Update the ufr using partial update
        Ufr partialUpdatedUfr = new Ufr();
        partialUpdatedUfr.setId(ufr.getId());

        partialUpdatedUfr.libelleUfr(UPDATED_LIBELLE_UFR).sigleUfr(UPDATED_SIGLE_UFR).prefixe(UPDATED_PREFIXE).actifYN(UPDATED_ACTIF_YN);

        restUfrMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedUfr.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedUfr))
            )
            .andExpect(status().isOk());

        // Validate the Ufr in the database
        List<Ufr> ufrList = ufrRepository.findAll();
        assertThat(ufrList).hasSize(databaseSizeBeforeUpdate);
        Ufr testUfr = ufrList.get(ufrList.size() - 1);
        assertThat(testUfr.getLibelleUfr()).isEqualTo(UPDATED_LIBELLE_UFR);
        assertThat(testUfr.getSigleUfr()).isEqualTo(UPDATED_SIGLE_UFR);
        assertThat(testUfr.getPrefixe()).isEqualTo(UPDATED_PREFIXE);
        assertThat(testUfr.getActifYN()).isEqualTo(UPDATED_ACTIF_YN);
    }

    @Test
    @Transactional
    void patchNonExistingUfr() throws Exception {
        int databaseSizeBeforeUpdate = ufrRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(ufrSearchRepository.findAll());
        ufr.setId(longCount.incrementAndGet());

        // Create the Ufr
        UfrDTO ufrDTO = ufrMapper.toDto(ufr);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restUfrMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, ufrDTO.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(ufrDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Ufr in the database
        List<Ufr> ufrList = ufrRepository.findAll();
        assertThat(ufrList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(ufrSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void patchWithIdMismatchUfr() throws Exception {
        int databaseSizeBeforeUpdate = ufrRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(ufrSearchRepository.findAll());
        ufr.setId(longCount.incrementAndGet());

        // Create the Ufr
        UfrDTO ufrDTO = ufrMapper.toDto(ufr);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUfrMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(ufrDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Ufr in the database
        List<Ufr> ufrList = ufrRepository.findAll();
        assertThat(ufrList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(ufrSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamUfr() throws Exception {
        int databaseSizeBeforeUpdate = ufrRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(ufrSearchRepository.findAll());
        ufr.setId(longCount.incrementAndGet());

        // Create the Ufr
        UfrDTO ufrDTO = ufrMapper.toDto(ufr);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUfrMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(ufrDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Ufr in the database
        List<Ufr> ufrList = ufrRepository.findAll();
        assertThat(ufrList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(ufrSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void deleteUfr() throws Exception {
        // Initialize the database
        ufrRepository.saveAndFlush(ufr);
        ufrRepository.save(ufr);
        ufrSearchRepository.save(ufr);

        int databaseSizeBeforeDelete = ufrRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(ufrSearchRepository.findAll());
        assertThat(searchDatabaseSizeBefore).isEqualTo(databaseSizeBeforeDelete);

        // Delete the ufr
        restUfrMockMvc
            .perform(delete(ENTITY_API_URL_ID, ufr.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Ufr> ufrList = ufrRepository.findAll();
        assertThat(ufrList).hasSize(databaseSizeBeforeDelete - 1);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(ufrSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore - 1);
    }

    @Test
    @Transactional
    void searchUfr() throws Exception {
        // Initialize the database
        ufr = ufrRepository.saveAndFlush(ufr);
        ufrSearchRepository.save(ufr);

        // Search the ufr
        restUfrMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + ufr.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(ufr.getId().intValue())))
            .andExpect(jsonPath("$.[*].libelleUfr").value(hasItem(DEFAULT_LIBELLE_UFR)))
            .andExpect(jsonPath("$.[*].sigleUfr").value(hasItem(DEFAULT_SIGLE_UFR)))
            .andExpect(jsonPath("$.[*].prefixe").value(hasItem(DEFAULT_PREFIXE)))
            .andExpect(jsonPath("$.[*].actifYN").value(hasItem(DEFAULT_ACTIF_YN.booleanValue())));
    }
}
