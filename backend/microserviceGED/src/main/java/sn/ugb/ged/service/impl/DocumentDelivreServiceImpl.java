package sn.ugb.ged.service.impl;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sn.ugb.ged.domain.DocumentDelivre;
import sn.ugb.ged.repository.DocumentDelivreRepository;
import sn.ugb.ged.repository.search.DocumentDelivreSearchRepository;
import sn.ugb.ged.service.DocumentDelivreService;
import sn.ugb.ged.service.dto.DocumentDelivreDTO;
import sn.ugb.ged.service.mapper.DocumentDelivreMapper;

/**
 * Service Implementation for managing {@link sn.ugb.ged.domain.DocumentDelivre}.
 */
@Service
@Transactional
public class DocumentDelivreServiceImpl implements DocumentDelivreService {

    private final Logger log = LoggerFactory.getLogger(DocumentDelivreServiceImpl.class);

    private final DocumentDelivreRepository documentDelivreRepository;

    private final DocumentDelivreMapper documentDelivreMapper;

    private final DocumentDelivreSearchRepository documentDelivreSearchRepository;

    public DocumentDelivreServiceImpl(
        DocumentDelivreRepository documentDelivreRepository,
        DocumentDelivreMapper documentDelivreMapper,
        DocumentDelivreSearchRepository documentDelivreSearchRepository
    ) {
        this.documentDelivreRepository = documentDelivreRepository;
        this.documentDelivreMapper = documentDelivreMapper;
        this.documentDelivreSearchRepository = documentDelivreSearchRepository;
    }

    @Override
    public DocumentDelivreDTO save(DocumentDelivreDTO documentDelivreDTO) {
        log.debug("Request to save DocumentDelivre : {}", documentDelivreDTO);
        DocumentDelivre documentDelivre = documentDelivreMapper.toEntity(documentDelivreDTO);
        documentDelivre = documentDelivreRepository.save(documentDelivre);
        DocumentDelivreDTO result = documentDelivreMapper.toDto(documentDelivre);
        documentDelivreSearchRepository.index(documentDelivre);
        return result;
    }

    @Override
    public DocumentDelivreDTO update(DocumentDelivreDTO documentDelivreDTO) {
        log.debug("Request to update DocumentDelivre : {}", documentDelivreDTO);
        DocumentDelivre documentDelivre = documentDelivreMapper.toEntity(documentDelivreDTO);
        documentDelivre = documentDelivreRepository.save(documentDelivre);
        DocumentDelivreDTO result = documentDelivreMapper.toDto(documentDelivre);
        documentDelivreSearchRepository.index(documentDelivre);
        return result;
    }

    @Override
    public Optional<DocumentDelivreDTO> partialUpdate(DocumentDelivreDTO documentDelivreDTO) {
        log.debug("Request to partially update DocumentDelivre : {}", documentDelivreDTO);

        return documentDelivreRepository
            .findById(documentDelivreDTO.getId())
            .map(existingDocumentDelivre -> {
                documentDelivreMapper.partialUpdate(existingDocumentDelivre, documentDelivreDTO);

                return existingDocumentDelivre;
            })
            .map(documentDelivreRepository::save)
            .map(savedDocumentDelivre -> {
                documentDelivreSearchRepository.index(savedDocumentDelivre);
                return savedDocumentDelivre;
            })
            .map(documentDelivreMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<DocumentDelivreDTO> findAll(Pageable pageable) {
        log.debug("Request to get all DocumentDelivres");
        return documentDelivreRepository.findAll(pageable).map(documentDelivreMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<DocumentDelivreDTO> findOne(Long id) {
        log.debug("Request to get DocumentDelivre : {}", id);
        return documentDelivreRepository.findById(id).map(documentDelivreMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete DocumentDelivre : {}", id);
        documentDelivreRepository.deleteById(id);
        documentDelivreSearchRepository.deleteFromIndexById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<DocumentDelivreDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of DocumentDelivres for query {}", query);
        return documentDelivreSearchRepository.search(query, pageable).map(documentDelivreMapper::toDto);
    }
}
