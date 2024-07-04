package sn.ugb.gir.service.impl;

import java.util.Optional;

import jakarta.persistence.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sn.ugb.gir.domain.Etudiant;
import sn.ugb.gir.domain.InformationPersonnelle;
import sn.ugb.gir.domain.ProcessusInscriptionAdministrative;
import sn.ugb.gir.repository.EtudiantRepository;
import sn.ugb.gir.repository.InformationPersonnelleRepository;
import sn.ugb.gir.repository.ProcessusInscriptionAdministrativeRepository;
import sn.ugb.gir.repository.search.ProcessusInscriptionAdministrativeSearchRepository;
import sn.ugb.gir.service.ProcessusInscriptionAdministrativeService;
import sn.ugb.gir.service.dto.EtudiantDTO;
import sn.ugb.gir.service.dto.InformationPersonnelleDTO;
import sn.ugb.gir.service.dto.ProcessusInscriptionAdministrativeDTO;
import sn.ugb.gir.service.mapper.ProcessusInscriptionAdministrativeMapper;

/**
 * Service Implementation for managing {@link sn.ugb.gir.domain.ProcessusInscriptionAdministrative}.
 */
@Service
@Transactional
public class ProcessusInscriptionAdministrativeServiceImpl implements ProcessusInscriptionAdministrativeService {

    private final Logger log = LoggerFactory.getLogger(ProcessusInscriptionAdministrativeServiceImpl.class);

    private final ProcessusInscriptionAdministrativeRepository processusInscriptionAdministrativeRepository;
    private final InformationPersonnelleRepository informationPersonnelleRepository;

    private final ProcessusInscriptionAdministrativeMapper processusInscriptionAdministrativeMapper;

    private final ProcessusInscriptionAdministrativeSearchRepository processusInscriptionAdministrativeSearchRepository;
    private final EtudiantRepository etudiantRepository;

    public ProcessusInscriptionAdministrativeServiceImpl(
        ProcessusInscriptionAdministrativeRepository processusInscriptionAdministrativeRepository,
        InformationPersonnelleRepository informationPersonnelleRepository, ProcessusInscriptionAdministrativeMapper processusInscriptionAdministrativeMapper,
        ProcessusInscriptionAdministrativeSearchRepository processusInscriptionAdministrativeSearchRepository,
        EtudiantRepository etudiantRepository) {
        this.processusInscriptionAdministrativeRepository = processusInscriptionAdministrativeRepository;
        this.informationPersonnelleRepository = informationPersonnelleRepository;
        this.processusInscriptionAdministrativeMapper = processusInscriptionAdministrativeMapper;
        this.processusInscriptionAdministrativeSearchRepository = processusInscriptionAdministrativeSearchRepository;
        this.etudiantRepository = etudiantRepository;
    }

    @Override
    public ProcessusInscriptionAdministrativeDTO save(ProcessusInscriptionAdministrativeDTO processusInscriptionAdministrativeDTO) {
        log.debug("Request to save ProcessusInscriptionAdministrative : {}", processusInscriptionAdministrativeDTO);
        ProcessusInscriptionAdministrative processusInscriptionAdministrative = processusInscriptionAdministrativeMapper.toEntity(
            processusInscriptionAdministrativeDTO
        );
        processusInscriptionAdministrative = processusInscriptionAdministrativeRepository.save(processusInscriptionAdministrative);
        ProcessusInscriptionAdministrativeDTO result = processusInscriptionAdministrativeMapper.toDto(processusInscriptionAdministrative);
        processusInscriptionAdministrativeSearchRepository.index(processusInscriptionAdministrative);
        return result;
    }

    @Override
    public ProcessusInscriptionAdministrativeDTO update(ProcessusInscriptionAdministrativeDTO processusInscriptionAdministrativeDTO) {
        log.debug("Request to update ProcessusInscriptionAdministrative : {}", processusInscriptionAdministrativeDTO);
        ProcessusInscriptionAdministrative processusInscriptionAdministrative = processusInscriptionAdministrativeMapper.toEntity(
            processusInscriptionAdministrativeDTO
        );
        processusInscriptionAdministrative = processusInscriptionAdministrativeRepository.save(processusInscriptionAdministrative);
        ProcessusInscriptionAdministrativeDTO result = processusInscriptionAdministrativeMapper.toDto(processusInscriptionAdministrative);
        processusInscriptionAdministrativeSearchRepository.index(processusInscriptionAdministrative);
        return result;
    }

