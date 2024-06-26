package sn.ugb.gir.service.impl;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sn.ugb.gir.domain.Doctorat;
import sn.ugb.gir.repository.DoctoratRepository;
import sn.ugb.gir.repository.search.DoctoratSearchRepository;
import sn.ugb.gir.service.DoctoratService;
import sn.ugb.gir.service.dto.DoctoratDTO;
import sn.ugb.gir.service.mapper.DoctoratMapper;

/**
 * Service Implementation for managing {@link sn.ugb.gir.domain.Doctorat}.
 */
@Service
@Transactional
public class DoctoratServiceImpl implements DoctoratService {

    private final Logger log = LoggerFactory.getLogger(DoctoratServiceImpl.class);

    private final DoctoratRepository doctoratRepository;

    private final DoctoratMapper doctoratMapper;

    private final DoctoratSearchRepository doctoratSearchRepository;

    public DoctoratServiceImpl(
        DoctoratRepository doctoratRepository,
        DoctoratMapper doctoratMapper,
        DoctoratSearchRepository doctoratSearchRepository
    ) {
        this.doctoratRepository = doctoratRepository;
        this.doctoratMapper = doctoratMapper;
        this.doctoratSearchRepository = doctoratSearchRepository;
    }

    @Override
    public DoctoratDTO save(DoctoratDTO doctoratDTO) {
        log.debug("Request to save Doctorat : {}", doctoratDTO);
        Doctorat doctorat = doctoratMapper.toEntity(doctoratDTO);
        doctorat = doctoratRepository.save(doctorat);
        DoctoratDTO result = doctoratMapper.toDto(doctorat);
        doctoratSearchRepository.index(doctorat);
        return result;
    }

    @Override
    public DoctoratDTO update(DoctoratDTO doctoratDTO) {
        log.debug("Request to update Doctorat : {}", doctoratDTO);
        Doctorat doctorat = doctoratMapper.toEntity(doctoratDTO);
        doctorat = doctoratRepository.save(doctorat);
        DoctoratDTO result = doctoratMapper.toDto(doctorat);
        doctoratSearchRepository.index(doctorat);
        return result;
    }

    @Override
    public Optional<DoctoratDTO> partialUpdate(DoctoratDTO doctoratDTO) {
        log.debug("Request to partially update Doctorat : {}", doctoratDTO);

        return doctoratRepository
            .findById(doctoratDTO.getId())
            .map(existingDoctorat -> {
                doctoratMapper.partialUpdate(existingDoctorat, doctoratDTO);

                return existingDoctorat;
            })
            .map(doctoratRepository::save)
            .map(savedDoctorat -> {
                doctoratSearchRepository.index(savedDoctorat);
                return savedDoctorat;
            })
            .map(doctoratMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<DoctoratDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Doctorats");
        return doctoratRepository.findAll(pageable).map(doctoratMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<DoctoratDTO> findOne(Long id) {
        log.debug("Request to get Doctorat : {}", id);
        return doctoratRepository.findById(id).map(doctoratMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Doctorat : {}", id);
        doctoratRepository.deleteById(id);
        doctoratSearchRepository.deleteFromIndexById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<DoctoratDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Doctorats for query {}", query);
        return doctoratSearchRepository.search(query, pageable).map(doctoratMapper::toDto);
    }
}
