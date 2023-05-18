package com.gempukku.libgdx.graph.shader.builder;

import com.gempukku.libgdx.graph.shader.builder.recipe.DefaultGraphShaderRecipe;
import com.gempukku.libgdx.graph.shader.builder.recipe.finalize.DebugShadersIngredient;
import com.gempukku.libgdx.graph.shader.builder.recipe.finalize.InitializeShaderProgramIngredient;
import com.gempukku.libgdx.graph.shader.builder.recipe.finalize.SetShaderProgramIngredient;
import com.gempukku.libgdx.graph.shader.builder.recipe.fragment.AlphaDiscardFragmentIngredient;
import com.gempukku.libgdx.graph.shader.builder.recipe.fragment.ColorAlphaFragmentIngredient;
import com.gempukku.libgdx.graph.shader.builder.recipe.fragment.DeadParticleDiscardFragmentIngredient;
import com.gempukku.libgdx.graph.shader.builder.recipe.init.InitializePropertyMapIngredient;
import com.gempukku.libgdx.graph.shader.builder.recipe.init.SetupFloatPrevisionIngredient;
import com.gempukku.libgdx.graph.shader.builder.recipe.init.SetupOpenGLSettingsIngredient;
import com.gempukku.libgdx.graph.shader.builder.recipe.vertex.ModelPositionVertexShaderIngredient;

public class ParticlesGraphShaderRecipe extends DefaultGraphShaderRecipe {
    public ParticlesGraphShaderRecipe() {
        addInitIngredient(new InitializePropertyMapIngredient());
        addInitIngredient(new SetupOpenGLSettingsIngredient("end"));
        addInitIngredient(new SetupFloatPrevisionIngredient());

        addVertexShaderIngredient(new ModelPositionVertexShaderIngredient("end", "position"));

        addFragmentShaderIngredient(new DeadParticleDiscardFragmentIngredient());
        addFragmentShaderIngredient(new AlphaDiscardFragmentIngredient("end", "alpha", "end", "alphaClip"));
        addFragmentShaderIngredient(new ColorAlphaFragmentIngredient("end", "color", "end", "alpha"));

        addFinalizeShaderIngredient(new DebugShadersIngredient("particles"));
        addFinalizeShaderIngredient(new SetShaderProgramIngredient());
        addFinalizeShaderIngredient(new InitializeShaderProgramIngredient());
    }
}
