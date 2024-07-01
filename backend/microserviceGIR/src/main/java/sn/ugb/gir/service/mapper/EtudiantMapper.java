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
@Mapper(componentModel = "spring" , uses = { RegionMapper.class, TypeAdmissionMapper.class, LyceeMapper.class })
public interface EtudiantMapper extends EntityMapper<EtudiantDTO, Etudiant> {
    @Mapping(target = "region", source = "region")
    @Mapping(target = "typeSelection", source = "typeSelection", qualifiedByName = "typeSelectionId")
    @Mapping(target = "lycee", source = "lycee", qualifiedByName = "lyceeId")
    EtudiantDTO toDto(Etudiant s);

    @Named("regionId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "libelleRegion", source = "libelleRegion")
    RegionDTO toDtoRegionId(Region region);

    @Named("typeSelectionId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "libelleTypeSelection", source = "libelleTypeSelection")
    TypeSelectionDTO toDtoTypeSelectionId(TypeSelection typeSelection);

    @Named("lyceeId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "nomLycee", source = "nomLycee")
    @Mapping(target = "codeLycee", source = "codeLycee")
    @Mapping(target = "villeLycee", source = "villeLycee")
    @Mapping(target = "academieLycee", source = "academieLycee")
    @Mapping(target = "centreExamen", source = "centreExamen")
    LyceeDTO toDtoLyceeId(Lycee lycee);
}
