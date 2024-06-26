package sn.ugb.gd.web.rest;

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
import sn.ugb.gd.IntegrationTest;
import sn.ugb.gd.domain.TypeRapport;
import sn.ugb.gd.repository.TypeRapportRepository;
import sn.ugb.gd.repository.search.TypeRapportSearchRepository;
import sn.ugb.gd.service.dto.TypeRapportDTO;
import sn.ugb.gd.service.mapper.TypeRapportMapper;

/**
 * Integration tests for the {@link TypeRapportResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class TypeRapportResourceIT {

    private static final String DEFAULT_LIBELLE_TYPE_RAPPORT = "AAAAAAAAAA";
    private static final String UPDATED_LIBELLE_TYPE_RAPPORT = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/type-rapports";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/type-rapports/_search";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private TypeRapportRepository typeRapportRepository;

    @Autowired
    private TypeRapportMapper typeRapportMapper;

    @Autowired
    private TypeRapportSearchRepository typeRapportSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restTypeRapportMockMvc;

    private TypeRapport typeRapport;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TypeRapport createEntity(EntityManager em) {
        TypeRapport typeRapport = new TypeRapport().libelleTypeRapport(DEFAULT_LIBELLE_TYPE_RAPPORT);
        return typeRapport;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TypeRapport createUpdatedEntity(EntityManager em) {
        TypeRapport typeRapport = new TypeRapport().libelleTypeRapport(UPDATED_LIBELLE_TYPE_RAPPORT);
        return typeRapport;
    }

    @AfterEach
    public void cleanupElasticSearchRepository() {
        typeRapportSearchRepository.deleteAll();
        assertThat(typeRapportSearchRepository.count()).isEqualTo(0);
    }

    @BeforeEach
    public void initTest() {
        typeRapport = createEntity(em);
    }

    @Test
    @Transactional
    void createTypeRapport() throws Exception {
        int databaseSizeBeforeCreate = typeRapportRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(typeRapportSearchRepository.findAll());
        // Create the TypeRapport
        TypeRapportDTO typeRapportDTO = typeRapportMapper.toDto(typeRapport);
        restTypeRapportMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(typeRapportDTO))
            )
            .andExpect(status().isCreated());

        // Validate the TypeRapport in the database
        List<TypeRapport> typeRapportList = typeRapportRepository.findAll();
        assertThat(typeRapportList).hasSize(databaseSizeBeforeCreate + 1);
        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(typeRapportSearchRepository.findAll());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore + 1);
            });
        TypeRapport testTypeRapport = typeRapportList.get(typeRapportList.size() - 1);
        assertThat(testTypeRapport.getLibelleTypeRapport()).isEqualTo(DEFAULT_LIBELLE_TYPE_RAPPORT);
    }

    @Test
    @Transactional
    void createTypeRapportWithExistingId() throws Exception {
        // Create the TypeRapport with an existing ID
        typeRapport.setId(1L);
        TypeRapportDTO typeRapportDTO = typeRapportMapper.toDto(typeRapport);

        int databaseSizeBeforeCreate = typeRapportRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(typeRapportSearchRepository.findAll());

        // An entity with an existing ID cannot be created, so this API call must fail
        restTypeRapportMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(typeRapportDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TypeRapport in the database
        List<TypeRapport> typeRapportList = typeRapportRepository.findAll();
        assertThat(typeRapportList).hasSize(databaseSizeBeforeCreate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(typeRapportSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void getAllTypeRapports() throws Exception {
        // Initialize the database
        typeRapportRepository.saveAndFlush(typeRapport);

        // Get all the typeRapportList
        restTypeRapportMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(typeRapport.getId().intValue())))
            .andExpect(jsonPath("$.[*].libelleTypeRapport").value(hasItem(DEFAULT_LIBELLE_TYPE_RAPPORT)));
    }

    @Test
    @Transactional
    void getTypeRapport() throws Exception {
        // Initialize the database
        typeRapportRepository.saveAndFlush(typeRapport);

        // Get the typeRapport
        restTypeRapportMockMvc
            .perform(get(ENTITY_API_URL_ID, typeRapport.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(typeRapport.getId().intValue()))
            .andExpect(jsonPath("$.libelleTypeRapport").value(DEFAULT_LIBELLE_TYPE_RAPPORT));
    }

    @Test
    @Transactional
    void getNonExistingTypeRapport() throws Exception {
        // Get the typeRapport
        restTypeRapportMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingTypeRapport() throws Exception {
        // Initialize the database
        typeRapportRepository.saveAndFlush(typeRapport);

        int databaseSizeBeforeUpdate = typeRapportRepository.findAll().size();
        typeRapportSearchRepository.save(typeRapport);
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(typeRapportSearchRepository.findAll());

        // Update the typeRapport
        TypeRapport updatedTypeRapport = typeRapportRepository.findById(typeRapport.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedTypeRapport are not directly saved in db
        em.detach(updatedTypeRapport);
        updatedTypeRapport.libelleTypeRapport(UPDATED_LIBELLE_TYPE_RAPPORT);
        TypeRapportDTO typeRapportDTO = typeRapportMapper.toDto(updatedTypeRapport);

        restTypeRapportMockMvc
            .perform(
                put(ENTITY_API_URL_ID, typeRapportDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(typeRapportDTO))
            )
            .andExpect(status().isOk());

        // Validate the TypeRapport in the database
        List<TypeRapport> typeRapportList = typeRapportRepository.findAll();
        assertThat(typeRapportList).hasSize(databaseSizeBeforeUpdate);
        TypeRapport testTypeRapport = typeRapportList.get(typeRapportList.size() - 1);
        assertThat(testTypeRapport.getLibelleTypeRapport()).isEqualTo(UPDATED_LIBELLE_TYPE_RAPPORT);
        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(typeRapportSearchRepository.findAll());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
                List<TypeRapport> typeRapportSearchList = IterableUtils.toList(typeRapportSearchRepository.findAll());
                TypeRapport testTypeRapportSearch = typeRapportSearchList.get(searchDatabaseSizeAfter - 1);
                assertThat(testTypeRapportSearch.getLibelleTypeRapport()).isEqualTo(UPDATED_LIBELLE_TYPE_RAPPORT);
            });
    }

    @Test
    @Transactional
    void putNonExistingTypeRapport() throws Exception {
        int databaseSizeBeforeUpdate = typeRapportRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(typeRapportSearchRepository.findAll());
        typeRapport.setId(longCount.incrementAndGet());

        // Create the TypeRapport
        TypeRapportDTO typeRapportDTO = typeRapportMapper.toDto(typeRapport);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTypeRapportMockMvc
            .perform(
                put(ENTITY_API_URL_ID, typeRapportDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(typeRapportDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TypeRapport in the database
        List<TypeRapport> typeRapportList = typeRapportRepository.findAll();
        assertThat(typeRapportList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(typeRapportSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void putWithIdMismatchTypeRapport() throws Exception {
        int databaseSizeBeforeUpdate = typeRapportRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(typeRapportSearchRepository.findAll());
        typeRapport.setId(longCount.incrementAndGet());

        // Create the TypeRapport
        TypeRapportDTO typeRapportDTO = typeRapportMapper.toDto(typeRapport);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTypeRapportMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(typeRapportDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TypeRapport in the database
        List<TypeRapport> typeRapportList = typeRapportRepository.findAll();
        assertThat(typeRapportList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(typeRapportSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamTypeRapport() throws Exception {
        int databaseSizeBeforeUpdate = typeRapportRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(typeRapportSearchRepository.findAll());
        typeRapport.setId(longCount.incrementAndGet());

        // Create the TypeRapport
        TypeRapportDTO typeRapportDTO = typeRapportMapper.toDto(typeRapport);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTypeRapportMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(typeRapportDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the TypeRapport in the database
        List<TypeRapport> typeRapportList = typeRapportRepository.findAll();
        assertThat(typeRapportList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(typeRapportSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void partialUpdateTypeRapportWithPatch() throws Exception {
        // Initialize the database
        typeRapportRepository.saveAndFlush(typeRapport);

        int databaseSizeBeforeUpdate = typeRapportRepository.findAll().size();

        // Update the typeRapport using partial update
        TypeRapport partialUpdatedTypeRapport = new TypeRapport();
        partialUpdatedTypeRapport.setId(typeRapport.getId());

        partialUpdatedTypeRapport.libelleTypeRapport(UPDATED_LIBELLE_TYPE_RAPPORT);

        restTypeRapportMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTypeRapport.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedTypeRapport))
            )
            .andExpect(status().isOk());

        // Validate the TypeRapport in the database
        List<TypeRapport> typeRapportList = typeRapportRepository.findAll();
        assertThat(typeRapportList).hasSize(databaseSizeBeforeUpdate);
        TypeRapport testTypeRapport = typeRapportList.get(typeRapportList.size() - 1);
        assertThat(testTypeRapport.getLibelleTypeRapport()).isEqualTo(UPDATED_LIBELLE_TYPE_RAPPORT);
    }

    @Test
    @Transactional
    void fullUpdateTypeRapportWithPatch() throws Exception {
        // Initialize the database
        typeRapportRepository.saveAndFlush(typeRapport);

        int databaseSizeBeforeUpdate = typeRapportRepository.findAll().size();

        // Update the typeRapport using partial update
        TypeRapport partialUpdatedTypeRapport = new TypeRapport();
        partialUpdatedTypeRapport.setId(typeRapport.getId());

        partialUpdatedTypeRapport.libelleTypeRapport(UPDATED_LIBELLE_TYPE_RAPPORT);

        restTypeRapportMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTypeRapport.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedTypeRapport))
            )
            .andExpect(status().isOk());

        // Validate the TypeRapport in the database
        List<TypeRapport> typeRapportList = typeRapportRepository.findAll();
        assertThat(typeRapportList).hasSize(databaseSizeBeforeUpdate);
        TypeRapport testTypeRapport = typeRapportList.get(typeRapportList.size() - 1);
        assertThat(testTypeRapport.getLibelleTypeRapport()).isEqualTo(UPDATED_LIBELLE_TYPE_RAPPORT);
    }

    @Test
    @Transactional
    void patchNonExistingTypeRapport() throws Exception {
        int databaseSizeBeforeUpdate = typeRapportRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(typeRapportSearchRepository.findAll());
        typeRapport.setId(longCount.incrementAndGet());

        // Create the TypeRapport
        TypeRapportDTO typeRapportDTO = typeRapportMapper.toDto(typeRapport);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTypeRapportMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, typeRapportDTO.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(typeRapportDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TypeRapport in the database
        List<TypeRapport> typeRapportList = typeRapportRepository.findAll();
        assertThat(typeRapportList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(typeRapportSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void patchWithIdMismatchTypeRapport() throws Exception {
        int databaseSizeBeforeUpdate = typeRapportRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(typeRapportSearchRepository.findAll());
        typeRapport.setId(longCount.incrementAndGet());

        // Create the TypeRapport
        TypeRapportDTO typeRapportDTO = typeRapportMapper.toDto(typeRapport);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTypeRapportMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(typeRapportDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TypeRapport in the database
        List<TypeRapport> typeRapportList = typeRapportRepository.findAll();
        assertThat(typeRapportList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(typeRapportSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamTypeRapport() throws Exception {
        int databaseSizeBeforeUpdate = typeRapportRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(typeRapportSearchRepository.findAll());
        typeRapport.setId(longCount.incrementAndGet());

        // Create the TypeRapport
        TypeRapportDTO typeRapportDTO = typeRapportMapper.toDto(typeRapport);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTypeRapportMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(typeRapportDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the TypeRapport in the database
        List<TypeRapport> typeRapportList = typeRapportRepository.findAll();
        assertThat(typeRapportList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(typeRapportSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void deleteTypeRapport() throws Exception {
        // Initialize the database
        typeRapportRepository.saveAndFlush(typeRapport);
        typeRapportRepository.save(typeRapport);
        typeRapportSearchRepository.save(typeRapport);

        int databaseSizeBeforeDelete = typeRapportRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(typeRapportSearchRepository.findAll());
        assertThat(searchDatabaseSizeBefore).isEqualTo(databaseSizeBeforeDelete);

        // Delete the typeRapport
        restTypeRapportMockMvc
            .perform(delete(ENTITY_API_URL_ID, typeRapport.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<TypeRapport> typeRapportList = typeRapportRepository.findAll();
        assertThat(typeRapportList).hasSize(databaseSizeBeforeDelete - 1);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(typeRapportSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore - 1);
    }

    @Test
    @Transactional
    void searchTypeRapport() throws Exception {
        // Initialize the database
        typeRapport = typeRapportRepository.saveAndFlush(typeRapport);
        typeRapportSearchRepository.save(typeRapport);

        // Search the typeRapport
        restTypeRapportMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + typeRapport.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(typeRapport.getId().intValue())))
            .andExpect(jsonPath("$.[*].libelleTypeRapport").value(hasItem(DEFAULT_LIBELLE_TYPE_RAPPORT)));
    }
}
