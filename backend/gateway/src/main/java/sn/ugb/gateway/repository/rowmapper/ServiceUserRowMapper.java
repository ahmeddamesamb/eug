package sn.ugb.gateway.repository.rowmapper;

import io.r2dbc.spi.Row;
import java.time.LocalDate;
import java.util.function.BiFunction;
import org.springframework.stereotype.Service;
import sn.ugb.gateway.domain.ServiceUser;

/**
 * Converter between {@link Row} to {@link ServiceUser}, with proper type conversions.
 */
@Service
public class ServiceUserRowMapper implements BiFunction<Row, String, ServiceUser> {

    private final ColumnConverter converter;

    public ServiceUserRowMapper(ColumnConverter converter) {
        this.converter = converter;
    }

    /**
     * Take a {@link Row} and a column prefix, and extract all the fields.
     * @return the {@link ServiceUser} stored in the database.
     */
    @Override
    public ServiceUser apply(Row row, String prefix) {
        ServiceUser entity = new ServiceUser();
        entity.setId(converter.fromRow(row, prefix + "_id", Long.class));
        entity.setNom(converter.fromRow(row, prefix + "_nom", String.class));
        entity.setDateAjout(converter.fromRow(row, prefix + "_date_ajout", LocalDate.class));
        entity.setActifYN(converter.fromRow(row, prefix + "_actif_yn", Boolean.class));
        return entity;
    }
}
