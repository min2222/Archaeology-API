package com.min01.archaeology.mixin;

import com.min01.archaeology.misc.IDesertPyramidPiece;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.StructureManager;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.ScatteredFeaturePiece;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceType;
import net.minecraft.world.level.levelgen.structure.structures.DesertPyramidPiece;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.ArrayList;
import java.util.List;

/** Generate the new cellar part */
@Mixin(DesertPyramidPiece.class)
public abstract class MixinDesertPyramidPiece extends ScatteredFeaturePiece implements IDesertPyramidPiece {
    @Unique
    private final List<BlockPos> archaeology$potentialSuspiciousSandWorldPositions = new ArrayList<>();
    @Unique
    private BlockPos archaeology$randomCollapsedRoofPos = BlockPos.ZERO;

    protected MixinDesertPyramidPiece(final StructurePieceType type, final CompoundTag tag) {
        super(type, tag);
    }

    @Inject(method = "postProcess", at = @At("TAIL"))
    private void postProcess(final WorldGenLevel level, final StructureManager manager, final ChunkGenerator generator, final RandomSource random, final BoundingBox boundingBox, final ChunkPos chunkPosition, final BlockPos position, final CallbackInfo callback) {
        archaeology$addCellar(level, boundingBox);
    }

    @Unique
    private void archaeology$addCellar(final WorldGenLevel level, final BoundingBox boundingBox) {
        BlockPos blockpos = new BlockPos(16, -4, 13);
        archaeology$addCellarStairs(blockpos, level, boundingBox);
        archaeology$addCellarRoom(blockpos, level, boundingBox);
    }

    @Unique
    @SuppressWarnings("deprecation")
    private void archaeology$addCellarStairs(final BlockPos position, final WorldGenLevel level, final BoundingBox boundingBox) {
        int x = position.getX();
        int y = position.getY();
        int z = position.getZ();

        BlockState sandstoneStairs = Blocks.SANDSTONE_STAIRS.defaultBlockState();
        placeBlock(level, sandstoneStairs.rotate(Rotation.COUNTERCLOCKWISE_90), 13, -1, 17, boundingBox);
        placeBlock(level, sandstoneStairs.rotate(Rotation.COUNTERCLOCKWISE_90), 14, -2, 17, boundingBox);
        placeBlock(level, sandstoneStairs.rotate(Rotation.COUNTERCLOCKWISE_90), 15, -3, 17, boundingBox);

        BlockState sand = Blocks.SAND.defaultBlockState();
        BlockState sandStone = Blocks.SANDSTONE.defaultBlockState();
        boolean flag = level.getRandom().nextBoolean();

        placeBlock(level, sand, x - 4, y + 4, z + 4, boundingBox);
        placeBlock(level, sand, x - 3, y + 4, z + 4, boundingBox);
        placeBlock(level, sand, x - 2, y + 4, z + 4, boundingBox);
        placeBlock(level, sand, x - 1, y + 4, z + 4, boundingBox);
        placeBlock(level, sand, x, y + 4, z + 4, boundingBox);
        placeBlock(level, sand, x - 2, y + 3, z + 4, boundingBox);
        placeBlock(level, flag ? sand : sandStone, x - 1, y + 3, z + 4, boundingBox);
        placeBlock(level, !flag ? sand : sandStone, x, y + 3, z + 4, boundingBox);
        placeBlock(level, sand, x - 1, y + 2, z + 4, boundingBox);
        placeBlock(level, sandStone, x, y + 2, z + 4, boundingBox);
        placeBlock(level, sand, x, y + 1, z + 4, boundingBox);
    }

