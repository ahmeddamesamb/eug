package sn.ugb.gateway.service.mapper;

import org.mapstruct.*;
import sn.ugb.gateway.domain.Profil;
import sn.ugb.gateway.service.dto.ProfilDTO;

/**
 * Mapper for the entity {@link Profil} and its DTO {@link ProfilDTO}.
 */
@Mapper(componentModel = "spring")
public interface ProfilMapper extends EntityMapper<ProfilDTO, Profil> {}
