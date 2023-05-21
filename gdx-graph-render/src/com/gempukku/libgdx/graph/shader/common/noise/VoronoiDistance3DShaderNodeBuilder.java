package com.gempukku.libgdx.graph.shader.common.noise;

import com.badlogic.gdx.assets.loaders.FileHandleResolver;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.ObjectMap;
import com.badlogic.gdx.utils.ObjectSet;
import com.gempukku.libgdx.common.LibGDXCollections;
import com.gempukku.libgdx.graph.shader.GraphShader;
import com.gempukku.libgdx.graph.shader.GraphShaderContext;
import com.gempukku.libgdx.graph.shader.builder.CommonShaderBuilder;
import com.gempukku.libgdx.graph.shader.common.math.value.RemapShaderNodeBuilder;
import com.gempukku.libgdx.graph.shader.config.common.noise.VoronoiDistance3DNodeConfiguration;
import com.gempukku.libgdx.graph.shader.field.ShaderFieldType;
import com.gempukku.libgdx.graph.shader.field.ShaderFieldTypeRegistry;
import com.gempukku.libgdx.graph.shader.node.ConfigurationCommonShaderNodeBuilder;
import com.gempukku.libgdx.graph.shader.node.DefaultFieldOutput;

import static com.gempukku.libgdx.graph.shader.common.noise.GLSLAdapter.*;

public class VoronoiDistance3DShaderNodeBuilder extends ConfigurationCommonShaderNodeBuilder {
    public VoronoiDistance3DShaderNodeBuilder() {
        super(new VoronoiDistance3DNodeConfiguration());
    }

    @Override
    protected ObjectMap<String, ? extends FieldOutput> buildCommonNode(boolean designTime, String nodeId, JsonValue data, ObjectMap<String, FieldOutput> inputs, ObjectSet<String> producedOutputs,
                                                                       CommonShaderBuilder commonShaderBuilder, GraphShaderContext graphShaderContext, GraphShader graphShader, FileHandleResolver assetResolver) {
        FieldOutput pointValue = inputs.get("point");
        FieldOutput scaleValue = inputs.get("scale");
        FieldOutput progressValue = inputs.get("progress");
        FieldOutput rangeValue = inputs.get("range");

        String scale = (scaleValue != null) ? scaleValue.getRepresentation() : "1.0";
        String progress = (progressValue != null) ? progressValue.getRepresentation() : "0.0";

        loadFragmentIfNotDefined(commonShaderBuilder, assetResolver, "voronoiDistance3d");

        commonShaderBuilder.addMainLine("// Voronoi distance 3D node");
        String name = "result_" + nodeId;
        String output = "voronoiDistance3d(" + pointValue.getRepresentation() + " * " + scale + ", " + progress + ")";

        String noiseRange = "vec3(0.0, 2.6)";
        if (rangeValue != null) {
            String functionName = RemapShaderNodeBuilder.appendRemapFunction(commonShaderBuilder, ShaderFieldTypeRegistry.findShaderFieldType(ShaderFieldType.Float));
            commonShaderBuilder.addMainLine("float " + name + " = " + functionName + "(" + output + ", " + noiseRange + ", " + rangeValue.getRepresentation() + ");");
        } else {
            commonShaderBuilder.addMainLine("float " + name + " = " + output + ";");
        }

        return LibGDXCollections.singletonMap("output", new DefaultFieldOutput(ShaderFieldType.Float, name));
    }

    private static Vector2 voronoiDistanceRandom2(Vector2 p) {
        return fract(sin(new Vector2(dot(p, new Vector2(127.1f, 311.7f)), dot(p, new Vector2(269.5f, 183.3f)))).scl(43758.5453f));
    }

    // The MIT License
    // Copyright © 2013 Inigo Quilez
    // Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated
    // documentation files (the "Software"), to deal in the Software without restriction, including without limitation the
    // rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and
    // to permit persons to whom the Software is furnished to do so, subject to the following conditions: The above
    // copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
    // THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE
    // WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR
    // COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT
    // OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
    private static float voronoiDistance(Vector2 x, float progress) {
        Vector2 n = floor(x);
        Vector2 f = fract(x);

        //----------------------------------
        // first pass: regular voronoi
        //----------------------------------
        Vector2 mg = new Vector2();
        Vector2 mr = new Vector2();

        float md = 8.0f;
        for (int j = -1; j <= 1; j++) {
            for (int i = -1; i <= 1; i++) {
                Vector2 g = new Vector2(1f * i, 1f * j);
                Vector2 o = voronoiDistanceRandom2(new Vector2(n).add(g));
                o = sin(new Vector2(o).scl(6.2831f).add(progress, progress)).scl(0.5f).add(0.5f, 0.5f);
                Vector2 r = new Vector2(g).add(o).sub(f);
                float d = dot(r, r);

                md = Math.min(md, d);
            }
        }

        return (float) Math.sqrt(md);
    }

    public static void main(String[] args) {
        float min = Float.MAX_VALUE;
        float max = -Float.MAX_VALUE;
        // Calculate min and max of simplexNoise2d
        float v = 10f;
        for (float x = -v; x < v; x += 0.01f) {
            for (float y = -v; y < v; y += 0.01f) {
                //for (float z = -v; z < v; z += 0.01f) {
                float noise = voronoiDistance(new Vector2(x, y), 0);
                min = Math.min(min, noise);
                max = Math.max(max, noise);
                //}
            }
        }
        System.out.println("Min: " + min);
        System.out.println("Max: " + max);
    }
}
