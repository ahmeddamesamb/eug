package sn.ugb.gir.service.impl;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sn.ugb.gir.domain.Frais;
import sn.ugb.gir.domain.enumeration.Cycle;
import sn.ugb.gir.repository.FraisRepository;
import sn.ugb.gir.service.FraisService;
import sn.ugb.gir.service.dto.FraisDTO;
import sn.ugb.gir.service.mapper.FraisMapper;

/**
 * Service Implementation for managing {@link sn.ugb.gir.domain.Frais}.
 */
@Service
@Transactional
public class FraisServiceImpl implements FraisService {

    private final Logger log = LoggerFactory.getLogger(FraisServiceImpl.class);

    private final FraisRepository fraisRepository;

    private final FraisMapper fraisMapper;

    public FraisServiceImpl(FraisRepository fraisRepository, FraisMapper fraisMapper) {
        this.fraisRepository = fraisRepository;
        this.fraisMapper = fraisMapper;
    }

    @Override
    public FraisDTO save(FraisDTO fraisDTO) {
        log.debug("Request to save Frais : {}", fraisDTO);
        Frais frais = fraisMapper.toEntity(fraisDTO);
        frais = fraisRepository.save(frais);
        return fraisMapper.toDto(frais);
    }

    @Override
    public FraisDTO update(FraisDTO fraisDTO) {
        log.debug("Request to update Frais : {}", fraisDTO);
        Frais frais = fraisMapper.toEntity(fraisDTO);
        frais = fraisRepository.save(frais);
        return fraisMapper.toDto(frais);
    }

    @Override
    public Optional<FraisDTO> partialUpdate(FraisDTO fraisDTO) {
        log.debug("Request to partially update Frais : {}", fraisDTO);

        return fraisRepository
            .findById(fraisDTO.getId())
            .map(existingFrais -> {
                fraisMapper.partialUpdate(existingFrais, fraisDTO);

                return existingFrais;
            })
            .map(fraisRepository::save)
            .map(fraisMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<FraisDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Frais");
        return fraisRepository.findAll(pageable).map(fraisMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<FraisDTO> findOne(Long id) {
        log.debug("Request to get Frais : {}", id);
        return fraisRepository.findById(id).map(fraisMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Frais : {}", id);
        fraisRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<FraisDTO> findAllFraisByCycle(Pageable pageable, Cycle cycle) {
        log.debug("Request to get all Frais for a cycle");
        return fraisRepository.findFraisByCycle(pageable,cycle).map(fraisMapper::toDto);
    }

}
