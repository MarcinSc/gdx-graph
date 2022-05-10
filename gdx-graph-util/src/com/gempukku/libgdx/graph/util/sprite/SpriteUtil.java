package com.gempukku.libgdx.graph.util.sprite;

public class SpriteUtil {
    private SpriteUtil() {
    }

    public static short[] createSpriteIndicesArray(int spriteCount) {
        short[] indices = new short[spriteCount * 6];
        int vertexIndex = 0;
        for (int i = 0; i < indices.length; i += 6) {
            indices[i + 0] = (short) (vertexIndex * 4 + 0);
            indices[i + 1] = (short) (vertexIndex * 4 + 2);
            indices[i + 2] = (short) (vertexIndex * 4 + 1);
            indices[i + 3] = (short) (vertexIndex * 4 + 2);
            indices[i + 4] = (short) (vertexIndex * 4 + 3);
            indices[i + 5] = (short) (vertexIndex * 4 + 1);
            vertexIndex++;
        }
        return indices;
    }
}
