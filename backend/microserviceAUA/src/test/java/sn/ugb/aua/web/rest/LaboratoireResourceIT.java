package sn.ugb.aua.web.rest;

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
import sn.ugb.aua.IntegrationTest;
import sn.ugb.aua.domain.Laboratoire;
import sn.ugb.aua.repository.LaboratoireRepository;
import sn.ugb.aua.service.dto.LaboratoireDTO;
import sn.ugb.aua.service.mapper.LaboratoireMapper;

/**
 * Integration tests for the {@link LaboratoireResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class LaboratoireResourceIT {

    private static final String DEFAULT_NOM_LABORATOIRE = "AAAAAAAAAA";
    private static final String UPDATED_NOM_LABORATOIRE = "BBBBBBBBBB";

    private static final Integer DEFAULT_LABORATOIRE_COTUTELLE_YN = 1;
    private static final Integer UPDATED_LABORATOIRE_COTUTELLE_YN = 2;

    private static final String ENTITY_API_URL = "/api/laboratoires";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private LaboratoireRepository laboratoireRepository;

    @Autowired
    private LaboratoireMapper laboratoireMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restLaboratoireMockMvc;

    private Laboratoire laboratoire;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Laboratoire createEntity(EntityManager em) {
        Laboratoire laboratoire = new Laboratoire()
            .nomLaboratoire(DEFAULT_NOM_LABORATOIRE)
            .laboratoireCotutelleYN(DEFAULT_LABORATOIRE_COTUTELLE_YN);
        return laboratoire;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Laboratoire createUpdatedEntity(EntityManager em) {
        Laboratoire laboratoire = new Laboratoire()
            .nomLaboratoire(UPDATED_NOM_LABORATOIRE)
            .laboratoireCotutelleYN(UPDATED_LABORATOIRE_COTUTELLE_YN);
        return laboratoire;
    }

    @BeforeEach
    public void initTest() {
        laboratoire = createEntity(em);
    }

    @Test
    @Transactional
    void createLaboratoire() throws Exception {
        int databaseSizeBeforeCreate = laboratoireRepository.findAll().size();
        // Create the Laboratoire
        LaboratoireDTO laboratoireDTO = laboratoireMapper.toDto(laboratoire);
        restLaboratoireMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(laboratoireDTO))
            )
            .andExpect(status().isCreated());

        // Validate the Laboratoire in the database
        List<Laboratoire> laboratoireList = laboratoireRepository.findAll();
        assertThat(laboratoireList).hasSize(databaseSizeBeforeCreate + 1);
        Laboratoire testLaboratoire = laboratoireList.get(laboratoireList.size() - 1);
        assertThat(testLaboratoire.getNomLaboratoire()).isEqualTo(DEFAULT_NOM_LABORATOIRE);
        assertThat(testLaboratoire.getLaboratoireCotutelleYN()).isEqualTo(DEFAULT_LABORATOIRE_COTUTELLE_YN);
    }

    @Test
    @Transactional
    void createLaboratoireWithExistingId() throws Exception {
        // Create the Laboratoire with an existing ID
        laboratoire.setId(1L);
        LaboratoireDTO laboratoireDTO = laboratoireMapper.toDto(laboratoire);

        int databaseSizeBeforeCreate = laboratoireRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restLaboratoireMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(laboratoireDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Laboratoire in the database
        List<Laboratoire> laboratoireList = laboratoireRepository.findAll();
        assertThat(laboratoireList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNomLaboratoireIsRequired() throws Exception {
        int databaseSizeBeforeTest = laboratoireRepository.findAll().size();
        // set the field null
        laboratoire.setNomLaboratoire(null);

        // Create the Laboratoire, which fails.
        LaboratoireDTO laboratoireDTO = laboratoireMapper.toDto(laboratoire);

        restLaboratoireMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(laboratoireDTO))
            )
            .andExpect(status().isBadRequest());

        List<Laboratoire> laboratoireList = laboratoireRepository.findAll();
        assertThat(laboratoireList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkLaboratoireCotutelleYNIsRequired() throws Exception {
        int databaseSizeBeforeTest = laboratoireRepository.findAll().size();
        // set the field null
        laboratoire.setLaboratoireCotutelleYN(null);

        // Create the Laboratoire, which fails.
        LaboratoireDTO laboratoireDTO = laboratoireMapper.toDto(laboratoire);

        restLaboratoireMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(laboratoireDTO))
            )
            .andExpect(status().isBadRequest());

        List<Laboratoire> laboratoireList = laboratoireRepository.findAll();
        assertThat(laboratoireList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllLaboratoires() throws Exception {
        // Initialize the database
        laboratoireRepository.saveAndFlush(laboratoire);

        // Get all the laboratoireList
        restLaboratoireMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(laboratoire.getId().intValue())))
            .andExpect(jsonPath("$.[*].nomLaboratoire").value(hasItem(DEFAULT_NOM_LABORATOIRE)))
            .andExpect(jsonPath("$.[*].laboratoireCotutelleYN").value(hasItem(DEFAULT_LABORATOIRE_COTUTELLE_YN)));
    }

    @Test
    @Transactional
    void getLaboratoire() throws Exception {
        // Initialize the database
        laboratoireRepository.saveAndFlush(laboratoire);

        // Get the laboratoire
        restLaboratoireMockMvc
            .perform(get(ENTITY_API_URL_ID, laboratoire.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(laboratoire.getId().intValue()))
            .andExpect(jsonPath("$.nomLaboratoire").value(DEFAULT_NOM_LABORATOIRE))
            .andExpect(jsonPath("$.laboratoireCotutelleYN").value(DEFAULT_LABORATOIRE_COTUTELLE_YN));
    }

    @Test
    @Transactional
    void getNonExistingLaboratoire() throws Exception {
        // Get the laboratoire
        restLaboratoireMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingLaboratoire() throws Exception {
        // Initialize the database
        laboratoireRepository.saveAndFlush(laboratoire);

        int databaseSizeBeforeUpdate = laboratoireRepository.findAll().size();

        // Update the laboratoire
        Laboratoire updatedLaboratoire = laboratoireRepository.findById(laboratoire.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedLaboratoire are not directly saved in db
        em.detach(updatedLaboratoire);
        updatedLaboratoire.nomLaboratoire(UPDATED_NOM_LABORATOIRE).laboratoireCotutelleYN(UPDATED_LABORATOIRE_COTUTELLE_YN);
        LaboratoireDTO laboratoireDTO = laboratoireMapper.toDto(updatedLaboratoire);

        restLaboratoireMockMvc
            .perform(
                put(ENTITY_API_URL_ID, laboratoireDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(laboratoireDTO))
            )
            .andExpect(status().isOk());

        // Validate the Laboratoire in the database
        List<Laboratoire> laboratoireList = laboratoireRepository.findAll();
        assertThat(laboratoireList).hasSize(databaseSizeBeforeUpdate);
        Laboratoire testLaboratoire = laboratoireList.get(laboratoireList.size() - 1);
        assertThat(testLaboratoire.getNomLaboratoire()).isEqualTo(UPDATED_NOM_LABORATOIRE);
        assertThat(testLaboratoire.getLaboratoireCotutelleYN()).isEqualTo(UPDATED_LABORATOIRE_COTUTELLE_YN);
    }

    @Test
    @Transactional
    void putNonExistingLaboratoire() throws Exception {
        int databaseSizeBeforeUpdate = laboratoireRepository.findAll().size();
        laboratoire.setId(longCount.incrementAndGet());

        // Create the Laboratoire
        LaboratoireDTO laboratoireDTO = laboratoireMapper.toDto(laboratoire);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restLaboratoireMockMvc
            .perform(
                put(ENTITY_API_URL_ID, laboratoireDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(laboratoireDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Laboratoire in the database
        List<Laboratoire> laboratoireList = laboratoireRepository.findAll();
        assertThat(laboratoireList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchLaboratoire() throws Exception {
        int databaseSizeBeforeUpdate = laboratoireRepository.findAll().size();
        laboratoire.setId(longCount.incrementAndGet());

        // Create the Laboratoire
        LaboratoireDTO laboratoireDTO = laboratoireMapper.toDto(laboratoire);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLaboratoireMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(laboratoireDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Laboratoire in the database
        List<Laboratoire> laboratoireList = laboratoireRepository.findAll();
        assertThat(laboratoireList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamLaboratoire() throws Exception {
        int databaseSizeBeforeUpdate = laboratoireRepository.findAll().size();
        laboratoire.setId(longCount.incrementAndGet());

        // Create the Laboratoire
        LaboratoireDTO laboratoireDTO = laboratoireMapper.toDto(laboratoire);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLaboratoireMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(laboratoireDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Laboratoire in the database
        List<Laboratoire> laboratoireList = laboratoireRepository.findAll();
        assertThat(laboratoireList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateLaboratoireWithPatch() throws Exception {
        // Initialize the database
        laboratoireRepository.saveAndFlush(laboratoire);

        int databaseSizeBeforeUpdate = laboratoireRepository.findAll().size();

        // Update the laboratoire using partial update
        Laboratoire partialUpdatedLaboratoire = new Laboratoire();
        partialUpdatedLaboratoire.setId(laboratoire.getId());

        partialUpdatedLaboratoire.nomLaboratoire(UPDATED_NOM_LABORATOIRE);

        restLaboratoireMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedLaboratoire.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedLaboratoire))
            )
            .andExpect(status().isOk());

        // Validate the Laboratoire in the database
        List<Laboratoire> laboratoireList = laboratoireRepository.findAll();
        assertThat(laboratoireList).hasSize(databaseSizeBeforeUpdate);
        Laboratoire testLaboratoire = laboratoireList.get(laboratoireList.size() - 1);
        assertThat(testLaboratoire.getNomLaboratoire()).isEqualTo(UPDATED_NOM_LABORATOIRE);
        assertThat(testLaboratoire.getLaboratoireCotutelleYN()).isEqualTo(DEFAULT_LABORATOIRE_COTUTELLE_YN);
    }

    @Test
    @Transactional
    void fullUpdateLaboratoireWithPatch() throws Exception {
        // Initialize the database
        laboratoireRepository.saveAndFlush(laboratoire);

        int databaseSizeBeforeUpdate = laboratoireRepository.findAll().size();

        // Update the laboratoire using partial update
        Laboratoire partialUpdatedLaboratoire = new Laboratoire();
        partialUpdatedLaboratoire.setId(laboratoire.getId());

        partialUpdatedLaboratoire.nomLaboratoire(UPDATED_NOM_LABORATOIRE).laboratoireCotutelleYN(UPDATED_LABORATOIRE_COTUTELLE_YN);

        restLaboratoireMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedLaboratoire.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedLaboratoire))
            )
            .andExpect(status().isOk());

        // Validate the Laboratoire in the database
        List<Laboratoire> laboratoireList = laboratoireRepository.findAll();
        assertThat(laboratoireList).hasSize(databaseSizeBeforeUpdate);
        Laboratoire testLaboratoire = laboratoireList.get(laboratoireList.size() - 1);
        assertThat(testLaboratoire.getNomLaboratoire()).isEqualTo(UPDATED_NOM_LABORATOIRE);
        assertThat(testLaboratoire.getLaboratoireCotutelleYN()).isEqualTo(UPDATED_LABORATOIRE_COTUTELLE_YN);
    }

    @Test
    @Transactional
    void patchNonExistingLaboratoire() throws Exception {
        int databaseSizeBeforeUpdate = laboratoireRepository.findAll().size();
        laboratoire.setId(longCount.incrementAndGet());

        // Create the Laboratoire
        LaboratoireDTO laboratoireDTO = laboratoireMapper.toDto(laboratoire);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restLaboratoireMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, laboratoireDTO.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(laboratoireDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Laboratoire in the database
        List<Laboratoire> laboratoireList = laboratoireRepository.findAll();
        assertThat(laboratoireList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchLaboratoire() throws Exception {
        int databaseSizeBeforeUpdate = laboratoireRepository.findAll().size();
        laboratoire.setId(longCount.incrementAndGet());

        // Create the Laboratoire
        LaboratoireDTO laboratoireDTO = laboratoireMapper.toDto(laboratoire);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLaboratoireMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(laboratoireDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Laboratoire in the database
        List<Laboratoire> laboratoireList = laboratoireRepository.findAll();
        assertThat(laboratoireList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamLaboratoire() throws Exception {
        int databaseSizeBeforeUpdate = laboratoireRepository.findAll().size();
        laboratoire.setId(longCount.incrementAndGet());

        // Create the Laboratoire
        LaboratoireDTO laboratoireDTO = laboratoireMapper.toDto(laboratoire);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLaboratoireMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(laboratoireDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Laboratoire in the database
        List<Laboratoire> laboratoireList = laboratoireRepository.findAll();
        assertThat(laboratoireList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteLaboratoire() throws Exception {
        // Initialize the database
        laboratoireRepository.saveAndFlush(laboratoire);

        int databaseSizeBeforeDelete = laboratoireRepository.findAll().size();

        // Delete the laboratoire
        restLaboratoireMockMvc
            .perform(delete(ENTITY_API_URL_ID, laboratoire.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Laboratoire> laboratoireList = laboratoireRepository.findAll();
        assertThat(laboratoireList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
