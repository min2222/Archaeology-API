package com.min01.archaeology.mixin;

import java.util.List;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.min01.archaeology.init.ArchaeologyBlockEntityType;
import com.min01.archaeology.init.ArchaeologyBlocks;
import com.min01.archaeology.init.ArchaeologyLootTables;

import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.levelgen.feature.DesertWellFeature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;

/** Place suspicious sand in desert well features */
@Mixin(DesertWellFeature.class)
public abstract class MixinDesertWellFeature {
    @Inject(method = "place", at = @At("TAIL"))
    private void place(final FeaturePlaceContext<NoneFeatureConfiguration> context, final CallbackInfoReturnable<Boolean> callback) {
        if (callback.getReturnValue()) {
            BlockPos origin = context.origin();
            List<BlockPos> positions = List.of(origin, origin.east(), origin.south(), origin.west(), origin.north());
            archaeology$placeSuspiciousSand(context.level(), Util.getRandom(positions, context.random()).below(1));
            archaeology$placeSuspiciousSand(context.level(), Util.getRandom(positions, context.random()).below(2));
        }
    }

    @Unique
    private static void archaeology$placeSuspiciousSand(final WorldGenLevel level, final BlockPos position) {
        level.setBlock(position, ArchaeologyBlocks.SUSPICIOUS_SAND.get().defaultBlockState(), Block.UPDATE_ALL);
        level.getBlockEntity(position, ArchaeologyBlockEntityType.BRUSHABLE_BLOCK.get()).ifPresent(brushableEntity -> brushableEntity.setLootTable(ArchaeologyLootTables.DESERT_WELL_ARCHAEOLOGY, position.asLong()));
    }
}
