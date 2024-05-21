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
import sn.ugb.gir.domain.Frais;
import sn.ugb.gir.domain.enumeration.CYCLE;
import sn.ugb.gir.repository.FraisRepository;
import sn.ugb.gir.service.dto.FraisDTO;
import sn.ugb.gir.service.mapper.FraisMapper;

/**
 * Integration tests for the {@link FraisResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class FraisResourceIT {

    private static final Double DEFAULT_VALEUR_FRAIS = 1D;
    private static final Double UPDATED_VALEUR_FRAIS = 2D;

    private static final String DEFAULT_DESCRIPTION_FRAIS = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION_FRAIS = "BBBBBBBBBB";

    private static final Integer DEFAULT_FRAISPOUR_ASSIMILE_YN = 1;
    private static final Integer UPDATED_FRAISPOUR_ASSIMILE_YN = 2;

    private static final CYCLE DEFAULT_CYCLE = CYCLE.LICENCE;
    private static final CYCLE UPDATED_CYCLE = CYCLE.MASTER;

    private static final Double DEFAULT_DIA = 1D;
    private static final Double UPDATED_DIA = 2D;

    private static final Double DEFAULT_DIP = 1D;
    private static final Double UPDATED_DIP = 2D;

    private static final Float DEFAULT_DIP_PRIVEE = 1F;
    private static final Float UPDATED_DIP_PRIVEE = 2F;

    private static final LocalDate DEFAULT_DATE_APPLICATION = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE_APPLICATION = LocalDate.now(ZoneId.systemDefault());

    private static final LocalDate DEFAULT_DATE_FIN = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE_FIN = LocalDate.now(ZoneId.systemDefault());

    private static final Integer DEFAULT_EST_EN_APPLICATION_YN = 1;
    private static final Integer UPDATED_EST_EN_APPLICATION_YN = 2;

    private static final String ENTITY_API_URL = "/api/frais";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private FraisRepository fraisRepository;

    @Autowired
    private FraisMapper fraisMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restFraisMockMvc;

    private Frais frais;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Frais createEntity(EntityManager em) {
        Frais frais = new Frais()
            .valeurFrais(DEFAULT_VALEUR_FRAIS)
            .descriptionFrais(DEFAULT_DESCRIPTION_FRAIS)
            .fraispourAssimileYN(DEFAULT_FRAISPOUR_ASSIMILE_YN)
            .cycle(DEFAULT_CYCLE)
            .dia(DEFAULT_DIA)
            .dip(DEFAULT_DIP)
            .dipPrivee(DEFAULT_DIP_PRIVEE)
            .dateApplication(DEFAULT_DATE_APPLICATION)
            .dateFin(DEFAULT_DATE_FIN)
            .estEnApplicationYN(DEFAULT_EST_EN_APPLICATION_YN);
        return frais;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Frais createUpdatedEntity(EntityManager em) {
        Frais frais = new Frais()
            .valeurFrais(UPDATED_VALEUR_FRAIS)
            .descriptionFrais(UPDATED_DESCRIPTION_FRAIS)
            .fraispourAssimileYN(UPDATED_FRAISPOUR_ASSIMILE_YN)
            .cycle(UPDATED_CYCLE)
            .dia(UPDATED_DIA)
            .dip(UPDATED_DIP)
            .dipPrivee(UPDATED_DIP_PRIVEE)
            .dateApplication(UPDATED_DATE_APPLICATION)
            .dateFin(UPDATED_DATE_FIN)
            .estEnApplicationYN(UPDATED_EST_EN_APPLICATION_YN);
        return frais;
    }

    @BeforeEach
    public void initTest() {
        frais = createEntity(em);
    }

    @Test
    @Transactional
    void createFrais() throws Exception {
        int databaseSizeBeforeCreate = fraisRepository.findAll().size();
        // Create the Frais
        FraisDTO fraisDTO = fraisMapper.toDto(frais);
        restFraisMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(fraisDTO))
            )
            .andExpect(status().isCreated());

        // Validate the Frais in the database
        List<Frais> fraisList = fraisRepository.findAll();
        assertThat(fraisList).hasSize(databaseSizeBeforeCreate + 1);
        Frais testFrais = fraisList.get(fraisList.size() - 1);
        assertThat(testFrais.getValeurFrais()).isEqualTo(DEFAULT_VALEUR_FRAIS);
        assertThat(testFrais.getDescriptionFrais()).isEqualTo(DEFAULT_DESCRIPTION_FRAIS);
        assertThat(testFrais.getFraispourAssimileYN()).isEqualTo(DEFAULT_FRAISPOUR_ASSIMILE_YN);
        assertThat(testFrais.getCycle()).isEqualTo(DEFAULT_CYCLE);
        assertThat(testFrais.getDia()).isEqualTo(DEFAULT_DIA);
        assertThat(testFrais.getDip()).isEqualTo(DEFAULT_DIP);
        assertThat(testFrais.getDipPrivee()).isEqualTo(DEFAULT_DIP_PRIVEE);
        assertThat(testFrais.getDateApplication()).isEqualTo(DEFAULT_DATE_APPLICATION);
        assertThat(testFrais.getDateFin()).isEqualTo(DEFAULT_DATE_FIN);
        assertThat(testFrais.getEstEnApplicationYN()).isEqualTo(DEFAULT_EST_EN_APPLICATION_YN);
    }

    @Test
    @Transactional
    void createFraisWithExistingId() throws Exception {
        // Create the Frais with an existing ID
        frais.setId(1L);
        FraisDTO fraisDTO = fraisMapper.toDto(frais);

        int databaseSizeBeforeCreate = fraisRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restFraisMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(fraisDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Frais in the database
        List<Frais> fraisList = fraisRepository.findAll();
        assertThat(fraisList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllFrais() throws Exception {
        // Initialize the database
        fraisRepository.saveAndFlush(frais);

        // Get all the fraisList
        restFraisMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(frais.getId().intValue())))
            .andExpect(jsonPath("$.[*].valeurFrais").value(hasItem(DEFAULT_VALEUR_FRAIS.doubleValue())))
            .andExpect(jsonPath("$.[*].descriptionFrais").value(hasItem(DEFAULT_DESCRIPTION_FRAIS)))
            .andExpect(jsonPath("$.[*].fraispourAssimileYN").value(hasItem(DEFAULT_FRAISPOUR_ASSIMILE_YN)))
            .andExpect(jsonPath("$.[*].cycle").value(hasItem(DEFAULT_CYCLE.toString())))
            .andExpect(jsonPath("$.[*].dia").value(hasItem(DEFAULT_DIA.doubleValue())))
            .andExpect(jsonPath("$.[*].dip").value(hasItem(DEFAULT_DIP.doubleValue())))
            .andExpect(jsonPath("$.[*].dipPrivee").value(hasItem(DEFAULT_DIP_PRIVEE.doubleValue())))
            .andExpect(jsonPath("$.[*].dateApplication").value(hasItem(DEFAULT_DATE_APPLICATION.toString())))
            .andExpect(jsonPath("$.[*].dateFin").value(hasItem(DEFAULT_DATE_FIN.toString())))
            .andExpect(jsonPath("$.[*].estEnApplicationYN").value(hasItem(DEFAULT_EST_EN_APPLICATION_YN)));
    }

    @Test
    @Transactional
    void getFrais() throws Exception {
        // Initialize the database
        fraisRepository.saveAndFlush(frais);

        // Get the frais
        restFraisMockMvc
            .perform(get(ENTITY_API_URL_ID, frais.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(frais.getId().intValue()))
            .andExpect(jsonPath("$.valeurFrais").value(DEFAULT_VALEUR_FRAIS.doubleValue()))
            .andExpect(jsonPath("$.descriptionFrais").value(DEFAULT_DESCRIPTION_FRAIS))
            .andExpect(jsonPath("$.fraispourAssimileYN").value(DEFAULT_FRAISPOUR_ASSIMILE_YN))
            .andExpect(jsonPath("$.cycle").value(DEFAULT_CYCLE.toString()))
            .andExpect(jsonPath("$.dia").value(DEFAULT_DIA.doubleValue()))
            .andExpect(jsonPath("$.dip").value(DEFAULT_DIP.doubleValue()))
            .andExpect(jsonPath("$.dipPrivee").value(DEFAULT_DIP_PRIVEE.doubleValue()))
            .andExpect(jsonPath("$.dateApplication").value(DEFAULT_DATE_APPLICATION.toString()))
            .andExpect(jsonPath("$.dateFin").value(DEFAULT_DATE_FIN.toString()))
            .andExpect(jsonPath("$.estEnApplicationYN").value(DEFAULT_EST_EN_APPLICATION_YN));
    }

    @Test
    @Transactional
    void getNonExistingFrais() throws Exception {
        // Get the frais
        restFraisMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingFrais() throws Exception {
        // Initialize the database
        fraisRepository.saveAndFlush(frais);

        int databaseSizeBeforeUpdate = fraisRepository.findAll().size();

        // Update the frais
        Frais updatedFrais = fraisRepository.findById(frais.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedFrais are not directly saved in db
        em.detach(updatedFrais);
        updatedFrais
            .valeurFrais(UPDATED_VALEUR_FRAIS)
            .descriptionFrais(UPDATED_DESCRIPTION_FRAIS)
            .fraispourAssimileYN(UPDATED_FRAISPOUR_ASSIMILE_YN)
            .cycle(UPDATED_CYCLE)
            .dia(UPDATED_DIA)
            .dip(UPDATED_DIP)
            .dipPrivee(UPDATED_DIP_PRIVEE)
            .dateApplication(UPDATED_DATE_APPLICATION)
            .dateFin(UPDATED_DATE_FIN)
            .estEnApplicationYN(UPDATED_EST_EN_APPLICATION_YN);
        FraisDTO fraisDTO = fraisMapper.toDto(updatedFrais);

        restFraisMockMvc
            .perform(
                put(ENTITY_API_URL_ID, fraisDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(fraisDTO))
            )
            .andExpect(status().isOk());

        // Validate the Frais in the database
        List<Frais> fraisList = fraisRepository.findAll();
        assertThat(fraisList).hasSize(databaseSizeBeforeUpdate);
        Frais testFrais = fraisList.get(fraisList.size() - 1);
        assertThat(testFrais.getValeurFrais()).isEqualTo(UPDATED_VALEUR_FRAIS);
        assertThat(testFrais.getDescriptionFrais()).isEqualTo(UPDATED_DESCRIPTION_FRAIS);
        assertThat(testFrais.getFraispourAssimileYN()).isEqualTo(UPDATED_FRAISPOUR_ASSIMILE_YN);
        assertThat(testFrais.getCycle()).isEqualTo(UPDATED_CYCLE);
        assertThat(testFrais.getDia()).isEqualTo(UPDATED_DIA);
        assertThat(testFrais.getDip()).isEqualTo(UPDATED_DIP);
        assertThat(testFrais.getDipPrivee()).isEqualTo(UPDATED_DIP_PRIVEE);
        assertThat(testFrais.getDateApplication()).isEqualTo(UPDATED_DATE_APPLICATION);
        assertThat(testFrais.getDateFin()).isEqualTo(UPDATED_DATE_FIN);
        assertThat(testFrais.getEstEnApplicationYN()).isEqualTo(UPDATED_EST_EN_APPLICATION_YN);
    }

    @Test
    @Transactional
    void putNonExistingFrais() throws Exception {
        int databaseSizeBeforeUpdate = fraisRepository.findAll().size();
        frais.setId(longCount.incrementAndGet());

        // Create the Frais
        FraisDTO fraisDTO = fraisMapper.toDto(frais);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFraisMockMvc
            .perform(
                put(ENTITY_API_URL_ID, fraisDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(fraisDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Frais in the database
        List<Frais> fraisList = fraisRepository.findAll();
        assertThat(fraisList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchFrais() throws Exception {
        int databaseSizeBeforeUpdate = fraisRepository.findAll().size();
        frais.setId(longCount.incrementAndGet());

        // Create the Frais
        FraisDTO fraisDTO = fraisMapper.toDto(frais);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFraisMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(fraisDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Frais in the database
        List<Frais> fraisList = fraisRepository.findAll();
        assertThat(fraisList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamFrais() throws Exception {
        int databaseSizeBeforeUpdate = fraisRepository.findAll().size();
        frais.setId(longCount.incrementAndGet());

        // Create the Frais
        FraisDTO fraisDTO = fraisMapper.toDto(frais);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFraisMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(fraisDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Frais in the database
        List<Frais> fraisList = fraisRepository.findAll();
        assertThat(fraisList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateFraisWithPatch() throws Exception {
        // Initialize the database
        fraisRepository.saveAndFlush(frais);

        int databaseSizeBeforeUpdate = fraisRepository.findAll().size();

        // Update the frais using partial update
        Frais partialUpdatedFrais = new Frais();
        partialUpdatedFrais.setId(frais.getId());

        partialUpdatedFrais
            .fraispourAssimileYN(UPDATED_FRAISPOUR_ASSIMILE_YN)
            .dia(UPDATED_DIA)
            .dip(UPDATED_DIP)
            .dateApplication(UPDATED_DATE_APPLICATION);

        restFraisMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedFrais.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedFrais))
            )
            .andExpect(status().isOk());

        // Validate the Frais in the database
        List<Frais> fraisList = fraisRepository.findAll();
        assertThat(fraisList).hasSize(databaseSizeBeforeUpdate);
        Frais testFrais = fraisList.get(fraisList.size() - 1);
        assertThat(testFrais.getValeurFrais()).isEqualTo(DEFAULT_VALEUR_FRAIS);
        assertThat(testFrais.getDescriptionFrais()).isEqualTo(DEFAULT_DESCRIPTION_FRAIS);
        assertThat(testFrais.getFraispourAssimileYN()).isEqualTo(UPDATED_FRAISPOUR_ASSIMILE_YN);
        assertThat(testFrais.getCycle()).isEqualTo(DEFAULT_CYCLE);
        assertThat(testFrais.getDia()).isEqualTo(UPDATED_DIA);
        assertThat(testFrais.getDip()).isEqualTo(UPDATED_DIP);
        assertThat(testFrais.getDipPrivee()).isEqualTo(DEFAULT_DIP_PRIVEE);
        assertThat(testFrais.getDateApplication()).isEqualTo(UPDATED_DATE_APPLICATION);
        assertThat(testFrais.getDateFin()).isEqualTo(DEFAULT_DATE_FIN);
        assertThat(testFrais.getEstEnApplicationYN()).isEqualTo(DEFAULT_EST_EN_APPLICATION_YN);
    }

    @Test
    @Transactional
    void fullUpdateFraisWithPatch() throws Exception {
        // Initialize the database
        fraisRepository.saveAndFlush(frais);

        int databaseSizeBeforeUpdate = fraisRepository.findAll().size();

        // Update the frais using partial update
        Frais partialUpdatedFrais = new Frais();
        partialUpdatedFrais.setId(frais.getId());

        partialUpdatedFrais
            .valeurFrais(UPDATED_VALEUR_FRAIS)
            .descriptionFrais(UPDATED_DESCRIPTION_FRAIS)
            .fraispourAssimileYN(UPDATED_FRAISPOUR_ASSIMILE_YN)
            .cycle(UPDATED_CYCLE)
            .dia(UPDATED_DIA)
            .dip(UPDATED_DIP)
            .dipPrivee(UPDATED_DIP_PRIVEE)
            .dateApplication(UPDATED_DATE_APPLICATION)
            .dateFin(UPDATED_DATE_FIN)
            .estEnApplicationYN(UPDATED_EST_EN_APPLICATION_YN);

        restFraisMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedFrais.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedFrais))
            )
            .andExpect(status().isOk());

        // Validate the Frais in the database
        List<Frais> fraisList = fraisRepository.findAll();
        assertThat(fraisList).hasSize(databaseSizeBeforeUpdate);
        Frais testFrais = fraisList.get(fraisList.size() - 1);
        assertThat(testFrais.getValeurFrais()).isEqualTo(UPDATED_VALEUR_FRAIS);
        assertThat(testFrais.getDescriptionFrais()).isEqualTo(UPDATED_DESCRIPTION_FRAIS);
        assertThat(testFrais.getFraispourAssimileYN()).isEqualTo(UPDATED_FRAISPOUR_ASSIMILE_YN);
        assertThat(testFrais.getCycle()).isEqualTo(UPDATED_CYCLE);
        assertThat(testFrais.getDia()).isEqualTo(UPDATED_DIA);
        assertThat(testFrais.getDip()).isEqualTo(UPDATED_DIP);
        assertThat(testFrais.getDipPrivee()).isEqualTo(UPDATED_DIP_PRIVEE);
        assertThat(testFrais.getDateApplication()).isEqualTo(UPDATED_DATE_APPLICATION);
        assertThat(testFrais.getDateFin()).isEqualTo(UPDATED_DATE_FIN);
        assertThat(testFrais.getEstEnApplicationYN()).isEqualTo(UPDATED_EST_EN_APPLICATION_YN);
    }

    @Test
    @Transactional
    void patchNonExistingFrais() throws Exception {
        int databaseSizeBeforeUpdate = fraisRepository.findAll().size();
        frais.setId(longCount.incrementAndGet());

        // Create the Frais
        FraisDTO fraisDTO = fraisMapper.toDto(frais);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFraisMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, fraisDTO.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(fraisDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Frais in the database
        List<Frais> fraisList = fraisRepository.findAll();
        assertThat(fraisList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchFrais() throws Exception {
        int databaseSizeBeforeUpdate = fraisRepository.findAll().size();
        frais.setId(longCount.incrementAndGet());

        // Create the Frais
        FraisDTO fraisDTO = fraisMapper.toDto(frais);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFraisMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(fraisDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Frais in the database
        List<Frais> fraisList = fraisRepository.findAll();
        assertThat(fraisList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamFrais() throws Exception {
        int databaseSizeBeforeUpdate = fraisRepository.findAll().size();
        frais.setId(longCount.incrementAndGet());

        // Create the Frais
        FraisDTO fraisDTO = fraisMapper.toDto(frais);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFraisMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(fraisDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Frais in the database
        List<Frais> fraisList = fraisRepository.findAll();
        assertThat(fraisList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteFrais() throws Exception {
        // Initialize the database
        fraisRepository.saveAndFlush(frais);

        int databaseSizeBeforeDelete = fraisRepository.findAll().size();

        // Delete the frais
        restFraisMockMvc
            .perform(delete(ENTITY_API_URL_ID, frais.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Frais> fraisList = fraisRepository.findAll();
        assertThat(fraisList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
