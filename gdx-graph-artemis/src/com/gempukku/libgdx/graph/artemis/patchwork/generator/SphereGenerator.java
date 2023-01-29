package com.gempukku.libgdx.graph.artemis.patchwork.generator;

import com.badlogic.gdx.graphics.g3d.utils.shapebuilders.SphereShapeBuilder;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.utils.JsonValue;
import com.gempukku.libgdx.graph.artemis.Vector2ValuePerVertex;
import com.gempukku.libgdx.graph.artemis.Vector3ValuePerVertex;
import com.gempukku.libgdx.graph.util.model.GeometryMeshCapture;

public class SphereGenerator implements PatchGenerator {
    @Override
    public GeneratedPatch generatePatch(Matrix4 transform, JsonValue generatorParameters) {
        GeometryMeshCapture capture = new GeometryMeshCapture();
        capture.setVertexTransform(transform);

        float width = generatorParameters.getFloat("width", 1f);
        float height = generatorParameters.getFloat("height", 1f);
        float depth = generatorParameters.getFloat("depth", 1f);
        int divisionsU = generatorParameters.getInt("divisionsU", 5);
        int divisionsV = generatorParameters.getInt("divisionsV", 5);
        float angleUFrom = generatorParameters.getFloat("angleUFrom", 0f);
        float angleUTo = generatorParameters.getFloat("angleUTo", 360f);
        float angleVFrom = generatorParameters.getFloat("angleVFrom", 0f);
        float angleVTo = generatorParameters.getFloat("angleVTo", 180f);

        SphereShapeBuilder.build(capture, width, height, depth, divisionsU, divisionsV, angleUFrom, angleUTo, angleVFrom, angleVTo);

        DefaultGeneratedPatch result = new DefaultGeneratedPatch(capture.getVertexCount(),
                capture.getIndicesArray().toArray());

        String positionProperty = generatorParameters.getString("positionProperty", null);
        if (positionProperty != null) {
            Vector3ValuePerVertex positionValues = new Vector3ValuePerVertex(capture.getPositionArray().toArray());
            result.getProperties().put(positionProperty, positionValues);
        }
        String normalProperty = generatorParameters.getString("normalProperty", null);
        if (normalProperty != null) {
            Vector3ValuePerVertex normalValues = new Vector3ValuePerVertex(capture.getNormalArray().toArray());
            result.getProperties().put(normalProperty, normalValues);
        }
        String uvProperty = generatorParameters.getString("uvProperty", null);
        if (uvProperty != null) {
            Vector2ValuePerVertex uvValues = new Vector2ValuePerVertex(capture.getUvArray().toArray());
            result.getProperties().put(uvProperty, uvValues);
        }

        return result;
    }

    private float getFloat(Object value) {
        return ((Number) value).floatValue();
    }

    private int getInt(Object value) {
        return ((Number) value).intValue();
    }
}
