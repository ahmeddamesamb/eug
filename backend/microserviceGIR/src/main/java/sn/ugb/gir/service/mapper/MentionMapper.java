package sn.ugb.gir.service.mapper;

import org.mapstruct.*;
import sn.ugb.gir.domain.Domaine;
import sn.ugb.gir.domain.Mention;
import sn.ugb.gir.service.dto.DomaineDTO;
import sn.ugb.gir.service.dto.MentionDTO;

/**
 * Mapper for the entity {@link Mention} and its DTO {@link MentionDTO}.
 */
@Mapper(componentModel = "spring")
public interface MentionMapper extends EntityMapper<MentionDTO, Mention> {
    @Mapping(target = "domaine", source = "domaine", qualifiedByName = "domaineId")
    MentionDTO toDto(Mention s);

    @Named("domaineId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    DomaineDTO toDtoDomaineId(Domaine domaine);

    MentionDTO toDto(MentionDTO mentionDTO);
}
