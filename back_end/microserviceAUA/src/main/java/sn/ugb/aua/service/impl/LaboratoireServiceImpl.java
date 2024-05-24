package sn.ugb.aua.service.impl;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sn.ugb.aua.domain.Laboratoire;
import sn.ugb.aua.repository.LaboratoireRepository;
import sn.ugb.aua.service.LaboratoireService;
import sn.ugb.aua.service.dto.LaboratoireDTO;
import sn.ugb.aua.service.mapper.LaboratoireMapper;

/**
 * Service Implementation for managing {@link sn.ugb.aua.domain.Laboratoire}.
 */
@Service
@Transactional
public class LaboratoireServiceImpl implements LaboratoireService {

    private final Logger log = LoggerFactory.getLogger(LaboratoireServiceImpl.class);

    private final LaboratoireRepository laboratoireRepository;

    private final LaboratoireMapper laboratoireMapper;

    public LaboratoireServiceImpl(LaboratoireRepository laboratoireRepository, LaboratoireMapper laboratoireMapper) {
        this.laboratoireRepository = laboratoireRepository;
        this.laboratoireMapper = laboratoireMapper;
    }

    @Override
    public LaboratoireDTO save(LaboratoireDTO laboratoireDTO) {
        log.debug("Request to save Laboratoire : {}", laboratoireDTO);
        Laboratoire laboratoire = laboratoireMapper.toEntity(laboratoireDTO);
        laboratoire = laboratoireRepository.save(laboratoire);
        return laboratoireMapper.toDto(laboratoire);
    }

    @Override
    public LaboratoireDTO update(LaboratoireDTO laboratoireDTO) {
        log.debug("Request to update Laboratoire : {}", laboratoireDTO);
        Laboratoire laboratoire = laboratoireMapper.toEntity(laboratoireDTO);
        laboratoire = laboratoireRepository.save(laboratoire);
        return laboratoireMapper.toDto(laboratoire);
    }

    @Override
    public Optional<LaboratoireDTO> partialUpdate(LaboratoireDTO laboratoireDTO) {
        log.debug("Request to partially update Laboratoire : {}", laboratoireDTO);

        return laboratoireRepository
            .findById(laboratoireDTO.getId())
            .map(existingLaboratoire -> {
                laboratoireMapper.partialUpdate(existingLaboratoire, laboratoireDTO);

                return existingLaboratoire;
            })
            .map(laboratoireRepository::save)
            .map(laboratoireMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<LaboratoireDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Laboratoires");
        return laboratoireRepository.findAll(pageable).map(laboratoireMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<LaboratoireDTO> findOne(Long id) {
        log.debug("Request to get Laboratoire : {}", id);
        return laboratoireRepository.findById(id).map(laboratoireMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Laboratoire : {}", id);
        laboratoireRepository.deleteById(id);
    }
}
