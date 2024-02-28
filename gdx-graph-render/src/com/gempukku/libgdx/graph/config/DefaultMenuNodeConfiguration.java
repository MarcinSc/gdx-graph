package com.gempukku.libgdx.graph.config;

import com.gempukku.libgdx.graph.data.impl.DefaultNodeConfiguration;

public class DefaultMenuNodeConfiguration extends DefaultNodeConfiguration implements MenuNodeConfiguration {
    private String menuLocation;

    public DefaultMenuNodeConfiguration(String type, String name, String menuLocation) {
        super(type, name);
        this.menuLocation = menuLocation;
    }

    @Override
    public String getMenuLocation() {
        return menuLocation;
    }
}
