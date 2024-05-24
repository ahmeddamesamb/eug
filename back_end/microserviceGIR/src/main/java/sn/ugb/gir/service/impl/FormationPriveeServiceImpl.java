package sn.ugb.gir.service.impl;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sn.ugb.gir.domain.FormationPrivee;
import sn.ugb.gir.repository.FormationPriveeRepository;
import sn.ugb.gir.service.FormationPriveeService;
import sn.ugb.gir.service.dto.FormationPriveeDTO;
import sn.ugb.gir.service.mapper.FormationPriveeMapper;

/**
 * Service Implementation for managing {@link sn.ugb.gir.domain.FormationPrivee}.
 */
@Service
@Transactional
public class FormationPriveeServiceImpl implements FormationPriveeService {

    private final Logger log = LoggerFactory.getLogger(FormationPriveeServiceImpl.class);

    private final FormationPriveeRepository formationPriveeRepository;

    private final FormationPriveeMapper formationPriveeMapper;

    public FormationPriveeServiceImpl(FormationPriveeRepository formationPriveeRepository, FormationPriveeMapper formationPriveeMapper) {
        this.formationPriveeRepository = formationPriveeRepository;
        this.formationPriveeMapper = formationPriveeMapper;
    }

    @Override
    public FormationPriveeDTO save(FormationPriveeDTO formationPriveeDTO) {
        log.debug("Request to save FormationPrivee : {}", formationPriveeDTO);
        FormationPrivee formationPrivee = formationPriveeMapper.toEntity(formationPriveeDTO);
        formationPrivee = formationPriveeRepository.save(formationPrivee);
        return formationPriveeMapper.toDto(formationPrivee);
    }

    @Override
    public FormationPriveeDTO update(FormationPriveeDTO formationPriveeDTO) {
        log.debug("Request to update FormationPrivee : {}", formationPriveeDTO);
        FormationPrivee formationPrivee = formationPriveeMapper.toEntity(formationPriveeDTO);
        formationPrivee = formationPriveeRepository.save(formationPrivee);
        return formationPriveeMapper.toDto(formationPrivee);
    }

    @Override
    public Optional<FormationPriveeDTO> partialUpdate(FormationPriveeDTO formationPriveeDTO) {
        log.debug("Request to partially update FormationPrivee : {}", formationPriveeDTO);

        return formationPriveeRepository
            .findById(formationPriveeDTO.getId())
            .map(existingFormationPrivee -> {
                formationPriveeMapper.partialUpdate(existingFormationPrivee, formationPriveeDTO);

                return existingFormationPrivee;
            })
            .map(formationPriveeRepository::save)
            .map(formationPriveeMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<FormationPriveeDTO> findAll(Pageable pageable) {
        log.debug("Request to get all FormationPrivees");
        return formationPriveeRepository.findAll(pageable).map(formationPriveeMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<FormationPriveeDTO> findOne(Long id) {
        log.debug("Request to get FormationPrivee : {}", id);
        return formationPriveeRepository.findById(id).map(formationPriveeMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete FormationPrivee : {}", id);
        formationPriveeRepository.deleteById(id);
    }
}
