package sn.ugb.gir.service.mapper;

import org.mapstruct.*;
import sn.ugb.gir.domain.Etudiant;
import sn.ugb.gir.domain.InformationPersonnelle;
import sn.ugb.gir.domain.TypeBourse;
import sn.ugb.gir.domain.TypeHandicap;
import sn.ugb.gir.service.dto.EtudiantDTO;
import sn.ugb.gir.service.dto.InformationPersonnelleDTO;
import sn.ugb.gir.service.dto.TypeBourseDTO;
import sn.ugb.gir.service.dto.TypeHandicapDTO;

/**
 * Mapper for the entity {@link InformationPersonnelle} and its DTO {@link InformationPersonnelleDTO}.
 */
@Mapper(componentModel = "spring")
public interface InformationPersonnelleMapper extends EntityMapper<InformationPersonnelleDTO, InformationPersonnelle> {
    @Mapping(target = "etudiant", source = "etudiant", qualifiedByName = "etudiantId")
    @Mapping(target = "typeHandique", source = "typeHandique", qualifiedByName = "typeHandicapId")
    @Mapping(target = "typeBourse", source = "typeBourse", qualifiedByName = "typeBourseId")
    InformationPersonnelleDTO toDto(InformationPersonnelle s);

    @Named("etudiantId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    EtudiantDTO toDtoEtudiantId(Etudiant etudiant);

    @Named("typeHandicapId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    TypeHandicapDTO toDtoTypeHandicapId(TypeHandicap typeHandicap);

    @Named("typeBourseId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    TypeBourseDTO toDtoTypeBourseId(TypeBourse typeBourse);
}
