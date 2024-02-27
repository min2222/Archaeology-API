package com.min01.archaeology.structure.processor;

import com.min01.archaeology.block.BrushableBlock;
import com.min01.archaeology.blockentity.BrushableBlockEntity;
import com.min01.archaeology.init.ArchaeologyStructureProcessorType;
import com.min01.archaeology.misc.StructureBlockInfoAccess;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.valueproviders.IntProvider;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessor;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorType;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class CappedProcessor extends StructureProcessor {
    public static final Codec<CappedProcessor> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            StructureProcessorType.SINGLE_CODEC.fieldOf("delegate").forGetter(processor -> processor.delegate),
            IntProvider.POSITIVE_CODEC.fieldOf("limit").forGetter(processor -> processor.limit),
            ResourceLocation.CODEC.fieldOf("loot_table").forGetter(processor -> processor.lootTable)
    ).apply(instance, CappedProcessor::new));

    private final StructureProcessor delegate;
    private final IntProvider limit;
    private final @Nullable ResourceLocation lootTable;

    private StructurePlaceSettings previousSettings;
    private int generated;

    public CappedProcessor(final StructureProcessor delegate, final IntProvider limit, final @Nullable ResourceLocation lootTable) {
        this.delegate = delegate;
        this.limit = limit;
        this.lootTable = lootTable;
    }

    protected @NotNull StructureProcessorType<?> getType() {
        return ArchaeologyStructureProcessorType.CAPPED;
    }

    @Override
    public @Nullable StructureTemplate.StructureBlockInfo processBlock(@NotNull final LevelReader level, @NotNull final BlockPos position, @NotNull final BlockPos relativePosition, @NotNull final StructureTemplate.StructureBlockInfo structureBlockInfo, @NotNull final StructureTemplate.StructureBlockInfo relativeStructureBlockInfo, @NotNull final StructurePlaceSettings settings) {
        if (generated == limit.getMaxValue() || /* Hard code this because I can't be bothered anymore */ relativeStructureBlockInfo.state.getBlock() instanceof BrushableBlock || /* Avoid converting the same blocks every time */ settings.getRandom(position).nextDouble() < 0.3) {
            return relativeStructureBlockInfo;
        }

        if (/* This processor is (sometimes?) re-used */ previousSettings != settings) {
            previousSettings = settings;
            generated = 0;
        }

        if (lootTable != null && relativeStructureBlockInfo instanceof StructureBlockInfoAccess access) {
            access.archaeology$addLootTable(BrushableBlockEntity.LOOT_TABLE_TAG, lootTable.toString());
        }

        generated++;

        return delegate.processBlock(level, position, relativePosition, structureBlockInfo, relativeStructureBlockInfo, settings);
    }
}