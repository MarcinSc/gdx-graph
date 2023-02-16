package com.gempukku.libgdx.graph.util.sprite.storage;

import com.badlogic.gdx.*;
import com.badlogic.gdx.utils.Clipboard;
import com.gempukku.libgdx.graph.util.sprite.SpriteReference;
import com.gempukku.libgdx.graph.util.storage.MeshSerializer;
import com.gempukku.libgdx.graph.util.storage.MeshUpdater;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import static org.junit.Assert.assertEquals;

public class SpriteSlotMemoryMeshTest {
    private TwoFloatSerializer serializer = new TwoFloatSerializer();

    @Before
    public void initialize() {
        Gdx.app = new Application() {
            @Override
            public ApplicationListener getApplicationListener() {
                return null;
            }

            @Override
            public Graphics getGraphics() {
                return null;
            }

            @Override
            public Audio getAudio() {
                return null;
            }

            @Override
            public Input getInput() {
                return null;
            }

            @Override
            public Files getFiles() {
                return null;
            }

            @Override
            public Net getNet() {
                return null;
            }

            @Override
            public void log(String tag, String message) {

            }

            @Override
            public void log(String tag, String message, Throwable exception) {

            }

            @Override
            public void error(String tag, String message) {

            }

            @Override
            public void error(String tag, String message, Throwable exception) {

            }

            @Override
            public void debug(String tag, String message) {

            }

            @Override
            public void debug(String tag, String message, Throwable exception) {

            }

            @Override
            public void setLogLevel(int logLevel) {

            }

            @Override
            public int getLogLevel() {
                return Application.LOG_DEBUG;
            }

            @Override
            public void setApplicationLogger(ApplicationLogger applicationLogger) {

            }

            @Override
            public ApplicationLogger getApplicationLogger() {
                return null;
            }

            @Override
            public ApplicationType getType() {
                return null;
            }

            @Override
            public int getVersion() {
                return 0;
            }

            @Override
            public long getJavaHeap() {
                return 0;
            }

            @Override
            public long getNativeHeap() {
                return 0;
            }

            @Override
            public Preferences getPreferences(String name) {
                return null;
            }

            @Override
            public Clipboard getClipboard() {
                return null;
            }

            @Override
            public void postRunnable(Runnable runnable) {

            }

            @Override
            public void exit() {

            }

            @Override
            public void addLifecycleListener(LifecycleListener listener) {

            }

            @Override
            public void removeLifecycleListener(LifecycleListener listener) {

            }
        };
    }

    @Test
    public void testEmpty() {
        SpriteSlotMemoryMesh<TwoFloats> memoryMesh = new SpriteSlotMemoryMesh<>(10, new OneVertexSpriteModel(), serializer);
        assertEquals(10, memoryMesh.getMaxVertexCount());
        assertEquals(0, memoryMesh.getSpriteCount());

        MeshUpdater meshUpdater = Mockito.mock(MeshUpdater.class);
        memoryMesh.updateGdxMesh(meshUpdater);
        Mockito.verifyNoMoreInteractions(meshUpdater);
    }

    @Test
    public void testAddUnit() {
        SpriteSlotMemoryMesh<TwoFloats> memoryMesh = new SpriteSlotMemoryMesh<>(10, new OneVertexSpriteModel(), serializer);
        memoryMesh.addPart(new TwoFloats(1, 2));
        assertEquals(1, memoryMesh.getSpriteCount());

        ArgumentCaptor<float[]> valuesArray = ArgumentCaptor.forClass(float[].class);
        ArgumentCaptor<Integer> start = ArgumentCaptor.forClass(Integer.class);
        ArgumentCaptor<Integer> count = ArgumentCaptor.forClass(Integer.class);

        MeshUpdater meshUpdater = Mockito.mock(MeshUpdater.class);
        memoryMesh.updateGdxMesh(meshUpdater);
        Mockito.verify(meshUpdater).updateMeshValues(valuesArray.capture(), start.capture(), count.capture());

        float[] value = valuesArray.getValue();
        assertEquals(1, value[0], 0.001f);
        assertEquals(2, value[1], 0.001f);
        assertEquals(0, start.getValue().intValue());
        assertEquals(2, count.getValue().intValue());
    }

