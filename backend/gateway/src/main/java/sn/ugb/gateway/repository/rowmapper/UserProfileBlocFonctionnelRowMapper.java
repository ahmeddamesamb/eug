package sn.ugb.gateway.repository.rowmapper;

import io.r2dbc.spi.Row;
import java.time.LocalDate;
import java.util.function.BiFunction;
import org.springframework.stereotype.Service;
import sn.ugb.gateway.domain.UserProfileBlocFonctionnel;

/**
 * Converter between {@link Row} to {@link UserProfileBlocFonctionnel}, with proper type conversions.
 */
@Service
public class UserProfileBlocFonctionnelRowMapper implements BiFunction<Row, String, UserProfileBlocFonctionnel> {

    private final ColumnConverter converter;

    public UserProfileBlocFonctionnelRowMapper(ColumnConverter converter) {
        this.converter = converter;
    }

    /**
     * Take a {@link Row} and a column prefix, and extract all the fields.
     * @return the {@link UserProfileBlocFonctionnel} stored in the database.
     */
    @Override
    public UserProfileBlocFonctionnel apply(Row row, String prefix) {
        UserProfileBlocFonctionnel entity = new UserProfileBlocFonctionnel();
        entity.setId(converter.fromRow(row, prefix + "_id", Long.class));
        entity.setDate(converter.fromRow(row, prefix + "_date", LocalDate.class));
        entity.setEnCoursYN(converter.fromRow(row, prefix + "_en_cours_yn", Boolean.class));
        entity.setUserProfilId(converter.fromRow(row, prefix + "_user_profil_id", Long.class));
        entity.setBlocFonctionnelId(converter.fromRow(row, prefix + "_bloc_fonctionnel_id", Long.class));
        return entity;
    }
}
