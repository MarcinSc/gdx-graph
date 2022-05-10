package com.gempukku.libgdx.graph.util.sprite;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.*;
import com.gempukku.libgdx.graph.pipeline.producer.rendering.producer.WritablePropertyContainer;
import com.gempukku.libgdx.graph.plugin.models.GraphModel;
import com.gempukku.libgdx.graph.plugin.models.GraphModels;
import com.gempukku.libgdx.graph.plugin.models.RenderableModel;
import com.gempukku.libgdx.graph.shader.ShaderContext;
import com.gempukku.libgdx.graph.shader.field.ShaderFieldType;
import com.gempukku.libgdx.graph.shader.property.MapWritablePropertyContainer;
import com.gempukku.libgdx.graph.shader.property.PropertyLocation;
import com.gempukku.libgdx.graph.shader.property.PropertySource;
import com.gempukku.libgdx.graph.util.IntMapping;
import com.gempukku.libgdx.graph.util.ValuePerVertex;
import com.gempukku.libgdx.graph.util.culling.CullingTest;

public class SpriteBatchModel implements Disposable {
    private final Matrix4 worldTransform = new Matrix4();
    private final Vector3 position = new Vector3();
    private final GraphModels graphModels;
    private final String tag;
    private final WritablePropertyContainer propertyContainer;
    private final GraphModel graphModel;
    private CullingTest cullingTest;

    private final Mesh mesh;
    private final int spriteCapacity;
    private final ObjectMap<String, PropertySource> shaderProperties;
    private final float[] vertexData;
    private final int floatCountPerVertex;
    private final IntMap<String> propertyIndexNames = new IntMap<>();
    private final VertexAttributes vertexAttributes;
    private int[] attributeLocations;

    private final RenderableSprite[] spritePosition;

    private int spriteCount = 0;
    private int minUpdatedIndex = Integer.MAX_VALUE;
    private int maxUpdatedIndex = -1;
    private final ObjectSet<RenderableSprite> updatedSprites = new ObjectSet<>();

    public SpriteBatchModel(boolean staticBatch, int spriteCapacity, GraphModels graphModels, String tag) {
        this(staticBatch, spriteCapacity, graphModels, tag, new MapWritablePropertyContainer());
    }

    public SpriteBatchModel(boolean staticBatch, int spriteCapacity, GraphModels graphModels, String tag,
                            WritablePropertyContainer propertyContainer) {
        this.spriteCapacity = spriteCapacity;
        this.graphModels = graphModels;
        this.tag = tag;
        this.shaderProperties = graphModels.getShaderProperties(tag);
        this.propertyContainer = propertyContainer;

        if (shaderProperties == null)
            throw new GdxRuntimeException("Unable to locate shader with tag: " + tag);

        Array<VertexAttribute> vertexAttributeArray = new Array<>(VertexAttribute.class);
        for (ObjectMap.Entry<String, PropertySource> shaderProperty : shaderProperties) {
            PropertyLocation propertyLocation = shaderProperty.value.getPropertyLocation();
            if (propertyLocation == PropertyLocation.Attribute) {
                ShaderFieldType shaderFieldType = shaderProperty.value.getShaderFieldType();
                vertexAttributeArray.add(new VertexAttribute(1024, shaderFieldType.getNumberOfComponents(), shaderProperty.value.getAttributeName()));
                propertyIndexNames.put(shaderProperty.value.getPropertyIndex(), shaderProperty.key);
            }
        }

        vertexAttributes = new VertexAttributes(vertexAttributeArray.toArray());

        floatCountPerVertex = vertexAttributes.vertexSize / 4;

        spritePosition = new RenderableSprite[spriteCapacity];
        vertexData = new float[4 * floatCountPerVertex * spriteCapacity];

        int numberOfIndices = 6 * spriteCapacity;
        mesh = new Mesh(staticBatch, true, 4 * spriteCapacity, numberOfIndices, vertexAttributes);
        mesh.setVertices(vertexData);
        short[] indices = new short[numberOfIndices];
        int vertexIndex = 0;
        for (int i = 0; i < numberOfIndices; i += 6) {
            indices[i + 0] = (short) (vertexIndex * 4 + 0);
            indices[i + 1] = (short) (vertexIndex * 4 + 2);
            indices[i + 2] = (short) (vertexIndex * 4 + 1);
            indices[i + 3] = (short) (vertexIndex * 4 + 2);
            indices[i + 4] = (short) (vertexIndex * 4 + 3);
            indices[i + 5] = (short) (vertexIndex * 4 + 1);
            vertexIndex++;
        }
        mesh.setIndices(indices, 0, indices.length);

        graphModel = graphModels.addModel(tag, new BatchRenderableModel());
    }

    public String getTag() {
        return tag;
    }

    public void setCullingTest(CullingTest cullingTest) {
        this.cullingTest = cullingTest;
    }

    public boolean hasSprite(RenderableSprite sprite) {
        return findSpriteIndex(sprite) != -1;
    }

    public boolean addSprite(RenderableSprite sprite) {
        if (spriteCount == spriteCapacity)
            return false;

        spritePosition[spriteCount] = sprite;

        updatedSprites.add(sprite);

        spriteCount++;

        return true;
    }

    public void updateSprite(RenderableSprite sprite) {
        updatedSprites.add(sprite);
    }

