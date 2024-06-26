package sn.ugb.gir.service.mapper;

import org.mapstruct.*;
import sn.ugb.gir.domain.Etudiant;
import sn.ugb.gir.domain.Lycee;
import sn.ugb.gir.domain.Region;
import sn.ugb.gir.domain.TypeSelection;
import sn.ugb.gir.service.dto.EtudiantDTO;
import sn.ugb.gir.service.dto.LyceeDTO;
import sn.ugb.gir.service.dto.RegionDTO;
import sn.ugb.gir.service.dto.TypeSelectionDTO;

/**
 * Mapper for the entity {@link Etudiant} and its DTO {@link EtudiantDTO}.
 */
@Mapper(componentModel = "spring")
public interface EtudiantMapper extends EntityMapper<EtudiantDTO, Etudiant> {
    @Mapping(target = "region", source = "region", qualifiedByName = "regionId")
    @Mapping(target = "typeSelection", source = "typeSelection", qualifiedByName = "typeSelectionId")
    @Mapping(target = "lycee", source = "lycee", qualifiedByName = "lyceeId")
    EtudiantDTO toDto(Etudiant s);

    @Named("regionId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    RegionDTO toDtoRegionId(Region region);

    @Named("typeSelectionId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    TypeSelectionDTO toDtoTypeSelectionId(TypeSelection typeSelection);

    @Named("lyceeId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    LyceeDTO toDtoLyceeId(Lycee lycee);
}
