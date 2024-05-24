package sn.ugb.gir.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import jakarta.persistence.EntityManager;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
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
import sn.ugb.gir.domain.PaiementFormationPrivee;
import sn.ugb.gir.repository.PaiementFormationPriveeRepository;
import sn.ugb.gir.service.dto.PaiementFormationPriveeDTO;
import sn.ugb.gir.service.mapper.PaiementFormationPriveeMapper;

/**
 * Integration tests for the {@link PaiementFormationPriveeResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class PaiementFormationPriveeResourceIT {

    private static final Instant DEFAULT_DATE_PAIEMENT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_DATE_PAIEMENT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_MOIS_PAIEMENT = "AAAAAAAAAA";
    private static final String UPDATED_MOIS_PAIEMENT = "BBBBBBBBBB";

    private static final String DEFAULT_ANNEE_PAIEMENT = "AAAAAAAAAA";
    private static final String UPDATED_ANNEE_PAIEMENT = "BBBBBBBBBB";

    private static final Integer DEFAULT_PAYER_MENSUALITE_YN = 1;
    private static final Integer UPDATED_PAYER_MENSUALITE_YN = 2;

    private static final Integer DEFAULT_PAYER_DELAIS_YN = 1;
    private static final Integer UPDATED_PAYER_DELAIS_YN = 2;

    private static final String DEFAULT_EMAIL_USER = "AAAAAAAAAA";
    private static final String UPDATED_EMAIL_USER = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/paiement-formation-privees";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private PaiementFormationPriveeRepository paiementFormationPriveeRepository;

    @Autowired
    private PaiementFormationPriveeMapper paiementFormationPriveeMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restPaiementFormationPriveeMockMvc;

    private PaiementFormationPrivee paiementFormationPrivee;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PaiementFormationPrivee createEntity(EntityManager em) {
        PaiementFormationPrivee paiementFormationPrivee = new PaiementFormationPrivee()
            .datePaiement(DEFAULT_DATE_PAIEMENT)
            .moisPaiement(DEFAULT_MOIS_PAIEMENT)
            .anneePaiement(DEFAULT_ANNEE_PAIEMENT)
            .payerMensualiteYN(DEFAULT_PAYER_MENSUALITE_YN)
            .payerDelaisYN(DEFAULT_PAYER_DELAIS_YN)
            .emailUser(DEFAULT_EMAIL_USER);
        return paiementFormationPrivee;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PaiementFormationPrivee createUpdatedEntity(EntityManager em) {
        PaiementFormationPrivee paiementFormationPrivee = new PaiementFormationPrivee()
            .datePaiement(UPDATED_DATE_PAIEMENT)
            .moisPaiement(UPDATED_MOIS_PAIEMENT)
            .anneePaiement(UPDATED_ANNEE_PAIEMENT)
            .payerMensualiteYN(UPDATED_PAYER_MENSUALITE_YN)
            .payerDelaisYN(UPDATED_PAYER_DELAIS_YN)
            .emailUser(UPDATED_EMAIL_USER);
        return paiementFormationPrivee;
    }

    @BeforeEach
    public void initTest() {
        paiementFormationPrivee = createEntity(em);
    }

    @Test
    @Transactional
    void createPaiementFormationPrivee() throws Exception {
        int databaseSizeBeforeCreate = paiementFormationPriveeRepository.findAll().size();
        // Create the PaiementFormationPrivee
        PaiementFormationPriveeDTO paiementFormationPriveeDTO = paiementFormationPriveeMapper.toDto(paiementFormationPrivee);
        restPaiementFormationPriveeMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(paiementFormationPriveeDTO))
            )
            .andExpect(status().isCreated());

        // Validate the PaiementFormationPrivee in the database
        List<PaiementFormationPrivee> paiementFormationPriveeList = paiementFormationPriveeRepository.findAll();
        assertThat(paiementFormationPriveeList).hasSize(databaseSizeBeforeCreate + 1);
        PaiementFormationPrivee testPaiementFormationPrivee = paiementFormationPriveeList.get(paiementFormationPriveeList.size() - 1);
        assertThat(testPaiementFormationPrivee.getDatePaiement()).isEqualTo(DEFAULT_DATE_PAIEMENT);
        assertThat(testPaiementFormationPrivee.getMoisPaiement()).isEqualTo(DEFAULT_MOIS_PAIEMENT);
        assertThat(testPaiementFormationPrivee.getAnneePaiement()).isEqualTo(DEFAULT_ANNEE_PAIEMENT);
        assertThat(testPaiementFormationPrivee.getPayerMensualiteYN()).isEqualTo(DEFAULT_PAYER_MENSUALITE_YN);
        assertThat(testPaiementFormationPrivee.getPayerDelaisYN()).isEqualTo(DEFAULT_PAYER_DELAIS_YN);
        assertThat(testPaiementFormationPrivee.getEmailUser()).isEqualTo(DEFAULT_EMAIL_USER);
    }

    @Test
    @Transactional
    void createPaiementFormationPriveeWithExistingId() throws Exception {
        // Create the PaiementFormationPrivee with an existing ID
        paiementFormationPrivee.setId(1L);
        PaiementFormationPriveeDTO paiementFormationPriveeDTO = paiementFormationPriveeMapper.toDto(paiementFormationPrivee);

        int databaseSizeBeforeCreate = paiementFormationPriveeRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restPaiementFormationPriveeMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(paiementFormationPriveeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PaiementFormationPrivee in the database
        List<PaiementFormationPrivee> paiementFormationPriveeList = paiementFormationPriveeRepository.findAll();
        assertThat(paiementFormationPriveeList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllPaiementFormationPrivees() throws Exception {
        // Initialize the database
        paiementFormationPriveeRepository.saveAndFlush(paiementFormationPrivee);

        // Get all the paiementFormationPriveeList
        restPaiementFormationPriveeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(paiementFormationPrivee.getId().intValue())))
            .andExpect(jsonPath("$.[*].datePaiement").value(hasItem(DEFAULT_DATE_PAIEMENT.toString())))
            .andExpect(jsonPath("$.[*].moisPaiement").value(hasItem(DEFAULT_MOIS_PAIEMENT)))
            .andExpect(jsonPath("$.[*].anneePaiement").value(hasItem(DEFAULT_ANNEE_PAIEMENT)))
            .andExpect(jsonPath("$.[*].payerMensualiteYN").value(hasItem(DEFAULT_PAYER_MENSUALITE_YN)))
            .andExpect(jsonPath("$.[*].payerDelaisYN").value(hasItem(DEFAULT_PAYER_DELAIS_YN)))
            .andExpect(jsonPath("$.[*].emailUser").value(hasItem(DEFAULT_EMAIL_USER)));
    }

    @Test
    @Transactional
    void getPaiementFormationPrivee() throws Exception {
        // Initialize the database
        paiementFormationPriveeRepository.saveAndFlush(paiementFormationPrivee);

        // Get the paiementFormationPrivee
        restPaiementFormationPriveeMockMvc
            .perform(get(ENTITY_API_URL_ID, paiementFormationPrivee.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(paiementFormationPrivee.getId().intValue()))
            .andExpect(jsonPath("$.datePaiement").value(DEFAULT_DATE_PAIEMENT.toString()))
            .andExpect(jsonPath("$.moisPaiement").value(DEFAULT_MOIS_PAIEMENT))
            .andExpect(jsonPath("$.anneePaiement").value(DEFAULT_ANNEE_PAIEMENT))
            .andExpect(jsonPath("$.payerMensualiteYN").value(DEFAULT_PAYER_MENSUALITE_YN))
            .andExpect(jsonPath("$.payerDelaisYN").value(DEFAULT_PAYER_DELAIS_YN))
            .andExpect(jsonPath("$.emailUser").value(DEFAULT_EMAIL_USER));
    }

    @Test
    @Transactional
    void getNonExistingPaiementFormationPrivee() throws Exception {
        // Get the paiementFormationPrivee
        restPaiementFormationPriveeMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingPaiementFormationPrivee() throws Exception {
        // Initialize the database
        paiementFormationPriveeRepository.saveAndFlush(paiementFormationPrivee);

        int databaseSizeBeforeUpdate = paiementFormationPriveeRepository.findAll().size();

        // Update the paiementFormationPrivee
        PaiementFormationPrivee updatedPaiementFormationPrivee = paiementFormationPriveeRepository
            .findById(paiementFormationPrivee.getId())
            .orElseThrow();
        // Disconnect from session so that the updates on updatedPaiementFormationPrivee are not directly saved in db
        em.detach(updatedPaiementFormationPrivee);
        updatedPaiementFormationPrivee
            .datePaiement(UPDATED_DATE_PAIEMENT)
            .moisPaiement(UPDATED_MOIS_PAIEMENT)
            .anneePaiement(UPDATED_ANNEE_PAIEMENT)
            .payerMensualiteYN(UPDATED_PAYER_MENSUALITE_YN)
            .payerDelaisYN(UPDATED_PAYER_DELAIS_YN)
            .emailUser(UPDATED_EMAIL_USER);
        PaiementFormationPriveeDTO paiementFormationPriveeDTO = paiementFormationPriveeMapper.toDto(updatedPaiementFormationPrivee);

        restPaiementFormationPriveeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, paiementFormationPriveeDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(paiementFormationPriveeDTO))
            )
            .andExpect(status().isOk());

        // Validate the PaiementFormationPrivee in the database
        List<PaiementFormationPrivee> paiementFormationPriveeList = paiementFormationPriveeRepository.findAll();
        assertThat(paiementFormationPriveeList).hasSize(databaseSizeBeforeUpdate);
        PaiementFormationPrivee testPaiementFormationPrivee = paiementFormationPriveeList.get(paiementFormationPriveeList.size() - 1);
        assertThat(testPaiementFormationPrivee.getDatePaiement()).isEqualTo(UPDATED_DATE_PAIEMENT);
        assertThat(testPaiementFormationPrivee.getMoisPaiement()).isEqualTo(UPDATED_MOIS_PAIEMENT);
        assertThat(testPaiementFormationPrivee.getAnneePaiement()).isEqualTo(UPDATED_ANNEE_PAIEMENT);
        assertThat(testPaiementFormationPrivee.getPayerMensualiteYN()).isEqualTo(UPDATED_PAYER_MENSUALITE_YN);
        assertThat(testPaiementFormationPrivee.getPayerDelaisYN()).isEqualTo(UPDATED_PAYER_DELAIS_YN);
        assertThat(testPaiementFormationPrivee.getEmailUser()).isEqualTo(UPDATED_EMAIL_USER);
    }

    @Test
    @Transactional
    void putNonExistingPaiementFormationPrivee() throws Exception {
        int databaseSizeBeforeUpdate = paiementFormationPriveeRepository.findAll().size();
        paiementFormationPrivee.setId(longCount.incrementAndGet());

        // Create the PaiementFormationPrivee
        PaiementFormationPriveeDTO paiementFormationPriveeDTO = paiementFormationPriveeMapper.toDto(paiementFormationPrivee);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPaiementFormationPriveeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, paiementFormationPriveeDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(paiementFormationPriveeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PaiementFormationPrivee in the database
        List<PaiementFormationPrivee> paiementFormationPriveeList = paiementFormationPriveeRepository.findAll();
        assertThat(paiementFormationPriveeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchPaiementFormationPrivee() throws Exception {
        int databaseSizeBeforeUpdate = paiementFormationPriveeRepository.findAll().size();
        paiementFormationPrivee.setId(longCount.incrementAndGet());

        // Create the PaiementFormationPrivee
        PaiementFormationPriveeDTO paiementFormationPriveeDTO = paiementFormationPriveeMapper.toDto(paiementFormationPrivee);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPaiementFormationPriveeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(paiementFormationPriveeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PaiementFormationPrivee in the database
        List<PaiementFormationPrivee> paiementFormationPriveeList = paiementFormationPriveeRepository.findAll();
        assertThat(paiementFormationPriveeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamPaiementFormationPrivee() throws Exception {
        int databaseSizeBeforeUpdate = paiementFormationPriveeRepository.findAll().size();
        paiementFormationPrivee.setId(longCount.incrementAndGet());

        // Create the PaiementFormationPrivee
        PaiementFormationPriveeDTO paiementFormationPriveeDTO = paiementFormationPriveeMapper.toDto(paiementFormationPrivee);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPaiementFormationPriveeMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(paiementFormationPriveeDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the PaiementFormationPrivee in the database
        List<PaiementFormationPrivee> paiementFormationPriveeList = paiementFormationPriveeRepository.findAll();
        assertThat(paiementFormationPriveeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdatePaiementFormationPriveeWithPatch() throws Exception {
        // Initialize the database
        paiementFormationPriveeRepository.saveAndFlush(paiementFormationPrivee);

        int databaseSizeBeforeUpdate = paiementFormationPriveeRepository.findAll().size();

        // Update the paiementFormationPrivee using partial update
        PaiementFormationPrivee partialUpdatedPaiementFormationPrivee = new PaiementFormationPrivee();
        partialUpdatedPaiementFormationPrivee.setId(paiementFormationPrivee.getId());

        partialUpdatedPaiementFormationPrivee
            .moisPaiement(UPDATED_MOIS_PAIEMENT)
            .anneePaiement(UPDATED_ANNEE_PAIEMENT)
            .payerMensualiteYN(UPDATED_PAYER_MENSUALITE_YN)
            .payerDelaisYN(UPDATED_PAYER_DELAIS_YN);

        restPaiementFormationPriveeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPaiementFormationPrivee.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPaiementFormationPrivee))
            )
            .andExpect(status().isOk());

        // Validate the PaiementFormationPrivee in the database
        List<PaiementFormationPrivee> paiementFormationPriveeList = paiementFormationPriveeRepository.findAll();
        assertThat(paiementFormationPriveeList).hasSize(databaseSizeBeforeUpdate);
        PaiementFormationPrivee testPaiementFormationPrivee = paiementFormationPriveeList.get(paiementFormationPriveeList.size() - 1);
        assertThat(testPaiementFormationPrivee.getDatePaiement()).isEqualTo(DEFAULT_DATE_PAIEMENT);
        assertThat(testPaiementFormationPrivee.getMoisPaiement()).isEqualTo(UPDATED_MOIS_PAIEMENT);
        assertThat(testPaiementFormationPrivee.getAnneePaiement()).isEqualTo(UPDATED_ANNEE_PAIEMENT);
        assertThat(testPaiementFormationPrivee.getPayerMensualiteYN()).isEqualTo(UPDATED_PAYER_MENSUALITE_YN);
        assertThat(testPaiementFormationPrivee.getPayerDelaisYN()).isEqualTo(UPDATED_PAYER_DELAIS_YN);
        assertThat(testPaiementFormationPrivee.getEmailUser()).isEqualTo(DEFAULT_EMAIL_USER);
    }

    @Test
    @Transactional
    void fullUpdatePaiementFormationPriveeWithPatch() throws Exception {
        // Initialize the database
        paiementFormationPriveeRepository.saveAndFlush(paiementFormationPrivee);

        int databaseSizeBeforeUpdate = paiementFormationPriveeRepository.findAll().size();

        // Update the paiementFormationPrivee using partial update
        PaiementFormationPrivee partialUpdatedPaiementFormationPrivee = new PaiementFormationPrivee();
        partialUpdatedPaiementFormationPrivee.setId(paiementFormationPrivee.getId());

        partialUpdatedPaiementFormationPrivee
            .datePaiement(UPDATED_DATE_PAIEMENT)
            .moisPaiement(UPDATED_MOIS_PAIEMENT)
            .anneePaiement(UPDATED_ANNEE_PAIEMENT)
            .payerMensualiteYN(UPDATED_PAYER_MENSUALITE_YN)
            .payerDelaisYN(UPDATED_PAYER_DELAIS_YN)
            .emailUser(UPDATED_EMAIL_USER);

        restPaiementFormationPriveeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPaiementFormationPrivee.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPaiementFormationPrivee))
            )
            .andExpect(status().isOk());

        // Validate the PaiementFormationPrivee in the database
        List<PaiementFormationPrivee> paiementFormationPriveeList = paiementFormationPriveeRepository.findAll();
        assertThat(paiementFormationPriveeList).hasSize(databaseSizeBeforeUpdate);
        PaiementFormationPrivee testPaiementFormationPrivee = paiementFormationPriveeList.get(paiementFormationPriveeList.size() - 1);
        assertThat(testPaiementFormationPrivee.getDatePaiement()).isEqualTo(UPDATED_DATE_PAIEMENT);
        assertThat(testPaiementFormationPrivee.getMoisPaiement()).isEqualTo(UPDATED_MOIS_PAIEMENT);
        assertThat(testPaiementFormationPrivee.getAnneePaiement()).isEqualTo(UPDATED_ANNEE_PAIEMENT);
        assertThat(testPaiementFormationPrivee.getPayerMensualiteYN()).isEqualTo(UPDATED_PAYER_MENSUALITE_YN);
        assertThat(testPaiementFormationPrivee.getPayerDelaisYN()).isEqualTo(UPDATED_PAYER_DELAIS_YN);
        assertThat(testPaiementFormationPrivee.getEmailUser()).isEqualTo(UPDATED_EMAIL_USER);
    }

    @Test
    @Transactional
    void patchNonExistingPaiementFormationPrivee() throws Exception {
        int databaseSizeBeforeUpdate = paiementFormationPriveeRepository.findAll().size();
        paiementFormationPrivee.setId(longCount.incrementAndGet());

        // Create the PaiementFormationPrivee
        PaiementFormationPriveeDTO paiementFormationPriveeDTO = paiementFormationPriveeMapper.toDto(paiementFormationPrivee);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPaiementFormationPriveeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, paiementFormationPriveeDTO.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(paiementFormationPriveeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PaiementFormationPrivee in the database
        List<PaiementFormationPrivee> paiementFormationPriveeList = paiementFormationPriveeRepository.findAll();
        assertThat(paiementFormationPriveeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchPaiementFormationPrivee() throws Exception {
        int databaseSizeBeforeUpdate = paiementFormationPriveeRepository.findAll().size();
        paiementFormationPrivee.setId(longCount.incrementAndGet());

        // Create the PaiementFormationPrivee
        PaiementFormationPriveeDTO paiementFormationPriveeDTO = paiementFormationPriveeMapper.toDto(paiementFormationPrivee);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPaiementFormationPriveeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(paiementFormationPriveeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PaiementFormationPrivee in the database
        List<PaiementFormationPrivee> paiementFormationPriveeList = paiementFormationPriveeRepository.findAll();
        assertThat(paiementFormationPriveeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamPaiementFormationPrivee() throws Exception {
        int databaseSizeBeforeUpdate = paiementFormationPriveeRepository.findAll().size();
        paiementFormationPrivee.setId(longCount.incrementAndGet());

        // Create the PaiementFormationPrivee
        PaiementFormationPriveeDTO paiementFormationPriveeDTO = paiementFormationPriveeMapper.toDto(paiementFormationPrivee);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPaiementFormationPriveeMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(paiementFormationPriveeDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the PaiementFormationPrivee in the database
        List<PaiementFormationPrivee> paiementFormationPriveeList = paiementFormationPriveeRepository.findAll();
        assertThat(paiementFormationPriveeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deletePaiementFormationPrivee() throws Exception {
        // Initialize the database
        paiementFormationPriveeRepository.saveAndFlush(paiementFormationPrivee);

        int databaseSizeBeforeDelete = paiementFormationPriveeRepository.findAll().size();

        // Delete the paiementFormationPrivee
        restPaiementFormationPriveeMockMvc
            .perform(delete(ENTITY_API_URL_ID, paiementFormationPrivee.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<PaiementFormationPrivee> paiementFormationPriveeList = paiementFormationPriveeRepository.findAll();
        assertThat(paiementFormationPriveeList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
