package com.gempukku.libgdx.graph.ui.part;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.JsonValue;
import com.gempukku.libgdx.graph.ui.graph.GraphBoxInputConnector;
import com.gempukku.libgdx.graph.ui.graph.GraphBoxOutputConnector;
import com.gempukku.libgdx.graph.ui.graph.GraphBoxPart;
import com.gempukku.libgdx.graph.ui.graph.GraphChangedEvent;
import com.kotcrab.vis.ui.widget.VisLabel;
import com.kotcrab.vis.ui.widget.VisTable;
import com.kotcrab.vis.ui.widget.VisTextButton;
import com.kotcrab.vis.ui.widget.file.FileChooser;
import com.kotcrab.vis.ui.widget.file.FileChooserAdapter;

public class FileSelectorBoxPart extends VisTable implements GraphBoxPart {
    private String property;
    private String selectedPath;

    public FileSelectorBoxPart(String label, String property) {
        this(label, property, null);
    }

    public FileSelectorBoxPart(String label, String property, String defaultPath) {
        this.property = property;
        this.selectedPath = defaultPath;

        add(new VisLabel(label)).growX();
        VisTextButton chooseFileButton = new VisTextButton("Choose");
        add(chooseFileButton);
        row();

        chooseFileButton.addListener(
                new ChangeListener() {
                    @Override
                    public void changed(ChangeEvent event, Actor actor) {
                        final FileChooser fileChooser = new FileChooser(FileChooser.Mode.OPEN);
                        fileChooser.setModal(true);
                        fileChooser.setSelectionMode(FileChooser.SelectionMode.FILES);
                        fileChooser.setListener(new FileChooserAdapter() {
                            @Override
                            public void selected(Array<FileHandle> file) {
                                selectedPath = file.get(0).path();
                                fire(new GraphChangedEvent(false, true));
                            }
                        });
                        getStage().addActor(fileChooser.fadeIn());
                    }
                });
    }

    @Override
    public Actor getActor() {
        return this;
    }

    @Override
    public GraphBoxOutputConnector getOutputConnector() {
        return null;
    }

    @Override
    public GraphBoxInputConnector getInputConnector() {
        return null;
    }

    public void initialize(JsonValue data) {
        if (data != null) {
            selectedPath = data.getString(property);
        }
    }

    @Override
    public void serializePart(JsonValue object) {
        object.addChild(property, new JsonValue(selectedPath));
    }

    @Override
    public void dispose() {

    }
}
