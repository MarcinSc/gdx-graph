package com.gempukku.libgdx.graph.shader;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.gempukku.libgdx.graph.GraphTypeRegistry;
import com.gempukku.libgdx.graph.pipeline.RendererPipelineConfiguration;
import com.gempukku.libgdx.graph.plugin.PluginRuntimeInitializer;
import com.gempukku.libgdx.graph.shader.common.effect.*;
import com.gempukku.libgdx.graph.shader.common.math.arithmetic.*;
import com.gempukku.libgdx.graph.shader.common.math.common.*;
import com.gempukku.libgdx.graph.shader.common.math.exponential.*;
import com.gempukku.libgdx.graph.shader.common.math.geometric.*;
import com.gempukku.libgdx.graph.shader.common.math.trigonometry.*;
import com.gempukku.libgdx.graph.shader.common.math.utility.DistanceFromPlaneShaderNodeBuilder;
import com.gempukku.libgdx.graph.shader.common.math.value.*;
import com.gempukku.libgdx.graph.shader.common.noise.*;
import com.gempukku.libgdx.graph.shader.common.provided.*;
import com.gempukku.libgdx.graph.shader.common.shape.*;
import com.gempukku.libgdx.graph.shader.common.sprite.BillboardSpriteShaderNodeBuilder;
import com.gempukku.libgdx.graph.shader.common.sprite.ScreenSpriteShaderNodeBuilder;
import com.gempukku.libgdx.graph.shader.common.texture.BorderDetectionShaderNodeBuilder;
import com.gempukku.libgdx.graph.shader.common.texture.Sampler2DShaderNodeBuilder;
import com.gempukku.libgdx.graph.shader.common.texture.UVFlipbookShaderNodeBuilder;
import com.gempukku.libgdx.graph.shader.common.texture.UVTilingAndOffsetShaderNodeBuilder;
import com.gempukku.libgdx.graph.shader.common.value.*;
import com.gempukku.libgdx.graph.shader.producer.EndModelShaderNodeBuilder;
import com.gempukku.libgdx.graph.shader.producer.ModelShaderRendererPipelineNodeProducer;
import com.gempukku.libgdx.graph.shader.property.*;
import com.gempukku.libgdx.graph.shader.provided.*;

