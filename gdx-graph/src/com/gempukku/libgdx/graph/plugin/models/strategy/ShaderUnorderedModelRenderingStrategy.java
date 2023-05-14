package com.gempukku.libgdx.graph.plugin.models.strategy;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.utils.Array;
import com.gempukku.libgdx.graph.plugin.models.RenderableModel;
import com.gempukku.libgdx.graph.plugin.models.impl.GraphModelsImpl;
import com.gempukku.libgdx.graph.shader.GraphShader;

public class ShaderUnorderedModelRenderingStrategy implements ModelRenderingStrategy {
    @Override
    public void processModels(GraphModelsImpl models, Array<GraphShader> shaders, Camera camera, StrategyCallback callback) {
        callback.begin();
        for (GraphShader shader : shaders) {
            for (RenderableModel model : models.getModels()) {
                if (model.isRendered(shader, camera)) {
                    callback.process(model, shader);
                }
            }
        }
        callback.end();
    }
}
