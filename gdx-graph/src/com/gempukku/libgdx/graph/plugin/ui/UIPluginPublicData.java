package com.gempukku.libgdx.graph.plugin.ui;

import com.badlogic.gdx.scenes.scene2d.Stage;

public interface UIPluginPublicData {
    void setStage(String id, Stage stage);

    Stage getStage(String id);
}
