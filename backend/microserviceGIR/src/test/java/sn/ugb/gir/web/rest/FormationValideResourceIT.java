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
import sn.ugb.gir.domain.FormationValide;
import sn.ugb.gir.repository.FormationValideRepository;
import sn.ugb.gir.service.dto.FormationValideDTO;
import sn.ugb.gir.service.mapper.FormationValideMapper;

/**
 * Integration tests for the {@link FormationValideResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class FormationValideResourceIT {

    private static final Integer DEFAULT_VALIDE_YN = 1;
    private static final Integer UPDATED_VALIDE_YN = 2;

    private static final LocalDate DEFAULT_DATE_DEBUT = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE_DEBUT = LocalDate.now(ZoneId.systemDefault());

    private static final LocalDate DEFAULT_DATE_FIN = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE_FIN = LocalDate.now(ZoneId.systemDefault());

    private static final String ENTITY_API_URL = "/api/formation-valides";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private FormationValideRepository formationValideRepository;

    @Autowired
    private FormationValideMapper formationValideMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restFormationValideMockMvc;

    private FormationValide formationValide;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static FormationValide createEntity(EntityManager em) {
        FormationValide formationValide = new FormationValide()
            .valideYN(DEFAULT_VALIDE_YN)
            .dateDebut(DEFAULT_DATE_DEBUT)
            .dateFin(DEFAULT_DATE_FIN);
        return formationValide;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static FormationValide createUpdatedEntity(EntityManager em) {
        FormationValide formationValide = new FormationValide()
            .valideYN(UPDATED_VALIDE_YN)
            .dateDebut(UPDATED_DATE_DEBUT)
            .dateFin(UPDATED_DATE_FIN);
        return formationValide;
    }

    @BeforeEach
    public void initTest() {
        formationValide = createEntity(em);
    }

    @Test
    @Transactional
    void createFormationValide() throws Exception {
        int databaseSizeBeforeCreate = formationValideRepository.findAll().size();
        // Create the FormationValide
        FormationValideDTO formationValideDTO = formationValideMapper.toDto(formationValide);
        restFormationValideMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(formationValideDTO))
            )
            .andExpect(status().isCreated());

        // Validate the FormationValide in the database
        List<FormationValide> formationValideList = formationValideRepository.findAll();
        assertThat(formationValideList).hasSize(databaseSizeBeforeCreate + 1);
        FormationValide testFormationValide = formationValideList.get(formationValideList.size() - 1);
        assertThat(testFormationValide.getValideYN()).isEqualTo(DEFAULT_VALIDE_YN);
        assertThat(testFormationValide.getDateDebut()).isEqualTo(DEFAULT_DATE_DEBUT);
        assertThat(testFormationValide.getDateFin()).isEqualTo(DEFAULT_DATE_FIN);
    }

    @Test
    @Transactional
    void createFormationValideWithExistingId() throws Exception {
        // Create the FormationValide with an existing ID
        formationValide.setId(1L);
        FormationValideDTO formationValideDTO = formationValideMapper.toDto(formationValide);

        int databaseSizeBeforeCreate = formationValideRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restFormationValideMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(formationValideDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the FormationValide in the database
        List<FormationValide> formationValideList = formationValideRepository.findAll();
        assertThat(formationValideList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllFormationValides() throws Exception {
        // Initialize the database
        formationValideRepository.saveAndFlush(formationValide);

        // Get all the formationValideList
        restFormationValideMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(formationValide.getId().intValue())))
            .andExpect(jsonPath("$.[*].valideYN").value(hasItem(DEFAULT_VALIDE_YN)))
            .andExpect(jsonPath("$.[*].dateDebut").value(hasItem(DEFAULT_DATE_DEBUT.toString())))
            .andExpect(jsonPath("$.[*].dateFin").value(hasItem(DEFAULT_DATE_FIN.toString())));
    }

    @Test
    @Transactional
    void getFormationValide() throws Exception {
        // Initialize the database
        formationValideRepository.saveAndFlush(formationValide);

        // Get the formationValide
        restFormationValideMockMvc
            .perform(get(ENTITY_API_URL_ID, formationValide.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(formationValide.getId().intValue()))
            .andExpect(jsonPath("$.valideYN").value(DEFAULT_VALIDE_YN))
            .andExpect(jsonPath("$.dateDebut").value(DEFAULT_DATE_DEBUT.toString()))
            .andExpect(jsonPath("$.dateFin").value(DEFAULT_DATE_FIN.toString()));
    }

    @Test
    @Transactional
    void getNonExistingFormationValide() throws Exception {
        // Get the formationValide
        restFormationValideMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingFormationValide() throws Exception {
        // Initialize the database
        formationValideRepository.saveAndFlush(formationValide);

        int databaseSizeBeforeUpdate = formationValideRepository.findAll().size();

        // Update the formationValide
        FormationValide updatedFormationValide = formationValideRepository.findById(formationValide.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedFormationValide are not directly saved in db
        em.detach(updatedFormationValide);
        updatedFormationValide.valideYN(UPDATED_VALIDE_YN).dateDebut(UPDATED_DATE_DEBUT).dateFin(UPDATED_DATE_FIN);
        FormationValideDTO formationValideDTO = formationValideMapper.toDto(updatedFormationValide);

        restFormationValideMockMvc
            .perform(
                put(ENTITY_API_URL_ID, formationValideDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(formationValideDTO))
            )
            .andExpect(status().isOk());

        // Validate the FormationValide in the database
        List<FormationValide> formationValideList = formationValideRepository.findAll();
        assertThat(formationValideList).hasSize(databaseSizeBeforeUpdate);
        FormationValide testFormationValide = formationValideList.get(formationValideList.size() - 1);
        assertThat(testFormationValide.getValideYN()).isEqualTo(UPDATED_VALIDE_YN);
        assertThat(testFormationValide.getDateDebut()).isEqualTo(UPDATED_DATE_DEBUT);
        assertThat(testFormationValide.getDateFin()).isEqualTo(UPDATED_DATE_FIN);
    }

    @Test
    @Transactional
    void putNonExistingFormationValide() throws Exception {
        int databaseSizeBeforeUpdate = formationValideRepository.findAll().size();
        formationValide.setId(longCount.incrementAndGet());

        // Create the FormationValide
        FormationValideDTO formationValideDTO = formationValideMapper.toDto(formationValide);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFormationValideMockMvc
            .perform(
                put(ENTITY_API_URL_ID, formationValideDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(formationValideDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the FormationValide in the database
        List<FormationValide> formationValideList = formationValideRepository.findAll();
        assertThat(formationValideList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchFormationValide() throws Exception {
        int databaseSizeBeforeUpdate = formationValideRepository.findAll().size();
        formationValide.setId(longCount.incrementAndGet());

        // Create the FormationValide
        FormationValideDTO formationValideDTO = formationValideMapper.toDto(formationValide);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFormationValideMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(formationValideDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the FormationValide in the database
        List<FormationValide> formationValideList = formationValideRepository.findAll();
        assertThat(formationValideList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamFormationValide() throws Exception {
        int databaseSizeBeforeUpdate = formationValideRepository.findAll().size();
        formationValide.setId(longCount.incrementAndGet());

        // Create the FormationValide
        FormationValideDTO formationValideDTO = formationValideMapper.toDto(formationValide);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFormationValideMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(formationValideDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the FormationValide in the database
        List<FormationValide> formationValideList = formationValideRepository.findAll();
        assertThat(formationValideList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateFormationValideWithPatch() throws Exception {
        // Initialize the database
        formationValideRepository.saveAndFlush(formationValide);

        int databaseSizeBeforeUpdate = formationValideRepository.findAll().size();

        // Update the formationValide using partial update
        FormationValide partialUpdatedFormationValide = new FormationValide();
        partialUpdatedFormationValide.setId(formationValide.getId());

        partialUpdatedFormationValide.valideYN(UPDATED_VALIDE_YN).dateDebut(UPDATED_DATE_DEBUT).dateFin(UPDATED_DATE_FIN);

        restFormationValideMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedFormationValide.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedFormationValide))
            )
            .andExpect(status().isOk());

        // Validate the FormationValide in the database
        List<FormationValide> formationValideList = formationValideRepository.findAll();
        assertThat(formationValideList).hasSize(databaseSizeBeforeUpdate);
        FormationValide testFormationValide = formationValideList.get(formationValideList.size() - 1);
        assertThat(testFormationValide.getValideYN()).isEqualTo(UPDATED_VALIDE_YN);
        assertThat(testFormationValide.getDateDebut()).isEqualTo(UPDATED_DATE_DEBUT);
        assertThat(testFormationValide.getDateFin()).isEqualTo(UPDATED_DATE_FIN);
    }

    @Test
    @Transactional
    void fullUpdateFormationValideWithPatch() throws Exception {
        // Initialize the database
        formationValideRepository.saveAndFlush(formationValide);

        int databaseSizeBeforeUpdate = formationValideRepository.findAll().size();

        // Update the formationValide using partial update
        FormationValide partialUpdatedFormationValide = new FormationValide();
        partialUpdatedFormationValide.setId(formationValide.getId());

        partialUpdatedFormationValide.valideYN(UPDATED_VALIDE_YN).dateDebut(UPDATED_DATE_DEBUT).dateFin(UPDATED_DATE_FIN);

        restFormationValideMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedFormationValide.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedFormationValide))
            )
            .andExpect(status().isOk());

        // Validate the FormationValide in the database
        List<FormationValide> formationValideList = formationValideRepository.findAll();
        assertThat(formationValideList).hasSize(databaseSizeBeforeUpdate);
        FormationValide testFormationValide = formationValideList.get(formationValideList.size() - 1);
        assertThat(testFormationValide.getValideYN()).isEqualTo(UPDATED_VALIDE_YN);
        assertThat(testFormationValide.getDateDebut()).isEqualTo(UPDATED_DATE_DEBUT);
        assertThat(testFormationValide.getDateFin()).isEqualTo(UPDATED_DATE_FIN);
    }

    @Test
    @Transactional
    void patchNonExistingFormationValide() throws Exception {
        int databaseSizeBeforeUpdate = formationValideRepository.findAll().size();
        formationValide.setId(longCount.incrementAndGet());

        // Create the FormationValide
        FormationValideDTO formationValideDTO = formationValideMapper.toDto(formationValide);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFormationValideMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, formationValideDTO.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(formationValideDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the FormationValide in the database
        List<FormationValide> formationValideList = formationValideRepository.findAll();
        assertThat(formationValideList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchFormationValide() throws Exception {
        int databaseSizeBeforeUpdate = formationValideRepository.findAll().size();
        formationValide.setId(longCount.incrementAndGet());

        // Create the FormationValide
        FormationValideDTO formationValideDTO = formationValideMapper.toDto(formationValide);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFormationValideMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(formationValideDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the FormationValide in the database
        List<FormationValide> formationValideList = formationValideRepository.findAll();
        assertThat(formationValideList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamFormationValide() throws Exception {
        int databaseSizeBeforeUpdate = formationValideRepository.findAll().size();
        formationValide.setId(longCount.incrementAndGet());

        // Create the FormationValide
        FormationValideDTO formationValideDTO = formationValideMapper.toDto(formationValide);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFormationValideMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(formationValideDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the FormationValide in the database
        List<FormationValide> formationValideList = formationValideRepository.findAll();
        assertThat(formationValideList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteFormationValide() throws Exception {
        // Initialize the database
        formationValideRepository.saveAndFlush(formationValide);

        int databaseSizeBeforeDelete = formationValideRepository.findAll().size();

        // Delete the formationValide
        restFormationValideMockMvc
            .perform(delete(ENTITY_API_URL_ID, formationValide.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<FormationValide> formationValideList = formationValideRepository.findAll();
        assertThat(formationValideList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
