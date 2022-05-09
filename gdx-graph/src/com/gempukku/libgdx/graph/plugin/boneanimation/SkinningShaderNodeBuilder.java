package com.gempukku.libgdx.graph.plugin.boneanimation;

import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.ObjectMap;
import com.badlogic.gdx.utils.ObjectSet;
import com.gempukku.libgdx.graph.plugin.boneanimation.config.SkinningShaderNodeConfiguration;
import com.gempukku.libgdx.graph.plugin.boneanimation.property.BoneTransformFieldType;
import com.gempukku.libgdx.graph.plugin.boneanimation.property.BoneWeightFieldType;
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
        FieldOutput boneWeights = inputs.get("boneWeights");
        BoneWeightFieldType boneWeightFieldType = (BoneWeightFieldType) boneWeights.getFieldType();
        FieldOutput boneTransformations = inputs.get("boneTransformations");
        BoneTransformFieldType boneTransformFieldType = (BoneTransformFieldType) boneTransformations.getFieldType();

        String boneTransformVariableName = boneTransformations.getRepresentation();
        String boneWeightVariableName = boneWeights.getRepresentation() + "_";

        int boneWeightCount = boneWeightFieldType.getMaxBoneWeightCount();

        String functionName = "getSkinning_" + nodeId;
        String skinningMatrixName = "skinning_" + nodeId;
        if (!vertexShaderBuilder.containsFunction(functionName)) {
            StringBuilder getSkinning = new StringBuilder();
            getSkinning.append("mat4 " + functionName + "() {\n");
            getSkinning.append("  mat4 skinning = mat4(0.0);\n");
            for (int i = 0; i < boneWeightCount; i++) {
                getSkinning.append("  skinning += (").append(boneWeightVariableName).append(i).append(".y) * " + boneTransformVariableName + "[int(").append(boneWeightVariableName).append(i).append(".x)];\n");
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
        FieldOutput boneWeights = inputs.get("boneWeights");
        BoneWeightFieldType boneWeightFieldType = (BoneWeightFieldType) boneWeights.getFieldType();
        FieldOutput boneTransformations = inputs.get("boneTransformations");
        BoneTransformFieldType boneTransformFieldType = (BoneTransformFieldType) boneTransformations.getFieldType();

        String boneTransformVariableName = boneTransformations.getRepresentation();
        String boneWeightVariableName = boneWeights.getRepresentation() + "_";

        int boneCount = boneWeightFieldType.getMaxBoneWeightCount();
        int boneWeightCount = boneTransformFieldType.getMaxBoneCount();

        String skinningMatrixName = "skinning_" + nodeId;
        String functionName = "getSkinning_" + nodeId;
        if (!vertexShaderBuilder.containsFunction(functionName)) {
            StringBuilder getSkinning = new StringBuilder();
            getSkinning.append("mat4 " + functionName + "() {\n");
            getSkinning.append("  mat4 skinning = mat4(0.0);\n");
            for (int i = 0; i < boneWeightCount; i++) {
                getSkinning.append("  skinning += (").append(boneWeightVariableName).append(i).append(".y) * " + boneTransformVariableName + "[int(").append(boneWeightVariableName).append(i).append(".x)];\n");
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