public class ShaderPluginRuntimeInitializer implements PluginRuntimeInitializer {
    @Override
    public void initialize() {
        GraphTypeRegistry.registerType(new ModelShaderGraphType());

        // Math - Arithmetic
        ModelShaderConfiguration.addNodeBuilder(new AddShaderNodeBuilder());
        ModelShaderConfiguration.addNodeBuilder(new SubtractShaderNodeBuilder());
        ModelShaderConfiguration.addNodeBuilder(new OneMinusShaderNodeBuilder());
        ModelShaderConfiguration.addNodeBuilder(new MultiplyShaderNodeBuilder());
        ModelShaderConfiguration.addNodeBuilder(new DivideShaderNodeBuilder());
        ModelShaderConfiguration.addNodeBuilder(new ReciprocalShaderNodeBuilder());

        // Math - exponential
        ModelShaderConfiguration.addNodeBuilder(new PowerShaderNodeBuilder());
        ModelShaderConfiguration.addNodeBuilder(new ExponentialShaderNodeBuilder());
        ModelShaderConfiguration.addNodeBuilder(new ExponentialBase2ShaderNodeBuilder());
        ModelShaderConfiguration.addNodeBuilder(new NaturalLogarithmShaderNodeBuilder());
        ModelShaderConfiguration.addNodeBuilder(new LogarithmBase2ShaderNodeBuilder());
        ModelShaderConfiguration.addNodeBuilder(new SquareRootShaderNodeBuilder());
        ModelShaderConfiguration.addNodeBuilder(new InverseSquareRootShaderNodeBuilder());

        // Math - Common
        ModelShaderConfiguration.addNodeBuilder(new AbsShaderNodeBuilder());
        ModelShaderConfiguration.addNodeBuilder(new SignShaderNodeBuilder());
        ModelShaderConfiguration.addNodeBuilder(new FloorShaderNodeBuilder());
        ModelShaderConfiguration.addNodeBuilder(new CeilingShaderNodeBuilder());
        ModelShaderConfiguration.addNodeBuilder(new FractionalPartShaderNodeBuilder());
        ModelShaderConfiguration.addNodeBuilder(new ModuloShaderNodeBuilder());
        ModelShaderConfiguration.addNodeBuilder(new MinimumShaderNodeBuilder());
        ModelShaderConfiguration.addNodeBuilder(new MaximumShaderNodeBuilder());
        ModelShaderConfiguration.addNodeBuilder(new MaximumShaderNodeBuilder());
        ModelShaderConfiguration.addNodeBuilder(new ClampShaderNodeBuilder());
        ModelShaderConfiguration.addNodeBuilder(new SaturateShaderNodeBuilder());
        ModelShaderConfiguration.addNodeBuilder(new LerpShaderNodeBuilder());
        ModelShaderConfiguration.addNodeBuilder(new ConditionalShaderNodeBuilder());
        ModelShaderConfiguration.addNodeBuilder(new StepShaderNodeBuilder());
        ModelShaderConfiguration.addNodeBuilder(new SmoothstepShaderNodeBuilder());

        // Math - geometric
        ModelShaderConfiguration.addNodeBuilder(new LengthShaderNodeBuilder());
        ModelShaderConfiguration.addNodeBuilder(new DistanceShaderNodeBuilder());
        ModelShaderConfiguration.addNodeBuilder(new DotProductShaderNodeBuilder());
        ModelShaderConfiguration.addNodeBuilder(new CrossProductShaderNodeBuilder());
        ModelShaderConfiguration.addNodeBuilder(new NormalizeShaderNodeBuilder());

        // Math - advanced
        ModelShaderConfiguration.addNodeBuilder(new MergeShaderNodeBuilder());
        ModelShaderConfiguration.addNodeBuilder(new SplitShaderNodeBuilder());
        ModelShaderConfiguration.addNodeBuilder(new RemapShaderNodeBuilder());
        ModelShaderConfiguration.addNodeBuilder(new RemapVectorShaderNodeBuilder());
        ModelShaderConfiguration.addNodeBuilder(new RemapValueShaderNodeBuilder());

        // Math - trigonometry
        ModelShaderConfiguration.addNodeBuilder(new SinShaderNodeBuilder());
        ModelShaderConfiguration.addNodeBuilder(new CosShaderNodeBuilder());
        ModelShaderConfiguration.addNodeBuilder(new TanShaderNodeBuilder());
        ModelShaderConfiguration.addNodeBuilder(new ArcsinShaderNodeBuilder());
        ModelShaderConfiguration.addNodeBuilder(new ArccosShaderNodeBuilder());
        ModelShaderConfiguration.addNodeBuilder(new ArctanShaderNodeBuilder());
        ModelShaderConfiguration.addNodeBuilder(new Arctan2ShaderNodeBuilder());
        ModelShaderConfiguration.addNodeBuilder(new RadiansShaderNodeBuilder());
        ModelShaderConfiguration.addNodeBuilder(new DegreesShaderNodeBuilder());

        // Math - utilities
        ModelShaderConfiguration.addNodeBuilder(new DistanceFromPlaneShaderNodeBuilder());

        // Effect
        ModelShaderConfiguration.addNodeBuilder(new FresnelEffectShaderNodeBuilder());
        ModelShaderConfiguration.addNodeBuilder(new DitherShaderNodeBuilder());
        ModelShaderConfiguration.addNodeBuilder(new DitherColorShaderNodeBuilder());
        ModelShaderConfiguration.addNodeBuilder(new IntensityShaderNodeBuilder());
        ModelShaderConfiguration.addNodeBuilder(new GradientShaderNodeBuilder());

        // Texture
        ModelShaderConfiguration.addNodeBuilder(new Sampler2DShaderNodeBuilder());
        ModelShaderConfiguration.addNodeBuilder(new UVFlipbookShaderNodeBuilder());
        ModelShaderConfiguration.addNodeBuilder(new UVTilingAndOffsetShaderNodeBuilder());
        ModelShaderConfiguration.addNodeBuilder(new BorderDetectionShaderNodeBuilder());

        // Noise
        ModelShaderConfiguration.addNodeBuilder(new SimplexNoise2DShaderNodeBuilder());
        ModelShaderConfiguration.addNodeBuilder(new SimplexNoise3DShaderNodeBuilder());
        ModelShaderConfiguration.addNodeBuilder(new PerlinNoise2DShaderNodeBuilder());
        ModelShaderConfiguration.addNodeBuilder(new PerlinNoise3DShaderNodeBuilder());
        ModelShaderConfiguration.addNodeBuilder(new VoronoiDistance2DShaderNodeBuilder());
        ModelShaderConfiguration.addNodeBuilder(new VoronoiDistance3DShaderNodeBuilder());
        ModelShaderConfiguration.addNodeBuilder(new VoronoiBorder2DShaderNodeBuilder());
        ModelShaderConfiguration.addNodeBuilder(new VoronoiBorder3DShaderNodeBuilder());

        // Shape
        ModelShaderConfiguration.addNodeBuilder(new DotShapeShaderNodeBuilder());
        ModelShaderConfiguration.addNodeBuilder(new CheckerboardShapeShaderNodeBuilder());
        ModelShaderConfiguration.addNodeBuilder(new EllipseShapeShaderNodeBuilder());
        ModelShaderConfiguration.addNodeBuilder(new RectangleShapeShaderNodeBuilder());
        ModelShaderConfiguration.addNodeBuilder(new StarShapeShaderNodeBuilder());

        // Provided
        ModelShaderConfiguration.addNodeBuilder(new TimeGraphShaderNodeBuilder());
        ModelShaderConfiguration.addNodeBuilder(new CameraPositionShaderNodeBuilder());
        ModelShaderConfiguration.addNodeBuilder(new CameraDirectionShaderNodeBuilder());
        ModelShaderConfiguration.addNodeBuilder(new CameraViewportSizeShaderNodeBuilder());
        ModelShaderConfiguration.addNodeBuilder(new FragmentCoordinateShaderNodeBuilder());
        ModelShaderConfiguration.addNodeBuilder(new SceneDepthShaderNodeBuilder());
        ModelShaderConfiguration.addNodeBuilder(new SceneColorShaderNodeBuilder());
        ModelShaderConfiguration.addNodeBuilder(new ScreenPositionShaderNodeBuilder());
        ModelShaderConfiguration.addNodeBuilder(new PixelSizeShaderNodeBuilder());
        ModelShaderConfiguration.addNodeBuilder(new ViewportSizeShaderNodeBuilder());

        // Sprite
        ModelShaderConfiguration.addNodeBuilder(new BillboardSpriteShaderNodeBuilder());
        ModelShaderConfiguration.addNodeBuilder(new ScreenSpriteShaderNodeBuilder());

        // Values
        ModelShaderConfiguration.addNodeBuilder(new ValueBooleanShaderNodeBuilder());
        ModelShaderConfiguration.addNodeBuilder(new ValueColorShaderNodeBuilder());
        ModelShaderConfiguration.addNodeBuilder(new ValueFloatShaderNodeBuilder());
        ModelShaderConfiguration.addNodeBuilder(new ValueVector2ShaderNodeBuilder());
        ModelShaderConfiguration.addNodeBuilder(new ValueVector3ShaderNodeBuilder());

        ModelShaderConfiguration.addPropertyProducer(new FloatShaderPropertyProducer());
        ModelShaderConfiguration.addPropertyProducer(new ColorShaderPropertyProducer());
        ModelShaderConfiguration.addPropertyProducer(new Vector2ShaderPropertyProducer());
        ModelShaderConfiguration.addPropertyProducer(new Vector3ShaderPropertyProducer());
        ModelShaderConfiguration.addPropertyProducer(new Matrix4ShaderPropertyProducer());
        ModelShaderConfiguration.addPropertyProducer(new TextureShaderPropertyProducer() {
            @Override
            protected TextureRegion getDefaultTextureRegion() {
                return null;
            }
        });

        // End
        ModelShaderConfiguration.addNodeBuilder(new EndModelShaderNodeBuilder());

        // Provided
        ModelShaderConfiguration.addNodeBuilder(new WorldPositionShaderNodeBuilder());
        ModelShaderConfiguration.addNodeBuilder(new ObjectToWorldShaderNodeBuilder());
        ModelShaderConfiguration.addNodeBuilder(new ObjectNormalToWorldShaderNodeBuilder());
        ModelShaderConfiguration.addNodeBuilder(new ModelFragmentCoordinateShaderNodeBuilder());
        ModelShaderConfiguration.addNodeBuilder(new InstanceIdShaderNodeBuilder());

        RendererPipelineConfiguration.register(new ModelShaderRendererPipelineNodeProducer());
    }

    @Override
    public void dispose() {

    }
}
