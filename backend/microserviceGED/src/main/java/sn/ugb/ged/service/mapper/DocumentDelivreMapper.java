package sn.ugb.ged.service.mapper;

import org.mapstruct.*;
import sn.ugb.ged.domain.DocumentDelivre;
import sn.ugb.ged.domain.TypeDocument;
import sn.ugb.ged.service.dto.DocumentDelivreDTO;
import sn.ugb.ged.service.dto.TypeDocumentDTO;

/**
 * Mapper for the entity {@link DocumentDelivre} and its DTO {@link DocumentDelivreDTO}.
 */
@Mapper(componentModel = "spring")
public interface DocumentDelivreMapper extends EntityMapper<DocumentDelivreDTO, DocumentDelivre> {
    @Mapping(target = "typeDocument", source = "typeDocument", qualifiedByName = "typeDocumentId")
    DocumentDelivreDTO toDto(DocumentDelivre s);

    @Named("typeDocumentId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    TypeDocumentDTO toDtoTypeDocumentId(TypeDocument typeDocument);
}
