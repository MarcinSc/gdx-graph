package com.gempukku.libgdx.graph.render.screenshot.producer;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.gempukku.libgdx.common.Function;
import com.gempukku.libgdx.common.SimpleNumberFormatter;

public class PathParser {
    public static Function<Float, FileHandle> parsePath(final String path) {
        return new Function<Float, FileHandle>() {
            @Override
            public FileHandle evaluate(Float time) {
                return Gdx.files.local(path.replaceAll("\\[second]", SimpleNumberFormatter.format(time, 0))
                        .replaceAll("\\[time]", SimpleNumberFormatter.format(time, 4)));
            }
        };
    }
}
