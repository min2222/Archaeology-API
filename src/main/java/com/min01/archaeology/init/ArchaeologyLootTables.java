package com.min01.archaeology.init;

import net.minecraft.resources.ResourceLocation;

public class ArchaeologyLootTables
{
	public static final ResourceLocation DESERT_WELL_ARCHAEOLOGY = register("archaeology/desert_well");
	public static final ResourceLocation DESERT_PYRAMID_ARCHAEOLOGY = register("archaeology/desert_pyramid");
	public static final ResourceLocation TRAIL_RUINS_ARCHAEOLOGY_COMMON = register("archaeology/trail_ruins_common");
	public static final ResourceLocation TRAIL_RUINS_ARCHAEOLOGY_RARE = register("archaeology/trail_ruins_rare");
	public static final ResourceLocation OCEAN_RUIN_WARM_ARCHAEOLOGY = register("archaeology/ocean_ruin_warm");
	public static final ResourceLocation OCEAN_RUIN_COLD_ARCHAEOLOGY = register("archaeology/ocean_ruin_cold");

	private static ResourceLocation register(String p_78768_)
	{
		return new ResourceLocation(p_78768_);
	}
}
