package sn.ugb.gir.web.rest;

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
import sn.ugb.gir.IntegrationTest;
import sn.ugb.gir.domain.DisciplineSportiveEtudiant;
import sn.ugb.gir.repository.DisciplineSportiveEtudiantRepository;
import sn.ugb.gir.service.dto.DisciplineSportiveEtudiantDTO;
import sn.ugb.gir.service.mapper.DisciplineSportiveEtudiantMapper;

/**
 * Integration tests for the {@link DisciplineSportiveEtudiantResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class DisciplineSportiveEtudiantResourceIT {

    private static final Integer DEFAULT_LICENCE_SPORTIVE_YN = 1;
    private static final Integer UPDATED_LICENCE_SPORTIVE_YN = 2;

    private static final Integer DEFAULT_COMPETITION_UGBYN = 1;
    private static final Integer UPDATED_COMPETITION_UGBYN = 2;

    private static final String ENTITY_API_URL = "/api/discipline-sportive-etudiants";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private DisciplineSportiveEtudiantRepository disciplineSportiveEtudiantRepository;

    @Autowired
    private DisciplineSportiveEtudiantMapper disciplineSportiveEtudiantMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restDisciplineSportiveEtudiantMockMvc;

    private DisciplineSportiveEtudiant disciplineSportiveEtudiant;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static DisciplineSportiveEtudiant createEntity(EntityManager em) {
        DisciplineSportiveEtudiant disciplineSportiveEtudiant = new DisciplineSportiveEtudiant()
            .licenceSportiveYN(DEFAULT_LICENCE_SPORTIVE_YN)
            .competitionUGBYN(DEFAULT_COMPETITION_UGBYN);
        return disciplineSportiveEtudiant;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static DisciplineSportiveEtudiant createUpdatedEntity(EntityManager em) {
        DisciplineSportiveEtudiant disciplineSportiveEtudiant = new DisciplineSportiveEtudiant()
            .licenceSportiveYN(UPDATED_LICENCE_SPORTIVE_YN)
            .competitionUGBYN(UPDATED_COMPETITION_UGBYN);
        return disciplineSportiveEtudiant;
    }

    @BeforeEach
    public void initTest() {
        disciplineSportiveEtudiant = createEntity(em);
    }

    @Test
    @Transactional
    void createDisciplineSportiveEtudiant() throws Exception {
        int databaseSizeBeforeCreate = disciplineSportiveEtudiantRepository.findAll().size();
        // Create the DisciplineSportiveEtudiant
        DisciplineSportiveEtudiantDTO disciplineSportiveEtudiantDTO = disciplineSportiveEtudiantMapper.toDto(disciplineSportiveEtudiant);
        restDisciplineSportiveEtudiantMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(disciplineSportiveEtudiantDTO))
            )
            .andExpect(status().isCreated());

        // Validate the DisciplineSportiveEtudiant in the database
        List<DisciplineSportiveEtudiant> disciplineSportiveEtudiantList = disciplineSportiveEtudiantRepository.findAll();
        assertThat(disciplineSportiveEtudiantList).hasSize(databaseSizeBeforeCreate + 1);
        DisciplineSportiveEtudiant testDisciplineSportiveEtudiant = disciplineSportiveEtudiantList.get(
            disciplineSportiveEtudiantList.size() - 1
        );
        assertThat(testDisciplineSportiveEtudiant.getLicenceSportiveYN()).isEqualTo(DEFAULT_LICENCE_SPORTIVE_YN);
        assertThat(testDisciplineSportiveEtudiant.getCompetitionUGBYN()).isEqualTo(DEFAULT_COMPETITION_UGBYN);
    }

    @Test
    @Transactional
    void createDisciplineSportiveEtudiantWithExistingId() throws Exception {
        // Create the DisciplineSportiveEtudiant with an existing ID
        disciplineSportiveEtudiant.setId(1L);
        DisciplineSportiveEtudiantDTO disciplineSportiveEtudiantDTO = disciplineSportiveEtudiantMapper.toDto(disciplineSportiveEtudiant);

        int databaseSizeBeforeCreate = disciplineSportiveEtudiantRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restDisciplineSportiveEtudiantMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(disciplineSportiveEtudiantDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the DisciplineSportiveEtudiant in the database
        List<DisciplineSportiveEtudiant> disciplineSportiveEtudiantList = disciplineSportiveEtudiantRepository.findAll();
        assertThat(disciplineSportiveEtudiantList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllDisciplineSportiveEtudiants() throws Exception {
        // Initialize the database
        disciplineSportiveEtudiantRepository.saveAndFlush(disciplineSportiveEtudiant);

        // Get all the disciplineSportiveEtudiantList
        restDisciplineSportiveEtudiantMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(disciplineSportiveEtudiant.getId().intValue())))
            .andExpect(jsonPath("$.[*].licenceSportiveYN").value(hasItem(DEFAULT_LICENCE_SPORTIVE_YN)))
            .andExpect(jsonPath("$.[*].competitionUGBYN").value(hasItem(DEFAULT_COMPETITION_UGBYN)));
    }

    @Test
    @Transactional
    void getDisciplineSportiveEtudiant() throws Exception {
        // Initialize the database
        disciplineSportiveEtudiantRepository.saveAndFlush(disciplineSportiveEtudiant);

        // Get the disciplineSportiveEtudiant
        restDisciplineSportiveEtudiantMockMvc
            .perform(get(ENTITY_API_URL_ID, disciplineSportiveEtudiant.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(disciplineSportiveEtudiant.getId().intValue()))
            .andExpect(jsonPath("$.licenceSportiveYN").value(DEFAULT_LICENCE_SPORTIVE_YN))
            .andExpect(jsonPath("$.competitionUGBYN").value(DEFAULT_COMPETITION_UGBYN));
    }

    @Test
    @Transactional
    void getNonExistingDisciplineSportiveEtudiant() throws Exception {
        // Get the disciplineSportiveEtudiant
        restDisciplineSportiveEtudiantMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingDisciplineSportiveEtudiant() throws Exception {
        // Initialize the database
        disciplineSportiveEtudiantRepository.saveAndFlush(disciplineSportiveEtudiant);

        int databaseSizeBeforeUpdate = disciplineSportiveEtudiantRepository.findAll().size();

        // Update the disciplineSportiveEtudiant
        DisciplineSportiveEtudiant updatedDisciplineSportiveEtudiant = disciplineSportiveEtudiantRepository
            .findById(disciplineSportiveEtudiant.getId())
            .orElseThrow();
        // Disconnect from session so that the updates on updatedDisciplineSportiveEtudiant are not directly saved in db
        em.detach(updatedDisciplineSportiveEtudiant);
        updatedDisciplineSportiveEtudiant.licenceSportiveYN(UPDATED_LICENCE_SPORTIVE_YN).competitionUGBYN(UPDATED_COMPETITION_UGBYN);
        DisciplineSportiveEtudiantDTO disciplineSportiveEtudiantDTO = disciplineSportiveEtudiantMapper.toDto(
            updatedDisciplineSportiveEtudiant
        );

        restDisciplineSportiveEtudiantMockMvc
            .perform(
                put(ENTITY_API_URL_ID, disciplineSportiveEtudiantDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(disciplineSportiveEtudiantDTO))
            )
            .andExpect(status().isOk());

        // Validate the DisciplineSportiveEtudiant in the database
        List<DisciplineSportiveEtudiant> disciplineSportiveEtudiantList = disciplineSportiveEtudiantRepository.findAll();
        assertThat(disciplineSportiveEtudiantList).hasSize(databaseSizeBeforeUpdate);
        DisciplineSportiveEtudiant testDisciplineSportiveEtudiant = disciplineSportiveEtudiantList.get(
            disciplineSportiveEtudiantList.size() - 1
        );
        assertThat(testDisciplineSportiveEtudiant.getLicenceSportiveYN()).isEqualTo(UPDATED_LICENCE_SPORTIVE_YN);
        assertThat(testDisciplineSportiveEtudiant.getCompetitionUGBYN()).isEqualTo(UPDATED_COMPETITION_UGBYN);
    }

    @Test
    @Transactional
    void putNonExistingDisciplineSportiveEtudiant() throws Exception {
        int databaseSizeBeforeUpdate = disciplineSportiveEtudiantRepository.findAll().size();
        disciplineSportiveEtudiant.setId(longCount.incrementAndGet());

        // Create the DisciplineSportiveEtudiant
        DisciplineSportiveEtudiantDTO disciplineSportiveEtudiantDTO = disciplineSportiveEtudiantMapper.toDto(disciplineSportiveEtudiant);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDisciplineSportiveEtudiantMockMvc
            .perform(
                put(ENTITY_API_URL_ID, disciplineSportiveEtudiantDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(disciplineSportiveEtudiantDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the DisciplineSportiveEtudiant in the database
        List<DisciplineSportiveEtudiant> disciplineSportiveEtudiantList = disciplineSportiveEtudiantRepository.findAll();
        assertThat(disciplineSportiveEtudiantList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchDisciplineSportiveEtudiant() throws Exception {
        int databaseSizeBeforeUpdate = disciplineSportiveEtudiantRepository.findAll().size();
        disciplineSportiveEtudiant.setId(longCount.incrementAndGet());

        // Create the DisciplineSportiveEtudiant
        DisciplineSportiveEtudiantDTO disciplineSportiveEtudiantDTO = disciplineSportiveEtudiantMapper.toDto(disciplineSportiveEtudiant);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDisciplineSportiveEtudiantMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(disciplineSportiveEtudiantDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the DisciplineSportiveEtudiant in the database
        List<DisciplineSportiveEtudiant> disciplineSportiveEtudiantList = disciplineSportiveEtudiantRepository.findAll();
        assertThat(disciplineSportiveEtudiantList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamDisciplineSportiveEtudiant() throws Exception {
        int databaseSizeBeforeUpdate = disciplineSportiveEtudiantRepository.findAll().size();
        disciplineSportiveEtudiant.setId(longCount.incrementAndGet());

        // Create the DisciplineSportiveEtudiant
        DisciplineSportiveEtudiantDTO disciplineSportiveEtudiantDTO = disciplineSportiveEtudiantMapper.toDto(disciplineSportiveEtudiant);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDisciplineSportiveEtudiantMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(disciplineSportiveEtudiantDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the DisciplineSportiveEtudiant in the database
        List<DisciplineSportiveEtudiant> disciplineSportiveEtudiantList = disciplineSportiveEtudiantRepository.findAll();
        assertThat(disciplineSportiveEtudiantList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateDisciplineSportiveEtudiantWithPatch() throws Exception {
        // Initialize the database
        disciplineSportiveEtudiantRepository.saveAndFlush(disciplineSportiveEtudiant);

        int databaseSizeBeforeUpdate = disciplineSportiveEtudiantRepository.findAll().size();

        // Update the disciplineSportiveEtudiant using partial update
        DisciplineSportiveEtudiant partialUpdatedDisciplineSportiveEtudiant = new DisciplineSportiveEtudiant();
        partialUpdatedDisciplineSportiveEtudiant.setId(disciplineSportiveEtudiant.getId());

        restDisciplineSportiveEtudiantMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDisciplineSportiveEtudiant.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedDisciplineSportiveEtudiant))
            )
            .andExpect(status().isOk());

        // Validate the DisciplineSportiveEtudiant in the database
        List<DisciplineSportiveEtudiant> disciplineSportiveEtudiantList = disciplineSportiveEtudiantRepository.findAll();
        assertThat(disciplineSportiveEtudiantList).hasSize(databaseSizeBeforeUpdate);
        DisciplineSportiveEtudiant testDisciplineSportiveEtudiant = disciplineSportiveEtudiantList.get(
            disciplineSportiveEtudiantList.size() - 1
        );
        assertThat(testDisciplineSportiveEtudiant.getLicenceSportiveYN()).isEqualTo(DEFAULT_LICENCE_SPORTIVE_YN);
        assertThat(testDisciplineSportiveEtudiant.getCompetitionUGBYN()).isEqualTo(DEFAULT_COMPETITION_UGBYN);
    }

    @Test
    @Transactional
    void fullUpdateDisciplineSportiveEtudiantWithPatch() throws Exception {
        // Initialize the database
        disciplineSportiveEtudiantRepository.saveAndFlush(disciplineSportiveEtudiant);

        int databaseSizeBeforeUpdate = disciplineSportiveEtudiantRepository.findAll().size();

        // Update the disciplineSportiveEtudiant using partial update
        DisciplineSportiveEtudiant partialUpdatedDisciplineSportiveEtudiant = new DisciplineSportiveEtudiant();
        partialUpdatedDisciplineSportiveEtudiant.setId(disciplineSportiveEtudiant.getId());

        partialUpdatedDisciplineSportiveEtudiant.licenceSportiveYN(UPDATED_LICENCE_SPORTIVE_YN).competitionUGBYN(UPDATED_COMPETITION_UGBYN);

        restDisciplineSportiveEtudiantMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDisciplineSportiveEtudiant.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedDisciplineSportiveEtudiant))
            )
            .andExpect(status().isOk());

        // Validate the DisciplineSportiveEtudiant in the database
        List<DisciplineSportiveEtudiant> disciplineSportiveEtudiantList = disciplineSportiveEtudiantRepository.findAll();
        assertThat(disciplineSportiveEtudiantList).hasSize(databaseSizeBeforeUpdate);
        DisciplineSportiveEtudiant testDisciplineSportiveEtudiant = disciplineSportiveEtudiantList.get(
            disciplineSportiveEtudiantList.size() - 1
        );
        assertThat(testDisciplineSportiveEtudiant.getLicenceSportiveYN()).isEqualTo(UPDATED_LICENCE_SPORTIVE_YN);
        assertThat(testDisciplineSportiveEtudiant.getCompetitionUGBYN()).isEqualTo(UPDATED_COMPETITION_UGBYN);
    }

    @Test
    @Transactional
    void patchNonExistingDisciplineSportiveEtudiant() throws Exception {
        int databaseSizeBeforeUpdate = disciplineSportiveEtudiantRepository.findAll().size();
        disciplineSportiveEtudiant.setId(longCount.incrementAndGet());

        // Create the DisciplineSportiveEtudiant
        DisciplineSportiveEtudiantDTO disciplineSportiveEtudiantDTO = disciplineSportiveEtudiantMapper.toDto(disciplineSportiveEtudiant);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDisciplineSportiveEtudiantMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, disciplineSportiveEtudiantDTO.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(disciplineSportiveEtudiantDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the DisciplineSportiveEtudiant in the database
        List<DisciplineSportiveEtudiant> disciplineSportiveEtudiantList = disciplineSportiveEtudiantRepository.findAll();
        assertThat(disciplineSportiveEtudiantList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchDisciplineSportiveEtudiant() throws Exception {
        int databaseSizeBeforeUpdate = disciplineSportiveEtudiantRepository.findAll().size();
        disciplineSportiveEtudiant.setId(longCount.incrementAndGet());

        // Create the DisciplineSportiveEtudiant
        DisciplineSportiveEtudiantDTO disciplineSportiveEtudiantDTO = disciplineSportiveEtudiantMapper.toDto(disciplineSportiveEtudiant);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDisciplineSportiveEtudiantMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(disciplineSportiveEtudiantDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the DisciplineSportiveEtudiant in the database
        List<DisciplineSportiveEtudiant> disciplineSportiveEtudiantList = disciplineSportiveEtudiantRepository.findAll();
        assertThat(disciplineSportiveEtudiantList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamDisciplineSportiveEtudiant() throws Exception {
        int databaseSizeBeforeUpdate = disciplineSportiveEtudiantRepository.findAll().size();
        disciplineSportiveEtudiant.setId(longCount.incrementAndGet());

        // Create the DisciplineSportiveEtudiant
        DisciplineSportiveEtudiantDTO disciplineSportiveEtudiantDTO = disciplineSportiveEtudiantMapper.toDto(disciplineSportiveEtudiant);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDisciplineSportiveEtudiantMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(disciplineSportiveEtudiantDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the DisciplineSportiveEtudiant in the database
        List<DisciplineSportiveEtudiant> disciplineSportiveEtudiantList = disciplineSportiveEtudiantRepository.findAll();
        assertThat(disciplineSportiveEtudiantList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteDisciplineSportiveEtudiant() throws Exception {
        // Initialize the database
        disciplineSportiveEtudiantRepository.saveAndFlush(disciplineSportiveEtudiant);

        int databaseSizeBeforeDelete = disciplineSportiveEtudiantRepository.findAll().size();

        // Delete the disciplineSportiveEtudiant
        restDisciplineSportiveEtudiantMockMvc
            .perform(delete(ENTITY_API_URL_ID, disciplineSportiveEtudiant.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<DisciplineSportiveEtudiant> disciplineSportiveEtudiantList = disciplineSportiveEtudiantRepository.findAll();
        assertThat(disciplineSportiveEtudiantList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
