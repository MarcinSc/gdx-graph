package com.gempukku.libgdx.graph.shader.strategy;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.utils.Array;
import com.gempukku.libgdx.graph.shader.GraphShader;
import com.gempukku.libgdx.graph.shader.ShaderRendererConfiguration;

public interface ModelRenderingStrategy {
    void processModels(ShaderRendererConfiguration configuration, Array<GraphShader> shaders, Camera camera, StrategyCallback callback);

    interface StrategyCallback {
        void begin();

        void process(Object model, GraphShader shader);

        void end();
    }
}
