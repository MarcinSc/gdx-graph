package com.gempukku.libgdx.graph.artemis.lighting;

import com.artemis.Component;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector3;

public class LightingEnvironmentComponent extends Component {
    private String id;
    private String name;
    private Vector3 center;
    private int radius;
    private Color ambientColor;

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Vector3 getCenter() {
        return center;
    }

    public int getRadius() {
        return radius;
    }

    public Color getAmbientColor() {
        return ambientColor;
    }
}
