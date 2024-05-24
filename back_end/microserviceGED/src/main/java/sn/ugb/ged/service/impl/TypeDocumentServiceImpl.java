package sn.ugb.ged.service.impl;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sn.ugb.ged.domain.TypeDocument;
import sn.ugb.ged.repository.TypeDocumentRepository;
import sn.ugb.ged.service.TypeDocumentService;
import sn.ugb.ged.service.dto.TypeDocumentDTO;
import sn.ugb.ged.service.mapper.TypeDocumentMapper;

/**
 * Service Implementation for managing {@link sn.ugb.ged.domain.TypeDocument}.
 */
@Service
@Transactional
public class TypeDocumentServiceImpl implements TypeDocumentService {

    private final Logger log = LoggerFactory.getLogger(TypeDocumentServiceImpl.class);

    private final TypeDocumentRepository typeDocumentRepository;

    private final TypeDocumentMapper typeDocumentMapper;

    public TypeDocumentServiceImpl(TypeDocumentRepository typeDocumentRepository, TypeDocumentMapper typeDocumentMapper) {
        this.typeDocumentRepository = typeDocumentRepository;
        this.typeDocumentMapper = typeDocumentMapper;
    }

    @Override
    public TypeDocumentDTO save(TypeDocumentDTO typeDocumentDTO) {
        log.debug("Request to save TypeDocument : {}", typeDocumentDTO);
        TypeDocument typeDocument = typeDocumentMapper.toEntity(typeDocumentDTO);
        typeDocument = typeDocumentRepository.save(typeDocument);
        return typeDocumentMapper.toDto(typeDocument);
    }

    @Override
    public TypeDocumentDTO update(TypeDocumentDTO typeDocumentDTO) {
        log.debug("Request to update TypeDocument : {}", typeDocumentDTO);
        TypeDocument typeDocument = typeDocumentMapper.toEntity(typeDocumentDTO);
        typeDocument = typeDocumentRepository.save(typeDocument);
        return typeDocumentMapper.toDto(typeDocument);
    }

    @Override
    public Optional<TypeDocumentDTO> partialUpdate(TypeDocumentDTO typeDocumentDTO) {
        log.debug("Request to partially update TypeDocument : {}", typeDocumentDTO);

        return typeDocumentRepository
            .findById(typeDocumentDTO.getId())
            .map(existingTypeDocument -> {
                typeDocumentMapper.partialUpdate(existingTypeDocument, typeDocumentDTO);

                return existingTypeDocument;
            })
            .map(typeDocumentRepository::save)
            .map(typeDocumentMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<TypeDocumentDTO> findAll(Pageable pageable) {
        log.debug("Request to get all TypeDocuments");
        return typeDocumentRepository.findAll(pageable).map(typeDocumentMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<TypeDocumentDTO> findOne(Long id) {
        log.debug("Request to get TypeDocument : {}", id);
        return typeDocumentRepository.findById(id).map(typeDocumentMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete TypeDocument : {}", id);
        typeDocumentRepository.deleteById(id);
    }
}
