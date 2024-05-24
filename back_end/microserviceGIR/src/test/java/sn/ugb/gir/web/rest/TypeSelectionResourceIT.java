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
import sn.ugb.gir.domain.TypeSelection;
import sn.ugb.gir.repository.TypeSelectionRepository;
import sn.ugb.gir.service.dto.TypeSelectionDTO;
import sn.ugb.gir.service.mapper.TypeSelectionMapper;

/**
 * Integration tests for the {@link TypeSelectionResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class TypeSelectionResourceIT {

    private static final String DEFAULT_LIBELLE_TYPE_SELECTION = "AAAAAAAAAA";
    private static final String UPDATED_LIBELLE_TYPE_SELECTION = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/type-selections";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private TypeSelectionRepository typeSelectionRepository;

    @Autowired
    private TypeSelectionMapper typeSelectionMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restTypeSelectionMockMvc;

    private TypeSelection typeSelection;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TypeSelection createEntity(EntityManager em) {
        TypeSelection typeSelection = new TypeSelection().libelleTypeSelection(DEFAULT_LIBELLE_TYPE_SELECTION);
        return typeSelection;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TypeSelection createUpdatedEntity(EntityManager em) {
        TypeSelection typeSelection = new TypeSelection().libelleTypeSelection(UPDATED_LIBELLE_TYPE_SELECTION);
        return typeSelection;
    }

    @BeforeEach
    public void initTest() {
        typeSelection = createEntity(em);
    }

    @Test
    @Transactional
    void createTypeSelection() throws Exception {
        int databaseSizeBeforeCreate = typeSelectionRepository.findAll().size();
        // Create the TypeSelection
        TypeSelectionDTO typeSelectionDTO = typeSelectionMapper.toDto(typeSelection);
        restTypeSelectionMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(typeSelectionDTO))
            )
            .andExpect(status().isCreated());

        // Validate the TypeSelection in the database
        List<TypeSelection> typeSelectionList = typeSelectionRepository.findAll();
        assertThat(typeSelectionList).hasSize(databaseSizeBeforeCreate + 1);
        TypeSelection testTypeSelection = typeSelectionList.get(typeSelectionList.size() - 1);
        assertThat(testTypeSelection.getLibelleTypeSelection()).isEqualTo(DEFAULT_LIBELLE_TYPE_SELECTION);
    }

    @Test
    @Transactional
    void createTypeSelectionWithExistingId() throws Exception {
        // Create the TypeSelection with an existing ID
        typeSelection.setId(1L);
        TypeSelectionDTO typeSelectionDTO = typeSelectionMapper.toDto(typeSelection);

        int databaseSizeBeforeCreate = typeSelectionRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restTypeSelectionMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(typeSelectionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TypeSelection in the database
        List<TypeSelection> typeSelectionList = typeSelectionRepository.findAll();
        assertThat(typeSelectionList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkLibelleTypeSelectionIsRequired() throws Exception {
        int databaseSizeBeforeTest = typeSelectionRepository.findAll().size();
        // set the field null
        typeSelection.setLibelleTypeSelection(null);

        // Create the TypeSelection, which fails.
        TypeSelectionDTO typeSelectionDTO = typeSelectionMapper.toDto(typeSelection);

        restTypeSelectionMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(typeSelectionDTO))
            )
            .andExpect(status().isBadRequest());

        List<TypeSelection> typeSelectionList = typeSelectionRepository.findAll();
        assertThat(typeSelectionList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllTypeSelections() throws Exception {
        // Initialize the database
        typeSelectionRepository.saveAndFlush(typeSelection);

        // Get all the typeSelectionList
        restTypeSelectionMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(typeSelection.getId().intValue())))
            .andExpect(jsonPath("$.[*].libelleTypeSelection").value(hasItem(DEFAULT_LIBELLE_TYPE_SELECTION)));
    }

    @Test
    @Transactional
    void getTypeSelection() throws Exception {
        // Initialize the database
        typeSelectionRepository.saveAndFlush(typeSelection);

        // Get the typeSelection
        restTypeSelectionMockMvc
            .perform(get(ENTITY_API_URL_ID, typeSelection.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(typeSelection.getId().intValue()))
            .andExpect(jsonPath("$.libelleTypeSelection").value(DEFAULT_LIBELLE_TYPE_SELECTION));
    }

    @Test
    @Transactional
    void getNonExistingTypeSelection() throws Exception {
        // Get the typeSelection
        restTypeSelectionMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingTypeSelection() throws Exception {
        // Initialize the database
        typeSelectionRepository.saveAndFlush(typeSelection);

        int databaseSizeBeforeUpdate = typeSelectionRepository.findAll().size();

        // Update the typeSelection
        TypeSelection updatedTypeSelection = typeSelectionRepository.findById(typeSelection.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedTypeSelection are not directly saved in db
        em.detach(updatedTypeSelection);
        updatedTypeSelection.libelleTypeSelection(UPDATED_LIBELLE_TYPE_SELECTION);
        TypeSelectionDTO typeSelectionDTO = typeSelectionMapper.toDto(updatedTypeSelection);

        restTypeSelectionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, typeSelectionDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(typeSelectionDTO))
            )
            .andExpect(status().isOk());

        // Validate the TypeSelection in the database
        List<TypeSelection> typeSelectionList = typeSelectionRepository.findAll();
        assertThat(typeSelectionList).hasSize(databaseSizeBeforeUpdate);
        TypeSelection testTypeSelection = typeSelectionList.get(typeSelectionList.size() - 1);
        assertThat(testTypeSelection.getLibelleTypeSelection()).isEqualTo(UPDATED_LIBELLE_TYPE_SELECTION);
    }

    @Test
    @Transactional
    void putNonExistingTypeSelection() throws Exception {
        int databaseSizeBeforeUpdate = typeSelectionRepository.findAll().size();
        typeSelection.setId(longCount.incrementAndGet());

        // Create the TypeSelection
        TypeSelectionDTO typeSelectionDTO = typeSelectionMapper.toDto(typeSelection);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTypeSelectionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, typeSelectionDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(typeSelectionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TypeSelection in the database
        List<TypeSelection> typeSelectionList = typeSelectionRepository.findAll();
        assertThat(typeSelectionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchTypeSelection() throws Exception {
        int databaseSizeBeforeUpdate = typeSelectionRepository.findAll().size();
        typeSelection.setId(longCount.incrementAndGet());

        // Create the TypeSelection
        TypeSelectionDTO typeSelectionDTO = typeSelectionMapper.toDto(typeSelection);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTypeSelectionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(typeSelectionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TypeSelection in the database
        List<TypeSelection> typeSelectionList = typeSelectionRepository.findAll();
        assertThat(typeSelectionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamTypeSelection() throws Exception {
        int databaseSizeBeforeUpdate = typeSelectionRepository.findAll().size();
        typeSelection.setId(longCount.incrementAndGet());

        // Create the TypeSelection
        TypeSelectionDTO typeSelectionDTO = typeSelectionMapper.toDto(typeSelection);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTypeSelectionMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(typeSelectionDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the TypeSelection in the database
        List<TypeSelection> typeSelectionList = typeSelectionRepository.findAll();
        assertThat(typeSelectionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateTypeSelectionWithPatch() throws Exception {
        // Initialize the database
        typeSelectionRepository.saveAndFlush(typeSelection);

        int databaseSizeBeforeUpdate = typeSelectionRepository.findAll().size();

        // Update the typeSelection using partial update
        TypeSelection partialUpdatedTypeSelection = new TypeSelection();
        partialUpdatedTypeSelection.setId(typeSelection.getId());

        restTypeSelectionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTypeSelection.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedTypeSelection))
            )
            .andExpect(status().isOk());

        // Validate the TypeSelection in the database
        List<TypeSelection> typeSelectionList = typeSelectionRepository.findAll();
        assertThat(typeSelectionList).hasSize(databaseSizeBeforeUpdate);
        TypeSelection testTypeSelection = typeSelectionList.get(typeSelectionList.size() - 1);
        assertThat(testTypeSelection.getLibelleTypeSelection()).isEqualTo(DEFAULT_LIBELLE_TYPE_SELECTION);
    }

    @Test
    @Transactional
    void fullUpdateTypeSelectionWithPatch() throws Exception {
        // Initialize the database
        typeSelectionRepository.saveAndFlush(typeSelection);

        int databaseSizeBeforeUpdate = typeSelectionRepository.findAll().size();

        // Update the typeSelection using partial update
        TypeSelection partialUpdatedTypeSelection = new TypeSelection();
        partialUpdatedTypeSelection.setId(typeSelection.getId());

        partialUpdatedTypeSelection.libelleTypeSelection(UPDATED_LIBELLE_TYPE_SELECTION);

        restTypeSelectionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTypeSelection.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedTypeSelection))
            )
            .andExpect(status().isOk());

        // Validate the TypeSelection in the database
        List<TypeSelection> typeSelectionList = typeSelectionRepository.findAll();
        assertThat(typeSelectionList).hasSize(databaseSizeBeforeUpdate);
        TypeSelection testTypeSelection = typeSelectionList.get(typeSelectionList.size() - 1);
        assertThat(testTypeSelection.getLibelleTypeSelection()).isEqualTo(UPDATED_LIBELLE_TYPE_SELECTION);
    }

    @Test
    @Transactional
    void patchNonExistingTypeSelection() throws Exception {
        int databaseSizeBeforeUpdate = typeSelectionRepository.findAll().size();
        typeSelection.setId(longCount.incrementAndGet());

        // Create the TypeSelection
        TypeSelectionDTO typeSelectionDTO = typeSelectionMapper.toDto(typeSelection);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTypeSelectionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, typeSelectionDTO.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(typeSelectionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TypeSelection in the database
        List<TypeSelection> typeSelectionList = typeSelectionRepository.findAll();
        assertThat(typeSelectionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchTypeSelection() throws Exception {
        int databaseSizeBeforeUpdate = typeSelectionRepository.findAll().size();
        typeSelection.setId(longCount.incrementAndGet());

        // Create the TypeSelection
        TypeSelectionDTO typeSelectionDTO = typeSelectionMapper.toDto(typeSelection);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTypeSelectionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(typeSelectionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TypeSelection in the database
        List<TypeSelection> typeSelectionList = typeSelectionRepository.findAll();
        assertThat(typeSelectionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamTypeSelection() throws Exception {
        int databaseSizeBeforeUpdate = typeSelectionRepository.findAll().size();
        typeSelection.setId(longCount.incrementAndGet());

        // Create the TypeSelection
        TypeSelectionDTO typeSelectionDTO = typeSelectionMapper.toDto(typeSelection);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTypeSelectionMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(typeSelectionDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the TypeSelection in the database
        List<TypeSelection> typeSelectionList = typeSelectionRepository.findAll();
        assertThat(typeSelectionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteTypeSelection() throws Exception {
        // Initialize the database
        typeSelectionRepository.saveAndFlush(typeSelection);

        int databaseSizeBeforeDelete = typeSelectionRepository.findAll().size();

        // Delete the typeSelection
        restTypeSelectionMockMvc
            .perform(delete(ENTITY_API_URL_ID, typeSelection.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<TypeSelection> typeSelectionList = typeSelectionRepository.findAll();
        assertThat(typeSelectionList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
