package sn.ugb.gir.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sn.ugb.gir.domain.Niveau;
import sn.ugb.gir.repository.NiveauRepository;
import sn.ugb.gir.service.NiveauService;
import sn.ugb.gir.service.dto.NiveauDTO;
import sn.ugb.gir.service.mapper.NiveauMapper;
import sn.ugb.gir.web.rest.errors.BadRequestAlertException;

import java.util.Optional;


/**
 * Service Implementation for managing {@link sn.ugb.gir.domain.Niveau}.
 */
@Service
@Transactional
public class NiveauServiceImpl implements NiveauService {

    private final Logger log = LoggerFactory.getLogger(NiveauServiceImpl.class);
    private static final String ENTITY_NAME = "Niveau";

    private final NiveauRepository niveauRepository;
    private final NiveauMapper niveauMapper;

    public NiveauServiceImpl(NiveauRepository niveauRepository, NiveauMapper niveauMapper) {
        this.niveauRepository = niveauRepository;
        this.niveauMapper = niveauMapper;
    }

    @Override
    public NiveauDTO save(NiveauDTO niveauDTO) {
        log.debug("Request to save Niveau : {}", niveauDTO);

        validateData(niveauDTO);

        Niveau niveau = niveauMapper.toEntity(niveauDTO);
        niveau = niveauRepository.save(niveau);
        return niveauMapper.toDto(niveau);
    }

    @Override
    public NiveauDTO update(NiveauDTO niveauDTO) {
        log.debug("Request to update Niveau : {}", niveauDTO);

        validateData(niveauDTO);

        Niveau niveau = niveauMapper.toEntity(niveauDTO);
        niveau = niveauRepository.save(niveau);
        return niveauMapper.toDto(niveau);
    }

    @Override
    public Optional<NiveauDTO> partialUpdate(NiveauDTO niveauDTO) {
        log.debug("Request to partially update Niveau : {}", niveauDTO);

        validateData(niveauDTO);

        return niveauRepository
            .findById(niveauDTO.getId())
            .map(existingNiveau -> {
                niveauMapper.partialUpdate(existingNiveau, niveauDTO);
                return existingNiveau;
            })
            .map(niveauRepository::save)
            .map(niveauMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<NiveauDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Niveaus");
        return niveauRepository.findAll(pageable).map(niveauMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<NiveauDTO> findOne(Long id) {
        log.debug("Request to get Niveau : {}", id);
        return niveauRepository.findById(id).map(niveauMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Niveau : {}", id);

        if (!niveauRepository.existsById(id)) {
            throw new BadRequestAlertException("Niveau with id " + id + " not found", ENTITY_NAME, "niveauNotFound");
        }

        niveauRepository.deleteById(id);
    }

    private void validateData(NiveauDTO niveauDTO) {
        if (niveauDTO == null) {
            throw new BadRequestAlertException("Niveau cannot be null", ENTITY_NAME, "nullNiveau");
        }

        String codeNiveau = niveauDTO.getCodeNiveau();
        String anneeEtude = niveauDTO.getAnneeEtude();
        String libelleNiveau = niveauDTO.getLibelleNiveau();

        if (isEmptyOrBlank(anneeEtude)) {
            throw new BadRequestAlertException("Niveau year of study cannot be empty", ENTITY_NAME, "emptyAnneeEtude");
        }

        if (isEmptyOrBlank(codeNiveau)) {
            throw new BadRequestAlertException("Niveau code cannot be empty", ENTITY_NAME, "emptyCodeNiveau");
        }

        if (isEmptyOrBlank(libelleNiveau)) {
            throw new BadRequestAlertException("Niveau year of study cannot be empty", ENTITY_NAME, "emptyAnneeEtude");
        }
        Long id = niveauDTO.getId();
        if (id == null) {
            // Save operation
            if (niveauRepository.existsByAnneeEtudeIgnoreCase(anneeEtude)) {
                throw new BadRequestAlertException("A niveau with the same year of study already exists", ENTITY_NAME, "anneeEtude.exists");
            }

            if (niveauRepository.existsByCodeNiveauIgnoreCase(codeNiveau)) {
                throw new BadRequestAlertException("A niveau with the same code already exists", ENTITY_NAME, "CodeNiveau.exists");
            }

            if (niveauRepository.existsByLibelleNiveauIgnoreCase(libelleNiveau)) {
                throw new BadRequestAlertException("A niveau with the same year of study already exists", ENTITY_NAME, "LibelleNiveau.exists");
            }
        } else {
            // Update or PartialUpdate operation
            Optional<Niveau> existingNiveau = niveauRepository.findById(id);
            if (existingNiveau.isEmpty()) {
                throw new BadRequestAlertException("Niveau with id " + id + " not found", ENTITY_NAME, "niveauNotFound");
            }

            Niveau niveau = existingNiveau.get();
            if (!niveau.getAnneeEtude().equalsIgnoreCase(anneeEtude) && niveauRepository.existsByAnneeEtudeIgnoreCase(anneeEtude)) {
                throw new BadRequestAlertException("A niveau with the same year of study already exists", ENTITY_NAME, "anneeEtude.exists");
            }
            if (!niveau.getCodeNiveau().equalsIgnoreCase(codeNiveau) && niveauRepository.existsByCodeNiveauIgnoreCase(codeNiveau)) {
                throw new BadRequestAlertException("A niveau with the same code already exists", ENTITY_NAME, "CodeNiveau.exists");
            }

            if (!niveau.getLibelleNiveau().equalsIgnoreCase(libelleNiveau) && niveauRepository.existsByLibelleNiveauIgnoreCase(anneeEtude)) {
                throw new BadRequestAlertException("A niveau with the same year of study already exists", ENTITY_NAME, "LibelleNiveau.exists");
            }
        }
    }


    private boolean isEmptyOrBlank(String str) {
        return str == null || str.trim().isEmpty();
    }


    @Override
    public Page<NiveauDTO> getAllNiveauByUniversite(Long universiteId, Pageable pageable) {
        return niveauRepository.getAllNiveauByUniversiteId(universiteId, pageable)
            .map(niveauMapper::toDto);
    }

    @Override
    public Page<NiveauDTO> getAllNiveauByMinistere(Long ministereId, Pageable pageable) {
        return niveauRepository.getAllNiveauByMinistereId(ministereId, pageable)
            .map(niveauMapper::toDto);
    }
}

