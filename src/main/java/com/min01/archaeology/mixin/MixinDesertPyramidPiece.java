package com.min01.archaeology.mixin;

import java.util.ArrayList;
import java.util.List;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

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

@Mixin(DesertPyramidPiece.class)
public abstract class MixinDesertPyramidPiece extends ScatteredFeaturePiece implements IDesertPyramidPiece {
	
	private final List<BlockPos> potentialSuspiciousSandWorldPositions = new ArrayList<>();
	private BlockPos randomCollapsedRoofPos = BlockPos.ZERO;
	   
	protected MixinDesertPyramidPiece(StructurePieceType p_209929_, CompoundTag p_209930_) {
		super(p_209929_, p_209930_);
	}
	
	@Inject(at = @At("TAIL"), method = "postProcess", cancellable = true)
	private void postProcess(WorldGenLevel p_227405_, StructureManager p_227406_, ChunkGenerator p_227407_, RandomSource p_227408_, BoundingBox p_227409_, ChunkPos p_227410_, BlockPos p_227411_, CallbackInfo ci)
	{
        this.addCellar(p_227405_, p_227409_);
	}

	private void addCellar(WorldGenLevel p_272769_, BoundingBox p_273155_) {
		BlockPos blockpos = new BlockPos(16, -4, 13);
		this.addCellarStairs(blockpos, p_272769_, p_273155_);
		this.addCellarRoom(blockpos, p_272769_, p_273155_);
	}

	@SuppressWarnings("deprecation")
	private void addCellarStairs(BlockPos p_272997_, WorldGenLevel p_272699_, BoundingBox p_273559_) {
		int i = p_272997_.getX();
		int j = p_272997_.getY();
		int k = p_272997_.getZ();
		BlockState blockstate = Blocks.SANDSTONE_STAIRS.defaultBlockState();
		this.placeBlock(p_272699_, blockstate.rotate(Rotation.COUNTERCLOCKWISE_90), 13, -1, 17, p_273559_);
		this.placeBlock(p_272699_, blockstate.rotate(Rotation.COUNTERCLOCKWISE_90), 14, -2, 17, p_273559_);
		this.placeBlock(p_272699_, blockstate.rotate(Rotation.COUNTERCLOCKWISE_90), 15, -3, 17, p_273559_);
		BlockState blockstate1 = Blocks.SAND.defaultBlockState();
		BlockState blockstate2 = Blocks.SANDSTONE.defaultBlockState();
		boolean flag = p_272699_.getRandom().nextBoolean();
		this.placeBlock(p_272699_, blockstate1, i - 4, j + 4, k + 4, p_273559_);
		this.placeBlock(p_272699_, blockstate1, i - 3, j + 4, k + 4, p_273559_);
		this.placeBlock(p_272699_, blockstate1, i - 2, j + 4, k + 4, p_273559_);
		this.placeBlock(p_272699_, blockstate1, i - 1, j + 4, k + 4, p_273559_);
		this.placeBlock(p_272699_, blockstate1, i, j + 4, k + 4, p_273559_);
		this.placeBlock(p_272699_, blockstate1, i - 2, j + 3, k + 4, p_273559_);
		this.placeBlock(p_272699_, flag ? blockstate1 : blockstate2, i - 1, j + 3, k + 4, p_273559_);
		this.placeBlock(p_272699_, !flag ? blockstate1 : blockstate2, i, j + 3, k + 4, p_273559_);
		this.placeBlock(p_272699_, blockstate1, i - 1, j + 2, k + 4, p_273559_);
		this.placeBlock(p_272699_, blockstate2, i, j + 2, k + 4, p_273559_);
		this.placeBlock(p_272699_, blockstate1, i, j + 1, k + 4, p_273559_);
	}

