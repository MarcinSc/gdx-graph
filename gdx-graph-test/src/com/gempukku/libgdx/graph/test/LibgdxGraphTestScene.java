package com.gempukku.libgdx.graph.test;

public interface LibgdxGraphTestScene {
    String getName();

    void initializeScene();

    void renderScene();

    void resizeScene(int width, int height);

    void disposeScene();
}
