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
import sn.ugb.gir.domain.TypeOperation;
import sn.ugb.gir.repository.TypeOperationRepository;
import sn.ugb.gir.repository.search.TypeOperationSearchRepository;
import sn.ugb.gir.service.dto.TypeOperationDTO;
import sn.ugb.gir.service.mapper.TypeOperationMapper;

/**
 * Integration tests for the {@link TypeOperationResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class TypeOperationResourceIT {

    private static final String DEFAULT_LIBELLE_TYPE_OPERATION = "AAAAAAAAAA";
    private static final String UPDATED_LIBELLE_TYPE_OPERATION = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/type-operations";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/type-operations/_search";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private TypeOperationRepository typeOperationRepository;

    @Autowired
    private TypeOperationMapper typeOperationMapper;

    @Autowired
    private TypeOperationSearchRepository typeOperationSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restTypeOperationMockMvc;

    private TypeOperation typeOperation;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TypeOperation createEntity(EntityManager em) {
        TypeOperation typeOperation = new TypeOperation().libelleTypeOperation(DEFAULT_LIBELLE_TYPE_OPERATION);
        return typeOperation;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TypeOperation createUpdatedEntity(EntityManager em) {
        TypeOperation typeOperation = new TypeOperation().libelleTypeOperation(UPDATED_LIBELLE_TYPE_OPERATION);
        return typeOperation;
    }

    @AfterEach
    public void cleanupElasticSearchRepository() {
        typeOperationSearchRepository.deleteAll();
        assertThat(typeOperationSearchRepository.count()).isEqualTo(0);
    }

    @BeforeEach
    public void initTest() {
        typeOperation = createEntity(em);
    }

    @Test
    @Transactional
    void createTypeOperation() throws Exception {
        int databaseSizeBeforeCreate = typeOperationRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(typeOperationSearchRepository.findAll());
        // Create the TypeOperation
        TypeOperationDTO typeOperationDTO = typeOperationMapper.toDto(typeOperation);
        restTypeOperationMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(typeOperationDTO))
            )
            .andExpect(status().isCreated());

        // Validate the TypeOperation in the database
        List<TypeOperation> typeOperationList = typeOperationRepository.findAll();
        assertThat(typeOperationList).hasSize(databaseSizeBeforeCreate + 1);
        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(typeOperationSearchRepository.findAll());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore + 1);
            });
        TypeOperation testTypeOperation = typeOperationList.get(typeOperationList.size() - 1);
        assertThat(testTypeOperation.getLibelleTypeOperation()).isEqualTo(DEFAULT_LIBELLE_TYPE_OPERATION);
    }

    @Test
    @Transactional
    void createTypeOperationWithExistingId() throws Exception {
        // Create the TypeOperation with an existing ID
        typeOperation.setId(1L);
        TypeOperationDTO typeOperationDTO = typeOperationMapper.toDto(typeOperation);

        int databaseSizeBeforeCreate = typeOperationRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(typeOperationSearchRepository.findAll());

        // An entity with an existing ID cannot be created, so this API call must fail
        restTypeOperationMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(typeOperationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TypeOperation in the database
        List<TypeOperation> typeOperationList = typeOperationRepository.findAll();
        assertThat(typeOperationList).hasSize(databaseSizeBeforeCreate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(typeOperationSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void checkLibelleTypeOperationIsRequired() throws Exception {
        int databaseSizeBeforeTest = typeOperationRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(typeOperationSearchRepository.findAll());
        // set the field null
        typeOperation.setLibelleTypeOperation(null);

        // Create the TypeOperation, which fails.
        TypeOperationDTO typeOperationDTO = typeOperationMapper.toDto(typeOperation);

        restTypeOperationMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(typeOperationDTO))
            )
            .andExpect(status().isBadRequest());

        List<TypeOperation> typeOperationList = typeOperationRepository.findAll();
        assertThat(typeOperationList).hasSize(databaseSizeBeforeTest);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(typeOperationSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void getAllTypeOperations() throws Exception {
        // Initialize the database
        typeOperationRepository.saveAndFlush(typeOperation);

        // Get all the typeOperationList
        restTypeOperationMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(typeOperation.getId().intValue())))
            .andExpect(jsonPath("$.[*].libelleTypeOperation").value(hasItem(DEFAULT_LIBELLE_TYPE_OPERATION)));
    }

    @Test
    @Transactional
    void getTypeOperation() throws Exception {
        // Initialize the database
        typeOperationRepository.saveAndFlush(typeOperation);

        // Get the typeOperation
        restTypeOperationMockMvc
            .perform(get(ENTITY_API_URL_ID, typeOperation.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(typeOperation.getId().intValue()))
            .andExpect(jsonPath("$.libelleTypeOperation").value(DEFAULT_LIBELLE_TYPE_OPERATION));
    }

    @Test
    @Transactional
    void getNonExistingTypeOperation() throws Exception {
        // Get the typeOperation
        restTypeOperationMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingTypeOperation() throws Exception {
        // Initialize the database
        typeOperationRepository.saveAndFlush(typeOperation);

        int databaseSizeBeforeUpdate = typeOperationRepository.findAll().size();
        typeOperationSearchRepository.save(typeOperation);
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(typeOperationSearchRepository.findAll());

        // Update the typeOperation
        TypeOperation updatedTypeOperation = typeOperationRepository.findById(typeOperation.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedTypeOperation are not directly saved in db
        em.detach(updatedTypeOperation);
        updatedTypeOperation.libelleTypeOperation(UPDATED_LIBELLE_TYPE_OPERATION);
        TypeOperationDTO typeOperationDTO = typeOperationMapper.toDto(updatedTypeOperation);

        restTypeOperationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, typeOperationDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(typeOperationDTO))
            )
            .andExpect(status().isOk());

        // Validate the TypeOperation in the database
        List<TypeOperation> typeOperationList = typeOperationRepository.findAll();
        assertThat(typeOperationList).hasSize(databaseSizeBeforeUpdate);
        TypeOperation testTypeOperation = typeOperationList.get(typeOperationList.size() - 1);
        assertThat(testTypeOperation.getLibelleTypeOperation()).isEqualTo(UPDATED_LIBELLE_TYPE_OPERATION);
        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(typeOperationSearchRepository.findAll());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
                List<TypeOperation> typeOperationSearchList = IterableUtils.toList(typeOperationSearchRepository.findAll());
                TypeOperation testTypeOperationSearch = typeOperationSearchList.get(searchDatabaseSizeAfter - 1);
                assertThat(testTypeOperationSearch.getLibelleTypeOperation()).isEqualTo(UPDATED_LIBELLE_TYPE_OPERATION);
            });
    }

    @Test
    @Transactional
    void putNonExistingTypeOperation() throws Exception {
        int databaseSizeBeforeUpdate = typeOperationRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(typeOperationSearchRepository.findAll());
        typeOperation.setId(longCount.incrementAndGet());

        // Create the TypeOperation
        TypeOperationDTO typeOperationDTO = typeOperationMapper.toDto(typeOperation);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTypeOperationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, typeOperationDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(typeOperationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TypeOperation in the database
        List<TypeOperation> typeOperationList = typeOperationRepository.findAll();
        assertThat(typeOperationList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(typeOperationSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void putWithIdMismatchTypeOperation() throws Exception {
        int databaseSizeBeforeUpdate = typeOperationRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(typeOperationSearchRepository.findAll());
        typeOperation.setId(longCount.incrementAndGet());

        // Create the TypeOperation
        TypeOperationDTO typeOperationDTO = typeOperationMapper.toDto(typeOperation);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTypeOperationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(typeOperationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TypeOperation in the database
        List<TypeOperation> typeOperationList = typeOperationRepository.findAll();
        assertThat(typeOperationList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(typeOperationSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamTypeOperation() throws Exception {
        int databaseSizeBeforeUpdate = typeOperationRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(typeOperationSearchRepository.findAll());
        typeOperation.setId(longCount.incrementAndGet());

        // Create the TypeOperation
        TypeOperationDTO typeOperationDTO = typeOperationMapper.toDto(typeOperation);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTypeOperationMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(typeOperationDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the TypeOperation in the database
        List<TypeOperation> typeOperationList = typeOperationRepository.findAll();
        assertThat(typeOperationList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(typeOperationSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void partialUpdateTypeOperationWithPatch() throws Exception {
        // Initialize the database
        typeOperationRepository.saveAndFlush(typeOperation);

        int databaseSizeBeforeUpdate = typeOperationRepository.findAll().size();

        // Update the typeOperation using partial update
        TypeOperation partialUpdatedTypeOperation = new TypeOperation();
        partialUpdatedTypeOperation.setId(typeOperation.getId());

        restTypeOperationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTypeOperation.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedTypeOperation))
            )
            .andExpect(status().isOk());

        // Validate the TypeOperation in the database
        List<TypeOperation> typeOperationList = typeOperationRepository.findAll();
        assertThat(typeOperationList).hasSize(databaseSizeBeforeUpdate);
        TypeOperation testTypeOperation = typeOperationList.get(typeOperationList.size() - 1);
        assertThat(testTypeOperation.getLibelleTypeOperation()).isEqualTo(DEFAULT_LIBELLE_TYPE_OPERATION);
    }

    @Test
    @Transactional
    void fullUpdateTypeOperationWithPatch() throws Exception {
        // Initialize the database
        typeOperationRepository.saveAndFlush(typeOperation);

        int databaseSizeBeforeUpdate = typeOperationRepository.findAll().size();

        // Update the typeOperation using partial update
        TypeOperation partialUpdatedTypeOperation = new TypeOperation();
        partialUpdatedTypeOperation.setId(typeOperation.getId());

        partialUpdatedTypeOperation.libelleTypeOperation(UPDATED_LIBELLE_TYPE_OPERATION);

        restTypeOperationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTypeOperation.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedTypeOperation))
            )
            .andExpect(status().isOk());

        // Validate the TypeOperation in the database
        List<TypeOperation> typeOperationList = typeOperationRepository.findAll();
        assertThat(typeOperationList).hasSize(databaseSizeBeforeUpdate);
        TypeOperation testTypeOperation = typeOperationList.get(typeOperationList.size() - 1);
        assertThat(testTypeOperation.getLibelleTypeOperation()).isEqualTo(UPDATED_LIBELLE_TYPE_OPERATION);
    }

    @Test
    @Transactional
    void patchNonExistingTypeOperation() throws Exception {
        int databaseSizeBeforeUpdate = typeOperationRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(typeOperationSearchRepository.findAll());
        typeOperation.setId(longCount.incrementAndGet());

        // Create the TypeOperation
        TypeOperationDTO typeOperationDTO = typeOperationMapper.toDto(typeOperation);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTypeOperationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, typeOperationDTO.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(typeOperationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TypeOperation in the database
        List<TypeOperation> typeOperationList = typeOperationRepository.findAll();
        assertThat(typeOperationList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(typeOperationSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void patchWithIdMismatchTypeOperation() throws Exception {
        int databaseSizeBeforeUpdate = typeOperationRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(typeOperationSearchRepository.findAll());
        typeOperation.setId(longCount.incrementAndGet());

        // Create the TypeOperation
        TypeOperationDTO typeOperationDTO = typeOperationMapper.toDto(typeOperation);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTypeOperationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(typeOperationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TypeOperation in the database
        List<TypeOperation> typeOperationList = typeOperationRepository.findAll();
        assertThat(typeOperationList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(typeOperationSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamTypeOperation() throws Exception {
        int databaseSizeBeforeUpdate = typeOperationRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(typeOperationSearchRepository.findAll());
        typeOperation.setId(longCount.incrementAndGet());

        // Create the TypeOperation
        TypeOperationDTO typeOperationDTO = typeOperationMapper.toDto(typeOperation);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTypeOperationMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(typeOperationDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the TypeOperation in the database
        List<TypeOperation> typeOperationList = typeOperationRepository.findAll();
        assertThat(typeOperationList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(typeOperationSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void deleteTypeOperation() throws Exception {
        // Initialize the database
        typeOperationRepository.saveAndFlush(typeOperation);
        typeOperationRepository.save(typeOperation);
        typeOperationSearchRepository.save(typeOperation);

        int databaseSizeBeforeDelete = typeOperationRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(typeOperationSearchRepository.findAll());
        assertThat(searchDatabaseSizeBefore).isEqualTo(databaseSizeBeforeDelete);

        // Delete the typeOperation
        restTypeOperationMockMvc
            .perform(delete(ENTITY_API_URL_ID, typeOperation.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<TypeOperation> typeOperationList = typeOperationRepository.findAll();
        assertThat(typeOperationList).hasSize(databaseSizeBeforeDelete - 1);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(typeOperationSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore - 1);
    }

    @Test
    @Transactional
    void searchTypeOperation() throws Exception {
        // Initialize the database
        typeOperation = typeOperationRepository.saveAndFlush(typeOperation);
        typeOperationSearchRepository.save(typeOperation);

        // Search the typeOperation
        restTypeOperationMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + typeOperation.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(typeOperation.getId().intValue())))
            .andExpect(jsonPath("$.[*].libelleTypeOperation").value(hasItem(DEFAULT_LIBELLE_TYPE_OPERATION)));
    }
}
