package com.min01.archaeology.init;

import com.min01.archaeology.Archaeology;

import net.minecraft.core.Registry;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class ArchaeologyStructureProcessor
{
	public static final DeferredRegister<StructureProcessorType<?>> STRUCTURE_PROCESSOR = DeferredRegister.create(Registry.STRUCTURE_PROCESSOR_REGISTRY, Archaeology.MC_ID);
	
	public static final RegistryObject<StructureProcessorType<?>> CAPPED_PROCESSOR = STRUCTURE_PROCESSOR.register("capped", () -> ArchaeologyStructureProcessorType.CAPPED);
}
