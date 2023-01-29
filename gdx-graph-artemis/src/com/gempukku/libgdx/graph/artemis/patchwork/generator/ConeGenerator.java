package com.gempukku.libgdx.graph.artemis.patchwork.generator;

import com.badlogic.gdx.graphics.g3d.utils.shapebuilders.ConeShapeBuilder;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.utils.JsonValue;
import com.gempukku.libgdx.graph.artemis.Vector2ValuePerVertex;
import com.gempukku.libgdx.graph.artemis.Vector3ValuePerVertex;
import com.gempukku.libgdx.graph.util.model.GeometryMeshCapture;

public class ConeGenerator implements PatchGenerator {
    @Override
    public GeneratedPatch generatePatch(Matrix4 transform, JsonValue generatorParameters) {
        GeometryMeshCapture capture = new GeometryMeshCapture();
        capture.setVertexTransform(transform);

        float width = generatorParameters.getFloat("width", 1f);
        float height = generatorParameters.getFloat("height", 1f);
        float depth = generatorParameters.getFloat("depth", 1f);
        int divisions = generatorParameters.getInt("divisions", 5);
        float angleFrom = generatorParameters.getFloat("angleFrom", 0f);
        float angleTo = generatorParameters.getFloat("angleTo", 360f);
        boolean close = generatorParameters.getBoolean("close", true);

        ConeShapeBuilder.build(capture, width, height, depth, divisions, angleFrom, angleTo, close);

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
}
