package sn.ugb.gateway.repository;

import java.util.ArrayList;
import java.util.List;
import org.springframework.data.relational.core.sql.Column;
import org.springframework.data.relational.core.sql.Expression;
import org.springframework.data.relational.core.sql.Table;

public class BlocFonctionnelSqlHelper {

    public static List<Expression> getColumns(Table table, String columnPrefix) {
        List<Expression> columns = new ArrayList<>();
        columns.add(Column.aliased("id", table, columnPrefix + "_id"));
        columns.add(Column.aliased("libelle_bloc", table, columnPrefix + "_libelle_bloc"));
        columns.add(Column.aliased("date_ajout_bloc", table, columnPrefix + "_date_ajout_bloc"));
        columns.add(Column.aliased("actif_yn", table, columnPrefix + "_actif_yn"));

        columns.add(Column.aliased("service_id", table, columnPrefix + "_service_id"));
        return columns;
    }
}
