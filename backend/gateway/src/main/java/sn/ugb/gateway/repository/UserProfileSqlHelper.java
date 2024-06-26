package sn.ugb.gateway.repository;

import java.util.ArrayList;
import java.util.List;
import org.springframework.data.relational.core.sql.Column;
import org.springframework.data.relational.core.sql.Expression;
import org.springframework.data.relational.core.sql.Table;

public class UserProfileSqlHelper {

    public static List<Expression> getColumns(Table table, String columnPrefix) {
        List<Expression> columns = new ArrayList<>();
        columns.add(Column.aliased("id", table, columnPrefix + "_id"));
        columns.add(Column.aliased("date_debut", table, columnPrefix + "_date_debut"));
        columns.add(Column.aliased("date_fin", table, columnPrefix + "_date_fin"));
        columns.add(Column.aliased("en_cours_yn", table, columnPrefix + "_en_cours_yn"));

        columns.add(Column.aliased("profil_id", table, columnPrefix + "_profil_id"));
        columns.add(Column.aliased("info_user_id", table, columnPrefix + "_info_user_id"));
        return columns;
    }
}
