package sn.ugb.gir.service.mapper;

import org.mapstruct.*;
import sn.ugb.gir.domain.AnneeAcademique;
import sn.ugb.gir.domain.Etudiant;
import sn.ugb.gir.domain.InscriptionAdministrative;
import sn.ugb.gir.domain.TypeAdmission;
import sn.ugb.gir.service.dto.AnneeAcademiqueDTO;
import sn.ugb.gir.service.dto.EtudiantDTO;
import sn.ugb.gir.service.dto.InscriptionAdministrativeDTO;
import sn.ugb.gir.service.dto.TypeAdmissionDTO;

/**
 * Mapper for the entity {@link InscriptionAdministrative} and its DTO {@link InscriptionAdministrativeDTO}.
 */
@Mapper(componentModel = "spring")
public interface InscriptionAdministrativeMapper extends EntityMapper<InscriptionAdministrativeDTO, InscriptionAdministrative> {
    @Mapping(target = "typeAdmission", source = "typeAdmission", qualifiedByName = "typeAdmissionId")
    @Mapping(target = "anneeAcademique", source = "anneeAcademique", qualifiedByName = "anneeAcademiqueId")
    @Mapping(target = "etudiant", source = "etudiant", qualifiedByName = "etudiantId")
    InscriptionAdministrativeDTO toDto(InscriptionAdministrative s);

    @Named("typeAdmissionId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    TypeAdmissionDTO toDtoTypeAdmissionId(TypeAdmission typeAdmission);

    @Named("anneeAcademiqueId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    AnneeAcademiqueDTO toDtoAnneeAcademiqueId(AnneeAcademique anneeAcademique);

    @Named("etudiantId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    EtudiantDTO toDtoEtudiantId(Etudiant etudiant);
}
