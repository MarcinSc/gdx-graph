package com.gempukku.libgdx.graph.shader.preview;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.VertexAttribute;
import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.ObjectMap;
import com.badlogic.gdx.utils.ObjectSet;
import com.gempukku.libgdx.common.IntMapping;
import com.gempukku.libgdx.graph.data.PropertyContainer;
import com.gempukku.libgdx.graph.data.WritablePropertyContainer;
import com.gempukku.libgdx.graph.pipeline.util.ArrayValuePerVertex;
import com.gempukku.libgdx.graph.shader.BasicShader;
import com.gempukku.libgdx.graph.shader.GraphShader;
import com.gempukku.libgdx.graph.shader.ShaderContext;
import com.gempukku.libgdx.graph.shader.property.ShaderPropertySource;
import com.gempukku.libgdx.graph.util.model.GraphModelUtil;
import com.gempukku.libgdx.graph.util.model.PropertiesRenderableModel;
import com.gempukku.libgdx.graph.util.property.HierarchicalPropertyContainer;

public class MeshPreviewRenderableModel implements PreviewRenderableModel, Disposable {
    private final int vertexCount;
    private final short[] indices;
    private final ObjectSet<String> tags = new ObjectSet<>();
    private PropertiesRenderableModel propertiesRenderableModel;
    private HierarchicalPropertyContainer hierarchicalPropertyContainer;

    public MeshPreviewRenderableModel(Mesh mesh) {
        this.vertexCount = mesh.getNumVertices();

        this.indices = new short[mesh.getNumIndices()];
        mesh.getIndices(indices);

        this.hierarchicalPropertyContainer = new HierarchicalPropertyContainer();
        fillPropertyContainerBasedOnMesh(hierarchicalPropertyContainer, mesh);
    }

    public void addTag(String tag) {
        tags.add(tag);
    }

    public void removeTag(String tag) {
        tags.remove(tag);
    }

    private static void fillPropertyContainerBasedOnMesh(WritablePropertyContainer propertyContainer, Mesh mesh) {
        propertyContainer.setValue("Position", createVector3Value(VertexAttributes.Usage.Position, mesh));
        propertyContainer.setValue("Normal", createVector3Value(VertexAttributes.Usage.Normal, mesh));
        propertyContainer.setValue("Tangent", createVector3Value(VertexAttributes.Usage.Tangent, mesh));
        propertyContainer.setValue("UV", createVector2Value(VertexAttributes.Usage.TextureCoordinates, mesh));
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

    @Override
    public void updateModel(ObjectMap<String, BasicShader.Attribute> attributeMap,
                            ObjectMap<String, ShaderPropertySource> propertySourceMap, PropertyContainer propertyContainer) {
        if (propertiesRenderableModel != null)
            propertiesRenderableModel.dispose();

        hierarchicalPropertyContainer.setParent(propertyContainer);

        VertexAttributes vertexAttributes = GraphModelUtil.getVertexAttributes(attributeMap);
        ObjectMap<VertexAttribute, ShaderPropertySource> vertexPropertySources = GraphModelUtil.getPropertySourceMap(vertexAttributes, propertySourceMap);

        propertiesRenderableModel = new PropertiesRenderableModel(
                vertexAttributes, vertexPropertySources, vertexCount, indices, hierarchicalPropertyContainer);
    }

    @Override
    public Vector3 getPosition() {
        return propertiesRenderableModel.getPosition();
    }

    @Override
    public boolean isRendered(GraphShader graphShader, Camera camera) {
        return tags.contains(graphShader.getTag()) && propertiesRenderableModel.isRendered(graphShader, camera);
    }

    @Override
    public Matrix4 getWorldTransform() {
        return propertiesRenderableModel.getWorldTransform();
    }

    @Override
    public PropertyContainer getPropertyContainer() {
        return propertiesRenderableModel.getPropertyContainer();
    }

    @Override
    public void prepareToRender(ShaderContext shaderContext) {
        propertiesRenderableModel.prepareToRender(shaderContext);
    }

    @Override
    public void render(Camera camera, ShaderProgram shaderProgram, IntMapping<String> propertyToLocationMapping) {
        propertiesRenderableModel.render(camera, shaderProgram, propertyToLocationMapping);
    }

    @Override
    public void dispose() {
        if (propertiesRenderableModel != null) {
            propertiesRenderableModel.dispose();
        }
    }
}
