package sn.ugb.gateway.service.mapper;

import org.mapstruct.*;
import sn.ugb.gateway.domain.Ressource;
import sn.ugb.gateway.service.dto.RessourceDTO;

/**
 * Mapper for the entity {@link Ressource} and its DTO {@link RessourceDTO}.
 */
@Mapper(componentModel = "spring")
public interface RessourceMapper extends EntityMapper<RessourceDTO, Ressource> {}
