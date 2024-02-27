package com.min01.archaeology;

import com.min01.archaeology.init.ArchaeologyBlockEntityType;
import com.min01.archaeology.init.ArchaeologyBlocks;
import com.min01.archaeology.init.ArchaeologyItems;
import com.min01.archaeology.init.ArchaeologyParticleTypes;
import com.min01.archaeology.init.ArchaeologyRecipeSerializer;
import com.min01.archaeology.init.ArchaeologySounds;
import com.min01.archaeology.init.ArchaeologyStructureProcessor;

import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(Archaeology.MODID)
public class Archaeology {
	public static final String MODID = "archaeology";
	public static final String MC_ID = "minecraft";

	public Archaeology() {
		IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
		ArchaeologyItems.ITEMS.register(modEventBus);
		ArchaeologySounds.SOUND_EVENTS.register(modEventBus);
		ArchaeologyBlocks.BLOCKS.register(modEventBus);
		ArchaeologyBlockEntityType.BLOCK_ENTITIES.register(modEventBus);
		ArchaeologyRecipeSerializer.RECIPE_SERIALIZERS.register(modEventBus);
		ArchaeologyStructureProcessor.STRUCTURE_PROCESSOR.register(modEventBus);
		ArchaeologyParticleTypes.PARTICLES.register(modEventBus);
	}
}
