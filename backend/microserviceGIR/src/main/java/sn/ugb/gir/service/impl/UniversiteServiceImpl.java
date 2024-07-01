package sn.ugb.gir.service.impl;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sn.ugb.gir.domain.Universite;
import sn.ugb.gir.repository.UniversiteRepository;
import sn.ugb.gir.repository.search.UniversiteSearchRepository;
import sn.ugb.gir.service.UniversiteService;
import sn.ugb.gir.service.dto.UniversiteDTO;
import sn.ugb.gir.service.mapper.UniversiteMapper;
import sn.ugb.gir.web.rest.errors.BadRequestAlertException;

import static org.hibernate.id.IdentifierGenerator.ENTITY_NAME;

/**
 * Service Implementation for managing {@link sn.ugb.gir.domain.Universite}.
 */
@Service
@Transactional
public class UniversiteServiceImpl implements UniversiteService {

    private final Logger log = LoggerFactory.getLogger(UniversiteServiceImpl.class);

    private final UniversiteRepository universiteRepository;

    private final UniversiteMapper universiteMapper;

    private final UniversiteSearchRepository universiteSearchRepository;

    public UniversiteServiceImpl(
        UniversiteRepository universiteRepository,
        UniversiteMapper universiteMapper,
        UniversiteSearchRepository universiteSearchRepository
    ) {
        this.universiteRepository = universiteRepository;
        this.universiteMapper = universiteMapper;
        this.universiteSearchRepository = universiteSearchRepository;
    }

    @Override
    public UniversiteDTO save(UniversiteDTO universiteDTO) {
        log.debug("Request to save Universite : {}", universiteDTO);

        validateData(universiteDTO);
        Universite universite = universiteMapper.toEntity(universiteDTO);
        universite = universiteRepository.save(universite);
        UniversiteDTO result = universiteMapper.toDto(universite);
        universiteSearchRepository.index(universite);
        return result;
    }

    @Override
    public UniversiteDTO update(UniversiteDTO universiteDTO) {
        log.debug("Request to update Universite : {}", universiteDTO);

        validateData(universiteDTO);
        Universite universite = universiteMapper.toEntity(universiteDTO);
        universite = universiteRepository.save(universite);
        UniversiteDTO result = universiteMapper.toDto(universite);
        universiteSearchRepository.index(universite);
        return result;
    }

    @Override
    public Optional<UniversiteDTO> partialUpdate(UniversiteDTO universiteDTO) {
        log.debug("Request to partially update Universite : {}", universiteDTO);

        validateData(universiteDTO);
        return universiteRepository
            .findById(universiteDTO.getId())
            .map(existingUniversite -> {
                universiteMapper.partialUpdate(existingUniversite, universiteDTO);

                return existingUniversite;
            })
            .map(universiteRepository::save)
            .map(savedUniversite -> {
                universiteSearchRepository.index(savedUniversite);
                return savedUniversite;
            })
            .map(universiteMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<UniversiteDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Universites");
        return universiteRepository.findAll(pageable).map(universiteMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<UniversiteDTO> findOne(Long id) {
        log.debug("Request to get Universite : {}", id);
        return universiteRepository.findById(id).map(universiteMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Universite : {}", id);
        universiteRepository.deleteById(id);
        universiteSearchRepository.deleteFromIndexById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<UniversiteDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Universites for query {}", query);
        return universiteSearchRepository.search(query, pageable).map(universiteMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<UniversiteDTO> findAllByMinistereId(Pageable pageable,Long id) {
        log.debug("Request to get all Universites");
        return universiteRepository.findByMinistereId(pageable,id).map(universiteMapper::toDto);
    }

    public void validateData(UniversiteDTO universiteDTO){

        if ( universiteDTO.getNomUniversite().isEmpty() || universiteDTO.getNomUniversite().isBlank()) {
            throw new BadRequestAlertException("veuillez renseigné le nom de l'universite", ENTITY_NAME, "nomUniversiteNull");
        }
        if ( universiteDTO.getSigleUniversite().isEmpty() || universiteDTO.getSigleUniversite().isBlank()) {
            throw new BadRequestAlertException("veuillez renseigné le sigle de l'universite", ENTITY_NAME, "sigleUniversiteNull");
        }
        if ( universiteDTO.getMinistere().getId().describeConstable().isEmpty() ) {
            throw new BadRequestAlertException("Entite universite :  le ministere rataché doit etre renseigné", ENTITY_NAME, "ministereIdNull");
        }
        Optional<Universite> existingNomUniversite = universiteRepository.findByNomUniversiteIgnoreCase(universiteDTO.getNomUniversite());
        if( existingNomUniversite.isPresent() && !existingNomUniversite.get().getId().equals(universiteDTO.getId())){
            throw new BadRequestAlertException("Un autre universite porte deja ce nom ", ENTITY_NAME, "nomUniversiteExistedeja");
        }
    }
}
