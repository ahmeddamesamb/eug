package sn.ugb.gir.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import jakarta.persistence.EntityManager;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;
import org.apache.commons.collections4.IterableUtils;
import org.assertj.core.util.IterableUtil;
import org.junit.jupiter.api.AfterEach;
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
import sn.ugb.gir.domain.Pays;
import sn.ugb.gir.repository.PaysRepository;
import sn.ugb.gir.repository.search.PaysSearchRepository;
import sn.ugb.gir.service.PaysService;
import sn.ugb.gir.service.dto.PaysDTO;
import sn.ugb.gir.service.mapper.PaysMapper;

/**
 * Integration tests for the {@link PaysResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class PaysResourceIT {

    private static final String DEFAULT_LIBELLE_PAYS = "AAAAAAAAAA";
    private static final String UPDATED_LIBELLE_PAYS = "BBBBBBBBBB";

    private static final String DEFAULT_PAYS_EN_ANGLAIS = "AAAAAAAAAA";
    private static final String UPDATED_PAYS_EN_ANGLAIS = "BBBBBBBBBB";

    private static final String DEFAULT_NATIONALITE = "AAAAAAAAAA";
    private static final String UPDATED_NATIONALITE = "BBBBBBBBBB";

    private static final String DEFAULT_CODE_PAYS = "AAAAAAAAAA";
    private static final String UPDATED_CODE_PAYS = "BBBBBBBBBB";

    private static final Boolean DEFAULT_U_EMOAYN = false;
    private static final Boolean UPDATED_U_EMOAYN = true;

    private static final Boolean DEFAULT_C_EDEAOYN = false;
    private static final Boolean UPDATED_C_EDEAOYN = true;

    private static final Boolean DEFAULT_R_IMYN = false;
    private static final Boolean UPDATED_R_IMYN = true;

    private static final Boolean DEFAULT_AUTRE_YN = false;
    private static final Boolean UPDATED_AUTRE_YN = true;

    private static final Boolean DEFAULT_EST_ETRANGER_YN = false;
    private static final Boolean UPDATED_EST_ETRANGER_YN = true;

    private static final String ENTITY_API_URL = "/api/pays";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/pays/_search";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private PaysRepository paysRepository;

    @Mock
    private PaysRepository paysRepositoryMock;

    @Autowired
    private PaysMapper paysMapper;

    @Mock
    private PaysService paysServiceMock;

    @Autowired
    private PaysSearchRepository paysSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restPaysMockMvc;

    private Pays pays;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Pays createEntity(EntityManager em) {
        Pays pays = new Pays()
            .libellePays(DEFAULT_LIBELLE_PAYS)
            .paysEnAnglais(DEFAULT_PAYS_EN_ANGLAIS)
            .nationalite(DEFAULT_NATIONALITE)
            .codePays(DEFAULT_CODE_PAYS)
            .uEMOAYN(DEFAULT_U_EMOAYN)
            .cEDEAOYN(DEFAULT_C_EDEAOYN)
            .rIMYN(DEFAULT_R_IMYN)
            .autreYN(DEFAULT_AUTRE_YN)
            .estEtrangerYN(DEFAULT_EST_ETRANGER_YN);
        return pays;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Pays createUpdatedEntity(EntityManager em) {
        Pays pays = new Pays()
            .libellePays(UPDATED_LIBELLE_PAYS)
            .paysEnAnglais(UPDATED_PAYS_EN_ANGLAIS)
            .nationalite(UPDATED_NATIONALITE)
            .codePays(UPDATED_CODE_PAYS)
            .uEMOAYN(UPDATED_U_EMOAYN)
            .cEDEAOYN(UPDATED_C_EDEAOYN)
            .rIMYN(UPDATED_R_IMYN)
            .autreYN(UPDATED_AUTRE_YN)
            .estEtrangerYN(UPDATED_EST_ETRANGER_YN);
        return pays;
    }

    @AfterEach
    public void cleanupElasticSearchRepository() {
        paysSearchRepository.deleteAll();
        assertThat(paysSearchRepository.count()).isEqualTo(0);
    }

    @BeforeEach
    public void initTest() {
        pays = createEntity(em);
    }

    @Test
    @Transactional
    void createPays() throws Exception {
        int databaseSizeBeforeCreate = paysRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(paysSearchRepository.findAll());
        // Create the Pays
        PaysDTO paysDTO = paysMapper.toDto(pays);
        restPaysMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(paysDTO))
            )
            .andExpect(status().isCreated());

        // Validate the Pays in the database
        List<Pays> paysList = paysRepository.findAll();
        assertThat(paysList).hasSize(databaseSizeBeforeCreate + 1);
        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(paysSearchRepository.findAll());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore + 1);
            });
        Pays testPays = paysList.get(paysList.size() - 1);
        assertThat(testPays.getLibellePays()).isEqualTo(DEFAULT_LIBELLE_PAYS);
        assertThat(testPays.getPaysEnAnglais()).isEqualTo(DEFAULT_PAYS_EN_ANGLAIS);
        assertThat(testPays.getNationalite()).isEqualTo(DEFAULT_NATIONALITE);
        assertThat(testPays.getCodePays()).isEqualTo(DEFAULT_CODE_PAYS);
        assertThat(testPays.getuEMOAYN()).isEqualTo(DEFAULT_U_EMOAYN);
        assertThat(testPays.getcEDEAOYN()).isEqualTo(DEFAULT_C_EDEAOYN);
        assertThat(testPays.getrIMYN()).isEqualTo(DEFAULT_R_IMYN);
        assertThat(testPays.getAutreYN()).isEqualTo(DEFAULT_AUTRE_YN);
        assertThat(testPays.getEstEtrangerYN()).isEqualTo(DEFAULT_EST_ETRANGER_YN);
    }

    @Test
    @Transactional
    void createPaysWithExistingId() throws Exception {
        // Create the Pays with an existing ID
        pays.setId(1L);
        PaysDTO paysDTO = paysMapper.toDto(pays);

        int databaseSizeBeforeCreate = paysRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(paysSearchRepository.findAll());

        // An entity with an existing ID cannot be created, so this API call must fail
        restPaysMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(paysDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Pays in the database
        List<Pays> paysList = paysRepository.findAll();
        assertThat(paysList).hasSize(databaseSizeBeforeCreate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(paysSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void checkLibellePaysIsRequired() throws Exception {
        int databaseSizeBeforeTest = paysRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(paysSearchRepository.findAll());
        // set the field null
        pays.setLibellePays(null);

        // Create the Pays, which fails.
        PaysDTO paysDTO = paysMapper.toDto(pays);

        restPaysMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(paysDTO))
            )
            .andExpect(status().isBadRequest());

        List<Pays> paysList = paysRepository.findAll();
        assertThat(paysList).hasSize(databaseSizeBeforeTest);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(paysSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void getAllPays() throws Exception {
        // Initialize the database
        paysRepository.saveAndFlush(pays);

        // Get all the paysList
        restPaysMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(pays.getId().intValue())))
            .andExpect(jsonPath("$.[*].libellePays").value(hasItem(DEFAULT_LIBELLE_PAYS)))
            .andExpect(jsonPath("$.[*].paysEnAnglais").value(hasItem(DEFAULT_PAYS_EN_ANGLAIS)))
            .andExpect(jsonPath("$.[*].nationalite").value(hasItem(DEFAULT_NATIONALITE)))
            .andExpect(jsonPath("$.[*].codePays").value(hasItem(DEFAULT_CODE_PAYS)))
            .andExpect(jsonPath("$.[*].uEMOAYN").value(hasItem(DEFAULT_U_EMOAYN.booleanValue())))
            .andExpect(jsonPath("$.[*].cEDEAOYN").value(hasItem(DEFAULT_C_EDEAOYN.booleanValue())))
            .andExpect(jsonPath("$.[*].rIMYN").value(hasItem(DEFAULT_R_IMYN.booleanValue())))
            .andExpect(jsonPath("$.[*].autreYN").value(hasItem(DEFAULT_AUTRE_YN.booleanValue())))
            .andExpect(jsonPath("$.[*].estEtrangerYN").value(hasItem(DEFAULT_EST_ETRANGER_YN.booleanValue())));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllPaysWithEagerRelationshipsIsEnabled() throws Exception {
        when(paysServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restPaysMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(paysServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllPaysWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(paysServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restPaysMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(paysRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getPays() throws Exception {
        // Initialize the database
        paysRepository.saveAndFlush(pays);

        // Get the pays
        restPaysMockMvc
            .perform(get(ENTITY_API_URL_ID, pays.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(pays.getId().intValue()))
            .andExpect(jsonPath("$.libellePays").value(DEFAULT_LIBELLE_PAYS))
            .andExpect(jsonPath("$.paysEnAnglais").value(DEFAULT_PAYS_EN_ANGLAIS))
            .andExpect(jsonPath("$.nationalite").value(DEFAULT_NATIONALITE))
            .andExpect(jsonPath("$.codePays").value(DEFAULT_CODE_PAYS))
            .andExpect(jsonPath("$.uEMOAYN").value(DEFAULT_U_EMOAYN.booleanValue()))
            .andExpect(jsonPath("$.cEDEAOYN").value(DEFAULT_C_EDEAOYN.booleanValue()))
            .andExpect(jsonPath("$.rIMYN").value(DEFAULT_R_IMYN.booleanValue()))
            .andExpect(jsonPath("$.autreYN").value(DEFAULT_AUTRE_YN.booleanValue()))
            .andExpect(jsonPath("$.estEtrangerYN").value(DEFAULT_EST_ETRANGER_YN.booleanValue()));
    }

    @Test
    @Transactional
    void getNonExistingPays() throws Exception {
        // Get the pays
        restPaysMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingPays() throws Exception {
        // Initialize the database
        paysRepository.saveAndFlush(pays);

        int databaseSizeBeforeUpdate = paysRepository.findAll().size();
        paysSearchRepository.save(pays);
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(paysSearchRepository.findAll());

        // Update the pays
        Pays updatedPays = paysRepository.findById(pays.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedPays are not directly saved in db
        em.detach(updatedPays);
        updatedPays
            .libellePays(UPDATED_LIBELLE_PAYS)
            .paysEnAnglais(UPDATED_PAYS_EN_ANGLAIS)
            .nationalite(UPDATED_NATIONALITE)
            .codePays(UPDATED_CODE_PAYS)
            .uEMOAYN(UPDATED_U_EMOAYN)
            .cEDEAOYN(UPDATED_C_EDEAOYN)
            .rIMYN(UPDATED_R_IMYN)
            .autreYN(UPDATED_AUTRE_YN)
            .estEtrangerYN(UPDATED_EST_ETRANGER_YN);
        PaysDTO paysDTO = paysMapper.toDto(updatedPays);

        restPaysMockMvc
            .perform(
                put(ENTITY_API_URL_ID, paysDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(paysDTO))
            )
            .andExpect(status().isOk());

        // Validate the Pays in the database
        List<Pays> paysList = paysRepository.findAll();
        assertThat(paysList).hasSize(databaseSizeBeforeUpdate);
        Pays testPays = paysList.get(paysList.size() - 1);
        assertThat(testPays.getLibellePays()).isEqualTo(UPDATED_LIBELLE_PAYS);
        assertThat(testPays.getPaysEnAnglais()).isEqualTo(UPDATED_PAYS_EN_ANGLAIS);
        assertThat(testPays.getNationalite()).isEqualTo(UPDATED_NATIONALITE);
        assertThat(testPays.getCodePays()).isEqualTo(UPDATED_CODE_PAYS);
        assertThat(testPays.getuEMOAYN()).isEqualTo(UPDATED_U_EMOAYN);
        assertThat(testPays.getcEDEAOYN()).isEqualTo(UPDATED_C_EDEAOYN);
        assertThat(testPays.getrIMYN()).isEqualTo(UPDATED_R_IMYN);
        assertThat(testPays.getAutreYN()).isEqualTo(UPDATED_AUTRE_YN);
        assertThat(testPays.getEstEtrangerYN()).isEqualTo(UPDATED_EST_ETRANGER_YN);
        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(paysSearchRepository.findAll());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
                List<Pays> paysSearchList = IterableUtils.toList(paysSearchRepository.findAll());
                Pays testPaysSearch = paysSearchList.get(searchDatabaseSizeAfter - 1);
                assertThat(testPaysSearch.getLibellePays()).isEqualTo(UPDATED_LIBELLE_PAYS);
                assertThat(testPaysSearch.getPaysEnAnglais()).isEqualTo(UPDATED_PAYS_EN_ANGLAIS);
                assertThat(testPaysSearch.getNationalite()).isEqualTo(UPDATED_NATIONALITE);
                assertThat(testPaysSearch.getCodePays()).isEqualTo(UPDATED_CODE_PAYS);
                assertThat(testPaysSearch.getuEMOAYN()).isEqualTo(UPDATED_U_EMOAYN);
                assertThat(testPaysSearch.getcEDEAOYN()).isEqualTo(UPDATED_C_EDEAOYN);
                assertThat(testPaysSearch.getrIMYN()).isEqualTo(UPDATED_R_IMYN);
                assertThat(testPaysSearch.getAutreYN()).isEqualTo(UPDATED_AUTRE_YN);
                assertThat(testPaysSearch.getEstEtrangerYN()).isEqualTo(UPDATED_EST_ETRANGER_YN);
            });
    }

    @Test
    @Transactional
    void putNonExistingPays() throws Exception {
        int databaseSizeBeforeUpdate = paysRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(paysSearchRepository.findAll());
        pays.setId(longCount.incrementAndGet());

        // Create the Pays
        PaysDTO paysDTO = paysMapper.toDto(pays);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPaysMockMvc
            .perform(
                put(ENTITY_API_URL_ID, paysDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(paysDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Pays in the database
        List<Pays> paysList = paysRepository.findAll();
        assertThat(paysList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(paysSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void putWithIdMismatchPays() throws Exception {
        int databaseSizeBeforeUpdate = paysRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(paysSearchRepository.findAll());
        pays.setId(longCount.incrementAndGet());

        // Create the Pays
        PaysDTO paysDTO = paysMapper.toDto(pays);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPaysMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(paysDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Pays in the database
        List<Pays> paysList = paysRepository.findAll();
        assertThat(paysList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(paysSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamPays() throws Exception {
        int databaseSizeBeforeUpdate = paysRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(paysSearchRepository.findAll());
        pays.setId(longCount.incrementAndGet());

        // Create the Pays
        PaysDTO paysDTO = paysMapper.toDto(pays);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPaysMockMvc
            .perform(
                put(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(paysDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Pays in the database
        List<Pays> paysList = paysRepository.findAll();
        assertThat(paysList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(paysSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void partialUpdatePaysWithPatch() throws Exception {
        // Initialize the database
        paysRepository.saveAndFlush(pays);

        int databaseSizeBeforeUpdate = paysRepository.findAll().size();

        // Update the pays using partial update
        Pays partialUpdatedPays = new Pays();
        partialUpdatedPays.setId(pays.getId());

        partialUpdatedPays
            .libellePays(UPDATED_LIBELLE_PAYS)
            .nationalite(UPDATED_NATIONALITE)
            .codePays(UPDATED_CODE_PAYS)
            .uEMOAYN(UPDATED_U_EMOAYN)
            .cEDEAOYN(UPDATED_C_EDEAOYN);

        restPaysMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPays.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPays))
            )
            .andExpect(status().isOk());

        // Validate the Pays in the database
        List<Pays> paysList = paysRepository.findAll();
        assertThat(paysList).hasSize(databaseSizeBeforeUpdate);
        Pays testPays = paysList.get(paysList.size() - 1);
        assertThat(testPays.getLibellePays()).isEqualTo(UPDATED_LIBELLE_PAYS);
        assertThat(testPays.getPaysEnAnglais()).isEqualTo(DEFAULT_PAYS_EN_ANGLAIS);
        assertThat(testPays.getNationalite()).isEqualTo(UPDATED_NATIONALITE);
        assertThat(testPays.getCodePays()).isEqualTo(UPDATED_CODE_PAYS);
        assertThat(testPays.getuEMOAYN()).isEqualTo(UPDATED_U_EMOAYN);
        assertThat(testPays.getcEDEAOYN()).isEqualTo(UPDATED_C_EDEAOYN);
        assertThat(testPays.getrIMYN()).isEqualTo(DEFAULT_R_IMYN);
        assertThat(testPays.getAutreYN()).isEqualTo(DEFAULT_AUTRE_YN);
        assertThat(testPays.getEstEtrangerYN()).isEqualTo(DEFAULT_EST_ETRANGER_YN);
    }

    @Test
    @Transactional
    void fullUpdatePaysWithPatch() throws Exception {
        // Initialize the database
        paysRepository.saveAndFlush(pays);

        int databaseSizeBeforeUpdate = paysRepository.findAll().size();

        // Update the pays using partial update
        Pays partialUpdatedPays = new Pays();
        partialUpdatedPays.setId(pays.getId());

        partialUpdatedPays
            .libellePays(UPDATED_LIBELLE_PAYS)
            .paysEnAnglais(UPDATED_PAYS_EN_ANGLAIS)
            .nationalite(UPDATED_NATIONALITE)
            .codePays(UPDATED_CODE_PAYS)
            .uEMOAYN(UPDATED_U_EMOAYN)
            .cEDEAOYN(UPDATED_C_EDEAOYN)
            .rIMYN(UPDATED_R_IMYN)
            .autreYN(UPDATED_AUTRE_YN)
            .estEtrangerYN(UPDATED_EST_ETRANGER_YN);

        restPaysMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPays.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPays))
            )
            .andExpect(status().isOk());

        // Validate the Pays in the database
        List<Pays> paysList = paysRepository.findAll();
        assertThat(paysList).hasSize(databaseSizeBeforeUpdate);
        Pays testPays = paysList.get(paysList.size() - 1);
        assertThat(testPays.getLibellePays()).isEqualTo(UPDATED_LIBELLE_PAYS);
        assertThat(testPays.getPaysEnAnglais()).isEqualTo(UPDATED_PAYS_EN_ANGLAIS);
        assertThat(testPays.getNationalite()).isEqualTo(UPDATED_NATIONALITE);
        assertThat(testPays.getCodePays()).isEqualTo(UPDATED_CODE_PAYS);
        assertThat(testPays.getuEMOAYN()).isEqualTo(UPDATED_U_EMOAYN);
        assertThat(testPays.getcEDEAOYN()).isEqualTo(UPDATED_C_EDEAOYN);
        assertThat(testPays.getrIMYN()).isEqualTo(UPDATED_R_IMYN);
        assertThat(testPays.getAutreYN()).isEqualTo(UPDATED_AUTRE_YN);
        assertThat(testPays.getEstEtrangerYN()).isEqualTo(UPDATED_EST_ETRANGER_YN);
    }

    @Test
    @Transactional
    void patchNonExistingPays() throws Exception {
        int databaseSizeBeforeUpdate = paysRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(paysSearchRepository.findAll());
        pays.setId(longCount.incrementAndGet());

        // Create the Pays
        PaysDTO paysDTO = paysMapper.toDto(pays);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPaysMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, paysDTO.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(paysDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Pays in the database
        List<Pays> paysList = paysRepository.findAll();
        assertThat(paysList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(paysSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void patchWithIdMismatchPays() throws Exception {
        int databaseSizeBeforeUpdate = paysRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(paysSearchRepository.findAll());
        pays.setId(longCount.incrementAndGet());

        // Create the Pays
        PaysDTO paysDTO = paysMapper.toDto(pays);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPaysMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(paysDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Pays in the database
        List<Pays> paysList = paysRepository.findAll();
        assertThat(paysList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(paysSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamPays() throws Exception {
        int databaseSizeBeforeUpdate = paysRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(paysSearchRepository.findAll());
        pays.setId(longCount.incrementAndGet());

        // Create the Pays
        PaysDTO paysDTO = paysMapper.toDto(pays);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPaysMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(paysDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Pays in the database
        List<Pays> paysList = paysRepository.findAll();
        assertThat(paysList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(paysSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void deletePays() throws Exception {
        // Initialize the database
        paysRepository.saveAndFlush(pays);
        paysRepository.save(pays);
        paysSearchRepository.save(pays);

        int databaseSizeBeforeDelete = paysRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(paysSearchRepository.findAll());
        assertThat(searchDatabaseSizeBefore).isEqualTo(databaseSizeBeforeDelete);

        // Delete the pays
        restPaysMockMvc
            .perform(delete(ENTITY_API_URL_ID, pays.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Pays> paysList = paysRepository.findAll();
        assertThat(paysList).hasSize(databaseSizeBeforeDelete - 1);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(paysSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore - 1);
    }

    @Test
    @Transactional
    void searchPays() throws Exception {
        // Initialize the database
        pays = paysRepository.saveAndFlush(pays);
        paysSearchRepository.save(pays);

        // Search the pays
        restPaysMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + pays.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(pays.getId().intValue())))
            .andExpect(jsonPath("$.[*].libellePays").value(hasItem(DEFAULT_LIBELLE_PAYS)))
            .andExpect(jsonPath("$.[*].paysEnAnglais").value(hasItem(DEFAULT_PAYS_EN_ANGLAIS)))
            .andExpect(jsonPath("$.[*].nationalite").value(hasItem(DEFAULT_NATIONALITE)))
            .andExpect(jsonPath("$.[*].codePays").value(hasItem(DEFAULT_CODE_PAYS)))
            .andExpect(jsonPath("$.[*].uEMOAYN").value(hasItem(DEFAULT_U_EMOAYN.booleanValue())))
            .andExpect(jsonPath("$.[*].cEDEAOYN").value(hasItem(DEFAULT_C_EDEAOYN.booleanValue())))
            .andExpect(jsonPath("$.[*].rIMYN").value(hasItem(DEFAULT_R_IMYN.booleanValue())))
            .andExpect(jsonPath("$.[*].autreYN").value(hasItem(DEFAULT_AUTRE_YN.booleanValue())))
            .andExpect(jsonPath("$.[*].estEtrangerYN").value(hasItem(DEFAULT_EST_ETRANGER_YN.booleanValue())));
    }
}
