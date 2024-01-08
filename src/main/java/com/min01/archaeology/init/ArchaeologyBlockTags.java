package com.min01.archaeology.init;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;

public class ArchaeologyBlockTags 
{
	public static final TagKey<Block> TRAIL_RUINS_REPLACEABLE = createBlockTagKey("trail_ruins_replaceable");
	
	private static TagKey<Block> createBlockTagKey(String p_203849_) 
	{
		return TagKey.create(Registry.BLOCK_REGISTRY, new ResourceLocation(p_203849_));
	}
}
