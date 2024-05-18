package sn.ugb.gir.service.mapper;

import org.mapstruct.*;
import sn.ugb.gir.domain.Domaine;
import sn.ugb.gir.service.dto.DomaineDTO;

/**
 * Mapper for the entity {@link Domaine} and its DTO {@link DomaineDTO}.
 */
@Mapper(componentModel = "spring")
public interface DomaineMapper extends EntityMapper<DomaineDTO, Domaine> {}
