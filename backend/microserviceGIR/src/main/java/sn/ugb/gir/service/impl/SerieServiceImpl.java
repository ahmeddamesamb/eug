package sn.ugb.gir.service.impl;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sn.ugb.gir.domain.Serie;
import sn.ugb.gir.repository.SerieRepository;
import sn.ugb.gir.repository.search.SerieSearchRepository;
import sn.ugb.gir.service.SerieService;
import sn.ugb.gir.service.dto.SerieDTO;
import sn.ugb.gir.service.mapper.SerieMapper;

/**
 * Service Implementation for managing {@link sn.ugb.gir.domain.Serie}.
 */
@Service
@Transactional
public class SerieServiceImpl implements SerieService {

    private final Logger log = LoggerFactory.getLogger(SerieServiceImpl.class);

    private final SerieRepository serieRepository;

    private final SerieMapper serieMapper;

    private final SerieSearchRepository serieSearchRepository;

    public SerieServiceImpl(SerieRepository serieRepository, SerieMapper serieMapper, SerieSearchRepository serieSearchRepository) {
        this.serieRepository = serieRepository;
        this.serieMapper = serieMapper;
        this.serieSearchRepository = serieSearchRepository;
    }

    @Override
    public SerieDTO save(SerieDTO serieDTO) {
        log.debug("Request to save Serie : {}", serieDTO);
        Serie serie = serieMapper.toEntity(serieDTO);
        serie = serieRepository.save(serie);
        SerieDTO result = serieMapper.toDto(serie);
        serieSearchRepository.index(serie);
        return result;
    }

    @Override
    public SerieDTO update(SerieDTO serieDTO) {
        log.debug("Request to update Serie : {}", serieDTO);
        Serie serie = serieMapper.toEntity(serieDTO);
        serie = serieRepository.save(serie);
        SerieDTO result = serieMapper.toDto(serie);
        serieSearchRepository.index(serie);
        return result;
    }

    @Override
    public Optional<SerieDTO> partialUpdate(SerieDTO serieDTO) {
        log.debug("Request to partially update Serie : {}", serieDTO);

        return serieRepository
            .findById(serieDTO.getId())
            .map(existingSerie -> {
                serieMapper.partialUpdate(existingSerie, serieDTO);

                return existingSerie;
            })
            .map(serieRepository::save)
            .map(savedSerie -> {
                serieSearchRepository.index(savedSerie);
                return savedSerie;
            })
            .map(serieMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<SerieDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Series");
        return serieRepository.findAll(pageable).map(serieMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<SerieDTO> findOne(Long id) {
        log.debug("Request to get Serie : {}", id);
        return serieRepository.findById(id).map(serieMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Serie : {}", id);
        serieRepository.deleteById(id);
        serieSearchRepository.deleteFromIndexById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<SerieDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Series for query {}", query);
        return serieSearchRepository.search(query, pageable).map(serieMapper::toDto);
    }
}
