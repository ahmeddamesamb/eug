package sn.ugb.aide.service.mapper;

import org.mapstruct.*;
import sn.ugb.aide.domain.Ressource;
import sn.ugb.aide.service.dto.RessourceDTO;

/**
 * Mapper for the entity {@link Ressource} and its DTO {@link RessourceDTO}.
 */
@Mapper(componentModel = "spring")
public interface RessourceMapper extends EntityMapper<RessourceDTO, Ressource> {}
