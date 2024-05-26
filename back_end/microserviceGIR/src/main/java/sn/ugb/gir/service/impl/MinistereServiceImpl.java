package sn.ugb.gir.service.impl;

import java.time.LocalDate;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sn.ugb.gir.domain.Ministere;
import sn.ugb.gir.repository.MinistereRepository;
import sn.ugb.gir.service.MinistereService;
import sn.ugb.gir.service.dto.MinistereDTO;
import sn.ugb.gir.service.mapper.MinistereMapper;

/**
 * Service Implementation for managing {@link sn.ugb.gir.domain.Ministere}.
 */
@Service
@Transactional
public class MinistereServiceImpl implements MinistereService {

    private final Logger log = LoggerFactory.getLogger(MinistereServiceImpl.class);

    private final MinistereRepository ministereRepository;

    private final MinistereMapper ministereMapper;

    public MinistereServiceImpl(MinistereRepository ministereRepository, MinistereMapper ministereMapper) {
        this.ministereRepository = ministereRepository;
        this.ministereMapper = ministereMapper;
    }

    @Override
    public MinistereDTO save(MinistereDTO ministereDTO) {
        log.debug("Request to save Ministere : {}", ministereDTO);
        Ministere ministere = ministereMapper.toEntity(ministereDTO);
        ministere = ministereRepository.save(ministere);
        return ministereMapper.toDto(ministere);
    }

    @Override
    public MinistereDTO update(MinistereDTO ministereDTO) {
        log.debug("Request to update Ministere : {}", ministereDTO);
        Ministere ministere = ministereMapper.toEntity(ministereDTO);
        ministere = ministereRepository.save(ministere);
        return ministereMapper.toDto(ministere);
    }

    @Override
    public Optional<MinistereDTO> partialUpdate(MinistereDTO ministereDTO) {
        log.debug("Request to partially update Ministere : {}", ministereDTO);

        return ministereRepository
            .findById(ministereDTO.getId())
            .map(existingMinistere -> {
                ministereMapper.partialUpdate(existingMinistere, ministereDTO);

                return existingMinistere;
            })
            .map(ministereRepository::save)
            .map(ministereMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<MinistereDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Ministeres");
        return ministereRepository.findAll(pageable).map(ministereMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<MinistereDTO> findOne(Long id) {
        log.debug("Request to get Ministere : {}", id);
        return ministereRepository.findById(id).map(ministereMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Ministere : {}", id);
        ministereRepository.deleteById(id);
    }


    /**
     * @return
     */
    @Override
    public Optional<MinistereDTO> findCurrent() {
        Ministere ministereEnCours = ministereRepository.findByEnCoursYN(1)
            .orElse(null);
        return Optional.ofNullable(ministereMapper.toDto(ministereEnCours));
    }

    /**
     * @param startDate
     * @param endDate
     * @param pageable
     * @return
     */
    @Override
    public Page<MinistereDTO> findByPeriode(LocalDate startDate, LocalDate endDate, Pageable pageable) {
        Page<Ministere> ministerePage = ministereRepository.findByPeriode(startDate, endDate, pageable);
        return ministerePage.map(ministereMapper::toDto);
    }
}
