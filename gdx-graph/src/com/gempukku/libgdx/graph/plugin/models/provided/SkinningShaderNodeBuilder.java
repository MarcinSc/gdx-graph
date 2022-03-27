package com.gempukku.libgdx.graph.plugin.models.provided;

import com.badlogic.gdx.graphics.VertexAttribute;
import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.ObjectMap;
import com.badlogic.gdx.utils.ObjectSet;
import com.gempukku.libgdx.graph.plugin.models.ModelsUniformSetters;
import com.gempukku.libgdx.graph.plugin.models.config.provided.SkinningShaderNodeConfiguration;
import com.gempukku.libgdx.graph.shader.GraphShader;
import com.gempukku.libgdx.graph.shader.GraphShaderContext;
import com.gempukku.libgdx.graph.shader.builder.FragmentShaderBuilder;
import com.gempukku.libgdx.graph.shader.builder.VertexShaderBuilder;
import com.gempukku.libgdx.graph.shader.field.ShaderFieldType;
import com.gempukku.libgdx.graph.shader.node.ConfigurationShaderNodeBuilder;
import com.gempukku.libgdx.graph.shader.node.DefaultFieldOutput;

public class SkinningShaderNodeBuilder extends ConfigurationShaderNodeBuilder {
    public SkinningShaderNodeBuilder() {
        super(new SkinningShaderNodeConfiguration());
    }

    @Override
    protected ObjectMap<String, ? extends FieldOutput> buildVertexNodeSingleInputs(boolean designTime, String nodeId, JsonValue data, ObjectMap<String, FieldOutput> inputs, ObjectSet<String> producedOutputs, VertexShaderBuilder vertexShaderBuilder, GraphShaderContext graphShaderContext, GraphShader graphShader) {
        int boneCount = data.getInt("boneCount");
        int boneWeightCount = data.getInt("boneWeightCount");

        if (!vertexShaderBuilder.hasUniformVariable("u_bones")) {
            vertexShaderBuilder.addArrayUniformVariable("u_bones", boneCount, "mat4", false, new ModelsUniformSetters.Bones(boneCount), "Skeletal bones");
            for (int i = 0; i < boneWeightCount; i++) {
                vertexShaderBuilder.addAttributeVariable(VertexAttribute.BoneWeight(i), "vec2");
            }
        }

        String functionName = "getSkinning_" + nodeId;
        String skinningMatrixName = "skinning_" + nodeId;
        if (!vertexShaderBuilder.containsFunction(functionName)) {
            StringBuilder getSkinning = new StringBuilder();
            getSkinning.append("mat4 " + functionName + "() {\n");
            getSkinning.append("  mat4 skinning = mat4(0.0);\n");
            for (int i = 0; i < boneWeightCount; i++) {
                getSkinning.append("  skinning += (a_boneWeight").append(i).append(".y) * u_bones[int(a_boneWeight").append(i).append(".x)];\n");
            }
            getSkinning.append("  return skinning;\n");
            getSkinning.append("}\n");

            vertexShaderBuilder.addFunction(functionName, getSkinning.toString());

            vertexShaderBuilder.addMainLine("mat4 " + skinningMatrixName + " = " + functionName + "();");
        }

        ObjectMap<String, FieldOutput> result = new ObjectMap<>();
        if (producedOutputs.contains("skinnedPosition")) {
            String positionVarying = "v_skinning_position_" + nodeId;
            if (!vertexShaderBuilder.hasVaryingVariable(positionVarying)) {
                FieldOutput position = inputs.get("position");
                String positionField = (position != null) ? position.getRepresentation() : "vec3(0.0, 0.0, 0.0)";
                String variableName = "skinningPosition_" + nodeId;
                vertexShaderBuilder.addMainLine("vec3 " + variableName + " = (" + skinningMatrixName + " * vec4(" + positionField + ", 1.0)).xyz;");

                vertexShaderBuilder.addVaryingVariable(positionVarying, "vec3");
                vertexShaderBuilder.addMainLine(positionVarying + " = " + variableName + ";");

                result.put("skinnedPosition", new DefaultFieldOutput(ShaderFieldType.Vector3, variableName));
            }
        }
        if (producedOutputs.contains("skinnedNormal")) {
            String normalVarying = "v_skinning_normal_" + nodeId;
            if (!vertexShaderBuilder.hasVaryingVariable(normalVarying)) {
                FieldOutput normal = inputs.get("normal");
                String normalField = (normal != null) ? normal.getRepresentation() : "vec3(0.0, 1.0, 0.0)";
                String variableName = "skinningNormal_" + nodeId;
                vertexShaderBuilder.addMainLine("vec3 " + variableName + " = normalize((" + skinningMatrixName + " * vec4(" + normalField + ", 0.0)).xyz);");

                vertexShaderBuilder.addVaryingVariable(normalVarying, "vec3");
                vertexShaderBuilder.addMainLine(normalVarying + " = " + variableName + ";");

                result.put("skinnedNormal", new DefaultFieldOutput(ShaderFieldType.Vector3, variableName));
            }
        }

        return result;
    }

