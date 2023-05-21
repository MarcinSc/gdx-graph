package com.gempukku.libgdx.graph.shader.builder;

import com.badlogic.gdx.assets.loaders.FileHandleResolver;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.ObjectMap;
import com.gempukku.libgdx.common.LibGDXCollections;

import java.nio.charset.StandardCharsets;

public class GLSLFragmentReader {
    private static final ObjectMap<String, String> map = new ObjectMap<>();

    private GLSLFragmentReader() {
    }

    public static String getFragment(FileHandleResolver assetResolver, String fragmentName) {
        return getFragment(assetResolver, fragmentName, LibGDXCollections.<String, String>emptyMap());
    }

    public static String getFragment(FileHandleResolver assetResolver, String fragmentName, ObjectMap<String, String> replacements) {
        String fragment = readFragment(assetResolver, fragmentName);
        for (ObjectMap.Entry<String, String> replacement : replacements.entries())
            fragment = fragment.replace(replacement.key, replacement.value);

        return fragment;
    }

    private static String readFragment(FileHandleResolver assetResolver, String fragmentName) {
        String result = map.get(fragmentName);
        if (result != null)
            return result;

        FileHandle fileHandle = assetResolver.resolve("glsl/fragment/" + fragmentName + ".glsl");
        String fragment = new String(fileHandle.readBytes(), StandardCharsets.UTF_8);
        map.put(fragmentName, fragment);

        return fragment;
    }
}
