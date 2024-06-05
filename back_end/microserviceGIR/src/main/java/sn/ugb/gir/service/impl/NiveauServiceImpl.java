package sn.ugb.gir.service.impl;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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

import static org.hibernate.id.IdentifierGenerator.ENTITY_NAME;
import static org.springframework.util.ClassUtils.isPresent;

/**
 * Service Implementation for managing {@link sn.ugb.gir.domain.Niveau}.
 */
@Service
@Transactional
public class NiveauServiceImpl implements NiveauService {

    private final Logger log = LoggerFactory.getLogger(NiveauServiceImpl.class);

    private static final String ENTITY_NAME = "Niveau" ;

    private final NiveauRepository niveauRepository;

    private final NiveauMapper niveauMapper;

    public NiveauServiceImpl(NiveauRepository niveauRepository, NiveauMapper niveauMapper) {
        this.niveauRepository = niveauRepository;
        this.niveauMapper = niveauMapper;
    }

    @Override
    public NiveauDTO save(NiveauDTO niveauDTO) {
        log.debug("Request to save Niveau : {}", niveauDTO);

        if (niveauRepository.findByAnneeEtude(niveauDTO.getCodeNiveau()).isPresent()) {
            throw new BadRequestAlertException("A new niveau have an Code Niveau ", ENTITY_NAME, "Code Niveau");
        }

        if (niveauRepository.findByAnneeEtude(niveauDTO.getAnneeEtude()).isPresent()) {
            throw new BadRequestAlertException("A new niveau have an ANNEE_ETUDE", ENTITY_NAME, "Annnee Etude exists");
        }
        Niveau niveau = niveauMapper.toEntity(niveauDTO);
        niveau = niveauRepository.save(niveau);
        return niveauMapper.toDto(niveau);
    }

    @Override
    public NiveauDTO update(NiveauDTO niveauDTO) {
        log.debug("Request to update Niveau : {}", niveauDTO);

        if (niveauRepository.findByAnneeEtude(niveauDTO.getAnneeEtude()).isPresent()) {
            throw new BadRequestAlertException("A new niveau have an ANNEE_ETUDE exists ", ENTITY_NAME, "Annnee Etude exists");
        }

        if (niveauRepository.findByAnneeEtude(niveauDTO.getCodeNiveau()).isPresent() ) {
            throw new BadRequestAlertException("A udate niveau have an Code Niveau ", ENTITY_NAME, "Code Niveau");
        }

        Niveau niveau = niveauMapper.toEntity(niveauDTO);
        niveau = niveauRepository.save(niveau);
        return niveauMapper.toDto(niveau);
    }

    @Override
    public Optional<NiveauDTO> partialUpdate(NiveauDTO niveauDTO) {
        log.debug("Request to partially update Niveau : {}", niveauDTO);

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
        niveauRepository.deleteById(id);
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
