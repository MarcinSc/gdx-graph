package com.gempukku.libgdx.graph.plugin.models.strategy;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.utils.Array;
import com.gempukku.libgdx.graph.plugin.models.GraphModel;
import com.gempukku.libgdx.graph.plugin.models.impl.GraphModelsImpl;

public class ShaderFrontToBackModelRenderingStrategy implements ModelRenderingStrategy {
    private final Array<GraphModel> orderingArray = new Array<>();
    private final DistanceModelSorter modelSorter = new DistanceModelSorter(DistanceModelSorter.Order.Front_To_Back);

    @Override
    public void processModels(GraphModelsImpl models, Array<String> tags, Camera camera, StrategyCallback callback) {
        callback.begin();
        for (String tag : tags) {
            orderingArray.clear();
            for (GraphModel model : models.getModels(tag))
                if (model.getRenderableModel().isRendered(camera))
                    orderingArray.add(model);
            modelSorter.sort(camera.position, orderingArray);
            for (GraphModel graphModel : orderingArray) {
                callback.process(graphModel, tag);
            }
        }
        callback.end();
    }
}
