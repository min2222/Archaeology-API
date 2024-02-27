package com.min01.archaeology.mixin;

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
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructurePiece;
import net.minecraft.world.level.levelgen.structure.pieces.PiecesContainer;
import net.minecraft.world.level.levelgen.structure.structures.DesertPyramidStructure;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Set;

/** Place suspicious sand in the desert pyramid structure */
@Mixin(Structure.class)
public abstract class MixinStructure {
    @Inject(method = "afterPlace", at = @At("TAIL"))
    protected void afterPlace(final WorldGenLevel level, final StructureManager manager, final ChunkGenerator generator, final RandomSource random, final BoundingBox boundingBox, final ChunkPos chunkPosition, final PiecesContainer pieces, final CallbackInfo callback) {
        if ((Object) this instanceof DesertPyramidStructure) {
            Set<BlockPos> positions = SortedArraySet.create(Vec3i::compareTo);

            for (StructurePiece piece : pieces.pieces()) {
                if (piece instanceof IDesertPyramidPiece pyramidPiece) {
                    positions.addAll(pyramidPiece.archaeology$getPotentialSuspiciousSandWorldPositions());
                    archaeology$setSuspiciousSand(boundingBox, level, pyramidPiece.archaeology$getRandomCollapsedRoofPos());
                }
            }

            ObjectArrayList<BlockPos> shuffledPositions = new ObjectArrayList<>(positions.stream().toList());
            RandomSource newRandom = RandomSource.create(level.getSeed()).forkPositional().at(pieces.calculateBoundingBox().getCenter());
            Util.shuffle(shuffledPositions, newRandom);
            int suspiciousAmount = Math.min(positions.size(), newRandom.nextInt(5, 8));

            for (BlockPos blockpos : shuffledPositions) {
                if (suspiciousAmount > 0) {
                    suspiciousAmount--;
                    archaeology$setSuspiciousSand(boundingBox, level, blockpos);
                } else if (boundingBox.isInside(blockpos)) {
                    level.setBlock(blockpos, Blocks.SAND.defaultBlockState(), Block.UPDATE_CLIENTS);
                }
            }
        }
    }

    @Unique
    private static void archaeology$setSuspiciousSand(final BoundingBox boundingBox, final WorldGenLevel level, final BlockPos position) {
        if (boundingBox.isInside(position)) {
            level.setBlock(position, ArchaeologyBlocks.SUSPICIOUS_SAND.get().defaultBlockState(), Block.UPDATE_CLIENTS);
            level.getBlockEntity(position, ArchaeologyBlockEntityType.BRUSHABLE_BLOCK.get()).ifPresent(brushableEntity -> brushableEntity.setLootTable(ArchaeologyLootTables.DESERT_PYRAMID_ARCHAEOLOGY, position.asLong()));
        }
    }
}
