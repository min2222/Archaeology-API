package com.min01.archaeology.init;

import com.min01.archaeology.structure.processor.CappedProcessor;
import com.mojang.serialization.Codec;

import net.minecraft.core.Registry;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessor;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorType;

public class ArchaeologyStructureProcessorType
{
	public static final StructureProcessorType<CappedProcessor> CAPPED = register("capped", CappedProcessor.CODEC);
	
	private static <S extends StructureProcessor> StructureProcessorType<S> register(String p_226882_, Codec<S> p_226883_) {
		return Registry.register(Registry.STRUCTURE_PROCESSOR, p_226882_, () -> {
			return p_226883_;
		});
	}
}
