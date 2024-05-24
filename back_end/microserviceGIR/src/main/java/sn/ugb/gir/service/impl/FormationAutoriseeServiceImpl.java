package sn.ugb.gir.service.impl;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sn.ugb.gir.domain.FormationAutorisee;
import sn.ugb.gir.repository.FormationAutoriseeRepository;
import sn.ugb.gir.service.FormationAutoriseeService;
import sn.ugb.gir.service.dto.FormationAutoriseeDTO;
import sn.ugb.gir.service.mapper.FormationAutoriseeMapper;

/**
 * Service Implementation for managing {@link sn.ugb.gir.domain.FormationAutorisee}.
 */
@Service
@Transactional
public class FormationAutoriseeServiceImpl implements FormationAutoriseeService {

    private final Logger log = LoggerFactory.getLogger(FormationAutoriseeServiceImpl.class);

    private final FormationAutoriseeRepository formationAutoriseeRepository;

    private final FormationAutoriseeMapper formationAutoriseeMapper;

    public FormationAutoriseeServiceImpl(
        FormationAutoriseeRepository formationAutoriseeRepository,
        FormationAutoriseeMapper formationAutoriseeMapper
    ) {
        this.formationAutoriseeRepository = formationAutoriseeRepository;
        this.formationAutoriseeMapper = formationAutoriseeMapper;
    }

    @Override
    public FormationAutoriseeDTO save(FormationAutoriseeDTO formationAutoriseeDTO) {
        log.debug("Request to save FormationAutorisee : {}", formationAutoriseeDTO);
        FormationAutorisee formationAutorisee = formationAutoriseeMapper.toEntity(formationAutoriseeDTO);
        formationAutorisee = formationAutoriseeRepository.save(formationAutorisee);
        return formationAutoriseeMapper.toDto(formationAutorisee);
    }

    @Override
    public FormationAutoriseeDTO update(FormationAutoriseeDTO formationAutoriseeDTO) {
        log.debug("Request to update FormationAutorisee : {}", formationAutoriseeDTO);
        FormationAutorisee formationAutorisee = formationAutoriseeMapper.toEntity(formationAutoriseeDTO);
        formationAutorisee = formationAutoriseeRepository.save(formationAutorisee);
        return formationAutoriseeMapper.toDto(formationAutorisee);
    }

    @Override
    public Optional<FormationAutoriseeDTO> partialUpdate(FormationAutoriseeDTO formationAutoriseeDTO) {
        log.debug("Request to partially update FormationAutorisee : {}", formationAutoriseeDTO);

        return formationAutoriseeRepository
            .findById(formationAutoriseeDTO.getId())
            .map(existingFormationAutorisee -> {
                formationAutoriseeMapper.partialUpdate(existingFormationAutorisee, formationAutoriseeDTO);

                return existingFormationAutorisee;
            })
            .map(formationAutoriseeRepository::save)
            .map(formationAutoriseeMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<FormationAutoriseeDTO> findAll(Pageable pageable) {
        log.debug("Request to get all FormationAutorisees");
        return formationAutoriseeRepository.findAll(pageable).map(formationAutoriseeMapper::toDto);
    }

    public Page<FormationAutoriseeDTO> findAllWithEagerRelationships(Pageable pageable) {
        return formationAutoriseeRepository.findAllWithEagerRelationships(pageable).map(formationAutoriseeMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<FormationAutoriseeDTO> findOne(Long id) {
        log.debug("Request to get FormationAutorisee : {}", id);
        return formationAutoriseeRepository.findOneWithEagerRelationships(id).map(formationAutoriseeMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete FormationAutorisee : {}", id);
        formationAutoriseeRepository.deleteById(id);
    }
}
