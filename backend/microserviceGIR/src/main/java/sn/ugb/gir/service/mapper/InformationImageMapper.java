package sn.ugb.gir.service.mapper;

import org.mapstruct.*;
import sn.ugb.gir.domain.InformationImage;
import sn.ugb.gir.service.dto.InformationImageDTO;

/**
 * Mapper for the entity {@link InformationImage} and its DTO {@link InformationImageDTO}.
 */
@Mapper(componentModel = "spring")
public interface InformationImageMapper extends EntityMapper<InformationImageDTO, InformationImage> {}
