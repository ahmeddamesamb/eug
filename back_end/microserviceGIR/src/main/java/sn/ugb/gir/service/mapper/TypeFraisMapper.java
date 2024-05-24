package sn.ugb.gir.service.mapper;

import org.mapstruct.*;
import sn.ugb.gir.domain.TypeFrais;
import sn.ugb.gir.service.dto.TypeFraisDTO;

/**
 * Mapper for the entity {@link TypeFrais} and its DTO {@link TypeFraisDTO}.
 */
@Mapper(componentModel = "spring")
public interface TypeFraisMapper extends EntityMapper<TypeFraisDTO, TypeFrais> {}
