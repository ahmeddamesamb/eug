package sn.ugb.gir.service.mapper;

import org.mapstruct.*;
import sn.ugb.gir.domain.Operation;
import sn.ugb.gir.domain.TypeOperation;
import sn.ugb.gir.service.dto.OperationDTO;
import sn.ugb.gir.service.dto.TypeOperationDTO;

/**
 * Mapper for the entity {@link Operation} and its DTO {@link OperationDTO}.
 */
@Mapper(componentModel = "spring")
public interface OperationMapper extends EntityMapper<OperationDTO, Operation> {
    @Mapping(target = "typeOperation", source = "typeOperation", qualifiedByName = "typeOperationId")
    OperationDTO toDto(Operation s);

    @Named("typeOperationId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    TypeOperationDTO toDtoTypeOperationId(TypeOperation typeOperation);
}
