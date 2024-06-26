package sn.ugb.gir.service.mapper;

import org.mapstruct.*;
import sn.ugb.gir.domain.Campagne;
import sn.ugb.gir.service.dto.CampagneDTO;

/**
 * Mapper for the entity {@link Campagne} and its DTO {@link CampagneDTO}.
 */
@Mapper(componentModel = "spring")
public interface CampagneMapper extends EntityMapper<CampagneDTO, Campagne> {}
