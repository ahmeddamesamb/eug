package sn.ugb.gir.service.mapper;

import org.mapstruct.*;
import sn.ugb.gir.domain.DisciplineSportive;
import sn.ugb.gir.service.dto.DisciplineSportiveDTO;

/**
 * Mapper for the entity {@link DisciplineSportive} and its DTO {@link DisciplineSportiveDTO}.
 */
@Mapper(componentModel = "spring")
public interface DisciplineSportiveMapper extends EntityMapper<DisciplineSportiveDTO, DisciplineSportive> {}
