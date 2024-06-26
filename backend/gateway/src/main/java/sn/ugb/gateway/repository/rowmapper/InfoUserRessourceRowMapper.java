package sn.ugb.gateway.repository.rowmapper;

import io.r2dbc.spi.Row;
import java.time.LocalDate;
import java.util.function.BiFunction;
import org.springframework.stereotype.Service;
import sn.ugb.gateway.domain.InfoUserRessource;

/**
 * Converter between {@link Row} to {@link InfoUserRessource}, with proper type conversions.
 */
@Service
public class InfoUserRessourceRowMapper implements BiFunction<Row, String, InfoUserRessource> {

    private final ColumnConverter converter;

    public InfoUserRessourceRowMapper(ColumnConverter converter) {
        this.converter = converter;
    }

    /**
     * Take a {@link Row} and a column prefix, and extract all the fields.
     * @return the {@link InfoUserRessource} stored in the database.
     */
    @Override
    public InfoUserRessource apply(Row row, String prefix) {
        InfoUserRessource entity = new InfoUserRessource();
        entity.setId(converter.fromRow(row, prefix + "_id", Long.class));
        entity.setDateAjout(converter.fromRow(row, prefix + "_date_ajout", LocalDate.class));
        entity.setEnCoursYN(converter.fromRow(row, prefix + "_en_cours_yn", Boolean.class));
        entity.setInfosUserId(converter.fromRow(row, prefix + "_infos_user_id", Long.class));
        entity.setRessourceId(converter.fromRow(row, prefix + "_ressource_id", Long.class));
        return entity;
    }
}
