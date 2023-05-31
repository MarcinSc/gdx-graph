package com.gempukku.libgdx.graph.config;

import com.badlogic.gdx.utils.Array;
import org.junit.Test;

import static com.gempukku.libgdx.graph.config.MapUtil.mapOf;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class ValidateSameTypeOutputTypeFunctionTest {
    @Test
    public void testOneInput() {
        ValidateSameTypeOutputTypeFunction func = new ValidateSameTypeOutputTypeFunction("Float", "input");
        assertEquals("Float", func.evaluate(
                mapOf(
                        Array.with("input"),
                        Array.with(Array.with("Vector2", "Vector2"))
                )));
        assertEquals("Float", func.evaluate(
                mapOf(
                        Array.with("input"),
                        Array.with(Array.with("Float", "Float"))
                )));
        assertNull(func.evaluate(
                mapOf(
                        Array.with("input"),
                        Array.with(Array.with("Float", "Vector2"))
                )));
    }

    @Test
    public void testTwoInputs() {
        ValidateSameTypeOutputTypeFunction func = new ValidateSameTypeOutputTypeFunction("Float", "input1", "input2");
        assertEquals("Float", func.evaluate(
                mapOf(
                        Array.with("input1", "input2"),
                        Array.with(Array.with("Vector2"), Array.with("Vector2"))
                )));
        assertEquals("Float", func.evaluate(
                mapOf(
                        Array.with("input1", "input2"),
                        Array.with(Array.with("Float"), Array.with("Float"))
                )));
        assertNull(func.evaluate(
                mapOf(
                        Array.with("input1", "input2"),
                        Array.with(Array.with("Float"), Array.with("Vector2"))
                )));
    }
}