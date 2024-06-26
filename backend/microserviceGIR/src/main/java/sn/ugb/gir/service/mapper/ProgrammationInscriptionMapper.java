package sn.ugb.gir.service.mapper;

import org.mapstruct.*;
import sn.ugb.gir.domain.AnneeAcademique;
import sn.ugb.gir.domain.Campagne;
import sn.ugb.gir.domain.Formation;
import sn.ugb.gir.domain.ProgrammationInscription;
import sn.ugb.gir.service.dto.AnneeAcademiqueDTO;
import sn.ugb.gir.service.dto.CampagneDTO;
import sn.ugb.gir.service.dto.FormationDTO;
import sn.ugb.gir.service.dto.ProgrammationInscriptionDTO;

/**
 * Mapper for the entity {@link ProgrammationInscription} and its DTO {@link ProgrammationInscriptionDTO}.
 */
@Mapper(componentModel = "spring")
public interface ProgrammationInscriptionMapper extends EntityMapper<ProgrammationInscriptionDTO, ProgrammationInscription> {
    @Mapping(target = "anneeAcademique", source = "anneeAcademique", qualifiedByName = "anneeAcademiqueId")
    @Mapping(target = "formation", source = "formation", qualifiedByName = "formationId")
    @Mapping(target = "campagne", source = "campagne", qualifiedByName = "campagneId")
    ProgrammationInscriptionDTO toDto(ProgrammationInscription s);

    @Named("anneeAcademiqueId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    AnneeAcademiqueDTO toDtoAnneeAcademiqueId(AnneeAcademique anneeAcademique);

    @Named("formationId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    FormationDTO toDtoFormationId(Formation formation);

    @Named("campagneId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    CampagneDTO toDtoCampagneId(Campagne campagne);
}
