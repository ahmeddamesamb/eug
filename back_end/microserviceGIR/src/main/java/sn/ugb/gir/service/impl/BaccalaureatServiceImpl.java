package sn.ugb.gir.service.impl;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sn.ugb.gir.domain.Baccalaureat;
import sn.ugb.gir.repository.BaccalaureatRepository;
import sn.ugb.gir.service.BaccalaureatService;
import sn.ugb.gir.service.dto.BaccalaureatDTO;
import sn.ugb.gir.service.mapper.BaccalaureatMapper;

/**
 * Service Implementation for managing {@link sn.ugb.gir.domain.Baccalaureat}.
 */
@Service
@Transactional
public class BaccalaureatServiceImpl implements BaccalaureatService {

    private final Logger log = LoggerFactory.getLogger(BaccalaureatServiceImpl.class);

    private final BaccalaureatRepository baccalaureatRepository;

    private final BaccalaureatMapper baccalaureatMapper;

    public BaccalaureatServiceImpl(BaccalaureatRepository baccalaureatRepository, BaccalaureatMapper baccalaureatMapper) {
        this.baccalaureatRepository = baccalaureatRepository;
        this.baccalaureatMapper = baccalaureatMapper;
    }

    @Override
    public BaccalaureatDTO save(BaccalaureatDTO baccalaureatDTO) {
        log.debug("Request to save Baccalaureat : {}", baccalaureatDTO);
        Baccalaureat baccalaureat = baccalaureatMapper.toEntity(baccalaureatDTO);
        baccalaureat = baccalaureatRepository.save(baccalaureat);
        return baccalaureatMapper.toDto(baccalaureat);
    }

    @Override
    public BaccalaureatDTO update(BaccalaureatDTO baccalaureatDTO) {
        log.debug("Request to update Baccalaureat : {}", baccalaureatDTO);
        Baccalaureat baccalaureat = baccalaureatMapper.toEntity(baccalaureatDTO);
        baccalaureat = baccalaureatRepository.save(baccalaureat);
        return baccalaureatMapper.toDto(baccalaureat);
    }

    @Override
    public Optional<BaccalaureatDTO> partialUpdate(BaccalaureatDTO baccalaureatDTO) {
        log.debug("Request to partially update Baccalaureat : {}", baccalaureatDTO);

        return baccalaureatRepository
            .findById(baccalaureatDTO.getId())
            .map(existingBaccalaureat -> {
                baccalaureatMapper.partialUpdate(existingBaccalaureat, baccalaureatDTO);

                return existingBaccalaureat;
            })
            .map(baccalaureatRepository::save)
            .map(baccalaureatMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<BaccalaureatDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Baccalaureats");
        return baccalaureatRepository.findAll(pageable).map(baccalaureatMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<BaccalaureatDTO> findOne(Long id) {
        log.debug("Request to get Baccalaureat : {}", id);
        return baccalaureatRepository.findById(id).map(baccalaureatMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Baccalaureat : {}", id);
        baccalaureatRepository.deleteById(id);
    }
}
