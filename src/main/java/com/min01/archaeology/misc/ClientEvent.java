package com.min01.archaeology.misc;

import com.min01.archaeology.blockentity.BrushableBlockRenderer;
import com.min01.archaeology.blockentity.DecoratedPotRenderer;
import com.min01.archaeology.init.ArchaeologyArmPose;
import com.min01.archaeology.init.ArchaeologyBlockEntityType;
import com.min01.archaeology.init.ArchaeologyItems;

import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ClientEvent {
    @SubscribeEvent
    public static void onClientSetup(final FMLClientSetupEvent event) {
        BlockEntityRenderers.register(ArchaeologyBlockEntityType.BRUSHABLE_BLOCK.get(), BrushableBlockRenderer::new);
        BlockEntityRenderers.register(ArchaeologyBlockEntityType.DECORATED_POT.get(), DecoratedPotRenderer::new);
    	ArchaeologyArmPose.BRUSH = HumanoidModel.ArmPose.create("BRUSH", false, new ArchaeologyArmPose());
    	ItemProperties.register(ArchaeologyItems.BRUSH.get(), new ResourceLocation("brushing"), (stack, level, entity, i) -> entity != null && entity.getUseItem() == stack ? (float) (entity.getUseItemRemainingTicks() % 10) / 10.0F : 0.0F);
    }
    
    @SubscribeEvent
    public static void onTextureStitchEvent(final TextureStitchEvent.Pre event) {
    	event.addSprite(DecoratedPotPatterns.location(DecoratedPotPatterns.BRICK)); 
    	event.addSprite(DecoratedPotPatterns.location(DecoratedPotPatterns.ANGLER));
    	event.addSprite(DecoratedPotPatterns.location(DecoratedPotPatterns.ARCHER));
    	event.addSprite(DecoratedPotPatterns.location(DecoratedPotPatterns.ARMS_UP));
    	event.addSprite(DecoratedPotPatterns.location(DecoratedPotPatterns.BLADE)); 
    	event.addSprite(DecoratedPotPatterns.location(DecoratedPotPatterns.BREWER));
    	event.addSprite(DecoratedPotPatterns.location(DecoratedPotPatterns.BURN));
    	event.addSprite(DecoratedPotPatterns.location(DecoratedPotPatterns.DANGER));
    	event.addSprite(DecoratedPotPatterns.location(DecoratedPotPatterns.EXPLORER));
    	event.addSprite(DecoratedPotPatterns.location(DecoratedPotPatterns.FRIEND));
    	event.addSprite(DecoratedPotPatterns.location(DecoratedPotPatterns.HEART));
    	event.addSprite(DecoratedPotPatterns.location(DecoratedPotPatterns.HEARTBREAK));
    	event.addSprite(DecoratedPotPatterns.location(DecoratedPotPatterns.HOWL));
    	event.addSprite(DecoratedPotPatterns.location(DecoratedPotPatterns.MINER)); 
    	event.addSprite(DecoratedPotPatterns.location(DecoratedPotPatterns.MOURNER));
    	event.addSprite(DecoratedPotPatterns.location(DecoratedPotPatterns.PLENTY));
    	event.addSprite(DecoratedPotPatterns.location(DecoratedPotPatterns.PRIZE));
    	event.addSprite(DecoratedPotPatterns.location(DecoratedPotPatterns.SHEAF));
    	event.addSprite(DecoratedPotPatterns.location(DecoratedPotPatterns.SHELTER));
    	event.addSprite(DecoratedPotPatterns.location(DecoratedPotPatterns.SKULL));
    	event.addSprite(DecoratedPotPatterns.location(DecoratedPotPatterns.SNORT));
    	event.addSprite(DecoratedPotPatterns.location(DecoratedPotPatterns.BASE));
    }
    
    @SubscribeEvent
    public static void registerLayerDefinitions(final EntityRenderersEvent.RegisterLayerDefinitions event) {
    	event.registerLayerDefinition(DecoratedPotRenderer.DECORATED_POT_BASE, DecoratedPotRenderer::createBaseLayer);
    	event.registerLayerDefinition(DecoratedPotRenderer.DECORATED_POT_SIDES, DecoratedPotRenderer::createSidesLayer);
    }
}
