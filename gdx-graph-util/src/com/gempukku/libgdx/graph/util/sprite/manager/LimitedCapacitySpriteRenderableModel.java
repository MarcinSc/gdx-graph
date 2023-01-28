package com.gempukku.libgdx.graph.util.sprite.manager;

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
import com.gempukku.libgdx.graph.util.sprite.RenderableSprite;
import com.gempukku.libgdx.graph.util.sprite.SpriteReference;
import com.gempukku.libgdx.graph.util.sprite.SpriteRenderableModel;
import com.gempukku.libgdx.graph.util.sprite.model.QuadSpriteModel;
import com.gempukku.libgdx.graph.util.sprite.model.SpriteModel;
import com.gempukku.libgdx.graph.util.sprite.storage.SpriteStorage;

public class LimitedCapacitySpriteRenderableModel implements SpriteRenderableModel {
    private final Matrix4 worldTransform = new Matrix4();
    private final Vector3 position = new Vector3();
    private final WritablePropertyContainer propertyContainer;
    private CullingTest cullingTest;

    private final Mesh mesh;
    private final SpriteModel spriteModel;
    private final VertexAttributes vertexAttributes;
    private int[] attributeLocations;

    private SpriteStorage<RenderableSprite> spriteStorage;

    public LimitedCapacitySpriteRenderableModel(
            boolean staticBatch, SpriteStorage<RenderableSprite> spriteStorage,
            VertexAttributes vertexAttributes, WritablePropertyContainer propertyContainer) {
        this(staticBatch, spriteStorage, vertexAttributes, propertyContainer,
                new QuadSpriteModel());
    }

    public LimitedCapacitySpriteRenderableModel(
            boolean staticBatch, SpriteStorage<RenderableSprite> spriteStorage,
            VertexAttributes vertexAttributes,
            WritablePropertyContainer propertyContainer, SpriteModel spriteModel) {
        this.propertyContainer = propertyContainer;
        this.spriteModel = spriteModel;
        this.spriteStorage = spriteStorage;

        this.vertexAttributes = vertexAttributes;

        int spriteCapacity = spriteStorage.getSpriteCapacity();

        mesh = new Mesh(staticBatch, true,
                spriteModel.getVertexCount() * spriteCapacity,
                spriteModel.getIndexCount() * spriteCapacity, vertexAttributes);
        mesh.setVertices(spriteStorage.getFloatArray());

        short[] indices = new short[spriteModel.getIndexCount() * spriteCapacity];
        spriteModel.initializeIndexBuffer(indices, spriteCapacity);

        mesh.setIndices(indices, 0, indices.length);
    }

    @Override
    public int getSpriteCount() {
        return spriteStorage.getSpriteCount();
    }

    @Override
    public boolean isAtCapacity() {
        return spriteStorage.isAtCapacity();
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
    public SpriteReference addSprite(RenderableSprite sprite) {
        return spriteStorage.addSprite(sprite);
    }

    @Override
    public boolean containsSprite(SpriteReference spriteReference) {
        return spriteStorage.containsSprite(spriteReference);
    }

    @Override
    public SpriteReference updateSprite(RenderableSprite sprite, SpriteReference spriteReference) {
        spriteStorage.updateSprite(sprite, spriteReference);
        return spriteReference;
    }

    @Override
    public void removeSprite(SpriteReference spriteReference) {
        spriteStorage.removeSprite(spriteReference);
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
        return spriteStorage.getSpriteCount() > 0 && !isCulled(camera);
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
        int minUpdatedIndex = spriteStorage.getMinUpdatedIndex();
        int maxUpdatedIndex = spriteStorage.getMaxUpdatedIndex();

        if (minUpdatedIndex < maxUpdatedIndex) {
            if (Gdx.app.getLogLevel() >= Gdx.app.LOG_DEBUG)
                Gdx.app.debug("Sprite", "Updating vertex array - float count: " + (maxUpdatedIndex - minUpdatedIndex));
            mesh.updateVertices(minUpdatedIndex, spriteStorage.getFloatArray(), minUpdatedIndex, maxUpdatedIndex - minUpdatedIndex);
            spriteStorage.resetUpdates();
        }
    }

    @Override
    public void render(Camera camera, ShaderProgram shaderProgram, IntMapping<String> propertyToLocationMapping) {
        int spriteCount = spriteStorage.getSpriteCount();
        if (Gdx.app.getLogLevel() >= Gdx.app.LOG_DEBUG)
            Gdx.app.debug("Sprite", "Rendering " + spriteCount + " sprite(s)");
        if (attributeLocations == null)
            attributeLocations = GraphModelUtil.getAttributeLocations(shaderProgram, vertexAttributes);

        spriteModel.renderMesh(shaderProgram, mesh, 0, spriteCount, attributeLocations);
    }
}
