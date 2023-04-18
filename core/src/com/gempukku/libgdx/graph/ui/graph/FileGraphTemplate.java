package com.gempukku.libgdx.graph.ui.graph;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;
import com.gempukku.libgdx.graph.GraphType;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class FileGraphTemplate implements GraphTemplate {
    private GraphType graphType;
    private String title;
    private FileHandle fileHandle;

    public FileGraphTemplate(GraphType graphType, String title, FileHandle fileHandle) {
        this.graphType = graphType;
        this.title = title;
        this.fileHandle = fileHandle;
    }

    @Override
    public String getTitle() {
        return title;
    }

    @Override
    public JsonValue getGraph() {
        JsonReader parser = new JsonReader();
        try {
            InputStream is = fileHandle.read();
            try {
                return parser.parse(new InputStreamReader(is));
            } finally {
                is.close();
            }
        } catch (IOException e) {
            throw new GdxRuntimeException("Unable to load graph template: " + title, e);
        }
    }
}
