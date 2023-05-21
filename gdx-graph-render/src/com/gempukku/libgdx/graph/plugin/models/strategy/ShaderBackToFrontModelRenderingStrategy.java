package com.gempukku.libgdx.graph.plugin.models.strategy;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.utils.Array;
import com.gempukku.libgdx.graph.plugin.models.RenderableModel;
import com.gempukku.libgdx.graph.plugin.models.impl.GraphModelsImpl;
import com.gempukku.libgdx.graph.shader.GraphShader;

public class ShaderBackToFrontModelRenderingStrategy implements ModelRenderingStrategy {
    private static final DistanceModelSorter modelSorter = new DistanceModelSorter(DistanceModelSorter.Order.Back_To_Front);
    private final Array<RenderableModel> orderingArray = new Array<>();

    @Override
    public void processModels(GraphModelsImpl models, Array<GraphShader> shaders, Camera camera, StrategyCallback callback) {
        callback.begin();
        for (GraphShader shader : shaders) {
            orderingArray.clear();
            for (RenderableModel model : models.getModels()) {
                if (model.isRendered(shader, camera)) {
                    orderingArray.add(model);
                }
            }
            modelSorter.sort(camera.position, orderingArray);
            for (RenderableModel renderableModel : orderingArray) {
                callback.process(renderableModel, shader);
            }
        }
        callback.end();
    }
}