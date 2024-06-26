package sn.ugb.gir.service.mapper;

import org.mapstruct.*;
import sn.ugb.gir.domain.Cycle;
import sn.ugb.gir.service.dto.CycleDTO;

/**
 * Mapper for the entity {@link Cycle} and its DTO {@link CycleDTO}.
 */
@Mapper(componentModel = "spring")
public interface CycleMapper extends EntityMapper<CycleDTO, Cycle> {}
