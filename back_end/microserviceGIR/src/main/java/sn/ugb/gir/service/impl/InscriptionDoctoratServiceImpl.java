package sn.ugb.gir.service.impl;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sn.ugb.gir.domain.InscriptionDoctorat;
import sn.ugb.gir.repository.InscriptionDoctoratRepository;
import sn.ugb.gir.service.InscriptionDoctoratService;
import sn.ugb.gir.service.dto.InscriptionDoctoratDTO;
import sn.ugb.gir.service.mapper.InscriptionDoctoratMapper;

/**
 * Service Implementation for managing {@link sn.ugb.gir.domain.InscriptionDoctorat}.
 */
@Service
@Transactional
public class InscriptionDoctoratServiceImpl implements InscriptionDoctoratService {

    private final Logger log = LoggerFactory.getLogger(InscriptionDoctoratServiceImpl.class);

    private final InscriptionDoctoratRepository inscriptionDoctoratRepository;

    private final InscriptionDoctoratMapper inscriptionDoctoratMapper;

    public InscriptionDoctoratServiceImpl(
        InscriptionDoctoratRepository inscriptionDoctoratRepository,
        InscriptionDoctoratMapper inscriptionDoctoratMapper
    ) {
        this.inscriptionDoctoratRepository = inscriptionDoctoratRepository;
        this.inscriptionDoctoratMapper = inscriptionDoctoratMapper;
    }

    @Override
    public InscriptionDoctoratDTO save(InscriptionDoctoratDTO inscriptionDoctoratDTO) {
        log.debug("Request to save InscriptionDoctorat : {}", inscriptionDoctoratDTO);
        InscriptionDoctorat inscriptionDoctorat = inscriptionDoctoratMapper.toEntity(inscriptionDoctoratDTO);
        inscriptionDoctorat = inscriptionDoctoratRepository.save(inscriptionDoctorat);
        return inscriptionDoctoratMapper.toDto(inscriptionDoctorat);
    }

    @Override
    public InscriptionDoctoratDTO update(InscriptionDoctoratDTO inscriptionDoctoratDTO) {
        log.debug("Request to update InscriptionDoctorat : {}", inscriptionDoctoratDTO);
        InscriptionDoctorat inscriptionDoctorat = inscriptionDoctoratMapper.toEntity(inscriptionDoctoratDTO);
        inscriptionDoctorat = inscriptionDoctoratRepository.save(inscriptionDoctorat);
        return inscriptionDoctoratMapper.toDto(inscriptionDoctorat);
    }

    @Override
    public Optional<InscriptionDoctoratDTO> partialUpdate(InscriptionDoctoratDTO inscriptionDoctoratDTO) {
        log.debug("Request to partially update InscriptionDoctorat : {}", inscriptionDoctoratDTO);

        return inscriptionDoctoratRepository
            .findById(inscriptionDoctoratDTO.getId())
            .map(existingInscriptionDoctorat -> {
                inscriptionDoctoratMapper.partialUpdate(existingInscriptionDoctorat, inscriptionDoctoratDTO);

                return existingInscriptionDoctorat;
            })
            .map(inscriptionDoctoratRepository::save)
            .map(inscriptionDoctoratMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<InscriptionDoctoratDTO> findAll(Pageable pageable) {
        log.debug("Request to get all InscriptionDoctorats");
        return inscriptionDoctoratRepository.findAll(pageable).map(inscriptionDoctoratMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<InscriptionDoctoratDTO> findOne(Long id) {
        log.debug("Request to get InscriptionDoctorat : {}", id);
        return inscriptionDoctoratRepository.findById(id).map(inscriptionDoctoratMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete InscriptionDoctorat : {}", id);
        inscriptionDoctoratRepository.deleteById(id);
    }
}
