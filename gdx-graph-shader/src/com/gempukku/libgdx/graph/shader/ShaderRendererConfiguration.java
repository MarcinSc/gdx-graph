package com.gempukku.libgdx.graph.shader;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;
import com.gempukku.libgdx.common.IntMapping;
import com.gempukku.libgdx.graph.data.PropertyContainer;
import com.gempukku.libgdx.graph.pipeline.RendererConfiguration;

public interface ShaderRendererConfiguration<Model> extends ModelContainer<Model>, RendererConfiguration {
    void registerShader(GraphShader graphShader);

    PropertyContainer getGlobalUniforms(GraphShader graphShader);

    PropertyContainer getModelUniforms(Model model, GraphShader graphShader);

    Vector3 getPosition(Model model, GraphShader graphShader);

    Matrix4 getWorldTransform(Model model, GraphShader graphShader);

    boolean isRendered(Model model, GraphShader shader, Camera camera);

    void renderModel(Model model, ShaderContext shaderContext, IntMapping<String> propertyToLocationMapping);
}
