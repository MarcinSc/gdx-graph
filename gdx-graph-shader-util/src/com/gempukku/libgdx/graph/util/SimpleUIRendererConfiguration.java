package com.gempukku.libgdx.graph.util;

import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.ObjectMap;
import com.gempukku.libgdx.graph.render.ui.UIRendererConfiguration;

public class SimpleUIRendererConfiguration implements UIRendererConfiguration {
    private ObjectMap<String, Stage> stageMap = new ObjectMap<>();

    public void setStage(String stageId, Stage stage) {
        stageMap.put(stageId, stage);
    }

    @Override
    public Stage getStage(String stageId) {
        return stageMap.get(stageId);
    }

    public ObjectMap<String, Stage> getStageMap() {
        return stageMap;
    }

    public Stage removeStage(String stageId) {
        return stageMap.remove(stageId);
    }

    public void removeAllStages() {
        stageMap.clear();
    }
}
