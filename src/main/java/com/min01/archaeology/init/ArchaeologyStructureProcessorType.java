package com.min01.archaeology.init;

import com.min01.archaeology.structure.processor.CappedProcessor;
import com.mojang.serialization.Codec;

import net.minecraft.core.Registry;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessor;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorType;

public class ArchaeologyStructureProcessorType {
	public static final StructureProcessorType<CappedProcessor> CAPPED = register("capped", CappedProcessor.CODEC);

	@SuppressWarnings("SameParameterValue")
	private static <S extends StructureProcessor> StructureProcessorType<S> register(final String name, final Codec<S> codec) {
		return Registry.register(Registry.STRUCTURE_PROCESSOR, name, () -> codec);
	}
}
