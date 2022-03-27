package com.gempukku.libgdx.graph.ui.plugin;

import com.badlogic.gdx.assets.loaders.resolvers.ExternalFileHandleResolver;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.reflect.ClassReflection;

import java.io.*;
import java.util.jar.Attributes;
import java.util.jar.JarFile;
import java.util.jar.Manifest;

public class PluginPreferences {
    public static final String PLUGIN_LIST_FOLDER = ".prefs";
    public static final String PLUGIN_LIST_FILE = "com.gempukku.libgdx.graph.plugins.jars";
    private static final String PLUGIN_NAME = "GDX-Graph-Plugin-Name";
    private static final String PLUGIN_VERSION = "GDX-Graph-Plugin-Version";
    private static final String PLUGIN_CLASS = "GDX-Graph-Plugin-Class";

    public static void savePlugins(Iterable<String> plugins) {
        FileHandle jarFile = new FileHandle(getPluginListFile());
        try {
            try (Writer writer = jarFile.writer(false)) {
                for (String plugin : plugins) {
                    writer.write(plugin);
                    writer.write('\n');
                }
            }
        } catch (IOException exp) {
            throw new GdxRuntimeException(exp);
        }
    }

    public static Iterable<String> getPlugins() {
        FileHandle jarFile = new FileHandle(getPluginListFile());
        if (!jarFile.exists())
            return new Array<>();

        try {
            Array<String> result = new Array<>();
            try (BufferedReader reader = new BufferedReader(jarFile.reader())) {
                String line;
                while ((line = reader.readLine()) != null) {
                    result.add(line);
                }
                return result;
            }
        } catch (IOException exp) {
            throw new GdxRuntimeException(exp);
        }
    }

    private static File getPluginListFile() {
        return new File(new File("").getAbsolutePath() + File.separator + PLUGIN_LIST_FOLDER + File.separator + PLUGIN_LIST_FILE);
    }

    public static PluginDefinition getPluginDefinition(File pluginFile) throws IOException, ClassNotFoundException {
        if (pluginFile.getName().toLowerCase().endsWith(".json")) {
            JsonReader reader = new JsonReader();
            FileReader fileReader = new FileReader(pluginFile);
            try {
                JsonValue pluginJson = reader.parse(fileReader);
                String pluginName = pluginJson.getString("pluginName", "undefined");
                String pluginVersion = pluginJson.getString("pluginVersion", "undefined");

                return new PluginDefinition(pluginFile.getAbsolutePath(),
                        new ExternalFileHandleResolver(), pluginFile.getAbsolutePath(), pluginName, pluginVersion, false, true);
            } finally {
                fileReader.close();
            }
        } else {
            JarFile jarFile = new JarFile(pluginFile);
            Manifest manifest = jarFile.getManifest();
            Attributes attributes = manifest.getMainAttributes();
            if (!attributes.containsKey(PLUGIN_NAME) || !attributes.containsKey(PLUGIN_VERSION)
                    || !attributes.containsKey(PLUGIN_CLASS)) {
                throw new GdxRuntimeException("Specified JAR does not contain plugin");
            }
            String pluginName = attributes.getValue(PLUGIN_NAME);
            String pluginVersion = attributes.getValue(PLUGIN_VERSION);
            Class<? extends PluginDesignInitializer> pluginClass = (Class<? extends PluginDesignInitializer>) Class.forName(attributes.getValue(PLUGIN_CLASS));
            if (!ClassReflection.isAssignableFrom(PluginDesignInitializer.class, pluginClass))
                throw new GdxRuntimeException("Plugin class is not of required type");

            return new PluginDefinition(
                    pluginFile.getAbsolutePath(), pluginClass, pluginName, pluginVersion, false, true);
        }
    }
}
