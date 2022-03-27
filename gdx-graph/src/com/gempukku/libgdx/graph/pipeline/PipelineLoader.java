package com.gempukku.libgdx.graph.pipeline;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.gempukku.libgdx.graph.loader.GraphLoader;
import com.gempukku.libgdx.graph.time.TimeProvider;

import java.io.IOException;
import java.io.InputStream;

public class PipelineLoader {
    public static PipelineRenderer loadPipelineRenderer(FileHandle pipelineFile, TimeProvider timeProvider) {
        try {
            return GraphLoader.loadGraph(pipelineFile, new PipelineLoaderCallback(timeProvider));
        } catch (IOException exp) {
            throw new GdxRuntimeException("Unable to load pipeline", exp);
        }
    }

    public static PipelineRenderer loadPipelineRenderer(InputStream pipelineInputStream, TimeProvider timeProvider) {
        try {
            return GraphLoader.loadGraph(pipelineInputStream, new PipelineLoaderCallback(timeProvider));
        } catch (IOException exp) {
            throw new GdxRuntimeException("Unable to load pipeline", exp);
        }
    }
}
