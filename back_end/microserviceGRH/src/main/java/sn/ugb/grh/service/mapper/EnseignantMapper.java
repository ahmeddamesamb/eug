package sn.ugb.grh.service.mapper;

import org.mapstruct.*;
import sn.ugb.grh.domain.Enseignant;
import sn.ugb.grh.service.dto.EnseignantDTO;

/**
 * Mapper for the entity {@link Enseignant} and its DTO {@link EnseignantDTO}.
 */
@Mapper(componentModel = "spring")
public interface EnseignantMapper extends EntityMapper<EnseignantDTO, Enseignant> {}
