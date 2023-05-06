package com.gempukku.libgdx.graph.ui;

import com.badlogic.gdx.assets.loaders.FileHandleResolver;
import com.badlogic.gdx.files.FileHandle;

public class AssetResolver implements FileHandleResolver {
    public static AssetResolver instance;
    private final FileHandleResolver assetResolver;

    private AssetResolver(FileHandleResolver assetResolver) {
        this.assetResolver = assetResolver;
    }

    @Override
    public FileHandle resolve(String fileName) {
        return assetResolver.resolve(fileName);
    }

    public static void initialize(FileHandleResolver assetResolver) {
        instance = new AssetResolver(assetResolver);
    }

    public static void dispose() {

    }
}
