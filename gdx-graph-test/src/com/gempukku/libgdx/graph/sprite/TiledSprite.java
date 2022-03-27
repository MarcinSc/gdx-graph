package com.gempukku.libgdx.graph.sprite;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.gempukku.libgdx.graph.component.AnchorComponent;
import com.gempukku.libgdx.graph.component.PositionComponent;
import com.gempukku.libgdx.graph.component.SizeComponent;
import com.gempukku.libgdx.graph.pipeline.PipelineRenderer;
import com.gempukku.libgdx.graph.pipeline.producer.rendering.producer.PropertyContainer;
import com.gempukku.libgdx.graph.time.TimeProvider;

public class TiledSprite implements Sprite, PropertyContainer {
    private static final Vector3 tmpVec3 = new Vector3();
    private static final Vector2 tmpVec2 = new Vector2();
    private final Entity entity;
    private final TextureRegion textureRegion;
    private final Vector2 tileRepeat = new Vector2();
    private boolean dirty = true;
    private float outline;

    public TiledSprite(Entity entity, TextureRegion textureRegion, Vector2 tileRepeat) {
        this.entity = entity;
        this.textureRegion = textureRegion;
        this.tileRepeat.set(tileRepeat);
    }

    @Override
    public void setOutline(float outline) {
        if (this.outline != outline) {
            this.outline = outline;
            dirty = true;
        }
    }

    @Override
    public Vector3 getPosition() {
        final PositionComponent positionComponent = entity.getComponent(PositionComponent.class);
        return positionComponent.getPosition(TiledSprite.tmpVec3);
    }

    @Override
    public boolean isRendered(Camera camera) {
        return true;
    }

    @Override
    public PropertyContainer getPropertyContainer(String tag) {
        return this;
    }

    @Override
    public Object getValue(String name) {
        if (name.equals("Size")) {
            final SizeComponent sizeComponent = entity.getComponent(SizeComponent.class);
            return sizeComponent.getSize(TiledSprite.tmpVec2);
        }
        if (name.equals("Anchor")) {
            final AnchorComponent anchorComponent = entity.getComponent(AnchorComponent.class);
            return anchorComponent.getAnchor(TiledSprite.tmpVec2);
        }
        if (name.equals("Outline")) {
            return outline;
        }
        if (name.equals("Tile Texture")) {
            return textureRegion;
        }
        if (name.equals("Tile Repeat")) {
            return tileRepeat;
        }
        return null;
    }

    @Override
    public boolean cleanup(TimeProvider timeProvider, PipelineRenderer pipelineRenderer) {
        final PositionComponent positionComponent = entity.getComponent(PositionComponent.class);
        boolean positionDirty = positionComponent.isDirty();

        boolean result = positionDirty || dirty;

        if (positionDirty) {
            positionComponent.clean();
        }

        dirty = false;

        return result;
    }
}
