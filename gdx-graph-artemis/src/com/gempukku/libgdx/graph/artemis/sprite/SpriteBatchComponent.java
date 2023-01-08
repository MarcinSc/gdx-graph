package com.gempukku.libgdx.graph.artemis.sprite;

import com.artemis.PooledComponent;
import com.badlogic.gdx.utils.ObjectMap;

public class SpriteBatchComponent extends PooledComponent {
    public enum SystemType {
        TexturePaged, MultiPaged
    }

    private SystemType type;
    private String shapeName;
    private String name;
    private String renderTag;
    private int minimumPages = 1;
    private boolean staticBatch = true;
    private int spritesPerPage = 16383;

    private final ObjectMap<String, Object> properties = new ObjectMap<>();

    public SystemType getType() {
        return type;
    }

    public String getShapeName() {
        return shapeName;
    }

    public String getName() {
        return name;
    }

    public String getRenderTag() {
        return renderTag;
    }

    public int getMinimumPages() {
        return minimumPages;
    }

    public boolean isStaticBatch() {
        return staticBatch;
    }

    public int getSpritesPerPage() {
        return spritesPerPage;
    }

    public ObjectMap<String, Object> getProperties() {
        return properties;
    }

    @Override
    protected void reset() {
        type = null;
        shapeName = null;
        name = null;
        renderTag = null;
        minimumPages = 1;
        staticBatch = true;
        spritesPerPage = 16383;
        properties.clear();
    }
}
