package com.gempukku.libgdx.graph.artemis.renderer;

import com.artemis.Component;
import com.badlogic.gdx.utils.Array;

public class PipelineRendererComponent extends Component {
    private String pipelinePath;
    private Array<CameraDefinition> cameraDefinitions = new Array<>();

    public String getPipelinePath() {
        return pipelinePath;
    }

    public Array<CameraDefinition> getCameraDefinitions() {
        return cameraDefinitions;
    }
}
