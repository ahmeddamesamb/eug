package sn.ugb.gir.service.mapper;

import org.mapstruct.*;
import sn.ugb.gir.domain.Formation;
import sn.ugb.gir.domain.FormationValide;
import sn.ugb.gir.service.dto.FormationDTO;
import sn.ugb.gir.service.dto.FormationValideDTO;

/**
 * Mapper for the entity {@link FormationValide} and its DTO {@link FormationValideDTO}.
 */
@Mapper(componentModel = "spring")
public interface FormationValideMapper extends EntityMapper<FormationValideDTO, FormationValide> {
    @Mapping(target = "formation", source = "formation", qualifiedByName = "formationId")
    FormationValideDTO toDto(FormationValide s);

    @Named("formationId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    FormationDTO toDtoFormationId(Formation formation);
}
