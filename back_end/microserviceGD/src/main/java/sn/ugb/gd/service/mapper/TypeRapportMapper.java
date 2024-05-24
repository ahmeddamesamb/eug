package sn.ugb.gd.service.mapper;

import org.mapstruct.*;
import sn.ugb.gd.domain.TypeRapport;
import sn.ugb.gd.service.dto.TypeRapportDTO;

/**
 * Mapper for the entity {@link TypeRapport} and its DTO {@link TypeRapportDTO}.
 */
@Mapper(componentModel = "spring")
public interface TypeRapportMapper extends EntityMapper<TypeRapportDTO, TypeRapport> {}
