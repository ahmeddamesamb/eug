package sn.ugb.gir.service.mapper;

import org.mapstruct.*;
import sn.ugb.gir.domain.Ministere;
import sn.ugb.gir.service.dto.MinistereDTO;

/**
 * Mapper for the entity {@link Ministere} and its DTO {@link MinistereDTO}.
 */
@Mapper(componentModel = "spring")
public interface MinistereMapper extends EntityMapper<MinistereDTO, Ministere> {}
