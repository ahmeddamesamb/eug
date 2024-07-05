package sn.ugb.gir.service.impl;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Arrays;
import java.util.Base64;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import sn.ugb.gir.domain.Etudiant;
import sn.ugb.gir.domain.InformationImage;
import sn.ugb.gir.domain.InformationPersonnelle;
import sn.ugb.gir.repository.EtudiantRepository;
import sn.ugb.gir.repository.InformationImageRepository;
import sn.ugb.gir.repository.InformationPersonnelleRepository;
import sn.ugb.gir.repository.search.InformationPersonnelleSearchRepository;
import sn.ugb.gir.service.EtudiantService;
import sn.ugb.gir.service.InformationPersonnelleService;
import sn.ugb.gir.service.TypeBourseService;
import sn.ugb.gir.service.TypeHandicapService;
import sn.ugb.gir.service.dto.EtudiantDTO;
import sn.ugb.gir.service.dto.InformationPersonnelleDTO;
import sn.ugb.gir.service.dto.TypeBourseDTO;
import sn.ugb.gir.service.dto.TypeHandicapDTO;

import sn.ugb.gir.service.mapper.InformationPersonnelleMapper;
import sn.ugb.gir.web.rest.errors.BadRequestAlertException;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.GCMParameterSpec;

import static org.hibernate.id.IdentifierGenerator.ENTITY_NAME;

/**
 * Service Implementation for managing {@link sn.ugb.gir.domain.InformationPersonnelle}.
 */
@Service
@Transactional
public class InformationPersonnelleServiceImpl implements InformationPersonnelleService {

    private final Logger log = LoggerFactory.getLogger(InformationPersonnelleServiceImpl.class);

    private final InformationPersonnelleRepository informationPersonnelleRepository;

    private final InformationPersonnelleMapper informationPersonnelleMapper;

    private final InformationPersonnelleSearchRepository informationPersonnelleSearchRepository;

    @Autowired
    private TypeHandicapService typeHandicapService;
    @Autowired
    private TypeBourseService typeBourseService;
    @Autowired
    private EtudiantService etudiantService;

    @Autowired
    private EtudiantRepository etudiantRepository;

    @Autowired
    private InformationImageRepository informationImageRepository;

    private static final String ENCRYPTION_ALGORITHM = "AES";
    private static final String ENCRYPTION_MODE = "AES/GCM/NoPadding";
    private static final int GCM_TAG_LENGTH = 16;
    private static final int GCM_IV_LENGTH = 12;
    private static final int AES_KEY_SIZE = 256;

    private final SecretKey secretKey;

