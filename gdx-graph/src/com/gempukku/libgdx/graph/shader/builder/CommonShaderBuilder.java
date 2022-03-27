package com.gempukku.libgdx.graph.shader.builder;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectMap;
import com.badlogic.gdx.utils.OrderedMap;
import com.gempukku.libgdx.graph.shader.UniformRegistry;

public abstract class CommonShaderBuilder {
    protected UniformRegistry uniformRegistry;

    private final OrderedMap<String, UniformVariable> uniformVariables = new OrderedMap<String, UniformVariable>();
    private final OrderedMap<String, String> varyingVariables = new OrderedMap<String, String>();
    private final OrderedMap<String, String> variables = new OrderedMap<String, String>();
    private final OrderedMap<String, String> functions = new OrderedMap<String, String>();
    private final OrderedMap<String, String> structures = new OrderedMap<String, String>();
    private final Array<String> mainLines = new Array<>();
    private final Array<String> initialLines = new Array<>();

    public CommonShaderBuilder(UniformRegistry uniformRegistry) {
        this.uniformRegistry = uniformRegistry;
    }

    public void addStructArrayUniformVariable(String name, String[] fieldNames, int size, String type, boolean global,
                                              UniformRegistry.StructArrayUniformSetter setter, String comment) {
        if (global)
            uniformRegistry.registerGlobalStructArrayUniform(name, fieldNames, setter);
        else
            uniformRegistry.registerLocalStructArrayUniform(name, fieldNames, setter);
        uniformVariables.put(name + "[" + size + "]", new UniformVariable(type, global, null, comment));
    }

    public void addArrayUniformVariable(String name, int size, String type, boolean global, UniformRegistry.UniformSetter setter, String comment) {
        if (global)
            uniformRegistry.registerGlobalUniform(name, setter);
        else
            uniformRegistry.registerLocalUniform(name, setter);
        uniformVariables.put(name, new UniformVariable(type, global, setter, comment, size));
    }

    public boolean hasUniformVariable(String name) {
        return uniformVariables.containsKey(name);
    }

    public void addUniformVariable(String name, String type, boolean global, UniformRegistry.UniformSetter setter, String comment) {
        addArrayUniformVariable(name, -1, type, global, setter, comment);
    }

    public boolean hasVaryingVariable(String name) {
        return varyingVariables.containsKey(name);
    }

    public void addVaryingVariable(String name, String type) {
        if (varyingVariables.containsKey(name))
            throw new IllegalStateException("Already contains varying variable of that name");
        varyingVariables.put(name, type);
    }

    public void addVariable(String name, String type) {
        if (variables.containsKey(name))
            throw new IllegalStateException("Already contains variable of that name");
        variables.put(name, type);
    }

    public void addFunction(String name, String functionText) {
        if (functions.containsKey(name))
            throw new IllegalStateException("Already contains function of that name");
        functions.put(name, functionText);
    }

    public boolean containsFunction(String name) {
        return functions.containsKey(name);
    }

    public void addStructure(String name, String structureText) {
        if (structures.containsKey(name) && !structures.get(name).equals(structureText))
            throw new IllegalStateException("Already contains structure of that name with different text");
        structures.put(name, structureText);
    }

    public void addInitialLine(String initialLine) {
        initialLines.add(initialLine);
    }

    public void addMainLine(String mainLine) {
        mainLines.add(mainLine);
    }

    protected void appendUniformVariables(StringBuilder stringBuilder) {
        for (ObjectMap.Entry<String, UniformVariable> uniformDefinition : uniformVariables.entries()) {
            UniformVariable uniformVariable = uniformDefinition.value;
            String name = uniformDefinition.key;
            if (uniformVariable.size > -1)
                name += "[" + uniformVariable.size + "]";
            if (uniformVariable.comment != null)
                stringBuilder.append("// " + uniformVariable.comment + "\n");
            stringBuilder.append("uniform " + uniformVariable.type + " " + name + ";\n");
        }
        if (!uniformVariables.isEmpty())
            stringBuilder.append('\n');
    }

    protected void appendVaryingVariables(StringBuilder stringBuilder) {
        for (ObjectMap.Entry<String, String> varyingDefinition : varyingVariables.entries()) {
            stringBuilder.append("varying " + varyingDefinition.value + " " + varyingDefinition.key + ";\n");
        }
        if (!varyingVariables.isEmpty())
            stringBuilder.append('\n');
    }

    protected void appendVariables(StringBuilder stringBuilder) {
        for (ObjectMap.Entry<String, String> variable : variables.entries()) {
            stringBuilder.append(variable.value + " " + variable.key + ";\n");
        }
        if (!variables.isEmpty())
            stringBuilder.append('\n');
    }

    protected void appendFunctions(StringBuilder stringBuilder) {
        for (String function : functions.values()) {
            stringBuilder.append(function);
            stringBuilder.append('\n');
        }
    }

    protected void appendStructures(StringBuilder stringBuilder) {
        for (ObjectMap.Entry<String, String> structureEntry : structures.entries()) {
            stringBuilder.append("struct " + structureEntry.key + " {\n")
                    .append(structureEntry.value).append("};\n\n");
        }
        if (!structures.isEmpty())
            stringBuilder.append('\n');
    }

    protected void appendInitial(StringBuilder stringBuilder) {
        for (String initialLine : initialLines) {
            stringBuilder.append(initialLine).append('\n');
        }
        if (!initialLines.isEmpty())
            stringBuilder.append('\n');
    }

    protected void appendMain(StringBuilder stringBuilder) {
        stringBuilder.append("void main() {\n");
        for (String mainLine : mainLines) {
            stringBuilder.append("  ").append(mainLine).append('\n');
        }

        stringBuilder.append("}\n");
    }

    private static class UniformVariable {
        public final String type;
        public final int size;
        public final boolean global;
        public final UniformRegistry.UniformSetter setter;
        public final String comment;

        public UniformVariable(String type, boolean global, UniformRegistry.UniformSetter setter, String comment) {
            this(type, global, setter, comment, -1);
        }

        public UniformVariable(String type, boolean global, UniformRegistry.UniformSetter setter, String comment, int size) {
            this.type = type;
            this.global = global;
            this.setter = setter;
            this.comment = comment;
            this.size = size;
        }
    }
}
