package com.gempukku.libgdx.graph.entity.def;

import com.badlogic.gdx.utils.ObjectMap;

public class StateBasedSpriteDef {
    // State based attributes
    private String state;
    private ObjectMap<String, SpriteStateDataDef> stateData;

    public String getState() {
        return state;
    }

    public ObjectMap<String, SpriteStateDataDef> getStateData() {
        return stateData;
    }
}
