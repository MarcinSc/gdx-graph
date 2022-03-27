package com.gempukku.libgdx.graph.plugin.models.strategy;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.utils.Array;
import com.gempukku.libgdx.graph.plugin.models.GraphModel;
import com.gempukku.libgdx.graph.plugin.models.impl.GraphModelsImpl;

public class ShaderUnorderedModelRenderingStrategy implements ModelRenderingStrategy {
    @Override
    public void processModels(GraphModelsImpl models, Array<String> tags, Camera camera, StrategyCallback callback) {
        callback.begin();
        for (String tag : tags) {
            for (GraphModel model : models.getModels(tag)) {
                if (model.getRenderableModel().isRendered(camera)) {
                    callback.process(model, tag);
                }
            }
        }
        callback.end();
    }
}
