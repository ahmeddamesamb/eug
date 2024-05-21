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
import sn.ugb.gir.domain.TypeAdmission;
import sn.ugb.gir.repository.TypeAdmissionRepository;
import sn.ugb.gir.service.dto.TypeAdmissionDTO;
import sn.ugb.gir.service.mapper.TypeAdmissionMapper;

/**
 * Integration tests for the {@link TypeAdmissionResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class TypeAdmissionResourceIT {

    private static final String DEFAULT_LIBELLE = "AAAAAAAAAA";
    private static final String UPDATED_LIBELLE = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/type-admissions";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private TypeAdmissionRepository typeAdmissionRepository;

    @Autowired
    private TypeAdmissionMapper typeAdmissionMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restTypeAdmissionMockMvc;

    private TypeAdmission typeAdmission;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TypeAdmission createEntity(EntityManager em) {
        TypeAdmission typeAdmission = new TypeAdmission().libelle(DEFAULT_LIBELLE);
        return typeAdmission;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TypeAdmission createUpdatedEntity(EntityManager em) {
        TypeAdmission typeAdmission = new TypeAdmission().libelle(UPDATED_LIBELLE);
        return typeAdmission;
    }

    @BeforeEach
    public void initTest() {
        typeAdmission = createEntity(em);
    }

    @Test
    @Transactional
    void createTypeAdmission() throws Exception {
        int databaseSizeBeforeCreate = typeAdmissionRepository.findAll().size();
        // Create the TypeAdmission
        TypeAdmissionDTO typeAdmissionDTO = typeAdmissionMapper.toDto(typeAdmission);
        restTypeAdmissionMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(typeAdmissionDTO))
            )
            .andExpect(status().isCreated());

        // Validate the TypeAdmission in the database
        List<TypeAdmission> typeAdmissionList = typeAdmissionRepository.findAll();
        assertThat(typeAdmissionList).hasSize(databaseSizeBeforeCreate + 1);
        TypeAdmission testTypeAdmission = typeAdmissionList.get(typeAdmissionList.size() - 1);
        assertThat(testTypeAdmission.getLibelle()).isEqualTo(DEFAULT_LIBELLE);
    }

    @Test
    @Transactional
    void createTypeAdmissionWithExistingId() throws Exception {
        // Create the TypeAdmission with an existing ID
        typeAdmission.setId(1L);
        TypeAdmissionDTO typeAdmissionDTO = typeAdmissionMapper.toDto(typeAdmission);

        int databaseSizeBeforeCreate = typeAdmissionRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restTypeAdmissionMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(typeAdmissionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TypeAdmission in the database
        List<TypeAdmission> typeAdmissionList = typeAdmissionRepository.findAll();
        assertThat(typeAdmissionList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllTypeAdmissions() throws Exception {
        // Initialize the database
        typeAdmissionRepository.saveAndFlush(typeAdmission);

        // Get all the typeAdmissionList
        restTypeAdmissionMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(typeAdmission.getId().intValue())))
            .andExpect(jsonPath("$.[*].libelle").value(hasItem(DEFAULT_LIBELLE)));
    }

    @Test
    @Transactional
    void getTypeAdmission() throws Exception {
        // Initialize the database
        typeAdmissionRepository.saveAndFlush(typeAdmission);

        // Get the typeAdmission
        restTypeAdmissionMockMvc
            .perform(get(ENTITY_API_URL_ID, typeAdmission.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(typeAdmission.getId().intValue()))
            .andExpect(jsonPath("$.libelle").value(DEFAULT_LIBELLE));
    }

    @Test
    @Transactional
    void getNonExistingTypeAdmission() throws Exception {
        // Get the typeAdmission
        restTypeAdmissionMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingTypeAdmission() throws Exception {
        // Initialize the database
        typeAdmissionRepository.saveAndFlush(typeAdmission);

        int databaseSizeBeforeUpdate = typeAdmissionRepository.findAll().size();

        // Update the typeAdmission
        TypeAdmission updatedTypeAdmission = typeAdmissionRepository.findById(typeAdmission.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedTypeAdmission are not directly saved in db
        em.detach(updatedTypeAdmission);
        updatedTypeAdmission.libelle(UPDATED_LIBELLE);
        TypeAdmissionDTO typeAdmissionDTO = typeAdmissionMapper.toDto(updatedTypeAdmission);

        restTypeAdmissionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, typeAdmissionDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(typeAdmissionDTO))
            )
            .andExpect(status().isOk());

        // Validate the TypeAdmission in the database
        List<TypeAdmission> typeAdmissionList = typeAdmissionRepository.findAll();
        assertThat(typeAdmissionList).hasSize(databaseSizeBeforeUpdate);
        TypeAdmission testTypeAdmission = typeAdmissionList.get(typeAdmissionList.size() - 1);
        assertThat(testTypeAdmission.getLibelle()).isEqualTo(UPDATED_LIBELLE);
    }

    @Test
    @Transactional
    void putNonExistingTypeAdmission() throws Exception {
        int databaseSizeBeforeUpdate = typeAdmissionRepository.findAll().size();
        typeAdmission.setId(longCount.incrementAndGet());

        // Create the TypeAdmission
        TypeAdmissionDTO typeAdmissionDTO = typeAdmissionMapper.toDto(typeAdmission);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTypeAdmissionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, typeAdmissionDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(typeAdmissionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TypeAdmission in the database
        List<TypeAdmission> typeAdmissionList = typeAdmissionRepository.findAll();
        assertThat(typeAdmissionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchTypeAdmission() throws Exception {
        int databaseSizeBeforeUpdate = typeAdmissionRepository.findAll().size();
        typeAdmission.setId(longCount.incrementAndGet());

        // Create the TypeAdmission
        TypeAdmissionDTO typeAdmissionDTO = typeAdmissionMapper.toDto(typeAdmission);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTypeAdmissionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(typeAdmissionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TypeAdmission in the database
        List<TypeAdmission> typeAdmissionList = typeAdmissionRepository.findAll();
        assertThat(typeAdmissionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamTypeAdmission() throws Exception {
        int databaseSizeBeforeUpdate = typeAdmissionRepository.findAll().size();
        typeAdmission.setId(longCount.incrementAndGet());

        // Create the TypeAdmission
        TypeAdmissionDTO typeAdmissionDTO = typeAdmissionMapper.toDto(typeAdmission);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTypeAdmissionMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(typeAdmissionDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the TypeAdmission in the database
        List<TypeAdmission> typeAdmissionList = typeAdmissionRepository.findAll();
        assertThat(typeAdmissionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateTypeAdmissionWithPatch() throws Exception {
        // Initialize the database
        typeAdmissionRepository.saveAndFlush(typeAdmission);

        int databaseSizeBeforeUpdate = typeAdmissionRepository.findAll().size();

        // Update the typeAdmission using partial update
        TypeAdmission partialUpdatedTypeAdmission = new TypeAdmission();
        partialUpdatedTypeAdmission.setId(typeAdmission.getId());

        partialUpdatedTypeAdmission.libelle(UPDATED_LIBELLE);

        restTypeAdmissionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTypeAdmission.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedTypeAdmission))
            )
            .andExpect(status().isOk());

        // Validate the TypeAdmission in the database
        List<TypeAdmission> typeAdmissionList = typeAdmissionRepository.findAll();
        assertThat(typeAdmissionList).hasSize(databaseSizeBeforeUpdate);
        TypeAdmission testTypeAdmission = typeAdmissionList.get(typeAdmissionList.size() - 1);
        assertThat(testTypeAdmission.getLibelle()).isEqualTo(UPDATED_LIBELLE);
    }

    @Test
    @Transactional
    void fullUpdateTypeAdmissionWithPatch() throws Exception {
        // Initialize the database
        typeAdmissionRepository.saveAndFlush(typeAdmission);

        int databaseSizeBeforeUpdate = typeAdmissionRepository.findAll().size();

        // Update the typeAdmission using partial update
        TypeAdmission partialUpdatedTypeAdmission = new TypeAdmission();
        partialUpdatedTypeAdmission.setId(typeAdmission.getId());

        partialUpdatedTypeAdmission.libelle(UPDATED_LIBELLE);

        restTypeAdmissionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTypeAdmission.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedTypeAdmission))
            )
            .andExpect(status().isOk());

        // Validate the TypeAdmission in the database
        List<TypeAdmission> typeAdmissionList = typeAdmissionRepository.findAll();
        assertThat(typeAdmissionList).hasSize(databaseSizeBeforeUpdate);
        TypeAdmission testTypeAdmission = typeAdmissionList.get(typeAdmissionList.size() - 1);
        assertThat(testTypeAdmission.getLibelle()).isEqualTo(UPDATED_LIBELLE);
    }

    @Test
    @Transactional
    void patchNonExistingTypeAdmission() throws Exception {
        int databaseSizeBeforeUpdate = typeAdmissionRepository.findAll().size();
        typeAdmission.setId(longCount.incrementAndGet());

        // Create the TypeAdmission
        TypeAdmissionDTO typeAdmissionDTO = typeAdmissionMapper.toDto(typeAdmission);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTypeAdmissionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, typeAdmissionDTO.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(typeAdmissionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TypeAdmission in the database
        List<TypeAdmission> typeAdmissionList = typeAdmissionRepository.findAll();
        assertThat(typeAdmissionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchTypeAdmission() throws Exception {
        int databaseSizeBeforeUpdate = typeAdmissionRepository.findAll().size();
        typeAdmission.setId(longCount.incrementAndGet());

        // Create the TypeAdmission
        TypeAdmissionDTO typeAdmissionDTO = typeAdmissionMapper.toDto(typeAdmission);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTypeAdmissionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(typeAdmissionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TypeAdmission in the database
        List<TypeAdmission> typeAdmissionList = typeAdmissionRepository.findAll();
        assertThat(typeAdmissionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamTypeAdmission() throws Exception {
        int databaseSizeBeforeUpdate = typeAdmissionRepository.findAll().size();
        typeAdmission.setId(longCount.incrementAndGet());

        // Create the TypeAdmission
        TypeAdmissionDTO typeAdmissionDTO = typeAdmissionMapper.toDto(typeAdmission);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTypeAdmissionMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(typeAdmissionDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the TypeAdmission in the database
        List<TypeAdmission> typeAdmissionList = typeAdmissionRepository.findAll();
        assertThat(typeAdmissionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteTypeAdmission() throws Exception {
        // Initialize the database
        typeAdmissionRepository.saveAndFlush(typeAdmission);

        int databaseSizeBeforeDelete = typeAdmissionRepository.findAll().size();

        // Delete the typeAdmission
        restTypeAdmissionMockMvc
            .perform(delete(ENTITY_API_URL_ID, typeAdmission.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<TypeAdmission> typeAdmissionList = typeAdmissionRepository.findAll();
        assertThat(typeAdmissionList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
