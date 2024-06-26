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
@Mapper(componentModel = "spring")
public interface FormationMapper extends EntityMapper<FormationDTO, Formation> {
    @Mapping(target = "typeFormation", source = "typeFormation", qualifiedByName = "typeFormationId")
    @Mapping(target = "niveau", source = "niveau", qualifiedByName = "niveauId")
    @Mapping(target = "specialite", source = "specialite", qualifiedByName = "specialiteId")
    @Mapping(target = "departement", source = "departement", qualifiedByName = "departementId")
    FormationDTO toDto(Formation s);

    @Named("typeFormationId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    TypeFormationDTO toDtoTypeFormationId(TypeFormation typeFormation);

    @Named("niveauId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    NiveauDTO toDtoNiveauId(Niveau niveau);

    @Named("specialiteId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    SpecialiteDTO toDtoSpecialiteId(Specialite specialite);

    @Named("departementId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    DepartementDTO toDtoDepartementId(Departement departement);
}
