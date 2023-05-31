package com.gempukku.libgdx.graph.artemis.renderer;

import com.artemis.Component;
import com.badlogic.gdx.utils.Array;

public class PipelineRendererComponent extends Component {
    private String pipelineName = "";
    private String pipelinePath;
    private boolean renderingPipeline = true;
    private Array<CameraDefinition> cameraDefinitions = new Array<>();

    public String getPipelineName() {
        return pipelineName;
    }

    public String getPipelinePath() {
        return pipelinePath;
    }

    public boolean isRenderingPipeline() {
        return renderingPipeline;
    }

    public Array<CameraDefinition> getCameraDefinitions() {
        return cameraDefinitions;
    }
}
