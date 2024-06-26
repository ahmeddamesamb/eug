package sn.ugb.gateway.repository.rowmapper;

import io.r2dbc.spi.Row;
import java.time.LocalDate;
import java.util.function.BiFunction;
import org.springframework.stereotype.Service;
import sn.ugb.gateway.domain.BlocFonctionnel;

/**
 * Converter between {@link Row} to {@link BlocFonctionnel}, with proper type conversions.
 */
@Service
public class BlocFonctionnelRowMapper implements BiFunction<Row, String, BlocFonctionnel> {

    private final ColumnConverter converter;

    public BlocFonctionnelRowMapper(ColumnConverter converter) {
        this.converter = converter;
    }

    /**
     * Take a {@link Row} and a column prefix, and extract all the fields.
     * @return the {@link BlocFonctionnel} stored in the database.
     */
    @Override
    public BlocFonctionnel apply(Row row, String prefix) {
        BlocFonctionnel entity = new BlocFonctionnel();
        entity.setId(converter.fromRow(row, prefix + "_id", Long.class));
        entity.setLibelleBloc(converter.fromRow(row, prefix + "_libelle_bloc", String.class));
        entity.setDateAjoutBloc(converter.fromRow(row, prefix + "_date_ajout_bloc", LocalDate.class));
        entity.setActifYN(converter.fromRow(row, prefix + "_actif_yn", Boolean.class));
        entity.setServiceId(converter.fromRow(row, prefix + "_service_id", Long.class));
        return entity;
    }
}
