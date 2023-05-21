package com.gempukku.libgdx.graph.util.sprite;

import com.badlogic.gdx.math.Vector2;
import com.gempukku.libgdx.graph.pipeline.util.ArrayValuePerVertex;

public class SpriteUtil {
    public static final ArrayValuePerVertex<Vector2> QUAD_UVS = new ArrayValuePerVertex<>(
            new Vector2(0, 1), new Vector2(1, 1), new Vector2(0, 0), new Vector2(1, 0));

    private SpriteUtil() {
    }
}
