package sn.ugb.gateway.repository.rowmapper;

import io.r2dbc.spi.Row;
import java.time.LocalDate;
import java.util.function.BiFunction;
import org.springframework.stereotype.Service;
import sn.ugb.gateway.domain.InfosUser;

/**
 * Converter between {@link Row} to {@link InfosUser}, with proper type conversions.
 */
@Service
public class InfosUserRowMapper implements BiFunction<Row, String, InfosUser> {

    private final ColumnConverter converter;

    public InfosUserRowMapper(ColumnConverter converter) {
        this.converter = converter;
    }

    /**
     * Take a {@link Row} and a column prefix, and extract all the fields.
     * @return the {@link InfosUser} stored in the database.
     */
    @Override
    public InfosUser apply(Row row, String prefix) {
        InfosUser entity = new InfosUser();
        entity.setId(converter.fromRow(row, prefix + "_id", Long.class));
        entity.setDateAjout(converter.fromRow(row, prefix + "_date_ajout", LocalDate.class));
        entity.setActifYN(converter.fromRow(row, prefix + "_actif_yn", Boolean.class));
        entity.setUserId(converter.fromRow(row, prefix + "_user_id", String.class));
        return entity;
    }
}
