package com.br.framework.internal.test;

import com.br.framework.Framework;
import com.br.framework.internal.component.Window;

public class RuntimeTest {

    public static void main(String[] args) throws Exception {
        Framework framework = Framework.getInstance();
        framework.setDatabaseCredentials("jdbc:mariadb://localhost:3306/tcc", "root", "root"); // Credenciais do banco
        final Window frame = framework.getWindowConfigurator()
                .newConfiguration()
                .title("Cadastro de Alunos") // Titulo da Janela
                .width(1024) // Largura
                .height(768) // Altura
                .centered(true) // Centralizado
                .table("aluno") // Tabela
                .includeAttribute("id_aluno", "Código de Matrícula") // Atributo com label
                .includeAttribute("nome", "Nome do Aluno") // Atributo com label
                .includeAttribute("idade") // Atributo sem label
                .primaryKey("id_aluno") // Chave primaria
                .sequence("aluno_seq") // Sequence do banco (geracao de chaves)
                .build();
        frame.show(); // Exibe a tela
    }

}