    @Override
    public Optional<ProcessusInscriptionAdministrativeDTO> partialUpdate(
        ProcessusInscriptionAdministrativeDTO processusInscriptionAdministrativeDTO
    ) {
        log.debug("Request to partially update ProcessusInscriptionAdministrative : {}", processusInscriptionAdministrativeDTO);

        return processusInscriptionAdministrativeRepository
            .findById(processusInscriptionAdministrativeDTO.getId())
            .map(existingProcessusInscriptionAdministrative -> {
                processusInscriptionAdministrativeMapper.partialUpdate(
                    existingProcessusInscriptionAdministrative,
                    processusInscriptionAdministrativeDTO
                );

                return existingProcessusInscriptionAdministrative;
            })
            .map(processusInscriptionAdministrativeRepository::save)
            .map(savedProcessusInscriptionAdministrative -> {
                processusInscriptionAdministrativeSearchRepository.index(savedProcessusInscriptionAdministrative);
                return savedProcessusInscriptionAdministrative;
            })
            .map(processusInscriptionAdministrativeMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ProcessusInscriptionAdministrativeDTO> findAll(Pageable pageable) {
        log.debug("Request to get all ProcessusInscriptionAdministratives");
        return processusInscriptionAdministrativeRepository.findAll(pageable).map(processusInscriptionAdministrativeMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ProcessusInscriptionAdministrativeDTO> findOne(Long id) {
        log.debug("Request to get ProcessusInscriptionAdministrative : {}", id);
        return processusInscriptionAdministrativeRepository.findById(id).map(processusInscriptionAdministrativeMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete ProcessusInscriptionAdministrative : {}", id);
        processusInscriptionAdministrativeRepository.deleteById(id);
        processusInscriptionAdministrativeSearchRepository.deleteFromIndexById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ProcessusInscriptionAdministrativeDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of ProcessusInscriptionAdministratives for query {}", query);
        return processusInscriptionAdministrativeSearchRepository
            .search(query, pageable)
            .map(processusInscriptionAdministrativeMapper::toDto);
    }

    @Override
    public String generateInstitutionalEmail(EtudiantDTO etudiantDTO ) {
        ProcessusInscriptionAdministrativeDTO  processusInscriptionAdministrativeDTO = new ProcessusInscriptionAdministrativeDTO();
        if (!processusInscriptionAdministrativeDTO.getInscritAdministrativementYN()) {
            throw new IllegalStateException("L'inscription administrative n'est pas terminée pour l'étudiant.");
        }
        String emailUGB =trouverNomPrenomUnique(getInformationPersonnelleByCodeBU(processusInscriptionAdministrativeDTO.getInscriptionAdministrative().getEtudiant().getCodeBU()).getNomEtu(),getInformationPersonnelleByCodeBU(processusInscriptionAdministrativeDTO.getInscriptionAdministrative().getEtudiant().getCodeBU()).getPrenomEtu())+"@ugb.edu.sn";
        return emailUGB;
    }

    @Override
    public String generateCodeEtudiant(EtudiantDTO etudiantDTO ) {
        return null;
    }


    @Override
    public String generateCodeBareBU(EtudiantDTO etudiantDTO) {

        if (etudiantDTO.getCodeBU() != null && etudiantDTO.getCodeBU().isEmpty()) {
            return "L'étudiant a un code BU : " + etudiantDTO.getCodeBU();
        } else {
            return "Aucun code BU n'est associé à cet étudiant.";
        }

    }

    @Override
    public InformationPersonnelleDTO getInformationPersonnelleByCodeBU(String codeBU) {
        Etudiant etudiant = etudiantRepository.findByCodeBU(codeBU);

        if (etudiant == null) {
            throw new EntityNotFoundException("Etudiant non trouvé avec le codeBU: " + codeBU);
        }
        InformationPersonnelle infoPersonnelle = etudiant.getInformationPersonnelle();

        if (infoPersonnelle == null) {
            throw new EntityNotFoundException("Information personnelle non trouvée pour l'étudiant avec le codeBU: " + codeBU);
        }

        InformationPersonnelleDTO infoPersonnelleDTO = new InformationPersonnelleDTO();
        infoPersonnelleDTO.getNomEtu();
        infoPersonnelleDTO.getPrenomEtu();
        return infoPersonnelleDTO;
    }
    @Override
    public String trouverNomPrenomUnique(String nomEtu, String prenomEtu) {
        Long count = informationPersonnelleRepository.countByNomEtuAndPrenomEtu(nomEtu, prenomEtu);
        if (count == 0) {
            return nomEtu+"."+prenomEtu;
        } else {
            int increment = 1;
            String nomUnique = nomEtu+"."+prenomEtu+ increment;
            while (informationPersonnelleRepository.existsByNomEtuAndPrenomEtu(nomUnique, prenomEtu)) {
                increment++;
                nomUnique = nomEtu+"."+prenomEtu + increment;
            }
            return nomUnique;
        }
    }

}
