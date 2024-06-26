package sn.ugb.gir.service.mapper;

import org.mapstruct.*;
import sn.ugb.gir.domain.Departement;
import sn.ugb.gir.domain.Ufr;
import sn.ugb.gir.service.dto.DepartementDTO;
import sn.ugb.gir.service.dto.UfrDTO;

/**
 * Mapper for the entity {@link Departement} and its DTO {@link DepartementDTO}.
 */
@Mapper(componentModel = "spring")
public interface DepartementMapper extends EntityMapper<DepartementDTO, Departement> {
    @Mapping(target = "ufr", source = "ufr", qualifiedByName = "ufrId")
    DepartementDTO toDto(Departement s);

    @Named("ufrId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    UfrDTO toDtoUfrId(Ufr ufr);
}
