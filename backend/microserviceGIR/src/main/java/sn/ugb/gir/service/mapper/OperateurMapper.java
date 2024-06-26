package sn.ugb.gir.service.mapper;

import org.mapstruct.*;
import sn.ugb.gir.domain.Operateur;
import sn.ugb.gir.service.dto.OperateurDTO;

/**
 * Mapper for the entity {@link Operateur} and its DTO {@link OperateurDTO}.
 */
@Mapper(componentModel = "spring")
public interface OperateurMapper extends EntityMapper<OperateurDTO, Operateur> {}
