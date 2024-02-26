package com.min01.archaeology.init;

import com.min01.archaeology.Archaeology;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ArchaeologyParticleTypes {
    public static final DeferredRegister<ParticleType<?>> PARTICLES = DeferredRegister.create(ForgeRegistries.PARTICLE_TYPES, Archaeology.MC_ID);
    public static final RegistryObject<SimpleParticleType> DUST_PLUME = PARTICLES.register("dust_plume", () -> new SimpleParticleType(false));
}
