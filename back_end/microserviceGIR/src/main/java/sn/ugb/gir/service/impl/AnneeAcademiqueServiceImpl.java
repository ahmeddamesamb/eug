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
import sn.ugb.gir.service.AnneeAcademiqueService;
import sn.ugb.gir.service.dto.AnneeAcademiqueDTO;
import sn.ugb.gir.service.mapper.AnneeAcademiqueMapper;
import sn.ugb.gir.web.rest.errors.BadRequestAlertException;


/**
 * Service Implementation for managing {@link sn.ugb.gir.domain.AnneeAcademique}.
 */
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

    /*@Override
    public AnneeAcademiqueDTO save(AnneeAcademiqueDTO anneeAcademiqueDTO) {
        log.debug("Request to save AnneeAcademique : {}", anneeAcademiqueDTO);

        // Vérifier s'il existe déjà une AnneeAcademique avec anneeCouranteYN = 1
        if (anneeAcademiqueDTO.getAnneeCouranteYN() == 1) {
            Optional<AnneeAcademique> existing = anneeAcademiqueRepository.findByAnneeCouranteYN(1);

            if (existing.isPresent() && !existing.get().getId().equals(anneeAcademiqueDTO.getId())) {
                throw new IllegalStateException("There can only be one current academic year.");
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

        // Vérifier s'il existe déjà une AnneeAcademique avec anneeCouranteYN = 1
        if (anneeAcademiqueDTO.getAnneeCouranteYN() == 1) {
            Optional<AnneeAcademique> existing = anneeAcademiqueRepository.findByAnneeCouranteYN(1);
            if (existing.isPresent() && !existing.get().getId().equals(anneeAcademiqueDTO.getId())) {
                throw new IllegalStateException("There can only be one current academic year.");
            }
        }

        AnneeAcademique anneeAcademique = anneeAcademiqueMapper.toEntity(anneeAcademiqueDTO);
        //anneeAcademique.setLibelleAnneeAcademique(generateLibelleAnneeAcademique(anneeAcademique.getAnneeAc()));
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
        // Convert anneeAc to an integer to calculate the next year
        int currentYear = Integer.parseInt(anneeAc);
        int nextYear = currentYear + 1;

        // Concatenate the current year and the next year with a dash in between
        return currentYear + "-" + nextYear;
    }



}
*/
