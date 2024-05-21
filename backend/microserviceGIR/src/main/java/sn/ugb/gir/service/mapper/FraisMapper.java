package sn.ugb.gir.service.mapper;

import org.mapstruct.*;
import sn.ugb.gir.domain.Frais;
import sn.ugb.gir.domain.TypeFrais;
import sn.ugb.gir.service.dto.FraisDTO;
import sn.ugb.gir.service.dto.TypeFraisDTO;

/**
 * Mapper for the entity {@link Frais} and its DTO {@link FraisDTO}.
 */
@Mapper(componentModel = "spring")
public interface FraisMapper extends EntityMapper<FraisDTO, Frais> {
    @Mapping(target = "typeFrais", source = "typeFrais", qualifiedByName = "typeFraisId")
    FraisDTO toDto(Frais s);

    @Named("typeFraisId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    TypeFraisDTO toDtoTypeFraisId(TypeFrais typeFrais);
}
