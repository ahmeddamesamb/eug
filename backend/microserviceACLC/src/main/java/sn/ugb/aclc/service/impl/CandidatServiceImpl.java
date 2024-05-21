package sn.ugb.aclc.service.impl;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sn.ugb.aclc.domain.Candidat;
import sn.ugb.aclc.repository.CandidatRepository;
import sn.ugb.aclc.service.CandidatService;
import sn.ugb.aclc.service.dto.CandidatDTO;
import sn.ugb.aclc.service.mapper.CandidatMapper;

/**
 * Service Implementation for managing {@link sn.ugb.aclc.domain.Candidat}.
 */
@Service
@Transactional
public class CandidatServiceImpl implements CandidatService {

    private final Logger log = LoggerFactory.getLogger(CandidatServiceImpl.class);

    private final CandidatRepository candidatRepository;

    private final CandidatMapper candidatMapper;

    public CandidatServiceImpl(CandidatRepository candidatRepository, CandidatMapper candidatMapper) {
        this.candidatRepository = candidatRepository;
        this.candidatMapper = candidatMapper;
    }

    @Override
    public CandidatDTO save(CandidatDTO candidatDTO) {
        log.debug("Request to save Candidat : {}", candidatDTO);
        Candidat candidat = candidatMapper.toEntity(candidatDTO);
        candidat = candidatRepository.save(candidat);
        return candidatMapper.toDto(candidat);
    }

    @Override
    public CandidatDTO update(CandidatDTO candidatDTO) {
        log.debug("Request to update Candidat : {}", candidatDTO);
        Candidat candidat = candidatMapper.toEntity(candidatDTO);
        candidat = candidatRepository.save(candidat);
        return candidatMapper.toDto(candidat);
    }

    @Override
    public Optional<CandidatDTO> partialUpdate(CandidatDTO candidatDTO) {
        log.debug("Request to partially update Candidat : {}", candidatDTO);

        return candidatRepository
            .findById(candidatDTO.getId())
            .map(existingCandidat -> {
                candidatMapper.partialUpdate(existingCandidat, candidatDTO);

                return existingCandidat;
            })
            .map(candidatRepository::save)
            .map(candidatMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<CandidatDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Candidats");
        return candidatRepository.findAll(pageable).map(candidatMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<CandidatDTO> findOne(Long id) {
        log.debug("Request to get Candidat : {}", id);
        return candidatRepository.findById(id).map(candidatMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Candidat : {}", id);
        candidatRepository.deleteById(id);
    }
}
