package com.br.framework.internal.database;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class QueryMetadataFactory {

    public List<Map<String, Object>> buildRowsFromResultSet(final ResultSet resultSet) throws SQLException {
        final ResultSetMetaData resultSetMetadata = resultSet.getMetaData();
        final List<Map<String, Object>> rowList = new ArrayList<>();
        while (resultSet.next()) {
            final Map<String, Object> row = new LinkedHashMap<>();
            for (int i = 1; i <= resultSetMetadata.getColumnCount(); i++) {
                row.put(resultSetMetadata.getColumnLabel(i), resultSet.getObject(i));
            }
            rowList.add(row);
        }
        return rowList;
    }

    public List<String> buildColumnsFromResultSet(final ResultSet resultSet) throws SQLException {
        final ResultSetMetaData resultSetMetadata = resultSet.getMetaData();
        final List<String> fieldList = new ArrayList<>();
        resultSet.beforeFirst();
        if (resultSet.next()) {
            for (int i = 1; i <= resultSetMetadata.getColumnCount(); i++) {
                fieldList.add(resultSetMetadata.getColumnName(i));
            }
        }
        return fieldList;
    }

    public Map<String, String> mapColumnAlias(final ResultSet resultSet) throws SQLException {
        final Map<String, String> mapping = new LinkedHashMap<>();
        final ResultSetMetaData resultSetMetadata = resultSet.getMetaData();
        resultSet.beforeFirst();
        if (resultSet.next()) {
            for (int i = 1; i <= resultSetMetadata.getColumnCount(); i++) {
                mapping.put(resultSetMetadata.getColumnName(i), resultSetMetadata.getColumnLabel(i));
            }
        }
        return mapping;
    }
}
