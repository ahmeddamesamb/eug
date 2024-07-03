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
@Mapper(componentModel = "spring" , uses = { EtudiantMapper.class, TypeHandicapMapper.class, TypeBourseMapper.class })
public interface InformationPersonnelleMapper extends EntityMapper<InformationPersonnelleDTO, InformationPersonnelle> {
    @Mapping(target = "etudiant", source = "etudiant")
    @Mapping(target = "typeHandicap", source = "typeHandicap")
    @Mapping(target = "typeBourse", source = "typeBourse")
    InformationPersonnelleDTO toDto(InformationPersonnelle s);

    @Named("etudiantId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "codeEtu", source = "codeEtu")
    @Mapping(target = "ine", source = "ine")
    @Mapping(target = "codeBU", source = "codeBU")
    @Mapping(target = "emailUGB", source = "emailUGB")
    @Mapping(target = "dateNaissEtu", source = "dateNaissEtu")
    @Mapping(target = "lieuNaissEtu", source = "lieuNaissEtu")
    @Mapping(target = "sexe", source = "sexe")
    @Mapping(target = "numDocIdentite", source = "numDocIdentite")
    @Mapping(target = "assimileYN", source = "assimileYN")
    @Mapping(target = "actifYN", source = "actifYN")
    EtudiantDTO toDtoEtudiantId(Etudiant etudiant);

    @Named("typeHandicapId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "libelleTypeHandicap", source = "libelleTypeHandicap")
    TypeHandicapDTO toDtoTypeHandicapId(TypeHandicap typeHandicap);

    @Named("typeBourseId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "libelleTypeBourse", source = "libelleTypeBourse")
    TypeBourseDTO toDtoTypeBourseId(TypeBourse typeBourse);
}
