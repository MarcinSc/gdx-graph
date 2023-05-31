package com.gempukku.libgdx.graph.config;

import com.badlogic.gdx.utils.Array;
import org.junit.Test;

import static com.gempukku.libgdx.graph.config.MapUtil.mapOf;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class VectorArithmeticOutputTypeFunctionTest {
    @Test
    public void testOneInput() {
        VectorArithmeticOutputTypeFunction func = new VectorArithmeticOutputTypeFunction("Float", "a");

        assertEquals("Float", func.evaluate(
                mapOf(
                        Array.with("a"),
                        Array.with(Array.with("Float"))
                )));

        assertEquals("Vector2", func.evaluate(
                mapOf(
                        Array.with("a"),
                        Array.with(Array.with("Vector2"))
                )));
    }

    @Test
    public void testTwoInputs() {
        VectorArithmeticOutputTypeFunction func = new VectorArithmeticOutputTypeFunction("Float", "a");

        assertEquals("Float", func.evaluate(
                mapOf(
                        Array.with("a"),
                        Array.with(Array.with("Float", "Float"))
                )));

        assertEquals("Vector2", func.evaluate(
                mapOf(
                        Array.with("a"),
                        Array.with(Array.with("Vector2", "Vector2"))
                )));

        assertEquals("Vector2", func.evaluate(
                mapOf(
                        Array.with("a"),
                        Array.with(Array.with("Vector2", "Float"))
                )));

        assertNull(func.evaluate(
                mapOf(
                        Array.with("a"),
                        Array.with(Array.with("Vector2", "Vector3"))
                )));
    }
}