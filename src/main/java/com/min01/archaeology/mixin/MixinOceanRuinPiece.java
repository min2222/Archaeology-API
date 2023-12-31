package com.min01.archaeology.mixin;

import java.util.List;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

import com.min01.archaeology.init.ArchaeologyBlocks;
import com.min01.archaeology.init.ArchaeologyLootTables;
import com.min01.archaeology.misc.IOceanRuinPiece;
import com.min01.archaeology.structure.processor.CappedProcessor;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.util.valueproviders.ConstantInt;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
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

@Mixin(OceanRuinPiece.class)
public class MixinOceanRuinPiece implements IOceanRuinPiece
{
	@Shadow
	private @Final OceanRuinStructure.Type biomeType;
	
	@Unique
	private RandomSource random = RandomSource.create();
	
	private static final StructureProcessor WARM_SUSPICIOUS_BLOCK_PROCESSOR = archyRuleProcessor(Blocks.SAND, ArchaeologyBlocks.SUSPICIOUS_SAND.get(), ArchaeologyLootTables.OCEAN_RUIN_WARM_ARCHAEOLOGY);
	private static final StructureProcessor COLD_SUSPICIOUS_BLOCK_PROCESSOR = archyRuleProcessor(Blocks.GRAVEL, ArchaeologyBlocks.SUSPICIOUS_GRAVEL.get(), ArchaeologyLootTables.OCEAN_RUIN_COLD_ARCHAEOLOGY);
	
	private static StructureProcessor archyRuleProcessor(Block p_277376_, Block p_277934_, ResourceLocation p_277968_) 
	{
		return new CappedProcessor(new RuleProcessor(List.of(new ProcessorRule(new BlockMatchTest(p_277376_), AlwaysTrueTest.INSTANCE, PosAlwaysTrueTest.INSTANCE, p_277934_.defaultBlockState()))), ConstantInt.of(5));
	}
	
	@Override
	public StructurePlaceSettings getPlaceSetting() 
	{
		StructureProcessor structureprocessor = this.biomeType == OceanRuinStructure.Type.COLD ? COLD_SUSPICIOUS_BLOCK_PROCESSOR : WARM_SUSPICIOUS_BLOCK_PROCESSOR;
		return (new StructurePlaceSettings()).setRotation(Rotation.getRandom(this.random)).setMirror(Mirror.NONE).addProcessor(BlockIgnoreProcessor.STRUCTURE_AND_AIR).addProcessor(structureprocessor);
	}
}