    @Override
    protected ObjectMap<String, ? extends FieldOutput> buildFragmentNodeSingleInputs(boolean designTime, String nodeId, JsonValue data, ObjectMap<String, FieldOutput> inputs, ObjectSet<String> producedOutputs, VertexShaderBuilder vertexShaderBuilder, FragmentShaderBuilder fragmentShaderBuilder, GraphShaderContext graphShaderContext, GraphShader graphShader) {
        int boneCount = data.getInt("boneCount");
        int boneWeightCount = data.getInt("boneWeightCount");

        if (!vertexShaderBuilder.hasUniformVariable("u_bones")) {
            vertexShaderBuilder.addArrayUniformVariable("u_bones", boneCount, "mat4", false, new ModelsUniformSetters.Bones(boneCount), "Skeletal bones");
            for (int i = 0; i < boneWeightCount; i++) {
                vertexShaderBuilder.addAttributeVariable(VertexAttribute.BoneWeight(i), "vec2");
            }
        }

        String skinningMatrixName = "skinning_" + nodeId;
        String functionName = "getSkinning_" + nodeId;
        if (!vertexShaderBuilder.containsFunction(functionName)) {
            StringBuilder getSkinning = new StringBuilder();
            getSkinning.append("mat4 " + functionName + "() {\n");
            getSkinning.append("  mat4 skinning = mat4(0.0);\n");
            for (int i = 0; i < boneWeightCount; i++) {
                getSkinning.append("  skinning += (a_boneWeight").append(i).append(".y) * u_bones[int(a_boneWeight").append(i).append(".x)];\n");
            }
            getSkinning.append("  return skinning;\n");
            getSkinning.append("}\n");

            vertexShaderBuilder.addFunction(functionName, getSkinning.toString());

            vertexShaderBuilder.addMainLine("mat4 " + skinningMatrixName + " = " + functionName + "();");
        }

        ObjectMap<String, FieldOutput> result = new ObjectMap<>();
        if (producedOutputs.contains("skinnedPosition")) {
            String positionVarying = "v_skinning_position_" + nodeId;
            if (!vertexShaderBuilder.hasVaryingVariable(positionVarying)) {
                FieldOutput position = inputs.get("position");
                String positionField = (position != null) ? position.getRepresentation() : "vec3(0.0, 0.0, 0.0)";
                String variableName = "skinningPosition_" + nodeId;
                vertexShaderBuilder.addMainLine("vec3 " + variableName + " = (" + skinningMatrixName + " * vec4(" + positionField + ", 1.0)).xyz;");

                vertexShaderBuilder.addVaryingVariable(positionVarying, "vec3");
                vertexShaderBuilder.addMainLine(positionVarying + " = " + variableName + ";");
            }
            if (!fragmentShaderBuilder.hasVaryingVariable(positionVarying)) {
                fragmentShaderBuilder.addVaryingVariable(positionVarying, "vec3");
            }
            result.put("skinnedPosition", new DefaultFieldOutput(ShaderFieldType.Vector3, positionVarying));
        }
        if (producedOutputs.contains("skinnedNormal")) {
            String normalVarying = "v_skinning_normal_" + nodeId;
            if (!vertexShaderBuilder.hasVaryingVariable(normalVarying)) {
                FieldOutput normal = inputs.get("normal");
                String normalField = (normal != null) ? normal.getRepresentation() : "vec3(0.0, 1.0, 0.0)";
                String variableName = "skinningNormal_" + nodeId;
                vertexShaderBuilder.addMainLine("vec3 " + variableName + " = normalize((" + skinningMatrixName + " * vec4(" + normalField + ", 0.0)).xyz);");

                vertexShaderBuilder.addVaryingVariable(normalVarying, "vec3");
                vertexShaderBuilder.addMainLine(normalVarying + " = " + variableName + ";");
            }
            if (!fragmentShaderBuilder.hasVaryingVariable(normalVarying)) {
                fragmentShaderBuilder.addVaryingVariable(normalVarying, "vec3");
            }
            result.put("skinnedNormal", new DefaultFieldOutput(ShaderFieldType.Vector3, normalVarying));
        }

        return result;
    }
}
