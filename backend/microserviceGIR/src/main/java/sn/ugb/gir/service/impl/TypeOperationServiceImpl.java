package sn.ugb.gir.service.impl;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sn.ugb.gir.domain.TypeOperation;
import sn.ugb.gir.repository.TypeOperationRepository;
import sn.ugb.gir.repository.search.TypeOperationSearchRepository;
import sn.ugb.gir.service.TypeOperationService;
import sn.ugb.gir.service.dto.TypeOperationDTO;
import sn.ugb.gir.service.mapper.TypeOperationMapper;
import sn.ugb.gir.web.rest.errors.BadRequestAlertException;

import static org.hibernate.id.IdentifierGenerator.ENTITY_NAME;

/**
 * Implémentation du service pour la gestion des {@link sn.ugb.gir.domain.TypeOperation}.
 */
@Service
@Transactional
public class TypeOperationServiceImpl implements TypeOperationService {

    private final Logger log = LoggerFactory.getLogger(TypeOperationServiceImpl.class);

    private final TypeOperationRepository typeHandicapRepository;

    private final TypeOperationMapper typeHandicapMapper;

    private final TypeOperationSearchRepository typeHandicapSearchRepository;

    public TypeOperationServiceImpl(
        TypeOperationRepository typeHandicapRepository,
        TypeOperationMapper typeHandicapMapper,
        TypeOperationSearchRepository typeHandicapSearchRepository
    ) {
        this.typeHandicapRepository = typeHandicapRepository;
        this.typeHandicapMapper = typeHandicapMapper;
        this.typeHandicapSearchRepository = typeHandicapSearchRepository;
    }

    @Override
    public TypeOperationDTO save(TypeOperationDTO typeHandicapDTO) {
        log.debug("Requête pour enregistrer TypeOperation : {}", typeHandicapDTO);

        validateData(typeHandicapDTO.getLibelleTypeOperation(), typeHandicapDTO.getId());

        TypeOperation typeHandicap = typeHandicapMapper.toEntity(typeHandicapDTO);
        typeHandicap = typeHandicapRepository.save(typeHandicap);
        TypeOperationDTO result = typeHandicapMapper.toDto(typeHandicap);
        typeHandicapSearchRepository.index(typeHandicap);
        return result;
    }

    @Override
    public TypeOperationDTO update(TypeOperationDTO typeHandicapDTO) {
        log.debug("Requête pour mettre à jour TypeOperation : {}", typeHandicapDTO);

        validateData(typeHandicapDTO.getLibelleTypeOperation(), typeHandicapDTO.getId());

        TypeOperation typeHandicap = typeHandicapMapper.toEntity(typeHandicapDTO);
        typeHandicap = typeHandicapRepository.save(typeHandicap);
        TypeOperationDTO result = typeHandicapMapper.toDto(typeHandicap);
        typeHandicapSearchRepository.index(typeHandicap);
        return result;
    }

    @Override
    public Optional<TypeOperationDTO> partialUpdate(TypeOperationDTO typeHandicapDTO) {
        log.debug("Requête pour mettre à jour partiellement TypeOperation : {}", typeHandicapDTO);

        validateData(typeHandicapDTO.getLibelleTypeOperation(), typeHandicapDTO.getId());

        return typeHandicapRepository
            .findById(typeHandicapDTO.getId())
            .map(existingTypeOperation -> {
                typeHandicapMapper.partialUpdate(existingTypeOperation, typeHandicapDTO);
                return existingTypeOperation;
            })
            .map(typeHandicapRepository::save)
            .map(savedTypeOperation -> {
                typeHandicapSearchRepository.index(savedTypeOperation);
                return typeHandicapMapper.toDto(savedTypeOperation);
            });
    }

    @Override
    @Transactional(readOnly = true)
    public Page<TypeOperationDTO> findAll(Pageable pageable) {
        log.debug("Requête pour récupérer tous les TypeOperations");
        return typeHandicapRepository.findAll(pageable).map(typeHandicapMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<TypeOperationDTO> findOne(Long id) {
        log.debug("Requête pour récupérer TypeOperation : {}", id);
        return typeHandicapRepository.findById(id).map(typeHandicapMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Requête pour supprimer TypeOperation : {}", id);
        typeHandicapRepository.deleteById(id);
        typeHandicapSearchRepository.deleteFromIndexById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<TypeOperationDTO> search(String query, Pageable pageable) {
        log.debug("Requête pour rechercher une page de TypeOperations pour la requête {}", query);
        return typeHandicapSearchRepository.search(query, pageable).map(typeHandicapMapper::toDto);
    }

    private void validateData(String libelleTypeOperation, Long id) {
        if (libelleTypeOperation == null || libelleTypeOperation.trim().isBlank()) {
            throw new BadRequestAlertException("Le libellé du TypeOperation ne peut pas être vide", ENTITY_NAME, "libelleTypeOperationVide");
        }

        Optional<TypeOperation> existingTypeOperation = typeHandicapRepository.findByLibelleTypeOperationIgnoreCase(libelleTypeOperation.trim());
        if (existingTypeOperation.isPresent() && !existingTypeOperation.get().getId().equals(id)) {
            throw new BadRequestAlertException("Un TypeOperation avec le même nom existe déjà", ENTITY_NAME, "typeHandicapExiste");
        }
    }
}
