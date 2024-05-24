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
import sn.ugb.gir.domain.TypeBourse;
import sn.ugb.gir.repository.TypeBourseRepository;
import sn.ugb.gir.service.dto.TypeBourseDTO;
import sn.ugb.gir.service.mapper.TypeBourseMapper;

/**
 * Integration tests for the {@link TypeBourseResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class TypeBourseResourceIT {

    private static final String DEFAULT_LIBELLE_TYPE_BOURSE = "AAAAAAAAAA";
    private static final String UPDATED_LIBELLE_TYPE_BOURSE = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/type-bourses";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private TypeBourseRepository typeBourseRepository;

    @Autowired
    private TypeBourseMapper typeBourseMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restTypeBourseMockMvc;

    private TypeBourse typeBourse;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TypeBourse createEntity(EntityManager em) {
        TypeBourse typeBourse = new TypeBourse().libelleTypeBourse(DEFAULT_LIBELLE_TYPE_BOURSE);
        return typeBourse;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TypeBourse createUpdatedEntity(EntityManager em) {
        TypeBourse typeBourse = new TypeBourse().libelleTypeBourse(UPDATED_LIBELLE_TYPE_BOURSE);
        return typeBourse;
    }

    @BeforeEach
    public void initTest() {
        typeBourse = createEntity(em);
    }

    @Test
    @Transactional
    void createTypeBourse() throws Exception {
        int databaseSizeBeforeCreate = typeBourseRepository.findAll().size();
        // Create the TypeBourse
        TypeBourseDTO typeBourseDTO = typeBourseMapper.toDto(typeBourse);
        restTypeBourseMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(typeBourseDTO))
            )
            .andExpect(status().isCreated());

        // Validate the TypeBourse in the database
        List<TypeBourse> typeBourseList = typeBourseRepository.findAll();
        assertThat(typeBourseList).hasSize(databaseSizeBeforeCreate + 1);
        TypeBourse testTypeBourse = typeBourseList.get(typeBourseList.size() - 1);
        assertThat(testTypeBourse.getLibelleTypeBourse()).isEqualTo(DEFAULT_LIBELLE_TYPE_BOURSE);
    }

    @Test
    @Transactional
    void createTypeBourseWithExistingId() throws Exception {
        // Create the TypeBourse with an existing ID
        typeBourse.setId(1L);
        TypeBourseDTO typeBourseDTO = typeBourseMapper.toDto(typeBourse);

        int databaseSizeBeforeCreate = typeBourseRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restTypeBourseMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(typeBourseDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TypeBourse in the database
        List<TypeBourse> typeBourseList = typeBourseRepository.findAll();
        assertThat(typeBourseList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkLibelleTypeBourseIsRequired() throws Exception {
        int databaseSizeBeforeTest = typeBourseRepository.findAll().size();
        // set the field null
        typeBourse.setLibelleTypeBourse(null);

        // Create the TypeBourse, which fails.
        TypeBourseDTO typeBourseDTO = typeBourseMapper.toDto(typeBourse);

        restTypeBourseMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(typeBourseDTO))
            )
            .andExpect(status().isBadRequest());

        List<TypeBourse> typeBourseList = typeBourseRepository.findAll();
        assertThat(typeBourseList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllTypeBourses() throws Exception {
        // Initialize the database
        typeBourseRepository.saveAndFlush(typeBourse);

        // Get all the typeBourseList
        restTypeBourseMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(typeBourse.getId().intValue())))
            .andExpect(jsonPath("$.[*].libelleTypeBourse").value(hasItem(DEFAULT_LIBELLE_TYPE_BOURSE)));
    }

    @Test
    @Transactional
    void getTypeBourse() throws Exception {
        // Initialize the database
        typeBourseRepository.saveAndFlush(typeBourse);

        // Get the typeBourse
        restTypeBourseMockMvc
            .perform(get(ENTITY_API_URL_ID, typeBourse.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(typeBourse.getId().intValue()))
            .andExpect(jsonPath("$.libelleTypeBourse").value(DEFAULT_LIBELLE_TYPE_BOURSE));
    }

    @Test
    @Transactional
    void getNonExistingTypeBourse() throws Exception {
        // Get the typeBourse
        restTypeBourseMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingTypeBourse() throws Exception {
        // Initialize the database
        typeBourseRepository.saveAndFlush(typeBourse);

        int databaseSizeBeforeUpdate = typeBourseRepository.findAll().size();

        // Update the typeBourse
        TypeBourse updatedTypeBourse = typeBourseRepository.findById(typeBourse.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedTypeBourse are not directly saved in db
        em.detach(updatedTypeBourse);
        updatedTypeBourse.libelleTypeBourse(UPDATED_LIBELLE_TYPE_BOURSE);
        TypeBourseDTO typeBourseDTO = typeBourseMapper.toDto(updatedTypeBourse);

        restTypeBourseMockMvc
            .perform(
                put(ENTITY_API_URL_ID, typeBourseDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(typeBourseDTO))
            )
            .andExpect(status().isOk());

        // Validate the TypeBourse in the database
        List<TypeBourse> typeBourseList = typeBourseRepository.findAll();
        assertThat(typeBourseList).hasSize(databaseSizeBeforeUpdate);
        TypeBourse testTypeBourse = typeBourseList.get(typeBourseList.size() - 1);
        assertThat(testTypeBourse.getLibelleTypeBourse()).isEqualTo(UPDATED_LIBELLE_TYPE_BOURSE);
    }

    @Test
    @Transactional
    void putNonExistingTypeBourse() throws Exception {
        int databaseSizeBeforeUpdate = typeBourseRepository.findAll().size();
        typeBourse.setId(longCount.incrementAndGet());

        // Create the TypeBourse
        TypeBourseDTO typeBourseDTO = typeBourseMapper.toDto(typeBourse);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTypeBourseMockMvc
            .perform(
                put(ENTITY_API_URL_ID, typeBourseDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(typeBourseDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TypeBourse in the database
        List<TypeBourse> typeBourseList = typeBourseRepository.findAll();
        assertThat(typeBourseList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchTypeBourse() throws Exception {
        int databaseSizeBeforeUpdate = typeBourseRepository.findAll().size();
        typeBourse.setId(longCount.incrementAndGet());

        // Create the TypeBourse
        TypeBourseDTO typeBourseDTO = typeBourseMapper.toDto(typeBourse);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTypeBourseMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(typeBourseDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TypeBourse in the database
        List<TypeBourse> typeBourseList = typeBourseRepository.findAll();
        assertThat(typeBourseList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamTypeBourse() throws Exception {
        int databaseSizeBeforeUpdate = typeBourseRepository.findAll().size();
        typeBourse.setId(longCount.incrementAndGet());

        // Create the TypeBourse
        TypeBourseDTO typeBourseDTO = typeBourseMapper.toDto(typeBourse);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTypeBourseMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(typeBourseDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the TypeBourse in the database
        List<TypeBourse> typeBourseList = typeBourseRepository.findAll();
        assertThat(typeBourseList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateTypeBourseWithPatch() throws Exception {
        // Initialize the database
        typeBourseRepository.saveAndFlush(typeBourse);

        int databaseSizeBeforeUpdate = typeBourseRepository.findAll().size();

        // Update the typeBourse using partial update
        TypeBourse partialUpdatedTypeBourse = new TypeBourse();
        partialUpdatedTypeBourse.setId(typeBourse.getId());

        partialUpdatedTypeBourse.libelleTypeBourse(UPDATED_LIBELLE_TYPE_BOURSE);

        restTypeBourseMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTypeBourse.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedTypeBourse))
            )
            .andExpect(status().isOk());

        // Validate the TypeBourse in the database
        List<TypeBourse> typeBourseList = typeBourseRepository.findAll();
        assertThat(typeBourseList).hasSize(databaseSizeBeforeUpdate);
        TypeBourse testTypeBourse = typeBourseList.get(typeBourseList.size() - 1);
        assertThat(testTypeBourse.getLibelleTypeBourse()).isEqualTo(UPDATED_LIBELLE_TYPE_BOURSE);
    }

    @Test
    @Transactional
    void fullUpdateTypeBourseWithPatch() throws Exception {
        // Initialize the database
        typeBourseRepository.saveAndFlush(typeBourse);

        int databaseSizeBeforeUpdate = typeBourseRepository.findAll().size();

        // Update the typeBourse using partial update
        TypeBourse partialUpdatedTypeBourse = new TypeBourse();
        partialUpdatedTypeBourse.setId(typeBourse.getId());

        partialUpdatedTypeBourse.libelleTypeBourse(UPDATED_LIBELLE_TYPE_BOURSE);

        restTypeBourseMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTypeBourse.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedTypeBourse))
            )
            .andExpect(status().isOk());

        // Validate the TypeBourse in the database
        List<TypeBourse> typeBourseList = typeBourseRepository.findAll();
        assertThat(typeBourseList).hasSize(databaseSizeBeforeUpdate);
        TypeBourse testTypeBourse = typeBourseList.get(typeBourseList.size() - 1);
        assertThat(testTypeBourse.getLibelleTypeBourse()).isEqualTo(UPDATED_LIBELLE_TYPE_BOURSE);
    }

    @Test
    @Transactional
    void patchNonExistingTypeBourse() throws Exception {
        int databaseSizeBeforeUpdate = typeBourseRepository.findAll().size();
        typeBourse.setId(longCount.incrementAndGet());

        // Create the TypeBourse
        TypeBourseDTO typeBourseDTO = typeBourseMapper.toDto(typeBourse);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTypeBourseMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, typeBourseDTO.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(typeBourseDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TypeBourse in the database
        List<TypeBourse> typeBourseList = typeBourseRepository.findAll();
        assertThat(typeBourseList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchTypeBourse() throws Exception {
        int databaseSizeBeforeUpdate = typeBourseRepository.findAll().size();
        typeBourse.setId(longCount.incrementAndGet());

        // Create the TypeBourse
        TypeBourseDTO typeBourseDTO = typeBourseMapper.toDto(typeBourse);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTypeBourseMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(typeBourseDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TypeBourse in the database
        List<TypeBourse> typeBourseList = typeBourseRepository.findAll();
        assertThat(typeBourseList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamTypeBourse() throws Exception {
        int databaseSizeBeforeUpdate = typeBourseRepository.findAll().size();
        typeBourse.setId(longCount.incrementAndGet());

        // Create the TypeBourse
        TypeBourseDTO typeBourseDTO = typeBourseMapper.toDto(typeBourse);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTypeBourseMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(typeBourseDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the TypeBourse in the database
        List<TypeBourse> typeBourseList = typeBourseRepository.findAll();
        assertThat(typeBourseList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteTypeBourse() throws Exception {
        // Initialize the database
        typeBourseRepository.saveAndFlush(typeBourse);

        int databaseSizeBeforeDelete = typeBourseRepository.findAll().size();

        // Delete the typeBourse
        restTypeBourseMockMvc
            .perform(delete(ENTITY_API_URL_ID, typeBourse.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<TypeBourse> typeBourseList = typeBourseRepository.findAll();
        assertThat(typeBourseList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
