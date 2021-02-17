package com.br.framework.internal.component;

import java.util.ArrayList;
import java.util.List;

public class TableMetadata {

    private TableMetadata() {
        // Empty constructor.
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
