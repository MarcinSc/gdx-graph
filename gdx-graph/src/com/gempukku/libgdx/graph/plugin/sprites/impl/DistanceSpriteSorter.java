package com.gempukku.libgdx.graph.plugin.sprites.impl;

import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.gempukku.libgdx.graph.plugin.sprites.GraphSprite;

import java.util.Comparator;

public class DistanceSpriteSorter implements Comparator<GraphSprite> {
    public enum Order {
        Front_To_Back, Back_To_Front;

        public int result(float dst) {
            if (this == Front_To_Back)
                return dst > 0 ? 1 : (dst < 0 ? -1 : 0);
            else
                return dst < 0 ? 1 : (dst > 0 ? -1 : 0);
        }
    }

    private Vector3 cameraPosition;
    private final Order order;

    public DistanceSpriteSorter(Order order) {
        this.order = order;
    }

    public void sort(Vector3 cameraPosition, Array<GraphSprite> renderables) {
        this.cameraPosition = cameraPosition;
        renderables.sort(this);
    }

    @Override
    public int compare(GraphSprite o1, GraphSprite o2) {
        Vector3 position1 = o1.getRenderableSprite().getPosition();
        Vector3 position2 = o2.getRenderableSprite().getPosition();
        final float dst = (int) (1000f * cameraPosition.dst2(position1)) - (int) (1000f * cameraPosition.dst2(position2));
        return order.result(dst);
    }
}
