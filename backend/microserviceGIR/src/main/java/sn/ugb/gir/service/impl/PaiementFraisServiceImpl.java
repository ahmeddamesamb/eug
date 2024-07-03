package sn.ugb.gir.service.impl;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sn.ugb.gir.domain.PaiementFrais;
import sn.ugb.gir.repository.PaiementFraisRepository;
import sn.ugb.gir.repository.search.PaiementFraisSearchRepository;
import sn.ugb.gir.service.PaiementFraisService;
import sn.ugb.gir.service.dto.PaiementFraisDTO;
import sn.ugb.gir.service.mapper.PaiementFraisMapper;

/**
 * Service Implementation for managing {@link sn.ugb.gir.domain.PaiementFrais}.
 */
@Service
@Transactional
public class PaiementFraisServiceImpl implements PaiementFraisService {

    private final Logger log = LoggerFactory.getLogger(PaiementFraisServiceImpl.class);

    private final PaiementFraisRepository paiementFraisRepository;

    private final PaiementFraisMapper paiementFraisMapper;

    private final PaiementFraisSearchRepository paiementFraisSearchRepository;

    public PaiementFraisServiceImpl(
        PaiementFraisRepository paiementFraisRepository,
        PaiementFraisMapper paiementFraisMapper,
        PaiementFraisSearchRepository paiementFraisSearchRepository
    ) {
        this.paiementFraisRepository = paiementFraisRepository;
        this.paiementFraisMapper = paiementFraisMapper;
        this.paiementFraisSearchRepository = paiementFraisSearchRepository;
    }

    @Override
    public PaiementFraisDTO save(PaiementFraisDTO paiementFraisDTO) {
        log.debug("Request to save PaiementFrais : {}", paiementFraisDTO);
        PaiementFrais paiementFrais = paiementFraisMapper.toEntity(paiementFraisDTO);
        paiementFrais = paiementFraisRepository.save(paiementFrais);
        PaiementFraisDTO result = paiementFraisMapper.toDto(paiementFrais);
        paiementFraisSearchRepository.index(paiementFrais);
        return result;
    }

    @Override
    public PaiementFraisDTO  update(PaiementFraisDTO paiementFraisDTO) {
        log.debug("Request to update PaiementFrais : {}", paiementFraisDTO);
        PaiementFrais paiementFrais = paiementFraisMapper.toEntity(paiementFraisDTO);
        paiementFrais = paiementFraisRepository.save(paiementFrais);
        PaiementFraisDTO result = paiementFraisMapper.toDto(paiementFrais);
        paiementFraisSearchRepository.index(paiementFrais);
        return result;
    }

    @Override
    public Optional<PaiementFraisDTO> partialUpdate(PaiementFraisDTO paiementFraisDTO) {
        log.debug("Request to partially update PaiementFrais : {}", paiementFraisDTO);

        return paiementFraisRepository
            .findById(paiementFraisDTO.getId())
            .map(existingPaiementFrais -> {
                paiementFraisMapper.partialUpdate(existingPaiementFrais, paiementFraisDTO);

                return existingPaiementFrais;
            })
            .map(paiementFraisRepository::save)
            .map(savedPaiementFrais -> {
                paiementFraisSearchRepository.index(savedPaiementFrais);
                return savedPaiementFrais;
            })
            .map(paiementFraisMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<PaiementFraisDTO> findAll(Pageable pageable) {
        log.debug("Request to get all PaiementFrais");
        return paiementFraisRepository.findAll(pageable).map(paiementFraisMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<PaiementFraisDTO> findOne(Long id) {
        log.debug("Request to get PaiementFrais : {}", id);
        return paiementFraisRepository.findById(id).map(paiementFraisMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete PaiementFrais : {}", id);
        paiementFraisRepository.deleteById(id);
        paiementFraisSearchRepository.deleteFromIndexById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<PaiementFraisDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of PaiementFrais for query {}", query);
        return paiementFraisSearchRepository.search(query, pageable).map(paiementFraisMapper::toDto);
    }
}
