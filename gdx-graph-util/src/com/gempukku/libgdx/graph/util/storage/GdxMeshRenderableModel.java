package com.gempukku.libgdx.graph.util.storage;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Disposable;
import com.gempukku.libgdx.graph.pipeline.producer.rendering.producer.WritablePropertyContainer;
import com.gempukku.libgdx.graph.shader.ShaderContext;
import com.gempukku.libgdx.graph.util.IntMapping;
import com.gempukku.libgdx.graph.util.culling.CullingTest;
import com.gempukku.libgdx.graph.util.model.GraphModelUtil;
import com.gempukku.libgdx.graph.util.model.WritableRenderableModel;
import com.gempukku.libgdx.graph.util.renderer.MeshRenderer;
import com.gempukku.libgdx.graph.util.renderer.TrianglesMeshRenderer;

public class GdxMeshRenderableModel implements WritableRenderableModel, Disposable {
    private final Matrix4 worldTransform = new Matrix4();
    private final Vector3 position = new Vector3();
    private final WritablePropertyContainer propertyContainer;
    private CullingTest cullingTest;

    private final Mesh mesh;
    private final VertexAttributes vertexAttributes;
    private final MeshRenderer meshRenderer;
    private int[] attributeLocations;

    private final MemoryMesh memoryMesh;

    public GdxMeshRenderableModel(
            boolean staticBatch, MemoryMesh memoryMesh,
            VertexAttributes vertexAttributes,
            WritablePropertyContainer propertyContainer) {
        this(staticBatch, memoryMesh, vertexAttributes, propertyContainer, new TrianglesMeshRenderer());
    }

    public GdxMeshRenderableModel(
            boolean staticBatch, MemoryMesh memoryMesh,
            VertexAttributes vertexAttributes,
            WritablePropertyContainer propertyContainer, MeshRenderer meshRenderer) {
        this.propertyContainer = propertyContainer;
        this.memoryMesh = memoryMesh;

        this.vertexAttributes = vertexAttributes;
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
    public boolean isRendered(Camera camera) {
        return !memoryMesh.isEmpty() && !isCulled(camera);
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
        memoryMesh.updateGdxMesh(mesh);
    }

    @Override
    public void render(Camera camera, ShaderProgram shaderProgram, IntMapping<String> propertyToLocationMapping) {
        if (attributeLocations == null)
            attributeLocations = GraphModelUtil.getAttributeLocations(shaderProgram, vertexAttributes);
        memoryMesh.renderGdxMesh(shaderProgram, mesh, attributeLocations, meshRenderer);
    }
}
