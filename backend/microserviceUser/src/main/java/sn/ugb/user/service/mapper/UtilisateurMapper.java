package sn.ugb.user.service.mapper;

import org.mapstruct.*;
import sn.ugb.user.domain.Utilisateur;
import sn.ugb.user.service.dto.UtilisateurDTO;

/**
 * Mapper for the entity {@link Utilisateur} and its DTO {@link UtilisateurDTO}.
 */
@Mapper(componentModel = "spring")
public interface UtilisateurMapper extends EntityMapper<UtilisateurDTO, Utilisateur> {}
