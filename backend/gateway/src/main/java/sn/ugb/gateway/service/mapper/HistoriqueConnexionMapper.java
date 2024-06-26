package sn.ugb.gateway.service.mapper;

import org.mapstruct.*;
import sn.ugb.gateway.domain.HistoriqueConnexion;
import sn.ugb.gateway.domain.InfosUser;
import sn.ugb.gateway.service.dto.HistoriqueConnexionDTO;
import sn.ugb.gateway.service.dto.InfosUserDTO;

/**
 * Mapper for the entity {@link HistoriqueConnexion} and its DTO {@link HistoriqueConnexionDTO}.
 */
@Mapper(componentModel = "spring")
public interface HistoriqueConnexionMapper extends EntityMapper<HistoriqueConnexionDTO, HistoriqueConnexion> {
    @Mapping(target = "infoUser", source = "infoUser", qualifiedByName = "infosUserId")
    HistoriqueConnexionDTO toDto(HistoriqueConnexion s);

    @Named("infosUserId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    InfosUserDTO toDtoInfosUserId(InfosUser infosUser);
}
