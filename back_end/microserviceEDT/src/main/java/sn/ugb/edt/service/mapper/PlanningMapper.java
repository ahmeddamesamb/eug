package sn.ugb.edt.service.mapper;

import org.mapstruct.*;
import sn.ugb.edt.domain.Planning;
import sn.ugb.edt.service.dto.PlanningDTO;

/**
 * Mapper for the entity {@link Planning} and its DTO {@link PlanningDTO}.
 */
@Mapper(componentModel = "spring")
public interface PlanningMapper extends EntityMapper<PlanningDTO, Planning> {}
