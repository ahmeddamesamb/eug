package sn.ugb.gir.service.mapper;

import java.util.Set;
import java.util.stream.Collectors;
import org.mapstruct.*;
import sn.ugb.gir.domain.Cycle;
import sn.ugb.gir.domain.Frais;
import sn.ugb.gir.domain.TypeFrais;
import sn.ugb.gir.domain.Universite;
import sn.ugb.gir.service.dto.CycleDTO;
import sn.ugb.gir.service.dto.FraisDTO;
import sn.ugb.gir.service.dto.TypeFraisDTO;
import sn.ugb.gir.service.dto.UniversiteDTO;

/**
 * Mapper for the entity {@link Frais} and its DTO {@link FraisDTO}.
 */
@Mapper(componentModel = "spring")
public interface FraisMapper extends EntityMapper<FraisDTO, Frais> {
    @Mapping(target = "typeFrais", source = "typeFrais", qualifiedByName = "typeFraisId")
    @Mapping(target = "typeCycle", source = "typeCycle", qualifiedByName = "cycleId")
    @Mapping(target = "universites", source = "universites", qualifiedByName = "universiteIdSet")
    FraisDTO toDto(Frais s);

    @Mapping(target = "removeUniversite", ignore = true)
    Frais toEntity(FraisDTO fraisDTO);

    @Named("typeFraisId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "libelleTypeFrais", source = "libelleTypeFrais")
    TypeFraisDTO toDtoTypeFraisId(TypeFrais typeFrais);

    @Named("cycleId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "libelleCycle", source = "libelleCycle")
    CycleDTO toDtoCycleId(Cycle cycle);

    @Named("universiteId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "nomUniversite", source = "nomUniversite")
    @Mapping(target = "sigleUniversite", source = "sigleUniversite")
    UniversiteDTO toDtoUniversiteId(Universite universite);

    @Named("universiteIdSet")
    default Set<UniversiteDTO> toDtoUniversiteIdSet(Set<Universite> universite) {
        return universite.stream().map(this::toDtoUniversiteId).collect(Collectors.toSet());
    }
}
