package com.min01.archaeology.mixin;

import java.util.List;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.min01.archaeology.init.ArchaeologyBlocks;
import com.min01.archaeology.init.ArchaeologyLootTables;
import com.min01.archaeology.structure.processor.CappedProcessor;

import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.util.valueproviders.ConstantInt;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.levelgen.structure.TemplateStructurePiece;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceType;
import net.minecraft.world.level.levelgen.structure.structures.OceanRuinPieces.OceanRuinPiece;
import net.minecraft.world.level.levelgen.structure.structures.OceanRuinStructure;
import net.minecraft.world.level.levelgen.structure.templatesystem.AlwaysTrueTest;
import net.minecraft.world.level.levelgen.structure.templatesystem.BlockIgnoreProcessor;
import net.minecraft.world.level.levelgen.structure.templatesystem.BlockMatchTest;
import net.minecraft.world.level.levelgen.structure.templatesystem.PosAlwaysTrueTest;
import net.minecraft.world.level.levelgen.structure.templatesystem.ProcessorRule;
import net.minecraft.world.level.levelgen.structure.templatesystem.RuleProcessor;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessor;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplateManager;

@Mixin(TemplateStructurePiece.class)
public class MixinTemplateStructurePiece
{
	@Shadow
	protected StructurePlaceSettings placeSettings;
	
	private static final StructureProcessor WARM_SUSPICIOUS_BLOCK_PROCESSOR = archyRuleProcessor(Blocks.SAND, ArchaeologyBlocks.SUSPICIOUS_SAND.get(), ArchaeologyLootTables.OCEAN_RUIN_WARM_ARCHAEOLOGY);
	private static final StructureProcessor COLD_SUSPICIOUS_BLOCK_PROCESSOR = archyRuleProcessor(Blocks.GRAVEL, ArchaeologyBlocks.SUSPICIOUS_GRAVEL.get(), ArchaeologyLootTables.OCEAN_RUIN_COLD_ARCHAEOLOGY);
	
	private static StructureProcessor archyRuleProcessor(Block p_277376_, Block p_277934_, ResourceLocation p_277968_) {
		return new CappedProcessor(new RuleProcessor(List.of(new ProcessorRule(new BlockMatchTest(p_277376_), AlwaysTrueTest.INSTANCE, PosAlwaysTrueTest.INSTANCE, p_277934_.defaultBlockState()))), ConstantInt.of(5));
	}
	
	@Inject(at = @At("TAIL"), method = "<init>(Lnet/minecraft/world/level/levelgen/structure/pieces/StructurePieceType;ILnet/minecraft/world/level/levelgen/structure/templatesystem/StructureTemplateManager;Lnet/minecraft/resources/ResourceLocation;Ljava/lang/String;Lnet/minecraft/world/level/levelgen/structure/templatesystem/StructurePlaceSettings;Lnet/minecraft/core/BlockPos;)V")
	private void init(StructurePieceType p_226886_, int p_226887_, StructureTemplateManager p_226888_, ResourceLocation p_226889_, String p_226890_, StructurePlaceSettings p_226891_, BlockPos p_226892_, CallbackInfo ci)
	{
		if(TemplateStructurePiece.class.cast(this) instanceof OceanRuinPiece piece)
		{
			OceanRuinPieceAccessor accessor = (OceanRuinPieceAccessor) piece;
			RandomSource random = RandomSource.create();
			((TemplateStructurePieceAccessor) TemplateStructurePiece.class.cast(this)).setPlaceSettings(makeSettings(Rotation.getRandom(random), accessor.getBiomeType()));
		}
	}
	
	private static StructurePlaceSettings makeSettings(Rotation p_229037_, OceanRuinStructure.Type type)
	{      
		StructureProcessor structureprocessor = type == OceanRuinStructure.Type.COLD ? COLD_SUSPICIOUS_BLOCK_PROCESSOR : WARM_SUSPICIOUS_BLOCK_PROCESSOR;
		return (new StructurePlaceSettings()).setRotation(p_229037_).setMirror(Mirror.NONE).addProcessor(BlockIgnoreProcessor.STRUCTURE_AND_AIR).addProcessor(structureprocessor);
	}
}
