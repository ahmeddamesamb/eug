package sn.ugb.gir.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import jakarta.persistence.EntityManager;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;
import org.apache.commons.collections4.IterableUtils;
import org.assertj.core.util.IterableUtil;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import sn.ugb.gir.IntegrationTest;
import sn.ugb.gir.domain.FormationInvalide;
import sn.ugb.gir.repository.FormationInvalideRepository;
import sn.ugb.gir.repository.search.FormationInvalideSearchRepository;
import sn.ugb.gir.service.dto.FormationInvalideDTO;
import sn.ugb.gir.service.mapper.FormationInvalideMapper;

/**
 * Integration tests for the {@link FormationInvalideResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class FormationInvalideResourceIT {

    private static final Boolean DEFAULT_ACTIF_YN = false;
    private static final Boolean UPDATED_ACTIF_YN = true;

    private static final String ENTITY_API_URL = "/api/formation-invalides";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/formation-invalides/_search";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private FormationInvalideRepository formationInvalideRepository;

    @Autowired
    private FormationInvalideMapper formationInvalideMapper;

    @Autowired
    private FormationInvalideSearchRepository formationInvalideSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restFormationInvalideMockMvc;

    private FormationInvalide formationInvalide;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static FormationInvalide createEntity(EntityManager em) {
        FormationInvalide formationInvalide = new FormationInvalide().actifYN(DEFAULT_ACTIF_YN);
        return formationInvalide;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static FormationInvalide createUpdatedEntity(EntityManager em) {
        FormationInvalide formationInvalide = new FormationInvalide().actifYN(UPDATED_ACTIF_YN);
        return formationInvalide;
    }

    @AfterEach
    public void cleanupElasticSearchRepository() {
        formationInvalideSearchRepository.deleteAll();
        assertThat(formationInvalideSearchRepository.count()).isEqualTo(0);
    }

    @BeforeEach
    public void initTest() {
        formationInvalide = createEntity(em);
    }

    @Test
    @Transactional
    void createFormationInvalide() throws Exception {
        int databaseSizeBeforeCreate = formationInvalideRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(formationInvalideSearchRepository.findAll());
        // Create the FormationInvalide
        FormationInvalideDTO formationInvalideDTO = formationInvalideMapper.toDto(formationInvalide);
        restFormationInvalideMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(formationInvalideDTO))
            )
            .andExpect(status().isCreated());

        // Validate the FormationInvalide in the database
        List<FormationInvalide> formationInvalideList = formationInvalideRepository.findAll();
        assertThat(formationInvalideList).hasSize(databaseSizeBeforeCreate + 1);
        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(formationInvalideSearchRepository.findAll());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore + 1);
            });
        FormationInvalide testFormationInvalide = formationInvalideList.get(formationInvalideList.size() - 1);
        assertThat(testFormationInvalide.getActifYN()).isEqualTo(DEFAULT_ACTIF_YN);
    }

    @Test
    @Transactional
    void createFormationInvalideWithExistingId() throws Exception {
        // Create the FormationInvalide with an existing ID
        formationInvalide.setId(1L);
        FormationInvalideDTO formationInvalideDTO = formationInvalideMapper.toDto(formationInvalide);

        int databaseSizeBeforeCreate = formationInvalideRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(formationInvalideSearchRepository.findAll());

        // An entity with an existing ID cannot be created, so this API call must fail
        restFormationInvalideMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(formationInvalideDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the FormationInvalide in the database
        List<FormationInvalide> formationInvalideList = formationInvalideRepository.findAll();
        assertThat(formationInvalideList).hasSize(databaseSizeBeforeCreate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(formationInvalideSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void getAllFormationInvalides() throws Exception {
        // Initialize the database
        formationInvalideRepository.saveAndFlush(formationInvalide);

        // Get all the formationInvalideList
        restFormationInvalideMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(formationInvalide.getId().intValue())))
            .andExpect(jsonPath("$.[*].actifYN").value(hasItem(DEFAULT_ACTIF_YN.booleanValue())));
    }

    @Test
    @Transactional
    void getFormationInvalide() throws Exception {
        // Initialize the database
        formationInvalideRepository.saveAndFlush(formationInvalide);

        // Get the formationInvalide
        restFormationInvalideMockMvc
            .perform(get(ENTITY_API_URL_ID, formationInvalide.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(formationInvalide.getId().intValue()))
            .andExpect(jsonPath("$.actifYN").value(DEFAULT_ACTIF_YN.booleanValue()));
    }

    @Test
    @Transactional
    void getNonExistingFormationInvalide() throws Exception {
        // Get the formationInvalide
        restFormationInvalideMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingFormationInvalide() throws Exception {
        // Initialize the database
        formationInvalideRepository.saveAndFlush(formationInvalide);

        int databaseSizeBeforeUpdate = formationInvalideRepository.findAll().size();
        formationInvalideSearchRepository.save(formationInvalide);
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(formationInvalideSearchRepository.findAll());

        // Update the formationInvalide
        FormationInvalide updatedFormationInvalide = formationInvalideRepository.findById(formationInvalide.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedFormationInvalide are not directly saved in db
        em.detach(updatedFormationInvalide);
        updatedFormationInvalide.actifYN(UPDATED_ACTIF_YN);
        FormationInvalideDTO formationInvalideDTO = formationInvalideMapper.toDto(updatedFormationInvalide);

        restFormationInvalideMockMvc
            .perform(
                put(ENTITY_API_URL_ID, formationInvalideDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(formationInvalideDTO))
            )
            .andExpect(status().isOk());

        // Validate the FormationInvalide in the database
        List<FormationInvalide> formationInvalideList = formationInvalideRepository.findAll();
        assertThat(formationInvalideList).hasSize(databaseSizeBeforeUpdate);
        FormationInvalide testFormationInvalide = formationInvalideList.get(formationInvalideList.size() - 1);
        assertThat(testFormationInvalide.getActifYN()).isEqualTo(UPDATED_ACTIF_YN);
        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(formationInvalideSearchRepository.findAll());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
                List<FormationInvalide> formationInvalideSearchList = IterableUtils.toList(formationInvalideSearchRepository.findAll());
                FormationInvalide testFormationInvalideSearch = formationInvalideSearchList.get(searchDatabaseSizeAfter - 1);
                assertThat(testFormationInvalideSearch.getActifYN()).isEqualTo(UPDATED_ACTIF_YN);
            });
    }

    @Test
    @Transactional
    void putNonExistingFormationInvalide() throws Exception {
        int databaseSizeBeforeUpdate = formationInvalideRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(formationInvalideSearchRepository.findAll());
        formationInvalide.setId(longCount.incrementAndGet());

        // Create the FormationInvalide
        FormationInvalideDTO formationInvalideDTO = formationInvalideMapper.toDto(formationInvalide);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFormationInvalideMockMvc
            .perform(
                put(ENTITY_API_URL_ID, formationInvalideDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(formationInvalideDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the FormationInvalide in the database
        List<FormationInvalide> formationInvalideList = formationInvalideRepository.findAll();
        assertThat(formationInvalideList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(formationInvalideSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void putWithIdMismatchFormationInvalide() throws Exception {
        int databaseSizeBeforeUpdate = formationInvalideRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(formationInvalideSearchRepository.findAll());
        formationInvalide.setId(longCount.incrementAndGet());

        // Create the FormationInvalide
        FormationInvalideDTO formationInvalideDTO = formationInvalideMapper.toDto(formationInvalide);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFormationInvalideMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(formationInvalideDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the FormationInvalide in the database
        List<FormationInvalide> formationInvalideList = formationInvalideRepository.findAll();
        assertThat(formationInvalideList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(formationInvalideSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamFormationInvalide() throws Exception {
        int databaseSizeBeforeUpdate = formationInvalideRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(formationInvalideSearchRepository.findAll());
        formationInvalide.setId(longCount.incrementAndGet());

        // Create the FormationInvalide
        FormationInvalideDTO formationInvalideDTO = formationInvalideMapper.toDto(formationInvalide);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFormationInvalideMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(formationInvalideDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the FormationInvalide in the database
        List<FormationInvalide> formationInvalideList = formationInvalideRepository.findAll();
        assertThat(formationInvalideList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(formationInvalideSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void partialUpdateFormationInvalideWithPatch() throws Exception {
        // Initialize the database
        formationInvalideRepository.saveAndFlush(formationInvalide);

        int databaseSizeBeforeUpdate = formationInvalideRepository.findAll().size();

        // Update the formationInvalide using partial update
        FormationInvalide partialUpdatedFormationInvalide = new FormationInvalide();
        partialUpdatedFormationInvalide.setId(formationInvalide.getId());

        restFormationInvalideMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedFormationInvalide.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedFormationInvalide))
            )
            .andExpect(status().isOk());

        // Validate the FormationInvalide in the database
        List<FormationInvalide> formationInvalideList = formationInvalideRepository.findAll();
        assertThat(formationInvalideList).hasSize(databaseSizeBeforeUpdate);
        FormationInvalide testFormationInvalide = formationInvalideList.get(formationInvalideList.size() - 1);
        assertThat(testFormationInvalide.getActifYN()).isEqualTo(DEFAULT_ACTIF_YN);
    }

    @Test
    @Transactional
    void fullUpdateFormationInvalideWithPatch() throws Exception {
        // Initialize the database
        formationInvalideRepository.saveAndFlush(formationInvalide);

        int databaseSizeBeforeUpdate = formationInvalideRepository.findAll().size();

        // Update the formationInvalide using partial update
        FormationInvalide partialUpdatedFormationInvalide = new FormationInvalide();
        partialUpdatedFormationInvalide.setId(formationInvalide.getId());

        partialUpdatedFormationInvalide.actifYN(UPDATED_ACTIF_YN);

        restFormationInvalideMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedFormationInvalide.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedFormationInvalide))
            )
            .andExpect(status().isOk());

        // Validate the FormationInvalide in the database
        List<FormationInvalide> formationInvalideList = formationInvalideRepository.findAll();
        assertThat(formationInvalideList).hasSize(databaseSizeBeforeUpdate);
        FormationInvalide testFormationInvalide = formationInvalideList.get(formationInvalideList.size() - 1);
        assertThat(testFormationInvalide.getActifYN()).isEqualTo(UPDATED_ACTIF_YN);
    }

    @Test
    @Transactional
    void patchNonExistingFormationInvalide() throws Exception {
        int databaseSizeBeforeUpdate = formationInvalideRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(formationInvalideSearchRepository.findAll());
        formationInvalide.setId(longCount.incrementAndGet());

        // Create the FormationInvalide
        FormationInvalideDTO formationInvalideDTO = formationInvalideMapper.toDto(formationInvalide);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFormationInvalideMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, formationInvalideDTO.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(formationInvalideDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the FormationInvalide in the database
        List<FormationInvalide> formationInvalideList = formationInvalideRepository.findAll();
        assertThat(formationInvalideList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(formationInvalideSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void patchWithIdMismatchFormationInvalide() throws Exception {
        int databaseSizeBeforeUpdate = formationInvalideRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(formationInvalideSearchRepository.findAll());
        formationInvalide.setId(longCount.incrementAndGet());

        // Create the FormationInvalide
        FormationInvalideDTO formationInvalideDTO = formationInvalideMapper.toDto(formationInvalide);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFormationInvalideMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(formationInvalideDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the FormationInvalide in the database
        List<FormationInvalide> formationInvalideList = formationInvalideRepository.findAll();
        assertThat(formationInvalideList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(formationInvalideSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamFormationInvalide() throws Exception {
        int databaseSizeBeforeUpdate = formationInvalideRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(formationInvalideSearchRepository.findAll());
        formationInvalide.setId(longCount.incrementAndGet());

        // Create the FormationInvalide
        FormationInvalideDTO formationInvalideDTO = formationInvalideMapper.toDto(formationInvalide);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFormationInvalideMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(formationInvalideDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the FormationInvalide in the database
        List<FormationInvalide> formationInvalideList = formationInvalideRepository.findAll();
        assertThat(formationInvalideList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(formationInvalideSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void deleteFormationInvalide() throws Exception {
        // Initialize the database
        formationInvalideRepository.saveAndFlush(formationInvalide);
        formationInvalideRepository.save(formationInvalide);
        formationInvalideSearchRepository.save(formationInvalide);

        int databaseSizeBeforeDelete = formationInvalideRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(formationInvalideSearchRepository.findAll());
        assertThat(searchDatabaseSizeBefore).isEqualTo(databaseSizeBeforeDelete);

        // Delete the formationInvalide
        restFormationInvalideMockMvc
            .perform(delete(ENTITY_API_URL_ID, formationInvalide.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<FormationInvalide> formationInvalideList = formationInvalideRepository.findAll();
        assertThat(formationInvalideList).hasSize(databaseSizeBeforeDelete - 1);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(formationInvalideSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore - 1);
    }

    @Test
    @Transactional
    void searchFormationInvalide() throws Exception {
        // Initialize the database
        formationInvalide = formationInvalideRepository.saveAndFlush(formationInvalide);
        formationInvalideSearchRepository.save(formationInvalide);

        // Search the formationInvalide
        restFormationInvalideMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + formationInvalide.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(formationInvalide.getId().intValue())))
            .andExpect(jsonPath("$.[*].actifYN").value(hasItem(DEFAULT_ACTIF_YN.booleanValue())));
    }
}
