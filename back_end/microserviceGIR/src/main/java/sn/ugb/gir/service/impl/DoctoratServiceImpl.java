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

    public DoctoratServiceImpl(DoctoratRepository doctoratRepository, DoctoratMapper doctoratMapper) {
        this.doctoratRepository = doctoratRepository;
        this.doctoratMapper = doctoratMapper;
    }

    @Override
    public DoctoratDTO save(DoctoratDTO doctoratDTO) {
        log.debug("Request to save Doctorat : {}", doctoratDTO);
        Doctorat doctorat = doctoratMapper.toEntity(doctoratDTO);
        doctorat = doctoratRepository.save(doctorat);
        return doctoratMapper.toDto(doctorat);
    }

    @Override
    public DoctoratDTO update(DoctoratDTO doctoratDTO) {
        log.debug("Request to update Doctorat : {}", doctoratDTO);
        Doctorat doctorat = doctoratMapper.toEntity(doctoratDTO);
        doctorat = doctoratRepository.save(doctorat);
        return doctoratMapper.toDto(doctorat);
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
    }
}