    public InformationPersonnelleServiceImpl(
        InformationPersonnelleRepository informationPersonnelleRepository,
        InformationPersonnelleMapper informationPersonnelleMapper,
        InformationPersonnelleSearchRepository informationPersonnelleSearchRepository
    ) {
        this.informationPersonnelleRepository = informationPersonnelleRepository;
        this.informationPersonnelleMapper = informationPersonnelleMapper;
        this.informationPersonnelleSearchRepository = informationPersonnelleSearchRepository;
        KeyGenerator keyGen = null;
        try {
            keyGen = KeyGenerator.getInstance(ENCRYPTION_ALGORITHM);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
        keyGen.init(AES_KEY_SIZE);
        this.secretKey = keyGen.generateKey();
    }

    @Override
    @Transactional
    public InformationPersonnelleDTO save(InformationPersonnelleDTO informationPersonnelleDTO) {
        log.debug("Request to save InformationPersonnelle : {}", informationPersonnelleDTO);

        validateData(informationPersonnelleDTO,true);

        if (informationPersonnelleDTO.getEtudiant() != null && informationPersonnelleDTO.getEtudiant().getId() == null) {
            // Save the Etudiant entity first using EtudiantService
            EtudiantDTO etudiantDTO = informationPersonnelleDTO.getEtudiant();
            etudiantDTO = etudiantService.save(etudiantDTO);
            informationPersonnelleDTO.setEtudiant(etudiantDTO);
        }

        if (informationPersonnelleDTO.getTypeHandicap() != null && informationPersonnelleDTO.getTypeHandicap().getId() == null) {
            // Save the TypeHandicap entity first using TypeHandicapService
            TypeHandicapDTO typeHandicapDTO = informationPersonnelleDTO.getTypeHandicap();
            typeHandicapDTO = typeHandicapService.save(typeHandicapDTO);
            informationPersonnelleDTO.setTypeHandicap(typeHandicapDTO);
        }

        if (informationPersonnelleDTO.getTypeBourse() != null && informationPersonnelleDTO.getTypeBourse().getId() == null) {
            // Save the TypeBourse entity first using TypeBourseService
            TypeBourseDTO typeBourseDTO = informationPersonnelleDTO.getTypeBourse();
            typeBourseDTO = typeBourseService.save(typeBourseDTO);
            informationPersonnelleDTO.setTypeBourse(typeBourseDTO);
        }

        InformationPersonnelle informationPersonnelle = informationPersonnelleMapper.toEntity(informationPersonnelleDTO);
        informationPersonnelle = informationPersonnelleRepository.save(informationPersonnelle);
        InformationPersonnelleDTO result = informationPersonnelleMapper.toDto(informationPersonnelle);
        informationPersonnelleSearchRepository.index(informationPersonnelle);
        return result;
    }

    @Override
    @Transactional
    public InformationPersonnelleDTO update(InformationPersonnelleDTO informationPersonnelleDTO) {
        log.debug("Request to update InformationPersonnelle : {}", informationPersonnelleDTO);

        validateData(informationPersonnelleDTO,true);

        if (informationPersonnelleDTO.getEtudiant() != null && informationPersonnelleDTO.getEtudiant().getId() != null) {
            // Save the Etudiant entity first using EtudiantService
            EtudiantDTO etudiantDTO = informationPersonnelleDTO.getEtudiant();
            etudiantDTO = etudiantService.update(etudiantDTO);
            informationPersonnelleDTO.setEtudiant(etudiantDTO);
        }

        if (informationPersonnelleDTO.getTypeHandicap() != null && informationPersonnelleDTO.getTypeHandicap().getId() != null) {
            // Save the TypeHandicap entity first using TypeHandicapService
            TypeHandicapDTO typeHandicapDTO = informationPersonnelleDTO.getTypeHandicap();
            typeHandicapDTO = typeHandicapService.update(typeHandicapDTO);
            informationPersonnelleDTO.setTypeHandicap(typeHandicapDTO);
        }

        if (informationPersonnelleDTO.getTypeBourse() != null && informationPersonnelleDTO.getTypeBourse().getId() != null) {
            // Save the TypeBourse entity first using TypeBourseService
            TypeBourseDTO typeBourseDTO = informationPersonnelleDTO.getTypeBourse();
            typeBourseDTO = typeBourseService.update(typeBourseDTO);
            informationPersonnelleDTO.setTypeBourse(typeBourseDTO);
        }

        InformationPersonnelle informationPersonnelle = informationPersonnelleMapper.toEntity(informationPersonnelleDTO);
        informationPersonnelle = informationPersonnelleRepository.save(informationPersonnelle);
        InformationPersonnelleDTO result = informationPersonnelleMapper.toDto(informationPersonnelle);
        informationPersonnelleSearchRepository.index(informationPersonnelle);
        return result;
    }

    @Override
    public Optional<InformationPersonnelleDTO> partialUpdate(InformationPersonnelleDTO informationPersonnelleDTO) {
        log.debug("Request to partially update InformationPersonnelle : {}", informationPersonnelleDTO);

        return informationPersonnelleRepository
            .findById(informationPersonnelleDTO.getId())
            .map(existingInformationPersonnelle -> {
                informationPersonnelleMapper.partialUpdate(existingInformationPersonnelle, informationPersonnelleDTO);

                return existingInformationPersonnelle;
            })
            .map(informationPersonnelleRepository::save)
            .map(savedInformationPersonnelle -> {
                informationPersonnelleSearchRepository.index(savedInformationPersonnelle);
                return savedInformationPersonnelle;
            })
            .map(informationPersonnelleMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<InformationPersonnelleDTO> findAll(Pageable pageable) {
        log.debug("Request to get all InformationPersonnelles");
        return informationPersonnelleRepository.findAll(pageable).map(informationPersonnelleMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<InformationPersonnelleDTO> findOne(Long id) {
        log.debug("Request to get InformationPersonnelle : {}", id);
        return informationPersonnelleRepository.findById(id).map(informationPersonnelleMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<InformationPersonnelleDTO> findOneByCodeEtudiant(String code) {
        log.debug("Request to get InformationPersonnelle : {}", code);
        return informationPersonnelleRepository.findByEtudiantCodeEtu(code).map(informationPersonnelleMapper::toDto);
    }


    @Override
    public void delete(Long id) {
        log.debug("Request to delete InformationPersonnelle : {}", id);
        informationPersonnelleRepository.deleteById(id);
        informationPersonnelleSearchRepository.deleteFromIndexById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<InformationPersonnelleDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of InformationPersonnelles for query {}", query);
        return informationPersonnelleSearchRepository.search(query, pageable).map(informationPersonnelleMapper::toDto);
    }
    private void validateData(InformationPersonnelleDTO informationPersonnelleDTO, boolean isAdmin) {
        if (informationPersonnelleDTO == null) {
            throw new BadRequestAlertException("InformationPersonnelle cannot be null", ENTITY_NAME, "nullInformationPersonnelle");
        }

        String situationMatrimoniale = informationPersonnelleDTO.getStatutMarital();
        String telephone = informationPersonnelleDTO.getTelEtu();
        String emailPersonnel = informationPersonnelleDTO.getEmailEtu();
        String prenomParent = informationPersonnelleDTO.getPrenomParent();
        String nomParent = informationPersonnelleDTO.getNomParent();
        String adresseParent = informationPersonnelleDTO.getAdresseParent();

        if (isEmptyOrBlank(situationMatrimoniale)) {
            throw new BadRequestAlertException("La situation matrimoniale est obligatoire", ENTITY_NAME, "situationMatrimonialeObligatoire");
        }

        if (isEmptyOrBlank(telephone)) {
            throw new BadRequestAlertException("Le téléphone est obligatoire", ENTITY_NAME, "telephoneObligatoire");
        }

        if (isEmptyOrBlank(emailPersonnel)) {
            throw new BadRequestAlertException("L'email personnel est obligatoire", ENTITY_NAME, "emailPersonnelObligatoire");
        }

        if (isEmptyOrBlank(prenomParent)) {
            throw new BadRequestAlertException("Le prénom du parent est obligatoire", ENTITY_NAME, "prenomParentObligatoire");
        }

        if (isEmptyOrBlank(nomParent)) {
            throw new BadRequestAlertException("Le nom du parent est obligatoire", ENTITY_NAME, "nomParentObligatoire");
        }

        if (isEmptyOrBlank(adresseParent)) {
            throw new BadRequestAlertException("L'adresse du parent est obligatoire", ENTITY_NAME, "adresseParentObligatoire");
        }

        Long id = informationPersonnelleDTO.getId();
        if (id != null) {
            Optional<InformationPersonnelle> existingInformationPersonnelle = informationPersonnelleRepository.findById(id);
            if (existingInformationPersonnelle.isPresent()) {
                InformationPersonnelle informationPersonnelle = existingInformationPersonnelle.get();

                if (!isAdmin) {
                    if (!informationPersonnelle.getRegime().equals(informationPersonnelleDTO.getRegime())) {
                        throw new BadRequestAlertException("L'identifiant ne peut être modifié que par l'administrateur", ENTITY_NAME, "identifiantNonModifiable");
                    }
                    if (!informationPersonnelle.getNomEtu().equals(informationPersonnelleDTO.getNomEtu())) {
                        throw new BadRequestAlertException("Le nom ne peut être modifié que par l'administrateur", ENTITY_NAME, "nomNonModifiable");
                    }
                    if (!informationPersonnelle.getPrenomEtu().equals(informationPersonnelleDTO.getPrenomEtu())) {
                        throw new BadRequestAlertException("Le prénom ne peut être modifié que par l'administrateur", ENTITY_NAME, "prenomNonModifiable");
                    }
                    if (!informationPersonnelle.getProfession().equals(informationPersonnelleDTO.getProfession())) {
                        throw new BadRequestAlertException("L'INE ne peut être modifié que par l'administrateur", ENTITY_NAME, "ineNonModifiable");
                    }
                    if (!informationPersonnelle.getAdresseEtu().equals(informationPersonnelleDTO.getAdresseEtu())) {
                        throw new BadRequestAlertException("La date de naissance ne peut être modifiée que par l'administrateur", ENTITY_NAME, "dateNaissanceNonModifiable");
                    }
                    if (!informationPersonnelle.getTelEtu().equals(informationPersonnelleDTO.getTelEtu())) {
                        throw new BadRequestAlertException("Le lieu de naissance ne peut être modifié que par l'administrateur", ENTITY_NAME, "lieuNaissanceNonModifiable");
                    }
                    if (!informationPersonnelle.getAdresseParent().equals(informationPersonnelleDTO.getAdresseParent())) {
                        throw new BadRequestAlertException("L'adresse parent ne peut être modifié que par l'administrateur", ENTITY_NAME, "adresseParentNonModifiable");
                    }
                }
            } else {
                throw new BadRequestAlertException("InformationPersonnelle avec id " + id + " non trouvé", ENTITY_NAME, "informationPersonnelleNotFound");
            }
        }
    }

    private boolean isEmptyOrBlank(String value) {
        return value == null || value.trim().isEmpty();
    }

    public String store(MultipartFile file, String codeEtu) {
        try {
            if (file.isEmpty()) {
                throw new RuntimeException("Le fichier ne peut pas etre vide.");
            }

            if (!file.getContentType().equals("image/jpeg") && !file.getContentType().equals("image/png")) {
                throw new IllegalArgumentException("Seuls les fichiers JPEG et PNG sont autorisés.");
            }

            if (file.getSize() > 2*1024*1024) {
                throw new IllegalArgumentException("Fichier trop volumineux.");
            }

            String filename = encrypt(codeEtu) + "." + getFileExtension(file.getOriginalFilename());

            Optional<InformationImage> informationImageOpt = informationImageRepository.findByEnCoursYN("true");
            if (informationImageOpt.isEmpty()) {
                throw new RuntimeException("InformationImage avec enCoursYN=true non trouvée.");
            }
            InformationImage informationImage = informationImageOpt.get();
            Path rootLocation = Paths.get(informationImage.getCheminPath());

            Files.createDirectories(rootLocation);

            Path destinationFile = rootLocation.resolve(Paths.get(filename)).normalize().toAbsolutePath();

            try (var inputStream = file.getInputStream()) {
                Files.copy(inputStream, destinationFile);
            }

            return filename;
        } catch (IOException e) {
            throw new RuntimeException("Échec de l'enregistrement du fichier.", e);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private String getFileExtension(String filename) {
        if (filename == null || filename.isEmpty()) {
            return "";
        }
        int dotIndex = filename.lastIndexOf('.');
        return (dotIndex == -1) ? "" : filename.substring(dotIndex + 1);
    }

    public String encrypt(String plainText) throws Exception {
        Cipher cipher = Cipher.getInstance(ENCRYPTION_MODE);
        byte[] iv = new byte[GCM_IV_LENGTH];
        SecureRandom random = new SecureRandom();
        random.nextBytes(iv);
        GCMParameterSpec parameterSpec = new GCMParameterSpec(GCM_TAG_LENGTH * 8, iv);
        cipher.init(Cipher.ENCRYPT_MODE, secretKey, parameterSpec);
        byte[] encryptedText = cipher.doFinal(plainText.getBytes());
        byte[] encryptedTextWithIv = new byte[iv.length + encryptedText.length];
        System.arraycopy(iv, 0, encryptedTextWithIv, 0, iv.length);
        System.arraycopy(encryptedText, 0, encryptedTextWithIv, iv.length, encryptedText.length);
        return Base64.getUrlEncoder().encodeToString(encryptedTextWithIv);
    }

    public String decrypt(String encryptedText) throws Exception {
        byte[] encryptedTextWithIv = Base64.getUrlDecoder().decode(encryptedText);
        byte[] iv = Arrays.copyOfRange(encryptedTextWithIv, 0, GCM_IV_LENGTH);
        byte[] encryptedTextBytes = Arrays.copyOfRange(encryptedTextWithIv, GCM_IV_LENGTH, encryptedTextWithIv.length);
        Cipher cipher = Cipher.getInstance(ENCRYPTION_MODE);
        GCMParameterSpec parameterSpec = new GCMParameterSpec(GCM_TAG_LENGTH * 8, iv);
        cipher.init(Cipher.DECRYPT_MODE, secretKey, parameterSpec);
        byte[] decryptedTextBytes = cipher.doFinal(encryptedTextBytes);
        return new String(decryptedTextBytes);
    }

    @Override
    public String uploadPhoto(MultipartFile file, String codeEtu) {
        Optional<Etudiant> etudiant = etudiantRepository.findByCodeEtu(codeEtu);
        if (!etudiant.isPresent()) {
            throw new RuntimeException("Étudiant non trouvé");
        }

        String filename = store(file, codeEtu);

        InformationPersonnelle informationPersonnelle = etudiant.get().getInformationPersonnelle();
        if (informationPersonnelle == null) {
            throw new RuntimeException("L'étudiant n'est associée à aucune information personnelle.");
        }

        informationPersonnelle.setPhoto(filename);
        informationPersonnelleRepository.save(informationPersonnelle);

        return filename;
    }
}
