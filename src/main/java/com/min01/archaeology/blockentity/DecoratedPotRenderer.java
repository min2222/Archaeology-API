package com.min01.archaeology.blockentity;

import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

import javax.annotation.Nullable;

import org.jetbrains.annotations.NotNull;

import com.min01.archaeology.misc.DecoratedPotPatterns;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Quaternion;
import com.mojang.math.Vector3f;

import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.resources.model.Material;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;

public class DecoratedPotRenderer implements BlockEntityRenderer<DecoratedPotBlockEntity> {
    private static final String NECK = "neck";
    private static final String FRONT = "front";
    private static final String BACK = "back";
    private static final String LEFT = "left";
    private static final String RIGHT = "right";
    private static final String TOP = "top";
    private static final String BOTTOM = "bottom";
    private final ModelPart neck;
    private final ModelPart frontSide;
    private final ModelPart backSide;
    private final ModelPart leftSide;
    private final ModelPart rightSide;
    private final ModelPart top;
    private final ModelPart bottom;
    private final Material baseMaterial = Objects.requireNonNull(getDecoratedPotMaterial(DecoratedPotPatterns.BASE));
    public static final ModelLayerLocation DECORATED_POT_BASE = new ModelLayerLocation(new ResourceLocation("decorated_pot_base"), "main");
    public static final ModelLayerLocation DECORATED_POT_SIDES = new ModelLayerLocation(new ResourceLocation("decorated_pot_sides"), "main");
    public static final ResourceLocation DECORATED_POT_SHEET = InventoryMenu.BLOCK_ATLAS;
    public static final Map<ResourceKey<String>, Material> DECORATED_POT_MATERIALS = DecoratedPotPatterns.bootstrap().stream().collect(Collectors.toMap(Function.identity(), DecoratedPotRenderer::createDecoratedPotMaterial));
    private static final float WOBBLE_AMPLITUDE = 0.125F;

    public DecoratedPotRenderer(final BlockEntityRendererProvider.Context context) {
        ModelPart base = context.bakeLayer(DECORATED_POT_BASE);
        this.neck = base.getChild(NECK);
        this.top = base.getChild(TOP);
        this.bottom = base.getChild(BOTTOM);

        ModelPart sides = context.bakeLayer(DECORATED_POT_SIDES);
        this.frontSide = sides.getChild(FRONT);
        this.backSide = sides.getChild(BACK);
        this.leftSide = sides.getChild(LEFT);
        this.rightSide = sides.getChild(RIGHT);
    }

