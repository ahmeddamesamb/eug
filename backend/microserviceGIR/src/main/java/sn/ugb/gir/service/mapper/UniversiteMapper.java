package sn.ugb.gir.service.mapper;

import org.mapstruct.*;
import sn.ugb.gir.domain.Ministere;
import sn.ugb.gir.domain.Universite;
import sn.ugb.gir.service.dto.MinistereDTO;
import sn.ugb.gir.service.dto.UniversiteDTO;

/**
 * Mapper for the entity {@link Universite} and its DTO {@link UniversiteDTO}.
 */
@Mapper(componentModel = "spring")
public interface UniversiteMapper extends EntityMapper<UniversiteDTO, Universite> {
    @Mapping(target = "ministere", source = "ministere", qualifiedByName = "ministereId")
    UniversiteDTO toDto(Universite s);

    @Named("ministereId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "nomMinistere", source = "nomMinistere")
    @Mapping(target = "sigleMinistere", source = "sigleMinistere")
    @Mapping(target = "dateDebut", source = "dateDebut")
    @Mapping(target = "dateFin", source = "dateFin")
    @Mapping(target = "enCoursYN", source = "enCoursYN")
    @Mapping(target = "actifYN", source = "actifYN")

    MinistereDTO toDtoMinistereId(Ministere ministere);
}
