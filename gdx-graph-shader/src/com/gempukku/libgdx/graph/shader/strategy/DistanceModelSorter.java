package com.gempukku.libgdx.graph.shader.strategy;

import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.gempukku.libgdx.graph.shader.GraphShader;
import com.gempukku.libgdx.graph.shader.ShaderRendererConfiguration;

import java.util.Comparator;

public class DistanceModelSorter implements Comparator<Object> {
    public enum Order {
        Front_To_Back, Back_To_Front;

        public int result(float dst) {
            if (this == Front_To_Back)
                return dst > 0 ? 1 : (dst < 0 ? -1 : 0);
            else
                return dst < 0 ? 1 : (dst > 0 ? -1 : 0);
        }
    }

    private ShaderRendererConfiguration configuration;
    private GraphShader shader;
    private Vector3 cameraPosition;
    private final Order order;

    public DistanceModelSorter(Order order) {
        this.order = order;
    }

    public void sort(ShaderRendererConfiguration configuration, GraphShader shader, Vector3 cameraPosition, Array<Object> renderables) {
        this.configuration = configuration;
        this.shader = shader;
        this.cameraPosition = cameraPosition;
        renderables.sort(this);
    }

    @Override
    public int compare(Object o1, Object o2) {
        Vector3 position1 = configuration.getPosition(o1, shader);
        Vector3 position2 = configuration.getPosition(o2, shader);
        final float dst = (int) (1000f * cameraPosition.dst2(position1)) - (int) (1000f * cameraPosition.dst2(position2));
        return order.result(dst);
    }
}
