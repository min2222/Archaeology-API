package com.min01.archaeology.particle;

import org.jetbrains.annotations.NotNull;

import com.min01.archaeology.init.ArchaeologyParticleTypes;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.BaseAshSmokeParticle;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.util.FastColor;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RegisterParticleProvidersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

public class DustPlumeParticle extends BaseAshSmokeParticle {
    private static final int COLOR_RGB24 = 12235202;

    protected DustPlumeParticle(final ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed, float sizeMultiplier, final SpriteSet spriteSet) {
        super(level, x, y, z, 0.7F, 0.6F, 0.7F, xSpeed, ySpeed + 0.15F, zSpeed, sizeMultiplier, spriteSet, 0.5F, 7, 0.5F, false);
        float random = (float) Math.random() * 0.2F;
        this.rCol = (float) FastColor.ARGB32.red(COLOR_RGB24) / 255.0F - random;
        this.gCol = (float) FastColor.ARGB32.green(COLOR_RGB24) / 255.0F - random;
        this.bCol = (float) FastColor.ARGB32.blue(COLOR_RGB24) / 255.0F - random;
    }

    @Override
    public void tick() {
        this.gravity = 0.88F * this.gravity;
        this.friction = 0.92F * this.friction;
        super.tick();
    }

    @Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class Provider implements ParticleProvider<SimpleParticleType> {
        private final SpriteSet spriteSet;

        public Provider(final SpriteSet spriteSet) {
            this.spriteSet = spriteSet;
        }

        @SubscribeEvent
        public static void register(final RegisterParticleProvidersEvent event) {
            event.register(ArchaeologyParticleTypes.DUST_PLUME.get(), Provider::new);
        }

        public Particle createParticle(@NotNull final SimpleParticleType type, @NotNull final ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
            return new DustPlumeParticle(level, x, y, z, xSpeed, ySpeed, zSpeed, 1, spriteSet);
        }
    }
}
