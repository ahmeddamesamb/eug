package sn.ugb.gir.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import jakarta.persistence.EntityManager;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
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
import sn.ugb.gir.service.dto.CampagneDTO;
import sn.ugb.gir.service.mapper.CampagneMapper;

/**
 * Integration tests for the {@link CampagneResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class CampagneResourceIT {

    private static final String DEFAULT_LIBELLE = "AAAAAAAAAA";
    private static final String UPDATED_LIBELLE = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_DATE_DEBUT = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE_DEBUT = LocalDate.now(ZoneId.systemDefault());

    private static final LocalDate DEFAULT_DATE_FIN = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE_FIN = LocalDate.now(ZoneId.systemDefault());

    private static final String DEFAULT_LIBELLE_ABREGE = "AAAAAAAAAA";
    private static final String UPDATED_LIBELLE_ABREGE = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/campagnes";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private CampagneRepository campagneRepository;

    @Autowired
    private CampagneMapper campagneMapper;

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
            .libelle(DEFAULT_LIBELLE)
            .dateDebut(DEFAULT_DATE_DEBUT)
            .dateFin(DEFAULT_DATE_FIN)
            .libelleAbrege(DEFAULT_LIBELLE_ABREGE);
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
            .libelle(UPDATED_LIBELLE)
            .dateDebut(UPDATED_DATE_DEBUT)
            .dateFin(UPDATED_DATE_FIN)
            .libelleAbrege(UPDATED_LIBELLE_ABREGE);
        return campagne;
    }

    @BeforeEach
    public void initTest() {
        campagne = createEntity(em);
    }

    @Test
    @Transactional
    void createCampagne() throws Exception {
        int databaseSizeBeforeCreate = campagneRepository.findAll().size();
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
        Campagne testCampagne = campagneList.get(campagneList.size() - 1);
        assertThat(testCampagne.getLibelle()).isEqualTo(DEFAULT_LIBELLE);
        assertThat(testCampagne.getDateDebut()).isEqualTo(DEFAULT_DATE_DEBUT);
        assertThat(testCampagne.getDateFin()).isEqualTo(DEFAULT_DATE_FIN);
        assertThat(testCampagne.getLibelleAbrege()).isEqualTo(DEFAULT_LIBELLE_ABREGE);
    }

    @Test
    @Transactional
    void createCampagneWithExistingId() throws Exception {
        // Create the Campagne with an existing ID
        campagne.setId(1L);
        CampagneDTO campagneDTO = campagneMapper.toDto(campagne);

        int databaseSizeBeforeCreate = campagneRepository.findAll().size();

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
            .andExpect(jsonPath("$.[*].libelle").value(hasItem(DEFAULT_LIBELLE)))
            .andExpect(jsonPath("$.[*].dateDebut").value(hasItem(DEFAULT_DATE_DEBUT.toString())))
            .andExpect(jsonPath("$.[*].dateFin").value(hasItem(DEFAULT_DATE_FIN.toString())))
            .andExpect(jsonPath("$.[*].libelleAbrege").value(hasItem(DEFAULT_LIBELLE_ABREGE)));
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
            .andExpect(jsonPath("$.libelle").value(DEFAULT_LIBELLE))
            .andExpect(jsonPath("$.dateDebut").value(DEFAULT_DATE_DEBUT.toString()))
            .andExpect(jsonPath("$.dateFin").value(DEFAULT_DATE_FIN.toString()))
            .andExpect(jsonPath("$.libelleAbrege").value(DEFAULT_LIBELLE_ABREGE));
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

        // Update the campagne
        Campagne updatedCampagne = campagneRepository.findById(campagne.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedCampagne are not directly saved in db
        em.detach(updatedCampagne);
        updatedCampagne
            .libelle(UPDATED_LIBELLE)
            .dateDebut(UPDATED_DATE_DEBUT)
            .dateFin(UPDATED_DATE_FIN)
            .libelleAbrege(UPDATED_LIBELLE_ABREGE);
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
        assertThat(testCampagne.getLibelle()).isEqualTo(UPDATED_LIBELLE);
        assertThat(testCampagne.getDateDebut()).isEqualTo(UPDATED_DATE_DEBUT);
        assertThat(testCampagne.getDateFin()).isEqualTo(UPDATED_DATE_FIN);
        assertThat(testCampagne.getLibelleAbrege()).isEqualTo(UPDATED_LIBELLE_ABREGE);
    }

    @Test
    @Transactional
    void putNonExistingCampagne() throws Exception {
        int databaseSizeBeforeUpdate = campagneRepository.findAll().size();
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
    }

    @Test
    @Transactional
    void putWithIdMismatchCampagne() throws Exception {
        int databaseSizeBeforeUpdate = campagneRepository.findAll().size();
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
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamCampagne() throws Exception {
        int databaseSizeBeforeUpdate = campagneRepository.findAll().size();
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
        assertThat(testCampagne.getLibelle()).isEqualTo(DEFAULT_LIBELLE);
        assertThat(testCampagne.getDateDebut()).isEqualTo(DEFAULT_DATE_DEBUT);
        assertThat(testCampagne.getDateFin()).isEqualTo(DEFAULT_DATE_FIN);
        assertThat(testCampagne.getLibelleAbrege()).isEqualTo(DEFAULT_LIBELLE_ABREGE);
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
            .libelle(UPDATED_LIBELLE)
            .dateDebut(UPDATED_DATE_DEBUT)
            .dateFin(UPDATED_DATE_FIN)
            .libelleAbrege(UPDATED_LIBELLE_ABREGE);

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
        assertThat(testCampagne.getLibelle()).isEqualTo(UPDATED_LIBELLE);
        assertThat(testCampagne.getDateDebut()).isEqualTo(UPDATED_DATE_DEBUT);
        assertThat(testCampagne.getDateFin()).isEqualTo(UPDATED_DATE_FIN);
        assertThat(testCampagne.getLibelleAbrege()).isEqualTo(UPDATED_LIBELLE_ABREGE);
    }

    @Test
    @Transactional
    void patchNonExistingCampagne() throws Exception {
        int databaseSizeBeforeUpdate = campagneRepository.findAll().size();
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
    }

    @Test
    @Transactional
    void patchWithIdMismatchCampagne() throws Exception {
        int databaseSizeBeforeUpdate = campagneRepository.findAll().size();
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
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamCampagne() throws Exception {
        int databaseSizeBeforeUpdate = campagneRepository.findAll().size();
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
    }

    @Test
    @Transactional
    void deleteCampagne() throws Exception {
        // Initialize the database
        campagneRepository.saveAndFlush(campagne);

        int databaseSizeBeforeDelete = campagneRepository.findAll().size();

        // Delete the campagne
        restCampagneMockMvc
            .perform(delete(ENTITY_API_URL_ID, campagne.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Campagne> campagneList = campagneRepository.findAll();
        assertThat(campagneList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
