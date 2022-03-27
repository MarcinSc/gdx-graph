package com.gempukku.libgdx.graph.ui.pipeline.producer.shader.registry;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;
import com.kotcrab.vis.ui.widget.file.FileChooser;
import com.kotcrab.vis.ui.widget.file.FileChooserAdapter;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class LoadFileGraphShaderTemplate implements GraphShaderTemplate {
    private String title;

    public LoadFileGraphShaderTemplate(String title) {
        this.title = title;
    }

    @Override
    public String getTitle() {
        return title;
    }

    @Override
    public void invokeTemplate(Stage stage, final Callback callback) {
        FileChooser fileChooser = new FileChooser(FileChooser.Mode.OPEN);
        fileChooser.setModal(true);
        fileChooser.setSelectionMode(FileChooser.SelectionMode.FILES);
        fileChooser.setListener(new FileChooserAdapter() {
            @Override
            public void selected(Array<FileHandle> file) {
                FileHandle selectedFile = file.get(0);
                JsonReader parser = new JsonReader();
                try {
                    InputStream is = selectedFile.read();
                    try {
                        JsonValue shader = parser.parse(new InputStreamReader(is));
                        callback.addShader(selectedFile.nameWithoutExtension(), shader);
                    } finally {
                        is.close();
                    }
                } catch (IOException exp) {
                    // Ignored
                }
            }
        });
        stage.addActor(fileChooser.fadeIn());
    }
}
