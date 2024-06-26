package sn.ugb.gateway.repository.rowmapper;

import io.r2dbc.spi.Row;
import java.time.LocalDate;
import java.util.function.BiFunction;
import org.springframework.stereotype.Service;
import sn.ugb.gateway.domain.HistoriqueConnexion;

/**
 * Converter between {@link Row} to {@link HistoriqueConnexion}, with proper type conversions.
 */
@Service
public class HistoriqueConnexionRowMapper implements BiFunction<Row, String, HistoriqueConnexion> {

    private final ColumnConverter converter;

    public HistoriqueConnexionRowMapper(ColumnConverter converter) {
        this.converter = converter;
    }

    /**
     * Take a {@link Row} and a column prefix, and extract all the fields.
     * @return the {@link HistoriqueConnexion} stored in the database.
     */
    @Override
    public HistoriqueConnexion apply(Row row, String prefix) {
        HistoriqueConnexion entity = new HistoriqueConnexion();
        entity.setId(converter.fromRow(row, prefix + "_id", Long.class));
        entity.setDateDebutConnexion(converter.fromRow(row, prefix + "_date_debut_connexion", LocalDate.class));
        entity.setDateFinConnexion(converter.fromRow(row, prefix + "_date_fin_connexion", LocalDate.class));
        entity.setAdresseIp(converter.fromRow(row, prefix + "_adresse_ip", String.class));
        entity.setActifYN(converter.fromRow(row, prefix + "_actif_yn", Boolean.class));
        entity.setInfoUserId(converter.fromRow(row, prefix + "_info_user_id", Long.class));
        return entity;
    }
}
