package sn.ugb.gateway.service.mapper;

import org.mapstruct.*;
import sn.ugb.gateway.domain.BlocFonctionnel;
import sn.ugb.gateway.domain.ServiceUser;
import sn.ugb.gateway.service.dto.BlocFonctionnelDTO;
import sn.ugb.gateway.service.dto.ServiceUserDTO;

/**
 * Mapper for the entity {@link BlocFonctionnel} and its DTO {@link BlocFonctionnelDTO}.
 */
@Mapper(componentModel = "spring")
public interface BlocFonctionnelMapper extends EntityMapper<BlocFonctionnelDTO, BlocFonctionnel> {
    @Mapping(target = "service", source = "service", qualifiedByName = "serviceUserId")
    BlocFonctionnelDTO toDto(BlocFonctionnel s);

    @Named("serviceUserId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    ServiceUserDTO toDtoServiceUserId(ServiceUser serviceUser);
}
