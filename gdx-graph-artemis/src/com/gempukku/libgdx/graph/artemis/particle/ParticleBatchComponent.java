package com.gempukku.libgdx.graph.artemis.particle;

import com.artemis.PooledComponent;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectMap;

public class ParticleBatchComponent extends PooledComponent {
    private String pipelineName = "";
    private String shapeName;
    private String name;
    private String renderTag;
    private Array<String> particleBirthAttributes;
    private Array<String> particleDeathAttributes;
    private int spritesPerPage = 16383;

    private final ObjectMap<String, Object> properties = new ObjectMap<>();

    public String getPipelineName() {
        return pipelineName;
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

    public Array<String> getParticleBirthAttributes() {
        return particleBirthAttributes;
    }

    public Array<String> getParticleDeathAttributes() {
        return particleDeathAttributes;
    }

    public int getSpritesPerPage() {
        return spritesPerPage;
    }

    public ObjectMap<String, Object> getProperties() {
        return properties;
    }

    @Override
    protected void reset() {
        shapeName = null;
        name = null;
        renderTag = null;
        spritesPerPage = 16383;
        properties.clear();
    }
}
