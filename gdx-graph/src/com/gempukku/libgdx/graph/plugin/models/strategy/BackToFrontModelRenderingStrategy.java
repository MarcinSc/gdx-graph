package com.gempukku.libgdx.graph.plugin.models.strategy;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.utils.Array;
import com.gempukku.libgdx.graph.plugin.models.GraphModel;
import com.gempukku.libgdx.graph.plugin.models.impl.GraphModelsImpl;

public class BackToFrontModelRenderingStrategy implements ModelRenderingStrategy {
    private final Array<GraphModel> orderingArray = new Array<>();
    private final DistanceModelSorter modelSorter = new DistanceModelSorter(DistanceModelSorter.Order.Back_To_Front);

    @Override
    public void processModels(GraphModelsImpl models, Array<String> tags, Camera camera, StrategyCallback callback) {
        callback.begin();
        orderingArray.clear();
        for (String tag : tags) {
            for (GraphModel model : models.getModels(tag))
                if (model.getRenderableModel().isRendered(camera))
                    orderingArray.add(model);
        }
        modelSorter.sort(camera.position, orderingArray);
        for (GraphModel graphModel : orderingArray) {
            callback.process(graphModel.getRenderableModel(), graphModel.getTag());
        }
        callback.end();
    }
}
