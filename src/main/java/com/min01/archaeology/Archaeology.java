package com.min01.archaeology;

import com.min01.archaeology.init.ArchaeologyBlockEntityType;
import com.min01.archaeology.init.ArchaeologyBlocks;
import com.min01.archaeology.init.ArchaeologyItems;
import com.min01.archaeology.init.ArchaeologyRecipeSerializer;
import com.min01.archaeology.init.ArchaeologySounds;
import com.min01.archaeology.init.ArchaeologyStructureProcessor;

import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(Archaeology.MODID)
public class Archaeology
{
	public static final String MODID = "archaeology";
	public static final String MC_ID = "minecraft";
	public static IEventBus MOD_EVENT_BUS;
	
	public Archaeology() 
	{
		MOD_EVENT_BUS = FMLJavaModLoadingContext.get().getModEventBus();
		ArchaeologyItems.ITEMS.register(MOD_EVENT_BUS);
		ArchaeologySounds.SOUND_EVENTS.register(MOD_EVENT_BUS);
		ArchaeologyBlocks.BLOCKS.register(MOD_EVENT_BUS);
		ArchaeologyBlockEntityType.BLOCK_ENTITIES.register(MOD_EVENT_BUS);
		ArchaeologyRecipeSerializer.RECIPE_SERIALIZERS.register(MOD_EVENT_BUS);
		ArchaeologyStructureProcessor.STRUCTURE_PROCESSOR.register(MOD_EVENT_BUS);
	}
}
