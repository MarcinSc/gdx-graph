package com.gempukku.libgdx.graph.ui;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.VerticalGroup;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.gempukku.libgdx.graph.ui.plugin.PluginDefinition;
import com.gempukku.libgdx.graph.ui.plugin.PluginPreferences;
import com.gempukku.libgdx.graph.ui.plugin.PluginRegistry;
import com.kotcrab.vis.ui.util.dialog.Dialogs;
import com.kotcrab.vis.ui.widget.*;
import com.kotcrab.vis.ui.widget.file.FileChooser;
import com.kotcrab.vis.ui.widget.file.FileChooserAdapter;
import com.kotcrab.vis.ui.widget.file.FileTypeFilter;

public class PluginsDialog extends VisWindow {
    private Array<String> pluginJars = new Array<>();
    private final VerticalGroup pluginList;

    public PluginsDialog() {
        super("Plugins");
        setModal(true);
        setResizable(true);
        addCloseButton();
        align(Align.topLeft);
        setSize(640, 480);

        pluginList = new VerticalGroup();
        pluginList.top();
        pluginList.grow();
        pluginList.pad(5);

        VisTable listHeader = new VisTable();
        listHeader.pad(2);
        listHeader.add("JAR path").growX().left();
        listHeader.add("Plugin name").width(180).left();
        listHeader.add("Version").width(80).left();
        listHeader.add("Loaded").width(60);
        listHeader.add("Remove").width(60);
        listHeader.row();
        pluginList.addActor(listHeader);

        for (final PluginDefinition pluginDefinition : PluginRegistry.getPluginDefinitions()) {
            addPluginDefinition(pluginDefinition);
        }

        VisScrollPane scrollPane = new VisScrollPane(pluginList);
        scrollPane.setFadeScrollBars(false);
        scrollPane.setForceScroll(false, true);

        VisTable buttonTable = new VisTable();
        buttonTable.pad(5);
        VisTextButton addPlugin = new VisTextButton("Install plugin");
        addPlugin.addListener(
                new ChangeListener() {
                    @Override
                    public void changed(ChangeEvent event, Actor actor) {
                        addPlugin();
                    }
                });

        buttonTable.add(addPlugin);
        buttonTable.add("").growX();

        VisTextButton cancel = new VisTextButton("Cancel");
        cancel.addListener(
                new ChangeListener() {
                    @Override
                    public void changed(ChangeEvent event, Actor actor) {
                        close();
                    }
                });
        buttonTable.add(cancel);
        buttonTable.add("").width(5);

        VisTextButton save = new VisTextButton("Save");
        save.addListener(
                new ChangeListener() {
                    @Override
                    public void changed(ChangeEvent event, Actor actor) {
                        PluginPreferences.savePlugins(pluginJars);
                        close();
                    }
                });
        buttonTable.add(save);

        add(scrollPane).grow();
        row();
        add(new VisLabel("Any changes made on this screen take effect after restarting the application"));
        row();
        add(new Separator());
        row();
        add(buttonTable).growX();
        row();
    }

    private void addPluginDefinition(final PluginDefinition pluginDefinition) {
        final Separator separator = new Separator();
        pluginList.addActor(separator);
        final VisTable pluginTable = new VisTable();
        pluginTable.pad(2);
        pluginTable.add(createValueLabel(pluginDefinition.jarPath)).growX().left();
        pluginTable.add(createValueLabel(pluginDefinition.pluginName)).width(180).left();
        pluginTable.add(createValueLabel(pluginDefinition.pluginVersion)).width(80).left();
        pluginTable.add(createValueLabel(String.valueOf(pluginDefinition.loaded))).width(60);
        if (pluginDefinition.canBeRemoved) {
            pluginJars.add(pluginDefinition.jarPath);
            VisTextButton textButton = new VisTextButton("Remove");
            textButton.addListener(
                    new ChangeListener() {
                        @Override
                        public void changed(ChangeEvent event, Actor actor) {
                            pluginJars.removeValue(pluginDefinition.jarPath, false);
                            pluginList.removeActor(pluginTable);
                            pluginList.removeActor(separator);
                        }
                    });
            pluginTable.add(textButton).width(60);
        } else {
            pluginTable.add("").width(60);
        }
        pluginTable.row();
        pluginList.addActor(pluginTable);
    }

    private VisLabel createValueLabel(String text) {
        VisLabel label = new VisLabel(text);
        label.setWrap(true);
        label.setColor(Color.GRAY);
        return label;
    }

    private void addPlugin() {
        FileTypeFilter filter = new FileTypeFilter(true);
        filter.addRule("Plugin File (*.jar, *.json)", "jar", "json");

        FileChooser fileChooser = new FileChooser(FileChooser.Mode.OPEN);
        fileChooser.setModal(true);
        fileChooser.setSelectionMode(FileChooser.SelectionMode.FILES);
        fileChooser.setFileTypeFilter(filter);
        fileChooser.setListener(new FileChooserAdapter() {
            @Override
            public void selected(Array<FileHandle> file) {
                FileHandle selectedFile = file.get(0);
                processPlugin(selectedFile);
            }
        });
        getStage().addActor(fileChooser.fadeIn());
    }

    private void processPlugin(FileHandle selectedFile) {
        try {
            PluginDefinition pluginDefinition = PluginPreferences.getPluginDefinition(selectedFile.file());
            addPluginDefinition(pluginDefinition);
        } catch (Exception exp) {
            Dialogs.showErrorDialog(getStage(), "Unable to process the file as a plugin");
        }
    }
}
