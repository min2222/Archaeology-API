package com.min01.archaeology.init;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;

public class ArchaeologyRegistryKey
{
	public static final ResourceKey<Registry<String>> DECORATED_POT_PATTERNS = createRegistryKey("decorated_pot_patterns");
	   
	private static <T> ResourceKey<Registry<T>> createRegistryKey(String p_259572_) {
		return ResourceKey.createRegistryKey(new ResourceLocation(p_259572_));
	}
}
