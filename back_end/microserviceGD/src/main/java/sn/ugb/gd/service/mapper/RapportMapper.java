package sn.ugb.gd.service.mapper;

import org.mapstruct.*;
import sn.ugb.gd.domain.Rapport;
import sn.ugb.gd.service.dto.RapportDTO;

/**
 * Mapper for the entity {@link Rapport} and its DTO {@link RapportDTO}.
 */
@Mapper(componentModel = "spring")
public interface RapportMapper extends EntityMapper<RapportDTO, Rapport> {}
