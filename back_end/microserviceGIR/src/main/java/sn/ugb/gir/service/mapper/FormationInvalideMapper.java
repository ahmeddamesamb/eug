package sn.ugb.gir.service.mapper;

import org.mapstruct.*;
import sn.ugb.gir.domain.AnneeAcademique;
import sn.ugb.gir.domain.Formation;
import sn.ugb.gir.domain.FormationInvalide;
import sn.ugb.gir.service.dto.AnneeAcademiqueDTO;
import sn.ugb.gir.service.dto.FormationDTO;
import sn.ugb.gir.service.dto.FormationInvalideDTO;

/**
 * Mapper for the entity {@link FormationInvalide} and its DTO {@link FormationInvalideDTO}.
 */
@Mapper(componentModel = "spring")
public interface FormationInvalideMapper extends EntityMapper<FormationInvalideDTO, FormationInvalide> {
    @Mapping(target = "formation", source = "formation", qualifiedByName = "formationId")
    @Mapping(target = "anneAcademique", source = "anneAcademique", qualifiedByName = "anneeAcademiqueId")
    FormationInvalideDTO toDto(FormationInvalide s);

    @Named("formationId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    FormationDTO toDtoFormationId(Formation formation);

    @Named("anneeAcademiqueId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    AnneeAcademiqueDTO toDtoAnneeAcademiqueId(AnneeAcademique anneeAcademique);
}