    public static LayerDefinition createBaseLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();
        CubeDeformation cubedeformation = new CubeDeformation(0.2F);
        CubeDeformation cubedeformation1 = new CubeDeformation(-0.1F);
        partdefinition.addOrReplaceChild(NECK,
                CubeListBuilder.create()
                        .texOffs(0, 0)
                        .addBox(4.0F, 17.0F, 4.0F, 8.0F, 3.0F, 8.0F, cubedeformation1)
                        .texOffs(0, 5)
                        .addBox(5.0F, 20.0F, 5.0F, 6.0F, 1.0F, 6.0F, cubedeformation),
                PartPose.offsetAndRotation(0.0F, 37.0F, 16.0F, (float) Math.PI, 0.0F, 0.0F));
        CubeListBuilder cubelistbuilder = CubeListBuilder.create().texOffs(-14, 13).addBox(0.0F, 0.0F, 0.0F, 14.0F, 0.0F, 14.0F);
        partdefinition.addOrReplaceChild(TOP, cubelistbuilder, PartPose.offsetAndRotation(1.0F, 16.0F, 1.0F, 0.0F, 0.0F, 0.0F));
        partdefinition.addOrReplaceChild(BOTTOM, cubelistbuilder, PartPose.offsetAndRotation(1.0F, 0.0F, 1.0F, 0.0F, 0.0F, 0.0F));
        return LayerDefinition.create(meshdefinition, 32, 32);
    }

    public static LayerDefinition createSidesLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();
        CubeListBuilder cubelistbuilder = CubeListBuilder.create().texOffs(1, 0).addBox(0.0F, 0.0F, 0.0F, 14.0F, 16.0F, 0.0F);
        partdefinition.addOrReplaceChild(BACK, cubelistbuilder, PartPose.offsetAndRotation(15.0F, 16.0F, 1.0F, 0.0F, 0.0F, (float) Math.PI));
        partdefinition.addOrReplaceChild(LEFT, cubelistbuilder, PartPose.offsetAndRotation(1.0F, 16.0F, 1.0F, 0.0F, (-(float) Math.PI / 2F), (float) Math.PI));
        partdefinition.addOrReplaceChild(RIGHT, cubelistbuilder, PartPose.offsetAndRotation(15.0F, 16.0F, 15.0F, 0.0F, ((float) Math.PI / 2F), (float) Math.PI));
        partdefinition.addOrReplaceChild(FRONT, cubelistbuilder, PartPose.offsetAndRotation(1.0F, 16.0F, 15.0F, (float) Math.PI, 0.0F, 0.0F));
        return LayerDefinition.create(meshdefinition, 16, 16);
    }

    @Nullable
    private static Material getMaterial(final Item item) {
        Material material = getDecoratedPotMaterial(DecoratedPotPatterns.getResourceKey(item));
        if (material == null) {
            material = getDecoratedPotMaterial(DecoratedPotPatterns.getResourceKey(Items.BRICK));
        }

        return material;
    }

    private static Material createDecoratedPotMaterial(final ResourceKey<String> materialKey) {
        return new Material(DECORATED_POT_SHEET, DecoratedPotPatterns.location(materialKey));
    }

    @Nullable
    public static Material getDecoratedPotMaterial(final @Nullable ResourceKey<String> materialKey) {
        return materialKey == null ? null : DECORATED_POT_MATERIALS.get(materialKey);
    }

    public void render(final DecoratedPotBlockEntity potEntity, float partialTick, final PoseStack pose, @NotNull final MultiBufferSource buffer, int packedLight, int packedOverlay) {
        pose.pushPose();
        Direction direction = potEntity.getDirection();
        rotateAround(pose, Vector3f.YP.rotationDegrees(180 - direction.toYRot()));
        DecoratedPotBlockEntity.WobbleStyle wobbleStyle = potEntity.lastWobbleStyle;

        if (wobbleStyle != null && potEntity.getLevel() != null) {
            float g = ((potEntity.getLevel().getGameTime() - potEntity.wobbleStartedAtTick) + partialTick) / wobbleStyle.duration;

            if (g >= 0 && g < 1) {
                if (wobbleStyle == DecoratedPotBlockEntity.WobbleStyle.POSITIVE) {
                    float h = 0.015625f;
                    float k = g * (float) (Math.PI * 2);
                    float l = -1.5F * (Mth.cos(k) + 0.5F) * Mth.sin(k / 2.0F);
                    rotateAround(pose, Vector3f.XP.rotation(l * h));
                    float m = Mth.sin(k);
                    rotateAround(pose, Vector3f.ZP.rotation(m * h));
                } else {
                    float h = Mth.sign(-g * 3 * Math.PI) * WOBBLE_AMPLITUDE;
                    float k = 1 - g;
                    rotateAround(pose, Vector3f.YP.rotation(h * k));
                }
            }
        }

        VertexConsumer vertexconsumer = this.baseMaterial.buffer(buffer, RenderType::entitySolid);
        this.neck.render(pose, vertexconsumer, packedLight, packedOverlay);
        this.top.render(pose, vertexconsumer, packedLight, packedOverlay);
        this.bottom.render(pose, vertexconsumer, packedLight, packedOverlay);
        DecoratedPotBlockEntity.Decorations decoratedpotblockentity$decorations = potEntity.getDecorations();
        this.renderSide(this.frontSide, pose, buffer, packedLight, packedOverlay, getMaterial(decoratedpotblockentity$decorations.front()));
        this.renderSide(this.backSide, pose, buffer, packedLight, packedOverlay, getMaterial(decoratedpotblockentity$decorations.back()));
        this.renderSide(this.leftSide, pose, buffer, packedLight, packedOverlay, getMaterial(decoratedpotblockentity$decorations.left()));
        this.renderSide(this.rightSide, pose, buffer, packedLight, packedOverlay, getMaterial(decoratedpotblockentity$decorations.right()));
        pose.popPose();
    }

    private void rotateAround(final PoseStack pose, final Quaternion quaternion) {
        pose.translate(0.5, 0, 0.5);
        pose.mulPose(quaternion);
        pose.translate(-0.5, 0, -0.5);
    }

    private void renderSide(final ModelPart modelPart, final PoseStack pose, final MultiBufferSource buffer, int packedLight, int packedOverlay, @Nullable Material material) {
        if (material == null) {
            material = getMaterial(Items.BRICK);
        }

        if (material != null) {
            modelPart.render(pose, material.buffer(buffer, RenderType::entitySolid), packedLight, packedOverlay);
        }
    }
}