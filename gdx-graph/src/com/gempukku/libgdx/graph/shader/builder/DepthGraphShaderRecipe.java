package com.gempukku.libgdx.graph.shader.builder;

import com.gempukku.libgdx.graph.shader.builder.recipe.DefaultGraphShaderRecipe;
import com.gempukku.libgdx.graph.shader.builder.recipe.finalize.DebugShadersIngredient;
import com.gempukku.libgdx.graph.shader.builder.recipe.finalize.InitializeShaderProgramIngredient;
import com.gempukku.libgdx.graph.shader.builder.recipe.finalize.SetShaderProgramIngredient;
import com.gempukku.libgdx.graph.shader.builder.recipe.fragment.AlphaDiscardFragmentIngredient;
import com.gempukku.libgdx.graph.shader.builder.recipe.fragment.DepthFragmentIngredient;
import com.gempukku.libgdx.graph.shader.builder.recipe.init.InitializeDepthShaderIngredient;
import com.gempukku.libgdx.graph.shader.builder.recipe.init.InitializePropertyMapIngredient;
import com.gempukku.libgdx.graph.shader.builder.recipe.init.SetupFloatPrevisionIngredient;
import com.gempukku.libgdx.graph.shader.builder.recipe.init.SetupOpenGLSettingsIngredient;
import com.gempukku.libgdx.graph.shader.builder.recipe.vertex.ModelPositionVertexShaderIngredient;

public class DepthGraphShaderRecipe extends DefaultGraphShaderRecipe {
    public DepthGraphShaderRecipe() {
        addInitIngredient(new InitializePropertyMapIngredient());
        addInitIngredient(new SetupOpenGLSettingsIngredient("end"));
        addInitIngredient(new SetupFloatPrevisionIngredient());
        addInitIngredient(new InitializeDepthShaderIngredient());

        addVertexShaderIngredient(new ModelPositionVertexShaderIngredient("end", "position"));

        addFragmentShaderIngredient(new AlphaDiscardFragmentIngredient("end", "alpha", "end", "alphaClip"));
        addFragmentShaderIngredient(new DepthFragmentIngredient());

        addFinalizeShaderIngredient(new DebugShadersIngredient("depth"));
        addFinalizeShaderIngredient(new SetShaderProgramIngredient());
        addFinalizeShaderIngredient(new InitializeShaderProgramIngredient());
    }
}
