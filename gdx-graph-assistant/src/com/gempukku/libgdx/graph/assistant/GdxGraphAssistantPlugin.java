package com.gempukku.libgdx.graph.assistant;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.Collections;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.reflect.ReflectionException;
import com.gempukku.gdx.assistant.plugin.*;
import com.gempukku.gdx.plugins.PluginEnvironment;
import com.gempukku.gdx.plugins.PluginVersion;
import com.gempukku.libgdx.graph.pipeline.util.WhitePixel;
import com.gempukku.libgdx.graph.plugin.boneanimation.design.UIBoneAnimationPlugin;
import com.gempukku.libgdx.graph.plugin.callback.design.UIRenderCallbackPlugin;
import com.gempukku.libgdx.graph.plugin.callback.design.UIScreenshotPlugin;
import com.gempukku.libgdx.graph.plugin.lighting3d.design.UILighting3DPlugin;
import com.gempukku.libgdx.graph.plugin.maps.design.UIMapsPlugin;
import com.gempukku.libgdx.graph.plugin.particles.design.UIParticlesPlugin;
import com.gempukku.libgdx.graph.plugin.postprocess.design.UIPostprocessPlugin;
import com.gempukku.libgdx.graph.plugin.ui.design.UIUserInterfacePlugin;
import com.gempukku.libgdx.graph.shader.UIModelsPlugin;
import com.gempukku.libgdx.graph.ui.AssetResolver;
import com.gempukku.libgdx.graph.ui.ColorPickerSupplier;
import com.gempukku.libgdx.graph.ui.PatternTextures;
import com.gempukku.libgdx.graph.ui.UIGdxGraphPluginRegistry;
import com.gempukku.libgdx.graph.ui.pipeline.UIRenderPipelinePlugin;
import com.gempukku.libgdx.ui.input.KeyCombination;
import com.kotcrab.vis.ui.VisUI;

public class GdxGraphAssistantPlugin implements AssistantPlugin {
    private AssistantApplication assistantApplication;
    private TextureAtlas gdxGraphTextureAtlas;

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
    public void registerPlugin() {
        Collections.allocateIterators = true;
        registerGdxPlugins();
    }

    @Override
    public void initializePlugin(AssistantApplication assistantApplication) {
        this.assistantApplication = assistantApplication;

        Skin skin = VisUI.getSkin();

        gdxGraphTextureAtlas = new TextureAtlas(assistantApplication.getAssetResolver().resolve("skin/gdx-graph/uiskin.atlas"));
        skin.addRegions(gdxGraphTextureAtlas);
        skin.load(assistantApplication.getAssetResolver().resolve("skin/gdx-graph/uiskin.json"));

        WhitePixel.initializeShared();
        PatternTextures.initializeShared(assistantApplication.getAssetResolver());
        try {
            UIGdxGraphPluginRegistry.initializePlugins(assistantApplication.getAssetResolver());
        } catch (ReflectionException exp) {
            throw new GdxRuntimeException("Unable to initialize plugins", exp);
        }
        ColorPickerSupplier.initialize();
        AssetResolver.initialize(assistantApplication.getAssetResolver());

        MenuManager menuManager = assistantApplication.getMenuManager();

        menuManager.addMainMenu("Graph");

        menuManager.addPopupMenu("Graph", null, "New");
        menuManager.setPopupMenuDisabled("Graph", null, "New", true);

        menuManager.addPopupMenu("Graph", null, "Open");
        menuManager.setPopupMenuDisabled("Graph", null, "Open", true);

        menuManager.addPopupMenu("Graph", null, "Import");
        menuManager.setPopupMenuDisabled("Graph", null, "Import", true);

        menuManager.addPopupMenu("Graph", null, "Remove");
        menuManager.setPopupMenuDisabled("Graph", null, "Remove", true);

        menuManager.addMenuSeparator("Graph", null);

        menuManager.addMenuItem("Graph", null, "Create group", new KeyCombination(true, false, false, Input.Keys.G), null);
        menuManager.setMenuItemDisabled("Graph", null, "Create group", true);

        menuManager.addMenuItem("Graph", null, "View shader text", new KeyCombination(true, true, false, Input.Keys.V), null);
        menuManager.setMenuItemDisabled("Graph", null, "View shader text", true);
    }

    private static void registerGdxPlugins() {
        UIGdxGraphPluginRegistry.register(UIRenderPipelinePlugin.class);
        UIGdxGraphPluginRegistry.register(UIRenderCallbackPlugin.class);
        UIGdxGraphPluginRegistry.register(UIMapsPlugin.class);
        UIGdxGraphPluginRegistry.register(UIUserInterfacePlugin.class);
        UIGdxGraphPluginRegistry.register(UIModelsPlugin.class);
        UIGdxGraphPluginRegistry.register(UILighting3DPlugin.class);
        UIGdxGraphPluginRegistry.register(UIParticlesPlugin.class);
        UIGdxGraphPluginRegistry.register(UIPostprocessPlugin.class);
        UIGdxGraphPluginRegistry.register(UIBoneAnimationPlugin.class);
        UIGdxGraphPluginRegistry.register(UIScreenshotPlugin.class);
    }

    @Override
    public void deregisterPlugin() {
        AssetResolver.dispose();
        ColorPickerSupplier.dispose();
        gdxGraphTextureAtlas.dispose();
        WhitePixel.disposeShared();
        PatternTextures.disposeShared();
    }

    @Override
    public AssistantPluginProject newProjectCreated(AssistantProject assistantProject) {
        return new GdxGraphProject(assistantApplication, assistantProject);
    }

    @Override
    public AssistantPluginProject projectOpened(AssistantProject assistantProject, JsonValue projectData) {
        return new GdxGraphProject(assistantApplication, assistantProject, projectData);
    }
}
