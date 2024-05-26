package sn.ugb.gir.service.mapper;

import org.mapstruct.*;
import sn.ugb.gir.domain.Serie;
import sn.ugb.gir.service.dto.SerieDTO;

/**
 * Mapper for the entity {@link Serie} and its DTO {@link SerieDTO}.
 */
@Mapper(componentModel = "spring")
public interface SerieMapper extends EntityMapper<SerieDTO, Serie> {}
