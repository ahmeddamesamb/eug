package sn.ugb.gir.service.mapper;

import org.mapstruct.*;
import sn.ugb.gir.domain.TypeSelection;
import sn.ugb.gir.service.dto.TypeSelectionDTO;

/**
 * Mapper for the entity {@link TypeSelection} and its DTO {@link TypeSelectionDTO}.
 */
@Mapper(componentModel = "spring")
public interface TypeSelectionMapper extends EntityMapper<TypeSelectionDTO, TypeSelection> {}
