package com.gempukku.libgdx.graph.shader.preview;

import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;

public class MeshPreviewModels {
    public static PreviewRenderableModel createRectangle() {
        ModelBuilder modelBuilder = new ModelBuilder();
        Material material = new Material();
        float sphereDiameter = 0.8f;
        Model model = modelBuilder.createRect(
                -0.5f, 0.5f, 0,
                -0.5f, -0.5f,0,
                0.5f, -0.5f,0,
                0.5f, 0.5f,0,
                0, 0, 1,
                material,
                VertexAttributes.Usage.Position | VertexAttributes.Usage.Normal | VertexAttributes.Usage.Tangent | VertexAttributes.Usage.TextureCoordinates);

        PreviewRenderableModel result = new MeshPreviewRenderableModel(model.meshes.get(0));
        model.dispose();
        return result;
    }

    public static PreviewRenderableModel createSphere() {
        ModelBuilder modelBuilder = new ModelBuilder();
        Material material = new Material();
        float sphereDiameter = 0.8f;
        Model model = modelBuilder.createSphere(sphereDiameter, sphereDiameter, sphereDiameter, 50, 50,
                material,
                VertexAttributes.Usage.Position | VertexAttributes.Usage.Normal | VertexAttributes.Usage.Tangent | VertexAttributes.Usage.TextureCoordinates);

        PreviewRenderableModel result = new MeshPreviewRenderableModel(model.meshes.get(0));
        model.dispose();
        return result;
    }
}
