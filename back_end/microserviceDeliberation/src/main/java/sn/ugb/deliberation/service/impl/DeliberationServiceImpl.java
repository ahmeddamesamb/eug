package sn.ugb.deliberation.service.impl;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sn.ugb.deliberation.domain.Deliberation;
import sn.ugb.deliberation.repository.DeliberationRepository;
import sn.ugb.deliberation.service.DeliberationService;
import sn.ugb.deliberation.service.dto.DeliberationDTO;
import sn.ugb.deliberation.service.mapper.DeliberationMapper;

/**
 * Service Implementation for managing {@link sn.ugb.deliberation.domain.Deliberation}.
 */
@Service
@Transactional
public class DeliberationServiceImpl implements DeliberationService {

    private final Logger log = LoggerFactory.getLogger(DeliberationServiceImpl.class);

    private final DeliberationRepository deliberationRepository;

    private final DeliberationMapper deliberationMapper;

    public DeliberationServiceImpl(DeliberationRepository deliberationRepository, DeliberationMapper deliberationMapper) {
        this.deliberationRepository = deliberationRepository;
        this.deliberationMapper = deliberationMapper;
    }

    @Override
    public DeliberationDTO save(DeliberationDTO deliberationDTO) {
        log.debug("Request to save Deliberation : {}", deliberationDTO);
        Deliberation deliberation = deliberationMapper.toEntity(deliberationDTO);
        deliberation = deliberationRepository.save(deliberation);
        return deliberationMapper.toDto(deliberation);
    }

    @Override
    public DeliberationDTO update(DeliberationDTO deliberationDTO) {
        log.debug("Request to update Deliberation : {}", deliberationDTO);
        Deliberation deliberation = deliberationMapper.toEntity(deliberationDTO);
        deliberation = deliberationRepository.save(deliberation);
        return deliberationMapper.toDto(deliberation);
    }

    @Override
    public Optional<DeliberationDTO> partialUpdate(DeliberationDTO deliberationDTO) {
        log.debug("Request to partially update Deliberation : {}", deliberationDTO);

        return deliberationRepository
            .findById(deliberationDTO.getId())
            .map(existingDeliberation -> {
                deliberationMapper.partialUpdate(existingDeliberation, deliberationDTO);

                return existingDeliberation;
            })
            .map(deliberationRepository::save)
            .map(deliberationMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<DeliberationDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Deliberations");
        return deliberationRepository.findAll(pageable).map(deliberationMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<DeliberationDTO> findOne(Long id) {
        log.debug("Request to get Deliberation : {}", id);
        return deliberationRepository.findById(id).map(deliberationMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Deliberation : {}", id);
        deliberationRepository.deleteById(id);
    }
}
