package sn.ugb.gateway.service.mapper;

import org.mapstruct.*;
import sn.ugb.gateway.domain.ServiceUser;
import sn.ugb.gateway.service.dto.ServiceUserDTO;

/**
 * Mapper for the entity {@link ServiceUser} and its DTO {@link ServiceUserDTO}.
 */
@Mapper(componentModel = "spring")
public interface ServiceUserMapper extends EntityMapper<ServiceUserDTO, ServiceUser> {}
