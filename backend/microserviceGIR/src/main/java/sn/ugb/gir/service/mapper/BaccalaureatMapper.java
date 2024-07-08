package sn.ugb.gir.service.mapper;

import org.mapstruct.*;
import sn.ugb.gir.domain.Baccalaureat;
import sn.ugb.gir.domain.Etudiant;
import sn.ugb.gir.domain.Serie;
import sn.ugb.gir.service.dto.BaccalaureatDTO;
import sn.ugb.gir.service.dto.EtudiantDTO;
import sn.ugb.gir.service.dto.SerieDTO;

/**
 * Mapper for the entity {@link Baccalaureat} and its DTO {@link BaccalaureatDTO}.
 */
@Mapper(componentModel = "spring", uses = {SerieMapper.class, EtudiantMapper.class} )
public interface BaccalaureatMapper extends EntityMapper<BaccalaureatDTO, Baccalaureat> {
    @Mapping(target = "etudiant", source = "etudiant", qualifiedByName = "etudiantId")
    @Mapping(target = "serie", source = "serie", qualifiedByName = "serieId")
    BaccalaureatDTO toDto(Baccalaureat s);

    @Named("etudiantId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    EtudiantDTO toDtoEtudiantId(Etudiant etudiant);

    @Named("serieId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "codeSerie", source = "codeSerie")
    @Mapping(target = "libelleSerie", source = "libelleSerie")
    @Mapping(target = "sigleSerie", source = "sigleSerie")
    SerieDTO toDtoSerieId(Serie serie);
}
