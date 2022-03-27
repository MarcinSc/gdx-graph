package com.gempukku.libgdx.graph.shader.config.common.math.common;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectMap;
import com.gempukku.libgdx.graph.data.NodeConfigurationImpl;
import com.gempukku.libgdx.graph.pipeline.producer.node.GraphNodeInputImpl;
import com.gempukku.libgdx.graph.pipeline.producer.node.GraphNodeOutputImpl;
import com.gempukku.libgdx.graph.shader.field.ShaderFieldType;

import java.util.function.Function;

public class ConditionalShaderNodeConfiguration extends NodeConfigurationImpl {
    public ConditionalShaderNodeConfiguration() {
        super("Conditional", "Conditional", "Math/Common");
        addNodeInput(
                new GraphNodeInputImpl("a", "A", true, ShaderFieldType.Vector4, ShaderFieldType.Vector3, ShaderFieldType.Vector2, ShaderFieldType.Float));
        addNodeInput(
                new GraphNodeInputImpl("b", "B", true, ShaderFieldType.Vector4, ShaderFieldType.Vector3, ShaderFieldType.Vector2, ShaderFieldType.Float));
        addNodeInput(
                new GraphNodeInputImpl("true", "True", true, ShaderFieldType.Vector4, ShaderFieldType.Vector3, ShaderFieldType.Vector2, ShaderFieldType.Float));
        addNodeInput(
                new GraphNodeInputImpl("false", "False", true, ShaderFieldType.Vector4, ShaderFieldType.Vector3, ShaderFieldType.Vector2, ShaderFieldType.Float));
        addNodeOutput(
                new GraphNodeOutputImpl("output", "Result",
                        new Function<ObjectMap<String, Array<String>>, String>() {
                            @Override
                            public String apply(ObjectMap<String, Array<String>> entries) {
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
