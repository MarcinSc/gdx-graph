package com.gempukku.libgdx.graph.plugin.models.design.producer;

import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.gempukku.libgdx.graph.shader.builder.recipe.GraphShaderRecipe;
import com.gempukku.libgdx.graph.ui.preview.MeshPreviewRenderableModel;
import com.gempukku.libgdx.graph.ui.preview.ShaderPreview;

public class ModelShaderPreview extends ShaderPreview {
    public enum ShaderPreviewModel {
        Sphere, Rectangle;
    }

    private ShaderPreviewModel previewModel = ShaderPreviewModel.Sphere;
    private MeshPreviewRenderableModel renderableModel;

    public ModelShaderPreview(GraphShaderRecipe shaderRecipe) {
        super(shaderRecipe);
    }

    private MeshPreviewRenderableModel createRenderableModel(ShaderPreviewModel shaderPreviewModel) {
        Model model = createModel(shaderPreviewModel);

        MeshPreviewRenderableModel result = new MeshPreviewRenderableModel(model.meshes.get(0));
        model.dispose();

        return result;
    }

    private Model createModel(ShaderPreviewModel shaderPreviewModel) {
        ModelBuilder modelBuilder = new ModelBuilder();
        Material material = new Material();

        switch (shaderPreviewModel) {
            case Rectangle:
                return modelBuilder.createRect(
                        -0.5f, 0.5f, 0,
                         -0.5f, -0.5f,0,
                         0.5f, -0.5f,0,
                         0.5f, 0.5f,0,
                        0, 0, 1,
                        material,
                        VertexAttributes.Usage.Position | VertexAttributes.Usage.Normal | VertexAttributes.Usage.Tangent | VertexAttributes.Usage.TextureCoordinates);
            case Sphere:
                float sphereDiameter = 0.8f;
                return modelBuilder.createSphere(sphereDiameter, sphereDiameter, sphereDiameter, 50, 50,
                        material,
                        VertexAttributes.Usage.Position | VertexAttributes.Usage.Normal | VertexAttributes.Usage.Tangent | VertexAttributes.Usage.TextureCoordinates);
        }
        return null;
    }

    public void setModel(ShaderPreviewModel previewModel) {
        this.previewModel = previewModel;
        if (renderableModel != null) {
            renderableModel.dispose();
        }
        renderableModel = createRenderableModel(this.previewModel);
        setRenderableModel(renderableModel);
    }

    @Override
    protected void initializeWidget() {
        super.initializeWidget();

        renderableModel = createRenderableModel(previewModel);
        setRenderableModel(renderableModel);
    }

    @Override
    protected void disposeWidget() {
        super.disposeWidget();
        renderableModel.dispose();
    }
}
