package sn.ugb.gir.service.mapper;

import org.mapstruct.*;
import sn.ugb.gir.domain.Formation;
import sn.ugb.gir.domain.Niveau;
import sn.ugb.gir.domain.Specialite;
import sn.ugb.gir.service.dto.FormationDTO;
import sn.ugb.gir.service.dto.NiveauDTO;
import sn.ugb.gir.service.dto.SpecialiteDTO;

/**
 * Mapper for the entity {@link Formation} and its DTO {@link FormationDTO}.
 */
@Mapper(componentModel = "spring")
public interface FormationMapper extends EntityMapper<FormationDTO, Formation> {
    @Mapping(target = "specialite", source = "specialite", qualifiedByName = "specialiteId")
    @Mapping(target = "niveau", source = "niveau", qualifiedByName = "niveauId")
    FormationDTO toDto(Formation s);

    @Named("specialiteId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    SpecialiteDTO toDtoSpecialiteId(Specialite specialite);

    @Named("niveauId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    NiveauDTO toDtoNiveauId(Niveau niveau);
}
