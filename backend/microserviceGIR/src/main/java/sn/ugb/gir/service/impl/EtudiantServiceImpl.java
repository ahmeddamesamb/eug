package sn.ugb.gir.service.impl;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sn.ugb.gir.domain.*;
import sn.ugb.gir.repository.EtudiantRepository;
import sn.ugb.gir.repository.InformationPersonnelleRepository;
import sn.ugb.gir.repository.PaiementFraisRepository;
import sn.ugb.gir.repository.search.EtudiantSearchRepository;
import sn.ugb.gir.service.EtudiantService;
import sn.ugb.gir.service.dto.*;
import sn.ugb.gir.service.mapper.EtudiantMapper;
import sn.ugb.gir.service.mapper.FormationPriveeMapper;
import sn.ugb.gir.service.mapper.InformationPersonnelleMapper;
import sn.ugb.gir.service.mapper.PaiementFraisMapper;

/**
 * Service Implementation for managing {@link sn.ugb.gir.domain.Etudiant}.
 */
@Service
@Transactional
public class EtudiantServiceImpl implements EtudiantService {

    private final Logger log = LoggerFactory.getLogger(EtudiantServiceImpl.class);

    private final EtudiantRepository etudiantRepository;

    private final EtudiantMapper etudiantMapper;

    private final EtudiantSearchRepository etudiantSearchRepository;

    @Autowired
    private PaiementFraisRepository paiementFraisRepository;

    @Autowired
    private PaiementFraisMapper paiementFraisMapper;

    @Autowired
    private InformationPersonnelleMapper informationPersonnelleMapper;

    @Autowired
    private InformationPersonnelleRepository informationPersonnelleRepository;

    @Autowired
    FormationPriveeMapper formationPriveeMapper;


    public EtudiantServiceImpl(
        EtudiantRepository etudiantRepository,
        EtudiantMapper etudiantMapper,
        EtudiantSearchRepository etudiantSearchRepository
    ) {
        this.etudiantRepository = etudiantRepository;
        this.etudiantMapper = etudiantMapper;
        this.etudiantSearchRepository = etudiantSearchRepository;
    }

    @Override
    public EtudiantDTO save(EtudiantDTO etudiantDTO) {
        log.debug("Request to save Etudiant : {}", etudiantDTO);
        Etudiant etudiant = etudiantMapper.toEntity(etudiantDTO);
        etudiant = etudiantRepository.save(etudiant);
        EtudiantDTO result = etudiantMapper.toDto(etudiant);
        etudiantSearchRepository.index(etudiant);
        return result;
    }

    @Override
    public EtudiantDTO update(EtudiantDTO etudiantDTO) {
        log.debug("Request to update Etudiant : {}", etudiantDTO);
        Etudiant etudiant = etudiantMapper.toEntity(etudiantDTO);
        etudiant = etudiantRepository.save(etudiant);
        EtudiantDTO result = etudiantMapper.toDto(etudiant);
        etudiantSearchRepository.index(etudiant);
        return result;
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
            .map(savedEtudiant -> {
                etudiantSearchRepository.index(savedEtudiant);
                return savedEtudiant;
            })
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
        etudiantSearchRepository.deleteFromIndexById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<EtudiantDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Etudiants for query {}", query);
        return etudiantSearchRepository.search(query, pageable).map(etudiantMapper::toDto);
    }

    @Override
    public DossierEtudiantDTO getEtudiantDetailsByCodeEtu(String codeEtudiant) {

        List<PaiementFrais> paiementFraisEntities = paiementFraisRepository.findByInscriptionAdministrativeFormationInscriptionAdministrativeEtudiantCodeEtu(codeEtudiant);

        List<PaiementFraisDTO> paiementFrais = paiementFraisEntities.stream()
            .map(paiementFraisMapper::toDto)
            .collect(Collectors.toList());

        Formation formation = paiementFraisEntities.stream()
            .map(pf -> pf.getInscriptionAdministrativeFormation().getFormation())
            .findFirst()
            .orElse(null);

        FormationPriveeDTO formationPriveeDTO = null;
        if (formation != null) {
            FormationPrivee formationPrivee = formation.getFormationPrivee();
            if (formationPrivee != null) {
                formationPriveeDTO = formationPriveeMapper.toDto(formationPrivee);
            }
        }

        InformationPersonnelle informationPersonnelle = informationPersonnelleRepository.findByEtudiantCodeEtu(codeEtudiant);
        InformationPersonnelleDTO informationPersonnelleDTO = informationPersonnelleMapper.toDto(informationPersonnelle);

        informationPersonnelleDTO.setEtudiant(null);

        DossierEtudiantDTO dossierEtudiantDTO = new DossierEtudiantDTO();
        dossierEtudiantDTO.setInformationPersonnelle(informationPersonnelleDTO);
        dossierEtudiantDTO.setPaiementFrais(paiementFrais);
        dossierEtudiantDTO.setFormationPrivee(formationPriveeDTO);

        return dossierEtudiantDTO;
    }
}
