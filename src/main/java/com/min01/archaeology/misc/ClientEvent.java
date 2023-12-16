package com.min01.archaeology.misc;

import com.min01.archaeology.Archaeology;
import com.min01.archaeology.blockentity.BrushableBlockRenderer;
import com.min01.archaeology.blockentity.DecoratedPotRenderer;
import com.min01.archaeology.init.ArchaeologyArmPose;
import com.min01.archaeology.init.ArchaeologyBlockEntityType;
import com.min01.archaeology.init.ArchaeologyItems;

import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

@Mod.EventBusSubscriber(value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD, modid = Archaeology.MODID)
public class ClientEvent 
{
    @SubscribeEvent
    public static void onClientSetup(FMLClientSetupEvent event)
    {
        BlockEntityRenderers.register(ArchaeologyBlockEntityType.BRUSHABLE_BLOCK.get(), BrushableBlockRenderer::new);
        BlockEntityRenderers.register(ArchaeologyBlockEntityType.DECORATED_POT.get(), DecoratedPotRenderer::new);
    	ArchaeologyArmPose.BRUSH = HumanoidModel.ArmPose.create("BRUSH", false, new ArchaeologyArmPose());
    	ItemProperties.register(ArchaeologyItems.BRUSH.get(), new ResourceLocation("brushing"), (p_272332_, p_272333_, p_272334_, p_272335_) ->
    	{
    		return p_272334_ != null && p_272334_.getUseItem() == p_272332_ ? (float)(p_272334_.getUseItemRemainingTicks() % 10) / 10.0F : 0.0F;
    	});
    }
    
    @SubscribeEvent
    public static void onTextureStitchEvent(TextureStitchEvent.Pre event)
    {
 	   for(ResourceKey<String> key : DecoratedPotPatterns.PATTERNS_REGISTRY)
 	   {
 		   event.addSprite(DecoratedPotPatterns.location(key));
 	   }
    }
    
    @SubscribeEvent
    public static void registerLayerDefinitions(EntityRenderersEvent.RegisterLayerDefinitions event)
    {
    	event.registerLayerDefinition(DecoratedPotRenderer.DECORATED_POT_BASE, DecoratedPotRenderer::createBaseLayer);
    	event.registerLayerDefinition(DecoratedPotRenderer.DECORATED_POT_SIDES, DecoratedPotRenderer::createSidesLayer);
    }
}
