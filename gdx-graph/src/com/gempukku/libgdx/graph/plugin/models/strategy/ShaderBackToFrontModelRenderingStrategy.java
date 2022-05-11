package com.gempukku.libgdx.graph.plugin.models.strategy;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Pool;
import com.badlogic.gdx.utils.Pools;
import com.gempukku.libgdx.graph.plugin.models.RenderableModel;
import com.gempukku.libgdx.graph.plugin.models.impl.GraphModelsImpl;

public class ShaderBackToFrontModelRenderingStrategy implements ModelRenderingStrategy {
    private static final Pool<ModelWithTag> pool = Pools.get(ModelWithTag.class);
    private final Array<ModelWithTag> orderingArray = new Array<>();
    private final DistanceModelSorter modelSorter = new DistanceModelSorter(DistanceModelSorter.Order.Back_To_Front);

    @Override
    public void processModels(GraphModelsImpl models, Array<String> tags, Camera camera, StrategyCallback callback) {
        callback.begin();
        for (String tag : tags) {
            clearSortingArray();
            for (RenderableModel model : models.getModels(tag))
                if (model.isRendered(camera)) {
                    ModelWithTag modelWithTag = pool.obtain();
                    modelWithTag.setTag(tag);
                    modelWithTag.setRenderableModel(model);
                    orderingArray.add(modelWithTag);
                }
            modelSorter.sort(camera.position, orderingArray);
            for (ModelWithTag model : orderingArray) {
                callback.process(model.getRenderableModel(), model.getTag());
            }
        }
        callback.end();
    }

    private void clearSortingArray() {
        pool.freeAll(orderingArray);
        orderingArray.clear();
    }
}
