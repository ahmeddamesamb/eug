package sn.ugb.gir.service.impl;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sn.ugb.gir.domain.Domaine;
import sn.ugb.gir.repository.DomaineRepository;
import sn.ugb.gir.repository.search.DomaineSearchRepository;
import sn.ugb.gir.service.DomaineService;
import sn.ugb.gir.service.dto.DomaineDTO;
import sn.ugb.gir.service.mapper.DomaineMapper;

/**
 * Service Implementation for managing {@link sn.ugb.gir.domain.Domaine}.
 */
@Service
@Transactional
public class DomaineServiceImpl implements DomaineService {

    private final Logger log = LoggerFactory.getLogger(DomaineServiceImpl.class);

    private final DomaineRepository domaineRepository;

    private final DomaineMapper domaineMapper;

    private final DomaineSearchRepository domaineSearchRepository;

    public DomaineServiceImpl(
        DomaineRepository domaineRepository,
        DomaineMapper domaineMapper,
        DomaineSearchRepository domaineSearchRepository
    ) {
        this.domaineRepository = domaineRepository;
        this.domaineMapper = domaineMapper;
        this.domaineSearchRepository = domaineSearchRepository;
    }

    @Override
    public DomaineDTO save(DomaineDTO domaineDTO) {
        log.debug("Request to save Domaine : {}", domaineDTO);
        Domaine domaine = domaineMapper.toEntity(domaineDTO);
        domaine = domaineRepository.save(domaine);
        DomaineDTO result = domaineMapper.toDto(domaine);
        domaineSearchRepository.index(domaine);
        return result;
    }

    @Override
    public DomaineDTO update(DomaineDTO domaineDTO) {
        log.debug("Request to update Domaine : {}", domaineDTO);
        Domaine domaine = domaineMapper.toEntity(domaineDTO);
        domaine = domaineRepository.save(domaine);
        DomaineDTO result = domaineMapper.toDto(domaine);
        domaineSearchRepository.index(domaine);
        return result;
    }

    @Override
    public Optional<DomaineDTO> partialUpdate(DomaineDTO domaineDTO) {
        log.debug("Request to partially update Domaine : {}", domaineDTO);

        return domaineRepository
            .findById(domaineDTO.getId())
            .map(existingDomaine -> {
                domaineMapper.partialUpdate(existingDomaine, domaineDTO);

                return existingDomaine;
            })
            .map(domaineRepository::save)
            .map(savedDomaine -> {
                domaineSearchRepository.index(savedDomaine);
                return savedDomaine;
            })
            .map(domaineMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<DomaineDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Domaines");
        return domaineRepository.findAll(pageable).map(domaineMapper::toDto);
    }

    public Page<DomaineDTO> findAllWithEagerRelationships(Pageable pageable) {
        return domaineRepository.findAllWithEagerRelationships(pageable).map(domaineMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<DomaineDTO> findOne(Long id) {
        log.debug("Request to get Domaine : {}", id);
        return domaineRepository.findOneWithEagerRelationships(id).map(domaineMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Domaine : {}", id);
        domaineRepository.deleteById(id);
        domaineSearchRepository.deleteFromIndexById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<DomaineDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Domaines for query {}", query);
        return domaineSearchRepository.search(query, pageable).map(domaineMapper::toDto);
    }
}
