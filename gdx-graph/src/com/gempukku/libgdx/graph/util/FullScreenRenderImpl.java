package com.gempukku.libgdx.graph.util;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.VertexAttribute;
import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.utils.Disposable;
import com.gempukku.libgdx.graph.pipeline.producer.FullScreenRender;

public class FullScreenRenderImpl implements FullScreenRender, Disposable {
    private final Mesh mesh;

    public FullScreenRenderImpl() {
        float[] verticeData = new float[]{
                0, 0, 0,
                0, 1, 0,
                1, 0, 0,
                1, 1, 0};
        short[] indices = {0, 2, 1, 2, 3, 1};

        mesh = new Mesh(true, 4, 6, new VertexAttributes(VertexAttribute.Position()));
        mesh.setVertices(verticeData);
        mesh.setIndices(indices);
    }

    @Override
    public void renderFullScreen(ShaderProgram shaderProgram) {
        mesh.bind(shaderProgram);
        Gdx.gl20.glDrawElements(Gdx.gl20.GL_TRIANGLES, 6, GL20.GL_UNSIGNED_SHORT, 0);
        mesh.unbind(shaderProgram);
    }

    @Override
    public void dispose() {
        mesh.dispose();
    }
}
