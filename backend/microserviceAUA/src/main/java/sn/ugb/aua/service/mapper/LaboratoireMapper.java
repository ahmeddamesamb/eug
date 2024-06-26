package sn.ugb.aua.service.mapper;

import org.mapstruct.*;
import sn.ugb.aua.domain.Laboratoire;
import sn.ugb.aua.service.dto.LaboratoireDTO;

/**
 * Mapper for the entity {@link Laboratoire} and its DTO {@link LaboratoireDTO}.
 */
@Mapper(componentModel = "spring")
public interface LaboratoireMapper extends EntityMapper<LaboratoireDTO, Laboratoire> {}
