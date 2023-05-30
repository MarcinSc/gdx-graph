package com.gempukku.libgdx.graph.shader.strategy;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.utils.Array;
import com.gempukku.libgdx.graph.shader.GraphShader;
import com.gempukku.libgdx.graph.shader.ShaderRendererConfiguration;

public class ShaderUnorderedModelRenderingStrategy implements ModelRenderingStrategy {
    @Override
    public void processModels(ShaderRendererConfiguration configuration, Array<GraphShader> shaders, Camera camera, StrategyCallback callback) {
        callback.begin();
        for (GraphShader shader : shaders) {
            for (Object model : configuration.getModels()) {
                if (configuration.isRendered(model, shader, camera)) {
                    callback.process(model, shader);
                }
            }
        }
        callback.end();
    }
}
