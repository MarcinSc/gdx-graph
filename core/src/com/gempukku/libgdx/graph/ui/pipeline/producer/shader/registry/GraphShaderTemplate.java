package com.gempukku.libgdx.graph.ui.pipeline.producer.shader.registry;

import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.JsonValue;

public interface GraphShaderTemplate {
    String getTitle();

    void invokeTemplate(Stage stage, Callback callback);

    interface Callback {
        void addShader(String tag, JsonValue shader);
    }
}
