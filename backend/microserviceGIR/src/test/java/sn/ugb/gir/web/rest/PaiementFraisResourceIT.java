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
import sn.ugb.gir.domain.PaiementFrais;
import sn.ugb.gir.repository.PaiementFraisRepository;
import sn.ugb.gir.service.dto.PaiementFraisDTO;
import sn.ugb.gir.service.mapper.PaiementFraisMapper;

/**
 * Integration tests for the {@link PaiementFraisResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class PaiementFraisResourceIT {

    private static final LocalDate DEFAULT_DATE_PAIEMENT = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE_PAIEMENT = LocalDate.now(ZoneId.systemDefault());

    private static final Integer DEFAULT_OBLIGATOIRE_YN = 1;
    private static final Integer UPDATED_OBLIGATOIRE_YN = 2;

    private static final Integer DEFAULT_ECHEANCE_PAYEE_YN = 1;
    private static final Integer UPDATED_ECHEANCE_PAYEE_YN = 2;

    private static final String DEFAULT_EMAIL_USER = "AAAAAAAAAA";
    private static final String UPDATED_EMAIL_USER = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/paiement-frais";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private PaiementFraisRepository paiementFraisRepository;

    @Autowired
    private PaiementFraisMapper paiementFraisMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restPaiementFraisMockMvc;

    private PaiementFrais paiementFrais;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PaiementFrais createEntity(EntityManager em) {
        PaiementFrais paiementFrais = new PaiementFrais()
            .datePaiement(DEFAULT_DATE_PAIEMENT)
            .obligatoireYN(DEFAULT_OBLIGATOIRE_YN)
            .echeancePayeeYN(DEFAULT_ECHEANCE_PAYEE_YN)
            .emailUser(DEFAULT_EMAIL_USER);
        return paiementFrais;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PaiementFrais createUpdatedEntity(EntityManager em) {
        PaiementFrais paiementFrais = new PaiementFrais()
            .datePaiement(UPDATED_DATE_PAIEMENT)
            .obligatoireYN(UPDATED_OBLIGATOIRE_YN)
            .echeancePayeeYN(UPDATED_ECHEANCE_PAYEE_YN)
            .emailUser(UPDATED_EMAIL_USER);
        return paiementFrais;
    }

    @BeforeEach
    public void initTest() {
        paiementFrais = createEntity(em);
    }

    @Test
    @Transactional
    void createPaiementFrais() throws Exception {
        int databaseSizeBeforeCreate = paiementFraisRepository.findAll().size();
        // Create the PaiementFrais
        PaiementFraisDTO paiementFraisDTO = paiementFraisMapper.toDto(paiementFrais);
        restPaiementFraisMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(paiementFraisDTO))
            )
            .andExpect(status().isCreated());

        // Validate the PaiementFrais in the database
        List<PaiementFrais> paiementFraisList = paiementFraisRepository.findAll();
        assertThat(paiementFraisList).hasSize(databaseSizeBeforeCreate + 1);
        PaiementFrais testPaiementFrais = paiementFraisList.get(paiementFraisList.size() - 1);
        assertThat(testPaiementFrais.getDatePaiement()).isEqualTo(DEFAULT_DATE_PAIEMENT);
        assertThat(testPaiementFrais.getObligatoireYN()).isEqualTo(DEFAULT_OBLIGATOIRE_YN);
        assertThat(testPaiementFrais.getEcheancePayeeYN()).isEqualTo(DEFAULT_ECHEANCE_PAYEE_YN);
        assertThat(testPaiementFrais.getEmailUser()).isEqualTo(DEFAULT_EMAIL_USER);
    }

    @Test
    @Transactional
    void createPaiementFraisWithExistingId() throws Exception {
        // Create the PaiementFrais with an existing ID
        paiementFrais.setId(1L);
        PaiementFraisDTO paiementFraisDTO = paiementFraisMapper.toDto(paiementFrais);

        int databaseSizeBeforeCreate = paiementFraisRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restPaiementFraisMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(paiementFraisDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PaiementFrais in the database
        List<PaiementFrais> paiementFraisList = paiementFraisRepository.findAll();
        assertThat(paiementFraisList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllPaiementFrais() throws Exception {
        // Initialize the database
        paiementFraisRepository.saveAndFlush(paiementFrais);

        // Get all the paiementFraisList
        restPaiementFraisMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(paiementFrais.getId().intValue())))
            .andExpect(jsonPath("$.[*].datePaiement").value(hasItem(DEFAULT_DATE_PAIEMENT.toString())))
            .andExpect(jsonPath("$.[*].obligatoireYN").value(hasItem(DEFAULT_OBLIGATOIRE_YN)))
            .andExpect(jsonPath("$.[*].echeancePayeeYN").value(hasItem(DEFAULT_ECHEANCE_PAYEE_YN)))
            .andExpect(jsonPath("$.[*].emailUser").value(hasItem(DEFAULT_EMAIL_USER)));
    }

    @Test
    @Transactional
    void getPaiementFrais() throws Exception {
        // Initialize the database
        paiementFraisRepository.saveAndFlush(paiementFrais);

        // Get the paiementFrais
        restPaiementFraisMockMvc
            .perform(get(ENTITY_API_URL_ID, paiementFrais.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(paiementFrais.getId().intValue()))
            .andExpect(jsonPath("$.datePaiement").value(DEFAULT_DATE_PAIEMENT.toString()))
            .andExpect(jsonPath("$.obligatoireYN").value(DEFAULT_OBLIGATOIRE_YN))
            .andExpect(jsonPath("$.echeancePayeeYN").value(DEFAULT_ECHEANCE_PAYEE_YN))
            .andExpect(jsonPath("$.emailUser").value(DEFAULT_EMAIL_USER));
    }

    @Test
    @Transactional
    void getNonExistingPaiementFrais() throws Exception {
        // Get the paiementFrais
        restPaiementFraisMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingPaiementFrais() throws Exception {
        // Initialize the database
        paiementFraisRepository.saveAndFlush(paiementFrais);

        int databaseSizeBeforeUpdate = paiementFraisRepository.findAll().size();

        // Update the paiementFrais
        PaiementFrais updatedPaiementFrais = paiementFraisRepository.findById(paiementFrais.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedPaiementFrais are not directly saved in db
        em.detach(updatedPaiementFrais);
        updatedPaiementFrais
            .datePaiement(UPDATED_DATE_PAIEMENT)
            .obligatoireYN(UPDATED_OBLIGATOIRE_YN)
            .echeancePayeeYN(UPDATED_ECHEANCE_PAYEE_YN)
            .emailUser(UPDATED_EMAIL_USER);
        PaiementFraisDTO paiementFraisDTO = paiementFraisMapper.toDto(updatedPaiementFrais);

        restPaiementFraisMockMvc
            .perform(
                put(ENTITY_API_URL_ID, paiementFraisDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(paiementFraisDTO))
            )
            .andExpect(status().isOk());

        // Validate the PaiementFrais in the database
        List<PaiementFrais> paiementFraisList = paiementFraisRepository.findAll();
        assertThat(paiementFraisList).hasSize(databaseSizeBeforeUpdate);
        PaiementFrais testPaiementFrais = paiementFraisList.get(paiementFraisList.size() - 1);
        assertThat(testPaiementFrais.getDatePaiement()).isEqualTo(UPDATED_DATE_PAIEMENT);
        assertThat(testPaiementFrais.getObligatoireYN()).isEqualTo(UPDATED_OBLIGATOIRE_YN);
        assertThat(testPaiementFrais.getEcheancePayeeYN()).isEqualTo(UPDATED_ECHEANCE_PAYEE_YN);
        assertThat(testPaiementFrais.getEmailUser()).isEqualTo(UPDATED_EMAIL_USER);
    }

    @Test
    @Transactional
    void putNonExistingPaiementFrais() throws Exception {
        int databaseSizeBeforeUpdate = paiementFraisRepository.findAll().size();
        paiementFrais.setId(longCount.incrementAndGet());

        // Create the PaiementFrais
        PaiementFraisDTO paiementFraisDTO = paiementFraisMapper.toDto(paiementFrais);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPaiementFraisMockMvc
            .perform(
                put(ENTITY_API_URL_ID, paiementFraisDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(paiementFraisDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PaiementFrais in the database
        List<PaiementFrais> paiementFraisList = paiementFraisRepository.findAll();
        assertThat(paiementFraisList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchPaiementFrais() throws Exception {
        int databaseSizeBeforeUpdate = paiementFraisRepository.findAll().size();
        paiementFrais.setId(longCount.incrementAndGet());

        // Create the PaiementFrais
        PaiementFraisDTO paiementFraisDTO = paiementFraisMapper.toDto(paiementFrais);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPaiementFraisMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(paiementFraisDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PaiementFrais in the database
        List<PaiementFrais> paiementFraisList = paiementFraisRepository.findAll();
        assertThat(paiementFraisList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamPaiementFrais() throws Exception {
        int databaseSizeBeforeUpdate = paiementFraisRepository.findAll().size();
        paiementFrais.setId(longCount.incrementAndGet());

        // Create the PaiementFrais
        PaiementFraisDTO paiementFraisDTO = paiementFraisMapper.toDto(paiementFrais);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPaiementFraisMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(paiementFraisDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the PaiementFrais in the database
        List<PaiementFrais> paiementFraisList = paiementFraisRepository.findAll();
        assertThat(paiementFraisList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdatePaiementFraisWithPatch() throws Exception {
        // Initialize the database
        paiementFraisRepository.saveAndFlush(paiementFrais);

        int databaseSizeBeforeUpdate = paiementFraisRepository.findAll().size();

        // Update the paiementFrais using partial update
        PaiementFrais partialUpdatedPaiementFrais = new PaiementFrais();
        partialUpdatedPaiementFrais.setId(paiementFrais.getId());

        partialUpdatedPaiementFrais.datePaiement(UPDATED_DATE_PAIEMENT);

        restPaiementFraisMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPaiementFrais.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPaiementFrais))
            )
            .andExpect(status().isOk());

        // Validate the PaiementFrais in the database
        List<PaiementFrais> paiementFraisList = paiementFraisRepository.findAll();
        assertThat(paiementFraisList).hasSize(databaseSizeBeforeUpdate);
        PaiementFrais testPaiementFrais = paiementFraisList.get(paiementFraisList.size() - 1);
        assertThat(testPaiementFrais.getDatePaiement()).isEqualTo(UPDATED_DATE_PAIEMENT);
        assertThat(testPaiementFrais.getObligatoireYN()).isEqualTo(DEFAULT_OBLIGATOIRE_YN);
        assertThat(testPaiementFrais.getEcheancePayeeYN()).isEqualTo(DEFAULT_ECHEANCE_PAYEE_YN);
        assertThat(testPaiementFrais.getEmailUser()).isEqualTo(DEFAULT_EMAIL_USER);
    }

    @Test
    @Transactional
    void fullUpdatePaiementFraisWithPatch() throws Exception {
        // Initialize the database
        paiementFraisRepository.saveAndFlush(paiementFrais);

        int databaseSizeBeforeUpdate = paiementFraisRepository.findAll().size();

        // Update the paiementFrais using partial update
        PaiementFrais partialUpdatedPaiementFrais = new PaiementFrais();
        partialUpdatedPaiementFrais.setId(paiementFrais.getId());

        partialUpdatedPaiementFrais
            .datePaiement(UPDATED_DATE_PAIEMENT)
            .obligatoireYN(UPDATED_OBLIGATOIRE_YN)
            .echeancePayeeYN(UPDATED_ECHEANCE_PAYEE_YN)
            .emailUser(UPDATED_EMAIL_USER);

        restPaiementFraisMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPaiementFrais.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPaiementFrais))
            )
            .andExpect(status().isOk());

        // Validate the PaiementFrais in the database
        List<PaiementFrais> paiementFraisList = paiementFraisRepository.findAll();
        assertThat(paiementFraisList).hasSize(databaseSizeBeforeUpdate);
        PaiementFrais testPaiementFrais = paiementFraisList.get(paiementFraisList.size() - 1);
        assertThat(testPaiementFrais.getDatePaiement()).isEqualTo(UPDATED_DATE_PAIEMENT);
        assertThat(testPaiementFrais.getObligatoireYN()).isEqualTo(UPDATED_OBLIGATOIRE_YN);
        assertThat(testPaiementFrais.getEcheancePayeeYN()).isEqualTo(UPDATED_ECHEANCE_PAYEE_YN);
        assertThat(testPaiementFrais.getEmailUser()).isEqualTo(UPDATED_EMAIL_USER);
    }

    @Test
    @Transactional
    void patchNonExistingPaiementFrais() throws Exception {
        int databaseSizeBeforeUpdate = paiementFraisRepository.findAll().size();
        paiementFrais.setId(longCount.incrementAndGet());

        // Create the PaiementFrais
        PaiementFraisDTO paiementFraisDTO = paiementFraisMapper.toDto(paiementFrais);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPaiementFraisMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, paiementFraisDTO.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(paiementFraisDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PaiementFrais in the database
        List<PaiementFrais> paiementFraisList = paiementFraisRepository.findAll();
        assertThat(paiementFraisList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchPaiementFrais() throws Exception {
        int databaseSizeBeforeUpdate = paiementFraisRepository.findAll().size();
        paiementFrais.setId(longCount.incrementAndGet());

        // Create the PaiementFrais
        PaiementFraisDTO paiementFraisDTO = paiementFraisMapper.toDto(paiementFrais);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPaiementFraisMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(paiementFraisDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PaiementFrais in the database
        List<PaiementFrais> paiementFraisList = paiementFraisRepository.findAll();
        assertThat(paiementFraisList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamPaiementFrais() throws Exception {
        int databaseSizeBeforeUpdate = paiementFraisRepository.findAll().size();
        paiementFrais.setId(longCount.incrementAndGet());

        // Create the PaiementFrais
        PaiementFraisDTO paiementFraisDTO = paiementFraisMapper.toDto(paiementFrais);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPaiementFraisMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(paiementFraisDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the PaiementFrais in the database
        List<PaiementFrais> paiementFraisList = paiementFraisRepository.findAll();
        assertThat(paiementFraisList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deletePaiementFrais() throws Exception {
        // Initialize the database
        paiementFraisRepository.saveAndFlush(paiementFrais);

        int databaseSizeBeforeDelete = paiementFraisRepository.findAll().size();

        // Delete the paiementFrais
        restPaiementFraisMockMvc
            .perform(delete(ENTITY_API_URL_ID, paiementFrais.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<PaiementFrais> paiementFraisList = paiementFraisRepository.findAll();
        assertThat(paiementFraisList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
