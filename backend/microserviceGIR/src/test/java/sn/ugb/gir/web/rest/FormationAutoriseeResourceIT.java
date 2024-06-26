package sn.ugb.gir.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import jakarta.persistence.EntityManager;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;
import org.apache.commons.collections4.IterableUtils;
import org.assertj.core.util.IterableUtil;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import sn.ugb.gir.IntegrationTest;
import sn.ugb.gir.domain.FormationAutorisee;
import sn.ugb.gir.repository.FormationAutoriseeRepository;
import sn.ugb.gir.repository.search.FormationAutoriseeSearchRepository;
import sn.ugb.gir.service.FormationAutoriseeService;
import sn.ugb.gir.service.dto.FormationAutoriseeDTO;
import sn.ugb.gir.service.mapper.FormationAutoriseeMapper;

/**
 * Integration tests for the {@link FormationAutoriseeResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class FormationAutoriseeResourceIT {

    private static final LocalDate DEFAULT_DATE_DEBUT = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE_DEBUT = LocalDate.now(ZoneId.systemDefault());

    private static final LocalDate DEFAULT_DATE_FIN = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE_FIN = LocalDate.now(ZoneId.systemDefault());

    private static final Boolean DEFAULT_EN_COURS_YN = false;
    private static final Boolean UPDATED_EN_COURS_YN = true;

    private static final Boolean DEFAULT_ACTIF_YN = false;
    private static final Boolean UPDATED_ACTIF_YN = true;

    private static final String ENTITY_API_URL = "/api/formation-autorisees";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/formation-autorisees/_search";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private FormationAutoriseeRepository formationAutoriseeRepository;

    @Mock
    private FormationAutoriseeRepository formationAutoriseeRepositoryMock;

    @Autowired
    private FormationAutoriseeMapper formationAutoriseeMapper;

    @Mock
    private FormationAutoriseeService formationAutoriseeServiceMock;

    @Autowired
    private FormationAutoriseeSearchRepository formationAutoriseeSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restFormationAutoriseeMockMvc;

    private FormationAutorisee formationAutorisee;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static FormationAutorisee createEntity(EntityManager em) {
        FormationAutorisee formationAutorisee = new FormationAutorisee()
            .dateDebut(DEFAULT_DATE_DEBUT)
            .dateFin(DEFAULT_DATE_FIN)
            .enCoursYN(DEFAULT_EN_COURS_YN)
            .actifYN(DEFAULT_ACTIF_YN);
        return formationAutorisee;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static FormationAutorisee createUpdatedEntity(EntityManager em) {
        FormationAutorisee formationAutorisee = new FormationAutorisee()
            .dateDebut(UPDATED_DATE_DEBUT)
            .dateFin(UPDATED_DATE_FIN)
            .enCoursYN(UPDATED_EN_COURS_YN)
            .actifYN(UPDATED_ACTIF_YN);
        return formationAutorisee;
    }

    @AfterEach
    public void cleanupElasticSearchRepository() {
        formationAutoriseeSearchRepository.deleteAll();
        assertThat(formationAutoriseeSearchRepository.count()).isEqualTo(0);
    }

    @BeforeEach
    public void initTest() {
        formationAutorisee = createEntity(em);
    }

    @Test
    @Transactional
    void createFormationAutorisee() throws Exception {
        int databaseSizeBeforeCreate = formationAutoriseeRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(formationAutoriseeSearchRepository.findAll());
        // Create the FormationAutorisee
        FormationAutoriseeDTO formationAutoriseeDTO = formationAutoriseeMapper.toDto(formationAutorisee);
        restFormationAutoriseeMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(formationAutoriseeDTO))
            )
            .andExpect(status().isCreated());

        // Validate the FormationAutorisee in the database
        List<FormationAutorisee> formationAutoriseeList = formationAutoriseeRepository.findAll();
        assertThat(formationAutoriseeList).hasSize(databaseSizeBeforeCreate + 1);
        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(formationAutoriseeSearchRepository.findAll());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore + 1);
            });
        FormationAutorisee testFormationAutorisee = formationAutoriseeList.get(formationAutoriseeList.size() - 1);
        assertThat(testFormationAutorisee.getDateDebut()).isEqualTo(DEFAULT_DATE_DEBUT);
        assertThat(testFormationAutorisee.getDateFin()).isEqualTo(DEFAULT_DATE_FIN);
        assertThat(testFormationAutorisee.getEnCoursYN()).isEqualTo(DEFAULT_EN_COURS_YN);
        assertThat(testFormationAutorisee.getActifYN()).isEqualTo(DEFAULT_ACTIF_YN);
    }

    @Test
    @Transactional
    void createFormationAutoriseeWithExistingId() throws Exception {
        // Create the FormationAutorisee with an existing ID
        formationAutorisee.setId(1L);
        FormationAutoriseeDTO formationAutoriseeDTO = formationAutoriseeMapper.toDto(formationAutorisee);

        int databaseSizeBeforeCreate = formationAutoriseeRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(formationAutoriseeSearchRepository.findAll());

        // An entity with an existing ID cannot be created, so this API call must fail
        restFormationAutoriseeMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(formationAutoriseeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the FormationAutorisee in the database
        List<FormationAutorisee> formationAutoriseeList = formationAutoriseeRepository.findAll();
        assertThat(formationAutoriseeList).hasSize(databaseSizeBeforeCreate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(formationAutoriseeSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void getAllFormationAutorisees() throws Exception {
        // Initialize the database
        formationAutoriseeRepository.saveAndFlush(formationAutorisee);

        // Get all the formationAutoriseeList
        restFormationAutoriseeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(formationAutorisee.getId().intValue())))
            .andExpect(jsonPath("$.[*].dateDebut").value(hasItem(DEFAULT_DATE_DEBUT.toString())))
            .andExpect(jsonPath("$.[*].dateFin").value(hasItem(DEFAULT_DATE_FIN.toString())))
            .andExpect(jsonPath("$.[*].enCoursYN").value(hasItem(DEFAULT_EN_COURS_YN.booleanValue())))
            .andExpect(jsonPath("$.[*].actifYN").value(hasItem(DEFAULT_ACTIF_YN.booleanValue())));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllFormationAutoriseesWithEagerRelationshipsIsEnabled() throws Exception {
        when(formationAutoriseeServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restFormationAutoriseeMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(formationAutoriseeServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllFormationAutoriseesWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(formationAutoriseeServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restFormationAutoriseeMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(formationAutoriseeRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getFormationAutorisee() throws Exception {
        // Initialize the database
        formationAutoriseeRepository.saveAndFlush(formationAutorisee);

        // Get the formationAutorisee
        restFormationAutoriseeMockMvc
            .perform(get(ENTITY_API_URL_ID, formationAutorisee.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(formationAutorisee.getId().intValue()))
            .andExpect(jsonPath("$.dateDebut").value(DEFAULT_DATE_DEBUT.toString()))
            .andExpect(jsonPath("$.dateFin").value(DEFAULT_DATE_FIN.toString()))
            .andExpect(jsonPath("$.enCoursYN").value(DEFAULT_EN_COURS_YN.booleanValue()))
            .andExpect(jsonPath("$.actifYN").value(DEFAULT_ACTIF_YN.booleanValue()));
    }

    @Test
    @Transactional
    void getNonExistingFormationAutorisee() throws Exception {
        // Get the formationAutorisee
        restFormationAutoriseeMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingFormationAutorisee() throws Exception {
        // Initialize the database
        formationAutoriseeRepository.saveAndFlush(formationAutorisee);

        int databaseSizeBeforeUpdate = formationAutoriseeRepository.findAll().size();
        formationAutoriseeSearchRepository.save(formationAutorisee);
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(formationAutoriseeSearchRepository.findAll());

        // Update the formationAutorisee
        FormationAutorisee updatedFormationAutorisee = formationAutoriseeRepository.findById(formationAutorisee.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedFormationAutorisee are not directly saved in db
        em.detach(updatedFormationAutorisee);
        updatedFormationAutorisee
            .dateDebut(UPDATED_DATE_DEBUT)
            .dateFin(UPDATED_DATE_FIN)
            .enCoursYN(UPDATED_EN_COURS_YN)
            .actifYN(UPDATED_ACTIF_YN);
        FormationAutoriseeDTO formationAutoriseeDTO = formationAutoriseeMapper.toDto(updatedFormationAutorisee);

        restFormationAutoriseeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, formationAutoriseeDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(formationAutoriseeDTO))
            )
            .andExpect(status().isOk());

        // Validate the FormationAutorisee in the database
        List<FormationAutorisee> formationAutoriseeList = formationAutoriseeRepository.findAll();
        assertThat(formationAutoriseeList).hasSize(databaseSizeBeforeUpdate);
        FormationAutorisee testFormationAutorisee = formationAutoriseeList.get(formationAutoriseeList.size() - 1);
        assertThat(testFormationAutorisee.getDateDebut()).isEqualTo(UPDATED_DATE_DEBUT);
        assertThat(testFormationAutorisee.getDateFin()).isEqualTo(UPDATED_DATE_FIN);
        assertThat(testFormationAutorisee.getEnCoursYN()).isEqualTo(UPDATED_EN_COURS_YN);
        assertThat(testFormationAutorisee.getActifYN()).isEqualTo(UPDATED_ACTIF_YN);
        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(formationAutoriseeSearchRepository.findAll());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
                List<FormationAutorisee> formationAutoriseeSearchList = IterableUtils.toList(formationAutoriseeSearchRepository.findAll());
                FormationAutorisee testFormationAutoriseeSearch = formationAutoriseeSearchList.get(searchDatabaseSizeAfter - 1);
                assertThat(testFormationAutoriseeSearch.getDateDebut()).isEqualTo(UPDATED_DATE_DEBUT);
                assertThat(testFormationAutoriseeSearch.getDateFin()).isEqualTo(UPDATED_DATE_FIN);
                assertThat(testFormationAutoriseeSearch.getEnCoursYN()).isEqualTo(UPDATED_EN_COURS_YN);
                assertThat(testFormationAutoriseeSearch.getActifYN()).isEqualTo(UPDATED_ACTIF_YN);
            });
    }

    @Test
    @Transactional
    void putNonExistingFormationAutorisee() throws Exception {
        int databaseSizeBeforeUpdate = formationAutoriseeRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(formationAutoriseeSearchRepository.findAll());
        formationAutorisee.setId(longCount.incrementAndGet());

        // Create the FormationAutorisee
        FormationAutoriseeDTO formationAutoriseeDTO = formationAutoriseeMapper.toDto(formationAutorisee);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFormationAutoriseeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, formationAutoriseeDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(formationAutoriseeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the FormationAutorisee in the database
        List<FormationAutorisee> formationAutoriseeList = formationAutoriseeRepository.findAll();
        assertThat(formationAutoriseeList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(formationAutoriseeSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void putWithIdMismatchFormationAutorisee() throws Exception {
        int databaseSizeBeforeUpdate = formationAutoriseeRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(formationAutoriseeSearchRepository.findAll());
        formationAutorisee.setId(longCount.incrementAndGet());

        // Create the FormationAutorisee
        FormationAutoriseeDTO formationAutoriseeDTO = formationAutoriseeMapper.toDto(formationAutorisee);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFormationAutoriseeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(formationAutoriseeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the FormationAutorisee in the database
        List<FormationAutorisee> formationAutoriseeList = formationAutoriseeRepository.findAll();
        assertThat(formationAutoriseeList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(formationAutoriseeSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamFormationAutorisee() throws Exception {
        int databaseSizeBeforeUpdate = formationAutoriseeRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(formationAutoriseeSearchRepository.findAll());
        formationAutorisee.setId(longCount.incrementAndGet());

        // Create the FormationAutorisee
        FormationAutoriseeDTO formationAutoriseeDTO = formationAutoriseeMapper.toDto(formationAutorisee);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFormationAutoriseeMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(formationAutoriseeDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the FormationAutorisee in the database
        List<FormationAutorisee> formationAutoriseeList = formationAutoriseeRepository.findAll();
        assertThat(formationAutoriseeList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(formationAutoriseeSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void partialUpdateFormationAutoriseeWithPatch() throws Exception {
        // Initialize the database
        formationAutoriseeRepository.saveAndFlush(formationAutorisee);

        int databaseSizeBeforeUpdate = formationAutoriseeRepository.findAll().size();

        // Update the formationAutorisee using partial update
        FormationAutorisee partialUpdatedFormationAutorisee = new FormationAutorisee();
        partialUpdatedFormationAutorisee.setId(formationAutorisee.getId());

        partialUpdatedFormationAutorisee.dateDebut(UPDATED_DATE_DEBUT).enCoursYN(UPDATED_EN_COURS_YN).actifYN(UPDATED_ACTIF_YN);

        restFormationAutoriseeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedFormationAutorisee.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedFormationAutorisee))
            )
            .andExpect(status().isOk());

        // Validate the FormationAutorisee in the database
        List<FormationAutorisee> formationAutoriseeList = formationAutoriseeRepository.findAll();
        assertThat(formationAutoriseeList).hasSize(databaseSizeBeforeUpdate);
        FormationAutorisee testFormationAutorisee = formationAutoriseeList.get(formationAutoriseeList.size() - 1);
        assertThat(testFormationAutorisee.getDateDebut()).isEqualTo(UPDATED_DATE_DEBUT);
        assertThat(testFormationAutorisee.getDateFin()).isEqualTo(DEFAULT_DATE_FIN);
        assertThat(testFormationAutorisee.getEnCoursYN()).isEqualTo(UPDATED_EN_COURS_YN);
        assertThat(testFormationAutorisee.getActifYN()).isEqualTo(UPDATED_ACTIF_YN);
    }

    @Test
    @Transactional
    void fullUpdateFormationAutoriseeWithPatch() throws Exception {
        // Initialize the database
        formationAutoriseeRepository.saveAndFlush(formationAutorisee);

        int databaseSizeBeforeUpdate = formationAutoriseeRepository.findAll().size();

        // Update the formationAutorisee using partial update
        FormationAutorisee partialUpdatedFormationAutorisee = new FormationAutorisee();
        partialUpdatedFormationAutorisee.setId(formationAutorisee.getId());

        partialUpdatedFormationAutorisee
            .dateDebut(UPDATED_DATE_DEBUT)
            .dateFin(UPDATED_DATE_FIN)
            .enCoursYN(UPDATED_EN_COURS_YN)
            .actifYN(UPDATED_ACTIF_YN);

        restFormationAutoriseeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedFormationAutorisee.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedFormationAutorisee))
            )
            .andExpect(status().isOk());

        // Validate the FormationAutorisee in the database
        List<FormationAutorisee> formationAutoriseeList = formationAutoriseeRepository.findAll();
        assertThat(formationAutoriseeList).hasSize(databaseSizeBeforeUpdate);
        FormationAutorisee testFormationAutorisee = formationAutoriseeList.get(formationAutoriseeList.size() - 1);
        assertThat(testFormationAutorisee.getDateDebut()).isEqualTo(UPDATED_DATE_DEBUT);
        assertThat(testFormationAutorisee.getDateFin()).isEqualTo(UPDATED_DATE_FIN);
        assertThat(testFormationAutorisee.getEnCoursYN()).isEqualTo(UPDATED_EN_COURS_YN);
        assertThat(testFormationAutorisee.getActifYN()).isEqualTo(UPDATED_ACTIF_YN);
    }

    @Test
    @Transactional
    void patchNonExistingFormationAutorisee() throws Exception {
        int databaseSizeBeforeUpdate = formationAutoriseeRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(formationAutoriseeSearchRepository.findAll());
        formationAutorisee.setId(longCount.incrementAndGet());

        // Create the FormationAutorisee
        FormationAutoriseeDTO formationAutoriseeDTO = formationAutoriseeMapper.toDto(formationAutorisee);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFormationAutoriseeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, formationAutoriseeDTO.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(formationAutoriseeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the FormationAutorisee in the database
        List<FormationAutorisee> formationAutoriseeList = formationAutoriseeRepository.findAll();
        assertThat(formationAutoriseeList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(formationAutoriseeSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void patchWithIdMismatchFormationAutorisee() throws Exception {
        int databaseSizeBeforeUpdate = formationAutoriseeRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(formationAutoriseeSearchRepository.findAll());
        formationAutorisee.setId(longCount.incrementAndGet());

        // Create the FormationAutorisee
        FormationAutoriseeDTO formationAutoriseeDTO = formationAutoriseeMapper.toDto(formationAutorisee);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFormationAutoriseeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(formationAutoriseeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the FormationAutorisee in the database
        List<FormationAutorisee> formationAutoriseeList = formationAutoriseeRepository.findAll();
        assertThat(formationAutoriseeList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(formationAutoriseeSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamFormationAutorisee() throws Exception {
        int databaseSizeBeforeUpdate = formationAutoriseeRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(formationAutoriseeSearchRepository.findAll());
        formationAutorisee.setId(longCount.incrementAndGet());

        // Create the FormationAutorisee
        FormationAutoriseeDTO formationAutoriseeDTO = formationAutoriseeMapper.toDto(formationAutorisee);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFormationAutoriseeMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(formationAutoriseeDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the FormationAutorisee in the database
        List<FormationAutorisee> formationAutoriseeList = formationAutoriseeRepository.findAll();
        assertThat(formationAutoriseeList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(formationAutoriseeSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void deleteFormationAutorisee() throws Exception {
        // Initialize the database
        formationAutoriseeRepository.saveAndFlush(formationAutorisee);
        formationAutoriseeRepository.save(formationAutorisee);
        formationAutoriseeSearchRepository.save(formationAutorisee);

        int databaseSizeBeforeDelete = formationAutoriseeRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(formationAutoriseeSearchRepository.findAll());
        assertThat(searchDatabaseSizeBefore).isEqualTo(databaseSizeBeforeDelete);

        // Delete the formationAutorisee
        restFormationAutoriseeMockMvc
            .perform(delete(ENTITY_API_URL_ID, formationAutorisee.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<FormationAutorisee> formationAutoriseeList = formationAutoriseeRepository.findAll();
        assertThat(formationAutoriseeList).hasSize(databaseSizeBeforeDelete - 1);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(formationAutoriseeSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore - 1);
    }

    @Test
    @Transactional
    void searchFormationAutorisee() throws Exception {
        // Initialize the database
        formationAutorisee = formationAutoriseeRepository.saveAndFlush(formationAutorisee);
        formationAutoriseeSearchRepository.save(formationAutorisee);

        // Search the formationAutorisee
        restFormationAutoriseeMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + formationAutorisee.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(formationAutorisee.getId().intValue())))
            .andExpect(jsonPath("$.[*].dateDebut").value(hasItem(DEFAULT_DATE_DEBUT.toString())))
            .andExpect(jsonPath("$.[*].dateFin").value(hasItem(DEFAULT_DATE_FIN.toString())))
            .andExpect(jsonPath("$.[*].enCoursYN").value(hasItem(DEFAULT_EN_COURS_YN.booleanValue())))
            .andExpect(jsonPath("$.[*].actifYN").value(hasItem(DEFAULT_ACTIF_YN.booleanValue())));
    }
}
