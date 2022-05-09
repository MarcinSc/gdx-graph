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
import com.badlogic.gdx.utils.IntArray;
import com.badlogic.gdx.utils.ObjectMap;
import com.badlogic.gdx.utils.ObjectSet;
import com.gempukku.libgdx.graph.pipeline.producer.rendering.producer.PropertyContainer;
import com.gempukku.libgdx.graph.pipeline.producer.rendering.producer.WritablePropertyContainer;
import com.gempukku.libgdx.graph.plugin.models.GraphModel;
import com.gempukku.libgdx.graph.plugin.models.GraphModels;
import com.gempukku.libgdx.graph.plugin.models.RenderableModel;
import com.gempukku.libgdx.graph.shader.ShaderContext;
import com.gempukku.libgdx.graph.shader.property.MapWritablePropertyContainer;
import com.gempukku.libgdx.graph.util.IntMapping;
import com.gempukku.libgdx.graph.util.culling.CullingTest;

public class CommonPropertiesModelInstanceModelAdapter {
    private ModelInstance modelInstance;
    private GraphModels graphModels;
    private ObjectMap<String, ObjectSet<GraphModel>> graphModelsByTag = new ObjectMap<>();
    private WritablePropertyContainer propertyContainer;
    private CullingTest cullingTest;

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

    public WritablePropertyContainer getPropertyContainer() {
        return propertyContainer;
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
            return propertyContainer;
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
        public boolean isRendered(Camera camera) {
            return nodePart.enabled && (cullingTest == null || !cullingTest.isCulled(camera, getPosition()));
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
            if (attributeLocations == null) {
                VertexAttributes attributes = mesh.getVertexAttributes();
                IntArray result = new IntArray();
                for (int i = 0; i < attributes.size(); i++) {
                    final VertexAttribute vertexAttribute = attributes.get(i);
                    result.add(propertyLocationMapping.map(getAttributeName(vertexAttribute.alias)));
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
