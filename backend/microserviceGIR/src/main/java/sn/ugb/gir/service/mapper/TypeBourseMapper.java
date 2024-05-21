package sn.ugb.gir.service.mapper;

import org.mapstruct.*;
import sn.ugb.gir.domain.TypeBourse;
import sn.ugb.gir.service.dto.TypeBourseDTO;

/**
 * Mapper for the entity {@link TypeBourse} and its DTO {@link TypeBourseDTO}.
 */
@Mapper(componentModel = "spring")
public interface TypeBourseMapper extends EntityMapper<TypeBourseDTO, TypeBourse> {}
