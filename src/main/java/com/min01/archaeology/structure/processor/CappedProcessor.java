package com.min01.archaeology.structure.processor;

import com.min01.archaeology.init.ArchaeologyStructureProcessorType;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import it.unimi.dsi.fastutil.ints.IntArrayList;
import it.unimi.dsi.fastutil.ints.IntIterator;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.util.valueproviders.IntProvider;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessor;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorType;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.stream.IntStream;

public class CappedProcessor extends StructureProcessor {
    public static final Codec<CappedProcessor> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            StructureProcessorType.SINGLE_CODEC.fieldOf("delegate").forGetter(processor -> processor.delegate),
            IntProvider.POSITIVE_CODEC.fieldOf("limit").forGetter(processor -> processor.limit)
    ).apply(instance, CappedProcessor::new));

    private final StructureProcessor delegate;
    private final IntProvider limit;

    public CappedProcessor(final StructureProcessor processor, final IntProvider limit) {
        this.delegate = processor;
        this.limit = limit;
    }

    protected @NotNull StructureProcessorType<?> getType() {
        return ArchaeologyStructureProcessorType.CAPPED;
    }

    public final List<StructureTemplate.StructureBlockInfo> finalizeProcessing(final ServerLevelAccessor level, final BlockPos position, final BlockPos directionalPosition, final List<StructureTemplate.StructureBlockInfo> structureBlocks, final List<StructureTemplate.StructureBlockInfo> structureBlocks2, final StructurePlaceSettings settings) {
        if (limit.getMaxValue() != 0 && !structureBlocks2.isEmpty()) {
            if (structureBlocks.size() != structureBlocks2.size()) {
                Util.logAndPauseIfInIde("Original block info list not in sync with processed list, skipping processing. Original size: " + structureBlocks.size() + ", Processed size: " + structureBlocks2.size());
            } else {
                RandomSource random = RandomSource.create(level.getLevel().getSeed()).forkPositional().at(position);
                int i = Math.min(limit.sample(random), structureBlocks2.size());

                if (i >= 1) {
                    IntArrayList shuffled = Util.toShuffledList(IntStream.range(0, structureBlocks2.size()), random);
                    IntIterator shuffledIterator = shuffled.intIterator();
                    int j = 0;

                    while (shuffledIterator.hasNext() && j < i) {
                        int index = shuffledIterator.nextInt();
                        StructureTemplate.StructureBlockInfo structureBlockInfo = structureBlocks.get(index);
                        StructureTemplate.StructureBlockInfo structureBlockInfo2 = structureBlocks2.get(index);
                        StructureTemplate.StructureBlockInfo structureBlockInfo3 = delegate.processBlock(level, position, directionalPosition, structureBlockInfo, structureBlockInfo2, settings);

                        if (structureBlockInfo3 != null && !structureBlockInfo2.equals(structureBlockInfo3)) {
                            structureBlocks2.set(index, structureBlockInfo3);
                            j++;
                        }
                    }
                }
            }
        }

        return structureBlocks2;
    }
}