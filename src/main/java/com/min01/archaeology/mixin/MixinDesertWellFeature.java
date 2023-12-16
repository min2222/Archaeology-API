package com.min01.archaeology.mixin;

import java.util.List;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.min01.archaeology.init.ArchaeologyBlockEntityType;
import com.min01.archaeology.init.ArchaeologyBlocks;
import com.min01.archaeology.init.ArchaeologyLootTables;

import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.levelgen.feature.DesertWellFeature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;

@Mixin(DesertWellFeature.class)
public class MixinDesertWellFeature 
{
	@Inject(at = @At("TAIL"), method = "place", cancellable = true)
	private void place(FeaturePlaceContext<NoneFeatureConfiguration> p_159571_, CallbackInfoReturnable<Boolean> ci)
	{
		if(ci.getReturnValue())
		{
			WorldGenLevel worldgenlevel = p_159571_.level();
	    	BlockPos blockpos = p_159571_.origin();
		    List<BlockPos> list = List.of(blockpos, blockpos.east(), blockpos.south(), blockpos.west(), blockpos.north());
		    RandomSource randomsource = p_159571_.random();
		    placeSusSand(worldgenlevel, Util.getRandom(list, randomsource).below(1));
		    placeSusSand(worldgenlevel, Util.getRandom(list, randomsource).below(2));
		}
	}

	private static void placeSusSand(WorldGenLevel p_278029_, BlockPos p_278082_) {
		p_278029_.setBlock(p_278082_, ArchaeologyBlocks.SUSPICIOUS_SAND.get().defaultBlockState(), 3);
		p_278029_.getBlockEntity(p_278082_, ArchaeologyBlockEntityType.BRUSHABLE_BLOCK.get()).ifPresent((p_277322_) -> {
			p_277322_.setLootTable(ArchaeologyLootTables.DESERT_WELL_ARCHAEOLOGY, p_278082_.asLong());
		});
	}
}
