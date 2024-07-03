package sn.ugb.gir.service.impl;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sn.ugb.gir.domain.Lycee;
import sn.ugb.gir.repository.LyceeRepository;
import sn.ugb.gir.repository.search.LyceeSearchRepository;
import sn.ugb.gir.service.LyceeService;
import sn.ugb.gir.service.dto.LyceeDTO;
import sn.ugb.gir.service.mapper.LyceeMapper;
import sn.ugb.gir.web.rest.errors.BadRequestAlertException;

import static org.hibernate.id.IdentifierGenerator.ENTITY_NAME;
/**
 * Service Implementation for managing {@link sn.ugb.gir.domain.Lycee}.
 */
@Service
@Transactional
public class LyceeServiceImpl implements LyceeService {

    private final Logger log = LoggerFactory.getLogger(LyceeServiceImpl.class);

    private final LyceeRepository lyceeRepository;

    private final LyceeMapper lyceeMapper;

    private final LyceeSearchRepository lyceeSearchRepository;

    public LyceeServiceImpl(LyceeRepository lyceeRepository, LyceeMapper lyceeMapper, LyceeSearchRepository lyceeSearchRepository) {
        this.lyceeRepository = lyceeRepository;
        this.lyceeMapper = lyceeMapper;
        this.lyceeSearchRepository = lyceeSearchRepository;
    }

    @Override
    public LyceeDTO save(LyceeDTO lyceeDTO) {
        log.debug("Request to save Lycee : {}", lyceeDTO);

        if (lyceeDTO.getId() != null) {
            throw new BadRequestAlertException("A new lycee cannot already have an ID", ENTITY_NAME, "idexists");
        }
        validateData(lyceeDTO);
        Lycee lycee = lyceeMapper.toEntity(lyceeDTO);
        lycee = lyceeRepository.save(lycee);
        LyceeDTO result = lyceeMapper.toDto(lycee);
        lyceeSearchRepository.index(lycee);
        return result;
    }

    @Override
    public LyceeDTO update(LyceeDTO lyceeDTO) {
        log.debug("Request to update Lycee : {}", lyceeDTO);

        validateData(lyceeDTO);
        Lycee lycee = lyceeMapper.toEntity(lyceeDTO);
        lycee = lyceeRepository.save(lycee);
        LyceeDTO result = lyceeMapper.toDto(lycee);
        lyceeSearchRepository.index(lycee);
        return result;
    }

    @Override
    public Optional<LyceeDTO> partialUpdate(LyceeDTO lyceeDTO) {
        log.debug("Request to partially update Lycee : {}", lyceeDTO);

        validateData(lyceeDTO);
        return lyceeRepository
            .findById(lyceeDTO.getId())
            .map(existingLycee -> {
                lyceeMapper.partialUpdate(existingLycee, lyceeDTO);

                return existingLycee;
            })
            .map(lyceeRepository::save)
            .map(savedLycee -> {
                lyceeSearchRepository.index(savedLycee);
                return savedLycee;
            })
            .map(lyceeMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<LyceeDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Lycees");
        return lyceeRepository.findAll(pageable).map(lyceeMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<LyceeDTO> findOne(Long id) {
        log.debug("Request to get Lycee : {}", id);
        return lyceeRepository.findById(id).map(lyceeMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Lycee : {}", id);
        lyceeRepository.deleteById(id);
        lyceeSearchRepository.deleteFromIndexById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<LyceeDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Lycees for query {}", query);
        return lyceeSearchRepository.search(query, pageable).map(lyceeMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<LyceeDTO> findAllByRegionId(Pageable pageable, Long id) {
        log.debug("Request to get all Lycees having an id region");
        return lyceeRepository.findAllByRegionId(pageable,id).map(lyceeMapper::toDto);
    }

    public void validateData(LyceeDTO lyceeDTO){
        if (lyceeDTO.getCodeLycee().isEmpty() || lyceeDTO.getCodeLycee().isBlank()) {
            throw new BadRequestAlertException("Veuillez renseigner le code du lycee", ENTITY_NAME, "codeLyceevide");
        }
        if (lyceeDTO.getNomLycee().isEmpty() || lyceeDTO.getNomLycee().isBlank()) {
            throw new BadRequestAlertException("Veuillez renseigner le nom du lycee ", ENTITY_NAME, "nomLyceevide");
        }
        Optional<Lycee> existingNomLycee = lyceeRepository.findByNomLyceeIgnoreCase( lyceeDTO.getNomLycee());
        if ( existingNomLycee.isPresent() && !existingNomLycee.get().getId().equals(lyceeDTO.getId())){
            throw new BadRequestAlertException("Ce Lycee existe. Deux Lycees ne peuvent pas avoir le même nom", ENTITY_NAME, "nomlyceeexists");
        }
    }
}
