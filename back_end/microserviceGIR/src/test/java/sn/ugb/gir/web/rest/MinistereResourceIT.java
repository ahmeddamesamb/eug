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
import sn.ugb.gir.domain.Ministere;
import sn.ugb.gir.repository.MinistereRepository;
import sn.ugb.gir.service.dto.MinistereDTO;
import sn.ugb.gir.service.mapper.MinistereMapper;

/**
 * Integration tests for the {@link MinistereResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class MinistereResourceIT {

    private static final String DEFAULT_NOM_MINISTERE = "AAAAAAAAAA";
    private static final String UPDATED_NOM_MINISTERE = "BBBBBBBBBB";

    private static final String DEFAULT_SIGLE_MINISTERE = "AAAAAAAAAA";
    private static final String UPDATED_SIGLE_MINISTERE = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_DATE_DEBUT = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE_DEBUT = LocalDate.now(ZoneId.systemDefault());

    private static final LocalDate DEFAULT_DATE_FIN = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE_FIN = LocalDate.now(ZoneId.systemDefault());

    private static final Integer DEFAULT_EN_COURS_YN = 1;
    private static final Integer UPDATED_EN_COURS_YN = 2;

    private static final String ENTITY_API_URL = "/api/ministeres";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private MinistereRepository ministereRepository;

    @Autowired
    private MinistereMapper ministereMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restMinistereMockMvc;

    private Ministere ministere;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Ministere createEntity(EntityManager em) {
        Ministere ministere = new Ministere()
            .nomMinistere(DEFAULT_NOM_MINISTERE)
            .sigleMinistere(DEFAULT_SIGLE_MINISTERE)
            .dateDebut(DEFAULT_DATE_DEBUT)
            .dateFin(DEFAULT_DATE_FIN)
            .enCoursYN(DEFAULT_EN_COURS_YN);
        return ministere;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Ministere createUpdatedEntity(EntityManager em) {
        Ministere ministere = new Ministere()
            .nomMinistere(UPDATED_NOM_MINISTERE)
            .sigleMinistere(UPDATED_SIGLE_MINISTERE)
            .dateDebut(UPDATED_DATE_DEBUT)
            .dateFin(UPDATED_DATE_FIN)
            .enCoursYN(UPDATED_EN_COURS_YN);
        return ministere;
    }

    @BeforeEach
    public void initTest() {
        ministere = createEntity(em);
    }

    @Test
    @Transactional
    void createMinistere() throws Exception {
        int databaseSizeBeforeCreate = ministereRepository.findAll().size();
        // Create the Ministere
        MinistereDTO ministereDTO = ministereMapper.toDto(ministere);
        restMinistereMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(ministereDTO))
            )
            .andExpect(status().isCreated());

        // Validate the Ministere in the database
        List<Ministere> ministereList = ministereRepository.findAll();
        assertThat(ministereList).hasSize(databaseSizeBeforeCreate + 1);
        Ministere testMinistere = ministereList.get(ministereList.size() - 1);
        assertThat(testMinistere.getNomMinistere()).isEqualTo(DEFAULT_NOM_MINISTERE);
        assertThat(testMinistere.getSigleMinistere()).isEqualTo(DEFAULT_SIGLE_MINISTERE);
        assertThat(testMinistere.getDateDebut()).isEqualTo(DEFAULT_DATE_DEBUT);
        assertThat(testMinistere.getDateFin()).isEqualTo(DEFAULT_DATE_FIN);
        assertThat(testMinistere.getEnCoursYN()).isEqualTo(DEFAULT_EN_COURS_YN);
    }

    @Test
    @Transactional
    void createMinistereWithExistingId() throws Exception {
        // Create the Ministere with an existing ID
        ministere.setId(1L);
        MinistereDTO ministereDTO = ministereMapper.toDto(ministere);

        int databaseSizeBeforeCreate = ministereRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restMinistereMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(ministereDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Ministere in the database
        List<Ministere> ministereList = ministereRepository.findAll();
        assertThat(ministereList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNomMinistereIsRequired() throws Exception {
        int databaseSizeBeforeTest = ministereRepository.findAll().size();
        // set the field null
        ministere.setNomMinistere(null);

        // Create the Ministere, which fails.
        MinistereDTO ministereDTO = ministereMapper.toDto(ministere);

        restMinistereMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(ministereDTO))
            )
            .andExpect(status().isBadRequest());

        List<Ministere> ministereList = ministereRepository.findAll();
        assertThat(ministereList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkDateDebutIsRequired() throws Exception {
        int databaseSizeBeforeTest = ministereRepository.findAll().size();
        // set the field null
        ministere.setDateDebut(null);

        // Create the Ministere, which fails.
        MinistereDTO ministereDTO = ministereMapper.toDto(ministere);

        restMinistereMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(ministereDTO))
            )
            .andExpect(status().isBadRequest());

        List<Ministere> ministereList = ministereRepository.findAll();
        assertThat(ministereList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkEnCoursYNIsRequired() throws Exception {
        int databaseSizeBeforeTest = ministereRepository.findAll().size();
        // set the field null
        ministere.setEnCoursYN(null);

        // Create the Ministere, which fails.
        MinistereDTO ministereDTO = ministereMapper.toDto(ministere);

        restMinistereMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(ministereDTO))
            )
            .andExpect(status().isBadRequest());

        List<Ministere> ministereList = ministereRepository.findAll();
        assertThat(ministereList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllMinisteres() throws Exception {
        // Initialize the database
        ministereRepository.saveAndFlush(ministere);

        // Get all the ministereList
        restMinistereMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(ministere.getId().intValue())))
            .andExpect(jsonPath("$.[*].nomMinistere").value(hasItem(DEFAULT_NOM_MINISTERE)))
            .andExpect(jsonPath("$.[*].sigleMinistere").value(hasItem(DEFAULT_SIGLE_MINISTERE)))
            .andExpect(jsonPath("$.[*].dateDebut").value(hasItem(DEFAULT_DATE_DEBUT.toString())))
            .andExpect(jsonPath("$.[*].dateFin").value(hasItem(DEFAULT_DATE_FIN.toString())))
            .andExpect(jsonPath("$.[*].enCoursYN").value(hasItem(DEFAULT_EN_COURS_YN)));
    }

    @Test
    @Transactional
    void getMinistere() throws Exception {
        // Initialize the database
        ministereRepository.saveAndFlush(ministere);

        // Get the ministere
        restMinistereMockMvc
            .perform(get(ENTITY_API_URL_ID, ministere.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(ministere.getId().intValue()))
            .andExpect(jsonPath("$.nomMinistere").value(DEFAULT_NOM_MINISTERE))
            .andExpect(jsonPath("$.sigleMinistere").value(DEFAULT_SIGLE_MINISTERE))
            .andExpect(jsonPath("$.dateDebut").value(DEFAULT_DATE_DEBUT.toString()))
            .andExpect(jsonPath("$.dateFin").value(DEFAULT_DATE_FIN.toString()))
            .andExpect(jsonPath("$.enCoursYN").value(DEFAULT_EN_COURS_YN));
    }

    @Test
    @Transactional
    void getNonExistingMinistere() throws Exception {
        // Get the ministere
        restMinistereMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingMinistere() throws Exception {
        // Initialize the database
        ministereRepository.saveAndFlush(ministere);

        int databaseSizeBeforeUpdate = ministereRepository.findAll().size();

        // Update the ministere
        Ministere updatedMinistere = ministereRepository.findById(ministere.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedMinistere are not directly saved in db
        em.detach(updatedMinistere);
        updatedMinistere
            .nomMinistere(UPDATED_NOM_MINISTERE)
            .sigleMinistere(UPDATED_SIGLE_MINISTERE)
            .dateDebut(UPDATED_DATE_DEBUT)
            .dateFin(UPDATED_DATE_FIN)
            .enCoursYN(UPDATED_EN_COURS_YN);
        MinistereDTO ministereDTO = ministereMapper.toDto(updatedMinistere);

        restMinistereMockMvc
            .perform(
                put(ENTITY_API_URL_ID, ministereDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(ministereDTO))
            )
            .andExpect(status().isOk());

        // Validate the Ministere in the database
        List<Ministere> ministereList = ministereRepository.findAll();
        assertThat(ministereList).hasSize(databaseSizeBeforeUpdate);
        Ministere testMinistere = ministereList.get(ministereList.size() - 1);
        assertThat(testMinistere.getNomMinistere()).isEqualTo(UPDATED_NOM_MINISTERE);
        assertThat(testMinistere.getSigleMinistere()).isEqualTo(UPDATED_SIGLE_MINISTERE);
        assertThat(testMinistere.getDateDebut()).isEqualTo(UPDATED_DATE_DEBUT);
        assertThat(testMinistere.getDateFin()).isEqualTo(UPDATED_DATE_FIN);
        assertThat(testMinistere.getEnCoursYN()).isEqualTo(UPDATED_EN_COURS_YN);
    }

    @Test
    @Transactional
    void putNonExistingMinistere() throws Exception {
        int databaseSizeBeforeUpdate = ministereRepository.findAll().size();
        ministere.setId(longCount.incrementAndGet());

        // Create the Ministere
        MinistereDTO ministereDTO = ministereMapper.toDto(ministere);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMinistereMockMvc
            .perform(
                put(ENTITY_API_URL_ID, ministereDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(ministereDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Ministere in the database
        List<Ministere> ministereList = ministereRepository.findAll();
        assertThat(ministereList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchMinistere() throws Exception {
        int databaseSizeBeforeUpdate = ministereRepository.findAll().size();
        ministere.setId(longCount.incrementAndGet());

        // Create the Ministere
        MinistereDTO ministereDTO = ministereMapper.toDto(ministere);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMinistereMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(ministereDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Ministere in the database
        List<Ministere> ministereList = ministereRepository.findAll();
        assertThat(ministereList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamMinistere() throws Exception {
        int databaseSizeBeforeUpdate = ministereRepository.findAll().size();
        ministere.setId(longCount.incrementAndGet());

        // Create the Ministere
        MinistereDTO ministereDTO = ministereMapper.toDto(ministere);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMinistereMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(ministereDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Ministere in the database
        List<Ministere> ministereList = ministereRepository.findAll();
        assertThat(ministereList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateMinistereWithPatch() throws Exception {
        // Initialize the database
        ministereRepository.saveAndFlush(ministere);

        int databaseSizeBeforeUpdate = ministereRepository.findAll().size();

        // Update the ministere using partial update
        Ministere partialUpdatedMinistere = new Ministere();
        partialUpdatedMinistere.setId(ministere.getId());

        partialUpdatedMinistere.dateDebut(UPDATED_DATE_DEBUT).dateFin(UPDATED_DATE_FIN).enCoursYN(UPDATED_EN_COURS_YN);

        restMinistereMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedMinistere.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedMinistere))
            )
            .andExpect(status().isOk());

        // Validate the Ministere in the database
        List<Ministere> ministereList = ministereRepository.findAll();
        assertThat(ministereList).hasSize(databaseSizeBeforeUpdate);
        Ministere testMinistere = ministereList.get(ministereList.size() - 1);
        assertThat(testMinistere.getNomMinistere()).isEqualTo(DEFAULT_NOM_MINISTERE);
        assertThat(testMinistere.getSigleMinistere()).isEqualTo(DEFAULT_SIGLE_MINISTERE);
        assertThat(testMinistere.getDateDebut()).isEqualTo(UPDATED_DATE_DEBUT);
        assertThat(testMinistere.getDateFin()).isEqualTo(UPDATED_DATE_FIN);
        assertThat(testMinistere.getEnCoursYN()).isEqualTo(UPDATED_EN_COURS_YN);
    }

    @Test
    @Transactional
    void fullUpdateMinistereWithPatch() throws Exception {
        // Initialize the database
        ministereRepository.saveAndFlush(ministere);

        int databaseSizeBeforeUpdate = ministereRepository.findAll().size();

        // Update the ministere using partial update
        Ministere partialUpdatedMinistere = new Ministere();
        partialUpdatedMinistere.setId(ministere.getId());

        partialUpdatedMinistere
            .nomMinistere(UPDATED_NOM_MINISTERE)
            .sigleMinistere(UPDATED_SIGLE_MINISTERE)
            .dateDebut(UPDATED_DATE_DEBUT)
            .dateFin(UPDATED_DATE_FIN)
            .enCoursYN(UPDATED_EN_COURS_YN);

        restMinistereMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedMinistere.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedMinistere))
            )
            .andExpect(status().isOk());

        // Validate the Ministere in the database
        List<Ministere> ministereList = ministereRepository.findAll();
        assertThat(ministereList).hasSize(databaseSizeBeforeUpdate);
        Ministere testMinistere = ministereList.get(ministereList.size() - 1);
        assertThat(testMinistere.getNomMinistere()).isEqualTo(UPDATED_NOM_MINISTERE);
        assertThat(testMinistere.getSigleMinistere()).isEqualTo(UPDATED_SIGLE_MINISTERE);
        assertThat(testMinistere.getDateDebut()).isEqualTo(UPDATED_DATE_DEBUT);
        assertThat(testMinistere.getDateFin()).isEqualTo(UPDATED_DATE_FIN);
        assertThat(testMinistere.getEnCoursYN()).isEqualTo(UPDATED_EN_COURS_YN);
    }

    @Test
    @Transactional
    void patchNonExistingMinistere() throws Exception {
        int databaseSizeBeforeUpdate = ministereRepository.findAll().size();
        ministere.setId(longCount.incrementAndGet());

        // Create the Ministere
        MinistereDTO ministereDTO = ministereMapper.toDto(ministere);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMinistereMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, ministereDTO.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(ministereDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Ministere in the database
        List<Ministere> ministereList = ministereRepository.findAll();
        assertThat(ministereList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchMinistere() throws Exception {
        int databaseSizeBeforeUpdate = ministereRepository.findAll().size();
        ministere.setId(longCount.incrementAndGet());

        // Create the Ministere
        MinistereDTO ministereDTO = ministereMapper.toDto(ministere);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMinistereMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(ministereDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Ministere in the database
        List<Ministere> ministereList = ministereRepository.findAll();
        assertThat(ministereList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamMinistere() throws Exception {
        int databaseSizeBeforeUpdate = ministereRepository.findAll().size();
        ministere.setId(longCount.incrementAndGet());

        // Create the Ministere
        MinistereDTO ministereDTO = ministereMapper.toDto(ministere);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMinistereMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(ministereDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Ministere in the database
        List<Ministere> ministereList = ministereRepository.findAll();
        assertThat(ministereList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteMinistere() throws Exception {
        // Initialize the database
        ministereRepository.saveAndFlush(ministere);

        int databaseSizeBeforeDelete = ministereRepository.findAll().size();

        // Delete the ministere
        restMinistereMockMvc
            .perform(delete(ENTITY_API_URL_ID, ministere.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Ministere> ministereList = ministereRepository.findAll();
        assertThat(ministereList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
