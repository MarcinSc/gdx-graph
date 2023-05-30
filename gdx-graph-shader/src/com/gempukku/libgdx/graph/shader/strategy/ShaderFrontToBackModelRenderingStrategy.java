package com.gempukku.libgdx.graph.shader.strategy;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.utils.Array;
import com.gempukku.libgdx.graph.shader.GraphShader;
import com.gempukku.libgdx.graph.shader.ShaderRendererConfiguration;

public class ShaderFrontToBackModelRenderingStrategy implements ModelRenderingStrategy {
    private static final DistanceModelSorter modelSorter = new DistanceModelSorter(DistanceModelSorter.Order.Front_To_Back);
    private final Array<Object> orderingArray = new Array<>();

    @Override
    public void processModels(ShaderRendererConfiguration configuration, Array<GraphShader> shaders, Camera camera, StrategyCallback callback) {
        callback.begin();
        for (GraphShader shader : shaders) {
            orderingArray.clear();
            for (Object model : configuration.getModels()) {
                if (configuration.isRendered(model, shader, camera)) {
                    orderingArray.add(model);
                }
            }
            modelSorter.sort(configuration, shader, camera.position, orderingArray);
            for (Object renderableModel : orderingArray) {
                callback.process(renderableModel, shader);
            }
        }
        callback.end();
    }
}
