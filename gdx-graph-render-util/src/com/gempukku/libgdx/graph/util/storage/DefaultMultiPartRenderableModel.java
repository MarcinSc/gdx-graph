package com.gempukku.libgdx.graph.util.storage;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;
import com.gempukku.libgdx.common.IntMapping;
import com.gempukku.libgdx.graph.data.WritablePropertyContainer;
import com.gempukku.libgdx.graph.shader.GraphShader;
import com.gempukku.libgdx.graph.shader.ShaderContext;
import com.gempukku.libgdx.graph.util.culling.CullingTest;
import com.gempukku.libgdx.graph.util.model.WritableRenderableModel;

public class DefaultMultiPartRenderableModel<T, U> implements MultiPartRenderableModel<T, U> {
    private MultiPartMesh<T, U> multiPartMemoryMesh;
    private WritableRenderableModel renderableModel;

    public DefaultMultiPartRenderableModel(MultiPartMesh<T, U> multiPartMemoryMesh, WritableRenderableModel renderableModel) {
        this.multiPartMemoryMesh = multiPartMemoryMesh;
        this.renderableModel = renderableModel;
    }

    @Override
    public WritablePropertyContainer getPropertyContainer() {
        return renderableModel.getPropertyContainer();
    }

    @Override
    public void setCullingTest(CullingTest cullingTest) {
        renderableModel.setCullingTest(cullingTest);
    }

    @Override
    public void setPosition(Vector3 position) {
        renderableModel.setPosition(position);
    }

    @Override
    public void setWorldTransform(Matrix4 worldTransform) {
        renderableModel.setWorldTransform(worldTransform);
    }

    @Override
    public U addPart(T part) {
        return multiPartMemoryMesh.addPart(part);
    }

    @Override
    public boolean containsPart(U partReference) {
        return multiPartMemoryMesh.containsPart(partReference);
    }

    @Override
    public U updatePart(T part, U partReference) {
        return multiPartMemoryMesh.updatePart(part, partReference);
    }

    @Override
    public void removePart(U partReference) {
        multiPartMemoryMesh.removePart(partReference);
    }

    @Override
    public boolean canStore(T part) {
        return multiPartMemoryMesh.canStore(part);
    }

    @Override
    public boolean isEmpty() {
        return multiPartMemoryMesh.isEmpty();
    }

    @Override
    public Vector3 getPosition() {
        return renderableModel.getPosition();
    }

    @Override
    public boolean isRendered(GraphShader graphShader, Camera camera) {
        return renderableModel.isRendered(graphShader, camera);
    }

    @Override
    public Matrix4 getWorldTransform() {
        return renderableModel.getWorldTransform();
    }

    @Override
    public void prepareToRender(ShaderContext shaderContext) {
        renderableModel.prepareToRender(shaderContext);
    }

    @Override
    public void render(Camera camera, ShaderProgram shaderProgram, IntMapping<String> propertyToLocationMapping) {
        renderableModel.render(camera, shaderProgram, propertyToLocationMapping);
    }

    @Override
    public void dispose() {
        renderableModel.dispose();
    }
}
