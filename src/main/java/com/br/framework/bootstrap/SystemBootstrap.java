package com.br.framework.bootstrap;

import com.br.framework.api.Framework;
import com.br.framework.core.Frame;
import com.br.framework.core.system.FrameworkProperties;

public class SystemBootstrap {

    public static void main(String[] args) throws Exception {
        FrameworkProperties.setDatabaseConnectionValues(
                "jdbc:mariadb://localhost:3306/tcc",
                "root",
                "root"
        );

        final Frame frame = Framework.getFrameConfigurator()
                .title("Teste")
                .width(1280)
                .height(720)
                .centered(true)
                .table("produto")
                .includeAttribute("id", "Código")
                .includeAttribute("descricao", "Descrição")
                .includeAttribute("qtd", "Quantidade")
                .build();
        frame.show();

    }

}
