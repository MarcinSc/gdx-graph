package com.gempukku.libgdx.graph.artemis.patchwork.generator;

import com.badlogic.gdx.graphics.g3d.utils.shapebuilders.ArrowShapeBuilder;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.utils.JsonValue;
import com.gempukku.libgdx.graph.artemis.Vector2ValuePerVertex;
import com.gempukku.libgdx.graph.artemis.Vector3ValuePerVertex;
import com.gempukku.libgdx.graph.util.model.GeometryMeshCapture;

public class ArrowGenerator implements PatchGenerator {
    @Override
    public GeneratedPatch generatePatch(Matrix4 transform, JsonValue generatorParameters) {
        GeometryMeshCapture capture = new GeometryMeshCapture();
        capture.setVertexTransform(transform);

        float fromX = generatorParameters.getFloat("fromX", 0f);
        float fromY = generatorParameters.getFloat("fromY", 0f);
        float fromZ = generatorParameters.getFloat("fromZ", 0f);
        float toX = generatorParameters.getFloat("toX", 1f);
        float toY = generatorParameters.getFloat("toY", 1f);
        float toZ = generatorParameters.getFloat("toZ", 1f);
        float capLength = generatorParameters.getFloat("capLength", 0.3f);
        float stemThickness = generatorParameters.getFloat("stemThickness", 0.1f);
        int divisions = generatorParameters.getInt("divisions", 5);

        ArrowShapeBuilder.build(capture, fromX, fromY, fromZ, toX, toY, toZ, capLength, stemThickness, divisions);

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
