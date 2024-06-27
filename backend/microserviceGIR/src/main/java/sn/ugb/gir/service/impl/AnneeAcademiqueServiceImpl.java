package sn.ugb.gir.service.impl;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sn.ugb.gir.domain.AnneeAcademique;
import sn.ugb.gir.repository.AnneeAcademiqueRepository;
import sn.ugb.gir.repository.search.AnneeAcademiqueSearchRepository;
import sn.ugb.gir.service.AnneeAcademiqueService;
import sn.ugb.gir.service.dto.AnneeAcademiqueDTO;
import sn.ugb.gir.service.mapper.AnneeAcademiqueMapper;
import sn.ugb.gir.web.rest.errors.BadRequestAlertException;

import static org.hibernate.id.IdentifierGenerator.ENTITY_NAME;

/**
 * Service Implementation for managing {@link sn.ugb.gir.domain.AnneeAcademique}.
 */
@Service
@Transactional
public class AnneeAcademiqueServiceImpl implements AnneeAcademiqueService {

    private final Logger log = LoggerFactory.getLogger(AnneeAcademiqueServiceImpl.class);

    private final AnneeAcademiqueRepository anneeAcademiqueRepository;

    private final AnneeAcademiqueMapper anneeAcademiqueMapper;

    private final AnneeAcademiqueSearchRepository anneeAcademiqueSearchRepository;

    public AnneeAcademiqueServiceImpl(
        AnneeAcademiqueRepository anneeAcademiqueRepository,
        AnneeAcademiqueMapper anneeAcademiqueMapper,
        AnneeAcademiqueSearchRepository anneeAcademiqueSearchRepository
    ) {
        this.anneeAcademiqueRepository = anneeAcademiqueRepository;
        this.anneeAcademiqueMapper = anneeAcademiqueMapper;
        this.anneeAcademiqueSearchRepository = anneeAcademiqueSearchRepository;
    }

    @Override
    public AnneeAcademiqueDTO save(AnneeAcademiqueDTO anneeAcademiqueDTO) {
        log.debug("Request to save AnneeAcademique : {}", anneeAcademiqueDTO);

        validateData(anneeAcademiqueDTO);

        // Vérifier s'il existe déjà une AnneeAcademique avec anneeCouranteYN = true
        if (anneeAcademiqueDTO.getAnneeCouranteYN()) {
            Optional<AnneeAcademique> currentAnneeAcademique = anneeAcademiqueRepository.findByAnneeCouranteYN(true);
            currentAnneeAcademique.ifPresent(annee -> {
                annee.setAnneeCouranteYN(false);
                anneeAcademiqueRepository.save(annee);
            });
        }

        AnneeAcademique anneeAcademique = anneeAcademiqueMapper.toEntity(anneeAcademiqueDTO);
        anneeAcademique.setLibelleAnneeAcademique(generateLibelleAnneeAcademique(anneeAcademique.getAnneeAc(), anneeAcademiqueDTO.getSeparateur()));
        anneeAcademique = anneeAcademiqueRepository.save(anneeAcademique);
        AnneeAcademiqueDTO result = anneeAcademiqueMapper.toDto(anneeAcademique);
        anneeAcademiqueSearchRepository.index(anneeAcademique);
        return result;
    }

    @Override
    public AnneeAcademiqueDTO update(AnneeAcademiqueDTO anneeAcademiqueDTO) {
        log.debug("Request to update AnneeAcademique : {}", anneeAcademiqueDTO);

        validateData(anneeAcademiqueDTO);

        // Vérifier s'il existe déjà une AnneeAcademique avec anneeCouranteYN = true
        if (anneeAcademiqueDTO.getAnneeCouranteYN()) {
            Optional<AnneeAcademique> currentAnneeAcademique = anneeAcademiqueRepository.findByAnneeCouranteYN(true);
            currentAnneeAcademique.ifPresent(annee -> {
                annee.setAnneeCouranteYN(false);
                anneeAcademiqueRepository.save(annee);
            });
        }

        AnneeAcademique anneeAcademique = anneeAcademiqueMapper.toEntity(anneeAcademiqueDTO);
        anneeAcademique = anneeAcademiqueRepository.save(anneeAcademique);
        AnneeAcademiqueDTO result = anneeAcademiqueMapper.toDto(anneeAcademique);
        anneeAcademiqueSearchRepository.index(anneeAcademique);
        return result;
    }

