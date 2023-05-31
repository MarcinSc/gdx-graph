package com.gempukku.libgdx.graph.config;

import com.badlogic.gdx.utils.Array;
import org.junit.Test;

import static com.gempukku.libgdx.graph.config.MapUtil.mapOf;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class MathCommonOutputTypeFunctionTest {
    @Test
    public void testOneInputOneAccepting() {
        MathCommonOutputTypeFunction func = new MathCommonOutputTypeFunction("Float", new String[]{"input"}, new String[]{"a"});

        assertEquals("Float", func.evaluate(
                mapOf(
                        Array.with("input", "a"),
                        Array.with(Array.with("Float"), Array.with("Float"))
                )));

        assertEquals("Float", func.evaluate(
                mapOf(
                        Array.with("input", "a"),
                        Array.with(Array.with("Float", "Float"), Array.with("Float"))
                )));

        assertEquals("Float", func.evaluate(
                mapOf(
                        Array.with("input", "a"),
                        Array.with(Array.with("Float", "Float"), Array.with("Float", "Float"))
                )));


        assertEquals("Vector2", func.evaluate(
                mapOf(
                        Array.with("input", "a"),
                        Array.with(Array.with("Vector2"), Array.with("Vector2"))
                )));

        assertEquals("Vector2", func.evaluate(
                mapOf(
                        Array.with("input", "a"),
                        Array.with(Array.with("Vector2"), Array.with("Float"))
                )));

        assertEquals("Vector2", func.evaluate(
                mapOf(
                        Array.with("input", "a"),
                        Array.with(Array.with("Vector2"), Array.with("Vector2", "Float"))
                )));

        assertNull(func.evaluate(
                mapOf(
                        Array.with("input", "a"),
                        Array.with(Array.with("Float", "Vector2"), Array.with("Float"))
                )));

        assertNull(func.evaluate(
                mapOf(
                        Array.with("input", "a"),
                        Array.with(Array.with("Vector2"), Array.with("Vector3"))
                )));

        assertNull(func.evaluate(
                mapOf(
                        Array.with("input", "a"),
                        Array.with(Array.with("Vector2"), Array.with("Float", "Vector3"))
                )));
    }
}
