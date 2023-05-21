package com.gempukku.libgdx.graph.shader;

import com.badlogic.gdx.assets.loaders.FileHandleResolver;
import com.gempukku.libgdx.graph.GraphTypeRegistry;
import com.gempukku.libgdx.graph.plugin.RuntimePluginRegistry;
import com.gempukku.libgdx.graph.shader.common.sprite.BillboardSpriteShaderNodeConfiguration;
import com.gempukku.libgdx.graph.shader.common.sprite.ScreenSpriteShaderNodeConfiguration;
import com.gempukku.libgdx.graph.shader.config.common.effect.FresnelEffectShaderNodeConfiguration;
import com.gempukku.libgdx.graph.shader.config.common.effect.IntensityShaderNodeConfiguration;
import com.gempukku.libgdx.graph.shader.config.common.math.arithmetic.*;
import com.gempukku.libgdx.graph.shader.config.common.math.common.*;
import com.gempukku.libgdx.graph.shader.config.common.math.exponential.*;
import com.gempukku.libgdx.graph.shader.config.common.math.geometric.*;
import com.gempukku.libgdx.graph.shader.config.common.math.trigonometry.*;
import com.gempukku.libgdx.graph.shader.config.common.math.utility.DistanceFromPlaneShaderNodeConfiguration;
import com.gempukku.libgdx.graph.shader.config.common.math.value.MergeShaderNodeConfiguration;
import com.gempukku.libgdx.graph.shader.config.common.math.value.RemapShaderNodeConfiguration;
import com.gempukku.libgdx.graph.shader.config.common.math.value.SplitShaderNodeConfiguration;
import com.gempukku.libgdx.graph.shader.config.common.noise.*;
import com.gempukku.libgdx.graph.shader.config.common.provided.*;
import com.gempukku.libgdx.graph.shader.config.common.shape.*;
import com.gempukku.libgdx.graph.shader.config.common.texture.BorderDetectionShaderNodeConfiguration;
import com.gempukku.libgdx.graph.shader.config.common.texture.Sampler2DShaderNodeConfiguration;
import com.gempukku.libgdx.graph.shader.config.common.texture.UVTilingAndOffsetShaderNodeConfiguration;
import com.gempukku.libgdx.graph.shader.config.common.value.*;
import com.gempukku.libgdx.graph.shader.config.provided.*;
import com.gempukku.libgdx.graph.shader.field.ShaderFieldType;
import com.gempukku.libgdx.graph.shader.preview.ScreenPreviewShaderGraphNodeEditorProducer;
import com.gempukku.libgdx.graph.shader.producer.EndModelShaderEditorProducer;
import com.gempukku.libgdx.graph.shader.producer.effect.DitherColorShaderEditorProducer;
import com.gempukku.libgdx.graph.shader.producer.effect.DitherShaderEditorProducer;
import com.gempukku.libgdx.graph.shader.producer.effect.GradientShaderEditorProducer;
import com.gempukku.libgdx.graph.shader.producer.math.common.ConditionalShaderEditorProducer;
import com.gempukku.libgdx.graph.shader.producer.math.value.RemapValueShaderEditorProducer;
import com.gempukku.libgdx.graph.shader.producer.math.value.RemapVectorShaderEditorProducer;
import com.gempukku.libgdx.graph.shader.producer.property.*;
import com.gempukku.libgdx.graph.shader.producer.provided.SceneColorShaderEditorProducer;
import com.gempukku.libgdx.graph.shader.producer.provided.TimeShaderEditorProducer;
import com.gempukku.libgdx.graph.shader.producer.texture.UVFlipbookShaderEditorProducer;
import com.gempukku.libgdx.graph.ui.UIGdxGraphPlugin;
import com.gempukku.libgdx.graph.ui.graph.FileGraphTemplate;
import com.gempukku.libgdx.graph.ui.graph.GdxGraphNodeEditorProducer;
import com.gempukku.libgdx.graph.ui.pipeline.value.*;
import com.kotcrab.vis.ui.VisUI;

