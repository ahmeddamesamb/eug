package sn.ugb.gir.service.mapper;

import org.mapstruct.*;
import sn.ugb.gir.domain.InscriptionAdministrative;
import sn.ugb.gir.domain.ProcessusInscriptionAdministrative;
import sn.ugb.gir.service.dto.InscriptionAdministrativeDTO;
import sn.ugb.gir.service.dto.ProcessusInscriptionAdministrativeDTO;

/**
 * Mapper for the entity {@link ProcessusInscriptionAdministrative} and its DTO {@link ProcessusInscriptionAdministrativeDTO}.
 */
@Mapper(componentModel = "spring", uses = { EtudiantMapper.class,AnneeAcademiqueMapper.class })
public interface ProcessusInscriptionAdministrativeMapper
    extends EntityMapper<ProcessusInscriptionAdministrativeDTO, ProcessusInscriptionAdministrative> {
    @Mapping(target = "inscriptionAdministrative", source = "inscriptionAdministrative", qualifiedByName = "inscriptionAdministrativeId")
    ProcessusInscriptionAdministrativeDTO toDto(ProcessusInscriptionAdministrative s);

    @Named("inscriptionAdministrativeId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "nouveauInscritYN", source = "nouveauInscritYN")
    @Mapping(target = "repriseYN", source = "repriseYN")
    @Mapping(target = "autoriseYN", source = "autoriseYN")
    @Mapping(target = "ordreInscription", source = "ordreInscription")
    @Mapping(target = "typeAdmission", source = "typeAdmission")
    @Mapping(target = "anneeAcademique", source = "anneeAcademique")
    @Mapping(target = "etudiant", source = "etudiant")

    InscriptionAdministrativeDTO toDtoInscriptionAdministrativeId(InscriptionAdministrative inscriptionAdministrative);
}
