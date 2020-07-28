package com.br.framework.core.enumerator;

public enum ConnectionAuth {

    URL("db.url"),
    USER("db.user"),
    PASSWORD("db.pass");

    private final String content;

    private ConnectionAuth(final String content) {
        this.content = content;
    }

    public String content() {
        return this.content;
    }

}
