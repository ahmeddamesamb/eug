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
import sn.ugb.gir.domain.InscriptionAdministrativeFormation;
import sn.ugb.gir.repository.InscriptionAdministrativeFormationRepository;
import sn.ugb.gir.service.dto.InscriptionAdministrativeFormationDTO;
import sn.ugb.gir.service.mapper.InscriptionAdministrativeFormationMapper;

/**
 * Integration tests for the {@link InscriptionAdministrativeFormationResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class InscriptionAdministrativeFormationResourceIT {

    private static final Integer DEFAULT_INSCRIPTION_PRINCIPALE_YN = 1;
    private static final Integer UPDATED_INSCRIPTION_PRINCIPALE_YN = 2;

    private static final Integer DEFAULT_INSCRIPTION_ANNULEE_YN = 1;
    private static final Integer UPDATED_INSCRIPTION_ANNULEE_YN = 2;

    private static final Integer DEFAULT_PAIEMENT_FRAIS_OBL_YN = 1;
    private static final Integer UPDATED_PAIEMENT_FRAIS_OBL_YN = 2;

    private static final Integer DEFAULT_PAIEMENT_FRAIS_INTEGERG_YN = 1;
    private static final Integer UPDATED_PAIEMENT_FRAIS_INTEGERG_YN = 2;

    private static final Integer DEFAULT_CERTIFICAT_DELIVRE_YN = 1;
    private static final Integer UPDATED_CERTIFICAT_DELIVRE_YN = 2;

    private static final Instant DEFAULT_DATE_CHOIX_FORMATION = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_DATE_CHOIX_FORMATION = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_DATE_VALIDATION_INSCRIPTION = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_DATE_VALIDATION_INSCRIPTION = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/inscription-administrative-formations";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private InscriptionAdministrativeFormationRepository inscriptionAdministrativeFormationRepository;

    @Autowired
    private InscriptionAdministrativeFormationMapper inscriptionAdministrativeFormationMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restInscriptionAdministrativeFormationMockMvc;

    private InscriptionAdministrativeFormation inscriptionAdministrativeFormation;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static InscriptionAdministrativeFormation createEntity(EntityManager em) {
        InscriptionAdministrativeFormation inscriptionAdministrativeFormation = new InscriptionAdministrativeFormation()
            .inscriptionPrincipaleYN(DEFAULT_INSCRIPTION_PRINCIPALE_YN)
            .inscriptionAnnuleeYN(DEFAULT_INSCRIPTION_ANNULEE_YN)
            .paiementFraisOblYN(DEFAULT_PAIEMENT_FRAIS_OBL_YN)
            .paiementFraisIntegergYN(DEFAULT_PAIEMENT_FRAIS_INTEGERG_YN)
            .certificatDelivreYN(DEFAULT_CERTIFICAT_DELIVRE_YN)
            .dateChoixFormation(DEFAULT_DATE_CHOIX_FORMATION)
            .dateValidationInscription(DEFAULT_DATE_VALIDATION_INSCRIPTION);
        return inscriptionAdministrativeFormation;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static InscriptionAdministrativeFormation createUpdatedEntity(EntityManager em) {
        InscriptionAdministrativeFormation inscriptionAdministrativeFormation = new InscriptionAdministrativeFormation()
            .inscriptionPrincipaleYN(UPDATED_INSCRIPTION_PRINCIPALE_YN)
            .inscriptionAnnuleeYN(UPDATED_INSCRIPTION_ANNULEE_YN)
            .paiementFraisOblYN(UPDATED_PAIEMENT_FRAIS_OBL_YN)
            .paiementFraisIntegergYN(UPDATED_PAIEMENT_FRAIS_INTEGERG_YN)
            .certificatDelivreYN(UPDATED_CERTIFICAT_DELIVRE_YN)
            .dateChoixFormation(UPDATED_DATE_CHOIX_FORMATION)
            .dateValidationInscription(UPDATED_DATE_VALIDATION_INSCRIPTION);
        return inscriptionAdministrativeFormation;
    }

    @BeforeEach
    public void initTest() {
        inscriptionAdministrativeFormation = createEntity(em);
    }

    @Test
    @Transactional
    void createInscriptionAdministrativeFormation() throws Exception {
        int databaseSizeBeforeCreate = inscriptionAdministrativeFormationRepository.findAll().size();
        // Create the InscriptionAdministrativeFormation
        InscriptionAdministrativeFormationDTO inscriptionAdministrativeFormationDTO = inscriptionAdministrativeFormationMapper.toDto(
            inscriptionAdministrativeFormation
        );
        restInscriptionAdministrativeFormationMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(inscriptionAdministrativeFormationDTO))
            )
            .andExpect(status().isCreated());

        // Validate the InscriptionAdministrativeFormation in the database
        List<InscriptionAdministrativeFormation> inscriptionAdministrativeFormationList =
            inscriptionAdministrativeFormationRepository.findAll();
        assertThat(inscriptionAdministrativeFormationList).hasSize(databaseSizeBeforeCreate + 1);
        InscriptionAdministrativeFormation testInscriptionAdministrativeFormation = inscriptionAdministrativeFormationList.get(
            inscriptionAdministrativeFormationList.size() - 1
        );
        assertThat(testInscriptionAdministrativeFormation.getInscriptionPrincipaleYN()).isEqualTo(DEFAULT_INSCRIPTION_PRINCIPALE_YN);
        assertThat(testInscriptionAdministrativeFormation.getInscriptionAnnuleeYN()).isEqualTo(DEFAULT_INSCRIPTION_ANNULEE_YN);
        assertThat(testInscriptionAdministrativeFormation.getPaiementFraisOblYN()).isEqualTo(DEFAULT_PAIEMENT_FRAIS_OBL_YN);
        assertThat(testInscriptionAdministrativeFormation.getPaiementFraisIntegergYN()).isEqualTo(DEFAULT_PAIEMENT_FRAIS_INTEGERG_YN);
        assertThat(testInscriptionAdministrativeFormation.getCertificatDelivreYN()).isEqualTo(DEFAULT_CERTIFICAT_DELIVRE_YN);
        assertThat(testInscriptionAdministrativeFormation.getDateChoixFormation()).isEqualTo(DEFAULT_DATE_CHOIX_FORMATION);
        assertThat(testInscriptionAdministrativeFormation.getDateValidationInscription()).isEqualTo(DEFAULT_DATE_VALIDATION_INSCRIPTION);
    }

    @Test
    @Transactional
    void createInscriptionAdministrativeFormationWithExistingId() throws Exception {
        // Create the InscriptionAdministrativeFormation with an existing ID
        inscriptionAdministrativeFormation.setId(1L);
        InscriptionAdministrativeFormationDTO inscriptionAdministrativeFormationDTO = inscriptionAdministrativeFormationMapper.toDto(
            inscriptionAdministrativeFormation
        );

        int databaseSizeBeforeCreate = inscriptionAdministrativeFormationRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restInscriptionAdministrativeFormationMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(inscriptionAdministrativeFormationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the InscriptionAdministrativeFormation in the database
        List<InscriptionAdministrativeFormation> inscriptionAdministrativeFormationList =
            inscriptionAdministrativeFormationRepository.findAll();
        assertThat(inscriptionAdministrativeFormationList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllInscriptionAdministrativeFormations() throws Exception {
        // Initialize the database
        inscriptionAdministrativeFormationRepository.saveAndFlush(inscriptionAdministrativeFormation);

        // Get all the inscriptionAdministrativeFormationList
        restInscriptionAdministrativeFormationMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(inscriptionAdministrativeFormation.getId().intValue())))
            .andExpect(jsonPath("$.[*].inscriptionPrincipaleYN").value(hasItem(DEFAULT_INSCRIPTION_PRINCIPALE_YN)))
            .andExpect(jsonPath("$.[*].inscriptionAnnuleeYN").value(hasItem(DEFAULT_INSCRIPTION_ANNULEE_YN)))
            .andExpect(jsonPath("$.[*].paiementFraisOblYN").value(hasItem(DEFAULT_PAIEMENT_FRAIS_OBL_YN)))
            .andExpect(jsonPath("$.[*].paiementFraisIntegergYN").value(hasItem(DEFAULT_PAIEMENT_FRAIS_INTEGERG_YN)))
            .andExpect(jsonPath("$.[*].certificatDelivreYN").value(hasItem(DEFAULT_CERTIFICAT_DELIVRE_YN)))
            .andExpect(jsonPath("$.[*].dateChoixFormation").value(hasItem(DEFAULT_DATE_CHOIX_FORMATION.toString())))
            .andExpect(jsonPath("$.[*].dateValidationInscription").value(hasItem(DEFAULT_DATE_VALIDATION_INSCRIPTION.toString())));
    }

    @Test
    @Transactional
    void getInscriptionAdministrativeFormation() throws Exception {
        // Initialize the database
        inscriptionAdministrativeFormationRepository.saveAndFlush(inscriptionAdministrativeFormation);

        // Get the inscriptionAdministrativeFormation
        restInscriptionAdministrativeFormationMockMvc
            .perform(get(ENTITY_API_URL_ID, inscriptionAdministrativeFormation.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(inscriptionAdministrativeFormation.getId().intValue()))
            .andExpect(jsonPath("$.inscriptionPrincipaleYN").value(DEFAULT_INSCRIPTION_PRINCIPALE_YN))
            .andExpect(jsonPath("$.inscriptionAnnuleeYN").value(DEFAULT_INSCRIPTION_ANNULEE_YN))
            .andExpect(jsonPath("$.paiementFraisOblYN").value(DEFAULT_PAIEMENT_FRAIS_OBL_YN))
            .andExpect(jsonPath("$.paiementFraisIntegergYN").value(DEFAULT_PAIEMENT_FRAIS_INTEGERG_YN))
            .andExpect(jsonPath("$.certificatDelivreYN").value(DEFAULT_CERTIFICAT_DELIVRE_YN))
            .andExpect(jsonPath("$.dateChoixFormation").value(DEFAULT_DATE_CHOIX_FORMATION.toString()))
            .andExpect(jsonPath("$.dateValidationInscription").value(DEFAULT_DATE_VALIDATION_INSCRIPTION.toString()));
    }

    @Test
    @Transactional
    void getNonExistingInscriptionAdministrativeFormation() throws Exception {
        // Get the inscriptionAdministrativeFormation
        restInscriptionAdministrativeFormationMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingInscriptionAdministrativeFormation() throws Exception {
        // Initialize the database
        inscriptionAdministrativeFormationRepository.saveAndFlush(inscriptionAdministrativeFormation);

        int databaseSizeBeforeUpdate = inscriptionAdministrativeFormationRepository.findAll().size();

        // Update the inscriptionAdministrativeFormation
        InscriptionAdministrativeFormation updatedInscriptionAdministrativeFormation = inscriptionAdministrativeFormationRepository
            .findById(inscriptionAdministrativeFormation.getId())
            .orElseThrow();
        // Disconnect from session so that the updates on updatedInscriptionAdministrativeFormation are not directly saved in db
        em.detach(updatedInscriptionAdministrativeFormation);
        updatedInscriptionAdministrativeFormation
            .inscriptionPrincipaleYN(UPDATED_INSCRIPTION_PRINCIPALE_YN)
            .inscriptionAnnuleeYN(UPDATED_INSCRIPTION_ANNULEE_YN)
            .paiementFraisOblYN(UPDATED_PAIEMENT_FRAIS_OBL_YN)
            .paiementFraisIntegergYN(UPDATED_PAIEMENT_FRAIS_INTEGERG_YN)
            .certificatDelivreYN(UPDATED_CERTIFICAT_DELIVRE_YN)
            .dateChoixFormation(UPDATED_DATE_CHOIX_FORMATION)
            .dateValidationInscription(UPDATED_DATE_VALIDATION_INSCRIPTION);
        InscriptionAdministrativeFormationDTO inscriptionAdministrativeFormationDTO = inscriptionAdministrativeFormationMapper.toDto(
            updatedInscriptionAdministrativeFormation
        );

        restInscriptionAdministrativeFormationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, inscriptionAdministrativeFormationDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(inscriptionAdministrativeFormationDTO))
            )
            .andExpect(status().isOk());

        // Validate the InscriptionAdministrativeFormation in the database
        List<InscriptionAdministrativeFormation> inscriptionAdministrativeFormationList =
            inscriptionAdministrativeFormationRepository.findAll();
        assertThat(inscriptionAdministrativeFormationList).hasSize(databaseSizeBeforeUpdate);
        InscriptionAdministrativeFormation testInscriptionAdministrativeFormation = inscriptionAdministrativeFormationList.get(
            inscriptionAdministrativeFormationList.size() - 1
        );
        assertThat(testInscriptionAdministrativeFormation.getInscriptionPrincipaleYN()).isEqualTo(UPDATED_INSCRIPTION_PRINCIPALE_YN);
        assertThat(testInscriptionAdministrativeFormation.getInscriptionAnnuleeYN()).isEqualTo(UPDATED_INSCRIPTION_ANNULEE_YN);
        assertThat(testInscriptionAdministrativeFormation.getPaiementFraisOblYN()).isEqualTo(UPDATED_PAIEMENT_FRAIS_OBL_YN);
        assertThat(testInscriptionAdministrativeFormation.getPaiementFraisIntegergYN()).isEqualTo(UPDATED_PAIEMENT_FRAIS_INTEGERG_YN);
        assertThat(testInscriptionAdministrativeFormation.getCertificatDelivreYN()).isEqualTo(UPDATED_CERTIFICAT_DELIVRE_YN);
        assertThat(testInscriptionAdministrativeFormation.getDateChoixFormation()).isEqualTo(UPDATED_DATE_CHOIX_FORMATION);
        assertThat(testInscriptionAdministrativeFormation.getDateValidationInscription()).isEqualTo(UPDATED_DATE_VALIDATION_INSCRIPTION);
    }

    @Test
    @Transactional
    void putNonExistingInscriptionAdministrativeFormation() throws Exception {
        int databaseSizeBeforeUpdate = inscriptionAdministrativeFormationRepository.findAll().size();
        inscriptionAdministrativeFormation.setId(longCount.incrementAndGet());

        // Create the InscriptionAdministrativeFormation
        InscriptionAdministrativeFormationDTO inscriptionAdministrativeFormationDTO = inscriptionAdministrativeFormationMapper.toDto(
            inscriptionAdministrativeFormation
        );

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restInscriptionAdministrativeFormationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, inscriptionAdministrativeFormationDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(inscriptionAdministrativeFormationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the InscriptionAdministrativeFormation in the database
        List<InscriptionAdministrativeFormation> inscriptionAdministrativeFormationList =
            inscriptionAdministrativeFormationRepository.findAll();
        assertThat(inscriptionAdministrativeFormationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchInscriptionAdministrativeFormation() throws Exception {
        int databaseSizeBeforeUpdate = inscriptionAdministrativeFormationRepository.findAll().size();
        inscriptionAdministrativeFormation.setId(longCount.incrementAndGet());

        // Create the InscriptionAdministrativeFormation
        InscriptionAdministrativeFormationDTO inscriptionAdministrativeFormationDTO = inscriptionAdministrativeFormationMapper.toDto(
            inscriptionAdministrativeFormation
        );

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restInscriptionAdministrativeFormationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(inscriptionAdministrativeFormationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the InscriptionAdministrativeFormation in the database
        List<InscriptionAdministrativeFormation> inscriptionAdministrativeFormationList =
            inscriptionAdministrativeFormationRepository.findAll();
        assertThat(inscriptionAdministrativeFormationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamInscriptionAdministrativeFormation() throws Exception {
        int databaseSizeBeforeUpdate = inscriptionAdministrativeFormationRepository.findAll().size();
        inscriptionAdministrativeFormation.setId(longCount.incrementAndGet());

        // Create the InscriptionAdministrativeFormation
        InscriptionAdministrativeFormationDTO inscriptionAdministrativeFormationDTO = inscriptionAdministrativeFormationMapper.toDto(
            inscriptionAdministrativeFormation
        );

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restInscriptionAdministrativeFormationMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(inscriptionAdministrativeFormationDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the InscriptionAdministrativeFormation in the database
        List<InscriptionAdministrativeFormation> inscriptionAdministrativeFormationList =
            inscriptionAdministrativeFormationRepository.findAll();
        assertThat(inscriptionAdministrativeFormationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateInscriptionAdministrativeFormationWithPatch() throws Exception {
        // Initialize the database
        inscriptionAdministrativeFormationRepository.saveAndFlush(inscriptionAdministrativeFormation);

        int databaseSizeBeforeUpdate = inscriptionAdministrativeFormationRepository.findAll().size();

        // Update the inscriptionAdministrativeFormation using partial update
        InscriptionAdministrativeFormation partialUpdatedInscriptionAdministrativeFormation = new InscriptionAdministrativeFormation();
        partialUpdatedInscriptionAdministrativeFormation.setId(inscriptionAdministrativeFormation.getId());

        partialUpdatedInscriptionAdministrativeFormation
            .paiementFraisIntegergYN(UPDATED_PAIEMENT_FRAIS_INTEGERG_YN)
            .dateChoixFormation(UPDATED_DATE_CHOIX_FORMATION)
            .dateValidationInscription(UPDATED_DATE_VALIDATION_INSCRIPTION);

        restInscriptionAdministrativeFormationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedInscriptionAdministrativeFormation.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedInscriptionAdministrativeFormation))
            )
            .andExpect(status().isOk());

        // Validate the InscriptionAdministrativeFormation in the database
        List<InscriptionAdministrativeFormation> inscriptionAdministrativeFormationList =
            inscriptionAdministrativeFormationRepository.findAll();
        assertThat(inscriptionAdministrativeFormationList).hasSize(databaseSizeBeforeUpdate);
        InscriptionAdministrativeFormation testInscriptionAdministrativeFormation = inscriptionAdministrativeFormationList.get(
            inscriptionAdministrativeFormationList.size() - 1
        );
        assertThat(testInscriptionAdministrativeFormation.getInscriptionPrincipaleYN()).isEqualTo(DEFAULT_INSCRIPTION_PRINCIPALE_YN);
        assertThat(testInscriptionAdministrativeFormation.getInscriptionAnnuleeYN()).isEqualTo(DEFAULT_INSCRIPTION_ANNULEE_YN);
        assertThat(testInscriptionAdministrativeFormation.getPaiementFraisOblYN()).isEqualTo(DEFAULT_PAIEMENT_FRAIS_OBL_YN);
        assertThat(testInscriptionAdministrativeFormation.getPaiementFraisIntegergYN()).isEqualTo(UPDATED_PAIEMENT_FRAIS_INTEGERG_YN);
        assertThat(testInscriptionAdministrativeFormation.getCertificatDelivreYN()).isEqualTo(DEFAULT_CERTIFICAT_DELIVRE_YN);
        assertThat(testInscriptionAdministrativeFormation.getDateChoixFormation()).isEqualTo(UPDATED_DATE_CHOIX_FORMATION);
        assertThat(testInscriptionAdministrativeFormation.getDateValidationInscription()).isEqualTo(UPDATED_DATE_VALIDATION_INSCRIPTION);
    }

    @Test
    @Transactional
    void fullUpdateInscriptionAdministrativeFormationWithPatch() throws Exception {
        // Initialize the database
        inscriptionAdministrativeFormationRepository.saveAndFlush(inscriptionAdministrativeFormation);

        int databaseSizeBeforeUpdate = inscriptionAdministrativeFormationRepository.findAll().size();

        // Update the inscriptionAdministrativeFormation using partial update
        InscriptionAdministrativeFormation partialUpdatedInscriptionAdministrativeFormation = new InscriptionAdministrativeFormation();
        partialUpdatedInscriptionAdministrativeFormation.setId(inscriptionAdministrativeFormation.getId());

        partialUpdatedInscriptionAdministrativeFormation
            .inscriptionPrincipaleYN(UPDATED_INSCRIPTION_PRINCIPALE_YN)
            .inscriptionAnnuleeYN(UPDATED_INSCRIPTION_ANNULEE_YN)
            .paiementFraisOblYN(UPDATED_PAIEMENT_FRAIS_OBL_YN)
            .paiementFraisIntegergYN(UPDATED_PAIEMENT_FRAIS_INTEGERG_YN)
            .certificatDelivreYN(UPDATED_CERTIFICAT_DELIVRE_YN)
            .dateChoixFormation(UPDATED_DATE_CHOIX_FORMATION)
            .dateValidationInscription(UPDATED_DATE_VALIDATION_INSCRIPTION);

        restInscriptionAdministrativeFormationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedInscriptionAdministrativeFormation.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedInscriptionAdministrativeFormation))
            )
            .andExpect(status().isOk());

        // Validate the InscriptionAdministrativeFormation in the database
        List<InscriptionAdministrativeFormation> inscriptionAdministrativeFormationList =
            inscriptionAdministrativeFormationRepository.findAll();
        assertThat(inscriptionAdministrativeFormationList).hasSize(databaseSizeBeforeUpdate);
        InscriptionAdministrativeFormation testInscriptionAdministrativeFormation = inscriptionAdministrativeFormationList.get(
            inscriptionAdministrativeFormationList.size() - 1
        );
        assertThat(testInscriptionAdministrativeFormation.getInscriptionPrincipaleYN()).isEqualTo(UPDATED_INSCRIPTION_PRINCIPALE_YN);
        assertThat(testInscriptionAdministrativeFormation.getInscriptionAnnuleeYN()).isEqualTo(UPDATED_INSCRIPTION_ANNULEE_YN);
        assertThat(testInscriptionAdministrativeFormation.getPaiementFraisOblYN()).isEqualTo(UPDATED_PAIEMENT_FRAIS_OBL_YN);
        assertThat(testInscriptionAdministrativeFormation.getPaiementFraisIntegergYN()).isEqualTo(UPDATED_PAIEMENT_FRAIS_INTEGERG_YN);
        assertThat(testInscriptionAdministrativeFormation.getCertificatDelivreYN()).isEqualTo(UPDATED_CERTIFICAT_DELIVRE_YN);
        assertThat(testInscriptionAdministrativeFormation.getDateChoixFormation()).isEqualTo(UPDATED_DATE_CHOIX_FORMATION);
        assertThat(testInscriptionAdministrativeFormation.getDateValidationInscription()).isEqualTo(UPDATED_DATE_VALIDATION_INSCRIPTION);
    }

    @Test
    @Transactional
    void patchNonExistingInscriptionAdministrativeFormation() throws Exception {
        int databaseSizeBeforeUpdate = inscriptionAdministrativeFormationRepository.findAll().size();
        inscriptionAdministrativeFormation.setId(longCount.incrementAndGet());

        // Create the InscriptionAdministrativeFormation
        InscriptionAdministrativeFormationDTO inscriptionAdministrativeFormationDTO = inscriptionAdministrativeFormationMapper.toDto(
            inscriptionAdministrativeFormation
        );

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restInscriptionAdministrativeFormationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, inscriptionAdministrativeFormationDTO.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(inscriptionAdministrativeFormationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the InscriptionAdministrativeFormation in the database
        List<InscriptionAdministrativeFormation> inscriptionAdministrativeFormationList =
            inscriptionAdministrativeFormationRepository.findAll();
        assertThat(inscriptionAdministrativeFormationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchInscriptionAdministrativeFormation() throws Exception {
        int databaseSizeBeforeUpdate = inscriptionAdministrativeFormationRepository.findAll().size();
        inscriptionAdministrativeFormation.setId(longCount.incrementAndGet());

        // Create the InscriptionAdministrativeFormation
        InscriptionAdministrativeFormationDTO inscriptionAdministrativeFormationDTO = inscriptionAdministrativeFormationMapper.toDto(
            inscriptionAdministrativeFormation
        );

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restInscriptionAdministrativeFormationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(inscriptionAdministrativeFormationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the InscriptionAdministrativeFormation in the database
        List<InscriptionAdministrativeFormation> inscriptionAdministrativeFormationList =
            inscriptionAdministrativeFormationRepository.findAll();
        assertThat(inscriptionAdministrativeFormationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamInscriptionAdministrativeFormation() throws Exception {
        int databaseSizeBeforeUpdate = inscriptionAdministrativeFormationRepository.findAll().size();
        inscriptionAdministrativeFormation.setId(longCount.incrementAndGet());

        // Create the InscriptionAdministrativeFormation
        InscriptionAdministrativeFormationDTO inscriptionAdministrativeFormationDTO = inscriptionAdministrativeFormationMapper.toDto(
            inscriptionAdministrativeFormation
        );

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restInscriptionAdministrativeFormationMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(inscriptionAdministrativeFormationDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the InscriptionAdministrativeFormation in the database
        List<InscriptionAdministrativeFormation> inscriptionAdministrativeFormationList =
            inscriptionAdministrativeFormationRepository.findAll();
        assertThat(inscriptionAdministrativeFormationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteInscriptionAdministrativeFormation() throws Exception {
        // Initialize the database
        inscriptionAdministrativeFormationRepository.saveAndFlush(inscriptionAdministrativeFormation);

        int databaseSizeBeforeDelete = inscriptionAdministrativeFormationRepository.findAll().size();

        // Delete the inscriptionAdministrativeFormation
        restInscriptionAdministrativeFormationMockMvc
            .perform(delete(ENTITY_API_URL_ID, inscriptionAdministrativeFormation.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<InscriptionAdministrativeFormation> inscriptionAdministrativeFormationList =
            inscriptionAdministrativeFormationRepository.findAll();
        assertThat(inscriptionAdministrativeFormationList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
