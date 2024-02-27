package com.min01.archaeology.mixin;

import com.min01.archaeology.blockentity.BrushableBlockEntity;
import com.min01.archaeology.init.ArchaeologyBlocks;
import com.min01.archaeology.init.ArchaeologyLootTables;
import com.min01.archaeology.structure.processor.CappedProcessor;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.util.valueproviders.ConstantInt;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.StructureManager;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.TemplateStructurePiece;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceType;
import net.minecraft.world.level.levelgen.structure.structures.OceanRuinPieces.OceanRuinPiece;
import net.minecraft.world.level.levelgen.structure.structures.OceanRuinStructure;
import net.minecraft.world.level.levelgen.structure.templatesystem.AlwaysTrueTest;
import net.minecraft.world.level.levelgen.structure.templatesystem.BlockMatchTest;
import net.minecraft.world.level.levelgen.structure.templatesystem.PosAlwaysTrueTest;
import net.minecraft.world.level.levelgen.structure.templatesystem.ProcessorRule;
import net.minecraft.world.level.levelgen.structure.templatesystem.RuleProcessor;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessor;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplateManager;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;
import java.util.Optional;

/** Set suspicious block processors for the vanilla ocean ruin structures */
@Mixin(OceanRuinPiece.class)
public abstract class MixinOceanRuinPiece extends TemplateStructurePiece {
    public MixinOceanRuinPiece(final StructurePieceType type, int genDepth, final StructureTemplateManager manager, final ResourceLocation location, final String name, final StructurePlaceSettings settings, final BlockPos position) {
        super(type, genDepth, manager, location, name, settings, position);
    }

    @Unique
    private static StructureProcessor archaeology$archyRuleProcessor(final Block inputBlock, final Block outputBlock, final ResourceLocation lootTable) {
        CompoundTag lootTableTag = new CompoundTag();
        lootTableTag.putString(BrushableBlockEntity.LOOT_TABLE_TAG, lootTable.toString());
        return new CappedProcessor(new RuleProcessor(List.of(new ProcessorRule(new BlockMatchTest(inputBlock), AlwaysTrueTest.INSTANCE, PosAlwaysTrueTest.INSTANCE, outputBlock.defaultBlockState(), Optional.of(lootTableTag)))), ConstantInt.of(5), lootTable);
    }

    @Inject(method = "postProcess", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/levelgen/structure/templatesystem/StructurePlaceSettings;clearProcessors()Lnet/minecraft/world/level/levelgen/structure/templatesystem/StructurePlaceSettings;", shift = At.Shift.AFTER))
    private void addProccessors(final WorldGenLevel level, final StructureManager manager, final ChunkGenerator generator, final RandomSource random, final BoundingBox boundingBox, final ChunkPos chunkPosition, final BlockPos position, final CallbackInfo callback) {
        StructureProcessor suspiciousProcessor = biomeType == OceanRuinStructure.Type.COLD ? archaeology$archyRuleProcessor(Blocks.GRAVEL, ArchaeologyBlocks.SUSPICIOUS_GRAVEL.get(), ArchaeologyLootTables.OCEAN_RUIN_COLD_ARCHAEOLOGY) : archaeology$archyRuleProcessor(Blocks.SAND, ArchaeologyBlocks.SUSPICIOUS_SAND.get(), ArchaeologyLootTables.OCEAN_RUIN_WARM_ARCHAEOLOGY);
        placeSettings.addProcessor(suspiciousProcessor);
    }

    @Shadow @Final private OceanRuinStructure.Type biomeType;
}
