package com.min01.archaeology.blockentity;

import org.jetbrains.annotations.NotNull;

import com.min01.archaeology.block.BrushableBlock;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Vector3f;

import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemStack;

public class BrushableBlockRenderer implements BlockEntityRenderer<BrushableBlockEntity> {
    private final ItemRenderer itemRenderer;

    public BrushableBlockRenderer(final BlockEntityRendererProvider.Context context) {
        this.itemRenderer = context.getItemRenderer();
    }

    public void render(final BrushableBlockEntity brushableBlock, float partialTick, final @NotNull PoseStack pose, final @NotNull MultiBufferSource buffer, int packedLight, int packedOverlay) {
        if (brushableBlock.getLevel() != null) {
            int dusted = brushableBlock.getBlockState().getValue(BrushableBlock.DUSTED);

            if (dusted > 0) {
                Direction direction = brushableBlock.getHitDirection();

                if (direction != null) {
                    ItemStack stack = brushableBlock.getItem();

                    if (!stack.isEmpty()) {
                        pose.pushPose();
                        pose.translate(0.0F, 0.5F, 0.0F);
                        float[] translations = translations(direction, dusted);
                        pose.translate(translations[0], translations[1], translations[2]);
                        pose.mulPose(Vector3f.YP.rotationDegrees(75.0F));
                        boolean flag = direction == Direction.EAST || direction == Direction.WEST;
                        pose.mulPose(Vector3f.YP.rotationDegrees((float) ((flag ? 90 : 0) + 11)));
                        pose.scale(0.5F, 0.5F, 0.5F);
                        int j = LevelRenderer.getLightColor(brushableBlock.getLevel(), brushableBlock.getBlockState(), brushableBlock.getBlockPos().relative(direction));
                        itemRenderer.renderStatic(stack, ItemTransforms.TransformType.FIXED, j, OverlayTexture.NO_OVERLAY, pose, buffer, 0);
                        pose.popPose();
                    }
                }
            }
        }
    }

    private float[] translations(final Direction direction, int dusted) {
        float[] translations = new float[]{0.5F, 0.0F, 0.5F};
        float f = (float) dusted / 10 * 0.75F;

        switch (direction) {
            case EAST:
                translations[0] = 0.73F + f;
                break;
            case WEST:
                translations[0] = 0.25F - f;
                break;
            case UP:
                translations[1] = 0.25F + f;
                break;
            case DOWN:
                translations[1] = -0.23F - f;
                break;
            case NORTH:
                translations[2] = 0.25F - f;
                break;
            case SOUTH:
                translations[2] = 0.73F + f;
        }

        return translations;
    }
}