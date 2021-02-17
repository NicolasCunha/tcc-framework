package com.br.framework.runtimeTest;

import com.br.framework.internal.component.Window;
import com.br.framework.Database;
import com.br.framework.WindowBuilder;

public class RuntimeTest {

    public static void main(String[] args) throws Exception {
        Database.setDatabaseCredentials("jdbc:mariadb://localhost:3306/tcc", "root", "root"); // Credenciais do banco
        final Window window = WindowBuilder.getInstance()
                .title("Cadastro de Produtos") // Titulo da Janela
                .width(1024) // Largura
                .height(768) // Altura
                .centered(true) // Centralizado
                .table("produto") // Tabela
                .includeAttribute("id_produto", "Código do Produto") // Atributo com label
                .includeAttribute("nm_produto", "Nome do Produto") // Atributo com label
                .includeAttribute("vl_produto", "Valor do Produto") // Atributo com label
                .includeAttribute("qt_produto") // Atributo sem label
                .primaryKey("id_produto") // Chave primaria
                .sequence("produto_seq") // Sequence do banco (geracao de chaves)
                .build();
        window.show(); // Exibe a tela
    }

}
