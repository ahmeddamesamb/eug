package sn.ugb.ged.web.rest;

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
import sn.ugb.ged.IntegrationTest;
import sn.ugb.ged.domain.TypeDocument;
import sn.ugb.ged.repository.TypeDocumentRepository;
import sn.ugb.ged.service.dto.TypeDocumentDTO;
import sn.ugb.ged.service.mapper.TypeDocumentMapper;

/**
 * Integration tests for the {@link TypeDocumentResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class TypeDocumentResourceIT {

    private static final String DEFAULT_LIBELLE = "AAAAAAAAAA";
    private static final String UPDATED_LIBELLE = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/type-documents";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private TypeDocumentRepository typeDocumentRepository;

    @Autowired
    private TypeDocumentMapper typeDocumentMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restTypeDocumentMockMvc;

    private TypeDocument typeDocument;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TypeDocument createEntity(EntityManager em) {
        TypeDocument typeDocument = new TypeDocument().libelle(DEFAULT_LIBELLE);
        return typeDocument;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TypeDocument createUpdatedEntity(EntityManager em) {
        TypeDocument typeDocument = new TypeDocument().libelle(UPDATED_LIBELLE);
        return typeDocument;
    }

    @BeforeEach
    public void initTest() {
        typeDocument = createEntity(em);
    }

    @Test
    @Transactional
    void createTypeDocument() throws Exception {
        int databaseSizeBeforeCreate = typeDocumentRepository.findAll().size();
        // Create the TypeDocument
        TypeDocumentDTO typeDocumentDTO = typeDocumentMapper.toDto(typeDocument);
        restTypeDocumentMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(typeDocumentDTO))
            )
            .andExpect(status().isCreated());

        // Validate the TypeDocument in the database
        List<TypeDocument> typeDocumentList = typeDocumentRepository.findAll();
        assertThat(typeDocumentList).hasSize(databaseSizeBeforeCreate + 1);
        TypeDocument testTypeDocument = typeDocumentList.get(typeDocumentList.size() - 1);
        assertThat(testTypeDocument.getLibelle()).isEqualTo(DEFAULT_LIBELLE);
    }

    @Test
    @Transactional
    void createTypeDocumentWithExistingId() throws Exception {
        // Create the TypeDocument with an existing ID
        typeDocument.setId(1L);
        TypeDocumentDTO typeDocumentDTO = typeDocumentMapper.toDto(typeDocument);

        int databaseSizeBeforeCreate = typeDocumentRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restTypeDocumentMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(typeDocumentDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TypeDocument in the database
        List<TypeDocument> typeDocumentList = typeDocumentRepository.findAll();
        assertThat(typeDocumentList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllTypeDocuments() throws Exception {
        // Initialize the database
        typeDocumentRepository.saveAndFlush(typeDocument);

        // Get all the typeDocumentList
        restTypeDocumentMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(typeDocument.getId().intValue())))
            .andExpect(jsonPath("$.[*].libelle").value(hasItem(DEFAULT_LIBELLE)));
    }

    @Test
    @Transactional
    void getTypeDocument() throws Exception {
        // Initialize the database
        typeDocumentRepository.saveAndFlush(typeDocument);

        // Get the typeDocument
        restTypeDocumentMockMvc
            .perform(get(ENTITY_API_URL_ID, typeDocument.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(typeDocument.getId().intValue()))
            .andExpect(jsonPath("$.libelle").value(DEFAULT_LIBELLE));
    }

    @Test
    @Transactional
    void getNonExistingTypeDocument() throws Exception {
        // Get the typeDocument
        restTypeDocumentMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingTypeDocument() throws Exception {
        // Initialize the database
        typeDocumentRepository.saveAndFlush(typeDocument);

        int databaseSizeBeforeUpdate = typeDocumentRepository.findAll().size();

        // Update the typeDocument
        TypeDocument updatedTypeDocument = typeDocumentRepository.findById(typeDocument.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedTypeDocument are not directly saved in db
        em.detach(updatedTypeDocument);
        updatedTypeDocument.libelle(UPDATED_LIBELLE);
        TypeDocumentDTO typeDocumentDTO = typeDocumentMapper.toDto(updatedTypeDocument);

        restTypeDocumentMockMvc
            .perform(
                put(ENTITY_API_URL_ID, typeDocumentDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(typeDocumentDTO))
            )
            .andExpect(status().isOk());

        // Validate the TypeDocument in the database
        List<TypeDocument> typeDocumentList = typeDocumentRepository.findAll();
        assertThat(typeDocumentList).hasSize(databaseSizeBeforeUpdate);
        TypeDocument testTypeDocument = typeDocumentList.get(typeDocumentList.size() - 1);
        assertThat(testTypeDocument.getLibelle()).isEqualTo(UPDATED_LIBELLE);
    }

    @Test
    @Transactional
    void putNonExistingTypeDocument() throws Exception {
        int databaseSizeBeforeUpdate = typeDocumentRepository.findAll().size();
        typeDocument.setId(longCount.incrementAndGet());

        // Create the TypeDocument
        TypeDocumentDTO typeDocumentDTO = typeDocumentMapper.toDto(typeDocument);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTypeDocumentMockMvc
            .perform(
                put(ENTITY_API_URL_ID, typeDocumentDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(typeDocumentDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TypeDocument in the database
        List<TypeDocument> typeDocumentList = typeDocumentRepository.findAll();
        assertThat(typeDocumentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchTypeDocument() throws Exception {
        int databaseSizeBeforeUpdate = typeDocumentRepository.findAll().size();
        typeDocument.setId(longCount.incrementAndGet());

        // Create the TypeDocument
        TypeDocumentDTO typeDocumentDTO = typeDocumentMapper.toDto(typeDocument);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTypeDocumentMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(typeDocumentDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TypeDocument in the database
        List<TypeDocument> typeDocumentList = typeDocumentRepository.findAll();
        assertThat(typeDocumentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamTypeDocument() throws Exception {
        int databaseSizeBeforeUpdate = typeDocumentRepository.findAll().size();
        typeDocument.setId(longCount.incrementAndGet());

        // Create the TypeDocument
        TypeDocumentDTO typeDocumentDTO = typeDocumentMapper.toDto(typeDocument);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTypeDocumentMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(typeDocumentDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the TypeDocument in the database
        List<TypeDocument> typeDocumentList = typeDocumentRepository.findAll();
        assertThat(typeDocumentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateTypeDocumentWithPatch() throws Exception {
        // Initialize the database
        typeDocumentRepository.saveAndFlush(typeDocument);

        int databaseSizeBeforeUpdate = typeDocumentRepository.findAll().size();

        // Update the typeDocument using partial update
        TypeDocument partialUpdatedTypeDocument = new TypeDocument();
        partialUpdatedTypeDocument.setId(typeDocument.getId());

        restTypeDocumentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTypeDocument.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedTypeDocument))
            )
            .andExpect(status().isOk());

        // Validate the TypeDocument in the database
        List<TypeDocument> typeDocumentList = typeDocumentRepository.findAll();
        assertThat(typeDocumentList).hasSize(databaseSizeBeforeUpdate);
        TypeDocument testTypeDocument = typeDocumentList.get(typeDocumentList.size() - 1);
        assertThat(testTypeDocument.getLibelle()).isEqualTo(DEFAULT_LIBELLE);
    }

    @Test
    @Transactional
    void fullUpdateTypeDocumentWithPatch() throws Exception {
        // Initialize the database
        typeDocumentRepository.saveAndFlush(typeDocument);

        int databaseSizeBeforeUpdate = typeDocumentRepository.findAll().size();

        // Update the typeDocument using partial update
        TypeDocument partialUpdatedTypeDocument = new TypeDocument();
        partialUpdatedTypeDocument.setId(typeDocument.getId());

        partialUpdatedTypeDocument.libelle(UPDATED_LIBELLE);

        restTypeDocumentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTypeDocument.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedTypeDocument))
            )
            .andExpect(status().isOk());

        // Validate the TypeDocument in the database
        List<TypeDocument> typeDocumentList = typeDocumentRepository.findAll();
        assertThat(typeDocumentList).hasSize(databaseSizeBeforeUpdate);
        TypeDocument testTypeDocument = typeDocumentList.get(typeDocumentList.size() - 1);
        assertThat(testTypeDocument.getLibelle()).isEqualTo(UPDATED_LIBELLE);
    }

    @Test
    @Transactional
    void patchNonExistingTypeDocument() throws Exception {
        int databaseSizeBeforeUpdate = typeDocumentRepository.findAll().size();
        typeDocument.setId(longCount.incrementAndGet());

        // Create the TypeDocument
        TypeDocumentDTO typeDocumentDTO = typeDocumentMapper.toDto(typeDocument);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTypeDocumentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, typeDocumentDTO.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(typeDocumentDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TypeDocument in the database
        List<TypeDocument> typeDocumentList = typeDocumentRepository.findAll();
        assertThat(typeDocumentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchTypeDocument() throws Exception {
        int databaseSizeBeforeUpdate = typeDocumentRepository.findAll().size();
        typeDocument.setId(longCount.incrementAndGet());

        // Create the TypeDocument
        TypeDocumentDTO typeDocumentDTO = typeDocumentMapper.toDto(typeDocument);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTypeDocumentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(typeDocumentDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TypeDocument in the database
        List<TypeDocument> typeDocumentList = typeDocumentRepository.findAll();
        assertThat(typeDocumentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamTypeDocument() throws Exception {
        int databaseSizeBeforeUpdate = typeDocumentRepository.findAll().size();
        typeDocument.setId(longCount.incrementAndGet());

        // Create the TypeDocument
        TypeDocumentDTO typeDocumentDTO = typeDocumentMapper.toDto(typeDocument);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTypeDocumentMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(typeDocumentDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the TypeDocument in the database
        List<TypeDocument> typeDocumentList = typeDocumentRepository.findAll();
        assertThat(typeDocumentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteTypeDocument() throws Exception {
        // Initialize the database
        typeDocumentRepository.saveAndFlush(typeDocument);

        int databaseSizeBeforeDelete = typeDocumentRepository.findAll().size();

        // Delete the typeDocument
        restTypeDocumentMockMvc
            .perform(delete(ENTITY_API_URL_ID, typeDocument.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<TypeDocument> typeDocumentList = typeDocumentRepository.findAll();
        assertThat(typeDocumentList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
