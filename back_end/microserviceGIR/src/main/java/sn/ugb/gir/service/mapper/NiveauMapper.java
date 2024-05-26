package sn.ugb.gir.service.mapper;

import org.mapstruct.*;
import sn.ugb.gir.domain.Niveau;
import sn.ugb.gir.service.dto.NiveauDTO;

/**
 * Mapper for the entity {@link Niveau} and its DTO {@link NiveauDTO}.
 */
@Mapper(componentModel = "spring")
public interface NiveauMapper extends EntityMapper<NiveauDTO, Niveau> {}
