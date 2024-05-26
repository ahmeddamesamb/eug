package sn.ugb.gir.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import jakarta.persistence.EntityManager;
import java.time.LocalDate;
import java.time.ZoneId;
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
import sn.ugb.gir.domain.InformationPersonnelle;
import sn.ugb.gir.repository.InformationPersonnelleRepository;
import sn.ugb.gir.service.dto.InformationPersonnelleDTO;
import sn.ugb.gir.service.mapper.InformationPersonnelleMapper;

/**
 * Integration tests for the {@link InformationPersonnelleResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class InformationPersonnelleResourceIT {

    private static final String DEFAULT_NOM_ETU = "AAAAAAAAAA";
    private static final String UPDATED_NOM_ETU = "BBBBBBBBBB";

    private static final String DEFAULT_NOM_JEUNE_FILLE_ETU = "AAAAAAAAAA";
    private static final String UPDATED_NOM_JEUNE_FILLE_ETU = "BBBBBBBBBB";

    private static final String DEFAULT_PRENOM_ETU = "AAAAAAAAAA";
    private static final String UPDATED_PRENOM_ETU = "BBBBBBBBBB";

    private static final String DEFAULT_STATUT_MARITAL = "AAAAAAAAAA";
    private static final String UPDATED_STATUT_MARITAL = "BBBBBBBBBB";

    private static final Integer DEFAULT_REGIME = 1;
    private static final Integer UPDATED_REGIME = 2;

    private static final String DEFAULT_PROFESSION = "AAAAAAAAAA";
    private static final String UPDATED_PROFESSION = "BBBBBBBBBB";

    private static final String DEFAULT_ADRESSE_ETU = "AAAAAAAAAA";
    private static final String UPDATED_ADRESSE_ETU = "BBBBBBBBBB";

    private static final String DEFAULT_TEL_ETU = "AAAAAAAAAA";
    private static final String UPDATED_TEL_ETU = "BBBBBBBBBB";

    private static final String DEFAULT_EMAIL_ETU = "AAAAAAAAAA";
    private static final String UPDATED_EMAIL_ETU = "BBBBBBBBBB";

    private static final String DEFAULT_ADRESSE_PARENT = "AAAAAAAAAA";
    private static final String UPDATED_ADRESSE_PARENT = "BBBBBBBBBB";

    private static final String DEFAULT_TEL_PARENT = "AAAAAAAAAA";
    private static final String UPDATED_TEL_PARENT = "BBBBBBBBBB";

    private static final String DEFAULT_EMAIL_PARENT = "AAAAAAAAAA";
    private static final String UPDATED_EMAIL_PARENT = "BBBBBBBBBB";

    private static final String DEFAULT_NOM_PARENT = "AAAAAAAAAA";
    private static final String UPDATED_NOM_PARENT = "BBBBBBBBBB";

    private static final String DEFAULT_PRENOM_PARENT = "AAAAAAAAAA";
    private static final String UPDATED_PRENOM_PARENT = "BBBBBBBBBB";

    private static final Integer DEFAULT_HANDICAP_YN = 1;
    private static final Integer UPDATED_HANDICAP_YN = 2;

    private static final String DEFAULT_PHOTO = "AAAAAAAAAA";
    private static final String UPDATED_PHOTO = "BBBBBBBBBB";

    private static final Integer DEFAULT_ORDI_PERSO_YN = 1;
    private static final Integer UPDATED_ORDI_PERSO_YN = 2;

    private static final LocalDate DEFAULT_DERNIERE_MODIFICATION = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DERNIERE_MODIFICATION = LocalDate.now(ZoneId.systemDefault());

    private static final String DEFAULT_EMAIL_USER = "AAAAAAAAAA";
    private static final String UPDATED_EMAIL_USER = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/information-personnelles";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private InformationPersonnelleRepository informationPersonnelleRepository;

    @Autowired
    private InformationPersonnelleMapper informationPersonnelleMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restInformationPersonnelleMockMvc;

    private InformationPersonnelle informationPersonnelle;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static InformationPersonnelle createEntity(EntityManager em) {
        InformationPersonnelle informationPersonnelle = new InformationPersonnelle()
            .nomEtu(DEFAULT_NOM_ETU)
            .nomJeuneFilleEtu(DEFAULT_NOM_JEUNE_FILLE_ETU)
            .prenomEtu(DEFAULT_PRENOM_ETU)
            .statutMarital(DEFAULT_STATUT_MARITAL)
            .regime(DEFAULT_REGIME)
            .profession(DEFAULT_PROFESSION)
            .adresseEtu(DEFAULT_ADRESSE_ETU)
            .telEtu(DEFAULT_TEL_ETU)
            .emailEtu(DEFAULT_EMAIL_ETU)
            .adresseParent(DEFAULT_ADRESSE_PARENT)
            .telParent(DEFAULT_TEL_PARENT)
            .emailParent(DEFAULT_EMAIL_PARENT)
            .nomParent(DEFAULT_NOM_PARENT)
            .prenomParent(DEFAULT_PRENOM_PARENT)
            .handicapYN(DEFAULT_HANDICAP_YN)
            .photo(DEFAULT_PHOTO)
            .ordiPersoYN(DEFAULT_ORDI_PERSO_YN)
            .derniereModification(DEFAULT_DERNIERE_MODIFICATION)
            .emailUser(DEFAULT_EMAIL_USER);
        return informationPersonnelle;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static InformationPersonnelle createUpdatedEntity(EntityManager em) {
        InformationPersonnelle informationPersonnelle = new InformationPersonnelle()
            .nomEtu(UPDATED_NOM_ETU)
            .nomJeuneFilleEtu(UPDATED_NOM_JEUNE_FILLE_ETU)
            .prenomEtu(UPDATED_PRENOM_ETU)
            .statutMarital(UPDATED_STATUT_MARITAL)
            .regime(UPDATED_REGIME)
            .profession(UPDATED_PROFESSION)
            .adresseEtu(UPDATED_ADRESSE_ETU)
            .telEtu(UPDATED_TEL_ETU)
            .emailEtu(UPDATED_EMAIL_ETU)
            .adresseParent(UPDATED_ADRESSE_PARENT)
            .telParent(UPDATED_TEL_PARENT)
            .emailParent(UPDATED_EMAIL_PARENT)
            .nomParent(UPDATED_NOM_PARENT)
            .prenomParent(UPDATED_PRENOM_PARENT)
            .handicapYN(UPDATED_HANDICAP_YN)
            .photo(UPDATED_PHOTO)
            .ordiPersoYN(UPDATED_ORDI_PERSO_YN)
            .derniereModification(UPDATED_DERNIERE_MODIFICATION)
            .emailUser(UPDATED_EMAIL_USER);
        return informationPersonnelle;
    }

    @BeforeEach
    public void initTest() {
        informationPersonnelle = createEntity(em);
    }

    @Test
    @Transactional
    void createInformationPersonnelle() throws Exception {
        int databaseSizeBeforeCreate = informationPersonnelleRepository.findAll().size();
        // Create the InformationPersonnelle
        InformationPersonnelleDTO informationPersonnelleDTO = informationPersonnelleMapper.toDto(informationPersonnelle);
        restInformationPersonnelleMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(informationPersonnelleDTO))
            )
            .andExpect(status().isCreated());

        // Validate the InformationPersonnelle in the database
        List<InformationPersonnelle> informationPersonnelleList = informationPersonnelleRepository.findAll();
        assertThat(informationPersonnelleList).hasSize(databaseSizeBeforeCreate + 1);
        InformationPersonnelle testInformationPersonnelle = informationPersonnelleList.get(informationPersonnelleList.size() - 1);
        assertThat(testInformationPersonnelle.getNomEtu()).isEqualTo(DEFAULT_NOM_ETU);
        assertThat(testInformationPersonnelle.getNomJeuneFilleEtu()).isEqualTo(DEFAULT_NOM_JEUNE_FILLE_ETU);
        assertThat(testInformationPersonnelle.getPrenomEtu()).isEqualTo(DEFAULT_PRENOM_ETU);
        assertThat(testInformationPersonnelle.getStatutMarital()).isEqualTo(DEFAULT_STATUT_MARITAL);
        assertThat(testInformationPersonnelle.getRegime()).isEqualTo(DEFAULT_REGIME);
        assertThat(testInformationPersonnelle.getProfession()).isEqualTo(DEFAULT_PROFESSION);
        assertThat(testInformationPersonnelle.getAdresseEtu()).isEqualTo(DEFAULT_ADRESSE_ETU);
        assertThat(testInformationPersonnelle.getTelEtu()).isEqualTo(DEFAULT_TEL_ETU);
        assertThat(testInformationPersonnelle.getEmailEtu()).isEqualTo(DEFAULT_EMAIL_ETU);
        assertThat(testInformationPersonnelle.getAdresseParent()).isEqualTo(DEFAULT_ADRESSE_PARENT);
        assertThat(testInformationPersonnelle.getTelParent()).isEqualTo(DEFAULT_TEL_PARENT);
        assertThat(testInformationPersonnelle.getEmailParent()).isEqualTo(DEFAULT_EMAIL_PARENT);
        assertThat(testInformationPersonnelle.getNomParent()).isEqualTo(DEFAULT_NOM_PARENT);
        assertThat(testInformationPersonnelle.getPrenomParent()).isEqualTo(DEFAULT_PRENOM_PARENT);
        assertThat(testInformationPersonnelle.getHandicapYN()).isEqualTo(DEFAULT_HANDICAP_YN);
        assertThat(testInformationPersonnelle.getPhoto()).isEqualTo(DEFAULT_PHOTO);
        assertThat(testInformationPersonnelle.getOrdiPersoYN()).isEqualTo(DEFAULT_ORDI_PERSO_YN);
        assertThat(testInformationPersonnelle.getDerniereModification()).isEqualTo(DEFAULT_DERNIERE_MODIFICATION);
        assertThat(testInformationPersonnelle.getEmailUser()).isEqualTo(DEFAULT_EMAIL_USER);
    }

    @Test
    @Transactional
    void createInformationPersonnelleWithExistingId() throws Exception {
        // Create the InformationPersonnelle with an existing ID
        informationPersonnelle.setId(1L);
        InformationPersonnelleDTO informationPersonnelleDTO = informationPersonnelleMapper.toDto(informationPersonnelle);

        int databaseSizeBeforeCreate = informationPersonnelleRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restInformationPersonnelleMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(informationPersonnelleDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the InformationPersonnelle in the database
        List<InformationPersonnelle> informationPersonnelleList = informationPersonnelleRepository.findAll();
        assertThat(informationPersonnelleList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNomEtuIsRequired() throws Exception {
        int databaseSizeBeforeTest = informationPersonnelleRepository.findAll().size();
        // set the field null
        informationPersonnelle.setNomEtu(null);

        // Create the InformationPersonnelle, which fails.
        InformationPersonnelleDTO informationPersonnelleDTO = informationPersonnelleMapper.toDto(informationPersonnelle);

        restInformationPersonnelleMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(informationPersonnelleDTO))
            )
            .andExpect(status().isBadRequest());

        List<InformationPersonnelle> informationPersonnelleList = informationPersonnelleRepository.findAll();
        assertThat(informationPersonnelleList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkPrenomEtuIsRequired() throws Exception {
        int databaseSizeBeforeTest = informationPersonnelleRepository.findAll().size();
        // set the field null
        informationPersonnelle.setPrenomEtu(null);

        // Create the InformationPersonnelle, which fails.
        InformationPersonnelleDTO informationPersonnelleDTO = informationPersonnelleMapper.toDto(informationPersonnelle);

        restInformationPersonnelleMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(informationPersonnelleDTO))
            )
            .andExpect(status().isBadRequest());

        List<InformationPersonnelle> informationPersonnelleList = informationPersonnelleRepository.findAll();
        assertThat(informationPersonnelleList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkStatutMaritalIsRequired() throws Exception {
        int databaseSizeBeforeTest = informationPersonnelleRepository.findAll().size();
        // set the field null
        informationPersonnelle.setStatutMarital(null);

        // Create the InformationPersonnelle, which fails.
        InformationPersonnelleDTO informationPersonnelleDTO = informationPersonnelleMapper.toDto(informationPersonnelle);

        restInformationPersonnelleMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(informationPersonnelleDTO))
            )
            .andExpect(status().isBadRequest());

        List<InformationPersonnelle> informationPersonnelleList = informationPersonnelleRepository.findAll();
        assertThat(informationPersonnelleList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkAdresseEtuIsRequired() throws Exception {
        int databaseSizeBeforeTest = informationPersonnelleRepository.findAll().size();
        // set the field null
        informationPersonnelle.setAdresseEtu(null);

        // Create the InformationPersonnelle, which fails.
        InformationPersonnelleDTO informationPersonnelleDTO = informationPersonnelleMapper.toDto(informationPersonnelle);

        restInformationPersonnelleMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(informationPersonnelleDTO))
            )
            .andExpect(status().isBadRequest());

        List<InformationPersonnelle> informationPersonnelleList = informationPersonnelleRepository.findAll();
        assertThat(informationPersonnelleList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllInformationPersonnelles() throws Exception {
        // Initialize the database
        informationPersonnelleRepository.saveAndFlush(informationPersonnelle);

        // Get all the informationPersonnelleList
        restInformationPersonnelleMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(informationPersonnelle.getId().intValue())))
            .andExpect(jsonPath("$.[*].nomEtu").value(hasItem(DEFAULT_NOM_ETU)))
            .andExpect(jsonPath("$.[*].nomJeuneFilleEtu").value(hasItem(DEFAULT_NOM_JEUNE_FILLE_ETU)))
            .andExpect(jsonPath("$.[*].prenomEtu").value(hasItem(DEFAULT_PRENOM_ETU)))
            .andExpect(jsonPath("$.[*].statutMarital").value(hasItem(DEFAULT_STATUT_MARITAL)))
            .andExpect(jsonPath("$.[*].regime").value(hasItem(DEFAULT_REGIME)))
            .andExpect(jsonPath("$.[*].profession").value(hasItem(DEFAULT_PROFESSION)))
            .andExpect(jsonPath("$.[*].adresseEtu").value(hasItem(DEFAULT_ADRESSE_ETU)))
            .andExpect(jsonPath("$.[*].telEtu").value(hasItem(DEFAULT_TEL_ETU)))
            .andExpect(jsonPath("$.[*].emailEtu").value(hasItem(DEFAULT_EMAIL_ETU)))
            .andExpect(jsonPath("$.[*].adresseParent").value(hasItem(DEFAULT_ADRESSE_PARENT)))
            .andExpect(jsonPath("$.[*].telParent").value(hasItem(DEFAULT_TEL_PARENT)))
            .andExpect(jsonPath("$.[*].emailParent").value(hasItem(DEFAULT_EMAIL_PARENT)))
            .andExpect(jsonPath("$.[*].nomParent").value(hasItem(DEFAULT_NOM_PARENT)))
            .andExpect(jsonPath("$.[*].prenomParent").value(hasItem(DEFAULT_PRENOM_PARENT)))
            .andExpect(jsonPath("$.[*].handicapYN").value(hasItem(DEFAULT_HANDICAP_YN)))
            .andExpect(jsonPath("$.[*].photo").value(hasItem(DEFAULT_PHOTO)))
            .andExpect(jsonPath("$.[*].ordiPersoYN").value(hasItem(DEFAULT_ORDI_PERSO_YN)))
            .andExpect(jsonPath("$.[*].derniereModification").value(hasItem(DEFAULT_DERNIERE_MODIFICATION.toString())))
            .andExpect(jsonPath("$.[*].emailUser").value(hasItem(DEFAULT_EMAIL_USER)));
    }

    @Test
    @Transactional
    void getInformationPersonnelle() throws Exception {
        // Initialize the database
        informationPersonnelleRepository.saveAndFlush(informationPersonnelle);

        // Get the informationPersonnelle
        restInformationPersonnelleMockMvc
            .perform(get(ENTITY_API_URL_ID, informationPersonnelle.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(informationPersonnelle.getId().intValue()))
            .andExpect(jsonPath("$.nomEtu").value(DEFAULT_NOM_ETU))
            .andExpect(jsonPath("$.nomJeuneFilleEtu").value(DEFAULT_NOM_JEUNE_FILLE_ETU))
            .andExpect(jsonPath("$.prenomEtu").value(DEFAULT_PRENOM_ETU))
            .andExpect(jsonPath("$.statutMarital").value(DEFAULT_STATUT_MARITAL))
            .andExpect(jsonPath("$.regime").value(DEFAULT_REGIME))
            .andExpect(jsonPath("$.profession").value(DEFAULT_PROFESSION))
            .andExpect(jsonPath("$.adresseEtu").value(DEFAULT_ADRESSE_ETU))
            .andExpect(jsonPath("$.telEtu").value(DEFAULT_TEL_ETU))
            .andExpect(jsonPath("$.emailEtu").value(DEFAULT_EMAIL_ETU))
            .andExpect(jsonPath("$.adresseParent").value(DEFAULT_ADRESSE_PARENT))
            .andExpect(jsonPath("$.telParent").value(DEFAULT_TEL_PARENT))
            .andExpect(jsonPath("$.emailParent").value(DEFAULT_EMAIL_PARENT))
            .andExpect(jsonPath("$.nomParent").value(DEFAULT_NOM_PARENT))
            .andExpect(jsonPath("$.prenomParent").value(DEFAULT_PRENOM_PARENT))
            .andExpect(jsonPath("$.handicapYN").value(DEFAULT_HANDICAP_YN))
            .andExpect(jsonPath("$.photo").value(DEFAULT_PHOTO))
            .andExpect(jsonPath("$.ordiPersoYN").value(DEFAULT_ORDI_PERSO_YN))
            .andExpect(jsonPath("$.derniereModification").value(DEFAULT_DERNIERE_MODIFICATION.toString()))
            .andExpect(jsonPath("$.emailUser").value(DEFAULT_EMAIL_USER));
    }

    @Test
    @Transactional
    void getNonExistingInformationPersonnelle() throws Exception {
        // Get the informationPersonnelle
        restInformationPersonnelleMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingInformationPersonnelle() throws Exception {
        // Initialize the database
        informationPersonnelleRepository.saveAndFlush(informationPersonnelle);

        int databaseSizeBeforeUpdate = informationPersonnelleRepository.findAll().size();

        // Update the informationPersonnelle
        InformationPersonnelle updatedInformationPersonnelle = informationPersonnelleRepository
            .findById(informationPersonnelle.getId())
            .orElseThrow();
        // Disconnect from session so that the updates on updatedInformationPersonnelle are not directly saved in db
        em.detach(updatedInformationPersonnelle);
        updatedInformationPersonnelle
            .nomEtu(UPDATED_NOM_ETU)
            .nomJeuneFilleEtu(UPDATED_NOM_JEUNE_FILLE_ETU)
            .prenomEtu(UPDATED_PRENOM_ETU)
            .statutMarital(UPDATED_STATUT_MARITAL)
            .regime(UPDATED_REGIME)
            .profession(UPDATED_PROFESSION)
            .adresseEtu(UPDATED_ADRESSE_ETU)
            .telEtu(UPDATED_TEL_ETU)
            .emailEtu(UPDATED_EMAIL_ETU)
            .adresseParent(UPDATED_ADRESSE_PARENT)
            .telParent(UPDATED_TEL_PARENT)
            .emailParent(UPDATED_EMAIL_PARENT)
            .nomParent(UPDATED_NOM_PARENT)
            .prenomParent(UPDATED_PRENOM_PARENT)
            .handicapYN(UPDATED_HANDICAP_YN)
            .photo(UPDATED_PHOTO)
            .ordiPersoYN(UPDATED_ORDI_PERSO_YN)
            .derniereModification(UPDATED_DERNIERE_MODIFICATION)
            .emailUser(UPDATED_EMAIL_USER);
        InformationPersonnelleDTO informationPersonnelleDTO = informationPersonnelleMapper.toDto(updatedInformationPersonnelle);

        restInformationPersonnelleMockMvc
            .perform(
                put(ENTITY_API_URL_ID, informationPersonnelleDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(informationPersonnelleDTO))
            )
            .andExpect(status().isOk());

        // Validate the InformationPersonnelle in the database
        List<InformationPersonnelle> informationPersonnelleList = informationPersonnelleRepository.findAll();
        assertThat(informationPersonnelleList).hasSize(databaseSizeBeforeUpdate);
        InformationPersonnelle testInformationPersonnelle = informationPersonnelleList.get(informationPersonnelleList.size() - 1);
        assertThat(testInformationPersonnelle.getNomEtu()).isEqualTo(UPDATED_NOM_ETU);
        assertThat(testInformationPersonnelle.getNomJeuneFilleEtu()).isEqualTo(UPDATED_NOM_JEUNE_FILLE_ETU);
        assertThat(testInformationPersonnelle.getPrenomEtu()).isEqualTo(UPDATED_PRENOM_ETU);
        assertThat(testInformationPersonnelle.getStatutMarital()).isEqualTo(UPDATED_STATUT_MARITAL);
        assertThat(testInformationPersonnelle.getRegime()).isEqualTo(UPDATED_REGIME);
        assertThat(testInformationPersonnelle.getProfession()).isEqualTo(UPDATED_PROFESSION);
        assertThat(testInformationPersonnelle.getAdresseEtu()).isEqualTo(UPDATED_ADRESSE_ETU);
        assertThat(testInformationPersonnelle.getTelEtu()).isEqualTo(UPDATED_TEL_ETU);
        assertThat(testInformationPersonnelle.getEmailEtu()).isEqualTo(UPDATED_EMAIL_ETU);
        assertThat(testInformationPersonnelle.getAdresseParent()).isEqualTo(UPDATED_ADRESSE_PARENT);
        assertThat(testInformationPersonnelle.getTelParent()).isEqualTo(UPDATED_TEL_PARENT);
        assertThat(testInformationPersonnelle.getEmailParent()).isEqualTo(UPDATED_EMAIL_PARENT);
        assertThat(testInformationPersonnelle.getNomParent()).isEqualTo(UPDATED_NOM_PARENT);
        assertThat(testInformationPersonnelle.getPrenomParent()).isEqualTo(UPDATED_PRENOM_PARENT);
        assertThat(testInformationPersonnelle.getHandicapYN()).isEqualTo(UPDATED_HANDICAP_YN);
        assertThat(testInformationPersonnelle.getPhoto()).isEqualTo(UPDATED_PHOTO);
        assertThat(testInformationPersonnelle.getOrdiPersoYN()).isEqualTo(UPDATED_ORDI_PERSO_YN);
        assertThat(testInformationPersonnelle.getDerniereModification()).isEqualTo(UPDATED_DERNIERE_MODIFICATION);
        assertThat(testInformationPersonnelle.getEmailUser()).isEqualTo(UPDATED_EMAIL_USER);
    }

    @Test
    @Transactional
    void putNonExistingInformationPersonnelle() throws Exception {
        int databaseSizeBeforeUpdate = informationPersonnelleRepository.findAll().size();
        informationPersonnelle.setId(longCount.incrementAndGet());

        // Create the InformationPersonnelle
        InformationPersonnelleDTO informationPersonnelleDTO = informationPersonnelleMapper.toDto(informationPersonnelle);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restInformationPersonnelleMockMvc
            .perform(
                put(ENTITY_API_URL_ID, informationPersonnelleDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(informationPersonnelleDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the InformationPersonnelle in the database
        List<InformationPersonnelle> informationPersonnelleList = informationPersonnelleRepository.findAll();
        assertThat(informationPersonnelleList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchInformationPersonnelle() throws Exception {
        int databaseSizeBeforeUpdate = informationPersonnelleRepository.findAll().size();
        informationPersonnelle.setId(longCount.incrementAndGet());

        // Create the InformationPersonnelle
        InformationPersonnelleDTO informationPersonnelleDTO = informationPersonnelleMapper.toDto(informationPersonnelle);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restInformationPersonnelleMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(informationPersonnelleDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the InformationPersonnelle in the database
        List<InformationPersonnelle> informationPersonnelleList = informationPersonnelleRepository.findAll();
        assertThat(informationPersonnelleList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamInformationPersonnelle() throws Exception {
        int databaseSizeBeforeUpdate = informationPersonnelleRepository.findAll().size();
        informationPersonnelle.setId(longCount.incrementAndGet());

        // Create the InformationPersonnelle
        InformationPersonnelleDTO informationPersonnelleDTO = informationPersonnelleMapper.toDto(informationPersonnelle);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restInformationPersonnelleMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(informationPersonnelleDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the InformationPersonnelle in the database
        List<InformationPersonnelle> informationPersonnelleList = informationPersonnelleRepository.findAll();
        assertThat(informationPersonnelleList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateInformationPersonnelleWithPatch() throws Exception {
        // Initialize the database
        informationPersonnelleRepository.saveAndFlush(informationPersonnelle);

        int databaseSizeBeforeUpdate = informationPersonnelleRepository.findAll().size();

        // Update the informationPersonnelle using partial update
        InformationPersonnelle partialUpdatedInformationPersonnelle = new InformationPersonnelle();
        partialUpdatedInformationPersonnelle.setId(informationPersonnelle.getId());

        partialUpdatedInformationPersonnelle
            .nomJeuneFilleEtu(UPDATED_NOM_JEUNE_FILLE_ETU)
            .statutMarital(UPDATED_STATUT_MARITAL)
            .telEtu(UPDATED_TEL_ETU)
            .emailEtu(UPDATED_EMAIL_ETU)
            .adresseParent(UPDATED_ADRESSE_PARENT)
            .telParent(UPDATED_TEL_PARENT)
            .emailParent(UPDATED_EMAIL_PARENT)
            .prenomParent(UPDATED_PRENOM_PARENT)
            .handicapYN(UPDATED_HANDICAP_YN)
            .photo(UPDATED_PHOTO)
            .ordiPersoYN(UPDATED_ORDI_PERSO_YN)
            .derniereModification(UPDATED_DERNIERE_MODIFICATION)
            .emailUser(UPDATED_EMAIL_USER);

        restInformationPersonnelleMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedInformationPersonnelle.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedInformationPersonnelle))
            )
            .andExpect(status().isOk());

        // Validate the InformationPersonnelle in the database
        List<InformationPersonnelle> informationPersonnelleList = informationPersonnelleRepository.findAll();
        assertThat(informationPersonnelleList).hasSize(databaseSizeBeforeUpdate);
        InformationPersonnelle testInformationPersonnelle = informationPersonnelleList.get(informationPersonnelleList.size() - 1);
        assertThat(testInformationPersonnelle.getNomEtu()).isEqualTo(DEFAULT_NOM_ETU);
        assertThat(testInformationPersonnelle.getNomJeuneFilleEtu()).isEqualTo(UPDATED_NOM_JEUNE_FILLE_ETU);
        assertThat(testInformationPersonnelle.getPrenomEtu()).isEqualTo(DEFAULT_PRENOM_ETU);
        assertThat(testInformationPersonnelle.getStatutMarital()).isEqualTo(UPDATED_STATUT_MARITAL);
        assertThat(testInformationPersonnelle.getRegime()).isEqualTo(DEFAULT_REGIME);
        assertThat(testInformationPersonnelle.getProfession()).isEqualTo(DEFAULT_PROFESSION);
        assertThat(testInformationPersonnelle.getAdresseEtu()).isEqualTo(DEFAULT_ADRESSE_ETU);
        assertThat(testInformationPersonnelle.getTelEtu()).isEqualTo(UPDATED_TEL_ETU);
        assertThat(testInformationPersonnelle.getEmailEtu()).isEqualTo(UPDATED_EMAIL_ETU);
        assertThat(testInformationPersonnelle.getAdresseParent()).isEqualTo(UPDATED_ADRESSE_PARENT);
        assertThat(testInformationPersonnelle.getTelParent()).isEqualTo(UPDATED_TEL_PARENT);
        assertThat(testInformationPersonnelle.getEmailParent()).isEqualTo(UPDATED_EMAIL_PARENT);
        assertThat(testInformationPersonnelle.getNomParent()).isEqualTo(DEFAULT_NOM_PARENT);
        assertThat(testInformationPersonnelle.getPrenomParent()).isEqualTo(UPDATED_PRENOM_PARENT);
        assertThat(testInformationPersonnelle.getHandicapYN()).isEqualTo(UPDATED_HANDICAP_YN);
        assertThat(testInformationPersonnelle.getPhoto()).isEqualTo(UPDATED_PHOTO);
        assertThat(testInformationPersonnelle.getOrdiPersoYN()).isEqualTo(UPDATED_ORDI_PERSO_YN);
        assertThat(testInformationPersonnelle.getDerniereModification()).isEqualTo(UPDATED_DERNIERE_MODIFICATION);
        assertThat(testInformationPersonnelle.getEmailUser()).isEqualTo(UPDATED_EMAIL_USER);
    }

    @Test
    @Transactional
    void fullUpdateInformationPersonnelleWithPatch() throws Exception {
        // Initialize the database
        informationPersonnelleRepository.saveAndFlush(informationPersonnelle);

        int databaseSizeBeforeUpdate = informationPersonnelleRepository.findAll().size();

        // Update the informationPersonnelle using partial update
        InformationPersonnelle partialUpdatedInformationPersonnelle = new InformationPersonnelle();
        partialUpdatedInformationPersonnelle.setId(informationPersonnelle.getId());

        partialUpdatedInformationPersonnelle
            .nomEtu(UPDATED_NOM_ETU)
            .nomJeuneFilleEtu(UPDATED_NOM_JEUNE_FILLE_ETU)
            .prenomEtu(UPDATED_PRENOM_ETU)
            .statutMarital(UPDATED_STATUT_MARITAL)
            .regime(UPDATED_REGIME)
            .profession(UPDATED_PROFESSION)
            .adresseEtu(UPDATED_ADRESSE_ETU)
            .telEtu(UPDATED_TEL_ETU)
            .emailEtu(UPDATED_EMAIL_ETU)
            .adresseParent(UPDATED_ADRESSE_PARENT)
            .telParent(UPDATED_TEL_PARENT)
            .emailParent(UPDATED_EMAIL_PARENT)
            .nomParent(UPDATED_NOM_PARENT)
            .prenomParent(UPDATED_PRENOM_PARENT)
            .handicapYN(UPDATED_HANDICAP_YN)
            .photo(UPDATED_PHOTO)
            .ordiPersoYN(UPDATED_ORDI_PERSO_YN)
            .derniereModification(UPDATED_DERNIERE_MODIFICATION)
            .emailUser(UPDATED_EMAIL_USER);

        restInformationPersonnelleMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedInformationPersonnelle.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedInformationPersonnelle))
            )
            .andExpect(status().isOk());

        // Validate the InformationPersonnelle in the database
        List<InformationPersonnelle> informationPersonnelleList = informationPersonnelleRepository.findAll();
        assertThat(informationPersonnelleList).hasSize(databaseSizeBeforeUpdate);
        InformationPersonnelle testInformationPersonnelle = informationPersonnelleList.get(informationPersonnelleList.size() - 1);
        assertThat(testInformationPersonnelle.getNomEtu()).isEqualTo(UPDATED_NOM_ETU);
        assertThat(testInformationPersonnelle.getNomJeuneFilleEtu()).isEqualTo(UPDATED_NOM_JEUNE_FILLE_ETU);
        assertThat(testInformationPersonnelle.getPrenomEtu()).isEqualTo(UPDATED_PRENOM_ETU);
        assertThat(testInformationPersonnelle.getStatutMarital()).isEqualTo(UPDATED_STATUT_MARITAL);
        assertThat(testInformationPersonnelle.getRegime()).isEqualTo(UPDATED_REGIME);
        assertThat(testInformationPersonnelle.getProfession()).isEqualTo(UPDATED_PROFESSION);
        assertThat(testInformationPersonnelle.getAdresseEtu()).isEqualTo(UPDATED_ADRESSE_ETU);
        assertThat(testInformationPersonnelle.getTelEtu()).isEqualTo(UPDATED_TEL_ETU);
        assertThat(testInformationPersonnelle.getEmailEtu()).isEqualTo(UPDATED_EMAIL_ETU);
        assertThat(testInformationPersonnelle.getAdresseParent()).isEqualTo(UPDATED_ADRESSE_PARENT);
        assertThat(testInformationPersonnelle.getTelParent()).isEqualTo(UPDATED_TEL_PARENT);
        assertThat(testInformationPersonnelle.getEmailParent()).isEqualTo(UPDATED_EMAIL_PARENT);
        assertThat(testInformationPersonnelle.getNomParent()).isEqualTo(UPDATED_NOM_PARENT);
        assertThat(testInformationPersonnelle.getPrenomParent()).isEqualTo(UPDATED_PRENOM_PARENT);
        assertThat(testInformationPersonnelle.getHandicapYN()).isEqualTo(UPDATED_HANDICAP_YN);
        assertThat(testInformationPersonnelle.getPhoto()).isEqualTo(UPDATED_PHOTO);
        assertThat(testInformationPersonnelle.getOrdiPersoYN()).isEqualTo(UPDATED_ORDI_PERSO_YN);
        assertThat(testInformationPersonnelle.getDerniereModification()).isEqualTo(UPDATED_DERNIERE_MODIFICATION);
        assertThat(testInformationPersonnelle.getEmailUser()).isEqualTo(UPDATED_EMAIL_USER);
    }

    @Test
    @Transactional
    void patchNonExistingInformationPersonnelle() throws Exception {
        int databaseSizeBeforeUpdate = informationPersonnelleRepository.findAll().size();
        informationPersonnelle.setId(longCount.incrementAndGet());

        // Create the InformationPersonnelle
        InformationPersonnelleDTO informationPersonnelleDTO = informationPersonnelleMapper.toDto(informationPersonnelle);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restInformationPersonnelleMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, informationPersonnelleDTO.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(informationPersonnelleDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the InformationPersonnelle in the database
        List<InformationPersonnelle> informationPersonnelleList = informationPersonnelleRepository.findAll();
        assertThat(informationPersonnelleList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchInformationPersonnelle() throws Exception {
        int databaseSizeBeforeUpdate = informationPersonnelleRepository.findAll().size();
        informationPersonnelle.setId(longCount.incrementAndGet());

        // Create the InformationPersonnelle
        InformationPersonnelleDTO informationPersonnelleDTO = informationPersonnelleMapper.toDto(informationPersonnelle);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restInformationPersonnelleMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(informationPersonnelleDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the InformationPersonnelle in the database
        List<InformationPersonnelle> informationPersonnelleList = informationPersonnelleRepository.findAll();
        assertThat(informationPersonnelleList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamInformationPersonnelle() throws Exception {
        int databaseSizeBeforeUpdate = informationPersonnelleRepository.findAll().size();
        informationPersonnelle.setId(longCount.incrementAndGet());

        // Create the InformationPersonnelle
        InformationPersonnelleDTO informationPersonnelleDTO = informationPersonnelleMapper.toDto(informationPersonnelle);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restInformationPersonnelleMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(informationPersonnelleDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the InformationPersonnelle in the database
        List<InformationPersonnelle> informationPersonnelleList = informationPersonnelleRepository.findAll();
        assertThat(informationPersonnelleList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteInformationPersonnelle() throws Exception {
        // Initialize the database
        informationPersonnelleRepository.saveAndFlush(informationPersonnelle);

        int databaseSizeBeforeDelete = informationPersonnelleRepository.findAll().size();

        // Delete the informationPersonnelle
        restInformationPersonnelleMockMvc
            .perform(delete(ENTITY_API_URL_ID, informationPersonnelle.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<InformationPersonnelle> informationPersonnelleList = informationPersonnelleRepository.findAll();
        assertThat(informationPersonnelleList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