    @Unique
    private void archaeology$addCellarRoom(final BlockPos position, final WorldGenLevel level, final BoundingBox boundingBox) {
        int x = position.getX();
        int y = position.getY();
        int z = position.getZ();

        BlockState cutSandstone = Blocks.CUT_SANDSTONE.defaultBlockState();
        BlockState chiseledSandstone = Blocks.CHISELED_SANDSTONE.defaultBlockState();

        generateBox(level, boundingBox, x - 3, y + 1, z - 3, x - 3, y + 1, z + 2, cutSandstone, cutSandstone, true);
        generateBox(level, boundingBox, x + 3, y + 1, z - 3, x + 3, y + 1, z + 2, cutSandstone, cutSandstone, true);
        generateBox(level, boundingBox, x - 3, y + 1, z - 3, x + 3, y + 1, z - 2, cutSandstone, cutSandstone, true);
        generateBox(level, boundingBox, x - 3, y + 1, z + 3, x + 3, y + 1, z + 3, cutSandstone, cutSandstone, true);
        generateBox(level, boundingBox, x - 3, y + 2, z - 3, x - 3, y + 2, z + 2, chiseledSandstone, chiseledSandstone, true);
        generateBox(level, boundingBox, x + 3, y + 2, z - 3, x + 3, y + 2, z + 2, chiseledSandstone, chiseledSandstone, true);
        generateBox(level, boundingBox, x - 3, y + 2, z - 3, x + 3, y + 2, z - 2, chiseledSandstone, chiseledSandstone, true);
        generateBox(level, boundingBox, x - 3, y + 2, z + 3, x + 3, y + 2, z + 3, chiseledSandstone, chiseledSandstone, true);
        generateBox(level, boundingBox, x - 3, -1, z - 3, x - 3, -1, z + 2, cutSandstone, cutSandstone, true);
        generateBox(level, boundingBox, x + 3, -1, z - 3, x + 3, -1, z + 2, cutSandstone, cutSandstone, true);
        generateBox(level, boundingBox, x - 3, -1, z - 3, x + 3, -1, z - 2, cutSandstone, cutSandstone, true);
        generateBox(level, boundingBox, x - 3, -1, z + 3, x + 3, -1, z + 3, cutSandstone, cutSandstone, true);

        archaeology$placeSandBox(x - 2, y + 1, z - 2, x + 2, y + 3, z + 2);
        archaeology$placeCollapsedRoof(level, boundingBox, x - 2, y + 4, z - 2, x + 2, z + 2);

        BlockState orangeTerracotta = Blocks.ORANGE_TERRACOTTA.defaultBlockState();
        BlockState blueTerracotta = Blocks.BLUE_TERRACOTTA.defaultBlockState();

        placeBlock(level, blueTerracotta, x, y, z, boundingBox);
        placeBlock(level, orangeTerracotta, x + 1, y, z - 1, boundingBox);
        placeBlock(level, orangeTerracotta, x + 1, y, z + 1, boundingBox);
        placeBlock(level, orangeTerracotta, x - 1, y, z - 1, boundingBox);
        placeBlock(level, orangeTerracotta, x - 1, y, z + 1, boundingBox);
        placeBlock(level, orangeTerracotta, x + 2, y, z, boundingBox);
        placeBlock(level, orangeTerracotta, x - 2, y, z, boundingBox);
        placeBlock(level, orangeTerracotta, x, y, z + 2, boundingBox);
        placeBlock(level, orangeTerracotta, x, y, z - 2, boundingBox);
        placeBlock(level, orangeTerracotta, x + 3, y, z, boundingBox);
        archaeology$placeSand(x + 3, y + 1, z);
        archaeology$placeSand(x + 3, y + 2, z);
        placeBlock(level, cutSandstone, x + 4, y + 1, z, boundingBox);
        placeBlock(level, chiseledSandstone, x + 4, y + 2, z, boundingBox);
        placeBlock(level, orangeTerracotta, x - 3, y, z, boundingBox);
        archaeology$placeSand(x - 3, y + 1, z);
        archaeology$placeSand(x - 3, y + 2, z);
        placeBlock(level, cutSandstone, x - 4, y + 1, z, boundingBox);
        placeBlock(level, chiseledSandstone, x - 4, y + 2, z, boundingBox);
        placeBlock(level, orangeTerracotta, x, y, z + 3, boundingBox);
        archaeology$placeSand(x, y + 1, z + 3);
        archaeology$placeSand(x, y + 2, z + 3);
        placeBlock(level, orangeTerracotta, x, y, z - 3, boundingBox);
        archaeology$placeSand(x, y + 1, z - 3);
        archaeology$placeSand(x, y + 2, z - 3);
        placeBlock(level, cutSandstone, x, y + 1, z - 4, boundingBox);
        placeBlock(level, chiseledSandstone, x, -2, z - 4, boundingBox);
    }

    @Unique
    private void archaeology$placeSand(int x, int y, int z) {
        archaeology$potentialSuspiciousSandWorldPositions.add(this.getWorldPos(x, y, z));
    }

    @Unique
    private void archaeology$placeSandBox(int xFrom, int yFrom, int zFrom, int xTo, int yTo, int zTo) {
        for (int y = yFrom; y <= yTo; y++) {
            for (int x = xFrom; x <= xTo; x++) {
                for (int z = zFrom; z <= zTo; z++) {
                    this.archaeology$placeSand(x, y, z);
                }
            }
        }
    }

    @Unique
    private void archaeology$placeCollapsedRoofPiece(final WorldGenLevel level, int x, int y, int z, final BoundingBox boundingBox) {
        if (level.getRandom().nextFloat() < 0.33F) {
            placeBlock(level, Blocks.SANDSTONE.defaultBlockState(), x, y, z, boundingBox);
        } else {
            placeBlock(level, Blocks.SAND.defaultBlockState(), x, y, z, boundingBox);
        }
    }

    @Unique
    private void archaeology$placeCollapsedRoof(final WorldGenLevel level, final BoundingBox boundingBox, int xFrom, int y, int zFrom, int xTo, int zTo) {
        for (int x = xFrom; x <= xTo; x++) {
            for (int z = zFrom; z <= zTo; z++) {
                this.archaeology$placeCollapsedRoofPiece(level, x, y, z, boundingBox);
            }
        }

        RandomSource random = RandomSource.create(level.getSeed()).forkPositional().at(this.getWorldPos(xFrom, y, zFrom));
        int x = random.nextIntBetweenInclusive(xFrom, xTo);
        int z = random.nextIntBetweenInclusive(zFrom, zTo);
        archaeology$randomCollapsedRoofPos = new BlockPos(this.getWorldX(x, z), getWorldY(y), getWorldZ(x, z));
    }

    @Override
    public List<BlockPos> archaeology$getPotentialSuspiciousSandWorldPositions() {
        return archaeology$potentialSuspiciousSandWorldPositions;
    }

    @Override
    public BlockPos archaeology$getRandomCollapsedRoofPos() {
        return archaeology$randomCollapsedRoofPos;
    }
}
