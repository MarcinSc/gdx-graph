package com.gempukku.libgdx.graph.shader;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.ObjectMap;
import com.gempukku.libgdx.graph.pipeline.producer.rendering.producer.ShaderContextImpl;
import com.gempukku.libgdx.graph.plugin.models.RenderableModel;
import com.gempukku.libgdx.graph.shader.field.ArrayShaderFieldType;
import com.gempukku.libgdx.graph.shader.field.ShaderFieldType;
import com.gempukku.libgdx.graph.shader.property.PropertyLocation;
import com.gempukku.libgdx.graph.shader.property.ShaderPropertySource;
import com.gempukku.libgdx.graph.util.IntMapping;

public class GraphShader extends UniformCachingShader implements GraphShaderContext {
    private final PropertyToLocationMapping propertyToLocationMapping = new PropertyToLocationMapping();
    private final Array<Disposable> disposableList = new Array<>();
    protected ObjectMap<String, ShaderPropertySource> propertySourceMap = new ObjectMap<>();
    private ShaderProgram shaderProgram;

    public GraphShader(String tag, Texture defaultTexture) {
        super(tag, defaultTexture);
    }

    public void setProgram(ShaderProgram shaderProgram) {
        this.shaderProgram = shaderProgram;
    }

    public ShaderProgram getShaderProgram() {
        return shaderProgram;
    }

    public ObjectMap<String, Attribute> getAttributes() {
        return attributes;
    }

    public IntMapping<String> getPropertyToLocationMapping() {
        return propertyToLocationMapping;
    }

    public void init() {
        init(shaderProgram);
    }

    public void addPropertySource(String name, ShaderPropertySource shaderPropertySource) {
        propertySourceMap.put(name, shaderPropertySource);
    }

    public ObjectMap<String, ShaderPropertySource> getProperties() {
        return propertySourceMap;
    }

    @Override
    public ShaderPropertySource getPropertySource(String name) {
        return propertySourceMap.get(name);
    }

    @Override
    public void addManagedResource(Disposable disposable) {
        disposableList.add(disposable);
    }

    public void render(ShaderContextImpl shaderContext, RenderableModel renderableModel) {
        renderableModel.prepareToRender(shaderContext);

        shaderContext.setRenderableModel(renderableModel);
        shaderContext.setLocalPropertyContainer(renderableModel.getPropertyContainer());

        for (Uniform uniform : localUniforms.values()) {
            uniform.getSetter().set(this, uniform.getLocation(), shaderContext);
        }
        for (StructArrayUniform uniform : localStructArrayUniforms.values()) {
            uniform.getSetter().set(this, uniform.getStartIndex(), uniform.getFieldOffsets(), uniform.getSize(), shaderContext);
        }
        renderableModel.render(shaderContext.getCamera(), program, getPropertyToLocationMapping());
    }

    @Override
    public void dispose() {
        for (Disposable disposable : disposableList) {
            disposable.dispose();
        }
        disposableList.clear();

        if (shaderProgram != null)
            shaderProgram.dispose();
        super.dispose();
    }

    private class PropertyToLocationMapping implements IntMapping<String> {
        @Override
        public int map(String value) {
            ShaderPropertySource shaderPropertySource = propertySourceMap.get(value);
            if (shaderPropertySource == null) {
                int lastIndex = value.lastIndexOf('_');
                if (lastIndex > -1) {
                    String arrayValue = value.substring(0, lastIndex);
                    shaderPropertySource = propertySourceMap.get(arrayValue);
                    if (shaderPropertySource == null)
                        return -1;

                    ShaderFieldType shaderFieldType = shaderPropertySource.getShaderFieldType();
                    if (!(shaderFieldType instanceof ArrayShaderFieldType))
                        return -1;

                    int index = Integer.parseInt(value.substring(lastIndex + 1));
                    ArrayShaderFieldType arrayShaderFieldType = (ArrayShaderFieldType) shaderFieldType;
                    if (index >= arrayShaderFieldType.getArrayLength())
                        return -1;
                    return attributes.get(shaderPropertySource.getAttributeName(index)).getLocation();
                }
                return -1;
            }
            if (shaderPropertySource.getPropertyLocation() != PropertyLocation.Attribute)
                return -1;
            Attribute attribute = attributes.get(shaderPropertySource.getAttributeName());
            if (attribute == null)
                return -1;
            return attribute.getLocation();
        }
    }
}
