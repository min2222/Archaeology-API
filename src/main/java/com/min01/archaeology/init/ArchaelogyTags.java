package com.min01.archaeology.init;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

public class ArchaelogyTags {
	public static final TagKey<Block> TRAIL_RUINS_REPLACEABLE = block("trail_ruins_replaceable");

	public static final TagKey<Item> BREAKS_DECORATED_POTS = item("breaks_decorated_pots");
	public static final TagKey<Item> DECORATED_POT_INGREDIENTS = item("decorated_pot_ingredients");
	public static final TagKey<Item> DECORATED_POT_SHERDS = item("decorated_pot_sherds");

	public static final TagKey<EntityType<?>> IMPACT_PROJECTILES = entityType("impact_projectiles");

	@SuppressWarnings("SameParameterValue")
	private static TagKey<Block> block(final String path) {
		return TagKey.create(Registry.BLOCK_REGISTRY, new ResourceLocation(path));
	}

	@SuppressWarnings("SameParameterValue")
	private static TagKey<Item> item(final String path) {
		return TagKey.create(Registry.ITEM_REGISTRY, new ResourceLocation(path));
	}

	@SuppressWarnings("SameParameterValue")
	private static TagKey<EntityType<?>> entityType(final String path) {
		return TagKey.create(Registry.ENTITY_TYPE_REGISTRY, new ResourceLocation(path));
	}
}
