package com.gempukku.libgdx.graph.plugin.models.strategy;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Pool;
import com.badlogic.gdx.utils.Pools;
import com.gempukku.libgdx.graph.plugin.models.RenderableModel;
import com.gempukku.libgdx.graph.plugin.models.impl.GraphModelsImpl;
import com.gempukku.libgdx.graph.shader.GraphShader;

public class FrontToBackModelRenderingStrategy implements ModelRenderingStrategy {
    private static final Pool<ModelWithShader> pool = Pools.get(ModelWithShader.class);
    private final Array<ModelWithShader> orderingArray = new Array<>();
    private final DistanceModelWithShaderSorter modelSorter = new DistanceModelWithShaderSorter(DistanceModelWithShaderSorter.Order.Front_To_Back);

    @Override
    public void processModels(GraphModelsImpl models, Array<GraphShader> shaders, Camera camera, StrategyCallback callback) {
        callback.begin();
        clearSortingArray();
        for (RenderableModel model : models.getModels()) {
            for (GraphShader shader : shaders) {
                if (model.isRendered(shader, camera)) {
                    ModelWithShader modelWithShader = pool.obtain();
                    modelWithShader.setShader(shader);
                    modelWithShader.setRenderableModel(model);
                    orderingArray.add(modelWithShader);
                }
            }
        }
        modelSorter.sort(camera.position, orderingArray);
        for (ModelWithShader model : orderingArray) {
            callback.process(model.getRenderableModel(), model.getShader());
        }
        callback.end();
    }

    private void clearSortingArray() {
        pool.freeAll(orderingArray);
        orderingArray.clear();
    }
}
