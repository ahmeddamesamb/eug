package sn.ugb.gir.service.mapper;

import java.util.Set;
import java.util.stream.Collectors;
import org.mapstruct.*;
import sn.ugb.gir.domain.Domaine;
import sn.ugb.gir.domain.Ufr;
import sn.ugb.gir.service.dto.DomaineDTO;
import sn.ugb.gir.service.dto.UfrDTO;

/**
 * Mapper for the entity {@link Domaine} and its DTO {@link DomaineDTO}.
 */
@Mapper(componentModel = "spring")
public interface DomaineMapper extends EntityMapper<DomaineDTO, Domaine> {
    @Mapping(target = "ufrs", source = "ufrs", qualifiedByName = "ufrIdSet")
    DomaineDTO toDto(Domaine s);

    @Mapping(target = "removeUfr", ignore = true)
    Domaine toEntity(DomaineDTO domaineDTO);

    @Named("ufrId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    UfrDTO toDtoUfrId(Ufr ufr);

    @Named("ufrIdSet")
    default Set<UfrDTO> toDtoUfrIdSet(Set<Ufr> ufr) {
        return ufr.stream().map(this::toDtoUfrId).collect(Collectors.toSet());
    }
}
