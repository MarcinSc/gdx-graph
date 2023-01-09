package com.gempukku.libgdx.graph.test.component;

import com.artemis.Component;
import com.badlogic.gdx.utils.ObjectMap;

public class StateBasedSpriteComponent extends Component {
    private ObjectMap<String, ObjectMap<String, Object>> stateProperties = new ObjectMap<>();

    public ObjectMap<String, ObjectMap<String, Object>> getStateProperties() {
        return stateProperties;
    }
}
