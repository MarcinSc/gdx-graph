package com.gempukku.libgdx.graph.test;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

public class TestDesktopLauncher {
    public static void main(String[] arg) {
        LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
        config.width = 1440;
        config.height = 810;
        new LwjglApplication(new ReloadableGraphTestApplication(), config);
    }
}
