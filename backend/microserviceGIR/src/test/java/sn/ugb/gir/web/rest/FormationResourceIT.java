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
import sn.ugb.gir.domain.Formation;
import sn.ugb.gir.repository.FormationRepository;
import sn.ugb.gir.service.dto.FormationDTO;
import sn.ugb.gir.service.mapper.FormationMapper;

/**
 * Integration tests for the {@link FormationResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class FormationResourceIT {

    private static final Integer DEFAULT_NOMBRE_MENSUALITES = 1;
    private static final Integer UPDATED_NOMBRE_MENSUALITES = 2;

    private static final Integer DEFAULT_FRAIS_DOSSIER_YN = 1;
    private static final Integer UPDATED_FRAIS_DOSSIER_YN = 2;

    private static final Integer DEFAULT_CLASSE_DIPLOMANTE_YN = 1;
    private static final Integer UPDATED_CLASSE_DIPLOMANTE_YN = 2;

    private static final String DEFAULT_LIBELLE_DIPLOME = "AAAAAAAAAA";
    private static final String UPDATED_LIBELLE_DIPLOME = "BBBBBBBBBB";

    private static final String DEFAULT_CODE_FORMATION = "AAAAAAAAAA";
    private static final String UPDATED_CODE_FORMATION = "BBBBBBBBBB";

    private static final Integer DEFAULT_NBRE_CREDITS_MIN = 1;
    private static final Integer UPDATED_NBRE_CREDITS_MIN = 2;

    private static final Integer DEFAULT_EST_PARCOURS_YN = 1;
    private static final Integer UPDATED_EST_PARCOURS_YN = 2;

    private static final Integer DEFAULT_LMD_YN = 1;
    private static final Integer UPDATED_LMD_YN = 2;

    private static final String ENTITY_API_URL = "/api/formations";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private FormationRepository formationRepository;

    @Autowired
    private FormationMapper formationMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restFormationMockMvc;

    private Formation formation;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Formation createEntity(EntityManager em) {
        Formation formation = new Formation()
            .nombreMensualites(DEFAULT_NOMBRE_MENSUALITES)
            .fraisDossierYN(DEFAULT_FRAIS_DOSSIER_YN)
            .classeDiplomanteYN(DEFAULT_CLASSE_DIPLOMANTE_YN)
            .libelleDiplome(DEFAULT_LIBELLE_DIPLOME)
            .codeFormation(DEFAULT_CODE_FORMATION)
            .nbreCreditsMin(DEFAULT_NBRE_CREDITS_MIN)
            .estParcoursYN(DEFAULT_EST_PARCOURS_YN)
            .lmdYN(DEFAULT_LMD_YN);
        return formation;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Formation createUpdatedEntity(EntityManager em) {
        Formation formation = new Formation()
            .nombreMensualites(UPDATED_NOMBRE_MENSUALITES)
            .fraisDossierYN(UPDATED_FRAIS_DOSSIER_YN)
            .classeDiplomanteYN(UPDATED_CLASSE_DIPLOMANTE_YN)
            .libelleDiplome(UPDATED_LIBELLE_DIPLOME)
            .codeFormation(UPDATED_CODE_FORMATION)
            .nbreCreditsMin(UPDATED_NBRE_CREDITS_MIN)
            .estParcoursYN(UPDATED_EST_PARCOURS_YN)
            .lmdYN(UPDATED_LMD_YN);
        return formation;
    }

    @BeforeEach
    public void initTest() {
        formation = createEntity(em);
    }

    @Test
    @Transactional
    void createFormation() throws Exception {
        int databaseSizeBeforeCreate = formationRepository.findAll().size();
        // Create the Formation
        FormationDTO formationDTO = formationMapper.toDto(formation);
        restFormationMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(formationDTO))
            )
            .andExpect(status().isCreated());

        // Validate the Formation in the database
        List<Formation> formationList = formationRepository.findAll();
        assertThat(formationList).hasSize(databaseSizeBeforeCreate + 1);
        Formation testFormation = formationList.get(formationList.size() - 1);
        assertThat(testFormation.getNombreMensualites()).isEqualTo(DEFAULT_NOMBRE_MENSUALITES);
        assertThat(testFormation.getFraisDossierYN()).isEqualTo(DEFAULT_FRAIS_DOSSIER_YN);
        assertThat(testFormation.getClasseDiplomanteYN()).isEqualTo(DEFAULT_CLASSE_DIPLOMANTE_YN);
        assertThat(testFormation.getLibelleDiplome()).isEqualTo(DEFAULT_LIBELLE_DIPLOME);
        assertThat(testFormation.getCodeFormation()).isEqualTo(DEFAULT_CODE_FORMATION);
        assertThat(testFormation.getNbreCreditsMin()).isEqualTo(DEFAULT_NBRE_CREDITS_MIN);
        assertThat(testFormation.getEstParcoursYN()).isEqualTo(DEFAULT_EST_PARCOURS_YN);
        assertThat(testFormation.getLmdYN()).isEqualTo(DEFAULT_LMD_YN);
    }

    @Test
    @Transactional
    void createFormationWithExistingId() throws Exception {
        // Create the Formation with an existing ID
        formation.setId(1L);
        FormationDTO formationDTO = formationMapper.toDto(formation);

        int databaseSizeBeforeCreate = formationRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restFormationMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(formationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Formation in the database
        List<Formation> formationList = formationRepository.findAll();
        assertThat(formationList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllFormations() throws Exception {
        // Initialize the database
        formationRepository.saveAndFlush(formation);

        // Get all the formationList
        restFormationMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(formation.getId().intValue())))
            .andExpect(jsonPath("$.[*].nombreMensualites").value(hasItem(DEFAULT_NOMBRE_MENSUALITES)))
            .andExpect(jsonPath("$.[*].fraisDossierYN").value(hasItem(DEFAULT_FRAIS_DOSSIER_YN)))
            .andExpect(jsonPath("$.[*].classeDiplomanteYN").value(hasItem(DEFAULT_CLASSE_DIPLOMANTE_YN)))
            .andExpect(jsonPath("$.[*].libelleDiplome").value(hasItem(DEFAULT_LIBELLE_DIPLOME)))
            .andExpect(jsonPath("$.[*].codeFormation").value(hasItem(DEFAULT_CODE_FORMATION)))
            .andExpect(jsonPath("$.[*].nbreCreditsMin").value(hasItem(DEFAULT_NBRE_CREDITS_MIN)))
            .andExpect(jsonPath("$.[*].estParcoursYN").value(hasItem(DEFAULT_EST_PARCOURS_YN)))
            .andExpect(jsonPath("$.[*].lmdYN").value(hasItem(DEFAULT_LMD_YN)));
    }

    @Test
    @Transactional
    void getFormation() throws Exception {
        // Initialize the database
        formationRepository.saveAndFlush(formation);

        // Get the formation
        restFormationMockMvc
            .perform(get(ENTITY_API_URL_ID, formation.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(formation.getId().intValue()))
            .andExpect(jsonPath("$.nombreMensualites").value(DEFAULT_NOMBRE_MENSUALITES))
            .andExpect(jsonPath("$.fraisDossierYN").value(DEFAULT_FRAIS_DOSSIER_YN))
            .andExpect(jsonPath("$.classeDiplomanteYN").value(DEFAULT_CLASSE_DIPLOMANTE_YN))
            .andExpect(jsonPath("$.libelleDiplome").value(DEFAULT_LIBELLE_DIPLOME))
            .andExpect(jsonPath("$.codeFormation").value(DEFAULT_CODE_FORMATION))
            .andExpect(jsonPath("$.nbreCreditsMin").value(DEFAULT_NBRE_CREDITS_MIN))
            .andExpect(jsonPath("$.estParcoursYN").value(DEFAULT_EST_PARCOURS_YN))
            .andExpect(jsonPath("$.lmdYN").value(DEFAULT_LMD_YN));
    }

    @Test
    @Transactional
    void getNonExistingFormation() throws Exception {
        // Get the formation
        restFormationMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingFormation() throws Exception {
        // Initialize the database
        formationRepository.saveAndFlush(formation);

        int databaseSizeBeforeUpdate = formationRepository.findAll().size();

        // Update the formation
        Formation updatedFormation = formationRepository.findById(formation.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedFormation are not directly saved in db
        em.detach(updatedFormation);
        updatedFormation
            .nombreMensualites(UPDATED_NOMBRE_MENSUALITES)
            .fraisDossierYN(UPDATED_FRAIS_DOSSIER_YN)
            .classeDiplomanteYN(UPDATED_CLASSE_DIPLOMANTE_YN)
            .libelleDiplome(UPDATED_LIBELLE_DIPLOME)
            .codeFormation(UPDATED_CODE_FORMATION)
            .nbreCreditsMin(UPDATED_NBRE_CREDITS_MIN)
            .estParcoursYN(UPDATED_EST_PARCOURS_YN)
            .lmdYN(UPDATED_LMD_YN);
        FormationDTO formationDTO = formationMapper.toDto(updatedFormation);

        restFormationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, formationDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(formationDTO))
            )
            .andExpect(status().isOk());

        // Validate the Formation in the database
        List<Formation> formationList = formationRepository.findAll();
        assertThat(formationList).hasSize(databaseSizeBeforeUpdate);
        Formation testFormation = formationList.get(formationList.size() - 1);
        assertThat(testFormation.getNombreMensualites()).isEqualTo(UPDATED_NOMBRE_MENSUALITES);
        assertThat(testFormation.getFraisDossierYN()).isEqualTo(UPDATED_FRAIS_DOSSIER_YN);
        assertThat(testFormation.getClasseDiplomanteYN()).isEqualTo(UPDATED_CLASSE_DIPLOMANTE_YN);
        assertThat(testFormation.getLibelleDiplome()).isEqualTo(UPDATED_LIBELLE_DIPLOME);
        assertThat(testFormation.getCodeFormation()).isEqualTo(UPDATED_CODE_FORMATION);
        assertThat(testFormation.getNbreCreditsMin()).isEqualTo(UPDATED_NBRE_CREDITS_MIN);
        assertThat(testFormation.getEstParcoursYN()).isEqualTo(UPDATED_EST_PARCOURS_YN);
        assertThat(testFormation.getLmdYN()).isEqualTo(UPDATED_LMD_YN);
    }

    @Test
    @Transactional
    void putNonExistingFormation() throws Exception {
        int databaseSizeBeforeUpdate = formationRepository.findAll().size();
        formation.setId(longCount.incrementAndGet());

        // Create the Formation
        FormationDTO formationDTO = formationMapper.toDto(formation);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFormationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, formationDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(formationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Formation in the database
        List<Formation> formationList = formationRepository.findAll();
        assertThat(formationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchFormation() throws Exception {
        int databaseSizeBeforeUpdate = formationRepository.findAll().size();
        formation.setId(longCount.incrementAndGet());

        // Create the Formation
        FormationDTO formationDTO = formationMapper.toDto(formation);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFormationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(formationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Formation in the database
        List<Formation> formationList = formationRepository.findAll();
        assertThat(formationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamFormation() throws Exception {
        int databaseSizeBeforeUpdate = formationRepository.findAll().size();
        formation.setId(longCount.incrementAndGet());

        // Create the Formation
        FormationDTO formationDTO = formationMapper.toDto(formation);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFormationMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(formationDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Formation in the database
        List<Formation> formationList = formationRepository.findAll();
        assertThat(formationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateFormationWithPatch() throws Exception {
        // Initialize the database
        formationRepository.saveAndFlush(formation);

        int databaseSizeBeforeUpdate = formationRepository.findAll().size();

        // Update the formation using partial update
        Formation partialUpdatedFormation = new Formation();
        partialUpdatedFormation.setId(formation.getId());

        partialUpdatedFormation
            .nombreMensualites(UPDATED_NOMBRE_MENSUALITES)
            .fraisDossierYN(UPDATED_FRAIS_DOSSIER_YN)
            .codeFormation(UPDATED_CODE_FORMATION)
            .estParcoursYN(UPDATED_EST_PARCOURS_YN)
            .lmdYN(UPDATED_LMD_YN);

        restFormationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedFormation.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedFormation))
            )
            .andExpect(status().isOk());

        // Validate the Formation in the database
        List<Formation> formationList = formationRepository.findAll();
        assertThat(formationList).hasSize(databaseSizeBeforeUpdate);
        Formation testFormation = formationList.get(formationList.size() - 1);
        assertThat(testFormation.getNombreMensualites()).isEqualTo(UPDATED_NOMBRE_MENSUALITES);
        assertThat(testFormation.getFraisDossierYN()).isEqualTo(UPDATED_FRAIS_DOSSIER_YN);
        assertThat(testFormation.getClasseDiplomanteYN()).isEqualTo(DEFAULT_CLASSE_DIPLOMANTE_YN);
        assertThat(testFormation.getLibelleDiplome()).isEqualTo(DEFAULT_LIBELLE_DIPLOME);
        assertThat(testFormation.getCodeFormation()).isEqualTo(UPDATED_CODE_FORMATION);
        assertThat(testFormation.getNbreCreditsMin()).isEqualTo(DEFAULT_NBRE_CREDITS_MIN);
        assertThat(testFormation.getEstParcoursYN()).isEqualTo(UPDATED_EST_PARCOURS_YN);
        assertThat(testFormation.getLmdYN()).isEqualTo(UPDATED_LMD_YN);
    }

    @Test
    @Transactional
    void fullUpdateFormationWithPatch() throws Exception {
        // Initialize the database
        formationRepository.saveAndFlush(formation);

        int databaseSizeBeforeUpdate = formationRepository.findAll().size();

        // Update the formation using partial update
        Formation partialUpdatedFormation = new Formation();
        partialUpdatedFormation.setId(formation.getId());

        partialUpdatedFormation
            .nombreMensualites(UPDATED_NOMBRE_MENSUALITES)
            .fraisDossierYN(UPDATED_FRAIS_DOSSIER_YN)
            .classeDiplomanteYN(UPDATED_CLASSE_DIPLOMANTE_YN)
            .libelleDiplome(UPDATED_LIBELLE_DIPLOME)
            .codeFormation(UPDATED_CODE_FORMATION)
            .nbreCreditsMin(UPDATED_NBRE_CREDITS_MIN)
            .estParcoursYN(UPDATED_EST_PARCOURS_YN)
            .lmdYN(UPDATED_LMD_YN);

        restFormationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedFormation.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedFormation))
            )
            .andExpect(status().isOk());

        // Validate the Formation in the database
        List<Formation> formationList = formationRepository.findAll();
        assertThat(formationList).hasSize(databaseSizeBeforeUpdate);
        Formation testFormation = formationList.get(formationList.size() - 1);
        assertThat(testFormation.getNombreMensualites()).isEqualTo(UPDATED_NOMBRE_MENSUALITES);
        assertThat(testFormation.getFraisDossierYN()).isEqualTo(UPDATED_FRAIS_DOSSIER_YN);
        assertThat(testFormation.getClasseDiplomanteYN()).isEqualTo(UPDATED_CLASSE_DIPLOMANTE_YN);
        assertThat(testFormation.getLibelleDiplome()).isEqualTo(UPDATED_LIBELLE_DIPLOME);
        assertThat(testFormation.getCodeFormation()).isEqualTo(UPDATED_CODE_FORMATION);
        assertThat(testFormation.getNbreCreditsMin()).isEqualTo(UPDATED_NBRE_CREDITS_MIN);
        assertThat(testFormation.getEstParcoursYN()).isEqualTo(UPDATED_EST_PARCOURS_YN);
        assertThat(testFormation.getLmdYN()).isEqualTo(UPDATED_LMD_YN);
    }

    @Test
    @Transactional
    void patchNonExistingFormation() throws Exception {
        int databaseSizeBeforeUpdate = formationRepository.findAll().size();
        formation.setId(longCount.incrementAndGet());

        // Create the Formation
        FormationDTO formationDTO = formationMapper.toDto(formation);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFormationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, formationDTO.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(formationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Formation in the database
        List<Formation> formationList = formationRepository.findAll();
        assertThat(formationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchFormation() throws Exception {
        int databaseSizeBeforeUpdate = formationRepository.findAll().size();
        formation.setId(longCount.incrementAndGet());

        // Create the Formation
        FormationDTO formationDTO = formationMapper.toDto(formation);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFormationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(formationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Formation in the database
        List<Formation> formationList = formationRepository.findAll();
        assertThat(formationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamFormation() throws Exception {
        int databaseSizeBeforeUpdate = formationRepository.findAll().size();
        formation.setId(longCount.incrementAndGet());

        // Create the Formation
        FormationDTO formationDTO = formationMapper.toDto(formation);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFormationMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(formationDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Formation in the database
        List<Formation> formationList = formationRepository.findAll();
        assertThat(formationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteFormation() throws Exception {
        // Initialize the database
        formationRepository.saveAndFlush(formation);

        int databaseSizeBeforeDelete = formationRepository.findAll().size();

        // Delete the formation
        restFormationMockMvc
            .perform(delete(ENTITY_API_URL_ID, formation.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Formation> formationList = formationRepository.findAll();
        assertThat(formationList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
