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

public class DecoratedPotItemRenderer extends BlockEntityWithoutLevelRenderer
{
	private final BlockEntityRenderDispatcher blockEntityRenderDispatcher;
	   
	public DecoratedPotItemRenderer(BlockEntityRenderDispatcher p_172550_, EntityModelSet p_172551_)
	{
		super(p_172550_, p_172551_);
		this.blockEntityRenderDispatcher = p_172550_;
	}
	
	@Override
	public void renderByItem(ItemStack p_108830_, TransformType p_108831_, PoseStack p_108832_, MultiBufferSource p_108833_, int p_108834_, int p_108835_)
	{
		p_108832_.pushPose();
		DecoratedPotBlockEntity decoratedPot = new DecoratedPotBlockEntity(BlockPos.ZERO, ArchaeologyBlocks.DECORATED_POT.get().defaultBlockState());
		decoratedPot.setFromItem(p_108830_);
		this.blockEntityRenderDispatcher.renderItem(decoratedPot, p_108832_, p_108833_, LightTexture.FULL_BLOCK, OverlayTexture.NO_OVERLAY);
		p_108832_.popPose();
	}
}
