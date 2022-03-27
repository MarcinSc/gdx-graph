package com.gempukku.libgdx.graph.util.sprite;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.ObjectMap;
import com.gempukku.libgdx.graph.pipeline.producer.rendering.producer.PropertyContainer;
import com.gempukku.libgdx.graph.pipeline.producer.rendering.producer.WritablePropertyContainer;
import com.gempukku.libgdx.graph.plugin.sprites.GraphSprite;
import com.gempukku.libgdx.graph.plugin.sprites.GraphSprites;
import com.gempukku.libgdx.graph.plugin.sprites.RenderableSprite;
import com.gempukku.libgdx.graph.shader.property.MapWritablePropertyContainer;
import com.gempukku.libgdx.graph.util.culling.CullingTest;

public class CommonPropertiesSpriteAdapter {
    private final GraphSprites graphSprites;
    private final Vector3 position = new Vector3();
    private final WritablePropertyContainer propertyContainer;
    private final ObjectMap<String, GraphSprite> tagSprites = new ObjectMap<>();
    private CullingTest cullingTest;

    private final RenderableSpriteImpl renderableSprite = new RenderableSpriteImpl();

    public CommonPropertiesSpriteAdapter(GraphSprites graphSprites) {
        this(graphSprites, Vector3.Zero);
    }

    public CommonPropertiesSpriteAdapter(GraphSprites graphSprites, Vector3 position) {
        this(graphSprites, position, null);
    }

    public CommonPropertiesSpriteAdapter(GraphSprites graphSprites, Vector3 position, CullingTest cullingTest) {
        this(graphSprites, position, cullingTest, new MapWritablePropertyContainer());
    }

    public CommonPropertiesSpriteAdapter(GraphSprites graphSprites, Vector3 position, CullingTest cullingTest, WritablePropertyContainer propertyContainer) {
        this.graphSprites = graphSprites;
        this.position.set(position);
        this.propertyContainer = propertyContainer;
        this.cullingTest = cullingTest;
    }

    public void addTag(String tag) {
        if (!hasTag(tag)) {
            tagSprites.put(tag, graphSprites.addSprite(tag, renderableSprite));
        }
    }

    public void removeTag(String tag) {
        if (hasTag(tag)) {
            graphSprites.removeSprite(tagSprites.remove(tag));
        }
    }

    public boolean hasTag(String tag) {
        return tagSprites.containsKey(tag);
    }

    public void setCullingTest(CullingTest cullingTest) {
        this.cullingTest = cullingTest;
    }

    public Vector3 getPosition() {
        return position;
    }

    public WritablePropertyContainer getPropertyContainer() {
        return propertyContainer;
    }

    public void updateSprite() {
        for (GraphSprite value : tagSprites.values()) {
            graphSprites.updateSprite(value);
        }
    }

    private class RenderableSpriteImpl implements RenderableSprite {
        @Override
        public Vector3 getPosition() {
            return position;
        }

        @Override
        public boolean isRendered(Camera camera) {
            return cullingTest == null || !cullingTest.isCulled(camera, position);
        }

        @Override
        public PropertyContainer getPropertyContainer(String tag) {
            return propertyContainer;
        }
    }
}
