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
    InformationPersonnelleRepository informationPersonnelleRepository,
    ProcessusInscriptionAdministrativeMapper processusInscriptionAdministrativeMapper,
    ProcessusInscriptionAdministrativeSearchRepository processusInscriptionAdministrativeSearchRepository,
    EtudiantRepository etudiantRepository
) {
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
public String generateInstitutionalEmail(Long etudiantId) {

    Etudiant etudiant = etudiantRepository.findById(etudiantId)
                            .orElseThrow(() -> new EntityNotFoundException("Étudiant non trouvé avec l'ID : " + etudiantId));
    ProcessusInscriptionAdministrativeDTO processusInscriptionAdministrativeDTO = new ProcessusInscriptionAdministrativeDTO();
//     if (processusInscriptionAdministrativeDTO.getInscritAdministrativementYN()){
//         throw new IllegalStateException("L'étudiant n'a pas terminé son inscription administrative.");
//     }
    InformationPersonnelle infoPersonnelle = etudiant.getInformationPersonnelle();
    if (infoPersonnelle == null) {
        throw new EntityNotFoundException("Information personnelle non trouvée pour l'étudiant avec l'ID : " + etudiantId);
    }
    String email = trouverNomPrenomUnique(infoPersonnelle.getNomEtu(), infoPersonnelle.getPrenomEtu());
    etudiant.setEmailUGB(email);
    etudiantRepository.save(etudiant);

    return "Email UGB généré avec succès pour l'étudiant avec ID : " + etudiantId + ", Nouveau Email UGB : " + email;
}

@Override
public String generateCodeBareBU(Long etudiantId) {
    ProcessusInscriptionAdministrative processusInscription = processusInscriptionAdministrativeRepository.findByInscriptionAdministrativeEtudiantId(etudiantId)
                                                                  .orElseThrow(() -> new IllegalStateException("L'inscription administrative n'est pas démarrée pour l'étudiant avec ID : " + etudiantId));

    Etudiant etudiant = etudiantRepository.findById(etudiantId)
                            .orElseThrow(() -> new IllegalStateException("Étudiant non trouvé avec l'ID : " + etudiantId));

    String maxCodeBarre = etudiantRepository.findMaxCodeBU();
    String nouveauCodeBarre = (maxCodeBarre != null ? maxCodeBarre + 1 : String.valueOf(1));

    etudiant.setCodeBU(nouveauCodeBarre);
    etudiantRepository.save(etudiant);
    return "Code barre BU généré avec succès pour l'étudiant avec ID : " + etudiantId + ", Nouveau code barre : " + nouveauCodeBarre;
}

@Override
public String generateCodeEtudiant(EtudiantDTO etudiantDTO) {
    return null;
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
    infoPersonnelleDTO.setNomEtu(infoPersonnelle.getNomEtu());
    infoPersonnelleDTO.setPrenomEtu(infoPersonnelle.getPrenomEtu());
    return infoPersonnelleDTO;
}

@Override
public String trouverNomPrenomUnique(String nomEtu, String prenomEtu) {
    String domaine = "@ugb.edu.sn";
    Long count = informationPersonnelleRepository.countByNomEtuAndPrenomEtu(nomEtu, prenomEtu);
    if (count == 0) {
        return nomEtu + "." + prenomEtu + domaine;
    } else {
        String nomUnique = nomEtu + "." + prenomEtu + (count + 1);
        return nomUnique + domaine;
    }
}
}
