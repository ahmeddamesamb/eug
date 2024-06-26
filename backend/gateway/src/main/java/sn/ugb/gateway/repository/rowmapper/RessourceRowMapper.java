package sn.ugb.gateway.repository.rowmapper;

import io.r2dbc.spi.Row;
import java.util.function.BiFunction;
import org.springframework.stereotype.Service;
import sn.ugb.gateway.domain.Ressource;

/**
 * Converter between {@link Row} to {@link Ressource}, with proper type conversions.
 */
@Service
public class RessourceRowMapper implements BiFunction<Row, String, Ressource> {

    private final ColumnConverter converter;

    public RessourceRowMapper(ColumnConverter converter) {
        this.converter = converter;
    }

    /**
     * Take a {@link Row} and a column prefix, and extract all the fields.
     * @return the {@link Ressource} stored in the database.
     */
    @Override
    public Ressource apply(Row row, String prefix) {
        Ressource entity = new Ressource();
        entity.setId(converter.fromRow(row, prefix + "_id", Long.class));
        entity.setLibelle(converter.fromRow(row, prefix + "_libelle", String.class));
        entity.setActifYN(converter.fromRow(row, prefix + "_actif_yn", Boolean.class));
        return entity;
    }
}
