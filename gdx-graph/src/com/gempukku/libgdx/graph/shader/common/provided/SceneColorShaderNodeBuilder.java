package com.gempukku.libgdx.graph.shader.common.provided;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g3d.utils.TextureDescriptor;
import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.ObjectMap;
import com.badlogic.gdx.utils.ObjectSet;
import com.gempukku.libgdx.common.LibGDXCollections;
import com.gempukku.libgdx.graph.shader.*;
import com.gempukku.libgdx.graph.shader.builder.CommonShaderBuilder;
import com.gempukku.libgdx.graph.shader.builder.VertexShaderBuilder;
import com.gempukku.libgdx.graph.shader.config.common.provided.SceneColorShaderNodeConfiguration;
import com.gempukku.libgdx.graph.shader.field.ShaderFieldType;
import com.gempukku.libgdx.graph.shader.node.ConfigurationCommonShaderNodeBuilder;
import com.gempukku.libgdx.graph.shader.node.DefaultTextureFieldOutput;

public class SceneColorShaderNodeBuilder extends ConfigurationCommonShaderNodeBuilder {
    public SceneColorShaderNodeBuilder() {
        super(new SceneColorShaderNodeConfiguration());
    }

    @Override
    public ObjectMap<String, ? extends FieldOutput> buildVertexNodeSingleInputs(boolean designTime, String nodeId, JsonValue data, ObjectMap<String, FieldOutput> inputs, ObjectSet<String> producedOutputs, VertexShaderBuilder vertexShaderBuilder, GraphShaderContext graphShaderContext, GraphShader graphShader) {
        throw new UnsupportedOperationException("Sampling of textures is not available in vertex shader in OpenGL ES");
    }

    @Override
    protected ObjectMap<String, ? extends FieldOutput> buildCommonNode(boolean designTime, String nodeId, JsonValue data, ObjectMap<String, FieldOutput> inputs, ObjectSet<String> producedOutputs,
                                                                       CommonShaderBuilder commonShaderBuilder, GraphShaderContext graphShaderContext, GraphShader graphShader) {
        graphShader.setUsingColorTexture(true);
        String textureName = "u_" + nodeId;
        String transformName = "u_UV" + nodeId;
        final TextureDescriptor<Texture> textureDescriptor = new TextureDescriptor<>();
        textureDescriptor.minFilter = Texture.TextureFilter.Linear;
        textureDescriptor.magFilter = Texture.TextureFilter.Linear;
        textureDescriptor.uWrap = Texture.TextureWrap.ClampToEdge;
        textureDescriptor.vWrap = Texture.TextureWrap.ClampToEdge;

        Texture.TextureWrap uWrap = Texture.TextureWrap.ClampToEdge;
        Texture.TextureWrap vWrap = Texture.TextureWrap.ClampToEdge;
        if (data != null && data.has("uWrap"))
            uWrap = Texture.TextureWrap.valueOf(data.getString("uWrap"));
        if (data != null && data.has("vWrap"))
            vWrap = Texture.TextureWrap.valueOf(data.getString("vWrap"));

        commonShaderBuilder.addUniformVariable(textureName, "sampler2D", true,
                new UniformRegistry.UniformSetter() {
                    @Override
                    public void set(BasicShader shader, int location, ShaderContext shaderContext) {
                        textureDescriptor.texture = shaderContext.getColorTexture();
                        shader.setUniform(location, textureDescriptor);
                    }
                }, "Scene color texture");
        commonShaderBuilder.addUniformVariable(transformName, "vec4", false,
                new UniformRegistry.UniformSetter() {
                    @Override
                    public void set(BasicShader shader, int location, ShaderContext shaderContext) {
                        shader.setUniform(location, 0f, 0f, 1f, 1f);
                    }
                }, "Scene color texture UVs");

        return LibGDXCollections.singletonMap("texture",
                new DefaultTextureFieldOutput(ShaderFieldType.TextureRegion, transformName, textureName, uWrap, vWrap));
    }
}
