package com.br.framework.api;

import com.br.framework.api.configurator.FrameConfigurator;

public abstract class Framework {

    public static FrameConfigurator getFrameConfigurator() {
        return FrameConfigurator.getInstance();
    }

}
