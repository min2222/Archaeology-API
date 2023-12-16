package com.min01.archaeology.misc;

import java.util.List;

import net.minecraft.core.BlockPos;

public interface IDesertPyramidPiece
{
	public List<BlockPos> getPotentialSuspiciousSandWorldPositions();

	public BlockPos getRandomCollapsedRoofPos();
}
