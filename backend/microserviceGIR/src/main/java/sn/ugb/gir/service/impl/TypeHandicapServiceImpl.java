package sn.ugb.gir.service.impl;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sn.ugb.gir.domain.TypeHandicap;
import sn.ugb.gir.repository.TypeHandicapRepository;
import sn.ugb.gir.repository.search.TypeHandicapSearchRepository;
import sn.ugb.gir.service.TypeHandicapService;
import sn.ugb.gir.service.dto.TypeHandicapDTO;
import sn.ugb.gir.service.mapper.TypeHandicapMapper;
import sn.ugb.gir.web.rest.errors.BadRequestAlertException;

import static org.hibernate.id.IdentifierGenerator.ENTITY_NAME;

/**
 * Implémentation du service pour la gestion des {@link sn.ugb.gir.domain.TypeHandicap}.
 */
@Service
@Transactional
public class TypeHandicapServiceImpl implements TypeHandicapService {

    private final Logger log = LoggerFactory.getLogger(TypeHandicapServiceImpl.class);

    private final TypeHandicapRepository typeHandicapRepository;

    private final TypeHandicapMapper typeHandicapMapper;

    private final TypeHandicapSearchRepository typeHandicapSearchRepository;

    public TypeHandicapServiceImpl(
        TypeHandicapRepository typeHandicapRepository,
        TypeHandicapMapper typeHandicapMapper,
        TypeHandicapSearchRepository typeHandicapSearchRepository
    ) {
        this.typeHandicapRepository = typeHandicapRepository;
        this.typeHandicapMapper = typeHandicapMapper;
        this.typeHandicapSearchRepository = typeHandicapSearchRepository;
    }

    @Override
    public TypeHandicapDTO save(TypeHandicapDTO typeHandicapDTO) {
        log.debug("Requête pour enregistrer TypeHandicap : {}", typeHandicapDTO);

        validateData(typeHandicapDTO.getLibelleTypeHandicap(), typeHandicapDTO.getId());

        TypeHandicap typeHandicap = typeHandicapMapper.toEntity(typeHandicapDTO);
        typeHandicap = typeHandicapRepository.save(typeHandicap);
        TypeHandicapDTO result = typeHandicapMapper.toDto(typeHandicap);
        typeHandicapSearchRepository.index(typeHandicap);
        return result;
    }

    @Override
    public TypeHandicapDTO update(TypeHandicapDTO typeHandicapDTO) {
        log.debug("Requête pour mettre à jour TypeHandicap : {}", typeHandicapDTO);

        validateData(typeHandicapDTO.getLibelleTypeHandicap(), typeHandicapDTO.getId());

        TypeHandicap typeHandicap = typeHandicapMapper.toEntity(typeHandicapDTO);
        typeHandicap = typeHandicapRepository.save(typeHandicap);
        TypeHandicapDTO result = typeHandicapMapper.toDto(typeHandicap);
        typeHandicapSearchRepository.index(typeHandicap);
        return result;
    }

    @Override
    public Optional<TypeHandicapDTO> partialUpdate(TypeHandicapDTO typeHandicapDTO) {
        log.debug("Requête pour mettre à jour partiellement TypeHandicap : {}", typeHandicapDTO);

        validateData(typeHandicapDTO.getLibelleTypeHandicap(), typeHandicapDTO.getId());

        return typeHandicapRepository
            .findById(typeHandicapDTO.getId())
            .map(existingTypeHandicap -> {
                typeHandicapMapper.partialUpdate(existingTypeHandicap, typeHandicapDTO);
                return existingTypeHandicap;
            })
            .map(typeHandicapRepository::save)
            .map(savedTypeHandicap -> {
                typeHandicapSearchRepository.index(savedTypeHandicap);
                return typeHandicapMapper.toDto(savedTypeHandicap);
            });
    }

    @Override
    @Transactional(readOnly = true)
    public Page<TypeHandicapDTO> findAll(Pageable pageable) {
        log.debug("Requête pour récupérer tous les TypeHandicaps");
        return typeHandicapRepository.findAll(pageable).map(typeHandicapMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<TypeHandicapDTO> findOne(Long id) {
        log.debug("Requête pour récupérer TypeHandicap : {}", id);
        return typeHandicapRepository.findById(id).map(typeHandicapMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Requête pour supprimer TypeHandicap : {}", id);
        typeHandicapRepository.deleteById(id);
        typeHandicapSearchRepository.deleteFromIndexById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<TypeHandicapDTO> search(String query, Pageable pageable) {
        log.debug("Requête pour rechercher une page de TypeHandicaps pour la requête {}", query);
        return typeHandicapSearchRepository.search(query, pageable).map(typeHandicapMapper::toDto);
    }

    private void validateData(String libelleTypeHandicap, Long id) {
        if (libelleTypeHandicap == null || libelleTypeHandicap.trim().isBlank()) {
            throw new BadRequestAlertException("Le libellé du TypeHandicap ne peut pas être vide", ENTITY_NAME, "libelleTypeHandicapVide");
        }

        Optional<TypeHandicap> existingTypeHandicap = typeHandicapRepository.findByLibelleTypeHandicapIgnoreCase(libelleTypeHandicap.trim());
        if (existingTypeHandicap.isPresent() && !existingTypeHandicap.get().getId().equals(id)) {
            throw new BadRequestAlertException("Un TypeHandicap avec le même nom existe déjà", ENTITY_NAME, "typeHandicapExiste");
        }
    }
}
