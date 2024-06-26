package sn.ugb.gir.service.impl;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sn.ugb.gir.domain.FormationInvalide;
import sn.ugb.gir.repository.FormationInvalideRepository;
import sn.ugb.gir.repository.search.FormationInvalideSearchRepository;
import sn.ugb.gir.service.FormationInvalideService;
import sn.ugb.gir.service.dto.FormationInvalideDTO;
import sn.ugb.gir.service.mapper.FormationInvalideMapper;

/**
 * Service Implementation for managing {@link sn.ugb.gir.domain.FormationInvalide}.
 */
@Service
@Transactional
public class FormationInvalideServiceImpl implements FormationInvalideService {

    private final Logger log = LoggerFactory.getLogger(FormationInvalideServiceImpl.class);

    private final FormationInvalideRepository formationInvalideRepository;

    private final FormationInvalideMapper formationInvalideMapper;

    private final FormationInvalideSearchRepository formationInvalideSearchRepository;

    public FormationInvalideServiceImpl(
        FormationInvalideRepository formationInvalideRepository,
        FormationInvalideMapper formationInvalideMapper,
        FormationInvalideSearchRepository formationInvalideSearchRepository
    ) {
        this.formationInvalideRepository = formationInvalideRepository;
        this.formationInvalideMapper = formationInvalideMapper;
        this.formationInvalideSearchRepository = formationInvalideSearchRepository;
    }

    @Override
    public FormationInvalideDTO save(FormationInvalideDTO formationInvalideDTO) {
        log.debug("Request to save FormationInvalide : {}", formationInvalideDTO);
        FormationInvalide formationInvalide = formationInvalideMapper.toEntity(formationInvalideDTO);
        formationInvalide = formationInvalideRepository.save(formationInvalide);
        FormationInvalideDTO result = formationInvalideMapper.toDto(formationInvalide);
        formationInvalideSearchRepository.index(formationInvalide);
        return result;
    }

    @Override
    public FormationInvalideDTO update(FormationInvalideDTO formationInvalideDTO) {
        log.debug("Request to update FormationInvalide : {}", formationInvalideDTO);
        FormationInvalide formationInvalide = formationInvalideMapper.toEntity(formationInvalideDTO);
        formationInvalide = formationInvalideRepository.save(formationInvalide);
        FormationInvalideDTO result = formationInvalideMapper.toDto(formationInvalide);
        formationInvalideSearchRepository.index(formationInvalide);
        return result;
    }

    @Override
    public Optional<FormationInvalideDTO> partialUpdate(FormationInvalideDTO formationInvalideDTO) {
        log.debug("Request to partially update FormationInvalide : {}", formationInvalideDTO);

        return formationInvalideRepository
            .findById(formationInvalideDTO.getId())
            .map(existingFormationInvalide -> {
                formationInvalideMapper.partialUpdate(existingFormationInvalide, formationInvalideDTO);

                return existingFormationInvalide;
            })
            .map(formationInvalideRepository::save)
            .map(savedFormationInvalide -> {
                formationInvalideSearchRepository.index(savedFormationInvalide);
                return savedFormationInvalide;
            })
            .map(formationInvalideMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<FormationInvalideDTO> findAll(Pageable pageable) {
        log.debug("Request to get all FormationInvalides");
        return formationInvalideRepository.findAll(pageable).map(formationInvalideMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<FormationInvalideDTO> findOne(Long id) {
        log.debug("Request to get FormationInvalide : {}", id);
        return formationInvalideRepository.findById(id).map(formationInvalideMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete FormationInvalide : {}", id);
        formationInvalideRepository.deleteById(id);
        formationInvalideSearchRepository.deleteFromIndexById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<FormationInvalideDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of FormationInvalides for query {}", query);
        return formationInvalideSearchRepository.search(query, pageable).map(formationInvalideMapper::toDto);
    }
}
