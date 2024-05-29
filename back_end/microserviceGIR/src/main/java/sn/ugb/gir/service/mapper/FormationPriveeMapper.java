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
    @Mapping(target = "fraisDossierYN", source = "fraisDossierYN")
    @Mapping(target = "classeDiplomanteYN", source = "classeDiplomanteYN")
    @Mapping(target = "libelleDiplome", source = "libelleDiplome")
    @Mapping(target = "codeFormation", source = "codeFormation")
    @Mapping(target = "nbreCreditsMin", source = "nbreCreditsMin")
    @Mapping(target = "estParcoursYN", source = "estParcoursYN")
    @Mapping(target = "lmdYN", source = "lmdYN")
    @Mapping(target = "typeFormation", source = "typeFormation")
    FormationDTO toDtoFormationId(Formation formation);
}
