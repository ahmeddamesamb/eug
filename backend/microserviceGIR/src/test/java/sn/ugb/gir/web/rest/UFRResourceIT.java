package sn.ugb.gir.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import jakarta.persistence.EntityManager;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import sn.ugb.gir.IntegrationTest;
import sn.ugb.gir.domain.UFR;
import sn.ugb.gir.repository.UFRRepository;
import sn.ugb.gir.service.UFRService;
import sn.ugb.gir.service.dto.UFRDTO;
import sn.ugb.gir.service.mapper.UFRMapper;

/**
 * Integration tests for the {@link UFRResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class UFRResourceIT {

    private static final String DEFAULT_LIBELE_UFR = "AAAAAAAAAA";
    private static final String UPDATED_LIBELE_UFR = "BBBBBBBBBB";

    private static final String DEFAULT_SIGLE_UFR = "AAAAAAAAAA";
    private static final String UPDATED_SIGLE_UFR = "BBBBBBBBBB";

    private static final Integer DEFAULT_SYSTEME_LMDYN = 1;
    private static final Integer UPDATED_SYSTEME_LMDYN = 2;

    private static final Integer DEFAULT_ORDRE_STAT = 1;
    private static final Integer UPDATED_ORDRE_STAT = 2;

    private static final String ENTITY_API_URL = "/api/ufrs";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private UFRRepository uFRRepository;

    @Mock
    private UFRRepository uFRRepositoryMock;

    @Autowired
    private UFRMapper uFRMapper;

    @Mock
    private UFRService uFRServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restUFRMockMvc;

    private UFR uFR;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static UFR createEntity(EntityManager em) {
        UFR uFR = new UFR()
            .libeleUFR(DEFAULT_LIBELE_UFR)
            .sigleUFR(DEFAULT_SIGLE_UFR)
            .systemeLMDYN(DEFAULT_SYSTEME_LMDYN)
            .ordreStat(DEFAULT_ORDRE_STAT);
        return uFR;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static UFR createUpdatedEntity(EntityManager em) {
        UFR uFR = new UFR()
            .libeleUFR(UPDATED_LIBELE_UFR)
            .sigleUFR(UPDATED_SIGLE_UFR)
            .systemeLMDYN(UPDATED_SYSTEME_LMDYN)
            .ordreStat(UPDATED_ORDRE_STAT);
        return uFR;
    }

    @BeforeEach
    public void initTest() {
        uFR = createEntity(em);
    }

    @Test
    @Transactional
    void createUFR() throws Exception {
        int databaseSizeBeforeCreate = uFRRepository.findAll().size();
        // Create the UFR
        UFRDTO uFRDTO = uFRMapper.toDto(uFR);
        restUFRMockMvc
            .perform(
                post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(uFRDTO))
            )
            .andExpect(status().isCreated());

        // Validate the UFR in the database
        List<UFR> uFRList = uFRRepository.findAll();
        assertThat(uFRList).hasSize(databaseSizeBeforeCreate + 1);
        UFR testUFR = uFRList.get(uFRList.size() - 1);
        assertThat(testUFR.getLibeleUFR()).isEqualTo(DEFAULT_LIBELE_UFR);
        assertThat(testUFR.getSigleUFR()).isEqualTo(DEFAULT_SIGLE_UFR);
        assertThat(testUFR.getSystemeLMDYN()).isEqualTo(DEFAULT_SYSTEME_LMDYN);
        assertThat(testUFR.getOrdreStat()).isEqualTo(DEFAULT_ORDRE_STAT);
    }

    @Test
    @Transactional
    void createUFRWithExistingId() throws Exception {
        // Create the UFR with an existing ID
        uFR.setId(1L);
        UFRDTO uFRDTO = uFRMapper.toDto(uFR);

        int databaseSizeBeforeCreate = uFRRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restUFRMockMvc
            .perform(
                post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(uFRDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the UFR in the database
        List<UFR> uFRList = uFRRepository.findAll();
        assertThat(uFRList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllUFRS() throws Exception {
        // Initialize the database
        uFRRepository.saveAndFlush(uFR);

        // Get all the uFRList
        restUFRMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(uFR.getId().intValue())))
            .andExpect(jsonPath("$.[*].libeleUFR").value(hasItem(DEFAULT_LIBELE_UFR)))
            .andExpect(jsonPath("$.[*].sigleUFR").value(hasItem(DEFAULT_SIGLE_UFR)))
            .andExpect(jsonPath("$.[*].systemeLMDYN").value(hasItem(DEFAULT_SYSTEME_LMDYN)))
            .andExpect(jsonPath("$.[*].ordreStat").value(hasItem(DEFAULT_ORDRE_STAT)));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllUFRSWithEagerRelationshipsIsEnabled() throws Exception {
        when(uFRServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restUFRMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(uFRServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllUFRSWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(uFRServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restUFRMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(uFRRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getUFR() throws Exception {
        // Initialize the database
        uFRRepository.saveAndFlush(uFR);

        // Get the uFR
        restUFRMockMvc
            .perform(get(ENTITY_API_URL_ID, uFR.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(uFR.getId().intValue()))
            .andExpect(jsonPath("$.libeleUFR").value(DEFAULT_LIBELE_UFR))
            .andExpect(jsonPath("$.sigleUFR").value(DEFAULT_SIGLE_UFR))
            .andExpect(jsonPath("$.systemeLMDYN").value(DEFAULT_SYSTEME_LMDYN))
            .andExpect(jsonPath("$.ordreStat").value(DEFAULT_ORDRE_STAT));
    }

    @Test
    @Transactional
    void getNonExistingUFR() throws Exception {
        // Get the uFR
        restUFRMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingUFR() throws Exception {
        // Initialize the database
        uFRRepository.saveAndFlush(uFR);

        int databaseSizeBeforeUpdate = uFRRepository.findAll().size();

        // Update the uFR
        UFR updatedUFR = uFRRepository.findById(uFR.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedUFR are not directly saved in db
        em.detach(updatedUFR);
        updatedUFR
            .libeleUFR(UPDATED_LIBELE_UFR)
            .sigleUFR(UPDATED_SIGLE_UFR)
            .systemeLMDYN(UPDATED_SYSTEME_LMDYN)
            .ordreStat(UPDATED_ORDRE_STAT);
        UFRDTO uFRDTO = uFRMapper.toDto(updatedUFR);

        restUFRMockMvc
            .perform(
                put(ENTITY_API_URL_ID, uFRDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(uFRDTO))
            )
            .andExpect(status().isOk());

        // Validate the UFR in the database
        List<UFR> uFRList = uFRRepository.findAll();
        assertThat(uFRList).hasSize(databaseSizeBeforeUpdate);
        UFR testUFR = uFRList.get(uFRList.size() - 1);
        assertThat(testUFR.getLibeleUFR()).isEqualTo(UPDATED_LIBELE_UFR);
        assertThat(testUFR.getSigleUFR()).isEqualTo(UPDATED_SIGLE_UFR);
        assertThat(testUFR.getSystemeLMDYN()).isEqualTo(UPDATED_SYSTEME_LMDYN);
        assertThat(testUFR.getOrdreStat()).isEqualTo(UPDATED_ORDRE_STAT);
    }

    @Test
    @Transactional
    void putNonExistingUFR() throws Exception {
        int databaseSizeBeforeUpdate = uFRRepository.findAll().size();
        uFR.setId(longCount.incrementAndGet());

        // Create the UFR
        UFRDTO uFRDTO = uFRMapper.toDto(uFR);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restUFRMockMvc
            .perform(
                put(ENTITY_API_URL_ID, uFRDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(uFRDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the UFR in the database
        List<UFR> uFRList = uFRRepository.findAll();
        assertThat(uFRList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchUFR() throws Exception {
        int databaseSizeBeforeUpdate = uFRRepository.findAll().size();
        uFR.setId(longCount.incrementAndGet());

        // Create the UFR
        UFRDTO uFRDTO = uFRMapper.toDto(uFR);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUFRMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(uFRDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the UFR in the database
        List<UFR> uFRList = uFRRepository.findAll();
        assertThat(uFRList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamUFR() throws Exception {
        int databaseSizeBeforeUpdate = uFRRepository.findAll().size();
        uFR.setId(longCount.incrementAndGet());

        // Create the UFR
        UFRDTO uFRDTO = uFRMapper.toDto(uFR);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUFRMockMvc
            .perform(
                put(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(uFRDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the UFR in the database
        List<UFR> uFRList = uFRRepository.findAll();
        assertThat(uFRList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateUFRWithPatch() throws Exception {
        // Initialize the database
        uFRRepository.saveAndFlush(uFR);

        int databaseSizeBeforeUpdate = uFRRepository.findAll().size();

        // Update the uFR using partial update
        UFR partialUpdatedUFR = new UFR();
        partialUpdatedUFR.setId(uFR.getId());

        partialUpdatedUFR.systemeLMDYN(UPDATED_SYSTEME_LMDYN).ordreStat(UPDATED_ORDRE_STAT);

        restUFRMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedUFR.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedUFR))
            )
            .andExpect(status().isOk());

        // Validate the UFR in the database
        List<UFR> uFRList = uFRRepository.findAll();
        assertThat(uFRList).hasSize(databaseSizeBeforeUpdate);
        UFR testUFR = uFRList.get(uFRList.size() - 1);
        assertThat(testUFR.getLibeleUFR()).isEqualTo(DEFAULT_LIBELE_UFR);
        assertThat(testUFR.getSigleUFR()).isEqualTo(DEFAULT_SIGLE_UFR);
        assertThat(testUFR.getSystemeLMDYN()).isEqualTo(UPDATED_SYSTEME_LMDYN);
        assertThat(testUFR.getOrdreStat()).isEqualTo(UPDATED_ORDRE_STAT);
    }

    @Test
    @Transactional
    void fullUpdateUFRWithPatch() throws Exception {
        // Initialize the database
        uFRRepository.saveAndFlush(uFR);

        int databaseSizeBeforeUpdate = uFRRepository.findAll().size();

        // Update the uFR using partial update
        UFR partialUpdatedUFR = new UFR();
        partialUpdatedUFR.setId(uFR.getId());

        partialUpdatedUFR
            .libeleUFR(UPDATED_LIBELE_UFR)
            .sigleUFR(UPDATED_SIGLE_UFR)
            .systemeLMDYN(UPDATED_SYSTEME_LMDYN)
            .ordreStat(UPDATED_ORDRE_STAT);

        restUFRMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedUFR.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedUFR))
            )
            .andExpect(status().isOk());

        // Validate the UFR in the database
        List<UFR> uFRList = uFRRepository.findAll();
        assertThat(uFRList).hasSize(databaseSizeBeforeUpdate);
        UFR testUFR = uFRList.get(uFRList.size() - 1);
        assertThat(testUFR.getLibeleUFR()).isEqualTo(UPDATED_LIBELE_UFR);
        assertThat(testUFR.getSigleUFR()).isEqualTo(UPDATED_SIGLE_UFR);
        assertThat(testUFR.getSystemeLMDYN()).isEqualTo(UPDATED_SYSTEME_LMDYN);
        assertThat(testUFR.getOrdreStat()).isEqualTo(UPDATED_ORDRE_STAT);
    }

    @Test
    @Transactional
    void patchNonExistingUFR() throws Exception {
        int databaseSizeBeforeUpdate = uFRRepository.findAll().size();
        uFR.setId(longCount.incrementAndGet());

        // Create the UFR
        UFRDTO uFRDTO = uFRMapper.toDto(uFR);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restUFRMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, uFRDTO.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(uFRDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the UFR in the database
        List<UFR> uFRList = uFRRepository.findAll();
        assertThat(uFRList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchUFR() throws Exception {
        int databaseSizeBeforeUpdate = uFRRepository.findAll().size();
        uFR.setId(longCount.incrementAndGet());

        // Create the UFR
        UFRDTO uFRDTO = uFRMapper.toDto(uFR);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUFRMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(uFRDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the UFR in the database
        List<UFR> uFRList = uFRRepository.findAll();
        assertThat(uFRList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamUFR() throws Exception {
        int databaseSizeBeforeUpdate = uFRRepository.findAll().size();
        uFR.setId(longCount.incrementAndGet());

        // Create the UFR
        UFRDTO uFRDTO = uFRMapper.toDto(uFR);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUFRMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(uFRDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the UFR in the database
        List<UFR> uFRList = uFRRepository.findAll();
        assertThat(uFRList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteUFR() throws Exception {
        // Initialize the database
        uFRRepository.saveAndFlush(uFR);

        int databaseSizeBeforeDelete = uFRRepository.findAll().size();

        // Delete the uFR
        restUFRMockMvc
            .perform(delete(ENTITY_API_URL_ID, uFR.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<UFR> uFRList = uFRRepository.findAll();
        assertThat(uFRList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
