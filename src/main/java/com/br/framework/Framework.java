package com.br.framework;

import com.br.framework.configurator.WindowConfigurator;

public final class Framework {

    private Framework() {
        // Empty constructor.
    }

    public static WindowConfigurator windowConfigurator() {
        return WindowConfigurator.getInstance().newConfiguration();
    }

}
