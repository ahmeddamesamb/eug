package sn.ugb.gir.service.impl;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sn.ugb.gir.domain.PaiementFormationPrivee;
import sn.ugb.gir.repository.PaiementFormationPriveeRepository;
import sn.ugb.gir.service.PaiementFormationPriveeService;
import sn.ugb.gir.service.dto.PaiementFormationPriveeDTO;
import sn.ugb.gir.service.mapper.PaiementFormationPriveeMapper;

/**
 * Service Implementation for managing {@link sn.ugb.gir.domain.PaiementFormationPrivee}.
 */
@Service
@Transactional
public class PaiementFormationPriveeServiceImpl implements PaiementFormationPriveeService {

    private final Logger log = LoggerFactory.getLogger(PaiementFormationPriveeServiceImpl.class);

    private final PaiementFormationPriveeRepository paiementFormationPriveeRepository;

    private final PaiementFormationPriveeMapper paiementFormationPriveeMapper;

    public PaiementFormationPriveeServiceImpl(
        PaiementFormationPriveeRepository paiementFormationPriveeRepository,
        PaiementFormationPriveeMapper paiementFormationPriveeMapper
    ) {
        this.paiementFormationPriveeRepository = paiementFormationPriveeRepository;
        this.paiementFormationPriveeMapper = paiementFormationPriveeMapper;
    }

    @Override
    public PaiementFormationPriveeDTO save(PaiementFormationPriveeDTO paiementFormationPriveeDTO) {
        log.debug("Request to save PaiementFormationPrivee : {}", paiementFormationPriveeDTO);
        PaiementFormationPrivee paiementFormationPrivee = paiementFormationPriveeMapper.toEntity(paiementFormationPriveeDTO);
        paiementFormationPrivee = paiementFormationPriveeRepository.save(paiementFormationPrivee);
        return paiementFormationPriveeMapper.toDto(paiementFormationPrivee);
    }

    @Override
    public PaiementFormationPriveeDTO update(PaiementFormationPriveeDTO paiementFormationPriveeDTO) {
        log.debug("Request to update PaiementFormationPrivee : {}", paiementFormationPriveeDTO);
        PaiementFormationPrivee paiementFormationPrivee = paiementFormationPriveeMapper.toEntity(paiementFormationPriveeDTO);
        paiementFormationPrivee = paiementFormationPriveeRepository.save(paiementFormationPrivee);
        return paiementFormationPriveeMapper.toDto(paiementFormationPrivee);
    }

    @Override
    public Optional<PaiementFormationPriveeDTO> partialUpdate(PaiementFormationPriveeDTO paiementFormationPriveeDTO) {
        log.debug("Request to partially update PaiementFormationPrivee : {}", paiementFormationPriveeDTO);

        return paiementFormationPriveeRepository
            .findById(paiementFormationPriveeDTO.getId())
            .map(existingPaiementFormationPrivee -> {
                paiementFormationPriveeMapper.partialUpdate(existingPaiementFormationPrivee, paiementFormationPriveeDTO);

                return existingPaiementFormationPrivee;
            })
            .map(paiementFormationPriveeRepository::save)
            .map(paiementFormationPriveeMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<PaiementFormationPriveeDTO> findAll(Pageable pageable) {
        log.debug("Request to get all PaiementFormationPrivees");
        return paiementFormationPriveeRepository.findAll(pageable).map(paiementFormationPriveeMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<PaiementFormationPriveeDTO> findOne(Long id) {
        log.debug("Request to get PaiementFormationPrivee : {}", id);
        return paiementFormationPriveeRepository.findById(id).map(paiementFormationPriveeMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete PaiementFormationPrivee : {}", id);
        paiementFormationPriveeRepository.deleteById(id);
    }
}
