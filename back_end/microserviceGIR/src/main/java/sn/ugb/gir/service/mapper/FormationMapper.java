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
    @Mapping(target = "niveau", source = "niveau", qualifiedByName = "niveauId")
    @Mapping(target = "specialite", source = "specialite", qualifiedByName = "specialiteId")
    FormationDTO toDto(Formation s);

    @Named("niveauId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "libelleNiveau", source = "libelleNiveau")
    @Mapping(target = "cycleNiveau", source = "cycleNiveau")
    @Mapping(target = "codeNiveau", source = "codeNiveau")
    @Mapping(target = "anneeEtude", source = "anneeEtude")
    NiveauDTO toDtoNiveauId(Niveau niveau);

    @Named("specialiteId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "nomSpecialites", source = "nomSpecialites")
    @Mapping(target = "sigleSpecialites", source = "sigleSpecialites")
    @Mapping(target = "specialiteParticulierYN", source = "specialiteParticulierYN")
    @Mapping(target = "specialitesPayanteYN", source = "specialitesPayanteYN")
    SpecialiteDTO toDtoSpecialiteId(Specialite specialite);
}
