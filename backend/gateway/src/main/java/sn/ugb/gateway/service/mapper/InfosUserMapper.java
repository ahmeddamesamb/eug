package sn.ugb.gateway.service.mapper;

import org.mapstruct.*;
import sn.ugb.gateway.domain.InfosUser;
import sn.ugb.gateway.domain.User;
import sn.ugb.gateway.service.dto.InfosUserDTO;
import sn.ugb.gateway.service.dto.UserDTO;

/**
 * Mapper for the entity {@link InfosUser} and its DTO {@link InfosUserDTO}.
 */
@Mapper(componentModel = "spring", uses = {UserMapper.class})
public interface InfosUserMapper extends EntityMapper<InfosUserDTO, InfosUser> {
    @Mapping(target = "user", source = "user", qualifiedByName = "userLogin")
    InfosUserDTO toDto(InfosUser s);

    @Named("userLogin")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "login", source = "login")
    UserDTO toDtoUserLogin(User user);
}
