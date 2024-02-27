package com.min01.archaeology.mixin.client;

import javax.annotation.Nullable;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.min01.archaeology.block.BrushableBlock;
import com.min01.archaeology.misc.CustomLevelEvent;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

/** Add support for the new brush event */
@Mixin(LevelRenderer.class)
public abstract class LevelRendererMixin {
    @Inject(method = "levelEvent", at = @At(value = "HEAD"))
    private void handleCustomLevelEvents(int type, final BlockPos position, int data, final CallbackInfo callback) {
        if (type == CustomLevelEvent.PARTICLES_AND_SOUND_BRUSH_BLOCK_COMPLETE) {
            BlockState blockState2 = Block.stateById(data);

            if (blockState2.getBlock() instanceof BrushableBlock brushableBlock) {
                level.playLocalSound(position, brushableBlock.getBrushCompletedSound(), SoundSource.PLAYERS, 1, 1, false);
            }

            this.level.addDestroyBlockEffect(position, blockState2);
        }
    }

    @Shadow private @Nullable ClientLevel level;
}
