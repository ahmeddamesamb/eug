package sn.ugb.aide.service.mapper;

import org.mapstruct.*;
import sn.ugb.aide.domain.RessourceAide;
import sn.ugb.aide.service.dto.RessourceAideDTO;

/**
 * Mapper for the entity {@link RessourceAide} and its DTO {@link RessourceAideDTO}.
 */
@Mapper(componentModel = "spring")
public interface RessourceAideMapper extends EntityMapper<RessourceAideDTO, RessourceAide> {}
