package com.br.framework.bootstrap;

import com.br.framework.api.Framework;
import com.br.framework.core.system.FrameworkProperties;

public class SystemBootstrap {

    public static void main(String[] args) throws Exception {
        FrameworkProperties.setDatabaseConnectionValues(
                        "jdbc:mariadb://localhost:3306/tcc",
                        "root",
                        "root"
                );

        Framework.getFrameConfigurator()
                .title("Cadastro de Alunos")
                .width(1024)
                .height(768)
                .centered(true)
                .table("aluno")
                .includeAttribute(
                        "id",
                        "Matrícula"
                )
                .includeAttribute(
                        "nome",
                        "Nome do Aluno"
                )
                .includeAttribute(
                        "idade",
                        "Idade do Aluno"
                )
                .build()
                .show();
    }

}
