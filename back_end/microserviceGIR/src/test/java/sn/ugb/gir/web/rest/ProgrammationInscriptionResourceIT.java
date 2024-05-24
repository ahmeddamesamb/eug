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
import sn.ugb.gir.domain.ProgrammationInscription;
import sn.ugb.gir.repository.ProgrammationInscriptionRepository;
import sn.ugb.gir.service.dto.ProgrammationInscriptionDTO;
import sn.ugb.gir.service.mapper.ProgrammationInscriptionMapper;

/**
 * Integration tests for the {@link ProgrammationInscriptionResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ProgrammationInscriptionResourceIT {

    private static final String DEFAULT_LIBELLE_PROGRAMMATION = "AAAAAAAAAA";
    private static final String UPDATED_LIBELLE_PROGRAMMATION = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_DATE_DEBUT = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE_DEBUT = LocalDate.now(ZoneId.systemDefault());

    private static final LocalDate DEFAULT_DATE_FIN = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE_FIN = LocalDate.now(ZoneId.systemDefault());

    private static final Integer DEFAULT_OUVERT_YN = 1;
    private static final Integer UPDATED_OUVERT_YN = 2;

    private static final String DEFAULT_EMAIL_USER = "AAAAAAAAAA";
    private static final String UPDATED_EMAIL_USER = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/programmation-inscriptions";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ProgrammationInscriptionRepository programmationInscriptionRepository;

    @Autowired
    private ProgrammationInscriptionMapper programmationInscriptionMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restProgrammationInscriptionMockMvc;

    private ProgrammationInscription programmationInscription;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ProgrammationInscription createEntity(EntityManager em) {
        ProgrammationInscription programmationInscription = new ProgrammationInscription()
            .libelleProgrammation(DEFAULT_LIBELLE_PROGRAMMATION)
            .dateDebut(DEFAULT_DATE_DEBUT)
            .dateFin(DEFAULT_DATE_FIN)
            .ouvertYN(DEFAULT_OUVERT_YN)
            .emailUser(DEFAULT_EMAIL_USER);
        return programmationInscription;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ProgrammationInscription createUpdatedEntity(EntityManager em) {
        ProgrammationInscription programmationInscription = new ProgrammationInscription()
            .libelleProgrammation(UPDATED_LIBELLE_PROGRAMMATION)
            .dateDebut(UPDATED_DATE_DEBUT)
            .dateFin(UPDATED_DATE_FIN)
            .ouvertYN(UPDATED_OUVERT_YN)
            .emailUser(UPDATED_EMAIL_USER);
        return programmationInscription;
    }

    @BeforeEach
    public void initTest() {
        programmationInscription = createEntity(em);
    }

    @Test
    @Transactional
    void createProgrammationInscription() throws Exception {
        int databaseSizeBeforeCreate = programmationInscriptionRepository.findAll().size();
        // Create the ProgrammationInscription
        ProgrammationInscriptionDTO programmationInscriptionDTO = programmationInscriptionMapper.toDto(programmationInscription);
        restProgrammationInscriptionMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(programmationInscriptionDTO))
            )
            .andExpect(status().isCreated());

        // Validate the ProgrammationInscription in the database
        List<ProgrammationInscription> programmationInscriptionList = programmationInscriptionRepository.findAll();
        assertThat(programmationInscriptionList).hasSize(databaseSizeBeforeCreate + 1);
        ProgrammationInscription testProgrammationInscription = programmationInscriptionList.get(programmationInscriptionList.size() - 1);
        assertThat(testProgrammationInscription.getLibelleProgrammation()).isEqualTo(DEFAULT_LIBELLE_PROGRAMMATION);
        assertThat(testProgrammationInscription.getDateDebut()).isEqualTo(DEFAULT_DATE_DEBUT);
        assertThat(testProgrammationInscription.getDateFin()).isEqualTo(DEFAULT_DATE_FIN);
        assertThat(testProgrammationInscription.getOuvertYN()).isEqualTo(DEFAULT_OUVERT_YN);
        assertThat(testProgrammationInscription.getEmailUser()).isEqualTo(DEFAULT_EMAIL_USER);
    }

    @Test
    @Transactional
    void createProgrammationInscriptionWithExistingId() throws Exception {
        // Create the ProgrammationInscription with an existing ID
        programmationInscription.setId(1L);
        ProgrammationInscriptionDTO programmationInscriptionDTO = programmationInscriptionMapper.toDto(programmationInscription);

        int databaseSizeBeforeCreate = programmationInscriptionRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restProgrammationInscriptionMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(programmationInscriptionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProgrammationInscription in the database
        List<ProgrammationInscription> programmationInscriptionList = programmationInscriptionRepository.findAll();
        assertThat(programmationInscriptionList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllProgrammationInscriptions() throws Exception {
        // Initialize the database
        programmationInscriptionRepository.saveAndFlush(programmationInscription);

        // Get all the programmationInscriptionList
        restProgrammationInscriptionMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(programmationInscription.getId().intValue())))
            .andExpect(jsonPath("$.[*].libelleProgrammation").value(hasItem(DEFAULT_LIBELLE_PROGRAMMATION)))
            .andExpect(jsonPath("$.[*].dateDebut").value(hasItem(DEFAULT_DATE_DEBUT.toString())))
            .andExpect(jsonPath("$.[*].dateFin").value(hasItem(DEFAULT_DATE_FIN.toString())))
            .andExpect(jsonPath("$.[*].ouvertYN").value(hasItem(DEFAULT_OUVERT_YN)))
            .andExpect(jsonPath("$.[*].emailUser").value(hasItem(DEFAULT_EMAIL_USER)));
    }

    @Test
    @Transactional
    void getProgrammationInscription() throws Exception {
        // Initialize the database
        programmationInscriptionRepository.saveAndFlush(programmationInscription);

        // Get the programmationInscription
        restProgrammationInscriptionMockMvc
            .perform(get(ENTITY_API_URL_ID, programmationInscription.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(programmationInscription.getId().intValue()))
            .andExpect(jsonPath("$.libelleProgrammation").value(DEFAULT_LIBELLE_PROGRAMMATION))
            .andExpect(jsonPath("$.dateDebut").value(DEFAULT_DATE_DEBUT.toString()))
            .andExpect(jsonPath("$.dateFin").value(DEFAULT_DATE_FIN.toString()))
            .andExpect(jsonPath("$.ouvertYN").value(DEFAULT_OUVERT_YN))
            .andExpect(jsonPath("$.emailUser").value(DEFAULT_EMAIL_USER));
    }

    @Test
    @Transactional
    void getNonExistingProgrammationInscription() throws Exception {
        // Get the programmationInscription
        restProgrammationInscriptionMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingProgrammationInscription() throws Exception {
        // Initialize the database
        programmationInscriptionRepository.saveAndFlush(programmationInscription);

        int databaseSizeBeforeUpdate = programmationInscriptionRepository.findAll().size();

        // Update the programmationInscription
        ProgrammationInscription updatedProgrammationInscription = programmationInscriptionRepository
            .findById(programmationInscription.getId())
            .orElseThrow();
        // Disconnect from session so that the updates on updatedProgrammationInscription are not directly saved in db
        em.detach(updatedProgrammationInscription);
        updatedProgrammationInscription
            .libelleProgrammation(UPDATED_LIBELLE_PROGRAMMATION)
            .dateDebut(UPDATED_DATE_DEBUT)
            .dateFin(UPDATED_DATE_FIN)
            .ouvertYN(UPDATED_OUVERT_YN)
            .emailUser(UPDATED_EMAIL_USER);
        ProgrammationInscriptionDTO programmationInscriptionDTO = programmationInscriptionMapper.toDto(updatedProgrammationInscription);

        restProgrammationInscriptionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, programmationInscriptionDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(programmationInscriptionDTO))
            )
            .andExpect(status().isOk());

        // Validate the ProgrammationInscription in the database
        List<ProgrammationInscription> programmationInscriptionList = programmationInscriptionRepository.findAll();
        assertThat(programmationInscriptionList).hasSize(databaseSizeBeforeUpdate);
        ProgrammationInscription testProgrammationInscription = programmationInscriptionList.get(programmationInscriptionList.size() - 1);
        assertThat(testProgrammationInscription.getLibelleProgrammation()).isEqualTo(UPDATED_LIBELLE_PROGRAMMATION);
        assertThat(testProgrammationInscription.getDateDebut()).isEqualTo(UPDATED_DATE_DEBUT);
        assertThat(testProgrammationInscription.getDateFin()).isEqualTo(UPDATED_DATE_FIN);
        assertThat(testProgrammationInscription.getOuvertYN()).isEqualTo(UPDATED_OUVERT_YN);
        assertThat(testProgrammationInscription.getEmailUser()).isEqualTo(UPDATED_EMAIL_USER);
    }

    @Test
    @Transactional
    void putNonExistingProgrammationInscription() throws Exception {
        int databaseSizeBeforeUpdate = programmationInscriptionRepository.findAll().size();
        programmationInscription.setId(longCount.incrementAndGet());

        // Create the ProgrammationInscription
        ProgrammationInscriptionDTO programmationInscriptionDTO = programmationInscriptionMapper.toDto(programmationInscription);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restProgrammationInscriptionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, programmationInscriptionDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(programmationInscriptionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProgrammationInscription in the database
        List<ProgrammationInscription> programmationInscriptionList = programmationInscriptionRepository.findAll();
        assertThat(programmationInscriptionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchProgrammationInscription() throws Exception {
        int databaseSizeBeforeUpdate = programmationInscriptionRepository.findAll().size();
        programmationInscription.setId(longCount.incrementAndGet());

        // Create the ProgrammationInscription
        ProgrammationInscriptionDTO programmationInscriptionDTO = programmationInscriptionMapper.toDto(programmationInscription);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProgrammationInscriptionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(programmationInscriptionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProgrammationInscription in the database
        List<ProgrammationInscription> programmationInscriptionList = programmationInscriptionRepository.findAll();
        assertThat(programmationInscriptionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamProgrammationInscription() throws Exception {
        int databaseSizeBeforeUpdate = programmationInscriptionRepository.findAll().size();
        programmationInscription.setId(longCount.incrementAndGet());

        // Create the ProgrammationInscription
        ProgrammationInscriptionDTO programmationInscriptionDTO = programmationInscriptionMapper.toDto(programmationInscription);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProgrammationInscriptionMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(programmationInscriptionDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the ProgrammationInscription in the database
        List<ProgrammationInscription> programmationInscriptionList = programmationInscriptionRepository.findAll();
        assertThat(programmationInscriptionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateProgrammationInscriptionWithPatch() throws Exception {
        // Initialize the database
        programmationInscriptionRepository.saveAndFlush(programmationInscription);

        int databaseSizeBeforeUpdate = programmationInscriptionRepository.findAll().size();

        // Update the programmationInscription using partial update
        ProgrammationInscription partialUpdatedProgrammationInscription = new ProgrammationInscription();
        partialUpdatedProgrammationInscription.setId(programmationInscription.getId());

        partialUpdatedProgrammationInscription
            .libelleProgrammation(UPDATED_LIBELLE_PROGRAMMATION)
            .ouvertYN(UPDATED_OUVERT_YN)
            .emailUser(UPDATED_EMAIL_USER);

        restProgrammationInscriptionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedProgrammationInscription.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedProgrammationInscription))
            )
            .andExpect(status().isOk());

        // Validate the ProgrammationInscription in the database
        List<ProgrammationInscription> programmationInscriptionList = programmationInscriptionRepository.findAll();
        assertThat(programmationInscriptionList).hasSize(databaseSizeBeforeUpdate);
        ProgrammationInscription testProgrammationInscription = programmationInscriptionList.get(programmationInscriptionList.size() - 1);
        assertThat(testProgrammationInscription.getLibelleProgrammation()).isEqualTo(UPDATED_LIBELLE_PROGRAMMATION);
        assertThat(testProgrammationInscription.getDateDebut()).isEqualTo(DEFAULT_DATE_DEBUT);
        assertThat(testProgrammationInscription.getDateFin()).isEqualTo(DEFAULT_DATE_FIN);
        assertThat(testProgrammationInscription.getOuvertYN()).isEqualTo(UPDATED_OUVERT_YN);
        assertThat(testProgrammationInscription.getEmailUser()).isEqualTo(UPDATED_EMAIL_USER);
    }

    @Test
    @Transactional
    void fullUpdateProgrammationInscriptionWithPatch() throws Exception {
        // Initialize the database
        programmationInscriptionRepository.saveAndFlush(programmationInscription);

        int databaseSizeBeforeUpdate = programmationInscriptionRepository.findAll().size();

        // Update the programmationInscription using partial update
        ProgrammationInscription partialUpdatedProgrammationInscription = new ProgrammationInscription();
        partialUpdatedProgrammationInscription.setId(programmationInscription.getId());

        partialUpdatedProgrammationInscription
            .libelleProgrammation(UPDATED_LIBELLE_PROGRAMMATION)
            .dateDebut(UPDATED_DATE_DEBUT)
            .dateFin(UPDATED_DATE_FIN)
            .ouvertYN(UPDATED_OUVERT_YN)
            .emailUser(UPDATED_EMAIL_USER);

        restProgrammationInscriptionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedProgrammationInscription.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedProgrammationInscription))
            )
            .andExpect(status().isOk());

        // Validate the ProgrammationInscription in the database
        List<ProgrammationInscription> programmationInscriptionList = programmationInscriptionRepository.findAll();
        assertThat(programmationInscriptionList).hasSize(databaseSizeBeforeUpdate);
        ProgrammationInscription testProgrammationInscription = programmationInscriptionList.get(programmationInscriptionList.size() - 1);
        assertThat(testProgrammationInscription.getLibelleProgrammation()).isEqualTo(UPDATED_LIBELLE_PROGRAMMATION);
        assertThat(testProgrammationInscription.getDateDebut()).isEqualTo(UPDATED_DATE_DEBUT);
        assertThat(testProgrammationInscription.getDateFin()).isEqualTo(UPDATED_DATE_FIN);
        assertThat(testProgrammationInscription.getOuvertYN()).isEqualTo(UPDATED_OUVERT_YN);
        assertThat(testProgrammationInscription.getEmailUser()).isEqualTo(UPDATED_EMAIL_USER);
    }

    @Test
    @Transactional
    void patchNonExistingProgrammationInscription() throws Exception {
        int databaseSizeBeforeUpdate = programmationInscriptionRepository.findAll().size();
        programmationInscription.setId(longCount.incrementAndGet());

        // Create the ProgrammationInscription
        ProgrammationInscriptionDTO programmationInscriptionDTO = programmationInscriptionMapper.toDto(programmationInscription);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restProgrammationInscriptionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, programmationInscriptionDTO.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(programmationInscriptionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProgrammationInscription in the database
        List<ProgrammationInscription> programmationInscriptionList = programmationInscriptionRepository.findAll();
        assertThat(programmationInscriptionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchProgrammationInscription() throws Exception {
        int databaseSizeBeforeUpdate = programmationInscriptionRepository.findAll().size();
        programmationInscription.setId(longCount.incrementAndGet());

        // Create the ProgrammationInscription
        ProgrammationInscriptionDTO programmationInscriptionDTO = programmationInscriptionMapper.toDto(programmationInscription);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProgrammationInscriptionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(programmationInscriptionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProgrammationInscription in the database
        List<ProgrammationInscription> programmationInscriptionList = programmationInscriptionRepository.findAll();
        assertThat(programmationInscriptionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamProgrammationInscription() throws Exception {
        int databaseSizeBeforeUpdate = programmationInscriptionRepository.findAll().size();
        programmationInscription.setId(longCount.incrementAndGet());

        // Create the ProgrammationInscription
        ProgrammationInscriptionDTO programmationInscriptionDTO = programmationInscriptionMapper.toDto(programmationInscription);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProgrammationInscriptionMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(programmationInscriptionDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the ProgrammationInscription in the database
        List<ProgrammationInscription> programmationInscriptionList = programmationInscriptionRepository.findAll();
        assertThat(programmationInscriptionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteProgrammationInscription() throws Exception {
        // Initialize the database
        programmationInscriptionRepository.saveAndFlush(programmationInscription);

        int databaseSizeBeforeDelete = programmationInscriptionRepository.findAll().size();

        // Delete the programmationInscription
        restProgrammationInscriptionMockMvc
            .perform(delete(ENTITY_API_URL_ID, programmationInscription.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<ProgrammationInscription> programmationInscriptionList = programmationInscriptionRepository.findAll();
        assertThat(programmationInscriptionList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
