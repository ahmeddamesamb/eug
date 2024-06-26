package sn.ugb.gir.service.impl;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sn.ugb.gir.domain.Departement;
import sn.ugb.gir.repository.DepartementRepository;
import sn.ugb.gir.repository.search.DepartementSearchRepository;
import sn.ugb.gir.service.DepartementService;
import sn.ugb.gir.service.dto.DepartementDTO;
import sn.ugb.gir.service.mapper.DepartementMapper;

/**
 * Service Implementation for managing {@link sn.ugb.gir.domain.Departement}.
 */
@Service
@Transactional
public class DepartementServiceImpl implements DepartementService {

    private final Logger log = LoggerFactory.getLogger(DepartementServiceImpl.class);

    private final DepartementRepository departementRepository;

    private final DepartementMapper departementMapper;

    private final DepartementSearchRepository departementSearchRepository;

    public DepartementServiceImpl(
        DepartementRepository departementRepository,
        DepartementMapper departementMapper,
        DepartementSearchRepository departementSearchRepository
    ) {
        this.departementRepository = departementRepository;
        this.departementMapper = departementMapper;
        this.departementSearchRepository = departementSearchRepository;
    }

    @Override
    public DepartementDTO save(DepartementDTO departementDTO) {
        log.debug("Request to save Departement : {}", departementDTO);
        Departement departement = departementMapper.toEntity(departementDTO);
        departement = departementRepository.save(departement);
        DepartementDTO result = departementMapper.toDto(departement);
        departementSearchRepository.index(departement);
        return result;
    }

    @Override
    public DepartementDTO update(DepartementDTO departementDTO) {
        log.debug("Request to update Departement : {}", departementDTO);
        Departement departement = departementMapper.toEntity(departementDTO);
        departement = departementRepository.save(departement);
        DepartementDTO result = departementMapper.toDto(departement);
        departementSearchRepository.index(departement);
        return result;
    }

    @Override
    public Optional<DepartementDTO> partialUpdate(DepartementDTO departementDTO) {
        log.debug("Request to partially update Departement : {}", departementDTO);

        return departementRepository
            .findById(departementDTO.getId())
            .map(existingDepartement -> {
                departementMapper.partialUpdate(existingDepartement, departementDTO);

                return existingDepartement;
            })
            .map(departementRepository::save)
            .map(savedDepartement -> {
                departementSearchRepository.index(savedDepartement);
                return savedDepartement;
            })
            .map(departementMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<DepartementDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Departements");
        return departementRepository.findAll(pageable).map(departementMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<DepartementDTO> findOne(Long id) {
        log.debug("Request to get Departement : {}", id);
        return departementRepository.findById(id).map(departementMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Departement : {}", id);
        departementRepository.deleteById(id);
        departementSearchRepository.deleteFromIndexById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<DepartementDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Departements for query {}", query);
        return departementSearchRepository.search(query, pageable).map(departementMapper::toDto);
    }
}
