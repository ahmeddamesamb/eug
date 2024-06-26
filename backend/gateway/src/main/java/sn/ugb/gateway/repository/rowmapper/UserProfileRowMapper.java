package sn.ugb.gateway.repository.rowmapper;

import io.r2dbc.spi.Row;
import java.time.LocalDate;
import java.util.function.BiFunction;
import org.springframework.stereotype.Service;
import sn.ugb.gateway.domain.UserProfile;

/**
 * Converter between {@link Row} to {@link UserProfile}, with proper type conversions.
 */
@Service
public class UserProfileRowMapper implements BiFunction<Row, String, UserProfile> {

    private final ColumnConverter converter;

    public UserProfileRowMapper(ColumnConverter converter) {
        this.converter = converter;
    }

    /**
     * Take a {@link Row} and a column prefix, and extract all the fields.
     * @return the {@link UserProfile} stored in the database.
     */
    @Override
    public UserProfile apply(Row row, String prefix) {
        UserProfile entity = new UserProfile();
        entity.setId(converter.fromRow(row, prefix + "_id", Long.class));
        entity.setDateDebut(converter.fromRow(row, prefix + "_date_debut", LocalDate.class));
        entity.setDateFin(converter.fromRow(row, prefix + "_date_fin", LocalDate.class));
        entity.setEnCoursYN(converter.fromRow(row, prefix + "_en_cours_yn", Boolean.class));
        entity.setProfilId(converter.fromRow(row, prefix + "_profil_id", Long.class));
        entity.setInfoUserId(converter.fromRow(row, prefix + "_info_user_id", Long.class));
        return entity;
    }
}
