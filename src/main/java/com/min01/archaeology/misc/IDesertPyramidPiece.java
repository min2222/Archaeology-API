package com.min01.archaeology.misc;

import java.util.List;

import net.minecraft.core.BlockPos;

public interface IDesertPyramidPiece {
	List<BlockPos> archaeology$getPotentialSuspiciousSandWorldPositions();

	BlockPos archaeology$getRandomCollapsedRoofPos();
}