    public boolean removeSprite(RenderableSprite sprite) {
        int position = findSpriteIndex(sprite);
        if (position == -1)
            return false;

        updatedSprites.remove(sprite);

        if (spriteCount > 1 && position != spriteCount - 1) {
            // Need to shrink the arrays
            spritePosition[position] = spritePosition[spriteCount - 1];
            int sourcePosition = getSpriteDataStart(spriteCount - 1);
            int destinationPosition = getSpriteDataStart(position);
            int floatCount = floatCountPerVertex * 4;
            System.arraycopy(vertexData, sourcePosition, vertexData, destinationPosition, floatCount);

            markSpriteUpdated(position);
        }
        spriteCount--;

        return true;
    }

    public WritablePropertyContainer getPropertyContainer() {
        return propertyContainer;
    }

    private void markSpriteUpdated(int spriteIndex) {
        minUpdatedIndex = Math.min(minUpdatedIndex, getSpriteDataStart(spriteIndex));
        maxUpdatedIndex = Math.max(maxUpdatedIndex, getSpriteDataStart(spriteIndex + 1));
    }

    private int getSpriteDataStart(int spriteIndex) {
        return spriteIndex * floatCountPerVertex * 4;
    }

    private int findSpriteIndex(RenderableSprite sprite) {
        for (int i = 0; i < spriteCount; i++) {
            if (spritePosition[i] == sprite)
                return i;
        }
        return -1;
    }

    private void updateSpriteData(RenderableSprite sprite, int spriteIndex) {
        int spriteDataStart = getSpriteDataStart(spriteIndex);
        for (VertexAttribute vertexAttribute : vertexAttributes) {
            String alias = vertexAttribute.alias;
            int attributeOffset = vertexAttribute.offset / 4;
            PropertySource propertySource = findPropertyByAttributeName(alias);

            ShaderFieldType shaderFieldType = propertySource.getShaderFieldType();
            Object attributeValue = sprite.getPropertyContainer().getValue(propertySource.getPropertyName());
            if (attributeValue instanceof ValuePerVertex) {
                for (int vertexIndex = 0; vertexIndex < 4; vertexIndex++) {
                    int vertexOffset = spriteDataStart + vertexIndex * floatCountPerVertex;

                    Object vertexValue = ((ValuePerVertex) attributeValue).getValue(vertexIndex);
                    shaderFieldType.setValueInAttributesArray(vertexData, vertexOffset + attributeOffset, propertySource.getValueToUse(vertexValue));
                }
            } else {
                attributeValue = propertySource.getValueToUse(attributeValue);
                for (int vertexIndex = 0; vertexIndex < 4; vertexIndex++) {
                    int vertexOffset = spriteDataStart + vertexIndex * floatCountPerVertex;

                    shaderFieldType.setValueInAttributesArray(vertexData, vertexOffset + attributeOffset, attributeValue);
                }
            }
        }

        markSpriteUpdated(spriteIndex);
    }

    private PropertySource findPropertyByAttributeName(String attributeName) {
        for (PropertySource propertySource : shaderProperties.values()) {
            if (attributeName.equals(propertySource.getAttributeName()))
                return propertySource;
        }
        return null;
    }

    private void initializeAttributeLocations(ShaderProgram shaderProgram) {
        IntArray resultArray = new IntArray();
        for (VertexAttribute vertexAttribute : vertexAttributes) {
            resultArray.add(shaderProgram.getAttributeLocation(vertexAttribute.alias));
        }
        attributeLocations = resultArray.toArray();
    }

    @Override
    public void dispose() {
        graphModels.removeModel(graphModel);
        mesh.dispose();
    }

    private class BatchRenderableModel implements RenderableModel {
        @Override
        public Vector3 getPosition() {
            return position;
        }

        @Override
        public boolean isRendered(Camera camera) {
            return spriteCount > 0 && !isCulled(camera);
        }

        private boolean isCulled(Camera camera) {
            return cullingTest != null && cullingTest.isCulled(camera, getPosition());
        }

        @Override
        public Matrix4 getWorldTransform() {
            return worldTransform;
        }

        @Override
        public WritablePropertyContainer getPropertyContainer() {
            return SpriteBatchModel.this.getPropertyContainer();
        }

        @Override
        public void prepareToRender(ShaderContext shaderContext) {
            boolean debug = Gdx.app.getLogLevel() >= Gdx.app.LOG_DEBUG;
            if (debug && !updatedSprites.isEmpty())
                Gdx.app.debug("Sprite", "Updating info of " + updatedSprites.size + " sprite(s)");

            for (RenderableSprite updatedSprite : updatedSprites) {
                updateSpriteData(updatedSprite, findSpriteIndex(updatedSprite));
            }
            updatedSprites.clear();

            if (minUpdatedIndex != Integer.MAX_VALUE) {
                if (debug)
                    Gdx.app.debug("Sprite", "Updating vertex array - float count: " + (maxUpdatedIndex - minUpdatedIndex));
                mesh.updateVertices(minUpdatedIndex, vertexData, minUpdatedIndex, maxUpdatedIndex - minUpdatedIndex);
                minUpdatedIndex = Integer.MAX_VALUE;
                maxUpdatedIndex = -1;
            }
        }

        @Override
        public void render(Camera camera, ShaderProgram shaderProgram, IntMapping<String> propertyToLocationMapping) {
            if (Gdx.app.getLogLevel() >= Gdx.app.LOG_DEBUG)
                Gdx.app.debug("Sprite", "Rendering " + spriteCount + " sprite(s)");
            if (attributeLocations == null)
                initializeAttributeLocations(shaderProgram);
            mesh.bind(shaderProgram, attributeLocations);
            Gdx.gl20.glDrawElements(Gdx.gl20.GL_TRIANGLES, 6 * spriteCount, GL20.GL_UNSIGNED_SHORT, 0);
            mesh.unbind(shaderProgram, attributeLocations);
        }
    }
}
