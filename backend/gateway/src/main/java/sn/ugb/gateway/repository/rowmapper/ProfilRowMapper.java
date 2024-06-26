package sn.ugb.gateway.repository.rowmapper;

import io.r2dbc.spi.Row;
import java.time.LocalDate;
import java.util.function.BiFunction;
import org.springframework.stereotype.Service;
import sn.ugb.gateway.domain.Profil;

/**
 * Converter between {@link Row} to {@link Profil}, with proper type conversions.
 */
@Service
public class ProfilRowMapper implements BiFunction<Row, String, Profil> {

    private final ColumnConverter converter;

    public ProfilRowMapper(ColumnConverter converter) {
        this.converter = converter;
    }

    /**
     * Take a {@link Row} and a column prefix, and extract all the fields.
     * @return the {@link Profil} stored in the database.
     */
    @Override
    public Profil apply(Row row, String prefix) {
        Profil entity = new Profil();
        entity.setId(converter.fromRow(row, prefix + "_id", Long.class));
        entity.setLibelle(converter.fromRow(row, prefix + "_libelle", String.class));
        entity.setDateAjout(converter.fromRow(row, prefix + "_date_ajout", LocalDate.class));
        entity.setActifYN(converter.fromRow(row, prefix + "_actif_yn", Boolean.class));
        return entity;
    }
}
