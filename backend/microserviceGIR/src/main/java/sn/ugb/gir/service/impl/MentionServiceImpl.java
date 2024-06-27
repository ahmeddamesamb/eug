package sn.ugb.gir.service.impl;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sn.ugb.gir.domain.Mention;
import sn.ugb.gir.repository.MentionRepository;
import sn.ugb.gir.repository.search.MentionSearchRepository;
import sn.ugb.gir.service.MentionService;
import sn.ugb.gir.service.dto.MentionDTO;
import sn.ugb.gir.service.mapper.MentionMapper;
import sn.ugb.gir.web.rest.errors.BadRequestAlertException;

import static org.hibernate.id.IdentifierGenerator.ENTITY_NAME;

/**
 * Service Implementation for managing {@link sn.ugb.gir.domain.Mention}.
 */
@Service
@Transactional
public class MentionServiceImpl implements MentionService {

    private final Logger log = LoggerFactory.getLogger(MentionServiceImpl.class);

    private final MentionRepository mentionRepository;

    private final MentionMapper mentionMapper;

    private final MentionSearchRepository mentionSearchRepository;

    public MentionServiceImpl(
        MentionRepository mentionRepository,
        MentionMapper mentionMapper,
        MentionSearchRepository mentionSearchRepository
    ) {
        this.mentionRepository = mentionRepository;
        this.mentionMapper = mentionMapper;
        this.mentionSearchRepository = mentionSearchRepository;
    }

    @Override
    public MentionDTO save(MentionDTO mentionDTO) {
        log.debug("Request to save Mention : {}", mentionDTO);

        validateData(mentionDTO);

        Mention mention = mentionMapper.toEntity(mentionDTO);
        mention = mentionRepository.save(mention);
        MentionDTO result = mentionMapper.toDto(mention);
        mentionSearchRepository.index(mention);
        return result;
    }

    @Override
    public MentionDTO update(MentionDTO mentionDTO) {
        log.debug("Request to update Mention : {}", mentionDTO);

        validateData(mentionDTO);

        Mention mention = mentionMapper.toEntity(mentionDTO);
        mention = mentionRepository.save(mention);
        MentionDTO result = mentionMapper.toDto(mention);
        mentionSearchRepository.index(mention);
        return result;
    }

    @Override
    public Optional<MentionDTO> partialUpdate(MentionDTO mentionDTO) {
        log.debug("Request to partially update Mention : {}", mentionDTO);

        validateData(mentionDTO);

        return mentionRepository
            .findById(mentionDTO.getId())
            .map(existingMention -> {
                mentionMapper.partialUpdate(existingMention, mentionDTO);

                return existingMention;
            })
            .map(mentionRepository::save)
            .map(savedMention -> {
                mentionSearchRepository.index(savedMention);
                return savedMention;
            })
            .map(mentionMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<MentionDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Mentions");
        return mentionRepository.findAll(pageable).map(mentionMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<MentionDTO> findOne(Long id) {
        log.debug("Request to get Mention : {}", id);
        return mentionRepository.findById(id).map(mentionMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Mention : {}", id);
        mentionRepository.deleteById(id);
        mentionSearchRepository.deleteFromIndexById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<MentionDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Mentions for query {}", query);
        return mentionSearchRepository.search(query, pageable).map(mentionMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<MentionDTO> getAllMentionByUfr(Long ufrId, Pageable pageable) {
        log.debug("Request to get all Mentions by UFR ID : {}", ufrId);
        return mentionRepository.findByDomaineUfrsId(ufrId, pageable).map(mentionMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<MentionDTO> getAllMentionByUniversite(Long universiteId, Pageable pageable) {
        log.debug("Request to get all Mentions by Universite ID : {}", universiteId);
        return mentionRepository.findByDomaineUfrsUniversiteId(universiteId, pageable).map(mentionMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<MentionDTO> getAllMentionByMinistere(Long ministereId, Pageable pageable) {
        log.debug("Request to get all Mentions by Ministere ID : {}", ministereId);
        return mentionRepository.findByDomaineUfrsUniversiteMinistereId(ministereId, pageable).map(mentionMapper::toDto);
    }
    private void validateData(MentionDTO mentionDTO) {
        if (mentionDTO.getLibelleMention().isBlank()){
            throw new BadRequestAlertException("Le libellé ne peut pas être vide.", ENTITY_NAME, "LibelleMentionNotNull");
        }

        Optional<Mention> existingMention = mentionRepository.findByLibelleMentionIgnoreCase(mentionDTO.getLibelleMention());
        if (existingMention.isPresent() && !existingMention.get().getId().equals(mentionDTO.getId())) {
            throw new BadRequestAlertException("Une ufr avec le même libellé existe.", ENTITY_NAME, "LibelleMentionExist");
        }
    }
}
