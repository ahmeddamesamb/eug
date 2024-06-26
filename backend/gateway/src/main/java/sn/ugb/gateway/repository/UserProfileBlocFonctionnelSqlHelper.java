package sn.ugb.gateway.repository;

import java.util.ArrayList;
import java.util.List;
import org.springframework.data.relational.core.sql.Column;
import org.springframework.data.relational.core.sql.Expression;
import org.springframework.data.relational.core.sql.Table;

public class UserProfileBlocFonctionnelSqlHelper {

    public static List<Expression> getColumns(Table table, String columnPrefix) {
        List<Expression> columns = new ArrayList<>();
        columns.add(Column.aliased("id", table, columnPrefix + "_id"));
        columns.add(Column.aliased("date", table, columnPrefix + "_date"));
        columns.add(Column.aliased("en_cours_yn", table, columnPrefix + "_en_cours_yn"));

        columns.add(Column.aliased("user_profil_id", table, columnPrefix + "_user_profil_id"));
        columns.add(Column.aliased("bloc_fonctionnel_id", table, columnPrefix + "_bloc_fonctionnel_id"));
        return columns;
    }
}
