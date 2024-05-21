package sn.ugb.gir.service.impl;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sn.ugb.gir.domain.FormationValide;
import sn.ugb.gir.repository.FormationValideRepository;
import sn.ugb.gir.service.FormationValideService;
import sn.ugb.gir.service.dto.FormationValideDTO;
import sn.ugb.gir.service.mapper.FormationValideMapper;

/**
 * Service Implementation for managing {@link sn.ugb.gir.domain.FormationValide}.
 */
@Service
@Transactional
public class FormationValideServiceImpl implements FormationValideService {

    private final Logger log = LoggerFactory.getLogger(FormationValideServiceImpl.class);

    private final FormationValideRepository formationValideRepository;

    private final FormationValideMapper formationValideMapper;

    public FormationValideServiceImpl(FormationValideRepository formationValideRepository, FormationValideMapper formationValideMapper) {
        this.formationValideRepository = formationValideRepository;
        this.formationValideMapper = formationValideMapper;
    }

    @Override
    public FormationValideDTO save(FormationValideDTO formationValideDTO) {
        log.debug("Request to save FormationValide : {}", formationValideDTO);
        FormationValide formationValide = formationValideMapper.toEntity(formationValideDTO);
        formationValide = formationValideRepository.save(formationValide);
        return formationValideMapper.toDto(formationValide);
    }

    @Override
    public FormationValideDTO update(FormationValideDTO formationValideDTO) {
        log.debug("Request to update FormationValide : {}", formationValideDTO);
        FormationValide formationValide = formationValideMapper.toEntity(formationValideDTO);
        formationValide = formationValideRepository.save(formationValide);
        return formationValideMapper.toDto(formationValide);
    }

    @Override
    public Optional<FormationValideDTO> partialUpdate(FormationValideDTO formationValideDTO) {
        log.debug("Request to partially update FormationValide : {}", formationValideDTO);

        return formationValideRepository
            .findById(formationValideDTO.getId())
            .map(existingFormationValide -> {
                formationValideMapper.partialUpdate(existingFormationValide, formationValideDTO);

                return existingFormationValide;
            })
            .map(formationValideRepository::save)
            .map(formationValideMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<FormationValideDTO> findAll(Pageable pageable) {
        log.debug("Request to get all FormationValides");
        return formationValideRepository.findAll(pageable).map(formationValideMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<FormationValideDTO> findOne(Long id) {
        log.debug("Request to get FormationValide : {}", id);
        return formationValideRepository.findById(id).map(formationValideMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete FormationValide : {}", id);
        formationValideRepository.deleteById(id);
    }
}
