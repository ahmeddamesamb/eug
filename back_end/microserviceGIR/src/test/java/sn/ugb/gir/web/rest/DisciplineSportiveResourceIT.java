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
import sn.ugb.gir.domain.DisciplineSportive;
import sn.ugb.gir.repository.DisciplineSportiveRepository;
import sn.ugb.gir.service.dto.DisciplineSportiveDTO;
import sn.ugb.gir.service.mapper.DisciplineSportiveMapper;

/**
 * Integration tests for the {@link DisciplineSportiveResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class DisciplineSportiveResourceIT {

    private static final String DEFAULT_LIBELLE_DISCIPLINE_SPORTIVE = "AAAAAAAAAA";
    private static final String UPDATED_LIBELLE_DISCIPLINE_SPORTIVE = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/discipline-sportives";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private DisciplineSportiveRepository disciplineSportiveRepository;

    @Autowired
    private DisciplineSportiveMapper disciplineSportiveMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restDisciplineSportiveMockMvc;

    private DisciplineSportive disciplineSportive;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static DisciplineSportive createEntity(EntityManager em) {
        DisciplineSportive disciplineSportive = new DisciplineSportive().libelleDisciplineSportive(DEFAULT_LIBELLE_DISCIPLINE_SPORTIVE);
        return disciplineSportive;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static DisciplineSportive createUpdatedEntity(EntityManager em) {
        DisciplineSportive disciplineSportive = new DisciplineSportive().libelleDisciplineSportive(UPDATED_LIBELLE_DISCIPLINE_SPORTIVE);
        return disciplineSportive;
    }

    @BeforeEach
    public void initTest() {
        disciplineSportive = createEntity(em);
    }

    @Test
    @Transactional
    void createDisciplineSportive() throws Exception {
        int databaseSizeBeforeCreate = disciplineSportiveRepository.findAll().size();
        // Create the DisciplineSportive
        DisciplineSportiveDTO disciplineSportiveDTO = disciplineSportiveMapper.toDto(disciplineSportive);
        restDisciplineSportiveMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(disciplineSportiveDTO))
            )
            .andExpect(status().isCreated());

        // Validate the DisciplineSportive in the database
        List<DisciplineSportive> disciplineSportiveList = disciplineSportiveRepository.findAll();
        assertThat(disciplineSportiveList).hasSize(databaseSizeBeforeCreate + 1);
        DisciplineSportive testDisciplineSportive = disciplineSportiveList.get(disciplineSportiveList.size() - 1);
        assertThat(testDisciplineSportive.getLibelleDisciplineSportive()).isEqualTo(DEFAULT_LIBELLE_DISCIPLINE_SPORTIVE);
    }

    @Test
    @Transactional
    void createDisciplineSportiveWithExistingId() throws Exception {
        // Create the DisciplineSportive with an existing ID
        disciplineSportive.setId(1L);
        DisciplineSportiveDTO disciplineSportiveDTO = disciplineSportiveMapper.toDto(disciplineSportive);

        int databaseSizeBeforeCreate = disciplineSportiveRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restDisciplineSportiveMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(disciplineSportiveDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the DisciplineSportive in the database
        List<DisciplineSportive> disciplineSportiveList = disciplineSportiveRepository.findAll();
        assertThat(disciplineSportiveList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkLibelleDisciplineSportiveIsRequired() throws Exception {
        int databaseSizeBeforeTest = disciplineSportiveRepository.findAll().size();
        // set the field null
        disciplineSportive.setLibelleDisciplineSportive(null);

        // Create the DisciplineSportive, which fails.
        DisciplineSportiveDTO disciplineSportiveDTO = disciplineSportiveMapper.toDto(disciplineSportive);

        restDisciplineSportiveMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(disciplineSportiveDTO))
            )
            .andExpect(status().isBadRequest());

        List<DisciplineSportive> disciplineSportiveList = disciplineSportiveRepository.findAll();
        assertThat(disciplineSportiveList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllDisciplineSportives() throws Exception {
        // Initialize the database
        disciplineSportiveRepository.saveAndFlush(disciplineSportive);

        // Get all the disciplineSportiveList
        restDisciplineSportiveMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(disciplineSportive.getId().intValue())))
            .andExpect(jsonPath("$.[*].libelleDisciplineSportive").value(hasItem(DEFAULT_LIBELLE_DISCIPLINE_SPORTIVE)));
    }

    @Test
    @Transactional
    void getDisciplineSportive() throws Exception {
        // Initialize the database
        disciplineSportiveRepository.saveAndFlush(disciplineSportive);

        // Get the disciplineSportive
        restDisciplineSportiveMockMvc
            .perform(get(ENTITY_API_URL_ID, disciplineSportive.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(disciplineSportive.getId().intValue()))
            .andExpect(jsonPath("$.libelleDisciplineSportive").value(DEFAULT_LIBELLE_DISCIPLINE_SPORTIVE));
    }

    @Test
    @Transactional
    void getNonExistingDisciplineSportive() throws Exception {
        // Get the disciplineSportive
        restDisciplineSportiveMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingDisciplineSportive() throws Exception {
        // Initialize the database
        disciplineSportiveRepository.saveAndFlush(disciplineSportive);

        int databaseSizeBeforeUpdate = disciplineSportiveRepository.findAll().size();

        // Update the disciplineSportive
        DisciplineSportive updatedDisciplineSportive = disciplineSportiveRepository.findById(disciplineSportive.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedDisciplineSportive are not directly saved in db
        em.detach(updatedDisciplineSportive);
        updatedDisciplineSportive.libelleDisciplineSportive(UPDATED_LIBELLE_DISCIPLINE_SPORTIVE);
        DisciplineSportiveDTO disciplineSportiveDTO = disciplineSportiveMapper.toDto(updatedDisciplineSportive);

        restDisciplineSportiveMockMvc
            .perform(
                put(ENTITY_API_URL_ID, disciplineSportiveDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(disciplineSportiveDTO))
            )
            .andExpect(status().isOk());

        // Validate the DisciplineSportive in the database
        List<DisciplineSportive> disciplineSportiveList = disciplineSportiveRepository.findAll();
        assertThat(disciplineSportiveList).hasSize(databaseSizeBeforeUpdate);
        DisciplineSportive testDisciplineSportive = disciplineSportiveList.get(disciplineSportiveList.size() - 1);
        assertThat(testDisciplineSportive.getLibelleDisciplineSportive()).isEqualTo(UPDATED_LIBELLE_DISCIPLINE_SPORTIVE);
    }

    @Test
    @Transactional
    void putNonExistingDisciplineSportive() throws Exception {
        int databaseSizeBeforeUpdate = disciplineSportiveRepository.findAll().size();
        disciplineSportive.setId(longCount.incrementAndGet());

        // Create the DisciplineSportive
        DisciplineSportiveDTO disciplineSportiveDTO = disciplineSportiveMapper.toDto(disciplineSportive);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDisciplineSportiveMockMvc
            .perform(
                put(ENTITY_API_URL_ID, disciplineSportiveDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(disciplineSportiveDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the DisciplineSportive in the database
        List<DisciplineSportive> disciplineSportiveList = disciplineSportiveRepository.findAll();
        assertThat(disciplineSportiveList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchDisciplineSportive() throws Exception {
        int databaseSizeBeforeUpdate = disciplineSportiveRepository.findAll().size();
        disciplineSportive.setId(longCount.incrementAndGet());

        // Create the DisciplineSportive
        DisciplineSportiveDTO disciplineSportiveDTO = disciplineSportiveMapper.toDto(disciplineSportive);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDisciplineSportiveMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(disciplineSportiveDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the DisciplineSportive in the database
        List<DisciplineSportive> disciplineSportiveList = disciplineSportiveRepository.findAll();
        assertThat(disciplineSportiveList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamDisciplineSportive() throws Exception {
        int databaseSizeBeforeUpdate = disciplineSportiveRepository.findAll().size();
        disciplineSportive.setId(longCount.incrementAndGet());

        // Create the DisciplineSportive
        DisciplineSportiveDTO disciplineSportiveDTO = disciplineSportiveMapper.toDto(disciplineSportive);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDisciplineSportiveMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(disciplineSportiveDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the DisciplineSportive in the database
        List<DisciplineSportive> disciplineSportiveList = disciplineSportiveRepository.findAll();
        assertThat(disciplineSportiveList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateDisciplineSportiveWithPatch() throws Exception {
        // Initialize the database
        disciplineSportiveRepository.saveAndFlush(disciplineSportive);

        int databaseSizeBeforeUpdate = disciplineSportiveRepository.findAll().size();

        // Update the disciplineSportive using partial update
        DisciplineSportive partialUpdatedDisciplineSportive = new DisciplineSportive();
        partialUpdatedDisciplineSportive.setId(disciplineSportive.getId());

        restDisciplineSportiveMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDisciplineSportive.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedDisciplineSportive))
            )
            .andExpect(status().isOk());

        // Validate the DisciplineSportive in the database
        List<DisciplineSportive> disciplineSportiveList = disciplineSportiveRepository.findAll();
        assertThat(disciplineSportiveList).hasSize(databaseSizeBeforeUpdate);
        DisciplineSportive testDisciplineSportive = disciplineSportiveList.get(disciplineSportiveList.size() - 1);
        assertThat(testDisciplineSportive.getLibelleDisciplineSportive()).isEqualTo(DEFAULT_LIBELLE_DISCIPLINE_SPORTIVE);
    }

    @Test
    @Transactional
    void fullUpdateDisciplineSportiveWithPatch() throws Exception {
        // Initialize the database
        disciplineSportiveRepository.saveAndFlush(disciplineSportive);

        int databaseSizeBeforeUpdate = disciplineSportiveRepository.findAll().size();

        // Update the disciplineSportive using partial update
        DisciplineSportive partialUpdatedDisciplineSportive = new DisciplineSportive();
        partialUpdatedDisciplineSportive.setId(disciplineSportive.getId());

        partialUpdatedDisciplineSportive.libelleDisciplineSportive(UPDATED_LIBELLE_DISCIPLINE_SPORTIVE);

        restDisciplineSportiveMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDisciplineSportive.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedDisciplineSportive))
            )
            .andExpect(status().isOk());

        // Validate the DisciplineSportive in the database
        List<DisciplineSportive> disciplineSportiveList = disciplineSportiveRepository.findAll();
        assertThat(disciplineSportiveList).hasSize(databaseSizeBeforeUpdate);
        DisciplineSportive testDisciplineSportive = disciplineSportiveList.get(disciplineSportiveList.size() - 1);
        assertThat(testDisciplineSportive.getLibelleDisciplineSportive()).isEqualTo(UPDATED_LIBELLE_DISCIPLINE_SPORTIVE);
    }

    @Test
    @Transactional
    void patchNonExistingDisciplineSportive() throws Exception {
        int databaseSizeBeforeUpdate = disciplineSportiveRepository.findAll().size();
        disciplineSportive.setId(longCount.incrementAndGet());

        // Create the DisciplineSportive
        DisciplineSportiveDTO disciplineSportiveDTO = disciplineSportiveMapper.toDto(disciplineSportive);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDisciplineSportiveMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, disciplineSportiveDTO.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(disciplineSportiveDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the DisciplineSportive in the database
        List<DisciplineSportive> disciplineSportiveList = disciplineSportiveRepository.findAll();
        assertThat(disciplineSportiveList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchDisciplineSportive() throws Exception {
        int databaseSizeBeforeUpdate = disciplineSportiveRepository.findAll().size();
        disciplineSportive.setId(longCount.incrementAndGet());

        // Create the DisciplineSportive
        DisciplineSportiveDTO disciplineSportiveDTO = disciplineSportiveMapper.toDto(disciplineSportive);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDisciplineSportiveMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(disciplineSportiveDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the DisciplineSportive in the database
        List<DisciplineSportive> disciplineSportiveList = disciplineSportiveRepository.findAll();
        assertThat(disciplineSportiveList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamDisciplineSportive() throws Exception {
        int databaseSizeBeforeUpdate = disciplineSportiveRepository.findAll().size();
        disciplineSportive.setId(longCount.incrementAndGet());

        // Create the DisciplineSportive
        DisciplineSportiveDTO disciplineSportiveDTO = disciplineSportiveMapper.toDto(disciplineSportive);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDisciplineSportiveMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(disciplineSportiveDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the DisciplineSportive in the database
        List<DisciplineSportive> disciplineSportiveList = disciplineSportiveRepository.findAll();
        assertThat(disciplineSportiveList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteDisciplineSportive() throws Exception {
        // Initialize the database
        disciplineSportiveRepository.saveAndFlush(disciplineSportive);

        int databaseSizeBeforeDelete = disciplineSportiveRepository.findAll().size();

        // Delete the disciplineSportive
        restDisciplineSportiveMockMvc
            .perform(delete(ENTITY_API_URL_ID, disciplineSportive.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<DisciplineSportive> disciplineSportiveList = disciplineSportiveRepository.findAll();
        assertThat(disciplineSportiveList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
