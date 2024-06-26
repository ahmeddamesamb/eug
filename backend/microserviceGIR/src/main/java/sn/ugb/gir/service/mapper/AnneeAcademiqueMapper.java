package sn.ugb.gir.service.mapper;

import org.mapstruct.*;
import sn.ugb.gir.domain.AnneeAcademique;
import sn.ugb.gir.service.dto.AnneeAcademiqueDTO;

/**
 * Mapper for the entity {@link AnneeAcademique} and its DTO {@link AnneeAcademiqueDTO}.
 */
@Mapper(componentModel = "spring")
public interface AnneeAcademiqueMapper extends EntityMapper<AnneeAcademiqueDTO, AnneeAcademique> {}
