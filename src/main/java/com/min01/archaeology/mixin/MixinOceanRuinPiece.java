package com.min01.archaeology.mixin;

import java.util.List;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.min01.archaeology.init.ArchaeologyBlocks;
import com.min01.archaeology.init.ArchaeologyLootTables;
import com.min01.archaeology.structure.processor.CappedProcessor;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
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

/** Set suspicious block processors for the vanilla ocean ruin structures */
@Mixin(OceanRuinPiece.class)
public abstract class MixinOceanRuinPiece extends TemplateStructurePiece {
    public MixinOceanRuinPiece(final StructurePieceType type, int genDepth, final StructureTemplateManager manager, final ResourceLocation location, final String name, final StructurePlaceSettings settings, final BlockPos position) {
        super(type, genDepth, manager, location, name, settings, position);
    }

    @Unique
    private static StructureProcessor archaeology$archyRuleProcessor(final Block inputBlock, final Block outputBlock, final ResourceLocation lootTable) {
        return new CappedProcessor(new RuleProcessor(List.of(new ProcessorRule(new BlockMatchTest(inputBlock), AlwaysTrueTest.INSTANCE, PosAlwaysTrueTest.INSTANCE, outputBlock.defaultBlockState()))), ConstantInt.of(5), lootTable);
    }

    @Inject(method = "<init>(Lnet/minecraft/world/level/levelgen/structure/templatesystem/StructureTemplateManager;Lnet/minecraft/resources/ResourceLocation;Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/block/Rotation;FLnet/minecraft/world/level/levelgen/structure/structures/OceanRuinStructure$Type;Z)V", at = @At("RETURN"))
    private void archaeology$overridePlacementSettings(final StructureTemplateManager manager, final ResourceLocation location, final BlockPos position, final Rotation rotation, float integrity, final OceanRuinStructure.Type biomeType, boolean isLarge, final CallbackInfo callback) {
        archaeology$setProcessor(biomeType, rotation);
    }

    @Inject(method = "<init>(Lnet/minecraft/world/level/levelgen/structure/templatesystem/StructureTemplateManager;Lnet/minecraft/nbt/CompoundTag;)V", at = @At("RETURN"))
    private void archaeology$overridePlacementSettings(final StructureTemplateManager manager, final CompoundTag tag, final CallbackInfo callback) {
        OceanRuinStructure.Type biomeType = OceanRuinStructure.Type.valueOf(tag.getString("BiomeType"));
        Rotation rotation = Rotation.valueOf(tag.getString("Rot"));
        archaeology$setProcessor(biomeType, rotation);
    }

    @Unique
    private void archaeology$setProcessor(final OceanRuinStructure.Type biomeType, final Rotation rotation) {
        StructureProcessor suspiciousProcessor = biomeType == OceanRuinStructure.Type.COLD ? archaeology$archyRuleProcessor(Blocks.GRAVEL, ArchaeologyBlocks.SUSPICIOUS_GRAVEL.get(), ArchaeologyLootTables.OCEAN_RUIN_COLD_ARCHAEOLOGY) : archaeology$archyRuleProcessor(Blocks.SAND, ArchaeologyBlocks.SUSPICIOUS_SAND.get(), ArchaeologyLootTables.OCEAN_RUIN_WARM_ARCHAEOLOGY);
        placeSettings = new StructurePlaceSettings().setRotation(Rotation.getRandom(RandomSource.create())).setRotation(rotation).setMirror(Mirror.NONE).addProcessor(BlockIgnoreProcessor.STRUCTURE_AND_AIR).addProcessor(suspiciousProcessor);
    }
}
