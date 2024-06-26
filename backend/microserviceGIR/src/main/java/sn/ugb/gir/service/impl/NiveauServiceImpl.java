package sn.ugb.gir.service.impl;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sn.ugb.gir.domain.Niveau;
import sn.ugb.gir.repository.NiveauRepository;
import sn.ugb.gir.repository.search.NiveauSearchRepository;
import sn.ugb.gir.service.NiveauService;
import sn.ugb.gir.service.dto.NiveauDTO;
import sn.ugb.gir.service.mapper.NiveauMapper;

/**
 * Service Implementation for managing {@link sn.ugb.gir.domain.Niveau}.
 */
@Service
@Transactional
public class NiveauServiceImpl implements NiveauService {

    private final Logger log = LoggerFactory.getLogger(NiveauServiceImpl.class);

    private final NiveauRepository niveauRepository;

    private final NiveauMapper niveauMapper;

    private final NiveauSearchRepository niveauSearchRepository;

    public NiveauServiceImpl(NiveauRepository niveauRepository, NiveauMapper niveauMapper, NiveauSearchRepository niveauSearchRepository) {
        this.niveauRepository = niveauRepository;
        this.niveauMapper = niveauMapper;
        this.niveauSearchRepository = niveauSearchRepository;
    }

    @Override
    public NiveauDTO save(NiveauDTO niveauDTO) {
        log.debug("Request to save Niveau : {}", niveauDTO);
        Niveau niveau = niveauMapper.toEntity(niveauDTO);
        niveau = niveauRepository.save(niveau);
        NiveauDTO result = niveauMapper.toDto(niveau);
        niveauSearchRepository.index(niveau);
        return result;
    }

    @Override
    public NiveauDTO update(NiveauDTO niveauDTO) {
        log.debug("Request to update Niveau : {}", niveauDTO);
        Niveau niveau = niveauMapper.toEntity(niveauDTO);
        niveau = niveauRepository.save(niveau);
        NiveauDTO result = niveauMapper.toDto(niveau);
        niveauSearchRepository.index(niveau);
        return result;
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
            .map(savedNiveau -> {
                niveauSearchRepository.index(savedNiveau);
                return savedNiveau;
            })
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
        niveauSearchRepository.deleteFromIndexById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<NiveauDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Niveaus for query {}", query);
        return niveauSearchRepository.search(query, pageable).map(niveauMapper::toDto);
    }
}
