package com.min01.archaeology.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import net.minecraft.world.level.levelgen.structure.TemplateStructurePiece;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;

@Mixin(TemplateStructurePiece.class)
public interface TemplateStructurePieceAccessor 
{
	@Accessor("placeSettings")
	public void setPlaceSettings(StructurePlaceSettings settings);
}
