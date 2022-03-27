package com.gempukku.libgdx.graph.ui.pipeline.producer.shader.registry;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class FileGraphShaderTemplate implements GraphShaderTemplate {
    private String title;
    private FileHandle fileHandle;

    public FileGraphShaderTemplate(String title, FileHandle fileHandle) {
        this.title = title;
        this.fileHandle = fileHandle;
    }

    @Override
    public String getTitle() {
        return title;
    }

    @Override
    public void invokeTemplate(Stage stage, Callback callback) {
        JsonReader parser = new JsonReader();
        try {
            InputStream is = fileHandle.read();
            try {
                JsonValue shader = parser.parse(new InputStreamReader(is));
                callback.addShader("", shader);
            } finally {
                is.close();
            }
        } catch (IOException exp) {
            // Ignored
        }
    }
}
