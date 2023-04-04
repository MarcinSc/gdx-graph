package com.gempukku.libgdx.graph.assistant;

import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.JsonValue;
import com.gempukku.gdx.assistant.plugin.AssistantApplication;
import com.gempukku.gdx.assistant.plugin.AssistantPlugin;
import com.gempukku.gdx.assistant.plugin.AssistantPluginProject;
import com.gempukku.gdx.plugins.PluginEnvironment;
import com.gempukku.gdx.plugins.PluginVersion;
import com.gempukku.libgdx.graph.plugin.boneanimation.design.BoneAnimationPlugin;
import com.gempukku.libgdx.graph.plugin.callback.design.RenderCallbackPlugin;
import com.gempukku.libgdx.graph.plugin.lighting3d.design.Lighting3DPlugin;
import com.gempukku.libgdx.graph.plugin.models.design.ModelsPlugin;
import com.gempukku.libgdx.graph.plugin.particles.design.ParticlesPlugin;
import com.gempukku.libgdx.graph.plugin.screen.design.ScreenPlugin;
import com.gempukku.libgdx.graph.ui.PatternTextures;
import com.gempukku.libgdx.graph.util.WhitePixel;
import com.gempukku.libgdx.ui.curve.GCurveEditor;
import com.gempukku.libgdx.ui.gradient.GGradientEditor;
import com.kotcrab.vis.ui.VisUI;

public class GdxGraphAssistantPlugin implements AssistantPlugin {
    private AssistantApplication assistantApplication;

    @Override
    public String getId() {
        return "gdx-graph";
    }

    @Override
    public PluginVersion getVersion() {
        return new PluginVersion(0, 0, 1);
    }

    @Override
    public boolean shouldBeRegistered(PluginEnvironment pluginEnvironment) {
        return true;
    }

    @Override
    public void registerPlugin(AssistantApplication assistantApplication) {
        this.assistantApplication = assistantApplication;

        WhitePixel.initializeShared();
        PatternTextures.initializeShared();
        registerGdxPlugins();

        Skin skin = VisUI.getSkin();

        GGradientEditor.GGradientEditorStyle gradientEditorStyle = new GGradientEditor.GGradientEditorStyle();
        gradientEditorStyle.background = skin.getDrawable("white");
        gradientEditorStyle.tick = skin.getDrawable("white");
        skin.add("gdx-graph", gradientEditorStyle);

        GCurveEditor.GCurveEditorStyle curveEditorStyle = new GCurveEditor.GCurveEditorStyle();
        curveEditorStyle.background = skin.getDrawable("white");
        skin.add("gdx-graph", curveEditorStyle);

        assistantApplication.addMainMenu("Graph");

        assistantApplication.addPopupMenu("Graph", null, "New");
        assistantApplication.setPopupMenuDisabled("Graph", null, "New", true);

        assistantApplication.addPopupMenu("Graph", null, "Open");
        assistantApplication.setPopupMenuDisabled("Graph", null, "Open", true);

        assistantApplication.addMenuItem("Graph", null, "Import graph", null);
        assistantApplication.setMenuItemDisabled("Graph", null, "Import graph", true);
    }

    private static void registerGdxPlugins() {
        new RenderCallbackPlugin().initialize();
        new JsonGdxGraphPlugin("config/plugin-ui-config.json").initialize();
        new ScreenPlugin().initialize();
        new ParticlesPlugin().initialize();
        new ModelsPlugin().initialize();
        new BoneAnimationPlugin().initialize();
        new Lighting3DPlugin().initialize();
        new JsonGdxGraphPlugin("config/plugin-maps-config.json").initialize();
    }

    @Override
    public void deregisterPlugin() {
        WhitePixel.disposeShared();
        PatternTextures.disposeShared();
    }

    @Override
    public AssistantPluginProject newProjectCreated() {
        return new GdxGraphProject(assistantApplication);
    }

    @Override
    public AssistantPluginProject projectOpened(JsonValue projectData) {
        return new GdxGraphProject(assistantApplication, projectData);
    }
}
