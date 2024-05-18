package sn.ugb.gir.service.mapper;

import org.mapstruct.*;
import sn.ugb.gir.domain.Doctorat;
import sn.ugb.gir.service.dto.DoctoratDTO;

/**
 * Mapper for the entity {@link Doctorat} and its DTO {@link DoctoratDTO}.
 */
@Mapper(componentModel = "spring")
public interface DoctoratMapper extends EntityMapper<DoctoratDTO, Doctorat> {}
