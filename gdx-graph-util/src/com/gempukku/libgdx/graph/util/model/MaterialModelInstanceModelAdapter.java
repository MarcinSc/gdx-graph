package com.gempukku.libgdx.graph.util.model;

import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.attributes.FloatAttribute;
import com.badlogic.gdx.graphics.g3d.attributes.TextureAttribute;
import com.badlogic.gdx.graphics.g3d.model.Node;
import com.badlogic.gdx.graphics.g3d.model.NodePart;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.IntArray;
import com.badlogic.gdx.utils.ObjectMap;
import com.badlogic.gdx.utils.ObjectSet;
import com.gempukku.libgdx.graph.pipeline.producer.rendering.producer.PropertyContainer;
import com.gempukku.libgdx.graph.plugin.models.GraphModel;
import com.gempukku.libgdx.graph.plugin.models.GraphModels;
import com.gempukku.libgdx.graph.plugin.models.ModelGraphShader;
import com.gempukku.libgdx.graph.plugin.models.RenderableModel;
import com.gempukku.libgdx.graph.shader.property.MapWritablePropertyContainer;
import com.gempukku.libgdx.graph.shader.property.PropertySource;
import com.gempukku.libgdx.graph.util.culling.CullingTest;

public class MaterialModelInstanceModelAdapter {
    private ModelInstance modelInstance;
    private GraphModels graphModels;
    private ObjectMap<String, ObjectSet<GraphModel>> graphModelsByTag = new ObjectMap<>();

    private ObjectMap<Material, MapWritablePropertyContainer> materialProperties;
    private CullingTest cullingTest;

    public MaterialModelInstanceModelAdapter(ModelInstance modelInstance, GraphModels graphModels) {
        this.modelInstance = modelInstance;
        this.graphModels = graphModels;

        materialProperties = new ObjectMap<>();
        for (Material material : modelInstance.materials) {
            MapWritablePropertyContainer properties = new MapWritablePropertyContainer();
            properties.setValue("Diffuse Color", getColor(material, ColorAttribute.Diffuse));
            properties.setValue("Ambient Color", getColor(material, ColorAttribute.Ambient));
            properties.setValue("Emissive Color", getColor(material, ColorAttribute.Emissive));
            properties.setValue("Reflection Color", getColor(material, ColorAttribute.Reflection));
            properties.setValue("Shininess", getFloat(material, FloatAttribute.Shininess));
            properties.setValue("Alpha Test", getFloat(material, FloatAttribute.AlphaTest));
            properties.setValue("Diffuse Texture", getTexture(material, TextureAttribute.Diffuse));
            properties.setValue("Ambient Texture", getTexture(material, TextureAttribute.Ambient));
            properties.setValue("Emissive Texture", getTexture(material, TextureAttribute.Emissive));
            properties.setValue("Normal Texture", getTexture(material, TextureAttribute.Normal));
            properties.setValue("Bump Texture", getTexture(material, TextureAttribute.Bump));
            properties.setValue("Reflection Texture", getTexture(material, TextureAttribute.Reflection));
            properties.setValue("Specular Texture", getTexture(material, TextureAttribute.Specular));
            materialProperties.put(material, properties);
        }
    }

    public void setCullingTest(CullingTest cullingTest) {
        this.cullingTest = cullingTest;
    }

    private Color getColor(Material material, long type) {
        ColorAttribute attribute = (ColorAttribute) material.get(type);
        if (attribute != null)
            return attribute.color;
        return null;
    }

    private Float getFloat(Material material, long type) {
        FloatAttribute attribute = (FloatAttribute) material.get(type);
        if (attribute != null)
            return attribute.value;
        return null;
    }

    private TextureRegion getTexture(Material material, long type) {
        TextureAttribute attribute = (TextureAttribute) material.get(type);
        if (attribute != null)
            return new TextureRegion(
                    attribute.textureDescription.texture,
                    attribute.offsetU, attribute.offsetV, attribute.scaleU + attribute.offsetU, attribute.scaleV + attribute.offsetV);
        return null;
    }

    public boolean hasTag(String tag) {
        return graphModelsByTag.containsKey(tag);
    }

    public void addTag(String tag) {
        if (graphModelsByTag.containsKey(tag))
            throw new IllegalArgumentException("This model instance is already registered for this tag");

        ObjectSet<GraphModel> models = new ObjectSet<>();

        for (Node node : modelInstance.nodes) {
            registerNodeForTag(models, node, tag);
        }


        graphModelsByTag.put(tag, models);
    }

