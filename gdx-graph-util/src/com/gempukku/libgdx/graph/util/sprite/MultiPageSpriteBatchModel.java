package com.gempukku.libgdx.graph.util.sprite;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.ObjectMap;
import com.badlogic.gdx.utils.ObjectSet;
import com.gempukku.libgdx.graph.pipeline.producer.rendering.producer.WritablePropertyContainer;
import com.gempukku.libgdx.graph.plugin.models.GraphModel;
import com.gempukku.libgdx.graph.plugin.models.GraphModels;
import com.gempukku.libgdx.graph.plugin.models.RenderableModel;
import com.gempukku.libgdx.graph.shader.ShaderContext;
import com.gempukku.libgdx.graph.shader.field.ShaderFieldType;
import com.gempukku.libgdx.graph.shader.property.MapWritablePropertyContainer;
import com.gempukku.libgdx.graph.shader.property.PropertySource;
import com.gempukku.libgdx.graph.util.IntMapping;
import com.gempukku.libgdx.graph.util.ValuePerVertex;
import com.gempukku.libgdx.graph.util.culling.CullingTest;
import com.gempukku.libgdx.graph.util.model.GraphModelUtil;

public class MultiPageSpriteBatchModel implements Disposable {
    private final boolean staticBatch;
    private final int pageSpriteCapacity;
    private final GraphModels graphModels;
    private final String tag;
    private final WritablePropertyContainer propertyContainer;

    private final VertexAttributes vertexAttributes;
    private final ObjectMap<VertexAttribute, PropertySource> vertexPropertySources;
    private int[] attributeLocations;
    private final int floatCountPerVertex;

    private Array<SpriteBatchModelPage> pages = new Array<>();
    private ObjectMap<SpriteBatchModelPage, GraphModel> modelMap = new ObjectMap<>();

    private final Vector3 position = new Vector3();
    private final Matrix4 worldTransform = new Matrix4();
    private CullingTest cullingTest;

    public MultiPageSpriteBatchModel(boolean staticBatch, int pageSpriteCapacity, GraphModels graphModels, String tag) {
        this(staticBatch, pageSpriteCapacity, graphModels, tag, new MapWritablePropertyContainer());
    }

    public MultiPageSpriteBatchModel(boolean staticBatch, int pageSpriteCapacity, GraphModels graphModels, String tag,
                                     WritablePropertyContainer propertyContainer) {
        this.staticBatch = staticBatch;
        this.pageSpriteCapacity = pageSpriteCapacity;
        this.graphModels = graphModels;
        this.tag = tag;
        this.propertyContainer = propertyContainer;

        vertexAttributes = GraphModelUtil.getShaderVertexAttributes(graphModels, tag);
        vertexPropertySources = GraphModelUtil.getPropertySourceMap(graphModels, tag, vertexAttributes);

        floatCountPerVertex = vertexAttributes.vertexSize / 4;
    }

    public String getTag() {
        return tag;
    }

    public Vector3 getPosition() {
        return position;
    }

    public Matrix4 getWorldTransform() {
        return worldTransform;
    }

    public void setCullingTest(CullingTest cullingTest) {
        this.cullingTest = cullingTest;
    }

    public boolean hasSprite(RenderableSprite sprite) {
        for (SpriteBatchModelPage page : pages) {
            if (page.hasSprite(sprite))
                return true;
        }

        return false;
    }

    public void addSprite(RenderableSprite sprite) {
        for (SpriteBatchModelPage page : pages) {
            if (page.addSprite(sprite))
                return;
        }

        SpriteBatchModelPage newPage = new SpriteBatchModelPage();
        newPage.addSprite(sprite);
        pages.add(newPage);

        GraphModel model = graphModels.addModel(tag, newPage);
        modelMap.put(newPage, model);
    }

    public void updateSprite(RenderableSprite sprite) {
        for (SpriteBatchModelPage page : pages) {
            if (page.hasSprite(sprite)) {
                page.updateSprite(sprite);
                return;
            }
        }
    }

    public boolean removeSprite(RenderableSprite sprite) {
        for (SpriteBatchModelPage page : pages) {
            if (page.removeSprite(sprite))
                return true;
        }
        return false;
    }

