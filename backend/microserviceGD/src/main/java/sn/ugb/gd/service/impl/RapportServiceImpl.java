package sn.ugb.gd.service.impl;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sn.ugb.gd.domain.Rapport;
import sn.ugb.gd.repository.RapportRepository;
import sn.ugb.gd.repository.search.RapportSearchRepository;
import sn.ugb.gd.service.RapportService;
import sn.ugb.gd.service.dto.RapportDTO;
import sn.ugb.gd.service.mapper.RapportMapper;

/**
 * Service Implementation for managing {@link sn.ugb.gd.domain.Rapport}.
 */
@Service
@Transactional
public class RapportServiceImpl implements RapportService {

    private final Logger log = LoggerFactory.getLogger(RapportServiceImpl.class);

    private final RapportRepository rapportRepository;

    private final RapportMapper rapportMapper;

    private final RapportSearchRepository rapportSearchRepository;

    public RapportServiceImpl(
        RapportRepository rapportRepository,
        RapportMapper rapportMapper,
        RapportSearchRepository rapportSearchRepository
    ) {
        this.rapportRepository = rapportRepository;
        this.rapportMapper = rapportMapper;
        this.rapportSearchRepository = rapportSearchRepository;
    }

    @Override
    public RapportDTO save(RapportDTO rapportDTO) {
        log.debug("Request to save Rapport : {}", rapportDTO);
        Rapport rapport = rapportMapper.toEntity(rapportDTO);
        rapport = rapportRepository.save(rapport);
        RapportDTO result = rapportMapper.toDto(rapport);
        rapportSearchRepository.index(rapport);
        return result;
    }

    @Override
    public RapportDTO update(RapportDTO rapportDTO) {
        log.debug("Request to update Rapport : {}", rapportDTO);
        Rapport rapport = rapportMapper.toEntity(rapportDTO);
        rapport = rapportRepository.save(rapport);
        RapportDTO result = rapportMapper.toDto(rapport);
        rapportSearchRepository.index(rapport);
        return result;
    }

    @Override
    public Optional<RapportDTO> partialUpdate(RapportDTO rapportDTO) {
        log.debug("Request to partially update Rapport : {}", rapportDTO);

        return rapportRepository
            .findById(rapportDTO.getId())
            .map(existingRapport -> {
                rapportMapper.partialUpdate(existingRapport, rapportDTO);

                return existingRapport;
            })
            .map(rapportRepository::save)
            .map(savedRapport -> {
                rapportSearchRepository.index(savedRapport);
                return savedRapport;
            })
            .map(rapportMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<RapportDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Rapports");
        return rapportRepository.findAll(pageable).map(rapportMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<RapportDTO> findOne(Long id) {
        log.debug("Request to get Rapport : {}", id);
        return rapportRepository.findById(id).map(rapportMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Rapport : {}", id);
        rapportRepository.deleteById(id);
        rapportSearchRepository.deleteFromIndexById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<RapportDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Rapports for query {}", query);
        return rapportSearchRepository.search(query, pageable).map(rapportMapper::toDto);
    }
}
