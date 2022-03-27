package com.gempukku.libgdx.graph.plugin.models.strategy;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.utils.Array;
import com.gempukku.libgdx.graph.plugin.models.GraphModel;
import com.gempukku.libgdx.graph.plugin.models.impl.GraphModelsImpl;

public interface ModelRenderingStrategy {
    void processModels(GraphModelsImpl models, Array<String> tags, Camera camera, StrategyCallback callback);

    interface StrategyCallback {
        void begin();

        void process(GraphModel graphModel, String tag);

        void end();
    }
}
