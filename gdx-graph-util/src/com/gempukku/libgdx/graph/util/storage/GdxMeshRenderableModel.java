package com.gempukku.libgdx.graph.util.storage;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;
import com.gempukku.libgdx.graph.pipeline.producer.rendering.producer.WritablePropertyContainer;
import com.gempukku.libgdx.graph.shader.ShaderContext;
import com.gempukku.libgdx.graph.util.IntMapping;
import com.gempukku.libgdx.graph.util.culling.CullingTest;
import com.gempukku.libgdx.graph.util.model.GraphModelUtil;
import com.gempukku.libgdx.graph.util.renderer.MeshRenderer;
import com.gempukku.libgdx.graph.util.renderer.TrianglesMeshRenderer;

public class GdxMeshRenderableModel<T, U> implements ObjectRenderableModel<T, U> {
    private final Matrix4 worldTransform = new Matrix4();
    private final Vector3 position = new Vector3();
    private final WritablePropertyContainer propertyContainer;
    private CullingTest cullingTest;

    private final Mesh mesh;
    private final VertexAttributes vertexAttributes;
    private final MeshRenderer meshRenderer;
    private int[] attributeLocations;

    private final ObjectMeshStorage<T, U> objectMeshStorage;

    public GdxMeshRenderableModel(
            boolean staticBatch, ObjectMeshStorage<T, U> objectMeshStorage,
            VertexAttributes vertexAttributes,
            WritablePropertyContainer propertyContainer) {
        this(staticBatch, objectMeshStorage, vertexAttributes, propertyContainer, new TrianglesMeshRenderer());
    }

    public GdxMeshRenderableModel(
            boolean staticBatch, ObjectMeshStorage<T, U> objectMeshStorage,
            VertexAttributes vertexAttributes,
            WritablePropertyContainer propertyContainer, MeshRenderer meshRenderer) {
        this.propertyContainer = propertyContainer;
        this.objectMeshStorage = objectMeshStorage;

        this.vertexAttributes = vertexAttributes;
        this.meshRenderer = meshRenderer;

        mesh = new Mesh(staticBatch, true,
                objectMeshStorage.getMaxVertexCount(),
                objectMeshStorage.getIndexArray().length, vertexAttributes);
        mesh.setVertices(objectMeshStorage.getVertexArray());
        mesh.setIndices(objectMeshStorage.getIndexArray());
    }

    @Override
    public boolean isEmpty() {
        return objectMeshStorage.getUsedIndexCount() == 0;
    }

    @Override
    public boolean canStore(T object) {
        return objectMeshStorage.canStore(object);
    }

    @Override
    public void setPosition(Vector3 position) {
        this.position.set(position);
    }

    @Override
    public void setWorldTransform(Matrix4 worldTransform) {
        this.worldTransform.set(worldTransform);
    }

    @Override
    public void setCullingTest(CullingTest cullingTest) {
        this.cullingTest = cullingTest;
    }

    @Override
    public U addObject(T object) {
        return objectMeshStorage.addObject(object);
    }

    @Override
    public boolean containsObject(U objectReference) {
        return objectMeshStorage.containsObject(objectReference);
    }

    @Override
    public U updateObject(T object, U objectReference) {
        return objectMeshStorage.updateObject(object, objectReference);
    }

    @Override
    public void removeObject(U objectReference) {
        objectMeshStorage.removeObject(objectReference);
    }

    @Override
    public WritablePropertyContainer getPropertyContainer() {
        return propertyContainer;
    }

    @Override
    public void dispose() {
        mesh.dispose();
    }

    @Override
    public Vector3 getPosition() {
        return position;
    }

    @Override
    public boolean isRendered(Camera camera) {
        return objectMeshStorage.getUsedIndexCount() > 0 && !isCulled(camera);
    }

    private boolean isCulled(Camera camera) {
        return cullingTest != null && cullingTest.isCulled(camera, getPosition());
    }

    @Override
    public Matrix4 getWorldTransform() {
        return worldTransform;
    }

    @Override
    public void prepareToRender(ShaderContext shaderContext) {
        int minUpdatedIndex = objectMeshStorage.getMinUpdatedIndexArrayIndex();
        int maxUpdatedIndex = objectMeshStorage.getMaxUpdatedIndexArrayIndex();

        if (minUpdatedIndex < maxUpdatedIndex) {
            if (Gdx.app.getLogLevel() >= Gdx.app.LOG_DEBUG)
                Gdx.app.debug("MeshRendering", "Updating index array - short count: " + objectMeshStorage.getIndexArray().length);
            mesh.setIndices(objectMeshStorage.getIndexArray());
        }

        int minUpdatedVertexValueIndex = objectMeshStorage.getMinUpdatedVertexArrayIndex();
        int maxUpdatedVertexValueIndex = objectMeshStorage.getMaxUpdatedVertexArrayIndex();
        if (minUpdatedVertexValueIndex < maxUpdatedVertexValueIndex) {
            if (Gdx.app.getLogLevel() >= Gdx.app.LOG_DEBUG)
                Gdx.app.debug("MeshRendering", "Updating vertex array - float count: " + (maxUpdatedVertexValueIndex - minUpdatedVertexValueIndex));
            mesh.updateVertices(minUpdatedVertexValueIndex, objectMeshStorage.getVertexArray(), minUpdatedVertexValueIndex, maxUpdatedVertexValueIndex - minUpdatedVertexValueIndex);
        }

        objectMeshStorage.resetUpdates();
    }

    @Override
    public void render(Camera camera, ShaderProgram shaderProgram, IntMapping<String> propertyToLocationMapping) {
        int indexStart = objectMeshStorage.getUsedIndexStart();
        int indexCount = objectMeshStorage.getUsedIndexCount();
        if (Gdx.app.getLogLevel() >= Gdx.app.LOG_DEBUG)
            Gdx.app.debug("MeshRendering", "Rendering " + indexCount + " indexes(s)");
        if (attributeLocations == null)
            attributeLocations = GraphModelUtil.getAttributeLocations(shaderProgram, vertexAttributes);

        meshRenderer.renderMesh(shaderProgram, mesh, indexStart, indexCount, attributeLocations);
    }
}
