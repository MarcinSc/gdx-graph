package com.gempukku.libgdx.graph.test.component;

import com.artemis.Component;
import com.badlogic.gdx.utils.ObjectMap;

public class StateBasedSpriteComponent extends Component {
    private String pipelineName = "";
    private ObjectMap<String, ObjectMap<String, Object>> stateProperties = new ObjectMap<>();

    public String getPipelineName() {
        return pipelineName;
    }

    public ObjectMap<String, ObjectMap<String, Object>> getStateProperties() {
        return stateProperties;
    }
}
