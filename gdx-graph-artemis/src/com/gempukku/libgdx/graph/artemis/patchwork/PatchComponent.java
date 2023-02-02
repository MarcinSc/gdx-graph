package com.gempukku.libgdx.graph.artemis.patchwork;

import com.artemis.Component;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.ObjectMap;

public class PatchComponent extends Component {
    private Array<String> patchworkName = new Array<>();
    private String generator;
    private JsonValue generatorData;
    private ObjectMap<String, Object> properties = new ObjectMap<>();

    public Array<String> getPatchworkName() {
        return patchworkName;
    }

    public String getGenerator() {
        return generator;
    }

    public JsonValue getGeneratorData() {
        return generatorData;
    }

    public ObjectMap<String, Object> getProperties() {
        return properties;
    }
}
