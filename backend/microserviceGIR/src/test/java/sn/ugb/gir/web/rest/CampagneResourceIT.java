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
import sn.ugb.gir.domain.Campagne;
import sn.ugb.gir.repository.CampagneRepository;
import sn.ugb.gir.repository.search.CampagneSearchRepository;
import sn.ugb.gir.service.dto.CampagneDTO;
import sn.ugb.gir.service.mapper.CampagneMapper;

/**
 * Integration tests for the {@link CampagneResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class CampagneResourceIT {

    private static final String DEFAULT_LIBELLE_CAMPAGNE = "AAAAAAAAAA";
    private static final String UPDATED_LIBELLE_CAMPAGNE = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_DATE_DEBUT = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE_DEBUT = LocalDate.now(ZoneId.systemDefault());

    private static final LocalDate DEFAULT_DATE_FIN = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE_FIN = LocalDate.now(ZoneId.systemDefault());

    private static final String DEFAULT_LIBELLE_ABREGE = "AAAAAAAAAA";
    private static final String UPDATED_LIBELLE_ABREGE = "BBBBBBBBBB";

    private static final Boolean DEFAULT_ACTIF_YN = false;
    private static final Boolean UPDATED_ACTIF_YN = true;

    private static final String ENTITY_API_URL = "/api/campagnes";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/campagnes/_search";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private CampagneRepository campagneRepository;

    @Autowired
    private CampagneMapper campagneMapper;

    @Autowired
    private CampagneSearchRepository campagneSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restCampagneMockMvc;

    private Campagne campagne;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Campagne createEntity(EntityManager em) {
        Campagne campagne = new Campagne()
            .libelleCampagne(DEFAULT_LIBELLE_CAMPAGNE)
            .dateDebut(DEFAULT_DATE_DEBUT)
            .dateFin(DEFAULT_DATE_FIN)
            .libelleAbrege(DEFAULT_LIBELLE_ABREGE)
            .actifYN(DEFAULT_ACTIF_YN);
        return campagne;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Campagne createUpdatedEntity(EntityManager em) {
        Campagne campagne = new Campagne()
            .libelleCampagne(UPDATED_LIBELLE_CAMPAGNE)
            .dateDebut(UPDATED_DATE_DEBUT)
            .dateFin(UPDATED_DATE_FIN)
            .libelleAbrege(UPDATED_LIBELLE_ABREGE)
            .actifYN(UPDATED_ACTIF_YN);
        return campagne;
    }

    @AfterEach
    public void cleanupElasticSearchRepository() {
        campagneSearchRepository.deleteAll();
        assertThat(campagneSearchRepository.count()).isEqualTo(0);
    }

    @BeforeEach
    public void initTest() {
        campagne = createEntity(em);
    }

    @Test
    @Transactional
    void createCampagne() throws Exception {
        int databaseSizeBeforeCreate = campagneRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(campagneSearchRepository.findAll());
        // Create the Campagne
        CampagneDTO campagneDTO = campagneMapper.toDto(campagne);
        restCampagneMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(campagneDTO))
            )
            .andExpect(status().isCreated());

        // Validate the Campagne in the database
        List<Campagne> campagneList = campagneRepository.findAll();
        assertThat(campagneList).hasSize(databaseSizeBeforeCreate + 1);
        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(campagneSearchRepository.findAll());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore + 1);
            });
        Campagne testCampagne = campagneList.get(campagneList.size() - 1);
        assertThat(testCampagne.getLibelleCampagne()).isEqualTo(DEFAULT_LIBELLE_CAMPAGNE);
        assertThat(testCampagne.getDateDebut()).isEqualTo(DEFAULT_DATE_DEBUT);
        assertThat(testCampagne.getDateFin()).isEqualTo(DEFAULT_DATE_FIN);
        assertThat(testCampagne.getLibelleAbrege()).isEqualTo(DEFAULT_LIBELLE_ABREGE);
        assertThat(testCampagne.getActifYN()).isEqualTo(DEFAULT_ACTIF_YN);
    }

    @Test
    @Transactional
    void createCampagneWithExistingId() throws Exception {
        // Create the Campagne with an existing ID
        campagne.setId(1L);
        CampagneDTO campagneDTO = campagneMapper.toDto(campagne);

        int databaseSizeBeforeCreate = campagneRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(campagneSearchRepository.findAll());

        // An entity with an existing ID cannot be created, so this API call must fail
        restCampagneMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(campagneDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Campagne in the database
        List<Campagne> campagneList = campagneRepository.findAll();
        assertThat(campagneList).hasSize(databaseSizeBeforeCreate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(campagneSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void getAllCampagnes() throws Exception {
        // Initialize the database
        campagneRepository.saveAndFlush(campagne);

        // Get all the campagneList
        restCampagneMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(campagne.getId().intValue())))
            .andExpect(jsonPath("$.[*].libelleCampagne").value(hasItem(DEFAULT_LIBELLE_CAMPAGNE)))
            .andExpect(jsonPath("$.[*].dateDebut").value(hasItem(DEFAULT_DATE_DEBUT.toString())))
            .andExpect(jsonPath("$.[*].dateFin").value(hasItem(DEFAULT_DATE_FIN.toString())))
            .andExpect(jsonPath("$.[*].libelleAbrege").value(hasItem(DEFAULT_LIBELLE_ABREGE)))
            .andExpect(jsonPath("$.[*].actifYN").value(hasItem(DEFAULT_ACTIF_YN.booleanValue())));
    }

    @Test
    @Transactional
    void getCampagne() throws Exception {
        // Initialize the database
        campagneRepository.saveAndFlush(campagne);

        // Get the campagne
        restCampagneMockMvc
            .perform(get(ENTITY_API_URL_ID, campagne.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(campagne.getId().intValue()))
            .andExpect(jsonPath("$.libelleCampagne").value(DEFAULT_LIBELLE_CAMPAGNE))
            .andExpect(jsonPath("$.dateDebut").value(DEFAULT_DATE_DEBUT.toString()))
            .andExpect(jsonPath("$.dateFin").value(DEFAULT_DATE_FIN.toString()))
            .andExpect(jsonPath("$.libelleAbrege").value(DEFAULT_LIBELLE_ABREGE))
            .andExpect(jsonPath("$.actifYN").value(DEFAULT_ACTIF_YN.booleanValue()));
    }

    @Test
    @Transactional
    void getNonExistingCampagne() throws Exception {
        // Get the campagne
        restCampagneMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingCampagne() throws Exception {
        // Initialize the database
        campagneRepository.saveAndFlush(campagne);

        int databaseSizeBeforeUpdate = campagneRepository.findAll().size();
        campagneSearchRepository.save(campagne);
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(campagneSearchRepository.findAll());

        // Update the campagne
        Campagne updatedCampagne = campagneRepository.findById(campagne.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedCampagne are not directly saved in db
        em.detach(updatedCampagne);
        updatedCampagne
            .libelleCampagne(UPDATED_LIBELLE_CAMPAGNE)
            .dateDebut(UPDATED_DATE_DEBUT)
            .dateFin(UPDATED_DATE_FIN)
            .libelleAbrege(UPDATED_LIBELLE_ABREGE)
            .actifYN(UPDATED_ACTIF_YN);
        CampagneDTO campagneDTO = campagneMapper.toDto(updatedCampagne);

        restCampagneMockMvc
            .perform(
                put(ENTITY_API_URL_ID, campagneDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(campagneDTO))
            )
            .andExpect(status().isOk());

        // Validate the Campagne in the database
        List<Campagne> campagneList = campagneRepository.findAll();
        assertThat(campagneList).hasSize(databaseSizeBeforeUpdate);
        Campagne testCampagne = campagneList.get(campagneList.size() - 1);
        assertThat(testCampagne.getLibelleCampagne()).isEqualTo(UPDATED_LIBELLE_CAMPAGNE);
        assertThat(testCampagne.getDateDebut()).isEqualTo(UPDATED_DATE_DEBUT);
        assertThat(testCampagne.getDateFin()).isEqualTo(UPDATED_DATE_FIN);
        assertThat(testCampagne.getLibelleAbrege()).isEqualTo(UPDATED_LIBELLE_ABREGE);
        assertThat(testCampagne.getActifYN()).isEqualTo(UPDATED_ACTIF_YN);
        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(campagneSearchRepository.findAll());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
                List<Campagne> campagneSearchList = IterableUtils.toList(campagneSearchRepository.findAll());
                Campagne testCampagneSearch = campagneSearchList.get(searchDatabaseSizeAfter - 1);
                assertThat(testCampagneSearch.getLibelleCampagne()).isEqualTo(UPDATED_LIBELLE_CAMPAGNE);
                assertThat(testCampagneSearch.getDateDebut()).isEqualTo(UPDATED_DATE_DEBUT);
                assertThat(testCampagneSearch.getDateFin()).isEqualTo(UPDATED_DATE_FIN);
                assertThat(testCampagneSearch.getLibelleAbrege()).isEqualTo(UPDATED_LIBELLE_ABREGE);
                assertThat(testCampagneSearch.getActifYN()).isEqualTo(UPDATED_ACTIF_YN);
            });
    }

    @Test
    @Transactional
    void putNonExistingCampagne() throws Exception {
        int databaseSizeBeforeUpdate = campagneRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(campagneSearchRepository.findAll());
        campagne.setId(longCount.incrementAndGet());

        // Create the Campagne
        CampagneDTO campagneDTO = campagneMapper.toDto(campagne);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCampagneMockMvc
            .perform(
                put(ENTITY_API_URL_ID, campagneDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(campagneDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Campagne in the database
        List<Campagne> campagneList = campagneRepository.findAll();
        assertThat(campagneList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(campagneSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void putWithIdMismatchCampagne() throws Exception {
        int databaseSizeBeforeUpdate = campagneRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(campagneSearchRepository.findAll());
        campagne.setId(longCount.incrementAndGet());

        // Create the Campagne
        CampagneDTO campagneDTO = campagneMapper.toDto(campagne);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCampagneMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(campagneDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Campagne in the database
        List<Campagne> campagneList = campagneRepository.findAll();
        assertThat(campagneList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(campagneSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamCampagne() throws Exception {
        int databaseSizeBeforeUpdate = campagneRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(campagneSearchRepository.findAll());
        campagne.setId(longCount.incrementAndGet());

        // Create the Campagne
        CampagneDTO campagneDTO = campagneMapper.toDto(campagne);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCampagneMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(campagneDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Campagne in the database
        List<Campagne> campagneList = campagneRepository.findAll();
        assertThat(campagneList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(campagneSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void partialUpdateCampagneWithPatch() throws Exception {
        // Initialize the database
        campagneRepository.saveAndFlush(campagne);

        int databaseSizeBeforeUpdate = campagneRepository.findAll().size();

        // Update the campagne using partial update
        Campagne partialUpdatedCampagne = new Campagne();
        partialUpdatedCampagne.setId(campagne.getId());

        restCampagneMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCampagne.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCampagne))
            )
            .andExpect(status().isOk());

        // Validate the Campagne in the database
        List<Campagne> campagneList = campagneRepository.findAll();
        assertThat(campagneList).hasSize(databaseSizeBeforeUpdate);
        Campagne testCampagne = campagneList.get(campagneList.size() - 1);
        assertThat(testCampagne.getLibelleCampagne()).isEqualTo(DEFAULT_LIBELLE_CAMPAGNE);
        assertThat(testCampagne.getDateDebut()).isEqualTo(DEFAULT_DATE_DEBUT);
        assertThat(testCampagne.getDateFin()).isEqualTo(DEFAULT_DATE_FIN);
        assertThat(testCampagne.getLibelleAbrege()).isEqualTo(DEFAULT_LIBELLE_ABREGE);
        assertThat(testCampagne.getActifYN()).isEqualTo(DEFAULT_ACTIF_YN);
    }

    @Test
    @Transactional
    void fullUpdateCampagneWithPatch() throws Exception {
        // Initialize the database
        campagneRepository.saveAndFlush(campagne);

        int databaseSizeBeforeUpdate = campagneRepository.findAll().size();

        // Update the campagne using partial update
        Campagne partialUpdatedCampagne = new Campagne();
        partialUpdatedCampagne.setId(campagne.getId());

        partialUpdatedCampagne
            .libelleCampagne(UPDATED_LIBELLE_CAMPAGNE)
            .dateDebut(UPDATED_DATE_DEBUT)
            .dateFin(UPDATED_DATE_FIN)
            .libelleAbrege(UPDATED_LIBELLE_ABREGE)
            .actifYN(UPDATED_ACTIF_YN);

        restCampagneMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCampagne.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCampagne))
            )
            .andExpect(status().isOk());

        // Validate the Campagne in the database
        List<Campagne> campagneList = campagneRepository.findAll();
        assertThat(campagneList).hasSize(databaseSizeBeforeUpdate);
        Campagne testCampagne = campagneList.get(campagneList.size() - 1);
        assertThat(testCampagne.getLibelleCampagne()).isEqualTo(UPDATED_LIBELLE_CAMPAGNE);
        assertThat(testCampagne.getDateDebut()).isEqualTo(UPDATED_DATE_DEBUT);
        assertThat(testCampagne.getDateFin()).isEqualTo(UPDATED_DATE_FIN);
        assertThat(testCampagne.getLibelleAbrege()).isEqualTo(UPDATED_LIBELLE_ABREGE);
        assertThat(testCampagne.getActifYN()).isEqualTo(UPDATED_ACTIF_YN);
    }

    @Test
    @Transactional
    void patchNonExistingCampagne() throws Exception {
        int databaseSizeBeforeUpdate = campagneRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(campagneSearchRepository.findAll());
        campagne.setId(longCount.incrementAndGet());

        // Create the Campagne
        CampagneDTO campagneDTO = campagneMapper.toDto(campagne);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCampagneMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, campagneDTO.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(campagneDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Campagne in the database
        List<Campagne> campagneList = campagneRepository.findAll();
        assertThat(campagneList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(campagneSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void patchWithIdMismatchCampagne() throws Exception {
        int databaseSizeBeforeUpdate = campagneRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(campagneSearchRepository.findAll());
        campagne.setId(longCount.incrementAndGet());

        // Create the Campagne
        CampagneDTO campagneDTO = campagneMapper.toDto(campagne);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCampagneMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(campagneDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Campagne in the database
        List<Campagne> campagneList = campagneRepository.findAll();
        assertThat(campagneList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(campagneSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamCampagne() throws Exception {
        int databaseSizeBeforeUpdate = campagneRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(campagneSearchRepository.findAll());
        campagne.setId(longCount.incrementAndGet());

        // Create the Campagne
        CampagneDTO campagneDTO = campagneMapper.toDto(campagne);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCampagneMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(campagneDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Campagne in the database
        List<Campagne> campagneList = campagneRepository.findAll();
        assertThat(campagneList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(campagneSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void deleteCampagne() throws Exception {
        // Initialize the database
        campagneRepository.saveAndFlush(campagne);
        campagneRepository.save(campagne);
        campagneSearchRepository.save(campagne);

        int databaseSizeBeforeDelete = campagneRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(campagneSearchRepository.findAll());
        assertThat(searchDatabaseSizeBefore).isEqualTo(databaseSizeBeforeDelete);

        // Delete the campagne
        restCampagneMockMvc
            .perform(delete(ENTITY_API_URL_ID, campagne.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Campagne> campagneList = campagneRepository.findAll();
        assertThat(campagneList).hasSize(databaseSizeBeforeDelete - 1);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(campagneSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore - 1);
    }

    @Test
    @Transactional
    void searchCampagne() throws Exception {
        // Initialize the database
        campagne = campagneRepository.saveAndFlush(campagne);
        campagneSearchRepository.save(campagne);

        // Search the campagne
        restCampagneMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + campagne.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(campagne.getId().intValue())))
            .andExpect(jsonPath("$.[*].libelleCampagne").value(hasItem(DEFAULT_LIBELLE_CAMPAGNE)))
            .andExpect(jsonPath("$.[*].dateDebut").value(hasItem(DEFAULT_DATE_DEBUT.toString())))
            .andExpect(jsonPath("$.[*].dateFin").value(hasItem(DEFAULT_DATE_FIN.toString())))
            .andExpect(jsonPath("$.[*].libelleAbrege").value(hasItem(DEFAULT_LIBELLE_ABREGE)))
            .andExpect(jsonPath("$.[*].actifYN").value(hasItem(DEFAULT_ACTIF_YN.booleanValue())));
    }
}
