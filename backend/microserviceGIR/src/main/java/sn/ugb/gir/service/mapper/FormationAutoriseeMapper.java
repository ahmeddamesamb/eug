package sn.ugb.gir.service.mapper;

import java.util.Set;
import java.util.stream.Collectors;
import org.mapstruct.*;
import sn.ugb.gir.domain.Formation;
import sn.ugb.gir.domain.FormationAutorisee;
import sn.ugb.gir.service.dto.FormationAutoriseeDTO;
import sn.ugb.gir.service.dto.FormationDTO;

/**
 * Mapper for the entity {@link FormationAutorisee} and its DTO {@link FormationAutoriseeDTO}.
 */
@Mapper(componentModel = "spring")
public interface FormationAutoriseeMapper extends EntityMapper<FormationAutoriseeDTO, FormationAutorisee> {
    @Mapping(target = "formations", source = "formations", qualifiedByName = "formationIdSet")
    FormationAutoriseeDTO toDto(FormationAutorisee s);

    @Mapping(target = "removeFormation", ignore = true)
    FormationAutorisee toEntity(FormationAutoriseeDTO formationAutoriseeDTO);

    @Named("formationId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    FormationDTO toDtoFormationId(Formation formation);

    @Named("formationIdSet")
    default Set<FormationDTO> toDtoFormationIdSet(Set<Formation> formation) {
        return formation.stream().map(this::toDtoFormationId).collect(Collectors.toSet());
    }
}
