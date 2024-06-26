package sn.ugb.gir.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import jakarta.persistence.EntityManager;
import java.time.LocalDate;
import java.time.ZoneId;
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
import sn.ugb.gir.domain.Frais;
import sn.ugb.gir.repository.FraisRepository;
import sn.ugb.gir.repository.search.FraisSearchRepository;
import sn.ugb.gir.service.FraisService;
import sn.ugb.gir.service.dto.FraisDTO;
import sn.ugb.gir.service.mapper.FraisMapper;

/**
 * Integration tests for the {@link FraisResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class FraisResourceIT {

    private static final Double DEFAULT_VALEUR_FRAIS = 1D;
    private static final Double UPDATED_VALEUR_FRAIS = 2D;

    private static final String DEFAULT_DESCRIPTION_FRAIS = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION_FRAIS = "BBBBBBBBBB";

    private static final Boolean DEFAULT_FRAIS_POUR_ASSIMILE_YN = false;
    private static final Boolean UPDATED_FRAIS_POUR_ASSIMILE_YN = true;

    private static final Boolean DEFAULT_FRAIS_POUR_EXONERER_YN = false;
    private static final Boolean UPDATED_FRAIS_POUR_EXONERER_YN = true;

    private static final Double DEFAULT_DIA = 1D;
    private static final Double UPDATED_DIA = 2D;

    private static final Double DEFAULT_DIP = 1D;
    private static final Double UPDATED_DIP = 2D;

    private static final Double DEFAULT_FRAIS_PRIVEE = 1D;
    private static final Double UPDATED_FRAIS_PRIVEE = 2D;

    private static final LocalDate DEFAULT_DATE_APPLICATION = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE_APPLICATION = LocalDate.now(ZoneId.systemDefault());

    private static final LocalDate DEFAULT_DATE_FIN = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE_FIN = LocalDate.now(ZoneId.systemDefault());

    private static final Boolean DEFAULT_EST_EN_APPLICATION_YN = false;
    private static final Boolean UPDATED_EST_EN_APPLICATION_YN = true;

    private static final Boolean DEFAULT_ACTIF_YN = false;
    private static final Boolean UPDATED_ACTIF_YN = true;

    private static final String ENTITY_API_URL = "/api/frais";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/frais/_search";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private FraisRepository fraisRepository;

    @Mock
    private FraisRepository fraisRepositoryMock;

    @Autowired
    private FraisMapper fraisMapper;

    @Mock
    private FraisService fraisServiceMock;

    @Autowired
    private FraisSearchRepository fraisSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restFraisMockMvc;

    private Frais frais;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Frais createEntity(EntityManager em) {
        Frais frais = new Frais()
            .valeurFrais(DEFAULT_VALEUR_FRAIS)
            .descriptionFrais(DEFAULT_DESCRIPTION_FRAIS)
            .fraisPourAssimileYN(DEFAULT_FRAIS_POUR_ASSIMILE_YN)
            .fraisPourExonererYN(DEFAULT_FRAIS_POUR_EXONERER_YN)
            .dia(DEFAULT_DIA)
            .dip(DEFAULT_DIP)
            .fraisPrivee(DEFAULT_FRAIS_PRIVEE)
            .dateApplication(DEFAULT_DATE_APPLICATION)
            .dateFin(DEFAULT_DATE_FIN)
            .estEnApplicationYN(DEFAULT_EST_EN_APPLICATION_YN)
            .actifYN(DEFAULT_ACTIF_YN);
        return frais;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Frais createUpdatedEntity(EntityManager em) {
        Frais frais = new Frais()
            .valeurFrais(UPDATED_VALEUR_FRAIS)
            .descriptionFrais(UPDATED_DESCRIPTION_FRAIS)
            .fraisPourAssimileYN(UPDATED_FRAIS_POUR_ASSIMILE_YN)
            .fraisPourExonererYN(UPDATED_FRAIS_POUR_EXONERER_YN)
            .dia(UPDATED_DIA)
            .dip(UPDATED_DIP)
            .fraisPrivee(UPDATED_FRAIS_PRIVEE)
            .dateApplication(UPDATED_DATE_APPLICATION)
            .dateFin(UPDATED_DATE_FIN)
            .estEnApplicationYN(UPDATED_EST_EN_APPLICATION_YN)
            .actifYN(UPDATED_ACTIF_YN);
        return frais;
    }

    @AfterEach
    public void cleanupElasticSearchRepository() {
        fraisSearchRepository.deleteAll();
        assertThat(fraisSearchRepository.count()).isEqualTo(0);
    }

    @BeforeEach
    public void initTest() {
        frais = createEntity(em);
    }

    @Test
    @Transactional
    void createFrais() throws Exception {
        int databaseSizeBeforeCreate = fraisRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(fraisSearchRepository.findAll());
        // Create the Frais
        FraisDTO fraisDTO = fraisMapper.toDto(frais);
        restFraisMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(fraisDTO))
            )
            .andExpect(status().isCreated());

        // Validate the Frais in the database
        List<Frais> fraisList = fraisRepository.findAll();
        assertThat(fraisList).hasSize(databaseSizeBeforeCreate + 1);
        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(fraisSearchRepository.findAll());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore + 1);
            });
        Frais testFrais = fraisList.get(fraisList.size() - 1);
        assertThat(testFrais.getValeurFrais()).isEqualTo(DEFAULT_VALEUR_FRAIS);
        assertThat(testFrais.getDescriptionFrais()).isEqualTo(DEFAULT_DESCRIPTION_FRAIS);
        assertThat(testFrais.getFraisPourAssimileYN()).isEqualTo(DEFAULT_FRAIS_POUR_ASSIMILE_YN);
        assertThat(testFrais.getFraisPourExonererYN()).isEqualTo(DEFAULT_FRAIS_POUR_EXONERER_YN);
        assertThat(testFrais.getDia()).isEqualTo(DEFAULT_DIA);
        assertThat(testFrais.getDip()).isEqualTo(DEFAULT_DIP);
        assertThat(testFrais.getFraisPrivee()).isEqualTo(DEFAULT_FRAIS_PRIVEE);
        assertThat(testFrais.getDateApplication()).isEqualTo(DEFAULT_DATE_APPLICATION);
        assertThat(testFrais.getDateFin()).isEqualTo(DEFAULT_DATE_FIN);
        assertThat(testFrais.getEstEnApplicationYN()).isEqualTo(DEFAULT_EST_EN_APPLICATION_YN);
        assertThat(testFrais.getActifYN()).isEqualTo(DEFAULT_ACTIF_YN);
    }

    @Test
    @Transactional
    void createFraisWithExistingId() throws Exception {
        // Create the Frais with an existing ID
        frais.setId(1L);
        FraisDTO fraisDTO = fraisMapper.toDto(frais);

        int databaseSizeBeforeCreate = fraisRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(fraisSearchRepository.findAll());

        // An entity with an existing ID cannot be created, so this API call must fail
        restFraisMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(fraisDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Frais in the database
        List<Frais> fraisList = fraisRepository.findAll();
        assertThat(fraisList).hasSize(databaseSizeBeforeCreate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(fraisSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void checkValeurFraisIsRequired() throws Exception {
        int databaseSizeBeforeTest = fraisRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(fraisSearchRepository.findAll());
        // set the field null
        frais.setValeurFrais(null);

        // Create the Frais, which fails.
        FraisDTO fraisDTO = fraisMapper.toDto(frais);

        restFraisMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(fraisDTO))
            )
            .andExpect(status().isBadRequest());

        List<Frais> fraisList = fraisRepository.findAll();
        assertThat(fraisList).hasSize(databaseSizeBeforeTest);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(fraisSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void checkDateApplicationIsRequired() throws Exception {
        int databaseSizeBeforeTest = fraisRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(fraisSearchRepository.findAll());
        // set the field null
        frais.setDateApplication(null);

        // Create the Frais, which fails.
        FraisDTO fraisDTO = fraisMapper.toDto(frais);

        restFraisMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(fraisDTO))
            )
            .andExpect(status().isBadRequest());

        List<Frais> fraisList = fraisRepository.findAll();
        assertThat(fraisList).hasSize(databaseSizeBeforeTest);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(fraisSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void checkEstEnApplicationYNIsRequired() throws Exception {
        int databaseSizeBeforeTest = fraisRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(fraisSearchRepository.findAll());
        // set the field null
        frais.setEstEnApplicationYN(null);

        // Create the Frais, which fails.
        FraisDTO fraisDTO = fraisMapper.toDto(frais);

        restFraisMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(fraisDTO))
            )
            .andExpect(status().isBadRequest());

        List<Frais> fraisList = fraisRepository.findAll();
        assertThat(fraisList).hasSize(databaseSizeBeforeTest);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(fraisSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void getAllFrais() throws Exception {
        // Initialize the database
        fraisRepository.saveAndFlush(frais);

        // Get all the fraisList
        restFraisMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(frais.getId().intValue())))
            .andExpect(jsonPath("$.[*].valeurFrais").value(hasItem(DEFAULT_VALEUR_FRAIS.doubleValue())))
            .andExpect(jsonPath("$.[*].descriptionFrais").value(hasItem(DEFAULT_DESCRIPTION_FRAIS)))
            .andExpect(jsonPath("$.[*].fraisPourAssimileYN").value(hasItem(DEFAULT_FRAIS_POUR_ASSIMILE_YN.booleanValue())))
            .andExpect(jsonPath("$.[*].fraisPourExonererYN").value(hasItem(DEFAULT_FRAIS_POUR_EXONERER_YN.booleanValue())))
            .andExpect(jsonPath("$.[*].dia").value(hasItem(DEFAULT_DIA.doubleValue())))
            .andExpect(jsonPath("$.[*].dip").value(hasItem(DEFAULT_DIP.doubleValue())))
            .andExpect(jsonPath("$.[*].fraisPrivee").value(hasItem(DEFAULT_FRAIS_PRIVEE.doubleValue())))
            .andExpect(jsonPath("$.[*].dateApplication").value(hasItem(DEFAULT_DATE_APPLICATION.toString())))
            .andExpect(jsonPath("$.[*].dateFin").value(hasItem(DEFAULT_DATE_FIN.toString())))
            .andExpect(jsonPath("$.[*].estEnApplicationYN").value(hasItem(DEFAULT_EST_EN_APPLICATION_YN.booleanValue())))
            .andExpect(jsonPath("$.[*].actifYN").value(hasItem(DEFAULT_ACTIF_YN.booleanValue())));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllFraisWithEagerRelationshipsIsEnabled() throws Exception {
        when(fraisServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restFraisMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(fraisServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllFraisWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(fraisServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restFraisMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(fraisRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getFrais() throws Exception {
        // Initialize the database
        fraisRepository.saveAndFlush(frais);

        // Get the frais
        restFraisMockMvc
            .perform(get(ENTITY_API_URL_ID, frais.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(frais.getId().intValue()))
            .andExpect(jsonPath("$.valeurFrais").value(DEFAULT_VALEUR_FRAIS.doubleValue()))
            .andExpect(jsonPath("$.descriptionFrais").value(DEFAULT_DESCRIPTION_FRAIS))
            .andExpect(jsonPath("$.fraisPourAssimileYN").value(DEFAULT_FRAIS_POUR_ASSIMILE_YN.booleanValue()))
            .andExpect(jsonPath("$.fraisPourExonererYN").value(DEFAULT_FRAIS_POUR_EXONERER_YN.booleanValue()))
            .andExpect(jsonPath("$.dia").value(DEFAULT_DIA.doubleValue()))
            .andExpect(jsonPath("$.dip").value(DEFAULT_DIP.doubleValue()))
            .andExpect(jsonPath("$.fraisPrivee").value(DEFAULT_FRAIS_PRIVEE.doubleValue()))
            .andExpect(jsonPath("$.dateApplication").value(DEFAULT_DATE_APPLICATION.toString()))
            .andExpect(jsonPath("$.dateFin").value(DEFAULT_DATE_FIN.toString()))
            .andExpect(jsonPath("$.estEnApplicationYN").value(DEFAULT_EST_EN_APPLICATION_YN.booleanValue()))
            .andExpect(jsonPath("$.actifYN").value(DEFAULT_ACTIF_YN.booleanValue()));
    }

    @Test
    @Transactional
    void getNonExistingFrais() throws Exception {
        // Get the frais
        restFraisMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingFrais() throws Exception {
        // Initialize the database
        fraisRepository.saveAndFlush(frais);

        int databaseSizeBeforeUpdate = fraisRepository.findAll().size();
        fraisSearchRepository.save(frais);
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(fraisSearchRepository.findAll());

        // Update the frais
        Frais updatedFrais = fraisRepository.findById(frais.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedFrais are not directly saved in db
        em.detach(updatedFrais);
        updatedFrais
            .valeurFrais(UPDATED_VALEUR_FRAIS)
            .descriptionFrais(UPDATED_DESCRIPTION_FRAIS)
            .fraisPourAssimileYN(UPDATED_FRAIS_POUR_ASSIMILE_YN)
            .fraisPourExonererYN(UPDATED_FRAIS_POUR_EXONERER_YN)
            .dia(UPDATED_DIA)
            .dip(UPDATED_DIP)
            .fraisPrivee(UPDATED_FRAIS_PRIVEE)
            .dateApplication(UPDATED_DATE_APPLICATION)
            .dateFin(UPDATED_DATE_FIN)
            .estEnApplicationYN(UPDATED_EST_EN_APPLICATION_YN)
            .actifYN(UPDATED_ACTIF_YN);
        FraisDTO fraisDTO = fraisMapper.toDto(updatedFrais);

        restFraisMockMvc
            .perform(
                put(ENTITY_API_URL_ID, fraisDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(fraisDTO))
            )
            .andExpect(status().isOk());

        // Validate the Frais in the database
        List<Frais> fraisList = fraisRepository.findAll();
        assertThat(fraisList).hasSize(databaseSizeBeforeUpdate);
        Frais testFrais = fraisList.get(fraisList.size() - 1);
        assertThat(testFrais.getValeurFrais()).isEqualTo(UPDATED_VALEUR_FRAIS);
        assertThat(testFrais.getDescriptionFrais()).isEqualTo(UPDATED_DESCRIPTION_FRAIS);
        assertThat(testFrais.getFraisPourAssimileYN()).isEqualTo(UPDATED_FRAIS_POUR_ASSIMILE_YN);
        assertThat(testFrais.getFraisPourExonererYN()).isEqualTo(UPDATED_FRAIS_POUR_EXONERER_YN);
        assertThat(testFrais.getDia()).isEqualTo(UPDATED_DIA);
        assertThat(testFrais.getDip()).isEqualTo(UPDATED_DIP);
        assertThat(testFrais.getFraisPrivee()).isEqualTo(UPDATED_FRAIS_PRIVEE);
        assertThat(testFrais.getDateApplication()).isEqualTo(UPDATED_DATE_APPLICATION);
        assertThat(testFrais.getDateFin()).isEqualTo(UPDATED_DATE_FIN);
        assertThat(testFrais.getEstEnApplicationYN()).isEqualTo(UPDATED_EST_EN_APPLICATION_YN);
        assertThat(testFrais.getActifYN()).isEqualTo(UPDATED_ACTIF_YN);
        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(fraisSearchRepository.findAll());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
                List<Frais> fraisSearchList = IterableUtils.toList(fraisSearchRepository.findAll());
                Frais testFraisSearch = fraisSearchList.get(searchDatabaseSizeAfter - 1);
                assertThat(testFraisSearch.getValeurFrais()).isEqualTo(UPDATED_VALEUR_FRAIS);
                assertThat(testFraisSearch.getDescriptionFrais()).isEqualTo(UPDATED_DESCRIPTION_FRAIS);
                assertThat(testFraisSearch.getFraisPourAssimileYN()).isEqualTo(UPDATED_FRAIS_POUR_ASSIMILE_YN);
                assertThat(testFraisSearch.getFraisPourExonererYN()).isEqualTo(UPDATED_FRAIS_POUR_EXONERER_YN);
                assertThat(testFraisSearch.getDia()).isEqualTo(UPDATED_DIA);
                assertThat(testFraisSearch.getDip()).isEqualTo(UPDATED_DIP);
                assertThat(testFraisSearch.getFraisPrivee()).isEqualTo(UPDATED_FRAIS_PRIVEE);
                assertThat(testFraisSearch.getDateApplication()).isEqualTo(UPDATED_DATE_APPLICATION);
                assertThat(testFraisSearch.getDateFin()).isEqualTo(UPDATED_DATE_FIN);
                assertThat(testFraisSearch.getEstEnApplicationYN()).isEqualTo(UPDATED_EST_EN_APPLICATION_YN);
                assertThat(testFraisSearch.getActifYN()).isEqualTo(UPDATED_ACTIF_YN);
            });
    }

    @Test
    @Transactional
    void putNonExistingFrais() throws Exception {
        int databaseSizeBeforeUpdate = fraisRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(fraisSearchRepository.findAll());
        frais.setId(longCount.incrementAndGet());

        // Create the Frais
        FraisDTO fraisDTO = fraisMapper.toDto(frais);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFraisMockMvc
            .perform(
                put(ENTITY_API_URL_ID, fraisDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(fraisDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Frais in the database
        List<Frais> fraisList = fraisRepository.findAll();
        assertThat(fraisList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(fraisSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void putWithIdMismatchFrais() throws Exception {
        int databaseSizeBeforeUpdate = fraisRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(fraisSearchRepository.findAll());
        frais.setId(longCount.incrementAndGet());

        // Create the Frais
        FraisDTO fraisDTO = fraisMapper.toDto(frais);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFraisMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(fraisDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Frais in the database
        List<Frais> fraisList = fraisRepository.findAll();
        assertThat(fraisList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(fraisSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamFrais() throws Exception {
        int databaseSizeBeforeUpdate = fraisRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(fraisSearchRepository.findAll());
        frais.setId(longCount.incrementAndGet());

        // Create the Frais
        FraisDTO fraisDTO = fraisMapper.toDto(frais);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFraisMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(fraisDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Frais in the database
        List<Frais> fraisList = fraisRepository.findAll();
        assertThat(fraisList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(fraisSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void partialUpdateFraisWithPatch() throws Exception {
        // Initialize the database
        fraisRepository.saveAndFlush(frais);

        int databaseSizeBeforeUpdate = fraisRepository.findAll().size();

        // Update the frais using partial update
        Frais partialUpdatedFrais = new Frais();
        partialUpdatedFrais.setId(frais.getId());

        partialUpdatedFrais
            .fraisPourAssimileYN(UPDATED_FRAIS_POUR_ASSIMILE_YN)
            .dia(UPDATED_DIA)
            .dip(UPDATED_DIP)
            .dateApplication(UPDATED_DATE_APPLICATION)
            .actifYN(UPDATED_ACTIF_YN);

        restFraisMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedFrais.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedFrais))
            )
            .andExpect(status().isOk());

        // Validate the Frais in the database
        List<Frais> fraisList = fraisRepository.findAll();
        assertThat(fraisList).hasSize(databaseSizeBeforeUpdate);
        Frais testFrais = fraisList.get(fraisList.size() - 1);
        assertThat(testFrais.getValeurFrais()).isEqualTo(DEFAULT_VALEUR_FRAIS);
        assertThat(testFrais.getDescriptionFrais()).isEqualTo(DEFAULT_DESCRIPTION_FRAIS);
        assertThat(testFrais.getFraisPourAssimileYN()).isEqualTo(UPDATED_FRAIS_POUR_ASSIMILE_YN);
        assertThat(testFrais.getFraisPourExonererYN()).isEqualTo(DEFAULT_FRAIS_POUR_EXONERER_YN);
        assertThat(testFrais.getDia()).isEqualTo(UPDATED_DIA);
        assertThat(testFrais.getDip()).isEqualTo(UPDATED_DIP);
        assertThat(testFrais.getFraisPrivee()).isEqualTo(DEFAULT_FRAIS_PRIVEE);
        assertThat(testFrais.getDateApplication()).isEqualTo(UPDATED_DATE_APPLICATION);
        assertThat(testFrais.getDateFin()).isEqualTo(DEFAULT_DATE_FIN);
        assertThat(testFrais.getEstEnApplicationYN()).isEqualTo(DEFAULT_EST_EN_APPLICATION_YN);
        assertThat(testFrais.getActifYN()).isEqualTo(UPDATED_ACTIF_YN);
    }

    @Test
    @Transactional
    void fullUpdateFraisWithPatch() throws Exception {
        // Initialize the database
        fraisRepository.saveAndFlush(frais);

        int databaseSizeBeforeUpdate = fraisRepository.findAll().size();

        // Update the frais using partial update
        Frais partialUpdatedFrais = new Frais();
        partialUpdatedFrais.setId(frais.getId());

        partialUpdatedFrais
            .valeurFrais(UPDATED_VALEUR_FRAIS)
            .descriptionFrais(UPDATED_DESCRIPTION_FRAIS)
            .fraisPourAssimileYN(UPDATED_FRAIS_POUR_ASSIMILE_YN)
            .fraisPourExonererYN(UPDATED_FRAIS_POUR_EXONERER_YN)
            .dia(UPDATED_DIA)
            .dip(UPDATED_DIP)
            .fraisPrivee(UPDATED_FRAIS_PRIVEE)
            .dateApplication(UPDATED_DATE_APPLICATION)
            .dateFin(UPDATED_DATE_FIN)
            .estEnApplicationYN(UPDATED_EST_EN_APPLICATION_YN)
            .actifYN(UPDATED_ACTIF_YN);

        restFraisMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedFrais.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedFrais))
            )
            .andExpect(status().isOk());

        // Validate the Frais in the database
        List<Frais> fraisList = fraisRepository.findAll();
        assertThat(fraisList).hasSize(databaseSizeBeforeUpdate);
        Frais testFrais = fraisList.get(fraisList.size() - 1);
        assertThat(testFrais.getValeurFrais()).isEqualTo(UPDATED_VALEUR_FRAIS);
        assertThat(testFrais.getDescriptionFrais()).isEqualTo(UPDATED_DESCRIPTION_FRAIS);
        assertThat(testFrais.getFraisPourAssimileYN()).isEqualTo(UPDATED_FRAIS_POUR_ASSIMILE_YN);
        assertThat(testFrais.getFraisPourExonererYN()).isEqualTo(UPDATED_FRAIS_POUR_EXONERER_YN);
        assertThat(testFrais.getDia()).isEqualTo(UPDATED_DIA);
        assertThat(testFrais.getDip()).isEqualTo(UPDATED_DIP);
        assertThat(testFrais.getFraisPrivee()).isEqualTo(UPDATED_FRAIS_PRIVEE);
        assertThat(testFrais.getDateApplication()).isEqualTo(UPDATED_DATE_APPLICATION);
        assertThat(testFrais.getDateFin()).isEqualTo(UPDATED_DATE_FIN);
        assertThat(testFrais.getEstEnApplicationYN()).isEqualTo(UPDATED_EST_EN_APPLICATION_YN);
        assertThat(testFrais.getActifYN()).isEqualTo(UPDATED_ACTIF_YN);
    }

    @Test
    @Transactional
    void patchNonExistingFrais() throws Exception {
        int databaseSizeBeforeUpdate = fraisRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(fraisSearchRepository.findAll());
        frais.setId(longCount.incrementAndGet());

        // Create the Frais
        FraisDTO fraisDTO = fraisMapper.toDto(frais);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFraisMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, fraisDTO.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(fraisDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Frais in the database
        List<Frais> fraisList = fraisRepository.findAll();
        assertThat(fraisList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(fraisSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void patchWithIdMismatchFrais() throws Exception {
        int databaseSizeBeforeUpdate = fraisRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(fraisSearchRepository.findAll());
        frais.setId(longCount.incrementAndGet());

        // Create the Frais
        FraisDTO fraisDTO = fraisMapper.toDto(frais);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFraisMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(fraisDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Frais in the database
        List<Frais> fraisList = fraisRepository.findAll();
        assertThat(fraisList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(fraisSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamFrais() throws Exception {
        int databaseSizeBeforeUpdate = fraisRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(fraisSearchRepository.findAll());
        frais.setId(longCount.incrementAndGet());

        // Create the Frais
        FraisDTO fraisDTO = fraisMapper.toDto(frais);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFraisMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(fraisDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Frais in the database
        List<Frais> fraisList = fraisRepository.findAll();
        assertThat(fraisList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(fraisSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void deleteFrais() throws Exception {
        // Initialize the database
        fraisRepository.saveAndFlush(frais);
        fraisRepository.save(frais);
        fraisSearchRepository.save(frais);

        int databaseSizeBeforeDelete = fraisRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(fraisSearchRepository.findAll());
        assertThat(searchDatabaseSizeBefore).isEqualTo(databaseSizeBeforeDelete);

        // Delete the frais
        restFraisMockMvc
            .perform(delete(ENTITY_API_URL_ID, frais.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Frais> fraisList = fraisRepository.findAll();
        assertThat(fraisList).hasSize(databaseSizeBeforeDelete - 1);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(fraisSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore - 1);
    }

    @Test
    @Transactional
    void searchFrais() throws Exception {
        // Initialize the database
        frais = fraisRepository.saveAndFlush(frais);
        fraisSearchRepository.save(frais);

        // Search the frais
        restFraisMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + frais.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(frais.getId().intValue())))
            .andExpect(jsonPath("$.[*].valeurFrais").value(hasItem(DEFAULT_VALEUR_FRAIS.doubleValue())))
            .andExpect(jsonPath("$.[*].descriptionFrais").value(hasItem(DEFAULT_DESCRIPTION_FRAIS)))
            .andExpect(jsonPath("$.[*].fraisPourAssimileYN").value(hasItem(DEFAULT_FRAIS_POUR_ASSIMILE_YN.booleanValue())))
            .andExpect(jsonPath("$.[*].fraisPourExonererYN").value(hasItem(DEFAULT_FRAIS_POUR_EXONERER_YN.booleanValue())))
            .andExpect(jsonPath("$.[*].dia").value(hasItem(DEFAULT_DIA.doubleValue())))
            .andExpect(jsonPath("$.[*].dip").value(hasItem(DEFAULT_DIP.doubleValue())))
            .andExpect(jsonPath("$.[*].fraisPrivee").value(hasItem(DEFAULT_FRAIS_PRIVEE.doubleValue())))
            .andExpect(jsonPath("$.[*].dateApplication").value(hasItem(DEFAULT_DATE_APPLICATION.toString())))
            .andExpect(jsonPath("$.[*].dateFin").value(hasItem(DEFAULT_DATE_FIN.toString())))
            .andExpect(jsonPath("$.[*].estEnApplicationYN").value(hasItem(DEFAULT_EST_EN_APPLICATION_YN.booleanValue())))
            .andExpect(jsonPath("$.[*].actifYN").value(hasItem(DEFAULT_ACTIF_YN.booleanValue())));
    }
}
