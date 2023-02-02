package com.gempukku.libgdx.graph.artemis.patchwork.generator;

import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.JsonValue;
import com.gempukku.libgdx.graph.artemis.Vector2ValuePerVertex;
import com.gempukku.libgdx.graph.artemis.Vector3ValuePerVertex;
import com.gempukku.libgdx.graph.util.model.GeometryMeshCapture;

public class BoxGenerator implements PatchGenerator {
    @Override
    public GeneratedPatch generatePatch(Matrix4 transform, JsonValue generatorParameters) {
        GeometryMeshCapture capture = new GeometryMeshCapture();
        capture.setVertexTransform(transform);

        float width = generatorParameters.getFloat("width", 1f);
        float height = generatorParameters.getFloat("height", 1f);
        float depth = generatorParameters.getFloat("depth", 1f);
        boolean inside = generatorParameters.getBoolean("inside", false);

        float w2 = width / 2;
        float h2 = height / 2;
        float d2 = depth / 2;

        if (inside) {
            w2 *= -1;
            h2 *= -1;
            d2 *= -1;
        }

        float x0 = -w2;
        float y0 = -h2;
        float z0 = -d2;
        float x1 = w2;
        float y1 = h2;
        float z1 = d2;

        Vector3 v1 = new Vector3();
        Vector3 v2 = new Vector3();
        Vector3 v3 = new Vector3();
        Vector3 v4 = new Vector3();
        Vector3 n = new Vector3();

        capture.rect(v1.set(x0, y0, z0), v2.set(x0, y1, z0), v3.set(x1, y1, z0), v4.set(x1, y0, z0), n.set(0, 0, -1));
        capture.rect(v1.set(x0, y1, z1), v2.set(x0, y0, z1), v3.set(x1, y0, z1), v4.set(x1, y1, z1), n.set(0, 0, 1));
        capture.rect(v1.set(x0, y0, z1), v2.set(x0, y0, z0), v3.set(x1, y0, z0), v4.set(x1, y0, z1), n.set(0, -1, 0));
        capture.rect(v1.set(x0, y1, z0), v2.set(x0, y1, z1), v3.set(x1, y1, z1), v4.set(x1, y1, z0), n.set(0, 1, 0));
        capture.rect(v1.set(x0, y0, z1), v2.set(x0, y1, z1), v3.set(x0, y1, z0), v4.set(x0, y0, z0), n.set(-1, 0, 0));
        capture.rect(v1.set(x1, y0, z0), v2.set(x1, y1, z0), v3.set(x1, y1, z1), v4.set(x1, y0, z1), n.set(1, 0, 0));

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