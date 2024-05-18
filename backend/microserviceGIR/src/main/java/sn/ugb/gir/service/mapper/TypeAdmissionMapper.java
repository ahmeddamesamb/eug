package sn.ugb.gir.service.mapper;

import org.mapstruct.*;
import sn.ugb.gir.domain.TypeAdmission;
import sn.ugb.gir.service.dto.TypeAdmissionDTO;

/**
 * Mapper for the entity {@link TypeAdmission} and its DTO {@link TypeAdmissionDTO}.
 */
@Mapper(componentModel = "spring")
public interface TypeAdmissionMapper extends EntityMapper<TypeAdmissionDTO, TypeAdmission> {}
