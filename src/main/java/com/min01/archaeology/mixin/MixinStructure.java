package com.min01.archaeology.mixin;

import java.util.Set;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.min01.archaeology.init.ArchaeologyBlockEntityType;
import com.min01.archaeology.init.ArchaeologyBlocks;
import com.min01.archaeology.init.ArchaeologyLootTables;
import com.min01.archaeology.misc.IDesertPyramidPiece;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Vec3i;
import net.minecraft.util.RandomSource;
import net.minecraft.util.SortedArraySet;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.StructureManager;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructurePiece;
import net.minecraft.world.level.levelgen.structure.pieces.PiecesContainer;
import net.minecraft.world.level.levelgen.structure.structures.DesertPyramidPiece;
import net.minecraft.world.level.levelgen.structure.structures.DesertPyramidStructure;

@Mixin(Structure.class)
public class MixinStructure
{
	@Inject(at = @At("TAIL"), method = "afterPlace")
	protected void afterPlace(WorldGenLevel p_273644_, StructureManager p_272615_, ChunkGenerator p_273655_, RandomSource p_272939_, BoundingBox p_273179_, ChunkPos p_273334_, PiecesContainer p_273575_, CallbackInfo ci) 
	{
		if(Structure.class.cast(this) instanceof DesertPyramidStructure)
		{
			Set<BlockPos> set = SortedArraySet.create(Vec3i::compareTo);

			for(StructurePiece structurepiece : p_273575_.pieces())
			{
				if (structurepiece instanceof DesertPyramidPiece desertpyramidpiece) 
				{
					set.addAll(((IDesertPyramidPiece) desertpyramidpiece).getPotentialSuspiciousSandWorldPositions());
					placeSuspiciousSand(p_273179_, p_273644_, ((IDesertPyramidPiece) desertpyramidpiece).getRandomCollapsedRoofPos());
				}
			}

			ObjectArrayList<BlockPos> objectarraylist = new ObjectArrayList<>(set.stream().toList());
			RandomSource randomsource = RandomSource.create(p_273644_.getSeed()).forkPositional().at(p_273575_.calculateBoundingBox().getCenter());
			Util.shuffle(objectarraylist, randomsource);
			int i = Math.min(set.size(), randomsource.nextInt(5, 8));

			for(BlockPos blockpos : objectarraylist)
			{
				if (i > 0) 
				{
					--i;
					placeSuspiciousSand(p_273179_, p_273644_, blockpos);
				}
				else if (p_273179_.isInside(blockpos))
				{
					p_273644_.setBlock(blockpos, Blocks.SAND.defaultBlockState(), 2);
				}
			}
		}
	}
	
	@Unique
	private static void placeSuspiciousSand(BoundingBox p_279472_, WorldGenLevel p_279193_, BlockPos p_279136_) 
	{
		if (p_279472_.isInside(p_279136_))
		{
			p_279193_.setBlock(p_279136_, ArchaeologyBlocks.SUSPICIOUS_SAND.get().defaultBlockState(), 2);
			p_279193_.getBlockEntity(p_279136_, ArchaeologyBlockEntityType.BRUSHABLE_BLOCK.get()).ifPresent((p_277328_) ->
			{
				p_277328_.setLootTable(ArchaeologyLootTables.DESERT_PYRAMID_ARCHAEOLOGY, p_279136_.asLong());
			});
		}
	}
}
