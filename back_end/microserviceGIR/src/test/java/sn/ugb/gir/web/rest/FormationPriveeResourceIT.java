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
import sn.ugb.gir.domain.FormationPrivee;
import sn.ugb.gir.repository.FormationPriveeRepository;
import sn.ugb.gir.service.dto.FormationPriveeDTO;
import sn.ugb.gir.service.mapper.FormationPriveeMapper;

/**
 * Integration tests for the {@link FormationPriveeResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class FormationPriveeResourceIT {

    private static final Integer DEFAULT_NOMBRE_MENSUALITES = 1;
    private static final Integer UPDATED_NOMBRE_MENSUALITES = 2;

    private static final Integer DEFAULT_PAIEMENT_PREMIER_MOIS_YN = 1;
    private static final Integer UPDATED_PAIEMENT_PREMIER_MOIS_YN = 2;

    private static final Integer DEFAULT_PAIEMENT_DERNIER_MOIS_YN = 1;
    private static final Integer UPDATED_PAIEMENT_DERNIER_MOIS_YN = 2;

    private static final Integer DEFAULT_FRAIS_DOSSIER_YN = 1;
    private static final Integer UPDATED_FRAIS_DOSSIER_YN = 2;

    private static final Float DEFAULT_COUT_TOTAL = 1F;
    private static final Float UPDATED_COUT_TOTAL = 2F;

    private static final Float DEFAULT_MENSUALITE = 1F;
    private static final Float UPDATED_MENSUALITE = 2F;

    private static final String ENTITY_API_URL = "/api/formation-privees";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private FormationPriveeRepository formationPriveeRepository;

    @Autowired
    private FormationPriveeMapper formationPriveeMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restFormationPriveeMockMvc;

    private FormationPrivee formationPrivee;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static FormationPrivee createEntity(EntityManager em) {
        FormationPrivee formationPrivee = new FormationPrivee()
            .nombreMensualites(DEFAULT_NOMBRE_MENSUALITES)
            .paiementPremierMoisYN(DEFAULT_PAIEMENT_PREMIER_MOIS_YN)
            .paiementDernierMoisYN(DEFAULT_PAIEMENT_DERNIER_MOIS_YN)
            .fraisDossierYN(DEFAULT_FRAIS_DOSSIER_YN)
            .coutTotal(DEFAULT_COUT_TOTAL)
            .mensualite(DEFAULT_MENSUALITE);
        return formationPrivee;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static FormationPrivee createUpdatedEntity(EntityManager em) {
        FormationPrivee formationPrivee = new FormationPrivee()
            .nombreMensualites(UPDATED_NOMBRE_MENSUALITES)
            .paiementPremierMoisYN(UPDATED_PAIEMENT_PREMIER_MOIS_YN)
            .paiementDernierMoisYN(UPDATED_PAIEMENT_DERNIER_MOIS_YN)
            .fraisDossierYN(UPDATED_FRAIS_DOSSIER_YN)
            .coutTotal(UPDATED_COUT_TOTAL)
            .mensualite(UPDATED_MENSUALITE);
        return formationPrivee;
    }

    @BeforeEach
    public void initTest() {
        formationPrivee = createEntity(em);
    }

    @Test
    @Transactional
    void createFormationPrivee() throws Exception {
        int databaseSizeBeforeCreate = formationPriveeRepository.findAll().size();
        // Create the FormationPrivee
        FormationPriveeDTO formationPriveeDTO = formationPriveeMapper.toDto(formationPrivee);
        restFormationPriveeMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(formationPriveeDTO))
            )
            .andExpect(status().isCreated());

        // Validate the FormationPrivee in the database
        List<FormationPrivee> formationPriveeList = formationPriveeRepository.findAll();
        assertThat(formationPriveeList).hasSize(databaseSizeBeforeCreate + 1);
        FormationPrivee testFormationPrivee = formationPriveeList.get(formationPriveeList.size() - 1);
        assertThat(testFormationPrivee.getNombreMensualites()).isEqualTo(DEFAULT_NOMBRE_MENSUALITES);
        assertThat(testFormationPrivee.getPaiementPremierMoisYN()).isEqualTo(DEFAULT_PAIEMENT_PREMIER_MOIS_YN);
        assertThat(testFormationPrivee.getPaiementDernierMoisYN()).isEqualTo(DEFAULT_PAIEMENT_DERNIER_MOIS_YN);
        assertThat(testFormationPrivee.getFraisDossierYN()).isEqualTo(DEFAULT_FRAIS_DOSSIER_YN);
        assertThat(testFormationPrivee.getCoutTotal()).isEqualTo(DEFAULT_COUT_TOTAL);
        assertThat(testFormationPrivee.getMensualite()).isEqualTo(DEFAULT_MENSUALITE);
    }

    @Test
    @Transactional
    void createFormationPriveeWithExistingId() throws Exception {
        // Create the FormationPrivee with an existing ID
        formationPrivee.setId(1L);
        FormationPriveeDTO formationPriveeDTO = formationPriveeMapper.toDto(formationPrivee);

        int databaseSizeBeforeCreate = formationPriveeRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restFormationPriveeMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(formationPriveeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the FormationPrivee in the database
        List<FormationPrivee> formationPriveeList = formationPriveeRepository.findAll();
        assertThat(formationPriveeList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNombreMensualitesIsRequired() throws Exception {
        int databaseSizeBeforeTest = formationPriveeRepository.findAll().size();
        // set the field null
        formationPrivee.setNombreMensualites(null);

        // Create the FormationPrivee, which fails.
        FormationPriveeDTO formationPriveeDTO = formationPriveeMapper.toDto(formationPrivee);

        restFormationPriveeMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(formationPriveeDTO))
            )
            .andExpect(status().isBadRequest());

        List<FormationPrivee> formationPriveeList = formationPriveeRepository.findAll();
        assertThat(formationPriveeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCoutTotalIsRequired() throws Exception {
        int databaseSizeBeforeTest = formationPriveeRepository.findAll().size();
        // set the field null
        formationPrivee.setCoutTotal(null);

        // Create the FormationPrivee, which fails.
        FormationPriveeDTO formationPriveeDTO = formationPriveeMapper.toDto(formationPrivee);

        restFormationPriveeMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(formationPriveeDTO))
            )
            .andExpect(status().isBadRequest());

        List<FormationPrivee> formationPriveeList = formationPriveeRepository.findAll();
        assertThat(formationPriveeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkMensualiteIsRequired() throws Exception {
        int databaseSizeBeforeTest = formationPriveeRepository.findAll().size();
        // set the field null
        formationPrivee.setMensualite(null);

        // Create the FormationPrivee, which fails.
        FormationPriveeDTO formationPriveeDTO = formationPriveeMapper.toDto(formationPrivee);

        restFormationPriveeMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(formationPriveeDTO))
            )
            .andExpect(status().isBadRequest());

        List<FormationPrivee> formationPriveeList = formationPriveeRepository.findAll();
        assertThat(formationPriveeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllFormationPrivees() throws Exception {
        // Initialize the database
        formationPriveeRepository.saveAndFlush(formationPrivee);

        // Get all the formationPriveeList
        restFormationPriveeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(formationPrivee.getId().intValue())))
            .andExpect(jsonPath("$.[*].nombreMensualites").value(hasItem(DEFAULT_NOMBRE_MENSUALITES)))
            .andExpect(jsonPath("$.[*].paiementPremierMoisYN").value(hasItem(DEFAULT_PAIEMENT_PREMIER_MOIS_YN)))
            .andExpect(jsonPath("$.[*].paiementDernierMoisYN").value(hasItem(DEFAULT_PAIEMENT_DERNIER_MOIS_YN)))
            .andExpect(jsonPath("$.[*].fraisDossierYN").value(hasItem(DEFAULT_FRAIS_DOSSIER_YN)))
            .andExpect(jsonPath("$.[*].coutTotal").value(hasItem(DEFAULT_COUT_TOTAL.doubleValue())))
            .andExpect(jsonPath("$.[*].mensualite").value(hasItem(DEFAULT_MENSUALITE.doubleValue())));
    }

    @Test
    @Transactional
    void getFormationPrivee() throws Exception {
        // Initialize the database
        formationPriveeRepository.saveAndFlush(formationPrivee);

        // Get the formationPrivee
        restFormationPriveeMockMvc
            .perform(get(ENTITY_API_URL_ID, formationPrivee.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(formationPrivee.getId().intValue()))
            .andExpect(jsonPath("$.nombreMensualites").value(DEFAULT_NOMBRE_MENSUALITES))
            .andExpect(jsonPath("$.paiementPremierMoisYN").value(DEFAULT_PAIEMENT_PREMIER_MOIS_YN))
            .andExpect(jsonPath("$.paiementDernierMoisYN").value(DEFAULT_PAIEMENT_DERNIER_MOIS_YN))
            .andExpect(jsonPath("$.fraisDossierYN").value(DEFAULT_FRAIS_DOSSIER_YN))
            .andExpect(jsonPath("$.coutTotal").value(DEFAULT_COUT_TOTAL.doubleValue()))
            .andExpect(jsonPath("$.mensualite").value(DEFAULT_MENSUALITE.doubleValue()));
    }

    @Test
    @Transactional
    void getNonExistingFormationPrivee() throws Exception {
        // Get the formationPrivee
        restFormationPriveeMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingFormationPrivee() throws Exception {
        // Initialize the database
        formationPriveeRepository.saveAndFlush(formationPrivee);

        int databaseSizeBeforeUpdate = formationPriveeRepository.findAll().size();

        // Update the formationPrivee
        FormationPrivee updatedFormationPrivee = formationPriveeRepository.findById(formationPrivee.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedFormationPrivee are not directly saved in db
        em.detach(updatedFormationPrivee);
        updatedFormationPrivee
            .nombreMensualites(UPDATED_NOMBRE_MENSUALITES)
            .paiementPremierMoisYN(UPDATED_PAIEMENT_PREMIER_MOIS_YN)
            .paiementDernierMoisYN(UPDATED_PAIEMENT_DERNIER_MOIS_YN)
            .fraisDossierYN(UPDATED_FRAIS_DOSSIER_YN)
            .coutTotal(UPDATED_COUT_TOTAL)
            .mensualite(UPDATED_MENSUALITE);
        FormationPriveeDTO formationPriveeDTO = formationPriveeMapper.toDto(updatedFormationPrivee);

        restFormationPriveeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, formationPriveeDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(formationPriveeDTO))
            )
            .andExpect(status().isOk());

        // Validate the FormationPrivee in the database
        List<FormationPrivee> formationPriveeList = formationPriveeRepository.findAll();
        assertThat(formationPriveeList).hasSize(databaseSizeBeforeUpdate);
        FormationPrivee testFormationPrivee = formationPriveeList.get(formationPriveeList.size() - 1);
        assertThat(testFormationPrivee.getNombreMensualites()).isEqualTo(UPDATED_NOMBRE_MENSUALITES);
        assertThat(testFormationPrivee.getPaiementPremierMoisYN()).isEqualTo(UPDATED_PAIEMENT_PREMIER_MOIS_YN);
        assertThat(testFormationPrivee.getPaiementDernierMoisYN()).isEqualTo(UPDATED_PAIEMENT_DERNIER_MOIS_YN);
        assertThat(testFormationPrivee.getFraisDossierYN()).isEqualTo(UPDATED_FRAIS_DOSSIER_YN);
        assertThat(testFormationPrivee.getCoutTotal()).isEqualTo(UPDATED_COUT_TOTAL);
        assertThat(testFormationPrivee.getMensualite()).isEqualTo(UPDATED_MENSUALITE);
    }

    @Test
    @Transactional
    void putNonExistingFormationPrivee() throws Exception {
        int databaseSizeBeforeUpdate = formationPriveeRepository.findAll().size();
        formationPrivee.setId(longCount.incrementAndGet());

        // Create the FormationPrivee
        FormationPriveeDTO formationPriveeDTO = formationPriveeMapper.toDto(formationPrivee);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFormationPriveeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, formationPriveeDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(formationPriveeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the FormationPrivee in the database
        List<FormationPrivee> formationPriveeList = formationPriveeRepository.findAll();
        assertThat(formationPriveeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchFormationPrivee() throws Exception {
        int databaseSizeBeforeUpdate = formationPriveeRepository.findAll().size();
        formationPrivee.setId(longCount.incrementAndGet());

        // Create the FormationPrivee
        FormationPriveeDTO formationPriveeDTO = formationPriveeMapper.toDto(formationPrivee);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFormationPriveeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(formationPriveeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the FormationPrivee in the database
        List<FormationPrivee> formationPriveeList = formationPriveeRepository.findAll();
        assertThat(formationPriveeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamFormationPrivee() throws Exception {
        int databaseSizeBeforeUpdate = formationPriveeRepository.findAll().size();
        formationPrivee.setId(longCount.incrementAndGet());

        // Create the FormationPrivee
        FormationPriveeDTO formationPriveeDTO = formationPriveeMapper.toDto(formationPrivee);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFormationPriveeMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(formationPriveeDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the FormationPrivee in the database
        List<FormationPrivee> formationPriveeList = formationPriveeRepository.findAll();
        assertThat(formationPriveeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateFormationPriveeWithPatch() throws Exception {
        // Initialize the database
        formationPriveeRepository.saveAndFlush(formationPrivee);

        int databaseSizeBeforeUpdate = formationPriveeRepository.findAll().size();

        // Update the formationPrivee using partial update
        FormationPrivee partialUpdatedFormationPrivee = new FormationPrivee();
        partialUpdatedFormationPrivee.setId(formationPrivee.getId());

        partialUpdatedFormationPrivee.fraisDossierYN(UPDATED_FRAIS_DOSSIER_YN).mensualite(UPDATED_MENSUALITE);

        restFormationPriveeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedFormationPrivee.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedFormationPrivee))
            )
            .andExpect(status().isOk());

        // Validate the FormationPrivee in the database
        List<FormationPrivee> formationPriveeList = formationPriveeRepository.findAll();
        assertThat(formationPriveeList).hasSize(databaseSizeBeforeUpdate);
        FormationPrivee testFormationPrivee = formationPriveeList.get(formationPriveeList.size() - 1);
        assertThat(testFormationPrivee.getNombreMensualites()).isEqualTo(DEFAULT_NOMBRE_MENSUALITES);
        assertThat(testFormationPrivee.getPaiementPremierMoisYN()).isEqualTo(DEFAULT_PAIEMENT_PREMIER_MOIS_YN);
        assertThat(testFormationPrivee.getPaiementDernierMoisYN()).isEqualTo(DEFAULT_PAIEMENT_DERNIER_MOIS_YN);
        assertThat(testFormationPrivee.getFraisDossierYN()).isEqualTo(UPDATED_FRAIS_DOSSIER_YN);
        assertThat(testFormationPrivee.getCoutTotal()).isEqualTo(DEFAULT_COUT_TOTAL);
        assertThat(testFormationPrivee.getMensualite()).isEqualTo(UPDATED_MENSUALITE);
    }

    @Test
    @Transactional
    void fullUpdateFormationPriveeWithPatch() throws Exception {
        // Initialize the database
        formationPriveeRepository.saveAndFlush(formationPrivee);

        int databaseSizeBeforeUpdate = formationPriveeRepository.findAll().size();

        // Update the formationPrivee using partial update
        FormationPrivee partialUpdatedFormationPrivee = new FormationPrivee();
        partialUpdatedFormationPrivee.setId(formationPrivee.getId());

        partialUpdatedFormationPrivee
            .nombreMensualites(UPDATED_NOMBRE_MENSUALITES)
            .paiementPremierMoisYN(UPDATED_PAIEMENT_PREMIER_MOIS_YN)
            .paiementDernierMoisYN(UPDATED_PAIEMENT_DERNIER_MOIS_YN)
            .fraisDossierYN(UPDATED_FRAIS_DOSSIER_YN)
            .coutTotal(UPDATED_COUT_TOTAL)
            .mensualite(UPDATED_MENSUALITE);

        restFormationPriveeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedFormationPrivee.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedFormationPrivee))
            )
            .andExpect(status().isOk());

        // Validate the FormationPrivee in the database
        List<FormationPrivee> formationPriveeList = formationPriveeRepository.findAll();
        assertThat(formationPriveeList).hasSize(databaseSizeBeforeUpdate);
        FormationPrivee testFormationPrivee = formationPriveeList.get(formationPriveeList.size() - 1);
        assertThat(testFormationPrivee.getNombreMensualites()).isEqualTo(UPDATED_NOMBRE_MENSUALITES);
        assertThat(testFormationPrivee.getPaiementPremierMoisYN()).isEqualTo(UPDATED_PAIEMENT_PREMIER_MOIS_YN);
        assertThat(testFormationPrivee.getPaiementDernierMoisYN()).isEqualTo(UPDATED_PAIEMENT_DERNIER_MOIS_YN);
        assertThat(testFormationPrivee.getFraisDossierYN()).isEqualTo(UPDATED_FRAIS_DOSSIER_YN);
        assertThat(testFormationPrivee.getCoutTotal()).isEqualTo(UPDATED_COUT_TOTAL);
        assertThat(testFormationPrivee.getMensualite()).isEqualTo(UPDATED_MENSUALITE);
    }

    @Test
    @Transactional
    void patchNonExistingFormationPrivee() throws Exception {
        int databaseSizeBeforeUpdate = formationPriveeRepository.findAll().size();
        formationPrivee.setId(longCount.incrementAndGet());

        // Create the FormationPrivee
        FormationPriveeDTO formationPriveeDTO = formationPriveeMapper.toDto(formationPrivee);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFormationPriveeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, formationPriveeDTO.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(formationPriveeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the FormationPrivee in the database
        List<FormationPrivee> formationPriveeList = formationPriveeRepository.findAll();
        assertThat(formationPriveeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchFormationPrivee() throws Exception {
        int databaseSizeBeforeUpdate = formationPriveeRepository.findAll().size();
        formationPrivee.setId(longCount.incrementAndGet());

        // Create the FormationPrivee
        FormationPriveeDTO formationPriveeDTO = formationPriveeMapper.toDto(formationPrivee);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFormationPriveeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(formationPriveeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the FormationPrivee in the database
        List<FormationPrivee> formationPriveeList = formationPriveeRepository.findAll();
        assertThat(formationPriveeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamFormationPrivee() throws Exception {
        int databaseSizeBeforeUpdate = formationPriveeRepository.findAll().size();
        formationPrivee.setId(longCount.incrementAndGet());

        // Create the FormationPrivee
        FormationPriveeDTO formationPriveeDTO = formationPriveeMapper.toDto(formationPrivee);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFormationPriveeMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(formationPriveeDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the FormationPrivee in the database
        List<FormationPrivee> formationPriveeList = formationPriveeRepository.findAll();
        assertThat(formationPriveeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteFormationPrivee() throws Exception {
        // Initialize the database
        formationPriveeRepository.saveAndFlush(formationPrivee);

        int databaseSizeBeforeDelete = formationPriveeRepository.findAll().size();

        // Delete the formationPrivee
        restFormationPriveeMockMvc
            .perform(delete(ENTITY_API_URL_ID, formationPrivee.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<FormationPrivee> formationPriveeList = formationPriveeRepository.findAll();
        assertThat(formationPriveeList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
