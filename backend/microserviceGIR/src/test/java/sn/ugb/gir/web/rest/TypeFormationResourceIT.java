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
import sn.ugb.gir.domain.TypeFormation;
import sn.ugb.gir.repository.TypeFormationRepository;
import sn.ugb.gir.repository.search.TypeFormationSearchRepository;
import sn.ugb.gir.service.dto.TypeFormationDTO;
import sn.ugb.gir.service.mapper.TypeFormationMapper;

/**
 * Integration tests for the {@link TypeFormationResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class TypeFormationResourceIT {

    private static final String DEFAULT_LIBELLE_TYPE_FORMATION = "AAAAAAAAAA";
    private static final String UPDATED_LIBELLE_TYPE_FORMATION = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/type-formations";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/type-formations/_search";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private TypeFormationRepository typeFormationRepository;

    @Autowired
    private TypeFormationMapper typeFormationMapper;

    @Autowired
    private TypeFormationSearchRepository typeFormationSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restTypeFormationMockMvc;

    private TypeFormation typeFormation;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TypeFormation createEntity(EntityManager em) {
        TypeFormation typeFormation = new TypeFormation().libelleTypeFormation(DEFAULT_LIBELLE_TYPE_FORMATION);
        return typeFormation;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TypeFormation createUpdatedEntity(EntityManager em) {
        TypeFormation typeFormation = new TypeFormation().libelleTypeFormation(UPDATED_LIBELLE_TYPE_FORMATION);
        return typeFormation;
    }

    @AfterEach
    public void cleanupElasticSearchRepository() {
        typeFormationSearchRepository.deleteAll();
        assertThat(typeFormationSearchRepository.count()).isEqualTo(0);
    }

    @BeforeEach
    public void initTest() {
        typeFormation = createEntity(em);
    }

    @Test
    @Transactional
    void createTypeFormation() throws Exception {
        int databaseSizeBeforeCreate = typeFormationRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(typeFormationSearchRepository.findAll());
        // Create the TypeFormation
        TypeFormationDTO typeFormationDTO = typeFormationMapper.toDto(typeFormation);
        restTypeFormationMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(typeFormationDTO))
            )
            .andExpect(status().isCreated());

        // Validate the TypeFormation in the database
        List<TypeFormation> typeFormationList = typeFormationRepository.findAll();
        assertThat(typeFormationList).hasSize(databaseSizeBeforeCreate + 1);
        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(typeFormationSearchRepository.findAll());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore + 1);
            });
        TypeFormation testTypeFormation = typeFormationList.get(typeFormationList.size() - 1);
        assertThat(testTypeFormation.getLibelleTypeFormation()).isEqualTo(DEFAULT_LIBELLE_TYPE_FORMATION);
    }

    @Test
    @Transactional
    void createTypeFormationWithExistingId() throws Exception {
        // Create the TypeFormation with an existing ID
        typeFormation.setId(1L);
        TypeFormationDTO typeFormationDTO = typeFormationMapper.toDto(typeFormation);

        int databaseSizeBeforeCreate = typeFormationRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(typeFormationSearchRepository.findAll());

        // An entity with an existing ID cannot be created, so this API call must fail
        restTypeFormationMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(typeFormationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TypeFormation in the database
        List<TypeFormation> typeFormationList = typeFormationRepository.findAll();
        assertThat(typeFormationList).hasSize(databaseSizeBeforeCreate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(typeFormationSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void checkLibelleTypeFormationIsRequired() throws Exception {
        int databaseSizeBeforeTest = typeFormationRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(typeFormationSearchRepository.findAll());
        // set the field null
        typeFormation.setLibelleTypeFormation(null);

        // Create the TypeFormation, which fails.
        TypeFormationDTO typeFormationDTO = typeFormationMapper.toDto(typeFormation);

        restTypeFormationMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(typeFormationDTO))
            )
            .andExpect(status().isBadRequest());

        List<TypeFormation> typeFormationList = typeFormationRepository.findAll();
        assertThat(typeFormationList).hasSize(databaseSizeBeforeTest);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(typeFormationSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void getAllTypeFormations() throws Exception {
        // Initialize the database
        typeFormationRepository.saveAndFlush(typeFormation);

        // Get all the typeFormationList
        restTypeFormationMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(typeFormation.getId().intValue())))
            .andExpect(jsonPath("$.[*].libelleTypeFormation").value(hasItem(DEFAULT_LIBELLE_TYPE_FORMATION)));
    }

    @Test
    @Transactional
    void getTypeFormation() throws Exception {
        // Initialize the database
        typeFormationRepository.saveAndFlush(typeFormation);

        // Get the typeFormation
        restTypeFormationMockMvc
            .perform(get(ENTITY_API_URL_ID, typeFormation.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(typeFormation.getId().intValue()))
            .andExpect(jsonPath("$.libelleTypeFormation").value(DEFAULT_LIBELLE_TYPE_FORMATION));
    }

    @Test
    @Transactional
    void getNonExistingTypeFormation() throws Exception {
        // Get the typeFormation
        restTypeFormationMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingTypeFormation() throws Exception {
        // Initialize the database
        typeFormationRepository.saveAndFlush(typeFormation);

        int databaseSizeBeforeUpdate = typeFormationRepository.findAll().size();
        typeFormationSearchRepository.save(typeFormation);
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(typeFormationSearchRepository.findAll());

        // Update the typeFormation
        TypeFormation updatedTypeFormation = typeFormationRepository.findById(typeFormation.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedTypeFormation are not directly saved in db
        em.detach(updatedTypeFormation);
        updatedTypeFormation.libelleTypeFormation(UPDATED_LIBELLE_TYPE_FORMATION);
        TypeFormationDTO typeFormationDTO = typeFormationMapper.toDto(updatedTypeFormation);

        restTypeFormationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, typeFormationDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(typeFormationDTO))
            )
            .andExpect(status().isOk());

        // Validate the TypeFormation in the database
        List<TypeFormation> typeFormationList = typeFormationRepository.findAll();
        assertThat(typeFormationList).hasSize(databaseSizeBeforeUpdate);
        TypeFormation testTypeFormation = typeFormationList.get(typeFormationList.size() - 1);
        assertThat(testTypeFormation.getLibelleTypeFormation()).isEqualTo(UPDATED_LIBELLE_TYPE_FORMATION);
        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(typeFormationSearchRepository.findAll());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
                List<TypeFormation> typeFormationSearchList = IterableUtils.toList(typeFormationSearchRepository.findAll());
                TypeFormation testTypeFormationSearch = typeFormationSearchList.get(searchDatabaseSizeAfter - 1);
                assertThat(testTypeFormationSearch.getLibelleTypeFormation()).isEqualTo(UPDATED_LIBELLE_TYPE_FORMATION);
            });
    }

    @Test
    @Transactional
    void putNonExistingTypeFormation() throws Exception {
        int databaseSizeBeforeUpdate = typeFormationRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(typeFormationSearchRepository.findAll());
        typeFormation.setId(longCount.incrementAndGet());

        // Create the TypeFormation
        TypeFormationDTO typeFormationDTO = typeFormationMapper.toDto(typeFormation);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTypeFormationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, typeFormationDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(typeFormationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TypeFormation in the database
        List<TypeFormation> typeFormationList = typeFormationRepository.findAll();
        assertThat(typeFormationList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(typeFormationSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void putWithIdMismatchTypeFormation() throws Exception {
        int databaseSizeBeforeUpdate = typeFormationRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(typeFormationSearchRepository.findAll());
        typeFormation.setId(longCount.incrementAndGet());

        // Create the TypeFormation
        TypeFormationDTO typeFormationDTO = typeFormationMapper.toDto(typeFormation);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTypeFormationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(typeFormationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TypeFormation in the database
        List<TypeFormation> typeFormationList = typeFormationRepository.findAll();
        assertThat(typeFormationList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(typeFormationSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamTypeFormation() throws Exception {
        int databaseSizeBeforeUpdate = typeFormationRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(typeFormationSearchRepository.findAll());
        typeFormation.setId(longCount.incrementAndGet());

        // Create the TypeFormation
        TypeFormationDTO typeFormationDTO = typeFormationMapper.toDto(typeFormation);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTypeFormationMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(typeFormationDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the TypeFormation in the database
        List<TypeFormation> typeFormationList = typeFormationRepository.findAll();
        assertThat(typeFormationList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(typeFormationSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void partialUpdateTypeFormationWithPatch() throws Exception {
        // Initialize the database
        typeFormationRepository.saveAndFlush(typeFormation);

        int databaseSizeBeforeUpdate = typeFormationRepository.findAll().size();

        // Update the typeFormation using partial update
        TypeFormation partialUpdatedTypeFormation = new TypeFormation();
        partialUpdatedTypeFormation.setId(typeFormation.getId());

        restTypeFormationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTypeFormation.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedTypeFormation))
            )
            .andExpect(status().isOk());

        // Validate the TypeFormation in the database
        List<TypeFormation> typeFormationList = typeFormationRepository.findAll();
        assertThat(typeFormationList).hasSize(databaseSizeBeforeUpdate);
        TypeFormation testTypeFormation = typeFormationList.get(typeFormationList.size() - 1);
        assertThat(testTypeFormation.getLibelleTypeFormation()).isEqualTo(DEFAULT_LIBELLE_TYPE_FORMATION);
    }

    @Test
    @Transactional
    void fullUpdateTypeFormationWithPatch() throws Exception {
        // Initialize the database
        typeFormationRepository.saveAndFlush(typeFormation);

        int databaseSizeBeforeUpdate = typeFormationRepository.findAll().size();

        // Update the typeFormation using partial update
        TypeFormation partialUpdatedTypeFormation = new TypeFormation();
        partialUpdatedTypeFormation.setId(typeFormation.getId());

        partialUpdatedTypeFormation.libelleTypeFormation(UPDATED_LIBELLE_TYPE_FORMATION);

        restTypeFormationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTypeFormation.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedTypeFormation))
            )
            .andExpect(status().isOk());

        // Validate the TypeFormation in the database
        List<TypeFormation> typeFormationList = typeFormationRepository.findAll();
        assertThat(typeFormationList).hasSize(databaseSizeBeforeUpdate);
        TypeFormation testTypeFormation = typeFormationList.get(typeFormationList.size() - 1);
        assertThat(testTypeFormation.getLibelleTypeFormation()).isEqualTo(UPDATED_LIBELLE_TYPE_FORMATION);
    }

    @Test
    @Transactional
    void patchNonExistingTypeFormation() throws Exception {
        int databaseSizeBeforeUpdate = typeFormationRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(typeFormationSearchRepository.findAll());
        typeFormation.setId(longCount.incrementAndGet());

        // Create the TypeFormation
        TypeFormationDTO typeFormationDTO = typeFormationMapper.toDto(typeFormation);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTypeFormationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, typeFormationDTO.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(typeFormationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TypeFormation in the database
        List<TypeFormation> typeFormationList = typeFormationRepository.findAll();
        assertThat(typeFormationList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(typeFormationSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void patchWithIdMismatchTypeFormation() throws Exception {
        int databaseSizeBeforeUpdate = typeFormationRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(typeFormationSearchRepository.findAll());
        typeFormation.setId(longCount.incrementAndGet());

        // Create the TypeFormation
        TypeFormationDTO typeFormationDTO = typeFormationMapper.toDto(typeFormation);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTypeFormationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(typeFormationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TypeFormation in the database
        List<TypeFormation> typeFormationList = typeFormationRepository.findAll();
        assertThat(typeFormationList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(typeFormationSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamTypeFormation() throws Exception {
        int databaseSizeBeforeUpdate = typeFormationRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(typeFormationSearchRepository.findAll());
        typeFormation.setId(longCount.incrementAndGet());

        // Create the TypeFormation
        TypeFormationDTO typeFormationDTO = typeFormationMapper.toDto(typeFormation);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTypeFormationMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(typeFormationDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the TypeFormation in the database
        List<TypeFormation> typeFormationList = typeFormationRepository.findAll();
        assertThat(typeFormationList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(typeFormationSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void deleteTypeFormation() throws Exception {
        // Initialize the database
        typeFormationRepository.saveAndFlush(typeFormation);
        typeFormationRepository.save(typeFormation);
        typeFormationSearchRepository.save(typeFormation);

        int databaseSizeBeforeDelete = typeFormationRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(typeFormationSearchRepository.findAll());
        assertThat(searchDatabaseSizeBefore).isEqualTo(databaseSizeBeforeDelete);

        // Delete the typeFormation
        restTypeFormationMockMvc
            .perform(delete(ENTITY_API_URL_ID, typeFormation.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<TypeFormation> typeFormationList = typeFormationRepository.findAll();
        assertThat(typeFormationList).hasSize(databaseSizeBeforeDelete - 1);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(typeFormationSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore - 1);
    }

    @Test
    @Transactional
    void searchTypeFormation() throws Exception {
        // Initialize the database
        typeFormation = typeFormationRepository.saveAndFlush(typeFormation);
        typeFormationSearchRepository.save(typeFormation);

        // Search the typeFormation
        restTypeFormationMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + typeFormation.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(typeFormation.getId().intValue())))
            .andExpect(jsonPath("$.[*].libelleTypeFormation").value(hasItem(DEFAULT_LIBELLE_TYPE_FORMATION)));
    }
}
