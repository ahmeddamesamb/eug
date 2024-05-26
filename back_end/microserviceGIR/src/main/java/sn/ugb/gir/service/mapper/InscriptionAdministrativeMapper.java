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
@Mapper(componentModel = "spring" , uses = { TypeAdmissionMapper.class, AnneeAcademiqueMapper.class, EtudiantMapper.class })
public interface InscriptionAdministrativeMapper extends EntityMapper<InscriptionAdministrativeDTO, InscriptionAdministrative> {
    @Mapping(target = "typeAdmission", source = "typeAdmission")
    @Mapping(target = "anneeAcademique", source = "anneeAcademique")
    @Mapping(target = "etudiant", source = "etudiant")
    InscriptionAdministrativeDTO toDto(InscriptionAdministrative s);

    @Named("typeAdmissionId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "libelleTypeAdmission", source = "libelleTypeAdmission")
    TypeAdmissionDTO toDtoTypeAdmissionId(TypeAdmission typeAdmission);

    @Named("anneeAcademiqueId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "libelleAnneeAcademique", source = "libelleAnneeAcademique")
    @Mapping(target = "anneeAc", source = "anneeAc")
    @Mapping(target = "anneeCouranteYN", source = "anneeCouranteYN")
    AnneeAcademiqueDTO toDtoAnneeAcademiqueId(AnneeAcademique anneeAcademique);

    @Named("etudiantId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "codeEtu", source = "codeEtu")
    @Mapping(target = "ine", source = "ine")
    @Mapping(target = "codeBU", source = "codeBU")
    @Mapping(target = "emailUGB", source = "emailUGB")
    @Mapping(target = "dateNaissEtu", source = "dateNaissEtu")
    @Mapping(target = "numDocIdentite", source = "numDocIdentite")
    @Mapping(target = "assimileYN", source = "assimileYN")
    @Mapping(target = "exonereYN", source = "exonereYN")
    EtudiantDTO toDtoEtudiantId(Etudiant etudiant);
}
