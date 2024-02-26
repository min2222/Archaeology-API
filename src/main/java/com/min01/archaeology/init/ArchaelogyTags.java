package com.min01.archaeology.init;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;

public class ArchaelogyTags {
	public static final TagKey<Item> BREAKS_DECORATED_POTS = itemTag("breaks_decorated_pots");
	public static final TagKey<Item> DECORATED_POT_INGREDIENTS = itemTag("decorated_pot_ingredients");
	public static final TagKey<Item> DECORATED_POT_SHERDS = itemTag("decorated_pot_sherds");
	public static final TagKey<EntityType<?>> IMPACT_PROJECTILES = entityTypeTag("impact_projectiles");
	
	private static TagKey<Item> itemTag(final String path) {
		return TagKey.create(Registry.ITEM_REGISTRY, new ResourceLocation(path));
	}

	private static TagKey<EntityType<?>> entityTypeTag(final String path) {
		return TagKey.create(Registry.ENTITY_TYPE_REGISTRY, new ResourceLocation(path));
	}
}
