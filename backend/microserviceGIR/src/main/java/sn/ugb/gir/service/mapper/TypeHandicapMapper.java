package sn.ugb.gir.service.mapper;

import org.mapstruct.*;
import sn.ugb.gir.domain.TypeHandicap;
import sn.ugb.gir.service.dto.TypeHandicapDTO;

/**
 * Mapper for the entity {@link TypeHandicap} and its DTO {@link TypeHandicapDTO}.
 */
@Mapper(componentModel = "spring")
public interface TypeHandicapMapper extends EntityMapper<TypeHandicapDTO, TypeHandicap> {}
