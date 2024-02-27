package com.min01.archaeology.init;

import com.min01.archaeology.Archaeology;
import com.min01.archaeology.block.BrushableBlock;
import com.min01.archaeology.block.DecoratedPotBlock;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.material.MaterialColor;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ArchaeologyBlocks {
	public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, Archaeology.MC_ID);
	
	public static final RegistryObject<Block> SUSPICIOUS_SAND = BLOCKS.register("suspicious_sand", () -> new BrushableBlock(Blocks.SAND, BlockBehaviour.Properties.of(Material.SAND).color(MaterialColor.SAND).strength(0.25F).sound(ArchaeologySounds.SUSPICIOUS_SAND), ArchaeologySounds.BRUSH_SAND.get(), ArchaeologySounds.BRUSH_SAND_COMPLETED.get()));
	public static final RegistryObject<Block> SUSPICIOUS_GRAVEL = BLOCKS.register("suspicious_gravel", () -> new BrushableBlock(Blocks.GRAVEL, BlockBehaviour.Properties.of(Material.SAND).color(MaterialColor.STONE).strength(0.25F).sound(ArchaeologySounds.SUSPICIOUS_GRAVEL), ArchaeologySounds.BRUSH_GRAVEL.get(), ArchaeologySounds.BRUSH_GRAVEL_COMPLETED.get()));
	public static final RegistryObject<DecoratedPotBlock> DECORATED_POT = BLOCKS.register("decorated_pot", () -> new DecoratedPotBlock(BlockBehaviour.Properties.of(Material.STONE).color(MaterialColor.TERRACOTTA_RED).strength(0.0F, 0.0F).noOcclusion()));
}
