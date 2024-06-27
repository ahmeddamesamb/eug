package sn.ugb.gir.service.impl;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sn.ugb.gir.domain.TypeFrais;
import sn.ugb.gir.repository.TypeFraisRepository;
import sn.ugb.gir.repository.search.TypeFraisSearchRepository;
import sn.ugb.gir.service.TypeFraisService;
import sn.ugb.gir.service.dto.TypeFraisDTO;
import sn.ugb.gir.service.mapper.TypeFraisMapper;
import sn.ugb.gir.web.rest.errors.BadRequestAlertException;

import static org.hibernate.id.IdentifierGenerator.ENTITY_NAME;

/**
 * Implémentation du service pour la gestion des {@link sn.ugb.gir.domain.TypeFrais}.
 */
@Service
@Transactional
public class TypeFraisServiceImpl implements TypeFraisService {

    private final Logger log = LoggerFactory.getLogger(TypeFraisServiceImpl.class);

    private final TypeFraisRepository typeHandicapRepository;

    private final TypeFraisMapper typeHandicapMapper;

    private final TypeFraisSearchRepository typeHandicapSearchRepository;

    public TypeFraisServiceImpl(
        TypeFraisRepository typeHandicapRepository,
        TypeFraisMapper typeHandicapMapper,
        TypeFraisSearchRepository typeHandicapSearchRepository
    ) {
        this.typeHandicapRepository = typeHandicapRepository;
        this.typeHandicapMapper = typeHandicapMapper;
        this.typeHandicapSearchRepository = typeHandicapSearchRepository;
    }

    @Override
    public TypeFraisDTO save(TypeFraisDTO typeHandicapDTO) {
        log.debug("Requête pour enregistrer TypeFrais : {}", typeHandicapDTO);

        validateData(typeHandicapDTO.getLibelleTypeFrais(), typeHandicapDTO.getId());

        TypeFrais typeHandicap = typeHandicapMapper.toEntity(typeHandicapDTO);
        typeHandicap = typeHandicapRepository.save(typeHandicap);
        TypeFraisDTO result = typeHandicapMapper.toDto(typeHandicap);
        typeHandicapSearchRepository.index(typeHandicap);
        return result;
    }

    @Override
    public TypeFraisDTO update(TypeFraisDTO typeHandicapDTO) {
        log.debug("Requête pour mettre à jour TypeFrais : {}", typeHandicapDTO);

        validateData(typeHandicapDTO.getLibelleTypeFrais(), typeHandicapDTO.getId());

        TypeFrais typeHandicap = typeHandicapMapper.toEntity(typeHandicapDTO);
        typeHandicap = typeHandicapRepository.save(typeHandicap);
        TypeFraisDTO result = typeHandicapMapper.toDto(typeHandicap);
        typeHandicapSearchRepository.index(typeHandicap);
        return result;
    }

    @Override
    public Optional<TypeFraisDTO> partialUpdate(TypeFraisDTO typeHandicapDTO) {
        log.debug("Requête pour mettre à jour partiellement TypeFrais : {}", typeHandicapDTO);

        validateData(typeHandicapDTO.getLibelleTypeFrais(), typeHandicapDTO.getId());

        return typeHandicapRepository
            .findById(typeHandicapDTO.getId())
            .map(existingTypeFrais -> {
                typeHandicapMapper.partialUpdate(existingTypeFrais, typeHandicapDTO);
                return existingTypeFrais;
            })
            .map(typeHandicapRepository::save)
            .map(savedTypeFrais -> {
                typeHandicapSearchRepository.index(savedTypeFrais);
                return typeHandicapMapper.toDto(savedTypeFrais);
            });
    }

    @Override
    @Transactional(readOnly = true)
    public Page<TypeFraisDTO> findAll(Pageable pageable) {
        log.debug("Requête pour récupérer tous les TypeFraiss");
        return typeHandicapRepository.findAll(pageable).map(typeHandicapMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<TypeFraisDTO> findOne(Long id) {
        log.debug("Requête pour récupérer TypeFrais : {}", id);
        return typeHandicapRepository.findById(id).map(typeHandicapMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Requête pour supprimer TypeFrais : {}", id);
        typeHandicapRepository.deleteById(id);
        typeHandicapSearchRepository.deleteFromIndexById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<TypeFraisDTO> search(String query, Pageable pageable) {
        log.debug("Requête pour rechercher une page de TypeFraiss pour la requête {}", query);
        return typeHandicapSearchRepository.search(query, pageable).map(typeHandicapMapper::toDto);
    }

    private void validateData(String libelleTypeFrais, Long id) {
        if (libelleTypeFrais == null || libelleTypeFrais.trim().isBlank()) {
            throw new BadRequestAlertException("Le libellé du TypeFrais ne peut pas être vide", ENTITY_NAME, "libelleTypeFraisVide");
        }

        Optional<TypeFrais> existingTypeFrais = typeHandicapRepository.findByLibelleTypeFraisIgnoreCase(libelleTypeFrais.trim());
        if (existingTypeFrais.isPresent() && !existingTypeFrais.get().getId().equals(id)) {
            throw new BadRequestAlertException("Un TypeFrais avec le même nom existe déjà", ENTITY_NAME, "typeHandicapExiste");
        }
    }
}
