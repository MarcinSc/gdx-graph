package com.gempukku.libgdx.graph.shader.builder;

import com.gempukku.libgdx.graph.shader.builder.recipe.DefaultGraphShaderRecipe;
import com.gempukku.libgdx.graph.shader.builder.recipe.finalize.DebugShadersIngredient;
import com.gempukku.libgdx.graph.shader.builder.recipe.finalize.InitializeShaderProgramIngredient;
import com.gempukku.libgdx.graph.shader.builder.recipe.finalize.SetShaderProgramIngredient;
import com.gempukku.libgdx.graph.shader.builder.recipe.fragment.ColorAlphaFragmentIngredient;
import com.gempukku.libgdx.graph.shader.builder.recipe.fragment.DiscardFragmentIngredient;
import com.gempukku.libgdx.graph.shader.builder.recipe.init.InitializePropertyMapIngredient;
import com.gempukku.libgdx.graph.shader.builder.recipe.init.InitializeScreenShaderIngredient;
import com.gempukku.libgdx.graph.shader.builder.recipe.init.SetupFloatPrevisionIngredient;
import com.gempukku.libgdx.graph.shader.builder.recipe.init.SetupOpenGLSettingsIngredient;
import com.gempukku.libgdx.graph.shader.builder.recipe.source.InputSource;
import com.gempukku.libgdx.graph.shader.builder.recipe.vertex.ScreenPositionVertexShaderIngredient;

public class ScreenGraphShaderRecipe extends DefaultGraphShaderRecipe{
    public ScreenGraphShaderRecipe() {
        super("end");
        addInitIngredient(new InitializePropertyMapIngredient());
        addInitIngredient(new SetupOpenGLSettingsIngredient("end"));
        addInitIngredient(new SetupFloatPrevisionIngredient());
        addInitIngredient(new InitializeScreenShaderIngredient());

        addVertexShaderIngredient(new ScreenPositionVertexShaderIngredient());

        addFragmentShaderIngredient(new DiscardFragmentIngredient(new InputSource("end", "discardValue")));
        addFragmentShaderIngredient(new ColorAlphaFragmentIngredient(new InputSource("end", "color"), new InputSource("end", "alpha")));

        addFinalizeShaderIngredient(new DebugShadersIngredient("screen"));
        addFinalizeShaderIngredient(new SetShaderProgramIngredient());
        addFinalizeShaderIngredient(new InitializeShaderProgramIngredient());
    }
}
