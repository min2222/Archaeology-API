package com.min01.archaeology.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.min01.archaeology.misc.IOceanRuinPiece;

import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.structure.TemplateStructurePiece;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceType;
import net.minecraft.world.level.levelgen.structure.structures.OceanRuinPieces.OceanRuinPiece;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplateManager;

@Mixin(TemplateStructurePiece.class)
public class MixinTemplateStructurePiece 
{
	@Inject(at = @At("TAIL"), method = "<init>(Lnet/minecraft/world/level/levelgen/structure/pieces/StructurePieceType;ILnet/minecraft/world/level/levelgen/structure/templatesystem/StructureTemplateManager;Lnet/minecraft/resources/ResourceLocation;Ljava/lang/String;Lnet/minecraft/world/level/levelgen/structure/templatesystem/StructurePlaceSettings;Lnet/minecraft/core/BlockPos;)V")
	private void init(StructurePieceType p_226886_, int p_226887_, StructureTemplateManager p_226888_, ResourceLocation p_226889_, String p_226890_, StructurePlaceSettings p_226891_, BlockPos p_226892_, CallbackInfo ci) 
	{
		if(TemplateStructurePiece.class.cast(this) instanceof OceanRuinPiece piece)
		{
			IOceanRuinPiece mixin = (IOceanRuinPiece) piece;
			((TemplateStructurePieceAccessor) TemplateStructurePiece.class.cast(this)).setPlaceSettings(mixin.getPlaceSetting());
		}
	}
}
