package sn.ugb.gir.service.mapper;

import org.mapstruct.*;
import sn.ugb.gir.domain.TypeFormation;
import sn.ugb.gir.service.dto.TypeFormationDTO;

/**
 * Mapper for the entity {@link TypeFormation} and its DTO {@link TypeFormationDTO}.
 */
@Mapper(componentModel = "spring")
public interface TypeFormationMapper extends EntityMapper<TypeFormationDTO, TypeFormation> {}
