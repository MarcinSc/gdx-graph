package com.gempukku.libgdx.graph.shader.preview;

import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.VertexAttribute;
import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.JsonValue;
import com.gempukku.libgdx.common.Producer;
import com.gempukku.libgdx.common.ValueProducer;
import com.gempukku.libgdx.graph.pipeline.util.ArrayValuePerVertex;
import com.gempukku.libgdx.graph.shader.AttributeFunctions;

public class MeshPreviewModels {
    private static Producer<PreviewRenderableModelProducer> sphereModelSupplier;
    private static Producer<PreviewRenderableModelProducer> rectangleModelSupplier;

    public static Producer<PreviewRenderableModelProducer> getRectangleModelProducer() {
        if (rectangleModelSupplier == null) {
            rectangleModelSupplier = new ValueProducer<>(
                    new ModelProducer(createRectangleModelSupplier()));
        }
        return rectangleModelSupplier;
    }

    public static Producer<PreviewRenderableModelProducer> getSphereModelProducer() {
        if (sphereModelSupplier == null) {
            sphereModelSupplier = new ValueProducer<>(
                    new ModelProducer(createSphereModelSupplier()));
        }
        return sphereModelSupplier;
    }

    private static Producer<PreviewRenderableModel> createRectangleModelSupplier() {
        ModelBuilder modelBuilder = new ModelBuilder();
        Material material = new Material();
        Model model = modelBuilder.createRect(
                -1f, 1f, 1,
                -1f, -1f, 1,
                1f, -1f, 1,
                1f, 1f, 1,
                0, 0, 1,
                material,
                VertexAttributes.Usage.Position | VertexAttributes.Usage.Normal | VertexAttributes.Usage.Tangent | VertexAttributes.Usage.TextureCoordinates);
        try {
            return createModelProducerFromMesh(model.meshes.get(0));
        } finally {
            model.dispose();
        }
    }

    private static Producer<PreviewRenderableModel> createSphereModelSupplier() {
        ModelBuilder modelBuilder = new ModelBuilder();
        Material material = new Material();
        float sphereDiameter = 0.8f;
        Model model = modelBuilder.createSphere(sphereDiameter, sphereDiameter, sphereDiameter, 50, 50,
                material,
                VertexAttributes.Usage.Position | VertexAttributes.Usage.Normal | VertexAttributes.Usage.Tangent | VertexAttributes.Usage.TextureCoordinates);
        try {
            return createModelProducerFromMesh(model.meshes.get(0));
        } finally {
            model.dispose();
        }
    }

    private static Producer<PreviewRenderableModel> createModelProducerFromMesh(Mesh mesh) {
        int vertexCount = mesh.getNumVertices();
        short[] indices = new short[mesh.getNumIndices()];
        mesh.getIndices(indices);

        ArrayValuePerVertex<Vector3> positionValue = createVector3Value(VertexAttributes.Usage.Position, mesh);
        ArrayValuePerVertex<Vector3> normalValue = createVector3Value(VertexAttributes.Usage.Normal, mesh);
        ArrayValuePerVertex<Vector3> tangentValue = createVector3Value(VertexAttributes.Usage.Tangent, mesh);
        ArrayValuePerVertex<Vector2> uvValue = createVector2Value(VertexAttributes.Usage.TextureCoordinates, mesh);

        return new Producer<PreviewRenderableModel>() {
            @Override
            public PreviewRenderableModel create() {
                MapPreviewRenderableModel result = new MapPreviewRenderableModel(vertexCount, indices);
                result.addTag("Test");
                result.addAttributeFunctionValue(AttributeFunctions.Position, positionValue);
                result.addAttributeFunctionValue(AttributeFunctions.Normal, normalValue);
                result.addAttributeFunctionValue(AttributeFunctions.Tangent, tangentValue);
                result.addAttributeFunctionValue(AttributeFunctions.TexCoord0, uvValue);
                return result;
            }
        };
    }

    private static ArrayValuePerVertex<Vector3> createVector3Value(int usage, Mesh mesh) {
        int vertexCount = mesh.getNumVertices();
        int floatsPerVertex = mesh.getVertexSize() / 4;

        float[] vertices = new float[vertexCount * floatsPerVertex];
        mesh.getVertices(vertices);

        VertexAttribute attribute = mesh.getVertexAttributes().findByUsage(usage);
        if (attribute == null)
            return null;

        int offset = attribute.offset / 4;

        Array<Vector3> result = new Array<>(Vector3.class);
        for (int i = 0; i < vertexCount; i++) {
            result.add(new Vector3(
                    vertices[floatsPerVertex * i + offset + 0],
                    vertices[floatsPerVertex * i + offset + 1],
                    vertices[floatsPerVertex * i + offset + 2]));
        }

        return new ArrayValuePerVertex<>(result.toArray());
    }

    private static ArrayValuePerVertex<Vector2> createVector2Value(int usage, Mesh mesh) {
        int vertexCount = mesh.getNumVertices();
        int floatsPerVertex = mesh.getVertexSize() / 4;

        float[] vertices = new float[vertexCount * floatsPerVertex];
        mesh.getVertices(vertices);

        VertexAttribute attribute = mesh.getVertexAttributes().findByUsage(usage);
        if (attribute == null)
            return null;

        int offset = attribute.offset / 4;

        Array<Vector2> result = new Array<>(Vector2.class);
        for (int i = 0; i < vertexCount; i++) {
            result.add(new Vector2(
                    vertices[floatsPerVertex * i + offset + 0],
                    vertices[floatsPerVertex * i + offset + 1]));
        }

        return new ArrayValuePerVertex<>(result.toArray());
    }

    private static class ModelProducer implements PreviewRenderableModelProducer {
        private final Producer<PreviewRenderableModel> modelProducer;

        public ModelProducer(Producer<PreviewRenderableModel> modelProducer) {
            this.modelProducer = modelProducer;
        }

        @Override
        public void initialize(JsonValue data) {

        }

        @Override
        public void serialize(JsonValue value) {

        }

        @Override
        public Actor getCustomizationActor() {
            return null;
        }

        @Override
        public PreviewRenderableModel create() {
            return modelProducer.create();
        }
    }
}
