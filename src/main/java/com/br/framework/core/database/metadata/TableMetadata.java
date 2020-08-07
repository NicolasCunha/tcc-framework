package com.br.framework.core.database.metadata;

import java.util.ArrayList;
import java.util.List;

public class TableMetadata {

    private TableMetadata() {

    }

    public static TableMetadata getInstance() {
        return new TableMetadata();
    }

    private List<String> primaryKey;

    public List<String> getPrimaryKey() {
        if (primaryKey == null) {
            primaryKey = new ArrayList<>();
        }
        return primaryKey;
    }

    public void setPrimaryKey(List<String> primaryKey) {
        this.primaryKey = primaryKey;
    }

}
