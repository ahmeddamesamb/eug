package sn.ugb.aclc.service.mapper;

import org.mapstruct.*;
import sn.ugb.aclc.domain.Candidat;
import sn.ugb.aclc.service.dto.CandidatDTO;

/**
 * Mapper for the entity {@link Candidat} and its DTO {@link CandidatDTO}.
 */
@Mapper(componentModel = "spring")
public interface CandidatMapper extends EntityMapper<CandidatDTO, Candidat> {}
