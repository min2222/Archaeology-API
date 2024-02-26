package com.min01.archaeology.mixin;

import net.minecraft.world.level.levelgen.structure.TemplateStructurePiece;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(TemplateStructurePiece.class)
public interface TemplateStructurePieceAccessor {
	@Accessor("placeSettings")
    void setPlaceSettings(StructurePlaceSettings setting);
}
