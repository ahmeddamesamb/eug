package sn.ugb.gp.service.mapper;

import org.mapstruct.*;
import sn.ugb.gp.domain.Enseignement;
import sn.ugb.gp.service.dto.EnseignementDTO;

/**
 * Mapper for the entity {@link Enseignement} and its DTO {@link EnseignementDTO}.
 */
@Mapper(componentModel = "spring")
public interface EnseignementMapper extends EntityMapper<EnseignementDTO, Enseignement> {}
