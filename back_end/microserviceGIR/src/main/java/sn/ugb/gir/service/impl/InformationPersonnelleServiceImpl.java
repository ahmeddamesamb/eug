package sn.ugb.gir.service.impl;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sn.ugb.gir.domain.InformationPersonnelle;
import sn.ugb.gir.repository.InformationPersonnelleRepository;
import sn.ugb.gir.service.InformationPersonnelleService;
import sn.ugb.gir.service.dto.InformationPersonnelleDTO;
import sn.ugb.gir.service.mapper.InformationPersonnelleMapper;

/**
 * Service Implementation for managing {@link sn.ugb.gir.domain.InformationPersonnelle}.
 */
@Service
@Transactional
public class InformationPersonnelleServiceImpl implements InformationPersonnelleService {

    private final Logger log = LoggerFactory.getLogger(InformationPersonnelleServiceImpl.class);

    private final InformationPersonnelleRepository informationPersonnelleRepository;

    private final InformationPersonnelleMapper informationPersonnelleMapper;

    public InformationPersonnelleServiceImpl(
        InformationPersonnelleRepository informationPersonnelleRepository,
        InformationPersonnelleMapper informationPersonnelleMapper
    ) {
        this.informationPersonnelleRepository = informationPersonnelleRepository;
        this.informationPersonnelleMapper = informationPersonnelleMapper;
    }

    @Override
    public InformationPersonnelleDTO save(InformationPersonnelleDTO informationPersonnelleDTO) {
        log.debug("Request to save InformationPersonnelle : {}", informationPersonnelleDTO);
        InformationPersonnelle informationPersonnelle = informationPersonnelleMapper.toEntity(informationPersonnelleDTO);
        informationPersonnelle = informationPersonnelleRepository.save(informationPersonnelle);
        return informationPersonnelleMapper.toDto(informationPersonnelle);
    }

    @Override
    public InformationPersonnelleDTO update(InformationPersonnelleDTO informationPersonnelleDTO) {
        log.debug("Request to update InformationPersonnelle : {}", informationPersonnelleDTO);
        InformationPersonnelle informationPersonnelle = informationPersonnelleMapper.toEntity(informationPersonnelleDTO);
        informationPersonnelle = informationPersonnelleRepository.save(informationPersonnelle);
        return informationPersonnelleMapper.toDto(informationPersonnelle);
    }

    @Override
    public Optional<InformationPersonnelleDTO> partialUpdate(InformationPersonnelleDTO informationPersonnelleDTO) {
        log.debug("Request to partially update InformationPersonnelle : {}", informationPersonnelleDTO);

        return informationPersonnelleRepository
            .findById(informationPersonnelleDTO.getId())
            .map(existingInformationPersonnelle -> {
                informationPersonnelleMapper.partialUpdate(existingInformationPersonnelle, informationPersonnelleDTO);

                return existingInformationPersonnelle;
            })
            .map(informationPersonnelleRepository::save)
            .map(informationPersonnelleMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<InformationPersonnelleDTO> findAll(Pageable pageable) {
        log.debug("Request to get all InformationPersonnelles");
        return informationPersonnelleRepository.findAll(pageable).map(informationPersonnelleMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<InformationPersonnelleDTO> findOne(Long id) {
        log.debug("Request to get InformationPersonnelle : {}", id);
        return informationPersonnelleRepository.findById(id).map(informationPersonnelleMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete InformationPersonnelle : {}", id);
        informationPersonnelleRepository.deleteById(id);
    }
}
