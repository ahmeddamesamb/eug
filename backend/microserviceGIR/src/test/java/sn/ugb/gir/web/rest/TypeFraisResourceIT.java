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
import sn.ugb.gir.domain.TypeFrais;
import sn.ugb.gir.repository.TypeFraisRepository;
import sn.ugb.gir.service.dto.TypeFraisDTO;
import sn.ugb.gir.service.mapper.TypeFraisMapper;

/**
 * Integration tests for the {@link TypeFraisResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class TypeFraisResourceIT {

    private static final String DEFAULT_LIBELLE = "AAAAAAAAAA";
    private static final String UPDATED_LIBELLE = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/type-frais";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private TypeFraisRepository typeFraisRepository;

    @Autowired
    private TypeFraisMapper typeFraisMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restTypeFraisMockMvc;

    private TypeFrais typeFrais;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TypeFrais createEntity(EntityManager em) {
        TypeFrais typeFrais = new TypeFrais().libelle(DEFAULT_LIBELLE);
        return typeFrais;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TypeFrais createUpdatedEntity(EntityManager em) {
        TypeFrais typeFrais = new TypeFrais().libelle(UPDATED_LIBELLE);
        return typeFrais;
    }

    @BeforeEach
    public void initTest() {
        typeFrais = createEntity(em);
    }

    @Test
    @Transactional
    void createTypeFrais() throws Exception {
        int databaseSizeBeforeCreate = typeFraisRepository.findAll().size();
        // Create the TypeFrais
        TypeFraisDTO typeFraisDTO = typeFraisMapper.toDto(typeFrais);
        restTypeFraisMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(typeFraisDTO))
            )
            .andExpect(status().isCreated());

        // Validate the TypeFrais in the database
        List<TypeFrais> typeFraisList = typeFraisRepository.findAll();
        assertThat(typeFraisList).hasSize(databaseSizeBeforeCreate + 1);
        TypeFrais testTypeFrais = typeFraisList.get(typeFraisList.size() - 1);
        assertThat(testTypeFrais.getLibelle()).isEqualTo(DEFAULT_LIBELLE);
    }

    @Test
    @Transactional
    void createTypeFraisWithExistingId() throws Exception {
        // Create the TypeFrais with an existing ID
        typeFrais.setId(1L);
        TypeFraisDTO typeFraisDTO = typeFraisMapper.toDto(typeFrais);

        int databaseSizeBeforeCreate = typeFraisRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restTypeFraisMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(typeFraisDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TypeFrais in the database
        List<TypeFrais> typeFraisList = typeFraisRepository.findAll();
        assertThat(typeFraisList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllTypeFrais() throws Exception {
        // Initialize the database
        typeFraisRepository.saveAndFlush(typeFrais);

        // Get all the typeFraisList
        restTypeFraisMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(typeFrais.getId().intValue())))
            .andExpect(jsonPath("$.[*].libelle").value(hasItem(DEFAULT_LIBELLE)));
    }

    @Test
    @Transactional
    void getTypeFrais() throws Exception {
        // Initialize the database
        typeFraisRepository.saveAndFlush(typeFrais);

        // Get the typeFrais
        restTypeFraisMockMvc
            .perform(get(ENTITY_API_URL_ID, typeFrais.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(typeFrais.getId().intValue()))
            .andExpect(jsonPath("$.libelle").value(DEFAULT_LIBELLE));
    }

    @Test
    @Transactional
    void getNonExistingTypeFrais() throws Exception {
        // Get the typeFrais
        restTypeFraisMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingTypeFrais() throws Exception {
        // Initialize the database
        typeFraisRepository.saveAndFlush(typeFrais);

        int databaseSizeBeforeUpdate = typeFraisRepository.findAll().size();

        // Update the typeFrais
        TypeFrais updatedTypeFrais = typeFraisRepository.findById(typeFrais.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedTypeFrais are not directly saved in db
        em.detach(updatedTypeFrais);
        updatedTypeFrais.libelle(UPDATED_LIBELLE);
        TypeFraisDTO typeFraisDTO = typeFraisMapper.toDto(updatedTypeFrais);

        restTypeFraisMockMvc
            .perform(
                put(ENTITY_API_URL_ID, typeFraisDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(typeFraisDTO))
            )
            .andExpect(status().isOk());

        // Validate the TypeFrais in the database
        List<TypeFrais> typeFraisList = typeFraisRepository.findAll();
        assertThat(typeFraisList).hasSize(databaseSizeBeforeUpdate);
        TypeFrais testTypeFrais = typeFraisList.get(typeFraisList.size() - 1);
        assertThat(testTypeFrais.getLibelle()).isEqualTo(UPDATED_LIBELLE);
    }

    @Test
    @Transactional
    void putNonExistingTypeFrais() throws Exception {
        int databaseSizeBeforeUpdate = typeFraisRepository.findAll().size();
        typeFrais.setId(longCount.incrementAndGet());

        // Create the TypeFrais
        TypeFraisDTO typeFraisDTO = typeFraisMapper.toDto(typeFrais);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTypeFraisMockMvc
            .perform(
                put(ENTITY_API_URL_ID, typeFraisDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(typeFraisDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TypeFrais in the database
        List<TypeFrais> typeFraisList = typeFraisRepository.findAll();
        assertThat(typeFraisList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchTypeFrais() throws Exception {
        int databaseSizeBeforeUpdate = typeFraisRepository.findAll().size();
        typeFrais.setId(longCount.incrementAndGet());

        // Create the TypeFrais
        TypeFraisDTO typeFraisDTO = typeFraisMapper.toDto(typeFrais);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTypeFraisMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(typeFraisDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TypeFrais in the database
        List<TypeFrais> typeFraisList = typeFraisRepository.findAll();
        assertThat(typeFraisList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamTypeFrais() throws Exception {
        int databaseSizeBeforeUpdate = typeFraisRepository.findAll().size();
        typeFrais.setId(longCount.incrementAndGet());

        // Create the TypeFrais
        TypeFraisDTO typeFraisDTO = typeFraisMapper.toDto(typeFrais);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTypeFraisMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(typeFraisDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the TypeFrais in the database
        List<TypeFrais> typeFraisList = typeFraisRepository.findAll();
        assertThat(typeFraisList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateTypeFraisWithPatch() throws Exception {
        // Initialize the database
        typeFraisRepository.saveAndFlush(typeFrais);

        int databaseSizeBeforeUpdate = typeFraisRepository.findAll().size();

        // Update the typeFrais using partial update
        TypeFrais partialUpdatedTypeFrais = new TypeFrais();
        partialUpdatedTypeFrais.setId(typeFrais.getId());

        restTypeFraisMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTypeFrais.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedTypeFrais))
            )
            .andExpect(status().isOk());

        // Validate the TypeFrais in the database
        List<TypeFrais> typeFraisList = typeFraisRepository.findAll();
        assertThat(typeFraisList).hasSize(databaseSizeBeforeUpdate);
        TypeFrais testTypeFrais = typeFraisList.get(typeFraisList.size() - 1);
        assertThat(testTypeFrais.getLibelle()).isEqualTo(DEFAULT_LIBELLE);
    }

    @Test
    @Transactional
    void fullUpdateTypeFraisWithPatch() throws Exception {
        // Initialize the database
        typeFraisRepository.saveAndFlush(typeFrais);

        int databaseSizeBeforeUpdate = typeFraisRepository.findAll().size();

        // Update the typeFrais using partial update
        TypeFrais partialUpdatedTypeFrais = new TypeFrais();
        partialUpdatedTypeFrais.setId(typeFrais.getId());

        partialUpdatedTypeFrais.libelle(UPDATED_LIBELLE);

        restTypeFraisMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTypeFrais.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedTypeFrais))
            )
            .andExpect(status().isOk());

        // Validate the TypeFrais in the database
        List<TypeFrais> typeFraisList = typeFraisRepository.findAll();
        assertThat(typeFraisList).hasSize(databaseSizeBeforeUpdate);
        TypeFrais testTypeFrais = typeFraisList.get(typeFraisList.size() - 1);
        assertThat(testTypeFrais.getLibelle()).isEqualTo(UPDATED_LIBELLE);
    }

    @Test
    @Transactional
    void patchNonExistingTypeFrais() throws Exception {
        int databaseSizeBeforeUpdate = typeFraisRepository.findAll().size();
        typeFrais.setId(longCount.incrementAndGet());

        // Create the TypeFrais
        TypeFraisDTO typeFraisDTO = typeFraisMapper.toDto(typeFrais);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTypeFraisMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, typeFraisDTO.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(typeFraisDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TypeFrais in the database
        List<TypeFrais> typeFraisList = typeFraisRepository.findAll();
        assertThat(typeFraisList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchTypeFrais() throws Exception {
        int databaseSizeBeforeUpdate = typeFraisRepository.findAll().size();
        typeFrais.setId(longCount.incrementAndGet());

        // Create the TypeFrais
        TypeFraisDTO typeFraisDTO = typeFraisMapper.toDto(typeFrais);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTypeFraisMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(typeFraisDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TypeFrais in the database
        List<TypeFrais> typeFraisList = typeFraisRepository.findAll();
        assertThat(typeFraisList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamTypeFrais() throws Exception {
        int databaseSizeBeforeUpdate = typeFraisRepository.findAll().size();
        typeFrais.setId(longCount.incrementAndGet());

        // Create the TypeFrais
        TypeFraisDTO typeFraisDTO = typeFraisMapper.toDto(typeFrais);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTypeFraisMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(typeFraisDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the TypeFrais in the database
        List<TypeFrais> typeFraisList = typeFraisRepository.findAll();
        assertThat(typeFraisList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteTypeFrais() throws Exception {
        // Initialize the database
        typeFraisRepository.saveAndFlush(typeFrais);

        int databaseSizeBeforeDelete = typeFraisRepository.findAll().size();

        // Delete the typeFrais
        restTypeFraisMockMvc
            .perform(delete(ENTITY_API_URL_ID, typeFrais.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<TypeFrais> typeFraisList = typeFraisRepository.findAll();
        assertThat(typeFraisList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
