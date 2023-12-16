package com.min01.archaeology.init;

import com.min01.archaeology.Archaeology;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.level.block.SoundType;
import net.minecraftforge.common.util.ForgeSoundType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ArchaeologySounds 
{
	public static final DeferredRegister<SoundEvent> SOUND_EVENTS = DeferredRegister.create(ForgeRegistries.SOUND_EVENTS, Archaeology.MODID);
	
	public static final RegistryObject<SoundEvent> BRUSH_GENERIC = register("item.brush.brushing.generic");
	public static final RegistryObject<SoundEvent> BRUSH_SAND = register("item.brush.brushing.sand");
	public static final RegistryObject<SoundEvent> BRUSH_GRAVEL = register("item.brush.brushing.gravel");
	public static final RegistryObject<SoundEvent> BRUSH_SAND_COMPLETED = register("item.brush.brushing.sand.complete");
	public static final RegistryObject<SoundEvent> BRUSH_GRAVEL_COMPLETED = register("item.brush.brushing.gravel.complete");
	public static final RegistryObject<SoundEvent> SUSPICIOUS_SAND_BREAK = register("block.suspicious_sand.break");
	public static final RegistryObject<SoundEvent> SUSPICIOUS_SAND_STEP = register("block.suspicious_sand.step");
	public static final RegistryObject<SoundEvent> SUSPICIOUS_SAND_PLACE = register("block.suspicious_sand.place");
	public static final RegistryObject<SoundEvent> SUSPICIOUS_SAND_HIT = register("block.suspicious_sand.hit");
	public static final RegistryObject<SoundEvent> SUSPICIOUS_SAND_FALL = register("block.suspicious_sand.fall");
	public static final RegistryObject<SoundEvent> SUSPICIOUS_GRAVEL_BREAK = register("block.suspicious_gravel.break");
	public static final RegistryObject<SoundEvent> SUSPICIOUS_GRAVEL_STEP = register("block.suspicious_gravel.step");
	public static final RegistryObject<SoundEvent> SUSPICIOUS_GRAVEL_PLACE = register("block.suspicious_gravel.place");
	public static final RegistryObject<SoundEvent> SUSPICIOUS_GRAVEL_HIT = register("block.suspicious_gravel.hit");
	public static final RegistryObject<SoundEvent> SUSPICIOUS_GRAVEL_FALL = register("block.suspicious_gravel.fall");
	public static final RegistryObject<SoundEvent> MUSIC_DISC_RELIC = register("music_disc.relic");
	
	public static final RegistryObject<SoundEvent> DECORATED_POT_BREAK = register("block.decorated_pot.break");
	public static final RegistryObject<SoundEvent> DECORATED_POT_FALL = register("block.decorated_pot.fall");
	public static final RegistryObject<SoundEvent> DECORATED_POT_HIT = register("block.decorated_pot.hit");
	public static final RegistryObject<SoundEvent> DECORATED_POT_STEP = register("block.decorated_pot.step");
	public static final RegistryObject<SoundEvent> DECORATED_POT_PLACE = register("block.decorated_pot.place");
	public static final RegistryObject<SoundEvent> DECORATED_POT_SHATTER = register("block.decorated_pot.shatter");
	
	public static final SoundType SUSPICIOUS_SAND = new ForgeSoundType(1.0F, 1.0F, () -> SUSPICIOUS_SAND_BREAK.get(), () -> SUSPICIOUS_SAND_STEP.get(), () -> SUSPICIOUS_SAND_PLACE.get(), () -> SUSPICIOUS_SAND_HIT.get(), () -> SUSPICIOUS_SAND_FALL.get());
	public static final SoundType SUSPICIOUS_GRAVEL = new ForgeSoundType(1.0F, 1.0F, () -> SUSPICIOUS_GRAVEL_BREAK.get(), () -> SUSPICIOUS_GRAVEL_STEP.get(), () -> SUSPICIOUS_GRAVEL_PLACE.get(), () -> SUSPICIOUS_GRAVEL_HIT.get(), () -> SUSPICIOUS_GRAVEL_FALL.get());
	
	public static final SoundType DECORATED_POT = new ForgeSoundType(1.0F, 1.0F, () -> DECORATED_POT_BREAK.get(), () -> DECORATED_POT_STEP.get(), () -> DECORATED_POT_PLACE.get(), () -> DECORATED_POT_HIT.get(), () -> DECORATED_POT_FALL.get());
	public static final SoundType DECORATED_POT_CRACKED = new ForgeSoundType(1.0F, 1.0F, () -> DECORATED_POT_SHATTER.get(), () -> DECORATED_POT_STEP.get(), () -> DECORATED_POT_PLACE.get(), () -> DECORATED_POT_HIT.get(), () -> DECORATED_POT_FALL.get());
	   
	private static RegistryObject<SoundEvent> register(String name) 
	{
		return SOUND_EVENTS.register(name, () -> new SoundEvent(new ResourceLocation(Archaeology.MODID, name)));
    }
}
