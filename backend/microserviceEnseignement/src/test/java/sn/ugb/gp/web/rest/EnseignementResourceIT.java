package sn.ugb.gp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import jakarta.persistence.EntityManager;
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
import sn.ugb.gp.IntegrationTest;
import sn.ugb.gp.domain.Enseignement;
import sn.ugb.gp.repository.EnseignementRepository;
import sn.ugb.gp.service.dto.EnseignementDTO;
import sn.ugb.gp.service.mapper.EnseignementMapper;

/**
 * Integration tests for the {@link EnseignementResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class EnseignementResourceIT {

    private static final String DEFAULT_LIBELLE_ENSEIGNEMENTS = "AAAAAAAAAA";
    private static final String UPDATED_LIBELLE_ENSEIGNEMENTS = "BBBBBBBBBB";

    private static final Float DEFAULT_VOLUME_HORAIRE = 1F;
    private static final Float UPDATED_VOLUME_HORAIRE = 2F;

    private static final Integer DEFAULT_NOMBRE_INSCRITS = 1;
    private static final Integer UPDATED_NOMBRE_INSCRITS = 2;

    private static final Integer DEFAULT_GROUPE_YN = 1;
    private static final Integer UPDATED_GROUPE_YN = 2;

    private static final String ENTITY_API_URL = "/api/enseignements";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private EnseignementRepository enseignementRepository;

    @Autowired
    private EnseignementMapper enseignementMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restEnseignementMockMvc;

    private Enseignement enseignement;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Enseignement createEntity(EntityManager em) {
        Enseignement enseignement = new Enseignement()
            .libelleEnseignements(DEFAULT_LIBELLE_ENSEIGNEMENTS)
            .volumeHoraire(DEFAULT_VOLUME_HORAIRE)
            .nombreInscrits(DEFAULT_NOMBRE_INSCRITS)
            .groupeYN(DEFAULT_GROUPE_YN);
        return enseignement;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Enseignement createUpdatedEntity(EntityManager em) {
        Enseignement enseignement = new Enseignement()
            .libelleEnseignements(UPDATED_LIBELLE_ENSEIGNEMENTS)
            .volumeHoraire(UPDATED_VOLUME_HORAIRE)
            .nombreInscrits(UPDATED_NOMBRE_INSCRITS)
            .groupeYN(UPDATED_GROUPE_YN);
        return enseignement;
    }

    @BeforeEach
    public void initTest() {
        enseignement = createEntity(em);
    }

    @Test
    @Transactional
    void createEnseignement() throws Exception {
        int databaseSizeBeforeCreate = enseignementRepository.findAll().size();
        // Create the Enseignement
        EnseignementDTO enseignementDTO = enseignementMapper.toDto(enseignement);
        restEnseignementMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(enseignementDTO))
            )
            .andExpect(status().isCreated());

        // Validate the Enseignement in the database
        List<Enseignement> enseignementList = enseignementRepository.findAll();
        assertThat(enseignementList).hasSize(databaseSizeBeforeCreate + 1);
        Enseignement testEnseignement = enseignementList.get(enseignementList.size() - 1);
        assertThat(testEnseignement.getLibelleEnseignements()).isEqualTo(DEFAULT_LIBELLE_ENSEIGNEMENTS);
        assertThat(testEnseignement.getVolumeHoraire()).isEqualTo(DEFAULT_VOLUME_HORAIRE);
        assertThat(testEnseignement.getNombreInscrits()).isEqualTo(DEFAULT_NOMBRE_INSCRITS);
        assertThat(testEnseignement.getGroupeYN()).isEqualTo(DEFAULT_GROUPE_YN);
    }

    @Test
    @Transactional
    void createEnseignementWithExistingId() throws Exception {
        // Create the Enseignement with an existing ID
        enseignement.setId(1L);
        EnseignementDTO enseignementDTO = enseignementMapper.toDto(enseignement);

        int databaseSizeBeforeCreate = enseignementRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restEnseignementMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(enseignementDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Enseignement in the database
        List<Enseignement> enseignementList = enseignementRepository.findAll();
        assertThat(enseignementList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllEnseignements() throws Exception {
        // Initialize the database
        enseignementRepository.saveAndFlush(enseignement);

        // Get all the enseignementList
        restEnseignementMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(enseignement.getId().intValue())))
            .andExpect(jsonPath("$.[*].libelleEnseignements").value(hasItem(DEFAULT_LIBELLE_ENSEIGNEMENTS)))
            .andExpect(jsonPath("$.[*].volumeHoraire").value(hasItem(DEFAULT_VOLUME_HORAIRE.doubleValue())))
            .andExpect(jsonPath("$.[*].nombreInscrits").value(hasItem(DEFAULT_NOMBRE_INSCRITS)))
            .andExpect(jsonPath("$.[*].groupeYN").value(hasItem(DEFAULT_GROUPE_YN)));
    }

    @Test
    @Transactional
    void getEnseignement() throws Exception {
        // Initialize the database
        enseignementRepository.saveAndFlush(enseignement);

        // Get the enseignement
        restEnseignementMockMvc
            .perform(get(ENTITY_API_URL_ID, enseignement.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(enseignement.getId().intValue()))
            .andExpect(jsonPath("$.libelleEnseignements").value(DEFAULT_LIBELLE_ENSEIGNEMENTS))
            .andExpect(jsonPath("$.volumeHoraire").value(DEFAULT_VOLUME_HORAIRE.doubleValue()))
            .andExpect(jsonPath("$.nombreInscrits").value(DEFAULT_NOMBRE_INSCRITS))
            .andExpect(jsonPath("$.groupeYN").value(DEFAULT_GROUPE_YN));
    }

    @Test
    @Transactional
    void getNonExistingEnseignement() throws Exception {
        // Get the enseignement
        restEnseignementMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingEnseignement() throws Exception {
        // Initialize the database
        enseignementRepository.saveAndFlush(enseignement);

        int databaseSizeBeforeUpdate = enseignementRepository.findAll().size();

        // Update the enseignement
        Enseignement updatedEnseignement = enseignementRepository.findById(enseignement.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedEnseignement are not directly saved in db
        em.detach(updatedEnseignement);
        updatedEnseignement
            .libelleEnseignements(UPDATED_LIBELLE_ENSEIGNEMENTS)
            .volumeHoraire(UPDATED_VOLUME_HORAIRE)
            .nombreInscrits(UPDATED_NOMBRE_INSCRITS)
            .groupeYN(UPDATED_GROUPE_YN);
        EnseignementDTO enseignementDTO = enseignementMapper.toDto(updatedEnseignement);

        restEnseignementMockMvc
            .perform(
                put(ENTITY_API_URL_ID, enseignementDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(enseignementDTO))
            )
            .andExpect(status().isOk());

        // Validate the Enseignement in the database
        List<Enseignement> enseignementList = enseignementRepository.findAll();
        assertThat(enseignementList).hasSize(databaseSizeBeforeUpdate);
        Enseignement testEnseignement = enseignementList.get(enseignementList.size() - 1);
        assertThat(testEnseignement.getLibelleEnseignements()).isEqualTo(UPDATED_LIBELLE_ENSEIGNEMENTS);
        assertThat(testEnseignement.getVolumeHoraire()).isEqualTo(UPDATED_VOLUME_HORAIRE);
        assertThat(testEnseignement.getNombreInscrits()).isEqualTo(UPDATED_NOMBRE_INSCRITS);
        assertThat(testEnseignement.getGroupeYN()).isEqualTo(UPDATED_GROUPE_YN);
    }

    @Test
    @Transactional
    void putNonExistingEnseignement() throws Exception {
        int databaseSizeBeforeUpdate = enseignementRepository.findAll().size();
        enseignement.setId(longCount.incrementAndGet());

        // Create the Enseignement
        EnseignementDTO enseignementDTO = enseignementMapper.toDto(enseignement);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restEnseignementMockMvc
            .perform(
                put(ENTITY_API_URL_ID, enseignementDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(enseignementDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Enseignement in the database
        List<Enseignement> enseignementList = enseignementRepository.findAll();
        assertThat(enseignementList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchEnseignement() throws Exception {
        int databaseSizeBeforeUpdate = enseignementRepository.findAll().size();
        enseignement.setId(longCount.incrementAndGet());

        // Create the Enseignement
        EnseignementDTO enseignementDTO = enseignementMapper.toDto(enseignement);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEnseignementMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(enseignementDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Enseignement in the database
        List<Enseignement> enseignementList = enseignementRepository.findAll();
        assertThat(enseignementList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamEnseignement() throws Exception {
        int databaseSizeBeforeUpdate = enseignementRepository.findAll().size();
        enseignement.setId(longCount.incrementAndGet());

        // Create the Enseignement
        EnseignementDTO enseignementDTO = enseignementMapper.toDto(enseignement);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEnseignementMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(enseignementDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Enseignement in the database
        List<Enseignement> enseignementList = enseignementRepository.findAll();
        assertThat(enseignementList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateEnseignementWithPatch() throws Exception {
        // Initialize the database
        enseignementRepository.saveAndFlush(enseignement);

        int databaseSizeBeforeUpdate = enseignementRepository.findAll().size();

        // Update the enseignement using partial update
        Enseignement partialUpdatedEnseignement = new Enseignement();
        partialUpdatedEnseignement.setId(enseignement.getId());

        partialUpdatedEnseignement.volumeHoraire(UPDATED_VOLUME_HORAIRE).nombreInscrits(UPDATED_NOMBRE_INSCRITS);

        restEnseignementMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedEnseignement.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedEnseignement))
            )
            .andExpect(status().isOk());

        // Validate the Enseignement in the database
        List<Enseignement> enseignementList = enseignementRepository.findAll();
        assertThat(enseignementList).hasSize(databaseSizeBeforeUpdate);
        Enseignement testEnseignement = enseignementList.get(enseignementList.size() - 1);
        assertThat(testEnseignement.getLibelleEnseignements()).isEqualTo(DEFAULT_LIBELLE_ENSEIGNEMENTS);
        assertThat(testEnseignement.getVolumeHoraire()).isEqualTo(UPDATED_VOLUME_HORAIRE);
        assertThat(testEnseignement.getNombreInscrits()).isEqualTo(UPDATED_NOMBRE_INSCRITS);
        assertThat(testEnseignement.getGroupeYN()).isEqualTo(DEFAULT_GROUPE_YN);
    }

    @Test
    @Transactional
    void fullUpdateEnseignementWithPatch() throws Exception {
        // Initialize the database
        enseignementRepository.saveAndFlush(enseignement);

        int databaseSizeBeforeUpdate = enseignementRepository.findAll().size();

        // Update the enseignement using partial update
        Enseignement partialUpdatedEnseignement = new Enseignement();
        partialUpdatedEnseignement.setId(enseignement.getId());

        partialUpdatedEnseignement
            .libelleEnseignements(UPDATED_LIBELLE_ENSEIGNEMENTS)
            .volumeHoraire(UPDATED_VOLUME_HORAIRE)
            .nombreInscrits(UPDATED_NOMBRE_INSCRITS)
            .groupeYN(UPDATED_GROUPE_YN);

        restEnseignementMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedEnseignement.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedEnseignement))
            )
            .andExpect(status().isOk());

        // Validate the Enseignement in the database
        List<Enseignement> enseignementList = enseignementRepository.findAll();
        assertThat(enseignementList).hasSize(databaseSizeBeforeUpdate);
        Enseignement testEnseignement = enseignementList.get(enseignementList.size() - 1);
        assertThat(testEnseignement.getLibelleEnseignements()).isEqualTo(UPDATED_LIBELLE_ENSEIGNEMENTS);
        assertThat(testEnseignement.getVolumeHoraire()).isEqualTo(UPDATED_VOLUME_HORAIRE);
        assertThat(testEnseignement.getNombreInscrits()).isEqualTo(UPDATED_NOMBRE_INSCRITS);
        assertThat(testEnseignement.getGroupeYN()).isEqualTo(UPDATED_GROUPE_YN);
    }

    @Test
    @Transactional
    void patchNonExistingEnseignement() throws Exception {
        int databaseSizeBeforeUpdate = enseignementRepository.findAll().size();
        enseignement.setId(longCount.incrementAndGet());

        // Create the Enseignement
        EnseignementDTO enseignementDTO = enseignementMapper.toDto(enseignement);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restEnseignementMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, enseignementDTO.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(enseignementDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Enseignement in the database
        List<Enseignement> enseignementList = enseignementRepository.findAll();
        assertThat(enseignementList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchEnseignement() throws Exception {
        int databaseSizeBeforeUpdate = enseignementRepository.findAll().size();
        enseignement.setId(longCount.incrementAndGet());

        // Create the Enseignement
        EnseignementDTO enseignementDTO = enseignementMapper.toDto(enseignement);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEnseignementMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(enseignementDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Enseignement in the database
        List<Enseignement> enseignementList = enseignementRepository.findAll();
        assertThat(enseignementList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamEnseignement() throws Exception {
        int databaseSizeBeforeUpdate = enseignementRepository.findAll().size();
        enseignement.setId(longCount.incrementAndGet());

        // Create the Enseignement
        EnseignementDTO enseignementDTO = enseignementMapper.toDto(enseignement);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEnseignementMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(enseignementDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Enseignement in the database
        List<Enseignement> enseignementList = enseignementRepository.findAll();
        assertThat(enseignementList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteEnseignement() throws Exception {
        // Initialize the database
        enseignementRepository.saveAndFlush(enseignement);

        int databaseSizeBeforeDelete = enseignementRepository.findAll().size();

        // Delete the enseignement
        restEnseignementMockMvc
            .perform(delete(ENTITY_API_URL_ID, enseignement.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Enseignement> enseignementList = enseignementRepository.findAll();
        assertThat(enseignementList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
