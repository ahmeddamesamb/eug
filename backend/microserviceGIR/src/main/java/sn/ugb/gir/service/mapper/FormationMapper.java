package sn.ugb.gir.service.mapper;

import org.mapstruct.*;
import sn.ugb.gir.domain.Departement;
import sn.ugb.gir.domain.Formation;
import sn.ugb.gir.domain.Niveau;
import sn.ugb.gir.domain.Specialite;
import sn.ugb.gir.domain.TypeFormation;
import sn.ugb.gir.service.dto.DepartementDTO;
import sn.ugb.gir.service.dto.FormationDTO;
import sn.ugb.gir.service.dto.NiveauDTO;
import sn.ugb.gir.service.dto.SpecialiteDTO;
import sn.ugb.gir.service.dto.TypeFormationDTO;

/**
 * Mapper for the entity {@link Formation} and its DTO {@link FormationDTO}.
 */
@Mapper(componentModel = "spring" , uses = {TypeFormationMapper.class, NiveauMapper.class, SpecialiteMapper.class, DepartementMapper.class})
public interface FormationMapper extends EntityMapper<FormationDTO, Formation> {
    @Mapping(target = "typeFormation", source = "typeFormation", qualifiedByName = "typeFormationId")
    @Mapping(target = "niveau", source = "niveau", qualifiedByName = "niveauId")
    @Mapping(target = "specialite", source = "specialite", qualifiedByName = "specialiteId")
    @Mapping(target = "departement", source = "departement", qualifiedByName = "departementId")
    FormationDTO toDto(Formation s);

    @Named("typeFormationId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "libelleTypeFormation", source = "libelleTypeFormation")
    TypeFormationDTO toDtoTypeFormationId(TypeFormation typeFormation);

    @Named("niveauId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "libelleNiveau", source = "libelleNiveau")
    @Mapping(target = "codeNiveau", source = "codeNiveau")
    @Mapping(target = "anneeEtude", source = "anneeEtude")
    @Mapping(target = "actifYN", source = "actifYN")
    NiveauDTO toDtoNiveauId(Niveau niveau);

    @Named("specialiteId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "nomSpecialites", source = "nomSpecialites")
    @Mapping(target = "sigleSpecialites", source = "sigleSpecialites")
    @Mapping(target = "specialiteParticulierYN", source = "specialiteParticulierYN")
    @Mapping(target = "specialitesPayanteYN", source = "specialitesPayanteYN")
    @Mapping(target = "actifYN", source = "actifYN")
    SpecialiteDTO toDtoSpecialiteId(Specialite specialite);

    @Named("departementId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "nomDepatement", source = "nomDepatement")
    @Mapping(target = "actifYN", source = "actifYN")
    DepartementDTO toDtoDepartementId(Departement departement);
}
