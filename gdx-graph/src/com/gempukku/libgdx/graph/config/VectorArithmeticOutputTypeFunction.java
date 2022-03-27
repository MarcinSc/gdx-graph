package com.gempukku.libgdx.graph.config;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectMap;

import java.util.function.Function;

public class VectorArithmeticOutputTypeFunction implements Function<ObjectMap<String, Array<String>>, String> {
    private final String floatType;
    private final String input1;
    private final String input2;

    public VectorArithmeticOutputTypeFunction(String floatType, String input1, String input2) {
        this.floatType = floatType;
        this.input1 = input1;
        this.input2 = input2;
    }

    @Override
    public String apply(ObjectMap<String, Array<String>> inputs) {
        Array<String> inputA = inputs.get(input1);
        Array<String> inputB = inputs.get(input2);
        if (inputA.size < 1 || inputB.size < 1)
            return null;

        String a = inputA.get(0);
        String b = inputB.get(0);
        if (a == null || b == null)
            return null;

        if (a.equals(floatType))
            return b;
        if (b.equals(floatType))
            return a;
        if (!a.equals(b))
            return null;

        return a;
    }
}
