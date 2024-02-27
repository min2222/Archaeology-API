package com.min01.archaeology.init;

import com.min01.archaeology.Archaeology;
import com.min01.archaeology.blockentity.BrushableBlockEntity;
import com.min01.archaeology.blockentity.DecoratedPotBlockEntity;

import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ArchaeologyBlockEntityType {
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES = DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, Archaeology.MC_ID);
    
	public static final RegistryObject<BlockEntityType<BrushableBlockEntity>> BRUSHABLE_BLOCK = BLOCK_ENTITIES.register("brushable_block", () -> BlockEntityType.Builder.of(BrushableBlockEntity::new, ArchaeologyBlocks.SUSPICIOUS_SAND.get(), ArchaeologyBlocks.SUSPICIOUS_GRAVEL.get()).build(null));
	public static final RegistryObject<BlockEntityType<DecoratedPotBlockEntity>> DECORATED_POT = BLOCK_ENTITIES.register("decorated_pot", () -> BlockEntityType.Builder.of(DecoratedPotBlockEntity::new, ArchaeologyBlocks.DECORATED_POT.get()).build(null));
}
