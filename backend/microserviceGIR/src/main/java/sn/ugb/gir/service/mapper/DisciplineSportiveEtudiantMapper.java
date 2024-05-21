package sn.ugb.gir.service.mapper;

import org.mapstruct.*;
import sn.ugb.gir.domain.DisciplineSportive;
import sn.ugb.gir.domain.DisciplineSportiveEtudiant;
import sn.ugb.gir.domain.Etudiant;
import sn.ugb.gir.service.dto.DisciplineSportiveDTO;
import sn.ugb.gir.service.dto.DisciplineSportiveEtudiantDTO;
import sn.ugb.gir.service.dto.EtudiantDTO;

/**
 * Mapper for the entity {@link DisciplineSportiveEtudiant} and its DTO {@link DisciplineSportiveEtudiantDTO}.
 */
@Mapper(componentModel = "spring")
public interface DisciplineSportiveEtudiantMapper extends EntityMapper<DisciplineSportiveEtudiantDTO, DisciplineSportiveEtudiant> {
    @Mapping(target = "disciplineSportive", source = "disciplineSportive", qualifiedByName = "disciplineSportiveId")
    @Mapping(target = "etudiant", source = "etudiant", qualifiedByName = "etudiantId")
    DisciplineSportiveEtudiantDTO toDto(DisciplineSportiveEtudiant s);

    @Named("disciplineSportiveId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    DisciplineSportiveDTO toDtoDisciplineSportiveId(DisciplineSportive disciplineSportive);

    @Named("etudiantId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    EtudiantDTO toDtoEtudiantId(Etudiant etudiant);
}
