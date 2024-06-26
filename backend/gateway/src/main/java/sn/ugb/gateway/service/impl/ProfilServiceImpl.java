package sn.ugb.gateway.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import sn.ugb.gateway.repository.ProfilRepository;
import sn.ugb.gateway.repository.search.ProfilSearchRepository;
import sn.ugb.gateway.service.ProfilService;
import sn.ugb.gateway.service.dto.ProfilDTO;
import sn.ugb.gateway.service.mapper.ProfilMapper;

/**
 * Service Implementation for managing {@link sn.ugb.gateway.domain.Profil}.
 */
@Service
@Transactional
public class ProfilServiceImpl implements ProfilService {

    private final Logger log = LoggerFactory.getLogger(ProfilServiceImpl.class);

    private final ProfilRepository profilRepository;

    private final ProfilMapper profilMapper;

    private final ProfilSearchRepository profilSearchRepository;

    public ProfilServiceImpl(ProfilRepository profilRepository, ProfilMapper profilMapper, ProfilSearchRepository profilSearchRepository) {
        this.profilRepository = profilRepository;
        this.profilMapper = profilMapper;
        this.profilSearchRepository = profilSearchRepository;
    }

    @Override
    public Mono<ProfilDTO> save(ProfilDTO profilDTO) {
        log.debug("Request to save Profil : {}", profilDTO);
        return profilRepository.save(profilMapper.toEntity(profilDTO)).flatMap(profilSearchRepository::save).map(profilMapper::toDto);
    }

    @Override
    public Mono<ProfilDTO> update(ProfilDTO profilDTO) {
        log.debug("Request to update Profil : {}", profilDTO);
        return profilRepository.save(profilMapper.toEntity(profilDTO)).flatMap(profilSearchRepository::save).map(profilMapper::toDto);
    }

    @Override
    public Mono<ProfilDTO> partialUpdate(ProfilDTO profilDTO) {
        log.debug("Request to partially update Profil : {}", profilDTO);

        return profilRepository
            .findById(profilDTO.getId())
            .map(existingProfil -> {
                profilMapper.partialUpdate(existingProfil, profilDTO);

                return existingProfil;
            })
            .flatMap(profilRepository::save)
            .flatMap(savedProfil -> {
                profilSearchRepository.save(savedProfil);
                return Mono.just(savedProfil);
            })
            .map(profilMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Flux<ProfilDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Profils");
        return profilRepository.findAllBy(pageable).map(profilMapper::toDto);
    }

    public Mono<Long> countAll() {
        return profilRepository.count();
    }

    public Mono<Long> searchCount() {
        return profilSearchRepository.count();
    }

    @Override
    @Transactional(readOnly = true)
    public Mono<ProfilDTO> findOne(Long id) {
        log.debug("Request to get Profil : {}", id);
        return profilRepository.findById(id).map(profilMapper::toDto);
    }

    @Override
    public Mono<Void> delete(Long id) {
        log.debug("Request to delete Profil : {}", id);
        return profilRepository.deleteById(id).then(profilSearchRepository.deleteById(id));
    }

    @Override
    @Transactional(readOnly = true)
    public Flux<ProfilDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Profils for query {}", query);
        return profilSearchRepository.search(query, pageable).map(profilMapper::toDto);
    }
}