    private void registerNodeForTag(ObjectSet<GraphModel> models, Node node, String tag) {
        if (node.parts.size > 0) {
            for (NodePart nodePart : node.parts) {
                NodePartRenderableModel renderableModel = new NodePartRenderableModel(node, nodePart);
                GraphModel graphModel = graphModels.addModel(tag, renderableModel);
                models.add(graphModel);
            }
        }

        for (Node child : node.getChildren()) {
            registerNodeForTag(models, child, tag);
        }
    }

    public void removeTag(String tag) {
        if (!hasTag(tag))
            throw new IllegalArgumentException("This model instance does not have this tag");

        ObjectSet<GraphModel> models = graphModelsByTag.get(tag);
        for (GraphModel graphModel : models) {
            graphModels.removeModel(graphModel);
        }

        graphModelsByTag.remove(tag);
    }

    public void setProperty(String name, Object value) {
        for (MapWritablePropertyContainer container : materialProperties.values()) {
            container.setValue(name, value);
        }
    }

    public void resetProperty(String name) {
        for (MapWritablePropertyContainer container : materialProperties.values()) {
            container.remove(name);
        }
    }

    private class NodePartRenderableModel implements RenderableModel {
        private Node node;
        private NodePart nodePart;
        private Vector3 tmpVector = new Vector3();
        private Matrix4 worldTransform = new Matrix4();
        private int[] attributeLocations;

        public NodePartRenderableModel(Node node, NodePart nodePart) {
            this.node = node;
            this.nodePart = nodePart;
        }

        @Override
        public PropertyContainer getPropertyContainer(String tag) {
            return materialProperties.get(nodePart.material);
        }

        @Override
        public Vector3 getPosition() {
            return getWorldTransform(null).getTranslation(tmpVector);
        }

        @Override
        public Matrix4 getWorldTransform(String tag) {
            if (nodePart.bones == null && modelInstance.transform != null)
                worldTransform.set(modelInstance.transform).mul(node.globalTransform);
            else if (modelInstance.transform != null)
                worldTransform.set(modelInstance.transform);
            else
                worldTransform.idt();
            return worldTransform;
        }

        @Override
        public Matrix4[] getBones(String tag) {
            return nodePart.bones;
        }

        @Override
        public boolean isRendered(Camera camera) {
            return nodePart.enabled && (cullingTest == null || !cullingTest.isCulled(camera, getPosition()));
        }

        @Override
        public void render(Camera camera, ModelGraphShader shader) {
            ShaderProgram shaderProgram = shader.getShaderProgram();
            int[] attributeLocations = getAttributeLocations(shader, nodePart.meshPart.mesh);
            nodePart.meshPart.mesh.bind(shaderProgram, attributeLocations);
            nodePart.meshPart.render(shaderProgram);
            nodePart.meshPart.mesh.unbind(shaderProgram, attributeLocations);
        }

        private int[] getAttributeLocations(ModelGraphShader graphShader, Mesh mesh) {
            if (attributeLocations == null) {
                VertexAttributes attributes = mesh.getVertexAttributes();
                IntArray result = new IntArray();
                for (int i = 0; i < attributes.size(); i++) {
                    final VertexAttribute attribute = attributes.get(i);
                    String attributeName = getAttributeName(attribute.alias);
                    PropertySource propertySource = graphShader.getProperties().get(attributeName);
                    if (propertySource != null) {
                        int propertyIndex = propertySource.getPropertyIndex();
                        final int location = graphShader.getShaderProgram().getAttributeLocation("a_property_" + propertyIndex);
                        result.add(location);
                    } else {
                        result.add(-1);
                    }
                }
                attributeLocations = result.shrink();
            }
            return attributeLocations;
        }

        private String getAttributeName(String alias) {
            switch (alias) {
                case ShaderProgram.POSITION_ATTRIBUTE:
                    return "Position";
                case ShaderProgram.NORMAL_ATTRIBUTE:
                    return "Normal";
                case ShaderProgram.BINORMAL_ATTRIBUTE:
                    return "BiNormal";
                case ShaderProgram.TANGENT_ATTRIBUTE:
                    return "Tangent";
                case ShaderProgram.COLOR_ATTRIBUTE:
                    return "Color";
                case ShaderProgram.TEXCOORD_ATTRIBUTE + "0":
                    return "UV";
            }
            return "";
        }
    }
}
