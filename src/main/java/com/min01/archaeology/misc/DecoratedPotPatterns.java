package com.min01.archaeology.misc;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.annotation.Nullable;

import com.min01.archaeology.init.ArchaeologyItems;
import com.min01.archaeology.init.ArchaeologyRegistryKey;

import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;

public class DecoratedPotPatterns {
   public static final String BASE_NAME = "decorated_pot_base";
   public static final String BRICK_NAME = "decorated_pot_side";
   public static final String ANGLER_NAME = "angler_pottery_pattern";
   public static final String ARCHER_NAME = "archer_pottery_pattern";
   public static final String ARMS_UP_NAME = "arms_up_pottery_pattern";
   public static final String BLADE_NAME = "blade_pottery_pattern";
   public static final String BREWER_NAME = "brewer_pottery_pattern";
   public static final String BURN_NAME = "burn_pottery_pattern";
   public static final String DANGER_NAME = "danger_pottery_pattern";
   public static final String EXPLORER_NAME = "explorer_pottery_pattern";
   public static final String FRIEND_NAME = "friend_pottery_pattern";
   public static final String HEART_NAME = "heart_pottery_pattern";
   public static final String HEARTBREAK_NAME = "heartbreak_pottery_pattern";
   public static final String HOWL_NAME = "howl_pottery_pattern";
   public static final String MINER_NAME = "miner_pottery_pattern";
   public static final String MOURNER_NAME = "mourner_pottery_pattern";
   public static final String PLENTY_NAME = "plenty_pottery_pattern";
   public static final String PRIZE_NAME = "prize_pottery_pattern";
   public static final String SHEAF_NAME = "sheaf_pottery_pattern";
   public static final String SHELTER_NAME = "shelter_pottery_pattern";
   public static final String SKULL_NAME = "skull_pottery_pattern";
   public static final String SNORT_NAME = "snort_pottery_pattern";
   
   public static final ResourceKey<String> BASE = create("decorated_pot_base");
   public static final ResourceKey<String> BRICK = create("decorated_pot_side");
   public static final ResourceKey<String> ANGLER = create("angler_pottery_pattern");
   public static final ResourceKey<String> ARCHER = create("archer_pottery_pattern");
   public static final ResourceKey<String> ARMS_UP = create("arms_up_pottery_pattern");
   public static final ResourceKey<String> BLADE = create("blade_pottery_pattern");
   public static final ResourceKey<String> BREWER = create("brewer_pottery_pattern");
   public static final ResourceKey<String> BURN = create("burn_pottery_pattern");
   public static final ResourceKey<String> DANGER = create("danger_pottery_pattern");
   public static final ResourceKey<String> EXPLORER = create("explorer_pottery_pattern");
   public static final ResourceKey<String> FRIEND = create("friend_pottery_pattern");
   public static final ResourceKey<String> HEART = create("heart_pottery_pattern");
   public static final ResourceKey<String> HEARTBREAK = create("heartbreak_pottery_pattern");
   public static final ResourceKey<String> HOWL = create("howl_pottery_pattern");
   public static final ResourceKey<String> MINER = create("miner_pottery_pattern");
   public static final ResourceKey<String> MOURNER = create("mourner_pottery_pattern");
   public static final ResourceKey<String> PLENTY = create("plenty_pottery_pattern");
   public static final ResourceKey<String> PRIZE = create("prize_pottery_pattern");
   public static final ResourceKey<String> SHEAF = create("sheaf_pottery_pattern");
   public static final ResourceKey<String> SHELTER = create("shelter_pottery_pattern");
   public static final ResourceKey<String> SKULL = create("skull_pottery_pattern");
   public static final ResourceKey<String> SNORT = create("snort_pottery_pattern");
   
   public static final Map<Item, ResourceKey<String>> ITEM_TO_POT_TEXTURE = Map.ofEntries(Map.entry(Items.BRICK, BRICK), Map.entry(ArchaeologyItems.ANGLER_POTTERY_SHERD.get(), ANGLER), Map.entry(ArchaeologyItems.ARCHER_POTTERY_SHERD.get(), ARCHER), Map.entry(ArchaeologyItems.ARMS_UP_POTTERY_SHERD.get(), ARMS_UP), Map.entry(ArchaeologyItems.BLADE_POTTERY_SHERD.get(), BLADE), Map.entry(ArchaeologyItems.BREWER_POTTERY_SHERD.get(), BREWER), Map.entry(ArchaeologyItems.BURN_POTTERY_SHERD.get(), BURN), Map.entry(ArchaeologyItems.DANGER_POTTERY_SHERD.get(), DANGER), Map.entry(ArchaeologyItems.EXPLORER_POTTERY_SHERD.get(), EXPLORER), Map.entry(ArchaeologyItems.FRIEND_POTTERY_SHERD.get(), FRIEND), Map.entry(ArchaeologyItems.HEART_POTTERY_SHERD.get(), HEART), Map.entry(ArchaeologyItems.HEARTBREAK_POTTERY_SHERD.get(), HEARTBREAK), Map.entry(ArchaeologyItems.HOWL_POTTERY_SHERD.get(), HOWL), Map.entry(ArchaeologyItems.MINER_POTTERY_SHERD.get(), MINER), Map.entry(ArchaeologyItems.MOURNER_POTTERY_SHERD.get(), MOURNER), Map.entry(ArchaeologyItems.PLENTY_POTTERY_SHERD.get(), PLENTY), Map.entry(ArchaeologyItems.PRIZE_POTTERY_SHERD.get(), PRIZE), Map.entry(ArchaeologyItems.SHEAF_POTTERY_SHERD.get(), SHEAF), Map.entry(ArchaeologyItems.SHELTER_POTTERY_SHERD.get(), SHELTER), Map.entry(ArchaeologyItems.SKULL_POTTERY_SHERD.get(), SKULL), Map.entry(ArchaeologyItems.SNORT_POTTERY_SHERD.get(), SNORT));
   
   private static ResourceKey<String> create(String p_272919_) {
	   ResourceKey<String> key = ResourceKey.create(ArchaeologyRegistryKey.DECORATED_POT_PATTERNS, new ResourceLocation(p_272919_));
	   return key;
   }
   
   public static ResourceLocation location(String modid, ResourceKey<String> p_273198_) {
	   return new ResourceLocation(modid, "entity/decorated_pot/" + p_273198_.location().getPath());
   }

   public static ResourceLocation location(ResourceKey<String> p_273198_) {
	   return new ResourceLocation("entity/decorated_pot/" + p_273198_.location().getPath());
   }

   @Nullable
   public static ResourceKey<String> getResourceKey(Item p_273094_) {
	   return ITEM_TO_POT_TEXTURE.get(p_273094_);
   }

   public static Set<ResourceKey<String>> bootstrap() {
	   Set<ResourceKey<String>> set = new HashSet<>();
	   set.add(BRICK);
	   set.add(ANGLER);
	   set.add(ARCHER);
	   set.add(ARMS_UP);
	   set.add(BLADE); 
	   set.add(BREWER);
	   set.add(BURN);
	   set.add(DANGER);
	   set.add(EXPLORER);
	   set.add(FRIEND);
	   set.add(HEART);
	   set.add(HEARTBREAK);
	   set.add(HOWL);
	   set.add(MINER); 
	   set.add(MOURNER);
	   set.add(PLENTY);
	   set.add(PRIZE);
	   set.add(SHEAF);
	   set.add(SHELTER);
	   set.add(SKULL);
	   set.add(SNORT);
	   set.add(BASE);
	   return set;
   }
}