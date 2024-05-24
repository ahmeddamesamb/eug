package sn.ugb.ged.service.mapper;

import org.mapstruct.*;
import sn.ugb.ged.domain.TypeDocument;
import sn.ugb.ged.service.dto.TypeDocumentDTO;

/**
 * Mapper for the entity {@link TypeDocument} and its DTO {@link TypeDocumentDTO}.
 */
@Mapper(componentModel = "spring")
public interface TypeDocumentMapper extends EntityMapper<TypeDocumentDTO, TypeDocument> {}
