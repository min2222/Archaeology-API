package com.min01.archaeology.misc;

import java.util.Map;

import net.minecraft.resources.ResourceKey;

public class PotPattensRegistry {
	public static final Map<ResourceKey<String>, String> PATTERNS_REGISTRY = Map.ofEntries(
			Map.entry(DecoratedPotPatterns.BASE, DecoratedPotPatterns.BASE_NAME),
			Map.entry(DecoratedPotPatterns.BRICK, DecoratedPotPatterns.BRICK_NAME),
			Map.entry(DecoratedPotPatterns.ANGLER, DecoratedPotPatterns.ANGLER_NAME),
			Map.entry(DecoratedPotPatterns.ARCHER, DecoratedPotPatterns.ARCHER_NAME),
			Map.entry(DecoratedPotPatterns.ARMS_UP, DecoratedPotPatterns.ARMS_UP_NAME),
			Map.entry(DecoratedPotPatterns.BREWER, DecoratedPotPatterns.BREWER_NAME),
			Map.entry(DecoratedPotPatterns.BURN, DecoratedPotPatterns.BURN_NAME),
			Map.entry(DecoratedPotPatterns.DANGER, DecoratedPotPatterns.DANGER_NAME),
			Map.entry(DecoratedPotPatterns.EXPLORER, DecoratedPotPatterns.EXPLORER_NAME),
			Map.entry(DecoratedPotPatterns.FRIEND, DecoratedPotPatterns.FRIEND_NAME),
			Map.entry(DecoratedPotPatterns.HEART, DecoratedPotPatterns.HEART_NAME),
			Map.entry(DecoratedPotPatterns.HEARTBREAK, DecoratedPotPatterns.HEARTBREAK_NAME),
			Map.entry(DecoratedPotPatterns.HOWL, DecoratedPotPatterns.HOWL_NAME),
			Map.entry(DecoratedPotPatterns.MINER, DecoratedPotPatterns.MINER_NAME),
			Map.entry(DecoratedPotPatterns.MOURNER, DecoratedPotPatterns.MOURNER_NAME),
			Map.entry(DecoratedPotPatterns.PLENTY, DecoratedPotPatterns.PLENTY_NAME),
			Map.entry(DecoratedPotPatterns.PRIZE, DecoratedPotPatterns.PRIZE_NAME),
			Map.entry(DecoratedPotPatterns.SHEAF, DecoratedPotPatterns.SHEAF_NAME),
			Map.entry(DecoratedPotPatterns.SHELTER, DecoratedPotPatterns.SHELTER_NAME),
			Map.entry(DecoratedPotPatterns.SKULL, DecoratedPotPatterns.SKULL_NAME),
			Map.entry(DecoratedPotPatterns.SNORT, DecoratedPotPatterns.SNORT_NAME));

	public static void registerPotPatterns(ResourceKey<String> key, String string) {
		PotPattensRegistry.PATTERNS_REGISTRY.put(key, string);
	}
}
