package com.gempukku.libgdx.graph.plugin.models.strategy;

import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.gempukku.libgdx.graph.plugin.models.GraphModel;

import java.util.Comparator;

public class DistanceModelSorter implements Comparator<GraphModel> {
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

    public DistanceModelSorter(Order order) {
        this.order = order;
    }

    public void sort(Vector3 cameraPosition, Array<GraphModel> renderables) {
        this.cameraPosition = cameraPosition;
        renderables.sort(this);
    }

    @Override
    public int compare(GraphModel o1, GraphModel o2) {
        Vector3 position1 = o1.getRenderableModel().getPosition();
        Vector3 position2 = o2.getRenderableModel().getPosition();
        final float dst = (int) (1000f * cameraPosition.dst2(position1)) - (int) (1000f * cameraPosition.dst2(position2));
        return order.result(dst);
    }
}
