package sn.ugb.gir.service.mapper;

import org.mapstruct.*;
import sn.ugb.gir.domain.Formation;
import sn.ugb.gir.domain.FormationPrivee;
import sn.ugb.gir.service.dto.FormationDTO;
import sn.ugb.gir.service.dto.FormationPriveeDTO;

/**
 * Mapper for the entity {@link FormationPrivee} and its DTO {@link FormationPriveeDTO}.
 */
@Mapper(componentModel = "spring")
public interface FormationPriveeMapper extends EntityMapper<FormationPriveeDTO, FormationPrivee> {
    @Mapping(target = "formation", source = "formation", qualifiedByName = "formationId")
    FormationPriveeDTO toDto(FormationPrivee s);

    @Named("formationId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    FormationDTO toDtoFormationId(Formation formation);
}
