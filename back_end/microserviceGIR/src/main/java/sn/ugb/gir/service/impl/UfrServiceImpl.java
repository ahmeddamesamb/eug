package sn.ugb.gir.service.impl;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sn.ugb.gir.domain.Ufr;
import sn.ugb.gir.repository.UfrRepository;
import sn.ugb.gir.service.UfrService;
import sn.ugb.gir.service.dto.UfrDTO;
import sn.ugb.gir.service.mapper.UfrMapper;

/**
 * Service Implementation for managing {@link sn.ugb.gir.domain.Ufr}.
 */
@Service
@Transactional
public class UfrServiceImpl implements UfrService {

    private final Logger log = LoggerFactory.getLogger(UfrServiceImpl.class);

    private final UfrRepository ufrRepository;

    private final UfrMapper ufrMapper;

    public UfrServiceImpl(UfrRepository ufrRepository, UfrMapper ufrMapper) {
        this.ufrRepository = ufrRepository;
        this.ufrMapper = ufrMapper;
    }

    @Override
    public UfrDTO save(UfrDTO ufrDTO) {
        log.debug("Request to save Ufr : {}", ufrDTO);
        Ufr ufr = ufrMapper.toEntity(ufrDTO);
        ufr = ufrRepository.save(ufr);
        return ufrMapper.toDto(ufr);
    }

    @Override
    public UfrDTO update(UfrDTO ufrDTO) {
        log.debug("Request to update Ufr : {}", ufrDTO);
        Ufr ufr = ufrMapper.toEntity(ufrDTO);
        ufr = ufrRepository.save(ufr);
        return ufrMapper.toDto(ufr);
    }

    @Override
    public Optional<UfrDTO> partialUpdate(UfrDTO ufrDTO) {
        log.debug("Request to partially update Ufr : {}", ufrDTO);

        return ufrRepository
            .findById(ufrDTO.getId())
            .map(existingUfr -> {
                ufrMapper.partialUpdate(existingUfr, ufrDTO);

                return existingUfr;
            })
            .map(ufrRepository::save)
            .map(ufrMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<UfrDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Ufrs");
        return ufrRepository.findAll(pageable).map(ufrMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<UfrDTO> findOne(Long id) {
        log.debug("Request to get Ufr : {}", id);
        return ufrRepository.findById(id).map(ufrMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Ufr : {}", id);
        ufrRepository.deleteById(id);
    }
}
