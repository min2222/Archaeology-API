package com.min01.archaeology.client.renderer;

import com.min01.archaeology.blockentity.DecoratedPotBlockEntity;
import com.min01.archaeology.init.ArchaeologyBlocks;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.model.ItemTransforms.TransformType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderDispatcher;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

public class DecoratedPotItemRenderer extends BlockEntityWithoutLevelRenderer {
	private final BlockEntityRenderDispatcher dispatcher;
	   
	public DecoratedPotItemRenderer(final BlockEntityRenderDispatcher dispatcher, final EntityModelSet modelSet) {
		super(dispatcher, modelSet);
		this.dispatcher = dispatcher;
	}
	
	@Override
	public void renderByItem(final @NotNull ItemStack stack, final @NotNull TransformType transformType, final PoseStack pose, final @NotNull MultiBufferSource buffer, int packedLight, int packedOverlay) {
		pose.pushPose();
		DecoratedPotBlockEntity decoratedPot = new DecoratedPotBlockEntity(BlockPos.ZERO, ArchaeologyBlocks.DECORATED_POT.get().defaultBlockState());
		decoratedPot.setFromItem(stack);
		this.dispatcher.renderItem(decoratedPot, pose, buffer, LightTexture.FULL_BLOCK, OverlayTexture.NO_OVERLAY);
		pose.popPose();
	}
}
