package com.min01.archaeology.mixin;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;

import com.min01.archaeology.misc.StructureBlockInfoAccess;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;

@Mixin(StructureTemplate.StructureBlockInfo.class)
public class StructureBlockInfoMixin implements StructureBlockInfoAccess {
    @Final
    @Shadow
    @Mutable
    public CompoundTag nbt;

    @Override
    public void archaeology$addLootTable(final String tag, final String value) {
        if (nbt == null) {
            nbt = new CompoundTag();
        }

        nbt.putString(tag, value);
    }
}
