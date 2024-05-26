package sn.ugb.gir.service.impl;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sn.ugb.gir.domain.Etudiant;
import sn.ugb.gir.repository.EtudiantRepository;
import sn.ugb.gir.service.EtudiantService;
import sn.ugb.gir.service.dto.EtudiantDTO;
import sn.ugb.gir.service.mapper.EtudiantMapper;

/**
 * Service Implementation for managing {@link sn.ugb.gir.domain.Etudiant}.
 */
@Service
@Transactional
public class EtudiantServiceImpl implements EtudiantService {

    private final Logger log = LoggerFactory.getLogger(EtudiantServiceImpl.class);

    private final EtudiantRepository etudiantRepository;

    private final EtudiantMapper etudiantMapper;

    public EtudiantServiceImpl(EtudiantRepository etudiantRepository, EtudiantMapper etudiantMapper) {
        this.etudiantRepository = etudiantRepository;
        this.etudiantMapper = etudiantMapper;
    }

    @Override
    public EtudiantDTO save(EtudiantDTO etudiantDTO) {
        log.debug("Request to save Etudiant : {}", etudiantDTO);
        Etudiant etudiant = etudiantMapper.toEntity(etudiantDTO);
        etudiant = etudiantRepository.save(etudiant);
        return etudiantMapper.toDto(etudiant);
    }

    @Override
    public EtudiantDTO update(EtudiantDTO etudiantDTO) {
        log.debug("Request to update Etudiant : {}", etudiantDTO);
        Etudiant etudiant = etudiantMapper.toEntity(etudiantDTO);
        etudiant = etudiantRepository.save(etudiant);
        return etudiantMapper.toDto(etudiant);
    }

    @Override
    public Optional<EtudiantDTO> partialUpdate(EtudiantDTO etudiantDTO) {
        log.debug("Request to partially update Etudiant : {}", etudiantDTO);

        return etudiantRepository
            .findById(etudiantDTO.getId())
            .map(existingEtudiant -> {
                etudiantMapper.partialUpdate(existingEtudiant, etudiantDTO);

                return existingEtudiant;
            })
            .map(etudiantRepository::save)
            .map(etudiantMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<EtudiantDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Etudiants");
        return etudiantRepository.findAll(pageable).map(etudiantMapper::toDto);
    }

    /**
     *  Get all the etudiants where InformationPersonnelle is {@code null}.
     *  @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<EtudiantDTO> findAllWhereInformationPersonnelleIsNull() {
        log.debug("Request to get all etudiants where InformationPersonnelle is null");
        return StreamSupport
            .stream(etudiantRepository.findAll().spliterator(), false)
            .filter(etudiant -> etudiant.getInformationPersonnelle() == null)
            .map(etudiantMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     *  Get all the etudiants where Baccalaureat is {@code null}.
     *  @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<EtudiantDTO> findAllWhereBaccalaureatIsNull() {
        log.debug("Request to get all etudiants where Baccalaureat is null");
        return StreamSupport
            .stream(etudiantRepository.findAll().spliterator(), false)
            .filter(etudiant -> etudiant.getBaccalaureat() == null)
            .map(etudiantMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<EtudiantDTO> findOne(Long id) {
        log.debug("Request to get Etudiant : {}", id);
        return etudiantRepository.findById(id).map(etudiantMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Etudiant : {}", id);
        etudiantRepository.deleteById(id);
    }
}
