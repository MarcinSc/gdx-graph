package com.gempukku.libgdx.graph.entity;

import com.badlogic.ashley.core.Component;

public class EntityDef {
    private Component[] components;

    public Component[] getComponents() {
        return components;
    }

    public void setComponents(Component... components) {
        this.components = components;
    }
}
