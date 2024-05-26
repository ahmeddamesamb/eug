package sn.ugb.deliberation.service.mapper;

import org.mapstruct.*;
import sn.ugb.deliberation.domain.Deliberation;
import sn.ugb.deliberation.service.dto.DeliberationDTO;

/**
 * Mapper for the entity {@link Deliberation} and its DTO {@link DeliberationDTO}.
 */
@Mapper(componentModel = "spring")
public interface DeliberationMapper extends EntityMapper<DeliberationDTO, Deliberation> {}
