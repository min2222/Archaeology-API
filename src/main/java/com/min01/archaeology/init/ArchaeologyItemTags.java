package com.min01.archaeology.init;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;

public class ArchaeologyItemTags
{
	public static final TagKey<Item> BREAKS_DECORATED_POTS = createItemTagKey("breaks_decorated_pots");
	public static final TagKey<Item> DECORATED_POT_INGREDIENTS = createItemTagKey("decorated_pot_ingredients");
	public static final TagKey<Item> DECORATED_POT_SHERDS = createItemTagKey("decorated_pot_sherds");
	
	private static TagKey<Item> createItemTagKey(String p_203849_) 
	{
		return TagKey.create(Registry.ITEM_REGISTRY, new ResourceLocation(p_203849_));
	}
}
