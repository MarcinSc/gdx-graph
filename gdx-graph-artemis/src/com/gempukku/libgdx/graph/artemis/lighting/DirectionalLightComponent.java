package com.gempukku.libgdx.graph.artemis.lighting;

import com.artemis.Component;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector3;

public class DirectionalLightComponent extends Component {
    private String environmentName;
    private Vector3 direction;
    private Color color;
    private float intensity = 1f;
    private boolean shadowEnabled = false;
    private int shadowBufferSize = 256;

    public String getEnvironmentName() {
        return environmentName;
    }

    public Vector3 getDirection() {
        return direction;
    }

    public Color getColor() {
        return color;
    }

    public float getIntensity() {
        return intensity;
    }

    public boolean isShadowEnabled() {
        return shadowEnabled;
    }

    public int getShadowBufferSize() {
        return shadowBufferSize;
    }
}
