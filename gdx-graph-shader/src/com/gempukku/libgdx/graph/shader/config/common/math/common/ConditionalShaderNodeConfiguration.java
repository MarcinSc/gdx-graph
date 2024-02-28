package com.gempukku.libgdx.graph.shader.config.common.math.common;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectMap;
import com.gempukku.libgdx.common.Function;
import com.gempukku.libgdx.graph.config.DefaultMenuNodeConfiguration;
import com.gempukku.libgdx.graph.shader.field.ShaderFieldType;
import com.gempukku.libgdx.graph.data.impl.DefaultGraphNodeInput;
import com.gempukku.libgdx.graph.data.impl.DefaultGraphNodeOutput;

public class ConditionalShaderNodeConfiguration extends DefaultMenuNodeConfiguration {
    public ConditionalShaderNodeConfiguration() {
        super("Conditional", "Conditional", "Math/Common");
        addNodeInput(
                new DefaultGraphNodeInput("a", "A", true, ShaderFieldType.Vector4, ShaderFieldType.Vector3, ShaderFieldType.Vector2, ShaderFieldType.Float));
        addNodeInput(
                new DefaultGraphNodeInput("b", "B", true, ShaderFieldType.Vector4, ShaderFieldType.Vector3, ShaderFieldType.Vector2, ShaderFieldType.Float));
        addNodeInput(
                new DefaultGraphNodeInput("true", "True", true, ShaderFieldType.Vector4, ShaderFieldType.Vector3, ShaderFieldType.Vector2, ShaderFieldType.Float));
        addNodeInput(
                new DefaultGraphNodeInput("false", "False", true, ShaderFieldType.Vector4, ShaderFieldType.Vector3, ShaderFieldType.Vector2, ShaderFieldType.Float));
        addNodeOutput(
                new DefaultGraphNodeOutput("output", "Result",
                        new Function<ObjectMap<String, Array<String>>, String>() {
                            @Override
                            public String evaluate(ObjectMap<String, Array<String>> entries) {
                                Array<String> a = entries.get("a");
                                Array<String> b = entries.get("b");
                                Array<String> aTrue = entries.get("true");
                                Array<String> aFalse = entries.get("false");
                                if (a == null || a.size != 1 || b == null || b.size != 1
                                        || aTrue == null || aTrue.size != 1 || aFalse == null || aFalse.size != 1)
                                    return null;
                                if (!a.get(0).equals(b.get(0)) || !aTrue.get(0).equals(aFalse.get(0)))
                                    return null;

                                return aTrue.get(0);
                            }
                        },
                        ShaderFieldType.Float, ShaderFieldType.Vector2, ShaderFieldType.Vector3, ShaderFieldType.Vector4));
    }
}
