package com.gempukku.libgdx.graph.util.model;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.VertexAttribute;
import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
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
import com.gempukku.libgdx.graph.data.WritablePropertyContainer;
import com.gempukku.libgdx.graph.shader.GraphModels;
import com.gempukku.libgdx.graph.shader.GraphShader;
import com.gempukku.libgdx.graph.shader.RenderableModel;
import com.gempukku.libgdx.graph.shader.ShaderContext;
import com.gempukku.libgdx.graph.util.culling.CullingTest;

public class CommonPropertiesModelInstanceModelAdapter {
    private ModelInstance modelInstance;
    private GraphModels graphModels;

    private final ObjectSet<String> tags = new ObjectSet<>();
    private final Array<RenderableModel> registeredModels = new Array<>();

    private WritablePropertyContainer propertyContainer;
    private CullingTest cullingTest;
    private ObjectMap<IntMapping<String>, int[]> locationMappingCache = new ObjectMap<>();

    public CommonPropertiesModelInstanceModelAdapter(ModelInstance modelInstance, GraphModels graphModels) {
        this(modelInstance, graphModels, new MapWritablePropertyContainer());
    }

    public CommonPropertiesModelInstanceModelAdapter(ModelInstance modelInstance, GraphModels graphModels, WritablePropertyContainer propertyContainer) {
        this.modelInstance = modelInstance;
        this.graphModels = graphModels;
        this.propertyContainer = propertyContainer;
    }

    public void setCullingTest(CullingTest cullingTest) {
        this.cullingTest = cullingTest;
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

    public WritablePropertyContainer getPropertyContainer() {
        return propertyContainer;
    }

    private class NodePartRenderableModel implements RenderableModel {
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
            return propertyContainer;
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
            return "";
        }
    }
}
