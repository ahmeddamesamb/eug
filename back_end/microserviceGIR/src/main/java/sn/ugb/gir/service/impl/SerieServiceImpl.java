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
import sn.ugb.gir.service.SerieService;
import sn.ugb.gir.service.dto.SerieDTO;
import sn.ugb.gir.service.mapper.SerieMapper;
import sn.ugb.gir.web.rest.errors.BadRequestAlertException;

/**
 * Service Implementation for managing {@link sn.ugb.gir.domain.Serie}.
 */
@Service
@Transactional
public class SerieServiceImpl implements SerieService {

    private final Logger log = LoggerFactory.getLogger(SerieServiceImpl.class);

    private final SerieRepository serieRepository;

    private final SerieMapper serieMapper;

    private static final String ENTITY_NAME = "microserviceGirSerie";

    public SerieServiceImpl(SerieRepository serieRepository, SerieMapper serieMapper) {
        this.serieRepository = serieRepository;
        this.serieMapper = serieMapper;
    }

    @Override
    public SerieDTO save(SerieDTO serieDTO) {
        log.debug("Request to save Serie : {}", serieDTO);
        validateData(serieDTO);
        Serie serie = serieMapper.toEntity(serieDTO);
        serie = serieRepository.save(serie);
        return serieMapper.toDto(serie);
    }

    @Override
    public SerieDTO update(SerieDTO serieDTO) {
        log.debug("Request to update Serie : {}", serieDTO);
        validateData(serieDTO);
        Serie serie = serieMapper.toEntity(serieDTO);
        serie = serieRepository.save(serie);
        return serieMapper.toDto(serie);
    }

    @Override
    public Optional<SerieDTO> partialUpdate(SerieDTO serieDTO) {
        log.debug("Request to partially update Serie : {}", serieDTO);
        validateData(serieDTO);
        return serieRepository
            .findById(serieDTO.getId())
            .map(existingSerie -> {
                serieMapper.partialUpdate(existingSerie, serieDTO);

                return existingSerie;
            })
            .map(serieRepository::save)
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
    }

    private void validateData(SerieDTO serieDTO) {
        if (serieDTO.getLibelleSerie().isBlank()){
            throw new BadRequestAlertException("Le libellé ne peut pas être vide.", ENTITY_NAME, "libelleSerieNotNull");
        }
        if (serieDTO.getSigleSerie().isBlank()){
            throw new BadRequestAlertException("La sigle ne peut pas être vide.", ENTITY_NAME, "SigleSerieNotNull");
        }
        if (serieDTO.getCodeSerie().isBlank()){
            throw new BadRequestAlertException("Le code ne peut pas être vide.", ENTITY_NAME, "codeSerieNotNull");
        }
        Optional<Serie> existingSerie = serieRepository.findByLibelleSerieIgnoreCase(serieDTO.getLibelleSerie());
        if (existingSerie.isPresent() && !existingSerie.get().getId().equals(serieDTO.getId())) {
            throw new BadRequestAlertException("Une serie avec le même libellé existe.", ENTITY_NAME, "libelleSerieExist");
        }
        existingSerie = serieRepository.findBySigleSerieIgnoreCase(serieDTO.getSigleSerie());
        if (existingSerie.isPresent() && !existingSerie.get().getId().equals(serieDTO.getId())) {
            throw new BadRequestAlertException("Une serie avec la même sigle existe.", ENTITY_NAME, "sigleSerieExist");
        }
        existingSerie = serieRepository.findByCodeSerieIgnoreCase(serieDTO.getCodeSerie());
        if (existingSerie.isPresent() && !existingSerie.get().getId().equals(serieDTO.getId())) {
            throw new BadRequestAlertException("Une serie avec le même code existe.", ENTITY_NAME, "codeSerieExist");
        }
    }
}