	private void addCellarRoom(BlockPos p_272733_, WorldGenLevel p_273390_, BoundingBox p_273517_) {
		int i = p_272733_.getX();
		int j = p_272733_.getY();
		int k = p_272733_.getZ();
		BlockState blockstate = Blocks.CUT_SANDSTONE.defaultBlockState();
		BlockState blockstate1 = Blocks.CHISELED_SANDSTONE.defaultBlockState();
		this.generateBox(p_273390_, p_273517_, i - 3, j + 1, k - 3, i - 3, j + 1, k + 2, blockstate, blockstate, true);
		this.generateBox(p_273390_, p_273517_, i + 3, j + 1, k - 3, i + 3, j + 1, k + 2, blockstate, blockstate, true);
		this.generateBox(p_273390_, p_273517_, i - 3, j + 1, k - 3, i + 3, j + 1, k - 2, blockstate, blockstate, true);
		this.generateBox(p_273390_, p_273517_, i - 3, j + 1, k + 3, i + 3, j + 1, k + 3, blockstate, blockstate, true);
		this.generateBox(p_273390_, p_273517_, i - 3, j + 2, k - 3, i - 3, j + 2, k + 2, blockstate1, blockstate1,
				true);
		this.generateBox(p_273390_, p_273517_, i + 3, j + 2, k - 3, i + 3, j + 2, k + 2, blockstate1, blockstate1,
				true);
		this.generateBox(p_273390_, p_273517_, i - 3, j + 2, k - 3, i + 3, j + 2, k - 2, blockstate1, blockstate1,
				true);
		this.generateBox(p_273390_, p_273517_, i - 3, j + 2, k + 3, i + 3, j + 2, k + 3, blockstate1, blockstate1,
				true);
		this.generateBox(p_273390_, p_273517_, i - 3, -1, k - 3, i - 3, -1, k + 2, blockstate, blockstate, true);
		this.generateBox(p_273390_, p_273517_, i + 3, -1, k - 3, i + 3, -1, k + 2, blockstate, blockstate, true);
		this.generateBox(p_273390_, p_273517_, i - 3, -1, k - 3, i + 3, -1, k - 2, blockstate, blockstate, true);
		this.generateBox(p_273390_, p_273517_, i - 3, -1, k + 3, i + 3, -1, k + 3, blockstate, blockstate, true);
		this.placeSandBox(i - 2, j + 1, k - 2, i + 2, j + 3, k + 2);
		this.placeCollapsedRoof(p_273390_, p_273517_, i - 2, j + 4, k - 2, i + 2, k + 2);
		BlockState blockstate2 = Blocks.ORANGE_TERRACOTTA.defaultBlockState();
		BlockState blockstate3 = Blocks.BLUE_TERRACOTTA.defaultBlockState();
		this.placeBlock(p_273390_, blockstate3, i, j, k, p_273517_);
		this.placeBlock(p_273390_, blockstate2, i + 1, j, k - 1, p_273517_);
		this.placeBlock(p_273390_, blockstate2, i + 1, j, k + 1, p_273517_);
		this.placeBlock(p_273390_, blockstate2, i - 1, j, k - 1, p_273517_);
		this.placeBlock(p_273390_, blockstate2, i - 1, j, k + 1, p_273517_);
		this.placeBlock(p_273390_, blockstate2, i + 2, j, k, p_273517_);
		this.placeBlock(p_273390_, blockstate2, i - 2, j, k, p_273517_);
		this.placeBlock(p_273390_, blockstate2, i, j, k + 2, p_273517_);
		this.placeBlock(p_273390_, blockstate2, i, j, k - 2, p_273517_);
		this.placeBlock(p_273390_, blockstate2, i + 3, j, k, p_273517_);
		this.placeSand(i + 3, j + 1, k);
		this.placeSand(i + 3, j + 2, k);
		this.placeBlock(p_273390_, blockstate, i + 4, j + 1, k, p_273517_);
		this.placeBlock(p_273390_, blockstate1, i + 4, j + 2, k, p_273517_);
		this.placeBlock(p_273390_, blockstate2, i - 3, j, k, p_273517_);
		this.placeSand(i - 3, j + 1, k);
		this.placeSand(i - 3, j + 2, k);
		this.placeBlock(p_273390_, blockstate, i - 4, j + 1, k, p_273517_);
		this.placeBlock(p_273390_, blockstate1, i - 4, j + 2, k, p_273517_);
		this.placeBlock(p_273390_, blockstate2, i, j, k + 3, p_273517_);
		this.placeSand(i, j + 1, k + 3);
		this.placeSand(i, j + 2, k + 3);
		this.placeBlock(p_273390_, blockstate2, i, j, k - 3, p_273517_);
		this.placeSand(i, j + 1, k - 3);
		this.placeSand(i, j + 2, k - 3);
		this.placeBlock(p_273390_, blockstate, i, j + 1, k - 4, p_273517_);
		this.placeBlock(p_273390_, blockstate1, i, -2, k - 4, p_273517_);
	}

	private void placeSand(int p_279401_, int p_279451_, int p_279265_) {
		BlockPos blockpos = this.getWorldPos(p_279401_, p_279451_, p_279265_);
		this.potentialSuspiciousSandWorldPositions.add(blockpos);
	}

	private void placeSandBox(int p_279483_, int p_279321_, int p_279271_, int p_279471_, int p_279229_,
			int p_279111_) {
		for (int i = p_279321_; i <= p_279229_; ++i) {
			for (int j = p_279483_; j <= p_279471_; ++j) {
				for (int k = p_279271_; k <= p_279111_; ++k) {
					this.placeSand(j, i, k);
				}
			}
		}

	}

	private void placeCollapsedRoofPiece(WorldGenLevel p_272965_, int p_272618_, int p_273415_, int p_273110_,
			BoundingBox p_272645_) {
		if (p_272965_.getRandom().nextFloat() < 0.33F) {
			BlockState blockstate = Blocks.SANDSTONE.defaultBlockState();
			this.placeBlock(p_272965_, blockstate, p_272618_, p_273415_, p_273110_, p_272645_);
		} else {
			BlockState blockstate1 = Blocks.SAND.defaultBlockState();
			this.placeBlock(p_272965_, blockstate1, p_272618_, p_273415_, p_273110_, p_272645_);
		}

	}

	private void placeCollapsedRoof(WorldGenLevel p_273438_, BoundingBox p_273058_, int p_272638_, int p_272826_,
			int p_273026_, int p_272750_, int p_272639_) {
		for (int i = p_272638_; i <= p_272750_; ++i) {
			for (int j = p_273026_; j <= p_272639_; ++j) {
				this.placeCollapsedRoofPiece(p_273438_, i, p_272826_, j, p_273058_);
			}
		}

		RandomSource randomsource = RandomSource.create(p_273438_.getSeed()).forkPositional()
				.at(this.getWorldPos(p_272638_, p_272826_, p_273026_));
		int l = randomsource.nextIntBetweenInclusive(p_272638_, p_272750_);
		int k = randomsource.nextIntBetweenInclusive(p_273026_, p_272639_);
		this.randomCollapsedRoofPos = new BlockPos(this.getWorldX(l, k), this.getWorldY(p_272826_),
				this.getWorldZ(l, k));
	}

	@Override
	public List<BlockPos> getPotentialSuspiciousSandWorldPositions() {
		return this.potentialSuspiciousSandWorldPositions;
	}

	@Override
	public BlockPos getRandomCollapsedRoofPos() {
		return this.randomCollapsedRoofPos;
	}
}
