package sn.ugb.deliberation.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import jakarta.persistence.EntityManager;
import java.util.Base64;
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
import sn.ugb.deliberation.IntegrationTest;
import sn.ugb.deliberation.domain.Deliberation;
import sn.ugb.deliberation.repository.DeliberationRepository;
import sn.ugb.deliberation.service.dto.DeliberationDTO;
import sn.ugb.deliberation.service.mapper.DeliberationMapper;

/**
 * Integration tests for the {@link DeliberationResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class DeliberationResourceIT {

    private static final Integer DEFAULT_EST_VALIDEE_YN = 1;
    private static final Integer UPDATED_EST_VALIDEE_YN = 2;

    private static final byte[] DEFAULT_PV_DELIBERATION = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_PV_DELIBERATION = TestUtil.createByteArray(1, "1");
    private static final String DEFAULT_PV_DELIBERATION_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_PV_DELIBERATION_CONTENT_TYPE = "image/png";

    private static final String ENTITY_API_URL = "/api/deliberations";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private DeliberationRepository deliberationRepository;

    @Autowired
    private DeliberationMapper deliberationMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restDeliberationMockMvc;

    private Deliberation deliberation;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Deliberation createEntity(EntityManager em) {
        Deliberation deliberation = new Deliberation()
            .estValideeYN(DEFAULT_EST_VALIDEE_YN)
            .pvDeliberation(DEFAULT_PV_DELIBERATION)
            .pvDeliberationContentType(DEFAULT_PV_DELIBERATION_CONTENT_TYPE);
        return deliberation;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Deliberation createUpdatedEntity(EntityManager em) {
        Deliberation deliberation = new Deliberation()
            .estValideeYN(UPDATED_EST_VALIDEE_YN)
            .pvDeliberation(UPDATED_PV_DELIBERATION)
            .pvDeliberationContentType(UPDATED_PV_DELIBERATION_CONTENT_TYPE);
        return deliberation;
    }

    @BeforeEach
    public void initTest() {
        deliberation = createEntity(em);
    }

    @Test
    @Transactional
    void createDeliberation() throws Exception {
        int databaseSizeBeforeCreate = deliberationRepository.findAll().size();
        // Create the Deliberation
        DeliberationDTO deliberationDTO = deliberationMapper.toDto(deliberation);
        restDeliberationMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(deliberationDTO))
            )
            .andExpect(status().isCreated());

        // Validate the Deliberation in the database
        List<Deliberation> deliberationList = deliberationRepository.findAll();
        assertThat(deliberationList).hasSize(databaseSizeBeforeCreate + 1);
        Deliberation testDeliberation = deliberationList.get(deliberationList.size() - 1);
        assertThat(testDeliberation.getEstValideeYN()).isEqualTo(DEFAULT_EST_VALIDEE_YN);
        assertThat(testDeliberation.getPvDeliberation()).isEqualTo(DEFAULT_PV_DELIBERATION);
        assertThat(testDeliberation.getPvDeliberationContentType()).isEqualTo(DEFAULT_PV_DELIBERATION_CONTENT_TYPE);
    }

    @Test
    @Transactional
    void createDeliberationWithExistingId() throws Exception {
        // Create the Deliberation with an existing ID
        deliberation.setId(1L);
        DeliberationDTO deliberationDTO = deliberationMapper.toDto(deliberation);

        int databaseSizeBeforeCreate = deliberationRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restDeliberationMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(deliberationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Deliberation in the database
        List<Deliberation> deliberationList = deliberationRepository.findAll();
        assertThat(deliberationList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllDeliberations() throws Exception {
        // Initialize the database
        deliberationRepository.saveAndFlush(deliberation);

        // Get all the deliberationList
        restDeliberationMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(deliberation.getId().intValue())))
            .andExpect(jsonPath("$.[*].estValideeYN").value(hasItem(DEFAULT_EST_VALIDEE_YN)))
            .andExpect(jsonPath("$.[*].pvDeliberationContentType").value(hasItem(DEFAULT_PV_DELIBERATION_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].pvDeliberation").value(hasItem(Base64.getEncoder().encodeToString(DEFAULT_PV_DELIBERATION))));
    }

    @Test
    @Transactional
    void getDeliberation() throws Exception {
        // Initialize the database
        deliberationRepository.saveAndFlush(deliberation);

        // Get the deliberation
        restDeliberationMockMvc
            .perform(get(ENTITY_API_URL_ID, deliberation.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(deliberation.getId().intValue()))
            .andExpect(jsonPath("$.estValideeYN").value(DEFAULT_EST_VALIDEE_YN))
            .andExpect(jsonPath("$.pvDeliberationContentType").value(DEFAULT_PV_DELIBERATION_CONTENT_TYPE))
            .andExpect(jsonPath("$.pvDeliberation").value(Base64.getEncoder().encodeToString(DEFAULT_PV_DELIBERATION)));
    }

    @Test
    @Transactional
    void getNonExistingDeliberation() throws Exception {
        // Get the deliberation
        restDeliberationMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingDeliberation() throws Exception {
        // Initialize the database
        deliberationRepository.saveAndFlush(deliberation);

        int databaseSizeBeforeUpdate = deliberationRepository.findAll().size();

        // Update the deliberation
        Deliberation updatedDeliberation = deliberationRepository.findById(deliberation.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedDeliberation are not directly saved in db
        em.detach(updatedDeliberation);
        updatedDeliberation
            .estValideeYN(UPDATED_EST_VALIDEE_YN)
            .pvDeliberation(UPDATED_PV_DELIBERATION)
            .pvDeliberationContentType(UPDATED_PV_DELIBERATION_CONTENT_TYPE);
        DeliberationDTO deliberationDTO = deliberationMapper.toDto(updatedDeliberation);

        restDeliberationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, deliberationDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(deliberationDTO))
            )
            .andExpect(status().isOk());

        // Validate the Deliberation in the database
        List<Deliberation> deliberationList = deliberationRepository.findAll();
        assertThat(deliberationList).hasSize(databaseSizeBeforeUpdate);
        Deliberation testDeliberation = deliberationList.get(deliberationList.size() - 1);
        assertThat(testDeliberation.getEstValideeYN()).isEqualTo(UPDATED_EST_VALIDEE_YN);
        assertThat(testDeliberation.getPvDeliberation()).isEqualTo(UPDATED_PV_DELIBERATION);
        assertThat(testDeliberation.getPvDeliberationContentType()).isEqualTo(UPDATED_PV_DELIBERATION_CONTENT_TYPE);
    }

    @Test
    @Transactional
    void putNonExistingDeliberation() throws Exception {
        int databaseSizeBeforeUpdate = deliberationRepository.findAll().size();
        deliberation.setId(longCount.incrementAndGet());

        // Create the Deliberation
        DeliberationDTO deliberationDTO = deliberationMapper.toDto(deliberation);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDeliberationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, deliberationDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(deliberationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Deliberation in the database
        List<Deliberation> deliberationList = deliberationRepository.findAll();
        assertThat(deliberationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchDeliberation() throws Exception {
        int databaseSizeBeforeUpdate = deliberationRepository.findAll().size();
        deliberation.setId(longCount.incrementAndGet());

        // Create the Deliberation
        DeliberationDTO deliberationDTO = deliberationMapper.toDto(deliberation);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDeliberationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(deliberationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Deliberation in the database
        List<Deliberation> deliberationList = deliberationRepository.findAll();
        assertThat(deliberationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamDeliberation() throws Exception {
        int databaseSizeBeforeUpdate = deliberationRepository.findAll().size();
        deliberation.setId(longCount.incrementAndGet());

        // Create the Deliberation
        DeliberationDTO deliberationDTO = deliberationMapper.toDto(deliberation);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDeliberationMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(deliberationDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Deliberation in the database
        List<Deliberation> deliberationList = deliberationRepository.findAll();
        assertThat(deliberationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateDeliberationWithPatch() throws Exception {
        // Initialize the database
        deliberationRepository.saveAndFlush(deliberation);

        int databaseSizeBeforeUpdate = deliberationRepository.findAll().size();

        // Update the deliberation using partial update
        Deliberation partialUpdatedDeliberation = new Deliberation();
        partialUpdatedDeliberation.setId(deliberation.getId());

        partialUpdatedDeliberation.estValideeYN(UPDATED_EST_VALIDEE_YN);

        restDeliberationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDeliberation.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedDeliberation))
            )
            .andExpect(status().isOk());

        // Validate the Deliberation in the database
        List<Deliberation> deliberationList = deliberationRepository.findAll();
        assertThat(deliberationList).hasSize(databaseSizeBeforeUpdate);
        Deliberation testDeliberation = deliberationList.get(deliberationList.size() - 1);
        assertThat(testDeliberation.getEstValideeYN()).isEqualTo(UPDATED_EST_VALIDEE_YN);
        assertThat(testDeliberation.getPvDeliberation()).isEqualTo(DEFAULT_PV_DELIBERATION);
        assertThat(testDeliberation.getPvDeliberationContentType()).isEqualTo(DEFAULT_PV_DELIBERATION_CONTENT_TYPE);
    }

    @Test
    @Transactional
    void fullUpdateDeliberationWithPatch() throws Exception {
        // Initialize the database
        deliberationRepository.saveAndFlush(deliberation);

        int databaseSizeBeforeUpdate = deliberationRepository.findAll().size();

        // Update the deliberation using partial update
        Deliberation partialUpdatedDeliberation = new Deliberation();
        partialUpdatedDeliberation.setId(deliberation.getId());

        partialUpdatedDeliberation
            .estValideeYN(UPDATED_EST_VALIDEE_YN)
            .pvDeliberation(UPDATED_PV_DELIBERATION)
            .pvDeliberationContentType(UPDATED_PV_DELIBERATION_CONTENT_TYPE);

        restDeliberationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDeliberation.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedDeliberation))
            )
            .andExpect(status().isOk());

        // Validate the Deliberation in the database
        List<Deliberation> deliberationList = deliberationRepository.findAll();
        assertThat(deliberationList).hasSize(databaseSizeBeforeUpdate);
        Deliberation testDeliberation = deliberationList.get(deliberationList.size() - 1);
        assertThat(testDeliberation.getEstValideeYN()).isEqualTo(UPDATED_EST_VALIDEE_YN);
        assertThat(testDeliberation.getPvDeliberation()).isEqualTo(UPDATED_PV_DELIBERATION);
        assertThat(testDeliberation.getPvDeliberationContentType()).isEqualTo(UPDATED_PV_DELIBERATION_CONTENT_TYPE);
    }

    @Test
    @Transactional
    void patchNonExistingDeliberation() throws Exception {
        int databaseSizeBeforeUpdate = deliberationRepository.findAll().size();
        deliberation.setId(longCount.incrementAndGet());

        // Create the Deliberation
        DeliberationDTO deliberationDTO = deliberationMapper.toDto(deliberation);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDeliberationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, deliberationDTO.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(deliberationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Deliberation in the database
        List<Deliberation> deliberationList = deliberationRepository.findAll();
        assertThat(deliberationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchDeliberation() throws Exception {
        int databaseSizeBeforeUpdate = deliberationRepository.findAll().size();
        deliberation.setId(longCount.incrementAndGet());

        // Create the Deliberation
        DeliberationDTO deliberationDTO = deliberationMapper.toDto(deliberation);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDeliberationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(deliberationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Deliberation in the database
        List<Deliberation> deliberationList = deliberationRepository.findAll();
        assertThat(deliberationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamDeliberation() throws Exception {
        int databaseSizeBeforeUpdate = deliberationRepository.findAll().size();
        deliberation.setId(longCount.incrementAndGet());

        // Create the Deliberation
        DeliberationDTO deliberationDTO = deliberationMapper.toDto(deliberation);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDeliberationMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(deliberationDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Deliberation in the database
        List<Deliberation> deliberationList = deliberationRepository.findAll();
        assertThat(deliberationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteDeliberation() throws Exception {
        // Initialize the database
        deliberationRepository.saveAndFlush(deliberation);

        int databaseSizeBeforeDelete = deliberationRepository.findAll().size();

        // Delete the deliberation
        restDeliberationMockMvc
            .perform(delete(ENTITY_API_URL_ID, deliberation.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Deliberation> deliberationList = deliberationRepository.findAll();
        assertThat(deliberationList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