    @Override
    public Optional<AnneeAcademiqueDTO> partialUpdate(AnneeAcademiqueDTO anneeAcademiqueDTO) {
        log.debug("Request to partially update AnneeAcademique : {}", anneeAcademiqueDTO);

        return anneeAcademiqueRepository.findById(anneeAcademiqueDTO.getId())
            .map(existingAnneeAcademique -> {
                if (anneeAcademiqueDTO.getAnneeAc() != 0) {
                    existingAnneeAcademique.setAnneeAc(anneeAcademiqueDTO.getAnneeAc());
                }
                if (!anneeAcademiqueDTO.getSeparateur().isBlank()) {
                    existingAnneeAcademique.setSeparateur(anneeAcademiqueDTO.getSeparateur());
                }
                if (anneeAcademiqueDTO.getAnneeCouranteYN() != null) {
                    if (anneeAcademiqueDTO.getAnneeCouranteYN()) {
                        Optional<AnneeAcademique> currentAnneeAcademique = anneeAcademiqueRepository.findByAnneeCouranteYN(true);
                        currentAnneeAcademique.ifPresent(annee -> {
                            annee.setAnneeCouranteYN(false);
                            anneeAcademiqueRepository.save(annee);
                        });
                    }
                    existingAnneeAcademique.setAnneeCouranteYN(anneeAcademiqueDTO.getAnneeCouranteYN());
                }

                // Additional fields to update can be added here

                AnneeAcademique updatedAnneeAcademique = anneeAcademiqueRepository.save(existingAnneeAcademique);
                anneeAcademiqueSearchRepository.index(updatedAnneeAcademique);
                return updatedAnneeAcademique;
            })
            .map(anneeAcademiqueMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<AnneeAcademiqueDTO> findAll(Pageable pageable) {
        log.debug("Request to get all AnneeAcademiques");
        return anneeAcademiqueRepository.findAll(pageable).map(anneeAcademiqueMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<AnneeAcademiqueDTO> findOne(Long id) {
        log.debug("Request to get AnneeAcademique : {}", id);
        return anneeAcademiqueRepository.findById(id).map(anneeAcademiqueMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete AnneeAcademique : {}", id);
        anneeAcademiqueRepository.deleteById(id);
        anneeAcademiqueSearchRepository.deleteFromIndexById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<AnneeAcademiqueDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of AnneeAcademiques for query {}", query);
        return anneeAcademiqueSearchRepository.search(query, pageable).map(anneeAcademiqueMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<AnneeAcademiqueDTO> getInfosCurrentAnneeAcademique() {
        log.debug("Request to find current AnneeAcademique");
        return anneeAcademiqueRepository.findByAnneeCouranteYN(true).map(anneeAcademiqueMapper::toDto);
    }

    // Méthode pour valider les données avant sauvegarde ou mise à jour
    private void validateData(AnneeAcademiqueDTO anneeAcademiqueDTO) {
        if (anneeAcademiqueDTO == null) {
            throw new BadRequestAlertException("AnneeAcademiqueDTO cannot be null", ENTITY_NAME, "nullAnneeAcademiqueDTO");
        }

        int anneeAc = anneeAcademiqueDTO.getAnneeAc();
        if (anneeAc < 2010 || anneeAc > 2050) {
            throw new BadRequestAlertException("L'année académique doit être comprise entre 2010 et 2050", ENTITY_NAME, "AnneeAcademiqueInvalide");
        }

        if (anneeAcademiqueDTO.getSeparateur().isBlank()) {
            throw new BadRequestAlertException("Le séparateur de l'année académique ne peut pas être vide", ENTITY_NAME, "SeparateurVide");
        }

        Long id = anneeAcademiqueDTO.getId();
        if (id == null) {
            // Save operation
            if (anneeAcademiqueRepository.findByAnneeAc(anneeAc).isPresent()) {
                throw new BadRequestAlertException("Une année académique avec cette année existe déjà", ENTITY_NAME, "AnneeAcademiqueExistante");
            }
        } else {
            // Update operation
            Optional<AnneeAcademique> existingAnneeAcademique = anneeAcademiqueRepository.findById(id);
            if (existingAnneeAcademique.isEmpty()) {
                throw new BadRequestAlertException("AnneeAcademique with id " + id + " not found", ENTITY_NAME, "AnneeAcademiqueNotFound");
            }
        }
    }

    // Méthode pour générer le libellé de l'année académique
    private String generateLibelleAnneeAcademique(int anneeAc, String separateur) {
        return anneeAc + separateur + (anneeAc + 1);
    }

}

/**
 * Service Implementation for managing {@link sn.ugb.gir.domain.AnneeAcademique}.

@Service
@Transactional
public class AnneeAcademiqueServiceImpl implements AnneeAcademiqueService {

    private final Logger log = LoggerFactory.getLogger(AnneeAcademiqueServiceImpl.class);

    private static final String ENTITY_NAME = "AnneeAcademique";

    private final AnneeAcademiqueRepository anneeAcademiqueRepository;

    private final AnneeAcademiqueMapper anneeAcademiqueMapper;

    public AnneeAcademiqueServiceImpl(AnneeAcademiqueRepository anneeAcademiqueRepository, AnneeAcademiqueMapper anneeAcademiqueMapper) {
        this.anneeAcademiqueRepository = anneeAcademiqueRepository;
        this.anneeAcademiqueMapper = anneeAcademiqueMapper;
    }

    @Override
    public AnneeAcademiqueDTO save(AnneeAcademiqueDTO anneeAcademiqueDTO) {
        log.debug("Request to save AnneeAcademique : {}", anneeAcademiqueDTO);

        if (anneeAcademiqueDTO.getAnneeAc().isBlank()) {
            throw new BadRequestAlertException("A new niveau have an ANNEE_ACADEMIQUE empty ", ENTITY_NAME, "Annnee Academique empty");
        }
        if(anneeAcademiqueRepository.findByAnneeAc(anneeAcademiqueDTO.getAnneeAc()).isPresent()){
            throw new BadRequestAlertException("A new niveau have an ANNEE_ACADEMIQUE exists ", ENTITY_NAME, "Annnee Academique exists");
        }


        // Vérifier s'il existe déjà une AnneeAcademique avec anneeCouranteYN = 1
        if (anneeAcademiqueDTO.getAnneeCouranteYN() == 1) {
            Optional<AnneeAcademique> currentAnneeAcademiqueAnnee = anneeAcademiqueRepository.findByAnneeCouranteYN(1); ;
            if(currentAnneeAcademiqueAnnee.isPresent()){
                AnneeAcademique anneeExistant = currentAnneeAcademiqueAnnee.get();
                anneeExistant.setAnneeCouranteYN(0);
                anneeAcademiqueRepository.save(anneeExistant);
            }
        }

        AnneeAcademique anneeAcademique = anneeAcademiqueMapper.toEntity(anneeAcademiqueDTO);
        anneeAcademique.setLibelleAnneeAcademique(generateLibelleAnneeAcademique(anneeAcademique.getAnneeAc()));
        anneeAcademique = anneeAcademiqueRepository.save(anneeAcademique);
        return anneeAcademiqueMapper.toDto(anneeAcademique);
    }

    @Override
    public AnneeAcademiqueDTO update(AnneeAcademiqueDTO anneeAcademiqueDTO) {
        log.debug("Request to update AnneeAcademique : {}", anneeAcademiqueDTO);

        if (anneeAcademiqueDTO.getAnneeAc().isBlank()) {
            throw new BadRequestAlertException("A new niveau have an ANNEE_ACADEMIQUE empty ", ENTITY_NAME, "Annnee Academique empty");
        }
        if (anneeAcademiqueRepository.findByAnneeAc(anneeAcademiqueDTO.getAnneeAc()).isPresent()){
            throw new BadRequestAlertException("A new niveau have an ANNEE_ACADEMIQUE exists ", ENTITY_NAME, "Annnee Academique exists");
        }

        // Vérifier s'il existe déjà une AnneeAcademique avec anneeCouranteYN = 1
        if (anneeAcademiqueDTO.getAnneeCouranteYN() == 1) {
            Optional<AnneeAcademique> currentAnneeAcademiqueAnnee = anneeAcademiqueRepository.findByAnneeCouranteYN(1); ;
            if(currentAnneeAcademiqueAnnee.isPresent()){
                AnneeAcademique anneeExistant = currentAnneeAcademiqueAnnee.get();
                anneeExistant.setAnneeCouranteYN(0);
                anneeAcademiqueRepository.save(anneeExistant);
            }
        }

        AnneeAcademique anneeAcademique = anneeAcademiqueMapper.toEntity(anneeAcademiqueDTO);
        anneeAcademique = anneeAcademiqueRepository.save(anneeAcademique);
        return anneeAcademiqueMapper.toDto(anneeAcademique);
    }

    @Override
    public Optional<AnneeAcademiqueDTO> partialUpdate(AnneeAcademiqueDTO anneeAcademiqueDTO) {
        log.debug("Request to partially update AnneeAcademique : {}", anneeAcademiqueDTO);

        return anneeAcademiqueRepository
            .findById(anneeAcademiqueDTO.getId())
            .map(existingAnneeAcademique -> {
                anneeAcademiqueMapper.partialUpdate(existingAnneeAcademique, anneeAcademiqueDTO);
                return existingAnneeAcademique;
            })
            .map(anneeAcademiqueRepository::save)
            .map(anneeAcademiqueMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<AnneeAcademiqueDTO> findAll(Pageable pageable) {
        log.debug("Request to get all AnneeAcademiques");
        return anneeAcademiqueRepository.findAll(pageable).map(anneeAcademiqueMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<AnneeAcademiqueDTO> findOne(Long id) {
        log.debug("Request to get AnneeAcademique : {}", id);
        return anneeAcademiqueRepository.findById(id).map(anneeAcademiqueMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete AnneeAcademique : {}", id);
        anneeAcademiqueRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<AnneeAcademiqueDTO> getInfosCurrentAnneeAcademique() {
        log.debug("Request to find current AnneeAcademique");
        return anneeAcademiqueRepository.findByAnneeCouranteYN(1).map(anneeAcademiqueMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public String generateLibelleAnneeAcademique(String anneeAc) {
        int currentYear = Integer.parseInt(anneeAc);
        int nextYear = currentYear + 1;
        return currentYear + "-" + nextYear;
    }
}
*/
