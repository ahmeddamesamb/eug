package sn.ugb.gir.service.mapper;

import org.mapstruct.*;
import sn.ugb.gir.domain.TypeOperation;
import sn.ugb.gir.service.dto.TypeOperationDTO;

/**
 * Mapper for the entity {@link TypeOperation} and its DTO {@link TypeOperationDTO}.
 */
@Mapper(componentModel = "spring")
public interface TypeOperationMapper extends EntityMapper<TypeOperationDTO, TypeOperation> {}
