package com.gempukku.libgdx.graph.plugin.models.strategy;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.utils.Array;
import com.gempukku.libgdx.graph.plugin.models.RenderableModel;
import com.gempukku.libgdx.graph.plugin.models.impl.GraphModelsImpl;
import com.gempukku.libgdx.graph.shader.GraphShader;

public interface ModelRenderingStrategy {
    void processModels(GraphModelsImpl models, Array<GraphShader> shaders, Camera camera, StrategyCallback callback);

    interface StrategyCallback {
        void begin();

        void process(RenderableModel model, GraphShader shader);

        void end();
    }
}
