package sn.ugb.gateway.repository;

import java.util.ArrayList;
import java.util.List;
import org.springframework.data.relational.core.sql.Column;
import org.springframework.data.relational.core.sql.Expression;
import org.springframework.data.relational.core.sql.Table;

public class HistoriqueConnexionSqlHelper {

    public static List<Expression> getColumns(Table table, String columnPrefix) {
        List<Expression> columns = new ArrayList<>();
        columns.add(Column.aliased("id", table, columnPrefix + "_id"));
        columns.add(Column.aliased("date_debut_connexion", table, columnPrefix + "_date_debut_connexion"));
        columns.add(Column.aliased("date_fin_connexion", table, columnPrefix + "_date_fin_connexion"));
        columns.add(Column.aliased("adresse_ip", table, columnPrefix + "_adresse_ip"));
        columns.add(Column.aliased("actif_yn", table, columnPrefix + "_actif_yn"));

        columns.add(Column.aliased("info_user_id", table, columnPrefix + "_info_user_id"));
        return columns;
    }
}
