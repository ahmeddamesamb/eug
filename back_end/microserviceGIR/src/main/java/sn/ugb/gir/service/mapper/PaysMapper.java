package sn.ugb.gir.service.mapper;

import java.util.Set;
import java.util.stream.Collectors;
import org.mapstruct.*;
import sn.ugb.gir.domain.Pays;
import sn.ugb.gir.domain.Zone;
import sn.ugb.gir.service.dto.PaysDTO;
import sn.ugb.gir.service.dto.ZoneDTO;

/**
 * Mapper for the entity {@link Pays} and its DTO {@link PaysDTO}.
 */
@Mapper(componentModel = "spring")
public interface PaysMapper extends EntityMapper<PaysDTO, Pays> {
    @Mapping(target = "zones", source = "zones", qualifiedByName = "zoneIdSet")
    PaysDTO toDto(Pays s);

    @Mapping(target = "removeZone", ignore = true)
    Pays toEntity(PaysDTO paysDTO);

    @Named("zoneId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "libelleZone", source = "libelleZone")
    ZoneDTO toDtoZoneId(Zone zone);

    @Named("zoneIdSet")
    default Set<ZoneDTO> toDtoZoneIdSet(Set<Zone> zone) {
        return zone.stream().map(this::toDtoZoneId).collect(Collectors.toSet());
    }
}
