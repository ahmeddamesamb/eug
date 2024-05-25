package sn.ugb.gir.service.mapper;

import org.mapstruct.*;
import sn.ugb.gir.domain.Mention;
import sn.ugb.gir.domain.Specialite;
import sn.ugb.gir.service.dto.MentionDTO;
import sn.ugb.gir.service.dto.SpecialiteDTO;

/**
 * Mapper for the entity {@link Specialite} and its DTO {@link SpecialiteDTO}.
 */
@Mapper(componentModel = "spring")
public interface SpecialiteMapper extends EntityMapper<SpecialiteDTO, Specialite> {
    @Mapping(target = "mention", source = "mention", qualifiedByName = "mentionId")
    SpecialiteDTO toDto(Specialite s);

    @Named("mentionId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "libelleMention", source = "libelleMention")
    MentionDTO toDtoMentionId(Mention mention);
}
