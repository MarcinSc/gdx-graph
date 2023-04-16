package com.gempukku.libgdx.graph.loader;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;
import com.gempukku.libgdx.graph.data.GraphWithProperties;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class GraphLoader {
    public static GraphWithProperties loadGraph(String graphType, FileHandle fileHandle) throws IOException {
        try (InputStream is = fileHandle.read()) {
            return loadGraph(graphType, is);
        }
    }

    public static GraphWithProperties loadGraph(String graphType, InputStream inputStream) throws IOException {
        try {
            JsonReader parser = new JsonReader();
            JsonValue graph = parser.parse(new InputStreamReader(inputStream));
            return loadGraph(graphType, graph);
        } catch (Exception exp) {
            throw new IOException("Unable to parse graph", exp);
        }
    }

    public static GraphWithProperties loadGraph(String graphType, JsonValue graph) {
        return GraphSerializer.deserializeGraphWithProperties(graph, graphType);
    }
}
