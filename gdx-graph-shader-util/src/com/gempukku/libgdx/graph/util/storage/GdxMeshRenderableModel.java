package com.gempukku.libgdx.graph.util.storage;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Disposable;
import com.gempukku.libgdx.common.IntMapping;
import com.gempukku.libgdx.graph.data.WritablePropertyContainer;
import com.gempukku.libgdx.graph.shader.GraphShader;
import com.gempukku.libgdx.graph.shader.ShaderContext;
import com.gempukku.libgdx.graph.util.culling.CullingTest;
import com.gempukku.libgdx.graph.util.model.GraphModelUtil;
import com.gempukku.libgdx.graph.util.model.WritableRenderableModel;
import com.gempukku.libgdx.graph.util.renderer.MeshRenderer;
import com.gempukku.libgdx.graph.util.renderer.TrianglesMeshRenderer;

public class GdxMeshRenderableModel implements WritableRenderableModel, MeshUpdater, IndexedMeshRenderer, Disposable {
    private final Matrix4 worldTransform = new Matrix4();
    private final Vector3 position = new Vector3();
    private final WritablePropertyContainer propertyContainer;
    private CullingTest cullingTest;

    private final Mesh mesh;
    private final VertexAttributes vertexAttributes;
    private final String tag;
    private final MeshRenderer meshRenderer;

    private ShaderProgram shaderProgram;
    private int[] attributeLocations;

    private final MemoryMesh memoryMesh;

    public GdxMeshRenderableModel(
            boolean staticBatch, MemoryMesh memoryMesh,
            VertexAttributes vertexAttributes,
            WritablePropertyContainer propertyContainer, String tag) {
        this(staticBatch, memoryMesh, vertexAttributes, propertyContainer, tag, new TrianglesMeshRenderer());
    }

    public GdxMeshRenderableModel(
            boolean staticBatch, MemoryMesh memoryMesh,
            VertexAttributes vertexAttributes,
            WritablePropertyContainer propertyContainer, String tag, MeshRenderer meshRenderer) {
        this.propertyContainer = propertyContainer;
        this.memoryMesh = memoryMesh;

        this.vertexAttributes = vertexAttributes;
        this.tag = tag;
        this.meshRenderer = meshRenderer;

        mesh = new Mesh(staticBatch, true,
                memoryMesh.getMaxVertexCount(),
                memoryMesh.getMaxIndexCount(), vertexAttributes);
        memoryMesh.setupGdxMesh(mesh);
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
    public boolean isRendered(GraphShader graphShader, Camera camera) {
        return graphShader.getTag().equals(tag) && !memoryMesh.isEmpty() && !isCulled(camera);
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
        memoryMesh.updateGdxMesh(this);
    }

    @Override
    public void updateIndices(short[] indexValues, int startIndex, int count) {
        mesh.setIndices(indexValues);
    }

    @Override
    public void updateMeshValues(float[] values, int startIndex, int count) {
        mesh.updateVertices(startIndex, values, startIndex, count);
    }

    @Override
    public void render(Camera camera, ShaderProgram shaderProgram, IntMapping<String> propertyToLocationMapping) {
        this.shaderProgram = shaderProgram;
        memoryMesh.renderGdxMesh(this);
    }

    @Override
    public void renderMesh(int startIndex, int indexCount) {
        if (attributeLocations == null)
            attributeLocations = GraphModelUtil.getAttributeLocations(shaderProgram, vertexAttributes);
        meshRenderer.renderMesh(shaderProgram, mesh, startIndex, indexCount, attributeLocations);
    }
}
