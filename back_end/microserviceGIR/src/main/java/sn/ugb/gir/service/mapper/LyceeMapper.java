package sn.ugb.gir.service.mapper;

import org.mapstruct.*;
import sn.ugb.gir.domain.Lycee;
import sn.ugb.gir.domain.Region;
import sn.ugb.gir.service.dto.LyceeDTO;
import sn.ugb.gir.service.dto.RegionDTO;

/**
 * Mapper for the entity {@link Lycee} and its DTO {@link LyceeDTO}.
 */
@Mapper(componentModel = "spring")
public interface LyceeMapper extends EntityMapper<LyceeDTO, Lycee> {
    @Mapping(target = "region", source = "region", qualifiedByName = "regionId")
    LyceeDTO toDto(Lycee s);

    @Named("regionId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    RegionDTO toDtoRegionId(Region region);
}
