package com.min01.archaeology.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import net.minecraft.world.level.levelgen.structure.structures.OceanRuinPieces.OceanRuinPiece;
import net.minecraft.world.level.levelgen.structure.structures.OceanRuinStructure;

@Mixin(OceanRuinPiece.class)
public interface OceanRuinPieceAccessor
{
	@Accessor("biomeType")
	public OceanRuinStructure.Type getBiomeType();
}