    public void disposeEmptyPages() {
        Array.ArrayIterator<SpriteBatchModelPage> iterator = pages.iterator();
        while (iterator.hasNext()) {
            SpriteBatchModelPage page = iterator.next();
            if (page.getSpriteCount() == 0) {
                disposePage(page);
                iterator.remove();
            }
        }
    }

    public WritablePropertyContainer getPropertyContainer() {
        return propertyContainer;
    }

    @Override
    public void dispose() {
        for (SpriteBatchModelPage page : pages) {
            disposePage(page);
        }
        pages.clear();
    }

    private void disposePage(SpriteBatchModelPage page) {
        page.dispose();
        graphModels.removeModel(modelMap.remove(page));
    }

    private class SpriteBatchModelPage implements RenderableModel, Disposable {
        private final float[] vertexData;
        private final Mesh mesh;

        private final RenderableSprite[] spritePosition;

        private int spriteCount = 0;
        private int minUpdatedIndex = Integer.MAX_VALUE;
        private int maxUpdatedIndex = -1;

        private final ObjectSet<RenderableSprite> updatedSprites = new ObjectSet<>();
        private final ObjectSet<RenderableSprite> allSprites = new ObjectSet<>();

        public SpriteBatchModelPage() {
            spritePosition = new RenderableSprite[pageSpriteCapacity];

            vertexData = new float[4 * floatCountPerVertex * pageSpriteCapacity];

            mesh = new Mesh(staticBatch, true, 4 * pageSpriteCapacity, 6 * pageSpriteCapacity, vertexAttributes);
            mesh.setVertices(vertexData);

            short[] indices = SpriteUtil.createSpriteIndicesArray(pageSpriteCapacity);
            mesh.setIndices(indices, 0, indices.length);
        }

        public boolean hasSprite(RenderableSprite sprite) {
            return allSprites.contains(sprite);
        }

        public boolean addSprite(RenderableSprite sprite) {
            if (spriteCount == pageSpriteCapacity)
                return false;

            spritePosition[spriteCount] = sprite;

            updatedSprites.add(sprite);
            allSprites.add(sprite);

            spriteCount++;

            return true;
        }

        public boolean removeSprite(RenderableSprite sprite) {
            if (!hasSprite(sprite))
                return false;

            int position = findSpriteIndex(sprite);

            updatedSprites.remove(sprite);
            allSprites.remove(sprite);

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

        public void updateSprite(RenderableSprite sprite) {
            if (hasSprite(sprite))
                updatedSprites.add(sprite);
        }

        public int getSpriteCount() {
            return spriteCount;
        }

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
            return propertyContainer;
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
                attributeLocations = GraphModelUtil.getAttributeLocations(shaderProgram, vertexAttributes);
            mesh.bind(shaderProgram, attributeLocations);
            Gdx.gl20.glDrawElements(Gdx.gl20.GL_TRIANGLES, 6 * spriteCount, GL20.GL_UNSIGNED_SHORT, 0);
            mesh.unbind(shaderProgram, attributeLocations);
        }

        @Override
        public void dispose() {
            mesh.dispose();
        }

        private void updateSpriteData(RenderableSprite sprite, int spriteIndex) {
            int spriteDataStart = getSpriteDataStart(spriteIndex);
            for (VertexAttribute vertexAttribute : vertexAttributes) {
                PropertySource propertySource = vertexPropertySources.get(vertexAttribute);

                int attributeOffset = vertexAttribute.offset / 4;
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

        private int getSpriteDataStart(int spriteIndex) {
            return spriteIndex * floatCountPerVertex * 4;
        }

        private void markSpriteUpdated(int spriteIndex) {
            minUpdatedIndex = Math.min(minUpdatedIndex, getSpriteDataStart(spriteIndex));
            maxUpdatedIndex = Math.max(maxUpdatedIndex, getSpriteDataStart(spriteIndex + 1));
        }

        private int findSpriteIndex(RenderableSprite sprite) {
            for (int i = 0; i < spriteCount; i++) {
                if (spritePosition[i] == sprite)
                    return i;
            }
            return -1;
        }

    }
}
