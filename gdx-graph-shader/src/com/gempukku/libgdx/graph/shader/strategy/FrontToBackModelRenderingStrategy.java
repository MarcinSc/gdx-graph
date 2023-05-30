package com.gempukku.libgdx.graph.shader.strategy;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Pool;
import com.badlogic.gdx.utils.Pools;
import com.gempukku.libgdx.graph.shader.GraphShader;
import com.gempukku.libgdx.graph.shader.ShaderRendererConfiguration;

public class FrontToBackModelRenderingStrategy implements ModelRenderingStrategy {
    private static final Pool<ModelWithShader> pool = Pools.get(ModelWithShader.class);
    private final Array<ModelWithShader> orderingArray = new Array<>();
    private final DistanceModelWithShaderSorter modelSorter = new DistanceModelWithShaderSorter(DistanceModelWithShaderSorter.Order.Front_To_Back);

    @Override
    public void processModels(ShaderRendererConfiguration configuration, Array<GraphShader> shaders, Camera camera, StrategyCallback callback) {
        callback.begin();
        clearSortingArray();
        for (Object model : configuration.getModels()) {
            for (GraphShader shader : shaders) {
                if (configuration.isRendered(model, shader, camera)) {
                    ModelWithShader modelWithShader = pool.obtain();
                    modelWithShader.setShader(shader);
                    modelWithShader.setModel(model);
                    orderingArray.add(modelWithShader);
                }
            }
        }
        modelSorter.sort(configuration, camera.position, orderingArray);
        for (ModelWithShader model : orderingArray) {
            callback.process(model.getModel(), model.getShader());
        }
        callback.end();
    }

    private void clearSortingArray() {
        pool.freeAll(orderingArray);
        orderingArray.clear();
    }
}
