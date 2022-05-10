package com.gempukku.libgdx.graph.util.sprite;

import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.ObjectMap;
import com.gempukku.libgdx.graph.pipeline.producer.rendering.producer.WritablePropertyContainer;
import com.gempukku.libgdx.graph.plugin.models.GraphModels;
import com.gempukku.libgdx.graph.shader.property.MapWritablePropertyContainer;
import com.gempukku.libgdx.graph.util.culling.CullingTest;
import com.gempukku.libgdx.graph.util.property.HierarchicalPropertyContainer;

public class TexturePagedSpriteBatchModel implements SpriteBatchModel {
    private final Vector3 position = new Vector3();
    private final Matrix4 worldTransform = new Matrix4();
    private CullingTest cullingTest;
    private final ObjectMap<String, MultiPageSpriteBatchModel> batchModelPerTextureSignature = new ObjectMap<>();

    private final boolean staticBatch;
    private final int pageSpriteCapacity;
    private final GraphModels graphModels;
    private final String tag;
    private final WritablePropertyContainer propertyContainer;

    public TexturePagedSpriteBatchModel(boolean staticBatch, int pageSpriteCapacity, GraphModels graphModels, String tag) {
        this(staticBatch, pageSpriteCapacity, graphModels, tag, new MapWritablePropertyContainer());
    }

    public TexturePagedSpriteBatchModel(boolean staticBatch, int pageSpriteCapacity, GraphModels graphModels, String tag,
                                        WritablePropertyContainer propertyContainer) {
        this.staticBatch = staticBatch;
        this.pageSpriteCapacity = pageSpriteCapacity;
        this.graphModels = graphModels;
        this.tag = tag;
        this.propertyContainer = propertyContainer;
    }

    @Override
    public boolean addSprite(RenderableSprite sprite) {
        String textureSignature = getTextureSignature(sprite);

        MultiPageSpriteBatchModel spriteBatchModel = batchModelPerTextureSignature.get(textureSignature);
        if (spriteBatchModel == null) {
            HierarchicalPropertyContainer childPropertyContainer = new HierarchicalPropertyContainer(propertyContainer);

            spriteBatchModel = createNewSpriteBatchModel(childPropertyContainer);

            setupTextures(spriteBatchModel, sprite);
            batchModelPerTextureSignature.put(textureSignature, spriteBatchModel);
        }
        return spriteBatchModel.addSprite(sprite);
    }

    private MultiPageSpriteBatchModel createNewSpriteBatchModel(WritablePropertyContainer propertyContainer) {
        MultiPageSpriteBatchModel spriteBatchModel = new MultiPageSpriteBatchModel(staticBatch, pageSpriteCapacity, graphModels, tag, propertyContainer);
        spriteBatchModel.setCullingTest(cullingTest);
        spriteBatchModel.setPosition(position);
        spriteBatchModel.setWorldTransform(worldTransform);
        return spriteBatchModel;
    }

    @Override
    public boolean hasSprite(RenderableSprite sprite) {
        for (MultiPageSpriteBatchModel value : batchModelPerTextureSignature.values()) {
            if (value.hasSprite(sprite))
                return true;
        }
        return false;
    }

    @Override
    public boolean removeSprite(RenderableSprite sprite) {
        for (MultiPageSpriteBatchModel value : batchModelPerTextureSignature.values()) {
            if (value.removeSprite(sprite))
                return true;
        }

        return false;
    }


    @Override
    public boolean updateSprite(RenderableSprite sprite) {
        String oldTextureSignature = getOldTextureSignature(sprite);
        if (oldTextureSignature == null)
            return false;

        String textureSignature = getTextureSignature(sprite);
        if (textureSignature.equals(oldTextureSignature)) {
            return batchModelPerTextureSignature.get(textureSignature).updateSprite(sprite);
        } else {
            batchModelPerTextureSignature.get(oldTextureSignature).removeSprite(sprite);
            return batchModelPerTextureSignature.get(textureSignature).addSprite(sprite);
        }
    }

    @Override
    public WritablePropertyContainer getPropertyContainer() {
        return propertyContainer;
    }

    @Override
    public void setCullingTest(CullingTest cullingTest) {
        this.cullingTest = cullingTest;
        for (MultiPageSpriteBatchModel value : batchModelPerTextureSignature.values()) {
            value.setCullingTest(cullingTest);
        }
    }

    @Override
    public void setPosition(Vector3 position) {
        this.position.set(position);
        for (MultiPageSpriteBatchModel value : batchModelPerTextureSignature.values()) {
            value.setPosition(position);
        }
    }

    @Override
    public void setWorldTransform(Matrix4 worldTransform) {
        this.worldTransform.set(worldTransform);
        for (MultiPageSpriteBatchModel value : batchModelPerTextureSignature.values()) {
            value.setWorldTransform(worldTransform);
        }
    }

    @Override
    public void dispose() {
        for (MultiPageSpriteBatchModel value : batchModelPerTextureSignature.values()) {
            value.dispose();
        }
        batchModelPerTextureSignature.clear();
    }

    private String getOldTextureSignature(RenderableSprite sprite) {
        for (ObjectMap.Entry<String, MultiPageSpriteBatchModel> keyToSpriteBatchModel : batchModelPerTextureSignature) {
            if (keyToSpriteBatchModel.value.hasSprite(sprite))
                return keyToSpriteBatchModel.key;
        }
        return null;
    }

    private void setupTextures(MultiPageSpriteBatchModel spriteBatchModel, RenderableSprite sprite) {

    }

    private String getTextureSignature(RenderableSprite sprite) {
        return null;
    }
}
