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
@Mapper(componentModel = "spring", uses = {EtudiantMapper.class})
public interface DisciplineSportiveEtudiantMapper extends EntityMapper<DisciplineSportiveEtudiantDTO, DisciplineSportiveEtudiant> {
    @Mapping(target = "disciplineSportive", source = "disciplineSportive", qualifiedByName = "disciplineSportiveId")
    @Mapping(target = "etudiant", source = "etudiant", qualifiedByName = "etudiantId")
    DisciplineSportiveEtudiantDTO toDto(DisciplineSportiveEtudiant s);

    @Named("disciplineSportiveId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "libelleDisciplineSportive", source = "libelleDisciplineSportive")
    DisciplineSportiveDTO toDtoDisciplineSportiveId(DisciplineSportive disciplineSportive);

    @Named("etudiantId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "codeEtu", source = "codeEtu")
    @Mapping(target = "ine", source = "ine")
    @Mapping(target = "codeBU", source = "codeBU")
    @Mapping(target = "emailUGB", source = "emailUGB")
    @Mapping(target = "dateNaissEtu", source = "dateNaissEtu")
    @Mapping(target = "lieuNaissEtu", source = "lieuNaissEtu")
    @Mapping(target = "sexe", source = "sexe")
    @Mapping(target = "numDocIdentite", source = "numDocIdentite")
    @Mapping(target = "assimileYN", source = "assimileYN")
    @Mapping(target = "exonereYN", source = "exonereYN")
    @Mapping(target = "region", source = "region")
    @Mapping(target = "typeSelection", source = "typeSelection")
    @Mapping(target = "lycee", source = "lycee")
    EtudiantDTO toDtoEtudiantId(Etudiant etudiant);
}