    @Test
    public void testAddTwoRemoveFirst() {
        SpriteSlotMemoryMesh<TwoFloats> memoryMesh = new SpriteSlotMemoryMesh<>(10, new OneVertexSpriteModel(), serializer);
        SpriteReference object1 = memoryMesh.addPart(new TwoFloats(1, 2));
        SpriteReference object2 = memoryMesh.addPart(new TwoFloats(3, 4));
        memoryMesh.removePart(object1);
        assertEquals(1, memoryMesh.getSpriteCount());

        ArgumentCaptor<float[]> valuesArray = ArgumentCaptor.forClass(float[].class);
        ArgumentCaptor<Integer> start = ArgumentCaptor.forClass(Integer.class);
        ArgumentCaptor<Integer> count = ArgumentCaptor.forClass(Integer.class);

        MeshUpdater meshUpdater = Mockito.mock(MeshUpdater.class);
        memoryMesh.updateGdxMesh(meshUpdater);
        Mockito.verify(meshUpdater).updateMeshValues(valuesArray.capture(), start.capture(), count.capture());

        float[] value = valuesArray.getValue();
        assertEquals(3, value[0], 0.001f);
        assertEquals(4, value[1], 0.001f);
        assertEquals(0, start.getValue().intValue());
        assertEquals(2, count.getValue().intValue());
    }

    @Test
    public void testAddTwoRemoveSecond() {
        SpriteSlotMemoryMesh<TwoFloats> memoryMesh = new SpriteSlotMemoryMesh<>(10, new OneVertexSpriteModel(), serializer);
        SpriteReference object1 = memoryMesh.addPart(new TwoFloats(1, 2));
        SpriteReference object2 = memoryMesh.addPart(new TwoFloats(3, 4));
        memoryMesh.removePart(object2);
        assertEquals(1, memoryMesh.getSpriteCount());

        ArgumentCaptor<float[]> valuesArray = ArgumentCaptor.forClass(float[].class);
        ArgumentCaptor<Integer> start = ArgumentCaptor.forClass(Integer.class);
        ArgumentCaptor<Integer> count = ArgumentCaptor.forClass(Integer.class);

        MeshUpdater meshUpdater = Mockito.mock(MeshUpdater.class);
        memoryMesh.updateGdxMesh(meshUpdater);
        Mockito.verify(meshUpdater).updateMeshValues(valuesArray.capture(), start.capture(), count.capture());

        float[] value = valuesArray.getValue();
        assertEquals(1, value[0], 0.001f);
        assertEquals(2, value[1], 0.001f);
        assertEquals(0, start.getValue().intValue());
        assertEquals(2, count.getValue().intValue());
    }

    private static class TwoFloats {
        private float float1;
        private float float2;

        public TwoFloats(float float1, float float2) {
            this.float1 = float1;
            this.float2 = float2;
        }

        public float getFloat1() {
            return float1;
        }

        public float getFloat2() {
            return float2;
        }
    }

    private static class TwoFloatSerializer implements MeshSerializer<TwoFloats> {
        @Override
        public int getFloatsPerVertex() {
            return 2;
        }

        @Override
        public int getIndexCount(TwoFloats object) {
            return 6;
        }

        @Override
        public int getVertexCount(TwoFloats object) {
            return 4;
        }

        @Override
        public void serializeIndices(TwoFloats object, short[] indices, int indexStart, int vertexStart) {

        }

        @Override
        public void serializeVertices(TwoFloats object, float[] vertexValues, int vertexStart) {
            vertexValues[vertexStart] = object.getFloat1();
            vertexValues[vertexStart + 1] = object.getFloat2();
        }
    }
}