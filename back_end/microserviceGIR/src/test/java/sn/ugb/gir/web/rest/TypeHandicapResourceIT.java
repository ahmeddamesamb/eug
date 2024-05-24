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
import sn.ugb.gir.domain.TypeHandicap;
import sn.ugb.gir.repository.TypeHandicapRepository;
import sn.ugb.gir.service.dto.TypeHandicapDTO;
import sn.ugb.gir.service.mapper.TypeHandicapMapper;

/**
 * Integration tests for the {@link TypeHandicapResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class TypeHandicapResourceIT {

    private static final String DEFAULT_LIBELLE_TYPE_HANDICAP = "AAAAAAAAAA";
    private static final String UPDATED_LIBELLE_TYPE_HANDICAP = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/type-handicaps";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private TypeHandicapRepository typeHandicapRepository;

    @Autowired
    private TypeHandicapMapper typeHandicapMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restTypeHandicapMockMvc;

    private TypeHandicap typeHandicap;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TypeHandicap createEntity(EntityManager em) {
        TypeHandicap typeHandicap = new TypeHandicap().libelleTypeHandicap(DEFAULT_LIBELLE_TYPE_HANDICAP);
        return typeHandicap;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TypeHandicap createUpdatedEntity(EntityManager em) {
        TypeHandicap typeHandicap = new TypeHandicap().libelleTypeHandicap(UPDATED_LIBELLE_TYPE_HANDICAP);
        return typeHandicap;
    }

    @BeforeEach
    public void initTest() {
        typeHandicap = createEntity(em);
    }

    @Test
    @Transactional
    void createTypeHandicap() throws Exception {
        int databaseSizeBeforeCreate = typeHandicapRepository.findAll().size();
        // Create the TypeHandicap
        TypeHandicapDTO typeHandicapDTO = typeHandicapMapper.toDto(typeHandicap);
        restTypeHandicapMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(typeHandicapDTO))
            )
            .andExpect(status().isCreated());

        // Validate the TypeHandicap in the database
        List<TypeHandicap> typeHandicapList = typeHandicapRepository.findAll();
        assertThat(typeHandicapList).hasSize(databaseSizeBeforeCreate + 1);
        TypeHandicap testTypeHandicap = typeHandicapList.get(typeHandicapList.size() - 1);
        assertThat(testTypeHandicap.getLibelleTypeHandicap()).isEqualTo(DEFAULT_LIBELLE_TYPE_HANDICAP);
    }

    @Test
    @Transactional
    void createTypeHandicapWithExistingId() throws Exception {
        // Create the TypeHandicap with an existing ID
        typeHandicap.setId(1L);
        TypeHandicapDTO typeHandicapDTO = typeHandicapMapper.toDto(typeHandicap);

        int databaseSizeBeforeCreate = typeHandicapRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restTypeHandicapMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(typeHandicapDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TypeHandicap in the database
        List<TypeHandicap> typeHandicapList = typeHandicapRepository.findAll();
        assertThat(typeHandicapList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkLibelleTypeHandicapIsRequired() throws Exception {
        int databaseSizeBeforeTest = typeHandicapRepository.findAll().size();
        // set the field null
        typeHandicap.setLibelleTypeHandicap(null);

        // Create the TypeHandicap, which fails.
        TypeHandicapDTO typeHandicapDTO = typeHandicapMapper.toDto(typeHandicap);

        restTypeHandicapMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(typeHandicapDTO))
            )
            .andExpect(status().isBadRequest());

        List<TypeHandicap> typeHandicapList = typeHandicapRepository.findAll();
        assertThat(typeHandicapList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllTypeHandicaps() throws Exception {
        // Initialize the database
        typeHandicapRepository.saveAndFlush(typeHandicap);

        // Get all the typeHandicapList
        restTypeHandicapMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(typeHandicap.getId().intValue())))
            .andExpect(jsonPath("$.[*].libelleTypeHandicap").value(hasItem(DEFAULT_LIBELLE_TYPE_HANDICAP)));
    }

    @Test
    @Transactional
    void getTypeHandicap() throws Exception {
        // Initialize the database
        typeHandicapRepository.saveAndFlush(typeHandicap);

        // Get the typeHandicap
        restTypeHandicapMockMvc
            .perform(get(ENTITY_API_URL_ID, typeHandicap.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(typeHandicap.getId().intValue()))
            .andExpect(jsonPath("$.libelleTypeHandicap").value(DEFAULT_LIBELLE_TYPE_HANDICAP));
    }

    @Test
    @Transactional
    void getNonExistingTypeHandicap() throws Exception {
        // Get the typeHandicap
        restTypeHandicapMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingTypeHandicap() throws Exception {
        // Initialize the database
        typeHandicapRepository.saveAndFlush(typeHandicap);

        int databaseSizeBeforeUpdate = typeHandicapRepository.findAll().size();

        // Update the typeHandicap
        TypeHandicap updatedTypeHandicap = typeHandicapRepository.findById(typeHandicap.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedTypeHandicap are not directly saved in db
        em.detach(updatedTypeHandicap);
        updatedTypeHandicap.libelleTypeHandicap(UPDATED_LIBELLE_TYPE_HANDICAP);
        TypeHandicapDTO typeHandicapDTO = typeHandicapMapper.toDto(updatedTypeHandicap);

        restTypeHandicapMockMvc
            .perform(
                put(ENTITY_API_URL_ID, typeHandicapDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(typeHandicapDTO))
            )
            .andExpect(status().isOk());

        // Validate the TypeHandicap in the database
        List<TypeHandicap> typeHandicapList = typeHandicapRepository.findAll();
        assertThat(typeHandicapList).hasSize(databaseSizeBeforeUpdate);
        TypeHandicap testTypeHandicap = typeHandicapList.get(typeHandicapList.size() - 1);
        assertThat(testTypeHandicap.getLibelleTypeHandicap()).isEqualTo(UPDATED_LIBELLE_TYPE_HANDICAP);
    }

    @Test
    @Transactional
    void putNonExistingTypeHandicap() throws Exception {
        int databaseSizeBeforeUpdate = typeHandicapRepository.findAll().size();
        typeHandicap.setId(longCount.incrementAndGet());

        // Create the TypeHandicap
        TypeHandicapDTO typeHandicapDTO = typeHandicapMapper.toDto(typeHandicap);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTypeHandicapMockMvc
            .perform(
                put(ENTITY_API_URL_ID, typeHandicapDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(typeHandicapDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TypeHandicap in the database
        List<TypeHandicap> typeHandicapList = typeHandicapRepository.findAll();
        assertThat(typeHandicapList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchTypeHandicap() throws Exception {
        int databaseSizeBeforeUpdate = typeHandicapRepository.findAll().size();
        typeHandicap.setId(longCount.incrementAndGet());

        // Create the TypeHandicap
        TypeHandicapDTO typeHandicapDTO = typeHandicapMapper.toDto(typeHandicap);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTypeHandicapMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(typeHandicapDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TypeHandicap in the database
        List<TypeHandicap> typeHandicapList = typeHandicapRepository.findAll();
        assertThat(typeHandicapList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamTypeHandicap() throws Exception {
        int databaseSizeBeforeUpdate = typeHandicapRepository.findAll().size();
        typeHandicap.setId(longCount.incrementAndGet());

        // Create the TypeHandicap
        TypeHandicapDTO typeHandicapDTO = typeHandicapMapper.toDto(typeHandicap);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTypeHandicapMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(typeHandicapDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the TypeHandicap in the database
        List<TypeHandicap> typeHandicapList = typeHandicapRepository.findAll();
        assertThat(typeHandicapList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateTypeHandicapWithPatch() throws Exception {
        // Initialize the database
        typeHandicapRepository.saveAndFlush(typeHandicap);

        int databaseSizeBeforeUpdate = typeHandicapRepository.findAll().size();

        // Update the typeHandicap using partial update
        TypeHandicap partialUpdatedTypeHandicap = new TypeHandicap();
        partialUpdatedTypeHandicap.setId(typeHandicap.getId());

        restTypeHandicapMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTypeHandicap.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedTypeHandicap))
            )
            .andExpect(status().isOk());

        // Validate the TypeHandicap in the database
        List<TypeHandicap> typeHandicapList = typeHandicapRepository.findAll();
        assertThat(typeHandicapList).hasSize(databaseSizeBeforeUpdate);
        TypeHandicap testTypeHandicap = typeHandicapList.get(typeHandicapList.size() - 1);
        assertThat(testTypeHandicap.getLibelleTypeHandicap()).isEqualTo(DEFAULT_LIBELLE_TYPE_HANDICAP);
    }

    @Test
    @Transactional
    void fullUpdateTypeHandicapWithPatch() throws Exception {
        // Initialize the database
        typeHandicapRepository.saveAndFlush(typeHandicap);

        int databaseSizeBeforeUpdate = typeHandicapRepository.findAll().size();

        // Update the typeHandicap using partial update
        TypeHandicap partialUpdatedTypeHandicap = new TypeHandicap();
        partialUpdatedTypeHandicap.setId(typeHandicap.getId());

        partialUpdatedTypeHandicap.libelleTypeHandicap(UPDATED_LIBELLE_TYPE_HANDICAP);

        restTypeHandicapMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTypeHandicap.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedTypeHandicap))
            )
            .andExpect(status().isOk());

        // Validate the TypeHandicap in the database
        List<TypeHandicap> typeHandicapList = typeHandicapRepository.findAll();
        assertThat(typeHandicapList).hasSize(databaseSizeBeforeUpdate);
        TypeHandicap testTypeHandicap = typeHandicapList.get(typeHandicapList.size() - 1);
        assertThat(testTypeHandicap.getLibelleTypeHandicap()).isEqualTo(UPDATED_LIBELLE_TYPE_HANDICAP);
    }

    @Test
    @Transactional
    void patchNonExistingTypeHandicap() throws Exception {
        int databaseSizeBeforeUpdate = typeHandicapRepository.findAll().size();
        typeHandicap.setId(longCount.incrementAndGet());

        // Create the TypeHandicap
        TypeHandicapDTO typeHandicapDTO = typeHandicapMapper.toDto(typeHandicap);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTypeHandicapMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, typeHandicapDTO.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(typeHandicapDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TypeHandicap in the database
        List<TypeHandicap> typeHandicapList = typeHandicapRepository.findAll();
        assertThat(typeHandicapList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchTypeHandicap() throws Exception {
        int databaseSizeBeforeUpdate = typeHandicapRepository.findAll().size();
        typeHandicap.setId(longCount.incrementAndGet());

        // Create the TypeHandicap
        TypeHandicapDTO typeHandicapDTO = typeHandicapMapper.toDto(typeHandicap);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTypeHandicapMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(typeHandicapDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TypeHandicap in the database
        List<TypeHandicap> typeHandicapList = typeHandicapRepository.findAll();
        assertThat(typeHandicapList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamTypeHandicap() throws Exception {
        int databaseSizeBeforeUpdate = typeHandicapRepository.findAll().size();
        typeHandicap.setId(longCount.incrementAndGet());

        // Create the TypeHandicap
        TypeHandicapDTO typeHandicapDTO = typeHandicapMapper.toDto(typeHandicap);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTypeHandicapMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(typeHandicapDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the TypeHandicap in the database
        List<TypeHandicap> typeHandicapList = typeHandicapRepository.findAll();
        assertThat(typeHandicapList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteTypeHandicap() throws Exception {
        // Initialize the database
        typeHandicapRepository.saveAndFlush(typeHandicap);

        int databaseSizeBeforeDelete = typeHandicapRepository.findAll().size();

        // Delete the typeHandicap
        restTypeHandicapMockMvc
            .perform(delete(ENTITY_API_URL_ID, typeHandicap.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<TypeHandicap> typeHandicapList = typeHandicapRepository.findAll();
        assertThat(typeHandicapList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