public class UIModelsPlugin implements UIGdxGraphPlugin {
    @Override
    public void initialize(FileHandleResolver assetResolver) {
        // Register graph type
        UIModelShaderGraphType graphType = new UIModelShaderGraphType(VisUI.getSkin().getDrawable("graph-model-shader-icon"));
        GraphTypeRegistry.registerType(graphType);

        // Register node editors
        UIModelShaderConfiguration.register(new EndModelShaderEditorProducer());

        UIModelShaderConfiguration.register(new ScreenPreviewShaderGraphNodeEditorProducer(new Sampler2DShaderNodeConfiguration(), "color", 150, 150));
        UIModelShaderConfiguration.register(new UVFlipbookShaderEditorProducer());
        UIModelShaderConfiguration.register(new ScreenPreviewShaderGraphNodeEditorProducer(new UVTilingAndOffsetShaderNodeConfiguration(), "output", 150, 150));
        UIModelShaderConfiguration.register(new GdxGraphNodeEditorProducer(new BorderDetectionShaderNodeConfiguration()));

        UIModelShaderConfiguration.register(new ScreenPreviewShaderGraphNodeEditorProducer(new AddShaderNodeConfiguration(), "output", 150, 150));
        UIModelShaderConfiguration.register(new ScreenPreviewShaderGraphNodeEditorProducer(new SubtractShaderNodeConfiguration(), "output", 150, 150));
        UIModelShaderConfiguration.register(new ScreenPreviewShaderGraphNodeEditorProducer(new OneMinusShaderNodeConfiguration(), "output", 150, 150));
        UIModelShaderConfiguration.register(new ScreenPreviewShaderGraphNodeEditorProducer(new MultiplyShaderNodeConfiguration(), "output", 150, 150));
        UIModelShaderConfiguration.register(new ScreenPreviewShaderGraphNodeEditorProducer(new DivideShaderNodeConfiguration(), "output", 150, 150));
        UIModelShaderConfiguration.register(new ScreenPreviewShaderGraphNodeEditorProducer(new ReciprocalShaderNodeConfiguration(), "output", 150, 150));

        UIModelShaderConfiguration.register(new ScreenPreviewShaderGraphNodeEditorProducer(new PowerShaderNodeConfiguration(), "output", 150, 150));
        UIModelShaderConfiguration.register(new ScreenPreviewShaderGraphNodeEditorProducer(new ExponentialShaderNodeConfiguration(), "output", 150, 150));
        UIModelShaderConfiguration.register(new ScreenPreviewShaderGraphNodeEditorProducer(new ExponentialBase2ShaderNodeConfiguration(), "output", 150, 150));
        UIModelShaderConfiguration.register(new ScreenPreviewShaderGraphNodeEditorProducer(new NaturalLogarithmShaderNodeConfiguration(), "output", 150, 150));
        UIModelShaderConfiguration.register(new ScreenPreviewShaderGraphNodeEditorProducer(new LogarithmBase2ShaderNodeConfiguration(), "output", 150, 150));
        UIModelShaderConfiguration.register(new ScreenPreviewShaderGraphNodeEditorProducer(new SquareRootShaderNodeConfiguration(), "output", 150, 150));
        UIModelShaderConfiguration.register(new ScreenPreviewShaderGraphNodeEditorProducer(new InverseSquareRootShaderNodeConfiguration(), "output", 150, 150));

        UIModelShaderConfiguration.register(new ScreenPreviewShaderGraphNodeEditorProducer(new SinShaderNodeConfiguration(), "output", 150, 150));
        UIModelShaderConfiguration.register(new ScreenPreviewShaderGraphNodeEditorProducer(new CosShaderNodeConfiguration(), "output", 150, 150));
        UIModelShaderConfiguration.register(new ScreenPreviewShaderGraphNodeEditorProducer(new TanShaderNodeConfiguration(), "output", 150, 150));
        UIModelShaderConfiguration.register(new ScreenPreviewShaderGraphNodeEditorProducer(new ArcsinShaderNodeConfiguration(), "output", 150, 150));
        UIModelShaderConfiguration.register(new ScreenPreviewShaderGraphNodeEditorProducer(new ArccosShaderNodeConfiguration(), "output", 150, 150));
        UIModelShaderConfiguration.register(new ScreenPreviewShaderGraphNodeEditorProducer(new ArctanShaderNodeConfiguration(), "output", 150, 150));
        UIModelShaderConfiguration.register(new ScreenPreviewShaderGraphNodeEditorProducer(new Arctan2ShaderNodeConfiguration(), "output", 150, 150));
        UIModelShaderConfiguration.register(new ScreenPreviewShaderGraphNodeEditorProducer(new RadiansShaderNodeConfiguration(), "output", 150, 150));
        UIModelShaderConfiguration.register(new ScreenPreviewShaderGraphNodeEditorProducer(new DegreesShaderNodeConfiguration(), "output", 150, 150));

        UIModelShaderConfiguration.register(new ScreenPreviewShaderGraphNodeEditorProducer(new AbsShaderNodeConfiguration(), "output", 150, 150));
        UIModelShaderConfiguration.register(new ScreenPreviewShaderGraphNodeEditorProducer(new SignShaderNodeConfiguration(), "output", 150, 150));
        UIModelShaderConfiguration.register(new ScreenPreviewShaderGraphNodeEditorProducer(new FloorShaderNodeConfiguration(), "output", 150, 150));
        UIModelShaderConfiguration.register(new ScreenPreviewShaderGraphNodeEditorProducer(new CeilingShaderNodeConfiguration(), "output", 150, 150));
        UIModelShaderConfiguration.register(new ScreenPreviewShaderGraphNodeEditorProducer(new FractionalPartShaderNodeConfiguration(), "output", 150, 150));
        UIModelShaderConfiguration.register(new ScreenPreviewShaderGraphNodeEditorProducer(new ModuloShaderNodeConfiguration(), "output", 150, 150));
        UIModelShaderConfiguration.register(new ScreenPreviewShaderGraphNodeEditorProducer(new MinimumShaderNodeConfiguration(), "output", 150, 150));
        UIModelShaderConfiguration.register(new ScreenPreviewShaderGraphNodeEditorProducer(new MaximumShaderNodeConfiguration(), "output", 150, 150));
        UIModelShaderConfiguration.register(new ScreenPreviewShaderGraphNodeEditorProducer(new ClampShaderNodeConfiguration(), "output", 150, 150));
        UIModelShaderConfiguration.register(new ScreenPreviewShaderGraphNodeEditorProducer(new SaturateShaderNodeConfiguration(), "output", 150, 150));
        UIModelShaderConfiguration.register(new ScreenPreviewShaderGraphNodeEditorProducer(new LerpShaderNodeConfiguration(), "output", 150, 150));
        UIModelShaderConfiguration.register(new ConditionalShaderEditorProducer());
        UIModelShaderConfiguration.register(new ScreenPreviewShaderGraphNodeEditorProducer(new StepShaderNodeConfiguration(), "output", 150, 150));
        UIModelShaderConfiguration.register(new ScreenPreviewShaderGraphNodeEditorProducer(new SmoothstepShaderNodeConfiguration(), "output", 150, 150));

        UIModelShaderConfiguration.register(new ScreenPreviewShaderGraphNodeEditorProducer(new LengthShaderNodeConfiguration(), "output", 150, 150));
        UIModelShaderConfiguration.register(new ScreenPreviewShaderGraphNodeEditorProducer(new DistanceShaderNodeConfiguration(), "output", 150, 150));
        UIModelShaderConfiguration.register(new ScreenPreviewShaderGraphNodeEditorProducer(new DotProductShaderNodeConfiguration(), "output", 150, 150));
        UIModelShaderConfiguration.register(new ScreenPreviewShaderGraphNodeEditorProducer(new CrossProductShaderNodeConfiguration(), "output", 150, 150));
        UIModelShaderConfiguration.register(new ScreenPreviewShaderGraphNodeEditorProducer(new NormalizeShaderNodeConfiguration(), "output", 150, 150));

        UIModelShaderConfiguration.register(new ScreenPreviewShaderGraphNodeEditorProducer(new DistanceFromPlaneShaderNodeConfiguration(), "output", 150, 150));

        UIModelShaderConfiguration.register(new GdxGraphNodeEditorProducer(new SplitShaderNodeConfiguration()));
        UIModelShaderConfiguration.register(new ScreenPreviewShaderGraphNodeEditorProducer(new MergeShaderNodeConfiguration(), "color", 150, 150));
        UIModelShaderConfiguration.register(new ScreenPreviewShaderGraphNodeEditorProducer(new RemapShaderNodeConfiguration(), "output", 150, 150));
        UIModelShaderConfiguration.register(new RemapVectorShaderEditorProducer());
        UIModelShaderConfiguration.register(new RemapValueShaderEditorProducer());

        UIModelShaderConfiguration.register(new DitherShaderEditorProducer());
        UIModelShaderConfiguration.register(new DitherColorShaderEditorProducer());
        UIModelShaderConfiguration.register(new ScreenPreviewShaderGraphNodeEditorProducer(new IntensityShaderNodeConfiguration(), "output", 150, 150));
        UIModelShaderConfiguration.register(new ScreenPreviewShaderGraphNodeEditorProducer(new FresnelEffectShaderNodeConfiguration(), "output", 150, 150));
        UIModelShaderConfiguration.register(new GradientShaderEditorProducer());

        UIModelShaderConfiguration.register(new ScreenPreviewShaderGraphNodeEditorProducer(new SimplexNoise2DNodeConfiguration(), "output", 150, 150));
        UIModelShaderConfiguration.register(new ScreenPreviewShaderGraphNodeEditorProducer(new SimplexNoise3DNodeConfiguration(), "output", 150, 150));
        UIModelShaderConfiguration.register(new ScreenPreviewShaderGraphNodeEditorProducer(new PerlinNoise2DNodeConfiguration(), "output", 150, 150));
        UIModelShaderConfiguration.register(new ScreenPreviewShaderGraphNodeEditorProducer(new PerlinNoise3DNodeConfiguration(), "output", 150, 150));
        UIModelShaderConfiguration.register(new ScreenPreviewShaderGraphNodeEditorProducer(new VoronoiDistance2DNodeConfiguration(), "output", 150, 150));
        UIModelShaderConfiguration.register(new ScreenPreviewShaderGraphNodeEditorProducer(new VoronoiDistance3DNodeConfiguration(), "output", 150, 150));
        UIModelShaderConfiguration.register(new ScreenPreviewShaderGraphNodeEditorProducer(new VoronoiBorder2DNodeConfiguration(), "output", 150, 150));
        UIModelShaderConfiguration.register(new ScreenPreviewShaderGraphNodeEditorProducer(new VoronoiBorder3DNodeConfiguration(), "output", 150, 150));

        UIModelShaderConfiguration.register(new ScreenPreviewShaderGraphNodeEditorProducer(new DotShapeShaderNodeConfiguration(), "output", 150, 150));
        UIModelShaderConfiguration.register(new ScreenPreviewShaderGraphNodeEditorProducer(new CheckerboardShapeShaderNodeConfiguration(), "output", 150, 150));
        UIModelShaderConfiguration.register(new ScreenPreviewShaderGraphNodeEditorProducer(new EllipseShapeShaderNodeConfiguration(), "output", 150, 150));
        UIModelShaderConfiguration.register(new ScreenPreviewShaderGraphNodeEditorProducer(new RectangleShapeShaderNodeConfiguration(), "output", 150, 150));
        UIModelShaderConfiguration.register(new ScreenPreviewShaderGraphNodeEditorProducer(new StarShapeShaderNodeConfiguration(), "output", 150, 150));

        UIModelShaderConfiguration.register(new TimeShaderEditorProducer());
        UIModelShaderConfiguration.register(new GdxGraphNodeEditorProducer(new CameraPositionShaderNodeConfiguration()));
        UIModelShaderConfiguration.register(new GdxGraphNodeEditorProducer(new CameraDirectionShaderNodeConfiguration()));
        UIModelShaderConfiguration.register(new GdxGraphNodeEditorProducer(new CameraViewportSizeShaderNodeConfiguration()));
        UIModelShaderConfiguration.register(new GdxGraphNodeEditorProducer(new FragmentCoordinateShaderNodeConfiguration()));
        UIModelShaderConfiguration.register(new GdxGraphNodeEditorProducer(new SceneDepthShaderNodeConfiguration()));
        UIModelShaderConfiguration.register(new SceneColorShaderEditorProducer());
        UIModelShaderConfiguration.register(new GdxGraphNodeEditorProducer(new ScreenPositionShaderNodeConfiguration()));
        UIModelShaderConfiguration.register(new GdxGraphNodeEditorProducer(new PixelSizeShaderNodeConfiguration()));
        UIModelShaderConfiguration.register(new GdxGraphNodeEditorProducer(new ViewportSizeShaderNodeConfiguration()));

        UIModelShaderConfiguration.register(new GdxGraphNodeEditorProducer(new BillboardSpriteShaderNodeConfiguration()));
        UIModelShaderConfiguration.register(new GdxGraphNodeEditorProducer(new ScreenSpriteShaderNodeConfiguration()));

        UIModelShaderConfiguration.register(new ValueColorEditorProducer(new ValueColorShaderNodeConfiguration()));
        UIModelShaderConfiguration.register(new ValueFloatEditorProducer(new ValueFloatShaderNodeConfiguration()));
        UIModelShaderConfiguration.register(new ValueVector2EditorProducer(new ValueVector2ShaderNodeConfiguration()));
        UIModelShaderConfiguration.register(new ValueVector3EditorProducer(new ValueVector3ShaderNodeConfiguration()));
        UIModelShaderConfiguration.register(new ValueBooleanEditorProducer(new ValueBooleanShaderNodeConfiguration()));

        UIModelShaderConfiguration.register(new GdxGraphNodeEditorProducer(new WorldPositionShaderNodeConfiguration()));
        UIModelShaderConfiguration.register(new GdxGraphNodeEditorProducer(new ObjectToWorldShaderNodeConfiguration()));
        UIModelShaderConfiguration.register(new GdxGraphNodeEditorProducer(new ObjectNormalToWorldShaderNodeConfiguration()));
        UIModelShaderConfiguration.register(new GdxGraphNodeEditorProducer(new ModelFragmentCoordinateShaderNodeConfiguration()));
        UIModelShaderConfiguration.register(new GdxGraphNodeEditorProducer(new InstanceIdShaderNodeConfiguration()));

        UIModelShaderConfiguration.registerPropertyType(new ShaderPropertyFloatEditorDefinition());
        UIModelShaderConfiguration.registerPropertyType(new ShaderPropertyVector2EditorDefinition());
        UIModelShaderConfiguration.registerPropertyType(new ShaderPropertyVector3EditorDefinition());
        UIModelShaderConfiguration.registerPropertyType(new ShaderPropertyColorEditorDefinition());
        UIModelShaderConfiguration.registerPropertyType(new ShaderPropertyMatrix4EditorDefinition());
        UIModelShaderConfiguration.registerPropertyType(new ShaderPropertyTextureEditorDefinition());

        UIModelShaderConfiguration.registerPropertyFunction(ShaderFieldType.Vector2, "TexCoord0");

        UIModelShaderConfiguration.registerPropertyFunction(ShaderFieldType.Vector3, "Position");
        UIModelShaderConfiguration.registerPropertyFunction(ShaderFieldType.Vector3, "Normal");
        UIModelShaderConfiguration.registerPropertyFunction(ShaderFieldType.Vector3, "Bi-Normal");
        UIModelShaderConfiguration.registerPropertyFunction(ShaderFieldType.Vector3, "Tangent");

        UIModelShaderConfiguration.registerPropertyFunction(ShaderFieldType.Vector4, "Color");

        // Register runtime plugin
        RuntimePluginRegistry.register(ShaderPluginRuntimeInitializer.class);

        ModelsTemplateRegistry.register(
                new FileGraphTemplate(graphType, "Empty model shader", assetResolver.resolve("template/model/empty-model-shader.json")));
    }
}
