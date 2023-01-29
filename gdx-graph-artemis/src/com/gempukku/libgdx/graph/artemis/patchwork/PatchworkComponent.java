package com.gempukku.libgdx.graph.artemis.patchwork;

import com.artemis.PooledComponent;
import com.badlogic.gdx.utils.ObjectMap;

public class PatchworkComponent extends PooledComponent {
    public enum SystemType {
        TexturePaged, MultiPaged
    }

    private SystemType type;
    private String name;
    private String renderTag;
    private int minimumPages = 1;
    private boolean staticBatch = true;
    private int indexCapacityPerPage = 65535;
    private int vertexCapacityPerPage = 262144;

    private final ObjectMap<String, Object> properties = new ObjectMap<>();

    public SystemType getType() {
        return type;
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

    public int getIndexCapacityPerPage() {
        return indexCapacityPerPage;
    }

    public int getVertexCapacityPerPage() {
        return vertexCapacityPerPage;
    }

    public ObjectMap<String, Object> getProperties() {
        return properties;
    }

    @Override
    protected void reset() {
        type = null;
        name = null;
        renderTag = null;
        minimumPages = 1;
        staticBatch = true;
        indexCapacityPerPage = 65535;
        vertexCapacityPerPage = 262144;
        properties.clear();
    }
}
