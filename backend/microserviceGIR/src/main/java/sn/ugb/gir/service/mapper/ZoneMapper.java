package sn.ugb.gir.service.mapper;

import org.mapstruct.*;
import sn.ugb.gir.domain.Zone;
import sn.ugb.gir.service.dto.ZoneDTO;

/**
 * Mapper for the entity {@link Zone} and its DTO {@link ZoneDTO}.
 */
@Mapper(componentModel = "spring")
public interface ZoneMapper extends EntityMapper<ZoneDTO, Zone> {}
