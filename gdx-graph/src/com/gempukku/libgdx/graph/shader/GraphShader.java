package com.gempukku.libgdx.graph.shader;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.IntArray;
import com.badlogic.gdx.utils.ObjectMap;
import com.gempukku.libgdx.graph.shader.property.PropertySource;

public abstract class GraphShader extends UniformCachingShader implements GraphShaderContext {
    private final Array<Disposable> disposableList = new Array<>();
    protected ObjectMap<String, PropertySource> propertySourceMap = new ObjectMap<>();
    private ShaderProgram shaderProgram;
    private VertexAttributes vertexAttributes;
    private int[] attributeLocations;

    public GraphShader(String tag, Texture defaultTexture) {
        super(tag, defaultTexture);
    }

    public void setProgram(ShaderProgram shaderProgram) {
        this.shaderProgram = shaderProgram;
    }

    public ShaderProgram getShaderProgram() {
        return shaderProgram;
    }

    public void setVertexAttributes(VertexAttributes vertexAttributes) {
        this.vertexAttributes = vertexAttributes;
    }

    public VertexAttributes getVertexAttributes() {
        return vertexAttributes;
    }

    public int[] getAttributeLocations() {
        if (attributeLocations == null) {
            IntArray tempArray = new IntArray();
            final int n = vertexAttributes.size();
            for (int i = 0; i < n; i++) {
                Attribute attribute = attributes.get(vertexAttributes.get(i).alias);
                if (attribute != null)
                    tempArray.add(attribute.getLocation());
                else
                    tempArray.add(-1);
            }
            attributeLocations = tempArray.items;
        }
        return attributeLocations;
    }

    public void init() {
        init(shaderProgram);
    }

    public void addPropertySource(String name, PropertySource propertySource) {
        propertySourceMap.put(name, propertySource);
    }

    public ObjectMap<String, PropertySource> getProperties() {
        return propertySourceMap;
    }

    @Override
    public PropertySource getPropertySource(String name) {
        return propertySourceMap.get(name);
    }

    @Override
    public void addManagedResource(Disposable disposable) {
        disposableList.add(disposable);
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
}
