package com.gempukku.libgdx.graph.pipeline.producer.node;

public class PipelineRequirements {
    private boolean requiringDepthTexture;

    public void setRequiringDepthTexture() {
        requiringDepthTexture = true;
    }

    public boolean isRequiringDepthTexture() {
        return requiringDepthTexture;
    }

    public void reset() {
        requiringDepthTexture = false;
    }
}
