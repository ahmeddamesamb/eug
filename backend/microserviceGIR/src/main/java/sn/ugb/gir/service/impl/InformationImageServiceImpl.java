package sn.ugb.gir.service.impl;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sn.ugb.gir.domain.InformationImage;
import sn.ugb.gir.repository.InformationImageRepository;
import sn.ugb.gir.repository.search.InformationImageSearchRepository;
import sn.ugb.gir.service.InformationImageService;
import sn.ugb.gir.service.dto.InformationImageDTO;
import sn.ugb.gir.service.mapper.InformationImageMapper;

/**
 * Service Implementation for managing {@link sn.ugb.gir.domain.InformationImage}.
 */
@Service
@Transactional
public class InformationImageServiceImpl implements InformationImageService {

    private final Logger log = LoggerFactory.getLogger(InformationImageServiceImpl.class);

    private final InformationImageRepository informationImageRepository;

    private final InformationImageMapper informationImageMapper;

    private final InformationImageSearchRepository informationImageSearchRepository;

    public InformationImageServiceImpl(
        InformationImageRepository informationImageRepository,
        InformationImageMapper informationImageMapper,
        InformationImageSearchRepository informationImageSearchRepository
    ) {
        this.informationImageRepository = informationImageRepository;
        this.informationImageMapper = informationImageMapper;
        this.informationImageSearchRepository = informationImageSearchRepository;
    }

    @Override
    public InformationImageDTO save(InformationImageDTO informationImageDTO) {
        log.debug("Request to save InformationImage : {}", informationImageDTO);
        InformationImage informationImage = informationImageMapper.toEntity(informationImageDTO);
        informationImage = informationImageRepository.save(informationImage);
        InformationImageDTO result = informationImageMapper.toDto(informationImage);
        informationImageSearchRepository.index(informationImage);
        return result;
    }

    @Override
    public InformationImageDTO update(InformationImageDTO informationImageDTO) {
        log.debug("Request to update InformationImage : {}", informationImageDTO);
        InformationImage informationImage = informationImageMapper.toEntity(informationImageDTO);
        informationImage = informationImageRepository.save(informationImage);
        InformationImageDTO result = informationImageMapper.toDto(informationImage);
        informationImageSearchRepository.index(informationImage);
        return result;
    }

    @Override
    public Optional<InformationImageDTO> partialUpdate(InformationImageDTO informationImageDTO) {
        log.debug("Request to partially update InformationImage : {}", informationImageDTO);

        return informationImageRepository
            .findById(informationImageDTO.getId())
            .map(existingInformationImage -> {
                informationImageMapper.partialUpdate(existingInformationImage, informationImageDTO);

                return existingInformationImage;
            })
            .map(informationImageRepository::save)
            .map(savedInformationImage -> {
                informationImageSearchRepository.index(savedInformationImage);
                return savedInformationImage;
            })
            .map(informationImageMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<InformationImageDTO> findAll(Pageable pageable) {
        log.debug("Request to get all InformationImages");
        return informationImageRepository.findAll(pageable).map(informationImageMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<InformationImageDTO> findOne(Long id) {
        log.debug("Request to get InformationImage : {}", id);
        return informationImageRepository.findById(id).map(informationImageMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete InformationImage : {}", id);
        informationImageRepository.deleteById(id);
        informationImageSearchRepository.deleteFromIndexById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<InformationImageDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of InformationImages for query {}", query);
        return informationImageSearchRepository.search(query, pageable).map(informationImageMapper::toDto);
    }
}
