package com.br.framework.bootstrap;

import com.br.framework.api.Framework;
import com.br.framework.core.component.Frame;
import com.br.framework.api.services.LoggingService;
import com.br.framework.api.services.PropertiesServices;

public class SystemBootstrap {
    
    public static void main(String[] args) throws Exception {
        PropertiesServices.setupDatabaseAuth(
                "jdbc:mariadb://localhost:3306/tcc",
                "root",
                "root"
        ); // Credenciais do banco
        LoggingService.setShouldLogSql(true);
        final Frame frame = Framework.getFrameConfigurator()
                .title("Cadastro de Alunos") // Titulo da Janela
                .width(1024) // Largura
                .height(768) // Altura
                .centered(true) // Centralizado?
                .table("aluno") // Tabela
                .includeAttribute("id", "Código de Matrícula") // Atributo com label
                .includeAttribute("nome", "Nome do Aluno") // Atributo com label
                .includeAttribute("idade") // Atributo sem label
                .build();
        frame.show(); // Exibe a tela
    }
    
}
