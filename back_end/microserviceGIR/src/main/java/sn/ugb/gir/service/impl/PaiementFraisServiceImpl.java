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

    public PaiementFraisServiceImpl(PaiementFraisRepository paiementFraisRepository, PaiementFraisMapper paiementFraisMapper) {
        this.paiementFraisRepository = paiementFraisRepository;
        this.paiementFraisMapper = paiementFraisMapper;
    }

    @Override
    public PaiementFraisDTO save(PaiementFraisDTO paiementFraisDTO) {
        log.debug("Request to save PaiementFrais : {}", paiementFraisDTO);
        PaiementFrais paiementFrais = paiementFraisMapper.toEntity(paiementFraisDTO);
        paiementFrais = paiementFraisRepository.save(paiementFrais);
        return paiementFraisMapper.toDto(paiementFrais);
    }

    @Override
    public PaiementFraisDTO update(PaiementFraisDTO paiementFraisDTO) {
        log.debug("Request to update PaiementFrais : {}", paiementFraisDTO);
        PaiementFrais paiementFrais = paiementFraisMapper.toEntity(paiementFraisDTO);
        paiementFrais = paiementFraisRepository.save(paiementFrais);
        return paiementFraisMapper.toDto(paiementFrais);
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
    }
}
