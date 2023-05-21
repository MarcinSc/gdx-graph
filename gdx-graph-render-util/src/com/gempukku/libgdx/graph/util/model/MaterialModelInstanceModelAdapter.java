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
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.IntArray;
import com.badlogic.gdx.utils.ObjectMap;
import com.badlogic.gdx.utils.ObjectSet;
import com.gempukku.libgdx.common.IntMapping;
import com.gempukku.libgdx.graph.data.MapWritablePropertyContainer;
import com.gempukku.libgdx.graph.data.PropertyContainer;
import com.gempukku.libgdx.graph.shader.GraphModels;
import com.gempukku.libgdx.graph.shader.GraphShader;
import com.gempukku.libgdx.graph.shader.RenderableModel;
import com.gempukku.libgdx.graph.shader.ShaderContext;
import com.gempukku.libgdx.graph.util.culling.CullingTest;

public class MaterialModelInstanceModelAdapter {
    private ModelInstance modelInstance;
    private GraphModels graphModels;

    private final ObjectSet<String> tags = new ObjectSet<>();
    private final Array<RenderableModel> registeredModels = new Array<>();

    private ObjectMap<Material, MapWritablePropertyContainer> materialProperties;
    private CullingTest cullingTest;
    private ObjectMap<IntMapping<String>, int[]> locationMappingCache = new ObjectMap<>();

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
        return tags.contains(tag);
    }

    public void addTag(String tag) {
        if (tags.contains(tag))
            throw new IllegalArgumentException("This model instance is already registered for this tag");

        if (tags.isEmpty()) {
            for (Node node : modelInstance.nodes) {
                registerNode(node);
            }
        }

        tags.add(tag);
    }

    private void registerNode(Node node) {
        if (node.parts.size > 0) {
            for (NodePart nodePart : node.parts) {
                NodePartRenderableModel renderableModel = new NodePartRenderableModel(node, nodePart);
                graphModels.addModel(renderableModel);
                registeredModels.add(renderableModel);
            }
        }

        for (Node child : node.getChildren()) {
            registerNode(child);
        }
    }

    public void removeTag(String tag) {
        if (!hasTag(tag))
            throw new IllegalArgumentException("This model instance does not have this tag");

        tags.remove(tag);

        if (tags.isEmpty()) {
            for (RenderableModel registeredModel : registeredModels) {
                graphModels.removeModel(registeredModel);
            }
            registeredModels.clear();
        }
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

    private class NodePartRenderableModel implements RenderableModel, PropertyContainer {
        private Node node;
        private NodePart nodePart;
        private Vector3 tmpVector = new Vector3();
        private Matrix4 worldTransform = new Matrix4();

        public NodePartRenderableModel(Node node, NodePart nodePart) {
            this.node = node;
            this.nodePart = nodePart;
        }

        @Override
        public PropertyContainer getPropertyContainer() {
            return this;
        }

        @Override
        public Object getValue(String name) {
            if (name.equals("Bone Transforms"))
                return nodePart.bones;
            return materialProperties.get(nodePart.material).getValue(name);
        }

        @Override
        public Vector3 getPosition() {
            return getWorldTransform().getTranslation(tmpVector);
        }

        @Override
        public Matrix4 getWorldTransform() {
            if (nodePart.bones == null && modelInstance.transform != null)
                worldTransform.set(modelInstance.transform).mul(node.globalTransform);
            else if (modelInstance.transform != null)
                worldTransform.set(modelInstance.transform);
            else
                worldTransform.idt();
            return worldTransform;
        }

        @Override
        public boolean isRendered(GraphShader graphShader, Camera camera) {
            return tags.contains(graphShader.getTag()) && nodePart.enabled && (cullingTest == null || !cullingTest.isCulled(camera, getPosition()));
        }

        @Override
        public void prepareToRender(ShaderContext shaderContext) {

        }

        @Override
        public void render(Camera camera, ShaderProgram shaderProgram, IntMapping<String> propertyToLocationMapping) {
            int[] attributeLocations = getAttributeLocations(nodePart.meshPart.mesh, propertyToLocationMapping);
            nodePart.meshPart.mesh.bind(shaderProgram, attributeLocations);
            nodePart.meshPart.render(shaderProgram);
            nodePart.meshPart.mesh.unbind(shaderProgram, attributeLocations);
        }

        private int[] getAttributeLocations(Mesh mesh, IntMapping<String> propertyLocationMapping) {
            int[] attributeLocations = locationMappingCache.get(propertyLocationMapping);
            if (attributeLocations == null) {
                VertexAttributes attributes = mesh.getVertexAttributes();
                IntArray result = new IntArray();
                for (int i = 0; i < attributes.size(); i++) {
                    final VertexAttribute vertexAttribute = attributes.get(i);
                    result.add(propertyLocationMapping.map(getAttributeName(vertexAttribute.alias)));
                }
                attributeLocations = result.shrink();
                locationMappingCache.put(propertyLocationMapping, attributeLocations);
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
            if (alias.startsWith(ShaderProgram.BONEWEIGHT_ATTRIBUTE))
                return "Bone Weights_" + alias.substring(ShaderProgram.BONEWEIGHT_ATTRIBUTE.length());
            return "";
        }
    }
}
